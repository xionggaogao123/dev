/*
 * @Author: Tony
 * @Date:   2015-08-14 16:02:24
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-08-14 17:13:56
 */

'use strict';
define(['doT','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var Common = require('common');
    var wdgzData = {};
    var wdgz = {
        getSalaryTimes: function (year, month) {
            $.ajax({
                url: "/wages/getSalaryTimes.do",
                type: "get",
                datatype: "json",
                data: {year: year, month: month},
                success: function (data) {
                    if (data.code == 200) {
                        wdgz.buildSalaryNumber(data.message);
                    } else {
                        alert(data.message);
                    }
                }
            });
        },
        buildSalaryNumber: function (data) {
            $("#salaryNumber").empty();
            var times;
            for (var i = 0, len = data.length; i < len; i++) {
                times = data[i];
                $("#salaryNumber").append("<option value='" + times + "'>" + times + "</option>");
            }
            if (data.length > 0) {
                $("#salaryNumber").val(times);
                wdgz.selMySalaryDetail();
                //wdgz.retrieveSalary();
            } else {
                $("#salaryNumber").append("<option value='0'>全部</option>");
            }
        }
    };


    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiaoyujv_start.init()
     */
    wdgz.init = function(){


        Common.render({
            tmpl: $('#yearListTemp'),
            data: yearList,
            context: '#salaryYear',
            overwrite: 1
        });
        $("#salaryYear").val(currYear);
        $("#salaryMonth").val(currMonth);
        var year = $("#salaryYear").val();
        var month = $("#salaryMonth").val();
        this.getSalaryTimes(year, month);
        $('#seachMySalary').click(function () {
            wdgz.selMySalaryDetail();
        });
        $('#salaryMonth').change(function () {
            var year = $("#salaryYear").val();
            var month = $("#salaryMonth").val();
            wdgz.getSalaryTimes(year, month);
        });
    };

    wdgz.selMySalaryDetail = function() {
        wdgzData.year = $("#salaryYear").val();
        wdgzData.month = $("#salaryMonth").val();
        wdgzData.num = $("#salaryNumber").val();
        Common.getData('/wages/selMySalaryDetail.do', wdgzData, function (rep) {
            $('.gzgl-my-table').html('');
            Common.render({tmpl: $('#gzgl-my-table_temp'), data: rep.message, context: '.gzgl-my-table'});
            $('#date').html($("#salaryYear").val()+"-"+$("#salaryMonth").val());
        });
    }
    wdgz.init();
});