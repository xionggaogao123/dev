<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="layout" uri="http://www.dreamlu.net/tags/jsp-layout.tld" %>
<%-- 填充head --%>
<layout:override name="head">
    <title>复兰后台管理</title>
</layout:override>
<%-- 填充content --%>
<layout:override name="content">
    <div id="r-result">请输入页号:<input type="text" id="suggestId" size="20" style="width:150px;" /></div>
    <button id="submit">生成经纬度</button>
    <button id="generateIndex">生成索引</button>

    <div id="r-result-s">对区域对应的数据的处理:<input type="text" id="parentId" size="20" style="width:150px;" /></div>
    <button id="replaceRegion">数据处理</button>

    <div id="r-result-ss">处理残留未处理水印图片的数据:<input placeholder="请输入Id(用@隔开)" type="text" id="instituteIds" style="width:350px;" /></div>
    <button id="batchImages">处理残留图片</button>

    <div id="r-result-sss">一些数据用默认图片覆盖:<input placeholder="请输入Id(用@隔开)或者名字(用$隔开),注意:只传一个时传Id" type="text" id="idOrNames" style="width:350px;" /></div>
    <button id="defaultImage">默认图片覆盖处理</button>
</layout:override>
<%-- 填充script --%>
<layout:override name="script">
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css">
    <script src="//apps.bdimg.com/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
    <%--<script type="text/javascript"--%>
            <%--src="http://api.map.baidu.com/api?v=2.0&ak=TzFCVsUAf4RzyoOdgZ5tB10fASv5Dswy"></script>--%>
    <script type="text/javascript">

        $(function(){

            $('#batchImages').click(function () {
                var instituteIds=$('#instituteIds').val();
                $.ajax({
                    type: "GET",
                    data: {instituteIds:instituteIds},
                    url: '/train/batchImages.do',
                    async: false,
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    success: function (resp) {
                        if(resp.code=="200"){
                            alert("数据处理成功了!");
                        }
                    }
                });
            });


            $('#defaultImage').click(function () {
                var idOrNames=$('#idOrNames').val();
                if(idOrNames==""||idOrNames==undefined){
                    alert("请输入信息");
                    return false;
                }
                $.ajax({
                    type: "GET",
                    data: {idOrNames:idOrNames},
                    url: '/train/batchDefaultImage.do',
                    async: false,
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    success: function (resp) {
                        if(resp.code=="200"){
                            alert("数据处理成功了!");
                        }
                    }
                });
            });

            $('#replaceRegion').click(function(){
                var parentId=$('#parentId').val();
                $.ajax({
                    type: "GET",
                    data: {},
                    url: '/train/replaceRegion/'+parentId,
                    async: false,
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    success: function (resp) {
                        if(resp.code=="200"){
                            alert("数据处理成功了!");
                        }
                    }
                });
            });


            $('#generateIndex').click(function(){
                $.ajax({
                    type: "GET",
                    data: {},
                    url: '/train/create2dsphereIndex.do',
                    async: false,
                    dataType: "json",
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    success: function (resp) {
                        if(resp.code=="200"){
                            alert("生成索引成功了!");
                        }
                    }
                });
            });


            $('#submit').click(function(){
                var page=$('#suggestId').val();
                $.ajax({
                    type: "GET",
                    data: {page:page},
                    url: '/train/instituteData.do',
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
