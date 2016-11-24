<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2016/8/24
  Time: 18:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <title>成绩分析</title>
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="/static_new/css/scoreAnalysis.css" rel="stylesheet" />

  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script src="http://echarts.baidu.com/build/dist/echarts.js"></script>
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

  <!-- 广告部分 -->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <!-- 广告部分 -->

  <!--.col-right-->
  <div class="col-right">

    <!--.tab-col右侧-->
    <div class="tab-col">
      <div class="select-div clearfix">
        <select id="exams">
          <%--<option>暂无考试</option>--%>
        </select>
        <script id="examsTmpl" type="text/template">
          {{~it:value:index}}
          <option value="{{=value.id}}">{{=value.name}}</option>
          {{~ }}
        </script>
        <select id="grades">
          <%--<option>高一年级</option>--%>
        </select>
        <script id="gradesTmpl" type="text/template">
          {{~it:value:index}}
          <option value="{{=value.id}}">{{=value.name}}</option>
          {{~ }}
        </script>
        <%--<select class="year-select">--%>
          <%--<option>2015-2016学年第二学期</option>--%>
        <%--</select>--%>
      </div>
      <div id="info" style="display: none">
        <div class="tab-head clearfix">
          <ul>
            <li id="ZHYM" class="cur"><a href="javascript:;">综合页面</a><em></em></li>
            <li id="ZFFX"><a href="javascript:;">总分分析</a><em></em></li>
            <li id="DKFX"><a href="javascript:;">单科分析</a><em></em></li>
            <li id="CZDC"><a href="javascript:;">导出</a><em></em></li>
          </ul>
        </div>

        <div class="tab-main">
          <!--================================综合页面start==================================-->
          <div class="all-page" id="tab-ZHYM">
            <div class="grade-one">
              <span class="one-span">年级各科分数段排布</span>

              <div class="one-chart">
                <div style="width:750px;height:300px;" id="chart-one"></div>
                <!--
                <table class="ana-table">
                    <thead>
                        <tr>
                            <th style="width:11%;">科目</th>
                            <th style="width:11%;">优秀人数</th>
                            <th style="width:11%;">良好人数</th>
                            <th style="width:11%;">合格人数</th>
                            <th style="width:12%;">不合格人数</th>
                            <th style="width:11%;">优秀率</th>
                            <th style="width:11%;">良好率</th>
                            <th style="width:11%;">合格率</th>
                            <th style="width:11%;">不合格率</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>语文</td>
                            <td>123</td>
                            <td>123</td>
                            <td>23</td>
                            <td>11</td>
                            <td>12.58%</td>
                            <td>12.58%</td>
                            <td>12.58%</td>
                            <td>12.58%</td>
                        </tr>
                    </tbody>
                </table>
            -->
              </div>
            </div>
            <div class="grade-most">
              <span class="one-span">年级各科最高最低分</span>

              <div>
                <div style="width:750px;height:300px;" id="chart-two"></div>
                <!--
                <table class="ana-table">
                    <thead>
                        <tr>
                            <th style="width:25%;">科目</th>
                            <th style="width:25%;">最高分</th>
                            <th style="width:25%;">最低分</th>
                            <th style="width:25%;">平均分</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>语文</td>
                            <td>最高分</td>
                            <td>最低分</td>
                            <td>平均分</td>
                        </tr>
                    </tbody>
                </table>
            -->
              </div>
            </div>
            <div class="grade-num">
              <span class="one-span">实际参考人数</span>

              <div style="width:770px;overflow-x:auto;">
                <table class="ana-table">
                  <thead>
                  <tr>
                    <th style="width:7%;">科目</th>
                    <th style="width:7%;">最高分</th>
                    <th style="width:7%;">最低分</th>
                    <th style="width:8%;">平均分</th>
                    <th style="width:9%;">总人数</th>
                    <th style="width:9%;">优秀人数</th>
                    <%--<th style="width:9%;">良好人数</th>--%>
                    <th style="width:9%;">合格人数</th>
                    <th style="width:8%;">优秀率</th>
                    <%--<th style="width:8%;">良好率</th>--%>
                    <th style="width:8%;">合格率</th>
                  </tr>
                  </thead>
                  <tbody id="gradesummary">
                  <%--<tr>--%>
                    <%--<td>语文</td>--%>
                    <%--<td>159</td>--%>
                    <%--<td>158</td>--%>
                    <%--<td>99</td>--%>
                    <%--<td>12</td>--%>
                    <%--<td>1539</td>--%>
                    <%--<td>158</td>--%>
                    <%--<td>99</td>--%>
                    <%--<td>15.11%</td>--%>
                    <%--<td>15.11%</td>--%>
                    <%--<td>15.11%</td>--%>
                    <%--<td>15.11%</td>--%>
                  <%--</tr>--%>
                  </tbody>
                  <script id="gradesummaryTmpl" type="text/template">
                    {{~it:value:index}}
                    <tr>
                      <td>{{=value.subjectName}}</td>
                      <td>{{=value.max}}</td>
                      <td>{{=value.min}}</td>
                      <td>{{=value.avg}}</td>

                      <td>{{=value.count}}</td>
                      <td>{{=value.youXiuCount}}</td>
                      <td>{{=value.heGeCount}}</td>
                      <td>{{=value.youXiuRate}}%</td>
                      <td>{{=value.heGeRate}}%</td>
                    </tr>
                    {{~ }}
                  </script>
                </table>
              </div>
            </div>
          </div>
          <!--================================综合页面end==================================-->
          <!--================================总分分析start==================================-->
          <div class="all-analyse" id="tab-ZFFX">
            <div class="all-tab">
              <ul class="clearfix">
                <li class="m-cur" id="XZBWD-1"><a href="javascript:;">行政班维度</a></li>
                <li id="GRWD"><a href="javascript:;">个人维度</a></li>
              </ul>
            </div>
            <!--================================行政班提示start==================================-->
            <div class="less-inf" style="display:none;">
              <span><img src="img/score-1.png"></span>
              <span style="font-weight:bold;">本次考试导入成绩科目较少，无法进行分析</span>
              <em style="color:#666;">导入成绩少于3科，将不做成绩分析</em>
            </div>
            <!--================================行政班提示end==================================-->
            <div class="all-main">
              <!--================================行政班维度start==================================-->
              <div class="all-XZBWD" id="tab-XZBWD-1">
                <span class="one-span">各班总分名次排布情况</span>

                <div class="one-chart">
                  <div class="label-wrap">
                    <label id="ysw"><input type="radio" name="subject" checked>语数外</label>
                    <label id="all"><input type="radio" name="subject">走班（3+3）</label>
                  </div>
                  <div style="width:750px;height:300px;" id="chart-three"></div>

                </div>
                <div class="all-table">
                  <span class="table-title" style="font-weight:bold;">总分</span>
                  <table class="ana-table">
                    <thead>
                    <tr>
                      <th style="width:12%;" rowspan=2>班级</th>
                      <th style="width:22%;" colspan=2>前10名</th>
                      <th style="width:22%;" colspan=2>前50名</th>
                      <th style="width:22%;" colspan=2>前100名</th>
                      <th style="width:22%;" colspan=2>前200名</th>
                    </tr>
                    <tr>
                      <th style="width:11%;">人数</th>
                      <th style="width:11%;">百分率</th>
                      <th style="width:11%;">人数</th>
                      <th style="width:11%;">百分率</th>
                      <th style="width:11%;">人数</th>
                      <th style="width:11%;">百分率</th>
                      <th style="width:11%;">人数</th>
                      <th style="width:11%;">百分率</th>
                    </tr>
                    </thead>
                    <tbody id="zongfen">
                    <%--<tr>--%>
                      <%--<td>1班</td>--%>
                      <%--<td>5</td>--%>
                      <%--<td>10.00%</td>--%>
                      <%--<td>12</td>--%>
                      <%--<td>12</td>--%>
                      <%--<td>12</td>--%>
                      <%--<td>123</td>--%>
                      <%--<td>123123</td>--%>
                      <%--<td>123123</td>--%>
                    <%--</tr>--%>
                    </tbody>
                    <script id="zongfenTmpl" type="text/template">
                      {{~it:value:index}}
                      <tr>
                        <td>{{=value.className}}</td>
                        <td>{{=value.count_10}}</td>
                        <td>{{=value.rate_10}}%</td>
                        <td>{{=value.count_50}}</td>
                        <td>{{=value.rate_50}}%</td>
                        <td>{{=value.count_100}}</td>
                        <td>{{=value.rate_100}}%</td>
                        <td>{{=value.count_200}}</td>
                        <td>{{=value.rate_200}}%</td>
                      </tr>
                      {{~ }}
                    </script>
                  </table>
                </div>
                <div class="rank-table">
                  <span class="one-span">各班级语数外总均分及3+3总均分排名</span>
                  <table class=ana-table>
                    <thead>
                    <tr>
                      <th style="width:20%;">班级</th>
                      <th style="width:20%;">语数外总均分</th>
                      <th style="width:20%;">年级排名</th>
                      <th style="width:20%;">3+3总均分</th>
                      <th style="width:20%;">年级排名</th>
                    </tr>
                    </thead>
                    <tbody id="zongfen1">
                    <%--<tr>--%>
                      <%--<td>1班</td>--%>
                      <%--<td>234</td>--%>
                      <%--<td>234</td>--%>
                      <%--<td>2</td>--%>
                      <%--<td>23</td>--%>
                    <%--</tr>--%>
                    </tbody>
                    <script id="zongfenTmpl1" type="text/template">
                      {{~it:value:index}}
                      <tr>
                        <td>{{=value.className}}</td>
                        <td>{{=value.yswAvg}}</td>
                        <td>{{=value.yswGradeRank}}</td>
                        <td>{{=value.allAvg}}</td>
                        <td>{{=value.allGradeRank}}</td>
                      </tr>
                      {{~ }}
                    </script>
                  </table>
                </div>
              </div>

              <!--================================行政班维度end==================================-->
              <!--================================个人维度start==================================-->
              <div class="per-analyse" id="tab-GRWD">
                <table class="ana-table">
                  <thead>
                  <tr>
                    <th style="width:11%;" rowspan=2>班级</th>
                    <th style="width:12%;" rowspan=2>学号</th>
                    <th style="width:11%;" rowspan=2>姓名</th>
                    <th style="width:33%;" colspan=3>语数外总分</th>
                    <th style="width:33%;" colspan=3>3+3总分</th>
                  </tr>
                  <tr>
                    <th style="width:11%;">成绩</th>
                    <th style="width:11%;">班级排名</th>
                    <th style="width:11%;">年级排名</th>
                    <th style="width:11%;">成绩</th>
                    <th style="width:11%;">班级排名</th>
                    <th style="width:11%;">年级排名</th>
                  </tr>
                  </thead>
                  <tbody id="stuzongfen">
                  <%--<tr>--%>
                    <%--<td>1班</td>--%>
                    <%--<td>123123</td>--%>
                    <%--<td>张慧玲123</td>--%>
                    <%--<td>123</td>--%>
                    <%--<td>12</td>--%>
                    <%--<td>123</td>--%>
                    <%--<td>123</td>--%>
                    <%--<td>123</td>--%>
                    <%--<td>1</td>--%>
                  <%--</tr>--%>
                  </tbody>
                  <script id="stuzongfenTmpl" type="text/template">
                    {{~it:value:index}}
                    <tr>
                      <td>{{=value.className}}</td>
                      <td>{{=value.studentNo}}</td>
                      <td>{{=value.studentName}}</td>
                      <td>{{=value.yswZongFen}}</td>
                      <td>{{=value.yswClassRank}}</td>
                      <td>{{=value.yswGradeRank}}</td>
                      <td>{{=value.zongFen}}</td>
                      <td>{{=value.classRank}}</td>
                      <td>{{=value.gradeRank}}</td>
                    </tr>
                    {{~ }}
                  </script>
                </table>
                <div class="new-page-links"></div>
              </div>
              <!--================================个人维度end==================================-->
            </div>
          </div>
          <!--================================总分分析end==================================-->
          <!--================================单科分析start==================================-->
          <div class="one-analyse" id="tab-DKFX">
            <%--<div class="one-tab">
              <ul class="clearfix">
                <li class="m-cur" id="XZBWD"><a href="javascript:;">行政班维度</a></li>
                <li id="JXBWD"><a href="javascript:;">教学班维度</a></li>
              </ul>
            </div>--%>
            <div class="one-main">
              <!--================================行政班维度start==================================-->
             <%-- <div class="one-XZBWD" id="tab-XZBWD">
                <!--<table class="select-table">
                    <thead>
                        <tr>
                            <th>选择学期</th>
                            <th>选择年级</th>
                            <th>选择考试</th>
                            <th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <select>
                                    <option>2015-2016学年</option>
                                </select>
                            </td>
                            <td><select>
                                    <option>高一年级</option>
                                </select></td>
                            <td><select>
                                    <option>期中考试</option>
                                </select></td>
                            <td>
                                <button class="btn-sure">确认</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            -->
                <div class="one-fsd">
                  <span class="one-span">单科分数段分析</span>

                  <div class="one-chart">
                    <div class="sub-tab">
                      <ul class="clearfix">
                        <li class="s-cur" id="CHI"><img src="/img/score-2.png"><a href="javascript:;">语文</a></li>
                        <li id="MATH"><img src="/img/score-2.png"><a href="javascript:;">数学</a></li>
                        <li id="ENG"><img src="/img/score-2.png"><a href="javascript:;">英语</a></li>
                      </ul>
                    </div>
                    <div style="width:750px;height:300px;" id="chart-five"></div>
                  </div>
                  <div class="sub-fd">
                    <div class="sub-main">
                      <table class="ana-table" id="tab-CHI">
                        <thead>
                        <tr>
                          <th style="width:14%;">1班级</th>
                          <th style="width:14%;">优秀人数</th>
                          <th style="width:14%;">合格人数</th>
                          <th style="width:14%;">低分人数</th>
                          <th style="width:14%;">优秀率</th>
                          <th style="width:14%;">合格率</th>
                          <th style="width:16%;">低分率</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                          <td>1班</td>
                          <td>20</td>
                          <td>129</td>
                          <td>10</td>
                          <td>12.58%</td>
                          <td>81.13%</td>
                          <td>6.29%</td>
                        </tr>
                        <tr>
                          <td>1班</td>
                          <td>20</td>
                          <td>129</td>
                          <td>10</td>
                          <td>12.58%</td>
                          <td>81.13%</td>
                          <td>6.29%</td>
                        </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                  <div class="one-rank">
                    <div class="rank-tab clearfix">
                      <span class="one-span">单科排名</span>
                      <ul class="clearfix">
                        <li class="v-cur" id="ABJ"><a href="javascript:;">按班级</a></li>
                        <li id="AXS"><a href="javascript:;">按学生</a></li>
                      </ul>
                    </div>
                    <div class="rank-main" style="width:770px;overflow-x:auto;">
                      <table class="ana-table" id="tab-ABJ">
                        <thead>
                        <tr>
                          <th style="width:14%;">班级</th>
                          <th style="width:14%;">语文</th>
                          <th style="width:14%;">排名</th>
                          <th style="width:14%;">数学</th>
                          <th style="width:14%;">排名</th>
                          <th style="width:14%;">英语</th>
                          <th style="width:16%;">排名</th>
                        </tr>
                        </thead>
                        <thead>
                        <tr>
                          <td>1班</td>
                          <td>95.1</td>
                          <td>8</td>
                          <td>95.1</td>
                          <td>8</td>
                          <td>95.1</td>
                          <td>8</td>
                        </tr>
                        <tr>
                          <td>1班</td>
                          <td>95.1</td>
                          <td>8</td>
                          <td>95.1</td>
                          <td>8</td>
                          <td>95.1</td>
                          <td>8</td>
                        </tr>
                        </thead>
                      </table>
                      <table class="ana-table" style="width:1020px;" id="tab-AXS">
                        <thead>
                        <tr>
                          <th rowspan=2 style="width:8%">班级</th>
                          <th rowspan=2 style="width:10%">学号</th>
                          <th rowspan=2 style="width:10%">姓名</th>
                          <th colspan=3 style="width:24%">语文</th>
                          <th colspan=3 style="width:24%">数学</th>
                          <th colspan=3 style="width:24%">英语</th>
                        </tr>
                        <tr>
                          <th style="width:8%;">成绩</th>
                          <th style="width:8%;">班级排名</th>
                          <th style="width:8%;">年级排名</th>
                          <th style="width:8%;">成绩</th>
                          <th style="width:8%;">班级排名</th>
                          <th style="width:8%;">年级排名</th>
                          <th style="width:8%;">成绩</th>
                          <th style="width:8%;">班级排名</th>
                          <th style="width:8%;">年级排名</th>
                          <th style="width:8%;">成绩</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                          <td>1班</td>
                          <td>1001</td>
                          <td>姓名</td>
                          <td>张慧玲</td>
                          <td>92</td>
                          <td>323</td>
                          <td>329</td>
                          <td>22</td>
                          <td>23</td>
                          <td>23</td>
                          <td>2323</td>
                          <td>222</td>
                        </tr>
                        <tr>
                          <td>1班</td>
                          <td>1001</td>
                          <td>张慧玲</td>
                          <td>111</td>
                          <td>92</td>
                          <td>323</td>
                          <td>329</td>
                          <td>22</td>
                          <td>23</td>
                          <td>23</td>
                          <td>2323</td>
                          <td>222</td>
                        </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>--%>
              <!--================================行政班维度end==================================-->
              <!--================================教学班维度start==================================-->
              <div class="one-JXBWD" id="tab-JXBWD">
                <div class="one-fsd">
                  <span class="one-span">单科分数段分析</span>

                  <div class="one-chart">
                    <div class="sub-nav">
                      <ul class="clearfix" id="examsubject">
                        <%--<li class="s-cur"><img src="img/score-2.png"><a href="javascript:;">语文</a></li>--%>
                        <%--<li><img src="img/score-2.png"><a href="javascript:;">数学</a></li>--%>
                      </ul>
                      <script id="examsubjectTmpl" type="text/template">
                        {{~it:value:index}}
                        <li sid="{{=value.id}}"><img src="img/score-2.png"><a href="javascript:;">{{=value.value}}</a></li>
                        {{~ }}
                      </script>
                    </div>
                    <div style="width:750px;height:350px;" id="chart-four"></div>
                  </div>
                  <div class="sub-fd">
                    <div class="nav-main">
                      <table class="ana-table" id="tab-WL">
                        <thead>
                        <tr>
                          <th style="width:14%;">班级</th>
                          <th style="width:14%;">优秀人数</th>
                          <th style="width:14%;">优秀率</th>
                          <th style="width:14%;">合格人数</th>
                          <th style="width:14%;">合格率</th>
                          <th style="width:14%;">低分人数</th>
                          <th style="width:16%;">低分率</th>
                        </tr>
                        </thead>
                        <tbody id="subjectSummary">
                        <%--<tr>--%>
                          <%--<td>物理1班</td>--%>
                          <%--<td>20</td>--%>
                          <%--<td>129</td>--%>
                          <%--<td>10</td>--%>
                          <%--<td>12.58%</td>--%>
                          <%--<td>81.13%</td>--%>
                          <%--<td>6.29%</td>--%>
                        <%--</tr>--%>
                        </tbody>
                        <script id="subjectSummaryTmpl" type="text/template">
                          {{~it:value:index}}
                          <tr>
                            <td>{{=value.className}}</td>
                            <td>{{=value.youXiuCount}}</td>
                            <td>{{=value.youXiuRate}}%</td>
                            <td>{{=value.heGeCount}}</td>
                            <td>{{=value.heGeRate}}%</td>
                            <td>{{=value.diFenCount}}</td>
                            <td>{{=value.diFenRate}}%</td>
                          </tr>
                          {{~ }}
                        </script>
                      </table>
                    </div>
                  </div>
                  <div class="grade-rank">
                    <span class="one-span">单科班级排名</span>

                    <div class="one-chart">
                      <div style="width:750px;height:300px;" id="chart-six"></div>
                    </div>
                    <div class="sub-fd">
                      <div class="grade-main">
                        <table class="ana-table" id="tab-WL-1">
                          <thead>
                          <tr>
                            <th style="width:16%">班级</th>
                            <th style="width:28%">平均分</th>
                            <th style="width:28%">排名</th>
                            <th style="width:28%">学生人数</th>
                          </tr>
                          </thead>
                          <tbody id="subjectSummary1">
                          <%--<tr>--%>
                            <%--<td>物理A班</td>--%>
                            <%--<td>120</td>--%>
                            <%--<td>1</td>--%>
                            <%--<td>35</td>--%>
                          <%--</tr>--%>
                          </tbody>
                          <script id="subjectSummaryTmpl1" type="text/template">
                            {{~it:value:index}}
                            <tr>
                              <td>{{=value.className}}</td>
                              <td>{{=value.avg}}</td>
                              <td>{{=value.rank}}</td>
                              <td>{{=value.count}}</td>
                            </tr>
                            {{~ }}
                          </script>
                        </table>
                      </div>
                    </div>
                    <div class="stu-class">
                      <span class="one-span">单科学生排名</span>
                      <div class="stu-main">
                        <table class="ana-table" id="tab-WL-2">
                          <thead>
                          <tr>
                            <th rowspan=2 style="width:16%">班级</th>
                            <th rowspan=2 style="width:16%">学号</th>
                            <th rowspan=2 style="width:16%">姓名</th>
                            <th colspan=3 style="width:52%" id="subjectName">物理</th>
                          </tr>
                          <tr>
                            <th style="width:16%;">成绩</th>
                            <th style="width:16%;">教学班排名</th>
                            <th style="width:20%;">年级选修排名</th>
                          </tr>
                          </thead>
                          <tbody id="subjectScore">
                          <tr>
                            <td>物理A班</td>
                            <td>10123123</td>
                            <td>张慧玲</td>
                            <td>33</td>
                            <td>23</td>
                            <td>232</td>
                          </tr>
                          </tbody>
                          <script id="subjectScoreTmpl" type="text/template">
                            {{~it:value:index}}
                            <tr>
                              <td>{{=value.className}}</td>
                              <td>{{=value.studentNo}}</td>
                              <td>{{=value.studentName}}</td>
                              <td>{{=value.score}}</td>
                              <td>{{=value.classRank}}</td>
                              <td>{{=value.gradeRank}}</td>
                            </tr>
                            {{~ }}
                          </script>
                        </table>
                        <div class="new-page-links"></div>
                      </div>
                    </div>
                  </div>
                </div>
                <!--================================教学班维度end==================================-->
              </div>
            </div>
            <!--================================单科分析end==================================-->


          </div>
          <!--================================成绩分析导出start==================================-->
          <div class="score-out" id="tab-CZDC">
            <ul class="dc-list">
              <li id="3lv1fen"><em></em>各科各班“三率一分”统计表</li>
              <li id="subjectsRanking"><em></em>各科排名表</li>
              <li id="zongbiao"><em></em>年级学生总排名表</li>
              <%--<li><em></em>总分前X名分布表</li>--%>
            </ul>
          </div>
          <!--================================成绩分析导出end==================================-->
        </div>
      </div>
      <div id="warning" class="warning">
        暂无考试
      </div>
      <!--/.tab-col右侧-->

    </div>
    <!--/.col-right-->

  </div>
  <!--/#content-->
</div>
</div>
<div class="zhiban-meng"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/scoreanalysis/0.1.0/headmaster');
</script>

</body>
</html>
