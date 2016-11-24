<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<head>
    <title></title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/cloudclass.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/cloudclass.js"></script>

    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/css/inform.css">
    <link rel="stylesheet" href="/static/css/style.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="js/inform.js"></script>
    <script type="text/javascript" src="/static/plugins/ui-bootstrap-tpls-0.11.2.min.js"></script>
    <script src="/static/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <script type="text/javascript">

    $(function(){
    	resetPaginator(${pages});
    });

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
               if(res.code=="200")
            		{
            	      jQuery("#id_"+editId).remove();
            	      jQuery("#delete_div").hide();
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
            	      var length=jQuery("#top_"+id).find("span").length;
            	      if(length>0)
            	      {
            	    	  jQuery("#top_"+id).find("span").remove();
            	    	  jQuery("#id_"+id).appendTo(jQuery("#item_list"));
            	    	  jQuery("#topoper_"+id).text("置顶");
            	      }
            	      else
            	      {
            	    	  jQuery("#top_"+id).prepend("<span>【置顶】</span>");
            	    	  jQuery("#id_"+id).prependTo(jQuery("#item_list"));
            	    	  jQuery("#topoper_"+id).text("取消置顶");
            	      }
            	      
            	}
             }
         });
    }
    
    function detail(id)
    {
    	location.href="/notice/detail.do?id="+id;
    }
    
    var currentpage = "${pageIndex}";
    function resetPaginator(totalPages) {
        if (totalPages <= 0) {
            totalPages = 1;
        }
        $('#example').bootstrapPaginator("setOptions", {
            currentPage: currentpage,
            totalPages: totalPages,
            itemTexts: function (type, page, current) {
                switch (type) {
                    case "first":
                        return "首页";
                    case "prev":
                        return "<";
                    case "next":
                        return ">";
                    case "last":
                        return "末页"+page;
                    case "page":
                        return  page;
                }
            },
            onPageClicked: function(e, originalEvent, type, page) {
                currentpage = page;
                pageLoad(page);
            }
        });
    }
    
    function pageLoad(page)
    {
    	location.href="/notice/docflow.do?page="+page;
    }
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
<!--===========================编辑公文流转======================================-->
    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>
    <!--广告-->
<div class="inform-main">
    <div class="inform-top">
        <div class="inform-top-I" id="new-inform">
            <c:if test="${roles:isNotStudentAndParent(sessionValue.userRole)}">
            <img src="/img/notic/inform.png">
            <span><a href="/notice/createDocflow.do">发布新公文流转</a></span>
            </c:if>
            <!--====================发送中==============================-->
            <%--<span>1封邮件正在发送....</span>--%>
            <!--======================发送===========================-->
            <%--<span>发送成功</span>--%>
        </div>
        <div class="inform-top-II">
            <span class="inform-TJ">公文流转列表</span>
            <span>共${dto.count}条</span>
        </div>

    </div>

    <!--===========================编辑公文流转===============================-->
    <div class="compile-main" id="compile_in">
        <div class="compile-top">
            <span>收件人</span><input type="" name=" " value="">
            <button id="compile-but">通讯录</button>
        </div>
        <div class="compile-top">
            <span>标题</span> <input class="compile-top-input" type="" name="" value="">
        </div>
        <div class="compile-hr">
            <span>编辑公文流转的内容</span>
        </div>
        <textarea name="inform-BJ" class="inform-BJ">

        </textarea>
        <div class="compile-add">
            <img src="images/inform-recode.png"><span>添加语音</span>
            <img src="images/inform-complie.png"><span>添加附件</span>
        </div>
        <div class="infrom-YY">
            <img src="images/BF.png">
            <div>
                <span>3333</span>
                <span class="inform-YY-SC">删除</span>
            </div>

        </div>

        <div class="infrom-FJ">
            <img src="images/inform-complie.png">
            <div>
                <span>考试</span><span class="inform-kb">(88kb)</span>
                <span class="inform-FJ-hr"></span>
                <span class="inform-YY-SC">删除</span>
            </div>
        </div>
        <div class="inform-bottom">
            <div class="inform-bottom-left">
                <input type="checkbox" name=""><span>同步到日历</span>
            </div>
            <div class="inform-bottom-middle">
                <img src="images/inform-time.png"><span class="infrom-YX">有效时间</span><span class="testB">(选填)</span>
                <span>开始</span><input type="text" name="" value="">
                <span>结束</span><input type="text" name="" value="">
            </div>
            <div class="infrom-right">
                <button>发送</button>
                <button  id="QX">取消</button>
            </div>
        </div>


        <!--===========================有效时间说明==================================-->
        <div class="valid-time">
            <span class="valid-time-I">*有效时间</span>
            <span>有效时间是同步到日历中显示的时间，有效时间过后公文流转中自动显示为失效时间</span>
        </div>
        <div class="valid-timeT">

        </div>
    </div>

    <ul id="item_list">
        <!--================================置顶================================-->
        <c:forEach items="${dto.list}" var="notice">
        
	        <li id="id_${notice.id}">
	            <img src="${notice.avator }">
	            <div>
	                <span class="inform-ZD tet" id="top_${notice.id}">
	                   <c:if test="${notice.top==1 }"><span>【置顶】</span></c:if>
	                   ${notice.title }
	                </span>
	                <span class="inform-name tet"> ${notice.teacherName }</span>
	                <span class="inform-time"> ${notice.time }</span>
	                <div class="inform-li-right">
	                
	                     <c:if test="${notice.already==1 }">
	                      <span class="inform-delete infrom-LO" id="infrom-delete" onclick="detail('${notice.id}')">已看</span>
	                     </c:if>
	                     <c:if test="${notice.already==0 }">
	                      <span class="inform-delete inform-YC" id="infrom-delete" onclick="detail('${notice.id}')">查看</span>
	                     </c:if>
	                
	                    <span class="inform-delete inform-YC" id="infrom-delete" onclick="showDeleteDiv('${notice.id}')">删除</span>
	                     <c:if test="${notice.top==1 }">
	                     <span class="inform-XX inform-YC"><span id="topoper_${notice.id}" onclick="toTop('${notice.id}')">取消置顶</span></span>
	                     </c:if>
	                     <c:if test="${notice.top==0 }">
	                     <span class="inform-XX inform-YC"><span id="topoper_${notice.id}" onclick="toTop('${notice.id}')">置顶</span></span>
	                     </c:if>
	                </div>
	            </div>
	        </li>
        </c:forEach>
       
    </ul>



    <!--====================================分页===============================-->
</div>
    <div class='center-container' style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:hidden;margin:0 auto;">
        <div id="example" style="width:500px"></div>
    </div>
</div>
    <!--====================================分页===============================-->

    
<!--===============================删除弹出框=======================================-->
<div id="delete_div" class="inform-popup">
    <div class="inform-popup-top">
        <span>提示</span>
    </div>
    <div class="inform-popup-middle">
        <span>删除后收件人的公文流转列表和日历也将不再显示此公文流转!</span>
    </div>
    <div class="infrom-popup-bottom">
        <button onclick="deleteNotice()">删除</button>
        <button id="infrom-popup-QX" onclick="javascript:jQuery('#delete_div').hide();">取消</button>
    </div>
</div>
</div>
</div>
<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp" %>
<!-- 页尾 -->
</body>
 </html>
