/**
 * Created by guojing on 2016/8/24.
 */
define('imagebank',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var imagebank = {},
        Common = require('common');

    var searchParam={};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * imagebank.init()
     */
    imagebank.init = function(){


        var name = $(".yx-active").attr("id");
        $(".xxyh-main>div").hide();
        $("#" + "tab-" + name).show();
        searchParam.bigTitle=name;
        if(name=="YHGL"){
            $("#selGrade").get(0).selectedIndex = 0;
            imagebank.changeGrade();
        }else{
            imagebank.searchStuWeeklyQuality();
        }

        $(".prev").click(function(){
            $(this).siblings().removeClass("title-active");
            $(this).addClass("title-active");
            searchParam.operate="prev";
            searchParam.suspend=false;
            imagebank.searchStuWeeklyQuality();
        });

        $(".curr").click(function(){
            $(this).siblings().removeClass("title-active");
            $(this).addClass("title-active");
            searchParam.operate="curr";
            searchParam.suspend=false;
            imagebank.searchStuWeeklyQuality();
        });

        searchParam.operate="curr";
        $(".next").click(function(){
            if($("#curDate").val()!=$("#refDate").val()){
                $(this).siblings().removeClass("title-active");
                $(this).addClass("title-active");
                searchParam.operate="next";
                searchParam.suspend=false;
                imagebank.searchStuWeeklyQuality();
            }else{
                $(".curr").siblings().removeClass("title-active");
                $(".curr").addClass("title-active");
            }
        });

        $("#selGrade").change(function(){
            searchParam.suspend=true;
            imagebank.changeGrade();
        });

        $("#selClass").change(function(){
            searchParam.suspend=true;
            imagebank.changeClass();
        });

        $("#selStu").change(function(){
            searchParam.suspend=true;
            imagebank.searchStuWeeklyQuality();
        });
    }

    imagebank.changeGrade = function(){
        var selData = {};
        selData.gradeId=$("#selGrade").val();
        Common.getPostData('/qualityitem/getGradeClassValue.do', selData, function(rep){
            var list = rep.classList;
            var html='';
            $.each(list,function(i,item){
                html+='<option value='+item.id+'>'+item.className+'</option>';
            });
            $("#selClass").html(html);
            imagebank.changeClass();
        });
    }

    imagebank.changeClass = function(){
        var selData = {};
        selData.classId=$("#selClass").val()||"";
        Common.getPostData('/qualityitem/getClassStudentValue.do', selData, function(rep){
            var html = '';
            if(rep.classId!="") {
                var list = rep.userInfos;
                $.each(list, function (i, item) {
                    html += '<option value=' + item.id + '>' + item.userName + '</option>';
                });
                $("#selStu").html(html);
                imagebank.searchStuWeeklyQuality();
            }else{
                $("#selStu").html(html);
            }
        });
    }

    imagebank.searchStuWeeklyQuality = function (){
        if(searchParam.bigTitle=="YHGL") {
            searchParam.stuId = $("#selStu").val()||"";
        }
        searchParam.refDate=$("#refDate").val();
        Common.getPostData('/qualityitem/searchStuWeeklyQuality.do', searchParam,function(rep){
            $("#refDate").val(rep.refDate);
            var thead,tbody;
            if(searchParam.bigTitle=="YHGL") {
                $('#yxTheadT').html("");
                Common.render({tmpl: $('#j-tmpl'), data: rep.titleDates, context: '#yxTheadT'});
                $('#yxTbodyT').html("");
                Common.render({tmpl: $('#j-tmpl2'), data: rep.resultList, context: '#yxTbodyT'});
                $(".operate").click(function(){
                    var flag = $(this).attr("flag");
                    if(flag!="2"){
                        var id = $(this).attr("id");
                        var flagDate = $(this).attr("flagDate");
                        var currencyType = $(this).parent().attr("currencyType");
                        imagebank.editStuWeeklyQuality(id, currencyType, flagDate, flag);
                    }
                });
            }else{
                $('#yxTheadS').html("");
                Common.render({tmpl: $('#j-tmpl'), data: rep.titleDates, context: '#yxTheadS'});
                $('#yxTbodyS').html("");
                Common.render({tmpl: $('#j-tmpl3'), data: rep.resultList, context: '#yxTbodyS'});
            }
        });
    }

    imagebank.editStuWeeklyQuality = function (id, currencyType, flagDate, flag){
        var param={};
        param.id=id;
        param.currencyType=currencyType;
        param.gradeId=$("#selGrade").val();
        param.classId=$("#selClass").val()||"";
        param.stuId = $("#selStu").val();
        param.flagDate=flagDate;
        param.flag=flag;
        Common.getPostData('/qualityitem/editStuWeeklyQuality.do', param, function(rep){
            if(rep.result==true) {
                searchParam.suspend=true;
                imagebank.searchStuWeeklyQuality();
            }
        });
    }

    module.exports=imagebank;
});