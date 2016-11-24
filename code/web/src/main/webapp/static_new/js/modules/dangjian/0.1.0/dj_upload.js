/**
 * Created by fl on 2016/3/25.
 */
/**
 * Created by fl on 2016/3/23.
 */

define(function(require,exports,module){
    var Common = require('common');
    require('/static_new/js/modules/treeView/0.2.0/jquery.ztree.all-3.5.min.js');
    require('pagination');
    var dangjian = {};
    var type = 7;
    var selectedFolder = null;
    var preSelectedFoder = null;
    var dto = {};
    var srcs = [];
    var directory = '';


    (function(){

        $('#newFolder').click(function(){
            newDirByToolBtn();
        })

        $('input[name=type]').click(function(){
            type = $(this).attr('ty');
            dirType = dirTypes[type];
            ztree();
        })

        $('#savedto').click(function(){
            saveDTO();
        })

        $('body').on('click', '.dangjina_del', function(){
            srcs = [];
            $('#files').empty();
        })



        if($('body').attr('id') != ''){
            showResource($('body').attr('id'));
            $('#file_attach').siblings('em').text('替换文件');
        } else {
            type = sessionStorage.getItem('party_dirType');
            $('input:radio[ty='+type+']').attr('checked', true);
            dirType = dirTypes[type];
            ztree();
        }
        disableDirType();
        fileUpload();
    })()

    function disableDirType(){
        var teacher = $('body').attr('te');
        var partyMember = $('body').attr('pm');
        var partyManger = $('body').attr('pmn');
        if(partyMember == 'false'){
            disableTy(7);
            disableTy(11);
            disableTy(12);
            disableTy(15);
        }
        if(teacher == 'false'){
            disableTy(8);
            disableTy(9);
            disableTy(10);
            disableTy(13);
            disableTy(14);
            disableTy(16);
        }
    }

    function disableTy(ty){
        $('input:radio[ty='+ty+']').prop("disabled", true);
        $('input:radio[ty='+ty+']').next().addClass('disable');
    }

    function showResource(id){
        dto.id = id;
        Common.getData('/party/resources/'+id+'.do', {}, function(resp){
            if(resp.code == '500'){
                alert(resp.msg);
            } else {
                showDetail(resp.dto);
            }
        })
    }

    function showDetail(dto){
        $('#name').val(dto.name);
        $('input:radio[ty='+dto.dirType+']').attr('checked', true);
        dirType = dirTypes[dto.dirType];
        ztree();

        directory = dto.directory;
        srcs = dto.srcs;
        for(var i in srcs){
            var src = srcs[i];
            showFile({fileName: src.name, path: src.value});
        }
    }

    function ztreeReady(){
        var zTree = getDirTree();
        var nodes = zTree.transformToArray(zTree.getNodes());
        if (nodes.length>0) {
            for(var i=0;i<nodes.length;i++){
                if(nodes[i].id == directory){
                    nodes[i].checked = true;
                    zTree.selectNode(nodes[i]);
                    selectedFolder = directory;
                }
            }
        }
    }

    function newDirByToolBtn() {
        if (selectedFolder) {
            newDir(selectedFolder);
        } else {
            newRootDir();
        }
    }

    function newRootDir() {
        var name = prompt('请输入文件夹名');
        if (name) {
            Common.getPostData('/dir/party/add.do', {
                name: name,
                type: type
            }, function(resp){
                ztree();
            })
        }
    }

    function newDir(parentId){
        var name = prompt('请输入文件夹名');
        if (name) {
            Common.getPostData('/dir/party/add.do', {
                parentdir: parentId,
                name: name,
                type: type
            }, function(resp){
                ztree();
            })
        }
    }

    function ztree(){
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
                        DeleteBtnAllowed();
                    }
                },
                beforeRename: beforeRename,
                onRename: function(evt, treeId, node, isCancel){
                    onRename(evt, treeId, node, isCancel);
                },
                onDrop: moveDir,
                onAsyncSuccess: function (evt, treeId) {
                    asyncTreeCallback.onAsyncSuccess();
                    ztreeReady();
                },
                onExpand: function (evt, treeId) {
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

    function fileUpload(){
        $('#file_attach').fileupload({
            url: '/commonupload/doc/upload.do?useName=1',
            add: function (e, data) {
                var fileName = data.originalFiles[0].name;
                if(fileName.indexOf('.ppt') >=0 || fileName.indexOf('.doc') >=0 || fileName.indexOf('.xls') >=0){
                    data.submit();
                } else {
                    alert('请选择正确的文件格式上传');
                }
            },
            start: function(e) {
                $('#fileuploadLoading').show();
                MessageBox('上传中', 0);
            },
            done: function(e, data) {
                console.info(data);
                var resp = data.result.message[0];
                //srcs.push({id:resp.id, name:resp.name, value:resp.path})
                srcs[0]={id:resp.id, name:resp.fileName, value:resp.path};
                showFile(resp);
                $('#file_attach').siblings('em').text('替换文件');
            },
            fail: function (e, data) {

            },
            always: function (e, data) {
                $('#fileuploadLoading').hide();
                MessageBox('上传结束', 1);
            }
        });
    }

    function showFile(data){

        var html = '<div class="file">';
        html += '<a href="'+data.path+'" class="courselink">';

        if(data.fileName.indexOf('.doc') >= 0){
            html += '<img class="dangjian_pic" src="/img/docicon/doc.png"></a>';
        } else if(data.fileName.indexOf('.ppt') >= 0){
            html += '<img class="dangjian_pic" src="/img/docicon/ppt.png"></a>';
        } else if(data.fileName.indexOf('.xls') >= 0){
            html += '<img class="dangjian_pic" src="/img/docicon/xls.png"></a>';
        } else {
            html += '<img class="dangjian_pic" src="/img/docicon/unknow.png"></a>';
        }
        html += '<img class="dangjina_del" src="/img/dangjianup_del.png">';
        html += '<p title="'+data.fileName+'">'+data.fileName+'</p>';
        html += '</div>';

        $('#files').empty();
        $('#files').append(html);
    }

    function saveDTO(){
        var id = $('body').attr('id');
        var url;
        var type;
        if(id != ''){
            url = '/party/resources/' + id + '.do?';
            type = 'put';
        } else {
            url = '/party/resources.do?';
            type = 'post';
        }

        var name = $.trim($('#name').val());
        var directory = selectedFolder;

        //var name = 'ss';
        //directory = '56f357112947cd1a81589a82';
        //srcs[0] = {id:"56f4b16729473aa2785f8074", value:"http://7xiclj.com1.z0.glb.clouddn.com/56f4b16729473aa2785f8074.doc"};

        if(checkData(name, directory)){
            dto.name = name;
            dto.directory = directory;
            dto.srcs = srcs;
            dto.state = 0;
            //dto = {name:name, directory: directory, srcs:srcs, state:0};

            $.ajax({
                url: url,
                type: type,
                data: JSON.stringify(dto),
                contentType: 'application/json',
                success: function (resp) {
                    alert(resp.message);
                    if(resp.code == '200'){
                        window.opener.dangjian.refresh();
                        window.close();
                    }
                },
                error: function(e) {
                    alert('操作失败');
                }
            });
        }

    }


    function checkData(name, directory){
        var warning = '';
        var flag = true;
        if(name == ''){
            warning += '资料名称不能为空   ';
            flag = false;
        }
        if(directory == null){
            warning += '请先选择文件夹   ';
            flag = false;
        }
        if(srcs.length <= 0){
            warning += '您还没有上传文件';
            flag = false;
        }
        if(!flag){
            alert(warning);
        }
        return flag;
    }

    module.exports = dangjian;
});

