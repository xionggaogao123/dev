/* 
 * @Author: Tony
 * @Date:   2015-07-02 11:55:29
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-02 11:56:15
 */
define('moralculturemanage', ['jquery', 'doT', 'easing', 'common'], function (require, exports, module) {
    /**
     *初始化参数
     */
    require('jquery');
    require('doT');
    require('easing');
    var moralculturemanage = {},
        Common = require('common');

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * moralculturemanage.init()
     */
    moralculturemanage.init = function () {
        Common.cal('calId');
        Common.leftNavSel();
        moralculturemanage.initMoralCultureManageData();
        //对编辑模板初始设置
        moralculturemanage.initEditModal();
        //保存项目
        moralculturemanage.saveProject();
    };
    /**
     * 对编辑模板初始设置
     */
    moralculturemanage.initEditModal = function () {
        /*弹出层*/
        var L = 0;
        var T = 0;
        L = $(window).width() / 2 - $(".xjxm").width() / 2;
        T = 50;
        $(".xjxm").css({
            'left': L,
            'top': T
        });
        $(".gay").height($(document).height());
        //打开模态框
        $("#addProject").click(function () {
            /* Act on the event */
            $(".gay,.xjxm").fadeIn();
            /*弹出层*/
        });
        //关闭模态框
        $(".xjxm .gb").click(function (event) {
            /* Act on the event */
            $("#projectId").val("");
            $("#moralCultureName").val("");
            $(".gay,.xjxm").fadeOut();
        });
    }
    /**
     * 保存德育项目
     */
    moralculturemanage.saveProject = function () {
        $("#saveProject").click(function () {
            //保存参数
            var saveData = {};
            saveData.id = $("#projectId").val();
            if ($("#moralCultureName").val().trim() == "") {
                alert("项目名称不能为空！");
                return;
            }
            saveData.moralCultureName = $("#moralCultureName").val();
            var url = "";
            if (saveData.id != "" && saveData.id != null) {
                url = "/moralCultureManage/updMoralCultureProject.do";
            } else {
                url = "/moralCultureManage/addMoralCultureProject.do";
            }
            Common.getData(url, saveData, function (rep) {
                $("#projectId").val("");
                $("#moralCultureName").val("");
                $(".gay,.xjxm").fadeOut();
                //moralculturemanage.initMoralCultureManageData();
                location.reload();
            });
        });
    }

    /**
     * 查询德育项目信息数据
     */
    moralculturemanage.initMoralCultureManageData = function () {
        //查询参数
        var searchData = {};
        Common.getData('/moralCultureManage/selMoralCultureProjectList.do', searchData, function (rep) {
            $('.sub-info-list').html("");
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.sub-info-list'});
        });
        /*
         * 绑定处理按钮
         * */
        $('.edit').bind("click", function (event) {
            var id = $(this).parent().attr('id');
            $("#projectId").val(id);
            var moralCultureName = $(this).parent().find('h6').text();
            $("#moralCultureName").val(moralCultureName);
            $("#winTitle").text("编辑项目");
            $(".gay,.xjxm").fadeIn();
        });
        /*
         * 绑定删除按钮
         * */
        $('.del').bind("click", function (event) {
            var id = $(this).parent().attr('id');
            moralculturemanage.deleteProject(id);
        });
    }

    /*
     * 删除一条德育项目信息
     * */
    moralculturemanage.deleteProject = function (id) {
        if (confirm('确认删除此条德育项目信息！')) {
            //删除参数
            var delData = {};
            delData.id = id;
            Common.getData('/moralCultureManage/delMoralCultureProject.do', delData, function (rep) {
                    //查询德育项目信息数据
                    //moralculturemanage.initMoralCultureManageData();
                    location.reload();
                }
            );
        }
    }
    //moralculturemanage.init();
    module.exports = moralculturemanage;
});