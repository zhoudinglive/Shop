$(document).ready(function() {
    $.get("buy/getHistoryOrder", {}, function(data, status) {
        var json=JSON.parse(data);
        var html="";
        for(var i=0;i<json.length;i++){
            html+=('<div class="row"><div class="cart-header"><div class="cart-sec simpleCart_shelfItem"><div class="cart-item cyc"><img src="'+json[i].thumbnail
            +'" class="img-responsive"></div><div class="cart-item-info"><h3><a href="show?pid='+json[i].product_id
            +'">'+json[i].title
            +'</a><span>RMB: '+json[i].price
            +'</span></h3><ul class="qty"><li><p>FREE delivery</p></li></ul><div class="delivery"><p>数量: '+json[i].amount
            +'</p><span>订购时间: '+json[i].time
            +'</span></div></div></div></div></div>');
        }
        $("#orderItems").append(html);
    });
});