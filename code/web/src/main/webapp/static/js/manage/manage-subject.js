/**
 * Created by yan on 2015/4/22.
 */
    // 添加科目
$(function () {
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


    $('.list-add-subject').on('click', function () {
        $.ajax({
            url: '/myschool/gradelist.do',
            type: 'post',
            dataType: 'json',
            data: {},
            success: function (data) {
                var row = data.rows;
                if (row.length > 0) {
                    $('.edit-subject .edit-title-bar').find('div').text('添加学科');
                    $('.edit-subject').find('input').val('');
                    //getSelectRadioGradeList('subject');
                    showAllGrade(row);
                    showEditModal($('.edit-subject'));
                    addSubject();
                    getSelectTeacherList($('.edit-subject-teacher'), function() {
                        $('.edit-subject-teacher').select2("destroy");
                        initSelect($('.edit-subject-teacher'));
                        showEditModal($('.edit-subject'));
                    });
                } else {
                    alert("请先新建年级！");
                }
            },
            error: function () {
                console.log('error');
            }
        });
    });
    $('.subject-list').show();
    // 关闭模态框
    $('.close-modal').on('click', function () {
        hideAlert();
    });
    // 各个列表的修改项
    $('body').on('click', '.list-edit', function () {
        var belong = $(this).parents('.list-container').attr('belong');
        selectEditModal(belong, $(this));
    });
    //添加删除方法
    $('body').on('click', '.list-delete', function () {
        var belong = $(this).parents('.list-container').attr('belong');
        selectDeleteForm(belong, $(this));
    });
    getSubjectList();
});

