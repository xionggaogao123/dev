/**
 * Created by yan on 2015/4/22.
 */

$(function () {

    initHeight();
    getSchoolInfo();

    var cid = $('#content_main').attr('classId');
    var className=decodeURI(getUrlParam("className"));
    var gradeName=decodeURI(getUrlParam("gradeName"));

    $('.class-add-teacher,.class-add-student').attr('cid', cid);
    $('.list-container').hide();
    $('.pre-class-list .list-title-class').text(className);
    $('.list-title-grade-pre').text(gradeName);
    getClassUserList(cid);
    $('.pre-class-list').show();

    // 批量导入人员
    $('.batch-export').on('click', function () {
        window.location.href='/user/mamager/batchExportPersonPage.do';
    });

    // 关闭模态框
    $('.close-modal').on('click', function () {
        hideAlert();
    });
    // 各个列表的修改项
    $('body').on('click', '.list-edit', function() {
        var belong = $(this).parents('.list-container').attr('belong');
        selectEditModal(belong, $(this));
    });
    // 各个列表的删除项
    $('body').on('click', '.list-delete', function() {
        var belong = $(this).parents('.list-container').attr('belong');
        selectDeleteForm(belong, $(this));
    });
    // 修改默认密码
    $('.title-resetpassword').on('click', function() {
        showAlert();
        $('.edit-password').show();
        setPassword();
    });
    // 开始新学期
    $('.start-new-year').on('click', function() {
        if (confirm('确定要开始新学期吗？')) {
        	 $.ajax({
                 url: '/grade/update.do',
                 type: 'get',
                 contentType: 'application/json',
                 success: function (res) {
                 	alert('开始新学期成功了！');
                 }
             });
        }
    });

    // 修改学校信息
    $('.title-edit').on('click', function() {
        showAlert();
        editSchoolInfo();
    });



    // 班级添加老师
    $('.class-add-teacher').on('click', function() {
        var cid = $(this).attr('cid');
        $('.add-teacher .edit-title-bar').find('div').text('添加老师');
        $('.add-teacher').find('input').val('');

        getSelectTeacherList($('.add-class-teacher'), function() {
            getSelectClassSubjectList(cid);
            $('.add-class-teacher').select2("destroy");
            initSelect($('.add-class-teacher'));
            $('.reset-password-form').hide();
            showEditModal($('.add-teacher'));
        });
        addClassTeacher(cid);
    });

    // 班级添加学生
    $('.class-add-student').on('click', function() {
        var cid = $(this).attr('cid');
        $('.edit-student .edit-title-bar').find('div').text('添加学生');
        $('.edit-student').find('input:text').val('');
        $('.edit-student').find('input:text').attr('disabled', false);
        $('.edit-error').remove();
        $('.reset-password-form').hide();
        var target2 = $('.user-stuuser');
        target2.empty();
        var target3 = $('.user-paruser');
        target3.empty();
        showEditModal($('.edit-student'));
        addClassStudent(cid);
    });
    // 检查学生用户名重复
    $('.edit-student-name').keyup(function() {
        var target = $(this);
        var target2 = $('.user-stuuser');
        var target3 = $('.user-paruser');
        if ($.trim(target.val()) != '') {
            $.ajax({
                url: '/myschool/checkstunm.do',
                type: 'post',
                dataType: 'json',
                data: {
                    userName: $.trim(target.val()),
                    number: 1,
                    userId:$('.edit-student-name').attr('uid')
                },
                success: function(data) {
                    //if ($('.edit-student .edit-title-bar').find('div').text()=="添加学生") {
                        if (data.resultCode == 1) {
                            target.next().remove();
                            /*target.after('<span class="edit-error">用户名已存在！</span>');*/
                        } else {
                            target.next().remove();
                            target2.empty();
                            target3.empty();
                            var content = '';
                            content += '<span style="padding-bottom: 5px;">生成学生用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 150px;" id="studentname">' + data.username  + '</span>';
                            content += '<span>密码&nbsp;&nbsp;' + data.password + '</span>';
                            target2.append(content);
                            var content2 = '';
                            content2 += '<span>生成家长用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 150px;" id="parentname">' + data.parusername + '</span>';
                            content2 += '<span>密码&nbsp;&nbsp;' + data.password + '</span>';
                            target3.append(content2);
                        }
                    //}
                },
                error: function() {
                }
            });
        }
    });

    //调换班级
    $('body').on('click', '.list-change-class', function() {
        var stid = $(this).parent().attr('stid');
        var cid = $(this).parent().attr('cid');
        var stname = $(this).parent().find('span').eq(0).text();
        $('.change-stu-name').text(stname);
        getSelectClassGradeList();
        showEditModal($('.change-class'));
        changeClass(stid, cid, $(this));
    });
    $('.change-class-grade').on('change', function() {
        var gid = $(this).val();
        getSelectClassList(gid);
    });

    //重置密码
    $("#compile-but").click(function(){
        $(".contacts-main").css("display","block");
        $("#bg").css("display","block");
    });

});
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}
function addClassStudent(cid) {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-student-name').val()) == '') {
            $('.edit-student-name').next().remove();
            $('.edit-student-name').after('<span class="edit-error">请输入学生姓名!</span>');
        } else {
            $('.edit-student-name').next().remove();
        }
        if ($('.edit-student').find('.edit-error').length == 0) {
            $.ajax({
                url: '/myschool/addstu.do',
                type: 'post',
                dataType: 'json',
                data: {
                    classId: cid,
                    stuName: $.trim($('#studentname').text()),
                    stuNickname: $.trim($('#studentname').text()),
                    sex: $('.edit-student input:radio:checked').val(),
                    parentName: $.trim($('#parentname').text())
                },
                success: function(data) {
                    if (data == true) {
                        alert('添加成功！');
                        getClassUserList(cid);
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        }
    });
}

function getSelectClassSubjectList(cid) {
    var target = $('.add-class-subject');
    target.empty();
    $.ajax({
        url: '/myschool/subjectlist.do',
        type: 'post',
        dataType: 'json',
        async:false,
        data: {
            classId: cid
        },
        success: function(data) {
            var list = data.rows;
            for (var i = 0; i < list.length; i++) {
                var content = '';
                content += '<option value="' + list[i].id + '">' + list[i].name + '</option>';
                target.append(content);
            }
        },
        error: function() {
            console.log('selSchoolSubject error');
        }
    });
}


function addClassTeacher(cid) {
    $('.edit-commit-btn').on('click', function() {
        if ($('.add-class-teacher').select2("val") != 0) {
            $.ajax({
                url: '/myschool/addtesub.do',
                type: 'post',
                dataType: 'json',
                data: {
                    classid: cid,
                    teacherid: $('.add-class-teacher').select2("val"),
                    subjectid: $('.add-class-subject').val()
                },
                success: function(data) {
                    if (data == true) {
                        alert('添加成功！');
                        getClassUserList(cid);
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}
function editClassTeacher(tcsId, cid) {
    $('.edit-commit-btn').on('click', function() {
        if ($('.add-class-teacher').select2("val") != 0) {
            $.ajax({
                url: '/myschool/uptesub.do',
                type: 'post',
                dataType: 'json',
                data: {
                    tcsId: tcsId,
                    teacherid: $('.add-class-teacher').select2("val"),
                    subjectid: $('.add-class-subject').val()
                },
                success: function(data) {
                    if (data == true) {
                        alert('修改成功！');
                        getClassUserList(cid);
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('添加失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}
function setPassword() {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-password-input').val()) != '') {
            $.ajax({
                url: '/myschool/upnewinitpwd.do',
                type: 'post',
                dataType: 'json',
                data: {
                    newPwd: $('.edit-password-input').val()
                },
                success: function(data) {
                    if (data == true) {
                        alert('修改成功！');
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请输入密码');
        }
    });
}
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
function selectDeleteForm(belong, source) {
    switch (belong) {
        case 'pre-class':
            if (source.parents('.list-ul').hasClass('class-teacher-list')) {
                var tcsid = source.parent().attr('tcsid');
                deleteClassTeacher(tcsid, source);
            } else {
                var studentid = source.parent().attr('stid');
                deleteClassStudent(studentid, source);
            }
            break;
    }
}
function deleteClassStudent(stid, source) {
    var classId=source.parent().attr("cid")
    if (confirm('确定要删除该学生吗？')) {
        $.ajax({
            url: '/myschool/delstu.do',
            type: 'post',
            dataType: 'json',
            data: {
                studentId: stid,
                classId:classId
            },
            success: function(data) {
                if (data == true) {
                    alert('删除成功');
                    source.parent().remove();
                } else {
                    alert('删除失败');
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}
function deleteClassTeacher(tid, source) {
    if (confirm('确定要删除该老师吗？')) {
        $.ajax({
            url: '/myschool/deltesub.do',
            type: 'post',
            dataType: 'json',
            data: {
                tcsId: tid
            },
            success: function(data) {
                if (data == true) {
                    alert('删除成功');
                    source.parent().remove();
                } else {
                    alert('删除失败');
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}
function selectEditModal(belong, source) {
    switch (belong) {
        case 'pre-class':
            if (source.parents('.list-ul').hasClass('class-teacher-list')) {
                var teacherid = source.parent().attr('tid');
                var cid = source.parent().attr('cid');
                var sid=source.parent().attr('sid');
                var tcsId=source.parent().attr('tcsId');

                $('.add-teacher .edit-title-bar').find('div').text('编辑老师');
                getSelectTeacherList($('.add-class-teacher'), function() {
                    getSelectClassSubjectList(cid);
                    $('.add-class-teacher').select2("destroy");
                    initSelect($('.add-class-teacher'));
                    showEditModal($('.add-teacher'));
                    $('.add-class-teacher').select2('val',teacherid);
                    $('#add-class-subject').val(sid);
                });
                editClassTeacher(tcsId,cid);
            } else {
                var studentid = source.parent().attr('stid');
                $('.edit-student .edit-title-bar').find('div').text('编辑学生');
                $('.edit-error').remove();
                $('.reset-password-form').show();
                $('.reset-password-form img').hide();
                showEditClassStudentInfo(studentid);
                var target2 = $('.user-stuuser');
                target2.empty();
                var target3 = $('.user-paruser');
                target3.empty();
                showEditModal($('.edit-student'));
                editClassStudentInfo(studentid, source);
            }
            break;
    }
}
function editSchoolInfo() {
    var schoolName = $('.title-school-name').text();
    var schoolLevel = $('.title-school-level').text().split(',');
    var inputSchoolName = $('.edit-school .edit-input-group').eq(0).find('input');
    var selectLevel = $('.edit-school .edit-input-group').eq(1).find('input');
    var content = '';
    var typeList = '';
    inputSchoolName.val(schoolName);
    for (var i = 0; i < selectLevel.length; i++) {
        selectLevel.eq(i).prop('checked', false);
        for (var j in schoolLevel) {
            if ($.trim(selectLevel.eq(i).parent().text()) == schoolLevel[j]) {
                selectLevel.eq(i).prop('checked', 'checked');
            }
        }
    }
    $('.edit-school').show();
    $('.edit-commit-btn').on('click', function() {
        for (var i = 0; i < selectLevel.length; i++) {
            if (selectLevel.eq(i).is(':checked')) {
                if (content == '')
                    content += $.trim(selectLevel.eq(i).parent().text());
                else
                    content += ',' + $.trim(selectLevel.eq(i).parent().text());
                typeList += '1,';
            } else {
                typeList += '0,';
            }
        }
        if ($.trim(inputSchoolName.val()) != '' && typeList != '0,0,0,') {
            $.ajax({
                url: '/myschool/upschool.do',
                type: 'post',
                dataType: 'json',
                data: {
                    schoolName: inputSchoolName.val(),
                    schoolLevel: typeList
                },
                success: function(data) {
                    if (data == true) {
                        $('.title-school-name').text(inputSchoolName.val());
                        $('.title-school-level').text(content);
                        alert('修改成功！');
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}
function getSchoolInfo() {
    $.ajax({
        url: '/myschool/schoolinfo.do',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var schoolInfo = data;
            var content = [];
            $('.title-school-year').text(schoolInfo.schoolYear);
            $('.title-school-name').text(schoolInfo.schoolName);
            if (isPrimary(data.schoolTypeInt)) content.push('小学');
            if (isJunior(data.schoolTypeInt)) content.push('初中');
            if (isSenior(data.schoolTypeInt)) content.push('高中');
            $('.title-school-level').text(content);
        },
        error: function() {
            console.log('selSchoolInfo error');
        }
    });
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

function getSelectTeacherList(target,callback) {
    target.empty();
    target.append('<option value="0">请选择...</option>');
    $.ajax({
        url: '/myschool/teacherlist.do',
        type: 'post',
        dataType: 'json',
        async:false,
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


function editClass(cid) {
    var gid = $("#content_main").attr("gradeId");
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-class-number').val()) != '' && $('.edit-class-teacher').select2('val') != 0) {
            $.ajax({
                url: '/myschool/upclass.do',
                type: 'post',
                dataType: 'json',
                data: {
                    classid: cid,
                    classname: $('.edit-class-number').val(),
                    teacherid: $('.edit-class-teacher').select2('val')
                },
                success: function(data) {
                    if (data.code == 200) {
                        alert('修改成功！');
                        getClassList(gid);
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请填写完整！');
        }
    });
}

function deleteClassInfo(cid, target) {
    $.ajax({
        url: '/myschool/delclassinfo.do',
        type: 'post',
        dataType: 'json',
        data: {
            classid: cid
        },
        success: function(data) {
            if(data==true){
                alert("删除成功！");
            }else{
                alert("请先删除班级下的老师和学生！");
            }
        }
    });
}

function getClassList(gid) {
    var target = $('.class-list .list-ul');
    $.ajax({
        url: '/reverse/selClassByGradeId.action',
        type: 'POST',
        dataType: 'json',
        data: {
            gradeid: gid
        },
        success: function(data) {
            target.empty();
            var classList = data.classList;
            for (var i = 0; i < classList.length; i++) {
                var content = '';
                var teacherName = classList[i].teacherName == null ? '暂无' : classList[i].teacherName;
                content += '<li cid=' + classList[i].id + ' gid=' + gid + '><div class="list-content"><span>' + $('.list-title-grade').text() + classList[i].classname + '</span><span>班主任：</span>';
                content += '<span class="grade-manager-name">' + teacherName + '</span></div><i class="fa fa-pencil list-edit"></i>';
                content += '<i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }
        },
        error: function() {
            console.log('selClassByGradeId error');
        }
    });
}

function getClassUserList(cid) {
    var targetT = $('.class-teacher-list');
    var targetS = $('.class-student-list');
    targetT.empty();
    targetS.empty();
    $.ajax({
        url: '/myschool/testulist.do',
        type: 'post',
        dataType: 'json',
        data: {
            classid: cid
        },
        success: function(data) {
            var teacherList = data.techList;
            var studentList = data.stuList;
            for (var i = 0; i < teacherList.length; i++) {
                var content = '';
                content += '<li tid=' + teacherList[i].teacherId + ' tcsId="'+teacherList[i].tclId+'" cid=' + cid + ' sid="'+teacherList[i].subjectId+'"><div class="list-content"><span>' + teacherList[i].userName + '</span>';
                content += '<span>' + teacherList[i].subjectName + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i></li>';
                targetT.append(content);
            }
            for (var i = 0; i < studentList.length; i++) {
                var content = '';
                content += '<li stid=' + studentList[i].id + ' cid=' + cid + '><div class="list-content"><span>' + studentList[i].userName + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i>';
                content += '<img class="list-change-class" src="/img/K6KT/changeclass.png" title="学生调换班级"/></li>';
                targetS.append(content);
            }
        }
    });
}



function showEditClassStudentInfo(stid) {
    var target2 = $('.user-stuuser');
    target2.empty();
    var target3 = $('.user-paruser');
    target3.empty();
    $.ajax({
        url: '/myschool/stuandparent.do',
        type: 'post',
        dataType: 'json',
        data: {
            studentId: stid
        },
        success: function(data) {
            var sinfo = data.student;
            var pinfo = data.parent;
            $('.edit-student-username').val(sinfo.userName).attr('disabled', 'disabled');
            $('.edit-student-name').val(sinfo.userName);
            $('.edit-student-name').attr({'uid':stid});
            $('.edit-student input:radio').each(function(index, el) {
                if ($(this).val() == sinfo.sex) {
                    $(this).prop('checked', true);
                }
            });
            //$('.edit-parent-name').val(pinfo.nickName);
            $('.reset-student-password').attr('onclick', 'resetPsw("' + stid + '",1)');
            $('.reset-parent-password').attr('onclick', 'resetPsw("' + stid + '",0)');
            var content = '';
            content += '<span style="padding-bottom: 5px;">生成学生用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 150px;" id="studentname">' + sinfo.userName  + '</span>';
            target2.append(content);
            var content2 = '';
            content2 += '<span style="padding-bottom: 5px;">生成家长用户名&nbsp;&nbsp;&nbsp;</span><span style="width: 180px;" id="parentname">' + pinfo + '</span>';
            target3.append(content2);
            //$('#parentname').attr({
            //    'pid': pinfo.id
            //});
        }
    });
}

function editClassStudentInfo(stid, target) {
    $('.edit-commit-btn').on('click', function() {
        var pid = $('.edit-parent-username').attr('pid');
        if ($.trim($('.edit-student-username').val()) == '') {
            $('.edit-student-username').next().remove();
            $('.edit-student-username').after('<span class="edit-error">请输入学生用户名!</span>');
        }
        if ($.trim($('.edit-student-name').val()) == '') {
            $('.edit-student-name').next().remove();
            $('.edit-student-name').after('<span class="edit-error">请输入学生姓名!</span>');
        } else {
            $('.edit-student-name').next().remove();
        }
        if ($.trim($('.edit-parent-username').val()) == '') {
            $('.edit-parent-username').next().remove();
            $('.edit-parent-username').after('<span class="edit-error">请输入家长用户名!</span>');
        }
        if ($.trim($('.edit-parent-name').val()) == '') {
            $('.edit-parent-name').next().remove();
            $('.edit-parent-name').after('<span class="edit-error">请输入家长姓名!</span>');
        } else {
            $('.edit-parent-name').next().remove();
        }
        if ($('.edit-student').find('.edit-error').length == 0) {
            $.ajax({
                url: '/myschool/upstu.do',
                type: 'post',
                dataType: 'json',
                data: {
                    studentId: stid,
                    parentId: pid,
                    stuNickname: $.trim($('#studentname').text()),
                    sex: $('.edit-student input:radio:checked').val()
                },
                success: function(data) {
                    if (data == true) {
                        alert('修改成功！');
                        target.parent().find('span').eq(0).text($.trim($('#studentname').text()));
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    alert('修改失败！');
                },
                complete: function() {
                    hideAlert();
                }
            });
        }
    });
}

function changeClass(stid, cid, source) {
    $('.edit-commit-btn').on('click', function() {
        $.ajax({
            url: '/myschool/changestu.do',
            type: 'post',
            dataType: 'json',
            data: {
                oldClassId: cid,
                newClassId: $('.change-class-class').val(),
                studentId: stid
            },
            success: function(data) {
                if (data == true) {
                    alert('换班成功');
                    source.parent().remove();
                } else {
                    alert('换班失败，请重试！');
                }
            },
            error: function() {
                alert('换班失败，请重试！');
            },
            complete: function() {
                hideAlert();
            }
        });
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
            success: function(data) {
                if (data == true) {
                    if (type == 1) {
                        $('.reset-password-form img').eq(0).show();
                    } else if (type==2) {
                        $('.reset-password-form img').eq(2).show();
                    } else {
                        $('.reset-password-form img').eq(1).show();
                    }
                } else {
                    alert('重置失败');
                }
            },
            error: function() {
                alert('重置失败');
            }
        });
    }
}

function getSelectClassGradeList() {
    $.ajax({
        url: '/myschool/gradelist.do',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var row = data.rows;
            var target = $('.change-class-grade');
            target.empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<option value=' + row[i].id + '>' + row[i].name + '</option>';
                target.append(content);
            }
            getSelectClassList(row[0].id);
        }
    });
}

function getSelectClassList(gid) {
    $.ajax({
        url: '/myschool/classlist.do',
        type: 'post',
        dataType: 'json',
        async:false,
        data: {
            gradeid: gid
        },
        success: function(data) {
            var row = data.rows;
            var target = $('.change-class-class');
            target.empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<option value=' + row[i].id + '>' + row[i].className + '</option>';
                target.append(content);
            }
        }
    });
}
function gradeList(){
    window.location.href="/myschool/manageschool?version=57&tag=3";
}

function classInfoList(){
    var gradeId=getUrlParam("gid");
    window.location.href="/myschool/manageclass/"+gradeId+"?version=57&tag=3"+"&gradeName="+encodeURI(getUrlParam("gradeName"));
}

var createInfo={"all":"","users":""};

function userChoose(ug)
{
    $(".contacts-bottom-H").removeClass("contacts-bottom-H");
    $("#"+ug+"_li").addClass("contacts-bottom-H");
    var gs=["xiaoyuan_div","dep_div","teacher_div","student_div","parent_div","teacher2_div"];

    for(var i=0;i<=5;i++)
    {
        var s=gs[i];
        if(s==ug)
        {
            $("#"+s).show();
        }
        else
        {
            $("#"+s).hide();
        }
    }

    if(ug=="teacher_div")
    {
        getusets(1,1);
    }
    if(ug=="student_div")
    {
        getusets1(2);
    }
    /*if(ug=="student_div")
     {
     getusets(1,0);
     }*/
    if(ug=="parent_div")
    {
        getusets1(3);
    }

    /*if(ug=="parent_div")
     {
     getusets(2,0);
     }*/

    if(ug=="teacher2_div")
    {
        getusets1(1);
    }
    if(ug=="dep_div")
    {
        getusets(4,-1);
    }
}

function hideUserGroup()
{
    $("#selectUser1").val("");
    createInfo.all="";
    createInfo.users="";
    $(".contacts-main").css("display","none");
    $("#bg").css("display","none");
}

function selectAll(tag)
{
    var text="全校老师";
    if(tag==2)
        text="全校同学";
    if(tag==3)
        text="全校师生";
    var u=$("#selectUser1").val();
    var index=u.indexOf(text);
    if(index<0)
    {
        createInfo.all=createInfo.all+tag+",";
        $("#selectUser1").val(u+" "+text);
    }
}


function searchName(us,input)
{
    var text=$(input).val();
    if(!text)
    {
        $("#"+us).find("ul").show();
    }
    else
    {
        $("#"+us).find("ul").each(function(){
            var is=isContanitText($(this).attr("id"),text);
            if(is)
            {
                showGroup($(this).attr("id"));
            }
            else
            {
                $("#"+$(this).attr("id")).hide();
            }
        });
    }
}

function isContanitText(id,t)
{
    $("#"+id+" li:gt(0)").each(function(){
        var text=$(this).text();
        if(text.indexOf(t)>0)
            return true;
    });
    return false;
}

function showGroup(id)
{
    $("#k_"+id).addClass("fa-caret-down").removeClass('fa-caret-right');
    $("#"+id+" li:gt(0)").show();
}

//查询班级成员，以便添加各个人
function getusets1(ty)
{
    $.ajax({
        url: '/notice/classes/users.do?type='+ty,
        type: 'get',
        contentType: 'application/json',
        success: function (res) {
            {
                var $contain=jQuery("#student_user_group");
                if(ty==3)
                {
                    $contain=jQuery("#parent_user_group");
                }


                if(ty==1)
                {
                    $contain=jQuery("#teacher2_user_group");
                }
                $contain.empty();
                for(var i=0;i<res.length;i++)
                {
                    var html =createUserGroup(res[i],ty);
                    $contain.append(html);
                }
            }
        }
    });
}

function getusets(type,ist)
{
    $.ajax({
        url: '/notice/users.do?type='+type+"&ist="+ist,
        type: 'get',
        contentType: 'application/json',
        success: function (res) {
            {
                var $contain=$("#teacher_user_group");
                if(ist!="1") //老师
                {
                    if(type==1) //学生
                        $contain=$("#student_user_group");
                    if(type==2) //家长
                        $contain=$("#parent_user_group");
                }
                if(type== 4 && ist==-1)
                {
                    $contain=jQuery("#dep_user_group");
                }
                $contain.empty();
                for(var i=0;i<res.length;i++)
                {
                    var html =createUserGroup(res[i],type);
                    $contain.append(html);
                }
            }
        }
    });
}

function createUserGroup(obj,ty)
{
    try
    {
        var groupInfo =obj.t;
        var users=obj.list;

        var html='<ul id="'+groupInfo.idStr+ty+'">';
        html+='<li class="contacts-even SM"  >';
        html+='<div class="comtacts-sv" name="comtacts-sv">';
        html+='<i id="k_'+groupInfo.idStr+ty+'" class="comtacts-ss fa fa-caret-right" id="comtacts-ss" onclick="toggleGroup(\''+groupInfo.idStr+ty+'\')"></i>';
        html+='</div>';
        html+='<img src="/img/notic/contacts-WJ.png"><span>'+groupInfo.value+'</span>';
        html+='<span class="teacher_user_QX" onclick="selectAllUser(this,\''+groupInfo.idStr+ty+'\')">全选</span>';
        html+='</li>';

        for(var j=0;j<users.length;j++)
        {
            html+='<li id="li_'+users[j].idStr+'" class="contacts-odd XL" onclick="selectUser(\''+users[j].idStr+'\',\''+users[j].value+'\')">';
            html+=users[j].value;
            html+=' </li>';
        }
        html+='</ul>';
        return html;
    }catch(x)
    {
        alert(x);
    }
}

function toggleGroup(id)
{
    var cl=$("#k_"+id).prop('className');
    if(cl.indexOf("fa-caret-right")>0)
    {
        $("#k_"+id).addClass("fa-caret-down").removeClass('fa-caret-right');
        $("#"+id+" li:gt(0)").show();
    }
    else
    {
        $("#k_"+id).addClass("fa-caret-right").removeClass('fa-caret-down');
        $("#"+id+" li:gt(0)").hide();
    }

}

function selectAllUser(obj,id)
{
    var text=$(obj).text();
    if(text=='全选')
    {
        $("#"+id+" li:gt(0)").each(function(){
            $(this).trigger("click");
        });
        $(obj).text("已全选");
        $(obj).unbind();
    }
}

function selectUser(id,user)
{
    var u=$("#selectUser1").val();
    var index=u.indexOf(user);
    if(index<0)
    {
        createInfo.users=createInfo.users+id+",";
        $("#selectUser1").val(u+" "+user);
    }
}

function sureUserCancel()
{
    hideUserGroup();
}

function sureUser()
{
    var u=$("#selectUser1").val();
    if(!u&&!createInfo.all && !createInfo.users)
    {
        alert("请选择重置密码人员!");
        return;
    }
    $.ajax({
        url: '/myschool/getSchoolDefaultPwd.do',
        dataType: 'json',
        success: function(data) {
            if(confirm('重置后，这些人员的密码会变为"'+data+'"，您确定要这么做吗？')){
                $.ajax({
                    url: '/myschool/resetSelectPassword.do',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        all: createInfo.all,
                        users:createInfo.users,
                        pwd:data
                    },
                    success: function(data) {
                        if (data == true) {
                            alert('修改成功！');
                        } else {
                            alert('修改失败！');
                        }
                    },
                    error: function() {
                        alert('修改失败！');
                    },
                    complete: function() {
                        hideUserGroup();
                    }
                });
            }
        }
    });
}