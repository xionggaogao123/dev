<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="cc" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>  
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

  
<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html>
<%    
    String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath);  
    String userId = new BaseController().getSessionValue().getId();
    boolean isAdmin = UserRole.isManager(new BaseController().getSessionValue().getUserRole());
%> 
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-区域联考</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link rel="shortcut icon" href="" type="image/x-icon" />
    <link rel="icon" href="" type="image/x-icon" />
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/growRecord.css" rel="stylesheet" />
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

    <!--.banner-info-->

    <!--/.banner-info-->


        <div class="grow-tab-head clearfix contactExam">
            <ul class="clearfix">
                <li class="cur"><a href="#cjdId" class="curNone">区域联考</a></li>
            </ul>


        </div>    

    <div class="grow-col">
        <div class="xuenian">${term}</div>
        <form class="newConExam" action="" method="post">
            <table>
                <tr>
                    <td class="lab">考试名称：</td>
                    <td><input type="text" name="examName" id="examNmeInput"></td>
                </tr>
                <tr>
                    <td class="lab">年级</td>
                    <td>
                        <select id="gradeName">
                            
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="lab">考试类型</td>
                    <td><input type="text" value="区域联考" class="fixCon"></td>
                </tr>
                <tr>
                    <td class="lab">开始日期</td>
                    <td ><input style="cursor:pointer" type="text" onfocus="WdatePicker({minDate:'%y-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate" id="startDateInput"/></td>
                </tr>
                <tr>                   
                    <td valign="top" class="lab topbot">考试科目</td>
                    <td>
                    <!-- 
                        <label><input type="checkbox" class="check"/ >全选</label>
                         -->
                        <div class="checkbox-list" id="examSub">
                        <!--
                            <label><input type="checkbox" class="check" >数学</label>
                            <label><input type="checkbox" class="check">语文</label>
                            <label><input type="checkbox" class="check">外语</label>
                            <label><input type="checkbox" class="check">物理</label>
                            <label><input type="checkbox" class="check">化学</label>
                            <label><input type="checkbox" class="check">生物</label>
                            <br>
                            <label><input type="checkbox" class="check">历史</label>
                            <label><input type="checkbox" class="check">地理</label>
                            <label><input type="checkbox" class="check">政治</label>
  -->
                        </div>
                        <table class="itemGrade">
                            <thead>
                                <th width="90">科目名称</th>
                                <th width="90">满分</th>
                                <th width="90">优秀分</th>
                                <th width="90">及格分</th>
                                <th width="90" class="dateWidth">日期</th>
                                <th width="90" class="timeWidth">时间</th>

                            </thead>
                            <tbody id="subjectTable">
                            <!-- 
                                <tr>
                                    <td class="ddate"><input type="text"></td>
                                    <td class="ddate"><input type="text"></td>
                                    <td class="ddate"><input  id="full" type="text"></td>
                                    <td class="ddate"><input type="text"></td>
                                    <td class="ddate"><input type="text" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'%y-%M-%d'})" class="Wdate" /></td>
                                    <td class="ddate"><input type="text"></td>
                                </tr>
                                <tr>
                                    <td class="ddate"><input type="text"></td>
                                    <td class="ddate"><input type="text"></td>
                                    <td class="ddate"><input type="text"></td>
                                    <td class="ddate"><input type="text"></td>
                                    <td class="ddate"><input type="text" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'%y-%M-%d'})" class="Wdate" /></td>
                                    <td class="ddate"><input type="text"></td>
                                </tr>
                              -->
                            </tbody>
                        </table>


                    </td>
                </tr>
                <tr>                   
                    <td valign="top" class="lab topbot">学校</td>
                    <td>
                        <!--  
                        <label><input type="checkbox" class="check" value="schChoose"/>全选</label>
                        -->
                        <div class="checkbox-list" id="examSchool">
                        <!-- 
                            <label><input type="checkbox" class="check" value="">AAA学校</label>
                            <label><input type="checkbox" class="check">BBB学校</label>
                            <label><input type="checkbox" class="check">CCC学校</label>
                            <label><input type="checkbox" class="check">DDD学校</label>
                            <label><input type="checkbox" class="check">EEE学校</label>
                            <label><input type="checkbox" class="check">FFF学校</label>
                           -->
                        </div>
                        
                    </td>
                </tr>

            </table>

            <div class="exBtn">
                <input type="button" style="cursor:pointer" value="确定" class="examSureBtn" id="confirmBtn">
                <input type="button" style="cursor:pointer" value="取消" class="examCancelBtn" id="cancelBtn">
            </div>
        </form>
      
    </div>

</div>
<!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- 提示框 -->
<div class="pop-wrap input-wait" id="waitWindow_score" >
    <div class="pop-content">
        <h3 class="res">正在保存数据,请等待...</h3>
    </div>
</div>

<div class="pop-wrap done-btn" id="overWindow_score" >
    <div class="pop-content">
        <h3 class="res" id="overWindowH_score">保存数据成功！</h3>
    </div>
    <div class="pop-btn"><span class="active lef" id="overWindowBtn_score">确定</span></div>
</div>

<!-- 查看状态上报状态 -->
<div class="pop-wrap" id="status">
    <div class="pop-title">成绩上报状态</div>
    <div class="statusTitle">2015年初三第一次联考学校列表</div>
    
    <table>
        <thead>
            <th>学校名称</th>
            <th>状态</th>
        </thead>
        <tbody>
            <tr>
                <td width="250">嘻嘻嘻学校</td>
                <td width="150">未提交</td>                
            </tr>
            <tr>
                <td width="250">翰林学校</td>
                <td width="150">已提交</td>                
            </tr>
            <tr>
                <td width="250">希望学校</td>
                <td width="150">未提交</td>                
            </tr>            
        </tbody>
    </table>
    
    <div class="pop-btn"><span class="active">确定</span><span>取消</span></div>
</div>


<div class="bg-dialog"></div>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/jquery.min.js"></script>
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('newRegional');
</script>
</body>
</html>
