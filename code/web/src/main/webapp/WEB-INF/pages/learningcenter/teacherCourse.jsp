<%@ page import="com.pojo.lesson.DirType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <title>备课空</title>
    <script>
        var UEDITOR_HOME_URL = "/static/plugins/ueditor/";
    </script>
    <link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedclassroom.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/class-course.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/tree.css"/>
    <link rel="stylesheet" type="text/css" href="/static/js/contextMenu/src/jquery.contextMenu.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/artDialog/ui-dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/cloudclass.css">
    <link rel="stylesheet" type="text/css" href="/static/css/excellentLesson.css"/>
    <link rel="stylesheet" type="text/css" href="/static/js/jquery-ui-1.11.1.custom/jquery-ui.min.css"/>
    <script type="text/javascript" src='/static/js/jquery-1.11.1.min.js'></script>
    <script type="text/javascript" src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src='/static/js/jquery-ui.min.js'></script>
    <script type="text/javascript" src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
    <script type="text/javascript" src="/static/js/contextMenu/src/jquery.contextMenu.js"></script>
    <script type="text/javascript" src="/static/js/artDialog/dialog-plus-min.js"></script>
    <script type="text/javascript" src="/static/js/lessons/coursesManage.js"></script>
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>
    <script type="text/javascript" src="/static/js/excellentLesson.js"></script>
    <script type="text/javascript" src="/static/js/excellentPay.js"></script>

    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>
    <style type="text/css">
        .subject-ul li, .grade-ul li, .stage-ul li {
            float: left;
            padding: 0 10px;
            cursor: pointer;
        }

        /************************弹出**********************/
        .homework-bg{
            width: 100%;
            height: 100%;
            background: black;
            opacity: 0.4;
            filter:alpha(opacity=40);
            position: fixed;
            top:0px;
            left: 0px;
        }
        .homework-popup{
            width: 540px;
            min-height: 320px;
            background: #ffffff;
            position: fixed;
            top:40%;
            left: 50%;
            margin-left: -270px;
            margin-top: -160px;
            border-radius: 10px;

        }
        .homework-popup dt{
            margin-bottom: 10px;
            height: 52px;
            line-height: 52px;
            border-bottom: 1px solid #d2d2d2;
        }

        .popup-to-le{
            margin-left: 20px;
            color: black;
            font-size: 15px;
        }
        .popup-to-ri{
            float: right;
            margin-right: 50px;
            font-weight: bold;
            font-size: 17px;
            cursor: pointer;
        }
        .homework-popup dd{
            clear: both;
            padding: 0px 0 0 35px;
            min-height: 32px;
            font-size: 14px;
            position:relative;
        }
        .homework-popup em{
            vertical-align: top;
            width: 65px;
            display: inline-block;
        }
        .popup-te{
            width: 304px;
            height: 27px;
            border:1px solid #d2d2d2;
            border-radius: 5px;
            text-indent: 0.5em;
        }
        /*.homework-popup input{

        }*/


        .homework-popup span{
            margin-right: 5px;
        }
        .homework-popup i{
            display: inline-block;
            padding: 0 10px;
        }
        .homework-popup textarea{
            width: 400px;
            height: 102px;
            text-indent: 0.5em;
            border: 1px solid #d2d2d2;
            border-radius: 5px;
            resize: none;
            outline: none;
            font-size: 14px;
        }
        .homework-popup button{
            width:50px;
            height: 25px;
            background: #fb7e00;
            line-height: 20px!important;
            color: #ffffff;
            float: right;
            margin-right: 50px;
            margin-bottom: 10px;
            margin-top: 15px;;
        }
        #hw-classList span{
            display: inline-block;
        }

    </style>
    <%
        Boolean isVote = (Boolean) session.getAttribute("isVote");
    %>
    <script src="/static/js/jquery-ui-1.11.1.custom/jquery-ui.min.js"></script>
    <script>
        var dirType = '<%=DirType.BACK_UP%>';
        var currentPageID = 1;
        var classDialog = null;
        var schoolDialog = null;
        var selectedFolder = null;
        var preSelectedFoder = null;
        var saleCourseDialog = null;
        var locUrl = window.location.href.split('?');
        var html = '';
        op = true;
        var type = null;
        //var path = '';
        $(function () {
            type = 1;
            $.ajax({
                url: '/emarket/introduction.do',
                data: {},
                success: function (data) {
                    $('#teach-descript').val(data.introduction);
                    $('#teacherinfo').html('( ' + data.teacherinfo + ' )');
                }
            });

            navRoot();
            $.fn.zTree.init($("#teacherDirUl"), {
                async: {
                    enable: true,
                    url: '/dir/find.do?type=' + dirType/*,
                     autoParam: ['id']*/
                },
                data: {
                    simpleData: {
                        enable: true,
                        pIdKey: 'parentId',
                        rootPId: 0
                    }
                },
                edit: {
                    enable: true,
                    drag: {
                        isCopy: false
                    },
                    renameTitle: '重命名',
                    editNameSelectAll: true,
                    showRemoveBtn: false,
                    showRenameBtn: true
                },
                view: {
                    addHoverDom: function (treeId, treeNode) {
                        scrollNode(treeId, treeNode);
                    },
                    removeHoverDom: function (treeId) {
                        cancelScrollNode(treeId);
                    }
                },
                callback: $.extend({}, asyncTreeCallback, {
                    onClick: function (event, treeId, node) {
                        getSelectedFolderId(node.id);
                        var tree = getDirTree();
                        if (node.id == preSelectedFoder) {
                            tree.cancelSelectedNode(node);
                            clearupSelectedFolder();
                            DeleteBtnNotAllowed();
                        } else {
                            preSelectedFoder = node.id;
                            nav(node.id);
                            DeleteBtnAllowed();
                        }
                    },
                    beforeRename: beforeRename,
                    onRename: function(evt, treeId, node, isCancel){
                        onRename(evt, treeId, node, isCancel);
                        initFastTree();
                    },
                    onDrop: moveDir,
                    onAsyncSuccess: function (evt, treeId) {
                        asyncTreeCallback.onAsyncSuccess();
                        initFastTree();
                        makeTreeDroppable(treeId);
                    },
                    onExpand: function (evt, treeId) {
                        makeTreeDroppable(treeId);
                    }
                })
            });

            $.contextMenu({
                selector: '.fa-bars.file-op',
                trigger: 'left',
                callback: function (key) {
                    var lesson = this.parent(),
                            id = lesson.attr('file-id');
                    switch (key) {
                        case 'edit' :
                            editLesson(id);
                            break;
                        case 'delete':
                            deleteLesson(id);
                            break;
                        case 'pushToCourse':
                            showCoursePushingDialog();
                            break;
                        case 'pushToSchool':
                            showSchoolPushingDialog();
                            break;
                        case 'play':
                            play(lesson.attr('file-id'));
                            break;
                    }
                },
                items: {
                    edit: {
                        name: '编辑', icon: 'fa fa-align-left', accesskey: 'e', type: 'awesome'
                    },
                    'delete': {
                        name: '删除', icon: 'fa fa-times', accesskey: 'd', type: 'awesome'
                    },
                    pushToCourse: {
                        name: '推送到班级', icon: 'fa fa-send', accesskey: 'c', type: 'awesome'
                    },
                    pushToSchool: {
                        name: '推送到校本资源', icon: 'fa fa-share', accesskey: 's', type: 'awesome'
                    },
                    play: {
                        name: '播放', icon: 'fa fa-play', accesskey: 'p', type: 'awesome'
                    }
                }
            });

            <c:if test="${param.fastNewLesson}">
            showFastDialog();
            </c:if>
        });

        function initFastTree() {
            $.fn.zTree.init($("#teacherDirUlFast"), {
                callback: $.extend({
                    onClick: function (event, treeId, node) {
                        getSelectedFolderId(node.id);
                        var tree = getDirTreeView();
                        if (node.id == preSelectedFoder) {
                            tree.cancelSelectedNode(node);
                            clearupSelectedFolder();
                            $('#fileParentId').val('');
                        } else {
                            preSelectedFoder = node.id;
                            $('#fileParentId').val(node.id);
                        }
                    }
                })
            }, getDirTree().getNodes());
        }

        function getFilePath(node) {
            path = node.name + '/' + path;
            var parentNode = node.getParentNode();
            if (parentNode != null && parentNode.name != '') {
                getFilePath(parentNode);
            }
            if (parentNode == null) {
                $('#filepath').val(path.substring(0, path.lastIndexOf('/')));
            }
        }



        function showCoursePushingDialog(id) {
            if ($('#classTree li').length == 0) {
                $.fn.zTree.init($('#classTree'), {
                    async: {
                        enable: true,
                        <c:choose>
                        <c:when test="${isVote}">
                        url: '/directory/voteDirs.do'
                        </c:when>
                        <c:otherwise>
                        url: '/dir/find.do?type=CLASS_LESSON',
                        dataFilter: classSubjectsFilter
                        </c:otherwise>
                        </c:choose>
                    },
                    <%--<c:if test="${isVote}">--%>
                    data: {
                        simpleData: {
                            enable: true,
                            pIdKey: 'parentId',
                            rootPId: 0
                        }
                    },
                    <%--</c:if>--%>
                    callback: {
                        beforeClick: function (treeId, node) {
                            if (node.type != 'classSubject') {
                                /**
                                 *多选操作
                                 */
                                var ztreeObj = this.getZTreeObj(treeId);
                                if (ztreeObj.isSelectedNode(node)) {
                                    ztreeObj.cancelSelectedNode(node);
                                } else {
                                    ztreeObj.selectNode(node, true);
                                }
                            }
                            return false;
                        },
                        onAsyncSuccess: function (event, treeId) {
                            expandAll(event, treeId);
                        },
                        onRename: addDir
                    },
                    view: {
                        selectedMulti: true,
                        addHoverDom: renderTreeNewButton,
                        removeHoverDom: removeTreeNewButton
                    }
                });
                classDialog = showPushingDialog('${isVote?"参加微课评比":"推送课程至班级"}', $('#class-dialog-content')[0], 'classTree');
            }
            classDialog.lessonId = id;

            classDialog.showModal();
        }

        function showSchoolPushingDialog(id) {
            if ($('#schoolTree li').length == 0) {
                $.fn.zTree.init($('#schoolTree'), {
                    async: {
                        enable: true,
                        url: '/dir/find.do?type=SCHOOL_RESOURCE',
                        autoParam: ['id']
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            pIdKey: 'parentId',
                            rootPId: 0
                        }
                    },
                    callback: {
                        beforeClick: function (treeId, node) {
                            /**
                             *多选操作
                             */
                            var ztreeObj = this.getZTreeObj(treeId);
                            if (ztreeObj.isSelectedNode(node)) {
                                ztreeObj.cancelSelectedNode(node);
                            } else {
                                ztreeObj.selectNode(node, true);
                            }
                            return false;
                        },
                        onAsyncSuccess: function (event, treeId) {
//                            expandAll(event, treeId);
                        },
                        onExpand: function (event, treeId, treeNode) {
                            if (treeNode && treeNode.children && treeNode.children.length == 0) {
                                var treeObj = $.fn.zTree.getZTreeObj(treeId);
                                treeNode.isParent = false;
                                treeObj.updateNode(treeNode);
                            }
                        },
                        onRename: addDir
                    },
                    view: {
                        selectedMulti: true,
                        addHoverDom: renderTreeNewButton,
                        removeHoverDom: removeTreeNewButton
                    }
                });
                schoolDialog = showPushingDialog('推送课程至校本目录', $('#school-dialog-content')[0], 'schoolTree');
            }
            schoolDialog.lessonId = id;

            schoolDialog.showModal();
        }

        function showLeaguePushingDialog(id) {
            if ($('#leagueTree li').length == 0) {
                $.fn.zTree.init($('#leagueTree'), {
                    async: {
                        enable: true,
                        url: '/league/info.do',
                        dataFilter: filterLeagueDirs
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            pIdKey: 'parentId'
                        }
                    },
                    callback: {
                        beforeClick: ztreeMultiSelect
                    },
                    view: {
                        selectedMulti: true
                    }
                });
                leagueDialog = showPushingDialog('推送课程至联盟资源', $('#league-dialog-content')[0], 'leagueTree');
            }
            leagueDialog.lessonId = id;

            leagueDialog.showModal();
        }

        function showsaleCoursePushingDialog(courseID, obj) {
            var classname = $(obj).prop('className');
            if (classname && classname.indexOf('unable') > 0) {
                alert('该课程来自云课程,不可推送到电子超市哦~~~');
            } else {
                $.ajax({
                    url: '/emarket/reAddLesson.do',
                    data: {'lessonId': courseID},
                    success: function (data) {
                        if (data.resultCode == 1) {
                            if (confirm('该课程已推送至电子超市,是否再次推送?')) {
                                showCourseSalePage(courseID);
                            }
                        } else {
                            /* $('#select-course').show();
                             saleCourseDialog = showPushingDialogStep1('推送至电子超市',$('#select-course'),courseID);
                             saleCourseDialog.showModal(); */
                            showCourseSalePage(courseID);
                        }
                    }
                });
            }
        }

        function showCourseSalePage(courseID) {
            $('#select-course').show();
            calculatePrice();
            saleCourseDialog = showPushingDialogStep1('推送至电子超市', $('#select-course'), courseID);
            saleCourseDialog.showModal();
            $('button[data-id="ok"]').removeClass('ui-dialog-autofocus').addClass('notallow');
        }
        function switchAgree() {
            if ($('#agree').prop('checked')) {
                $('button[data-id="ok"]').removeClass('notallow').addClass('ui-dialog-autofocus');
            } else {
                $('button[data-id="ok"]').removeClass('ui-dialog-autofocus').addClass('notallow');
            }
        }
        function showPushingDialogStep1(title, content, courseID) {
            var d = dialog({
                title: title,
                content: content,
                width: 833,
                height: 546,
                okValue: '保存',
                ok: function () {
                    saveExcellentCourse(courseID);
                    return false;
                },
                cancelValue: '取消',
                cancel: function () {
                    clearDialogData();
                    d.close();
                    return false;
                }
            });
            return d;
        }

        function saveExcellentCourse(courseID) {
            if (!$('#agree').prop('checked')) {
                return;
            }
            if (checkIsBlanck()) {
                var stageIdList = '';
                $('#stages-ul li input').each(function () {
                    if ($(this).prop('checked')) {
                        stageIdList += ($(this).val() + ',');
                    }
                });
                var subjectIdList = '';
                $('#subjects-ul li input').each(function () {
                    if ($(this).prop('checked')) {
                        subjectIdList += ($(this).val() + ',');
                    }
                });
                var gradeIdList = '';
                $('#grades-ul li input').each(function () {
                    if ($(this).prop('checked')) {
                        gradeIdList += ($(this).val() + ',');
                    }
                });
                var examOutlineIdList = '';
                $('#knowledges-ul li input').each(function () {
                    if ($(this).prop('checked')) {
                        examOutlineIdList += ($(this).val() + ',');
                    }
                });
                //var expiretime;
                /* if ($('#course-validity').val()=='') {
                 expiretime = 0;
                 } else {
                 expiretime = $('#course-validity').val();
                 } */
                var isOpen = '1';
                $('input[name="openinfo"]').each(function () {
                    if ($(this).prop('checked')) {
                        isOpen = $(this).val();
                    }
                });
                $.ajax({
                    url: '/emarket/addLesson.do',
                    type: 'POST',
                    data: {
                        'goodid':'',
                        'lessonId': courseID,
                        'stageids': stageIdList,
                        'subjectIds': subjectIdList,
                        'gradeIds': gradeIdList,
                        'examOutlineIds': examOutlineIdList,
                        'price': $('#course-price').val(),
                        'expiretime': 6,  //默认6个月
                        'lessonContent': ue.getContent(),
                        'editType': 1,//新增
                        'lessonName': '',
                        'isopen': isOpen
                    },
                    success: function (data) {
                        clearDialogData();
                        saleCourseDialog.close();
                        if (data.resultCode == 0) {
                            alert("推送成功！");
                        } else if (data.resultCode == 1) {
                            alert("推送失败！");
                        }
                    }
                });
            }
        }

        function clearDialogData() {
            $('input:checked').each(function () {
                if ($(this).prop('checked')) {
                    $(this).prop('checked', false);
                }
            });
            $('#course-price').val('');
            $('#course-validity').val('');
            ue.setContent('');
        }

        function filterDirNodes(nodes) {
            $.each(nodes, function (i, node) {
                node.isParent = true;
                if (node.children) {
                    filterDirNodes(node.children);
                }
            });
        }

        function newRootDir() {
            var name = prompt('请输入文件夹名');
            if (name) {
                $.post('/dir/add.do', {
                    name: name,
                    type: 1
                }, reloadRoot);
            }
        }

        function getDirTree() {
            return $.fn.zTree.getZTreeObj("teacherDirUl");
        }

        function getDirTreeView() {
            return $.fn.zTree.getZTreeObj("teacherDirUlFast");
        }

        function navRoot() {
            loadCourse('/lesson/load/list.do', {
                type: dirType,
                pageSize: 16
            });
        }
        function getSelectedFolderId(id) {
            selectedFolder = id;
        }
        function newDirByToolBtn() {
            if (selectedFolder) {
                newDir(selectedFolder);
            } else {
                newRootDir();
            }
        }
        function deleteDirByToolBtn() {
            if (selectedFolder) {
                var flag = deleteDir(selectedFolder);
                if (flag) {
                    clearupSelectedFolder();
                }
            } else {
                alert('请先选择要删除的文件夹');
            }
        }

        function clearupSelectedFolder() {
            selectedFolder = null;
            preSelectedFoder = null;
        }
        function DeleteBtnAllowed() {
            $('#deleteFolder').removeClass('tool-not-allowed').addClass('tool-allowed');
            $('#deleteFolder').attr('title', '删除文件夹');
            $('#deleteFolder li label').removeClass('tool-not-allowed').addClass('tool-allowed');

        }
        function DeleteBtnNotAllowed() {
            $('#deleteFolder').removeClass('tool-allowed').addClass('tool-not-allowed');
            $('#deleteFolder').attr('title', '请先选择文件夹');
            $('#deleteFolder li label').removeClass('tool-allowed').addClass('tool-not-allowed');
        }

        function saveContainer() {
            var descript = $('#teach-descript').val();
            $.ajax({
                url: '/emarket/updateIntroduction.do',
                type: 'POST',
                data: {
                    'introduce': descript
                },
                success: function (data) {
                    if (data.resultCode == 0) {
                        //alert("教师介绍保存成功！");
                        //$('#teach-descript').attr('disabled','disabled');
                    } else {
                        alert("教师介绍保存失败！");
                    }
                }
            });

        }


  $(function(){
      $(".content_main_head img").click(function(){
          $(".content_main_head").hide();
      })
  })

    </script>
