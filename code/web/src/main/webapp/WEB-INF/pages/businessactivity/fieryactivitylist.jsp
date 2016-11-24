<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<head>
    <title>平台运营活动</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/businessactivity/businessactivity.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
    <script src="/static/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <script src="/static/plugins/echarts/echarts.js"></script>
    <script type="text/javascript">
        var option={};
        $(function(){
            option.total= '${dto.count}';
            option.pagesize= 12;
            option.currentpage='${pageIndex}';
            option.operate=function (totalPage) {
                $('.page-index span').each(function () {
                    $(this).click(function(){
                        if('${pageIndex}'!=parseInt($(this).text())){
                            pageLoad(parseInt($(this).text()));
                        }
                    });
                });
                $('.first-page').click(function(){
                    if('${pageIndex}'!=1) {
                        pageLoad(1);
                    }
                });
                $('.last-page').click(function(){
                    if('${pageIndex}'!=totalPage) {
                        pageLoad(totalPage);
                    }
                })
            }
            initPaginator(option);
        });

        function initPaginator(option) {
            var totalPage = '';
            if (option.total == 0)
                $('.page-paginator').hide();
            else
                $('.page-paginator').show();
            $('.page-index').empty();
            if (option.total % option.pagesize == 0) {
                totalPage = option.total / option.pagesize;
            } else {
                totalPage = parseInt(option.total / option.pagesize) + 1;
            }
            buildPaginator(totalPage, parseInt(option.currentpage));
            option.operate(totalPage);
        };

        /**
         * 构造分页
         * @param totalPage
         * @param currentPage
         */
        function buildPaginator(totalPage, currentPage) {
            if (totalPage > 5) {
                if (currentPage < 4) {
                    for (var i = 1; i <= 5; i++) {
                        if (i == currentPage) {
                            $('.page-index').append('<span class="active">' + i + '</span>');
                        } else {
                            $('.page-index').append('<span>' + i + '</span>');
                        }
                    }
                    $('.page-index').append('<i>···</i>');
                } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
                    $('.page-index').append('<i>···</i>');
                    for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                        if (i == currentPage) {
                            $('.page-index').append('<span class="active">' + i + '</span>');
                        } else {
                            $('.page-index').append('<span>' + i + '</span>');
                        }
                    }
                    $('.page-index').append('<i>···</i>');
                } else {
                    $('.page-index').append('<i>···</i>');
                    for (var i = totalPage - 4; i <= totalPage; i++) {
                        if (i == currentPage) {
                            $('.page-index').append('<span class="active">' + i + '</span>');
                        } else {
                            $('.page-index').append('<span>' + i + '</span>');
                        }
                    }
                }
            } else {
                for (var i = 1; i <= totalPage; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
            }
        }

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
                    if(res.code=="200")
                    {
                        $("#id_"+editId).remove();
                        $("#delete_div").hide();
                    }
                    else
                    {
                        alert(res.message);
                    }
                }
            });
        }

        function detail(id)
        {
            location.href="/business/detail.do?id="+id;
        }

        function update(id){
            location.href="/business/update.do?id="+id;
        }

        function pageLoad(page)
        {
            location.href="/business/fieryactivitylist.do?page="+page;
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
        <!--===========================编辑通知======================================-->
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
        <div class="inform-main" style="min-height: 900px;">
            <div class="inform-top">
                <div class="inform-top-I" id="new-inform">
                    <c:if test="${roles:isSysManager(sessionValue.userRole)}">
                        <img src="/img/notic/inform.png">
                        <span><a href="/business/create.do">新建火热活动</a></span>
                    </c:if>
                    <!--====================发送中==============================-->
                    <%--<span>1封邮件正在发送....</span>--%>
                    <!--======================发送===========================-->
                    <%--<span>发送成功</span>--%>
                </div>
                <div class="inform-top-II">
                    <span class="inform-TJ">火热活动列表</span>
                    <span>共${dto.count}条</span>
                </div>

            </div>

            <ul id="item_list">
                <!--================================置顶================================-->
                <c:forEach items="${dto.list}" var="item">
                    <li id="id_${item.id}">
                     <div>
                            <span class="inform-ZD tet fiery-wid">
                                <em>
                                <c:if test="${item.isFinish==0}">
                                   <em style="color: #fda616;font-weight: bold">[进行中] &nbsp; &nbsp; &nbsp; &nbsp;
                                       <a onclick="detail('${item.id}')">${item.title}</a>
                                   </em>
                                </c:if>
                                <c:if test="${item.isFinish==1}">
                                    <em style="color: #666;"> [已结束] &nbsp; &nbsp; &nbsp; &nbsp;
                                        <a onclick="detail('${item.id}')">${item.title}</a>
                                    </em>

                                </c:if>
                                </em>

                            </span>
                            <span class="inform-time">
                                <c:if test="${item.startDate==''&&item.endDate==''}">
                                    长期
                                </c:if>
                                <c:if test="${item.startDate!=''||item.endDate!=''}">
                                    ${item.startDate}——${item.endDate}
                                </c:if>
                            </span>
                         <div class="inform-li-right">
                             <span class="inform-deletee inform-YC" id="infrom-search" onclick="detail('${item.id}')">查看</span>
                             <c:if test="${roles:isSysManager(sessionValue.userRole)}">
                                 <span class="inform-deletee inform-YC" id="infrom-delete" onclick="update('${item.id}')">编辑</span>
                                 <span class="inform-deletee inform-YC" id="infrom-delete" onclick="showDeleteDiv('${item.id}')">删除</span>
                             </c:if>
                         </div>
                     </div>
                    </li>
                </c:forEach>
            </ul>

            <!--====================================分页===============================-->
        </div>
        <div class="page-paginator">
            <span class="first-page">首页</span>
                <span class="page-index">
                </span>
            <span class="last-page">尾页</span>
        </div>
    </div>
    <!--====================================分页===============================-->


    <!--===============================删除弹出框=======================================-->
    <div id="delete_div" class="inform-popup">
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
    </div>
</div>
</div>
<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp" %>
<!-- 页尾 -->
</body>
</html>
