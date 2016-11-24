<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/10/26
  Time: 15:50
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>成绩统计分析</title>
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
  <link href="/static_new/css/resultanalysis.css" rel="stylesheet" />
  <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
  <%--<script language="javascript" src="js/jquery-1.6.4.min.js" >--%>
  <%--<script type="text/javascript" src="/js/pagination/jqPaginator.min.js"></script>--%>

  <%--</script>--%>
</head>
<body>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div class="cont-main">
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <ul class="main-top">
    <li class="top-cur top-1 li">学校排名</li>
    <li class="top-2 li">成绩分布</li>
    <li class="top-4 li">学科统计</li>
    <li class="top-3 li">学生列表</li>
    <li class="li1"></li>
  </ul>
  <div class="main-4">
    <div class="main-LK">
      <dl>
          <dd>
              <span id="title8"></span>
          </dd>
          <dt>
              <em>1</em>设置等第
          </dt>
      </dl>
      <h3 id="title10">
          <%--2015-2016学年--%>
      </h3>
      <table class="tab1">
          <thead>
            <tr>
                <th>等第</th>
                <th>分数区间</th>
                <th>
                    <button>编辑</button>
                </th>
            </tr>
          </thead>
          <tbody id="showGrades">
            <%--<tr>--%>
                <%--<td><div>优秀</div></td>--%>
                <%--<td>≥90%</td>--%>
            <%--</tr>--%>
          </tbody>
          <script id="showGrades_tmpl" type="text/template">
              {{for (var i in it) { }}
                  <tr>
                      <td><div>{{=it[i].name}}</div></td>
                      <td>≥{{=it[i].percent}}%</td>
                  </tr>
              {{ }}}
          </script>
      </table>
        <dl>
            <dt>
                <em>2</em>学科成绩分布
            </dt>
        </dl>
        <div class="main-42-top" id="title9">汇总表</div>
        <table class="tab2">
            <thead>
                <tr id="showGrades1">
                    <%--<th class="th1"><div>考试学科</div></th>--%>
                    <%--<th class="th1"><div>考生总数</div></th>--%>
                    <%--<th class="th1"><div>考生总分</div></th>--%>
                    <%--<th class="th1"><div>平均分</div></th>--%>
                    <%--<th class="th1"><div>及格人数</div></th>--%>
                    <%--<th class="th1"><div>及格率</div></th>--%>
                    <%--<th class="th2"><div>A级<br>人数 | 百分率</div></th>--%>
                    <%--<th class="th2"><div>B级<br>人数 | 百分率</div></th>--%>
                    <%--<th class="th2"><div>C级<br>人数 | 百分率</div></th>--%>
                    <%--<th class="th2"><div>D级<br>人数 | 百分率</div></th>--%>
                    <%--<th class="th2"><div>C级<br>人数 | 百分率</div></th>--%>
                    <%--<th class="th2"><div>D级<br>人数 | 百分率</div></th>--%>
                </tr>
                <script id="showGrades_tmpl1" type="text/template">
                    <th class="th1"><div>考试学科</div></th>
                    <th class="th1"><div>考生总数</div></th>
                    <th class="th1"><div>考生总分</div></th>
                    <th class="th1"><div>平均分</div></th>
                    <th class="th1"><div>及格人数</div></th>
                    <th class="th1"><div>及格率</div></th>
                    {{for (var i in it) { }}
                    <th class="th2"><div>{{=it[i].name}}级<br>人数 | 百分率</div></th>
                    {{ }}}
                </script>
            </thead>
            <tbody id="subjectDistribution">
                <%--<tr>--%>
                    <%--<td>语文</td>--%>
                    <%--<td>3000</td>--%>
                    <%--<td>1000000</td>--%>
                    <%--<td>88</td>--%>
                    <%--<td>88</td>--%>
                    <%--<td>88%</td>--%>
                    <%--<td>88 | 50%</td>--%>
                    <%--<td>88 | 50%</td>--%>
                    <%--<td>88 | 50%</td>--%>
                    <%--<td>88 | 50%</td>--%>
                    <%--<td>88 | 50%</td>--%>
                    <%--<td>88 | 50%</td>--%>

                <%--</tr>--%>
            </tbody>
            <script id="subjectDistribution_tmpl" type="text/template">
                {{for (var i in it) { }}
                <tr>
                    <td>{{=it[i].subNm}}</td>
                    <td>{{=it[i].stuCount}}</td>
                    <td>{{=it[i].totalScore}}</td>
                    <td>{{=it[i].avgScore}}</td>
                    <td>{{=it[i].passNum}}</td>
                    <td>{{=it[i].passRate}}%</td>
                    {{ var grades = it[i].grades;}}
                    {{for(var j in grades) { }}
                    <td>{{=grades[j].count}} | {{=grades[j].rate}}%</td>
                    {{ } }}
                </tr>
                {{ }}}
            </script>
        </table>
    </div>
  </div>
  <div class="main-1">
    <div class="main1-1">
      <button class="detial-btn">考试详情</button>
    </div>
    <div class="main-LK">
      <dl>
        <dd>
          <span id="title0">
            <%--2014-2015学年第二学期初三年级九校联考--%>
          </span>
        </dd>
        <dt>
          <em>1</em>学校各科排名
        </dt>
      </dl>
      <!--=======================图表==========================-->
      <div class=""  id="pgfId" style="width:980px;height: 500px;">
      </div>
    </div>
    <div class="main1-2"  id="title1">
      <%--2014-2015学年第二学期初三年级&nbsp;九校联考各科排名--%>
    </div>
    <div class="main1-3">
      <div class="main1-3-ch">
        <table class="main1-3-tab">
          <thead class="row1" id="subject">
            <%--<td><em>学校</em></td>--%>
            <%--<td><em>综合</em></td>--%>
            <%--<td><em>语文</em></td>--%>
            <%--<td><em>数学</em></td>--%>
            <%--<td><em>英语</em></td>--%>
            </thead>
          <tbody id="school_rank">
            <%--<tr class="row">--%>
              <%--<td>县三中</td>--%>
              <%--<td>1</td>--%>
              <%--<td>1</td>--%>
              <%--<td>1</td>--%>
              <%--<td>1</td>--%>
            <%--</tr>--%>
            <%--<tr class="row">--%>
              <%--<td>216学校</td>--%>
              <%--<td>2</td>--%>
              <%--<td>2</td>--%>
              <%--<td>2</td>--%>
              <%--<td>2</td>--%>
            <%--</tr>--%>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <div class="main-1-2">
    <span class="back"><返回</span>
    <h2 id="title2">
      <%--2014-2015学年第二学期初三年级&nbsp;九校联考--%>
    </h2>
    <div class="main1-2-1"><span>1</span>学校考试详情</div>
    <div class="em-border"></div>
    <div class="main1-2-2" id="title3">
      <%--2014-2015学年第二学期初三年级&nbsp;九校联考（语文）考试详情--%>
    </div>
    <table class="main1-2-3">

      <thead class="row1" id="subject2">
        <%--<td class="col1"><em class="row1-em">学校</em></td>--%>
        <%--<td class="col2"><em class="row1-em">学生数</em></td>--%>
        <%--<td class="col3"><em class="row1-em">综合总分</em></td>--%>
        <%--<td class="col4"><em class="row1-em">综合名次</em></td>--%>

        <%--<td class="col5"><br />平均分</td>--%>
        <%--<td class="col6"><br /><i>/</i>最高分</td>--%>
        <%--<td class="col7"><br /><i>/</i>最低分</td>--%>
        <%--<td class="col8">语文<br /><i>/</i>及格人数</td>--%>
        <%--<td class="col9"><br /><i>/</i>优秀率</td>--%>
        <%--<td class="col10"><br /><i>/</i>综合评分</td>--%>
        <%--<td class="col11"><br /><i>/</i>综合名次</td>--%>

        <%--<td class="col5"><br />平均分</td>--%>
        <%--<td class="col6"><br /><i>/</i>最高分</td>--%>
        <%--<td class="col7"><br /><i>/</i>最低分</td>--%>
        <%--<td class="col8">数学<br /><i>/</i>及格人数</td>--%>
        <%--<td class="col9"><br /><i>/</i>优秀率</td>--%>
        <%--<td class="col10"><br /><i>/</i>综合评分</td>--%>
        <%--<td class="col11"><br /><i>/</i>综合名次</td>--%>

        <%--<td class="col5"><br />平均分</td>--%>
        <%--<td class="col6"><br /><i>/</i>最高分</td>--%>
        <%--<td class="col7"><br /><i>/</i>最低分</td>--%>
        <%--<td class="col8">数学<br /><i>/</i>及格人数</td>--%>
        <%--<td class="col9"><br /><i>/</i>优秀率</td>--%>
        <%--<td class="col10"><br /><i>/</i>综合评分</td>--%>
        <%--<td class="col11"><br /><i>/</i>综合名次</td>--%>
      </thead>
      <tbody id="summary">
      <tr class="row">
        <%--<td class="col1">216学校</td>--%>
        <%--<td class="col2">400</td>--%>
        <%--<td class="col3">400</td>--%>
        <%--<td class="col4">4</td>--%>

        <%--<td class="col5">400</td>--%>
        <%--<td class="col6">400</td>--%>
        <%--<td class="col7">400</td>--%>
        <%--<td class="col8">400</td>--%>
        <%--<td class="col9">40%</td>--%>
        <%--<td class="col10">400</td>--%>
        <%--<td class="col11">4</td>--%>

      <%--</tr>--%>
      <%--<tr class="row">--%>
        <%--<td class="col1">县二中</td>--%>
        <%--<td class="col2">400</td>--%>
        <%--<td class="col3">400</td>--%>
        <%--<td class="col4">4</td>--%>
        <%--<td class="col5">400</td>--%>
        <%--<td class="col6">400</td>--%>
        <%--<td class="col7">400</td>--%>
        <%--<td class="col8">400</td>--%>
        <%--<td class="col9">40%</td>--%>
        <%--<td class="col10">400</td>--%>
        <%--<td class="col11">4</td>--%>
      <%--</tr>--%>
      </tbody>
      <tbody id="totalsummary">
        <%--<tr>--%>
          <%--<td>总平均</td>--%>
          <%--<td>360</td>--%>
          <%--<td></td>--%>
          <%--<td></td>--%>
          <%--<td>360</td>--%>
          <%--<td>360</td>--%>
          <%--<td>360</td>--%>
          <%--<td>360</td>--%>
          <%--<td>36%</td>--%>
          <%--<td></td>--%>
          <%--<td></td>--%>
        <%--</tr>--%>
      </tbody>
    </table>
    <p>学科综合评分公式=平均分*0.4+优秀率*0.3+及格率*0.3</p>
  </div>
  <div class="main-2">
    <h2 id="title4">
      <%--2014-2015学年第二学期初三年级&nbsp;九校联考--%>
    </h2>
    <div class="main1-2-1"><span>1</span>设置分数段</div>
    <table class="main2-1">
      <tr class="row1">
        <td><em id="totalscore">满分750分</em><button class="main2-1-set">设置</button></td>
      </tr>
      <tr class="row2">
        <td><em>分数段</em></td>
      </tr>
      <tbody id="distriTitle1">
      <%--<tr>--%>
        <%--<td>680-750</td>--%>
      <%--</tr>--%>
      <%--<tr>--%>
        <%--<td>600-679</td>--%>
      <%--</tr>--%>
      <%--<tr>--%>
        <%--<td>0-599</td>--%>
      <%--</tr>--%>
      </tbody>
    </table>
    <div class="main1-2-1"><span>2</span>学校成绩分布</div>
    <div id="pgfIt" style="width: 1000px;height: 400px;">

    </div>
    <div class="main2-2" id="title5">
      <%--2014-2015学年第二学期初三年级&nbsp;九校联考（语文）成绩人数分布--%>
    </div>
    <table class="main2-3">
      <thead id="distriTitle">
      <tr class="row1">
        <td class="col1"><em>学校</em></td>
        <td><em>最高分</em></td>
        <td><em>680以上</em></td>
        <td><em>600-679</em></td>
        <td><em>600以下</em></td>
      </tr>
      </thead>
      <tbody id="distriBody">
      <%--<tr>--%>
        <%--<td>学校</td>--%>
        <%--<td>最高分</td>--%>
        <%--<td>680以上</td>--%>
        <%--<td>600-679</td>--%>
        <%--<td>600以下</td>--%>
      <%--</tr>--%>
      </tbody>

    </table>
  </div>
  <div class="main-3">
    <h2 id="title6">
      <%--2014-2015学年第二学期初三年级&nbsp;九校联考成绩列表--%>
    </h2>
    <div class="main1-2-1"><span>1</span>学生成绩列表</div>
    <table class="main3-1">
      <tr class="row0">
        <td colspan="5" id="title7">
          <%--2014-2015学年第二学期初三年级&nbsp;九校联考统计表--%>
        </td>
      </tr>
      <tr class="row1" id="subject1">
        <%--<td><em class="em1">全县排名</em></td>--%>
        <%--<td><em>姓名</em></td>--%>
        <%--<td><em>语文</em></td>--%>
        <%--<td><em>数学</em></td>--%>
        <%--<td><em>英语</em></td>--%>
        <%--<td><em>学校</em></td>--%>
        <%--<td><em class="em5">学校名次</em></td>--%>
      </tr>
      <tbody id="areaStuScore">
        <%--<tr>--%>
          <%--<td>1</td>--%>
          <%--<td>张三</td>--%>
          <%--<td>1</td>--%>
          <%--<td>1</td>--%>
          <%--<td>县三中</td>--%>
          <%--<td>1</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
          <%--<td>1</td>--%>
          <%--<td>张三</td>--%>
          <%--<td>1</td>--%>
          <%--<td>1</td>--%>
          <%--<td>县三中</td>--%>
          <%--<td>1</td>--%>
        <%--</tr>--%>
      </tbody>




    </table>
    <div class="new-page-links"></div>
  </div>


