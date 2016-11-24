/**
 * Created by guojing on 2016/6/15.
 */
/* global Config */
define('stukaoqindetail',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var stukaoqindetail = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * stukaoqindetail.init()
     */
    stukaoqindetail.init = function(){


        $('#searchBtn').click(function () {
            stukaoqindetail.searchKaoQinData();
        });
        stukaoqindetail.searchKaoQinData();

        $(".stukq-back").click(function () {
            var classId=$("#classId").val();
            var className=$("#className").val();
            var gradeId=$("#gradeId").val();
            var gradeName=$("#gradeName").val();
            var url="/smartCard/classStuKaoQin.do?classId="+classId+"&className="+className+"&gradeId="+gradeId+"&gradeName="+gradeName;
            Common.goTo(url);
        });
    };

    //查询参数
    var searchData = {};
    //查询
    stukaoqindetail.searchKaoQinData = function(){
        searchData.selDate=$('#selDate').val();
        searchData.userId=$('#userId').val();
        Common.getData('/smartCard/searchKaoQinData.do',searchData,function(rep){
            $('.mykq-table').html('');
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.mykq-table'});
        });
    }

    module.exports=stukaoqindetail;
});

