<%--
  Created by IntelliJ IDEA.
  User: Wangkaidong
  Date: 2016/7/13
  Time: 10:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>走班模式配置</title>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script src="/static_new/js/modules/core/0.1.0/jquery.min.js"></script>
    <script src="/static_new/js/modules/core/0.1.0/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>

    <style>
        td span {
            margin-left: 5px;
        }

        .light span {
            color: deepskyblue;
            cursor: pointer;
        }

        .cur {
            cursor: pointer;
        }

        .readonly {
            background-color: #ccc;
        }

        .modeWindow {
            display: none;
            position: absolute;
            top: 25%;
            left: 45%;
            border: 1px #ccc solid;
            width: 340px;
            min-height: 300px;
            background-color: #fff;
            z-index: 9999;
        }

        .modeWindow .title {
            width: 100%;
            height: 40px;
            background-color: #666;
            color: #fff;
            padding: 10px;
        }

        .modeWindow .main {
            width: 100%;
            margin: 10px auto;
            padding: 15px 30px;
        }

        .modeWindow .main > dd {
            margin: 10px auto;
        }

        .modeWindow .main > dd > label {
            width: 70px;
            text-align: right;
        }

        .modeWindow .main > dd > input, span {
            margin-left: 20px;
            vertical-align: middle;
            font-size: 100%;
            outline: 0;
            font-family: 'Microsoft YaHei',Arial,sans-serif;
        }

        .modeList {
            display: inline-block;
            min-height: 100px;
        }

        .modeList > input {
            margin: 0 5px 0 20px;
        }

        .btnBlock {
            margin: 25px 0 30px 80px !important;
        }
    </style>
</head>
<body>
<%--学校模式列表--%>
<div class="container-fluid">
    <div class="row"></div>
    <div class="row">
        <div class="col-md-3"></div>
        <div class="col-md-6">
            <div style="margin-top: 20px;">
                <button class="btn btn-primary" id="addSchool">新增</button>
                <div style="float: right">
                    <input type="text">
                    <button id="search" class="btn btn-info btn-xs" style="margin-left: 10px;">搜索</button>
                </div>

            </div>
            <div style="margin-top: 50px;">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>学校</th>
                        <th>模式</th>
                        <th>年级</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="schoolListCtx"></tbody>
                </table>
                <script id="schoolListTmpl" type="application/template">
                    {{~it:value:index}}
                    <tr>
                        <td>{{=value.schoolName}}</td>
                        <td>
                            {{~value.mode:value2:index2}}
                            <span mode="{{=value2.value}}">{{=value2.name}}</span>
                            {{~}}
                        </td>
                        <td class="light">
                            {{~value.gradeList:grade:index3}}
                            <span gid="{{=grade.idStr}}" gmode="{{=grade.value}}" schoolModeId="{{=value.id}}">{{=grade.name}}</span>
                            {{~}}
                        </td>
                        <td>
                            <span class="cur edit" schoolModeId="{{=value.id}}">编辑</span> |
                            <span class="cur del" schoolModeId="{{=value.id}}">删除</span>
                        </td>
                    </tr>
                    {{~}}
                </script>
            </div>

            <div class="new-page-links"></div>
        </div>
        <div class="col-md-3"></div>
    </div>
</div>

<%--背景--%>
<div class="bg"></div>

<%--学校模式配置--%>
<div id="schoolWindow" class="modeWindow">
    <div class="title">
        <label>学校走班模式配置</label>
    </div>
    <dl class="main">
        <dd>
            <label>学校id</label>
            <input id="schoolId" type="text">
        </dd>
        <dd>
            <label>学校名称</label>
            <input id="schoolName" type="text" class="readonly" readonly>
        </dd>
        <dd>
            <label>模式</label>

            <div class="modeList">
                <input name="mode" type="checkbox" value="0" checked><span>无走班</span><br>
                <input name="mode" type="checkbox" value="1"><span>3个逻辑位置模式</span><br>
                <input name="mode" type="checkbox" value="2"><span>虚拟班模式</span><br>
            </div>
        </dd>
        <dd class="btnBlock">
            <button id="submitSc" class="btn btn-primary" schoolModeId="">保存</button>
            <button id="closeSc" class="btn">取消</button>
        </dd>
    </dl>
</div>

<%--年级模式配置--%>
<div id="gradeWindow" class="modeWindow">
    <div class="title">
        <label>走班模式配置</label>
    </div>
    <dl class="main">
        <dd>
            <label>年级</label>
            <span id="grade"></span>
        </dd>
        <dd>
            <label>模式</label>
            <div id="gradeModeListCtx" class="modeList"></div>
        </dd>
        <script id="gradeModeListTmpl" type="application/template">
            {{~it:value:index}}
            <input type="radio" name="gradeMode" value="{{=value.value}}"><span>{{=value.name}}</span>
            {{?index % 1 == 0}}<br>{{?}}
            {{~}}
        </script>
        <dd class="btnBlock">
            <button id="setGrade" class="btn btn-primary" gid="" schoolModeId="">保存</button>
            <button id="closeGr" class="btn">取消</button>
        </dd>
    </dl>
</div>


<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('zoubanModeConfig', function (modeConfig) {
        modeConfig.init();
    });
</script>
</body>
</html>
