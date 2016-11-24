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
    var documentCreate = {},
        Common = require('common');

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * documentCreate.init()
     */
    documentCreate.init = function () {
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

    /*弹出层部分结束*/
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


    /**
     * 新开始
     */
    documentCreate.getUserList = function () {
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
    //获取发布范围显示数据
    function getOnlyDepartmentList() {
        choosedPeople = {data: []};
        for (var i = 0; i < baseChoosedPeople.data.length; i++) {
            choosedPeople.data.push(baseChoosedPeople.data[i]);
        }
        var userlist = departmentAndSubjectUser.department;
        //初始化是否被选中
        for (var i = 0; i < userlist.length; i++) {
            //for (var j = 0; j < userlist[i].list.length; j++) {
                if (checkIfChoosed(userlist[i].id)) {
                    userlist[i].choosed = 1;
                }
                else {
                    userlist[i].choosed = 0;
                }
            //}
        }
        $(".publishShow").empty();
        Common.render({tmpl: $('#publishJs'), data: {data: userlist}, context: '.publishShow'});
        choosedPeopleShow();
    }

    //判断该user是否已经被选择
    function checkIfChoosed(id) {
        for (var i = 0; i < choosedPeople.data.length; i++) {
            if (choosedPeople.data[i].id == id) {
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
    /*上传附件结束*/

    /**
     * 提交公文
     */
    function submitDocument() {
        var departmentId = $("#eduId").val();
        var term = $("#termShow").text();
        var title = $("#titleShow").val();

        var content = UE.getEditor('contentShow').getContent();
        //var content = $("#contentShow").val();
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

        var publishIds = "";
        var docNames = "";
        var docAddress = "";
        for (var i = 0; i < baseChoosedPeople.data.length; i++) {
            if (i == baseChoosedPeople.data.length - 1)
                publishIds += baseChoosedPeople.data[i].id;
            else
                publishIds += baseChoosedPeople.data[i].id + ",";
        }
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

        $.ajax({
            url: "/docflow/adddocEdu.do",
            type: "POST",
            data: {
                departmentId: departmentId,
                term: term,
                title: title,
                publishIds: publishIds,
                content: content,
                docNames: docNames,
                docAddress: docAddress
            },
            success: function (data) {
                alert("发表成功");
                $(window).unbind('beforeunload');
                if (GetQueryString("a")!="10000") {
                    location.href = '/docflow/documentList.do?type=2&version=51';
                }
                else {
                    location.href = '/docflow/documentList.do?type=2&version=51&a=10000';
                }
            }
        });
    }
    $(document).ready(function () {
        $(window).bind('beforeunload', function () {
            return '您撰写的公文尚未提交，确定离开此页面吗？';
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
                        choosedPeople.data.splice(i,1);
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


        //上传公文
        $("body").on("click", ".submitDocument", function () {
            submitDocument();

        });
        $("body").on("click", "#quit", function () {
            //$(window).unbind('beforeunload');
            location.href = '/docflow/documentList.do?type=2&version=51';
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
    //获取各学校校长列表
    documentCreate.getSchoolHeadmasterList=function()
    {
        $.ajax({
            url:"/docflow/getSchoolMastersByEdu.do",
            type:"post",
            success:function(data)
            {
                for(var i=0;i<data.length;i++)
                {
                    departmentAndSubjectUser.department.push({"id":data[i].id,"name":data[i].name,"choosed":0});
                }

            }
        })
    };

    //documentCreate.getUserList();

    documentCreate.init();
    documentCreate.getSchoolHeadmasterList();
});