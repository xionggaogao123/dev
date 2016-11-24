/**
 * Created by guojing on 2016/6/15.
 */
/* global Config */
define('teakaoqin',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var teakaoqin = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teakaoqin.init()
     */
    teakaoqin.init = function(){


        $(".stukq-main-nav ul li").click(function(){
            searchData.type=$(this).attr("id");
            $(this).parent().find('li').removeClass('stukq-main-active');
            $(this).prop("class","stukq-main-active");
            teakaoqin.searchTeaKaoQinData();
        });

        searchData.type=$(".stukq-main-active").attr("id");

        $('#dept').change(function () {
            teakaoqin.searchTeaKaoQinData();
        });
        teakaoqin.searchTeaKaoQinData();
    };
    
    //查询参数
    var searchData = {};
    //查询
    teakaoqin.searchTeaKaoQinData = function(){
        searchData.deptId=$('#dept').val();
        Common.getData('/smartCard/searchTeaKaoQinData.do',searchData,function(rep){
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
                var userId=$(this).attr("id");
                var url="/smartCard/userKaoQin.do?userId="+userId;
                Common.goTo(url);
            });
        });
    }

    module.exports=teakaoqin;
});

