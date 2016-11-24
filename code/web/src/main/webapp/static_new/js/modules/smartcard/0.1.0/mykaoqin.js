/**
 * Created by guojing on 2016/6/15.
 */
/* global Config */
define('mykaoqin',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var mykaoqin = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * mykaoqin.init()
     */
    mykaoqin.init = function(){


        $('#searchBtn').click(function () {
            mykaoqin.searchKaoQinData();
        });
        mykaoqin.searchKaoQinData();

        $(".stukq-back").click(function () {
            var url=$(this).attr("backUrl");
            Common.goTo(url);
        });
    };

    //查询参数
    var searchData = {};
    //查询
    mykaoqin.searchKaoQinData = function(){
        searchData.selDate=$('#selDate').val();
        searchData.userId=$('#userId').val();
        Common.getData('/smartCard/searchKaoQinData.do',searchData,function(rep){
            $('.mykq-table').html('');
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.mykq-table'});
        });
    }

    module.exports=mykaoqin;
});

