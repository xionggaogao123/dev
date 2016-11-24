
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
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-添加资源</title>
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
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
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


        <h3 class="add-head">上传至资源库</h3>


        <!--.add-ziyuan-->
        <div class="add-ziyuan">

            <!--.add-ziyuan-col-->
            <div class="add-ziyuan-col">

                <!--.form-col-->
                <div class="form-col">

                    <!--.form-head-->
                    <div class="form-head clearfix">
                        <h4>资源分类属性</h4>
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

            <button type="button" class="add-ziyuan-btn"> + 增加课件分类属性</button>

            <!--.form-col-->
            <div class="form-col">

                <!--.form-head-->
                <div class="form-head clearfix">
                    <h4>资源文件(资源文件不得修改)</h4>
                </div>
                
                
                <div>
                <br>
                  <h1> 资源文件:${resName}</h1>
                </div>
                <!--/.form-head-->

                <!--.form-main-->
                <div class="form-main uploader-info">

                    <div class="clearfix"  style="display:none;">
                        <label>资源文件</label>
                        <div id="uploader" class="wu-example">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns clearfix">
                                <div id="picker">选择文件</div>
                                <button id="ctlBtn" class="btn btn-default">开始上传</button>
                            </div>
                        </div>
                        <input id="fileId" type="hidden" value="${fileId}"/>
                    </div>

                    <div class="clearfix"  style="display:none;">
                        <label>资源封面</label>
                        <!--dom结构部分-->
                        <div id="uploader-demo">
                            <div id="filePicker">选择图片</div>
                            <!--用来存放item-->
                            <div id="fileList" class="uploader-list"></div>
                        </div>
                        <input id="coverId" type="hidden" value="${coverId}"/> 
                    </div>

                    <div class="btn-list">
                        <button type="button" id="commitUploadBtn">保存</button>
                        <button type="button" class="reset-btn" id="cancelBtn">取消</button>
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
	var isCourseware = ${isCourseware};
	var propertyList = ${properttList};
	var PUBLIC_ID = "${id}";
	var PUBLIC_FILE_ID = "${fileId}";
    seajs.use('gitresourseedit');
</script>
</body>

</html>
