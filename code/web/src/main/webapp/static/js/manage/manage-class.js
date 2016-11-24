/**
 * Created by yan on 2015/4/22.
 */

$(function () {

    initHeight();
    getSchoolInfo();

    $('.class-list').show();
    var cid = $(this).parent().attr('cid');
    var gid = $("#content_main").attr("gradeId");
    getClassList(gid);
    $('.class-list').show();

    //新建班级
    $('.list-add-class').on('click',function(){
        getSelectTeacherList($('.edit-class-teacher'), function() {
            var gradeName=getUrlParam("gradeName");
            gradeName=decodeURI(gradeName);
            $('.edit-class-grade').text(gradeName);
            $('.edit-class-teacher').select2("destroy");
            initSelect($('.edit-class-teacher'));
            showEditModal($('.edit-class'));
            $('.edit-class-number').val('');
            addClassInfo(gid);
        });
    });

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
    // 双击班级进入班级详情
    $('body').on('click', '.class-list .list-content', function() {
        var cid = $(this).parent().attr('cid');
        var gid = $(this).parent().attr('gid');
        var gradeName=$('.list-title-grade').text();
        var className=$(this).attr('class-name');
        gradeName=encodeURI(encodeURI(gradeName));
        className=encodeURI(encodeURI(className));
        window.location.href='/myschool/managestudent/'+cid+"?gradeName="+gradeName+"&className="+className+"&gid="+gid;
    });
    //重置密码
    $("#compile-but").click(function(){
        $(".contacts-main").css("display","block");
        $("#bg").css("display","block");
    });
});

function getClassList(gid) {
    var target = $('.class-list .list-ul');
    $.ajax({
        url: '/myschool/classlist.do',
        type: 'POST',
        dataType: 'json',
        data: {
            gradeid: gid
        },
        success: function(data) {
            target.empty();
            var classList = data.rows;
            for (var i = 0; i < classList.length; i++) {
                var content = '';
                var teacherName = classList[i].mainTeacher == null ? '暂无' : classList[i].mainTeacher;
                content += '<li cid=' + classList[i].id + ' gid=' + gid + '><div class="list-content" class-name="'+classList[i].className +
                '"><span>' + $('.list-title-grade').text() + classList[i].className + '</span><span>班主任：</span>';
                content += '<span class="grade-manager-name">' + teacherName + '</span></div><i class="fa fa-pencil list-edit"></i>';
                content += '<i class="fa fa-trash-o list-delete"></i></li>';
                target.append(content);
            }
        }
    });
}

function addClassInfo(gid) {
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-class-number').val()) != '' ) {
            $.ajax({
                url: '/myschool/addclass.do',
                type: 'post',
                dataType: 'json',
                data: {
                    classname: $('.edit-class-number').val(),
                    teacherid: $('.edit-class-teacher').select2("val"),
                    gradeid: gid
                },
                success: function(data) {
                    if (data==true) {
                        alert('添加成功！');
                        getClassList(gid);
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
        case 'class':
            var classId = source.parent().attr('cid');
            if(confirm("确认删除该班级吗？")){
                deleteClassInfo(classId, source);
            }
            break;

    }
}
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}
function selectEditModal(belong, source) {
    switch (belong) {
        case 'class':
            var classId = source.parent().attr('cid');
            var gradeName=getUrlParam("gradeName");
            gradeName=decodeURI(gradeName);
            $('.edit-class-grade').text(gradeName);
            $('.edit-class .edit-title-bar').find('div').text('编辑班级');
            getSelectTeacherList($('.edit-class-teacher'), function() {
                $('.edit-class-teacher').select2("destroy");
                initSelect($('.edit-class-teacher'));
                var teacherId=showEditClassInfo(classId);
                var childs=$('.edit-class-teacher').children();
                for(var i=0;i<childs.length;i++){
                    if(childs[i].value==teacherId){
                        childs[i].selected='selected';
                    }
                }
                showEditModal($('.edit-class'));
            });
            editClass(classId);
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
function showEditClassInfo(cid) {
    var teacherId='';
    $.ajax({
        url: '/myschool/findclassinfo.do',
        type: 'post',
        dataType: 'json',
        async:false,
        data: {
            classId: cid
        },
        success: function(data) {
                var classInfo = data;
                $('.edit-class-number').val(classInfo.className);
                $('.edit-class-teacher').select2('val', classInfo.mainTeacherId);
                teacherId = classInfo.mainTeacherId;
        }
    });
    return teacherId;
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


function editClass(cid) {
    var gid = $("#content_main").attr("gradeId");
    $('.edit-commit-btn').on('click', function() {
        if ($.trim($('.edit-class-number').val()) != '') {
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
                target.parent().remove();
            }else{
                alert("请先删除班级下的老师和学生！");
            }
        }
    });
}

function gradeList(){
    window.location.href="/myschool/manageschool?version=57&tag=3";
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