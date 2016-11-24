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
    boolean isAdmin = UserRole.isManager(userRole) || UserRole.isHeadmaster(userRole) || UserRole.isK6ktHelper(userRole);
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
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles --> 
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery ui styles -->
    <link href="<%=basePath%>static_new/js/modules/treeView/0.1.0/jquery.treeview.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/baoxiuguanli.css?v=2015071201" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<script>
	var jsonObjAll = ${message};
	var departments = jsonObjAll.departments;
	var userObj = jsonObjAll.userInfo;
	var schoolId = jsonObjAll.schoolId; 
	var isAdmin = <%=isAdmin%>;
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

        <!--.col-right-->
        <div class="col-right">

            <!--.banner-info-->
            <!-- <img src="http://placehold.it/770x100" class="banner-info" /> -->
            <!--/.banner-info-->

            <!--.tab-col-new-->
            <div class="tab-col-new">
                <div class="tab-head-new clearfix">
                    <ul>
                        <li class="cur">
                            <i></i>
                            <a href="#bxsqId">报修申请</a>
                        </li>
                        <li>
                            <i></i>
                            <a href="#bxqjcxId">报修情况查询</a>
                        </li>
                        <li >
                            <i></i>
                            <a href="#bxclId">报修处理</a>
                        </li>
                    </ul>
                </div>
                <div class="tab-main-new clearfix">
                    <!--.报修申请-->
                    <div class="bxsq-view" id="bxsqId">
                        <form action="#" method="post">
                            <table>
                            <tr>
                                <td width="105">报修人：</td>
                                <td id="declareRepairPerson"></td>
                            </tr>
                            <tr>
                                <td>报修至部门：</td>
                                <td>
                                    <select id="repairDepartment">
                                    </select>
                                    
                                    
                                      <select id="repairDepartmentUser">
                                    </select>
                                </td>
                                
                              
                            </tr>
                            <tr>
                                <td>报修地点：</td>
                                <td>
                                    <input type="text" id="repairPlace"/>
                                </td>
                            </tr>
                            <tr>
                                <td>联系电话：</td>
                                <td>
                                    <input type="text"  id="phone"/>
                                </td>
                            </tr>
                            <tr>
                                <td>报修类别：</td>
                                <td>
                                    <select id="repairType">
                                        <option value="建筑内饰">建筑内饰</option>
                						<option value="电脑硬件">电脑硬件</option>
                						<option value="电脑软件">电脑软件</option>
                						<option value="办公用品">办公用品</option>
                						<option value="其他故障">其他故障</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td valign="top">报修描述：</td>
                                <td>
                                    <textarea id="repairContent"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td>
                                    <div>
                                        <button type="button" class="orange-btn" id="addRepairBtn">申请</button>
                                        <button type="button" class="gray-sm-btn" id="resetAddFromBtn">重置</button>
                                    </div>
                                </td>
                            </tr>
                        </table>
                        </form>

                    </div>
                    <!--/.报修申请-->

                    <!--.报修情况查询--> 
                    <div class="hide bxqjcx-view" id="bxqjcxId">

                        <div class="bxqjcx-tit">
                            <label>报修学期:
                                <select id="queryTermType">
                                	<c:forEach items="${list}" var="term">
                    					<option value="${term}">${term}</option>
               						</c:forEach>
                                <!-- 
                                	<option value="2015年第二学期">2015年第二学期</option>
                                	<option value="2015年第一学期">2015年第一学期</option>
                                	 -->
                                </select>
                            </label>
                            <label>报修状态:
                                <select id="queryRepairProcedure">
                                	<option value="ALL">全部状态</option>
                                	<option value="待处理">待处理</option>
                                	<option value="已受理">已受理</option>
                                	<option value="已完毕">已完毕</option>
                                </select>
                            </label>
                            <button type="button" class="gray-sm-btn" id="queryBtnForQueryView">查询明细</button>
                        </div>

                        <h3 id="queryTermTypeForList">报修明细 2015年第一学期</h3>

                        <table class="gray-table" width="100%">
                            <thead>
                                <th>报修日期</th>
                                <th>报修人</th>
                                <th>报修地点</th>
                                <th>内容</th>
                                <th>处理部门</th>
                                <th>进度</th>
                                <th>维修人</th>
                                <th>结果</th>
                                <th>评分反馈</th>
                                <th>操作</th>
                            </thead>
                            <tbody id="queryList">
                            </tbody>
                        </table>
                        <div class="fenye">
 					<div class="new-page-links" id="pageForView">
 					</div>
                    </div>
                    </div>
                    
                    <!--/.报修情况查询-->

                    <!--.报修处理-->
                    <div class="hide bxcl-view" id="bxclId">

                        <div class="bxqjcx-tit">
                            <label>报修学期:
                                <select id="handlerTermType">
                                	 <c:forEach items="${list}" var="term">
                    					<option value="${term}">${term}</option>
               						</c:forEach>
                                	<!--  
                                	<option value="2015年第二学期">2015学年第二学期</option>
                                	-->
                                </select>
                            </label>
                            <label>报修部门:
                                <select id="handlerDepartment">
                                	<option value="ALL">全部部门</option>
                                </select>
                            </label>
                            <label>保修状态:
                                <select id="handlerRepairProcedure">
                                	<option value="ALL">全部状态</option>
                                	<option value="待处理">待处理</option>
                                	<option value="已受理">已受理</option>
                                	<option value="已完毕">已完毕</option>
                                </select>
                            </label>
                            <button type="button" class="gray-sm-btn" id="queryHandlerListBtn">查询明细</button>
                        </div>

                        <h3 id="manageListTitle">报修明细  2015年第一学期</h3>

                        <table class="gray-table" width="100%">
                            <thead>
                                <th>报修日期</th>
                                <th>报修人</th>
                                <th>报修地点</th>
                                <th>内容</th>
                                <th>处理部门</th>
                                <th>进度</th>
                                <th>维修人</th>
                                <th>结果</th>
                                <th>评分反馈</th>
                                <th>操作</th>
                                <% if(isAdmin){ %>
　								　<th>上报至教育局</th>
 								 <% } %>
                            </thead>
                            <tbody id="handlerList">
                            </tbody>
                        </table>
                        <div class="fenye"><div class="new-page-links" id="pageForManage"></div>
						
                    	</div>
                    </div>
                    <!--/.报修处理-->

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

    <div class="pop-wrap" id="pingfenfkId">
        <div class="pop-title">评分反馈</div>
        <div class="pop-content clearfix">
            <span>评分等级：</span>
            <div id="starBg" class="star_bg">
                <input type="radio" id="starScore1" class="score score_1" value="1" name="score">
                <a href="#starScore1" class="star star_1" title="差"><label for="starScore1">差</label></a>
                <input type="radio" id="starScore2" class="score score_2" value="2" name="score">
                <a href="#starScore2" class="star star_2" title="较差"><label for="starScore2">较差</label></a>
                <input type="radio" id="starScore3" class="score score_3" value="3" name="score">
                <a href="#starScore3" class="star star_3" title="普通"><label for="starScore3">普通</label></a>
                <input type="radio" id="starScore4" class="score score_4" value="4" name="score">
                <a href="#starScore4" class="star star_4" title="较好"><label for="starScore4">较好</label></a>
                <input type="radio" id="starScore5" class="score score_5" value="5" name="score">
                <a href="#5" class="star star_5" title="好"><label for="starScore5">好</label></a>
            </div>
        </div>
        <div class="pop-btn"><span class="active" id="evaluationCommitBtn">提交</span><span>取消</span></div>
    </div>

    <div class="pop-wrap" id="editbxsqId">
        <div class="pop-title">编辑报修申请</div>
        <div class="pop-content">
            <div class="bxsq-view">
                <form action="#" method="post">
                    <table width="100%">
                        <tr>
                            <td width="105">报修人：</td>
                            <td id="editDeclareRepairPerson"></td>
                        </tr>
                        <tr>
                            <td>报修至部门：</td>
                            <td>
                                <select id="editDepartmentSelection">
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>报修地点：</td>
                            <td>
                                <input type="text" id="editRepairPlace"/>
                            </td>
                        </tr>
                        <tr>
                            <td>联系电话：</td>
                            <td>
                                <input type="text" id="editPhone"/>
                            </td>
                        </tr>
                        <tr>
                            <td>报修类别：</td>
                            <td>
                                <select id="editRepairType">
                                    <option value="建筑内饰">建筑内饰</option>
                					<option value="电脑硬件">电脑硬件</option>
                					<option value="电脑软件">电脑软件</option>
                					<option value="办公用品">办公用品</option>
                					<option value="其他故障">其他故障</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">报修描述：</td>
                            <td>
                                <textarea id="editRepairContent"></textarea>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <div class="pop-btn"><span class="active" id="editSubmitRepairBtn">提交</span><span>取消</span></div>
    </div>
    <!-- 上报至教育局的弹窗 -->
     <div class="pop-wrap" id="editUpId">
      <div class="pop-title">上报教育局申请</div>
        <div class="pop-content">
            <div class="bxsq-view">
                <form action="#" method="post">
                    <table width="100%">
                        <tr>
                            <td width="105">报修人：</td>
                            <td id="editUpPerson"></td>
                        </tr>
                        <tr>
                            <td>报修地点：</td>
                            <td id="editUpPlace">
                            </td>
                        </tr>
                        <tr>
                            <td>联系电话：</td>
                            <td>
                                <input type="text" id="editUpPhone"/>
                            </td>
                        </tr>
                        <tr>
                            <td>报修类别：</td>
                            <td id="editUpType">
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">报修描述：</td>
                            <td ><textarea id="editUpContent" readonly></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">上报原因：</td>
                            <td>
                                <textarea id="editUpReason"></textarea>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <div class="pop-btn"><span class="active" id="editUpBtn">确定</span><span id="upToEduCloseBtn">取消</span></div>
    </div>
    <!-- 上报至教育局的弹窗 -->
	<div class="pop-wrap" id="jgcontentId">
        <div class="pop-title">内容</div>
        <dl class="pop-content">
            <dt>维修结果：</dt>
            <dd><p id="repairResultForView" class="huanhang"></p></dd>
        </dl>
        <div class="pop-btn"><span class="active">关闭</span></div>
    </div>
    <div class="pop-wrap" id="bxcontentId">
        <div class="pop-title">内容</div>
        <dl class="pop-content">
            <dt>报修描述：</dt>
            <dd><p id="repairContentForView" class="huanhang"></p></dd>
        </dl>
        <div class="pop-btn"><span class="active">关闭</span></div>
    </div>

    <div class="pop-wrap" id="bxresultId">
        <div class="pop-title">登记维修情况</div>
        <dl class="pop-content">
            <!-- <dt>维修结果：</dt> -->
            <dd><textarea id="repairResult" ></textarea></dd>
        </dl>
        <div class="pop-btn"><span class="active" id="repairResultSubmitBtn">提交</span><span>取消</span></div>
    </div>

    <div class="pop-wrap" id="bxstatusId">
        <div class="pop-title">派发维修任务</div>
        <div class="pop-content">
            <label>
                受理人：<select id="handleRepairUserSelection">
              </select>
            </label>
        </div>
        <div class="pop-btn"><span class="active" id="handlerRepairUserCommitBtn">提交</span><span>取消</span></div>
    </div>

    <div class="bg-dialog"></div>

    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
    <script src="<%=basePath%>static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('bxgl');
    </script>
</body>
</html>
