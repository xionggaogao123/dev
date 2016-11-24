<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<link href="/css/homepage.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
//    getPrivateLetterCount();
//    //setInterval("getPrivateLetterCount()", 60000);//定时刷新未读私信数
//    function getPrivateLetterCount() {
//        $.ajax({
//            url: "/user/getPrivateLetterCount.action",
//            type: "post",
//            data: {
//                'inJson': true
//            },
//            success: function (data) {
//                //$("#sl").html(data.messageCount);
//                if (data.messageCount > 0) {
//                    $('#message').css('background', 'url(/img/newmessage.png) no-repeat 5px 0px');
//                    $("#sl").html(data.messageCount);
//                    jQuery("#xyd").html(data.messageCount);
//                } else {
//                    $('#message').css('background', 'url(/img/nomessage.png) no-repeat 10px 10px');
//                    jQuery("#xyd").hide();
//                }
//            }
//        });
//    }

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
    }
</script>


<div class="homepage-top" style="font-size: 15px;background-color: #fda616;position: relative;color: white;line-height: 30px;overflow: visible">
    <div class="homepage-top-main">
        <div class="homepage-top-left" style="height: 50px; margin-top: -10px;">
            <%--<span>复兰科技</span>--%>
            <%--<span style="font-family:'Arial', 'Microsoft YaHei', 'Helvetica', 'sans-serif', 'simsun', 'Lucida Grande', 'Lucida Sans Unicode';--%>
            <%--font-size:smaller;font-weight: 700; color: #EDE7E7">400-820-6735</span>--%>

            <%
                int _xxid = 211;

                if (session.getAttribute("xxid") != null) {
                    _xxid = Integer
                            .parseInt((String) (session.getAttribute("xxid")));
                }
                if (_xxid>0) {
            %><img alt="" style="max-height: 50px;margin-right:5px;vertical-align: initial;" src="/img/logofz<%=_xxid%>.png">
            <%
                }
            %>

        </div>
        <div class="homepage-top-right" style="position:relative;left:-80px;">

            <!--
             <div class="homepage-top-right-IN">
               <input placeholder="查找课程">
               <i class="fa fa-search"></i>
             </div>
              -->




            <span>欢迎您，${currentUser.userName} <span id="fz_out" style="cursor: pointer;font-size:small; " onclick="loginout();"> [退出]</span></span>
        </div>
    </div>
</div>