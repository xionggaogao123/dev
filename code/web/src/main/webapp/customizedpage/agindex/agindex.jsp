<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
        <meta name="renderer" content="webkit">
        <title>复兰科技 K6KT-快乐课堂</title>
        <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/static/css/newmain.css"/>
        <script type='text/javascript' src='/static/js/jquery.min.js'></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script type='text/javascript' src='/static/js/newmain.js'></script>
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    </head>
    <body>
    <%@ include file="/WEB-INF/pages/common/ypxxhead.jsp"%>
        
        <div class="main-container-ag">
            <div class="login-bar"></div>
            <div class='title-bar-container' style="height:105px;">
                <img class="title-logo" src="/img/K6KT-LOGO.png">
            </div>
            <div class='content-container'>
                <img class="main-content" src="/img/K6KTcontent.png">
                <a href='/' class="online-btn">
                    <img src="/img/K6KT/online-btn.png">
                </a>
                <a href='http://10.20.18.5/cjcx/index.aspx' class="manage-btn">
                    <img src="/img/K6KT/manage-btn.png">
                </a>
            </div>
        </div>
        <!-- 页尾 -->
    <%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>
        <!-- 页尾 -->
    </body>
</html>