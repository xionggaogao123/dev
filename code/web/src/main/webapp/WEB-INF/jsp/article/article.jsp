<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <style>
        .lunt-index-hide {
            display: none;
        }
    </style>
</head>
<body>
<%@include file="../common/head.jsp" %>
<div class="cont-article">
    <div class="article-cc">
        <h1>
            ${title}
        </h1>
        <img src="${image}">
        <p class="p-p">
            ${article}
        </p>
    </div>
</div>
<%@include file="../common/login.jsp" %>
<%@include file="../common/footer.jsp" %>
</body>
</html>
