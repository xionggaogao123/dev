
<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2015/6/26
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/educationbureau/eduManageList.css?v=2015041602" rel="stylesheet" />

</head>
<body>


<!--#head-->
<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">
    <!--.tab-col-->
    <div class="tab-col" style="margin-top:20px;">

        <div class="tab-head clearfix">
            <ul>
                <li class="cur"><a href="javascript:;">教育局管理</a></li>
            </ul>
        </div>

        <div class="tab-main txt-info" style="margin-top:5px;">
            &nbsp;&nbsp;
            <label>教育局名称：</label>
            <input type="text" id="eduName" name="eduName"/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <label>省、直辖市：</label>
            <select id="province" name="province">
                <option value="">请选择</option>
                <c:forEach var="item" items="${regions}">
                    <option value="${item.id}">${item.name}</option>
                </c:forEach>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <label>市、区：</label>
            <select id="city" name="city">
                <option value="">请选择</option>
            </select>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <%--<label>区、县：</label>
            <select id="county" name="county">
            </select>--%>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <button type="button" class="search-btn">查询</button>&nbsp;&nbsp;&nbsp;&nbsp;
            <button type="button" class="add-btn">添加教育局</button>
        </div>

    </div>
    <!--/.tab-col-->

    <!--/.info-list-->
    <div class="info-list clearfix">
        <!--.info-main-->
        <div class="info-main">

            <table width="100%" border="1">
                <thead>
                <tr>
                    <th width="5%"><em>序号</em></th>
                    <th width="25%"><em>教育局Logo</em></th>
                    <th width="25%"><em>教育局名称</em></th>
                    <th width="30%"><em>地区</em></th>
                    <th width="20%"><em>操作</em></th>
                </tr>
                </thead>
                <tbody class="table">

                </tbody>
                <script type="text/template" id="table">
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{ var obj=it.rows[i];}}
                    <tr id="{{=obj.id}}" class="tr-class" align="center" style="height: 40px;">
                        <td>{{=i+1}}</td>
                        <td><img alt="" src="{{=obj.educationLogo}}" style="width: 170px;height:35px;"></td>
                        <td>{{=obj.educationName}}</td>
                        <td>{{=obj.provinceName}}{{=obj.cityName}}{{=obj.countyName}}</td>
                        <td>
                            <a class="infrom-edit" eduId="{{=obj.id}}">编辑</a>
                            <a class="infrom-delete" eduId="{{=obj.id}}">删除</a>
                            {{ if(obj.openCloud==0){ }}
                                <a class="infrom-openCloud" eduId="{{=obj.id}}">开通云平台</a>
                            {{}else if(obj.openCloud==1){ }}
                                <a class="infrom-closeCloud" eduId="{{=obj.id}}">关闭云平台</a>
                            {{ } }}
                        </td>
                    </tr>
                    {{ } }}
                </script>
            </table>

            <!--.page-links-->
            <%--<div style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:visible;">
                <div id="example"></div>
            </div>--%>
            <div class="page-paginator">
                <span class="first-page">首页</span>
                        <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                        </span>
                <span class="last-page">尾页</span>

            </div>
            <!--/.page-links-->

        </div>
        <!--/.info-main-->

    </div>
    <!--/.info-list-->
</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('eduManageList',function(eduManageList){
        eduManageList.init();
    });
</script>
</body>
</html>