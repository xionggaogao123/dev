/**
 * @author 郭靖
 * @module  管理统计
 * @description
 * 管理统计模块
 */
/* global Config */
define('teacherFunUseCount',['jquery','doT','easing','common','manageCountUtils','echarts','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var teacherFunUseCount = {},
        Common = require('common'),
        Util = require('manageCountUtils'),
        Paginator = require('initPaginator');
        require('echarts');
    var selParam = {};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacherFunUseCount.init()
     */
    teacherFunUseCount.init = function(){
        if (GetQueryString("a")!="10000") {


        }
        //设置初始页码
        selParam.page = 1;
        //设置每页数据长度
        selParam.size = 10;
        teacherFunUseCount.changeTimeArea();
        teacherFunUseCount.getClassInfo();
        teacherFunUseCount.submitFun();
        $("#timeArea").change(function(){
            teacherFunUseCount.changeTimeArea();
        });
        $("#role").change(function(){
            teacherFunUseCount.changeUserRole();
        });
        $("#submitBtn").click(function(){
            teacherFunUseCount.submitFun();
        });
    };

    /**
     * @func chartInit
     * @desc 渲染图表
     * @example
     * chengJi.chartInit(id,{})
     */
    teacherFunUseCount.chartInit = function(id,opt){
        var _chart = echarts.init($('#'+id)[0]);
        _chart.setOption(opt);
    }

    teacherFunUseCount.changeTimeArea = function(){
        var selData = {};
        selData.timeArea = $("#timeArea").val();
        Common.getPostData('/manageCount/getTimeAreaVal.do', selData,function(rep){
            $('#dateStart').val(rep.dateStart);
            $('#dateEnd').val(rep.dateEnd);
        });
    }

    teacherFunUseCount.changeUserRole = function(){
        var selData = {};
        selData.role = $("#role").val();
        Common.getPostData('/manageCount/getRoleFunValue.do', selData,function(rep){
            var list = rep.roleFuns;
            var html="";
            $.each(list,function(i,item){
                html+='<option value="'+item.key+'">'+item.value+'</option>';
            });
            $("#funId").html(html);
        });
    }

    teacherFunUseCount.getClassInfo = function(){
        var selData = {};
        Common.getPostData('/manageCount/getTeacherClass.do', selData,function(rep){
            var list = rep.classList;
            var html="";
            $.each(list,function(i,item){
                html+='<option value='+item.id+'>'+item.gradeName+item.className+'</option>';
            });
            $("#classId").html(html);
        });
    }

    teacherFunUseCount.titleChange = function (){
        var funName=$("#funId").find("option:selected").text();
        var html1="<span>1</span>&nbsp;&nbsp;&nbsp; "+funName+"排行榜";
        $("#title1").html(html1);
        $("#title11").text(funName);
        $("#title113").text(funName+"量");
        var html2="<span>2</span>&nbsp;&nbsp;&nbsp; "+funName+"统计";
        $("#title2").html(html2);

        var role=$("#role").val();
        var roleText=$("#role").find("option:selected").text();
        if(role==2||roleText=="老师"){
            $("#divTitle1").text(funName+"(老师)统计");
            $("#spanTitle1").text("老师姓名");
        }else if(role==1||roleText=="学生"){
            $("#spanTitle1").text("学生姓名");
            $("#divTitle1").text(funName+"(学生)统计");
        }else{
            $("#spanTitle1").text("家长姓名");
            $("#divTitle1").text(funName+"(家长)统计");
        }
        var spanTitle2=funName.substring(0,funName.length-1)+"总数";
        $("#spanTitle2").text(spanTitle2);

    }

    teacherFunUseCount.submitFun = function() {
        MessageBox('图表加载中...', 0);
        selParam.schoolId=$("#schoolId").val();
        selParam.classId=$("#classId").val();
        selParam.role=$("#role").val();
        selParam.funId=$("#funId").val();
        selParam.dateStart=$("#dateStart").val();
        selParam.dateEnd=$("#dateEnd").val();
        teacherFunUseCount.titleChange();
        teacherFunUseCount.getSchoolRankingList();
        teacherFunUseCount.seachUseTimeDist();
        teacherFunUseCount.getPagingQueryList();
        ClosePromptDialog();
    }

    teacherFunUseCount.getSchoolRankingList = function(){
        Common.getPostData('/manageCount/rankingList.do', selParam,function(rep){
            var result={};
            var number=new Array("第一名","第二名","第三名","第四名","第五名","第六名","第七名","第八名","第九名","第十名");
            result.list=[];
            var list = rep.list;
            $.each(list,function(i,item){
                var obj=new Object();
                obj.number=number[i];
                obj.userId=item.userId;
                obj.name=item.name;
                obj.newCountTotal=item.newCountTotal;
                result.list[i]=obj;
            });
            $('#mainTdiv1').html('');
            Common.render({tmpl: $('#j-tmpl1'), data: result.list, context: '#mainTdiv1'});
        });
    }


    teacherFunUseCount.seachUseTimeDist = function() {
        Common.getPostData('/manageCount/useTimeDistMap.do', selParam, function(data){
            var rawdata = {};
            rawdata.title_text = "访问分析";
            rawdata.title_subtext = $("#currTime").val();

            rawdata.time_area=[];
            rawdata.people_count=[];
            var list=data.list;
            for(var i in list) {
                rawdata.time_area[i]=list[i].createTime;
                rawdata.people_count[i]=list[i].newCountTotal;
            }
            var chardata1 = Util.buildChartData('gltj_fwfx','',rawdata);
            teacherFunUseCount.chartInit('main1',chardata1);
        });
    }

    var option={};
    teacherFunUseCount.getPagingQueryList = function(){
        Common.getData('/manageCount/pagingQuery.do',selParam,function(rep){
            $('#mainTdiv2').html('');
            Common.render({tmpl: $('#j-tmpl2'), data: rep.content, context: '#mainTdiv2'});
            var total = rep.totalElements;
            option.total= total;
            option.pagesize= selParam.size;
            option.currentpage=selParam.page;
            option.operate=function (totalPage) {
                $('.page-index span').each(function () {
                    $(this).click(function(){
                        if(selParam.page!=parseInt($(this).text())){
                            selParam.page=parseInt($(this).text());
                            teacherFunUseCount.getPagingQueryList();
                        }
                    });
                });
                $('.first-page').click(function(){
                    if(selParam.page!=1) {
                        selParam.page = 1;
                        teacherFunUseCount.getPagingQueryList();
                    }
                });
                $('.last-page').click(function(){
                    if(selParam.page!=totalPage) {
                        selParam.page = totalPage;
                        teacherFunUseCount.getPagingQueryList();
                    }
                })
            }
            Paginator.initPaginator(option);
        });
        /*
         * 绑定查看明细按钮
         * */
        $('.detail').bind("click",function(event){
            var id= $(this).attr('userId');
            var name= $(this).attr('userName');
            teacherFunUseCount.gotoDetail(id,name);
        });

    }

    teacherFunUseCount.gotoDetail = function(id,name){
        var schoolId=$("#schoolId").val();
        var role=$("#role").val();
        var funId=$("#funId").val();
        var dateStart=$("#dateStart").val();
        var dateEnd=$("#dateEnd").val();
        var param="schoolId="+schoolId+"&operateUserId="+id+"&userName="+name+"&role="+role+"&funId="+funId+"&dateStart="+dateStart+"&dateEnd="+dateEnd;
        var url="/manageCount/functionUsePage.do?"+param;
        Common.goTo(url);
    }
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    module.exports=teacherFunUseCount;
});

