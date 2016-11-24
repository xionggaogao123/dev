<%@ page import="com.pojo.lesson.DirType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <title>教师课程</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css?v=1209">
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedclassroom.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/class-course.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/tree.css"/>
    <link rel="stylesheet" type="text/css" href="/static/js/contextMenu/src/jquery.contextMenu.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/artDialog/ui-dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <script src='/static/js/jquery-1.11.1.min.js'></script>
    <script src="/static/js/bootstrap-paginator.min.js"></script>
    <script src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
    <script src="/static/js/contextMenu/src/jquery.contextMenu.js"></script>
    <script src="/static/js/artDialog/dialog-plus-min.js"></script>
    <script src="/static/js/lessons/coursesManage.js"></script>
    <script>
        var dirType = '<%=DirType.BACK_UP%>';
        var teacherId = '${param.teacherId}';
        var currentPageID = 2;
        var dirId;
        $(function () {
            navRoot();
            $.fn.zTree.init($("#teacherDirUl"), {
                async: {
                    enable: true,
                    url: '/myschool/backdir.do?userId='+teacherId+'&type='+dirType,
                    otherParam: {teacherId: teacherId}
                },
                data: {
                    simpleData: {
                        enable: true,
                        pIdKey: 'parentId',
                        rootPId: 0
                    }
                },
                callback: $.extend({
                    onClick: function (event, treeId, node) {
                        nav(node.id, 1);
                    }
                }, asyncTreeCallback)
            });
        });

        function navRoot() {
        	
        	var rootDirId='${rootDirId}';
        	if(rootDirId)
        		{
		            loadCourse('/lesson/dir/list/page.do?dirId='+'${rootDirId}'+'&viewonly=1', {
		            });
        		}
        }
    </script>
    <style>
        .student-course-container .course-container {
            height: 170px;
        }
    </style>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container" style="width: 1000px;margin: 0 auto">
    <div id="content_main">
        <%@ include file="../common_new/col-left.jsp" %>
        <div id="right-container" >
            <div class="main-container">
                <div class="course-left-container">
                    <ul id="teacherDirUl" class="ztree dir-tree"></ul>
                </div>
                <div class="course-right-container student-course-container"></div>
            </div>
        </div>
    </div>
</div>

<div style="clear: both"></div>
<div style="display: inline;clear: both">
    <%@ include file="../common_new/foot.jsp" %>
</div>
</body>