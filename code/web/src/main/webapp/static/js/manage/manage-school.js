/**
 * Created by yan on 2015/4/22.
 */
// 添加科目
$(function () {
    initHeight();
    getGradeList();
    getSchoolInfo();
    //添加年级
    $('.list-add-grade').on('click', function() {
        $('.edit-grade .edit-title-bar').find('div').text('添加年级');
        $('.edit-grade').find('input').val('');
        $.ajax({
            url: '/myschool/schoolinfo.do',
            type: 'post',
            dataType: 'json',
            data: {},
            success: function(data) {
                //var schoolInfo = data;
                $('.edit-grade-value').empty();
                var content = '';
                if(isPrimary(data.schoolTypeInt)){
                    for (var i = 1; i <= 6; i++) {
                        content += '<option value=' +i+ '>' + i + '年级</option>';
                    }
                }
                if (isJunior(data.schoolTypeInt)){
                    for (var i = 7; i <=9; i++) {
                        content += '<option value=' +i+ '>' + i + '年级</option>';
                    }
                }
                if (isSenior(data.schoolTypeInt)){
                    for (var i = 10; i <= 12; i++) {
                        content += '<option value=' +i+ '>' + i + '年级</option>';
                    }
                }
                $('.edit-grade-value').append(content);
                if(content==''){
                    alert("请先选择学校学段！");
                    showAlert();
                    editSchoolInfo();
                }else {
                    getSelectTeacherList($('.edit-grade-teacher'), function() {
                        $('.edit-grade-teacher').select2("destroy");
                        initSelect($('.edit-grade-teacher'));
                        showEditModal($('.edit-grade'));
                    });
                    getSelectTeacherList($('.edit-class-teacher'), function() {
                        $('.edit-class-teacher').select2("destroy");
                        initSelect($('.edit-class-teacher'));
                        showEditModal($('.edit-grade'));
                    });
                    addGradeInfo();
                }
            },
            error: function() {
            }
        });
    });

    $('.grade-list').show();

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

    // 双击年级进入班级
    $('body').on('click', '.grade-list .list-content', function() {
        var gid = $(this).parent().attr('gid');
        var gradeName = $(this).parent().attr('gradename');
        gradeName=encodeURI(encodeURI(gradeName));
        window.location.href='/myschool/manageclass/'+gid+"?gradeName="+gradeName;
    });
    //重置密码
    $("#compile-but").click(function(){
        $(".contacts-main").css("display","block");
        $("#bg").css("display","block");
    });

});

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
        case 'grade':
            var gradeId = source.parent().attr('gid');
            deleteGrade(gradeId, source);
            break;

    }
}
function selectEditModal(belong, source) {
    switch (belong) {
        case 'grade':
            var gradeId = source.parent().attr('gid');
            var leader=source.parent().attr('id');
            var cleader=source.parent().attr('cid');
            var level=source.parent().attr('level');
            var gradeName=source.parent().attr('gradeName');
            $('.edit-grade .edit-title-bar').find('div').text('编辑年级');
            getSelectTeacherList($('.edit-grade-teacher'), function() {
                //年级属性控制
                getSelectGradeList(level);
                //年级组长控制
                var childs=$('.edit-grade-teacher').children();
                for(var i=0;i<childs.length;i++){
                    if(childs[i].value==leader){
                        childs[i].selected='selected';
                    }
                }
                $('.edit-grade-teacher').select2("destroy");
                initSelect($('.edit-grade-teacher'));
                showEditModal($('.edit-grade'));
            });

            getSelectTeacherList($('.edit-class-teacher'), function() {
                //年级属性控制
                getSelectGradeList(level);
                //备课组长控制
                var clschilds=$('.edit-class-teacher').children();
                for(var i=0;i<clschilds.length;i++){
                    if(clschilds[i].value==cleader){
                        clschilds[i].selected='selected';
                    }
                }
                $('.edit-class-teacher').select2("destroy");
                initSelect($('.edit-class-teacher'));
                showEditModal($('.edit-grade'));
            });
            $('.edit-grade-name').val(gradeName);
            editGrade(gradeId);
            break;
    }
}
function editGrade(gid) {
    $('.edit-commit-btn').on('click', function() {
        //if($(".edit-grade-teacher").select2("val")==0)
        //{
        //    alert("请选择年级组长");
        //    return;
        //}
        //if($(".edit-class-teacher").select2("val")==0)
        //{
        //    alert("请选择备课组长");
        //    return;
        //}
        if ($.trim($('.edit-grade-name').val()) != '') {
            $.ajax({
                url: '/myschool/upgrade.do',
                type: 'post',
                dataType: 'json',
                data: {
                    gradeid: gid,
                    teacherid: $(".edit-grade-teacher").select2("val"),
                    cteacherid: $(".edit-class-teacher").select2("val"),
                    gradeLevel: $('.edit-grade-value').val(),
                    gradename: $('.edit-grade-name').val()
                },
                success: function(data) {
                    if (data == true) {
                        alert('修改成功！');
                        getGradeList();
                    } else {
                        alert('修改失败！');
                    }
                },
                error: function() {
                    console.log('updateGradeInfo error');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请输入年级名称！');
        }
    });
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
    $.ajax({
        url: '/myschool/schoolinfo.do',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            $('.edit-grade-value').empty();
            var content = '';
            if(isPrimary(data.schoolTypeInt)){
                for (var i = 1; i <= 6; i++) {
                    content+=makeOption(level,i);
                }
            }
            if (isJunior(data.schoolTypeInt)){
                for (var i = 7; i <=9; i++) {
                    content+=makeOption(level,i);
                }
            }
            if (isSenior(data.schoolTypeInt)){
                for (var i = 10; i <= 12; i++) {
                    content+=makeOption(level,i);
                }
            }
            $('.edit-grade-value').append(content);
            if(content==''){
                alert("请先选择学校学段！");
                showAlert();
                editSchoolInfo();
            }
        },
        error: function() {
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
function addGradeInfo() {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-grade-name').val()) != '') {
            $.ajax({
                url: '/myschool/addgrade.do',
                type: 'post',
                dataType: 'json',
                data: {
                    teacherid: $(".edit-grade-teacher").select2("val"),
                    cteacherid:$(".edit-class-teacher").select2("val"),
                    currentgradeid: $('.edit-grade-value').val(),
                    gradename: $('.edit-grade-name').val()
                },
                success: function(data) {
                    if (data == true) {
                        alert('添加成功！');
                        getGradeList();
                    } else {
                        alert('添加失败！');
                    }
                },
                error: function() {
                    console.log('addGradeInfo error');
                },
                complete: function() {
                    hideAlert();
                }
            });
        } else {
            alert('请输入年级名称！');
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
function getGradeList() {
    $.ajax({
        url: '/myschool/gradelist.do',
        type: 'post',
        dataType: 'json',
        data: {},
        success: function(data) {
            var row = data.rows;
            var target = $('.grade-list>.list-ul');
            target.empty();
            for (var i = 0; i < row.length; i++) {
                var content = '';
                var tname = row[i].leaderName == null ? '暂无' : row[i].leaderName;
                var cname = row[i].cleaderName == null ? '暂无' : row[i].cleaderName;
                content += '<li gid=' + row[i].id + ' level="'+row[i].gradeType+'" id="'+row[i].leader+'" cid="'+row[i].cleader+'"  gradeName='+row[i].name+'><div class="list-content"><span>' + row[i].name + '</span>';
                content += '<span>年级组长：</span>';
                content += '<span class="grade-manager-name">' + tname + '</span>';
                content += '<span>备课组长：</span>';
                content += '<span class="grade-manager-name">' + cname + '</span></div>';
                content += '<i class="fa fa-pencil list-edit"></i><i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }
        },
        error: function() {
            console.log('error');
        }
    });
}
function deleteGrade(gid, target) {
    $.ajax({
        url: '/myschool/delgrade.do',
        type: 'post',
        dataType: 'json',
        data: {
            gradeid: gid
        },
        success: function(data) {
            var code=data.code;
            var row = data.classList;
            if (code==1) {
                alert("请先删除年级下所有班级！");
            } else {
                alert('删除 成功！');
                getGradeList();
            }
        }
    });
}
function showEditGradeInfo(gid) {
    $.ajax({
        url: '/reverse/selectSingleGradeInfo.action',
        type: 'post',
        dataType: 'json',
        data: {
            gradeid: gid
        },
        success: function(data) {
            var info = data.gradesInfo;
            $('.edit-grade-name').val(info.gradeName);
            $('.edit-grade-value').val(info.gradeId);
            $(".edit-grade-teacher").select2("val", info.teacherid);
        },
        error: function() {
            console.log('selectSingleGradeInfo error');
        }
    });
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






