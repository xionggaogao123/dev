/* 
* @Author: Tony
* @Date:   2015-07-22 15:35:23
* @Last Modified by:   Tony
* @Last Modified time: 2015-07-22 16:59:53
*/

'use strict';
define(['doT','common'],function(require,exports,module){

    /**
     *初始化参数
     */
    var xiaoqu = {};
     var  Common = require('common');

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * xiaoquguanli.init()
     */
    xiaoqu.init = function(){



    };
    /*弹出层*/
    $(function() {
        xiaoqu.init();
        xiaoqu.list();
        //删除
        $('body').on('click','#delete', function(){
            var msg = "确认删除！"
            if(confirm(msg) == true) {
                var requestData={};
                requestData.campusId=$(this).attr("value");
                Common.getData('/myschool/deleteCampus.do',requestData, function(resp) {
                    if(resp) {
                        //列表
                        xiaoqu.list();
                    }
                })
            }

        })
        //编辑回显
        $('body').on('click','#edit', function(){
            var requestData={};
            sessionStorage.setItem("add", 0);
            sessionStorage.setItem("campusId",$(this).attr("value"));
            requestData.campusId=$(this).attr("value");
            Common.getData('/myschool/getCampusInfo.do',requestData, function(resp) {
                $(".gay,.zjxq").fadeIn();
                $("#name").val(resp.name);
                $("#addr").val(resp.addr);
                $("#phone").val(resp.phone);
                $("#manager").val(resp.manager);
            })

        })

        var L=0;
        var T=0;
        L=$(window).width()/2-$(".zjxq").width()/2;
        T=$(window).height()/2-$(".zjxq").height()/2;
        $(".zjxq").css({
            'left':L,
            'top':0
        });
        $(".gay").height($(document).height());
        $(".liebiao p span").click(function() {
            $("#name").val('');
            $("#addr").val('');
            $("#phone").val('');
            $("#manager").val('');
            $(".gay,.zjxq").fadeIn();
            sessionStorage.setItem("add", 1);
        });
        $(".zjxq .gb").click(function(){
            $(".gay,.zjxq").fadeOut();
        });
        $(".zjxq .zjxq_main button").click(function() {
            $(".gay,.zjxq").fadeOut();
            var requestData = {};
            requestData.name = $("#name").val();
            requestData.addr = $("#addr").val();
            requestData.phone = $("#phone").val();
            requestData.manager = $("#manager").val();
            if(sessionStorage.getItem("add") == 1) {//添加
                Common.getData('/myschool/addCampus.do', requestData, function (resp) {
                    if (resp) {
                        //列表
                        xiaoqu.list();
                    }
                })
            } else if(sessionStorage.getItem("add") == 0) {//保存编辑
                requestData.campusId = sessionStorage.getItem("campusId");
                Common.getData('/myschool/editCampus.do', requestData, function (resp) {
                    if (resp) {
                        //列表
                        xiaoqu.list();
                    }
                })
            }


        });
    });

    xiaoqu.list = function() {
        var requestData={};
        Common.getData('/myschool/listCampus.do',requestData, function(resp) {
            Common.render({
                tmpl:'#campusListTmpl',
                data : resp.campusList,
                context : '#campusList',
                overwrite : 1
            })
        })
    }
    /*弹出层*/



    //module.exports = xiaoqu;
});