<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <head>
        <!-- Basic Page Needs-->
        <meta charset="utf-8">
        <link rel="dns-prefetch" href="//source.ycode.cn" />
        <title>复兰科技-学生评价系统</title>
        <meta name="description" content="">
        <meta name="author" content="" />
        <meta name="copyright" content="" />
        <meta name="keywords" content="">
        <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
        <!-- css files -->
        <!-- Normalize default styles -->
        <link href="/static_new/css/reset.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="/static_new/css/indicator/evaluateManage.css">
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <!-- jquery artZoom4Liaoba styles -->
        <!-- Custom styles -->
    </head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <!--.col-right-->
    <div class="col-right">
        <div class="evaluate-con">
            <div class="clearfix evaluate-head">
                <h3 class="grow-col-stu">学生评价</h3>
            </div>
            <div class="main" id="main1">
                <%--<c:if test="${!isHeadMaster}">--%>
                <div class="new-add tab-main-right">
                    <span>新建评价</span>
                </div>
                <%--</c:if>--%>
                <div class="evaluate-select">
                    <input id="name" name="name" value="">
                    <button id="searchBtn" name="searchBtn">查询评价</button>
                </div>
                <table class="evaluate-table">
                    <tr>
                        <th width="255">评价名称</th>
                        <th width="255">评价发起人</th>
                        <th width="255">日期</th>
                        <th width="255">操作</th>
                    </tr>
                    <tbody class="evaluate-data">

                    </tbody>
                </table>
                <script type="text/template" id="j-tmpl">
                    {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{var obj=it.rows[i];}}
                    <tr id="{{=obj.id}}">
                        <td width="255" class="evaluate-name">
                            <a title="{{=obj.name}}" class="evaluate-a">{{=obj.name}}</a>
                        </td>
                        <td width="255">
                            {{=obj.createrName}}
                        </td>
                        <td width="255">
                            {{=obj.createDate}}
                        </td>
                        <td width="255">
                            {{if(obj.isHandle){}}
                                <a class="evaluate-edit evaluate-a">编辑</a>
                                <a class="evaluate-del evaluate-a">删除</a>
                            {{}}}
                        </td>
                    </tr>
                    {{}}}
                    {{}}}
                </script>
                <!--.page-links-->
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
        </div>
    </div>
    <!--/.col-right-->
    <!--/#content-->
</div>
<!--================================编辑评比选项弹窗start========================================-->
<div class="edit-alert">
    <div class="alert-title clearfix">
        <p>编辑评价</p>
        <span class="edit-alert-close">X</span>
        <input id="eAppliedId" type="hidden" value="">
    </div>
    <div class="alert-main">
        <div>
            <span class="eva-name">评价名称</span>
            <input id="eAppliedName" type="text" class="eva-input">
        </div>
        <div>
            <span class="eva-name">评价完成时间</span>
            <input id="eFinishTime" name="finishTime" class="eva-input dateTime"
                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
        </div>
        <span class="eva-des">评价说明</span>
        <textarea id="eAppliedDescribe" class="eva-area"></textarea>
    </div>
    <div class="alert-btn">
        <button class="edit-btn-sure">确定</button>
        <button class="edit-btn-qx">取消</button>
    </div>
</div>
<!--================================编辑评比选项弹窗end========================================-->
<!--================================新增评比选项弹窗start========================================-->
<div class="newadd-alert">
    <div class="alert-title clearfix">
        <p>新建评价</p>
        <span class="alert-close">X</span>
    </div>
    <div class="alert-main">
        <div>
            <span class="eva-name">评价名称</span>
            <input id="appliedName" type="text" class="eva-input">
        </div>
        <div>
            <span class="eva-name">评价完成时间</span>
            <input id="finishTime" name="finishTime" class="eva-input dateTime"
                   onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
        </div>
        <span class="eva-des">评价说明</span>
        <textarea id="appliedDescribe" class="eva-area"></textarea>
    </div>
    <div class="alert-tab">
        <span>选择评价指标体系</span>
        <input id="owner2" name="iTreeType" value="2" type="checkbox" checked><em>我的</em>
        <input id="owner1" name="iTreeType" value="1" type="checkbox" checked><em>校级</em><%--/共享--%>
    </div>
    <div class="alert-tabb">
        <table>

            <thead>
                <tr>
                    <th width="275">指标体系名称</th>
                    <th width="150">创建人</th>
                </tr>
            </thead>

            <tbody class="itree-data">

            </tbody>
            <script type="text/template" id="j-tmpl2">
                {{ if(it.length>0){ }}
                {{ for (var i = 0, l = it.length; i < l; i++) { }}
                {{var obj=it[i];}}
                <tr class="itreeInfo" tid="{{=obj.id}}">
                    <td width="275">
                        {{=obj.name}}
                    </td>
                    <td width="150">
                        {{=obj.createrName}}
                    </td>
                </tr>
                {{}}}
                {{}}}
            </script>
        </table>

    </div>
    <div class="alert-btn">
        <button class="alert-next-btn">下一步</button>
    </div>
</div>
<!--================================新增评比选项弹窗end========================================-->
<!--================================选择被评价对象弹窗start========================================-->
<div class="newadd-alertt">
    <div class="alert-title clearfix">
        <p>选择被评价对象</p>
        <span class="alert-close">X</span>
    </div>
    <div class="alert-main">
        <select id="term">
        </select>
        <select id="grade" hidden>
        </select>
        <select id="category" hidden>
        </select>
        <input type="hidden" id="evaluatedGroupIds" value="">
    </div>
    <div class="newadd-alertt-li">
        <div class="newadd-alertt-lii">
        </div>
        <script type="text/template" id="j-tmpl3">
            {{ if(it.length>0){ }}
                {{ for (var i = 0, l = it.length; i < l; i++) { }}
                {{var obj=it[i];}}
                <div cid="{{=obj.id}}" title="{{=obj.className}}"><em>{{=obj.className}}</em></div>
                {{}}}
            {{}}}
        </script>
    </div>
    <div class="alert-btn-two">
        <button class="alert-prev-btn">上一步</button>
        <button class="alert-btn-sure">确定</button>
    </div>
</div>
<!--================================选择被评价对象弹窗start========================================-->

<div class="bg"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('evaluateManage',function(evaluateManage){
        evaluateManage.init();
    });
</script>
</body>
</html>