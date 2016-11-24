/*
 * @Author: Tony
 * @Date:   2015-06-11 14:24:31
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-06-30 16:43:27
 */

'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','fancybox','experienceScore'],function(require,exports,module){
    /**
     *初始化参数
     */
    var myjiaban = {},
        Common = require('common');
    require('fileupload');
    var someFileFailed = false;
    //提交参数
    var myjiabanData = {};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * zhiban.init()
     */
    myjiaban.init = function() {


        myjiaban.getNowFormatDate();
        if (getQueryString('type')==5) {
            $('#LWDJB').addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            myjiaban.selMyOverTimeInfo();
            $("#tab-LWDJB").show();
        } else if (getQueryString('type')==6) {
            $('#LJBXC').addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            myjiaban.selMyJiaBanSalary(1);
            $("#tab-LJBXC").show();
        }
        $('#LWDJB').click(function () {
            $(this).addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            myjiaban.selMyOverTimeInfo();
            $("#tab-LWDJB").show();
        });
        $('#LJBXC').click(function () {
            $(this).addClass("cur").siblings().removeClass("cur");
            $(".tab-main>div").hide();
            myjiaban.selMyJiaBanSalary(1);
            $("#tab-LJBXC").show();
        });
        //$(".tab-head li").click(function () {
        //    $(this).addClass("cur").siblings().removeClass("cur");
        //    $(".tab-main>div").hide();
        //    var names = $(this).attr("id");
        //    if (names=='LWDJB') {
        //        myjiaban.getNowFormatDate();
        //        myjiaban.selMyOverTimeInfo();
        //    } else if (names=='LJBXC') {
        //        myjiaban.selMyJiaBanSalary(1);
        //    }
        //    $("#" + "tab-" + names).show();
        //});
        $('.view-log').click(function(){
            myjiaban.selMyJiaBanSalary(2);
            $('.zb-lookjl').show();
            $('.tab-LWDJB').hide();
        });
        $('.zhiban-back').click(function(){
            $('.zb-lookjl').hide();
            $('.tab-LWDJB').show();
        });
        $('#search-salary').click(function(){
            myjiaban.selMyJiaBanSalary(1);
        });
        $('.zhiban-qr').click(function(){
            myjiaban.selMyJiaBanSalary(2);
        });
        $('.JBSQ-daochu').click(function(){
            window.location.href="/overTime/exportMySarlaryList.do?year="+$("#year").val()+"&month="+$("#month").val();
        });
    }
    myjiaban.selMyOverTimeInfo = function() {
        Common.getData('/overTime/selMyOverTimeInfo.do', myjiabanData,function(rep){
            $('.myOverTime').html('');
            Common.render({tmpl:$('#myOverTime_templ'),data:rep,context:'.myOverTime'});
        });
    }

    myjiaban.selMyJiaBanSalary = function(type) {
        if (type==1) {
            myjiabanData.year = $('#year').val();
            myjiabanData.month = $('#month').val();
        } else {
            myjiabanData.year = $('#year2').val();
            myjiabanData.month = $('#month2').val();
        }

        Common.getData('/overTime/selMyJiaBanSalary.do', myjiabanData,function(rep){
            $('.myJiaBanSalary').html('');
            Common.render({tmpl:$('#myJiaBanSalary_templ'),data:rep,context:'.myJiaBanSalary'});
            $('.jiaban-log').html('');
            Common.render({tmpl:$('#jiaban-log_templ'),data:rep,context:'.jiaban-log'});
            $('.view').click(function() {
                var url = $(this).attr("pth");
                var urlarg = url.split(".")[url.split(".").length-1];
                if (urlarg=="doc"||urlarg=="docx"||urlarg=="xls"||urlarg=="xlsx"||urlarg=="ppt"||urlarg=="pptx") {
                    window.open("http://ow365.cn/?i=9666&furl="+url);
                } else {
                    window.open(url);
                }
            });
        });
    }
    myjiaban.getNowFormatDate = function() {
        var date = new Date();
        var seperator1 = "/";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        $('#month').val(month);
        $('#month2').val(month);
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
        $('#curtime').html(currentdate);
    }
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    myjiaban.init();
});