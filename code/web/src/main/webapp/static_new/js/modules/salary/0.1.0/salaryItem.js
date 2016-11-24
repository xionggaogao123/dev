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
    /**
     *初始化参数
     */
    var salaryItem = {},
        Common = require('common');

    Array.prototype.remove = function (dx) {
        if (isNaN(dx) || dx > this.length) {
            return false;
        }
        this.splice(dx, 1);
    }


    /**
     * @func dialog
     * @desc 模拟弹窗
     * @param {jQuery obj} jQuery
     * @example
     * salaryItem.dialog(jQuery)
     */

    salaryItem.dialog = function (obj) {
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        obj.find('.pop-btn span').click(function () {
            $(this).closest('.pop-wrap').hide();
            $('.bg-dialog').hide();
        });
        $('body').scrollTop(0);
    };

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * salaryItem.init()
     */
    salaryItem.init = function () {



        // open the add salary item dialog
        $('.green-btn').click(function () {
            salaryItem.dialog($('#addSalaryItem'));
            return false;
        });

        $('#saveItem').click(function () {
            if ($("#itemName").val() == null || $("#itemName").val() == "") {
                $("#itemName").focus();
                return false;
            }
            var itemName = $("#itemName").val();
            var itemType = $("#itemType").val();
            $.ajax({
                url: "itemAdd.do",
                type: "post",
                datatype: "json",
                data: {itemName: itemName, type: itemType},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        window.location.href = window.location.href;
                        return true;
                    } else {
                        alert(data.message);
                    }
                }
            });
            return true;
        });

        $('#updateItem').click(function () {
            if ($("#editItemName").val() == null || $("#editItemName").val() == "") {
                $("#editItemName").focus();
                return false;
            }
            var itemId = $("#editItemId").val();
            var itemName = $("#editItemName").val();
            var itemType = $("#editItemType").val();
            $.ajax({
                url: "itemUpdate.do",
                type: "post",
                datatype: "json",
                data: {id: itemId, itemName: itemName, type: itemType},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        window.location.href = window.location.href;
                        return true;
                    } else {
                        alert(data.message);
                    }


                }
            });
            return true;
        });

        $('body').on('click', '.jc-edit', function () {
            for (var i = 0; i < salaryItem.data.length; i++) {
                if (salaryItem.data[i].id === $(this).attr('data-name')) {
                    $("#editItemId").val(salaryItem.data[i].id);
                    $("#editItemName").val(salaryItem.data[i].itemName);
                    $("#editItemType").val(salaryItem.data[i].type);
                    break;
                }
            }
            salaryItem.dialog($('#editSalaryItem'));
            return false;
        });

        $('body').on('click', '.jc-del', function () {
            if (confirm('确定删除?')) {
                var itemId = $(this).attr('data-name');
                $.ajax({
                    url: "itemDelete.do",
                    type: "post",
                    datatype: "json",
                    data: {id: itemId},
                    success: function (data) {
                        if (!data) {
                            return false;
                        }
                        if (data.code == "200") {
                            window.location.href = window.location.href;
                            return true;
                        } else {
                            alert(data.message);
                        }
                    }
                });
            }
            return false;
        });
    };

    salaryItem.loadData = function (data) {
        salaryItem.data = data;
        Common.render({tmpl: $('#itemListTemp'), data: data, context: '.salaryItem-body', overwrite: 1})
    };

    (function () {
        salaryItem.init();
    })()
    module.exports = salaryItem;
})
