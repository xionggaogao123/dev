/**
 * Created by Tony on 2014/8/6.
 */
var op = null;
var _currentDir = null;
var queryParams = {};
var queryUrl = '';
var asyncTreeCallback = {
    beforeAsync: function () {
        showLoading();
    },
    onAsyncSuccess: hideLoading,
    beforeDrag: function (treeId, nodes) {
        for (var i in nodes) {
            var node = nodes[i];
            if (node.type == 'classSubject' || node.virtual || node.type == 'LEAGUE' && !node.manageable) {
                return false;
            }
        }
        return true;
    },
    beforeDrop: function (treeId, nodes, target, moveType) {
        for (var i in nodes) {
            var node = nodes[i];
            if (node.type == 'LEAGUE') {
                //联盟文件夹不能跨联盟拖拽
                if (!target || node.owner != (target.virtual ? target.originId : target.owner)) {
                    return false;
                }
            }
        }
        return dirType == 'TEACHER' || dirType == 'SCHOOL' || (target && (target.parentTId || moveType == 'inner' || !target.virtual));
    }
};
var fastNewDirCallback = false;

if ($.contextMenu) {
    $.contextMenu.types.awesome = function (item, opt, root) {
        item.$node.addClass('awesome');
        $('<li class="' + item.icon + '">&nbsp;' + item.name + '(<span>' + item.accesskey + '</span>)</li>').appendTo(this);
    };
}

$(function () {
    $(document).ajaxStart(function () {
//        showLoading();
    });
    $(document).ajaxStop(function () {
        hideLoading();
    });
});

/**
 *多选操作
 */
function ztreeMultiSelect(treeId, node) {
    if (!node.virtual) {
        var ztreeObj = $.fn.zTree.getZTreeObj(treeId);
        if (ztreeObj.isSelectedNode(node)) {
            ztreeObj.cancelSelectedNode(node);
        } else {
            ztreeObj.selectNode(node, true);
        }
    }
    return false;
}

function getSelectedFileId() {
    return $('.course-container.selected').attr('file-id');
}

function select(file) {
    $('.course-container').removeClass('selected');
    $(file).addClass('selected');
    $('.fa-check-square').remove();
    $('#icons').children('li').removeClass('disable');
    $('.tool-icon').off('click');

    var id = $(file).attr('file-id');
    $('.fa-align-left').on('click', function () {
        editLesson(id);
    });
    $('.fa-times').on('click', function () {
        deleteLesson(id);
    });
    $('.fa-play').on('click', function () {
        play(id);
    });
    $('.fa-send').on('click', function () {
        showCoursePushingDialog();
    });
    $('.fa-share').on('click', function () {
        showSchoolPushingDialog();
    });
    $('.fa-cloud-download').on('click', function () {
        showTeacherPushingDialog();
    });
    $('.fa-rocket').on('click',function(){showsaleCoursePushingDialog();});
}

function nav(dirId, readOnly) {
    if(dirId!=undefined && dirId!=null){
        if(dirId.indexOf('LEAGUE')==0)
            return;
    }

    if (dirId == null) {
        if (_currentDir == null) {
            navRoot();
        } else {
            dirId = _currentDir;
        }
    }

    if (dirId != null) {
        loadCourse('/lesson/dir/list/page.do', {
            dirId: dirId,
            viewonly: readOnly
        });
    } else {
        loadCourse('/lesson/load/list.do', {
            type: dirType
        });
    }
}


function navClass(classId) {
    $('.course-right-container').load('/myschool/classsubject.do', {
        classId: classId
    });
}

function editLesson(id) {
    var url = '/lesson/edit.do?lessonId=' + id;
    location.href = url;
}
function editLessonByType(id,type) {
    var url = '/lesson/edit.do?lessonId=' + id;
    location.href = url;
}
function editPractise(lessonId){
    var url="/lesson/edit.do?lessonId="+lessonId;
    location.href=url;
}

