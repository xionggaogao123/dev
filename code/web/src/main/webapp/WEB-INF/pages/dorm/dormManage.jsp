<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>  
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

  
<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html>
<%    
    String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath);  
    int userRole = new BaseController().getSessionValue().getUserRole();
    boolean isAdmin = UserRole.isDormManager(userRole) || UserRole.isK6ktHelper(userRole)|| UserRole.isManager(userRole);
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
    <link href="<%=basePath%>static_new/js/modules/treeView/0.2.0/zTreeStyle.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/dorm.css" rel="stylesheet" />
</head>
<script>
	var jsonObjAll = '${message}';
	var grade=jsonObjAll.grades;
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

            <!--.banner-info
            <img src="http://placehold.it/770x100" class="banner-info" />
           /.banner-info-->

            <!--.tab-col-new-->
            <div class="tab-col-new">
                <div class="tab-head-new clearfix">
                    <ul>
                        <li class="cur">
                            <i></i>
                            <a href="#dormConfig-view" id="dormResourcesview">宿舍资源配置</a>
                        </li>
                        <li>
                            <i></i>
                            <a href="#susheList-view" id="susheListview">宿舍列表</a>
                        </li>
                        <li>
                            <i></i>
                            <a href="#dormStu-view" id="findAllDormStudent">入住学生列表</a>
                        </li>
                   
                    </ul>
                </div>
                <div class="tab-main-new clearfix">

                    <!--#dormConfig-view-->
                    <div id="dormConfig-view">

                        <div class="class-table">
 							<%if(isAdmin){ %>
                            <div class="class-list clearfix">
                              <span class="add">
                                <a href="#" class="addStuDorm" id="addStuDorm">新建宿舍区</a>
                            </span>
                            </div>
                             <%} %>
                            <!--.表头固定的效果-->
                            <div class="gray-table-box">
                                <table class="gray-table " width="100%">
                                    <thead>
                                    <th>宿舍资源配置</th>
                                    </thead>
                                </table>
                                <div class="gray-table-body ">
                                    <table class="gray-table" width="100%">
                                        <thead>
                                            <tr class="inHead">
                                                <td width="370" class="quHead">资源名称</td>
                                                <td width="100">类别</td>
                                                <%if(isAdmin){ %>
                                                <td>操作</td>
                                               <%} %>
                                            </tr>
                                        </thead>
                                        
                                        <tbody class="dormDepart" id="dormListView">
                                           <!-- 
                                            <tr class="quId">
                                                <td class="qu">
                                                    <input type="checkbox" class="checkbox">
                                                    <span class="">
                                                       	 宿舍B区
                                                    </span>
                                                </td>
                                                <td>宿舍区</td>
                                                <td class="rightAlign">
                                                    <a href="" class="a">编辑</a>
                                                    <a href="" class="a addFloor">添加宿舍楼</a>
                                                    <a href="" class="a">删除</a>
                                                </td>
                                            </tr>
                                            <tr class="louId">
                                                <td class="lou">
                                                    <input type="checkbox" class="checkbox">
                                                    <i class="l"></i>
                                                    <span class="">宿舍B区1号楼</span></td>
                                                <td>宿舍楼</td>
                                                <td class="rightAlign">
                                                    <a href="" class="a">编辑</a>
                                                    <a href="" class="a addCeng">添加宿舍楼层</a>
                                                    <a href="" class="a">删除</a>
                                                </td>
                                            </tr>
                                            <tr class="cengId">
                                                <td class="ceng">
                                                    <input type="checkbox" class="checkbox">
                                                    <i class="l"></i>
                                                    <span class="">宿舍B区1号楼1层</span></td>
                                                <td>宿舍楼层</td>
                                                <td class="rightAlign">
                                                    <a href="" class="a">编辑</a>
                                                    <a href="" class="a">删除</a>
                                                </td>
                                            </tr>
                                             -->
                                        </tbody>
                                        
                                    </table>
                                </div>
                            </div>
                            <!--.表头固定的效果-->



                        </div>

                       

                    </div>
                    <!--/#dormConfig-view-->


                    <!--#susheList-view-->
                    <div id="susheList-view" class="hide">
                        <div class="class-table susheList-table" id="susheListId">
                            <div class="class-list clearfix">
                                <label>宿舍区</label>
                                  <select id="dormArea">
                                     <option value="ALL">全部</option>
                                  </select>
                               <label>宿舍楼</label>
                                  <select id="dormBuilding">
                                     <option value="ALL">全部</option>
                                  </select>
                               <label>楼层</label>
                                  <select id="dormFloor">
                                     <option value="ALL">全部</option>
                                  </select>
                              <%if(isAdmin){ %>
                              <span class="add">
                                <a href="#" class="addDorm">新建宿舍</a>
                              </span>
                              <%}%>
                            </div>

                            
                            <!--.表头固定的效果-->
                            <div class="gray-table-box">

                                <table class="gray-table tcommon" width="100%">
                                    <thead>
                                    <th class="solid">宿舍列表</th>
                                    
                                    </thead>
                                </table>

                                <div class="gray-table-body tcommon-detail">
                                    <table class="gray-table none" width="100%">
                                        <tbody id="dormList">
                                        <tr class="inHead">
                                            <td width="166">宿舍名</td>
                                            <td>性别</td>
                                            <td>年级/班级</td>
                                            <td>宿舍电话</td>
                                            <td>床位数</td>
                                            <td>宿舍物品</td>
                                            <td class="solid">操作</td>
                                        </tr>
                                        
                                        </tbody>
                                    </table>
                                    
                                     <!-- 分页div -->
                                  <div class="fenye">
                                      <div class="new-page-links" id="new-page-links"></div>
                                  </div>
                                </div>
                            </div>
                            <!--.表头固定的效果-->

                        </div>
                        <div class="class-table look-table hide" id="lookTableId">
                            <div class="class-list clearfix">
                                <a href="#" class="back">&lt;&lt;&nbsp;返回宿舍列表</a>
                              <%if(isAdmin){%>
                              <span class="add">
                                <a href="#" class="addStu">添加学生</a>
                              </span>
                              <%}%>
                            </div>

                            
                            <!--.表头固定的效果-->
                            <div class="gray-table-box">

                                <table class="gray-table tcommon" width="100%">
                                    <thead>
                                    <th class="solid">宿舍登记</th>
                                    
                                    </thead>
                                </table>

                                <div class="gray-table-body tcommon-detail">
                                    <table class="gray-table none" width="100%">
                                        <tbody id="dormDetial">
                                        <tr class="inHead">
                                            <td>床位</td>
                                            <td>学号</td>
                                            <td>姓名</td>
                                            <td>性别</td>
                                            <td>年级</td>
                                            <td>班级</td>
                                            <td class="solid">领取物品</td>
                                            <%if(isAdmin){%>
                                            <td class="solid">操作</td>
                                            <%}%>
                                        </tr>
                                        <!--  
                                        <tr>
                                            <td>1号床位</td>
                                            <td>103496</td>
                                            <td>张三那</td>
                                            <td>男</td>
                                            <td>五年级</td>
                                            <td>（1）班</td>
                                            <td class="objects">床板、水盆<div id="obj1">床板、水盆、床板、水盆</div></td>
                                            <td><a href="" class="delete look">删除</a></td>
                                        </tr>
                                       -->
                                        </tbody>
                                    </table>
                                    
                                </div>
                            </div>
                            <!--.表头固定的效果-->

                        </div>

                    </div>
                    <!--/#susheList-view-->

                    <!--#dormStu-view-->
                    <div id="dormStu-view" class="hide">
                        <div class="class-table">
                            <div class="class-list clearfix">
                               <label>年级</label>
                               <select id="gradeName">
                               		<option value="ALL">全部</option>
                               		<c:forEach items="${message.grades}" var="g">
                               			<option value="${g.gradeId}" gnm="${g.name}">${g.name}</option>
                               		</c:forEach>
                               </select>
                               <label>班级</label>
                               <select id="class_name">
                               		<option id="all_class" value="ALL">全部</option>
                               	</select>
                               <label>性别</label>
                               <select id="sex_name">
                               		<option value="ALL">全部</option>
                               		<option value="男">男</option>
                               		<option value="女">女</option>
                               	</select>
                            </div>
                            <div class="class-list clearfix">
                                <label>学生姓名</label>
                                <input type="text" id="student_name">
                                <label id="stuNoLabelId">学号</label>
                                <input type="text" id="student_No">
                                <span class="add">
                                <a href="#" class="addInRecord" id="findStudents">查询</a>
                            </span>
                            </div>                          

                            <!--.表头固定的效果-->
                            <div class="gray-table-box">

                                <table class="gray-table tcommon" width="100%">
                                    <thead>
                                    <th>住宿管理-入住学生列表</th>
                                    
                                    </thead>
                                </table>

                                <div class="gray-table-body tcommon-detail">
                                    <table class="gray-table" width="100%">
                                        <tbody id="studentList">
                                        <tr class="inHead">
                                            <td>姓名</td>
                                            <td>学号</td>
                                            <td width="194">宿舍名</td>
                                            <td>床位</td>
                                            <td>性别</td>
                                            <td>年级</td>
                                            <td>班级</td>
                                            <td >领取物品</td>
                                        </tr>
                               			<!--<tr>
                                            <td>张三那</td>
                                            <td>103496</td>
                                            <td>宿舍A区1号楼1层101室</td>
                                            <td>1号床位</td>
                                            <td>男</td>
                                            <td>五年级</td>
                                            <td>（1）班</td>
                                            <td class="objects">床板、水盆</td>
                                        </tr>-->
                                        </tbody>
                                    </table>
                                    <div class="fenye">
                                    	<div class="new-page-links" id="pageForDorm"></div>
                                    </div>
                                    <div  id="obj2"></div>
                               </div>	
                            </div>
                            <!--.表头固定的效果-->
                        </div>
                    </div>
                    <!--/#dormStu-view-->

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

    <div class="pop-wrap newDorm" id="newDorm">
        <div class="pop-title">

            <span>新建宿舍</span>
            <span class="closePop"></span>
        </div>
        
        <div class="pop-content">
            <table class="tcommon-table">
            <tr>
                <td>宿舍区号</td>
                <td>
                    <select id="newDormArea"><option value="ALL">请选择</option></select>
                    <span class="seldian">*</span>
                </td>
            </tr>
            <tr>
                <td>宿舍楼号</td>
                <td>
                   <select id="newDormBuilding"><option value="ALL">请选择</option></select>
                   <span class="seldian">*</span>
                </td>
            </tr>  
            <tr>
                <td>宿舍楼层</td>
                <td>
                   <select id="newDormFloor"><option value="ALL">请选择</option></select>
                   <span class="seldian">*</span>
                </td>
            </tr>

            <tr>
                <td>宿舍名称</td>
                <td><input id="dormName" type="text"><span class="inpdian">*</span></td>
            </tr>    
            <tr>
                <td>床位数量</td>
                <td><input id="bedNum" type="text"><span class="inpdian">*</span></td>
            </tr>
            <tr>
                <td>宿舍电话</td>
                <td><input id="dormPhone" type="text"><span class="inpdian">*</span></td>
            </tr>
            <tr>
                <td valign="top">设备配置</td>
                <td><textarea id="receiveGoods"></textarea><span class="seldian">*</span></td>
            </tr>
            
        </table>
        
        </div>
        <div class="pop-btn">
            <a class="save" id="newDormDone">确认</a>
            <a class="reset" id="newDormReset">重置</a>
        </div>
    </div>
    
    <!-- 新建宿舍区弹窗 -->
    <div class="pop-wrap newStuDormId" id="newStuDormId">
        <div class="pop-title">
            <span>新建宿舍区</span>
            <span class="closePop"></span>
        </div>
        
        <div class="pop-content">
            <table class="newStuDorm-table">  
            <tr>
               
                <td class="rightAlign">宿舍区名称</td>
                <td><input type="text" id="newDormAreaName"></td>
            </tr>
                          
        </table>
           
        </div>
        <div class="pop-btn">
            <a class="save" href="#" id="saveNewDormArea">确认</a>
            <a href="#" class="reset" id="resetNewDormArea">重置</a>
        </div>
    </div>
    <!-- 新建宿舍区弹窗 -->
    
     <!-- 编辑宿舍区弹窗 -->
    <div class="pop-wrap newStuDormId" id="editStuDormId">
        <div class="pop-title">
            <span>编辑宿舍区</span>
            <span class="closePop"></span>
        </div>
        
        <div class="pop-content">
            <table class="newStuDorm-table">  
            <tr>
               
                <td class="rightAlign" id="editDormAreaId">宿舍区名称</td>
                <td><input type="text" id="editDormAreaName"></td>
            </tr>
                          
        </table>
           
        </div>
        <div class="pop-btn">
            <a class="save" href="#" id="saveEditDormArea">确认</a>
            <a href="#" class="reset" id="resetEditDormArea">重置</a>
        </div>
    </div>
    <!-- 编辑宿舍区弹窗 -->
    <!-- 新建宿舍楼弹窗 -->
    <div class="pop-wrap newLou" id="newLou">
        <div class="pop-title">
            <span>新建宿舍楼</span>
            <span class="closePop"></span>
        </div>
        
        <div class="pop-content">
            <table class="newLou-table">  
            <tr>
               
                <td class="rightAlign">宿舍楼名称</td>
                <td><input type="text" id="newDormBuildName"></td>
            </tr>
            <tr>
               
                <td class="rightAlign" id="belongDromAreaId">所属宿舍区</td>
                <td><input type="text" id="belongDromAreaName" readonly="readonly"></td>
            </tr>
                          
        </table>
           
        </div>
        <div class="pop-btn">
            <a class="save" href="#" id="saveDormBuild">确认</a>
            <a href="#" class="reset" id="resetDormBuild">重置</a>
        </div>
    </div>
    <!-- 新建宿舍楼弹窗 -->
    
    <!-- 编辑宿舍楼弹窗 -->
    <div class="pop-wrap newLou" id="editBuild">
        <div class="pop-title">
            <span>编辑宿舍楼</span>
            <span class="closePop"></span>
        </div>
        
        <div class="pop-content">
            <table class="newLou-table">  
            <tr>
               
                <td class="rightAlign" id="editBuildId">宿舍楼名称</td>
                <td><input type="text" id="editBuildName"></td>
            </tr>
            <tr>
               
                <td class="rightAlign" id="editBelongAreaId">所属宿舍区</td>
               <!-- 
                <td><input type="text" id="editBelongAreaName"></td>
                 -->
                 <td><select  class="louSelect"></select></td>
            </tr>
                          
        </table>
           
        </div>
        <div class="pop-btn">
            <a class="save" href="#" id="saveEditBuild">确认</a>
            <a href="#" class="reset" id="resetEditBuild">重置</a>
        </div>
    </div>
    <!-- 编辑宿舍楼弹窗 -->
    
    <!-- 新建宿舍层弹窗 -->
    <div class="pop-wrap newFloor" id="newFloor">
        <div class="pop-title">
            <span>新建宿舍楼层</span>
            <span class="closePop"></span>
        </div>
        
        <div class="pop-content">
            <table class="newFloor-table">  
            <tr>
               
                <td class="rightAlign" id="newDormFloorId">宿舍楼层名称</td>
                <td><input type="text" id="newDormFloorName"></td>
            </tr>
            <tr>
               
                <td class="rightAlign" id="belongBuildId">所属宿舍楼</td>
                <td><input type="text" id="belongBuildName" readonly="readonly" ></td>
            </tr>
                          
        </table>
           
        </div>
        <div class="pop-btn">
            <a class="save" href="#" id="saveDormFloor">确认</a>
            <a href="#" class="reset" id="resetDormFloor">重置</a>
        </div>
    </div>
    
    <!-- 新建宿舍层弹窗 -->
    
    <!-- 编辑宿舍层弹窗 -->
    <div class="pop-wrap newFloor" id="editFloor">
        <div class="pop-title">
            <span>编辑宿舍楼层</span>
            <span class="closePop"></span>
        </div>
        
        <div class="pop-content">
            <table class="newFloor-table">  
            <tr>
               
                <td class="rightAlign" id="editFloorId">宿舍楼层名称</td>
                <td><input type="text" id="editFloorName"></td>
            </tr>
            <tr>
               
                <td class="rightAlign" id="editBelongBuidId">所属宿舍楼</td>
                <td><select  class="cengSelect"></select></td>
            </tr>
                          
        </table>
           
        </div>
        <div class="pop-btn">
            <a class="save" href="#" id="saveEditFloor">确认</a>
            <a href="#" class="reset" id="resetEditFloor">重置</a>
        </div>
    </div>
    
    <!-- 编辑宿舍层弹窗 -->
    
    <!-- 添加学生 -->
    <div class="pop-wrap" id="addStuId">
        <div class="pop-title">
            <span>添加学生</span>
            <span class="closePop"></span>
        </div>
        
        <div class="pop-content">
            <table class="addStuId-table">  
            <tr>
                <td class="rightAlign">年级</td>
                
                <td><select id="newDormGrade">
                       <option value="ALL">请选择</option>
                     <c:forEach items="${message.grades}" var="g">
                       <option value="${g.gradeId}">${g.name}</option>
                     </c:forEach>
                </select>
                <span class="seldian">*</span>
                </td>
                
                <td>班级</td>
                <td>
                    <select id="newDormCl">
                      <option value="ALL">请选择</option>
                    </select>
                    <span class="seldian">*</span>
                </td>
            </tr>
            <tr>
                <td class="rightAlign">姓名</td>
                <td>
                    <select id="stuName">
                      <option value="ALL">请选择</option>
                    </select>
                    <span class="seldian">*</span>
                </td>
                <td>性别</td>
                <td id="stuSex">
                    <input type="text" readonly="readonly">
               <!-- <select >
                       <option value="ALL">请选择</option>   
                    </select>  -->
                </td>
                </tr>
                <tr>
                <td class="rightAlign">学号</td>
                <td id="stuNum">
                     <input type="text" readonly="readonly"> 
                </td>
                <td>床位</td>
                <td>
                    <select id="bed">
                      <option value="ALL">请选择</option>
                    </select>
                    <span class="seldian">*</span>
                </td>
                </tr>
              
                <tr>
                    <td valign="top">领取物品</td>
                    <td colspan="3">
                        <textarea id="stuGoods"></textarea>
                    </td>
                </tr>  

        </table>
           
        </div>
        <div class="pop-btn">
            <a class="save" id="addStuDone">确认</a>
            <a id="newDormStuReset" class="reset">重置</a>
        </div>
    </div>
<!-- 添加学生 -->
  



    <div class="bg-dialog"></div>

    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
    <script src="<%=basePath%>static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js"></script>
    <script>
        seajs.use('dorm');
        seajs.use('studentIn');
        seajs.use('dormList');
    </script>
</body>
</html>
