<%--
  Created by IntelliJ IDEA.
  User: yan
  Date: 2016/3/15
  Time: 11:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>走班配置</title>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript" src="/static_new/js/sea.js"></script>
    <script type="application/javascript" src="/static_new/js/modules/core/0.1.0/doT.min.js"></script>
    <script type="application/javascript" src="/static/plugins/pagination/jqPaginator.min.js"></script>
    <script type="application/javascript" src="/static/plugins/angular-1.2.28/angular.min.js"></script>
    <script type="application/javascript">
        $(document).ready(function () {
            init(1);
            $("#valid").click(function () {
                var sid = $("#schoolId").val();
                $.ajax({
                    url: "/zouban/config/findSchool.do",
                    data: {id: sid},
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "200") {
                            alert("验证成功");
                        }
                        else {
                            alert("验证失败");
                        }
                    }
                });
            });
            $("#add").click(function () {
                var sid = $("#schoolId").val();
                var mode = Number($("#mode").val());
                add(sid, mode);
            });
            $("body").on("click", ".delete-sp", function () {
                var id = $(this).attr("cid");
                deleteConf(id);
            });
            /*test("55934c14f6f28b7261c19c63");
             test("55934c14f6f28b7261c19c64");*/
        });
        function add(sid, mode) {
            $.ajax({
                url: "/zouban/config/findSchool.do",
                data: {id: sid},
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.code == "200") {
                        $.ajax({
                            url: "/zouban/config/add.do",
                            data: {schoolId: sid, mode: mode},
                            type: "post",
                            dataType: "json",
                            success: function (data) {
                                if (data.code == "200") {
                                    init(1);
                                }
                                else {
                                    alert("添加失败");
                                }
                            }
                        })
                    }
                    else {
                        alert("学校id不正确");
                    }
                }
            });
        }
        function deleteConf(id) {
            if (confirm("确定删除吗")) {
                $.ajax({
                    url: "/zouban/config/delete.do",
                    data: {id: id},
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "200") {
                            init(1);
                        }
                        else {
                            alert("删除失败");
                        }
                    }
                })
            }
        }

        function init(page) {
            var isInit = true;
            $.ajax({
                url: "/zouban/config/find.do",
                data: {page: page, pageSize: 20},
                type: "post",
                dataType: "json",
                success: function (data) {
                    var interText = doT.template($("#tableTmpJs").text());
                    $("#tableList").html(interText(data));

                    $('.new-page-links').html("");
                    if (data.list.length > 0) {
                        //分页方法
                        $('.new-page-links').jqPaginator({
                            totalPages: Math.ceil(data.count / 20) == 0 ? 1 : Math.ceil(data.count / 20),//总页数
                            visiblePages: 10,//分多少页
                            currentPage: parseInt(page),//当前页数
                            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                            onPageChange: function (n) { //回调函数
                                if (isInit) {
                                    isInit = false;
                                } else {
                                    init(n);
                                }
                            }
                        });
                    }

                }
            })
        }
    </script>
</head>
<body>
<div class="content" style="width:1000px;margin: 30px 30%;">
    <p style="color: red;">此页面仅供复兰内部管理使用，请谨慎配置！</p>
    <input id="schoolId" placeholder="输入学校id" style="margin: 20px 0;">
    <select id="mode">
        <option value="0">无走班</option>
        <option value="1">晋元模式</option>
        <option value="2">格致模式</option>
        <option value="3">长征模式</option>
        <option value="4">株洲模式</option>
    </select>
    <button id="valid">验证学校id</button>
    <button id="add">添加</button>

    <table id="tableList">
    </table>
    <div class="new-page-links"></div>
    <script type="application/template" id="tableTmpJs">
        <tr>
            <td style="width: 200px">学校</td>
            <td style="width: 150px">走班模式</td>
            <td>操作</td>
        </tr>
        {{~it.list:value:index}}
        <tr>
            <td>{{=value.schoolName}}</td>
            <td>{{?value.mode==0}}无走班
                {{??value.mode==1}}晋元模式
                {{??value.mode==2}}格致模式
                {{??value.mode==3}}长征模式
                {{??value.mode==4}}株洲模式
                {{?}}
            </td>
            <td><span cid="{{=value.id}}" class="delete-sp" style="cursor: pointer">删除</span>
            </td>
        </tr>
        {{~}}
    </script>
</div>
</body>
</html>
