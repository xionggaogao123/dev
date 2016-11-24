/*
 * @Author: Tony
 * @Date:   2015-06-11 14:24:31
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-06-30 16:43:27
 */

'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','fancybox','experienceScore','select2'],function(require,exports,module){
    /**
     *初始化参数
     */
    var jiaban = {},
        Common = require('common');
    require('fileupload');
    require('select2');
    var someFileFailed = false;
    //提交参数
    var jiabanData = {};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * zhiban.init()
     */
    jiaban.init = function(){


        //jiaban.selJiaBanList(1);
        jiaban.fileUpload();
        if (getQueryString('type')==1) {
            $('#JBSQ').addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            jiaban.selJiaBanList(1);
            $("#tab-JBSQ").show();
        } else if (getQueryString('type')==2) {
            $('#JBXC').addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            jiaban.selJiaBanList(2);
            $("#tab-JBXC").show();
        } else if (getQueryString('type')==3) {
            $('#JBSH').addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            jiaban.selJiaBanList(3);
            $("#tab-JBSH").show();
        } else if (getQueryString('type')==4) {
            $('#JSJB').addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            jiaban.selTeacherJiaBanList();
            $("#tab-JSJB").show();
        }
        $(".shcheck").change(function() {
            var cek = $("input[name='check']:checked").val();
            if (cek == 1) {
                $('#shenheUser6').attr("disabled",true);
            } else {
                $('#shenheUser6').attr("disabled",false);
            }
        });
        $('#JBSQ').click(function () {
            $(this).addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            jiaban.selJiaBanList(1);
            $("#tab-JBSQ").show();
        });
        $('#JBXC').click(function () {
            $(this).addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            jiaban.selJiaBanList(2);
            $("#tab-JBXC").show();
        });
        $('#JBSH').click(function () {
            $(this).addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            jiaban.selJiaBanList(3);
            $("#tab-JBSH").show();
        });
        $('#JSJB').click(function () {
            $(this).addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            jiaban.selTeacherJiaBanList();
            $("#tab-JSJB").show();
        });
        //$(".tab-head li").click(function(){
        //    $(this).addClass("cur").siblings().removeClass("cur");
        //    $(".tab-main>div").hide();
        //    var name = $(this).attr("id");
        //    if (name=='JBSQ') {
        //        jiaban.selJiaBanList(1);
        //    } else if (name=='JBXC') {
        //        jiaban.selJiaBanList(2);
        //    } else if (name=='JBSH') {
        //        jiaban.selJiaBanList(3);
        //    } else if (name=='JSJB') {
        //        jiaban.selTeacherJiaBanList();
        //    }
        //    $("#" + "tab-" + name).show();
        //})
        $(".JBSQ-jiaban").click(function(){
            $(".bg").show();
            $(".popup-SQ").show();
        })
        $(".SQ-X").click(function(){
            $(".bg").hide();
            $(".popup-SQ").hide();
            $(".popup-MB").hide();
            $(".popup-SD").hide();
            $(".popup-JB").hide();
            $(".popup-SHR").hide();
        })
        $(".SHR-QX").click(function(){
            $(".popup-SHR").hide();
            $(".bg").hide();
        });

        $(".MB-QX").click(function(){
            $(".bg").hide();
            $(".popup-MB").hide();
        });
        $(".zb-set-close").click(function(){
            $(".bg").hide();
            $(".zb-model-alert").hide();
        });
        $(".zb-btn-qx").click(function(){
            $(".bg").hide();
            $(".zb-model-alert").hide();
        });
        $("#modellist").change(function(){
            jiabanData.overTimeModelId = $('#modellist').val();
            Common.getData('/overTime/selModelList.do', jiabanData,function(rep) {
                $('#jiabanUser5').val(rep.modeldto.jbUserId);
                $('#jiabanDate5').val(rep.modeldto.date);
                $('#startTime5').val(rep.modeldto.startTime);
                $('#endTime5').val(rep.modeldto.endTime);
                $('#cause5').val(rep.modeldto.cause);
                $('#shenheUser5').val(rep.modeldto.shUserId);
            });
        });
        $(".MB-TJ").click(function(){
            jiaban.addJiaBanInfo(2);
        });
        $(".SQ-sy").click(function(){
            jiabanData.overTimeModelId = "";
            Common.getData('/overTime/selModelList.do', jiabanData,function(rep) {
                if (rep.rows==null || rep.rows.length==0) {
                    alert("没有模板，请先创建模板！");
                } else {
                    $('#modellist').empty();
                    var sel = '';
                    for (var i=0;i<rep.rows.length;i++) {
                        sel += "<option value="+rep.rows[i].id+">"+rep.rows[i].name+"</option>";
                    }
                    $('#modellist').append(sel);
                    $('#jiabanUser5').val(rep.modeldto.jbUserId);
                    $('#jiabanDate5').val(rep.modeldto.date);
                    $('#startTime5').val(rep.modeldto.startTime);
                    $('#endTime5').val(rep.modeldto.endTime);
                    $('#cause5').val(rep.modeldto.cause);
                    $('#shenheUser5').val(rep.modeldto.shUserId);
                    $(".bg").show();
                    $(".popup-MB").show();
                }
            });
        })
        $(".SQ-sd").click(function(){
            $('#jiabanDate').val('');
            $('#startTime').val('');
            $('#endTime').val('');
            $('#cause').val('');
            //jiaban.getSelectTeacherList($('#shenheUser'), function() {
            //    $('#shenheUser').select2("destroy");
            //    jiaban.initSelect($('#shenheUser'));
            //});
            $(".bg").show();
            $(".ADDJB-POP").show();
        });
        $(".qx-jiaban").click(function(){
            $(".bg").hide();
            $(".popup-SD").hide();
        });
        $(".JB-QX").click(function(){
            $(".bg").hide();
            $(".popup-JB").hide();
        });
        $(".submit-log").click(function(){
            jiaban.addDutyLog();
        });
        $(".JBSQ-muban").click(function(){
            jiaban.selJiaBanModelList();
            $(".JBSQ-II").show();
            $(".JBSQ-I").hide();
        })
        $(".JBSQ-II-back").click(function(){
            $(".JBSQ-II").hide();
            $(".JBSQ-I").show();
        })
        $(".submit-jiaban").click(function(){
            jiaban.addJiaBanInfo(1);
        });
        $(".SHR-TJ").click(function(){
            var cek = $("input[name='check']:checked").val();
            var userId = '';
            if (cek==2) {
                if ($('#shenheUser6').val()==''||$('#shenheUser6').val()==null) {
                    alert('请选择审核人！');
                    return;
                } else {
                    userId = $('#shenheUser6').val();
                }
            }
            jiabanData.overTimeId = $('#overTimeId6').val();
            jiabanData.userId=userId;
            Common.getData('/overTime/updTongGuo.do', jiabanData,function(rep) {
                if (rep.flag) {
                    alert("审核通过！");
                    $('.popup-SHR').hide();
                    $('.bg').hide();
                    jiaban.selJiaBanList(3);
                } else {
                    alert("审核失败！");
                }
            });
        });

        $(".daochu-jiaban").click(function(){
            window.location.href="/overTime/exportJiaBanList.do?startDate="+$("#bTime").val()+"&endDate="+$("#eTime").val()+"&name="+$('#userName').val()+"&type=1&index=0";
        });
        $(".daochu-salary").click(function(){
            window.location.href="/overTime/exportJiaBanList.do?startDate="+$("#bTime2").val()+"&endDate="+$("#eTime2").val()+"&name="+$('#userName2').val()+"&type=2&index=0";
        });
        $(".daochu-shehe").click(function(){
            window.location.href="/overTime/exportJiaBanList.do?startDate="+$("#bTime3").val()+"&endDate="+$("#eTime3").val()+"&name="+$('#userName3').val()+"&type=3&index="+$('#shenhe').val();
        });

        $(".save-model").click(function(){
            if ($('#jiabanDate').val()=='') {
                alert("请输入加班日期！");
                return;
            }
            if ($('#startTime').val()==''||$('#endTime').val()=='') {
                alert("请输入加班时段！");
                return;
            }
            if ($('#cause').val()=='') {
                alert("请输入加班事由！");
                return;
            }
            $('#modelId').val('');
            $('#modelname').val('');
            $(".zb-model-alert").show();

        });
        $(".sure-model").click(function(){
            if ($('#modelname').val()=='') {
                alert("请输入模板名称！");
                return;
            }
            jiaban.saveModelInfo();
        });
        $("#submit-overtime").click(function(){
            jiaban.selJiaBanList(1);
        });
        $("#submit-overtime2").click(function(){
            jiaban.selJiaBanList(2);
        });
        $("#submit-overtime3").click(function(){
            jiaban.selJiaBanList(3);
        });


    }
    jiaban.selJiaBanModelList = function(){
        Common.getData('/overTime/selJiaBanModelList.do', jiabanData,function(rep){
            $('#model-table').html('');
            Common.render({tmpl:$('#model-table_templ'),data:rep,context:'#model-table'});
            $(".del-model").click(function(){
                jiabanData.overTimeModelId = $(this).attr('mid');
                Common.getData('/overTime/delOverTimeModel.do', jiabanData,function(rep) {
                    if (rep.flag) {
                        alert("删除成功！");
                        jiaban.selJiaBanModelList();
                    } else {
                        alert("删除失败！");
                    }
                });
            });
            $(".edit-model").click(function(){
                $('#modelname').val($(this).attr("mnm"));
                $('#modelId').val($(this).attr("mid"));
                $(".bg").show();
                $(".zb-model-alert").show();
            });
        });
    }
    jiaban.addDutyLog = function() {
        jiabanData.overTimeId = $('#overTime3').val();
        jiabanData.log = $('#dutylog').val();
        var flist = '';
        var fname = '';
        $(".popup-bottom-LAB label").each(function() {
            var srcPath=$(this).attr('path');
            var nm = $(this).attr('nm');
            flist += srcPath + ',';
            fname += nm+ ',';
        });
        jiabanData.filepath = flist;
        jiabanData.realName = fname;
        Common.getData('/overTime/addOverTimeLog.do', jiabanData,function(rep) {
            if (rep.flag) {
                alert("修改值班记录成功！");
                jiaban.selTeacherJiaBanList();
                $(".popup-JB").hide();
                $(".bg").hide();
            } else {
                alert("修改值班记录失败！");
            }
        });
    }
    jiaban.saveModelInfo = function() {
        jiabanData.jbUserId=$('#jiabanUser').val();
        jiabanData.date=$('#jiabanDate').val();
        jiabanData.startTime=$('#startTime').val();
        jiabanData.endTime=$('#endTime').val();
        jiabanData.cause=$('#cause').val();
        jiabanData.shUserId=$('#shenheUser').val();
        jiabanData.modelName = $('#modelname').val();
        jiabanData.modelId = $('#modelId').val();
        Common.getData('/overTime/saveModelInfo.do', jiabanData,function(rep){
            if (rep.flag) {
                if (rep.count!=0) {
                    alert("模板名称重复！");
                } else {
                    if($('#modelId').val()=='') {
                        alert("模板添加成功！");
                    } else {
                        alert("模板修改成功！");
                        $(".bg").hide();
                        jiaban.selJiaBanModelList();
                    }
                    $(".zb-model-alert").hide();
                }
            } else {
                alert("模板添加失败！");
            }
        });
    };
    jiaban.addJiaBanInfo = function(type) {
        if (type==1) {
            if ($('#jiabanDate').val()=='') {
                alert("请输入加班日期！");
                return;
            }
            if ($('#startTime').val()==''||$('#endTime').val()=='') {
                alert("请输入加班时段！");
                return;
            }
            if ($('#cause').val()=='') {
                alert("请输入加班事由！");
                return;
            }
            jiabanData.overTimeId = $('#overTimeId').val();
            jiabanData.jbUserId=$('#jiabanUser').val();
            jiabanData.date=$('#jiabanDate').val();
            jiabanData.startTime=$('#startTime').val();
            jiabanData.endTime=$('#endTime').val();
            jiabanData.cause=$('#cause').val();
            jiabanData.shUserId=$('#shenheUser').val();
        } else if (type==2) {
            if ($('#jiabanDate5').val()=='') {
                alert("请输入加班日期！");
                return;
            }
            if ($('#startTime5').val()==''||$('#endTime5').val()=='') {
                alert("请输入加班时段！");
                return;
            }
            if ($('#cause5').val()=='') {
                alert("请输入加班事由！");
                return;
            }
            jiabanData.overTimeId = "";
            jiabanData.jbUserId=$('#jiabanUser5').val();
            jiabanData.date=$('#jiabanDate5').val();
            jiabanData.startTime=$('#startTime5').val();
            jiabanData.endTime=$('#endTime5').val();
            jiabanData.cause=$('#cause5').val();
            jiabanData.shUserId=$('#shenheUser5').val();
        }

        Common.getData('/overTime/addJiaBanInfo.do', jiabanData,function(rep){
            if (rep.flag) {
                if (!rep.check) {
                    alert("申请加班时间冲突，请修改时间！");
                } else {
                    alert("申请成功！");
                    $(".bg").hide();
                    $(".popup-SD").hide();
                    $(".popup-SQ").hide();
                    $(".popup-MB").hide();

                    jiaban.selJiaBanList(1);
                }
            } else {
                alert("申请失败！");
            }
        });
    }
    jiaban.selTeacherJiaBanList = function() {
        Common.getData('/overTime/selTeacherJiaBanList.do', jiabanData,function(rep){
            $('.tea_jiabanList').html('');
            Common.render({tmpl:$('#tea_jiabanList_templ'),data:rep,context:'.tea_jiabanList'});
            $(".JSJB-QD").click(function(){
                jiaban.checkInOut(1,$(this).attr('otid'));
            });
            $(".JSJB-QT").click(function(){
                jiaban.checkInOut(2,$(this).attr('otid'));
            });
            $(".JSJB-TX").click(function(){
                $('#timeDuan6').html($(this).attr('dt'));
                $('#timeDuan3').html($(this).attr('td'));
                $('#cause3').html($(this).attr('cu'));
                $('#overTime3').val($(this).attr('otid'));
                jiabanData.overTimeId = $(this).attr('otid');
                Common.getData('/overTime/selSingleOverTime.do', jiabanData, function (rep) {
                    $('#dutylog').text(rep.rows.content);
                    $(".popup-bottom-LAB").html('');
                    if (rep.rows.lessonWareList!=null && rep.rows.lessonWareList.length!=0) {
                        //sessionStorage.setItem("realname",rep.rows.filePath);
                        //sessionStorage.setItem("filename",rep.rows.realName);
                        for (var i=0;i<rep.rows.lessonWareList.length;i++) {
                            var uf = '<label style="padding-left: 50px;" path="'+rep.rows.lessonWareList[i].path+'" nm="'+rep.rows.lessonWareList[i].name+'">'+rep.rows.lessonWareList[i].name+'<a class="yulan" style="padding-left:10px;" pth='+rep.rows.lessonWareList[i].path+'>预览</a>'+'<span  class="h-cursor del-file" style="padding-left:10px;">删除</span></label>';
                            $(".popup-bottom-LAB").append(uf);
                        }
                        $('.yulan').click(function() {
                            var url = $(this).attr("pth");
                            var urlarg = url.split(".")[url.split(".").length-1];
                            if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
                                window.open("http://ow365.cn/?i=9666&furl="+url);
                            } else {
                                window.open(url);
                            }
                        });
                        $(".del-file").click(function(){
                            //sessionStorage.clear();
                            $(this).parent().remove();
                        });
                    }
                });
                $(".bg").show();
                $(".popup-JB").show();
            })
        });
    }
    jiaban.checkInOut = function(type,id) {
        jiabanData.type = type;
        jiabanData.overTimeId = id;
        Common.getData('/overTime/checkInOut.do', jiabanData,function(rep){
            if(rep.flag) {
                if (type==1) {
                    alert("签到成功！");
                } else {
                    alert("签退成功！");
                }
                jiaban.selTeacherJiaBanList();
            } else {
                if (type==1) {
                    alert(rep.mesg);
                }  else{
                    alert("签退失败！");
                }
            }
        });
    }
    jiaban.selJiaBanList = function(type) {
        if (type==1) {
            jiabanData.startDate=$('#bTime').val();
            jiabanData.endDate=$('#eTime').val();
            jiabanData.name=$('#userName').val();
            jiabanData.type=1;
            jiabanData.index = 0;
        } else if (type==2) {
            jiabanData.startDate=$('#bTime2').val();
            jiabanData.endDate=$('#eTime2').val();
            jiabanData.name=$('#userName2').val();
            jiabanData.type=2;
            jiabanData.index = 0;
        } else if (type==3) {
            jiabanData.startDate=$('#bTime3').val();
            jiabanData.endDate=$('#eTime3').val();
            jiabanData.name=$('#userName3').val();
            jiabanData.type=3;
            jiabanData.index = $('#shenhe').val();
        }
        Common.getData('/overTime/selJiaBanList.do', jiabanData,function(rep){
            if (type==1) {
                $('.jiabanList').html('');
                Common.render({tmpl:$('#jiabanList_templ'),data:rep,context:'.jiabanList'});
                $(".del-vt").click(function(){
                    jiabanData.overTimeId = $(this).attr('vtid');
                    if(confirm("确定删除吗？")) {
                        Common.getData('/overTime/delJiaBanInfo.do', jiabanData, function (rep) {
                            if (rep.flag) {
                                alert("删除成功！");
                                jiaban.selJiaBanList(1);
                            } else {
                                alert("删除失败！");
                            }
                        });
                    }
                });
                $(".view-vt").click(function(){
                    jiabanData.overTimeId = $(this).attr('vtid');
                    Common.getData('/overTime/selSingleOverTime.do', jiabanData, function (rep) {
                        $('#jiabanUser2').val(rep.rows.jbUserId);
                        $('#jiabanDate2').val(rep.rows.date);
                        $('#startTime2').val(rep.rows.startTime);
                        $('#endTime2').val(rep.rows.endTime);
                        $('#cause2').val(rep.rows.cause);
                        $('#shenheUser2').val(rep.rows.shUserId);
                        $(".bg").show();
                        $(".XXJB-POP").show();
                    });
                });
                $(".submit-vt").click(function(){
                    jiabanData.overTimeId = $(this).attr('vtid');
                    jiabanData.type = 1;
                    if(confirm("确定提交吗？")) {
                        Common.getData('/overTime/submitJiaBan.do', jiabanData, function (rep) {
                            if (rep.flag) {
                                alert("提交成功！");
                                jiaban.selJiaBanList(1);
                            } else {
                                alert("提交失败！");
                            }
                        });
                    }
                });
                $(".edit-vt").click(function(){
                    jiabanData.overTimeId = $(this).attr('vtid');
                    Common.getData('/overTime/selSingleOverTime.do', jiabanData, function (rep) {
                        $('#jiabanUser').val(rep.rows.jbUserId);
                        $('#jiabanDate').val(rep.rows.date);
                        $('#startTime').val(rep.rows.startTime);
                        $('#endTime').val(rep.rows.endTime);
                        $('#cause').val(rep.rows.cause);
                        $('#shenheUser').val(rep.rows.shUserId);
                        $('#overTimeId').val(rep.rows.id);
                        $(".bg").show();
                        $(".ADDJB-POP").show();
                    });
                });
                $(".cexiao-vt").click(function(){
                    jiabanData.overTimeId = $(this).attr('vtid');
                    jiabanData.type = 0;
                    if(confirm("确定撤销吗？")) {
                        Common.getData('/overTime/submitJiaBan.do', jiabanData, function (rep) {
                            if (rep.flag) {
                                alert("撤销成功！");
                                jiaban.selJiaBanList(1);
                            } else {
                                alert("撤销失败！");
                            }
                        });
                    }
                });
            } else if (type==2) {
                $('.jiabanSalary').html('');
                Common.render({tmpl:$('#jiabanSalary_templ'),data:rep,context:'.jiabanSalary'});
                $(".salary").blur(function(){
                    jiabanData.salary = $(this).val();
                    jiabanData.overTimeId = $(this).attr("otid");
                    var re = /^[0-9]+.?[0-9]*$/;   //判断字符串是否为数字     //判断正整数 /^[1-9]+[0-9]*]*$/
                    if (!re.test($(this).val()))
                    {
                        alert("请输入数字");
                        $(this).val("");
                        return false;
                    }
                    Common.getData('/overTime/updateSalaryById.do', jiabanData, function (rep) {
                        if (rep.flag) {
                            alert("更新薪酬成功！");
                            jiaban.selJiaBanList(2);
                        } else {
                            alert("更新薪酬失败！");
                        }
                    });
                });
            } else if (type==3) {
                $('.sheheList').html('');
                Common.render({tmpl:$('#sheheList_templ'),data:rep,context:'.sheheList'});
                $(".tongguo").click(function(){
                    $('input:radio[name=check]').attr('checked',false);
                    $('input:radio[name=check]')[0].checked = true;
                    $('#shenheUser6').attr("disabled",true);
                    $('#overTimeId6').val($(this).attr('otid'));
                    $('#shenheUser6').val('');
                    $('.popup-SHR').show();
                    $('.bg').show();
                });
                $(".bohui").click(function(){
                    if (confirm('是否确认驳回！')) {
                        jiabanData.overTimeId = $(this).attr('otid');
                        jiabanData.type = 3;
                        Common.getData('/overTime/updOverTimeType.do', jiabanData, function (rep) {
                            if (rep.flag) {
                                alert("驳回成功！");
                                jiaban.selJiaBanList(3);
                            } else {
                                alert("驳回失败！");
                            }
                        });
                    }
                });
                $(".view-detail").click(function(){
                    jiabanData.overTimeId = $(this).attr('otid');
                    Common.getData('/overTime/selSingleOverTime.do', jiabanData, function (rep) {
                        $('#jiabanUser2').val(rep.rows.jbUserId);
                        $('#jiabanDate2').val(rep.rows.date);
                        $('#startTime2').val(rep.rows.startTime);
                        $('#endTime2').val(rep.rows.endTime);
                        $('#cause2').val(rep.rows.cause);
                        $('#shenheUser2').val(rep.rows.shUserId);
                        $(".bg").show();
                        $(".XXJB-POP").show();
                    });
                });
            }


        });
    }
    // 初始化select2
    jiaban.initSelect = function(target) {
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
    jiaban.getSelectTeacherList = function(target,callback) {
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
    /*
     * 上传校园安全附件信息
     * */
    jiaban.fileUpload = function(id) {
        //上传附件
        $('#file_attach').fileupload({
            url: '/duty/uploadattach.do',
            start: function(e) {
                $('#fileuploadLoading').show();
            },
            done: function(e, data) {
                if (data.dataType == 'iframe ') {
                    var response = $( 'pre', data.result ).text();
                } else {
                    var response = data.result;
                }
                try {
                    var ob = response;
                    if (ob.uploadType != 1) {
                        alert("附件上传失败！");
                    }
                    //sessionStorage.setItem("realname",ob.vurl);
                    //sessionStorage.setItem("filename",ob.name);
                    //$(".popup-bottom-LAB").html('');
                    var uf = '<label style="padding-left: 50px;" path="'+ob.vurl+'" nm="'+ob.name+'">'+ob.name+'<a class="yulan" style="padding-left:10px;" pth='+ob.vurl+'>预览</a>'+'<span  class="h-cursor del-file" style="padding-left:10px;">删除</span></label>';
                    $(".popup-bottom-LAB").append(uf);
                    $('.yulan').click(function() {
                        var url = $(this).attr("pth");
                        var urlarg = url.split(".")[url.split(".").length-1];
                        if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
                            window.open("http://ow365.cn/?i=9666&furl="+url);
                        } else {
                            window.open(url);
                        }
                    });
                    $(".del-file").click(function(){
                        //sessionStorage.clear();
                        $(this).parent().remove();
                    });
                } catch (err) {
                }
            },
            fail: function (e, data) {

            },
            always: function (e, data) {
                $('#fileuploadLoading').hide();
            }
        });
    }
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    jiaban.init();
});