/*
 * @Author: Tony
 * @Date:   2015-06-11 14:24:31
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-06-30 16:43:27
 */

'use strict';
define(['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var teaPlan = {},
        Common = require('common');
    //提交参数
    var teaPlanData = {};
    /*上传附件开始*/
    var files = [];
    var type = 0;
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * zhiban.init()
     */
    teaPlan.init = function(){
        if ($('#userId').val()!='') {
            $('.plan-set').css("display", "none");
            $('#termlist2').val($('#term').val());
        } else {
            $('.plan-set').css("display", "");
        }
        teaPlan.selTeachPlanList();
        //上传附件
        require.async("widget",function(widget) {
            require.async("fileupload", function (fileupload) {
                $('#file_attach').fileupload({
                    url: '/commonupload/doc/upload.do',
                    start: function (e) {
                        $('#fileuploadLoading').show();
                    },
                    done: function (e, data) {
                        var info = data.result.message[0];
                        files.push({name: info.fileName, "path": info.path});

                        $("#fileListShow").empty();
                        Common.render({
                            tmpl: $('#fileListJs'),
                            data: {data: files},
                            context: '#fileListShow'
                        });

                        $('#fileListShow').find('a').each(function(index, item){
                            var href = $(item).attr('href');
                            var fileKey = href.substring(href.lastIndexOf('/') + 1);
                            var fileName = $(this).attr("fn");

                            href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                            $(item).attr('href', href)
                        })
                        //$(".removeFile").click(function(){
                        //    var path = $(this).attr("did");
                        //    for (var i = 0; i < files.length; i++) {
                        //        if (files[i].path == path) {
                        //            files.splice(i, 1);
                        //            $("#fileListShow").empty();
                        //            Common.render({
                        //                tmpl: $('#fileListJs'),
                        //                data: {data: files},
                        //                context: '#fileListShow'
                        //            });
                        //            return;
                        //        }
                        //    }
                        //});
                    },
                    fail: function (e, data) {
                    },
                    always: function (e, data) {
                        $('#fileuploadLoading').hide();
                    }
                });
            });
        });
        $(".plan-newadd").click(function(){
            $('#title').text("新增教学计划");
            teaPlan.clearCss();
            teaPlan.clearValue();
            type = 0;
            $(".addplan-con,.next-path").show();
            $(".plan-con").hide();
        })
        $(".path-root,.newadd-con .btn-esc").click(function(){
            $(".addplan-con,.next-path").hide();
            $(".plan-con").show();
        })
        $(".btn-sure").click(function(){
            teaPlan.addUdpTeachPlan();
        });
        $(".plan-search").click(function(){
            teaPlan.selTeachPlanList();
        });

    };
    var currentNode;
    $("body").on("click",".removeFile",function(){
        $(".bg").css("display","block");
        $(".popup-info").css("display","block");
        $(".popup-op").html("确定要删除该附件吗？");
        currentNode=this;
    });
    $("body").on("click",".he_qx",function(){
        $(".bg").css("display","none");
        $(".popup-info").css("display","none");
    });
    $("body").on("click",".he_qd",function(){
        $(".bg").css("display","none");
        $(".popup-info").css("display","none");
        deleteFile();

    });
    function deleteFile() {
        var path = $(currentNode).attr("did");
        for (var i = 0; i < files.length; i++) {
            if (files[i].path == path) {
                files.splice(i, 1);
                $("#fileListShow").empty();
                Common.render({
                    tmpl: $('#fileListJs'),
                    data: {data: files},
                    context: '#fileListShow'
                });
                return;
            }
        }
    }
    teaPlan.selTeachPlanList = function() {
        teaPlanData.term = $('#termlist2').val();
        teaPlanData.planName = $('#planName2').val();
        teaPlanData.userId = $('#userId').val();
        Common.getData('/teach/selTeachPlanList.do',teaPlanData,function(rep){
            if (rep.code==200) {
                $('.planlist').html('');
                Common.render({tmpl: $('#planlist_templ'), data: rep, context: '.planlist'});
                $('.del-plan').click(function(){
                    if (confirm("确定要删除该条计划吗？")) {
                        teaPlan.delPlan($(this).parent().attr('pid'));
                    }
                });
                $('.view-plan').click(function(){
                    $('#title').text("教学计划详细");
                    teaPlan.selSinglePlan($(this).parent().attr('pid'),2);
                    type = 2;
                });
                $('.udp-plan').click(function(){
                    $('#title').text("编辑教学计划");
                    teaPlan.selSinglePlan($(this).parent().attr('pid'),1);
                    type = 1;
                });
            }
        });
    }
    teaPlan.delPlan = function(id) {
        teaPlanData.id = id;
        Common.getData('/teach/delPlan.do',teaPlanData,function(rep){
            alert(rep.message);
            if (rep.code==200) {
                teaPlan.selTeachPlanList();
            }
        });
    }
    teaPlan.selSinglePlan = function(id,type) {
        teaPlanData.id = id;
        Common.getData('/teach/selSinglePlan.do',teaPlanData,function(rep){
            teaPlan.clearCss();
            teaPlan.clearValue();
            $(".addplan-con,.next-path").show();
            $(".plan-con").hide();
            $('#planName').val(rep.rows.plainName);
            $('#termlist').val(rep.rows.term);
            $('#planid').val(rep.rows.id);
            UE.getEditor('contentShow').setContent(rep.rows.content);
            files = [];
            if (rep.rows.docList!=null && rep.rows.docList.length!=0) {
                for(var i=0;i<rep.rows.docList.length;i++) {
                    files.push({name: rep.rows.docList[i].name, "path": rep.rows.docList[i].value});
                }
                $("#fileListShow").empty();
                Common.render({
                    tmpl: $('#fileListJs'),
                    data: {data: files},
                    context: '#fileListShow'
                });

                $('#fileListShow').find('a').each(function(index, item){
                    var href = $(item).attr('href');
                    var fileKey = href.substring(href.lastIndexOf('/') + 1);
                    var fileName = $(this).attr("fn");

                    href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                    $(item).attr('href', href)
                });
                //$(".removeFile").click(function(){
                //    var path = $(this).attr("did");
                //    for (var i = 0; i < files.length; i++) {
                //        if (files[i].path == path) {
                //            files.splice(i, 1);
                //            $("#fileListShow").empty();
                //            Common.render({
                //                tmpl: $('#fileListJs'),
                //                data: {data: files},
                //                context: '#fileListShow'
                //            });
                //            return;
                //        }
                //    }
                //});
            }
            if (type==2) {
                $("#termlist").attr("disabled", true);
                $("#planName").attr("disabled", true);
                $(".fileAtt").css("display", "none");
                $(".btn-wrap").css("display", "none");
                $(".removeFile").css("display", "none");
            }
        });
    }

    teaPlan.clearCss = function() {
        $("#termlist").attr("disabled", false);
        $("#planName").attr("disabled", false);
        $(".fileAtt").css("display", "");
        $(".btn-wrap").css("display", "");
        $(".removeFile").css("display", "");
    }

    teaPlan.clearValue = function() {
        //$("#termlist").val('');
        $("#planName").val('');
        $("#fileListShow").empty();
        UE.getEditor('contentShow').setContent("");
        files = [];
    }

    //增加教学计划
    teaPlan.addUdpTeachPlan = function() {
        if (type==2) {
            $(".addplan-con,.next-path").hide();
            $(".plan-con").show();
            teaPlan.selTeachPlanList();
            return;
        }
        teaPlanData.id = $('#planid').val();
        teaPlanData.type = type;
        var content = UE.getEditor('contentShow').getContent();
        teaPlanData.plainName = $('#planName').val();
        teaPlanData.term = $('#termlist').val();
        teaPlanData.content = content;
        if ($('#planName').val()=="") {
            alert("请输入计划名称");
            return;
        }
        if (content == "") {
            alert("请输入教学计划");
            return;
        }
        var docNames = "";
        var docAddress = "";
        for (var i = 0; i < files.length; i++) {
            if (i == files.length - 1) {
                docNames += files[i].name;
                docAddress += files[i].path;
            }
            else {
                docNames += files[i].name + ",";
                docAddress += files[i].path + ",";
            }
        }
        teaPlanData.docNames = docNames;
        teaPlanData.docAddress = docAddress;
        Common.getData('/teach/addUdpTeachPlan.do',teaPlanData,function(rep){
            alert(rep.message);
            if (rep.code==200) {
                $(".addplan-con,.next-path").hide();
                $(".plan-con").show();
                teaPlan.selTeachPlanList();
            }
        });
    }

    teaPlan.init();
});