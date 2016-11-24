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
define(['common','ajaxfileupload'], function (require, exports, module) {
    var Common = require('common');
    require('ajaxfileupload');
    /**
     *初始化参数
     */
    var salary = {
        saveSalaryItem: function (id, itemName, newText) {
            $.ajax({
                url: "update.do",
                type: "post",
                datatype: "json",
                data: {id: id, itemName: itemName, m: newText},
                success: function (data) {
                    if (data.code == 200) {
                        var sal = data.message;
                        var tds = $("td", $("#" + id));
                        tds.eq(tds.length - 3).text(sal.ssStr);
                        tds.eq(tds.length - 2).text(sal.msStr);
                        tds.eq(tds.length - 1).text(sal.asStr);
                    } else {
                        alert(data.message);
                    }
                }
            });
        },
        retrieveSalary: function () {
            if($("#itemListBox").val()!="ALL"){
                salary.filterDataByQuery();
                return;
            }
            var year = $("#salaryYear").val();
            var month = $("#salaryMonth").val();
            var number = $("#salaryNumber").val();
            $.ajax({
                url: "list.do",
                type: "get",
                datatype: "json",
                data: {year: year, month: month, num: number},
                success: function (data) {
                    if (data.code == 200) {
                        if (data.message.length > 0) {
                            var item = [];
                            var moneys = data.message[0].money;
                            for (var i = 0; i < moneys.length; i++) {
                                item.push({id: "", itemName: moneys[i].itemName});
                            }
                            $("#salary-list-title").text(data.message[0].timesName);
                            salary.buildSalaryItem(item);
                            var salaryData = {};
                            salaryData.salary = data.message;
                            salaryData.item = item;
                            Common.render({
                                tmpl: $('#salaryHeaderTemp'),
                                data: item,
                                context: '#salaryHeaderBox',
                                overwrite: 1,
                                callback:function(){
                                    $('#salaryTableId table').width($('#salaryHeaderBox').find('th').length*120);
                                }
                            });
                            Common.render({
                                tmpl: $('#salaryListTemp'),
                                data: salaryData,
                                context: '#salaryBodyBox',
                                overwrite: 1
                            });
                        }
                    } else {
                        alert(data.message);
                    }
                }
            });
        },
        filterDataByQuery: function () {
            if($("#itemListBox").val()=="ALL"){
                salary.retrieveSalary();
                return;
            }
            var year = $("#salaryYear").val();
            var month = $("#salaryMonth").val();
            var number = $("#salaryNumber").val();
            $.ajax({
                url: "list.do",
                type: "get",
                datatype: "json",
                data: {year: year, month: month, num: number},
                success: function (data) {
                    if (data.code == 200) {
                        if (data.message.length > 0) {
                            var item = [];
                            var moneys = data.message[0].money;
                            var filter = $("#itemListBox").val();
                            for (var i = 0; i < moneys.length; i++) {
                                if (filter != "ALL" && moneys[i].itemName != filter) {
                                    continue;
                                } else {
                                    item.push({id: "", itemName: moneys[i].itemName});
                                }
                            }
                            $("#salary-list-title").text(data.message[0].timesName);
                            var salaryData = {};
                            salaryData.salary = data.message;
                            salaryData.item = item;
                            Common.render({
                                tmpl: $('#salaryFilterHeaderTemp'),
                                data: item,
                                context: '#salaryHeaderBox',
                                overwrite: 1,
                                callback:function(){
                                	$('#salaryTableId table').width('100%').find('th em').width('auto');
                                }
                            });
                            Common.render({
                                tmpl: $('#salaryFilterListTemp'),
                                data: salaryData,
                                context: '#salaryBodyBox',
                                overwrite: 1
                            });
                        }
                    } else {
                        alert(data.message);
                    }
                }
            });
        },
        getSalaryTimes: function (year, month) {
            $.ajax({
                url: "getSalaryTimes.do",
                type: "get",
                datatype: "json",
                data: {year: year, month: month},
                success: function (data) {
                    if (data.code == 200) {
                        salary.buildSalaryNumber(data.message);
                    } else {
                        alert(data.message);
                    }
                }
            });
        },
        buildSalaryItem: function (data) {
            $("#itemListBox").empty().append("<option value='ALL'>全部</option>");
            Common.render({
                tmpl: $('#itemListTemp'),
                data: data,
                context: '#itemListBox',
                overwrite: 0
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
                salary.retrieveSalary();
            } else {
                $("#salaryNumber").append("<option value='ALL'>全部</option>");
            }
        },
        changeSalaryName: function (oldtext, newtext) {
            var year = $("#salaryYear").val();
            var month = $("#salaryMonth").val();
            var number = $("#salaryNumber").val();
            $.ajax({
                url: "renameSalary.do",
                type: "post",
                datatype: "json",
                data: {year: year, month: month, num: number, name: newtext},
                success: function (data) {
                    if (data.code == 200) {
                        $('#salary-list-title').text(newtext);
                    } else {
                        $('#salary-list-title').text(oldtext);
                        alert(data.message);
                    }
                },
                error: function () {
                    $('#salary-list-title').text(oldtext);
                }
            });
        }
    };

    Array.prototype.remove = function (dx) {
        if (isNaN(dx) || dx > this.length) {
            return false;
        }
        this.splice(dx, 1);
    }

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * salary.init()
     */
    salary.init = function () {



        //init the year options data
        Common.render({
            tmpl: $('#yearListTemp'),
            data: yearList,
            context: '#salaryYear',
            overwrite: 1
        });
        $("#salaryYear").val(currYear);
        $("#salaryMonth").val(currMonth);

        $("#itemListBox").empty().append("<option value='ALL'>全部</option>");
        //init the salary table's headers
        //Common.render({tmpl: $('#salaryHeaderTemp'), data: itemList, context: '#salaryHeaderBox', overwrite: 1});

        var year = $("#salaryYear").val();
        var month = $("#salaryMonth").val();

        this.getSalaryTimes(year, month);

        $('#generateTable').click(function () {
            $("#salaryHeaderBox").empty();
            $("#salaryBodyBox").empty();
            var year = $("#salaryYear").val();
            var month = $("#salaryMonth").val();
            $.ajax({
                url: "tabulate.do",
                type: "post",
                datatype: "json",
                data: {year: year, month: month},
                success: function (data) {
                    if (data.code == 200) {
                        salary.buildSalaryNumber(data.message);
                    } else {
                        alert(data.message);
                    }
                }
            });
        });
        $('#deleteSalaryTable').click(function () {
            if (confirm('确定删除本次的工资单数据?')) {
                $("#salaryHeaderBox").empty();
                $("#salaryBodyBox").empty();
                var year = $("#salaryYear").val();
                var month = $("#salaryMonth").val();
                var number = $("#salaryNumber").val();
                $.ajax({
                    url: "deleteSalary.do",
                    type: "post",
                    datatype: "json",
                    data: {year: year, month: month, num: number},
                    success: function (data) {
                        if (data.code == 200) {
                            $("#salaryNumber").children("[value=" + number + "]").remove();
                            if ($("#salaryNumber").children().length == 0) {
                                $("#salaryNumber").append("<option value='ALL'>全部</option>");
                            } else {
                                $("#salaryNumber").val($("#salaryNumber").children(":last").attr("value"));
                                salary.retrieveSalary();
                            }
                        } else {
                            alert(data.message);
                        }
                    }
                });
            }
        });
        $("#salaryYear").add("#salaryMonth").change(function () {
            var year = $("#salaryYear").val();
            var month = $("#salaryMonth").val();
            $("#salaryHeaderBox").empty();
            $("#salaryBodyBox").empty();
            $('#salary-list-title').empty();
            salary.getSalaryTimes(year, month);
        });
        $("#salaryNumber").change(function () {
            $("#salaryHeaderBox").empty();
            $("#salaryBodyBox").empty();
            $('#salary-list-title').empty();
            salary.retrieveSalary();
        });
        $("#itemListBox").change(function () {
            $("#salaryHeaderBox").empty();
            $("#salaryBodyBox").empty();
            $('#salary-list-title').empty();
            salary.filterDataByQuery();
        });
        $("#genSalaryTemplate").click(function () {
            var $form = $("#genTemplateForm");
            $("[name=year]", $form).val($("#salaryYear").val());
            $("[name=month]", $form).val($("#salaryMonth").val());
            $("[name=num]", $form).val($("#salaryNumber").val());
            $form.attr("action", "template.do").submit();
        });
        $("#importSalaryData").click(function () {
            Common.dialog($('#importSalaryData-dialog'), function (obj) {
                $(obj).find('.pop-btn span.active').off().on('click', function () {
                    $.ajaxFileUpload({
                        url: '/salary/import.do', //用于文件上传的服务器端请求地址
                        secureuri: false, //是否需要安全协议，一般设置为false
                        fileElementId: 'importSalaryData-file', //文件上传域的ID
                        dataType: 'json', //返回值类型 一般设置为json
                        param: {},
                        success: function (data, status)  //服务器成功响应处理函数
                        {
                            if (data.code != 200) {
                                alert(data.message);
                            } else {
                                alert("工资导入成功");
                                salary.retrieveSalary();
                                Common.dialog.close($(obj));
                            }
                        },
                        error: function (data, status, e)//服务器响应失败处理函数
                        {
                            alert("工资导入失败");
                        }
                    });
                })
            });
        });
        $('body').on('click', '.editableTd', function () {
            var td = $(this);
            var text = td.text();
            var txt = $("<input type='text' maxlength='10' style='border:none;'>").val(text);
            txt.on("blur", function () {
                var newText = $(this).val();
                var reg = /^(\d[\.?\d]*)$/;
                var itemName = $(this).parent('.editableTd').attr('id');
                var id = $(this).parent('.editableTd').parent('tr').attr('id');
                $(this).remove();
                if (newText.match(reg)) {
                    td.text(newText);
                    salary.saveSalaryItem(id, itemName, newText);
                } else {
                    td.text(text);
                }
            });
            td.text("");
            td.append(txt);
            txt.focus();
        });


        $('#salary-list-title').dblclick(function () {
            var $this = $(this);
            var text = $this.text();
            var txt = $("<input type='text' maxlength='30' style='width:70%;border:none;text-align: center;'>").val(text);
            txt.on("blur", function () {
                var newText = $.trim($(this).val());
                if (newText == "") {
                    $(this).remove();
                    $this.text(text);
                } else {
                    salary.changeSalaryName(text, newText);
                }
            });
            $this.empty().append(txt);
            txt.focus();
        });
    };


    (function () {
        salary.init();
    })()

    module.exports = salary;
})
