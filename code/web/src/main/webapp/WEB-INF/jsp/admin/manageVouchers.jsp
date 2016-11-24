<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>抵用券管理</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/mall/manageOrders.css"/>
</head>
<body>

<div id="wrapper">

    <!-- Sidebar -->
    <%@include file="side_bar.jsp" %>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <table class="table table-hover">
                            <caption>生成抵用券</caption>
                            <tbody>
                            <tr>
                                <td>数量</td>
                                <td><input placeholder="数量" type="text"
                                           onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')" value="1" id="count">
                                </td>
                            </tr>
                            <tr>
                                <td>面额</td>
                                <td><input placeholder="面额(单位为分！！！)" onkeyup="this.value=this.value.replace(/\D/g,'')"
                                           onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text"
                                           id="denomination">分
                                </td>
                            </tr>
                            <tr>
                                <td>截止充值日期</td>
                                <td><input type="date" id="expirationTime"></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>
                                    <button class="btn btn-primary" id="generate">确定</button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>

            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <table class="table table-hover">
                            <caption>抵用券列表</caption>
                            <thead>
                            <tr>
                                <th>券号</th>
                                <th>面额</th>
                                <th>用户</th>
                                <th>状态</th>
                            </tr>
                            </thead>
                            <tbody id="voucherList">
                            </tbody>
                        </table>
                    </div>
                </div>

            </div>

            <div class="new-page-links"></div>
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

<script id="voucherListTmpl" type="text/template">
    {{ for(var i in it) { }}
    <tr id="{{=it[i].id}}">
        <td>{{=it[i].number}}</td>
        <td>{{=it[i].denomination}}分</td>
        <td>
            {{ if(it[i].userId != '') { }}
            {{=it[i].userName}}
            {{} }}
        </td>
        <td>{{=it[i].stateInfo}}</td>
        <td>
            <%--<button class="btn btn-info" state="4">发放</button>--%>
            {{if(it[i].state == 3){ }}
            <button class="btn btn-danger" state="5">删除</button>
            {{ } }}
        </td>
    </tr>
    {{ } }}
</script>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/mall/0.1.0/admin/manageVouchers.js');
</script>
</body>
</html>
