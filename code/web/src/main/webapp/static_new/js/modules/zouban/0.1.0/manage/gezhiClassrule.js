/**
 * Created by qiangm on 2015/10/20.
 */
'use strict';
define(['doT', 'common', 'jquery'],function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var classrule = {},
        Common = require('common');
    var classruleData = {};
    classruleData.type = 1;
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    classrule.init = function () {


        $(".right-main1").hide();
        $(".update-X").click(function(){
            $(".bg").hide();
            $(".gezhiConf").hide();
            $(".adjust-CUR").hide();
            $(".section-CUR").hide();
            $(".ranking-CUR").hide();
            $(".Sschedule-CUR").hide();
            $(".batch-CUR").hide();
            $(".tishi-CUR").hide();
        });
        $(".backUrl").click(
            function () {
                window.open('../paike/index.do?version=58&year='+ encodeURI(encodeURI($('#termShow').text()))+'&gradeId='+
                $('#gradeShow').attr("gid"),'_self');
            });
        $(".adjust-bottom-QX").click(function(){
            $(".bg").hide();
            $(".adjust-CUR").hide();
        });

        $(".rules-ZD").click(function(){
            $('#grade2').html($("#gradeShow").text());
            //$('#examlist').attr("disabled",true);
            //document.getElementById('findCJ').style.display="none";
            $(".bg").show();
            document.getElementById('error').style.display="none";
            $('#examlist2').attr("disabled",false);
            $(".section-CUR").show();
            classrule.loadExam();
        });

        $(".section-CUR-QX").click(function(){
            $(".bg").hide();
            $(".section-CUR").hide();
        });

        $(".ranking-CUR-QX").click(function(){
            $(".bg").hide();
            $(".ranking-CUR").hide();
            $(".section-CUR").hide();
        });
        $('body').on('change', '#fenduanlist2', function () {
            classrule.findBianBanList();
        });
        $('body').on('change', '#subjectlist', function () {
            classrule.findBianBanList();
        });
        $(".rules-CKFD").click(function(){
            $(".tab-rules").hide();
            $(".tab-variable").show();
            $('#term3').html($("#termShow").text());
            $('#grade3').html($("#gradeShow").text());
            classrule.findXuanKeDetail();
        });

        $(".Sschedule-CUR-BU").click(function(){
            $(".Sschedule-CUR").hide();
            $(".bg").hide();
        });
        $(".tab-rules-LI").click(function(){
            $(".tab-li-I").show();
            $(".tab-li-II").hide();
            $(".tab-li-III").hide();
            $(".tab-rules-VII").addClass('rules-LII');
            $(".tab-rules-VI").addClass('rules-LII');
            $(".tab-rules-V").addClass('rules-LII');
            $(".sfd-dd").show();
            $(".tab-DT-I").show();
            $(".tab-DT-II").hide();
            $(".right-main1").hide();
            $(".right-main-stu").hide();
            $(".right-main-tea").hide();
            $(".right-main-room").hide();
            $(this).addClass('rules-LI');
            $(this).removeClass('rules-LII');
            $(".tab-rules-LII").addClass('rules-LII');
            $(".tab-rules-LIII").addClass('rules-LII');
            $(".tab-rules-LIIII").addClass('rules-LII');

            classrule.findFengDuan();
        });
        $(".tab-rules-LII").click(function(){
            $(this).addClass('rules-LI');
            $(this).removeClass('rules-LII');
            $(".tab-rules-LI").addClass('rules-LII');
            $(".tab-rules-LIII").addClass('rules-LII');
            $(".tab-rules-LIIII").addClass('rules-LII');
            $(".tab-rules-VII").addClass('rules-LII');
            $(".tab-rules-VI").addClass('rules-LII');
            $(".tab-rules-V").addClass('rules-LII');
            $(".tab-li-I").hide();
            $(".tab-li-II").show();
            $(".tab-li-III").hide();
            $(".sfd-dd").hide();
            $(".tab-DT-I").hide();
            $(".tab-DT-II").show();
            $(".fdh-tab").hide();
            $(".right-main1").hide();
            $(".right-main-stu").hide();
            $(".right-main-room").hide();
            classrule.findSubjectConfCount();
        });
        $('.batch-CUR-QR').click(function(){
            classrule.updateSubjectConfCount();
        });
        $(".tab-rules-LIII").click(function(){
            $(this).addClass('rules-LI');
            $(this).removeClass('rules-LII');
            $(".tab-rules-LI").addClass('rules-LII');
            $(".tab-rules-LII").addClass('rules-LII');
            $(".tab-rules-LIIII").addClass('rules-LII');
            $(".tab-li-I").hide();
            $(".tab-li-II").hide();
            $(".tab-li-III").show();
            $(".tab-DT-I").hide();
            $(".tab-DT-II").show();
            $(".right-main1").hide();
            $(".fdh-tab").hide();
            $(".sfd-dd").hide();
            classrule.findFenCengList();
        });
        $(".tab-rules-LIIII").click(function(){
            if(classruleState>=2) {
                $(this).addClass('rules-LI');
                $(this).removeClass('rules-LII');
                $(".tab-rules-LI").addClass('rules-LII');
                $(".tab-rules-LII").addClass('rules-LII');
                $(".tab-rules-LIII").addClass('rules-LII');
                $(".tab-rules-VII").addClass('rules-LII');
                $(".tab-rules-VI").addClass('rules-LII');
                $(".tab-rules-V").addClass('rules-LII');

                $(".tab-li-I").hide();
                $(".tab-li-II").hide();
                $(".tab-li-III").hide();
                $(".tab-DT-I").hide();
                $(".sfd-dd").hide();
                $(".fdh-tab").hide();
                $(".tab-DT-II").show();
                $(".right-main1").show();
                $(".right-main-stu").hide();
                $(".right-main-tea").hide();
                $(".right-main-room").hide();
                classrule.findBianBanList();
            }
        });


        $("#autoBianBan").click(function(){
            $(".bg").show();
            $(".tishi-CUR").show();
            //classrule.addAutoBianBan();
        });
        $(".curri-TJ").click(function(){
            $(".bg").hide();
            $(".tishi-CUR").hide();
            classrule.addAutoBianBan();
        });
        $(".curri-QX").click(function(){
            $(".bg").hide();
            $(".tishi-CUR").hide();
        });

        $(".tab-li-II-PL").click(function(){
            $(".bg").show();
            $(".batch-CUR").show();
        });
        $(".batch-CUR-QX").click(function(){
            $(".bg").hide();
            $(".batch-CUR").hide();
        });
        $("#duanchoose2").click(function(){
            if($('#duanchoose2').is(':checked')) {
                $('#examlist').attr("disabled",false);
                $('#examlist2').attr("disabled",true);
                $('#mincnt').attr("disabled",true);
                $('#maxcnt').attr("disabled",true);
                $('#mincnt').val('');
                $('#maxcnt').val('');
            }
        });
        $("#duanchoose1").click(function(){
            if($('#duanchoose1').is(':checked')) {
                $('#examlist').attr("disabled",true);
                $('#examlist2').attr("disabled",false);
                $('#mincnt').attr("disabled",false);
                $('#maxcnt').attr("disabled",false);
            }
        });
        classrule.findFengDuan();
        classrule.getgroupSubjectSelect();
        $(".section-CUR-QR").click(function(){
            classrule.autoFengDuan();
        });


        $('body').on('change', '#fdlist', function () {
            classrule.findXuanKeDetail();
        });

        $('.regulation-SX').click(function(){
            classrule.findClassStudentDetailList();
        });
        $('#backBtn2').click(function(){
            $(".tab-rules").hide();
            $(".tab-variable").show();
            $(".tab-regulation").hide();
            $('#term3').html($("#termShow").text());
            $('#grade3').html($("#gradeShow").text());
            classrule.findXuanKeDetail();
        });

        $('#choose').click(function(){
            if ($('#classlist  input:checkbox').is(':checked')) {
                $('#classlist input:checkbox').prop('checked', false);
            } else {
                $('#classlist input:checkbox').prop('checked', true);
            }
        });
        $('#choose2').click(function(){
            if ($('#classlist2  input:checkbox').is(':checked')) {
                $('#classlist2 input:checkbox').prop('checked', false);
            } else {
                $('#classlist2 input:checkbox').prop('checked', true);
            }
        });

        $('#add').click(function(){
            $(".adjust-main-left input[name='checkbox']:checked").each(function () {
                $(this).parent().remove();
                $('#classlist2').append($(this).parent());
                $('#classlist2 input:checkbox').prop('checked', false);
                for(var i=0;i<fengDuanValue.length;i++)
                {
                    if(fengDuanValue[i].classId==$(this).attr("cid"))
                    {
                        fengDuanValue[i].groupId=$("#duanSelect").val();
                        fengDuanValue[i].group=$("#duanSelect option:selected").text().substring(1,2);
                        break;
                    }
                }
                $(this).parent().find("em").eq(1).text($("#duanSelect option:selected").text());
            });
        });
        $('#remove').click(function(){
            $(".adjust-main-right input[name='checkbox']:checked").each(function () {
                $(this).parent().remove();
                $('#classlist').append($(this).parent());
                $('#classlist input:checkbox').prop('checked', false);
                for(var i=0;i<fengDuanValue.length;i++)
                {
                    if(fengDuanValue[i].classId==$(this).attr("cid"))
                    {
                        fengDuanValue[i].groupId="";
                        fengDuanValue[i].group=0;
                        break;
                    }
                }
                $(this).parent().find("em").eq(1).text("第0段");
            });
        });
        $('#finish').click(function(){
            classrule.setZoubanState();
            window.location.href='/zouban/bianban.do?version=58&term='+$('#termShow').text()+'&gid='+$('#gradeShow').attr('gid')+'&gnm='+ $('#gradeShow').text();
        });
        $('#next').click(function(){
            $(".tab-li-I").hide();
            $(".tab-li-II").show();
            $(".tab-li-III").hide();
            $(".tab-DT-I").hide();
            $(".tab-DT-II").show();
            classrule.findSubjectConfCount();
        });
        $('#nextto').click(function(){
            $(".tab-li-I").hide();
            $(".tab-li-II").hide();
            $(".tab-li-III").show();
            $(".tab-DT-I").hide();
            $(".tab-DT-II").show();
            classrule.findFenCengList();
        });

        $(".back-btn").click(
            function () {
                $(".tab-col").show();
                $(".right-main2").hide();
                $(".right-main3").hide();
            }
        );
        var users = [];
        $('.ex-arrow1').click(function(){
            users = [];
            classruleData.courseClassId = $('#classlist5').val();
            classruleData.index = 0;
            $(".main3-sel1 input[name='checkbox']:checked").each(function () {
                users.push(this.value);
            });
            classruleData.studentArgs = users;
            classrule.updateClassCourseUserInfo(1);
        });
        $('.ex-arrow2').click(function(){
            users = [];
            classruleData.courseClassId = $('#classlist5').val();
            classruleData.index = 1;
            $(".main3-sel2 input[name='checkbox']:checked").each(function () {
                users.push(this.value);
            });
            classruleData.studentArgs = users;
            classrule.updateClassCourseUserInfo(2);
        });
        $('.select-all').click(function(){
            if ($('#userlist5  input:checkbox').is(':checked')) {
                $('#userlist5 input:checkbox').prop('checked', false);
            } else {
                $('#userlist5 input:checkbox').prop('checked', true);
            }
        });

    };

    $(document).ready(function(){
       $("body").on("click","#backBtn",function(){
          $(".tab-variable").css("display","none");
           $(".tab-rules").css("display","block");
       });
    });
    classrule.findFengDuan = function() {
            classruleData.team = $("#termShow").text();
            classruleData.gradeId = $("#gradeShow").attr('gid');
        Common.getPostData('/bianban/findFengDuan.do', classruleData,function(rep){
            $('#xuankeid').val(rep.xuankeId);
            $('#rules-ZD').html(rep.misscount+'/'+rep.classcount);
            $("#allClass").empty();
            Common.render({tmpl: $('#allClassTempJs'), data: {data: rep.fengduanDTOList2}, context: '#allClass'});
            if (rep.fenduans!=null && rep.fenduans.length!=0) {
                $('.fdh-tab').show();
                $("#duanlist").empty();
                Common.render({tmpl: $('#duanlistTempJs'), data: {data: rep.fenduans}, context: '#duanlist'});
            }
            $('#fdlist').empty();
            Common.render({tmpl: $('#fdlistTempJs'), data: {data: rep.fengduanList}, context: '#fdlist'});
            $('#fdlist2').empty();
            Common.render({tmpl: $('#fdlistTempJs2'), data: {data: rep.fengduanList}, context: '#fdlist2'});
            $('#duanSelect').empty();
            Common.render({tmpl: $('#duanSelectTemJs'), data: {data: rep.fengduanList}, context: '#duanSelect'});

            $(".tab-rules-TZ").click(function(){
                $('#grade5').html($("#gradeShow").text());
                $('#duan5').html($(this).parent().children().eq(0).text());
                classruleData.groupId = $(this).attr('fdid');
                classrule.findClassList();
                $("#duanSelect").val(classruleData.groupId);
                $(".bg").show();
                $(".adjust-CUR").show();
                $('.adjust-bottom-BU').click(function(){
                    //先检查是否调整完毕
                    for(var i=0;i<fengDuanValue.length;i++)
                    {
                        if(fengDuanValue[i].groupId=="")
                        {
                            alert(fengDuanValue[i].className+"未分段");
                            return;
                        }
                    }
                    $.ajax({
                        url:"/bianban/updateClassGroups2.do",
                        type:"post",
                        contentType:"application/json",
                        data:JSON.stringify(fengDuanValue),
                        success:function(rep)
                        {
                            if (rep.flg) {
                                alert("调班成功！");
                                $(".bg").hide();
                                $(".adjust-CUR").hide();
                                classrule.findFengDuan();
                            } else {
                                alert("调班失败！");
                            }
                        }
                    })
                });
                $("#duanSelect").change(function(){
                    //classruleData.groupId = $("#duanSelect").val();
                    //classrule.findClassList();
                    var groupId=$("#duanSelect").val();
                    var allList=[];
                    var duanList=[];
                    for(var i=0;i<fengDuanValue.length;i++)
                    {
                        if(fengDuanValue[i].groupId==groupId)
                        {
                            duanList.push(fengDuanValue[i]);
                        }
                        else{
                            allList.push(fengDuanValue[i]);
                        }
                    }
                    $("#classlist").empty();
                    Common.render({tmpl: $('#classlistTemJs'), data: {data: allList}, context: '#classlist'});
                    $("#classlist2").empty();
                    Common.render({tmpl: $('#classlistTemJs2'), data: {data: duanList}, context: '#classlist2'});
                });
            });
        });
    }
    var fengDuanValue=[];
    classrule.addAutoBianBan = function() {
        classruleData.term = $('#termShow').text();
        classruleData.gradeId = $("#gradeShow").attr('gid');
        classruleData.type = 1;
        Common.getPostData('/bianban/addAutoBianBan.do', classruleData,function(rep){
            if (rep.flg) {
                alert("自动编班成功！");
                classrule.setZoubanState();
                classrule.findBianBanList();
            } else {
                alert("自动编班失败！");
            }

        });
    };
    classrule.findClassSubjectCount = function() {

    }
    classrule.autoFengDuan = function() {
        document.getElementById('error').style.display="none";
        classruleData.xuankeId = $("#xuankeid").val();
        classruleData.gradeId = $("#gradeShow").attr('gid');
        classruleData.count = $("#count").val();
        classruleData.clscnt = $('.se2').val();
        classruleData.term = $("#termShow").text();
        if($('#duanchoose2').is(':checked')) {
            classruleData.examType = 1;
            classruleData.examId = $('#examlist').val();
            classruleData.mincnt = 0;
            classruleData.maxcnt = 0;
        } else {
            classruleData.examType = 0;
            classruleData.examId = '';
            //classruleData.examId = $('#examlist2').val();
            //classruleData.mincnt = $('#mincnt').val();
            //classruleData.maxcnt = $('#maxcnt').val();
            classruleData.mincnt = 0;
            classruleData.maxcnt = 60;
        }
        /*if (!$('#duanchoose2').is(':checked') && ($.trim($('#mincnt').val()) == ''|| $('#mincnt').val()== 0 || $.trim($('#maxcnt').val()) == ''|| $('#maxcnt').val()== 0)) {
            document.getElementById('error').style.display="block";
        } else {*/
            document.getElementById('error').style.display="none";
            Common.getPostData('/bianban/autoFengDuan.do', classruleData,function(rep){
                if (rep.flg) {
                    $(".bg").hide();
                    $(".section-CUR").hide();
                    classrule.findFengDuan();
                    classruleState=2;
                } else {
                    alert("自动分段失败，请重新设置！");
                }
            });
        //}
    };




    classrule.loadExam = function() {
        classruleData.gradeId = $("#gradeShow").attr('gid');
        classruleData.page = 1;
        Common.getPostData('/exam1/loadExam.do', classruleData,function(rep){
            $("#examlist").empty();
            Common.render({tmpl: $('#examlistTempJs'), data: {data: rep.message.list}, context: '#examlist'});
            $("#examlist2").empty();
            Common.render({tmpl: $('#examlistTempJs2'), data: {data: rep.message.list}, context: '#examlist2'});
        });
    }

    classrule.findXuanKeDetail = function() {
        classruleData.groupId=$('#fdlist').val();
        Common.getPostData('/bianban/findXuanKeDetail.do', classruleData,function(rep){
            $("#couselist").empty();
            Common.render({tmpl: $('#couselistTempJs'), data: {data: rep.details}, context: '#couselist'});
            $("#courselist2").empty();
            Common.render({tmpl: $('#courselistTempJs2'), data: {data: rep.details}, context: '#courselist2'});
            $('#duan').html($('#fdlist').find("option:selected").text());
            $(".tab-variable-MX").click(function(){
                $('#term4').html($("#termShow").text());
                $('#grade4').html($("#gradeShow").text());
                $('#courselist2').val($(this).attr('scid'));
                $('#fdlist2').val($('#fdlist').val());
                classrule.findClassStudentDetailList();
                $(".tab-variable").hide();
                $(".tab-regulation").show();
            });
        });
    }

    classrule.updateClassCourseUserInfo = function(type) {
        Common.getPostData('/bianban/updateClassCourseUserInfo.do', classruleData,function(rep){
            if (rep.flg) {
                alert("换班成功！");
                if (type==1) {
                    $(".main3-sel1 input[name='checkbox']:checked").each(function () {
                        $(this).parent().remove();
                        $('#userlist6').append($(this).parent());
                        $('#userlist6 input:checkbox').prop('checked', false);
                    });
                } else {
                    $(".main3-sel2 input[name='checkbox']:checked").each(function () {
                        $(this).parent().remove();
                        $('#userlist5').append($(this).parent());
                        $('#userlist5 input:checkbox').prop('checked', false);
                    });
                }

            } else {
                alert("换班失败！");
            }
        });
    }
    classrule.findBianBanList = function() {
        classruleData.term = $("#termShow").text();
        classruleData.gradeId = $("#gradeShow").attr('gid');
        classruleData.groupId = $("#fenduanlist2").val();
        classruleData.subjectId = $("#subjectlist").val();
        Common.getPostData('/bianban/findBianBanList.do', classruleData,function(rep){
            $("#courseclslist").empty();
            Common.render({tmpl: $('#courseclslistTempJs'), data: {data: rep.rows}, context: '#courseclslist'});
            $(".tab-check").click(function () {
                    $('#term6').html($('#termShow').text());
                    $('#grade6').html($('#gradeShow').text());
                    //bianban.getgroupSubjectSelect();
                    $(".tab-col").hide();
                    $(".right-main2").show();
                    classruleData.courseClassId = $(this).attr('couid');
                    classrule.findClassStudentList(1);
                }
            );
            $(".peo-set").click(function () {
                    classruleData.courseClassId = $(this).attr('couid');
                    classrule.findCourseById();
                    $('#term7').html($('#termShow').text());
                    $('#grade7').html($('#gradeShow').text());
                    $('#duan7').html('第'+$(this).attr('gp')+'段');
                    $('#cousename7').html($(this).attr('cn'));
                    classruleData.orgcourseClassId = $(this).attr('couid');
                    $(".main3-sel2 input[name='checkbox']").each(function () {
                        $(this).parent().remove();
                    });
                    $(".tab-col").hide();
                    $(".right-main3").show();
                    classrule.findClassStudentList(2);
                    classruleData.courseClassId = $('#classlist5').val();
                    classrule.findClassStudentList(3);
                }
            );

        });
    };
    classrule.findCourseById = function() {
        Common.getPostData('/bianban/findCourseById.do', classruleData,function(rep){
            if(rep!=null && rep.length!=0) {
                $("#classlist5").empty();
                Common.render({tmpl: $('#classlistTempJs5'), data: {data: rep}, context: '#classlist5'});
            } else {
                alert("只有一个班，不可调班！");
            }

        });
    };
    classrule.findClassStudentList = function(type) {
        Common.getPostData('/bianban/findClassStudentList.do', classruleData,function(rep){
            if (type==1) {
                $("#coursename2").html(rep.rows.className);
                $("#teachername").html(rep.rows.teacherName);
                $("#classroom").html(rep.rows.classRoom);
                $("#usercnt").html(rep.rows.count);
                $("#userlist").empty();
                Common.render({tmpl: $('#userlistTempJs'), data: {data: rep.rows.studentCengJiDTOs}, context: '#userlist'});
            } else if(type==2) {
                $("#cnt5").html(rep.rows.count);
                $("#teachername5").html(rep.rows.teacherName);
                $("#classromm5").html(rep.rows.classRoom);
                $("#userlist5").empty();
                Common.render({tmpl: $('#userlistTempJs5'), data: {data: rep.rows.studentCengJiDTOs}, context: '#userlist5'});
            } else if (type==3) {
                $("#cnt6").html(rep.rows.count);
                $("#teachername6").html(rep.rows.teacherName);
                $("#classromm6").html(rep.rows.classRoom);
            }

        });
    };
    classrule.getgroupSubjectSelect = function() {
        classruleData.term = $("#termShow").text();
        classruleData.gradeId = $("#gradeShow").attr('gid');
        Common.getPostData('/bianban/getgroupSubjectSelect.do', classruleData,function(rep){
            $('#xuankeid').val(rep.xuankeId);
            $("#fenduanlist2").empty();
            Common.render({tmpl: $('#fenduanlistTempJs'), data: {data: rep.classFendDuanList}, context: '#fenduanlist2'});
            $("#subjectlist").empty();
            Common.render({tmpl: $('#subjectlistTempJs'), data: {data: rep.subjectlist}, context: '#subjectlist'});

            //设置老师
            $("#fenduanlist2-tea").empty();
            Common.render({tmpl: $('#fenduanlistTempJs'), data: {data: rep.classFendDuanList}, context: '#fenduanlist2-tea'});
            $("#subjectlist-tea").empty();
            Common.render({tmpl: $('#subjectlistTempJs'), data: {data: rep.subjectlist}, context: '#subjectlist-tea'});

            //设置学生
            $("#fenduanlist2-stu").empty();
            Common.render({tmpl: $('#fenduanlistTempJs'), data: {data: rep.classFendDuanList}, context: '#fenduanlist2-stu'});
            $("#subjectlist-stu").empty();
            Common.render({tmpl: $('#subjectlistTempJs'), data: {data: rep.subjectlist}, context: '#subjectlist-stu'});

            //设置教室
            $("#fenduanlist2-room").empty();
            Common.render({tmpl: $('#fenduanlistTempJs'), data: {data: rep.classFendDuanList}, context: '#fenduanlist2-room'});
            $("#subjectlist-room").empty();
            Common.render({tmpl: $('#subjectlistTempJs'), data: {data: rep.subjectlist}, context: '#subjectlist-room'});
            classrule.findBianBanList();
        });
    };

    classrule.findSubjectConfCount = function() {
        classruleData.team = $("#termShow").text();
        classruleData.gradeId = $("#gradeShow").attr('gid');
        Common.getPostData('/bianban/findSubjectConfCount.do', classruleData,function(rep){
            $("#coursecountlist").empty();
            Common.render({tmpl: $('#coursecountlistTempJs'), data: {data: rep.rows}, context: '#coursecountlist'});
            $(".stucnt1").blur(function(){
                classruleData.choose = 1;
                classruleData.type = $(this).parent().parent().attr('tp');
                classruleData.count = $(this).val();
                classruleData.subjectConfId = $(this).parent().parent().attr('scid');
                classrule.updateSubjectConfSingleCount();
                //classrule.updateSubjectConfCount();
            });
            $(".stucnt2").blur(function(){
                classruleData.choose = 2;
                classruleData.type = $(this).parent().parent().attr('tp');
                classruleData.count = $(this).val();
                classruleData.subjectConfId = $(this).parent().parent().attr('scid');
                classrule.updateSubjectConfSingleCount();
                //classrule.updateSubjectConfCount();
            });
        });
    }

    classrule.updateSubjectConfSingleCount = function() {
        Common.getPostData('/bianban/updateSubjectConfSingleCount.do', classruleData,function(rep){
            if (rep.flg) {
                $(".bg").hide();
                $(".batch-CUR").hide();
                alert("修改成功！");
                classrule.findSubjectConfCount();
            } else {
                alert("修改失败！");
            }
        });
    }

    classrule.updateSubjectConfCount = function() {
        classruleData.xuankeId = $('#xuankeid').val();
        classruleData.max = $('#maxcount').val();
        classruleData.min = $('#mincount').val();
        Common.getPostData('/bianban/updateSubjectConfCount.do', classruleData,function(rep){
            if (rep.flg) {
                $(".bg").hide();
                $(".batch-CUR").hide();
                classrule.findSubjectConfCount();
            } else {
                alert("批量修改失败！");
            }
        });
    }

    classrule.findFenCengList = function() {
        classruleData.xuankeId = $('#xuankeid').val();
        //classruleData.groupId = $('#fcfdlist').val();
        Common.getPostData('/bianban/findFenCengList.do', classruleData,function(rep){
            $("#fencenglist").empty();
            Common.render({tmpl: $('#fencenglistTempJs'), data: {data: rep.rows}, context: '#fencenglist'});
        });
    }

    classrule.findFenCeng = function() {
        classruleData.xuankeId = $('#xuankeid').val();
        Common.getPostData('/bianban/findFenCengDetail.do', classruleData,function(rep) {
            for (var i = 0; i < rep.length; i++) {
                ZbFenCengDTO.fencengs.push(rep[i].ceng);
                ZbFenCengDTO.scores.push(rep[i].score);
                var userIds="";
                for(var j=0;j<rep[i].userIds.length;j++)
                {
                    userIds+=rep[i].userIds[j]+",";
                }
                ZbFenCengDTO.users.push(userIds.substring(0,userIds.length-1));
            }
            //显示在页面
            indexList=[];
            $("#fencengResultShow").empty();
            Common.render({tmpl: $('#fencengResultTempJs'), data: {data: ZbFenCengDTO}, context: '#fencengResultShow'});
            var base=0;
            for (var i = 0; i < rep.length-1; i++) {
                var count=rep[i].count+base;
                base=rep[i].count;
                $(".set-fenceng").each(function () {
                    if (Number($(this).attr("index")) ==count-1) {
                        $(this).css("background","green");
                        $(this).parent().css("border-bottom","solid 3px #F00");
                        indexList.push(Number($(this).attr("index")));
                    }
                    }
                );
            }
        });
    }

    classrule.addFenCeng = function() {
        Common.getPostData('/bianban/addFenCeng.do', classruleData,function(rep){

        });
    }

    classrule.init();
    //==============================================================
    $(document).ready(function(){
        $("body").on("click",".setting",function()
        {
            $('#groups').html($(this).attr("duan"));
            $('#coursename').html($(this).parent().children().eq(1).text());
            $(".tab-li-III").hide();
            $(".tab-rules").hide();
            $(".tab-slice").show();
            var scid=$(this).attr("scid");
            var gid=$(this).attr("gid");
            var ctype=$(this).attr("ctype");
            classrule.checkClassCount(gid,scid);
            classrule.getFenceng(gid,scid,ctype);
            indexList=[];
            ZbFenCengDTO.xuanKeId=$('#xuankeid').val();
            ZbFenCengDTO.groupId=gid;
            ZbFenCengDTO.subjectConfId=scid;
            ZbFenCengDTO.type=ctype;
            classrule.findFenCeng();
        });
        $("body").on("click",".back-slice",function()
        {
            $(".tab-rules").show();
            $(".tab-li-III").show();
            $(".tab-slice").hide();
            ZbFenCengDTO= {
                xuanKeId: "",
                groupId: "",
                subjectConfId: "",
                type: "",
                fencengs: [],
                scores: [],
                users: []
            };
            indexList=[];
        });
        $("body").on("click","#exportCJ",function()
        {
            window.open("/exam1/list.do?version=5d");
        });

        $("body").on("click",".set-fenceng",function(){
            var index=Number($(this).attr("index"));
            var p= $.inArray(index,indexList);
            if(p==-1)
            {
                if (indexList.length>$('#num').val()-1) {
                    alert("只能分"+$('#num').val()+"层！");
                    return;
                }
                if(indexList.length>24)
                {
                    alert("最多设置26层！");
                    return;
                }
                $(this).css("background","green");
                $(this).parent().css("border-bottom","solid 5px #F00");
                indexList.push(index);
                classrule.generalDTO();
            }
            else
            {
                indexList.splice(p,1);
                $(this).css("background","");
                $(this).parent().css("border-bottom","solid 1px #000");
                classrule.generalDTO();
            }
        });
        $("body").on("click","#saveFenceng",function(){
            classrule.addFenceng();
        });
    });

    classrule.checkClassCount = function(gid,scid) {
        classruleData.subjectConfId = scid;
        classruleData.groupId = gid;
        Common.getPostData("/bianban/checkClassCount.do",classruleData,function(rep){
            $('#num').val(rep.num);
        });
    };

    classrule.generalDTO=function()
    {
        var scoreMax=100;
        var scoreMin=0;
        var score="";
        var users="";
        indexList.sort(
            function(a, b)
            {
                if(a< b) return -1;
                if(a > b) return 1;
                return 0;
            }
        );
        ZbFenCengDTO.fencengs.splice(0,ZbFenCengDTO.fencengs.length);
        ZbFenCengDTO.scores.splice(0,ZbFenCengDTO.scores.length);
        ZbFenCengDTO.users.splice(0,ZbFenCengDTO.users.length);
        var _i=0;
        for(var j=0;j<indexList.length;j++) {
            for (var i = _i; i < scoreData.length; i++) {
                users+=scoreData[i].studentId+",";
                if(i==indexList[j])
                {
                    scoreMin=scoreData[i].score;
                    ZbFenCengDTO.fencengs.push(AZindex[j]);
                    ZbFenCengDTO.scores.push(scoreMin+"~"+scoreMax);
                    scoreMax=scoreData[i].score;
                    ZbFenCengDTO.users.push(users.substring(0,users.length-1));
                    users="";
                    _i=i+1;
                    break;
                }
            }
        }
        for(var i=_i;i<scoreData.length; i++)
        {
            users+=scoreData[i].studentId+",";
        }
        if(users.length>1)
        {
            ZbFenCengDTO.fencengs.push(AZindex[indexList.length]);
            ZbFenCengDTO.scores.push("0~"+scoreMax);
            ZbFenCengDTO.users.push(users.substring(0,users.length-1));
        }
        $("#fencengResultShow").empty();
        Common.render({tmpl: $('#fencengResultTempJs'), data: {data: ZbFenCengDTO}, context: '#fencengResultShow'});
    };
    var ZbFenCengDTO={
        xuanKeId:"",
        groupId:"",
        subjectConfId:"",
        type:"",
        fencengs:[],
        scores:[],
        users:[]
    };
    var scoreData=[];
    var indexList=[];
    var AZindex=["A","B","C","D","E","F","G","H","I","J","K","L","M","N",
    "O","P","Q","R","S","T","U","V","W","X","Y","Z"];
    classrule.getFenceng=function(groupid,subjectConfId,type)
    {
        var data={
            xuanKeId:$('#xuankeid').val(),
            groupId:groupid,
            subjectConfId:subjectConfId,
            type:type
        };
        Common.getPostData("/bianban/findFenCeng.do",data,function(rep){
            scoreData=rep.studentScore;
            $("#sliceTable").empty();
            Common.render({tmpl: $('#tab-sliceTemJs'), data: {data: rep}, context: '#sliceTable'});
        });
    };
    classrule.addFenceng=function(){
        $.ajax({
            url:"/bianban/addFenCeng.do",
            type:"post",
            contentType:"application/json",
            data:JSON.stringify(ZbFenCengDTO),
            success:function(rep)
            {
                if(rep=="SUCCESS")
                {
                    alert("保存成功");
                }
                else
                {
                    alert("保存失败");
                }
            }
        })
    };
    classrule.findClassStudentDetailList = function() {
        classruleData.subjectConfId = $('#courselist2').val();
        classruleData.groupId = $('#fdlist2').val();
        if (($('#courselist2').find("option:selected").text()).indexOf('等级考')!=-1) {
            classruleData.type=1;
        } else {
            classruleData.type=2;
        }
        Common.getPostData('/bianban/findClassStudentDetailList.do', classruleData,function(rep){
            $("#studentlist").empty();
            Common.render({tmpl: $('#studentlistTempJs'), data: {data: rep.rows}, context: '#studentlist'});
            $(".tab-regulation-XK").click(function(){
                $(".Sschedule-CUR").show();
                $(".bg").show();
                $("#userId").val($(this).attr("uid"));
                classruleData.studentName=$(this).parent().children().eq(0).text();
                classruleData.className=$(this).parent().children().eq(2).text();
                classrule.getConf();
            });
        });
    };
    classrule.findClassList = function() {
        Common.getPostData('/bianban/findClassList.do', classruleData,function(rep){
            $("#classlist").empty();
            Common.render({tmpl: $('#classlistTemJs'), data: {data: rep.clslist}, context: '#classlist'});
            $("#classlist2").empty();
            Common.render({tmpl: $('#classlistTemJs2'), data: {data: rep.fengduancls}, context: '#classlist2'});
            for(var i=0;i<rep.clslist.length;i++)
            {
                var obj=rep.clslist[i];
                fengDuanValue.push({groupId:obj.groupId,group:obj.group,classId:obj.classId,className:obj.className});
            }
            for(var i=0;i<rep.fengduancls.length;i++)
            {
                var obj=rep.fengduancls[i];
                fengDuanValue.push({groupId:obj.groupId,group:obj.group,classId:obj.classId,className:obj.className});
            }
        });
    };
    classrule.setZoubanState = function() {
        Common.getPostData('/bianban/setZoubanState.do', classruleData,function(rep){
        });
    };
    //===================================================调整选课========================================
    //===========================
    $(document).ready(function(){
        $("body").on("click","#getXuankeDetail",function()
        {
            classruleData.xuankeId=$("#xuankeid").val();
            classruleData.subjectId=$("#subjectmaps").val();
            classruleData.type=1;
            classruleData.classId=$("#classlist").val();
            Common.getPostData('/zouban/xuanKeSubjectDetail.do', classruleData,function(rep){
                $("#stulist").empty();
                Common.render({tmpl: $('#stulistTempJs'), data: {data: rep.sublist}, context: '#stulist'});
            });
        });
        //提交选课结果、
        $("body").on("click",".submitXuanke",function()
        {
            /*if(advList.length!=classruleData.advCount || simList.length!=classruleData.simCount)
            {
                alert("请选择"+classruleData.advCount+"门等级考，"+classruleData.simCount+"门合格考");
                return;
            }*/

            classruleData.advance=convertToStr(advList);
            classruleData.simple=convertToStr(simList);
            classruleData.stuId=$("#userId").val();
            classruleData.stuName=classruleData.studentName;
            classruleData.xuankeId=$("#xuankeid").val();
            Common.getPostData('/xuanke/teacherXK.do', classruleData,function(rep){
                if(rep=="200")
                {
                    alert("选课成功！");
                    classrule.getConf();
                }
                else
                {
                    alert("选课失败！");
                }
            });
        });
        $("body").on("click",".advcourse",function()
        {
            if($(this).text()=="已选")
            {
                $(this).removeClass("Sschedule-CUR-hov");
                $(this).text("我选");
                advList.splice($.inArray($(this).parent().parent().attr("sid"),advList),1);
            }
            else
            {
                $(this).addClass("Sschedule-CUR-hov");
                $(this).text("已选");
                advList.push($(this).parent().parent().attr("sid"));
                var index2=jQuery.inArray($(this).parent().parent().attr("sid"),simList);
                if(index2!=-1)
                {
                    simList.splice(index2, 1);
                    $(this).parent().parent().children().eq(2).find("span").removeClass("Sschedule-CUR-hov");
                    $(this).parent().parent().children().eq(2).find("span").text("我选");
                }
            }
        });
        $("body").on("click",".simcourse",function()
        {
            if($(this).text()=="已选")
            {
                $(this).removeClass("Sschedule-CUR-hov");
                $(this).text("我选");
                simList.splice($.inArray($(this).parent().parent().attr("sid"),simList),1);
            }
            else
            {
                $(this).addClass("Sschedule-CUR-hov");
                $(this).text("已选");
                simList.push($(this).parent().parent().attr("sid"));
                var index2=jQuery.inArray($(this).parent().parent().attr("sid"),advList);
                if(index2!=-1)
                {
                    advList.splice(index2, 1);
                    $(this).parent().parent().children().eq(1).find("span").removeClass("Sschedule-CUR-hov");
                    $(this).parent().parent().children().eq(1).find("span").text("我选");
                }
            }
        });
        $("body").on("click",".Sschedule-CUR-BU",function(){
            $(".Sschedule-CUR").hide();
            $(".bg").hide();
        });
    });
    var advList=[];
    var simList=[];
    //将array转成字符串，用,分割
    function convertToStr(obj)
    {
        var str="";
        for(var i=0;i<obj.length;i++)
        {
            str+=obj[i]+",";
        }
        if(str.length>1)
            str=str.substring(0,str.length-1);
        return str;
    }
    classrule.getConf=function()
    {
        Common.getPostData('/xuanke/getXuankeResultAdmin.do', {userId:$("#userId").val()},function(rep){
            $("#tableShow").empty();
            var advChoose=[];
            var simChoose=[];
            for(var i=0;i<rep.adv.length;i++)
            {
                advChoose.push(rep.adv[i].value);
            }
            for(var i=0;i<rep.sim.length;i++)
            {
                simChoose.push(rep.sim[i].value);
            }
            advList=[];
            for(var i=0;i<rep.conf.subConfList.length;i++)
            {
                if($.inArray(rep.conf.subConfList[i].subjectName,advChoose)>-1)
                {
                    advList.push(rep.conf.subConfList[i].subjectId);
                }
            }
            simList=[];
            for(var i=0;i<rep.conf.subConfList.length;i++)
            {
                if($.inArray(rep.conf.subConfList[i].subjectName,simChoose)>-1)
                {
                    simList.push(rep.conf.subConfList[i].subjectId);
                }
            }
            classruleData.advCount=rep.conf.advanceCount;
            classruleData.simCount=rep.conf.simpleCount;
            Common.render({tmpl: $('#tempJs'), data: {data: rep,advChoose:advChoose,simChoose:simChoose,
                stuName:classruleData.studentName,className:classruleData.className}, context: '#tableShow'});
        });
    };
    //==================================================================================================
    $(document).ready(function(){
        $("#gezhiBianBan").click(function(){
            classrule.getGezhiConf();
            $(".gezhiConf").show();
            $(".bg").show();
        });
        $(".curri-TJ1").click(function(){
            $(".gezhiConf").hide();
            $(".bg").hide();
            classrule.setGezhiConf();
        });
        $(".curri-QX1").click(function(){
            $(".gezhiConf").hide();
            $(".bg").hide();
        });
        $("body").on("change",".adv",function(){
            var groupId=$(this).attr("gid");
            var subjectId=$(this).attr("sid");
            var count=$(this).val();
            if(isNaN(count))
            {
                $(this).val("");
                return;
            }
            count=Number(count);
            for(var i=0;i<gezhiConfValue.gezhiConfList.length;i++)
            {
                if(gezhiConfValue.gezhiConfList[i].groupId==groupId && gezhiConfValue.gezhiConfList[i].subjectId==subjectId)
                {
                    gezhiConfValue.gezhiConfList[i].advCount=count;
                    return;
                }
            }
        });
        $("body").on("change",".sim",function(){
            var groupId=$(this).attr("gid");
            var subjectId=$(this).attr("sid");
            var count=$(this).val();
            if(isNaN(count))
            {
                $(this).val("");
                return;
            }
            count=Number(count);
            for(var i=0;i<gezhiConfValue.gezhiConfList.length;i++)
            {
                if(gezhiConfValue.gezhiConfList[i].groupId==groupId && gezhiConfValue.gezhiConfList[i].subjectId==subjectId)
                {
                    gezhiConfValue.gezhiConfList[i].simCount=count;
                    return;
                }
            }
        });
    });
    //==================================================================格致中学补充========================================
    $(document).ready(function(){
        $(".tab-rules-V").click(function(){
            if(classruleState>=3) {
                $(this).addClass('rules-LI');
                $(this).removeClass('rules-LII');
                $(".tab-rules-LI").addClass('rules-LII');
                $(".tab-rules-LIII").addClass('rules-LII');
                $(".tab-rules-LIIII").addClass('rules-LII');
                $(".tab-rules-VII").addClass('rules-LII');
                $(".tab-rules-VI").addClass('rules-LII');
                $(".tab-li-I").hide();
                $(".tab-li-II").hide();
                $(".tab-li-III").hide();
                $(".sfd-dd").hide();
                $(".tab-DT-I").hide();
                $(".tab-DT-II").show();
                $(".fdh-tab").hide();
                $(".right-main1").hide();
                $(".right-main-tea").show();
                $(".right-main-stu").hide();
                $(".right-main-room").hide();
                classrule.getGezhiCourseList($("#fenduanlist2-tea").val(), $("#subjectlist-tea").val(), $("#subject-type").val(), 0);
            }
        });
        $(".tab-rules-VI").click(function(){
            if(classruleState>=4) {
                $(this).addClass('rules-LI');
                $(this).removeClass('rules-LII');
                $(".tab-rules-LI").addClass('rules-LII');
                $(".tab-rules-LIII").addClass('rules-LII');
                $(".tab-rules-LIIII").addClass('rules-LII');
                $(".tab-rules-VII").addClass('rules-LII');
                $(".tab-rules-V").addClass('rules-LII');
                $(".tab-li-I").hide();
                $(".tab-li-II").hide();
                $(".tab-li-III").hide();
                $(".sfd-dd").hide();
                $(".tab-DT-I").hide();
                $(".tab-DT-II").show();
                $(".fdh-tab").hide();
                $(".right-main1").hide();
                $(".right-main-tea").hide();
                $(".right-main-stu").show();
                $(".right-main-room").hide();
                classrule.getGezhiCourseList($("#fenduanlist2-stu").val(), $("#subjectlist-stu").val(), 1, 1);
            }
        });
        $(".tab-rules-VII").click(function(){
            if(classruleState>=5) {
                $(this).addClass('rules-LI');
                $(this).removeClass('rules-LII');
                $(".tab-rules-LI").addClass('rules-LII');
                $(".tab-rules-LIII").addClass('rules-LII');
                $(".tab-rules-LIIII").addClass('rules-LII');
                $(".tab-rules-VI").addClass('rules-LII');
                $(".tab-rules-V").addClass('rules-LII');
                $(".tab-li-I").hide();
                $(".tab-li-II").hide();
                $(".tab-li-III").hide();
                $(".sfd-dd").hide();
                $(".tab-DT-I").hide();
                $(".tab-DT-II").show();
                $(".fdh-tab").hide();
                $(".right-main1").hide();
                $(".right-main-tea").hide();
                $(".right-main-stu").hide();
                $(".right-main-room").show();
                classrule.getGezhiCourseList($("#fenduanlist2-room").val(), $("#subjectlist-room").val(), 1, 2);
            }
        });

        $('body').on('change', '#fenduanlist2-tea', function () {
            var subjectId = $("#subjectlist-tea").val();
            if ($("#subject-type").val() == 2) {
                subjectId = -1;
            }
            classrule.getGezhiCourseList($("#fenduanlist2-tea").val(), subjectId, $("#subject-type").val(), 0);
        });
        $('body').on('change', '#subjectlist-tea', function () {
            var subjectId = $("#subjectlist-tea").val();
            if ($("#subject-type").val() == 2) {
                subjectId = -1;
            }
            classrule.getGezhiCourseList($("#fenduanlist2-tea").val(), subjectId, $("#subject-type").val(), 0);
        });
        $('body').on('change', '#subject-type', function () {
            var type = $("#subject-type").val();
            var subjectId = $("#subjectlist-tea").val();
            if (type==2) {
                $("#subjectlist-tea").hide();
                $(".subject-head").hide();
                subjectId=-1;
                $("#autoSetTea").html("一键设置非走班老师");
            }
            else{
                $("#subjectlist-tea").show();
                $(".subject-head").show();
                $("#autoSetTea").html("一键设置走班老师");
            }
            classrule.getGezhiCourseList($("#fenduanlist2-tea").val(), subjectId, $("#subject-type").val(), 0);
        });
        //自动设置老师
        $("#autoSetTea").click(function(){
            classrule.autoSetTeacher(Number($("#subject-type").val()));
        });
        //学生
        $('body').on('change', '#fenduanlist2-stu', function () {

            classrule.getGezhiCourseList($("#fenduanlist2-stu").val(), $("#subjectlist-stu").val(), 1, 1);
        });
        $('body').on('change', '#subjectlist-stu', function () {
            classrule.getGezhiCourseList($("#fenduanlist2-stu").val(), $("#subjectlist-stu").val(), 1, 1);
        });
        $("#main1-2btn-stu").click(function(){
            classrule.studentBianban();
        });
        //教室
        $('body').on('change', '#fenduanlist2-room', function () {

            classrule.getGezhiCourseList($("#fenduanlist2-room").val(), $("#subjectlist-room").val(), 1, 2);
        });
        $('body').on('change', '#subjectlist-room', function () {
            classrule.getGezhiCourseList($("#fenduanlist2-room").val(), $("#subjectlist-room").val(), 1, 2);
        });
        $("#autoSetClassroom").click(function(){
            classrule.autoSetClassroom();
        });
        //设置老师
        $("body").on("click",".edit-set-tea",function(){
            $(".edit-set-div-tea").show();

            $('#term2').html($('#termShow').text());
            $('#grade2-tea').html($('#gradeShow').text());
            $('#snm').html($(this).attr('snm'));
            if ($(this).attr('cn').indexOf('等级考')!=-1) {
                $('#ktp').html('等级考');
            } else {
                $('#ktp').html('合格考');
            }
            $('#coursenm').html($(this).attr('cn'));
            if($("#subject-type").val()==1)
                $('#duan-tea').html('第'+$(this).attr('gp')+'段');
            else {
                $('#duan-tea').html('');
                $('#ktp').html('');
            }
            var subjectId = $(this).attr('sid');
            Common.getPostData('/paike/findTeachersBySubjectAndGrade.do', {subjectId:subjectId,gradeId:$("#gradeShow").attr('gid')},function(rep){
                $("#teacherlist").empty();
                Common.render({tmpl: $('#teacherlistTempJs'), data: {data: rep}, context: '#teacherlist'});
            });
            if ($(this).attr('tid')!='null') {
                $('#teacherlist').val($(this).attr('tid'));
            }
            $("#courseId-set").val($(this).attr("couid"));
            $("#roomId-set").val($(this).attr("crid"));
            $("#weekCnt-set").val($(this).attr("cnt"));
            $("#classId-set").val($(this).attr("cid"));
            $(".bg").show();
        });
        //关闭设置弹窗
        $(".canc-btn,.setwind-cl").click(
            function () {
                $(".edit-set-div-tea").hide();
                $(".edit-set-div-room").hide();
                $(".bg").hide();
            });
        //保存设置老师
        $("#tea-cofi-btn").click(function(){
            var type=Number($("#subject-type").val());
            classrule.saveTeacher(type);
        });
        //设置教室
        $('body').on('click',".edit-set-room",function()
        {
            $(".edit-set-div-room").show();

            $('#term2-room').html($('#termShow').text());
            $('#grade2-room').html($('#gradeShow').text());
            $('#snm-room').html($(this).attr('snm'));
            if ($(this).attr('cn').indexOf('等级考')!=-1) {
                $('#ktp-room').html('等级考');
            } else if ($(this).attr('cn').indexOf('合格考')!=-1){
                $('#ktp-room').html('合格考');
            }
            $('#coursenm-room').html($(this).attr('cn'));
            $('#duan-room').html('第'+$(this).attr('gp')+'段');
            var subjectId = $(this).attr('sid');
            Common.getPostData('/paike/findAvailableRoom.do', {
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr('gid'),
                groupId:$(this).attr("gid")},
                function(rep){
                $("#classroomlist").empty();
                Common.render({tmpl: $('#classroomlistTempJs'), data: {data: rep}, context: '#classroomlist'});
            });
            if ($(this).attr('crid')!='null') {
                $('#classroomlist').val($(this).attr('crid'));
            }
            $("#courseId-set-room").val($(this).attr("couid"));
            $("#teacherId-set-room").val($(this).attr("tid"));
            $("#teacherName-set-room").val($(this).attr("tnm"));
            $("#weekCnt-set-room").val("1");
            $("#classId-set-room").val("1");
            $(".bg").show();
        });
        //保存教室
        $("#room-cofi-btn").click(function(){
            classrule.saveClassRoom();
        });
        //查看学生名单
        $("body").on("click",".tab-check",function(){
            $('#term6').html($('#termShow').text());
            $('#grade6').html($('#gradeShow').text());
            $(".tab-col").hide();
            $(".right-main2").show();
            classruleData.courseClassId = $(this).attr('couid');
            classrule.findClassStudentList(1);
        });
    });
    var gezhiConfValue={};
    var type=0;

    //保存设置老师
    classrule.saveTeacher = function(type)
    {
        Common.getPostData('/bianban/updateClassCourseInfo.do', {
            courseClassId:$("#courseId-set").val(),
            teacherId:$('#teacherlist').val(),
            classRoomId:$("#roomId-set").val()=="null"?"":$("#roomId-set").val(),
            teacherName:$('#teacherlist').find("option:selected").text(),
            weekcnt:$("#weekCnt-set").val(),
            type:type,
            classId:$("#classId-set").val()
        },function(rep){
            if(rep.flg) {
                $(".edit-set-div-tea").hide();
                $(".bg").hide();
                var subjectId = $("#subjectlist-tea").val();
                if ($("#subject-type").val() == 2) {
                    subjectId = -1;
                }
                classrule.checkTeacherFinish(type);
                classrule.getGezhiCourseList($("#fenduanlist2-tea").val(), subjectId, $("#subject-type").val(), 0);
            }
        });
    };
    //保存教室
    classrule.saveClassRoom = function()
    {
        Common.getPostData('/bianban/updateClassCourseInfo.do', {
            courseClassId:$("#courseId-set-room").val(),
            teacherId:$('#teacherId-set-room').val(),
            classRoomId:$('#classroomlist').val(),
            teacherName:$('#teacherName-set-room').val(),
            weekcnt:1,
            type:1,
            classId:""
        },function(rep){
            if(rep.flg) {
                $(".edit-set-div-room").hide();
                $(".bg").hide();
                classrule.checkClassroomFinish();
                classrule.getGezhiCourseList($("#fenduanlist2-room").val(), $("#subjectlist-room").val(), 1, 2);
            }
        });
    };
    //格致编班
    classrule.getGezhiConf=function()
    {
        $.ajax({
            url:"/paike/bianbanConf.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr('gid')
            },
            success:function(data)
            {
                gezhiConfValue.gezhiConfList=data;
                $("#temp").empty();
                var mode=$("#mode").val();
                if(mode=="gezhiIndex")
                {
                    Common.render({tmpl: $('#tempConfJs'), data: {data:data}, context: '#temp'});
                }
                else if(mode=="changZhengIndex")
                {
                    Common.render({tmpl: $('#tempConfCZJs'), data: {data:data}, context: '#temp'});
                }

            }
        })
    };
    //自动设置教室
    classrule.autoSetClassroom=function()
    {
        $.ajax({
            url:"/paike/setGezhiClassroom.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid")
            },
            success:function(data)
            {
                if(data.result!="200")
                {
                    alert("自动设置教室失败，可能原因：教室数目不足,请在首页教室管理中添加");
                }
                else
                {
                    classrule.getGezhiCourseList($("#fenduanlist2-room").val(), $("#subjectlist-room").val(), 1, 2);
                    classrule.checkClassroomFinish();
                }
            }
        })
    };
    var classruleState=Number($("#state").val());//记录步骤
    //学生编班
    classrule.studentBianban=function()
    {
        $(".bianbanTip").show();
        $(".bg").show();
        $.ajax({
            url:"/paike/studentBianban.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid")
            },
            success:function(data)
            {
                if(data.result==200)
                {
                    classruleState=5;
                }
                else
                {
                    alert("编排失败，请调整班级组合或学生选课");
                }
                $(".bianbanTip").hide();
                $(".bg").hide();
                classrule.getGezhiCourseList($("#fenduanlist2-stu").val(), $("#subjectlist-stu").val(), 1, 1);

            }
        })
    };
    classrule.getGezhiCourseList=function(groupId,subjectId,type,index)
    {
        $.ajax({
            url:"/bianban/findBianBanList.do",
            type:"post",
            data:{
                term:$("#termShow").text(),
                gradeId:$("#gradeShow").attr('gid'),
                groupId:groupId,
                subjectId:subjectId,
                type:type
            },
            dataType:"json",
            success:function(rep)
            {
                if(index==0) {
                    if (type == 1) {
                        $("#courseclslist-tea").empty();
                        Common.render({
                            tmpl: $('#courseclslistTempJs-tea'),
                            data: {data: rep.rows},
                            context: '#courseclslist-tea'
                        });
                    }
                    else if (type == 2) {
                        $("#courseclslist-tea").empty();
                        Common.render({
                            tmpl: $('#feizoubanlistTempJs'),
                            data: {data: rep.rows},
                            context: '#courseclslist-tea'
                        });
                    }
                }
                else if(index==1)
                {
                    $("#courseclslist-stu").empty();
                    Common.render({
                        tmpl: $('#courseclslistTempJs-stu'),
                        data: {data: rep.rows},
                        context: '#courseclslist-stu'
                    });
                }
                else if(index==2)
                {
                    $("#courseclslist-room").empty();
                    Common.render({
                        tmpl: $('#courseclslistTempJs-room'),
                        data: {data: rep.rows},
                        context: '#courseclslist-room'
                    });
                }

            }
        })
    };
    //自动设置老师
    classrule.autoSetTeacher=function(type)
    {
        $.ajax({
            url:"/paike/autoSetTeacher.do",
            type:"post",
            dataType:"json",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid"),
                type:type
            },
            success:function(data)
            {
                var subjectId = $("#subjectlist-tea").val();
                if ($("#subject-type").val() == 2) {
                    subjectId = -1;
                }
                classrule.getGezhiCourseList($("#fenduanlist2-tea").val(), subjectId, $("#subject-type").val(), 0);
                classrule.checkTeacherFinish(type);
            }
        })
    };
    //设置老师进度值
    classrule.checkTeacherFinish=function(type)
    {
        $.ajax({
            url:"/paike/setTeacherState.do",
            type:"post",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid"),
                type:type
            },
            success:function(data)
            {
                if(data=="finish")
                {
                    classruleState=4;
                }
            }
        })
    };
    //设置教室进度值
    classrule.checkClassroomFinish=function()
    {
        $.ajax({
            url:"/paike/setClassroomState.do",
            type:"post",
            data:{
                year:$("#termShow").text(),
                gradeId:$("#gradeShow").attr("gid")
            },
            success:function(data)
            {
                if(data=="finish")
                {
                    classruleState=6;
                }
            }
        })
    };
    classrule.setGezhiConf=function()
    {
        var advTotal=[];
        var groupIdList=[];
        for(var i=0;i<gezhiConfValue.gezhiConfList.length;i++)
        {
            if($.inArray(gezhiConfValue.gezhiConfList[i].groupId,groupIdList)==-1)
            {
                groupIdList.push(gezhiConfValue.gezhiConfList[i].groupId);
                advTotal.push({group:gezhiConfValue.gezhiConfList[i].groupId,advCount:0,simCount:0});
            }
        }

        for(var i=0;i<gezhiConfValue.gezhiConfList.length;i++)
        {
            for(var k=0;k<advTotal.length;k++)
            {
                if(advTotal[k].group==gezhiConfValue.gezhiConfList[i].groupId)
                {
                    advTotal[k].advCount=advTotal[k].advCount+gezhiConfValue.gezhiConfList[i].advCount;
                    advTotal[k].simCount=advTotal[k].simCount+gezhiConfValue.gezhiConfList[i].simCount;
                    if(advTotal[k].advCount>15)
                    {
                        alert(gezhiConfValue.gezhiConfList[i].groupName+"等级考编班总数超过12");
                        return;
                    }
                    else if(advTotal[k].simCount>15)
                    {
                        alert(gezhiConfValue.gezhiConfList[i].groupName+"合格考编班总数超过12");
                        return;
                    }
                }
            }
        }
        gezhiConfValue.year=$("#termShow").text();
        gezhiConfValue.gradeId=$("#gradeShow").attr('gid');
        var url="/paike/gezhiBianban.do";
        var mode=$("#mode").val();
        if(mode=="gezhiIndex")
        {
            url="/paike/gezhiBianban.do";
        }
        else if(mode=="changZhengIndex")
        {
            url="/paike/changZhengBianban.do";
        }
        $.ajax(
            {
                url:url,
                type:"post",
                contentType:"application/json",
                data: JSON.stringify(gezhiConfValue),
                success:function(data)
                {
                    if(data=="200")
                    {
                        classruleState=3;
                    }
                    classrule.getgroupSubjectSelect();
                    classrule.findBianBanList();
                }
            }
        )
    };
});