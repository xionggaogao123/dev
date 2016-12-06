<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="layout" uri="http://www.dreamlu.net/tags/jsp-layout.tld" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/community/community.css">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <title>社区</title>
    <layout:block name="head">
        <%--重写的head --%>
    </layout:block>
</head>
<body>

<div id="wrapper">

    <!-- Sidebar -->
    <%@include file="../common/communityHead.jsp" %>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <layout:block name="content">
                <%--主体部分 --%>
            </layout:block>
        </div>
    </div>
    <!-- /#page-content-wrapper -->

</div>

<layout:block name="script">
    <%--脚本部分--%>
</layout:block>
</body>
</html>
