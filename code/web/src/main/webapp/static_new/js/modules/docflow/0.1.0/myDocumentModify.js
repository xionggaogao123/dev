/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['jquery', 'common'], function (require, exports, module) {
    /**
     *初始化参数
     */
    /*require("jquery");
     require("doT");*/
    /*    require("fileupload");*/
    var myDocumentModify = {},
        Common = require('common');

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    myDocumentModify.init = function () {
        if (GetQueryString("a")!="10000") {


        }
    };
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    /*弹出层部分开始*/
    $(function () {
        $(".Tanchu1 .xuanren ul li").click(function (event) {
            $(this).toggleClass('current');
        });
        $(".Tanchu2 .xuanren ul.K2 li").click(function (event) {
            $(this).toggleClass('current').siblings().removeClass('current');
        });
    });
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
        for (var i = 0; i < files.length; i++) {
            if (files[i].value == path) {
                files.splice(i, 1);
                showFileList();
                return;
            }
        }
    }

    /*发布范围*/
    $(function () {
        var L = 0;
        var T = 0;
        L = $(window).width() / 2 - $(".Tanchu1").width() / 2;
        T = $(window).height() / 2 - $(".Tanchu1").height() / 2;
        $(".Tanchu1").css({
            'left': L,
            'top': T
        });
        $(".gay").height($(document).height());
        $(".ts1 button").click(function (event) {
            $(".gay,.Tanchu1").fadeIn();
            getOnlyDepartmentList();
        });
        $(".Tanchu1 .qx1").click(function (event) {
            $(".gay,.Tanchu1").fadeOut();
            showPublishOnMain();
        });
    });
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
        $(".ts2 button").click(function (event) {
            $(".gay,.Tanchu2").fadeIn();
            checkManId = baseCheckManId;
            getDepartmentList();

        });
        $(".Tanchu2 .qx2").click(function (event) {
            $(".gay,.Tanchu2").fadeOut();
            checkManName = "";
            $("#checkPeople").val(baseCheckManName);
        });
    });
    /*弹出层部分结束*/


    /**
     * 获取公文详情
     */
    myDocumentModify.getDocumentDetail = function () {
        var docId = $("body").attr("docid");
        $.ajax({
            url: "/docflow/getdocdetail.do",
            type: "GET",
            dataType: "json",
            data: {
                docId: docId
            },
            success: function (data) {
                $("#titleShow").val(data.title);
                $(".te-Mre").html("撰写日期： " + data.time);
                //$(".CJ-Mre").val(data.content);
                UE.getEditor('contentShow').setContent(data.content);
                //显示审阅历史
                Common.render({
                    tmpl: $("#checkHistoryJs"),
                    data: {data: data.checkDTOList},
                    context: "#checkHistoryShow"
                });
                //显示附件列表
                //$(".Mre-SC").empty();
                files = data.docList;
                showFileList();
                //Common.render({tmpl:$("#fileListJs"),data:{data:files},context:".Mre-SC"});

                var choosedUsed = data.publishList;
                for (var i = 0; i < choosedUsed.length; i++) {
                    prePublishList.push(choosedUsed[i]);
                }
                myDocumentModify.getUserList();
                preDepartmentId = data.departmentId;
                myDocumentModify.getAllDepartment();
            }
        });
    };

    /**
     * 新开始
     */
    var preDepartmentId = "";//之前的发布部门
    myDocumentModify.getAllDepartment = function () {
        $.ajax({
            url: "/docflow/getdepartmentlist.do",
            type: "get",
            success: function (data) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i].id == preDepartmentId) {
                        data[i].select = 1;
                    }
                    else {
                        data[i].select = 0;
                    }
                }

                Common.render({tmpl: $('#departmentJs'), data: {data: data}, context: '#departmentShow'});
            }
        })
    };
    myDocumentModify.getUserList = function () {
        $.ajax(
            {
                url: "/docflow/getdepartmentandsubjectusers.do",
                type: "get",
                dataType: "json",
                success: function (data) {
                    departmentAndSubjectUser.department = data["department"];
                    departmentAndSubjectUser.subject = data["subject"];
                    var userlist = departmentAndSubjectUser.department;
                    for (var i = 0; i < userlist.length; i++) {
                        for (var j = 0; j < userlist[i].list.length; j++) {
                            if (checkIfHave(userlist[i].list[j].idStr, prePublishList)) {
                                if (!checkIfChoosed(userlist[i].list[j].idStr, baseChoosedPeople.data)) {
                                    baseChoosedPeople.data.push({
                                        "id": userlist[i].list[j].idStr,
                                        "name": userlist[i].list[j].value
                                    });
                                }
                            }
                        }
                    }
                    showPublishOnMain();
                }
            }
        );
    };
    function checkIfHave(v, arr) {
        for (var i = 0; i < arr.length; i++) {
            if (v == arr[i]) {
                return true;
            }
        }
        return false;
    }

    /*审阅人选择开始*/
    var checkManId = "";
    var baseCheckManId = "";
    var checkManName = "";
    var baseCheckManName = "";
    var checkDepartmentId = "";
    var preCheckDepartmentId = "";
    var baseCheckDepartmentId = "";

    //获取部门列表
    function getDepartmentList() {
        var list = departmentAndSubjectUser.department;
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

    //获取学科列表
    function getSubjectList() {
        var list = departmentAndSubjectUser.subject;
        $(".K1show").empty();
        Common.render({tmpl: $('#K1JS'), data: {data: list, type: "学科"}, context: '.K1show'});
        $(".group2").eq(0).addClass('current').siblings('li').removeClass('current');
        getPeopleList("学科", 0);
    }

    //获取人员列表
    function getPeopleList(type, index) {
        var list = [];
        if (type == "部门") {
            for (var i = 0; i < departmentAndSubjectUser.department[index].list.length; i++) {
                list.push(departmentAndSubjectUser.department[index].list[i]);
            }
        }
        else if (type == "学科") {
            for (var i = 0; i < departmentAndSubjectUser.subject[index].list.length; i++) {
                list.push(departmentAndSubjectUser.subject[index].list[i]);
            }
        }
        for (var i = 0; i < list.length; i++) {
            //作者本人不可审阅
            if (list[i].value == $("body").attr("uid")) {
                list.splice(i, 1);
                i--;
            }
            if (list[i].idStr == checkManId) {
                list[i].choosed = 1;
            }
            else {
                list[i].choosed = 0;
            }
        }
        $(".K2show").empty();
        Common.render({tmpl: $('#K2JS'), data: {data: list}, context: '.K2show'});
    }

    //发布范围显示---主页面显示
    function showPublishOnMain() {
        var allPeople = "";
        for (var i = 0; i < baseChoosedPeople.data.length; i++) {
            if (i < baseChoosedPeople.data.length - 1) {
                allPeople += baseChoosedPeople.data[i].name + ",";
            }
            else {
                allPeople += baseChoosedPeople.data[i].name;
            }
        }
        $("#choosedPeople").val(allPeople);
    }

    /*审阅人选择结束*/


    var departmentAndSubjectUser = {
        "department": [],
        "subject": []
    };
    /*发布范围选择开始*/
    var choosedPeople = {data: []};
    var baseChoosedPeople = {data: []};
    var prePublishList = [];//之前的发布范围
    //获取发布范围显示数据
    function getOnlyDepartmentList() {
        choosedPeople = {data: []};
        for (var i = 0; i < baseChoosedPeople.data.length; i++) {
            choosedPeople.data.push(baseChoosedPeople.data[i]);
        }
        var userlist = departmentAndSubjectUser.department;
        //初始化是否被选中
        for (var i = 0; i < userlist.length; i++) {
            for (var j = 0; j < userlist[i].list.length; j++) {
                if (checkIfChoosed(userlist[i].list[j].idStr, choosedPeople.data)) {
                    userlist[i].list[j].choosed = 1;
                }
                else {
                    userlist[i].list[j].choosed = 0;
                }
            }
        }
        $(".publishShow").empty();
        Common.render({tmpl: $('#publishJs'), data: {data: userlist}, context: '.publishShow'});
        choosedPeopleShow();
    }

    //判断该user是否已经被选择
    function checkIfChoosed(id, arr) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].id == id) {
                return true;
            }
        }
        return false;
    }

    //添加选择的好友----发布范围
    function addPeople(id, name) {
        for (var i = 0; i < choosedPeople.data.length; i++) {
            if (choosedPeople.data[i].id == id) {
                return;
            }
        }
        choosedPeople.data.push({"id": id, "name": name});
        choosedPeopleShow();
    }

    //删除选择的好友
    function removePeople(id) {
        for (var i = 0; i < choosedPeople.data.length; i++) {
            if (choosedPeople.data[i].id == id) {
                choosedPeople.data.splice(i, 1);
                choosedPeopleShow();
                return;
            }
        }
    }

    //显示已经选择的好友
    function choosedPeopleShow() {
        var list = choosedPeople.data;
        if (list.length == 0) {
            $("#Hm").empty();
            $("#Hm").append("<em>未选择</em>");
            return;
        }
        $("#Hm").empty();
        Common.render({tmpl: $('#choosedPeopleJs'), data: {data: list}, context: '#Hm'});
    }

    /*发布范围结束*/


    /*上传附件开始*/
    var files = [];

    function showFileList() {
        $(".Mre-SC").empty();
        Common.render({
            tmpl: $('#fileListJs'),
            data: {data: files},
            context: '.Mre-SC'
        });
        $('.Mre-SC').find('a').each(function(index, item){
            var href = $(item).attr('href');
            var fileKey = href.substring(href.lastIndexOf('/') + 1);
            var fileName = $(this).attr("fn");

            href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
            $(item).attr('href', href)
        })
    }

    /*上传附件结束*/

    /**
     * 提交公文
     */
    function submitDocument() {
        var docId = $("body").attr("docid");
        var departmentId = $("#departmentShow").val();
        var title = $("#titleShow").val();

        var content = UE.getEditor('contentShow').getContent();
        //var content = $(".CJ-Mre").val();
        if (title == "") {
            alert("请输入标题");
            return;
        }
        if (content == "") {
            alert("请输入正文");
            return;
        }
        if (baseChoosedPeople.data.length == 0) {
            alert("请选择发布范围");
            return;
        }
        if (baseCheckManId == "") {
            alert("请选择审阅人");
            return;
        }
        var publishIds = "";

        for (var i = 0; i < baseChoosedPeople.data.length; i++) {
            if (i == baseChoosedPeople.data.length - 1)
                publishIds += baseChoosedPeople.data[i].id;
            else
                publishIds += baseChoosedPeople.data[i].id + ",";
        }
        var docIds = "";
        var userIds = "";
        var docNames = "";
        var docValues = "";
        for (var i = 0; i < files.length; i++) {
            if (i != files.length - 1) {
                docIds += files[i].id + ",";
                userIds += files[i].userId + ",";
                docNames += files[i].name + ",";
                docValues += files[i].value + ",";
            }
            else {
                docIds += files[i].id;
                userIds += files[i].userId;
                docNames += files[i].name;
                docValues += files[i].value;
            }
        }
        var modifyReason = $(".tab_xgyy").val();
        if (modifyReason == "") {
            alert("请填写修改原因");
            return;
        }
        $.ajax({
            url: "/docflow/modifyDoc.do",
            type: "post",
            dataType: "json",
            data: {
                docId: docId,
                departmentId: departmentId,
                title: title,
                publishIds: publishIds,
                content: content,
                docIds: docIds,
                userIds: userIds,
                docNames: docNames,
                docValues: docValues,
                checkManId: baseCheckManId,
                checkDepartmentId: baseCheckDepartmentId,
                modifyReason: modifyReason,
                type: 2
            },
            success: function (data) {
                if (data.code == "200") {
                    alert("修改成功");
                    if (GetQueryString("a")!="10000") {
                        location.href = "/docflow/documentList.do?type=2&version=51";
                    }
                    else {
                        location.href = "/docflow/documentList.do?type=2&version=51&a=10000";
                    }
                }
                else {
                    alert("修改失败");
                }
            }
        });
    }

    //获取未读或未审阅或可编辑的数目
    myDocumentModify.getUnreadCount = function () {
        $.ajax({
            url: "/docflow/getpromote.do",
            type: "GET",
            success: function (data) {
                $("#unReadCountShow").empty();
                Common.render({tmpl: "#unReadCountJs", data: data, context: "#unReadCountShow"});
            }
        });
    };
    $(document).ready(function () {
        $(window).bind('beforeunload', function () {
            return '您修改的公文尚未提交，确定离开此页面吗？';
        });

        //审阅人一级列表
        $("body").on("click", ".group1", function () {
            $(this).addClass('current').siblings('li').removeClass('current');
            var tag = $(this).attr("tag");
            if (tag == "dep")//部门
                getDepartmentList();
            else if (tag == "sub")//学科
                getSubjectList();
        });
        //审阅人二级列表
        $("body").on("click", ".group2", function () {
            $(this).addClass('current').siblings('li').removeClass('current');
            var tag = $(this).attr("tag");
            var index = $(this).attr("index");
            var dep = $(this).attr("dep");
            preCheckDepartmentId = dep;
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
                checkDepartmentId = preCheckDepartmentId
                checkManName = name;
                $(this).addClass('current').siblings().removeClass("current");
            }
        });
        $("body").on("click", ".departmentList", function () {
            var curId = $(this).attr("curId");
            var userlist = departmentAndSubjectUser.department;
            var departmentUser = userlist[curId].list;
            //点击部门选择全部人
            if (!$(this).hasClass("current")) {//未选择
                $(this).addClass("current");
                for (var j = 0; j < departmentUser.length; j++) {
                    var ifHave = false;
                    for (var i = 0; i < choosedPeople.data.length; i++) {
                        if (choosedPeople.data[i].id == departmentUser[j].idStr) {
                            ifHave = true;
                            break;
                        }
                    }
                    if (!ifHave) {
                        choosedPeople.data.push({"id": departmentUser[j].idStr, "name": departmentUser[j].value});
                        $(".publishList[userId='" + departmentUser[j].idStr + "']").addClass('current');
                    }
                }
                choosedPeopleShow();
            }
            else {
                $(this).removeClass("current");

                for (var i = 0; i < choosedPeople.data.length; i++) {
                    var ifHave = false;
                    for (var j = 0; j < departmentUser.length; j++) {
                        if (choosedPeople.data[i].id == departmentUser[j].idStr) {
                            ifHave = true;
                            break;
                        }
                    }
                    if (ifHave) {
                        choosedPeople.data.splice(i, 1);
                        i--;
                        $(".publishList[userId='" + departmentUser[j].idStr + "']").removeClass('current');
                    }
                }
                choosedPeopleShow();
            }
        });
        //发布范围选择人
        $("body").on("click", ".publishList", function () {
            var id = $(this).attr("userId");
            var name = $(this).attr("name");
            if ($(this).hasClass("current")) {
                //同一个人的都去除样式
                removePeople(id);
                $(".publishList[userId='" + id + "']").removeClass("current");
            }
            else {
                addPeople(id, name);
                $(".publishList[userId='" + id + "']").addClass('current');
            }
        });
        //发布范围弹出框确定
        $("body").on("click", ".QD1", function () {
            baseChoosedPeople = {data: []};
            for (var i = 0; i < choosedPeople.data.length; i++) {
                baseChoosedPeople.data.push(choosedPeople.data[i]);
            }
            showPublishOnMain();
            $(".gay,.Tanchu1").fadeOut();
        });
        $("body").on("click", ".QD2", function () {
            baseCheckManId = checkManId;
            baseCheckManName = checkManName;
            baseCheckDepartmentId = checkDepartmentId;
            $("#checkPeople").val(baseCheckManName);
            $(".gay,.Tanchu2").fadeOut();
        });

        $("body").on("click", ".ifCheck", function () {
            if ($('.ifCheck').is(':checked')) {
                $(".ts2").css("display", "block");
            }
            else {
                $(".ts2").css("display", "none");
            }
        });

        //保存修改公文
        $("body").on("click", ".tjbc", function () {
            $(window).unbind('beforeunload');
            submitDocument();
        });
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
                        files.push({id: -1, userId: -1, userName: "", name: info.fileName, value: info.path});
                        showFileList();
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


    myDocumentModify.getDocumentDetail();

    myDocumentModify.init();
    myDocumentModify.getUnreadCount();
});