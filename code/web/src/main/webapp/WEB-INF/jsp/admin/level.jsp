<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <style type="text/css">
        table {
            border-collapse: collapse;
        }

        .table-head {
            width: 795px;
            margin: 30px auto 0;
            height: 40px;
            line-height: 40px;
            color: #0088cc;
        }

        .add-new {
            padding: 3px 10px;
            background: #0088cc;
            color: #fff;
            border: none;
            border-radius: 4px;
            float: right;
            margin: 7px 0 0 0;
            cursor: pointer;
        }

        .user-table {
            border: 1px solid #e5e5e5;
            margin: 0 auto;
        }

        .user-table .th1 {
            width: 260px;
        }

        .user-table .th2 {
            width: 260px;
        }

        .user-table .th3 {
            width: 160px;
        }

        .user-table .th4 {
            width: 100px;
        }

        .user-table th {
            font-size: 14px;
            line-height: 34px;
            height: 34px;
            border: 1px solid #e5e5e5;
            background: #f3f3f3;
        }

        .user-table td {
            border: 1px solid #e5e5e5;
            line-height: 34px;
            height: 34px;
            font-size: 13px;
            text-align: center;
        }

        .user-table .input1 {
            height: 22px;
            line-height: 22px;
            border: 1px solid #e5e5e5;
            width: 100px;
        }

        .user-table .input2 {
            height: 22px;
            line-height: 22px;
            border: 1px solid #e5e5e5;
            width: 60px;
        }

        .user-table .span-edit {
            color: #0088cc;
            cursor: pointer;
        }
    </style>
</head>
<body>

<div id="wrapper">

    <!-- Sidebar -->
    <%@include file="side_bar.jsp" %>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="table-head">
                会员等级管理
                <button class="add-new">添加新等级</button>
            </div>
            <table class="user-table table table-bordered table-hover">
                <thead>
                <tr>
                    <th class="th1">组头衔</th>
                    <th class="th2">积分介于</th>
                    <th class="th3">星星数</th>
                    <th class="th4">操作</th>
                </tr>
                </thead>
                <tbody id="FLevel">
                </tbody>
            </table>

            <div id="level" style="display: none;">
                论坛Id：<input type="text" name="id" disabled="disabled" id="levelId"><br/>
                论坛等级名称：<input type="text" name="level" id="levelStr"><br/>
                起始经验值：<input type="text" name="startLevel" id="startLevel"><br/>
                结束经验值：<input type="text" name="endLevel" id="endLevel"><br/>
                星星数：<input type="text" name="stars" id="stars"><br/>
                <button id="submit">提交</button>
                <button id="cancel">取消</button>
            </div>

        </div>
    </div>
    <!-- /#page-content-wrapper -->

</div>

<link href="/static/css/side_bar.css" rel="stylesheet">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<script type="text/template" id="FLevelTml">
    {{~it:value:index}}
    <tr>
        <td><input type="text" class="input1" value="{{=value.level}}"></td>
        <td><input type="text" class="input2" value="{{=value.startLevel}}"> ~ <input type="text" class="input2"
                                                                                      value="{{=value.endLevel}}"></td>
        <td><input type="text" class="input2" value="{{=value.stars}}"></td>
        <td><span class="span-edit editSpan" value="{{=value.id}}">编辑</span>|<span class="span-edit deleteSpan"
                                                                                   value="{{=value.id}}">删除</span></td>
    </tr>
    {{~}}
</script>
<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/admin/level.js', function (level) {
        level.init();
    });
</script>
</body>
</html>
