 function GetQueryString(name) {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r!=null) return unescape(r[2]);
        return null;
    }
$(document).ready(function() {
    var keyword = escape(GetQueryString("keyword"));
    //alert(keyword);
    var page = GetQueryString("page");
    var sort = GetQueryString("sort");
    var order = GetQueryString("order");
    var resultCnt;
    var pageCnt;
    $.get("user/searchCount?keyword="+keyword, {}, function(data, status) {
        resultCnt = parseInt(data);
        pageCnt = parseInt(resultCnt/12)+1;
        $("#curPage").html(GetQueryString("page")+" / "+pageCnt);
        $("#total").html(resultCnt+" 件商品");
    });
    $.get("user/search?keyword="+keyword+"&page="+page+"&sort="+sort+"&order="+order, {}, function(data, status) {
        var json = JSON.parse(data);
        for (var i=0;i<json.length;i++){
            $("#thum"+i).attr("src",json[i].thumbnail);
            $("#title"+i).html(json[i].title);
            $("#price"+i).html("RMB: "+json[i].price);
            $("#sale"+i).html("销量:"+json[i].sale);
            $("#score"+i).html("评分:"+json[i].score);
            $("#link"+i).attr("href","show?pid="+json[i].id);
        }
        for (var i=json.length; i<12; i++) {
            $("#product"+i).hide();
        }
    });
    if(sort=="sale"){
        if(order=="desc"){
            $("#saleDesc").children().attr("class","glyphicon glyphicon-ok");
        }else if(order=="asc"){
            $("#saleAsc").children().attr("class","glyphicon glyphicon-ok");
        }
    }else if(sort=="score"){
        if(order=="desc"){
            $("#scoreDesc").children().attr("class","glyphicon glyphicon-ok");
        }else if(order=="asc"){
            $("#scoreAsc").children().attr("class","glyphicon glyphicon-ok");
        }
    }else if(sort=="price"){
        if(order=="desc"){
            $("#priceDesc").children().attr("class","glyphicon glyphicon-ok");
        }else if(order=="asc"){
            $("#priceAsc").children().attr("class","glyphicon glyphicon-ok");
        }
    }else{
        $("#default").children().attr("class","glyphicon glyphicon-ok");
    }
    $("#default").attr("href","products.html?keyword="+keyword+"&page=1");
    $("#saleDesc").attr("href","products.html?keyword="+keyword+"&page=1&sort=sale&order=desc");
    $("#saleAsc").attr("href","products.html?keyword="+keyword+"&page=1&sort=sale&order=asc");
    $("#scoreDesc").attr("href","products.html?keyword="+keyword+"&page=1&sort=score&order=desc");
    $("#scoreAsc").attr("href","products.html?keyword="+keyword+"&page=1&sort=score&order=asc");
    $("#priceDesc").attr("href","products.html?keyword="+keyword+"&page=1&sort=price&order=desc");
    $("#priceAsc").attr("href","products.html?keyword="+keyword+"&page=1&sort=price&order=asc");
    $("#prePage").click(function() {
        if (page!=1) {
            location.href="products.html?keyword="+keyword+"&page="+(parseInt(page)-1)+"&sort="+sort+"&order="+order;
        }
    });
    $("#nextPage").click(function() {
        if (page!=pageCnt) {
            location.href="products.html?keyword="+keyword+"&page="+(parseInt(page)+1)+"&sort="+sort+"&order="+order;
        }
    });
    $("#doJump").click(function() {
        if (parseInt($("#jumpTo").val())>=1&&parseInt($("#jumpTo").val())<=pageCnt) {
            location.href="products.html?keyword="+keyword+"&page="+$("#jumpTo").val()+"&sort="+sort+"&order="+order;
        }
    });

});