</div>
<!--/#content-->
<div class="wind-score">
  <div class="wind-1"><em>设置分数段</em></div>
  <div class="wind-2">
    <div class="wind-2-1"><em>开始</em></div>
    <div class="wind-2-2"><em>结束</em></div>
    <div class="wind-2-3"><em>开始:</em><input type="text" id="KS"></div>
    <div class="wind-2-4"><em>结束:</em><input type="text" id="JS"></div>
  </div>
  <div class="wind-3">
    <button class="btn-QX">取消</button>
    <button class="btn-QD">确定</button>
    <button class="btn-TJ">添加</button>
  </div>
  <table class="wind-4">
    <tbody>
    <tr class="row1" hidden="true" id="ksjs">
      <td class="col">开始</td>
      <td class="col2"></td>
      <td class="col">结束</td>
    </tr>
    <%--<tr>--%>
      <%--<td>680</td>--%>
      <%--<td>-</td>--%>
      <%--<td>750</td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
      <%--<td>600</td>--%>
      <%--<td>-</td>--%>
      <%--<td>680</td>--%>
    <%--</tr>--%>
    <%--<tr>--%>
      <%--<td>0</td>--%>
      <%--<td>-</td>--%>
      <%--<td>600</td>--%>
    <%--</tr>--%>
    </tbody>
  </table>

