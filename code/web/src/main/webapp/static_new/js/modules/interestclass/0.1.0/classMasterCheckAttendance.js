/**
 * Created by fl on 2016/5/16.
 */
define(function(require,exports,module){
    var Common = require('common');

    $(function(){
        showClassName();
        getGradeAndCategory();
        getInterestClassAttendanceClassMaster();
        getPassedWeeks();

        $('#terms').change(function(){
            getInterestClassAttendanceClassMaster();
            getPassedWeeks();
        })
    })

    function showClassName(){
        var className = sessionStorage.getItem("cnm");
        $('#cnm').text(className);
    }

    function getGradeAndCategory(){
        Common.getData("/myclass/getGradeAndCategory.do", {}, function(resp){
            //console.info(resp);
            Common.render({
                tmpl: '#termsTmpl',
                data: resp.termList,
                context: '#terms',
                overwrite: 1
            });
        })
    }

    function getInterestClassAttendanceClassMaster(){

        var requestData = {};
        requestData.classId = $('body').attr('cid');
        requestData.termType = $('#terms').val();
        var year = $('#terms').find('option:selected').text();
        requestData.year = year.substr(0, 11);
        var term = year.substr(12, 1)=='一' ? 1 : 2;
        requestData.term = term;
        Common.getDataAsync("/myclass/getInterestClassAttendanceClassMaster.do", requestData, function(resp){
            if(resp.code == '200'){
                Common.render({
                    tmpl: '#stusTmpl',
                    data: resp.message,
                    context: '#stus',
                    overwrite: 1
                });
                Common.render({
                    tmpl: '#atTmpl',
                    data: resp.message,
                    context: '#at',
                    overwrite: 1
                });
            } else {
                alert(resp.message);
            }

        })
    }

    function getPassedWeeks(){
        var requestData = {};
        var year = $('#terms').find('option:selected').text();
        requestData.year = year.substr(0, 11);
        var term = year.substr(12, 1)=='一' ? 1 : 2;
        requestData.term = term;
        Common.getDataAsync("/paike/findPassedWeeks.do", requestData, function(resp){
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

    checkAttendance = function(att){
        return att.indexOf('缺') > 0
    }

})