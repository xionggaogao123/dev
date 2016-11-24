
'use strict';
define(['jquery','doT','easing','common','sharedpart','uploadify','fancybox','experienceScore','echarts'],function(require,exports,module){
    /**
     *初始化参数
     */
    var track = {},
        Common = require('common');
    //提交参数
    var consumeData = {};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * homepage.init()
     */
    track.init = function(){


        $('#grade').change(function(event) {
            consumeData.gradeid = $('#grade').val();
            if ($('#grade').val()!="") {
                Common.getData('/myschool/classlist.do',consumeData,function(rep){
                    $('#class').empty();
                    $('#class').append(" <option value=''>全部</option>");
                    for (var i=0;i<rep.rows.length;i++) {
                        $('#class').append("<option value='"+rep.rows[i].id+"'>"+rep.rows[i].className +"</option>");
                    }
                });
            }
        });
        $('#class').change(function(event) {
            consumeData.classid = $('#class').val();
            if ($('#class').val()!="") {
                Common.getData('/myschool/testulist.do',consumeData,function(rep){
                    $('#user').empty();
                    $('#user').append(" <option value=''>全部</option>");
                    for (var i=0;i<rep.stuList.length;i++) {
                        $('#user').append("<option value='"+rep.stuList[i].id+"'>"+rep.stuList[i].userName +"</option>");
                    }
                });
            }
        });

    };

    track.init();
});