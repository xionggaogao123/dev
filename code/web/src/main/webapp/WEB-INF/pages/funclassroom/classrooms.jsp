<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController"%>
<%@ page import="com.pojo.user.UserRole"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com"%>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("basePath", basePath);
	int userRole = new BaseController().getSessionValue().getUserRole();
	boolean isAdmin = UserRole.isFunctionRoomManager(userRole) || UserRole.isManager(userRole);
%>

<html>
<head>
<!-- Basic Page Needs-->
<meta charset="utf-8">
<link rel="dns-prefetch" href="//source.ycode.cn" />
<title>复兰科技</title>
<meta name="description" content="">
<meta name="author" content="" />
<meta name="copyright" content="" />
<meta name="keywords" content="">
<meta name="viewport"
	content="width=device-width,initial-scale=1, maximum-scale=1">
<link rel="shortcut icon" href="" type="image/x-icon" />
<link rel="icon" href="" type="image/x-icon" />
<!-- css files -->
<!-- Normalize default styles -->
<link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
<!-- Custom styles -->
<link href="<%=basePath%>static_new/css/classManage.css"
	rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<script>
	
</script>
<body>


	<!--#head-->
	<%@ include file="../common_new/head.jsp"%>
	<!--/#head-->

	<!--#content-->
	<div id="content" class="clearfix">

		<!--.col-left-->
		<%@ include file="../common_new/col-left.jsp"%>
		<!--/.col-left-->

		<!--.col-right-->
		<div class="col-right">

			<!--.banner-info-->

			<!--/.banner-info-->

			<!--.tab-col-new-->
			<div class="tab-col-new">
				<div class="tab-head-new clearfix">
					<ul>
						<li class="cur" ><a href="#myReservation" id="myReservation123">我的预约</a></li>
						<li><a href="#classReservation" id="classReservation123">教室预约</a></li>
						<%--<%--%>
							<%--if (isAdmin) {--%>
						<%--%>--%>
						<c:if test="${count!=0}">
						<li id="updateList"><a href="#manageClass">我管理的教室</a></li>
						</c:if>
						<%--<%--%>
							<%--}--%>
						<%--%>--%>


					</ul>
				</div>
				<div class="tab-main-new clearfix">

					<!--#myReservation-->
						<div id="myReservation">
						<!--.表头固定的效果-->
						<div class="gray-table-box">
							<div class="searchDate">
								<label>选择日期</label> <input type="text" id="searchTime" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate">
									 <input type="button" id="searchBySearchTime" value="检索" class="search">
							</div>

							<div class="gray-table-body">
								<table class="gray-table" width="100%"  style="border:0">
									<thead>
										<th colspan="5">功能教室预约信息</th>
										<tr>
											<th class="theadbg">教室名称</th>
											<th class="theadbg">起始时间</th>
											<th class="theadbg">结束时间</th>
											<th class="theadbg">预约说明</th>
											<th class="theadbg">操作</th>
										</tr>
									</thead>
									<tbody id="functionClassRoomList">
									</tbody>
								</table>
								<div class="fenye ">
									<div class="inline clearfix">
										<div class="new-page-links" id="pageForFun"></div>
										<!-- <div class="pageSearch"  style="display:none">
											<input type="text"> <input type="submit"
												class="button" id="currentPageForAppoint" value="到该页">
										</div> -->  
									</div>
								</div>
							</div>
						</div>
						<!--.表头固定的效果-->
					</div>
						<!--/#myReservation-->


						<!--#classReservation-->
						<div id="classReservation" class="hide">

							<!--.表头固定的效果-->
							<div class="gray-table-box">

								<div class="gray-table-body" id="classMsg">
									<table class="gray-table" width="100%" style="border:0">
										<thead>
											<th colspan="4">功能教室预约信息</th>
											<tr>
												<th class="theadbg">序号</th>
												<th class="theadbg">教室名称</th>
												<th class="theadbg">管理者</th>
												<th class="theadbg">操作</th>
											</tr>
										</thead>
										<tbody id="classroomList">
										</tbody>
									</table>
									<div class="fenye ">
										<div class="inline clearfix">
											<div class="new-page-links"></div>
											<div class="pageSearch" style="display:none">
												<input type="text"> <input type="submit"
													class="button" id="currentPage" value="到该页">
											</div>
										</div>
								</div>
								</div>
								<!-- 查看预约 -->
								<div class="gray-table-body hide" id="lookReserv">
									<div class="index">
										<a href="#">教室列表</a>&gt;篮球馆
									</div>
									<div class="res">
										<label>教室预约信息</label> <a href="#" class="order">预约</a>
									</div>
									<table class="gray-table" width="100%" style="border:0">
										<thead>
											<th>起始时间</th>
											<th>结束时间</th>
											<th>使用者</th>
											<th>预约说明</th>
										</thead>
										<tbody id="AppointList">
										</tbody>
									</table>
									<div class="fenye ">
										<div class="inline clearfix">
											<div class="new-page-links"></div>
											<div class="pageSearch" style="display:none">
												<input type="text"> <input type="submit" value="到该页"
													class="button" id="goThisPage">
											</div>
										</div>
									</div>


								</div>
								<!-- 查看预约 -->
							</div>
							<!--.表头固定的效果-->
						</div>
						<!--/#classReservation-->

						<!--#manageClass-->
						<div id="manageClass" class="hide">
							<div class="gray-table-box">

								<div class="gray-table-body" id="manClassList">
									<table class="gray-table" width="100%" style="border:0">
										<thead>
											<th colspan="4">功能教室预约信息</th>
											<tr>
												<th class="theadbg">序号</th>
												<th class="theadbg">教室名称</th>
												<th class="theadbg">管理者</th>
												<th class="theadbg">操作</th>
											</tr>
										</thead>
										<tbody id="classRoomHead">

										</tbody>
									</table>
									<div class="fenye ">
										<div class="inline clearfix">
											<div class="new-page-links" id="pageFenye"></div>
											<div class="pageSearch" style="display:none">
												<input type="text"> <input type="text" class="button" value="到该页" id="toThisPage" style="cursor: hand">
											</div>
										</div>
									</div>


								</div>
								<!-- 我管理的教室-查看预约 -->
								<div class="gray-table-body hide" id="look-classMan">
									<div class="index" id="myClassName">
										<a href="#">教室列表</a>&gt;篮球馆
									</div>
									<div class="res">
										<label>教室预约信息</label> <a href="#" class="adminOrder">管理员预约</a>

									</div>
									<table class="gray-table" style="border:0" width="100%">
										<thead>
											<th>起始时间</th>
											<th>结束时间</th>
											<th>使用者</th>
											<th>预约说明</th>
											<th>操作</th>

										</thead>
										<tbody id="roomDetils">
										</tbody>
									</table>
									<div class="fenye">
										<div class="clearfix inline">
											<div class="new-page-links" id="detilPageFenye"></div>
											<div class="pageSearch" style="display:none">
												<input type="text"> <input type="submit"
													class="button" value="到该页" id="detilToPage" style="cursor: hand">
											</div>
										</div>
									</div>


								</div>
								<!-- 查看预约 -->
							</div>

						</div>
						<!--/#manageClass-->
					</div>
				</div>
				<!--/.tab-col-new-->

			</div>
			<!--/.col-right-->

		</div>
	</div>
	<!--/#content-->

	<!--#foot-->
	<%@ include file="../common_new/foot.jsp"%>
	<!--#foot-->
	<!-- 填写预约信息 -->

	<div class="pop-wrap" id="editReserv">
		<div class="pop-title">
			<span>编辑预约信息</span> <span class="closePop"></span>
		</div>

		<div class="pop-content">
			<table class="">
				<tr>
					<td class="right">使用者</td>
					<td><input id="eUser" type="text" disabled="disabled"></td>
				</tr>
				<tr>
					<td class="right">开始时间</td>
					<td><input id="eStartTime" type="text"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate"></td>
				</tr>
				<tr>
					<td class="right">结束时间</td>
					<td><input id="eEndTime" type="text"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate"></td>
				</tr>

				<tr>
					<td valign="top" class="right">使用说明</td>
					<td colspan="3"><textarea id="eReasons"></textarea></td>
				</tr>

			</table>
		</div>
		<div class="pop-btn">
			<a class="sure" href="#" id="sureSaveApp">确定</a> <a class="undo"
				href="#" id="cancelSaveApp">取消</a>
		</div>
	</div>
	<!-- 填写预约信息 -->
	<div class="pop-wrap" id="orderMsg">
		<div class="pop-title">
			<span>预约信息</span> <span class="closePop"></span>
		</div>

		<div class="pop-content">
			<table class="">
				<tr>
					<td class="right">开始时间</td>
					<td><input type="text"
						onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'%y-%M-%d'})"
						class="Wdate"></td>
				</tr>
				<tr>
					<td class="right">结束时间</td>
					<td><input type="text"
						onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'%y-%M-%d'})"
						class="Wdate"></td>
				</tr>

				<tr>
					<td valign="top" class="right">使用说明</td>
					<td colspan="3"><textarea></textarea></td>
				</tr>

			</table>
		</div>
		<div class="pop-btn">
			<a class="sure" href="#">确定</a> <a class="undo" href="#">取消</a>
		</div>
	</div>
	<!-- 我的预约编辑 -->
	<div class="pop-wrap" id="editMyAppointment">
		<div class="pop-title">
			<span>编辑我的预约</span> <span class="closePop"></span>
		</div>
		<div class="pop-content">
			<table class="">
				<tr>
					<td class="right">使用者</td>
					<td><input type="text" id="userNameId" disabled="disabled"
						value="管理员/老师"></td>
				</tr>
				<tr>
					<td class="right">开始时间</td>
					<!-- <td><input type="text" id="startTimeId" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'%y-%M-%d'})" class="Wdate"></td> -->
					<td><input type="text" id="startTimeId"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})"></td>
				</tr>
				<tr>
					<td class="right">结束时间</td>
					<!-- <td><input type="text" id="endTimeId" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'%y-%M-%d'})" class="Wdate"></td> -->
					<td><input type="text" id="endTimeId"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})"></td>
				</tr>

				<tr>
					<td valign="top" class="right">预约说明</td>
					<td colspan="3"><textarea id="useInfoId"></textarea></td>
				</tr>
			</table>
		</div>
		<div class="pop-btn">
			<a class="sure" href="javascript:void(0)" id="editSubmitfunctionBtn" flag=eid>确定</a> 
			<a class="undo" href="javascript:void(0)" id="editCloseBtn">取消</a>
		</div>
	</div>
	<!-- 点击预约 -->
	<div class="pop-wrap" id="order">
		<div class="pop-title">
			<span>预约信息</span> <span class="closePop"></span>
		</div>

		<div class="pop-content">
			<table class="">
				<tr>
					<td class="right">开始时间</td>
					<td><input id="startTime" type="text"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate"></td>
				</tr>
				<tr>
					<td class="right">结束时间</td>
					<td><input id="endTime" type="text"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate"></td>
				</tr>

				<tr>
					<td valign="top" class="right">使用说明</td>
					<td colspan="3"><textarea id="reasons"></textarea></td>
				</tr>

			</table>



		</div>
		<div class="pop-btn">
			<a class="sure" href="#" id="saveAppoint">确定</a> <a class="undo"
				href="#" id="cancelSave">取消</a>
		</div>
	</div>
	<!-- 点击预约 -->
	<!-- 管理员预约 -->
	<div class="pop-wrap" id="adminOrder">
		<div class="pop-title">
			<span>管理员预约</span> <span class="closePop"></span>
		</div>

		<div class="pop-content">
			<table class="">
				<tr>
					<td class="right">开始时间</td>
					<td><input type="text"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})"
						class="Wdate" id="detilStartTime"></td>
				</tr>
				<tr>
					<td class="right">结束时间</td>
					<td><input type="text"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})"
						class="Wdate" id="detilEndTime"></td>
				</tr>

				<tr>
					<td valign="top" class="right">使用说明</td>
					<td colspan="3"><textarea id="useReson"></textarea></td>
				</tr>

			</table>



		</div>
		<div class="pop-btn">
			<a class="sure" href="#" id="addRev">确定</a> <a class="undo" href="#"
				id="addCan">取消</a>
		</div>
	</div>
	<!-- 管理员预约 -->
	<!-- 管理员编辑信息 -->

	<div class="pop-wrap" id="manEdit">
		<div class="pop-title">
			<span>填写预约信息</span> <span class="closePop"></span>
		</div>

		<div class="pop-content">
			<table class="" id="detilId">
				<tr>
					<td class="right">使用者</td>
					<td><input id="userTr" type="text" disabled="disabled"></td>
				</tr>
				<tr>
					<td class="right">开始时间</td>
					<td><input type="text"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})"
						class="Wdate" id="startTimeTr"></td>
				</tr>
				<tr>
					<td class="right">结束时间</td>
					<td><input type="text"
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})"
						class="Wdate" id="endTimeTr"></td>
				</tr>

				<tr>
					<td valign="top" class="right">使用说明</td>
					<td colspan="3"><textarea id="resonTr"></textarea></td>
				</tr>

			</table>



		</div>
		<div class="pop-btn">
			<a class="sure" href="#" id="manSure">确定</a> <a class="undo" href="#"
				id="manCan">取消</a>
		</div>
	</div>
	<!-- 填写预约信息 -->
	<!-- 提示 -->
	<div class="pop-wrap" id="tip">
		<div class="pop-title">
			<span>提示</span> <span class="closePop"></span>
		</div>
		<div class="pop-content">
			<p id="revP">
				该时间段已有<span>3</span>人预约，是否要继续预约？
			</p>
			<table>
				<thead>
					<th width="124">起始时间</th>
					<th width="124">结束时间</th>
					<th width="76">使用者</th>
					<th>预约说明</th>
				</thead>
				</table>
				<div class="tbod">
					<table>
						<tbody id="inReservationDetils">
							<tr>
								<td>2015/12/01&nbsp;10:00</td>
								<td>2015/12/01&nbsp;10:00</td>
								<td>张老师</td>
								<td>上课</td>
							</tr>
						</tbody>
					</table>
				</div>
				
			
		</div>
		<div class="pop-btn">
			<a href="#" class="sure continue" id="continueRev">继续预约</a>
		</div>
	</div>
	<!-- 提示 -->

	<!-- 普通老师预约提示 -->
	<div class="pop-wrap" id="tip2">
		<div class="pop-title">
			<span>提示</span> <span class="closePop" style="display: none"></span>
		</div>
		<div class="pop-content">
			<p>
				该时间段已有<span id="tipCount"></span>人预约!
			</p>
			<table>
				<thead>
					<th width="124">起始时间</th>
					<th width="124">结束时间</th>
					<th width="76">使用者</th>
					<th>预约说明</th>
				</thead>
			</table>
			<div class="tbod">
				<table>
					<tbody id="tipBody">
					</tbody>
				</table>
			</div>
		</div>
		<div class="pop-btn">
			<a href="#" class="sure continue" id="getIt">确定</a>
		</div>
	</div>
	<!-- 普通老师预约提示結束 -->
	<!-- 普通老师編輯提示 -->
	<div class="pop-wrap" id="tip3">
		<div class="pop-title">
			<span>提示</span> <span class="closePop" style="display: none"></span>
		</div>
		<div class="pop-content">
			<p>
				该时间段已有<span id="tipCount3"></span>人预约
			</p>
			<table>
				<thead>
					<th width="124">起始时间</th>
					<th width="124">结束时间</th>
					<th width="76">使用者</th>
					<th>预约说明</th>
				</thead>
			</table>
				<div class="tbod">
					<table>
						<tbody id="tipBody3">
						</tbody>
					</table>
				</div>
			</table>
		</div>
		<div class="pop-btn">
			<a href="#" class="sure continue" id="getIt3">确定</a>
		</div>
	</div>
	<!-- 普通老师編輯提示結束 -->
	<div class="bg-dialog"></div>

	<!-- Javascript Files -->
	<!-- initialize seajs Library -->
	<script src="<%=basePath%>static_new/js/sea.js"></script>
	<!-- Custom js -->
	<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js"></script>
	<script>
		seajs.use('myAppointment');
		seajs.use('pagination');
		seajs.use('classAppoint');
		seajs.use('myManage');
	</script>
</body>
</html>
