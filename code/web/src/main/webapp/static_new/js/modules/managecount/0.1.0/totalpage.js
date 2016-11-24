/**
 * @author 郭靖
 * @module  管理统计
 * @description
 * 管理统计模块
 */
/* global Config */
define('totalpage',['jquery','doT','easing','common','manageCountUtils','echarts'],function(require,exports,module){
    /**
     *初始化参数
     */
    var totalpage = {},
        Common = require('common'),
        Util = require('manageCountUtils');
        require('echarts');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * totalpage.init()
     */
    totalpage.init = function(){
        if (GetQueryString("a")!="10000") {


        }
        totalpage.changeTimeArea();
        totalpage.changeGrade();
        totalpage.searchTotal();
        $("#timeArea").change(function(){
            totalpage.changeTimeArea();
        });
        $("#grade-search").change(function(){
            totalpage.changeGrade();
        });
        $("#searchTotal").click(function(){
            totalpage.searchTotal();
        });
    };

    /**
     * @func chartInit
     * @desc 渲染图表
     * @example
     * chengJi.chartInit(id,{})
     */
    totalpage.chartInit = function(id,opt){
        var _chart = echarts.init($('#'+id)[0]);
        _chart.setOption(opt);
    }

    totalpage.changeTimeArea = function(){
        var selData = {};
        selData.timeArea = $("#timeArea").val();
        Common.getPostData('/manageCount/getTimeAreaVal.do', selData,function(rep){
            $('#dateStart').val(rep.dateStart);
            $('#dateEnd').val(rep.dateEnd);
        });
    }

    totalpage.changeGrade = function(){
        var selData = {};
        selData.schoolId=$("#schoolid").val();
        selData.gradeId=$("#grade-search").val();
        var gradeName=$("#grade-search").find("option:selected").text();
        if(selData.gradeId!=""){
            Common.getPostData('/manageCount/getGradeClassValue.do', selData,function(rep){
                var list = rep.classList;
                var html='<option value="">'+gradeName+'全部班级</option>';
                $.each(list,function(i,item){
                    html+='<option value='+item.id+'>'+item.className+'</option>';
                });
                $("#class-search").html(html);
            });
        }else{
            var html='<option value="">'+gradeName+'全部班级</option>';
            $("#class-search").html(html);
        }
    }


    totalpage.searchTotal = function() {
        MessageBox('图表加载中...', 0);
        totalpage.schoolTotalData();
        ClosePromptDialog();
    }

    totalpage.schoolTotalData = function(){
        var selData = {};
        selData.schoolid=$('#schoolid').text();
        selData.gradeId =  $("#grade-search").val();
        selData.classId =  $("#class-search").val();
        selData.dateStart =  $("#dateStart").val();
        selData.dateEnd =  $("#dateEnd").val();
        Common.getPostData('/manageCount/schooltotaldata.do', selData,function(data){
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
            totalpage.chartInit('main1',chardata1);

            var count = data.tcount + data.scount + data.pcount;
            var teacher="";
            var student="";
            var parent="";
            if (count==0) {
                teacher=0;
                student=0;
                parent=0;
                $('#teacount').html('0('+teacher+'%)');
                $('#stucount').html('0('+student+'%)');
                $('#partcount').html('0('+parent+'%)');
            } else {
                teacher=Math.round(data.tcount * 100 / count);
                student=Math.round(data.scount * 100 / count);
                parent=Math.round(data.pcount * 100 / count);
                $('#teacount').html(data.tcount + '(' + teacher + '%)');
                $('#stucount').html(data.scount + '(' + student + '%)');
                $('#partcount').html(data.pcount + '(' + parent + '%)');
            }

            rawdata.teacher=teacher;
            rawdata.student=student;
            rawdata.parent=parent;
            var chardata2 = Util.buildChartData('gltj_fwtj','ryfb',rawdata);
            totalpage.chartInit('main2',chardata2);
            rawdata.scount=data.scount;
            rawdata.pcount=data.pcount;
            rawdata.tcount=data.tcount;
            var chardata3 = Util.buildChartData('gltj_fwtj','rysftj',rawdata);
            totalpage.chartInit('main3',chardata3);


            $('#lescount').html('总:' + data.teaLessonCount);
            $('#clscount').html('总:' + data.teaClassCount);
            $('#cldcount').html('总:' + data.stuCloudCount);
            $('#sclscount').html('总:' + data.stuClassCount);
            $('#hwcount').html('总:' + data.homeworkCount);
            $('#pcount').html('总:' + data.paperCount);

            rawdata.teaLessonCount=data.teaLessonCount;
            rawdata.teaClassCount=data.teaClassCount;
            var chardata4 = Util.buildChartData('gltj_bkzy','lsbktj',rawdata);
            totalpage.chartInit('main4',chardata4);

            rawdata.stuCloudCount=data.stuCloudCount;
            rawdata.stuClassCount=data.stuClassCount;
            var chardata5 = Util.buildChartData('gltj_bkzy','xsgktj',rawdata);
            totalpage.chartInit('main5',chardata5);

            rawdata.homeworkCount=data.homeworkCount;
            rawdata.paperCount=data.paperCount;
            var chardata6 = Util.buildChartData('gltj_bkzy','zytj',rawdata);
            totalpage.chartInit('main6',chardata6);

        });
    }
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    module.exports=totalpage;
});

