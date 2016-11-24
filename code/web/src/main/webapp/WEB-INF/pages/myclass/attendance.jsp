<%--
  Created by IntelliJ IDEA.
  User: yan
  Date: 2015/7/17
  Time: 18:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>复兰科技--考勤管理</title>
    <link rel="stylesheet" type="text/css" href="/static_new/css/myclass/attendance.css">
    <link rel="stylesheet" type="text/css" href="/static/css/activity/rome.css"/>

    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/activity/rome.js"></script>

    <script src="/static_new/js/modules/core/0.1.0/doT.min.js?v=1"></script>
</head>
<body classid="${classId}" type="0">
<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div class="c-manage-main">
        <%@ include file="../common_new/col-left.jsp" %>
        <%@ include file="../common/right.jsp" %>
        <div class="right-border">
            <div class="c-manage-right" style="width: 770px;float: right">
                <ul>
                    <li><a href="/myclass/statstu/${classId}">班级人员</a></li>
                    <li class="current">班级考勤</li>
                    <c:if test="${master == 1}">
                    <li><a href="/myclass/stuinteclasscountpage.do?classId=${classId}">选课去向</a></li>
                    </c:if>
                </ul>

                <div class="maincontent" style="width:700px">
                    <h4>学生名单</h4>

                    <script id="nameSheet" type="text/x-dot-template">
                        <dt>{{=it.className}}</dt>
                        <dd>
                            {{~it.studentList:v:index}}
					<span>
						{{?index==0}}
						<label for="rm{{=index}}"><input type="radio" class="nameSheet_radio" checked id="rm{{=index}}"
                                                         name="mz" uid="{{=v.id}}" uname="{{=v.userName}}"/>{{=v.userName}}</label>

						{{??index!=0}}
						<label for="rm{{=index}}"><input type="radio" class="nameSheet_radio" id="rm{{=index}}"
                                                         name="mz" uid="{{=v.id}}" uname="{{=v.userName}}"/>{{=v.userName}}</label>
						{{??}}
						{{?}}
					</span>
                            {{~}}
                        </dd>
                    </script>
                    <dl id="nameSheet_dl">
                    </dl>
                    <h4>学生考勤记录表</h4>

                    <div class="hou">
                        <h3 id="attendanceName">张三同学考勤记录表</h3>
                        <table width="100%">
                            <thead>
                            <tr>
                                <th>缺勤日期</th>
                                <th>时间</th>
                                <th>备注</th>
                                <th>操作</th>
                            </tr>
                            </thead>

                            <script id="attendanceSheet" type="text/x-dot-template">
                                {{~it:v:index}}
                                <tr>
                                    <td>{{=v.date}}</td>
                                    <td>
                                        {{=v.time==0?"全天":(v.time==1?"上午":"下午")}}
                                    </td>
                                    <td>{{=v.remark}}</td>
                                    <td><span aid="{{=v.id}}" class="deleteBtnClass">删除</span></td>
                                </tr>
                                {{~}}
                            </script>
                            <tbody id="attendanceSheetBody">
                            </tbody>
                            <tbody>
                            <tr>
                                <td>
                                    <%--<label for="dt">缺勤日期</label>--%>
                                    <input type="text" id="dt" readonly="true" class="input">

                                </td>
                                <td>
                                    <select name="" id="time">
                                        <option value="0">全天</option>
                                        <option value="1">上午</option>
                                        <option value="2">下午</option>
                                    </select>
                                </td>
                                <td>
                                    <input type="text" id="remark" placeholder="备注"
                                           style="width:200px;text-indent: 0.5em">
                                </td>
                                <td><span id="saveClick">添加</span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../common_new/foot.jsp" %>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use("attendance", function (attendance) {
        attendance.attendance();
    });
</script>
</body>
</html>
