/**
 * Created by Carpenter on 2017/3/10.
 */
$(document).ready(function () {
    $.ajax({
        url: "cart/addCart",
        type: 'GET',
        dataType: 'JSON'
    });
    $.ajax({
        url: "cart/getMyCart",
        type: 'GET',
        dataType: 'JSON',
        success: function (json) {
            if(typeof (json[0]) != "undefined"){
                $("#cartItemContainer").after(createCarts(json));
            }else{
                $("#cartItemContainer").after("<p>还没有添加任何商品到购物车！</p>");
            }




        },
        error: function () {
            alert("failed");
        }
    });
});

$(document).ready(function () {
    //加的效果

    $(document).on('click', '.add', function () {
        var n = $(this).prev().val();
        var num = parseInt(n) + 1;
        if (num == 0) {
            return;
        }
        $(this).prev().attr('value', num);
        var price = parseFloat($(this).parent().parent().parent().parent().children("p.price").attr("value"));
        $(this).parent().parent().next().children("p.totalPrice").text(price * num);
        var tmp = parseFloat($("#payPrice").text());
        var flag = $(this).parent().parent().next().children("p.totalPrice").attr("value");
        if(flag == 0) return;
        $("#submit").removeAttr("disabled");
        $("#payPrice").text(tmp + price);
    });
    $(document).on('click', '.jian', function () {
        var n = $(this).next().val();
        var num = parseInt(n) - 1;
        if (num == 0) {
            return
        }
        $(this).next().attr('value', num);
        var price = parseFloat($(this).parent().parent().parent().parent().children("p.price").attr("value"));
        $(this).parent().parent().next().children("p.totalPrice").text(price * num);
        var flag = $(this).parent().parent().next().children("p.totalPrice").attr("value");
        var tmp = parseFloat($("#payPrice").text());
        var final = tmp - price;
        if(final < 0 || flag == 0){
            $("#submit").attr("disabled", true);
            return;
        }
        if(final == 0) $("#submit").attr("disabled", true);
        $("#payPrice").text(final);
    });
    $(document).on('click', '#checkAll', function (e) {
        if($('#checkAll').attr('checked') == 'checked'){
            $('input:checkbox[type=checkbox]').each(function (i) {
                //noinspection JSAnnotator
                if(!$(this).is("checked")){
                    $(this).attr("checked", true);
                    $(this).parent().parent().next().children(".cart-header").find("p.totalPrice").attr('value', 1);
                }
            });
            var tmp = 0;
            $('p.totalPrice').each(function (i) {
                tmp += parseFloat($(this).text());
            })
            $("#submit").removeAttr("disabled");
            $("#payPrice").text(tmp);
        }else{
            $('input:checkbox[type=checkbox]').each(function (i) {
                //noinspection JSAnnotator
                if(!$(this).is("checked")){
                    $(this).attr("checked", false);
                    $(this).parent().parent().next().children(".cart-header").find("p.totalPrice").attr('value', 0);
                }
            });
            $("#submit").attr('disabled', true);
            $("#payPrice").text(0.0);
        }
    });
});

function onClick(e) {
    $(document).ready(function () {
        var id = $("#" + e.id).parent().attr("id");
        var pid = $("#" + e.id).find("div").attr("value");
        $.get("cart/deleteCart?product_id=" + pid, function (data, status) {
            if (status == "success") {
                var json = JSON.parse(data);
                if (json.state == 1) {
                    alert("success");
                    if($(e).next().find("p.totalPrice").attr('value') == 1){
                        $('#payPrice').text(parseFloat($('#payPrice').text()) - parseFloat($(e).next().find("p.totalPrice").text()));
                    }
                    $('#' + id).parent().parent().fadeOut('slow', function (c) {
                        $('#' + id).remove();
                    });
                }
            }
        })

    });
}