function getSubjectList() {
    var target = $('.subject-list .list-ul');
    target.empty();
    $.ajax({
        url: '/myschool/sublist.do',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function (data) {
            target.empty();
            var list = data.rows;
            for (var i = 0; i < list.length; i++) {
                var content = '';
                var teacherName = list[i].userName == "" ? '暂无' : list[i].userName;
                content += '<li sid=' + list[i].id + '><div class="list-content"><span>' + list[i].name + '</span><span>学科组长：</span><span class="subject-manager-name" tid="'+list[i].userId+'">' + teacherName + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }

        },
        error: function () {
            console.log('selSchoolSubject error');
        }
    });
}
// 初始化高度
function initHeight() {
    var h = document.documentElement.clientHeight;
    $('.manage-left-container').css('minHeight', h - 160);
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
            $.ajax({
                url: '/myschool/addsub.do',
                type: 'post',
                dataType: 'json',
                data: {
                    subjectName: $.trim($('.edit-subject-name').val()),
                    gradeArray: grList,
                    subjectTeacher:$(".edit-subject-teacher").select2("val")
                },
                success: function (data) {
                    if (data == true) {
                        alert('添加成功！');
                        getSubjectList();
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function () {
                    alert('添加失败！');
                },
                complete: function () {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
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
// 初始化select2
function initSelect(target) {
    target.select2({
        width: '270px',
        containerCss: {
            'margin-left': '10px',
            'font-family': 'sans-serif'
        },
        dropdownCss: {
            'font-size': '14px',
            'font-family': 'sans-serif'
        }
    });
}
function showEditModal(target) {
    $('.modal-bg').fadeIn();
    $('.edit-container').fadeIn();
    $('.edit-info-container').hide();
    target.fadeIn();
}
function getSelectRadioGradeList(blong) {
    var target = $('.edit-subject-selectgrade');
    $.ajax({
        url: '/myschool/gradelist.do',
        async: false,
        type: 'post',
        dataType: 'json',
        data: {},
        success: function (data) {
            var row = data.rows;
            target.empty();
            target.append('<label class="checkbox-inline"><input type="checkbox" class="edit-select-all"> 全部</label>');
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<label class="checkbox-inline"><input type="checkbox" gid=' + row[i].id + '> ' + row[i].name + '</label>';
                target.append(content);
            }
        },
        error: function () {
        }
    });
}
function showAllGrade(row) {
    var target = $('.edit-subject-selectgrade');
    target.empty();
    target.append('<label class="checkbox-inline"><input type="checkbox" class="edit-select-all"> 全部</label>');
    for (var i = 0; i < row.length; i++) {
        var content = '';
        content += '<label class="checkbox-inline"><input type="checkbox" gid=' + row[i].id + '> ' + row[i].name + '</label>';
        target.append(content);
    }
}
function selectDeleteForm(belong, source) {
    switch (belong) {
        case 'grade':
            var gradeId = source.parent().attr('gid');
            deleteGrade(gradeId, source);
            break;
        case 'class':
            var classId = source.parent().attr('cid');
            deleteClassInfo(classId, source);
            break;
        case 'pre-class':
            if (source.parents('.list-ul').hasClass('class-teacher-list')) {
                var teacherid = source.parent().attr('tid');
                deleteClassTeacher(teacherid, source);
            } else {
                var studentid = source.parent().attr('stid');
                deleteClassStudent(studentid, source);
            }
            break;
        case 'subject':
            var sid = source.parent().attr('sid');
            deleteSubject(sid, source);
            break;
        case 'teacher':
            var teacherId = source.parent().attr('tid');
            deleteTeacher(teacherId, source);
            break;
        case 'moral':
            var moralId = source.parent().attr('mid');
            deleteMoral(moralId, source);
            break;
        case 'interest':
            var classId = source.parent().attr('cid');
            deleteInterestClass(classId, source);
            break;
        case 'interest-class':
            var classId = source.parent().attr('cid');
            var tid = source.parent().attr('did');
            var userid = source.parent().attr('tid');
            deleteInterestClassUser(classId, tid, userid, source);
            break;
    }
}
function deleteSubject(sid, target) {
    if (confirm("确定要删除该科目？")) {
        $.ajax({
            url: '/myschool/delsub.do',
            type: 'post',
            dataType: 'json',
            data: {
                subjectId: sid
            },
            success: function (data) {
                if (data.result == true) {
                    alert("删除成功");
                    target.parent().remove();
                } else {
                    var reason="";
                    for(var i=0;i<data.reason.length;i++)
                    {
                        reason+=data.reason[i]+"\r\n"
                    }

                    alert("删除失败,因为该科目已经添加至如下班级，请先在班级内移除该科目:\r\n"+reason);
                }
            },
            error: function () {
                alert("删除失败");
            }
        });
    }
}
function editSubject(sid, target) {
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
            $.ajax({
                url: '/myschool/upsub.do',
                type: 'post',
                dataType: 'json',
                data: {
                    newSubjectName: $.trim($('.edit-subject-name').val()),
                    gradeArray: grList,
                    subjectId: sid,
                    subjectTeacher:$(".edit-subject-teacher").select2("val")
                },
                success: function (data) {
                    if (data == true) {
                        alert('修改成功！');
                        //target.parent().find('span').text($.trim($('.edit-subject-name').val()));
                        getSubjectList();
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function () {
                    alert('修改失败！');
                },
                complete: function () {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}
function getSelectTeacherList(target,callback) {
    target.empty();
    target.append('<option value="0">请选择...</option>');
    $.ajax({
        url: '/myschool/teacherlist.do',
        type: 'post',
        dataType: 'json',
        data: {
            pageSize: 1000
        },
        success: function(data) {
            var row = data.rows;
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<option value=' + row[i].id + '>' + row[i].jobNumber + ' ' + row[i].userName + '</option>';
                target.append(content);
            }
            callback();
        }
    });
}
function selectEditModal(belong, source) {
    switch (belong) {
        case 'subject':
            var sid = source.parent().attr('sid');
            $('.edit-subject .edit-title-bar').find('div').text('编辑学科');
            getSelectRadioGradeList('subject');

            //showEditModal($('.edit-subject'));
            getSelectTeacherList($('.edit-subject-teacher'), function() {
                //getSelectRadioGradeList('subject');
                $('.edit-subject-teacher').select2("destroy");
                initSelect($('.edit-subject-teacher'));
                var clschilds=$('.edit-subject-teacher').children();
                for(var i=0;i<clschilds.length;i++){
                    if(clschilds[i].value==userid){
                        clschilds[i].selected='selected';
                    }
                }

                showEditModal($('.edit-subject'));
            });
            showEditSubject(sid);
            editSubject(sid, source);
            break;
        default:
            break;
    }
}
var userid = "";
function showEditSubject(sid) {
    $.ajax({
        url: '/myschool/findsub.do',
        sync: false,
        type: 'post',
        dataType: 'json',
        data: {
            subjectId: sid
        },
        success: function (data) {
            userid = data.userId;
            var gradeList = data.gradeIds;
            var checkList = $('.edit-subject input:checkbox');
            $('.edit-subject-name').val(data.name);
            $('.edit-subject-teacher').select2('val', data.userId);
            for (var j = 0; j < checkList.length; j++) {
                for(var i=0;i<gradeList.length;i++){
                    var gid=checkList.eq(j).attr('gid');
                    if (gid == gradeList[i]) {
                        checkList.eq(j).prop('checked', true);
                    }
                }

            }

        }
    });
}