function getLessonId(icon) {
    return $(icon).closest('.lesson').attr('file-id');
}

function getLessonTitle(icon) {
    return $(icon).closest('.lesson').attr('title');
}

function getIsFromCloud(icon){
    return $(icon).closest('.lesson').attr('isFromCloud');
}

function isDir(k, options) {
    return options.$trigger.is('.dir');

}

function navClassSubject(classSubjectId, readOnly) {
    loadCourse('/lesson/lessons.do', {
        type: 'COURSE',
        owner: classSubjectId
    });
}

function expandAll(event, treeId) {
    $.fn.zTree.getZTreeObj(treeId).expandAll(true);
    hideLoading();
}

function reloadDirTree(parentId) {
    var tree = getDirTree();
    /*
     if (parentId > 0) {
     var parentNode = tree.getNodeByParam('id', parentId);
     if (parentNode.isParent) {
     tree.reAsyncChildNodes(parentNode, 'refresh');
     } else {
     parentNode.isParent = true;
     tree.updateNode(parentNode);
     tree.reAsyncChildNodes(parentNode, 'refresh');
     }
     } else {
     tree.reAsyncChildNodes(null, 'refresh');
     }
     */
    tree.reAsyncChildNodes(null, 'refresh');
}

function reloadDirTreeView(parentId) {
    if (typeof getDirTreeView != 'undefined') {
        var tree = getDirTreeView();
        /*
         if (parentId > 0) {
         var parentNode = tree.getNodeByParam('id', parentId);
         if (parentNode.isParent) {
         tree.reAsyncChildNodes(parentNode, 'refresh');
         } else {
         parentNode.isParent = true;
         tree.updateNode(parentNode);
         tree.reAsyncChildNodes(parentNode, 'refresh');
         }
         } else {
         tree.reAsyncChildNodes(null, 'refresh');
         }
         */
        tree.reAsyncChildNodes(null, 'refresh');
    }
}

function reloadRoot() {
    navRoot();
    reloadDirTree();
    reloadDirTreeView();
}

function newDir(parentId) {
    var name = prompt('请输入文件夹名'),
        callback = function (result) {
            if (result) {
                //nav(parentId);
                reloadDirTree(parentId);
                reloadDirTreeView(parentId);
            }
        };
    if (name) {
        if (typeof parentId == 'string' && parentId.indexOf('.') >= 0) {
            var typeInfo = getTypeInfoById(parentId);
            $.post('/directory/addRootDir.do', {
                type: typeInfo.type,
                owner: typeInfo.id,
                name: name
            }, callback);
        } else if (parentId) {
            $.post('/dir/add.do', {
                parentdir: parentId,
                name: name
            }, callback);
        }
    }
}

function getTypeInfoById(id) {
    var info = id.split('.');
    return {
        type: info[0],
        id: info[1]
    };
}

function newLesson(dirId) {
    if (dirId) {
        var name = prompt('请输入课程名');
        if (name) {
            $.post('/lesson/create.do', {
                dirId: dirId,
                name: name
            }, function (result) {
                if (result.code == "200") {
                    editLesson(result.message);
                    nav(dirId);
                } else {
                    MessageBox(result.message, -1);
                }
            });
        }
    }
}

function deleteLesson(id) {
    if (confirm('确认删除课程？')) {
        $.post('/lesson/remove.do', {
            lessonId: id
        }, function (result) {
            if (result.code == '200') {
                nav();
                if (result.score) {
                    scoreManager(result.scoreMsg, result.score);
                }
            } else {
                alert(result.message);
            }
        });
    }
}

function deleteExcellentLesson(id) {
	if (confirm('确认删除课程？')) {
		$.ajax({
            url: '/emarket/deleteMyExcellentLesson.do',
            type: 'post',
            dataType: 'json',
            data: {
                'goodid': id
            }
        }).success(function(data) {
            if (data.resultCode==0) {
            	getMyExcellentLesson(1);
           } else {
            alert("删除失败！");
           }
            
        }).error(function() {
            alert('服务器错误！');
        });
    }
}

