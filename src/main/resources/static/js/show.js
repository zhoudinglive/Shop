function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r!=null) return unescape(r[2]);
    return null;
}
$(document).ready(function() {
    var pid=GetQueryString("pid");
    $.get("user/detail?pid="+pid, {}, function(data, status) {
        var json=JSON.parse(data);
        $("#thum").attr("src",json.thumbnail);
        $("#title").html(json.title);
        $("#price").html("RMB: "+json.price);
        $("#detail").html(json.detail);
        $("#catalog").html("商品分类: "+json.catalog);
        var tags=new Array();
        tags=json.tag.split(";");
        var html="&nbsp;";
        for(var i=0;i<tags.length;i++){
            html+='<span class="label label-info">'+tags[i]+'</span>&nbsp;&nbsp;';
        }
        $("#tag").append(html);
        $("#addToCart").attr("action","cart/addCart");
        $("#product_id").val(json.id);
    });
    $(".add").click(function(){
        var n=$(this).prev().val();
        var num=parseInt(n)+1;
        if(num==0){return;}
        $(this).prev().val(num);
    });
    $(".sub").click(function(){
        var n=$(this).next().val();
        var num=parseInt(n)-1;
        if(num==0){return;}
        $(this).next().val(num);
    });
    $("#addToCart").ajaxForm(function(data) {
        try {
            var json=JSON.parse(data);
            if(json.state==1){
                toastr.success("添加成功!","提示");
            }else{
                toastr.error("添加失败!","提示");
            }
        }catch(e){
            toastr.error("添加失败!","提示");
        }
    });
});