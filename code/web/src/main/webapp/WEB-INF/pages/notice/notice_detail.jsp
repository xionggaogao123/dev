<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>通知</title>
    <%@ page language="java" contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8"%>
    <link rel="stylesheet" href="/static/css/inform.css">
    <link rel="stylesheet" href="/static/css/style.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/notic/inform.js"></script>
    <script type="application/javascript">
        $(function(){
            $("#transmit").click(function(){
                $("#compile_in").css("display","block")
            })
        })
        $(function(){
            $("#QX").click(function(){
                $(".inform-popup-I").css("display","block")
            })
            $(function(){
                $("#infrom-bottom-BQ").click(function(){
                    $(".inform-popup-I").css("display","none")
                })
            })
            $(function(){
                $("#infrom-bottom-QX").click(function(){
                    $(".inform-popup-I").css("display","none")
                    $(".compile-main").css("display","none")
                })
            })
            $(function(){
                $("#view-delete").click(function(){
                    $(".inform-popup").css("display","block")
                })
            })
            $(function(){
                $("#infrom-popup-QX").click(function(){
                    $(".inform-popup").css("display","none")
                })
            })
        })
        
    var editId="";
    function showDeleteDiv(id)
    {
    	editId=id;
    	jQuery("#delete_div").show();
    }
    function deleteNotice()
    {
    	 $.ajax({
             url: '/notice/delete.do?id='+editId,
             type: 'get',
             contentType: 'application/json',
             success: function (res) {
               jQuery("#delete_div").hide();
               if(res.code=="200")
               {
            	      location.href="/notice/index.do";
               }
               else
               {
            	    alert(res.message);
               }
             }
         });
    }
    
    function toTop(id)
    {
    	 $.ajax({
             url: '/notice/top.do?id='+id,
             type: 'get',
             contentType: 'application/json',
             success: function (res) {
               if(res.code=="200")
            	{
            	      var length=jQuery("#top_"+id).find(".view-ZD").length;
            	      if(length>0)
            	      {
            	    	  jQuery("#top_"+id).find(".view-ZD").remove();
            	    	  jQuery("#topoper_"+id).text("置顶");
            	      }
            	      else
            	      {
            	    	  jQuery("#top_"+id).prepend('<span class="view-ZD">【置顶】</span>');
            	    	  jQuery("#id_"+id).prependTo(jQuery("#item_list"));
            	    	  jQuery("#topoper_"+id).text("取消置顶");
            	      }
            	      
            	} else
            	   {
            	    alert(res.message);
            	   }
             }
         });
    }
    
    
    function PlayedMusic(url, voiceId) {
        var player = '<embed src="' + url + '" belong="' + voiceId + '" width="0" height="0" autostart="true" />';
        $('embed[belong=' + voiceId + ']').remove();
        $('body').append(player);
    }

    </script>

    <%--  <!--======================隔行变色===================-->
   <script type="application/javascript">
       $(document).ready(function(){
       var uls = document.getElementById("ul");
       var li =  uls.getElementsByTagName("li");
       for(var i =0;i<li.length;i++){
           if(i%2!=0){
               li[i].style.backgroundColor = "#ffffff";
           }
       }
       })
   </script>--%>
    <!--===========================================-->

    <script type="application/javascript">
        $(document).ready(function(){
            $(".comtacts-ss").click(function(){
                $(".XL").toggle();
            });
        });
    </script>
    <script type="application/javascript">
        $(function(){
            $("#compile-but").click(function(){
                $(".contacts-main").css("display","block")
                $("#bg").css("display","block")
            })
        })
    </script>
    <script type="application/javascript">
        $(function(){
            $(".contacts-top-right-I").click(function(){
                $(".contacts-main").css("display","none")
                $("#bg").css("display","none")
            })
        })
    </script>
</head>
<body>
<div class="inform-all">
<!-- 页头 -->
    <%@ include file="../common_new/head.jsp" %>
<!-- 页头 -->
<div class="informm-main">
<!--左侧导航-->
    <%@ include file="../common_new/col-left.jsp" %>
    <div class="view-main">
        <div class="view-top">
            <span class="view-I"><a href="/notice/index.do">全部通知</a></span>
            <span class="view-III">&nbsp;>&nbsp;</span>
            <span class="view-II">${dto.title}</span>
        </div>
        <div class="view-middle">
            <div class="view-left">
                <div id="top_${dto.id}">
                    <c:if test="${dto.top==1 }">
                     <span class="view-ZD">【置顶】</span>
                    </c:if>
                    <span class="viewB">${dto.title}</span>
                </div>
                <div>
                    <span>发件人：${dto.teacherName}</span>
                </div>
                <div>
                    <span>时间：${dto.time}</span>
                </div>
                <div>
                    <span>附件：<span class="viewC">${dto.attachCount}个</span></span>
                </div>
            </div>
            <div class="view-right">
                <div class="view-right-I viewC">
                    <span id="transmit"> </span>
	                    <c:if test="${dto.isSelfOper==1}">
		                    <span id="view-delete" onclick="showDeleteDiv('${dto.id}')"  style="margin-top: 48px;">删除</span>
		                    <c:if test="${dto.top==1}">
		                    <span class="view-right-II" id="topoper_${dto.id}" onclick="toTop('${dto.id}')">取消置顶</span>
		                   </c:if>
		                    <c:if test="${dto.top==0}">
		                    <span class="view-right-II"  id="topoper_${dto.id}" onclick="toTop('${dto.id}')" style="vertical-align: middle;margin-top: -22px;">置顶</span>
		                    </c:if>
	                     </c:if>
                </div>
            </div>
        </div>
        <div class="view-text">
            <div>
             ${dto.content}
            </div>
        </div>
        <ul>
            <!--==================================语音====================================-->
            <li>
                   <c:forEach items="${dto.voiceFile}" var="doc">
                    <div class="view-YY">
                       <span>语音</span>
                       <img src="/img/notic/BF.png" onclick="PlayedMusic('${doc.value}','${doc.idStr}')"><span class="colorB">&nbsp;&nbsp;</span>
                    </div>
                 </c:forEach>
            </li>
            <li>
                 <c:forEach items="${dto.docFile}" var="doc">
	                 <div class="view-XZ-left">
	                    ${doc.name}<span class="colorB"></span>
	                </div>
	                <div class="view-XZ-right viewC">
	                    <span><a href="/notice/doc/down.do?id=${dto.id}&docId=${doc.idStr}">下载</a></span>
	                    
	                </div>
                 </c:forEach>
            </li>
        </ul>
    </div>



	<div id="delete_div" class="inform-popup">
	    <div class="inform-popup-top">
	        <span>提示</span>
	    </div>
	    <div class="inform-popup-middle">
	        <span>删除后收件人的通知列表和日历也将不再显示此通知!</span>
	    </div>
	    <div class="infrom-popup-bottom">
	        <button onclick="deleteNotice()">删除</button>
	        <button id="infrom-popup-QX" onclick="javascript:jQuery('#delete_div').hide();">取消</button>
	    </div>
	</div>

</div>
</div>
</body>
</html>
