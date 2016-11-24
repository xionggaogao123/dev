<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2016/6/16
  Time: 11:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>工资管理</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link rel="shortcut icon" href="" type="image/x-icon" />
  <link rel="icon" href="" type="image/x-icon" />
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="/static/css/expand.css?v=2015041602" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/gzgl/gongziguanli.css">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <script type="text/javascript">
       function scroll(obj){
           document.getElementById('headDiv').scrollLeft=obj.scrollLeft;
       }
    </script>
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
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <!--.col-right-->
  <div class="col-right">

    <div class="gzgl-con">
      <div class="gzgl-nav clearfix">
        <ul>
          <li><a href="/wages/gzgl.do">薪酬管理</a><em></em></li>
          <li><a href="/wages/wdgz.do">我的工资条</a><em></em></li>
          <li class="gzgl-active"><a href="javascript:;">工资统计</a><em></em></li>
        </ul>
      </div>
      <div class="gzgl-main">
        <div class="gzgl-tj-select">
          <%--<select class="gzgl-tj-large">--%>
            <%--<option>选择部门</option>--%>
          <%--</select>--%>
          <%--<select class="gzgl-tj-large">--%>
            <%--<option value="">全部老师</option>--%>
            <%--<c:forEach items="${users}" var="user">--%>
              <%--<option value="${user.id}">${user.name}</option>--%>
            <%--</c:forEach>--%>
          <%--</select>--%>
          <br>
          <br>
          <select class="change" id="salaryYear">
          </select>
          <em>年</em>
          <select class="change" id="salaryMonth">
            <option value="1">01</option>
            <option value="2">02</option>
            <option value="3">03</option>
            <option value="4">04</option>
            <option value="5">05</option>
            <option value="6">06</option>
            <option value="7">07</option>
            <option value="8">08</option>
            <option value="9">09</option>
            <option value="10">10</option>
            <option value="11">11</option>
            <option value="12">12</option>
          </select>
          <em>月</em>
          <em>第</em>
          <select class="change" id="salaryNumber">
          </select>
          <em>次</em>
            <input placeholder="请输入姓名" id="username" style="width: 100px;">
          <button id="seachSalary">查询</button>
        </div>
        <div class="gzgl-tj-daochu">
          <a class="gzgl-tj-dc" href="javascript:;"><img src="/img/gzgl-dc.png">导出</a>
          <a class="gzgl-tj-gzt" href="javascript:;"><img src="/img/gzgl-gzt.png">生成工资条</a>
          <form id="genTemplateForm">
            <input name="year" type="hidden" /> <input name="month"
                                                       type="hidden" /> <input name="num" type="hidden" />
            <input name="userId" type="hidden" />
          </form>
        </div>
        <div class="gzgl-gly-table">
          <div class="gly-table-title">
            <span>制表时间：<em id="createTime">2016/3/12</em></span>
            <p>单位：<em>元</em></p>
          </div>
          <div class="gzgl-table-wrap" id="mainDiv">
              <div class="gzgl-tab-left">
                  <table>
                      <thead id="theadTotal" style="display: none;">
                      <tr>
                          <th>序号</th>
                          <th>姓名</th>
                      </tr>
                      </thead>
                      <tbody id="salaryUserList">
                      </tbody>
                  </table>
                  <script type="text/template" id="salaryUserListTemp">
                      {{~it.salary:value:index1}}
                      <tr>
                          <td width="48px">{{=index1+1}}</td>
                          <td width="95px"><span title="{{=value.userName}}">{{=value.userName}}</span></td>
                      </tr>
                      {{~}}
                  </script>
              </div>
              <div class="gzgl-tab-right">
                  <div class="gzgl-tableth" id="headDiv">
                      <table>
                          <thead id="salaryHeaderBox">
                          </thead>
                      </table>
                  </div>
                  <div class="gzgl-tabletd" id="bodyDiv" onscroll="scroll(this)">
                      <table>
                          <tbody class="salary-body" id="salaryBodyBox">
                          </tbody>
                      </table>
                  </div>
              </div>
          </div>
        </div>
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
    <script type="text/template" id="salaryHeaderTemp">
      {{~it:value:index}}
      <tr>
        <%--<th rowspan=2 style="width:3%;">序号</th>--%>
        <%--<th rowspan=2 style="width:6%;">姓名</th>--%>
        <th colspan={{=value.send}} >应发工资</th>
        <th colspan={{=value.debit}}>扣款统计</th>
        <th rowspan=2 width="120px">实发工资</th>
        <th rowspan=2 width="120px">备注</th>
      </tr>
      <tr>
        {{~value.sendList:value2:index2}}
        <th width="120px"><span class="xcgl-val" title="{{=value2.itemName}}">{{=value2.itemName}}</span></th>
        {{~}}
        <th width="120px">小计</th>
        {{~value.debitList:value3:index3}}
        <th width="120px"><span class="xcgl-val" title="{{=value3.itemName}}">{{=value3.itemName}}</span></th>
        {{~}}
        <th width="120px">小计</th>
      </tr>
      {{~}}
    </script>
    <script type="text/template" id="salaryListTemp">
      {{~it.salary:value:index1}}
      <tr id="{{=value.id}}">
        <%--<td width="3%">{{=index1+1}}</td>--%>
        <%--<td width="6%" title="{{=value.userName}}"><em>{{=value.userName}}</em></td>--%>
        {{~value.salaryItem:value2:index2}}
        {{~value2.sendList:value3:index3}}
        <td width="120px" id="{{=value3.itemName}}" class='editableTd'>{{=value3.m}}</td>
        {{~}}
        <td width="120px">{{=value.ssStr}}</td>
        {{~value2.debitList:value4:index4}}
        <td width="120px" id="{{=value4.itemName}}" class='editableTd'>{{=value4.m}}</td>
        {{~}}
        <td width="120px">{{=value.msStr}}</td>
        {{~}}
        <td width="120px">{{=value.asStr}}</td>
        <td width="120px">{{=value.remark}}</td>
      </tr>
      {{~}}
    </script>
  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
<div class="gzgl-meng"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<script type="text/template" id="yearListTemp">
  {{~it:value:index}}
  <option value="{{=value}}">{{=value}}</option>
  {{~}}
</script>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  var yearList = ${yearList};
  var currYear = ${currYear};
  var currMonth = ${currMonth};
  seajs.use('gztj');
</script>
</body>
</html>
