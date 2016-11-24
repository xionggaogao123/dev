
'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','fancybox','experienceScore'],function(require,exports,module){
    /**
     *初始化参数
     */
    var peoplegroup = {},
        Common = require('common');
    //提交参数
    var peoplegroupData = {};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * homepage.init()
     */
    peoplegroup.init = function(){


        peoplegroup.selRoomDetail();
        $(".population-po").click(function(){
            peoplegroup.selLoopInfoList();
            $(".population-tb").show();
        })

        $('.population-II').click(function(){
            $("#population-II").hide();
            $("#population-I").show();
        })
        $(".population-poo").click(function(){
            $("#population-II").hide();
            $("#population-III").show();
        })
        $(".population-III").click(function(){
            $("#population-II").show();
            $("#population-III").hide();
        });

    };
    peoplegroup.selRoomDetail = function() {
        Common.getData('/population/selRoomDetail.do',peoplegroupData,function(rep){
            $('.hwk1').html('');
            Common.render({tmpl: $('#hwk1_templ'), data: rep, context: '.hwk1'});
        });
    }

    peoplegroup.selLoopInfoList = function() {
        Common.getData('/population/selLoopInfoList.do',peoplegroupData,function(rep){
            $('.hwk2').html('');
            Common.render({tmpl: $('#hwk2_templ'), data: rep, context: '.hwk2'});
            $(".population-F").click(function(){
                peoplegroup.selRoomInfoList();
                $("#population-I").hide();
                $("#population-II").show();
                $(".population-tb").hide();
                $(".population-tri").hide();
            });
        });
    }

    peoplegroup.selRoomInfoList = function() {
        Common.getData('/population/selRoomInfoList.do',peoplegroupData,function(rep){
            $('.hwk3').html('');
            Common.render({tmpl: $('#hwk3_templ'), data: rep, context: '.hwk3'});
        });
    }
    peoplegroup.init();
});