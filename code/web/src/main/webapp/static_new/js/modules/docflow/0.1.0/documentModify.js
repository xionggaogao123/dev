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
    var documentModify = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    documentModify.init = function () {
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
        $("body").on("click", ".tanchu", function (event) {
            $(".gay,.Tanchu1").fadeIn();
            getOnlyDepartmentList();
        });
        $("body").on("click", ".Tanchu1 .qx1", function (event) {
            $(".gay,.Tanchu1").fadeOut();
            showPublishOnMain();
        });
    });

    var departmentAndSubjectUser = {
        "department": [],
        "subject": []
    };
    documentModify.getUserList = function () {
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

    /*发布范围选择开始*/
    var choosedPeople = {data: []};
    var baseChoosedPeople = {data: []};
    var prePublishList = [];//之前的发布范围
    //获取发布范围显示数据
    function getOnlyDepartmentList() {
        var userlist = departmentAndSubjectUser.department;
        choosedPeople = {data: []};
        for (var i = 0; i < baseChoosedPeople.data.length; i++) {
            choosedPeople.data.push(baseChoosedPeople.data[i]);
        }

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

    /**
     * 获取公文详情
     */
    documentModify.getDocumentDetail = function () {
        var docId = $("body").attr("docid");
        $.ajax({
            url: "/docflow/getdocdetail.do",
            type: "GET",
            dataType: "json",
            data: {
                docId: docId
            },
            success: function (data) {
                var choosedUsed = data.publishList;
                for (var i = 0; i < choosedUsed.length; i++) {
                    prePublishList.push(choosedUsed[i]);
                }
                files = data.docList;

                showFileList();

                $("#docTitle").val(data.title);
                $("#authorDepartment").val(data.departmentId);
                Common.render({
                    tmpl: $("#departTmpJs"),
                    data: data,
                    context: "#departTmpShow"
                });
                if (GetQueryString("a")!="10000") {
                    $(".gotoHref").attr("href", "/docflow/documentCheck.do?docId=" + data.id + "&version=51");
                }
                else {
                    $(".gotoHref").attr("href", "/docflow/documentCheck.do?docId=" + data.id + "&version=51&a=10000");
                }
                //$("#contentShow").val(data.content);
                UE.getEditor('contentShow').setContent(data.content);
                //显示审阅历史
                Common.render({
                    tmpl: $("#checkHistoryJs"),
                    data: {data: data.checkDTOList},
                    context: "#checkHistoryShow"
                });

            }
        });
    };
    //修改公文提交
    documentModify.modifyDocument = function (docId, departmentId, title, publishIds, content, files, checkManId, checkDepartmentId, modifyReason, type) {
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
                checkManId: checkManId,
                checkDepartmentId: checkDepartmentId,
                modifyReason: modifyReason,
                type: type
            },
            success: function (data) {
                if (data.code == "200") {
                    alert("修改成功");
                    if (type == 1) {
                        if (GetQueryString("a")!="10000") {
                            location.href = "/docflow/documentCheck.do?docId=" + docId + "&version=51";
                        }
                        else {
                            location.href = "/docflow/documentCheck.do?docId=" + docId + "&version=51&a=10000";
                        }
                    }
                    else if (type == 0) {
                        if (GetQueryString("a")!="10000") {
                            location.href = "/docflow/documentList.do?type=0&version=51";
                        }
                        else {
                            location.href = "/docflow/documentList.do?type=0&version=51&a=10000";
                        }
                    }
                }
                else {
                    alert("修改失败");
                }
            }
        });
    };

    //获取未读或可编辑数目
    documentModify.getUnreadCount = function () {
        $.ajax({
            url: "/docflow/getpromote.do",
            type: "GET",
            success: function (data) {
                $("#unReadCountShow").empty();
                Common.render({tmpl: "#unReadCountJs", data: data, context: "#unReadCountShow"});
            }
        });
    };
    var files = [];

//显示附件
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

    $(document).ready(function () {
        $(window).bind('beforeunload', function () {
            return '您修改的公文尚未提交，确定离开此页面吗？';
        });

        var type = $("body").attr("type");
        if (type == 0) {
            $(".tab1").css("display", "none");
            $(".tab0").css("display", "block");
        }
        else if (type == 1) {
            $(".tab0").css("display", "none");
            $(".tab1").css("display", "block");
        }
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

        //保存修改
        $("body").on("click", "#saveDocument", function () {
            var docId = $("body").attr("docid");
            var departmentId = $("#authorDepartment").val();
            var title = $("#docTitle").val();
            if (title == "") {
                alert("请输入标题");
                return;
            }
            if (baseChoosedPeople.data.length == 0) {
                alert("请选择发布范围");
                return;
            }
            var publishIds = "";
            for (var i = 0; i < baseChoosedPeople.data.length; i++) {
                if (i == baseChoosedPeople.data.length - 1)
                    publishIds += baseChoosedPeople.data[i].id;
                else
                    publishIds += baseChoosedPeople.data[i].id + ",";
            }
            //var content = $(".pro-de-ZW").val();
            var content = UE.getEditor('contentShow').getContent();

            if (content == "") {
                alert("请输入正文");
                return;
            }
            var modifyReason = "";
            var checkManId = "";
            var checkDepartmentId = "";
            var type = 1;
            $(window).unbind('beforeunload');
            documentModify.modifyDocument(docId, departmentId, title, publishIds, content, files, checkManId, checkDepartmentId, modifyReason, type);
        });
        //保存公文下的编辑修改
        $("body").on("click", "#savePublishedDoc", function () {
            var docId = $("body").attr("docid");
            var departmentId = $("#authorDepartment").val();
            var title = $("#docTitle").val();
            if (title == "") {
                alert("请输入标题");
                return;
            }
            if (baseChoosedPeople.data.length == 0) {
                alert("请选择发布范围");
                return;
            }
            var publishIds = "";
            for (var i = 0; i < baseChoosedPeople.data.length; i++) {
                if (i == baseChoosedPeople.data.length - 1)
                    publishIds += baseChoosedPeople.data[i].id;
                else
                    publishIds += baseChoosedPeople.data[i].id + ",";
            }
            //var content = $(".pro-de-ZW").val();
            var content = UE.getEditor('contentShow').getContent();
            if (content == "") {
                alert("请输入正文");
                return;
            }
            var modifyReason = "";
            var checkManId = "";
            var checkDepartmentId = "";
            var type = 0;
            $(window).unbind('beforeunload');
            documentModify.modifyDocument(docId, departmentId, title, publishIds, content, files, checkManId, checkDepartmentId, modifyReason, type)
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

    documentModify.init();
    documentModify.getUserList();
    documentModify.getDocumentDetail();

    documentModify.getUnreadCount();
});