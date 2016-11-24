<%--
  Created by IntelliJ IDEA.
  User: fulaan
  Date: 15-7-11
  Time: 下午3:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>复兰科技--导入人员</title>
    <link rel="stylesheet" href="/static_new/css/myschool/classManange.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript"
            src="/static_new/js/modules/core/0.1.0/jquery-upload/jquery.form.min.js?v=20150719"></script>

    <script src="/static_new/js/modules/core/0.1.0/doT.min.js?v=1"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $(".buu").on("click", function () {
                var file = $("#file").val();
                if (file == "") {
                    alert("请选择上传文件");
                } else {
                    var filetype = getFiletype(file);
                    var allowtype =  ["XLS","XLSX"];
                    if ($.inArray(filetype,allowtype) == -1)
                    {
                        alert("请选择正确的上传文件类型");
                        return;
                    }else{
                        $(".loading").show();
                        $("#myform").ajaxSubmit({
                            type: "post",
                            url: "/user/mamager/batchExportPerson.do",
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
                                console.log(XmlHttpRequest);
                                console.log(textStatus);
                                console.log(errorThrown);
                                alert("上传失败");
                                $(".loading").hide();
                            }
                        });
                    }
                }
            });
        });

        function getFiletype(filename)
        {
            var extStart  = filename.lastIndexOf(".")+1;
            return filename.substring(extStart,filename.length).toUpperCase();
        }
    </script>
</head>
<body>
<!--==================start引入头部==================-->
<%@include file="../common_new/head.jsp" %>
<!--====================end引入头部====================-->
<div id="content_main_container">
    <div class="c-num-main">
        <!--==================start引入左边导航==================-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--====================end引入左边导航====================-->
        <!--==================start引入右边广告==================-->
        <!--====================end引入右边广告====================-->
        <div class="right-border">
            <div class="c-img">
                <%--<img src="img/manage-IM-1.png">--%>
                <img src="/img/K6KT/list_3.png"style="margin-right: 5px;float:left;">年级列表
            </div>
            <div class="c-num-right">
               <%-- <!--========================头部=============================-->
                <div class="c-right-top">
                    <ul>
                        <li><a href="#" class="cur">学号管理</a></li>
                    </ul>
                </div>--%>
                <!---=====================老师列表==============================-->
                <dl class="c-right-info">
                    <dd class="c-right-c">
                        <em>导入人员</em>
                    </dd>
                    <dd class="c-right-d">
                        <em>第一步：</em>
                        <button onclick="window.location='/user/mamager/downloadPersonExcelModel.do'" class="c-right-bu bu">下载模板</button>
                    </dd>
                    <dd class="c-right-d">
                        <em>第二步：</em>
                        <em>选择要导入的师生信息</em>
                    </dd>
                    <dd class="c-right-d">
                        <em style="float: left">第三步：</em>
                        <form id="myform" style="float: left" method="post" enctype="multipart/form-data">
                            <input type="file" id="file" name="file"/>
                            <%--<button class="c-right-la">浏览</button>--%>
                        </form>
                        <button class="c-right-bu buu">开始导入</button>

                    </dd>
                    <dd class="c-right-c">
                        <em>操作提示</em>
                    </dd>
                </dl>
                <div class="c-right-bottom">
                    <ul class="c-right-ol c-bottom">
                        <p>录入老师信息的注意事项：</p>
                        <li>首行的12个字段不得进行删除或修改；</li>
                        <li>表格中不得有空格，字体统一为宋体，大小12；</li>
                        <li>职工号为选填，非必填</li>
                        <li>学校如果不提供性别信息，后台会做随机处理；</li>
                        <li>权限分老师和校领导两种；</li>
                        <li>职务可以不填，最多15个字符；</li>
                        <li>学科名称，年级名称和班级名称的命名必须一致，注意括号的中英文格式，要统一；</li>
                        <li>年级代码填写数字1-12,1代表小学一年级，12代表高中三年级</li>
                        <li>班主任用“是”表示，非班主任不要填任何字符，且一个班级只能记录一位班主任；</li>
                        <li>教研组长用所教学科名称表示；</li>
                        <li>年级组长用所教年级名称表示，且一个年级只能记录一位年级组长；</li>
                        <li>备课组长用“是”表示，非备课组长不要填写任何字符；</li>
                        <li>一个老师如果带多个班级，须分行列清，不得集中在班级名称的一个单元格中；</li>
                        <li>填完所有数据后，把红色注释内容行全部删掉。</li>
                    </ul>
                    <ul class="c-right-ol">
                        <p>录入学生信息的注意事项：</p>
                        <li>首行的6个字段不得进行删除或修改；</li>
                        <li>表格中不得有空格，字体统一为宋体，大小12；</li>
                        <li>学籍号和学号选填，非必填；</li>
                        <li>年级代码填写数字1-12,1代表小学一年级，12代表高中三年级；</li>
                        <li>班级名称必须和老师信息表中班级名称一致，注意括号的中英文格式，要统一；</li>
                        <li>学校如果不提供性别信息，后台会做随机处理；</li>
                        <li>填完所有数据后，把红色注释内容行全部删掉；</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%@include file="../common_new/foot.jsp" %>
</body>
</html>
