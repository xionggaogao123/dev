<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="layout" uri="http://www.dreamlu.net/tags/jsp-layout.tld" %>
<%-- 填充head --%>
<layout:override name="head">
    <title>复兰后台管理</title>
</layout:override>
<%-- 填充content --%>
<layout:override name="content">
    <div class="container">
        <div style="color:#E43838">
            警告：此页面为复兰科技内部页面，仅限复兰科技内部使用
        </div>
        文章
    </div>
</layout:override>
<%-- 填充script --%>
<layout:override name="script">
</layout:override>
<%@ include file="_layout.jsp" %>