</div>
<%--等第设置弹窗--%>
<div class="wind wind-ddsz">
    <div class="wind-top clearfix">
        等第设置
        <em>×</em>
    </div>
    <div class="ddtitle">
        <span class="span1">等第</span>
        <span class="span2">分数区间</span>
            <span class="span3">
                <button>添加</button>
            </span>
    </div>
    <table>
        <tbody id="grades">
        <tr>
            <th class="th1"></th><th class="th2"></th><th></th>
        </tr>
        <tr>
            <td class="td1">
                <input type="text" value="A">
            </td>
            <td class="td2">
                <input type="text" value="85" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><i>≥</i><span>%</span>
            </td>
            <td>
                <button class="btn-x1">删除</button>
            </td>
        </tr>
        <tr>
            <td class="td1">
                <input type="text" value="B">
            </td>
            <td class="td2">
                <input type="text" value="70" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><i>≥</i><span>%</span>
            </td>
            <td>
                <button class="btn-x1">删除</button>
            </td>
        </tr>
        <tr>
            <td class="td1">
                <input type="text" value="C">
            </td>
            <td class="td2">
                <input type="text" value="60" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><i>≥</i><span>%</span>
            </td>
            <td>
                <button class="btn-x1">删除</button>
            </td>
        </tr>
        <tr>
            <td class="td1">
                <input type="text" value="D">
            </td>
            <td class="td2">
                <input type="text" value="0" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"><i>≥</i><span>%</span>
            </td>
            <td>
                <button class="btn-x1">删除</button>
            </td>
        </tr>
        </tbody></table>
    <div class="btn-f">
        <button class="btn-ok">确认</button>
        <button class="btn-no">取消</button>
    </div>
