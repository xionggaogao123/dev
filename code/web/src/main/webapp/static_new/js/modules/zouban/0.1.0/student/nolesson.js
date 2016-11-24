/**
 * Created by qiangm on 2015/8/20.
 */
'use strict';
define(['doT', 'common', 'jquery'],function (require, exports, module) {
    /**
     *初始化参数
     */
    require("jquery");
    require("doT");
    var nolesson = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    nolesson.init = function () {



    };
    $(document).ready(function(){
        $(".class-select").click(
            function(){
                $(".class-list").removeClass("cont-style");
                $(".class-select").addClass("cont-style");
                $(".right-main1").hide();
                $(".right-main2").show();}
        );
        $(".class-list").click(
            function(){
                $(".class-select").removeClass("cont-style");
                $(".class-list").addClass("cont-style");
                $(".right-main1").show();
                $(".right-main2").hide();}
        );
    })


    nolesson.init();
});