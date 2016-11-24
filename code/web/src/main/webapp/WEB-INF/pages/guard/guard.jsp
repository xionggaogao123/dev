 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<%    
    String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath);  
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
    <link href="<%=basePath%>/static_new/css/reset.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="<%=basePath%>/static_new/css/guard.css" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
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

        <!--.col-right-->
        <div class="col-right">
            <!--.tab-col-new-->
            <div class="tab-col-new">
                <div class="tab-head-new clearfix" id="top-table">
                    <ul>
                        <li class="cur">
                            <i></i>
                            <a href="#stuIn-view">学生进校登记</a>
                        </li>
                        <li class="outSchools">
                            <i></i>
                            <a href="#stuOut-view">学生出校登记</a>
                        </li>
                        <li >
                            <i></i>
                            <a href="#stuInvite-view" >来访登记</a>
                        </li>
                   
                    </ul>
                </div>
                <div class="tab-main-new clearfix">

					<!--#stuIn-view-->
					<div id="stuIn-view" >

                        <div class="class-table">

                            <div class="class-list clearfix">
                               <select id="stuInGuardSelect" flag="ALL"></select>
                               <select id="stuInClassSelect" flag="ALL"><option>全部班级</option></select>
                               <c:choose>
								<c:when test="${power == '1'}">  
									 <span class="inAdd hide">
                                <a href="#" class="addInRecord">添加记录</a>
                            </span>
								</c:when>
								<c:otherwise> 
									 <span class="inAdd">
                                <a href="#" class="addInRecord">添加记录</a>
                            </span>
								</c:otherwise>
							</c:choose>
                            </div>

                            

                            <!--.表头固定的效果-->
                            <div class="gray-table-box">

                                <table class="gray-table inRecord" width="100%">
                                    <thead>
                                    <th>进校记录</th>
                                    </thead>
                                </table>

                                <div class="gray-table-body inRecord-detail">
                                    <table class="gray-table" width="100%">
                                       <thead >
                                        <tr class="inHead">
                                            <td >年级</td>
                                            <td>班级</td>
                                            <td>姓名</td>
                                            <td width="142">时间</td>
                                            <td>事由</td>
                                             <c:choose>
                                            	<c:when test="${power == '1'}">  
                                            	
                                            	</c:when>
                                            	<c:otherwise>
                                            		<td>操作</td>
                                            	</c:otherwise>
                                            </c:choose>
                                        </tr>
                                        </thead>
                                          <tbody id="enterSchoolLists">
                                          </tbody>
  
                                    </table>
                                   <div class="new-page-link"> <div class="new-page-links"></div></div>
                                </div>
                                  
                            </div>
                            <!--.表头固定的效果-->
 


                        </div>



                    </div>
                    <!--/#stuIn-view-->


                    <!--#stuOut-view-->
                    <div id="stuOut-view" class="hide">
                        <div class="class-table">

                            <div class="class-list clearfix">
                               <select id="out-grade"><option value="ALL">全部年级</option></select>
                               <select id="out-class"><option value="ALL">全部班级</option></select>
							<c:choose>
								<c:when test="${power == '1'}">  
									 <span class="inAdd hide">
                                		<a href="#" class="addOutRecord">添加记录</a>
                           			 </span>
								</c:when>
								<c:otherwise> 
									 <span class="inAdd">
                                		<a href="#" class="addOutRecord">添加记录</a>
                            		 </span>
								</c:otherwise>
							</c:choose>
                             
                            </div>

                            

                            <!--.表头固定的效果-->
                            <div class="gray-table-box">

                                <table class="gray-table inRecord" width="100%">
                                    <thead>
                                    <th>出校记录</th>
                                    
                                    </thead>
                                </table>

                               <div class="gray-table-body inRecord-detail">
                                    <table class="gray-table" width="100%">
                                    	<thead>
                                    		<tr class="inHead">
                                            <td>年级</td>
                                            <td>班级</td>
                                            <td>姓名</td>
                                            <td>批准教师</td>
                                            <td width="142">时间</td>
                                            <td>事由</td>
                                            <c:choose>
                                            	<c:when test="${power == '1'}">  
                                            	
                                            	</c:when>
                                            	<c:otherwise>
                                            		<td>操作</td>
                                            	</c:otherwise>
                                            </c:choose>
                                            
                                            
                                            
                                        </tr>
                                    	</thead>
                                        <tbody id="outSch">
                                        
                                        </tbody>
                                    </table>
                                    <div class='new-page-link'><div class='new-page-links'></div></div>
                                </div>
                                
                            </div>
                            <!--.表头固定的效果-->



                        </div>

						
                    

                    </div>
                    <!--/#stuOut-view-->

                    <!--#stuInvite-view-->
                    <div id="stuInvite-view" class="hide">
                        <div class="class-table">

                            <div class="class-list clearfix">
								<c:choose>
									<c:when test="${power=='1'}">
										<span class="inAdd hide">
			                                <a href="#" class="addInviteRecord" >添加记录</a>
		                              	</span>
									</c:when>
									<c:otherwise>
		                              <span class="inAdd">
		                                <a href="#" class="addInviteRecord" >添加记录</a>
		                              </span>
									</c:otherwise>
	                            </c:choose>
                            </div>

                            

                            <!--.表头固定的效果-->
                            <div class="gray-table-box">

                                <table class="gray-table inRecord" width="100%">
                                    <thead>
                                    <th>来访记录</th>
                                    
                                    </thead>
                                </table>

                                <div class="gray-table-body inRecord-detail">
                                    <table class="gray-table" width="100%">
                                        <thead>
                                        <tr class="inHead">
                                            <td>姓名</td>
                                            <td>性别</td>
                                            <td>单位</td>
                                            <td class='phoneWid'>联系电话</td>
                                            <td width="142">时间</td>
                                            <td>对象</td>
                                            <td>事由</td>
                                            <td>操作</td>
                                           
                                        </tr>
                                        </thead>
                                        <tbody id="visit">
                                        <!-- 
	                                        <tr>
	                                                 <td >张三那</td>
	                                            <td>男</td>
	                                            <td>上海复兰科技</td>
	                                            <td>13800100000</td>
	                                            <td>11月03日&nbsp;13:00</td>
	                                            <td>陈主任</td>
	                                            <td class="leave">事假事假事假事假</td>
	                                            <td><a href="" class="lookIn">查看</a><a href="" class="delIn">删除</a></td>
	                                        </tr>
	                                         -->
	                                     </tbody>
                                       

                                        </tbody>
                                    </table>
                                    <div class="new-page-link"><div class="new-page-links" id="pageForVisit"></div></div>
                                </div>
                                
                            </div>
                            <!--.表头固定的效果-->



                        </div>

                        

                    </div>
                    <!--/#stuInvite-view-->

                

                </div>
            </div>
            <!--/.tab-col-new-->

        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->

    <!--#foot-->
