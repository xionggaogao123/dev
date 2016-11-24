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
            $(".sfd-dd").show();
            $(".tab-DT-I").show();
            $(".tab-DT-II").hide();
            $(".right-main1").hide();
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
            $(".tab-li-I").hide();
            $(".tab-li-II").show();
            $(".tab-li-III").hide();
            $(".sfd-dd").hide();
            $(".tab-DT-I").hide();
            $(".tab-DT-II").show();
            $(".fdh-tab").hide();
            $(".right-main1").hide();
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
            $(this).addClass('rules-LI');
            $(this).removeClass('rules-LII');
            $(".tab-rules-LI").addClass('rules-LII');
            $(".tab-rules-LII").addClass('rules-LII');
            $(".tab-rules-LIII").addClass('rules-LII');
            $(".tab-li-I").hide();
            $(".tab-li-II").hide();
            $(".tab-li-III").hide();
            $(".tab-DT-I").hide();
            $(".sfd-dd").hide();
            $(".fdh-tab").hide();
            $(".tab-DT-II").show();
            $(".right-main1").show();
            classrule.findBianBanList();
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
        //$("#choose2").click(function(){
            //if($('#choose2').is(':checked')) {
            //    $('#examlist').attr("disabled",false);
            //    //document.getElementById('findCJ').style.display="block";
            //} else {
            //    $('#examlist').attr("disabled",true);
            //    //document.getElementById('findCJ').style.display="none";
            //}
            //alert('111');
        //});
        classrule.findFengDuan();
        classrule.getgroupSubjectSelect();
        $(".section-CUR-QR").click(function(){
            classrule.autoFengDuan();
        });

        //$("#seachfd").click(function(){
        //    classrule.findFengDuan();
        //});

        $('body').on('change', '#fdlist', function () {
            classrule.findXuanKeDetail();
        });
        //$('.tab-li-II-SX').click(function(){
        //    classrule.findFenCengList();
        //});
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
        //$('body').on('change', '#gradeShow', function () {
        //    classrule.findFengDuan();
        //});
        //
        //$('body').on('change', '#termShow', function () {
        //    classrule.findFengDuan();
        //});

    };

        /*$(function(){
            $(".section-CK").click(function(){
                $(".bg").show();
                $(".ranking-CUR").show();
            })
        })*/

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
            $('#nocnt').html(rep.misscount+'/'+rep.classcount);
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
            //$('#fcfdlist').empty();
            //Common.render({tmpl: $('#fcfdlistTempJs'), data: {data: rep.fengduanList}, context: '#fcfdlist'});

            //$(".tab-rules-TZ").click(
            //    function(){
            //        $(".bg").show();
            //        $(".adjust-CUR").show();
            //    }
            //)

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
                    /*var clsAry = [];
                    $('#classlist2').children().each(function () {
                        clsAry.push($(this).children().attr('cid')+','+$(this).children().attr('gop'));
                    });
                    classruleData.clsAry = clsAry;
                    Common.getPostData('/bianban/updateClassGroups.do', classruleData,function(rep){
                        if (rep.flg) {
                            alert("调班成功！");
                            $(".bg").hide();
                            $(".adjust-CUR").hide();
                        } else {
                            alert("调班失败！");
                        }
                    });*/
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
        Common.getPostData('/bianban/addAutoBianBanV2.do', classruleData,function(rep){
            if (rep.flg) {
                alert("自动编班成功！");
                classrule.setZoubanState();
                classrule.findBianBanList();
            } else {
                alert("自动编班失败！请重新设置班级人数范围，将编排后的等级考走班数量控制在1~2，合格考的走班数量控制在0~3");
            }

        });
    }
    classrule.findClassSubjectCount = function() {

    }
    classrule.autoFengDuan = function() {
        document.getElementById('error').style.display="none";
        classruleData.xuankeId = $("#xuankeid").val();
        classruleData.gradeId = $("#gradeShow").attr('gid');
        classruleData.count = $("#count").val();
        classruleData.clscnt = $('.se2').val();
        if($('#duanchoose2').is(':checked')) {
            classruleData.examType = 1;
            classruleData.examId = $('#examlist').val();
            classruleData.mincnt = 0;
            classruleData.maxcnt = 0;
        } else {
            classruleData.examType = 0;
            classruleData.examId = '';
            classruleData.examId = $('#examlist2').val();
            classruleData.mincnt = $('#mincnt').val();
            classruleData.maxcnt = $('#maxcnt').val();
        }
        if (!$('#duanchoose2').is(':checked') && ($.trim($('#mincnt').val()) == ''|| $('#mincnt').val()== 0 || $.trim($('#maxcnt').val()) == ''|| $('#maxcnt').val()== 0)) {
            document.getElementById('error').style.display="block";
        } else {
            document.getElementById('error').style.display="none";
            Common.getPostData('/bianban/autoFengDuan.do', classruleData,function(rep){
                if (rep.flg) {
                    $(".bg").hide();
                    $(".section-CUR").hide();
                    classrule.findFengDuan();
                } else {
                    alert("自动分段失败，请重新设置！");
                }

            });
        }
    }

    //$(".tab-rules-TZ").click(
    //    function(){
    //        $(".bg").show();
    //        $(".adjust-CUR").show();
    //    }
    //);

    //$(".section-CUR-QR").click(
    //    function(){
    //        $(".fdh-tab").show();
    //        $(".bg").hide();
    //        $(".section-CUR").hide();
    //    }
    //)



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
                    if(classrule.findCourseById()) {
                        $('#term7').html($('#termShow').text());
                        $('#grade7').html($('#gradeShow').text());
                        $('#duan7').html('第' + $(this).attr('gp') + '段');
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
                }
            );

        });
    };
    classrule.findCourseById = function() {
        Common.getPostData('/bianban/findCourseById.do', classruleData,function(rep){
            if(rep!=null && rep.length!=0) {
                $("#classlist5").empty();
                Common.render({tmpl: $('#classlistTempJs5'), data: {data: rep}, context: '#classlist5'});
                return true;
            } else {
                alert("只有一个班，不可调班！");
                return false;
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
        $("body").on("click",".rename",function()
        {
            $("#renameInp").val($(this).attr("nm"));
            $("#renameCid").val($(this).attr("cid"));
            $(".renameWin").show();
            $(".bg").show();
        });
        $("body").on("click",".update-X-rename,.rename-QX",function()
        {
            $(".renameWin").hide();
            $(".bg").hide();
        });
        $("body").on("click",".rename-TJ",function()
        {
            classrule.rename();
        });
    });
    classrule.rename=function()
    {
        $.ajax({
            url:"/bianban/rename.do",
            type:"post",
            dataType:"json",
            data:{
                courseId:$("#renameCid").val(),
                courseName:$("#renameInp").val()
            },
            success:function(data)
            {
                if(data.result=="200")
                {
                    $(".renameWin").hide();
                    $(".bg").hide();
                    classrule.findBianBanList();
                }
                else{
                    alert("修改失败");
                }
            }
        })
    }
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
});