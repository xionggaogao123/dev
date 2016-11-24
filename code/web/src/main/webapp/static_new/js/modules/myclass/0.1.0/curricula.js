/**
 * Created by guojing on 2015/8/7.
 */

define('curricula',['jquery','doT','easing','common'],function (require, exports, module) {
    /**
     *初始化参数
     */
    require('jquery');
    require('doT');
    require('easing');
    var curricula = {},
        Common = require('common');

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * moralculturemanage.init()
     */
    curricula.init = function(){

        //
        curricula.initCurriculaData();

        $('body').on('click', '.detail', function(){
            //alert($(this).attr('id'));
            window.open('/myclass/studentScore.do?classId=' + $(this).attr('id') + '&stuId=' + $(this).attr('sid'));
        })
    };

    /**
     * 查询德育项目信息数据
     */
    curricula.initCurriculaData=function(){
        //查询参数
        var searchData = {};
        searchData.schoolId=$('#schoolId').val();
        searchData.gradeId=$('#gradeId').val();
        searchData.classId=$('#classId').val();
        searchData.page=1;
        searchData.pageSize=100;
        Common.getData('/myclass/studentInterestClassCount.do', searchData,function(rep){
            $('.sub-info-list').html("");
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.sub-info-list'});
        });
    }

    module.exports=curricula;
});
