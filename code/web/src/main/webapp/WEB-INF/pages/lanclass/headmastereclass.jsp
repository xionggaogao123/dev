<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>复兰科技-互动课堂</title>
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link href="/static_new/css/lanclass/kehou.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static/css/dialog.css"/>
    <meta charset="utf-8">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript">
        function skipCheckProtocol(sender) {
            MessageBox("如果试卷编辑器未启动，请点击 <a href=\"/upload/resources/FulaanExamEditor.exe\" style=\"color:red;\">这里</a> 下载安装。", -1);
            location.href = sender.getAttribute('link');
        }
    </script>
</head>
<body>


<!--#head-->
<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

    <!--#content-->
    <div id="content" class="clearfix">

        <!--.col-left-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--/.col-left-->

        <!--.col-right-->
        <!--广告-->
        <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>

        <!--.col-right-->
        <div class="col-right">
            <!--.tab-col右侧-->
            <div class="tab-col">
                <div class="kehou-main">
                    <div class="kehou-top">
                        <select id="gradeId" name="gradeId">
                            <option value="">全年级</option>
                            <c:forEach items="${gradelist}" var="grade">
                                <option value="${grade.id}">${grade.name}</option>
                            </c:forEach>
                        </select>
                        <input id="teaName" name="teaName" value="">
                        <span id="searchBtn">搜索</span>
                    </div>
                    <span onclick="skipCheckProtocol(this)" class="uploadify-button" link="FulaanExamEditor://"></span>
                    <div class="kehou-middle">
                        <div>教师列表</div>
                        <table>
                            <tbody class="sub-info-list">
                                <%--<tr>
                                    <td width="40">1</td>
                                    <td width="730">
                                        <em>张小虎</em>
                                        <span>最后使用时间：2016-6-30</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="40">1</td>
                                    <td width="730">
                                        <em>张小虎</em>
                                        <span>最后使用时间：</span>
                                    </td>
                                </tr>--%>
                            </tbody>

                            <!--.list-info-->
                            <script type="text/template" id="tea_templ">
                                {{ if(it.rows.length>0){ }}
                                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                                        {{var obj=it.rows[i];}}

                                        <tr {{ if(obj.lastTime!=''){ }} uid="{{=obj.teaId}}" style="width: 82px;height: 56px;cursor: pointer;" class="searchDetail" {{}}}>
                                            <td width="40">{{=i+1}}</td>
                                            <td width="730">
                                                <em>{{=obj.teaName}}</em>
                                                {{ if(obj.lastTime==''){ }}
                                                    <span>无使用记录</span>
                                                {{}else{}}
                                                    <span>最后使用时间：{{=obj.lastTime}}</span>
                                                {{ } }}
                                            </td>
                                        </tr>
                                    {{ } }}
                                {{ } }}
                            </script>

                            <!--/.list-info-->
                        </table>
                    </div>

                    <div class="new-page-links" id="pageDiv"></div>
                </div>
            </div>
               
        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->
</div>
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015070101"></script>
<script>
    seajs.use('headmastereclass',function(headmastereclass){
        headmastereclass.init();
    });
</script>

</body>
</html>