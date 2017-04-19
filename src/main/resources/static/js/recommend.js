$(document).ready(function() {
    $.get("recommend/getItems", {}, function(data, status) {
        if(data!="[]"){
            var json=JSON.parse(data);
            var html="";
            for(var i=0;i<json.length;i++){
                html+=('<div class="row"><div class="cart-header"><div class="cart-sec simpleCart_shelfItem"><div class="cart-item cyc"><img src="'+json[i].thumbnail
                +'" class="img-responsive"></div><div class="cart-item-info"><h3><a href="show?pid='+json[i].pid
                +'">'+json[i].title
                +'</a><span>RMB: '+json[i].price
                +'</span></h3><ul class="qty"><li><p>FREE delivery</p></li></ul><div class="delivery"></div></div></div></div></div>');
            }
            $("#cfItems").append(html);
            $("#cfGallery").removeAttr("style");
        }
    });
});