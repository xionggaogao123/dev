/**
 * Created by guojing on 2016/6/15.
 */
/* global Config */
define('classkaoqin',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var classkaoqin = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * classkaoqin.init()
     */
    classkaoqin.init = function(){


        $(".headkq-main-nav ul li").click(function(){
            searchData.type=$(this).attr("id");
            $(this).parent().find('li').removeClass('stukq-main-active');
            $(this).prop("class","stukq-main-active");
            classkaoqin.searchClassKaoQinData();
        });

        $(".headkq-back").click(function () {
            var url="/smartCard/gradeKaoQin.do";
            Common.goTo(url);
        });

        searchData.type=$(".stukq-main-active").attr("id");
        classkaoqin.searchClassKaoQinData();
    };
    
    //查询参数
    var searchData = {};
    //查询
    classkaoqin.searchClassKaoQinData = function(){
        searchData.gradeId=$("#gradeId").val();
        Common.getData('/smartCard/searchClassKaoQinData.do',searchData,function(rep){
            var kaoQinReta=rep.kaoQinReta;
            $("#normal").text(kaoQinReta.normalReta);
            $("#late").text(kaoQinReta.lateReta);
            $("#punctual").text(kaoQinReta.punctualReta);
            $("#kuangke").text(kaoQinReta.kuangkeReta);
            $('.stukq-data').html('');
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.stukq-data'});
            /* 
             * 跳转到详情信息
             * */
            $('.user-tr').dblclick(function(event){
                var classId=$(this).attr("id");
                var className=$(this).attr("name");
                var url="/smartCard/classStuKaoQin.do?classId="+classId+"&className="+className+"&gradeId="+$("#gradeId").val()+"&gradeName="+$("#gradeName").val();
                Common.goTo(url);
            });
        });
    }

    module.exports=classkaoqin;
});

