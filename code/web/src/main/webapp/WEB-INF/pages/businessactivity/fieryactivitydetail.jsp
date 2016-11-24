<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>平台运营活动</title>
    <%@ page language="java" contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8"%>
    <link rel="stylesheet" href="/static/css/businessactivity/businessactivity.css">
    <%--<link rel="stylesheet" href="/static/css/style.css">--%>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <%--<link rel="stylesheet" type="text/css" href="/static/plugins/fancyBox/jquery.fancybox.css?v=2.1.5" media="screen"/>
    <script type="text/javascript" src="/static/plugins/fancyBox/jquery.fancybox.js?v=2.1.5"></script>--%>
    <script type="application/javascript">
    var editId="";
    function showDeleteDiv(id)
    {
    	editId=id;
    	$("#delete_div").show();
    }
    function deleteFiery()
    {
    	 $.ajax({
             url: '/business/delete.do?id='+editId,
             type: 'get',
             contentType: 'application/json',
             success: function (res) {
               $("#delete_div").hide();
               if(res.code=="200")
               {
            	      location.href="/business/fieryactivitylist.do";
               }
               else
               {
            	    alert(res.message);
               }
             }
         });
    }
    /*$(document).ready(function(){
        $('.fancybox').fancybox();
    });*/
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
            <span class="view-I"><a href="/business/fieryactivitylist.do" style="color: red;">全部火热活动</a></span>
            <span class="view-III">&nbsp;>&nbsp;</span>
            <span class="view-II">${dto.title}</span>
        </div>
        <div class="view-middle">
            <div class="view-left">
                <div>
                    <span class="view-MC">活动名称：${dto.title}</span>
                </div>
                <div>
                    <span>
                        活动时间：
                        <c:if test="${dto.startDate==''&&dto.endDate==''}">
                            长期
                        </c:if>
                        <c:if test="${dto.startDate!=''||dto.endDate!=''}">
                            ${dto.startDate}——${dto.endDate}
                        </c:if>
                    </span>
                </div>
                <div>
                    <span>附件：<span class="viewC">${dto.attachCount}个</span></span>
                </div>
            </div>
            <div class="view-right">
                <div class="view-right-I viewC">
                    <span id="transmit"> </span>
                    <%--<span id="view-delete" onclick="showDeleteDiv('${dto.id}')"  style="margin-top: 48px;">删除</span>--%>
                </div>
            </div>
        </div>
        <div class="view-text">
            <div class="gj-conts">
             ${dto.content}
            </div>
        </div>
        <ul>
            <%--<li>
                <c:if test="${dto.picFile!=null&&dto.picFile!=''}">
                    <span>PC端广告图片</span>
                    <div class="view-YY">
                        <a class="fancybox" href="${dto.picFile}" data-fancybox-group="home" title="预览">
                            <img src="${dto.picFile}?imageView/1/h/80/w/80">
                        </a>
                    </div>
                </c:if>
            </li>
            <li>
                <c:if test="${dto.phonePicFile!=null&&dto.phonePicFile!=''}">
                    <span>手机端广告图片</span>
                    <div class="view-YY">
                        <a class="fancybox" href="${dto.phonePicFile}"data-fancybox-group="home" title="预览">
                            <img src="${dto.phonePicFile}?imageView/1/h/80/w/80">
                        </a>
                    </div>
                </c:if>
            </li>--%>
            <li>
                 <c:forEach items="${dto.docFile}" var="doc">
	                 <div class="view-XZ-left">
	                    ${doc.name}<span class="colorB"></span>
	                </div>
	                <div class="view-XZ-right viewC">
	                    <span><a href="/business/doc/down.do?id=${dto.id}&docId=${doc.idStr}">下载</a></span>
	                </div>
                 </c:forEach>
            </li>
        </ul>
    </div>



	<%--<div id="delete_div" class="inform-popup">
	    <div class="inform-popup-top">
	        <span>提示</span>
	    </div>
	    <div class="inform-popup-middle">
	        <span>删除后收件人的火热活动列表将不再显示此火热活动!</span>
	    </div>
	    <div class="infrom-popup-bottom">
	        <button onclick="deleteFiery()">删除</button>
	        <button id="infrom-popup-QX" onclick="javascript:$('#delete_div').hide();">取消</button>
	    </div>
	</div>--%>

</div>
</div>
</body>
</html>
