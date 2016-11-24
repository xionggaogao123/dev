<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>运费模板设置</title>

    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="/static/css/expressTemplate.css" rel="stylesheet">

</head>
<body>
<div id="wrapper">
    <!-- Sidebar -->
    <%@include file="side_bar.jsp" %>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="container">
                    <div class="col-md-3"></div>
                    <div class="col-md-6 top">
                        <div style="margin-top: 20px">
                            <button class="btn btn-add" id="addTemp" onclick="addTemp()">新增运费模板</button>
                        </div>
                        <script id="expTempListTemp" type="application/template">
                            <ul class="list-group">
                                {{~it:value:index}}
                                <li class="list-group-item">
                                    <span>模板名称：{{=value.name}}</span>
                                    <a href="#" class="editTemp" onclick="editTemp('{{=value.id}}')">详情/编辑</a>
                                    <a href="#" class="deleteTemp" onclick="removeTemp('{{=value.id}}')">删除</a>
                                </li>
                                {{~}}
                            </ul>
                        </script>
                        <div id="expTempListCtx" style="margin-top: 20px"></div>
                        <div class="new-page-links"></div>
                    </div>
                    <div class="col-md-3"></div>
                </div>

                <%--运费模板--%>
                <div class="expTemp">
                    <form role="form">
                        <div class="form-group">
                            <input type="hidden" id="_id" name="id">

                            <div class="col-md-2">
                                <label for="name">模板名称：</label>
                            </div>
                            <div class="col-md-10">
                                <input type="text" class="form-control" id="name" name="name" maxlength="30"
                                       style="width: 300px" placeholder="请输入模板名称">
                            </div>
                        </div>
                        <div>
                            <table class="table table-bordered" style="TABLE-LAYOUT:fixed;WORD-BREAK:break-all">
                                <caption>
                                    <button type="button" class="btn btn-add" id="addRow">增加行</button>
                                </caption>
                                <thead>
                                <tr>
                                    <th>运送到</th>
                                    <th>首件（元/件）</th>
                                    <th>续费（元/件）</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <script id="tbodyTmpl" type="application/template">
                                    {{~it:value:index}}
                                    <tr>
                                        <td>
                                            <span>{{=value.zoneName}}</span>
                                            <input name="zoneName" type="hidden" value="{{=value.zoneName}}">
                                            <input name="zoneNo" type="hidden" value="{{=value.zoneNo}}">
                                            <a href="#" class="editAddr">编辑</a>
                                        </td>
                                        <td><input name="firstPrice" type="text" value="{{=value.firstPrice}}"
                                                   maxlength="5" required
                                                   onkeyup="if(isNaN(value))execCommand('undo')"
                                                   onafterpaste="if(isNaN(value))execCommand('undo')"></td>
                                        <td><input name="addOnePrice" type="text" value="{{=value.addOnePrice}}"
                                                   maxlength="5" required
                                                   onkeyup="if(isNaN(value))execCommand('undo')"
                                                   onafterpaste="if(isNaN(value))execCommand('undo')"></td>
                                        <td><a href="#" class="deleteRow">删除</a></td>
                                    </tr>
                                    {{~}}
                                </script>
                                <tbody></tbody>
                            </table>
                        </div>
                        <div class="span7 text-center">
                            <button id="submit" type="button" class="btn btn-primary">提交并返回</button>
                            <button id="cancel" type="button" class="btn btn-primary">取消</button>
                        </div>
                    </form>
                </div>

                <%--送货地区选择--%>
                <div class="bg"></div>
                <div id="addWin">
                    <div class="hide-top">选择省份 <em class="hide-x">×</em></div>
                    <div id="provinceList"></div>
                    <script type="application/template" id="provinceTempJs">
                        {{~it:value:index}}
                        <label style="width: 90px">
                            {{?value.choose==1}}
                            <input class="states" type="checkbox" name="check_name" value="{{=value.zn}}"
                                   disabled/><span
                                style="color:#888;">{{=value.znm}}</span>
                            {{?? value.cur==1}}
                            <input class="states" type="checkbox" name="check_name" value="{{=value.zn}}" checked/>{{=value.znm}}
                            {{??}}
                            <input class="states" type="checkbox" name="check_name" value="{{=value.zn}}"/>{{=value.znm}}
                            {{?}}
                        </label>
                        {{~}}
                    </script>
                    <button id="save">保存</button>
                </div>
            </div>
        </div>
    </div>
    <!-- /#page-content-wrapper -->

</div>
<%--运费模板列表--%>

<link href="/static/css/side_bar.css" rel="stylesheet">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<script>
    seajs.use('expressTemplate', function (expressTemplate) {
        expressTemplate.init(1);
    });
</script>
</body>
</html>
