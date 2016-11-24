<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <title>复兰科技--走班选课</title>
    <link rel="stylesheet" href="/static_new/css/myclass/classManange.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/common/role.js"></script>
    <script type="text/javascript"
            src="/static_new/js/modules/core/0.1.0/jquery-upload/jquery.form.min.js?v=20150719"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            getGradeList();
            getTermList();
            $(".buu").on("click", function () {
                if ($("#file").val() == "") {
                    alert("请先上传文件");
                }
                else {
                    $("#myform").ajaxSubmit({
                        type: "post",
                        url: "/paike/importInterestExcel.do",
                        success: function (data) {
                            //提交成功后调用
                            if (data.result=="success")
                                alert("导入成功");
                            else {
                                if(data.line=="0")
                                {
                                    alert("导入失败，失败原因："+data.reason);
                                }
                                else{
                                    alert("导入失败，失败原因："+data.reason+",错误位置："+data.line+"行");
                                }
                            }
                        },
                        error: function (XmlHttpRequest, textStatus, errorThrown) {
                            alert("导入失败");
                        }
                    });
                }
            });
            $("body").on("change", "#gradeList", function () {
                $("#gradeId").val($("#gradeList").val());
            });
            $("body").on("change", "#yearList", function () {
                $("#year").val($("#yearList").val());
            });
            $(".download").on("click", function () {
                window.location = '/paike/exportInterestExcel.do?gradeId=' + $("#gradeId").val()+'&year='+
                encodeURI(encodeURI($("#yearList").val()));
            });

        });
        function getGradeList() {
            $.ajax({
                url: "/course/getGradeList.do",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var value = "";
                    for (var i = 0; i < data.length; i++) {
                        value += "<option value='" + data[i].id + "'>" + data[i].name + "</option>";
                    }
                    $("#gradeList").empty();
                    $("#gradeList").append(value);
                    $("#gradeId").val(data[0].id);
                }
            })
        }
        function getTermList() {
            $.ajax({
                url: "/paike/getAllYear.do",
                type: "post",
                dataType: "json",
                success: function (data) {

                    var value = "";
                    for (var i = 0; i < data.length; i++) {
                        value += "<option value='" + data[i] + "'>" + data[i] + "</option>";
                    }
                    $("#yearList").empty();
                    $("#yearList").append(value);
                    $("#year").val(data[0]);
                }
            })
        }
    </script>
    <style>
        .title
        {
            margin-top: 20px;
            font-size: 20px;
            line-height: 49px;
        }
        .notice-detail
        {
            font-size: 15px;
            line-height: 20px;
        }
    </style>
</head>
<body>
<%@include file="../../common_new/head.jsp" %>
<!--====================end引入头部====================-->
<div id="content_main_container">
    <div class="c-num-main">
        <!--==================start引入左边导航==================-->
        <%@ include file="../../common_new/col-left.jsp" %>
        <!--====================end引入左边导航====================-->
        <!--==================start引入右边广告==================-->
        <%--<%@include file="../../common/right.jsp" %>--%>
        <!--====================end引入右边广告====================-->
        <div class="right-border">
            <div class="c-num-right">
                <!--========================头部=============================-->

                <!---=====================老师列表==============================-->
                <dl class="c-right-info">
                    <dd style="margin-bottom: 40px;">
                        <a onclick="history.back(-1);">< 返回</a>
                    </dd>
                    <dd class="c-right-c">
                        <em>导入兴趣班学生选课数据</em>
                    </dd>

                    <span>年级列表</span>
                    <select id="gradeList" style="width:300px;height: 25px;border: 1px solid #000;">
                    </select>
                    <span>学年列表</span>
                    <select id="yearList" style="width:300px;height: 25px;border: 1px solid #000;">
                    </select>

                    <form id="myform" style="margin-top: 50px;" method="post"
                          enctype="multipart/form-data">
                        <input type="file" id="file" name="file"/>
                        <input type="hidden" id="gradeId" name="gradeId"/>
                        <input type="hidden" id="year" name="year"/>
                        <%--<button class="c-right-la">浏览</button>--%>
                    </form>
                    <button class="c-right-bu download" style="margin-top: 20px;float: none!important;">下载模板</button>
                    <button class="c-right-bu buu" style="margin-top: 20px;margin-right: 400px;">开始导入</button>
                    <div>
                        <div class="title">导入数据说明：</div>
                        <div class="notice-detail">
                            1.导入数据请严格按照模板，请勿改变用户名和课程名。
                        </div>
                        <div class="notice-detail">
                            2.学生选择的课程请在对应地方填写1，未选择请空置。
                        </div>
                        <div class="notice-detail">
                            3.导入时请注意选择对应的年份和年级。
                        </div>
                        <div class="notice-detail">
                            4.请注意每门课的选课上限。
                        </div>
                    </div>
                </dl>

            </div>
        </div>
    </div>
</div>
<%@include file="../../common_new/foot.jsp" %>

</body>
</html>