function deleteDir(id, callback) {
    if (confirm('确认删除文件夹?')) {
        $.post('/dir/remove.do', {
            dirId: id
        }, function (result) {
            if (callback) {
                callback(result);
            } else if (result.code == "200") {
                var parentNode = getParentDirNode(id),
                    parentId = parentNode ? parentNode.id : null;
                if (parentNode) {
                    nav(parentId);
                    reloadDirTree(parentId);
                    reloadDirTreeView(parentId);
                } else {
                    navRoot();
                    reloadDirTree();
                    reloadDirTreeView();
                }
            } else {
                alert('不能删除该文件夹');
            }
        });
        return true;
    }
}

function getParentDirNode(id) {
    var tree = getDirTree();
    return tree.getNodeByParam('id', id).getParentNode();
}

function showLoading() {
    if ($('#loading-mask').length) {
        $('#loading-mask').show();
    } else {
        $('<div id="loading-mask" style="position:absolute;left:50%;right:0;top:0;bottom:0;"><img style="position:absolute;top:50%;" src="/img/loading.gif"></div>').appendTo('body');
    }
    makeTop($('#loading-mask'));
}

function hideLoading() {
    if ($('#loading-mask').length) {
        $('#loading-mask').hide();
    }
}

function makeTop(o) {
    var zIndex = 1000;
    $('body').children().each(function (i, e) {
        var temp = parseInt($(e).css('z-index'));
        if (temp > zIndex) {
            zIndex = temp + 1;
        }
    });
    $(o).css('z-index', zIndex);
}

function classSubjectsFilter(treeId, parent, data) {
    if (data[0] && data[0].id) {
        //dirTree数据
        return data;
    }
    //classSubjects数据，转换为treeNode
    for (j in data) {
        var classSubjectObj = data[j],
            dirs = classSubjectObj.dirs;
        classSubjectObj = classSubjectObj.classSubject;
        classSubjectObj.children = dirs;
        classSubjectObj.name = ((classSubjectObj.classInfo != null) ? classSubjectObj.classInfo.classname : '') + (classSubjectObj.subject ? classSubjectObj.subject.name : '');
        classSubjectObj.type = 'classSubject';
        classSubjectObj.virtual = true;
        classSubjectObj.iconSkin = 'virtual';
        data[j] = classSubjectObj;
    }
    return data;
}

function filterLeagueDirs(treeId, parent, data) {
    var result = [], leagueMemberResult = [];
    $.each(data, function (i, leagueObj) {
        var league = {}; //leagueObj.league;
        league.name = leagueObj.name;
        league.originId = league.league = i;//league.id;
        league.id = 'LEAGUE.' + i;//league.id;
        league.type = 'LEAGUE';
        league.virtual = true;
        league.iconSkin = 'league';
        result.push(league);
        $.each(leagueObj.dirList, function (j, dir) {
            /*if (dir.parent == 0) {
                dir.parent = league.id;
            }*/
            if (!dir.parentId) {
                dir.parent = league.id;
            } else {
                dir.parent = dir.parentId;
            }
            dir.league = league.league;
            dir.manageable = league.manageable;
            result.push(dir);
        });


        leagueMemberResult.push(league);
        for(var i in leagueObj.memberList) {
            var member = leagueObj.memberList[i];
            var memberNode = {
                "id": member.idStr,
                "name": member.value,
                "leagueId": league.id
            };
            leagueMemberResult.push(memberNode);
        }
    });
    initLeagueMemberTree(leagueMemberResult);
    return result;
}

function initLeagueMemberTree(treeNodes) {
    $.fn.zTree.init($('#league-ul'), {
        data: {
            key: {
                title: 'title'
            },
            simpleData: {
                enable: true,
                pIdKey: 'leagueId'
            }
        }
    }, treeNodes);
}

