<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>宠物</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>

<link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>


<script type="text/javascript" src="/static/js/jquery.min.js"></script>

<script src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" type="text/javascript"></script>

<script type="text/javascript" src="/static/js/sharedpart.js"></script>
<!-- <link rel="stylesheet" type="text/css" href="/pet/css/style.css"/> -->
<link rel="stylesheet" href="/static/css/pet/activate.css" type="text/css"/>
<link rel="stylesheet" href="/static/css/pet/forIE.css" type="text/css"/>
<link rel="stylesheet" href="/static/css/pet/bootstrap.min.css"/>
<link rel="stylesheet" href="/static/css/pet/k6kt.css"/>
<link rel="stylesheet" href="/static/css/pet/teacherSheet.css"/>
<link rel="stylesheet" href="/static/css/pet/dialog.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/pet/fulanUniversity.css">
<link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">

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

    .user-info p > * {
        float: left
    }
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
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/static/js/validate-form.js"></script>
<script type="text/javascript" src="/static/js/jquery-ui.min.js"></script>
<script src="/static/js/activity/bootstrap.min.js"></script>
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


    function chooseMyPet(id, obj) {
        $.ajax({
            url: '/pet/chooseMyPet.do',
            type:"post",
            data: {'id': id},
            success: function (data) {
                $(obj).html('当前宠物');
                $('.list_btn').removeClass('list_curPet');
                $('.list_btn').children('a').removeClass().addClass('change');
                $('.list_btn').children('a').html('更改');
                /* $('.list_btn').each(function(){
                 var petid = $(this).parent().siblings('input').val();
                 $(this).children('a').attr('onclick','chooseMyPet('+ petid +',this)');
                 }); */
                $(obj).addClass('current_pet');
                $(obj).parent().addClass('list_curPet');
                $(obj).html('当前宠物');
                var middle = $(obj).parent().siblings('dt').children('img').attr('src');
                middle = middle.split('_')[0] + '_max.jpg';
                image = middle.split('_')[0] + '.png';
                $('#left_petimg').parent().prev().attr('value', id);
                $('#left_petimg').attr('src', middle);
                $('#left_petname').html($(obj).parent().siblings('dd').find('em').html());
                $('#left_petrename').attr('onclick', 'rename(this,' + id + ')');
                $('#left_petintrdc').html($(obj).parent().siblings('dd').find('p').html());
                $('.pet-name').html($(obj).parent().siblings('dd').find('em').html());
                $('#choosenPet>img').attr('src', image);
            },
            error: function () {

            }
        });
    }

    function hatchPet(id) {
        $.ajax({
            url: '/pet/hatchPet.do',
            type:"post",
            data: {'id': id},
            success: function (data) {
                if (data.resultCode == 0) {
                    $('#popup1').show();
                    $('#pup_petimg').attr('src', data.petimage);
                    $('#pup_petname').html(data.petname);
                } else {
                    $('#popup2').show();
                    if (data.mesg != null && data.mesg != '') {
                        $('#hatchfail_msg').html(data.mesg);
                        $('.getExperienceBtn').hide();
                    } else {
                        $('#hatchfail_msg').html('对不起，您还没有达到孵化要求。<br/>您需要达到' + data.experience + '的经验值才能孵化这颗神秘蛋哦！<br/>加油吧！');
                        $('.getExperienceBtn').show();
                        $('.getExperienceBtn').attr('href', $('.help-score').attr('href'));
                        $('#makescore').parent().attr('target', '_blank');
                    }

                }
                showPopup();

            },
            error: function () {
//    			alert('服务器错误!');
            }
        });
    }
    function showPopup() {
        var screenWidth = $(window).width();
        var screenHeight = $(document).height() - 110;
        $(".popup").width(screenWidth);
        $(".popup").height(screenHeight);
        $(".popup_bg").css({width: screenWidth + "px", height: screenHeight + "px"});
        $(".transparent").center();
        $(".popup_con").center();

        $(".del-icon").click(function (e) {
            $(".popup").hide();
        });
        $(".sureBtn").click(function (e) {
            $(".popup").hide();
        });
        $(".know").click(function (e) {
            $(".popup").hide();
        });
    }
    $(function () {
        $("#dialog-activate iframe").attr('src', "/obligations.html");
        //计算进度条
        var experience = $('#experienceValue').html();
        var percent = (experience % 50) * 100 / 50;
        $('#progress-bar').attr('style', 'width:' + percent + '%');
        getAllPet();
    });
    $.ajaxSetup({cache: false });
    function getAllPet() {
        //获取宠物列表
        $.ajax({
            url: '/pet/selAllPet.do',
            type:"post",
            success: function (data) {
                var notHatchList = data.notHatchList;
                for (var i = 0; i < notHatchList.length; i++) {
                    var egg_item =
                            '<li class="right_w fuhua">' +
                            '   <dl>' +
                            '<input class="petid" value="' + notHatchList[i].id + '"/>' +
                            '  <dt><img src="/img/smallhatch.png" width="75" height="75" alt=""></dt>' +
                            ' <dd id="petIntroduce">' +
                            '    <h4><em>神秘蛋</em></h4>' +
                            '   <p>耐心等待！将会有神奇的事情发生！</p>' +
                            ' </dd>' +
                            '<dd class="list_btn_egg hatch"><a href="javascript:void(0);" onclick="hatchPet(\'' + notHatchList[i].id + '\')">孵化</a></dd>' +
                            '</dl>' +
                            '</li>';
                    $('#petlist').append(egg_item);
                }
                var hatchList = data.hatchList;
                for (var i = 0; i < hatchList.length; i++) {
                    var isSelect = '';
                    if (hatchList[i].selecttype == 1) {
                        isSelect = '<dd class="list_btn list_curPet"><a href="javascript:void(0);" class="current_pet" onclick = "chooseMyPet(\'' + hatchList[i].id + '\',this)">当前宠物</a></dd>'
                    } else {
                        isSelect = '<dd class="list_btn"><a href="javascript:void(0);" class="change" onclick = "chooseMyPet(\'' + hatchList[i].id + '\',this)">更改</a></dd>';
                    }
                    var pet_item = '<li class="right_w">' +
                            '<dl>' +
                            '<input class="petid" value="' + hatchList[i].id + '"/>' +
                            '<dt class="pet_img"><img src="' + hatchList[i].middlepetimage + '" width="135" height="134" alt=""></dt>' +
                            '<dd class="pet_introduce">' +
                            '   <h4>' +
                            '     <em>' + hatchList[i].petname + '</em><a href="javascript:void(0);" class="pet_name" onclick="renameDisplay(this);">改名</a>' +
                            '    <span class="reviseName" style="display:none;"><input type="text"/><a href="javascript:void(0);" class="sure_btn" onclick="rename(this,\'' + hatchList[i].id + '\')">确定</a></span>' +
                            '</h4>' +
                            '<p>' + hatchList[i].petexplain + '</p>' +
                            '</dd>' +
                            isSelect +
                            '</dl>' +
                            '</li>';
                    $('#petlist').append(pet_item);
                }
                if ($('#petlist').children('li').length > 0) {
                    $('#r_sorry').hide();
                }
                if (data.experience>=50) {
                	$('#span1').html('亲,对不起!您还没领取宠物哦~~');
                    var html='<a href="/petCenter">快去宠物中心领取吧...</a>';
                	$('#span2').html(html);

                }
            },
            error: function () {
//        		alert('服务器错误');
            }
        });
    }

    /*getPrivateLetterCount();
    //setInterval("getPrivateLetterCount()", 60000);//定时刷新未读私信数
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
//    function getPrivateLetterCount() {
//        $.ajax({
//            url: "/letter/count.do",
//            type: "post",
//            data: {
//                'inJson': true
//            },
//            success: function (data) {
//                //$("#sl").html(data.messageCount);
//
//                if (data > 0) {
//                    $('#message').css('background', 'url(/img/newmessage.png) no-repeat 5px 0px');
//                    $("#sl").html(data);
//                } else {
//                    $('#message').css('background', 'url(/img/nomessage.png) no-repeat 10px 10px');
//                }
//            }
//        });
//    }

//    getPrivateLetterCount();
</script>

</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<jsp:include page="../common/infoBanner.jsp">
    <jsp:param name="bannerType" value="petPage"/>
    <jsp:param name="menuIndex" value="0"/>
</jsp:include>
<div style="margin-top: 5px;"></div>
<div class="pet-tab">
<a href="/petbag" class="title_a"><span class="pet-tab-common active">宠物背包</span></a>
<a href="/petCenter" class="title_a"><span class="pet-tab-common">宠物中心</span></a>
<a href="/studentScoreList" class="title_a"><span class="pet-tab-common">历史积分</span></a>
<a href="/experience/experienceRule.do" class="title_a"><span class="pet-tab-common" style="width: 145px;">经验值获取规则</span></a>
</div>
<!-- copy start-->
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
                         <label class="incheng">昵称:</label><em id="left_petname">${userpet.petname}</em>
                         <a id="curt-rename" class="pet_name" href="javascript:void(0);" onclick="renameDisplay(this);">改名</a>
                         <span class="reviseName curtRename" style="display:none;"><input type="text" value=""/><a id="left_petrename"  href="javascript:void(0);" class="sure_btn" onclick="rename(this,'${userpet.id}')">确定</a></span>
                     </h5>
                     <p id="left_petintrdc">${userpet.petexplain}</p>
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
          <!--======================================宠物列表====================================-->
          <div class="student_list">
              <ul id="petlist">
              </ul>
          </div>
          <div class="sorrymsg" id="r_sorry">
              <c:if test="${experience<50}">
                  <div><img src="/img/sorry.png"/></div>
                  <div id="r_tips">
                      <span id="span1">亲,对不起!您的积分还不足~~</span>
                      <span id="span2" style="color: #bbb;">
                          <a href="/personal/userhelp">快去学习赚取积分吧...</a>
                      </span>
                  </div>
              </c:if>
              <c:if test="${experience>=50}">
                  <div id="r_tips">
                      <span id="span1">亲,恭喜您!您可以到宠物中心领取宠物蛋啦~~ </span>
                      <span id="span2" style="color: #bbb;">
                          <a href="/petCenter">快去宠物中心领取吧...</a>
                      </span>
                  </div>
              </c:if>
          </div>
     </div>
<!-- copy end -->



<!--============================================弹出层===================================-->

    <div id="popup1" class="popup" style="display:none;">
        <div class="popup_bg"></div>
        <div class="transparent"></div>
        <div class="popup_con">
             <div class="petCon" id="pethatch">
                  <dl>
                      <dt><img id="pup_petimg" src="/img/pet/images03.png" width="94" height="94" alt=""></dt>
                      <dd>恭喜，<br/>您获得了一只<em id="pup_petname">大胖胖</em>！</dd>
                  </dl>
                  <a href="javascript:void(0);" class="del-icon">删除</a>
             </div>
             <div class="pet_btn"><a href="/petbag" class="sureBtn" >确定</a></div>
        </div>
    </div>
    
    <div id="popup2" class="popup" style="display:none;">
        <div class="popup_bg"></div>
        <div class="transparent"></div>
        <div class="popup_con">
             <div class="petCon" id="pethatch">
                 <p id="hatchfail_msg">对不起，您还没有达到孵化要求。<br/>
                您需要达到200的经验值才能孵化这颗神秘蛋哦！<br/>
                加油吧！</p>
                  <a href="javascript:void(0);" class="del-icon">删除</a>
             </div>
             <div class="pet_btn nopet" style="overflow: hidden;"><a href="/personal/userhelp" class="getExperienceBtn">如何获取经验值</a><a href="javascript:void(0);" class="know">我知道了</a></div>
        </div>
    </div>
<!--============================================弹出层结束===================================-->



</div>
<%@ include file="../common_new/foot.jsp" %>
</body>
</html>
