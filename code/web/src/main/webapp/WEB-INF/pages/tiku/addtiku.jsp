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
    boolean isAdmin = UserRole.isHeadmaster(userRole) || UserRole.isK6ktHelper(userRole);
    boolean isEdu = UserRole.isEducation(userRole);
    boolean baseCanEdit = false;
    if(isAdmin){
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
    <title>复兰科技-添加题目</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- webuploader styles-->
    <link href="<%=basePath%>static_new/js/modules/webuploader/webuploader.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/ziyuan.css" rel="stylesheet" />
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
        <!--  
        <img src="http://placehold.it/770x100" class="banner-info" />
        -->
        <!--/.banner-info-->


        <h3 class="add-head">上传题目</h3>

        <div class="ask-txt">
        <!-- 
            <label>题目编号：<span id="num_id">${num}</span></label>
         -->
            <label>选择题型：<select id="selected">
           				 <option value="请选择">请选择</option>
           				 <option value="判断题">判断题</option>
            			 <option value="选择题">选择题</option>
                         <option value="填空题">填空题</option>
                         <option value="主观题">主观题</option>
            
            </select></label>
        </div>

        <!--.add-ziyuan-->
        <div class="add-ziyuan">

            <!--.add-ziyuan-col-->
            <div class="add-ziyuan-col">

                <!--.form-col-->
                <div class="form-col">

                    <!--.form-head-->
                    <div class="form-head clearfix">
                        <h4>题目分类属性</h4>
                        <span>同步教材和综合知识点都需同时填写</span>
                    </div>
                    <!--/.form-head->

                    <!--.form-main-->
                    <div class="form-main clearfix complex-value-class">
                        <dl>
                            <dt>同步教材版本</dt>
                            <dd>
                                <label>学段</label>
                                <select class="versionTermTypeSelection" subClass="versionSubjectSelection" typeInt="1">
                                </select>
                            </dd>
                            <dd>
                                <label>学科</label>
                                <select class="versionSubjectSelection" subClass="bookVertionSelection" typeInt="2">
                                </select>
                            </dd>
                            <dd>
                                <label>教材版本</label>
                                <select class="bookVertionSelection" subClass="gradeSelection" typeInt="3">
                                </select>
                            </dd>
                            <dd>
                                <label>年级/课本</label>
                                <select class="gradeSelection" subClass="chapterSelection" typeInt="4">
                                </select>
                            </dd>
                            <dd>
                                <label>章</label>
                                <select class="chapterSelection" subClass="partSelection" typeInt="5">
                                </select>
                            </dd>
                            <dd>
                                <label>节</label>
                                <select class="partSelection" subClass="0" typeInt="6">
                                </select>
                            </dd>
                        </dl>
                        <dl>
                            <dt>综合知识点</dt>
                            <dd>
                                <label>学段</label>
                                <select class="knowledgeTermtypeSelection" subClass="knowledgeSubjectSelection" typeInt="1">
                                </select>
                            </dd>
                            <dd>
                                <label>学科</label>
                                <select class="knowledgeSubjectSelection" subClass="knowledgeAreaSelection" typeInt="2">
                                </select>
                            </dd>
                            <dd>
                                <label>知识面</label>
                                <select class="knowledgeAreaSelection" subClass="knowledgePointSelection" typeInt="7">
                                </select>
                            </dd>
                            <dd>
                                <label>知识点</label>
                                <select class="knowledgePointSelection" subClass="0" typeInt="8">
                                </select>
                            </dd>
                        </dl>
                    </div>
                    <!--/.form-main-->

                </div>
                <!--/.form-col-->

            </div>
            <!--/.add-ziyuan-col-->

            <button  style='cursor:pointer' type="button" class="add-ziyuan-btn"> + 增加题目分类属性</button>
            <!--.form-col-->
            <div class="form-col">

                <!--.form-head-->
                <div class="form-head clearfix">
                    <h4>题目内容</h4>
                </div>
                <!--/.form-head-->

                <!--.form-main-->
                <div class="form-main">

                    <script type="text/plain" id="myEditor"></script>

                </div>
                <!--/.form-main-->

            </div>
            <!--/.form-col-->

            <!--.form-col-->
            <div class="form-col hide" id="answer_form">

                <!--.form-head-->
                
                <div class="form-head clearfix">
                    <h4>正确答案</h4>
                    <span class="add-answer">增加答案</span>
                </div>
               
                <!--/.form-head-->

                <!--.form-main-->
                <div class="form-main answer-col">
					 
                    <div class="answer-check check-id" id="check_id">
                        <label><input type="checkbox" name="answer" value = "对"/>对</label>
                        <label><input type="checkbox" name="answer" value="错"/>错</label>
                    </div>

                    <div class="answer-check blank-id" id="blanks_id">
                        <input type="text" class="answer-txt" />
                    </div>
 					<div class="answer-check addCheck choose-id" id="choose_id">
                        <label><input type="checkbox" value="A"/>A</label>
                        <label><input type="checkbox" value="B"/>B</label>
                        <label><input type="checkbox" value="C"/>C</label>
                        <label><input type="checkbox" value="D"/>D</label>
                        <div class="addItems" data-attr="A_B_C_D">
                            <span  class="icon-plus"><img src="<%=basePath%>static_new/images/add.png"></span>
                            <span  class="icon-minus"><img src="<%=basePath%>static_new/images/add1.png"></span>
          				</div>
                    </div>
                    <div class="answer-check" id="zhug_id">
                        <script type="text/plain" id="myEditor3"></script>

                    </div>
                   		 
                </div>
                <!--/.form-main-->

            </div>
            <!--/.form-col-->

            <!--.form-col-->
            <div class="form-col">

                <!--.form-head-->
                <div class="form-head clearfix">
                    <h4>答案解析</h4>
                </div>
                <!--/.form-head-->

                <!--.form-main-->
                <div class="form-main uploader-info">

                    <script type="text/plain" id="myEditor2"></script>

                    <div class="btn-list">
                        <button style='cursor:pointer' type="button" id="add">添加</button>
                        <button style='cursor:pointer' type="button" class="reset-btn">取消</button>
                    </div>


                </div>
                <!--/.form-main-->

            </div>
            <!--/.form-col-->


        </div>
        <!--/.add-ziyuan-->


    </div>
    <!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/jquery.min.js"></script>
<script src="<%=basePath%>static_new/js/modules/webuploader/webuploader.min.js"></script>
<script src="<%=basePath%>static_new/js/modules/webuploader/imgUpload.js"></script>
<script src="<%=basePath%>static_new/js/modules/ueditor/ueditor.config.js"></script>
<script src="<%=basePath%>static_new/js/modules/ueditor/ueditor.all.min.js"></script>
<script>
    seajs.use('addtiku');
</script>
</body>
</html>