function startPushing(dialog, id, ids) {
    dialog.title('推送中...');
    pushLesson(id, ids, function (result) {
        if (result) {
            dialog.close();
            //scoreManager("推送课程", 1);
            if (result.score) {
                scoreManager(result.scoreMsg, result.score);
            } else {
                alert('推送成功！');
            }
        }
    });
}

function pushLesson(id, dirIds, callback) {
    $.post('/lesson/push.do', {
        lessonId: id,
        dirIds: dirIds
    }, callback);
}

function getSelectedIds(treeId) {
    return $.map($.fn.zTree.getZTreeObj(treeId).getSelectedNodes(), function (node) {
        if (node.type != 'temp') {
            return node.id;
        }
    }).join();
}

function showPushingDialog(title, contentEle, treeId) {
    var d = dialog({
        title: title,
        content: contentEle,
        width: 486,
        height: 219,
        okValue: '确定',
        ok: function () {
            cancelEdit(treeId);
            var dirs = getSelectedIds(treeId);
            if (dirs) {
                startPushing(d, d.lessonId, dirs);
            } else {
                alert('请选择目标文件夹');
            }
            return false;
        },
        cancelValue: '取消',
        cancel: function () {
            d.close();
            return false;
        }
    });
    return d;
}

function play(id) {
    window.open('/lesson/view.do?lessonId=' + id);
}
function buyview(id) {//观看购买的课程
    window.open('/lesson/view.do?lessonId=' + id);
}

function stat(id) {
    window.open('/lesson/stat.do?lessonId=' + id);
}

function searchKeyPress(key, q) {
    if (key == 13 && q) {
        queryParams = {
            type: dirType,
            search: q,
            pageSize: 16
        };
        if (op) {
            queryParams.op = true;
        }
        switch (dirType) {
            case 'TEACHER':
                if (typeof teacherId != 'undefined' && teacherId) {
                    queryParams.owner = teacherId;
                }
                break;
            case 'COURSE':
                if (typeof classId != 'undefined' && classId) {
                    queryParams.classId = classId;
                }
                break;
        }
        queryUrl = '/directory/searchLessons';
        loadCourse(queryUrl, queryParams);
    }
}


/*快速新建课程*/
var fastDialog;
function initfastnewlessonDialog() {
    var d = dialog({
        title: '新建课程',
        content: $('#dialog-fast-newlesson'),
        width: 586,
        height: 360,
        skin: 'min-dialog tips',
        cancel: function () {
            d.close();
            if (window.location.href.indexOf('?flag=true') > -1) {
                location.href = '/teacher/course';
            }
            return false;
        },
        cancelDisplay: false
    });
    return d;
}
function fastnewlessonDialog() {
    if ($('#teacherDirUlFast').length) {
        showFastDialog();
    } else {
        location.href = '/lesson/teacher.do?fastNewLesson=true';
    }
}
function showFastDialog() {
    $('#dialog-fast-newlesson').show();
    fastDialog = initfastnewlessonDialog();
    fastDialog.showModal();
}
function fastNewLesson() {
    var dirId = $('#fileParentId').val();
    var name = $('#lessonname').val();
    if ($.trim(name) == '') {
        alert('请输入课程名称');
        return false;
    }
    if (!dirId) {
        alert('请选择备课空间');
        return false;
    }
//    var newWin = window.open();
    $.post('/lesson/create.do', {
        dirId: dirId,
        name: name
    }, function (result) {
        if (result.code == "200") {
            //editLesson(result.id);
            editPractise(result.message)
            nav(dirId);
            $('#lessonname').val('');
            $('#filepath').val('');
            //$('.ui-dialog-grid').hide();
            fastDialog.close();
        } else {
            MessageBox(result.message, -1);
        }
    });
}
function fastNewDir() {
    var parentId = $('#fileParentId').val();
    if (parentId) {
        newDir(parentId);
    } else {
        newRootDir();
    }
}

