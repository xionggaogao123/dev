<%@ page import="com.pojo.lesson.DirType" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <meta name="renderer" content="webkit">
  <title>联盟资源</title>
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
  <style>
    .tab-bars{background-color: #E5E5E5;height: 43px;overflow: hidden;color: #828282;background-attachment: fixed;}
    #file-tools{height:35px;}
  </style>
  <script>
    var dirType = '<%=DirType.UNION_RESOURCE%>';
    var currentPageID = 1;
    var teacherDialog = null;
    var selectedFolder = null;
    var preSelectedFoder = null;
    op = true;
    $(function () {
      navRoot();
      $.fn.zTree.init($("#league-course-ul"), {
        async: {
          enable: true,
          url: '/league/info.do',
          dataFilter: filterLeagueDirs
        },
        data: {
          simpleData: {
            enable: true,
            pIdKey: 'parent'
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
          showRenameBtn: function (treeId, node) {
            return !node.virtual && node.manageable;
          }
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
              $('#newFolder').addClass('tool-not-allowed');
            } else {
              preSelectedFoder = node.id;
              nav(node.id);
              if (node.manageable) {
                $('#newFolder').removeClass('tool-not-allowed');
                if (!node.virtual) {
                  DeleteBtnAllowed();
                } else {
                  DeleteBtnNotAllowed();
                }
              } else {
                DeleteBtnNotAllowed();
                $('#newFolder').addClass('tool-not-allowed');
              }
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
      /*
      $.fn.zTree.init($('#league-ul'), {
        async: {
          enable: true,
          url: '/league/members.do',
          dataFilter: function (treeId, parent, data) {
            var result = [];
            $.each(data.leagues, function (i, league) {
              if (league.showMembers) {
                league.title = league.name;
              } else {
                league.title = '学校过多暂不显示';
              }
              result.push(league);
            });
            $.each(data.members, function (i, member) {
              member.id = 'MEMBER.' + member.id;
              result.push(member);
            });
            return result;
          }
        },
        data: {
          key: {
            title: 'title'
          },
          simpleData: {
            enable: true,
            pIdKey: 'leagueId'
          }
        }
      });*/

      $('#league-ul').hide();
      $('.league-tab').click(function(){
        $('.dir-tree').hide();
        $(this).addClass('select');
        $(this).siblings().removeClass('select');
        if($(this).attr('id') == 'league-course-ul-bt'){
          $('#league-course-ul').show();
          $('#file-tools').show();
        }
        if($(this).attr('id') == 'league-ul-bt'){
          $('#league-ul').show();
          $('#file-tools').hide();
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
        teacherDialog = showPushingDialog('推送课程至我的备课空间', $('#teacher-dialog-content')[0], treeId);
      }
      teacherDialog.lessonId = id;

      teacherDialog.showModal();
    }

    function getDirTree() {
      return $.fn.zTree.getZTreeObj("league-course-ul");
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

      //if (selectedFolder && $('#league-course-ul').find('.curSelectedNode').length > 0) {

      if ($('#newFolder').is('.tool-not-allowed')) {
        return;
      }
      if (selectedFolder) {
        newDir(selectedFolder);
      }else{
        alert('请先选择一个文件夹!');
      }
    }
    function deleteDirByToolBtn() {
      if ($('#deleteFolder').is('.tool-not-allowed')) {
        return;
      }
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
      /* if($('#league-course-ul').find('.curSelectedNode').length > 0){ */
      $('#deleteFolder').removeClass('tool-not-allowed').addClass('tool-allowed');
      $('#deleteFolder').attr('title', '删除文件夹');
      $('#deleteFolder li label').removeClass('tool-not-allowed').addClass('tool-allowed');
      /* }else{
       DeleteBtnNotAllowed();
       } */
    }
    function DeleteBtnNotAllowed() {
      /* if($('#league-course-ul').find('.curSelectedNode').length > 0){
       DeleteBtnAllowed();
       }else{ */
      $('#deleteFolder').removeClass('tool-allowed').addClass('tool-not-allowed');
      $('#deleteFolder').attr('title', '请先选择文件夹');
      $('#deleteFolder li label').removeClass('tool-allowed').addClass('tool-not-allowed');
      //}
    }

  </script>
</head>
<body>
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>

<!-- 页头 -->
<div class="model-bg"></div>
<div id="content_main_container">
  <div id="content_main">
    <!-- 左侧导航-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!-- left end -->
    <!-- right start-->
    <div id="right-container">
      <jsp:include page="../common/teacherInfoHead.jsp">
        <jsp:param name="dir" value="league"/>
      </jsp:include>
      <div class="main-container">
        <div class="course-left-container">
          <div class="tab-bars">
            <button id="league-course-ul-bt" class="league-tab select">联盟课程</button>
            <button id="league-ul-bt" class="league-tab">联盟成员</button>
          </div>
          <div id="file-tools">
            <ul>

              <a id="newFolder" onclick="newDirByToolBtn()" class="tool-not-allowed">
                <li class="fa fa-plus fa-1.5x"><label style="margin-left:7px;">新建文件夹</label></li>

              </a>
              <a id="deleteFolder" onclick="deleteDirByToolBtn()" class="tool-not-allowed" title="请先选择文件夹">
                <li class="fa fa-trash-o fa-1.5x"><label style="margin-left:7px;" class="tool-not-allowed">删除</label></li>
              </a>
            </ul>
          </div>
          <ul id="league-course-ul" class="ztree dir-tree">
          </ul>
          <ul id="league-ul" class="ztree dir-tree">
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
      <div id="teacher-dialog-content">
        <ul id="teacher-tree" class="ztree dir-tree"></ul>
      </div>
    </div>
</body>
</html>