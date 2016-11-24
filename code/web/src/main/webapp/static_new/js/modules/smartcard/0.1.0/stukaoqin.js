/**
 * Created by guojing on 2016/6/15.
 */
/* global Config */
define('stukaoqin',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var stukaoqin = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * stukaoqin.init()
     */
    stukaoqin.init = function(){


        stukaoqin.getTeacherClass();

        $(".stukq-main-nav ul li").click(function(){
            searchData.type=$(this).attr("id");
            $(this).parent().find('li').removeClass('stukq-main-active');
            $(this).prop("class","stukq-main-active");
            stukaoqin.searchStuKaoQinData();
        });

        searchData.type=$(".stukq-main-active").attr("id");

        $('#searchBtn').click(function () {
            stukaoqin.searchStuKaoQinData();
        });

        stukaoqin.searchStuKaoQinData();
    };
    //查询所带班级
    stukaoqin.getTeacherClass = function(){
        searchData.gradeId=$('#selGrade').val();
        if(searchData.gradeId!=null){
            $('#selClass').show();
        }else{
            $('#selClass').hide();
            return;
        }
        Common.getData('/smartCard/getTeacherClass.do',searchData,function(rep){
           var html="";
            $.each(rep.list,function(i,item){
                html+='<option value="'+item.id+'">'+item.className+'</option>';
            });
            $("#selClass").html(html);
        });
    }
    
    //查询参数
    var searchData = {};
    //查询
    stukaoqin.searchStuKaoQinData = function(){
        searchData.classId=$('#selClass').val();
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
                var userId=$(this).attr("id");
                var url="/smartCard/userKaoQin.do?userId="+userId;
                Common.goTo(url);
            });
        });
    }

    module.exports=stukaoqin;
});

