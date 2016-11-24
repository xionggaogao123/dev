<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>学生考勤</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="/static_new/css/stuKq.css">
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    <!-- <script type="text/javascript" src="js/stuKq.js"></script> -->
</head>    
<body>
    <!--#head-->
    <%@include file="../common_new/head.jsp" %>
    <!--/#head-->

    <!--#content-->
    <div id="content" class="clearfix">

        <!--.col-left-->
        <%@include file="../common_new/col-left.jsp" %>
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
        	<c:set var="isMaster" value="${roles:isHeadmaster(sessionValue.userRole) }"></c:set>
        	<input id="masterFlag" type="hidden" value="${isMaster }"/>
            <!--.tab-col右侧-->
            <div class="tab-col">
                <div class="stukq-con">
                    <div class="new-title">学生考勤</div>
                    <div class="select-div">
                        <table>
                            <tr>
                            	<c:if test="${roles:isHeadmaster(sessionValue.userRole) }">
	                                <th style="width:115px;">年级</th>
                            	</c:if>
                                <th style="width:115px;">班级</th>
                                <th style="width:234px;">日期</th>
                                <th style="width:115px;">状态</th>
                                <th style="width:88px;">学生姓名</th>
                                <th style="width:78px;">&nbsp;</th>
                            </tr>
                            <tr>
                            	<c:if test="${roles:isHeadmaster(sessionValue.userRole) }">
                                	<td>
                                    	<select id="grade">
                                    	</select>
                                    	<script id="gradeTmpl" type="text/template">
										{{~it:value:index}}
										<option value="{{=value.id}}">{{=value.name}}</option>
                                        {{~}}
										</script>
                                	</td>
                            	</c:if>
                                <td>
                                    <select id="clazz">
                                        <option value="-1">全部班级</option>
                                    </select> 
                                    <script id="clazzTmpl" type="text/template">
									{{~it:value:index}}
                                      {{? value.classType == 0}}
										<option value="{{=value.id}}">{{=value.className}}</option>
									  {{?}}
									{{~}}
									</script>
                                </td>
                                <td>
                                    <input type="text" id="sDate" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy/MM/dd'})"/>-
                                    <input type="text" id="eDate" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
                                </td>
                                <td>
                                    <select id="type">
                                        <option value="-1">全部</option>
                                        <option value="1">事假</option>
                                        <option value="2">病假</option>
                                        <option value="9">其他</option>
                                    </select> 
                                </td>
                                <td>
                                    <input id="stuName" type="text" style="width:80px;"/>
                                </td>
                                <td>
                                    <button class="btn-cx">查询</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="stukq-main">
                        <div class="dc-wrap">
                            <button class="btn-dc fr">导出</button>
                        </div>
                        <table class="newTable">
                            <thead>
                                <tr>
                                    <th style="width:45px;">学号</th>
                                    <th style="width:98px;">姓名</th>
                                    <th style="width:98px;">考勤日期</th>
                                    <th style="width:120px;">班级</th>
                                    <th style="width:98px;">考勤状态</th>
                                    <th style="width:210px;">备注</th>
                                    <c:if test="${!roles:isHeadmaster(sessionValue.userRole) }">
                                    	<th style="width:80px;">操作</th>
                                    </c:if>
                                </tr>
                            </thead>
                            <tbody id="stuAttenInfo">
                            </tbody>
                            <script id="stuAttenInfoTmpl" type="text/template">
                            {{~it:value:index}}
							<tr id="{{=value.studentId}}">
                                    <td>{{=value.studentNumber}}</td>
                                    <td>{{=value.studentName}}</td>
                                    <td>{{=value.attendanceDate}}</td>
                                    <td>{{=value.clazzName}}</td>
                                    <td st="{{=value.attendanceStatus}}">
										{{? value.attendanceStatus == 0}}
                                        <img src="/img/stuAttendance/stukq-1.png">
										{{?? value.attendanceStatus == 1}}
										<img src="/img/stuAttendance/stukq-2.png">
										{{?? value.attendanceStatus == 2}}
										<img src="/img/stuAttendance/stukq-3.png">
										{{??}}
										<img src="/img/stuAttendance/stukq-4.png">
										{{?}}
                                    </td>
									<td class="spe-td">
									{{? value.remark != null}}
                                    {{=value.remark}}
									{{?}}
									</td>
									<c:if test="${!roles:isHeadmaster(sessionValue.userRole) }">
                                    <td>
                                        <a href="javascript:;" class="table-xg" onclick="showAttendanctBox('{{=value.studentId}}', '{{=value.id}}')">修改</a>
                                        <%--<em>|</em>
                                        <a href="javascript:;" class="table-set">批量考勤设置</a>--%>
                                    </td>
									</c:if>
                                </tr>
							{{~}}
                            </script>
                        </table>
                        <div class="info-msg">未查询出符合条件的结果</div>
                        <div class="new-page-links"></div>
                    </div>
                </div>  
            </div>
            <!--/.tab-col右侧-->

        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->
</div>
</div>
    <div class="xg-alert">
        <div class="alert-title clearfix">
            <p>修改学生考勤</p>  
            <span class="alert-close">x</span>
        </div>
        <div class="alert-main">
            <span class="alert-top"><em id="sn"></em><em id="attDate"></em></span>
            <div class="type-select">
                <span>状态</span>
                <select id="chStatus">
                	<option value="0">出勤</option>
                    <option value="1">事假</option>
                    <option value="2">病假</option>
                    <option value="9">其他</option>
                </select>
            </div>
            <div class="stukq-note">
                <span>备注</span>
                <textarea id="remark"></textarea>
            </div>
            <div class="alert-btn">
                <button class="alert-btn-sure">确定</button>
                <button class="alert-btn-qx">取消</button>
            </div>
        </div>   
    </div>
    <div class="bg"></div>
    <!--#foot-->
    <%@include file="../common_new/foot.jsp" %>
    <!--#foot-->

    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
  <script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
  <script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
  <script>
    seajs.use('/static_new/js/modules/studentattendance/0.1.0/stuKq.js');
    function showAttendanctBox(stuId, attendId) {
    	$('#remark').val("");
    	var _tds = $('#' + stuId).children('td');
    	var stuName = _tds.eq(1).html();
    	var date = _tds.eq(2).html();
    	var status = _tds.eq(4).attr('st');
    	var remark = _tds.eq(5).html();
    	$('#sn').text(stuName);
    	$('#attDate').text(date);
    	$('#chStatus').val(status);
    	$('#remark').val(remark);
    	$(".bg,.xg-alert").show();
    	$('.alert-btn-sure').on('click', function() {
    		changeAttendanceStatus(stuId, attendId);
    	});
    }
    function changeAttendanceStatus(stuId, attendId) {
    	attendId = attendId == 'null' ? '' : attendId;
    	var clazzId = $('#clazz').val();
    	var date = $('#attDate').text();
    	var remark = $('#remark').val();
    	console.log("remark_commit:" + remark)
    	var status = $('#chStatus').val();
    	$.ajax({
    		async : false,
    		cache : false,
    		dataType : 'json',
    		type : 'post',
    		url : '/stuAttendance/changeAttendance.do',
    		data : {
    			attendanceId : attendId,
    			studentId : stuId,
    			clazzId : clazzId,
    			remark : remark,
    			status : status,
    			attendanceDate : date
    		},
    		success : function(data) {
    			if(data.code == '200') {
    				$(".bg").hide();
    				$(".alert-main").parent().hide();
    				$('.btn-cx').click();
    				$('.alert-btn-sure').unbind('click');
    			}
    		}
    	});
    }
  </script>

</body>
</html>