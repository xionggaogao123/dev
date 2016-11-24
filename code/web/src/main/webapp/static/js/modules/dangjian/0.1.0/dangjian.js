/**
 * Created by fl on 2016/3/23.
 */

define(function (require, exports, module) {
    var Common = require('common');
    require('/static/js/modules/treeView/0.2.0/jquery.ztree.all-3.5.min.js');
    require('pagination');
    var dangjian = {};
    var type = 7;
    var selectedFolder = null;
    var preSelectedFoder = null;
    var fileEdit = false;//是否在编辑状态
    var upload = true;//是否有权限上传


    (function () {

        ztree();
        getResources(1);
        sessionStorage.setItem("party_dirType", type);

        $('#upload').click(function () {
            if (upload) {
                window.open('/party/upload.do', 'target');
            } else {
                alert('对不起，在本模块下你没有权限上传！');
            }
        })

        $('#newFolder').click(function () {
            newDirByToolBtn();
        })

        $('#deleteFolder').click(function () {
            deleteDirByToolBtn();
        })

        $('.dangjian_nav li').click(function () {
            $(this).siblings().each(function (index, ele) {
                $(ele).find('a').removeClass('focus');
            })
            $(this).find('a').addClass('focus');
            type = $(this).attr('type')
            dirType = dirTypes[type];
            clearupSelectedFolder();
            ztree();
            getResources(1);
            sessionStorage.setItem("party_dirType", type);
        })

        $('body').on('click', '.dangjian_file_del', function () {
            var msg = '确认删除';
            var id = $(this).parent().attr('id');
            if (confirm(msg)) {
                deleteFile(id);
            }
        })

        $('body').on('click', '.dangjian_file_edit', function () {
            var id = $(this).parent().attr('id');
            window.open('/party/edit/' + id + '.do');
        })

        $('#manage').click(function () {
            fileEdit = !fileEdit;
            if (fileEdit) {
                $('#manage').text('关闭资料管理');
            } else {
                $('#manage').text('开启资料管理');
            }
            checkFileEdit();
        })

        $('body').on('click', '.dangjiang_file_img', function () {
            if (!fileEdit) {
                var parent = $(this).parents('.dangjiang_file');
                //var src = '/party/resources/' + parent.attr('id') + '/' + parent.attr('fid') + '.do';
                var src = getUrl(parent.attr('src'));
                window.open(src);
            }
        })

        $('#term').change(function () {
            getResources(1);
        })


    })()

    function getUrl(strFileUrl) {
        //在线预览服务地址
        var strOfficeApps = "http://112.124.106.222";

        var strUrl = strOfficeApps + "/op/view.aspx?src=" + encodeURIComponent(strFileUrl);
        return strUrl;
    }

    function checkFileEdit() {
        if (fileEdit) {
            $('.dangjiang_file').each(function (index, ele) {
                var mine = $(ele).attr('mine');
                if (1 == mine && fileEdit) {
                    $(this).find('button').show();
                }
            })
        } else {
            $('.dangjiang_file').each(function (index, ele) {
                var mine = $(ele).attr('mine');
                if (1 == mine) {
                    $(this).find('button').hide();
                }
            })
        }
    }

    function deleteFile(id) {
        var url = '/party/resources/' + id + '.do';
        $.ajax({
            type: "DELETE",
            data: {},
            url: url,
            async: false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (resp) {
                alert(resp.message);
                getResources(1);
            }
        });
    }

    function fileHover() {
        //$('.dangjiang_file').hover(
        //    function() {
        //        var mine = $(this).attr('mine');
        //        if(1 == mine && fileEdit){
        //            $(this).find('button').show();
        //        }
        //    },
        //    function() {
        //        $(this).find('button').hide();
        //    }
        //)
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

    function deleteDir(id, callback) {
        if (confirm('确认删除文件夹及其子文件夹和文件?')) {
            $.post('/dir/party/remove.do', {
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
                    } else {
                        navRoot();
                        reloadDirTree();
                    }
                    getResources(1);
                } else {
                    alert('不能删除该文件夹');
                }
            });
            return true;
        }
    }

    function reloadDirTree() {
        var tree = getDirTree();
        tree.reAsyncChildNodes(null, 'refresh');
    }

    function getParentDirNode(id) {
        var tree = getDirTree();
        return tree.getNodeByParam('id', id).getParentNode();
    }

    function navRoot() {
        loadCourse('/lesson/load/list.do', {
            type: dirType,
            pageSize: 16
        });
    }

    function newRootDir() {
        var name = prompt('请输入文件夹名');
        if (name) {
            Common.getPostData('/dir/party/add.do', {
                name: name,
                type: type
            }, function (resp) {
                ztree();
            })
        }
    }

    function newDir(parentId) {
        var name = prompt('请输入文件夹名');
        if (name) {
            Common.getPostData('/dir/party/add.do', {
                parentdir: parentId,
                name: name,
                type: type
            }, function (resp) {
                ztree();
            })
        }
    }

    function ztree() {
        $.fn.zTree.init($("#DirUl"), {
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
                        getResources(1);
                        DeleteBtnAllowed();
                    }
                },
                beforeRename: beforeRename,
                onRename: function (evt, treeId, node, isCancel) {
                    onRename(evt, treeId, node, isCancel);
                },
                onDrop: moveDir,
                onAsyncSuccess: function (evt, treeId) {
                    asyncTreeCallback.onAsyncSuccess();
                    //makeTreeDroppable(treeId);
                },
                onExpand: function (evt, treeId) {
                    //makeTreeDroppable(treeId);
                }
            })
        });
    }


    function getSelectedFolderId(id) {
        selectedFolder = id;
    }

    function getDirTree() {
        return $.fn.zTree.getZTreeObj("DirUl");
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

    dangjian.refresh = function () {
        ztree();
        getResources(1);
        fileEdit = false;
    }

    function getResources(page) {
        var requestData = {};
        if (selectedFolder) {
            requestData.directoryId = selectedFolder;
        }
        requestData.dirType = dirType;
        requestData.term = $('#term').val();
        requestData.page = page;
        requestData.pageSize = 9;
        Common.getData('/party/resources.do', requestData, function (resp) {
            if (resp.flag) {
                upload = true;
                if (resp.dtos.length > 0) {
                    $('#warning').hide();
                } else {
                    $('#warning').text('暂无文件').show();
                }
            } else {
                $('#warning').text('对不起，您没有权限查看，请联系管理员！').show();
                upload = false;
            }
            fileHover();
            jqPaginator(resp);


            $('#filecount').text(resp.count);

            Common.render({
                tmpl: '#dtosTmpl',
                data: resp.dtos,
                context: '#dtos',
                overwrite: 1
            });

            checkFileEdit();

        })
    }

    function jqPaginator(data) {
        var isInit = true;
        $('.new-page-links').html("");
        if (data.dtos.length > 0) {
            $('.new-page-links').jqPaginator({
                totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                visiblePages: 10,//分多少页
                currentPage: parseInt(data.page),//当前页数
                first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                onPageChange: function (n) { //回调函数
                    if (isInit) {
                        isInit = false;
                    } else {
                        getResources(n);
                    }
                }
            });
        }
    }


    module.exports = dangjian;
});
