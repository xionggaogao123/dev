<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  pageContext.setAttribute("basePath",basePath);
  int userRole = new BaseController().getSessionValue().getUserRole();
  String currnetUserId = new BaseController().getSessionValue().getId();
  boolean isAdmin = UserRole.isHeadmaster(userRole) || UserRole.isK6ktHelper(userRole);
  boolean isEdu = UserRole.isEducation(userRole);
  boolean isStu = UserRole.isStudent(userRole);
  boolean isMaster = UserRole.isLeaderClass(userRole);
  boolean baseCanEdit = false;
  if(isAdmin || isMaster || isStu){
    baseCanEdit = true;
  }
  boolean rCanEdit = false;
  if(isAdmin){
    rCanEdit = true;
  }
%>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技-学籍管理</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="<%=basePath%>static_new/css/growRecord.css?v=2015041602" rel="stylesheet" />
  <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<script>
  var PUBLIC_USER_ID = "<%=currnetUserId%>";
  var schools = ${schools};
  var isEdu = "<%=isEdu%>";
  var rCanEdit = "<%=rCanEdit%>";
  var isStudent = ${isStudent};

</script>
<style>
  .pop-wrap {
    width: 740px !important;
    background: #fff;
    position: absolute;
    top: 30px;
    left: 50%;
    margin-left: -370px !important;
    z-index: 99;
    display: none;
  }
  .blue-btn{
      background: #0294E2 !important;
  }
    .vo-gray-btn{
        background: #E5E5E5 !important;
        color: #464646 !important;
        border: 1px solid #C9C9C9 !important;
    }
    .pop-title{
        color:#0294E2 !important;
    }
    .pop-btn span.active {
        background: #0294E2 !important;
        border: 1px solid #0294E2 !important ;
    }
</style>
<body>


<!--#head-->
<%@ include file="../common_new/head-cloud.jsp"%>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <!--.col-left-->
  <%--<%@ include file="../common_new/col-left.jsp" %>--%>
  <!--/.col-left-->

  <!--.col-right-->
  <div class="col-right"  style="width: 1000px;">

    <!--.banner-info-->
    <!--/.banner-info-->

    <div class="grow-col">

      <div class="grow-col-head clearfix">
        <h2>学籍管理</h2>
        <%if(isStu){ %>
        <a href="/growth/qualityDetail.do?id=<%=currnetUserId%>">素质教育详情</a>
        <a href="/growth/scoreDetail.do?id=<%=currnetUserId%>">成绩单</a>
        <%}else{ %>
        <!--  <a href="/growth/list.do">成长档案</a> -->
        <%} %>
      </div>

      <div class="grow-select" id="querySelectionArea" <% if(isStu){ %> style="display:none" <% } %>>

        <select id="schoolSelection" <% if(!isEdu){ %> style="display:none" <% } %>>
        </select>

        <select id="gradeSelection" class="vo-sel1" >
          <c:if test="${isStudent}">'
            <option value="${gradeMap.id}">${gradeMap.name}</option>
          </c:if>
        </select>
        <select id="classSelection" class="vo-sel1">
          <c:if test="${isStudent}">
            <option value="${classMap.id}">${classMap.name}</option>
          </c:if>
        </select>
        <input type="text" id="keywordInput"/>
        <button type="button" class="gray-sm-btn vo-gray-btn" id="searchBtn">搜索</button>
      </div>

      <table class="gray-table thin-gray" width="100%">
        <thead>
        <th width="96">学生姓名</th>
        <th width="150">学籍号</th>
        <th width="55">学号</th>
        <th width="120">年级/班级</th>
        <th>操作</th>
        </thead>
        <tbody id="listArea">

        </tbody>
      </table>
    </div>
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