</head>
<body>
<!-- 页头 -->
<%--<%@ include file="../common_new/head.jsp" %>--%>
<%@ include file="../common_new/head.jsp" %>
<!-- 页头 -->
<div id="content_main_container" style="overflow:hidden;">
    <div id="content_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->
        <!-- right start-->
        <div id="right-container">
            <jsp:include page="../common/teacherInfoHead.jsp">
                <jsp:param name="dir" value="teacher"/>
            </jsp:include>
            <div class="main-container">
                <div class="content_main_head">备课空间属于老师私密空间
                    <img src="/img/K6KT/recorder-XX.jpg">
                </div>
                <div class="course-left-container">
                    <div id="file-tools">
                        <ul>
                            <a id="newFolder" onclick="newDirByToolBtn()">
                                <li class="fa fa-plus fa-1.5x"><label
                                        style="margin-left:7px;cursor:pointer;">新建文件夹</label></li>
                            </a>
                            <a id="deleteFolder" onclick="deleteDirByToolBtn()" class="tool-not-allowed"
                               title="请先选择文件夹">
                                <li class="fa fa-trash-o fa-1.5x"><label style="margin-left:7px;"
                                                                         class="tool-not-allowed">删除</label></li>
                            </a>
                        </ul>
                    </div>
                    <ul id="teacherDirUl" class="ztree dir-tree">
                    </ul>
                </div>
                <div class="course-right-container">
                </div>
            </div>
        </div>
        <!-- right end-->
    </div>
    <div style="display: inline">


        <!-- 页尾 -->
        <%@ include file="../common_new/foot.jsp" %>
        <div style="display: none">
            <div id="class-dialog-content">
                <ul id="classTree" class="ztree dir-tree"></ul>
            </div>
            <div id="school-dialog-content">
                <ul id="schoolTree" class="ztree dir-tree"></ul>
            </div>
            <div id="league-dialog-content">
                <ul id="leagueTree" class="ztree dir-tree"></ul>
            </div>
        </div>
        <div id="dialog-fast-newlesson">
            <input id="fileParentId" style="display:none;" value="">

            <div class="fast-item">
                <span class="fast-label">请输入课程名称</span><input id="lessonname" name="lessoname" class="fast-input"
                                                              placeholder="输入课程名称"/>
            </div>

            <div class="fast-item">请选择存入备课空间文件夹</div>
            <div class="course-fast-container">
                <ul id="teacherDirUlFast" class="ztree dir-tree">
                </ul>
            </div>
            <div class="fast-btns">
                <a onclick="fastNewDir()" class="fast-newfolder">新建文件夹</a>
                <a onclick="fastNewLesson()" class="newlesson-next">下一步</a>
            </div>
        </div>

        <!-- 推送到精品课程 START -->
        <div id="select-course" style="display:none;">
            <div>
                <span id="stage-require" style="float: right;margin-right: 20px;" class="require-msg">*  选择"中小学"</span>
                <span id="grade-require" style="float: right;margin-right: 20px;" class="require-msg">*  选择"年级"</span>
            </div>
            <div class="sub-container-left">推送到分类</div>
            <div class="course-type">
                <div class="retrieval-stage" id="retrieval-stage">
                    <div class="stage-title">中小学</div>
                    <ul class="stage-ul" id="stages-ul">
                        <li data-stageId="2"><input type="checkbox" value="2" checked="checked"/>小学</li>
                        <li data-stageId="4"><input type="checkbox" value="4"/>初中</li>
                        <li data-stageId="8"><input type="checkbox" value="8"/>高中</li>
                    </ul>
                </div>
                <div class="retrieval-grade" id="retrieval-grade">
                    <div class="grade-title">年级</div>
                    <ul class="grade-ul" id="grades-ul">
                    </ul>
                </div>
                <div class="retrieval-subject">
                    <div class="subject-title">科目</div>
                    <ul class="subject-ul" id="subjects-ul">
                    </ul>
                </div>

            </div>
            <div class="middle-container">
                <span class="courselabel">课程价格</span>

                <select id="course-price" class="radis-input" onchange="calculatePrice()">
                    <option value="0">0</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>元
                <span id="price-require" style="margin-left: 6px;" class="require-msg">*  不要忘了定价哦</span>
                <span style="margin-left:15px;color:red;">[课程有效期为6个月,实际收入<span
                        id="actual-price"></span>元  (价格*0.7)]</span>

            </div>
            <div class="sub-container-left">课程介绍</div>
            <div>
                <script id="container" name="content" type="text/plain">

                </script>
                <!-- 配置文件 -->
                <script type="text/javascript" src="/static/plugins/ueditor/ueditor.config.js"></script>
                <!-- 编辑器源码文件 -->
                <script type="text/javascript" src="/static/plugins/ueditor/ueditor.all.js"></script>
                <!-- 实例化编辑器 -->
                <script type="text/javascript">
                    var ue = UE.getEditor('container');
                </script>
            </div>
            <div class="middle-container">
                <div class="sub-container-left">教师自我介绍</div>
                <div style="float:left;"><textarea id="teach-descript" cols="80" rows="3"
                                                   placeholder="老师,请介绍一下自己的光辉事迹哦~~~比如:职称、个人荣誉及其他成功经历..."></textarea>
                </div>
                <div><a href="javascript:saveContainer()"><span class="link-btn underline">保存</span></a></div>
            </div>
            <div class="middle-container" style="margin-bottom:5px;">
                <div style="margin-left:40px;"><span>个人信息</span><span style="padding: 1px 8px;" id="isOpen"><input
                        type="radio" name="openinfo" value="1" checked="checked">公开<input type="radio" name="openinfo"
                                                                                          value="0"
                                                                                          style="margin-left: 18px;">不公开</span>
                </div>
            </div>
            <div>
                <div style="margin-left:90px;">
                    <span id="teacherinfo" style="padding: 1px 8px;font-size: 12px;color: #888;"></span>
                    <span class="protocol"><input id="agree" type="checkbox" style="vertical-align: middle;"
                                                  onclick="switchAgree()"><span
                            style="vertical-align: middle;margin: 0 4px;">我已阅读<a href="/protocol/k6kt" target="_blank"
                                                                                 style="font-weight: 600;color: #ff7b00;">《电子超市使用协议》</a></span></span>
                </div>
            </div>
            <%--<div i="button" class="ui-dialog-button"><button type="button" data-id="cancel">取消</button><button type="button" data-id="ok" autofocus="" class="notallow">保存</button></div>--%>
        </div>

        <!-- 推送到精品课程 START -->


        <div class="homework-bg" style="display: none;z-index:2">
        </div>
        <div class="homework-popup" style="display: none ;z-index:2;overflow: visible">
            <dl>
                <dt>
                    <span class="popup-to-le">新建作业</span>
                    <span class="popup-to-ri" onclick="closepushingtohomework()" style="margin-right: 30px">x</span>
                </dt>
                <dd>
                    <em>标题：</em>
                    <input type="text" class="popup-te" id="hw-title">
                </dd>
                <dd>
                    <em>班级：</em>

                    <div id="hw-classList" style="display: inline-block;width: 390px">
                        <%--<span><input type="checkbox" name="ch"><i>三年级一班</i></span>--%>
                        <%--<span><input type="checkbox" name="ch"><i>三年级一班</i></span>--%>
                        <%--<span><input type="checkbox" name="ch"><i>三年级一班</i></span>--%>
                        <%--<span><input type="checkbox" name="ch"><i>三年级一班</i></span>--%>
                    </div>
                </dd>
                <dd>
                    <em>类型：</em>
                    <input type="radio" name="hw-type" class="basic-ra" value="0"><i>课前</i>
                    <input type="radio" name="hw-type" class="basic-ra" value="1"><i>课后</i>
                    <%--<input type="radio" name="hw-type" class="basic-ra" value="2"><i>其他</i>--%>
                </dd>
                <dd>
                    <em>内容：</em>
                    <textarea id="hw-con"> </textarea>
                </dd>
                <dd>
                    <%--<i class="fa fa-microphone  cur-en cur-co" id="yy"></i><span>语音</span>--%>
                    <%--<div style="padding-top: 10px;position: absolute;z-index: 50000;">--%>
                    <%--<div class="sanjiao"--%>
                    <%--style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;display:none;"></div>--%>
                    <%--<div id="myContent">--%>
                    <%----%>
                    <%--</div>--%>
                    <%--</div>--%>
                        <div id="submit_section" style="border:0px;padding: 0px;margin-left:55px">
                    <div id="recordercontainer1" style="clear:left;height:24px">
                        <div id="recorder" class="homework-recorder" >
                            <div class="area" >
                                        <span class="a12" onclick="showflash('recordercontainer1')">
                                            <img src="/img/mic.png" style="width: 19px; height: 19px;"/>
                                            语音
                                        </span>

                                <div style="padding-top: 10px;position: absolute;z-index: 50000">
                                    <div class="sanjiao"
                                         style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;display:none;"></div>
                                    <div id="myContent" >
                                    </div>
                                </div>
                            </div>
                            <form id="uploadForm" name="uploadForm" action="audiosave.jsp?userId=&voicetype=0&isinform=1">
                                <input name="authenticity_token" value="xxxxx" type="hidden">
                                <input name="upload_file[parent_id]" value="1" type="hidden">
                                <input name="format" value="jso n" type="hidden">
                            </form>
                        </div>
                    </div>
                            </div>

                        <span class="teacherCourse-Y"><input class="basic-TJ" type="checkbox" id="pg" checked>
                    <i>需要老师批改作业</i></span>

                    <button id="fb" onclick="pushtohomework()">
                        发布
                    </button>
                </dd>
            </dl>
        </div>