function checkClick(e) {
    var price = parseFloat($(e).parent().parent().next().children(".cart-header").find("p.totalPrice").text());
    var tmp = parseFloat($("#payPrice").text());
    if(typeof($(e).attr("checked")) == "undefined"){
        $("#checkAll").attr("checked", false);
        $(e).parent().parent().next().children(".cart-header").find("p.totalPrice").attr('value', 0);
        if(tmp - price < 0){
            return;
        }else{
            if(tmp - price <= 0) $("#submit").attr("disabled", true);
            $("#payPrice").text(tmp - price);
        }
    }else if($(e).attr("checked") == "checked"){
        var flag = true;
        $('input:checkbox[name=pCheckbox]').each(function (i) {
            if($(this).attr("checked") != "checked") {
                flag = false;
            }
        });

        if(flag){
            $("#checkAll").attr("checked", true);
        }
        $(e).parent().parent().next().children(".cart-header").find("p.totalPrice").attr('value', 1);
        $("#submit").removeAttr("disabled");
        $("#payPrice").text(tmp + price);
    }
}

function submitOrder(c){
    $('p.totalPrice').each(function (i) {
        if($(this).attr("value") != 0){
            var pid = parseInt($(this).attr("pid"));
            var amount = parseInt($('#num' + pid).attr("value"));
            var totalPrice = parseFloat($(this).text());
            $.get("buy/submitOrder?pid=" + pid + "&amount=" + amount + "&price=" + totalPrice, {}, function (data, status) {
                if(status == "success"){
                    var json = JSON.parse(data);
                    if(json.state == 1){
                        toastr.success("购买成功!","提示");
                    }else{
                        toastr.error("购买失败!","提示");
                    }
                }
            });
        }
    })
}

function createCarts(json) {
    var divElement = document.createElement("div");
    var cartHeader;
    var cartClose;
    var str = "<table>";
    var cnt = 0;
    for (var index in json) {
        var data = json[index];
        if(typeof(data) != "undefined") cnt++;
        cartHeader = "cart-header" + data.product_id;
        cartClose = "close" + data.product_id;
        str += "<tr><td><div class='checkBox'><input type='checkbox' name='pCheckbox' onclick='checkClick(this)'/></div></td>" +
            "<td><div class='cart-header' id='" + cartHeader +
            "'><div class='close' onclick='onClick(this)' id='" + cartClose + "'> " +
            "<div style='display: none' value='" + data.product_id + "'></div>" +
            "</div><div class='cart-sec simpleCart_shelfItem'><div class='cart-item cyc'>" +
            "<img src='" + data.thumbnail + "' class='img-responsive' alt=''></img></div>" +
            "<div class='cart-item-info'><h3><a href='show?pid="+ data.product_id +"'>" + data.title + "<div class='clearfix'></div>" +
            "</a></h3>" + "<div class='delivery'><p class='price' value='" + data.price + "'>Price: ￥" + data.price +
            "</p><span>Delivered in 24-72 hours</span><br></br><div class='clearfix'></div> " +
            "<ul class='qty'><li><p>Amount: &nbsp;&nbsp;</p>" +
            "<div class='gw_num'><em class='jian'>-</em><input type='text' value='" +
            data.amount + "' class='num' id='num" +data.product_id + "'></input><em class='add'>+</em></div></li>" +
            "<li><p>Total Price: &nbsp;&nbsp;</p><p class='totalPrice' value='0' pid='"+ data.product_id +"'>" + data.price * data.amount + "</p></li>" +
            "<li><p>FREE delivery</p></li></ul>" +
            "</div> </div> <div class='clearfix'></div> </div></div></td></tr>";
    }
    if(cnt == 0) str = "<table>";
    str += "</table><hr/><div id='allPrice' style='height: 34px;'>" +
        "<input type='checkbox' id='checkAll' style='margin:12px;' /><label style='font-size: 1.2em;'>Check All</label>" +
        "<div style='float: right; margin: 0px; font-size: 1.2em'>you should pay:&nbsp;" +
        "<label id='payPrice' style='margin-right: 20px; color: red;'>0.0</label>" +
        "<input type='button' id='submit' value='submit' class='btn btn-success'  onclick='submitOrder(this)' disabled='disabled'/></div></div><br />";
    divElement.innerHTML = str;
    return divElement;
}