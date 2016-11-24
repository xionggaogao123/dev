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
    var documentDetail = {},
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * teacher_homepage.init()
     */
    documentDetail.init = function () {
        if (GetQueryString("a")!="10000") {


        }
    };

    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }

    documentDetail.getDocumentDetail=function()
    {
        var docId=$("body").attr("docid");
        $.ajax({
            url:"/docflow/getdocdetail.do",
            type:"GET",
            dataType:"json",
            data:{
                docId:docId
            },
            success:function(data){
                Common.render({tmpl: $('#docDetail'), data:  data, context: '.tab_main'});
                $('.Fj').find('a').each(function(index, item){
                    var href = $(item).attr('href');
                    var fileKey = href.substring(href.lastIndexOf('/') + 1);
                    var fileName = $(this).attr("fn");

                    href = '/commonupload/doc/down.do?type=2&fileKey='+fileKey+'&fileName=' + fileName;
                    $(item).attr('href', href)
                })
            }
        });
    };
    documentDetail.init();
    documentDetail.getDocumentDetail();
});