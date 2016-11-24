/**
 * Created by yan on 2015/4/22.
 */

$(function () {

    initHeight();

    var interestclassId;

    var cid = $(this).parent().attr('cid');
    getInterestClassList();
    $('.interest-list').show();

    $('#category').html(sessionStorage.getItem("name") + '>' + '班级列表');

    //打开关闭选课
    $('body').on('click','.open-close-class', function() {
        var element = this;
        var classId = $(this).parent().attr('cid');
        $.ajax({
            url: '/myschool/upexpstat.do',
            type: 'post',
            dataType: 'json',
            data: {classid:classId},
            success: function(data) {
                    if(data ==1){
                        $(element).text("关闭选课");
                        $(element).attr("style", "background-color: #ff830a;");
                    } else {
                        $(element).text("打开选课");
                        $(element).attr("style", "background-color: #00BDBB;");
                    }
            },
            error: function() {
                console.log('error');
            }
        });
    });

    // 选择上课时间
    $('.select-time-lesson >li').on('click', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        } else {
            $(this).addClass('selected');
        }
    });

    // 添加拓展课
    $('.list-add-interest').on('click', function() {
        $('.course-type').show();
        $('.edit-interest .edit-title-bar').find('div').text('添加拓展课');
        $('.edit-interest').find('input').val('');
        $('.edit-interest').find('.select-time-lesson >li').removeClass('selected');
        $('.edit-interest').find('.edit-error').remove();
        $('.add-interest-teacher').select2("destroy");
        var checkSexList = $('input[name=interest-sex]');
        for (var j = 0; j < checkSexList.length; j++) {
            checkSexList.eq(j).prop('checked', false);
        }
        getSelectSubjectList();
        getSelectRadioGradeList('interest');
        getSelectTeacherList($('.add-interest-teacher'), function() {
            initSelect($('.add-interest-teacher'));
            showEditModal($('.edit-interest'));
        });
        addInterestClass();
    });

    // 学生选课去向
    $('.list-interest-change').on('click', function() {
        window.open("/myschool/stuinteclasscountpage.do","_blank");
    });

    // 拓展课添加学生
    $('.interest-class-add-student').on('click', function() {
        var cid = $(this).attr('cid');
        interestclassId = cid;
        var tid = $(this).attr('tid');
        $('.add-student .edit-title-bar').find('div').text('添加学生');
        $('.add-student').find('input').val('');

        getSelectStudentList($('.add-class-student'),cid,null,null, true, function() {
            //getSelectClassSubjectList(cid);
            $('.add-class-student').select2("destroy");
            initSelect($('.add-class-student'));
            $('.reset-password-form').hide();
            showEditModal($('.add-student'));
        });
        addInterestClassStudent(cid,tid);
    });

    $('body').on('change', '#gradeList',function(){
        var gradeId = $('#gradeList').find('option:selected').val();
        getSelectStudentList($('.add-class-student'),interestclassId,gradeId,null, false, function(data) {
            //getSelectClassSubjectList(cid);
            $('.add-class-student').select2("destroy");
            initSelect($('.add-class-student'));
            $('.reset-password-form').hide();
            showEditModal($('.add-student'));

            //展示班级
            var classList = data.classList;
            $('#classList').empty();
            var content = '<option value="">全部班级</option>';
            for (var i = 0; i < classList.length; i++) {
                content += '<option value=' + classList[i].classId + '>' + ' ' + classList[i].className + '</option>';
            }
            $('#classList').append(content);
        });
    })

    $('body').on('change', '#classList',function(){
        var gradeId = $('#gradeList').find('option:selected').val();
        var classId = $('#classList').find('option:selected').val();
        getSelectStudentList($('.add-class-student'),interestclassId,gradeId,classId, false, function(data) {
            //getSelectClassSubjectList(cid);
            $('.add-class-student').select2("destroy");
            initSelect($('.add-class-student'));
            $('.reset-password-form').hide();
            showEditModal($('.add-student'));
        });
    })


    // 关闭模态框
    $('.close-modal').on('click', function () {
        hideAlert();
    });
    //上下半学期切换
    $('.manage-tianjia').each(function(i){
        if(i==0){
            $(this).addClass('manage-hover');
            $('.interest-class-add-student').attr('tid', 1);
            $('.loading-img').fadeIn('fast');
        }
        $(this).click(function(){
            $('.manage-tianjia').removeClass('manage-hover');
            $(this).addClass('manage-hover');
            $('.header').nextAll().remove();
            $('.loading-img').fadeIn('fast');
            if(i==0) {
                $('.interest-class-student-list').show();
                $('.second-interest-class-student-list').hide();
                $('.interest-class-add-student').attr('tid', 1);
            } else {
                $('.interest-class-student-list').hide();
                $('.second-interest-class-student-list').show();
                $('.interest-class-add-student').attr('tid', 2);
            }



        });
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

    //从属性别单选
    $('input[name=interest-sex]').click(function(){
        $(this).attr('checked','checked').siblings().removeAttr('checked');
    });

    // 双击兴趣班进入学生列表
    $('body').on('click', '.interest-list .list-content', function() {
        var cid = $(this).parent().attr('cid');
        $('.interest-class-add-student').attr('cid', cid);
        $('.interest-list').hide();
        getInterestClassUserList(cid);
        $('.interest-class-list').show();
    });
    getSelectICList();
    $('body').on('click', '.change-category', function(){
        showEditModal($('.category'));
        changeCategory($(this).parent().attr('cid'));
    })

});

