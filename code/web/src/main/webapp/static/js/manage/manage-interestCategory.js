/**
 * Created by fl on 2015/11/17.
 */
define(function(require,exports,module) {
    var Common = require('common');
    var commonData;
    var role = $('#role').attr('role');

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

        $('body').on('click', '.ic', function(){
            var icid = $(this).parent().attr("icid");
            var name = $(this).parent().attr("nm");
            sessionStorage.setItem("icid", icid);
            sessionStorage.setItem("name", name);
            window.open("/myschool/interestList.do?role=" + role,"_blank");
        })

        // 学生选课去向
        $('#xsxkqx').on('click', function() {
            window.open("/myschool/stuinteclasscountpage.do","_blank");
        });

        // 开始新的选课
        $('#newterm').on('click', function() {
            var message = "开始新的选课会使已有的选课数据成为历史数据，学生只能选本次选课的课程，确认开始新的选课吗？";
            if(confirm(message) == true){
                Common.getData('/myschool/newterm.do', {}, function(resp){
                    if(resp.code == "200"){
                        alert("开始新的选课成功！");
                    } else{
                        alert("开始新的选课失败！");
                    }
                })
            }
        });

        $('.subject-list').show();
        // 关闭模态框
        $('.close-modal').on('click', function () {
            hideAlert();
        });


        //新建
        $('.list-add-subject').on('click', function () {
                showEditModal($('.edit-subject'));
                addInterestCategory();
        });

        // 编辑回显
        $('body').on('click', '.list-edit', function () {
            var icid = $(this).parent().attr('icid');
            var name = $(this).parent().attr('nm');
            $('.edit-subject-name').val(name);
            showEditModal($('.edit-subject'));
            editInterestCategory(icid);
        });
        //删除
        $('body').on('click', '.list-delete', function () {
            if(confirm("确定删除？") == true){
                var data = {};
                data.interestCategoryId = $(this).parent().attr('icid');
                Common.getData('/interestCategory/delete.do', data, function(resp){
                    if(resp.code == "300"){
                        alert("本类别下已有班级！");
                    }else if(resp.code == "200"){
                        alert("删除成功！");
                    } else{
                        alert("删除失败！");
                    }
                    hideAlert();
                    getInterestCategoryList();
                })
            }
        });
        getInterestCategoryList();


        function getInterestCategoryList() {
            var data = {};
            Common.getData('/interestCategory/list.do', data, function(resp){
                Common.render({
                    tmpl: '#subject_tmpl',
                    data: resp.list,
                    context: '#subject',
                    overwrite: 1
                })
            })
        }


        function addInterestCategory() {
            $('.edit-commit-btn').on('click', function () {
                if ($.trim($('.edit-subject-name').val()) != '') {
                    var data = {};
                    data.name = $('.edit-subject-name').val().trim();
                    Common.getData('/interestCategory/add.do', data, function(resp){
                        if(resp.code = "200"){
                            alert("添加成功！");
                        } else{
                            alert("添加失败！");
                        }
                        hideAlert();
                        getInterestCategoryList();
                    })
                } else {
                    alert('请填写完整！');
                }
            });
        }

        function editInterestCategory(id) {
            $('.edit-commit-btn').on('click', function () {
                if ($.trim($('.edit-subject-name').val()) != '') {
                    var data = {};
                    data.name = $('.edit-subject-name').val().trim();
                    data.interestCategoryId = id;
                    Common.getData('/interestCategory/edit.do', data, function(resp){
                        if(resp.code = "200"){
                            alert("编辑成功！");
                        } else{
                            alert("编辑失败！");
                        }
                        hideAlert();
                        getInterestCategoryList();
                    })
                } else {
                    alert('请填写完整！');
                }
            });
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







