<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/itempool/teacher_information.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>

    <script type="text/javascript">
     
     var selArr=[];
     function select(id)
     {
    	 var index= jQuery.inArray(id, selArr);
    	 if(index==-1)
    	 {
    		 selArr.push(id);
    		 jQuery("#"+id).append('<img src="/images/teacher_genterea.png">').removeClass("teacher_information_new_generata_middle_III").addClass("teacher_information_new_generata_middle_VI");
    	 }
    	 else
    	 {
    		 selArr.splice(index,1);
    		 jQuery("#"+id).find("img").remove();
    		 jQuery("#"+id).removeClass("teacher_information_new_generata_middle_VI").addClass("teacher_information_new_generata_middle_III");
         }
     }
     
     
     function push()
     {
    	 var ids =getids();
    	 if(!ids)
    	 {
    		 alert("请选择要推动的班级");
    		 return;
    	 }
    	 
    	 jQuery("#prompt-div").show();
    	 $.ajax({
             url: '/testpaper/push.do?pid='+jQuery("#paperId").val()+"&cids="+ids,
             type: 'get',
             contentType: 'application/json',
             success: function (res) {
                {
                	 jQuery("#prompt-div").hide();
              	 jQuery("#push_div").hide();
              	 jQuery("#success_div").show();
                }
             }
         });
     }
     
     function getids()
     {
  	   var ids="";
     	   for(var i=0;i<selArr.length;i++)
     	   {
     		ids+=selArr[i]+",";
     	   }
     	   return ids;
     }

     $(document).ready(function() {
         var type = $('body').attr('type');
         if(1 == type){
             $('#xbsj1').hide();
         }

     });
     
    </script>
    
</head>
<body style="background: #fff;" type="${type}">
<!-- 页头 -->
<%@ include file="../common_new/head.jsp"%>
<!-- 页头 -->

<div id="YCourse_player" class="player-container" >
    <div id="player_div" class="player-div"></div>
    <div id="sewise-div" style="display: none; width: 800px; height: 450px;max-width: 800px;">
        <script type="text/javascript" src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
    </div>
    <span onclick="closeCloudView()" class="player-close-btn"></span>
</div>

<script type="text/javascript">
    SewisePlayer.setup({
        server : "vod",
        type : "m3u8",
        skin : "vodFlowPlayer",
        logo : "none",
        lang: "zh_CN",
        topbardisplay : 'disabled',
        videourl: ""
    });

    $(document).ready(function() {
        $(".teacher_information_li").click(function () {
            $(this).removeClass('cur-a').siblings('.teacher_information_info ul li').addClass('cur-a');
        });
    });
</script>
<div class="teacher_infromation_body">
<%@ include file="../common_new/col-left.jsp" %>
  
    <input id="paperId" name="paperId" type="hidden" value="${paperId}" />
    <div id="continer" class="teacher_information_right" >
        <div class="teacher_information_info" >
            <ul>
                <li class="teacher_information_li"><a href="javascript:void(0)">我的组卷</a></li>
                <li class="teacher_information_li cur-a"><a href="javascript:void(0)" >校本试卷</a></li>
            </ul>
            <div  class="teacher_information_right_I" style="display:none;" >
                <button>手工组卷</button>
                <button>自动组卷</button>
            </div>
        </div>
        
        
        <!--==============================推送===============================-->
        <div>
            <div >
                <div class="teacher_information_new_I_top" style="clear: both">
                    <span class="information_I" style="margin-left: 20px;">我的组卷</span>
                    <span class="information_II">/&nbsp;试卷结构</span>
                </div>
                <div id="push_div">
                    <div class="teacher_information_new_info">
                        推送
                    </div>
                    <div class="teacher_information_new_generata_middle">
                        <ul>
                            <li>
                                <span>试卷名称</span>
                                <input value="${name }">
                            </li>
                            <li class="clearfix">
                                <span >选择班级</span>
                                <div class="teacher_information_new_generata_middle_II">
                                
                                    <c:forEach items="${alreadyList}" var="item" begin="0"  varStatus="status">
                                        <button   class="teacher_information_new_generata_middle_V"><div>${item.value }</div><span>已推送</span></button>
                                    </c:forEach> 
                                 
                                    <c:forEach items="${list}" var="item" begin="0"  varStatus="status">
                                        <button id="${item.idStr}"  class="teacher_information_new_generata_middle_III" onclick="select('${item.idStr}')"><div>${item.value }</div></button>
                                    </c:forEach>
                                   
                                </div>
                            </li>
                            <li class="clearfix" id="xbsj1">
                                <span>选择题库</span>
                                <div class="teacher_information_new_generata_middle_II">
                                    <c:choose>
                                        <c:when test="${state == 3}">
                                            <button  class="teacher_information_new_generata_middle_V"><div>校本试卷</div><span>已推送</span></button>
                                        </c:when>
                                        <c:otherwise>
                                            <button id="xbsj" class="teacher_information_new_generata_middle_III" onclick="select('xbsj')"><div>校本试卷</div></button>
                                        </c:otherwise>
                                    </c:choose>

                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="teacher_information_new_buttom">
                        <span onclick="push()">推送</span>
                    </div>
                </div>
                <!--==============================推送成功===============================-->
                <div id="success_div" style="display:none">
                    <div class="teacher_information_new_generata_bottom">
                        <img src="/images/teacher_genterea_I.png"><span class="teacher_information_new_generata_bottom_I">推送成功！</span>
                        <span>请去校本试卷中查看试卷或去考试中查看试卷做题情况</span>
                        <a class="teacher_information_new_generata_bottom_II"  href="/exam/index.do?version=13">>>查看考试</a>
                    </div>
                    <div class="teacher_information_new_buttom">
                        <span onclick="javascript:document.location.href='/testpaper/list.do'">返回我的组卷</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
	
<!--=============================推送弹出框=======================================================-->
<div id="prompt-div" style="display: null;">
    <div class="message-box"><div class="message-box-title">提示</div>
        <div><i class="fa fa-spinner fa-spin fa-4x" style="color:deepskyblue;vertical-align: middle; margin-right: 30px">

        </i>推送中......
        </div>
    </div>
</div>
            
<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp"%>
</body>
</html>
