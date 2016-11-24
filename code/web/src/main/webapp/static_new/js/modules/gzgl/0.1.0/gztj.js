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
    var gztjData = {};
    var gztj = {
        saveSalaryItem: function (id, itemName, newText) {
            $.ajax({
                url: "/wages/update.do",
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
            gztj.wagelist();
        },
        filterDataByQuery: function () {
            gztj.wagelist();
        },
        getSalaryTimes: function (year, month) {
            $.ajax({
                url: "/wages/getSalaryTimes.do",
                type: "get",
                datatype: "json",
                data: {year: year, month: month},
                success: function (data) {
                    if (data.code == 200) {
                        gztj.buildSalaryNumber(data.message);
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
                gztj.retrieveSalary();
            } else {
                $("#salaryNumber").append("<option value='0'>全部</option>");
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
     * jiaoyujv_start.init()
     */
    gztj.init = function(){


        //设置初始页码
        gztjData.page = 1;
        //设置每页数据长度
        gztjData.pageSize = 12;
        Common.render({
            tmpl: $('#yearListTemp'),
            data: yearList,
            context: '#salaryYear',
            overwrite: 1
        });
        $("#genSalaryTemplate").click(function () {
            var $form = $("#genTemplateForm");
            $("[name=year]", $form).val($("#salaryYear").val());
            $("[name=month]", $form).val($("#salaryMonth").val());
            $("[name=num]", $form).val($("#salaryNumber").val());
            $form.attr("action", "/wages/template.do").submit();
        });
        $('#salaryMonth').change(function () {
            var year = $("#salaryYear").val();
            var month = $("#salaryMonth").val();
            gztj.getSalaryTimes(year, month);
        });
        $('#seachSalary').click(function () {
            $("#salaryHeaderBox").empty();
            $("#salaryBodyBox").empty();
            $("#salaryUserList").empty();
            gztj.retrieveSalary();
        });
        $('.gzgl-tj-dc').click(function () {
            var $form = $("#genTemplateForm");
            $("[name=year]", $form).val($("#salaryYear").val());
            $("[name=month]", $form).val($("#salaryMonth").val());
            $("[name=num]", $form).val($("#salaryNumber").val());
            $("[name=userId]", $form).val($("#gzgl-tj-large").val());
            $form.attr("action", "/wages/exprotSalaryList.do").submit();
        });
        $('.gzgl-tj-gzt').click(function () {
            var $form = $("#genTemplateForm");
            $("[name=year]", $form).val($("#salaryYear").val());
            $("[name=month]", $form).val($("#salaryMonth").val());
            $("[name=num]", $form).val($("#salaryNumber").val());
            $("[name=userId]", $form).val($(".gzgl-tj-large").val());
            $form.attr("action", "/wages/exprotSalaryDetail.do").submit();
        });

        $('.hand-dr').click(function () {
            $("#salaryHeaderBox").empty();
            $("#salaryBodyBox").empty();
            $("#salaryUserList").empty();
            var year = $("#salaryYear").val();
            var month = $("#salaryMonth").val();
            $.ajax({
                url: "/wages/tabulate.do",
                type: "post",
                datatype: "json",
                data: {year: year, month: month},
                success: function (data) {
                    if (data.code == 200) {
                        gztj.buildSalaryNumber(data.message);
                        $(".gzgl-meng").hide();
                        $(".gzgl-gly-alert").hide();
                    } else {
                        alert(data.message);
                    }
                }
            });
        });
        //$('body').on('click', '.editableTd', function () {
        //    var td = $(this);
        //    var text = td.text();
        //    var txt = $("<input type='text' maxlength='10' style='border:none;width:60px;background: #5db85c;'>").val(text);
        //    txt.on("blur", function () {
        //        var newText = $(this).val();
        //        var reg = /^(\d[\.?\d]*)$/;
        //        var itemName = $(this).parent('.editableTd').attr('id');
        //        var id = $(this).parent('.editableTd').parent('tr').attr('id');
        //        $(this).remove();
        //        if (newText.match(reg)) {
        //            td.text(newText);
        //            gztj.saveSalaryItem(id, itemName, newText);
        //        } else {
        //            td.text(text);
        //        }
        //    });
        //    td.text("");
        //    td.append(txt);
        //    txt.focus();
        //});
        $("#salaryYear").val(currYear);
        $("#salaryMonth").val(currMonth);
        var year = $("#salaryYear").val();
        var month = $("#salaryMonth").val();
        this.getSalaryTimes(year, month);

        $('#deleteSalaryTable').click(function () {
            if (confirm('确定删除本次的工资单数据?')) {
                $('#theadTotal').hide();
                $("#salaryHeaderBox").empty();
                $("#salaryBodyBox").empty();
                $("#salaryUserList").empty();
                var year = $("#salaryYear").val();
                var month = $("#salaryMonth").val();
                var number = $("#salaryNumber").val();
                $.ajax({
                    url: "/wages/deleteSalary.do",
                    type: "post",
                    datatype: "json",
                    data: {year: year, month: month, num: number},
                    success: function (data) {
                        if (data.code == 200) {
                            $("#salaryNumber").children("[value=" + number + "]").remove();
                            if ($("#salaryNumber").children().length == 0) {
                                $("#salaryNumber").append("<option value='0'>全部</option>");
                            } else {
                                $("#salaryNumber").val($("#salaryNumber").children(":last").attr("value"));
                                gztj.retrieveSalary();
                            }
                        } else {
                            alert(data.message);
                        }
                    }
                });
            }
        });

        $("#gzglxm").click(function(){
            $(".tab2").show();
            $(".tab1").hide();
            gztj.itemList();
        });
        $(".gzxmgl-sure").click(function(){
            if ($("#itemName").val() == null || $("#itemName").val() == "") {
                $("#itemName").focus();
                alert("请输入项目名称！");
            }
            gztjData.itemName = $("#itemName").val();
            gztjData.type = $("input[name='gzgl-type']:checked").val();
            if ($("#type").val()==1) {
                gztj.itemAdd();
            } else if($("#type").val()==2) {
                gztjData.id=$('#salaryId').val();
                gztj.itemUpdate();
            }

        });

        $("#back").click(function(){
            $(".tab2").hide();
            $(".tab1").show();
        });

        $("#addprojcet").click(function(){
            $('#itemName').val('');
            $('#type').val(1);
            var selects = document.getElementsByName("gzgl-type");
            for (var i=0; i<selects.length; i++){
                if (selects[i].value=="发款") {
                    selects[i].checked= true;
                    break;
                }
            }
            $(".gzgl-meng").show();
            $(".gzxmgl-alert").show();
        });
        $(".gzgl-zhibiao").click(function(){
            $(".gzgl-meng").show();
            $(".gzgl-gly-alert").show();
        });
        $(".gzxmgl-qx,.gzgl-close").click(function(){
            $(".gzgl-meng").hide();
            $(".gzgl-gly-alert").hide();
        });
        $(".gzxmgl-qx,.gzxmgl-close").click(function(){
            $(".gzgl-meng").hide();
            $(".gzxmgl-alert").hide();
        });

    };
    gztj.itemList = function() {
        Common.getData('/wages/itemList.do',gztjData,function(rep){
            $('.gzxmgl-table').html('');
            Common.render({tmpl: $('#gzxmgl_templ'), data: rep, context: '.gzxmgl-table'});
            $(".gzxmgl-edit").click(function(){
                $('#type').val(2);
                $('#itemName').val($(this).parent().parent().children().eq(1).text());
                $('#salaryId').val($(this).attr('vid'));
                var selects = document.getElementsByName("gzgl-type");
                for (var i=0; i<selects.length; i++){
                    if (selects[i].value==$(this).parent().parent().children().eq(2).text()) {
                        selects[i].checked= true;
                        break;
                    }
                }
                $(".gzgl-meng").show();
                $(".gzxmgl-alert").show();
            });
            $('.gzxmgl-del').bind('click', function () {
                if (confirm('确认删除此工资项目！')) {
                    gztjData.id=$(this).attr('vid');
                    gztj.itemDelete();
                }
            });
        });
    }
    gztj.itemAdd = function() {
        Common.getPostData('/wages/itemAdd.do',gztjData,function(rep){
            if (!rep) {
                alert("添加失败！");
            }
            if (rep.code == "200") {
                gztj.itemList();
                $(".gzgl-meng").hide();
                $(".gzxmgl-alert").hide();
            } else {
                alert(rep.message);
            }
        });
    }
    gztj.itemDelete = function() {
        Common.getPostData('/wages/itemDelete.do',gztjData,function(rep){
            if (!rep) {
                alert("删除失败！");
            }
            if (rep.code == "200") {
                gztj.itemList();
            } else {
                alert(rep.message);
            }
        });
    }

    gztj.itemUpdate = function() {
        Common.getPostData('/wages/itemUpdate.do',gztjData,function(rep){
            if (!rep) {
                alert("修改失败！");
            }
            if (rep.code == "200") {
                gztj.itemList();
                $(".gzgl-meng").hide();
                $(".gzxmgl-alert").hide();
            } else {
                alert(rep.message);
            }
        });
    }
// 分页初始化
    gztj.initPaginator=function (option) {
        var totalPage = '';
        $('.page-paginator').show();
        $('.page-index').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        gztj.buildPaginator(totalPage, option.currentpage);
        option.operate(totalPage);
    }

    gztj.buildPaginator =function (totalPage, currentPage) {
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
    gztj.wagelist = function() {
        var year = $("#salaryYear").val();
        var month = $("#salaryMonth").val();
        var number = $("#salaryNumber").val();
        var userId = '';
        $.ajax({
            url: "/wages/list.do",
            type: "post",
            datatype: "json",
            data: {year: year, month: month, num: number,userId:userId,name:$('#username').val(),page:gztjData.page,pageSize:gztjData.pageSize},
            success: function (data) {
                $('#createTime').html("");
                $('#theadTotal').hide();
                if (data.salarylist.length > 0) {
                    var item = [];
                    var moneys = data.salarylist[0].money;
                    var time = data.salarylist[0].createTime;
                    $('#createTime').html(time);
                    for (var i = 0; i < moneys.length; i++) {
                        //if (filter != "0" && moneys[i].itemName != filter) {
                        //	continue;
                        //} else {
                        item.push({id: "", itemName: moneys[i].itemName});
                        //}
                    }
                    var salaryData = {};
                    salaryData.salary = data.salarylist;
                    salaryData.item = item;
                    Common.render({
                        tmpl: $('#salaryHeaderTemp'),
                        data: data.salarylist[0].salaryItem,
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
                    if (salaryData!=null&&salaryData.salary!=null&&salaryData.salary.length!=0) {
                        $('#theadTotal').show();
                    } else {
                        $('#theadTotal').hide();
                    }
                    Common.render({
                        tmpl: $('#salaryUserListTemp'),
                        data: salaryData,
                        context: '#salaryUserList',
                        overwrite: 1
                    });
                }
                var option = {
                    total: data.total,
                    pagesize: data.pageSize,
                    currentpage: data.page,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).click(function () {
                                gztjData.page = $(this).text();
                                gztj.wagelist();
                            })
                        });
                        $('.first-page').click(function () {
                            gztjData.page = 1;
                            gztj.wagelist();
                        });
                        $('.last-page').click(function () {
                            gztjData.page = totalPage;
                            gztj.wagelist();
                        })
                    }
                }
                gztj.initPaginator(option);
            }
        });
    }

    gztj.init();
});