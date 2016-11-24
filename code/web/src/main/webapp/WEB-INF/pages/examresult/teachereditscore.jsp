<%--
  Created by IntelliJ IDEA.
  User: qinbo
  Date: 15/6/21
  Time: 上午12:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>成绩分析</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link href="/static_new/css/chengji.css?v=2015041602" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>


</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<div id="content" class="clearfix">
    <%@ include file="../common_new/col-left.jsp" %>
    <!--.col-right-->
    <div class="col-right">

        <%--<!--.banner-info-->--%>
        <%--<img src="http://placehold.it/770x100" class="banner-info" />--%>
        <%--<!--/.banner-info-->--%>

        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li><a href="/score/teacher.do">首页</a></li>
                    <li><a href="/score/teacher/semester.do">学期成绩</a></li>
                </ul>
                <a href="/score/teacher/input.do" class="green-btn">成绩录入</a>
            </div>

            <div class="tab-main">



                <!--.charts-col-->
                <div class="charts-col">


                    <div class="charts-list" >

                        <dl>


                            <dd class="std-list" style="margin-bottom:20px;">
                                <h4 id="editScoreTitle"></h4>
                                <table width="100%">
                                    <thead>
                                    <th class="25%"><em>姓名</em></th>
                                    <th class="25%"><em>考试分数</em></th>
                                    <th class="25%"><em>缺考</em></th>
                                    <th class="25%"><em>免考</em></th>


                                    </thead>
                                    <tbody class="Ipt" id="Ipt">
                                  <!--  <tr>
                                        <td>张同学</td>
                                        <td><input value="88"/></td>
                                        <td><img src="/static_new/images/result_que_II.jpg" alt=""></td>
                                        <td><img src="/static_new/images/result_main_II.jpg" alt=""></td>


                                    </tr>-->


                                    </tbody>
                                </table>
                                <div id="page_bar"></div>
                            </dd>
                            <button class="green-btn bpn savescore">保存</button>
                            <button class="green-btn bpn quit">取消</button>
                            <%--<button class="green-btn bpn export">下载模板</button>--%>
                            <%--<button class="green-btn bpn import">导入成绩</button>--%>
                            <%--<input type="file" id="file" name="file"/>--%>
                            <%--<div style="clear:both;"></div>--%>
                        </dl>
<%--------------------------------------------------------------------------------------------%>
                        <dl class="c-right-info">
                            <dd class="c-right-c">
                                <em>导入成绩</em>
                            </dd>
                            <dd class="c-right-d">
                                <em>第一步：</em>
                                <button class="green-btn1 export">下载模板</button>
                            </dd>
                            <dd class="c-right-d">
                                <em>第二步：</em>
                                <em>填写成绩</em>
                            </dd>
                            <dd class="c-right-d">
                                <em style="float: left">第三步：</em>
                                <input type="file" id="file" name="file"/><button class="green-btn1 import">导入成绩</button>
                            </dd>
                            <dd class="c-right-c">
                                <em>操作提示</em>
                            </dd>
                            <ul class="c-right-ol">
                                <li>在填写成绩时，除了“成绩”、“缺考”、“免考”三列外，请不要修改其他内容。</li>
                                <li>“缺考”、“免考”默认为“0”，如果存在缺考或免考，请把对应位置的“0”改为“1”，同时对应的成绩不应填写。</li>
                            </ul>

                        </dl>



                    </div>

                </div>
                <!--/.charts-col-->

            </div>

        </div>
        <!--/.tab-col右侧-->

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->



<%@ include file="../common_new/foot.jsp" %>
<script id="ScoreListTmpl" type="text/template">
    {{ for(var i = 0, l = it.length; i < l; i++) {  }}
    <tr>
        <td>{{=it[i].studentName}}</td>
        {{?it[i].subjectScore != null}}
        <td><input value="{{=it[i].subjectScore}}" name="score"></td>
        {{??}}
        <td><input value="" name="score"></td>
        {{?}}
        <td>{{?it[i].absence == 1}}
            <img src="/static_new/images/result_que_I.jpg" class="abs" alt="">
            {{??}}
            <img src="/static_new/images/result_que_II.jpg" class="abs" alt="">
            {{?}}
            <input type="checkbox" style="display:none" name="absList" value="{{=it[i].absence}}">
        </td>
        <td>{{?it[i].exemption == 1}}
            <img src="/static_new/images/result_mian_I.jpg" class="exe" alt="">
            {{??}}
            <img src="/static_new/images/result_main_II.jpg" class="exe" alt="">
            {{?}}
            <input type="checkbox" style="display:none" name="exeList" value="{{=it[i].exemption}}">
        </td>
        <td style="display:none"><input type="checkbox" name="perList" value="{{=it[i].performanceId}}"></td>
        <%--<td style="display:none"><input type="checkbox" name="absList" value="{{=it.perDTOList[i].absence}}"></td>--%>
        <%--<td style="display:none"><input type="checkbox" name="exeList" value="{{=it.perDTOList[i].exemption}}"></td>--%>
    </tr>
    {{ } }}
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static_new/js/modules/chengji/0.1.0/teacher_editscore', function(teaEdit) {
        teaEdit.initPage();
    });
</script>
</body>
</html>
