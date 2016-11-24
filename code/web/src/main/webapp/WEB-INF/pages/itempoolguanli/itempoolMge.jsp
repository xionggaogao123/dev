<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-题库资源管理</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <!-- dialog files -->
    <link href="/static_new/js/modules/dialog/dialog.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="/static_new/css/ziyuan.css" rel="stylesheet" />
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
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
    <div class="col-right">

        <!--.banner-info-->
        <!--
        <img src="http://placehold.it/770x100" class="banner-info" />
         -->
        <!--/.banner-info-->

        <!--.ziyuan-col-->
        <div class="ziyuan-col">
            <div class="zy-head-right clearfix">
                <a href="/itempool/addItem.do" class="ziyuan-orange-btn">上传题目</a>
            </div>
            <dl id="selectionArea">
                <dd>
                    <span>
                        <label>分类</label>
                        <select id="propertyType">
                            <option value="tcv">教材</option>
                            <option value="kpn">知识点</option>
                        </select>
                    </span>
                    <span>
                        <label >题型</label>
                        <select id="quesType">
                            <option value="0">全部</option>
                            <option value="1">选择题</option>
                            <option value="3">判断题</option>
                            <option value="4">填空题</option>
                            <option value="5">主观题</option>
                        </select>
                    </span>
                    <span>
                        <label>难易程度</label>
                        <select id="level">
                            <option value="0">全部</option>
                            <option value="1">容易</option>
                            <option value="2">较容易</option>
                            <option value="3">中等</option>
                            <option value="4">较难</option>
                            <option value="5">难</option>
                        </select>
                    </span>
                    <span>
                        <label>审核状态</label>
                        <select id="isSaved">
                            <option value="99">全部</option>
                            <option value="0">待审核</option>
                            <option value="1">通过</option>
                            <option value="2">未通过</option>
                        </select>
                    </span>
                </dd>

                <dd class="versionSelection">
                    <span>
                        <label>学段</label>
                        <select id="versionTermTypeSelection" subId="versionSubjectSelection" typeInt="1">
                        </select>
                    </span>
                    <span>
                        <label>学科</label>
                        <select id="versionSubjectSelection" subId="bookVertionSelection" typeInt="2">
                        </select>
                    </span>
                    <span>
                        <label>教材版本</label>
                        <select id="bookVertionSelection" subId="gradeSelection" typeInt="3">
                        </select>
                    </span>
                    <span>
                        <label>年级/课本</label>
                        <select id="gradeSelection" subId="chapterSelection" typeInt="4">
                        </select>
                    </span>
                </dd>
                <dd class="versionSelection">
                    <span>
                        <label>章目录</label>
                        <select id="chapterSelection" subId="partSelection" typeInt="5">
                        </select>
                    </span>
                    <span>
                        <label>节目录</label>
                        <select id="partSelection" subId="0" typeInt="6">
                        </select>
                    </span>
                    <button type="button" class="zy-gray-btn search-btn">搜 索</button>
                </dd>
            </dl>

            <table class="zy-gray-table" width="100%">
                <thead>
                <th width="125">题型</th>
                <th width="110">上传日期</th>
                <th width="120">教材版本</th>
                <th width="110">知识点</th>
                <th width="140">上传信息</th>
                <th>操作</th>
                </thead>
                <tbody id="itemList">

                </tbody>
            </table>


        </div>
        <!--/.ziyuan-col-->
        <!-- 分页div -->
        <div class="new-page-links">
        </div>

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<script type="text/javascript" src="/static_new/js/modules/dialog/dialog.js"></script>
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('itempoolMge', function (itempoolMge) {
        itempoolMge.init();
    });
</script>
</body>
</html>