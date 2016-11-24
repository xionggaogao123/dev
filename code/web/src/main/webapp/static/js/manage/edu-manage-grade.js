/**
 * Created by fl on 2015/11/09.
 */
// 添加科目
define(function(require,exports,module){
    var Common = require('common');
    var eduId;

    (function () {
        initHeight();
        getGradeList();
        //getSchoolInfo();
        //添加年级
        $('.list-add-grade').on('click', function() {
            $('.edit-grade .edit-title-bar').find('div').text('添加年级');
            $('.edit-grade').find('input').val('');

            $('.edit-grade-value').empty();
            var content = '';
            for (var i = 1; i <= 12; i++) {
                content += '<option value=' +i+ '>' + i + '年级</option>';
            }
            $('.edit-grade-value').append(content);
            showEditModal($('.edit-grade'));
            addGradeInfo();
        });

        $('.grade-list').show();


        // 关闭模态框
        $('.close-modal').on('click', function () {
            hideAlert();
        });
        // 编辑
        $('body').on('click', '.list-edit', function() {
            var type = $(this).parent().attr('ty');
            var gradeName = $(this).parent().attr('nm');
            var gradeId = $(this).parent().attr('gid');
            $('.edit-grade-name').val(gradeName);
            getSelectGradeList(type);
            showEditModal($('.edit-grade'));
            editGrade(gradeId);
        });
        // 删除
        $('body').on('click', '.list-delete', function() {
            if(confirm("确定删除？") == true){
                var data = {};
                data.eduId = eduId;
                data.gradeId = $(this).parent().attr('gid');
                Common.getData('/education/delGrade.do', data, function(resp){
                    if(resp.code = "200"){
                        alert("删除成功！");
                    } else{
                        alert("删除失败！");
                    }
                    hideAlert();
                    getGradeList();
                })
            }

        });

        function getGradeList() {
            var requireData = {};
            Common.getData('/education/getGradeList.do', requireData, function(resp){
                eduId = resp.eduId;
                Common.render({
                    tmpl: '#grade_tmpl',
                    data: resp.gradeList,
                    context: '#grade',
                    overwrite: 1
                });
            })
        }

        function addGradeInfo() {
            $('.edit-commit-btn').on('click', function() {
                if ($.trim($('.edit-grade-name').val()) != '') {
                    var data = {};
                    data.eduId = eduId;
                    data.gradeName = $('.edit-grade-name').val();
                    data.gradeType = $('.edit-grade-value').val();
                    Common.getData('/education/addGrade.do', data, function(resp){
                        if(resp.code = "200"){
                            alert("添加成功！");
                        } else{
                            alert("添加失败！");
                        }
                        hideAlert();
                        getGradeList();
                    })
                } else {
                    alert('请输入年级名称！');
                }
            });
        }

        function editGrade(gradeId) {
            $('.edit-commit-btn').on('click', function() {
                if ($.trim($('.edit-grade-name').val()) != '') {
                    var data = {};
                    data.eduId = eduId;
                    data.gradeName = $('.edit-grade-name').val();
                    data.gradeType = $('.edit-grade-value').val();
                    data.gradeId = gradeId;
                    Common.getData('/education/editGrade.do', data, function(resp){
                        if(resp.code = "200"){
                            alert("编辑成功！");
                        } else{
                            alert("编辑失败！");
                        }
                        hideAlert();
                        getGradeList();
                    })
                } else {
                    alert('请输入年级名称！');
                }
            });
        }

    })()
});


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
function resetEditContainer() {
    $('.edit-info-container').hide();
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

function makeOption(level,i){
    var content;
    if(level==i){
        content += '<option selected ="selected"  value=' +i+ '>' + i + '年级</option>';
    }else{
        content += '<option value=' +i+ '>' + i + '年级</option>';
    }
    return content;
}
function getSelectGradeList(level) {
    $('.edit-grade-value').empty();
    var content = '';
    for (var i = 1; i <= 12; i++) {
        content+=makeOption(level,i);
    }
    $('.edit-grade-value').append(content);
}

