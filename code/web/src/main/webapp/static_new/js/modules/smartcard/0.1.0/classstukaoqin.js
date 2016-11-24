/**
 * Created by guojing on 2016/6/15.
 */
/* global Config */
define('classstukaoqin',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var classstukaoqin = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * classstukaoqin.init()
     */
    classstukaoqin.init = function(){


        $(".headkq-main-nav ul li").click(function(){
            searchData.type=$(this).attr("id");
            $(this).parent().find('li').removeClass('stukq-main-active');
            $(this).prop("class","stukq-main-active");
            classstukaoqin.searchStuKaoQinData();
        });

        $(".headkq-back").click(function () {
            var gradeId=$("#gradeId").val();
            var gradeName=$("#gradeName").val();
            var url="/smartCard/classKaoQin.do?gradeId="+gradeId+"&gradeName="+gradeName;
            Common.goTo(url);
        });

        searchData.type=$(".stukq-main-active").attr("id");

        classstukaoqin.searchStuKaoQinData();
    };
    
    //查询参数
    var searchData = {};
    //查询
    classstukaoqin.searchStuKaoQinData = function(){
        searchData.classId=$('#classId').val();
        Common.getData('/smartCard/searchStuKaoQinData.do',searchData,function(rep){
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
                var classId=$('#classId').val();
                var className=$("#className").val();
                var gradeId=$('#gradeId').val();
                var gradeName=$('#gradeName').val();
                var userId=$(this).attr("id");
                var url="/smartCard/classStuKaoQinDetail.do?userId="+userId+"&gradeId="+gradeId+"&gradeName="+gradeName+"&classId="+classId+"&className="+className;
                Common.goTo(url);
            });
        });
    }

    module.exports=classstukaoqin;
});

