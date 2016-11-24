/**
 * Created by guojing on 2016/6/15.
 */
/* global Config */
define('gradekaoqin',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var gradekaoqin = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * gradekaoqin.init()
     */
    gradekaoqin.init = function(){


        $(".headkq-main-nav ul li").click(function(){
            searchData.type=$(this).attr("id");
            $(this).parent().find('li').removeClass('stukq-main-active');
            $(this).prop("class","stukq-main-active");
            gradekaoqin.searchGradeKaoQinData();
        });

        searchData.type=$(".stukq-main-active").attr("id");
        gradekaoqin.searchGradeKaoQinData();
    };
    
    //查询参数
    var searchData = {};
    //查询
    gradekaoqin.searchGradeKaoQinData = function(){
        Common.getData('/smartCard/searchGradeKaoQinData.do',searchData,function(rep){
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
                var gradeId=$(this).attr("id");
                var gradeName=$(this).attr("name");
                var url="/smartCard/classKaoQin.do?gradeId="+gradeId+"&gradeName="+gradeName;
                Common.goTo(url);
            });
        });
    }

    module.exports=gradekaoqin;
});

