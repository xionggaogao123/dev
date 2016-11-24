/**
 * @author 郭靖
 * @module  管理统计
 * @description
 * 管理统计模块
 */
/* global Config */
define('educationSchools',['jquery','doT','easing','common','manageCountUtils','echarts'],function(require,exports,module){
    /**
     *初始化参数
     */
    var educationSchools = {},
        Common = require('common'),
        Util = require('manageCountUtils');
        require('echarts');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * educationSchools.init()
     */
    educationSchools.init = function(){
        if (GetQueryString("a")!="10000") {


        }
        educationSchools.changeTimeArea();
        educationSchools.changeSchool();
        educationSchools.searchTotal();
        $("#timeArea").change(function(){
            educationSchools.changeTimeArea();
        });
        $("#school-search").change(function(){
            educationSchools.changeSchool();
        });
        $("#searchTotal").click(function(){
            educationSchools.searchTotal();
        });
    };

    /**
     * @func chartInit
     * @desc 渲染图表
     * @example
     * chengJi.chartInit(id,{})
     */
    educationSchools.chartInit = function(id,opt){
        var _chart = echarts.init($('#'+id)[0]);
        _chart.setOption(opt);
    }

    educationSchools.changeTimeArea = function(){
        var selData = {};
        selData.timeArea = $("#timeArea").val();
        Common.getPostData('/manageCount/getTimeAreaVal.do', selData,function(rep){
            $('#dateStart').val(rep.dateStart);
            $('#dateEnd').val(rep.dateEnd);
        });
    }

    educationSchools.changeSchool = function(){
        var selData = {};
        selData.schoolType = $("#school-search").find("option:selected").attr("schoolType");
        var html='<option value="0">全部年级</option>';
        Common.getPostData('/manageCount/getSchoolGradeTypeValue.do', selData,function(rep){
            var list = rep.retList;
            $.each(list,function(i,item){
                html+='<option value='+item.gradeType+'>'+item.name+'</option>';
            });
        });
        $("#gradetype-search").html(html);
    }

    educationSchools.searchTotal = function() {
        MessageBox('图表加载中...', 0);
        //educationSchools.searchVisitor();
        //educationSchools.getPeopleTotal();
        //educationSchools.lessonTotal();
        educationSchools.eduSchoolsTotalData();
        var schoolName=$("#school-search").find("option:selected").text();
        $("#schoolName").text(schoolName);
        ClosePromptDialog();
    }

    educationSchools.eduSchoolsTotalData = function(){
        var selData = {};
        selData.gradeType =  $("#gradetype-search").val();
        selData.schoolId =  $("#school-search").val();
        selData.dateStart =  $("#dateStart").val();
        selData.dateEnd =  $("#dateEnd").val();
        Common.getPostData('/manageCount/eduschoolstotaldata.do', selData,function(data){
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
            educationSchools.chartInit('main1',chardata1);

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
            educationSchools.chartInit('main2',chardata2);
            rawdata.scount=data.scount;
            rawdata.pcount=data.pcount;
            rawdata.tcount=data.tcount;
            var chardata3 = Util.buildChartData('gltj_fwtj','rysftj',rawdata);
            educationSchools.chartInit('main3',chardata3);


            $('#lescount').html('总:' + data.teaLessonCount);
            $('#clscount').html('总:' + data.teaClassCount);
            $('#cldcount').html('总:' + data.stuCloudCount);
            $('#sclscount').html('总:' + data.stuClassCount);
            $('#hwcount').html('总:' + data.homeworkCount);
            $('#pcount').html('总:' + data.paperCount);

            rawdata.teaLessonCount=data.teaLessonCount;
            rawdata.teaClassCount=data.teaClassCount;
            var chardata4 = Util.buildChartData('gltj_bkzy','lsbktj',rawdata);
            educationSchools.chartInit('main4',chardata4);

            rawdata.stuCloudCount=data.stuCloudCount;
            rawdata.stuClassCount=data.stuClassCount;
            var chardata5 = Util.buildChartData('gltj_bkzy','xsgktj',rawdata);
            educationSchools.chartInit('main5',chardata5);

            rawdata.homeworkCount=data.homeworkCount;
            rawdata.paperCount=data.paperCount;
            var chardata6 = Util.buildChartData('gltj_bkzy','zytj',rawdata);
            educationSchools.chartInit('main6',chardata6);

        });
    }

    /*educationSchools.searchVisitor = function(){
        var selData = {};
        selData.gradeType =  $("#gradetype-search").val();
        selData.schoolId =  $("#school-search").val();
        selData.dateStart =  $("#dateStart").val();
        selData.dateEnd =  $("#dateEnd").val();
        Common.getPostData('/manageCount/eduAccessAnalysis.do', selData,function(data){
            var rawdata = {};
            rawdata.title_text = "访问分析";
            rawdata.title_subtext = $("#currTime").val();

            rawdata.time_area=[];
            rawdata.people_count=[];
            for(var i in data) {
                rawdata.time_area[i]=data[i].createTime;
                rawdata.people_count[i]=data[i].newCountTotal;
            }
            var chardata = Util.buildChartData('gltj_fwfx','',rawdata);
            educationSchools.chartInit('main1',chardata);
        });
    }
    educationSchools.getPeopleTotal = function(){
        var selData = {};
        selData.gradeType =  $("#gradetype-search").val();
        selData.schoolId =  $("#school-search").val();
        selData.dateStart =  $("#dateStart").val();
        selData.dateEnd =  $("#dateEnd").val();
        Common.getPostData('/manageCount/eduPeopleTotal.do', selData,function(data){
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


            var rawdata = {};
            rawdata.title_subtext = $("#currTime").val();
            rawdata.teacher=teacher;
            rawdata.student=student;
            rawdata.parent=parent;
            var chardata = Util.buildChartData('gltj_fwtj','ryfb',rawdata);
            educationSchools.chartInit('main2',chardata);
            rawdata.scount=data.scount;
            rawdata.pcount=data.pcount;
            rawdata.tcount=data.tcount;
            var chardata1 = Util.buildChartData('gltj_fwtj','rysftj',rawdata);
            educationSchools.chartInit('main3',chardata1);
        });
    }
    educationSchools.lessonTotal = function(){
        var selData = {};
        selData.gradeType =  $("#gradetype-search").val();
        selData.schoolId =  $("#school-search").val();
        selData.dateStart =  $("#dateStart").val();
        selData.dateEnd =  $("#dateEnd").val();
        Common.getPostData('/manageCount/eduLessonCount.do', selData,function(data){
            $('#lescount').html('总:' + data.teaLessonCount);
            $('#clscount').html('总:' + data.teaClassCount);
            $('#cldcount').html('总:' + data.stuCloudCount);
            $('#sclscount').html('总:' + data.stuClassCount);
            $('#hwcount').html('总:' + data.homeworkCount);
            $('#pcount').html('总:' + data.paperCount);

            var rawdata = {};
            rawdata.title_subtext = $("#currTime").val();
            rawdata.teaLessonCount=data.teaLessonCount;
            rawdata.teaClassCount=data.teaClassCount;
            var chardata = Util.buildChartData('gltj_bkzy','lsbktj',rawdata);
            educationSchools.chartInit('main4',chardata);
            rawdata.stuCloudCount=data.stuCloudCount;
            rawdata.stuClassCount=data.stuClassCount;
            var chardata1 = Util.buildChartData('gltj_bkzy','xsgktj',rawdata);
            educationSchools.chartInit('main5',chardata1);
            rawdata.homeworkCount=data.homeworkCount;
            rawdata.paperCount=data.paperCount;
            var chardata2 = Util.buildChartData('gltj_bkzy','zytj',rawdata);
            educationSchools.chartInit('main6',chardata2);

        });
    }*/
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    //educationSchools.init();
    module.exports=educationSchools;
});

