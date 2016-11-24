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
        <div>
            <p>日期：<input type="text" id="datepicker"></p>
            <button id="download">下载</button>
            <button id="create">生成id</button>
            社区id<input id="generateQr" ><button id="send">生成二维码</button>
            <button id="resetLogo">重置logo</button>
        </div>
    </div>
</layout:override>
<%-- 填充script --%>
<layout:override name="script">
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
    <script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
    <script>
        $(function() {

            $( "#datepicker" ).datepicker();
            $( "#datepicker" ).datepicker("option","dateFormat", "yy-mm-dd");
            $('#download').click(function () {
                alert($('#datepicker').val());
                window.location = "/loginInfo/downloadXls?date=" + $('#datepicker').val();
            });

            $('#create').click(function (){
                $.ajax({
                    type: "GET",
                    url: '/community/generateSeq.do',
                    success: function (result) {
                        alert('success');
                    }
                });
            });

            $('#send').click(function () {
                var parm = {
                    searchId:$('#generateQr').val()
                }
                $.ajax({
                    type: "GET",
                    url: '/community/generateQrUrl.do',
                    data:parm,
                    success: function (result) {
                        alert('success');
                    }
                });
            });

            $('#resetLogo').click(function () {
                $.ajax({
                    type: "GET",
                    url: '/community/resetLogo.do',
                    data:parm,
                    success: function (result) {
                        alert('success');
                    }
                });
            });

        });

    </script>
</layout:override>
<%@ include file="_layout.jsp" %>
