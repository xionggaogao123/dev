<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ page import="com.jintizi.entity.UserInfo" %>
<%@ page import="com.sql.oldDataPojo.UserInfo" %>
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>宠物</title>
    <meta charset="utf-8">
    <meta http-equiv = "X-UA-Compatible" content="IE=edge" />
    <link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/dialog.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/pet/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
	<link rel="stylesheet" href="/static/css/pet/activate.css" type="text/css"/>
	<link rel="stylesheet" href="/static/css/pet/forIE.css" type="text/css"/>
	<link rel="stylesheet" href="/static/css/pet/bootstrap.min.css"/>
	<link rel="stylesheet" href="/static/css/pet/k6kt.css"/>
	<link rel="stylesheet" href="/static/css/pet/teacherSheet.css"/>
	<link rel="stylesheet" href="/static/css/pet/dialog.css"/>
	<link rel="stylesheet" type="text/css" href="/static/css/pet/fulanUniversity.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>

    <script src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    


	<style>
	    #fubg {
	        background-color: gray;
	        left: 0;
	        opacity: 0.5;
	        position: fixed;
	        top: 0;
	        z-index: 30;
	        filter: alpha(opacity=50);
	        -moz-opacity: 0.5;
	        -khtml-opacity: 0.5;
	        width: 100%;
	        height: 100%;
	        display: none;
	    }
	
	    .popu div {
	        overflow: hidden
	    }
	    .user-info p > *{float:left}
	    .pet-tab {
		    width: 900px;
		    margin: 0 auto;
		    overflow: visible;
	    }
	    .pet-tab-common {
		    width: 98px;
		    border-right: 1px solid #DDDDDD;
		    color: #363636;
		    background: #F1F1F1;
		    background-repeat: no-repeat;
		    height: 38px;
		    line-height: 38px;
		    font: 16px/38px 'microsoft yahei';
		    padding-left: 17px;
	    }
	    .active{
		    background: white;
			border-top: 4px solid #FF8900;
			margin-top: -4px;
			border-right: 1px solid #DDDDDD;
			color: #FF8900;
	    }
	</style>


    <script type="text/javascript" src="/static/plugins/bjqs-1.3.min.js"></script>

    <script type="text/javascript" src="/static/js/validate-form.js"></script>
    <script type="text/javascript" src="/static/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="/static/js/timepicker.js"></script>
    <script type="text/javascript" src="/static/js/ypxxUtility.js"></script>
	<script type="text/javascript" src="/static/js/pet/jquery.center.js"></script>
	<script type="text/javascript" src="/static/js/pet/petstore.js"></script>
	<!--[if ie 6]>
    <script type="text/javascript" src="/static/js/activity/iepng.js"></script>
	<script type="text/javascript">
	   EvPNG.fix('div,ul,img,li,input,span,b,h1,h2,h3,h4,a,strong,em');
	</script>
	<![endif]-->
	<script type="text/javascript">
	    $(function () {
	        $("#dialog-activate iframe").attr('src',"/obligations.html");
	        //计算进度条
	        var experience = $('#experienceValue').html();
	        var percent = (experience%50)*100/50;
	        $('#progress-bar').attr('style','width:'+percent+'%');
	    });
	    $.ajaxSetup({cache: false });
	
	
	    /*getPrivateLetterCount();
	  //  setInterval("getPrivateLetterCount()", 60000);//定时刷新未读私信数
	    function getPrivateLetterCount() {
	        $.ajax({
	            url: "/user/getPrivateLetterCount.action",
	            type: "post",
	            data: {
	                'inJson': true
	            },
	            success: function (data) {
	                //$("#sl").html(data.messageCount);
	
	                if (data.messageCount > 0) {
	                    $('#message').css('background', 'url(/img/newmessage.png) no-repeat 5px 0px');
	                    $("#sl").html(data.messageCount);
	                } else {
	                    $('#message').css('background', 'url(/img/nomessage.png) no-repeat 10px 10px');
	                }
	            }
	        });
	    }*/

//        function getPrivateLetterCount() {
//            $.ajax({
//                url: "/letter/count.do",
//                type: "post",
//                data: {
//                    'inJson': true
//                },
//                success: function (data) {
//                    //$("#sl").html(data.messageCount);
//
//                    if (data > 0) {
//                        $('#message').css('background', 'url(/img/newmessage.png) no-repeat 5px 0px');
//                        $("#sl").html(data);
//                    } else {
//                        $('#message').css('background', 'url(/img/nomessage.png) no-repeat 10px 10px');
//                    }
//                }
//            });
//        }
//
//        getPrivateLetterCount();
	    
	    function addPetEgg(obj) {
	    	$.ajax({
	    		url:'/pet/addPet.do',
                type:"post",
	    		data:{},
	    		success:function(data){
	    			$('#popup').show();
	    			if (data.resultCode==0) {
	    				$('#hatchfail_msg').html('领取成功，<br/>新的神秘蛋已经放入你的宠物背包啦！');
	    			} else if (data.resultCode==1) {
	    				$('#hatchfail_msg').html('对不起，您还没有达到领取要求。<br/>您需要达到'+ data.experience+'的经验值才能领取这颗神秘蛋哦！<br/>');
	    				$('.getExperienceBtn').attr('href',$('.help-score').attr('href'));
	    			}else if (data.resultCode==2) {
	    				$('#hatchfail_msg').html('领取失败！');
	    			}
	    			$('.getExperienceBtn').attr('href','/petbag');
	    			showPopup();
	    		},
	    		error:function(){
	    			
	    		}
	    	});
	    }
	    

	function showPopup(){
		 var screenWidth=$(window).width();
	     var screenHeight=$(document).height()-110;
		 $(".popup").width(screenWidth);
		 $(".popup").height(screenHeight);
		 $(".popup_bg").css({width:screenWidth+"px",height:screenHeight+"px"});
		 $(".transparent").center();
		 $(".popup_con").center();
		 
		 $(".del-icon").click(function(e) {
            $(".popup").hide();
       });
		$(".know").click(function(e) {
            $(".popup").hide();
       });
	}
	</script>


