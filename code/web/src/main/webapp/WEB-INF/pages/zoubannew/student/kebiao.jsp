<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/7/26
  Time: 15:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>3+3走班</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>

<body gid="${gid}">


<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">

        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li id="XSKB" class="cur"><a href="javascript:;">学生课表</a><em></em></li>
                    <li id="XSXK"><a href="javascript:;">学生选课</a><em></em></li>
                </ul>
            </div>
            <div class="tab-main">
                <!--=================================第五步学生课表start==================================-->
                <div class="xskb-con clearfix" id="tab-XSKB" style="display: block">

                    <div class="fifthstep-title clearfix" style="float:left;width:100%;margin:10px 0 20px 0;">
                        <%--<div class="fstep-select">
                            <span id="currTerm"></span>
                            <span style="display: none">学年:</span>
                            <select id="termListCtx" style="display: none">
                                &lt;%&ndash;<option>政治</option>&ndash;%&gt;
                            </select>
                            <script id="termListTmpl" type="application/template">
                                {{~it:value:index}}
                                <option value="{{=value}}">{{=value}}</option>
                                {{~}}
                            </script>
                        </div>--%>
                            <div class="w-wrap" style="float: left">
                                <span class="termShowSp"></span><span class="weekShow2" style="margin-left: 30px;"></span>
                                <select id="weekSelectCtx" style="margin-left: 30px;"></select>
                                <script id="weekSelectTmpl" type="application/template">
                                    {{~it:week:index}}
                                    <option style="cursor: pointer" value="{{=week}}">第{{=week}}周</option>
                                    {{~}}
                                </script>
                                <%--<button id="otherweeks" class="other-week">其他周</button>--%>
                            </div>
                        <a href="javascript:;" class="fstep-dc" style="display: none;" id="exportStu">导出</a>
                    </div>
                    <table class="newTable" id="studentTable">
                        <thead>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    <div id="msg"
                         style="display: none; padding-top:150px;color: #666;font-size: 20px;font-weight: bold;text-align: center;">
                        本学年课表未发布
                    </div>
                    <script type="application/template" id="studentTableTmpl">
                        <thead>
                        <tr>
                            <th style="width:20%;">上课时间</th>
                            {{~it.conf.classDays:value:index}}
                            <th style="width:16%;">周{{?value==1}}一
                                {{??value==2}}二
                                {{??value==3}}三
                                {{??value==4}}四
                                {{??value==5}}五
                                {{??value==6}}六
                                {{??value==7}}日
                                {{?}}
                            </th>
                            {{~}}
                        </tr>
                        </thead>
                        {{~it.conf.classTime:value:index}}
                        <tr>
                            <td style="background:#ececec;">
                                <span class="class-turn">{{=index+1}}</span>{{=it.conf.classTime[index]}}
                            </td>
                            {{~it.conf.classDays:value1:index1}}
                            <td>
                                {{~it.course:value2:index2}}
                                {{?value2.xIndex==value1 && value2.yIndex==(index+1)}}
                                {{=value2.className}}<br/>
                                {{?value2.teacherName!=""}}
                                <span>({{=value2.teacherName}})</span><br/>
                                {{?}}
                                {{=value2.classRoom}}
                                {{?}}
                                {{~}}
                            </td>
                            {{~}}
                        </tr>
                        {{~}}
                    </script>
                </div>
                <!--=================================第五步学生课表end==================================-->
                <!--=================================学生选课start==================================-->
                <div class="xsxk-con" id="tab-XSXK" style="display: none">
                    <div class="zouban-title clearfix">
                        <div class="title-left">
                            <h3 class="xkzhsz-title">2015-2016学年高二年级</h3>
                        </div>
                    </div>
                    <table class="xkzhsz-table" style="margin-top: 20px;">
                        <thead>
                        <tr>
                            <th style="width:22%;">组合</th>
                            <th style="width:13%;">物理</th>
                            <th style="width:13%;">化学</th>
                            <th style="width:13%;">生物</th>
                            <th style="width:13%;">政治</th>
                            <th style="width:13%;">历史</th>
                            <th style="width:13%;">地理</th>
                        </tr>
                        </thead>
                        <tbody id="subjectGroupsContext"></tbody>
                        <script id="subjectGroupsTmpl" type="text/template">
                            {{~ it:group:i }}
                            <tr>
                                <td style="background:#ececec;">
                                    <label>{{=group.groupName}}</label>
                                </td>
                                {{~ group.chooseState:state:j }}
                                <td><input type="checkbox" {{? state==1}}checked{{?}} disabled></td>
                                {{~ }}
                            </tr>
                            {{~ }}
                        </script>

                        <tr>
                            <td style="background:#ececec;">共计20种组合</td>
                            <td>含物理10种</td>
                            <td>含化学10种</td>
                            <td>含生物10种</td>
                            <td>含政治10种</td>
                            <td>含历史10种</td>
                            <td>含地理10种</td>
                        </tr>
                    </table>
                </div>
                <!--================================学生选课end==================================-->
            </div>
        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->
</div>
</div>
<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('/static_new/js/modules/zouban/2.0/studentkebiao.js');
</script>

</body>
</html>
