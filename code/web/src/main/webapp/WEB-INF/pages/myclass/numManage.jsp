<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-11
  Time: 下午3:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <title>复兰科技--学号管理</title>
    <link rel="stylesheet" href="/static_new/css/myclass/classManange.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript"
            src="/static_new/js/modules/core/0.1.0/jquery-upload/jquery.form.min.js?v=20150719"></script>

    <script src="/static_new/js/modules/core/0.1.0/doT.min.js?v=1"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $(".buu").on("click", function () {
                if ($("#file").val() == "") {
                    alert("请先上传文件");
                }
                else {
                    $(".loading").show();
                    $("#myform").ajaxSubmit({
                        type: "post",
                        url: "/myclass/import.do",

                        success: function (data) {
                            //提交成功后调用
                            if (data == true)
                                alert("导入成功");
                            else {
                                alert("上传失败");
                            }
                            $(".loading").hide();
                        },
                        error: function (XmlHttpRequest, textStatus, errorThrown) {
                            alert("上传失败");
                            $(".loading").hide();
                        }
                    });
                }
            });
        });
    </script>
</head>
<body classid="${classId}">
<%--<div  class="bg-color">--%>
<!--==================start引入头部==================-->
<%@include file="../common_new/head.jsp" %>
<!--====================end引入头部====================-->
<div id="content_main_container">
    <div class="c-num-main">
        <!--==================start引入左边导航==================-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--====================end引入左边导航====================-->
        <!--==================start引入右边广告==================-->
        <%@include file="../common/right.jsp" %>
        <!--====================end引入右边广告====================-->
        <div class="right-border">
            <div class="c-num-right">
                <!--========================头部=============================-->
                <div class="c-right-top">
                    <ul>
                        <li><a href="#" class="cur">学生管理</a></li>
                    </ul>
                </div>
                <!---=====================老师列表==============================-->
                <dl class="c-right-info">
                    <dd class="c-right-c">
                        <em>导入学号及班干部</em>
                    </dd>
                    <dd class="c-right-d">
                        <em>第一步：</em>
                        <button onclick="window.location='/myclass/download.do?classId=${classId}'"
                                class="c-right-bu bu">下载模板
                        </button>
                    </dd>
                    <dd class="c-right-d">
                        <em>第二步：</em>
                        <em>选择要导入的学生学号及班干部</em>
                    </dd>
                    <dd class="c-right-d">
                        <em style="float: left">第三步：</em>
                        <%--<input>--%>
                        <form id="myform" style="float: left"<%--action="/myclass/import.do"--%> method="post"
                              enctype="multipart/form-data">
                            <input type="file" id="file" name="file"/>
                            <input type="hidden" id="classId" name="classId" value="${classId}"/>
                            <%--<button class="c-right-la">浏览</button>--%>
                        </form>
                        <button class="c-right-bu buu">开始导入</button>
                    </dd>
                    <dd class="c-right-c">
                        <em>操作提示</em>
                    </dd>
                </dl>
                <div class="c-right-bottom">
                    <ul class="c-right-ol">
                        <li>请按照以上步骤执行，先下载Excel模板文件，并另存为一个新的Excel文件</li>
                        <li>在新文件中直接输入信息，或将其它填写好的Excel文件中的信息拷贝粘贴到这个新文件中，请注意遵守以下规则：</li>
                    </ul>
                    <ul class="c-right-oo">
                        <li>Excel文件工作薄名称“Sheet1”不能修改</li>
                        <li>请勿修改删除第一行的标题内容</li>
                        <li>请勿直接修改或添加学生姓名</li>
                        <li>批量导入只识别本班学生，导入其他班级将无作用</li>
                        <li>点击“浏览按钮”，选择您整理好的模板文件；</li>
                        <li>点击“开始导入”按钮，完成导入信息操作。</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="../common_new/foot.jsp" %>
</body>
</html>
