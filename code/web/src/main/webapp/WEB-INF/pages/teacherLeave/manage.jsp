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
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/teacherLeave/qingjiashenhe.css?v=2015041602" rel="stylesheet"/>
    <link href="/static_new/css/teacherLeave/teacherqingjia.css?v=2015041602" rel="stylesheet"/>

    <%--<script type="text/javascript" src="/static/js/jquery.min.js"></script>--%><%--为使用select2.js--%>
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

        <!--.tab-col-->
        <div class="tqingjia_main clearfix">
            <div class="tqingjia_title clearfix">
                <ul class="ulShow">
                    <li id="qingjiaLi">教师请假</li>
                    <li id="daikeLi" class="grqingjia_active">教师代课</li>
                </ul>
                <button id="backOrReply">我的请假</button>
            </div>
            <div class="tqingjia_maincon qingjia">
                <div class="tqingjia_select">
                    <span>时间</span>
                <select class="tselect_time">
                    <option value="0">今天</option>
                    <option value="1">本周</option>
                    <option value="2">本月</option>
                    <option value="3">本学期</option>
                    <option value="4">全部</option>
                </select>
                    <span>老师</span><%--<input type="text" class="teacherChoose" value=""/>--%>
                    <%--<select class="teacherChoose container" id="teacherList1" style="width:130px;">
                        <option value="">全部</option>
                    </select>--%>
                    <input class="teacherChooseInp" placeholder="输入老师名查询">
                    <script type="aplication/template" id="teacherTempJs">
                        <option value="">全部</option>
                        {{~it.data:value:index}}
                        <option value="{{=value.id}}">{{=value.userName}}</option>
                        {{~}}
                    </script>
                    <span>类型</span>
                <select class="tselect_type">
                    <option value="-1">全部</option>
                    <option value="0">未处理</option>
                    <option value="1">同意</option>
                    <option value="2">驳回</option>
                </select>
                </div>
                <script type="application/template" id="leaveApplyListJs">
                    {{~it.data:value:index}}
                    <div class="tqingjia_reason clearfix">
                        <p>{{=value.title}}</p>
                        <p>{{=value.content}}[请假时间:
                            {{?value.dateFrom==value.dateEnd}}
                                {{=value.dateFrom}}
                            {{??}}
                                {{=value.dateFrom}}至{{=value.dateEnd}}
                            {{?}}
                            ,共计{{=value.classCount}}节课]</p>
                        <span>申请人:{{=value.userName}}</span><em>申请日期:{{=value.applyDateStr}}</em>
                        {{?value.reply==0}}
                            <button class="sh" li="{{=value.id}}">审核</button>
                            <button class="rejectLeave" li="{{=value.id}}">驳回</button>
                        {{??value.reply==1}}
                            <button class="replyDiv">已同意,审核人:{{=value.replyPersonName}}</button>
                        {{??value.reply==2}}
                            <button class="replyDiv">已驳回，审核人:{{=value.replyPersonName}}</button>
                        {{?}}
                    </div>
                    {{~}}
                </script>
                <div class="tqingjia_inf">
                    <%--<div class="tqingjia_reason clearfix">
                        <p>因为家里有亲戚病了，申请(2016-02-02号周三下午第二节课)请假，望领导批准。</p>
                        <span>申请人:张三</span><em>申请日期:2016年2月2日</em>
                        <button class="sh">审核</button>
                        <button>驳回</button>
                    </div>
                    --%>
                </div>
                <div class="noLeave">
                    <p>无请假记录</p>
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
            <div class="tqingjia_maincon daike">
                <div class="tqingjia_select">
                    <span>时间</span>
                    <select class="termList2">

                    </select>
                    <script type="application/template" id="termTempJs">
                        {{~it.data:value:index}}
                        <option value="{{=value}}">{{=value}}</option>
                        {{~}}
                    </script>
                    <span>老师</span><%--<input type="text" class="teacherChoose" value=""/>--%>
                    <%--<select class="teacherChoose2" style="width:130px;">
                        <option value="">全部</option>
                    </select>--%>
                    <input class="teacherChooseInp2" placeholder="输入老师名查询">


                </div>
                <script type="application/template" id="replaceTempJs">
                    {{~it.data:value:index}}
                    <div class="tqingjia_reason clearfix">
                        <p>因{{=value.oldTeacherName}}请假，调剂{{=value.teacherName}}代上第{{=value.week}}周星期
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
                <div class="noReplace">
                    <p>无代课记录</p>
                </div>
                <div class="tqingjia_inf">
                    <%--<div class="tqingjia_reason clearfix">
                        <p>因为家里有亲戚病了，申请(2016-02-02号周三下午第二节课)请假，望领导批准。</p>
                        <span>申请人:张三</span><em>申请日期:2016年2月2日</em>
                        <button class="sh">审核</button>
                        <button>驳回</button>
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
            <div class="qjshenhe_maincon">
                <div class="qjshenhe_ren">
                    <span>申请人:</span><em id="applyPerson">张三</em>
                    <span>申请时间:</span><em id="applyDate">2016年2月2日</em>
                    <br>
                    <span>课时:</span><em id="leaveDetail">周三第3节课</em>
                    <br>
                    <span class="qjmx_title">标题:</span><em id="tt"></em>
                    <br>
                    <span class="qjmx_xq">详情:</span><em id="dt"></em>
                </div>
                <div class="qjshenhe_select">
                    <span>请假周</span>
                    <select class="weekList">

                    </select>
                    <script type="application/template" id="weekTempJs">
                        {{~it.data:value:index}}
                                <option value="{{=value}}">第{{=value}}周</option>
                        {{~}}
                    </script>
                    <span>代课老师:</span><select id="teacherList">
                    <%--<option>一年级</option>
                    <option>一年级</option>
                    <option>一年级</option>
                    <option>一年级</option>--%>
                </select>
                    <script type="application/template" id="teacherTemJs">
                        {{~it.data:value:index}}
                                <option value="{{=value.id}}">{{=value.name}}</option>
                        {{~}}
                    </script>
                    <%--<select>
                        <option>一班</option>
                        <option>一班</option>
                        <option>一班</option>
                        <option>一班</option>
                    </select>
                    <input type="text" value="张三"/>--%>
                    <button id="replaceCourse">确认代课</button>
                    <button id="agreeLeave">未代课9节</button>
                </div>
                <script type="application/template" id="tableTmpJs">
                    <thead>
                    <tr>
                        <td class="qjshenhe_first_col">节次/时间</td>
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
                        <td class="qjshenhe_first_col">第{{=index+1}}节<br>{{=value}}</td>
                        {{~it.conf.classDays:value2:index2}}
                            {{~it.course:value1:index1}}
                                {{?value1.x==value2&&value1.y==index+1}}
                                    {{?value1.courseName==""}}
                                        <td></td>
                                    {{??}}
                                        <td class="qjshenhe_table_avtive" x="{{=value2}}" y="{{=index+1}}" cid="{{=value1.courseId}}" cnm="{{=value1.courseName}}">
                                            {{=value1.courseName}}
                                            {{~it.replace:value3:index3}}
                                                {{?value3.x==value2 && value3.y==index+1}}
                                                    <br>{{=value3.replaceTea}}代课
                                                {{?}}
                                            {{~}}
                                        </td>
                                    {{?}}
                                {{?}}
                            {{~}}
                        {{~}}
                    </tr>
                    {{~}}
                    </tbody>
                </script>
                <table class="qjshenhe_table" rules=all>
                    <%--<thead>
                    <tr>
                        <td class="qjshenhe_first_col">节次/时间</td>
                        <td>星期一</td>
                        <td>星期二</td>
                        <td>星期三</td>
                        <td>星期四</td>
                        <td>星期五</td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="qjshenhe_first_col">第1节<br>8:00~8:45</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="qjshenhe_first_col">第2节<br/>8:55~9:40</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td class="qjshenhe_table_avtive">英语课</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="qjshenhe_first_col">第3节<br/>10:00~10:45</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="qjshenhe_first_col">第4节<br/>10:55~11:40</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="qjshenhe_first_col">第5节<br/>14:00~14:45</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="qjshenhe_first_col">第6节<br/>14:55~15:40</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="qjshenhe_first_col">第7节<br/>16:00~16:45</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="qjshenhe_first_col">第8节<br/>16:55~17:40</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td class="qjshenhe_first_col">第9节<br/>19:00~19:50</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    </tbody>--%>

                </table>
            </div>
        </div>


    </div>
    <!--/.col-right-->

</div>

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('manageLeave');
</script>
</body>
</html>