function changeCategory(classId){
    $('.edit-commit-btn').on('click', function() {
        var interestCategoryId = $('.category-select').val();
        $.ajax({
            url: '/interestCategory/change.do',
            type: 'post',
            dataType: 'json',
            data: {
                classId: classId,
                interestCategoryId: interestCategoryId
            },
            success: function(data) {
                if(data.code == '200'){
                    getInterestClassList();
                    alert("调换成功");
                    hideAlert();
                } else {
                    alert('调换失败');
                }

            },
            error: function() {
                alert('调换失败');
            }
        });
    })
}


function deleteInterestClassUser(cid,tid, userid, target) {
    if (confirm('确定要删除该学生吗？')) {
        $.ajax({
            url: '/myschool/delstu4expand.do',
            type: 'post',
            dataType: 'json',
            data: {
                classId: cid,
                studentId: userid,
                type: tid
            },
            success: function(data) {
                if (data == true) {
                    alert('删除成功');
                    target.parent().remove();
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

function getSelectClassSubjectList(cid) {
    var target = $('.add-class-subject');
    target.empty();
    $.ajax({
        url: '/reverse/selSubjectInfoByClassId.action',
        type: 'post',
        dataType: 'json',
        data: {
            classid: cid
        },
        success: function(data) {
            console.log(data);
            var list = data.subjectList;
            for (var i = 0; i < list.length; i++) {
                var content = '';
                content += '<option value=' + list[i].id + '>' + list[i].name + '</option>';
                target.append(content);
            }
        },
        error: function() {
            console.log('selSchoolSubject error');
        }
    });
}

function getSelectStudentList(target, cid, gradeId, classId, type, callback) {
    target.empty();
    target.append('<option value="0">请选择...</option>');
    $.ajax({
        url: '/myschool/getGradeStudent.do',
        type: 'post',
        dataType: 'json',
        data: {
            pageSize: 5000,
            interestClassId: cid,
            gradeId:gradeId,
            classId:classId
        },
        success: function(data) {
            var row = data.rows;
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<option value=' + row[i].id + '>' +  ' ' + row[i].nickName + '</option>';
                target.append(content);
            }
            if(type) {
                //展示年级
                var gradeList = data.gradeList;
                $('#gradeList').empty();
                content = '<option value="">全部年级</option>';
                for (var i = 0; i < gradeList.length; i++) {
                    content += '<option value=' + gradeList[i].id + '>' + ' ' + gradeList[i].name + '</option>';
                }
                $('#gradeList').append(content);
                //展示班级
                var classList = data.classList;
                $('#classList').empty();
                content = '<option value="">全部班级</option>';
                for (var i = 0; i < classList.length; i++) {
                    content += '<option value=' + classList[i].classId + '>' + ' ' + classList[i].className + '</option>';
                }
                $('#classList').append(content);
            }
            callback(data);
        }
    });
}

function getInterestClassUserList(cid) {
    $('.manage-tianjia').removeClass('manage-hover');
    $('.first-class').addClass('manage-hover');
    $('.interest-class-student-list').show();
    $('.second-interest-class-student-list').hide();
    $('.team-sutdent').hide();
    $('.class-right-title-bar').show();
    var targetS = $('.interest-class-student-list');
    var targetC = $('.second-interest-class-student-list');
    targetS.empty();
    targetC.empty();
    $.ajax({
        url: '/myschool/interestdata.do',
        type: 'post',
        dataType: 'json',
        data: {
            classId: cid
        },
        success: function(data) {
            var students = data.students;
            var longTermStudent=[];
            var firstTermStudent=[];
            var secondTermStudent=[];
            for(var i=0;i<students.length;i++){
                if(students[i].courseType==0){
                    longTermStudent.push(students[i]);
                }else if(students[i].courseType==1){
                    firstTermStudent.push(students[i]);
                }else if(students[i].courseType==2){
                    secondTermStudent.push(students[i]);
                }
            }
            var classInfo = data.classInfo;
            $('.class-name').text("班级名称：" + classInfo.className);
            $('.teacher-name').text("老师：" + classInfo.teacherName);
            $('.right-title-bar').remove();
            if (classInfo.isLongCourse==true) {
                $('.fnum').text(students.length + "/" + classInfo.totalStudentCount);
                $('.snum').text('');
                $('.interest-class-student-list').show();
                $('.interest-class-add-student').attr("tid",0);
                firstTermStudent=students;
            } else {
                //if (classInfo.firstTerm==1){
                    $('.fnum').text(firstTermStudent.length + "/" + classInfo.totalStudentCount);
                //}
                //if (classInfo.secondTerm==1) {
                    $('.snum').text(" " + secondTermStudent.length + "/" + classInfo.totalStudentCount);
                //}
            }
            if (classInfo.isLongCourse==false) {
                $('.team-sutdent').show();
            }
            for (var i = 0; i < firstTermStudent.length; i++) {
                var content = '';
                content += '<li tid=' + firstTermStudent[i].studentId + ' cid=' + cid + ' pid=' + classInfo.coursetype + ' fid=' + classInfo.firstTerm + ' sid=' + classInfo.secondTerm + ' did=' + 1 + '><div class="list-content"><span>' + firstTermStudent[i].studentName + '</span>';
                content += '<span>行政班：</span><span>' + firstTermStudent[i].className + '</span></div>';
                content += '<i class="fa fa-trash-o list-delete"></i></li>';
                targetS.append(content);
            }
            for (var i=0;i<secondTermStudent.length;i++) {
                var content = '';
                content += '<li tid=' + secondTermStudent[i].studentId + ' cid=' + cid + ' pid=' + classInfo.coursetype + ' fid=' + classInfo.firstTerm + ' sid=' + classInfo.secondTerm + ' did=' + 2 + '><div class="list-content"><span>' + secondTermStudent[i].studentName + '</span>';
                content += '<span>行政班：</span><span>' + secondTermStudent[i].className + '</span></div>';
                content += '<i class="fa fa-trash-o list-delete"></i></li>';
                targetC.append(content);
            }
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
        case 'interest':
            var classId = source.parent().attr('cid');
            deleteInterestClass(classId, source);
            break;
        case 'interest-class':
            var classId = source.parent().attr('cid');
            var tid = $('.interest-class-add-student').attr("tid");
            var userid = source.parent().attr('tid');
            deleteInterestClassUser(classId,tid,userid,source);
            break;

    }
}
function selectEditModal(belong, source) {
    switch (belong) {
        case 'interest':
            var classId = source.parent().attr('cid');
            $('.course-type').hide();
            $('.edit-interest .edit-title-bar').find('div').text('编辑拓展课');
            $('.edit-interest').find('input').val('');
            $('.edit-interest').find('.select-time-lesson >li').removeClass('selected');
            $('.edit-interest').find('.edit-error').remove();
            $('.add-interest-teacher').select2("destroy");
            getSelectSubjectList();
            //getSelectICList();
            getSelectRadioGradeList('interest');
            getSelectTeacherList($('.add-interest-teacher'), function() {
                initSelect($('.add-interest-teacher'));
                showEditInterestInfo(classId);
                showEditModal($('.edit-interest'));
            });
            updateInterestClass(classId);
            break;
    }
}


// 初始化select2
function initSelect(target) {
    target.select2({
        width: '270px',
        containerCss: {
            'margin-left': '85px',
            'font-family': 'sans-serif',
            'margin-top': '10px'
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

function getInterestClassList() {
    var role = $('#role').attr('role');
    var userId = $('#role').attr('userId');
    var data={};
    data.interestCategoryId = sessionStorage.getItem("icid");
    $.ajax({
        url: '/myschool/expandlist.do',
        type: 'post',
        dataType: 'json',
        data: data,
        success: function(data) {
            var row = data.rows;
            var target = $('.interest-list>.list-ul');
            target.empty();
            var name = '';
            var background_color = '';
            for (var i = 0; i < row.length; i++) {
                if(1== role || row[i].teacherId == userId) {
                    if (row[i].state == 1) {
                        name = '关闭选课';
                        background_color = '#ff830a;';
                    } else {
                        name = '打开选课';
                        background_color = '#00BDBB;';
                    }
                    var content = '';
                    var tname = row[i].teacherName == null ? '暂无' : row[i].teacherName;
                    content += '<li cid="' + row[i].id + '"><div class="list-content"><span>' + row[i].className + '</span>';
                    content += '<span>老师：</span>';
                    content += '<span class="grade-manager-name">' + tname + '</span></div>';
                    if(1 == role) {
                        content += '<i style="position: relative;top:8px;" class="fa fa-pencil list-edit"></i><i style="position: relative;top:8px;" class="fa fa-trash-o list-delete"></i>' +
                        '<span class="open-close-class" style="background-color: '+background_color+'" >' + name + '</span>' +
                        '<span class="change-category">调换类别</span></li>';
                    }
                    target.append(content);
                }
            }
        }
    });
}
function gotoClassList(role)
{
    window.location.href="/myschool/interestList.do?role=" + role;
}
function getSelectSubjectList() {
    var target = $('.add-class-subject');
    target.empty();
    $.ajax({
        url: '/myschool/sublist.do',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var list = data.rows;
            for (var i = 0; i < list.length; i++) {
                var content = '';
                content += '<option value=' + list[i].id + '>' + list[i].name + '</option>';
                target.append(content);
            }
        },
        error: function() {
            console.log('sublist.do error');
        }
    });
}

function getSelectICList() {
    var target = $('.category-select');
    target.empty();
    $.ajax({
        url: '/interestCategory/list.do',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var list = data.list;
            for (var i = 0; i < list.length; i++) {
                var content = '';
                content += '<option value=' + list[i].id + '>' + list[i].name + '</option>';
                target.append(content);
            }
        },
        error: function() {
            console.log('IClist.do error');
        }
    });
}
function getSelectRadioGradeList(blong) {
    var target = $('.edit-subject-selectgrade');

    if (blong=="subject") {
        target = $('.edit-subject-selectgrade');
    } else if ("interest") {
        target = $('.edit-subject-selectgrade-interest');
    }

    $.ajax({
        url: '/myschool/gradelist.do',
        async:false,
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            console.log(data.rows);
            var row = data.rows;

            target.empty();
            target.append('<label class="checkbox-inline"><input type="checkbox" class="edit-select-all"> 全部</label>');
            for (var i = 0; i < row.length; i++) {
                var content = '';
                content += '<label class="checkbox-inline"><input type="checkbox" gid=' + row[i].id + '> ' + row[i].name + '</label>';
                target.append(content);
            }
        },
        error: function() {
//            console.log('error');
        }
    });
}
function addInterestClassStudent(cid,tid) {
    $('.edit-commit-btn').on('click', function() {

        var stuIdList = $('.add-class-student').select2("val");
        var stuIds = '';
        for(var i=0; i<stuIdList.length; i++){
            stuIds +=stuIdList[i] + ',';
        }

        if ($('.add-class-student').select2("val") != 0) {

            $.ajax({
                url: '/myschool/addstu2expand.do',
                type: 'post',
                dataType: 'json',
                data: {
                    classId: cid,
                    studentId: stuIds,
                    courseType:tid,
                    dropState:1
                },
                success: function(data) {
                    if (data.code == '200') {
                        alert(data.message);
                        getInterestClassUserList(cid);
                    } else if(data.resultCode == 2){
                        alert(data.mesg);
                    } else{
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
function addInterestClass() {
    $('.edit-commit-btn').on('click', function() {
        var selectTimeList = $('.select-time-lesson>li');
        var content = '';
        var startTime = $('#opentime').val();
        var closeTime = $('#closetime').val();
        var classContent = $('#content').val();
        selectTimeList.each(function() {
            if ($(this).hasClass('selected')) {
                content += $(this).attr('val') + ',';
            }
        });
        if( $('.add-interest-teacher').select2('val')=='0'){
            alert("请选择老师");
            return
        }
        if(content==''){
            alert("请选择上课时间")
            return;
        }

        var cousetype = 1;
        var fteam = 0;
        var steam = 0;
        if (document.getElementsByName("classtype")[1].checked){
            cousetype = 2;
            if ($('#fteam').is(':checked')) {
                fteam = 1;
            }
            if ($('#steam').is(':checked')) {
                steam = 1;
            }

        }
        var checkList = $('.edit-subject-selectgrade-interest input:checkbox');
        var grList = '';
        checkList.each(function() {
            if ($(this).is(':checked')) {
                if ($(this).attr('gid')) {
                    grList += $(this).attr('gid') + ',';
                }
            }
        });
        var checkSexList = $('input[name=interest-sex]');
        var sex = '';
        checkSexList.each(function() {
            if ($(this).is(':checked')) {
                sex = $(this).attr('sexid');
            }
        });
        if (sex=='') {
            alert("请填写性别属性！");
            return;
        }
        if (grList != '' && (cousetype==1 || (cousetype==2&&(fteam==1||steam==1)))) {
            validateInterestClass();
            if ($('.edit-interest').find('.edit-error').length == 0) {
                $.ajax({
                    url: '/myschool/addexpand.do',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        interestClassName: $.trim($('.edit-interest-name').val()),
                        teacherid: $('.add-interest-teacher').select2('val'),
                        studentCount: $('.edit-interest-number').val(),
                        classtime: content,
                        subjectid: $('.edit-interest .add-class-subject').val(),
                        opentime: startTime,
                        closetime: closeTime,
                        coursetype: cousetype,
                        typeId:sessionStorage.getItem("icid"),
                        firstteam: fteam,
                        secondteam: steam,
                        gradeArry: grList,
                        classContent:classContent,
                        room:$.trim($('.edit-interest-room').val()),
                        sex:sex
                    },
                    success: function(data) {
                        //if (data == true) {
                            alert('添加成功！');
                            getInterestClassList();
                        $('#content').val('');
                        //} else {
                            //alert('添加失败！');
                        //}
                    },
                    error: function() {
                        alert('添加失败！');
                        //alert('添加成功！');
                        getInterestClassList();
                    },
                    complete: function() {
                        hideAlert();
                    }
                });
            }
        } else {
            if (cousetype==2 && fteam==0 && steam==0) {
                alert("课程类型请填完整！");
            } else {
                alert("请填写年级属性！");
            }

        }
    });
}
function validateInterestClass() {
    if ($.trim($('.edit-interest-name').val()) == '') {
        $('.edit-interest-name').next().remove();
        $('.edit-interest-name').after('<span class="edit-error">请输入班级名称</span>');
    } else {
        $('.edit-interest-name').next().remove();
    }
    if ($('.edit-interest-number').val() == '' || $('.edit-interest-number').val() == 0) {
        $('.edit-interest-number').next().remove();
        $('.edit-interest-number').after('<span class="edit-error">请输入班级人数</span>');
    } else {
        $('.edit-interest-number').next().remove();
    }

    if($.trim($('#opentime').val())==""){
        $('#opentime').next().next().remove();
        $('#opentime').next().after('<span class="edit-error">请输入开放时间</span>');
    }else {
        $('#opentime').next().next().remove();
    }
    if($.trim($('#closetime').val())==""){
        $('#closetime').next().next().remove();
        $('#closetime').next().after('<span class="edit-error">请输入关闭时间</span>');
    }else {
        $('#closetime').next().next().remove();
    }

    var startTime = new Date($('#opentime').val());
    var closeTime = new Date($('#closetime').val());


    startTime = startTime.getTime();
    closeTime = closeTime.getTime();
    if($('#opentime').val()!=""  && $('#closetime').val()!=""){
        if (startTime > closeTime) {
            $('#closetime').next().next().remove();
            $('#closetime').next().after('<span class="edit-error">请输入正确的开放关闭时间</span>');
        } else {
            $('#closetime').next().next().remove();
        }
    }
}
function showEditInterestInfo(cid) {
    var selectTimeList = $('.select-time-lesson>li');
    $.ajax({
        url: '/myschool/oneinterest.do',
        type: 'post',
        dataType: 'json',
        data: {
            classId: cid
        },
        success: function(data) {
            console.log(data);
            var classInfo = data;
            var classtimeArray = classInfo.classTime;
            $('.edit-interest-name').val(classInfo.className);
            $('.edit-interest-room').val(classInfo.room);
            $('.add-interest-teacher').select2('val', classInfo.teacherId);
            $('.add-class-subject').val(classInfo.subjectId);
            $('.edit-interest-number').val(classInfo.totalStudentCount);
            if (classInfo.openTime!=null && classInfo.openTime!='') {
                $('#opentime').val(classInfo.openTime.replace(/-/g,'/').replace('T',' '));
            }
            if (classInfo.closeTime!=null && classInfo.closeTime!='') {
                $('#closetime').val(classInfo.closeTime.replace(/-/g,'/').replace('T',' '));
            }
            $('#content').val(classInfo.classContent);
            selectTimeList.each(function() {
                var t = $(this).attr('val');
                for (var i = 0; i < classtimeArray.length; i++) {
                    if (classtimeArray[i] == t) {
                        $(this).addClass('selected');
                        break;
                    }
                }
            });
            var gradeAry = data.gradeList;
            var checkList = $('.show-grade input:checkbox');
            for (var i in gradeAry) {
                for (var j = 0; j < checkList.length; j++) {
                    if (checkList.eq(j).attr('gid') == gradeAry[i]) {
                        checkList.eq(j).prop('checked', true);
                        break;
                    }
                }
            }
            var sex = classInfo.sex;
            var checkSexList = $('input[name=interest-sex]');
            for (var j = 0; j < checkSexList.length; j++) {
                    checkSexList.eq(j).prop('checked', false);
            }
            for (var j = 0; j < checkSexList.length; j++) {
                if (checkSexList.eq(j).attr('sexid') == sex) {
                    checkSexList.eq(j).prop('checked', true);
                    break;
                }
            }
        }
    });
}
function updateInterestClass(cid) {
    $('.edit-commit-btn').on('click', function() {
        var selectTimeList = $('.select-time-lesson>li');
        var content = '';
        var startTime = $('#opentime').val();
        var closeTime = $('#closetime').val();
        var classContent = $('#content').val();
        selectTimeList.each(function() {
            if ($(this).hasClass('selected')) {
                content += $(this).attr('val') + ',';
            }
        });
        var checkList = $('.edit-subject-selectgrade-interest input:checkbox');
        var grList = '';
        checkList.each(function() {
            if ($(this).is(':checked')) {
                if ($(this).attr('gid')) {
                    grList += $(this).attr('gid') + ',';
                }
            }
        });
        var sex = '';
        var checkSexList = $('input[name=interest-sex]');
        checkSexList.each(function() {
            if ($(this).is(':checked')) {
                sex = $(this).attr('sexid');;
            }
        });
        if (sex=='') {
            alert("请填写性别属性！");
            return;
        }
        if (grList != '') {
            validateInterestClass();
            if ($('.edit-interest').find('.edit-error').length == 0) {
                $.ajax({
                    url: '/myschool/upexpand.do',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        interestClassName: $.trim($('.edit-interest-name').val()),
                        teacherid: $('.add-interest-teacher').select2('val'),
                        studentCount: $('.edit-interest-number').val(),
                        classtime: content,
                        subjectid: $('.edit-interest .add-class-subject').val(),
                        typeId:sessionStorage.getItem("icid"),
                        classid: cid,
                        opentime: startTime,
                        closetime: closeTime,
                        gradeArry: grList,
                        classContent:classContent,
                        room:$.trim($('.edit-interest-room').val()),
                        sex:sex
                    },
                    success: function(data) {
                        if (data == true) {
                            alert('修改成功！');
                            getInterestClassList();
                            //$('#content').val('');
                        } else {
                            alert('修改失败1！');
                        }
                    },
                    error: function() {
                        alert('修改失败2！');
                    },
                    complete: function() {
                        hideAlert();
                    }
                });
            }
        } else {
            alert("请填写年级属性！");
        }
    });
}

function deleteInterestClass(cid, target) {
    if (confirm('确定要删除该拓展课吗？')) {
        $.ajax({
            url: '/myschool/delexpand.do',
            type: 'post',
            dataType: 'json',
            data: {
                classid: cid
            },
            success: function(data) {
                if (data == true) {
                    alert('删除成功');
                    target.parent().remove();
                } else {
                    alert('该班级下已经存在学生');
                }
            },
            error: function() {
                alert('删除失败');
            }
        });
    }
}
function getRadio() {
    if (document.getElementsByName("classtype")[0].checked) {
        $('#fteam').prop("checked", false);
        $('#steam').prop("checked", false);
    } else {
        $('#fteam').prop("checked", true);
        $('#steam').prop("checked", true);
    }
}