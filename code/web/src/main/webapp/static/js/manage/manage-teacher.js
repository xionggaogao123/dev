/**
 * Created by yan on 2015/4/22.
 */
    // 添加科目
$(function () {
    initHeight();
    // 添加老师
    $('.list-add-teacher').on('click', function () {
        $('.edit-teacher .edit-title-bar').find('div').text('添加老师');
        $('.edit-teacher').find('input:text').val('');
        $('.edit-teacher-username').attr('disabled', false);
        $("#ismanage").attr("checked", false);
        var target4 = $('.user-techuser');
        target4.empty();
        showEditModal($('.edit-teacher'));
        addTeacher();
    });

    $('.teacher-list').show();
    // 关闭模态框
    $('.close-modal').on('click', function () {
        hideAlert();
    });
    // 各个列表的修改项
    $('body').on('click', '.list-edit', function () {
        var belong = $(this).parents('.list-container').attr('belong');
        selectEditModal(belong, $(this));
    });
    // 各个列表的删除项
    $('body').off('click', '.list-delete');
    $('body').on('click', '.list-delete', function () {
        var belong = $(this).parents('.list-container').attr('belong');
        selectDeleteForm(belong, $(this));
    });
    // 修改默认密码
    $('.title-resetpassword').on('click', function () {
        showAlert();
        $('.edit-password').show();
        setPassword();
    });
    // 检查老师用户名重复
    $('.edit-teacher-name').keyup(function () {
        var target = $(this);
        var target4 = $('.user-techuser');
        if ($.trim(target.val()) != '') {
            $.ajax({
                url: '/myschool/checkUserName.do',
                type: 'post',
                dataType: 'json',
                data: {
                    userName: $.trim(target.val())
                },
                success: function (data) {
                    if ($('.edit-teacher .edit-title-bar').find('div').text() == "添加老师"||
                        $('.edit-teacher .edit-title-bar').find('div').text() == "编辑老师") {
                        if (data.resultCode == 1) {
                            target.next().remove();
                            /*target.after('<span class="edit-error">用户名已存在！</span>');*/
                        } else {
                            $('.edit-error').remove();
                        }
                        //target.next().remove();
                        target4.empty();
                        var content = '';
                        content += '<span style="padding-bottom: 5px;">生成用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 100px;" id="teachername">' + data.userName + '</span>';
                        content += '<span>密码&nbsp;&nbsp;' + data.pwd + '</span>';
                        target4.append(content);

                    }
                },
                error: function () {
                }
            });
        }
    });

    getTeacherList();
});

function showmange(option) {
    if (option=="老师") {
        $('.ismanageclass').show();
    } else {
        $('.ismanageclass').hide();
    }
}

