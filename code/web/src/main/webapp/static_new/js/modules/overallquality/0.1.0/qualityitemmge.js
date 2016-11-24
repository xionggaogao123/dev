/**
 * Created by guojing on 2016/8/11.
 */
define('qualityitemmge',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var qualityitemmge = {},
        Common = require('common');

    var searchParam={};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * qualityitemmge.init()
     */
    qualityitemmge.init = function(){


        var name = $(".yx-active").attr("id");
        $(".blgl-main>div").hide();
        $("#" + "tab-" + name).show();

        if(name=="PBSZ"){
            qualityitemmge.searchQualityItemList();
        }else if(name=="MDBL"){
            $(".bluo-build").show();
            var gId=$("#gradeId").val()||"";
            if(gId!=""){
                qualityitemmge.changeGradeTwo();
            }else{
                qualityitemmge.searchClassBuLuoInfo();
            }
        }

        $(".new-add").click(function(){
            $(".bg").show();
            $(".newadd-alert").show();
        });

        $(".alert-close,.alert-btn-qx").click(function(){
            $("#itemId").val("");
            $("#itemName").val("");
            $("#scoreId").val("");
            $(".bg").hide();
            $(".newadd-alert").hide();
        });
        
        $(".blgl-tab li").click(function(){
            $(".blgl-tab li").removeClass("yx-active");
            $(this).addClass("yx-active");
            $(".blgl-main>div").hide();
            var name = $(this).attr("id");
            if(name=="PBSZ"){
                qualityitemmge.searchQualityItemList();
            }
            if(name=="BLGL"){
                $("#selGrade").get(0).selectedIndex = 0;
                qualityitemmge.changeGrade();
            }
            if(name=="MDBL"){
                $(".bluo-build").show();
                $("#gradeId").get(0).selectedIndex = 0;
                qualityitemmge.changeGradeTwo();
            }else{
                $(".bluo-build").hide();
            }
            $("#" + "tab-" + name).show();
        });

        $(".alert-btn-sure").click(function(){
            qualityitemmge.addQualityItem();
        });

        searchParam.operate="curr";
        $("#selGrade").change(function(){
            searchParam.suspend=true;
            qualityitemmge.changeGrade();
        });

        $("#gradeId").change(function(){
            qualityitemmge.changeGradeTwo();
        });

        $("#selClass").change(function(){
            searchParam.suspend=true;
            qualityitemmge.searchClassWeeklyQuality();
        });

        $("#classId").change(function(){
            qualityitemmge.searchClassBuLuoInfo();
        });

        $("#prev").click(function(){
            $(this).siblings().removeClass("title-active");
            $(this).addClass("title-active");
            searchParam.operate="prev";
            searchParam.suspend=false;
            qualityitemmge.searchClassWeeklyQuality();
        });

        $("#curr").click(function(){
            $(this).siblings().removeClass("title-active");
            $(this).addClass("title-active");
            searchParam.operate="curr";
            searchParam.suspend=false;
            qualityitemmge.searchClassWeeklyQuality();
        });

        $("#next").click(function(){
            if($("#curDate").val()!=$("#refDate").val()){
                $(this).siblings().removeClass("title-active");
                $(this).addClass("title-active");
                searchParam.operate="next";
                searchParam.suspend=false;
                qualityitemmge.searchClassWeeklyQuality();
            }else{
                $("#curr").siblings().removeClass("title-active");
                $("#curr").addClass("title-active");
            }
        });

        $(".bluo-build").click(function(){
            window.open("/qualityitem/buLuoBuild.do?gradeId="+$("#curGradeId").val()+"&classId="+$("#curClassId").val());
        });
    };

    qualityitemmge.searchQualityItemList = function(){
        var selParam={};
        Common.getData("/qualityitem/searchQualityItemList.do", selParam,function(rep){
            $('.info-list').html("");
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.info-list'});

            $(".table-edit").click(function(){
                qualityitemmge.editQualityItem($(this).parent().attr("id"));
            });

            $(".table-del").click(function(){
                qualityitemmge.delQualityItem($(this).parent().attr("id"));
            });
        });
    };
    qualityitemmge.editQualityItem = function(id){
        var selParam={};
        selParam.id=id;
        Common.getData("/qualityitem/getQualityItem.do", selParam, function(rep){
            var obj=rep.dto;
            $("#itemId").val(obj.id);
            $("#itemName").val(obj.itemName);
            $("#scoreId").val(obj.scoreSetId);
            $(".bg").show();
            $(".newadd-alert").show();
        });
    }
    qualityitemmge.delQualityItem = function(id){
        if (confirm('确认删除此条项目信息？\n删除后，历史分将会保留！')){
            var delParam={};
            delParam.id=id;
            Common.getData("/qualityitem/delQualityItem.do", delParam, function(rep){
                if(rep.result==true){
                    alert("删除成功!");
                    qualityitemmge.searchQualityItemList();
                }
            });
        }
    }
    qualityitemmge.addQualityItem = function(){
        var addParam={};
        addParam.id = $("#itemId").val();
        addParam.itemName = $("#itemName").val();
        if(addParam.itemName==""&&$.trim(addParam.itemName)==""){
            alert("请输入评比名称!");
            return;
        }
        addParam.scoreId = $("#scoreId").val();
        if(addParam.scoreId==""){
            alert("请选择对应分值!");
            return;
        }
        Common.getData("/qualityitem/addOrEditQualityItem.do", addParam,function(rep){
            if(rep.result==true){
                $("#itemId").val("");
                $("#itemName").val("");
                $("#scoreId").val("");
                $(".bg").hide();
                $(".newadd-alert").hide();
                alert("保存成功!");
                qualityitemmge.searchQualityItemList();
            }else {
                if (addParam.id == "") {
                    $("#itemName").val("");
                    alert("评比名称已存在!");
                }else{
                    alert("评比名称重复!");
                }
            }
        });
    }


    qualityitemmge.changeGrade = function(){
        var selData = {};
        selData.gradeId=$("#selGrade").val();
        Common.getPostData('/qualityitem/getGradeClassValue.do', selData,function(rep){
            var list = rep.classList;
            var html='';
            $.each(list,function(i,item){
                html+='<option value='+item.id+'>'+item.className+'</option>';
            });
            $("#selClass").html(html);
            qualityitemmge.searchClassWeeklyQuality();
        });
    }

    qualityitemmge.changeGradeTwo = function(){
        var selData = {};
        selData.gradeId=$("#gradeId").val();
        Common.getPostData('/qualityitem/getGradeClassValue.do', selData,function(rep){
            var list = rep.classList;
            var html='';
            $.each(list,function(i,item){
                html+='<option value='+item.id+'>'+item.className+'</option>';
            });
            $("#classId").html(html);
            qualityitemmge.searchClassBuLuoInfo();
        });
    }

    qualityitemmge.searchClassWeeklyQuality = function (){
        searchParam.gradeId=$("#selGrade").val();
        searchParam.classId=$("#selClass").val()||"";
        searchParam.refDate=$("#refDate").val();
        Common.getPostData('/qualityitem/searchClassWeeklyQuality.do', searchParam,function(rep){
            $('#yxThead').html("");
            $('#yxTbody').html("");
            if(rep.classId!="") {
                $("#refDate").val(rep.refDate);
                Common.render({tmpl: $('#j-tmpl2'), data: rep.titleDates, context: '#yxThead'});
                Common.render({tmpl: $('#j-tmpl3'), data: rep.resultList, context: '#yxTbody'});
                $(".operate").click(function () {
                    var flag = $(this).attr("flag");
                    if (flag != "2") {
                        var id = $(this).attr("id");
                        var flagDate = $(this).attr("flagDate");
                        var itemId = $(this).parent().attr("itemId");
                        qualityitemmge.editClassWeeklyQuality(id, itemId, flagDate, flag);
                    }
                })
            }
        });
    }

    qualityitemmge.editClassWeeklyQuality = function (id, itemId, flagDate, flag){
        var param={};
        param.id=id;
        param.itemId=itemId;
        param.gradeId=$("#selGrade").val();
        param.classId=$("#selClass").val()||"";
        param.flagDate=flagDate;
        param.flag=flag;
        Common.getPostData('/qualityitem/editClassWeeklyQuality.do', param,function(rep){
            if(rep.result==true) {
                searchParam.suspend=true;
                qualityitemmge.searchClassWeeklyQuality();
            }
        });
    }

    qualityitemmge.searchClassBuLuoInfo = function(){
        var bluoParam={};
        bluoParam.gradeId=$("#gradeId").val()||"";
        bluoParam.classId=$("#classId").val()||"";
        $("#curGradeId").val("");
        $("#curClassId").val("");
        Common.getPostData('/qualityitem/searchClassBuLuoInfo.do', bluoParam,function(rep){
            $('.rank-list').html("");
            if(rep.classId!="") {
                $("#number").text("第" + rep.number + "名");
                var dto = rep.dto;
                $("#curGradeId").val(dto.gradeId);
                $("#curClassId").val(dto.classId);
                $.each(dto.coqsiList, function (i, item) {
                    if (item.type == "hg") {
                        $("#hanggui").text(item.score);
                    }
                    if (item.type == "ws") {
                        $("#weisheng").text(item.score);
                    }
                    if (item.type == "td") {
                        $("#tiduan").text(item.score);
                    }
                });

                $("#baseCampCount").text(dto.baseCampCount);
                $("#castleCount").text(dto.castleCount);
                $("#villagerCount").text(dto.villagerCount);
                $("#soldiersCount").text(dto.soldiersCount);
                Common.render({tmpl: $('#j-tmpl4'), data: rep, context: '.rank-list'});
            }else{
                $("#hanggui").text(0);
                $("#weisheng").text(0);
                $("#tiduan").text(0);
                $("#baseCampCount").text(0);
                $("#castleCount").text(0);
                $("#villagerCount").text(0);
                $("#soldiersCount").text(0);
            }
        });
    }

    module.exports=qualityitemmge;
});