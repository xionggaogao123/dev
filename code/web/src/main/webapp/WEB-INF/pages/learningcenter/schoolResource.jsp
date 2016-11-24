<%@page import="com.pojo.user.UserRole"%>
<%@page import="com.fulaan.base.controller.BaseController"%>
<%@page import="com.pojo.app.SessionValue"%>
<%@ page import="com.pojo.lesson.DirType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <meta name="renderer" content="webkit">
  <title>校本资源</title>
  <link rel="stylesheet" type="text/css" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/flippedclassroom.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/class-course.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/tree.css"/>
  <link rel="stylesheet" type="text/css" href="/static/js/contextMenu/src/jquery.contextMenu.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/artDialog/ui-dialog.css"/>
  <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
  <script type="text/javascript" src='/static/js/jquery-1.11.1.min.js'></script>
  <script type="text/javascript" src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js"></script>
  <script type="text/javascript" src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
  <script type="text/javascript" src="/static/js/contextMenu/src/jquery.contextMenu.js"></script>
  <script type="text/javascript" src="/static/js/artDialog/dialog-plus-min.js"></script>
  <script type="text/javascript" src="/static/js/lessons/coursesManage.js"></script>
  <script type="text/javascript" src="/static/js/experienceScore.js"></script>

  <script>
    var dirType = '<%=DirType.SCHOOL_RESOURCE%>';
    var currentPageID = 1;
    var teacherDialog = null;
    var selectedFolder = null;
    var preSelectedFoder = null;
    op = true;
    $(function () {
      navRoot();
      $.fn.zTree.init($("#school-course-ul"), {
        async: {
          enable: true,
          url: '/dir/find.do?type=' + dirType/*,
           autoParam: ['id'],
           dataFilter : function(treeId, parent, data){
           filterDirNodes(data);
           return data;
           }*/
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
              if (node.parent || node.type) {
                nav(node.id);
              } else {
                nav(node.id, 'COURSE');
              }
              DeleteBtnAllowed();
            }
          },
          beforeRename: beforeRename,
          onRename: onRename,
          onAsyncSuccess: function (event, treeId, treeNode, msg) {
            asyncTreeCallback.onAsyncSuccess();
            makeTreeDroppable(treeId);
          },
          onExpand: function (evt, treeId) {
            makeTreeDroppable(treeId);
          },
          onDrop: moveDir
        })
      });
      $.contextMenu({
        selector: '.fa-bars.file-op',
        trigger: 'left',
        callback: function (key, options) {
          var lesson = this.parent(),
                  id = lesson.attr('file-id');
          switch (key) {
            case 'delete':
              deleteLesson(id);
              break;
            case 'pushToTeacher':
              showTeacherPushingDialog();
              break;
            case 'play':
              play(lesson.attr('file-id'));
              break;
          }
        },
        items: {
          'delete': {
            name: '删除', icon: 'fa fa-times', accesskey: 'd', type: 'awesome'
          },
          play: {
            name: '播放', icon: 'fa fa-play', accesskey: 'p', type: 'awesome'
          },
          pushToTeacher: {
            name: '推送至备课空间', icon: 'fa fa-cloud-download', accesskey: 't', type: 'awesome'
          }
        }
      });
    });

    function showTeacherPushingDialog(id) {
      var treeId = 'teacher-tree';
      if ($('#' + treeId + ' li').length == 0) {
        $.fn.zTree.init($('#' + treeId), {
          async: {
            enable: true,
            url: '/dir/find.do?type=BACK_UP'
          },
          data: {
            simpleData: {
              enable: true,
              pIdKey: 'parentId'
            }
          },
          callback: {
            beforeClick: function (treeId, node) {
              return node.type != 'classSubject';
            },
            onAsyncSuccess: function (event, treeId) {
              expandAll(event, treeId);
            }
          },
          view: {
            selectedMulti: true
          }
        });
        teacherDialog = showPushingDialog('推送课程至备课空间', $('#teacher-dialog-content')[0], treeId);
      }
      teacherDialog.lessonId = id;

      teacherDialog.showModal();
    }

    function newRootDir() {
      var name = prompt('请输入文件夹名');
      if (name) {
        $.post('/dir/add.do', {
          name: name,
          type: 3
        }, reloadRoot);
      }
    }

    function getDirTree() {
      return $.fn.zTree.getZTreeObj("school-course-ul");
    }

    function navRoot() {
      loadCourse('/lesson/load/list.do', {
        type: dirType
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
  </script>
</head>
<body>
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>

<!-- 页头 -->

<div id="content_main_container">
  <div id="content_main">
    <!-- 左侧导航-->
    <%--<%@ include file="../common_new/col-left.jsp" %>--%>
    <%@ include file="../common_new/col-left.jsp" %>

    <!-- left end -->
    <!-- right start-->
    <div id="right-container">
      <jsp:include page="../common/teacherInfoHead.jsp">
        <jsp:param name="dir" value="school"/>
      </jsp:include>
      <div class="main-container">
        <div class="course-left-container">
          <div id="file-tools">
            <ul>
              <a id="newFolder" onclick="newDirByToolBtn()">
                <li class="fa fa-plus fa-1.5x"><label style="margin-left:7px;cursor:pointer;">新建文件夹</label></li>
              </a>
              <a id="deleteFolder" onclick="deleteDirByToolBtn()" class="tool-not-allowed" title="请先选择文件夹">
                   <% 
                      SessionValue sv =(SessionValue)request.getAttribute(BaseController.SESSION_VALUE);
                      if(sv.getUserRole()!=UserRole.TEACHER.getRole())
                      {
                    	 %>
                    	    <li class="fa fa-trash-o fa-1.5x">
                    	    <label style="margin-left:7px;" class="tool-not-allowed">删除</label>
                    	 <% 
                      }
                   %>
                </li>
              </a>
            </ul>
          </div>
          <ul id="school-course-ul" class="ztree dir-tree">
          </ul>
        </div>
        <div class="course-right-container">
        </div>
      </div>
    </div>
    <!-- right end-->
  </div>
  <div style="display: inline">

    <div class="model-bg"></div>

    <!-- 页尾 -->
    <%@ include file="../common_new/foot.jsp" %>
    <div style="display: none">
      <div id="teacher-dialog-content">
        <ul id="teacher-tree" class="ztree dir-tree"></ul>
      </div>
    </div>
</body>

</html>