</body>

<!-- homework use -->
<script type="text/javascript">
    //推送到作业
    var pushToHomeworkLessonId = 0;
    var isFromCloud = 0;

    function getTeacherClassSubjects(){
        $.ajax({
            url: '/homework/teacher/section.do',
            data: {},
            success: function (resp) {
                var classsubjectshtml = "";
                var it = resp.classSubjectList;
                for(var i in it){
                    classsubjectshtml+='<span>'+'<input classId='+it[i].classId+' subjectId='+it[i].subjectId+' type="checkbox" name="hw-clasub"><i>';
                    classsubjectshtml+=it[i].className+'('+it[i].subjectName+')';
                    classsubjectshtml+='</i>'
                    classsubjectshtml += '<input type="radio" name="'+it[i].classId+'" class="lesson" value="0" hidden="hidden" checked="true">';
                    var lesson = it[i].lesson;
                    if(it[i].classType==2 && lesson.length>0){
                        classsubjectshtml +='(课时：';
                        for(var j in lesson){
                            classsubjectshtml +='<input type="radio" name="'+it[i].classId+'" class="lesson" value="'+lesson[j].field+'"><span>'+lesson[j].value+'</span>';
                        }
                        classsubjectshtml +=')';
                    }
                    classsubjectshtml+='</span>'
                }

                $('#hw-classList').html(classsubjectshtml);
            }
        });
    }
    getTeacherClassSubjects();
    function showPushToHomeworkDialog(id, title, ifc){

        $("#hw-title")[0].value=title;
        $("#hw-con")[0].value='';
        $("#myContent").html('');
        $("#submit_section p").remove();
        $(".homework-bg").show();
        $(".homework-popup").show();
        pushToHomeworkLessonId = id;
        if(ifc == 'true'){
            isFromCloud = 1;
        } else {
            isFromCloud = 0;
        }
    }
    function closepushingtohomework(){
        $(".homework-bg").hide();
        $(".homework-popup").hide();
        pushToHomeworkLessonId = 0;
    }

    function pushtohomework(){

        //check input


        if(pushToHomeworkLessonId){
            var requestData={};
            requestData.lessonId = pushToHomeworkLessonId;
            requestData.hwTitle = $("#hw-title").val();
            requestData.hwContent = $("#hw-con").val();
            requestData.pg = 0;
            if($("#pg").prop("checked")){
                requestData.pg = 1;
            }
            var classIdList=$("input[name='hw-clasub']:checkbox:checked").map(function(index,elem) {
                var index = $(elem).siblings("input:radio:checked").val();
                if(index == null){
                    index = 0;
                }
                return "0," + $(elem).attr("classId") + "," + index;
            }).get().join(';');
            requestData.classIdList = classIdList;
            var subjectId = $("input[name='hw-clasub']:checkbox:checked").map(function(index,elem) {
                return $(elem).attr("subjectId")
            }).get();
            requestData.subjectId =subjectId[0];

            requestData.hwType = $("input[name='hw-type']:radio:checked").val();

            var voicefile = $('.voice').attr('url');
            requestData.voiceUrl = voicefile;
            if(!classIdList){
                alert("请选择班级");
                return;
            }
            if(!requestData.hwType){
                alert("请选择作业类型");
                return;
            }
            if(requestData.hwTitle.length<1){
                alert("请填写作业标题");
                return;
            }
            requestData.isFromCloud = isFromCloud;

            $.ajax({
                url: '/lesson/pushToHomework.do',
                data:requestData,
                success: function (resp) {
                    if(resp.result =='ok')
                    {
                        if (resp.score == 0) {
                            scoreManager("推送到作业", resp.score);
                        }
                        alert("推送作业成功，请到作业板块查看。")
                    }

                }
            });


            $(".homework-popup").hide();

            $(".homework-bg").hide();
        }
        else
        {
            alert("未得到课程信息，请重新推送。")
        }
    }
    var foo = true;
    function showflash(container) {
        var mc = new FlashObject("/static/plugins/audiorecorder.swf", "recorderApp", "350px", "23px", "8");
        mc.setAttribute("id", "recorderApp");
        mc.setAttribute("name", "recorderApp");

        mc.addVariable("uploadAction", "/homework/uploadvoice.do");
        mc.addVariable("fileName", "audio");
        mc.addVariable("recordTime", 10 * 60 * 1000);
        mc.addVariable("appName", "recorderApp");
        mc.write("myContent");
        if (foo) {
            $(container).append($('#recorder'));
            $('#recorder .sanjiao').show();
            $("#myContent").show();
            foo = false;
        } else {
            $("#myContent").hide();
            $('#recorder .sanjiao').hide();
            foo = true;
        }
    }
    function loadRecorder(ob) {
        $('.recorder').attr('id', '');
        $(ob).next().attr('id', 'recorder').show();
        $('#recorder record_button').click();
    }
</script>

</html>