//文件夹重命名校验
function beforeRename(treeId, treeNode, newName, isCancel) {
    if (newName.length == 0) {
        alert('名称不能为空');
        return false;
    }
    return true;
}

//文件夹重命名
function onRename(evt, treeId, node, isCancel) {
    $.post('/dir/rename.do', {
        dirId: node.id,
        name: node.name
    });
}

function moveDir(evt, treeId, nodes, target, moveType) {
    if (moveType) {
        var treeObj = $.fn.zTree.getZTreeObj(treeId),
            parent = 'inner' == moveType ? target : target.getParentNode(),
            parentId = !parent || parent.virtual ? 0 : parent.id;

        //目标文件夹下的其他文件夹
        var sortedNodes = treeObj.getNodesByFilter(function (node) {
            return $.inArray(node, nodes) < 0 && (parent ? node.parentId == parent.id : node.level == 0);
        }, false, parent);

        if ("inner" == moveType) {
            //追加至队列末尾
            $.each(nodes, function (i, node) {
                sortedNodes.push(node);
            });
        } else {
            //计算插入位置
            var index = $.inArray(target, sortedNodes);
            if ("next" == moveType) {
                index++;
            }
            $.each(nodes, function (i, node) {
                sortedNodes.splice(index + i, 0, node);
            });
        }

        var moveParam = {
            nodeIds: $.map(sortedNodes, function (node) {
                return node.id;
            }),
            parent: parentId
        };

        if (target) {
            //判断是否切换了虚拟目录
            var owner = target.virtual ? target.originId : target.owner;
            for (var i in nodes) {
                var node = nodes[i];
                if (node.owner != owner) {
                    moveParam.owner = owner;
                    break;
                }
            }
        }

        $.post('/dir/move.do', moveParam, function (result) {
            if (!result) {
                treeObj.reAsyncChildNodes(null, 'refresh');
            }
        });
    }
}

function renderCourse() {
    makeCourseDrag();
}

