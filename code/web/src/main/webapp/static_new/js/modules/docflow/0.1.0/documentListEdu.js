/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['doT', 'common', 'jquery','initPaginator'],function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var Paginator=require('initPaginator');
    var documentList = {},
        Common = require('common');
    var pageSize=10;//每页显示公文数目
    var currentDocid="";//当前操作的公文Id
    var currentOp=0;//当前执行的操作，0为撤销，1为删除
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    documentList.init = function () {
        if (GetQueryString("a")!="10000") {


        }
    };

    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }

    documentList.getTermList=function()
    {
        $.ajax({
            url:"/docflow/getallterm.do",
            type:"get",
            success:function(data)
            {
                $("#searchTerm").empty();
                Common.render({tmpl: $('#termJs'), data: {data:data}, context: '#searchTerm'});
                documentList.getSchoolHeadmasterList();
            }
        });
    };
    var headMasterList=[];
    documentList.getSchoolHeadmasterList=function()
    {
        $.ajax({
            url:"/docflow/getSchoolMastersByEdu.do",
            type:"post",
            success:function(data)
            {
                headMasterList=data;
                documentList.tabChange();
                $("#schoolName").text("接收学校");
                $("#schoolList").empty();
                Common.render({tmpl: $('#schoolMasterJs'), data: {data:headMasterList}, context: '#schoolList'});
            }
        })
    };
    var schoolList=[];
    //获取学校列表
    documentList.getSchoolList=function()
    {
        $.ajax({
            url:"/docflow/getSchoolsByEdu.do",
            type:"post",
            success:function(data)
            {
                schoolList=data;
            }
        })
    };
    documentList.getDocumentList=function(term,keyWords,page,type)
    {
        var schoolIds="";
        if($("#schoolList").val()=="-1")
        {
            for(var i=0;i<headMasterList.length;i++)
            {
                schoolIds+=headMasterList[i].id+",";
            }
            if(schoolIds.length>0)
            {
                schoolIds=schoolIds.substring(0,schoolIds.length-1);
            }
        }
        else{
            schoolIds=$("#schoolList").val();
        }
        $.ajax({
            url:"/docflow/doclistEdu.do",
            type:"POST",
            dataType:"json",
            data:{
                term:term,
                schoolId:schoolIds,
                keyWords:keyWords,
                page:page,
                pageSize:pageSize,
                type:type
            },
            success:function(data)
            {
                var option = {
                    total: data.rowCount,
                    pagesize: data.pageSize,
                    currentpage: data.page,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).click(function () {
                                documentList.getDocumentList(term,keyWords,$(this).text(),type);
                            });
                        });
                        $('.first-page').click(function () {
                            documentList.getDocumentList(term,keyWords,1,type);
                        });

                        $('.last-page').click(function () {
                            documentList.getDocumentList(term,keyWords,totalPage,type);
                        });
                    }
                };
                Paginator.initPaginator(option);

                if(type==0)
                {
                    initAllDoc(data);
                }
                else if(type==1)
                {
                    initSchoolDoc(data);
                }

            }
        });
    };
    $(document).ready(function(){
        //搜索
        $("body").on("click","#searchBtb",function(){
            var term=$("#searchTerm").val();
            var keyWords=$("#searchContent").val();
            var index=$(".tab-head ul .cur").index();
            documentList.getDocumentList(term,keyWords,1,index);
            //documentList.getUnreadCount();
        });
        $("body").on("click",".tab-head ul li",function()
        {
            $(this).addClass("cur").siblings().removeClass("cur");
            var index=$(this).index();

            if(index==0)
            {
                $("#schoolName").text("接收学校");
                $("#schoolList").empty();
                Common.render({tmpl: $('#schoolMasterJs'), data: {data:headMasterList}, context: '#schoolList'});
            }
            else if(index==1)
            {
                $("#schoolName").text("学校");
                $("#schoolList").empty();
                Common.render({tmpl: $('#schoolJs'), data: {data:schoolList}, context: '#schoolList'});
            }
            var term=$("#searchTerm").val();
            var keyWords=$("#searchContent").val();
            //var isHandler=$(".Xueqi-II").val();
            if(index<2)
                documentList.getDocumentList(term,keyWords,1,index);
            else{
                if(GetQueryString("a")=="10000")
                    window.open('../docflow/documentCreate.do?version=51&a=10000',"_self");
                else
                    window.open('../docflow/documentCreate.do?version=51',"_self");
            }
        });
        $("body").on("click",".revocation",function(){
            currentDocid=$(this).attr("docid");
            var title=$(this).attr("title");
            $(".popup-head").css("display","block");
            $(".popup-info").css("display","block");
            $(".popup-bt").html("公文标题："+title);
            $(".popup-op").html("确定要撤销该公文吗？");
            currentOp=0;
        });
        $("body").on("click",".deleteDoc",function(){
            currentDocid=$(this).attr("docid");
            var title=$(this).attr("title");
            $(".popup-head").css("display","block");
            $(".popup-info").css("display","block");
            $(".popup-bt").html("公文标题："+title);
            $(".popup-op").html("确定要删除该公文吗？");
            currentOp=1;
        });
        $("body").on("click",".he_qx",function(){
            $(".popup-head").css("display","none");
            $(".popup-info").css("display","none");
        });
        $("body").on("click",".he_qd",function(){
            $(".popup-head").css("display","none");
            $(".popup-info").css("display","none");
            if(currentOp==0)
            {
                revocateDoc(currentDocid);
            }
            else
            {
                deleteDoc(currentDocid);
            }
        });
    });
    //撤销公文
    function revocateDoc(docId)
    {
        $.ajax({
            url:"/docflow/revdoc.do",
            type:"post",
            data:{
                docId:docId
            },
            success:function(data)
            {
                if(data==true)
                {
                    alert("撤销成功");
                    if (GetQueryString("a")!="10000") {
                        location.href="/docflow/documentList.do?type=0&version=51";
                    } else {
                        location.href="/docflow/documentList.do?type=0&a=10000&version=51";
                    }
                }
                else
                {
                    alert("撤销失败");
                }
            }
        });
    }
    //删除公文
    function deleteDoc(docId)
    {
        $.ajax({
            url:"/docflow/deldoc.do",
            type:"post",
            data:{
                docId:docId
            },
            success:function(data)
            {
                if(data==true)
                {
                    alert("删除成功");
                    if (GetQueryString("a")!="10000") {
                        location.href="/docflow/documentList.do?type=0&version=51";
                    } else {
                        location.href="/docflow/documentList.do?type=0&a=10000&version=51";
                    }

                }
                else
                {
                    alert("删除失败");
                }
            }
        });
    }

    //初始化公文
    function initAllDoc(data)
    {
        $(".Lb").empty();
        Common.render({tmpl: $('#allDocJs'), data: {data: data.rows}, context: '.Lb'});
    }
    //初始化学校公文
    function initSchoolDoc(data)
    {
        $(".Lb").empty();
        Common.render({tmpl: $('#schoolDocJs'), data: {data: data.rows}, context: '.Lb'});
    }

    //获取未读或可编辑公文数目
    documentList.getUnreadCount=function()
    {
        $.ajax({
            url:"/docflow/getpromote.do",
            type:"GET",
            success:function(data)
            {
                $("#unReadCountShow").empty();
                Common.render({tmpl:$("#unReadCountJs"),data:data,context:"#unReadCountShow"});
            }
        });
    };
    //tab切换
    documentList.tabChange=function()
    {
        //var type=$("body").attr("type");
        var term=$("#searchTerm").val();
        var keyWords=$("#searchContent").val();
        if(term=="")
        {
            return;
        }
        documentList.getDocumentList(term,keyWords,1,0);
        //documentList.getUnreadCount();
    };
    documentList.init();

    documentList.getTermList();
    documentList.getSchoolList();
    //documentList.getUnreadCount();

});