</div>
<div class="bg"></div>

<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->

<script id="subject_tmpl" type="text/template">
  <td><em>学校</em></td>
  <td><em>综合</em></td>
  {{for (var i in it) { }}
  <td><em>{{=it[i]}}</em></td>
  {{ }}}
</script>

<script id="subject_tmpl1" type="text/template">
  <td><em class="em1">全县排名</em></td>
  <td><em>姓名</em></td>

  {{for (var i in it) { }}
  <td><em>{{=it[i]}}</em></td>
  {{ }}}
  <td><em>总分</em></td>
  <td><em>学校</em></td>
  <td><em class="em5">学校名次</em></td>
</script>

<script id="subject_tmpl2" type="text/template">
  <td class="col1"><em class="row1-em">学校</em></td>
  <td class="col2"><em class="row1-em">学生数</em></td>
  <td class="col3"><em class="row1-em">综合总分</em></td>
  <td class="col4"><em class="row1-em">综合名次</em></td>

  {{for (var i in it) { }}
  <td class="col5"><br />平均分</td>
  <td class="col6"><br /><i>/</i>最高分</td>
  <td class="col7"><br /><i>/</i>最低分</td>
  <td class="col8"><br /><i>/</i>及格人数</td>
  <td class="col9"><br /><i>/</i>及格率</td>
  <td class="col9">{{=it[i]}}<br /><i>/</i>优秀人数</td>
  <td class="col9"><br /><i>/</i>优秀率</td>
  <td class="col10"><br /><i>/</i>综合评分</td>
  <td class="col11"><br /><i>/</i>综合名次</td>
  <td class="col12"><br /><i>/</i>比均</td>
  <td class="col13"><br /><i>/</i>超均</td>
  {{ }}}