<!--===============================图片滚动===================================-->
    <script type="text/javascript">
        // 单行滚动
        $(document).ready(function(){
            $("#pet_left").click(function(){
                $("#scrollDiv").animate({marginLeft:"-100px"},500,function(){
                    $(this).css({marginLeft:"0px"}).find("li:first").appendTo(this);
                })
            });

        })

        </script>
    <script type="text/javascript">
        $(document).ready(function(){
            $("#pet_right").click(function(){
                $("#scrollDiv").animate({marginLeft:"100px"},500,function(){
                    $(this).css({marginLeft:"0px"}).find("li:last").prependTo(this);
                })
            });

        })
    </script>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<jsp:include page="../common/infoBanner.jsp">
    <jsp:param name="bannerType" value="petPage"/>
    <jsp:param name="menuIndex" value="1"/>
</jsp:include>
<div style="margin-top: 5px;"></div>
<div class="pet-tab">
<a href="/petbag" class="title_a"><span class="pet-tab-common">宠物背包</span></a>
<a href="/petCenter" class="title_a"><span class="pet-tab-common active">宠物中心</span></a>
<a href="/studentScoreList" class="title_a"><span class="pet-tab-common">历史积分</span></a>
<a href="/experience/experienceRule.do" class="title_a"><span class="pet-tab-common" style="width: 145px;">经验值获取规则</span></a>
</div>
<!-- copy START -->
<!--============================================中间内容===================================-->


<div class="content">
     <div class="con_l">
          <!--=======================领取新的神秘蛋=========================-->
          <div class="left_w">
               <ul class="experience_area">
                  <li class="title"><h4>领取新的神秘蛋</h4><span class="hot"></span></li>
                   <li class="experience">
                       <div><span>经验值<i id="experienceValue">${experience}</i></span></div>
                       <div class="progress">
                            <div id="progress-bar" class="progress-bar progress-bar-success progress-bar-striped" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width:60%">
                            </div>
                       </div>
                   </li>
               </ul>
          </div>
          <!--=======================当前宠物=========================-->
          <div class="current_pet left_w">
               <h4>当前宠物</h4>
               <dl>
                   <input type="hidden" id="petCount" value="${petCount}"/>
               	   <input id="choosePet" class="petid" value="${userpet.id}"/>
                   <dt>
                   	<img id="left_petimg" src="${userpet.maxpetimage}" width="273" height="207" alt="">
                   	<span id="havenopet" class="sorrymsg">亲,您当前还没有宠物,快去领取神秘蛋孵化吧~~</span>
                   </dt>
                   <dd>
                       <h5 class="petName">
                           <label class="incheng">昵称:</label><em>${userpet.petname}</em>
                           <a id="curt-rename" class="pet_name" href="javascript:void(0);" onclick="renameDisplay(this);">改名</a>
                           <span class="reviseName curtRename" style="display:none;"><input type="text" value=""/><a href="javascript:void(0);" class="sure_btn" onclick="rename(this,'${userpet.id}')">确定</a></span>
                       </h5>
                       <p>${userpet.petexplain}</p>
                   </dd>
               </dl>
          </div>
     </div>
     
     <script type="text/javascript">
            $(function(){
                var petImgHeight=$(".pet_img img").height();
				$(".pet_introduce").height(petImgHeight);
            });
     </script>
     


     <div class="con_r">
          <!--======================================领取神秘蛋====================================-->
           <div class="get">
               <img src="/img/pet/images05.png" width="559" height="346" alt="">
               <a href="javascript:void(0);" onclick="addPetEgg(this);">领取神秘蛋</a>
           </div>
           
          <!--======================================孵化宠物====================================-->




           <div class="hatch_pet" style="overflow:hidden;">
                <h3>领取神秘蛋以后，随机孵化出以下宠物：</h3>
               <div style="position: relative;top: 25px;;;width: 559px;height: 50px;">
                   <img src="/img/pet/pet_left.png" style="cursor: pointer;float: left;" id="pet_left">
                   <img src="/img/pet/pet_right.png" style="cursor: pointer;float: right;font-size: 30px;" id="pet_right">
               </div>
                <ul  class="bjqs"  id="scrollDiv" style="min-width: 580px;margin-top: -50px!important;height:100px;">
					<c:forEach items="${pets}" var="pet">
						<li>
							<img src="${pet.minpetimage}" width="98" height="98" alt="">
						</li>
					</c:forEach>
                </ul>
           </div>
           
          <!--======================================经验获取帮助====================================-->
          
          <!--======================================经验获取帮助结束====================================-->
     </div>
</div>

<!--============================================中间内容结束===================================-->
<!-- copy end -->
    <div id="popup" class="popup" style="display:none;">
        <div class="popup_bg"></div>
        <div class="transparent"></div>
        <div class="popup_con">
             <div class="petCon" id="pethatch">
                 <p id="hatchfail_msg">对不起，您还没有达到孵化要求。<br/>
您需要达到200的经验值才能孵化这颗神秘蛋哦！<br/>
加油吧！</p>
                  <a href="javascript:void(0);" class="del-icon">删除</a>
             </div>
             <div class="pet_btn nopet"><a href="/petbag" class="getExperienceBtn">查看我的宠物背包</a><a href="javascript:void(0);" class="know">返回</a></div>
        </div>
    </div>

<%@ include file="../common_new/foot.jsp" %>
</body>
</html>
