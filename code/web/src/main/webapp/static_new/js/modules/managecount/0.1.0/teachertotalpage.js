/**
 * @author 郭靖
 * @module  管理统计
 * @description
 * 管理统计模块
 */
/* global Config */
define('teachertotalpage',['jquery','doT','easing','common','manageCountUtils','echarts'],function(require,exports,module){
    /**
     *初始化参数
     */
    var teachertotalpage = {},
        Common = require('common'),
        Util = require('manageCountUtils');
        require('echarts');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teachertotalpage.init()
     */
    teachertotalpage.init = function(){
        if (GetQueryString("a")!="10000") {


        }
        teachertotalpage.changeTimeArea();
        teachertotalpage.getClassInfo();
        teachertotalpage.searchTotal();
        $("#timeArea").change(function(){
            teachertotalpage.changeTimeArea();
        });
        $("#searchTotal").click(function(){
            teachertotalpage.searchTotal();
        });
    };

    /**
     * @func chartInit
     * @desc 渲染图表
     * @example
     * chengJi.chartInit(id,{})
     */
    teachertotalpage.chartInit = function(id,opt){
        var _chart = echarts.init($('#'+id)[0]);
        _chart.setOption(opt);
    }

    teachertotalpage.changeTimeArea = function(){
        var selData = {};
        selData.timeArea = $("#timeArea").val();
        Common.getPostData('/manageCount/getTimeAreaVal.do', selData,function(rep){
            $('#dateStart').val(rep.dateStart);
            $('#dateEnd').val(rep.dateEnd);
        });
    }

    teachertotalpage.getClassInfo = function(){
        var selData = {};
        Common.getPostData('/manageCount/getTeacherClass.do', selData,function(rep){
            var list = rep.classList;
            var html="";
            $.each(list,function(i,item){
                html+='<option value='+item.id+'>'+item.gradeName+item.className+'</option>';
            });
            $("#class-search").html(html);
        });
    }

    teachertotalpage.searchTotal = function() {
        MessageBox('图表加载中...', 0);
        teachertotalpage.teacherClassTotalData();
        ClosePromptDialog();
    }

    teachertotalpage.teacherClassTotalData = function(){
        var selData = {};
        selData.classId =  $("#class-search").val();
        selData.dateStart =  $("#dateStart").val();
        selData.dateEnd =  $("#dateEnd").val();
        Common.getPostData('/manageCount/teacherClassTotalData.do', selData,function(data){
            var rawdata = {};
            rawdata.title_text = "访问分析";
            rawdata.title_subtext = $("#currTime").val();

            rawdata.time_area=[];
            rawdata.people_count=[];
            var acList=data.acList;
            for(var i in acList) {
                rawdata.time_area[i]=acList[i].createTime;
                rawdata.people_count[i]=acList[i].newCountTotal;
            }
            var chardata1 = Util.buildChartData('gltj_fwfx','',rawdata);
            teachertotalpage.chartInit('main1',chardata1);

            var count = data.scount + data.pcount;
            var student="";
            var parent="";
            if (count==0) {
                student=0;
                parent=0;
                $('#stucount').html('0('+student+'%)');
                $('#partcount').html('0('+parent+'%)');
            } else {
                student=Math.round(data.scount * 100 / count);
                parent=Math.round(data.pcount * 100 / count);
                $('#stucount').html(data.scount + '(' + student + '%)');
                $('#partcount').html(data.pcount + '(' + parent + '%)');
            }

            rawdata.student=student;
            rawdata.parent=parent;
            var chardata2 = Util.buildChartData('gltj_fwtj_teacher','ryfb',rawdata);
            teachertotalpage.chartInit('main2',chardata2);
            rawdata.scount=data.scount;
            rawdata.pcount=data.pcount;
            var chardata3 = Util.buildChartData('gltj_fwtj_teacher','rysftj',rawdata);
            teachertotalpage.chartInit('main3',chardata3);

            $('#cldcount').html('总:' + data.stuCloudCount);
            $('#sclscount').html('总:' + data.stuClassCount);

            rawdata.stuCloudCount=data.stuCloudCount;
            rawdata.stuClassCount=data.stuClassCount;
            var chardata5 = Util.buildChartData('gltj_bkzy','xsgktj',rawdata);
            teachertotalpage.chartInit('main5',chardata5);
        });
    }
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    module.exports=teachertotalpage;
});

