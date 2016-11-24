/**
 * Created by Niu Xin on 14/10/22.
 */
function loginout(t) {
    $.ajax({
    	 url: "/user/logout.do",
        type: "post",
        dataType: "json",
        data: {
            'inJson': true
        },
        success: function (data) {
        	 window.location.href = "/";
        }
    });
    
    ssoLoginout();
}


function ssoLoginout () {
    var logoutURL = "http://221.214.55.21:6603/dsssoserver/logout";
    
    $.ajax({
        url: logoutURL,
        type: "GET",
        dataType: 'jsonp',
        jsonp: "callback",
        crossDomain: true,
        cache: false,
        success: function (html) {
         
        },
        error: function (data) {
        	 
        }
    });
  }


function getPrivateLetterCount() {
    $.ajax({
        url: "/letter/count.do",
        type: "post",
        data: {
            'inJson': true
        },
        success: function (data) {
            if (data.messageCount > 0) {
              // jQuery("#yun").show();
            	 jQuery("#xyd").text(data.messageCount);
               jQuery("#letterCount").text(data.messageCount);
            } else {
            	//jQuery("#yun").hide();
            	 $("#xyd").hide();
            }
        }
    });
}
$(document).ready(function(){
	var school=jQuery("#schoolId").val();
	if(school)
	{
		//jQuery("#logo_hi").css("backgroundImage","url(../../img/logofz214.png)");
		var image="url(../../img/logofz"+school+".png)";
		jQuery("#logo_hi").css("backgroundImage",image);
	}
	//getPrivateLetterCount();
});
