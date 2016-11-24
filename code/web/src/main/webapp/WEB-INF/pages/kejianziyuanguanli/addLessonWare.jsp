<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>  
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <!-- webuploader styles-->
    <link href="/static_new/js/modules/webuploader/webuploader.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="/static_new/css/ziyuan.css" rel="stylesheet" />
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
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
                    </div>
                    <!--/.form-head->

                    <!--.form-main-->
                    <div class="form-main clearfix complex-value-class">
                        <input id="id" type="hidden" value=""/>
                        <dl>
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
                    </div>
                    <!--/.form-main-->

                </div>
                <!--/.form-col-->

            </div>
            <!--/.add-ziyuan-col-->
            <!--.form-col-->
            <div class="form-col">

                <!--.form-head-->
                <div class="form-head clearfix">
                    <h4>资源文件(本次上传将覆盖原文件)</h4>
                </div>
                <!--/.form-head-->

                <!--.form-main-->
                <div class="form-main uploader-info">

                    <div class="clearfix">
                        <label>资源文件</label>
                        <div id="uploader" class="wu-example">
                            <!--用来存放文件信息-->
                            <div id="thelist" class="uploader-list"></div>
                            <div class="btns clearfix">
                                <div id="picker">选择文件</div>
                                <button id="ctlBtn" class="btn btn-default">开始上传</button>
                            </div>
                            <%--<p class="img-script"> 支持的视频文件格式：avi/mp4/mpg/flv/wmv/mov/mkv，支持文件大小：100M以内</p>--%>
                        </div>
                        <input id="fileId" type="hidden"/>
                    </div>

                    <div class="clearfix">
                        <label>资源封面</label>
                        <!--dom结构部分-->
                        <div id="uploader-demo">
                            <div id="filePicker">选择图片</div>
                            <!--用来存放item-->
                            <div id="fileList" class="uploader-list"></div>
                        </div>
                        <input id="coverId" type="hidden"/>
                    </div>

                    <div class="btn-list">
                        <button type="button" id="commitUploadBtn">确认上传</button>
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
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script src="/static_new/js/modules/webuploader/webuploader.min.js"></script>
<script>
    seajs.use('addLessonWare',function(addLessonWare){
        addLessonWare.init();
    });
</script>
</body>

</html>