<%@ include file="../common_new/foot.jsp" %>    <!--#foot-->

    <div class="pop-wrap" id="inRecordId">
        <div class="pop-title">

            <span>添加进校登记</span>
            <span class="closePop"></span>
        </div>
        <!-- <div class="pop-title">添加进校登记</div> -->
        <div class="pop-content">
            <table class="inRecord-table">  
            <tr>
                <td>时间</td>
                <td><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate Wdate-enter">
                <span style="color:red;margin-left:2px;">*</span></td>
                <td>年级</td>
                <td>
                <select id="inRecordIdGrade">
                        <option>请选择</option>
                    </select> <span style="color:red;margin-left:2px;">*</span>
                </td>
            </tr>
            <tr>
                <td>班级</td>
                <td>
                    <select id="inRecordIdClass">
                        <option>请选择</option>
                    </select><span style="color:red;margin-left:8px;">*</span>
                </td>
                <td>姓名</td>
                <td>
                   <select id="inRecordIdName">
                        <option>请选择</option>
                    </select><span style="color:red;margin-left:8px;">*</span>
                </td>
                </tr>
                <tr>
                    <td valign="top">事由</td>
                    <td colspan="3">
                        <textarea id="inRecordIdTextarea"></textarea><span style="color:red;margin-left:8px;">*</span>
                    </td>
                </tr>  

        </table>


           
        </div>
        <div class="pop-btn" id="inRecordIdSave"><a id="inRecordSave" class="save">保存到进校记录</a></div>
    </div>
    <div class="pop-wrap" id="outRecordId">
        <div class="pop-title">

            <span>添加出校登记</span>
            <span class="closePop"></span>
        </div>
        <!-- <div class="pop-title">添加进校登记</div> -->
        <div class="pop-content">
            <table class="outRecord-table">  
            <tr>
                <td class="rightAlign">时间</td>
                <td><input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate Wdate-out"><span style="color:red;margin-left:6px;">*</span></td>
                <td>年级</td>
                <td>
                    <select id="outSch-grade">
                        <option value="ALL">请选择</option>
                    </select><span style="color:red;margin-left:6px;">*</span>
                </td>
            </tr>
            <tr>
                <td class="rightAlign">班级</td>
                <td>
                    <select id="outSch-class">
                        <option value="ALL">请选择</option>
                    </select><span style="color:red;margin-left:8px;">*</span>
                </td>
                <td>姓名</td>
                <td>
                    <select id="outSch-name">
                        <option value="ALL">请选择</option>
                    </select><span style="color:red;margin-left:8px;">*</span>
                </td>
                </tr>
                <tr>
                    <td class="rightAlign">批准老师</td>
                    <td colspan="3"><input type="text" class="agreeTeacher agreeTeacherOuts"><span style="color:red;margin-left:2px;">*</span></td>
                </tr>
             
                <tr>
                    <td valign="top" class="rightAlign">事由</td>
                    <td colspan="3">
                        <textarea class="rightAlignReason rightAlignReasonOutSch"></textarea><span style="color:red;margin-left:2px;">*</span>
                    </td>
                </tr>  

        </table>


           
        </div>
        <div class="pop-btn" id="add-Out"><a id="inRecordSave" class="save">保存到出校记录</a></div>
    </div>
    <div class="pop-wrap" id="inviteRecordId">
        <div class="pop-title">

            <span>添加来访登记</span>
            <span class="closePop"></span>
        </div>
        <!-- <div class="pop-title">添加进校登记</div> -->
        <div class="pop-content">
            <table class="inviteRecord-table">  
	            <tr>
	                <td class="rightAlign">时间</td>
	                <td><input id="visitTime" type="text" id="visitTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate"><span style="color:red;margin-left:5px;">*</span></td>
	                <td class="rightAlign">姓名</td>
	                <td><input type="text" id="visitorName"><span style="color:red;margin-left:5px;">*</span></td>
	            </tr>
	            <tr>
	                <td class="rightAlign">性别</td>
	                <td>
	                    <select id="visitorGender">
	                        <option value="1 ">男</option>
	                        <option value="0">女</option>
	                    </select><span style="color:red;margin-left:5px;">*</span>
	                </td>
	                <td class="rightAlign">单位</td>
	                <td>
	                    <input type="text" id="company"><span style="color:red;margin-left:5px;">*</span>
	                </td>
	            </tr>
	            <tr>
	            	<td class="rightAlign">电话</td>
	                <td>
	                    <input type="text" id="telephone"><span style="color:red;margin-left:5px;">*</span>
	                </td>
	                <td class="rightAlign">人数</td>
	                <td>
	                    <input type="text" id="numOfPeople">
	                </td>
	            </tr>
            	<tr>
                    <td class="rightAlign">证件</td>
                    <td colspan="3"><input type="text" class="agreeTeacher" id="papers"></td>
                </tr>
	             
                <tr>
                    <td valign="top" class="rightAlign">来访事由</td>
                    <td colspan="3">
                        <textarea id="visitReasons"></textarea><span style="color:red;margin-left:5px;">*</span>
                    </td>
                </tr>  
                <tr>
                    <td>到访部门</td>
                    <td>
                    	<select id="addDept">
                    	</select><span style="color:red;margin-left:5px;">*</span>
                    </td>
                    <td>到访对象</td>
                    <td><input type="text" id="object"><span style="color:red;margin-left:5px;">*</span></td>
                </tr>
        	</table>
     	</div>
        <div class="pop-btn" id="saveVisitor"><a id="inRecordSave" class="save">保存到来访记录</a></div>
    </div>
    <div class="pop-wrap lookInId" id="inviteRecordId" >
        <div class="pop-title">

            <span>查看来访登记</span>
            <span class="closePop"></span>
        </div>
        <div class="pop-content">
            <table class="inviteRecord-table">  
            <tr>
                <td class="rightAlign">时间</td>
                <td><input type="text" value="" id="seeTime" readonly="readonly"></td>
                <td class="rightAlign"align="">姓名</td>
                <td><input type="text" value="" id="seeName" readonly="readonly"></td>
            </tr>
            <tr>
                <td class="rightAlign">性别</td>
                <td>
                    <input type="text" value="" id="seeGender" readonly="readonly">
                </td>
                <td class="rightAlign">单位</td>
                <td >
                	<input type="text" value="" id="seeCompany" readonly="readonly">
                </td>
                
             </tr>
             <tr>
             	<td class="rightAlign">电话</td>
                <td>
                    <input type="text" value="" id="seeTelephone" readonly="readonly">
                </td>
                <td class="rightAlign">人数</td>
                <td >
                	<input type="text" value=""  id="seeNumOfPeople" readonly="readonly">
                </td>
             </tr>
             <tr>
                 <td class="rightAlign">证件</td>
              	 <td colspan="3">
                  <input type="text" class="agreeTeacher" value="" id="seePapers" readonly="readonly">
              	 </td>
             </tr>
             
                <tr>
                    <td valign="top" class="rightAlign">来访事由</td>
                    <td colspan="3">
                        <textarea id="seeReasons" readonly="readonly"></textarea>
                    </td>
                </tr>  
                <tr>
                    <td>到访部门</td>
                    <td><input type="text" id="seeDepartment" readonly="readonly"></td>
                    <td>到访对象</td>
                    <td><input type="text" id="seeObject" readonly="readonly"></td>
                </tr>

        </table>


           
        </div>
        <div class="pop-btn" id="seeSure"><a class="save">确认</a></div>
    </div>

  



    <div class="bg-dialog"></div>

    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
    <script src="<%=basePath%>static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js"></script>
   <script>
        seajs.use('enterSchool');
        seajs.use('outSchool');
        seajs.use('visiting');
        
    </script>
</body>
</html>
