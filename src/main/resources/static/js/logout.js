/**
 * Created by Carpenter on 2017/3/14.
 */
$(document).ready(function () {
    $.post("loginSession", {}, function (data, status) {
        if(status == "success") {
            var returnJson = JSON.parse(data);
            if(returnJson.state == 1) {
                $("#logout").css("display", "block");
            }else{
                $("#logout").css("display", "none");
            }
        }
    });
})
