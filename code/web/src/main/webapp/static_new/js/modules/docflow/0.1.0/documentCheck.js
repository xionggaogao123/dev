/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['jquery','common'],function (require, exports, module) {
    /**
     *初始化参数
     */
    /*require("jquery");
    require("doT");*/
    var documentCheck = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    documentCheck.init = function () {
        if (GetQueryString("a")!="10000") {


        }
    };

    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }

    var currentNode;
    $("body").on("click",".removeFile",function(){
        $(".popup-head").css("display","block");
        $(".popup-info").css("display","block");
        $(".popup-op").html("确定要删除该附件吗？");
        currentNode=this;
    });
    $("body").on("click",".he_qx",function(){
        $(".popup-head").css("display","none");
        $(".popup-info").css("display","none");
    });
    $("body").on("click",".he_qd",function(){
        $(".popup-head").css("display","none");
        $(".popup-info").css("display","none");
        deleteFile();

    });
    function deleteFile()
    {
        var path = $(currentNode).attr("did");
        var parent = $(currentNode).parent().attr("id");
        if (parent == "fileListShow_TY") {
            for (var i = 0; i < file_TY.length; i++) {
                if (file_TY[i].path == path) {
                    file_TY.splice(i, 1);
                    $("#fileListShow_TY").empty();
                    Common.render({
                        tmpl: $('#fileListJs'),
                        data: {data: file_TY},
                        context: '#fileListShow_TY'
                    });
                    return;
                }
            }
        }
        else if (parent == "fileListShow_BH") {
            for (var i = 0; i < file_BH.length; i++) {
                if (file_BH[i].path == path) {
                    file_BH.splice(i, 1);
                    $("#fileListShow_BH").empty();
                    Common.render({
                        tmpl: $('#fileListJs'),
                        data: {data: file_TY},
                        context: '#fileListShow_BH'
                    });
                    return;
                }
            }
        }
        else if (parent == "fileListShow_FB") {
            for (var i = 0; i < file_FB.length; i++) {
                if (file_FB[i].path == path) {
                    file_FB.splice(i, 1);
                    $("#fileListShow_BH").empty();
                    Common.render({
                        tmpl: $('#fileListJs'),
                        data: {data: file_TY},
                        context: '#fileListShow_BH'
                    });
                    return;
                }
            }
        }
        else if (parent == "fileListShow_FQ") {
            for (var i = 0; i < file_FQ.length; i++) {
                if (file_FQ[i].path == path) {
                    file_FQ.splice(i, 1);
                    $("#fileListShow_BH").empty();
                    Common.render({
                        tmpl: $('#fileListJs'),
                        data: {data: file_TY},
                        context: '#fileListShow_BH'
                    });
                    return;
                }
            }
        }
        else if (parent == "fileListShow_ZJ") {
            for (var i = 0; i < file_ZJ.length; i++) {
                if (file_ZJ[i].path == path) {
                    file_ZJ.splice(i, 1);
                    $("#fileListShow_BH").empty();
                    Common.render({
                        tmpl: $('#fileListJs'),
                        data: {data: file_TY},
                        context: '#fileListShow_BH'
                    });
                    return;
                }
            }
        }
    }
    documentCheck.getDocumentDetail = function () {
        var docId = $("body").attr("docid");
        $.ajax({
            url: "/docflow/getdocdetail.do",
            type: "GET",
            dataType: "json",
            data: {
                docId: docId
            },
            success: function (data) {
                //跳转链接
                if (GetQueryString("a")!="10000") {
                    $("#goToHref").attr("href","/docflow/documentModify.do?docId="+data.id+"&type=1&version=51");
                } else {
                    $("#goToHref").attr("href","/docflow/documentModify.do?docId="+data.id+"&type=1&a=10000&version=51");
                }

                //标题
                $("#titleShow").html(data.title);
                //部门以及日期
                Common.render({tmpl: $('#departmentJs'), data: data, context: '.Bt'});
                //正文
                $("#contentShow").html(data.content);
                //审阅历史
                Common.render({
                    tmpl: $('#checkHistoryJs'),
                    data: {data: data.checkDTOList},
                    context: '#checkHistoryShow'
                });
                //附件列表
                Common.render({tmpl: $('#fileJs'), data: {data: data.docList}, context: '#Fj_show'});
                $('#Fj_show').find('a').each(function(index, item){
                    var href = $(item).attr('href');
                    var fileKey = href.substring(href.lastIndexOf('/') + 1);
                    var fileName = $(this).attr("fn");

                    href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                    $(item).attr('href', href)
                })

                //驳回列表
                getRejectList(data.checkDTOList);
            }
        });
    };
    //生成驳回列表
    function getRejectList(list)
    {
        var resultList=[];
        var currentId="";
        //先将list按id时间排序
        for(var i=0;i<list.length;i++)
        {
            if(list[i].opinion!=1)
            {
                if (list[i].userName != $("body").attr("uid")) {//非本人
                    var ifHave=false;
                    for (var j = resultList.length - 1; j > 0; j--) {//去除重复
                        if (resultList[j].userId == list[i].userId) {
                            ifHave=true;
                            break;
                        }
                    }
                    if(!ifHave)
                    {
                        resultList.push(list[i]);
                    }
                }
            }
            else{
                if(i!=list.length-1) {
                    currentId = list[i + 1].userId;
                    i = i + 1;
                    for (var j = resultList.length - 1; j > 0; j--) {
                        if (resultList[j].userId != currentId) {
                            resultList.splice(j, 1);
                            j--;
                        }
                        else {
                            break;
                        }
                    }
                }
                else{
                    currentId = $("body").attr("uid");
                    for (var j = resultList.length - 1; j > 0; j--) {
                        if (resultList[j].userName != currentId) {
                            resultList.splice(j, 1);
                            j--;
                        }
                        else {
                            break;
                        }
                    }
                }
            }
        }
        var result=[];
        for(var i=0;i<resultList.length;i++)
        {
            for(var j=0;j<result.length;j++)
            {
                if(result[j].userId==resultList[i].userId)
                {
                    result.splice(j,1);
                    break;
                }
            }
            result.push(resultList[i]);
        }
        if(result.length==0)//没有驳回人，不显示驳回
        {
            $("#BH").css("display","none");
            return;
        }
        //result.reverse();
        Common.render({tmpl: $('#historyManJs'), data: {data: result}, context: '#BH_select'});
    }

    /*审阅人选择开始*/
    var checkManId = "";
    var baseCheckManId_TY = "";
    var checkManName = "";
    var baseCheckManName_TY = "";
    var checkDepartmentId = "";
    var preCheckDepartmentId = "";
    var baseCheckDepartmentId_TY = "";

    var baseCheckManId_ZJ = "";
    var baseCheckManName_ZJ = "";
    var baseCheckDepartmentId_ZJ = "";

    var type = 0;//type为0：选择同意并转发接受人；type为1 ：选择转寄下一位接收人
    var departmentAndSubjectUser = {
        "department": [],
        "subject": []
    };
    var file_TY = [];
    var file_BH = [];
    var file_FB = [];
    var file_FQ = [];
    var file_ZJ = [];
    documentCheck.getUserList = function () {
        $.ajax(
            {
                url: "/docflow/getdepartmentandsubjectusers.do",
                type: "get",
                dataType: "json",
                success: function (data) {
                    departmentAndSubjectUser.department = data["department"];
                    departmentAndSubjectUser.subject = data["subject"];
                }
            }
        );
    };
    //获取部门列表
    function getDepartmentList() {
        var list = [];
        for(var i=0;i<departmentAndSubjectUser.department.length;i++)
            list.push( departmentAndSubjectUser.department[i]);
        /*if (list.length == 0) {//数据还未获取到，再次执行
            documentCheck.getUserList();
            for(var i=0;i<departmentAndSubjectUser.department.length;i++)
                list.push( departmentAndSubjectUser.department[i]);
        }*/
        $(".K1show").empty();
        Common.render({tmpl: $('#K1JS'), data: {data: list, type: "部门"}, context: '.K1show'});
        $(".group2").eq(0).addClass('current').siblings('li').removeClass('current');
        if(list.length>0)
        {
            var dep = $(".group2").eq(0).attr("dep");
            preCheckDepartmentId = dep;
        }
        getPeopleList("部门", 0);
    }

    //获取人员列表
    function getPeopleList(type, index) {
        var list = [];
        if (type == "部门") {
            for(var i=0;i<departmentAndSubjectUser.department[index].list.length;i++)
                list.push( departmentAndSubjectUser.department[index].list[i]);
        }
        else if (type == "学科") {
            for(var i=0;i<departmentAndSubjectUser.subject[index].list.length;i++)
                list.push( departmentAndSubjectUser.subject[index].list[i]);
        }

        for (var i = 0; i < list.length; i++) {
            //作者本人不可审阅
            if (list[i].value == $("body").attr("uid")) {
                list.splice(i, 1);
                i--;
            }
            else {
                if (list[i].idStr == checkManId) {
                    list[i].choosed = 1;
                }
                else {
                    list[i].choosed = 0;
                }
            }
        }
        $(".K2show").empty();
        Common.render({tmpl: $('#K2JS'), data: {data: list}, context: '.K2show'});
    }
    /*审阅人*/
    $(function () {
        var L = 0;
        var T = 0;
        L = $(window).width() / 2 - $(".Tanchu2").width() / 2;
        T = $(window).height() / 2 - $(".Tanchu2").height() / 2;
        $(".Tanchu2").css({
            'left': L,
            'top': T
        });
        $(".gay").height($(document).height());
        $(".tab_TX").click(function (event) {
            $(".gay,.Tanchu2").fadeIn();
            var id = $(this).attr("id");
            if (id == "txl_TY")
                checkManId = baseCheckManId_TY;
            else if (id == "txl_ZJ")
                checkManId = baseCheckManId_ZJ;
            getDepartmentList();

        });
        $(".Tanchu2 .qx2").click(function (event) {
            $(".gay,.Tanchu2").fadeOut();
            checkManName = "";
            $("#checkPeople").val("");
        });
    });
    documentCheck.checkDocument = function (docId, handleType, nextMan,
                                            nextDepartment, remark, docNames, docAddress) {
        $.ajax({
            url: "/docflow/checkdoc.do",
            type: "POST",
            dataType: "json",
            data: {
                docId: docId,
                handleType: handleType,
                receiveId: nextMan,
                receiveDepartmentId: nextDepartment,
                remark: remark,
                docNames: docNames,
                docAddress: docAddress
            },
            success: function (data) {
                if (data == true) {
                    alert("审核成功");
                    if (GetQueryString("a")!="10000") {
                        location.href = "/docflow/documentList.do?type=1&version=51";
                    } else {
                        location.href = "/docflow/documentList.do?type=1&a=10000&version=51";
                    }
                }
                else {
                    alert(data);
                }
            }
        })
    };
    documentCheck.getUnreadCount=function()
    {
        $.ajax({
            url:"/docflow/getpromote.do",
            type:"GET",
            success:function(data)
            {
                $("#unReadCountShow").empty();
                Common.render({tmpl:"#unReadCountJs",data:data,context:"#unReadCountShow"});
            }
        });
    };
    documentCheck.getUnreadCount();
    documentCheck.init();
    documentCheck.getUserList();
    documentCheck.getDocumentDetail();
    /*弹出层部分结束*/
    $(document).ready(function () {
        //同意并转发
        $("body").on("click", "#TY", function () {
            $(".tab-TY").css("display", "none");
            $("#tab-TY").css("display", "block");
            $(".tab_BU button").addClass("tab_HV").siblings().removeClass("tab_HV");
            $("#TY").addClass("tab_HV");
            type = 0;
        });
        //驳回
        $("body").on("click", "#BH", function () {
            $(".tab-TY").css("display", "none");
            $("#tab-BH").css("display", "block");
            $(".tab_BU button").addClass("tab_HV").siblings().removeClass("tab_HV");
            $("#BH").addClass("tab_HV");
        });
        //结束流程并发布
        $("body").on("click", "#FB", function () {
            $(".tab-TY").css("display", "none");
            $("#tab-FB").css("display", "block");
            $(".tab_BU button").addClass("tab_HV").siblings().removeClass("tab_HV");
            $("#FB").addClass("tab_HV");
        });


        //废弃
        $("body").on("click", "#FQ", function () {
            $(".tab-TY").css("display", "none");
            $("#tab-FQ").css("display", "block");
            $(".tab_BU button").addClass("tab_HV").siblings().removeClass("tab_HV");
            $("#FQ").addClass("tab_HV");
        });
        //转寄
        $("body").on("click", "#ZJ", function () {
            $(".tab-TY").css("display", "none");
            $("#tab-ZJ").css("display", "block");
            $(".tab_BU button").addClass("tab_HV").siblings().removeClass("tab_HV");
            $("#ZJ").addClass("tab_HV");
            type = 1;
        });

        //审阅人一级列表
        $("body").on("click", ".group1", function () {
            /*$(this).addClass('current').siblings('li').removeClass('current');
             var tag = $(this).attr("tag");
             if (tag == "dep")//部门
             getDepartmentList();
             else if (tag == "sub")//学科
             getSubjectList();*/
        });
        //审阅人二级列表
        $("body").on("click", ".group2", function () {
            $(this).addClass('current').siblings('li').removeClass('current');
            var tag = $(this).attr("tag");
            var index = $(this).attr("index");
            var dep = $(this).attr("dep");
            preCheckDepartmentId = dep;
            //checkDepartmentId = dep;
            getPeopleList(tag, index);
        });
        //审阅人三级列表
        $("body").on("click", ".group3", function () {
            var id = $(this).attr("userId");
            var name = $(this).attr("name");
            if ($(this).hasClass("current"))//已被选中
            {
                $("#choosedMan").text("未选择");
                checkManId = "";
                checkManName = "";
                $(this).removeClass("current");
            }
            else {
                $("#choosedMan").text("@" + name);
                checkManId = id;
                checkDepartmentId=preCheckDepartmentId;
                checkManName = name;
                $(this).addClass('current').siblings().removeClass("current");
            }
        });
        $("body").on("click", ".QD2", function () {
            if (type == 0) {
                baseCheckManId_TY = checkManId;
                baseCheckManName_TY = checkManName;
                baseCheckDepartmentId_TY = checkDepartmentId;
                $(".checkPeople_TY").val(baseCheckManName_TY);
            }
            else if (type == 1) {
                baseCheckManId_ZJ = checkManId;
                baseCheckManName_ZJ = checkManName;
                baseCheckDepartmentId_ZJ = checkDepartmentId;
                $(".checkPeople_ZJ").val(baseCheckManName_ZJ);
            }
            $(".gay,.Tanchu2").fadeOut();
        });


        //删除附件
        /*$("body").on("click", ".removeFile", function () {

        });*/
        //审阅操作开始
        //同意并转发
        $("body").on("click", "#TY_btn", function () {
            var docId = $("body").attr("docid");
            var ManId = baseCheckManId_TY;
            var departmentId = baseCheckDepartmentId_TY;
            var opinion = 0;
            var reason = $("#TY_reason").val();
            var docNames = "";
            var docAddress = "";
            for (var i = 0; i < file_TY.length; i++) {
                if (i == file_TY.length - 1) {
                    docNames += file_TY[i].name;
                    docAddress += file_TY[i].path;
                }
                else {
                    docNames += file_TY[i].name + ",";
                    docAddress += file_TY[i].path + ",";
                }
            }
            if (ManId == "") {
                alert("请选择下一位审阅人");
                return;
            }
            if (reason == "") {
                alert("请填写备注说明");
                return;
            }
            documentCheck.checkDocument(docId, opinion, ManId, departmentId, reason, docNames, docAddress);
        });
        //驳回
        $("body").on("click", "#BH_btn", function () {
            var docId = $("body").attr("docid");
            var checkInfo = $("#BH_select").val();
            var ManId = "";
            var departmentId = "";
            if (checkInfo == "") {
                alert("请选择历史审阅人");
                return;
            }
            else {
                var arr = checkInfo.split("/");
                ManId = arr[0];
                if (arr.length == 2)
                    departmentId = arr[1];
            }

            var opinion = 1;
            var reason = $("#BH_reason").val();
            var docNames = "";
            var docAddress = "";
            for (var i = 0; i < file_BH.length; i++) {
                if (i == file_BH.length - 1) {
                    docNames += file_BH[i].name;
                    docAddress += file_BH[i].path;
                }
                else {
                    docNames += file_BH[i].name + ",";
                    docAddress += file_BH[i].path + ",";
                }
            }
            if (reason == "") {
                alert("请填写备注说明");
                return;
            }
            documentCheck.checkDocument(docId, opinion, ManId, departmentId, reason, docNames, docAddress);
        });
        //结束流程并刚发布
        $("body").on("click", "#FB_btn", function () {
            var docId = $("body").attr("docid");
            var ManId = "";
            var departmentId = "";
            var opinion = 2;
            var reason = $("#FB_reason").val();
            var docNames = "";
            var docAddress = "";
            for (var i = 0; i < file_FB.length; i++) {
                if (i == file_FB.length - 1) {
                    docNames += file_FB[i].name;
                    docAddress += file_FB[i].path;
                }
                else {
                    docNames += file_FB[i].name + ",";
                    docAddress += file_FB[i].path + ",";
                }
            }
            if (reason == "") {
                alert("请填写备注说明");
                return;
            }
            documentCheck.checkDocument(docId, opinion, ManId, departmentId, reason, docNames, docAddress);
        });
        //废弃
        $("body").on("click", "#FQ_btn", function () {
            var docId = $("body").attr("docid");
            var ManId = "";
            var departmentId = "";
            var opinion = 3;
            var reason = $("#FQ_reason").val();
            var docNames = "";
            var docAddress = "";
            for (var i = 0; i < file_FQ.length; i++) {
                if (i == file_FQ.length - 1) {
                    docNames += file_FQ[i].name;
                    docAddress += file_FQ[i].path;
                }
                else {
                    docNames += file_FQ[i].name + ",";
                    docAddress += file_FQ[i].path + ",";
                }
            }
            if (reason == "") {
                alert("请填写备注说明");
                return;
            }
            documentCheck.checkDocument(docId, opinion, ManId, departmentId, reason, docNames, docAddress);
        });
        //转寄
        $("body").on("click", "#ZJ_btn", function () {
            var docId = $("body").attr("docid");
            var ManId = baseCheckManId_ZJ;
            var departmentId = baseCheckDepartmentId_ZJ;
            var opinion = 4;
            var reason = $("#ZJ_reason").val();
            var docNames = "";
            var docAddress = "";
            for (var i = 0; i < file_ZJ.length; i++) {
                if (i == file_ZJ.length - 1) {
                    docNames += file_ZJ[i].name;
                    docAddress += file_ZJ[i].path;
                }
                else {
                    docNames += file_ZJ[i].name + ",";
                    docAddress += file_ZJ[i].path + ",";
                }
            }
            if (ManId == "") {
                alert("请选择转寄人");
                return;
            }
            if (reason == "") {
                alert("请填写备注说明");
                return;
            }
            documentCheck.checkDocument(docId, opinion, ManId, departmentId, reason, docNames, docAddress);
        });
        //上传附件
        require.async("widget",function(widget) {
            require.async("fileupload", function (fileupload) {
                $('#TY_fj').fileupload({
                    url: '/commonupload/doc/upload.do',
                    start: function (e) {
                        $('#fileuploadLoading').show();
                    },
                    done: function (e, data) {
                        var info = data.result.message[0];
                        file_TY.push({name: info.fileName, "path": info.path, "userName": "我上传的"});
                        $("#fileListShow_TY").empty();
                        Common.render({
                            tmpl: $('#fileListJs'),
                            data: {data: file_TY},
                            context: '#fileListShow_TY'
                        });
                        $('#fileListShow_TY').find('a').each(function(index, item){
                            var href = $(item).attr('href');
                            var fileKey = href.substring(href.lastIndexOf('/') + 1);
                            var fileName = $(this).attr("fn");

                            href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                            $(item).attr('href', href)
                        })
                    },
                    fail: function (e, data) {
                    },
                    always: function (e, data) {
                        $('#fileuploadLoading').hide();
                    }
                });
                $('#BH_fj').fileupload({
                    url: '/commonupload/doc/upload.do',
                    start: function (e) {
                        $('#fileuploadLoading').show();
                    },
                    done: function (e, data) {
                        var info = data.result.message[0];
                        file_BH.push({name: info.fileName, "path": info.path, "userName": "我上传的"});
                        $("#fileListShow_BH").empty();
                        Common.render({
                            tmpl: $('#fileListJs'),
                            data: {data: file_BH},
                            context: '#fileListShow_BH'
                        });
                        $('#fileListShow_BH').find('a').each(function(index, item){
                            var href = $(item).attr('href');
                            var fileKey = href.substring(href.lastIndexOf('/') + 1);
                            var fileName = $(this).attr("fn");

                            href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                            $(item).attr('href', href)
                        })
                    },
                    fail: function (e, data) {
                    },
                    always: function (e, data) {
                        $('#fileuploadLoading').hide();
                    }
                });
                $('#FB_fj').fileupload({
                    url: '/commonupload/doc/upload.do',
                    start: function (e) {
                        $('#fileuploadLoading').show();
                    },
                    done: function (e, data) {
                        var info = data.result.message[0];
                        file_FB.push({name: info.fileName, "path": info.path, "userName": "我上传的"});
                        $("#fileListShow_FB").empty();
                        Common.render({
                            tmpl: $('#fileListJs'),
                            data: {data: file_FB},
                            context: '#fileListShow_FB'
                        });
                        $('#fileListShow_FB').find('a').each(function(index, item){
                            var href = $(item).attr('href');
                            var fileKey = href.substring(href.lastIndexOf('/') + 1);
                            var fileName = $(this).attr("fn");

                            href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                            $(item).attr('href', href)
                        })
                    },
                    fail: function (e, data) {
                    },
                    always: function (e, data) {
                        $('#fileuploadLoading').hide();
                    }
                });
                $('#FQ_fj').fileupload({
                    url: '/commonupload/doc/upload.do',
                    start: function (e) {
                        $('#fileuploadLoading').show();
                    },
                    done: function (e, data) {
                        var info = data.result.message[0];
                        file_FQ.push({name: info.fileName, "path": info.path, "userName": "我上传的"});
                        $("#fileListShow_FQ").empty();
                        Common.render({
                            tmpl: $('#fileListJs'),
                            data: {data: file_FQ},
                            context: '#fileListShow_FQ'
                        });
                        $('#fileListShow_FQ').find('a').each(function(index, item){
                            var href = $(item).attr('href');
                            var fileKey = href.substring(href.lastIndexOf('/') + 1);
                            var fileName = $(this).attr("fn");

                            href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                            $(item).attr('href', href)
                        })
                    },
                    fail: function (e, data) {
                    },
                    always: function (e, data) {
                        $('#fileuploadLoading').hide();
                    }
                });
                $('#ZJ_fj').fileupload({
                    url: '/commonupload/doc/upload.do',
                    start: function (e) {
                        $('#fileuploadLoading').show();
                    },
                    done: function (e, data) {
                        var info = data.result.message[0];
                        file_ZJ.push({name: info.fileName, "path": info.path, "userName": "我上传的"});
                        $("#fileListShow_ZJ").empty();
                        Common.render({
                            tmpl: $('#fileListJs'),
                            data: {data: file_ZJ},
                            context: '#fileListShow_ZJ'
                        });
                        $('#fileListShow_ZJ').find('a').each(function(index, item){
                            var href = $(item).attr('href');
                            var fileKey = href.substring(href.lastIndexOf('/') + 1);
                            var fileName = $(this).attr("fn");

                            href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                            $(item).attr('href', href)
                        })
                    },
                    fail: function (e, data) {
                    },
                    always: function (e, data) {
                        $('#fileuploadLoading').hide();
                    }
                });
            });
        });
    });



});