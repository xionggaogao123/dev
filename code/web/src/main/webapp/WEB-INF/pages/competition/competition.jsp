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
    boolean isAdmin = UserRole.isManager(new BaseController().getSessionValue().getUserRole());
%>
<!DOCTYPE html>
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
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">

    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery ui styles -->
    <link href="<%=basePath%>static/css/jquery-ui.min.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/pinbi.css?v=2015071201" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<script>
	var jsonObjAll = ${message};
	var grades = jsonObjAll.grades;
	var classes = jsonObjAll.classes;

</script>
<body>


    <!--#head-->
    <%@ include file="../common_new/head.jsp" %>
    <!--/#head-->

    <!--#content-->
    <div id="content" class="clearfix">


        <!--.col-left-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--/.col-left-->

        <!--.banner-info-->
        <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>
        <!--/.banner-info-->
        
        <!--.col-right-->
        <div class="col-right">



            <!--.tab-col-new-->
            <div class="tab-col-new">
                <div class="tab-head-new clearfix competitions">
                    <ul>
                        <li >
                            <i></i>
                            <a href="#huizongTab" id="huizongTabTab">综合评比汇总</a>
                            <em></em>
                        </li>
                        <li >
                            <i></i>
                            <a href="#importTab" id="importTabTab">综合评比分数录入</a>
                            <em></em>
                        </li>
                        <li class="cur">
                            <i></i>
                            <a href="#configTab" id="reportTabTab">综合评比表设置</a>
                            <em></em>
                        </li>
                    </ul>
                </div>
                <!-- 汇总TAB -->
                <div class="tab-main-new" >
                    <div class="tab-main-content none" id="huizongTab">
                        <div class="huizong clearfix" >
                            <dl>
                               <%-- <dt>筛选</dt>--%>
                                <dd>
                                    <label>
                                        <span>学期</span>
                                        <select id="termTypeSelection">
                                            <option value="2015年第二学期">2015年第二学期</option>
                                        </select>
                                    </label>
                                </dd>
                                <dd>
                                    <label>
                                        <span>评比</span>
                                        <select id="competitionSelectionForReport">
                                            <option value="综合评比1">综合评比1</option>
                                        </select>
                                    </label>
                                </dd>
                                <dd  class="lastChild">
                                    <label>
                                        <span>年级</span>
                                        <select id="gradeSelectionForReport">
                                            <%--<option value="全部">全部</option>--%>
                                        </select>
                                    </label>
                                </dd>
                            </dl>
                        </div>
                        <div class="huizong-list" style="display: none;">
                           <%-- <h5>列表</h5>--%>
                            <div class="huizong-list-button clearfix">
                                 <button id="exportDetailBtn">导出明细</button>
                                 <button id="showDetailBtn">显示明细</button>
                                 <button id="exportReportBtn">导出汇总</button>
                            </div>
                            <table>
                                <thead id="reportTableHead">
                                    
                                </thead>
                                <tbody id="reportTableList">
                                   
                                </tbody>
                            </table>
                        </div>
                        <!-- 分页div -->
					 	<div class="new-page-links" id="reportPageDiv"></div>
                    </div>
                    <!-- 录入 -->
                    <div class="tab-main-content none" id="importTab">
                         <div class="huizong clearfix">
                            <dl>
                               <%-- <dt>筛选</dt>--%>
                                <dd>
                                    <label>
                                        <span>学期</span>
                                        <select id="inputTermTypeSelection">
                                            <option value="2015年第二学期">2015年第二学期</option>
                                        </select>
                                    </label>
                                </dd>
                                <dd>
                                    <label>
                                        <span>评比</span>
                                        <select id="inputCompetitionSelection">
                                            <option value="综合评比1">综合评比1</option>
                                        </select>
                                    </label>
                                </dd>
                                <dd class="lastChild">
                                    <label>
                                        <span>批次</span>
                                        <select id="inputBatchSelection">
                                            <option value="全部">全部</option>
                                        </select>
                                    </label>
                                </dd>
                            </dl>
                             <dl style="display:inline-block;margin-bottom: 15px;">
                                 <dd>
                                     <label>
                                         <span>年级</span>
                                         <select id="inputGradeSelection">
                                             <%--<option value="全部">全部</option>--%>
                                         </select>
                                     </label>
                                 </dd>
                             </dl>
                         </div>
                        <dl class="luru-detail" style="display:none;">
                            <dt>基本信息</dt>
                            <dd><label>评比名称:</label > <label id="inputCompetitionNameLabel">综合评比1</label></dd>
                            <dd><label>说明： </label> <label id="inputCompetitionPostscriptLabel">说明说明，说明</label></dd>
                            <dd><label>参与范围：   </label> <label id="inputCompetitionRangeLabel">初一</label></dd>
                            <dd><label>批次数：</label>  <label id="inputCompetitionBatchLabel">共 6 批次 </label></dd> 
                        </dl>
                        <div class="compeitition-score">
                            <table >
                                <tbody id="inputCompetititonScoreTable">
                                </tbody>
                            </table>
                        </div>
                        <!-- 分页div -->
					 	<div class="new-page-links" id="inputPageDiv"></div>
                    </div>
                    <!-- 设置 -->
                    <div class="tab-main-content" id="configTab"> 
                        <div class="huizong clearfix">
                            <dl>
                              <%--  <dt>筛选</dt>--%>
                                <dd>
                                    <label>
                                        <span>学期</span>
                                        <select id="editTermType">
                                        </select>
                                    </label>
                                </dd>


                            </dl>
                         </div>
                         <div class="join-button clearfix"><button id="addCompetitionBtn">添加评比</button></div>
                        <div class="pingbi-mian">
                            <table class="pingbi-set">
                                <thead>
                                     <tr>
                                         <th width="56">#</th>
                                         <th width="132">评比名称 </th>
                                         <th width="88">说明</th>
                                         <th width="145"> 参与范围 </th>
                                         <th  width="99">评比项目</th>
                                         <th width="110">批次数</th>
                                         <th width="125">操作</th>
                                     </tr>
                                </thead>
                                <tbody id="competitionList">
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <!-- 评比设置 -->
                    <div class="tab-main-content" id="config2Tab">
                        <div class="config2Tab-top">
                            <a class="back-configtab1">&lt;返回评比表</a>
                            <%--<span class="pinbi-tit">设置评比项目</span>--%>
                            <button class="config-add" id="editCompetitionItemAddBtn">添加评比项目</button>
                        </div>
                        <div class="competition-main">
                            <table class="table-config">
                            <thead>
                            <tr>
                                <th class="th1">#</th>
                                <th class="th2">评比项目名称</th>
                                <th class="th3">说明</th>
                                <th class="th4">满分</th>
                                <th class="th5">操作</th>
                            </tr>
                            </thead>
                            <tbody id="editItemList">
                                <tr>
                                    <td>1</td>
                                    <td>评比项目</td>
                                    <td>说明</td>
                                    <td>100</td>
                                    <td>
                                        <span class="span-cur span-detail">明细</span>
                                        <i class="i-l"></i>
                                        <span class="span-cur item-del">删除</span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        </div>
                    </div>
                </div>
                    
                </div>
            </div>
            <!--/.tab-col-new-->

        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->

    <!--#foot-->
   <%@ include file="../common_new/foot.jsp" %>
    <!--#foot-->
    <!-- 综合评比1明细 -->
    <div class="pop-wrap hz-pop hide" id="detailDialog">
        <h5>综合评比1明细<label></label></h5>
        <div class="hz-pop-button clearfix"><button id="detailInnerBtn">导出明细</button></div>
        <div class="hz-pop-content">
            
            <div class="list-wrap">
                <table>
                <tbody id="detailTableBody">
                   
                </tbody>
            </table>
            </div>
            
        </div>
        <div class="edit-close">关闭</div>
    </div>
    <!-- 综合评比1明细 -->
    <!-- 编辑评比 -->
    <div class="pop-wrap edit-complete hide" id="editCompetitionWindow">
        <h5>编辑评比</h5>
        <div class="edit-complete-con">
            <div class="edit-title"><label>评比名称</label><input value="" id="editCompetitionName"></div>
            <div class="edit-title"><label>说明</label><input value="" id="editCompetitionPostscript"></div>
            <dl class="choice-list clearfix">
                <dt>参与范围</dt>
                <dd id="editRange">
                </dd>
            </dl>
            <div class="edit-title"><label>流动红旗数</label><input value="0" id="editRedFlagNum"></div>
            <div class="eidt-complete-button">
                <button class="active" id="editCompetitionConfirm">确定</button>
                <button>取消</button>
            </div>
        </div>
    </div>
    <!-- 编辑评比 -->
    <!-- 批次设置 -->
    <div class="pop-wrap edit-complete hide" id="editCompetitionBatchWindow">
        <h5>批次设置</h5>
         <div class="edit-complete-con">
             <div class="pici-set">
                 <label>批次数</label>
                 <input type="text" id="editBatchNum">
                 <button id="editBatchConfirmBtn">确认</button>
             </div>
             <table class="pici-table">
                <thead>
                    <tr>
                        <th width="40">#</th>
                        <th width="223"> 批次名称 </th>
                    </tr>
                </thead>
                <tbody id="editBatchList">
                </tbody>
             </table>
         </div>
         <div class="eidt-complete-button mr160">
                <button class="active" id="editBatchSaveBtn">保存</button>
                <button>取消</button>
            </div>
    </div>
     <!-- 批次设置 -->
     <!-- 设置评比项目 -->
     <%--<div class="pop-wrap edit-complete hide" id="editCompetitionItemWindow">
        <h5>设置评比项目</h5>
         <div class="set-item">
             <h6><button id="editCompetitionItemAddBtn">添加</button></h6>
             <table>
                 <thead>
                     <tr>
                         <th width="42"># </th>
                         <th width="139">评比项目名称</th>  
                         <th width="70">说明</th>
                         <th width="80">满分</th>
                         <th width="70">操作</th>
                     </tr>
                 </thead>
                 <tbody id="editItemList">
                     
                 </tbody>
             </table>
             <div class="edit-close">关闭</div>
         </div>
         
    </div>--%>
     <!-- 设置评比项目 -->
      <!-- 添加评比 -->
    <div class="pop-wrap edit-complete hide" id="addCompetitionWindow">
        <h5>添加评比</h5>
        <div class="edit-complete-con">
            <div class="edit-title"><label>评比名称</label><input value="" id="addCompetitionName"></div>
            <div class="edit-title"><label>说明</label><input value="" id="addCompetitionPostscript"></div>
            <dl class="choice-list clearfix">
                <dt>参与范围</dt>
                <dd id="addRange">
                    
                </dd>
            </dl>
            <div class="edit-title"><label>流动红旗数</label><input value="0" id="addRedFlagNum"></div>
            <div class="eidt-complete-button">
                <button class="active" id="addCompetitionConfirm">确定</button>
                <button>取消</button>
            </div>
        </div>
    </div>

    <!--评比明细弹窗-->
    <div class="wind-pbdetail">
        <div class="xmdetial">明细设置</div>
        <input type="hidden" id="detailIdsStr" name="detailIdsStr" value=""/>
        <dl class="dl-padetial">
            <dd id="pingbi-all">
            </dd>
            <dd>
                <button class="add-pb">添加明细项</button>
            </dd>
            <dd>
                <button id="btn-ok" class="btn-ok">确定</button>
                <button id="btn-no" class="btn-no">取消</button>
            </dd>
        </dl>
    </div>

    <!--详情弹窗-->
    <div class="wind-pbdetail2">
        <div class="xmdetial">项目明细</div>
        <dl class="dl-padetial">
            <dd id="inputItemDetail" class="clearfix">

            </dd>
            <dd>
                <button id="btn-ok2" class="btn-ok">确定</button>
                <button id="btn-no2" class="btn-no">取消</button>
            </dd>
        </dl>
    </div>
    <div class="bg"></div>

    <!-- 添加评比 -->
	<!-- 导出评比汇总表用表单 -->
	<form action="/competition/exportReport.do" method="post" id="exportFormReport" style="display:none">
		<input type="text"  id="competitionIdFormReport" name="competitionId"/>
		<input type="text" id="gradeIdFormReport" name="gradeId"/>
	</form>
	<!-- 导出评比明细表用表单 -->
	<form action="/competition/exportDetail.do" method="post" id="exportFormDetail" style="display:none">
		<input type="text"  id="competitionIdFormDetail" name="competitionId"/>
		<input type="text" id="gradeIdFormDetail" name="gradeId"/>
	</form>
	<div class="bg-dialog"></div>
    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
    <script src="<%=basePath%>static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('competition');
    </script>
</body>
</html>