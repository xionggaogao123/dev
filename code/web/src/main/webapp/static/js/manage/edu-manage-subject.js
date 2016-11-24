/**
 * Created by fl on 2015/11/10.
 */
    // 添加科目
define(function(require,exports,module) {
    var Common = require('common');
    var eduId;
    var flag;
    var commonData;

    (function () {
        initHeight();

        // checkbox全选/反选
        $('body').on('change', '.edit-select-all', function() {
            if ($(this).is(':checked')) {
                $('.edit-subject-selectgrade input:checkbox').prop('checked', true);
                $('.edit-subject-selectgrade-interest input:checkbox').prop('checked', true);
            } else {
                $('.edit-subject-selectgrade input:checkbox').prop('checked', false);
                $('.edit-subject-selectgrade-interest input:checkbox').prop('checked', false);
            }
        });

        $('body').on('change', '.edit-subject-selectgrade input:checkbox', function() {
            if (!$(this).is(':checked')) {
                $('.edit-select-all').prop('checked', false);
            }
        });

        $('.subject-list').show();
        // 关闭模态框
        $('.close-modal').on('click', function () {
            hideAlert();
        });


        $('.list-add-subject').on('click', function () {
            if(flag == true){
                showEditModal($('.edit-subject'));
                addSubject();
            } else {
                alert("请先新建年级！");
            }
        });

        // 编辑回显
        $('body').on('click', '.list-edit', function () {
            var sid = $(this).parent().attr('sid');
            var subjectName = $(this).parent().attr('nm');
            var type = $(this).parent().attr('ty');
            $('.edit-subject .edit-title-bar').find('div').text('编辑学科');
            $('.edit-subject-name').val(subjectName);
            showEditSubject(sid, type);
            showEditModal($('.edit-subject'));
            editSubject(sid);
        });
        //删除
        $('body').on('click', '.list-delete', function () {
            if(confirm("确定删除？") == true){
                var data = {};
                data.eduId = eduId;
                data.subjectId = $(this).parent().attr('sid');
                Common.getData('/education/delSubject.do', data, function(resp){
                    if(resp.code = "200"){
                        alert("删除成功！");
                    } else{
                        alert("删除失败！");
                    }
                    hideAlert();
                    getSubjectList();
                })
            }
        });
        getSubjectList();
        getGradeList();


        function getSubjectList() {
            var data = {};
            Common.getData('/education/getSubjectList.do', data, function(resp){
                eduId = resp.eduId;
                commonData = resp.subjectList;
                Common.render({
                    tmpl: '#subject_tmpl',
                    data: resp.subjectList,
                    context: '#subject',
                    overwrite: 1
                })
            })
        }

        function getGradeList(){
            var data = {};
            Common.getData('/education/getGradeList.do', data, function(resp){
                var row = resp.gradeList;
                if(row.length>0){
                    $('.edit-subject .edit-title-bar').find('div').text('添加学科');
                    $('.edit-subject').find('input').val('');
                    showAllGrade(row);
                    flag = true;
                } else {
                    flag = false;
                }
            })
        }

        function addSubject() {
            $('.edit-commit-btn').on('click', function () {
                var checkList = $('.edit-subject input:checkbox');
                var grList = '';
                checkList.each(function () {
                    if ($(this).is(':checked')) {
                        if ($(this).attr('gid')) {
                            grList += $(this).attr('gid') + ',';
                        }
                    }
                });
                if ($.trim($('.edit-subject-name').val()) != '' && grList != '') {
                    var data = {};
                    data.eduId = eduId;
                    data.subjectName = $('.edit-subject-name').val().trim();
                    data.subjectType = $('.edit-subject-value').val();
                    data.gradeList = grList;
                    Common.getData('/education/addSubject.do', data, function(resp){
                        if(resp.code = "200"){
                            alert("添加成功！");
                        } else{
                            alert("添加失败！");
                        }
                        hideAlert();
                        getSubjectList();
                    })
                } else {
                    alert('请填写完整！');
                }
            });
        }

        function editSubject(sid) {
            $('.edit-commit-btn').on('click', function () {
                var checkList = $('.edit-subject input:checkbox');
                var grList = '';
                checkList.each(function () {
                    if ($(this).is(':checked')) {
                        if ($(this).attr('gid')) {
                            grList += $(this).attr('gid') + ',';
                        }
                    }
                });
                if ($.trim($('.edit-subject-name').val()) != '' && grList != '') {
                    var data = {};
                    data.eduId = eduId;
                    data.subjectName = $('.edit-subject-name').val().trim();
                    data.subjectType = $('.edit-subject-value').val();
                    data.gradeList = grList;
                    data.subjectId = sid;
                    Common.getData('/education/editSubject.do', data, function(resp){
                        if(resp.code = "200"){
                            alert("编辑成功！");
                        } else{
                            alert("编辑失败！");
                        }
                        hideAlert();
                        getSubjectList();
                    })
                } else {
                    alert('请填写完整！');
                }
            });
        }

        function showEditSubject(sid, type) {
            $('.edit-subject-value').find('option').each(function(index, item){
                if($(item).val() == type){
                    $(item).prop('selected', true);
                }
            })
            var gradeList;
            for(var i=0; i<commonData.length; i++){
                if(commonData[i].subjectId == sid){
                    gradeList = commonData[i].gradeList;
                }
            }
            var checkList = $('.edit-subject input:checkbox');
            for (var j = 0; j < checkList.length; j++) {
                checkList.eq(j).prop('checked', false);
            }
            for (var j = 0; j < checkList.length; j++) {
                for(var i=0;i<gradeList.length;i++){
                    var gid=checkList.eq(j).attr('gid');
                    if (gid == gradeList[i]) {
                        checkList.eq(j).prop('checked', true);
                    }
                }

            }

        }


    })()
})



// 初始化高度
function initHeight() {
    var h = document.documentElement.clientHeight;
    $('.manage-left-container').css('minHeight', h - 160);
}


function showAlert() {
    $('.modal-bg').fadeIn();
    $('.edit-container').fadeIn();
    resetEditContainer();
}

function hideAlert() {
    $('.modal-bg').fadeOut();
    $('.edit-container').fadeOut();
    $('.edit-commit-btn').unbind();
    $('.select-teacher-list').empty().hide();
}

function showEditModal(target) {
    $('.modal-bg').fadeIn();
    $('.edit-container').fadeIn();
    $('.edit-info-container').hide();
    target.fadeIn();
}

function showAllGrade(row) {
    var target = $('.edit-subject-selectgrade');
    target.empty();
    target.append('<label class="checkbox-inline"><input type="checkbox" class="edit-select-all"> 全部</label>');
    for (var i = 0; i < row.length; i++) {
        var content = '';
        content += '<label class="checkbox-inline"><input type="checkbox" gid=' + row[i].gradeId + '> ' + row[i].gradeName + '</label>';
        target.append(content);
    }
}




