/**
 * Created by liwei on 15/7/12.
 */
/**
 * @author 李伟
 * @module 工资项目操作
 * @description
 * 工资项目操作模块
 */
/* global Config */
define(['common'], function (require, exports, module) {
    var Common = require('common');

    Array.prototype.remove = function (dx) {
        if (isNaN(dx) || dx > this.length) {
            return false;
        }
        this.splice(dx, 1);
    }
    /**
     *初始化参数
     */
    var viewSalary = {
        loadData: function () {
            delete viewSalary.salaryData;
            var beginYear = $("#gognziview-beginyear").val();
            var beginMonth = $("#gognziview-beginmonth").val();
            var endYear = $("#gognziview-endyear").val();
            var endMonth = $("#gognziview-endmonth").val();
            var begin = parseInt(beginYear + (beginMonth < 10 ? "0" + beginMonth : "" + beginMonth));
            var end = parseInt(endYear + (endMonth < 10 ? "0" + endMonth : "" + endMonth));
            begin = begin < end ? begin : end;
            end = begin < end ? end : begin;
            $.ajax({
                url: "loadSalaryDetail.do",
                type: "post",
                datatype: "json",
                data: {begin: begin, end: end},
                success: function (data) {
                    if (data.code == 200) {
                        var list = {};
                        for (var i = 0, len = data.message.length; i < len; i++) {
                            list[data.message[i].id] = data.message[i];
                        }
                        viewSalary.salaryData = list;
                        Common.render({
                            tmpl: $('#salaryItemListTemp'),
                            data: data.message,
                            context: '#gongziview-datalist',
                            overwrite: 1
                        });
                    } else {
                        alert(data.message);
                    }
                }
            });
        }
    };
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * viewSalary.init()
     */
    viewSalary.init = function () {



        $("#gognziview-beginyear")
            .add($("#gognziview-beginmonth"))
            .add($("#gognziview-endyear"))
            .add($("#gognziview-endmonth")).change(function () {
                viewSalary.loadData();
            });

        $('body').on('click', '.viewSalary', function () {
            var parent = $(this).closest("tr");
            $("#viewSalaryDetailTitle").text($("td:eq(1)", parent).text());
            Common.render({
                tmpl: $('#itemListTemp'),
                data: viewSalary.salaryData[parent.attr("id")].money,
                context: '#view-salary-detail',
                overwrite: 1
            });
            Common.dialog($('#viewSalaryDetail'));
        });
        viewSalary.loadData();
    };

    module.exports = viewSalary;
})