<!--.编辑基本信息-->
<% if(baseCanEdit){ %>
<div class="pop-wrap" id="editSchoolRoll">
  <div class="pop-title">编辑基本信息</div>
  <input type="hidden" id="baseInfoIdInput"/>
  <div class="pop-content">
    <table class="schoolRoll-table">
      <tr>
        <td align="right" width="100">姓名</td>
        <td width="210"><input type="text" id="baseUserName" <% if(isStu){ %> readonly="readonly" <% }%> /></td>
        <td align="right" width="80">学号</td>
        <td><input type="text" id="baseStudentNumber" <% if(isStu){ %> readonly="readonly" <% }%>/></td>
      </tr>
      <tr>
        <td align="right">年级</td>
        <td>
          <select id="baseGrade" <% if(isStu){ %> disabled="disabled" <% }%>>
          </select>
        </td>
        <td align="right">班级</td>
        <td>
          <select id="baseClass" <% if(isStu){ %> disabled="disabled" <% }%>>
          </select>
        </td>
      </tr>
      <tr>
        <td align="right">性别</td>
        <td>
          <select id="baseSex">
            <option value="1">男</option>
            <option value="0">女</option>
          </select>
        </td>
        <td align="right">出生日期</td>
        <td>
          <input type="text" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate"  id="baseBirthday"/>
        </td>
      </tr>
      <tr>
        <td align="right">民族</td>
        <td>
          <select id="baseRace">
            <option value="汉族">汉族</option>
            <option value="其他">其他</option>
          </select>
        </td>
        <td align="right">健康状态</td>
        <td>
          <select id="baseHealth">
            <option value="健康">健康</option>
            <option value="一般">一般</option>
            <option value="较差">较差</option>
            <option value="残疾">残疾</option>
          </select>
        </td>
      </tr>
      <tr>
        <td align="right" valign="top">现在住址</td>
        <td><textarea id="baseAddressNow"></textarea></td>
        <td align="right" valign="top">户籍地址</td>
        <td><textarea id="baseAddressR"></textarea></td>
      </tr>
      <tr>
        <td align="right">联系电话</td>
        <td><input type="text" id="basePhone"/></td>
        <td align="right">电子邮箱</td>
        <td><input type="text" id="baseEmail"/></td>
      </tr>
    </table>
  </div>
  <div class="pop-btn"><span class="active" id="baseConfirmBtn">确定</span><span id="baseCloseBtn">取消</span></div>
</div>
<% }else{ %>
<div class="pop-wrap" id="editSchoolRoll">
  <div class="pop-title">基本信息</div>
  <input type="hidden" id="baseInfoIdInput"/>
  <div class="pop-content">
    <table class="schoolRoll-table">
      <tr>
        <td align="right" width="100">姓名</td>
        <td width="210"><input type="text" id="baseUserName" readonly="readonly"/></td>
        <td align="right" width="80">学号</td>
        <td><input type="text" id="baseStudentNumber" readonly="readonly"/></td>
      </tr>
      <tr>
        <td align="right">年级</td>
        <td>
          <select id="baseGrade" disabled="disabled">
            <option>请选择</option>
          </select>
        </td>
        <td align="right">班级</td>
        <td>
          <select id="baseClass" disabled="disabled">
            <option>请选择</option>
          </select>
        </td>
      </tr>
      <tr>
        <td align="right">性别</td>
        <td>
          <select id="baseSex" disabled="disabled">
            <option value="1">男</option>
            <option value="0">女</option>
            <option value="-1">未知</option>
          </select>
        </td>
        <td align="right">出生日期</td>
        <td>
          <input type="text"  id="baseBirthday" readonly="readonly"/>
        </td>
      </tr>
      <tr>
        <td align="right">民族</td>
        <td>
          <select id="baseRace" disabled="disabled">
            <option value="汉族">汉族</option>
            <option value="其他">其他</option>
          </select>
        </td>
        <td align="right">健康状态</td>
        <td>
          <select id="baseHealth" disabled="disabled">
            <option value="健康">健康</option>
            <option value="一般">一般</option>
            <option value="较差">较差</option>
            <option value="残疾">残疾</option>
          </select>
        </td>
      </tr>
      <tr>
        <td align="right" valign="top">现在住址</td>
        <td><textarea id="baseAddressNow" readonly="readonly"></textarea></td>
        <td align="right" valign="top">户籍地址</td>
        <td><textarea id="baseAddressR" readonly="readonly"></textarea></td>
      </tr>
      <tr>
        <td align="right">联系电话</td>
        <td><input type="text" id="basePhone" readonly="readonly"/></td>
        <td align="right">电子邮箱</td>
        <td><input type="text" id="baseEmail" readonly="readonly"/></td>
      </tr>
    </table>
  </div>
  <div class="pop-btn onlyCloseBtnDiv"><span id="baseCloseBtn" style="width:175px;">关闭</span></div>
</div>
<%	} %>
<!--/.编辑基本信息-->

<!--.学籍变动-->
<div class="pop-wrap" id="changeSchoolRoll">
  <div class="pop-title">学籍变动</div>
  <input type="hidden" id="RInfoIdInput"/>
  <div class="pop-content">
    <table class="changeRollTable">
      <% if(rCanEdit) {%>
      <thead>
      <th colspan="4">当前情况</th>
      </thead>
      <%} %>
      <tbody>
      <tr>
        <td width="70">姓名</td>
        <td width="245"><input type="text"  readonly="readonly" id="preRName"/></td>
        <td width="70">学号</td>
        <td><input type="text" readonly="readonly" id="preRStudentNumber"/></td>
      </tr>
      <tr>
        <td>年级</td>
        <td>
          <select id="preRGrade" disabled="disabled">
          </select>
        </td>
        <td>班级</td>
        <td>
          <select id="preRClass" disabled="disabled">
          </select>
        </td>
      </tr>
      <td width="70">变动日期</td>
      <td width="245"><input type="text"  readonly="readonly" id="preRDate"/></td>
      <td>新学校</td>
      <td >
        <input type="text" id="preRSchool" readonly="readonly"/>
      </td>

      </tr>
      <tr>
        <td>变动说明</td>
        <td colspan="3">
          <textarea readonly="readonly" id="preRContent"></textarea>
        </td>
      </tr>
      </tbody>
    </table>
    <% if(rCanEdit){ %>
    <table class="changeRollTable">
      <thead>
      <th colspan="4">变动情况</th>
      </thead>
      <tbody>
      <tr>
        <td width="70">变动日期</td>
        <td width="245"><input type="text" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate"  id="newRDate"/></td>
        <td width="70">新学号</td>
        <td><input type="text" id="newRStudentNumber"/></td>
      </tr>
      <tr>
        <td>新年级</td>
        <td>
          <select id="newRGrade">
          </select>
        </td>
        <td>新班级</td>
        <td>
          <select id="newRClass">
          </select>
        </td>
      </tr>
      <tr>
        <td>新学校</td>
        <td >
          <input type="text" id="newRSchool"/>
        </td>
      </tr>
      <tr>
        <td>变动说明</td>
        <td colspan="3">
          <textarea id="newRContent"></textarea>
        </td>
      </tr>
      </tbody>
    </table>

    <%} %>
  </div>
  <% if(rCanEdit){ %>
  <div class="pop-btn"><span class="active" id="RCommitBtn">确定</span><span id="RCloseDialogBtn">取消</span></div>
  <%}else{ %>
  <div class="pop-btn onlyCloseBtnDiv"><span id="RCloseDialogBtn" style="width:175px;">关闭</span></div>
  <%} %>
</div>
<!--/.学籍变动-->

<div class="bg-dialog"></div>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('roll')
</script>
</body>
</html>