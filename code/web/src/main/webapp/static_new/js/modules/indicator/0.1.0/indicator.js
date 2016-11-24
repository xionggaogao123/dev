/**
 * @author 李伟
 * @module  成绩分析
 * @description
 * 成绩分析模块
 */
/* global Config */
define('indicator',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var indicator = {},
        Common = require('common');
    var indiData = {};

    indicator.init = function() {
        Common.cal('calId');
        Common.leftNavSel();
        indiData.context='.dl-bg';
        indiData.treeId=$("#treeId").val();
        indiData.parentId="";
        indiData.level=1;
        indiData.searchType="sel";
        indicator.getIndicatorList();

        $('body').on('click', '.em-jia', function(){
            if($(this).hasClass("em-jia")){
                var id = $(this).parent().attr("tid");
                var zid = $(this).parent().attr("zid");
                var isOpen = $("dt[zid='"+zid+"']").attr("isOpen")||0;
                if(isOpen==0) {
                    var level = $(this).parent().attr("level");
                    indiData.context = "#" + id + "_dl";
                    indiData.parentId = zid;
                    indiData.level = parseInt(level) + 1;
                    indicator.getIndicatorList();
                }
                $(this).removeClass("em-jia").addClass("em-jian");
                $(this).siblings("dl").show();
            }
        });

        $('body').on('click', '.em-jian', function(){
            if($(this).hasClass("em-jian")){
                $(this).removeClass("em-jian").addClass("em-jia");
                $(this).siblings("dl").hide();
            }
        });


        $('body').on('click','.com-BC, .go-back',function() {
            Common.goTo("/indicator/iTreeManage.do?index=6&version=3");
        });

      /*  $('body').on('click', '.sel-edit', function(){*/
          /*  $(this).parent().siblings("dl").find("label").show();
            $(this).parent().siblings("dl").find("dl").removeClass("dl-none");
            *//*$(this).parent().siblings("dl").find("input").removeAttr("readonly");
      *//*    $(this).parent().siblings("dl").find("input").addClass("input-edit");*//*
            if($(this).parent().siblings("dl").find("input").siblings("label").hasClass("lab-add")){
                $(this).parent().siblings("dl").find("label").prev().removeAttr("readonly");
                $(this).parent().siblings("dl").find(".lab-add").prev().css("background","#ff8a00");
                }
            $(this).parent().parent().attr("edit",1);*/
          /*  $(this).siblings('input').removeAttr("readonly");
            $(this).siblings('input').css("background","#ff8a00");
            alert(1111);*/
       /* });*/
       /* $('body').on('click','.sel-edit-f',function(){
            $(this).siblings("label").show();
        })*/

        $('body').on('click','.lab-rel',function() {
            if ($(this).siblings("input").hasClass("del-cor")) {
                var obj=$(this).siblings("input");
                indicator.saveIndicator(obj);
                $(this).siblings("input").removeClass("del-cor");
                $(this).siblings("input").attr("readonly","readonly");
                $(this).text("编辑");
            } else {
                $(this).siblings("input").removeAttr("readonly");
                $(this).siblings("input").addClass("del-cor");
                $(this).text("保存");
            }
        });

        $('body').on('click','.tab-main-right span',function(){
            $(this).parent().siblings("ul").append("<li class='dl-f' edit='0'><dl><dt tid='' zid='' level='1'><!--<em class='em-jia'></em>--><input class='input-edit del-cor' placeholder='' value=''><label class='lab-del'>删除</label><label>|</label><label class='lab-rel'>保存</label></dt></dl></li>")
        });
        /*********************添加子指標*******************/
        $('body').on('click', '.lab-add', function(){
            $(this).siblings("em").removeClass("em-jia").addClass("em-jian");
            $(this).siblings("dl").show();
            var level = $(this).parent().attr("level");
            level=parseInt(level)+1;
            var zid = $(this).parent().attr("zid");
            var isOpen = $("dt[zid='"+zid+"']").attr("isOpen")||0;
            if(isOpen==0) {
                var id = $(this).parent().attr("tid");
                indiData.context = "#" + id + "_dl";
                indiData.parentId = zid;
                indiData.level = level;
                indicator.getIndicatorList();
            }
            $(this).siblings("dl").append("<dt tid='' zid='' level='"+level+"'><!--<em class='em-jia'></em>--><input class='input-edit del-cor' placeholder='' value=''/><label class='lab-del'>删除</label><label>|</label><label class='lab-rel'>保存</label></dt>");
        });

        $('body').on('click', '.lab-del', function(){
            var id=$(this).parent().attr("tid");
            indiData.level=$(this).parent().attr("level");
            if ($.trim(id)!="") {
                var type=$(this).parent().attr("type");
                indicator.deleteIndicator(id, type, $(this));
            }else{
                if(indiData.level==1){
                    $(this).parent().parent().parent().remove();
                }else{
                    $(this).parent().remove();
                }
            }
        });

        $('body').on('click', '.lab-dell', function() {
            var id = $(this).parent().attr("tid");
            if ($.trim(id)!="") {
                indiData.level = $(this).parent().attr("level");
                var type = $(this).parent().attr("type");
                indicator.deleteIndicator(id, type, $(this));
             }else{
                $(this).parent().remove();
            }
        });
    };
    indicator.saveIndicator = function(obj){
        var addParam={};
        addParam.name=obj.val();
        if($.trim(addParam.name)==""){
            alert("请输入指标名称!");
            return;
        }
        addParam.id=obj.parent().attr("tid");
        addParam.zid=obj.parent().attr("zid");
        addParam.treeId=$("#treeId").val();
        addParam.level=obj.parent().attr("level");
        addParam.parentId=obj.parent().parent().parent().attr("zid")||"";
        Common.getPostBodyData('/indicator/addOrEditIndicator.do', addParam,function(rep){
            var json = eval('(' + rep + ')');
            if(json.code==200){
                indiData.searchType="addOrEdit";
                //indiData.level=
                //indicator.getIndicatorList();
                var dto=json.message.dto;
                var parentObj=obj.parent();
                parentObj.attr("tid",dto.id);
                parentObj.attr("zid",dto.zid);
                parentObj.attr("level",dto.level);
                parentObj.attr("type",dto.type);
                parentObj.attr("title",dto.name);
                if(parentObj.parent().parent().attr("type")=="2"){
                    parentObj.parent().parent().attr("type",1);
                }
                var html='';
                if(dto.level<6){
                    html='<em class="em-jia"></em>';
                    html+='<input readonly="readonly"  placeholder="'+dto.name+'" value="'+dto.name+'"/>';
                    html += '<label class="lab-add">添加子指标</label><label>|</label>';
                }else{
                    html+='<input readonly="readonly"  placeholder="'+dto.name+'" value="'+dto.name+'"/>';
                }
                html += '<label class="lab-del">删除</label>';
                html += '<label>|</label><label class="lab-rel">编辑</label>';
                html+='<dl id="'+dto.id+'_dl" class="dl-sel dl-none"></dl>';
                parentObj.html(html);
                //alert(rep.message.msg);
            }else{
                alert(json.message);
            }
        });
    }

    indicator.deleteIndicator = function(id, type, obj){
        var delParam={};
        delParam.id=id;
        var msg="";
        if(type==1){
            msg="删除该指标信息,该指标下的所有子指标也会被删除,您确定删除吗？";
        }else{
            msg="您确定删除该指标信息吗？";
        }
        if(confirm(msg)){
            Common.getPostData('/indicator/delIndicatorById.do', delParam,function(rep){
                if(rep.code==200){
                    indiData.searchType="del";
                    if(indiData.level==1){
                        obj.parent().parent().parent().remove();
                    }else{
                        obj.parent().remove();
                    }
                    alert(rep.message);
                }else{
                    alert(rep.message);
                }
            });
        }
    };

    indicator.getIndicatorList = function(){
        if(indiData.type!="sel"||$(indiData.context).html()==""){
            Common.getPostData('/indicator/getIndicatorList.do', indiData,function(rep){
                $(indiData.context).html("");
                if(rep.code==200){
                    Common.render({tmpl: $('#j-tmpl'), data: rep, context: indiData.context});
                    $("dt[zid='"+indiData.parentId+"']").attr("isOpen",1);
                }
            });
        }
    };

    module.exports=indicator;
});