// 初始化高度
function initHeight() {
    var h = document.documentElement.clientHeight;
    $('.manage-left-container').css('minHeight', h - 160);
}
function getTeacherList(page) {
    var tpage = page || 1;
    $.ajax({
        url: '/myschool/teacherlist.do',
        type: 'post',
        dataType: 'json',
        data: {
            page: tpage,
            keyWord: $('#teacher-name-search').val()
        },
        success: function (data) {
            var row = data.rows;
            teacherList = row;
            var target = $('.teacher-list .list-ul');
            var option = {
                total: data.total,
                pagesize: data.pageSize,
                currentpage: data.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).attr('onclick', 'getTeacherList(' + $(this).text() + ');')
                    });
                    $('.first-page').attr('onclick', 'getTeacherList(1);');
                    $('.last-page').attr('onclick', 'getTeacherList(' + totalPage + ');');
                }
            }
            target.empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                var jobNumber = row[i].jobNumber == null ? "" : row[i].jobNumber;
                content += '<li tid=' + row[i].id + '><div class="list-content"><span>' + jobNumber + ' ' + row[i].userName + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }
            initPaginator(option);
        },
        error: function () {
            console.log('selSchoolAllTeacher error');
        }
    });
}
// 分页初始化
function initPaginator(option) {
    var totalPage = '';
    $('.page-paginator').show();
    $('.page-index').empty();
    if (option.total % option.pagesize == 0) {
        totalPage = option.total / option.pagesize;
    } else {
        totalPage = parseInt(option.total / option.pagesize) + 1;
    }
    buildPaginator(totalPage, option.currentpage);
    option.operate(totalPage);
}
function buildPaginator(totalPage, currentPage) {
    if (totalPage > 5) {
        if (currentPage < 4) {
            for (var i = 1; i <= 5; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
            $('.page-index').append('<i>···</i>');
        } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
            $('.page-index').append('<i>···</i>');
            for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
            $('.page-index').append('<i>···</i>');
        } else {
            $('.page-index').append('<i>···</i>');
            for (var i = totalPage - 4; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
        }
    } else {
        for (var i = 1; i <= totalPage; i++) {
            if (i == currentPage) {
                $('.page-index').append('<span class="active">' + i + '</span>');
            } else {
                $('.page-index').append('<span>' + i + '</span>');
            }
        }
    }
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
function selectDeleteForm(belong, source) {
    switch (belong) {
        case 'teacher':
            var teacherId = source.parent().attr('tid');
            deleteTeacher(teacherId, source);
            break;

    }
}
function selectEditModal(belong, source) {
    switch (belong) {
        case 'teacher':
            var teacherId = source.parent().attr('tid');
            $('.edit-teacher .edit-title-bar').find('div').text('编辑老师');
            $('.edit-teacher').find('input').val('');
            $('.ismanageclass').hide();
            $("#ismanage").attr("checked", false);
            showEditTeacher(teacherId);
            $('.reset-password-form').show();
            $('.reset-password-form img').hide();
            showEditModal($('.edit-teacher'));
            editTeacher(teacherId, source);
            break;
            break;
    }
}
function addTeacher() {
    $('.edit-commit-btn').off('click');
    $('.edit-commit-btn').on('click', function () {
        if ($.trim($('.edit-teacher-number').val()) == '') {
            $('.edit-teacher-number').next().remove();
            $('.edit-teacher-number').after('<span class="edit-error">请输入教职工号!</span>');
        } else {
            $('.edit-teacher-number').next().remove();
        }
        if ($.trim($('.edit-teacher-name').val()) == '') {
            $('.edit-teacher-name').next().remove();
            $('.edit-teacher-name').after('<span class="edit-error">请输入老师姓名!</span>');
        } else {
            $('.edit-teacher-name').next().remove();
        }
        if ($('.edit-teacher').find('.edit-error').length == 0) {
            var ismanage = 0;
            if (document.getElementById("ismanage").checked) {
                ismanage = 1;
            } else {
                ismanage = 0;
            }
            $.ajax({
                url: '/myschool/addteacher.do',
                type: 'post',
                dataType: 'json',
                data: {
                    teacherName: $.trim($('#teachername').text()),
                    jobNum: $.trim($('.edit-teacher-number').val()),
                    //teacherName: $.trim($('.edit-teacher-name').val()),
                    permission: $('.edit-teacher-permission').val(),
                    ismanage: ismanage
                },
                success: function (data) {
                    if (data == true) {
                        alert('添加成功！');
                        getTeacherList();
                    } else {
                        alert('添加失败');
                    }
                },
                error: function () {
                    console.log('addTeacyherInfo error');
                    alert('添加失败');
                },
                complete: function () {
                    hideAlert();
                }
            });
        }
    });
}
function deleteTeacher(teacherId, target) {
    if (confirm("确定要删除该老师吗？")) {
        $.ajax({
            url: '/myschool/delteach.do',
            type: 'post',
            dataType: 'json',
            data: {
                teacherId: teacherId
            },
            success: function (data) {
                if (data == true) {
                    alert('删除成功');
                    target.parent().remove();
                    getTeacherList();
                } else {
                    alert('删除失败');
                }
            },
            error: function () {
                alert('删除失败');
            }
        });
    }
}
function showEditTeacher(tid) {
    var target4 = $('.user-techuser');
    target4.empty();
    $.ajax({
        url: '/myschool/findteacher.do',
        type: 'post',
        dataType: 'json',
        data: {
            teacherId: tid
        },
        success: function (data) {
            if (data != null) {
                $('.edit-teacher-username').attr('disabled', 'disabled');
                $('.edit-teacher-username').val(data.userName);
                $('.edit-teacher-number').val(data.jobNumber);
                $('.edit-teacher-name').val(data.userName);
                if (isHeadMaster(data.role)) {
                    $('.edit-teacher-permission').val(8);
                } else if (isAdmin(data.role) && isTeacher(data.role)) {
                    $('.edit-teacher-permission').val(2);
                } else if (isAdmin(data.role)) {
                    $('.edit-teacher-permission').val(64);
                } else if (isTeacher(data.role)) {
                    $('.edit-teacher-permission').val(2);
                }
                $('.reset-teacher-password').attr('onclick', 'resetPsw(\'' + data.id + '\',2)');
                if (isTeacher(data.role)&&!isHeadMaster(data.role)) {
                    $('.ismanageclass').show();
                    if (isAdmin(data.role)) {
                        document.getElementById("ismanage").checked = true;
                    }
                }
                var content = '';
                content += '<span style="padding-bottom: 5px;">生成用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 150px;" id="teachername">' + data.userName + '</span>';
                target4.append(content);

            }
        },
        error: function () {
            console.log('selSingleTeacher error');
        }
    });
}
function editTeacher(teacherId, target) {
    $('.edit-commit-btn').off('click');
    $('.edit-commit-btn').on('click', function () {
        if ($.trim($('.edit-teacher-name').val()) == '') {
            $('.edit-teacher-name').next().remove();
            $('.edit-teacher-name').after('<span class="edit-error">请输入老师姓名!</span>');
        } else {
            $('.edit-teacher-name').next().remove();
        }
        if ($('.edit-teacher').find('.edit-error').length == 0) {
            var ismanage = 0;
            if (document.getElementById("ismanage").checked) {
                ismanage = 1;
            } else {
                ismanage = 0;
            }
            $.ajax({
                url: '/myschool/upteach.do',
                type: 'post',
                dataType: 'json',
                data: {
                    number: $('.edit-teacher-number').val(),
                    teacherName: $.trim($('#teachername').text()),
                    //teacherName: $('.edit-teacher-name').val(),
                    permission: $('.edit-teacher-permission').val(),
                    teacherId: teacherId,
                    jobNum: $.trim($('.edit-teacher-number').val()),
                    isManage: ismanage
                },
                success: function (data) {
                    if (data == true) {
                        //var content = $('.edit-teacher-number').val() + ' ' + $('.edit-teacher-name').val();
                        var content = $('.edit-teacher-number').val() + ' ' +$.trim($('#teachername').text());
                        target.parent().find('span').text(content);
                        alert('修改成功！');
                    } else {
                        alert('修改失败');
                    }
                },
                error: function () {
                    alert('修改失败');
                },
                complete: function () {
                    hideAlert();
                }
            });
        }
    });
}


function resetPsw(id, type) {
    if (confirm('确定要重置密码吗？')) {
        $.ajax({
            url: '/myschool/initpwd.do',
            type: 'post',
            dataType: 'json',
            data: {
                userId: id,
                type:type
            },
            success: function (data) {
                if (data == true) {
                    if (type == 1) {
                        $('.reset-password-form img').eq(0).show();
                    } else if (type == 2) {
                        $('.reset-password-form img').eq(2).show();
                    } else {
                        $('.reset-password-form img').eq(1).show();
                    }
                } else {
                    alert('重置失败');
                }
            },
            error: function () {
                alert('重置失败');
            }
        });
    }
}

