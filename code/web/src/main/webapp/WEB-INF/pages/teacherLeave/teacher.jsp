<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>教师请假</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <%--<link href="/static/css/homepage.css?v=1" rel="stylesheet"/>--%>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/teacherLeave/gerenqingjia.css?v=2015041602" rel="stylesheet"/>
    <link href="/static_new/css/teacherLeave/gerendaike.css?v=2015041602" rel="stylesheet"/>
    <link href="/static_new/css/teacherLeave/qingjiatijiao.css?v=2015041602" rel="stylesheet"/>
    <link href="/static_new/css/teacherLeave/qingjiamingxi.css?v=2015041602" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>

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
    <div class="col-right clearfix">

        <!--.banner-info-->
        <!--/.banner-info-->

        <!--.tab-col-->
        <div class="grqingjia_main right1 clearfix">
            <div class="grqingjia_title clearfix">
                <ul>
                    <li id="qingjia">个人请假</li>
                    <li id="daike" class="grqingjia_active">个人代课</li>
                </ul>
                <button id="applyLeave">我要请假</button>
            </div>
            <div class="grqingjia_maincon noLeave">
                <p>您还没有请过假</p>
            </div>
            <div class="grqingjia_maincon qingjia">

                <script type="application/template" id="myLeaveList">

                        {{~it.data:value:index}}
                        <div class="grqingjia_reason clearfix">
                            <p>{{=value.title}}</p>
                            <span>结果:{{=value.replyMessage}}</span><em>申请日期:{{=value.applyDateStr}}</em>
                            <button class="detail" li="{{=value.id}}">详情</button>
                            {{?value.reply==0||value.reply==2}}
                            <button class="removeLeave" li="{{=value.id}}" style="margin-right: 10px;">删除</button>
                            {{?}}
                        </div>
                        {{~}}

                </script>
                <div class="grqingjia_inf myLeave"></div>
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
            </div>
            <div class="grqingjia_maincon noReplace">
                <p>您还没有代课记录</p>
            </div>
            <div class="grqingjia_maincon daike">

                <script type="application/template" id="replaceTempJs">
                    {{~it.data:value:index}}
                        <div class="grqingjia_reason clearfix">
                            <p>因{{=value.oldTeacherName}}请假，调剂您代上第{{=value.week}}周星期
                                {{?value.x==1}}一
                                {{??value.x==2}}二
                                {{??value.x==3}}三
                                {{??value.x==4}}四
                                {{??value.x==5}}五
                                {{??value.x==6}}六
                                {{??value.x==0}}日
                                {{?}}
                                第{{=value.y}}节{{=value.courseName}}课。</p>
                            <span>审核人:{{=value.managerName}}</span><em>调剂时间:{{=value.time}}</em>
                            <%--<button class="detail">详情</button>--%>
                        </div>
                    {{~}}
                </script>

                <div class="grqingjia_inf myReplace">
                    <%--<div class="grqingjia_reason clearfix">
                        <p>因为家里有亲戚病了，申请(2016-02-02号周三下午第二节课)请假，望领导批准。</p>
                        <span>被代课老师:张三</span><em>申请日期:2016年2月2日</em>
                        <button class="detail">详情</button>
                    </div>
                    --%>
                </div>
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
            </div>
        </div>
        <div class="qjtijiao_main right2 clearfix">
            <div class="qjtijiao_title clearfix">
                <ul>
                    <li>我要请假</li>
                </ul>
                <button id="back">返回</button>
            </div>

            <div class="qjtijiao_maincon">
                <div class="qjtijiao_select">请假日期：
                    <input type="text" placeholder="开始时间：" id="from" style="margin-right: 20px;">至
                    <input type="text" placeholder="结束时间：" id="end" class="sd" style="margin-left:20px;margin-right: 50px;">
                    共有课程<em id="classCount">0</em>节
                    <button class="qjtijiao_submit">提交</button>
                </div>
                <div class="qjtijiao_select">
                    <%--<span>2015-2016学年第一学期:</span><select>
                    <option>一年级</option>
                    <option>一年级</option>
                    <option>一年级</option>
                    <option>一年级</option>
                </select>--%>
                    <span>课程预览</span>
                    <select class="weekList">
                    </select>
                    <script type="application/template" id="weekJs">
                        {{~it.data:value:index}}
                        <option value="{{=value}}">第{{=value}}周</option>
                        {{~}}
                    </script>

                </div>
                <script type="application/template" id="tableTmpJs">
                    <thead>
                    <tr>
                        <td class="qjtijiao_first_col">节次/时间</td>
                        {{~it.conf.classDays:value:index}}
                        <td>星期
                            {{?value==1}}一
                            {{??value==2}}二
                            {{??value==3}}三
                            {{??value==4}}四
                            {{??value==5}}五
                            {{??value==6}}六
                            {{??value==7}}日
                            {{?}}
                        </td>
                        {{~}}
                    </tr>
                    </thead>
                    <tbody>
                    {{~it.conf.classTime:value:index}}
                        <tr>
                            <td class="qjtijiao_first_col">第{{=index+1}}节<br>{{=value}}</td>
                            {{~it.conf.classDays:value2:index2}}
                                {{~it.course:value1:index1}}
                                    {{?value1.x==value2&&value1.y==index+1}}
                                        {{?value1.courseName==""}}
                                            <td></td>
                                        {{??}}
                                            <td class="qjtijiao_table_avtive">{{=value1.courseName}}</td>
                                        {{?}}
                                    {{?}}
                                {{~}}
                            {{~}}
                        </tr>
                    {{~}}
                    </tbody>
                </script>
                <table class="qjtijiao_table" rules=all>

                </table>
            </div>
        </div>
        <div class="qjmingxi_main right3 clearfix">
            <div class="qjmingxi_title clearfix">
                <ul>
                    <li>请假明细</li>
                </ul>
                <button id="detailBack">返回</button>
            </div>

            <div class="qjmingxi_maincon">
                <div class="qjmingxi_ren">
                    <span>申请人:</span><em id="applyPerson"></em>
                    <span>申请时间:</span><em id="applyDate"></em>
                    <br>
                    <span>审核意见：</span><span class="replyMessage" style="margin-right: 20px;">同意</span>
                    <span>课时:</span><em id="leaveDetail">周三第3节课</em>
                    <button id="deleteLeave" li="">删除</button>
                </div>
                <div class="qjmingxi_content">
                    <span class="qjmx_title">标题</span><input id="tt" type="text" class="qjmingxi_bt" readonly>
                    <br>
                    <span class="qjmx_xq">详情</span><textarea id="dt" readonly></textarea>
                </div>
            </div>
        </div>
        <div class="qjmx_alert">
            <div class="qjmx_alert_title">
                <span>请假明细</span>
            </div>
            <div class="qjmx_alert_main">
                <span>标题</span><input id="title" type="text"/>
                <br>
                <span>详情</span><textarea id="details"></textarea>
                <br>
                <button class="qjmx_alert_sure">确定</button>
                <button class="qjmx_alert_look">确定并查看</button>
                <button class="qjmx_alert_cancel">取消</button>
            </div>
        </div>
        <div class="qjmx_confirm">
            <div class="qjmx_alert_title">
                <span>删除请假</span>
            </div>
            <div class="qjmx_confirm_main">
                <input type="hidden" id="leaveIdInp">
                <input type="hidden" id="leaveTypeInp">
                <span>确定删除该请假记录吗？删除后将无法找回。</span>
                <button class="qjmx_confirm_sure">确定</button>
                <button class="qjmx_confirm_cancel">取消</button>
            </div>
        </div>
    </div>
    <!--/.col-right-->

</div>
<div class="bg"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('teacherLeave');
</script>
</body>
</html>