</script>

<script id="school_rank_tmpl" type="text/template">
  {{for (var i in it) { }}
  <tr class="row">
    <td>{{=it[i].schoolName}}</td>
    <td>{{=it[i].compositeRanking}}</td>
    {{for (var j in it[i].subjectDetailsDTOList){ }}
    <td>{{=it[i].subjectDetailsDTOList[j].compositeRanking}}</td>
    {{} }}
  </tr>
  {{ }}}
</script>

<script id="summary_tmpl" type="text/template">
  {{for (var i in it) { }}
  <tr class="row">
    <td>{{=it[i].schoolName}}</td>
    <td>{{=it[i].studentNum}}</td>
    <td>{{=it[i].compositeScores}}</td>
    <td>{{=it[i].compositeRanking}}</td>
    {{for (var j in it[i].subjectDetailsDTOList){ }}
    <td>{{=it[i].subjectDetailsDTOList[j].averageScore}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].maxScore}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].minScore}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].passNum}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].passRate}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].excellentNumber}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].excellentRate}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].compositeScores}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].compositeRanking}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].biJun}}</td>
    <td>{{=it[i].subjectDetailsDTOList[j].chaoJun}}</td>
    {{} }}
  </tr>
  {{ }}}
</script>

<script id="totalsummary_tmpl" type="text/template">
  <tr class="row">
    <td>{{=it.schoolName}}</td>
    <td>{{=it.studentNum}}</td>
    <td></td>
    <td></td>
    {{for (var j in it.subjectDetailsDTOList){ }}
    <td>{{=it.subjectDetailsDTOList[j].averageScore}}</td>
    <td>{{=it.subjectDetailsDTOList[j].maxScore}}</td>
    <td>{{=it.subjectDetailsDTOList[j].minScore}}</td>
    <td>{{=it.subjectDetailsDTOList[j].passNum}}</td>
    <td>{{=it.subjectDetailsDTOList[j].passRate}}</td>
    <td>{{=it.subjectDetailsDTOList[j].excellentNumber}}</td>
    <td>{{=it.subjectDetailsDTOList[j].excellentRate}}</td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    {{} }}
  </tr>
</script>

<script id="areaStuScore_tmpl" type="text/template">
  {{for (var i in it) { }}
  <tr>
    <td>{{=it[i].areaRanking}}</td>
    <td>{{=it[i].stuName}}</td>
    {{for (var j in it[i].subScore) { }}
    <td>{{=it[i].subScore[j].value}}</td>
    {{} }}
    <td>{{=it[i].scoreSum}}</td>
    <td>{{=it[i].schName}}</td>
    <td>{{=it[i].schRanking}}</td>
  </tr>
  {{ }}}
</script>

<script id="distriTitle_tmpl" type="text/template">
  <tr class="row1">
    <td class="col1"><em>学校</em></td>
    <td><em>最高分</em></td>
    {{for (var i in it) { }}
    <td><em>{{=it[i].beginScore}}-{{=it[i].endScore}}</em></td>
    {{ }}}
  </tr>
</script>

<script id="distriBody_tmpl" type="text/template">
  {{for (var i in it) { }}
  <tr>
    <td>{{=it[i].schoolName}}</td>
    <td>{{=it[i].maxScore}}</td>
    {{ var totalStuNum = 0;}}
    {{for (var j in it[i].scoreSection) { }}
    {{totalStuNum += it[i].scoreSection[j].num;}}
    {{ }}}
    {{for (var j in it[i].scoreSection) { }}
    {{ var per = it[i].scoreSection[j].num * 100 / totalStuNum;}}
    <td>{{=it[i].scoreSection[j].num}} ({{=formate(per)}}%)</td>
    {{ }}}
  </tr>
  {{} }}
</script>

<script id="distriTitle_tmpl1" type="text/template">
    {{for (var i in it) { }}
    <tr><td>{{=it[i].beginScore}}-{{=it[i].endScore}}</td></tr>
    {{ }}}
</script>

<script>
  function formate(num){
    return num.toFixed(2);
  }
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/chengji/0.1.0/jointExam');
</script>
</body>
</html>
