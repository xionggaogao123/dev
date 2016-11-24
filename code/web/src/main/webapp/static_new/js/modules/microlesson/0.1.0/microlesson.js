/*
 * @Author: Tony
 * @Date:   2015-08-14 14:11:24
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-08-14 15:24:25
 */

'use strict';
define(['doT','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var microlesson = {},

        Common = require('common');

    var microlessonData = {};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiaoyujv_start.init()
     */
    microlesson.init = function(){
        if (GetQueryString("a")!="10000") {


        }

        //设置初始页码
        microlessonData.page = 1;
        //设置每页数据长度
        microlessonData.pageSize = 12;
        /*
         * 绑定发起比赛按钮
         * */
        $('.kaishi').bind("click",function(event){
            //Common.getData('/microlesson/micromatchpage.do',microlessonData,function(rep){
            //
            //});
            if (GetQueryString("a")!="10000") {
                window.location='/microlesson/micromatchpage.do';
            } else {
                window.location='/microlesson/micromatchpage.do?a=10000';
            }

        });
        $('.kaishi1').bind("click",function(event){
            //Common.getData('/microlesson/micromatchpage.do',microlessonData,function(rep){
            //
            //});
            window.location='/microlesson/micromatchpage.do?a=10000';
        });


        // 查询数据
        microlesson.selMicroMatch();


    };

    microlesson.selMicroMatch=function() {
        Common.getData('/microlesson/matchlist.do', microlessonData,function(rep){
            $('.listbiao').html('');
            if (rep!=null && rep.rows!=null) {
                Common.render({tmpl: $('#matchlist'), data: rep, context: '.listbiao'});
            }

            /*
             * 绑定发起比赛按钮
             * */
            $('.match').bind("click",function(event){
                var id=$(this).attr('cid');
                if (GetQueryString("a")!="10000") {
                    window.location ='/microlesson/matchDetailInfo.do?matchid='+ id;
                } else {
                    window.location ='/microlesson/matchDetailInfo.do?matchid='+ id+'&a=10000';
                }
            });
            var option = {
                total: rep.total,
                pagesize: rep.pageSize,
                currentpage: rep.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).click(function(){
                            microlessonData.page=$(this).text();
                            microlesson.selMicroMatch();
                        })
                    });
                    $('.first-page').click(function(){
                        microlessonData.page=1;
                        microlesson.selMicroMatch();
                    });
                    $('.last-page').click(function(){
                        microlessonData.page=totalPage;
                        microlesson.selMicroMatch();
                    })
                }
            }
            microlesson.initPaginator(option);
        });
    }

    // 分页初始化
    microlesson.initPaginator=function (option) {
        var totalPage = '';
        $('.page-paginator').show();
        $('.page-index').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        microlesson.buildPaginator(totalPage, option.currentpage);
        option.operate(totalPage);
    }

    microlesson.buildPaginator =function (totalPage, currentPage) {
        if (totalPage > 5) {
            if (currentPage < 4) {
                for (var i = 1; i <= 5; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
                $('.page-index').append('<i>···</i>');
                for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else {
                $('.page-index').append('<i>···</i>');
                for (var i = totalPage - 4; i <= totalPage; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
            }
        } else {
            for (var i = 1; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
        }
    }


    /*比赛列表*/
    //var datalist1 = {
    //    rspCode:200,
    //    rspDesc:'通讯成功',
    //    data:[
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        },
    //        {
    //            imgUrl:'http://placehold.it/230x165',
    //            tit:'安徽省高中化学微课评比',
    //            date:'2015.08.10 – 2015.09.10'
    //        }
    //    ]
    //};
    //Common.render({tmpl:$('#listbiao'),data:datalist1,context:'.listbiao'});
    /*比赛列表*/
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }


















    microlesson.init();
});