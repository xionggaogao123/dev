<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
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
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/indicator/evaluate.css?v=2016102001" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
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

    <div class="evaluate-main">
        <div class="evaluate-title">
            <h3 class="grow-col-stu">拓展课评价结果</h3><%--<span class="go-back">&lt;返回</span>--%>
            <input id="evaluateId" type="hidden" value="${dto.id}">
            <input id="appliedId" type="hidden" value="${dto.appliedId}">
            <input id="termType" type="hidden" value="${dto.termType}">
            <input id="snapshotId" type="hidden" value="${dto.snapshotId}">
            <input id="classId" type="hidden" value="${dto.activityId}">
            <input id="commonToId" type="hidden" value="${dto.commonToId}">
        </div>
        <div class="evaluate-tab">
            <span>拓展课名称:</span><em>${dto.activityName}</em>
            <br>
            <span>被评价人:</span><em>${dto.commonToName}</em>
        </div>
        <div class="evaluate-info">
            <div class="evaluate-info-top">指标评价</div>
            <div class="evaluate-info-ev">
                <table id="indicatorTable" cellspacing="">
                </table>
                <script type="text/template" id="j-tmpl">
                    {{var dto=it.message;}}
                    {{ if(dto.zhiBiaos.length>0){ }}
                    {{ for (var i = 0, l = dto.zhiBiaos.length; i < l; i++) { }}
                    {{var obj=dto.zhiBiaos[i];}}
                    <tbody>
                    <tr class="evaluate-t-bl" zhiBiaoId="{{=obj.zhiBiaoId}}" zhiBiaoParentId="{{=obj.zhiBiaoParentId}}" level="{{=obj.level}}" isOpen="0" type="{{=obj.type}}">
                        <td width="475px;">
                            {{if(obj.level==1&&obj.type==1){}}
                                <em class="em-jia"></em>
                                <input readonly="readonly"  placeholder="{{=obj.zhiBiaoName}}"/>
                            {{}else{}}
                                <input  style="margin-left: 35px;" readonly="readonly"  placeholder="{{=obj.zhiBiaoName}}"/>
                            {{}}}
                        </td>
                        <td width="170px">
                            {{if(obj.scoreType==1){}}
                            {{var score=parseInt(obj.score);}}
                            <div class="gradecon Addnewskill_119">
                                <ul class="rev_pro clearfix">
                                    <li>
                                        <input class="fl" type="hidden" style="margin-top:2px;" name="InterviewCommentInfoSub[1]" value="{{=obj.score}}" />
                                        <div class="revinp">
                                            <span class="level">
                                                {{ for (var k = 1; k <=5; k++) { }}
                                                    {{if(k<=score){}}
                                                        <i class="level_solid" cjmark=""></i>
                                                    {{}else{}}
                                                        <i class="level_hollow" cjmark=""></i>
                                                    {{}}}
                                                {{}}}
                                            </span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            {{}}}
                        </td>
                    </tr>
                    </tbody>
                    {{}}}
                    {{}}}
                </script>
            </div>
        </div>
        <div class="evaluate-com">
            <div class="evaluate-com-top">评语</div>
            <span id="describe">${dto.describe}</span>
        </div>
    </div>

    </div>
    <!--/.col-right-->
    <!--/#content-->
</div>
<!--#foot-->
    <%@ include file="../common_new/foot.jsp" %>
    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
    <script src="/static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('stuEvaluateDetail',function(stuEvaluateDetail){
            stuEvaluateDetail.init();
        });

    </script>
</body>
</html>