function renderToolbar() {
    //根据不同页面加载操作按钮
    var currentUrl = window.location.href;
    var reg1 = new RegExp('/teacher/course$');
    var reg2 = new RegExp('/class/course$');
    var reg3 = new RegExp('/school/course$');
    var voteReg = new RegExp('/vote/course$');
    var studentReg = new RegExp('/student/course$');
    var myExcellentReg = new RegExp('/emarket');
    var isVote = $('.main-container').is('vote');
    var toolbar = $(".file-toolbar");
    if (reg1.test(currentUrl)) {
        toolbar.html('<li class="fa fa-align-left fa-lg tool-icon" title="编辑" onclick="editLessonByType(getLessonId(this),1)"></li>' +
            '<li class="fa fa-times fa-lg tool-icon" title="删除" onclick="deleteLesson(getLessonId(this))"></li>' +
            '<li class="fa fa-send fa-lg tool-icon" title="' + (isVote ? '参加评比' : '推送到班级') + '" onclick="showCoursePushingDialog(getLessonId(this))"></li>' +
            '<li class="fa fa-share fa-lg tool-icon" title="推送到校本资源" onclick="showSchoolPushingDialog(getLessonId(this))"></li>' +
            '<li class="fa fa-play fa-lg tool-icon" title="播放" onclick="play(getLessonId(this))"></li>'+
            '<li class="fa fa-rocket fa-lg tool-icon" title="推送到电子超市" onclick="showsaleCoursePushingDialog(getLessonId(this),this)"></li>');
        toolbar.each(function(){
        	if($(this).parent('.course-container').attr('isfromcloud') == 'true'){
    			$(this).children('.fa-rocket').addClass('unable');
    		}
        });
    } else if (reg2.test(currentUrl)) {
        toolbar.html('<li class="fa fa-align-left fa-lg tool-icon" title="编辑" onclick="editLessonByType(getLessonId(this),2)"></li>' +
            '<li class="fa fa-times fa-lg tool-icon" title="删除" onclick="deleteLesson(getLessonId(this))"></li>' +
            '<li class="fa fa-play fa-lg tool-icon" title="播放" onclick="play(getLessonId(this))"></li>' +
            '<li class="fa fa-bar-chart fa-lg tool-icon" title="统计" onclick="stat(getLessonId(this))"></li>');
    } else if (reg3.test(currentUrl)) {
        toolbar.html('<li class="fa fa-times fa-lg tool-icon" title="删除" onclick="deleteLesson(getLessonId(this))"></li>' +
            '<li class="fa fa-play fa-lg tool-icon" title="播放" onclick="play(getLessonId(this))"></li>' +
            '<li class="fa fa-cloud-download fa-lg tool-icon" title="推送至备课空间" onclick="showTeacherPushingDialog(getLessonId(this))"></li>');
    } else if (voteReg.test(currentUrl)) {
        toolbar.html('<li class="fa fa-align-left fa-lg tool-icon" title="编辑" onclick="editLesson(getLessonId(this))"></li>' +
            '<li class="fa fa-times fa-lg tool-icon" title="删除"' +
            'onclick="deleteLesson(getLessonId(this))"></li>' +
            '<li class="fa fa-play fa-lg tool-icon" title="播放" onclick="play(getLessonId(this))"></li>');
    } else if (studentReg.test(currentUrl)) {
        toolbar.remove();
    }else if(myExcellentReg.test(currentUrl) && myType == 1){
    	/*toolbar.html('<li class="fa fa-align-left fa-lg tool-icon" title="编辑" onclick="editExcellentLesson(getLessonId(this))"></li>' +
                '<li class="fa fa-times fa-lg tool-icon" title="删除"' +
                'onclick="deleteExcellentLesson(getLessonId(this))"></li>' +
                '<li class="fa fa-play fa-lg tool-icon" title="播放" onclick="play(getLessonId(this))"></li>');*/
    	toolbar.html('<a class="btn btn-primary btn-use" onclick="editExcellentLesson(getLessonId(this))">编辑</a><a class="btn btn-default btn-try" onclick="deleteExcellentLesson(getLessonId(this))">删除</a>');
    }
}

function makeCourseDrag() {
    $('.course-container').draggable({
        handle: '.file-title',
        helper: function (evt) {
            var ele = $(evt.currentTarget).clone().removeAttr('id').addClass('helper');
            return ele;
        },
        appendTo: '.main-container',
        containment: '.main-container',
        opacity: 0.8,
        cursorAt: {
            top: 25,
            left: 25
        }
    });
}

function makeTreeDroppable(treeId) {
    var tree = $.fn.zTree.getZTreeObj(treeId);
    $('#' + treeId + ' a[treenode_a]:not(:has(.virtual_ico_docu,.virtual_ico_open,.virtual_ico_close))').droppable({
        accept: '.course-container',
        activeClass: 'node-hightlight',
        hoverClass: 'tmpTargetNode_inner',
        drop: function (evt, ui) {
            moveLesson(ui.draggable.attr('file-id'), tree.getNodeByTId($(this).closest('li').attr('id')).id);
        }
    });
}

function scrollNode(treeId, node) {
    $('#' + treeId).stop(true, false);
    $('#' + treeId).animate({
        scrollLeft: node.level * 18
    }, 'fast');
}

function cancelScrollNode(treeId) {
    $('#' + treeId).stop(true, false);
    $('#' + treeId).animate({
        scrollLeft: 0
    }, 'fast');
}

