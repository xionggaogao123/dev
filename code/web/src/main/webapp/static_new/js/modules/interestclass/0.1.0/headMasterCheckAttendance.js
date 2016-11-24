/**
 * Created by fl on 2016/5/16.
 */
define(function(require,exports,module){
    var Common = require('common');

    $(function(){



        getGradeAndCategory();
        getPassedWeeks();
        query();
        $('#query').click(function(){
            query();
        })
    })

    function getGradeAndCategory(){
        Common.getData("/myclass/getGradeAndCategory.do", {}, function(resp){
            //console.info(resp);
            Common.render({
                tmpl: '#termsTmpl',
                data: resp.termList,
                context: '#terms',
                overwrite: 1
            });
            Common.render({
                tmpl: '#gradesTmpl',
                data: resp.gradeList,
                context: '#grades',
                overwrite: 1
            });
            Common.render({
                tmpl: '#categoryTmpl',
                data: resp.categoryList,
                context: '#category',
                overwrite: 1
            });
        })
    }

    function getPassedWeeks(){
        var date = new Date();
        var fullYear = date.getFullYear();
        var month = date.getMonth() + 1;
        var year = "";
        var term = 0;
        if(month >= 9){
            year = fullYear + "-" + Number(fullYear + 1) + "学年";
        } else {
            year = Number(fullYear - 1) + "-" + fullYear + "学年";
        }
        if(month>=2 && month<9){
            term = 2;
        } else {
            term = 1;
        }
        Common.getData("/myclass/findPassedWeeks.do", {year:year, term:term}, function(resp){
            if(resp.code == '200'){
                Common.render({
                    tmpl: '#weeksTmpl',
                    data: resp.message,
                    context: '#weeks',
                    overwrite: 1
                });
            } else {
                alert(resp.message);
            }

        })
    }

    transfer = function(n){//阿拉伯数字转成汉字
        var flag = false;
        if(n >= 10 && n < 20){
            flag = true;
        }
        var num = ['','一','二','三','四','五','六','七','八','九'];
        var num2 = ['','十','百','千'];
        var hanzi = [];
        for(var i=0; n>0; i++){
            var yushu = n%10;
            hanzi[i] = num[yushu];
            n = (n-yushu)/10;
        }
        var ret = '';
        for(var i=hanzi.length-1; i>=0; i--){
            ret += hanzi[i] + num2[i];
        }
        if(flag){
            ret = ret.substr(1, 2);
        }
        return ret;
    }

    function query(){
        var requestData = {};
        requestData.termType = -1;
        requestData.gradeId = $('#grades').val();
        requestData.categoryId = $('#category').val();
        requestData.weekIndex = $('#weeks').val();
        Common.getDataAsync("/myclass/getInterestClassAttendanceHeadMaster.do", requestData, function(resp){
            if(resp.message.length <= 0){
                $('.nodata').show();
            } else {
                $('.nodata').hide();
            }
            Common.render({
                tmpl: '#attendanceTmpl',
                data: resp.message,
                context: '#attendance',
                overwrite: 1
            });
        })
    }

    //end
})
