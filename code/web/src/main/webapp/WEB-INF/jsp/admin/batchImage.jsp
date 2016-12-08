<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="layout" uri="http://www.dreamlu.net/tags/jsp-layout.tld" %>
<%-- 填充head --%>
<layout:override name="head">
    <title>复兰后台管理</title>
</layout:override>
<%-- 填充content --%>
<layout:override name="content">
    <div id="r-result">请输入:<input type="text" id="suggestId" size="20" style="width:150px;" /></div>
    <button id="submit">提交</button>
</layout:override>
<%-- 填充script --%>
<layout:override name="script">
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
    <script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
    <%--<script type="text/javascript"--%>
    <%--src="http://api.map.baidu.com/api?v=2.0&ak=TzFCVsUAf4RzyoOdgZ5tB10fASv5Dswy"></script>--%>
    <script type="text/javascript">

        $(function(){
            $('#submit').click(function(){
                var page=$('#suggestId').val();
                $.ajax({
                    type: "GET",
                    data: {page:page},
                    url: '/train/batchDealImage.do',
                    async: false,
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    success: function (resp) {
                        if(resp.code=="200"){
                            alert("成功了!");
                        }
                    }
                });
            });
        })

    </script>
</layout:override>
<%@ include file="_layout.jsp" %>