function renderTreeNewButton(treeId, node) {
    var aObj = $("#" + node.tId + "_a");
    if (node.id == 'new' || $("#" + node.tId + "_add").length > 0) return;
    var editStr = "<span id='" + node.tId + "_add' class='button add' title='新建文件夹' treenode_add> </span>";
    aObj.append(editStr);
    var btn = $("#" + node.tId + "_add");
    if (btn) btn.bind("click", function () {
        var tree = $.fn.zTree.getZTreeObj(treeId);
        tree.editName(tree.addNodes(node, {
            name: '',
            id: 'new',
            type: 'temp'
        })[0]);
    });
}

function cancelEdit(treeId) {
    $.fn.zTree.getZTreeObj(treeId).cancelEditName();
}

function addDir(evt, treeId, node, cancel) {
    var tree = $.fn.zTree.getZTreeObj(treeId);
    if (cancel || node.name.trim() == '') {
        /**
         * 取消编辑，删除文件夹
         */
        tree.removeNode(node, false);
        return;
    }
    var parentNode = node.getParentNode();
    var updateNode = function (id) {
        if (id) {
            node.id = id.message;
            node.type = null;
            tree.updateNode(node);
        }
    };
    if (parentNode.type == 'classSubject') {
        /**
         * 添加班级根文件夹
         */
        $.post('/directory/addCourseRootDir', {
            classSubjectId: parentNode.id,
            name: node.name
        }, updateNode);
    } else {
        /**
         * 添加普通文件夹
         */
        $.post('/dir/add.do', {
            parentdir: parentNode.id,
            name: node.name

        }, updateNode);
    }
}

function removeTreeNewButton(treeId, node) {
    $('#' + node.tId + '_add').unbind().remove();
}

function moveLesson(lessonId, dirId) {
    $.post('/lesson/move.do', {
        lessonId: lessonId,
        dirId: dirId
    }, function (response) {
        if (response.code != '200') {
            alert('移动出错!');
        } else {
            nav();
        }
    });
}

function loadCourse(url, object) {
    var $courseContainer = $('.course-right-container');
    $courseContainer.data('queryUrl', url);
    $courseContainer.data('queryParams', object);
    $courseContainer.removeData('page');
    $courseContainer.removeData('total');
    $courseContainer.load(url, object, function () {
        if ($courseContainer.data('total')) {
            renderScrollPage();
        }
        renderCourse();
    });
}

function renderScrollPage() {
    var $courseContainer = $('.course-right-container');
    $('.course-list-container').scroll(function () {
        var loading = $courseContainer.data('loading');
        if (!loading) {
            var total = $courseContainer.data('total');
            /*如果到了浏览器 底部*/
            if ($(this).scrollTop() + $(this).height() >= this.scrollHeight) {
                /*如果页面显示超过十个且还有未显示数据*/
                if (total > $(this).find('.course-container').length) {
                    $courseContainer.data('loading', true);
                    appendCourse();
                } else {
                    $(this).unbind('scroll');
                    if ($('#load-all-msg').length < 1) {
                        $('.course-list-container').after("<div id='load-all-msg'>已加载全部</div>");
                        $('#load-all-msg').fadeOut(4000);
                    }
                    $('#load-all-msg').show();
                    $('#load-all-msg').fadeOut(4000);
                }
            }
        }
    });
}

function appendCourse() {
    var $courseContainer = $('.course-right-container');
    var page = $courseContainer.data('page');
    var queryUrl = $courseContainer.data('queryUrl');
    if (!page) {
        page = queryUrl.search(/\.do$/) > 0 ? 0 : 1;
    }
    var queryParams = $courseContainer.data('queryParams');
    $.ajax({
        type: 'post',
        url: queryUrl,
        data: $.extend({page: ++page}, queryParams),
        dataType: 'html',
        success: function (data) {
            $courseContainer.data('page', page);
            /*数据append到页面*/
            var $scrollContainer = $('.course-list-container');
            $scrollContainer.append(data);
            renderCourse();
            $scrollContainer.animate({
                scrollTop: $scrollContainer.scrollTop() + 80
            });
        },
        complete: function () {
            $courseContainer.data('loading', false);
        }
    });
}