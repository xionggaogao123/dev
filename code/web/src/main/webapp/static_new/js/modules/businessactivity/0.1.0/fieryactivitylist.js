/*
 * @Author: Tony
 * @Date:   2015-07-02 11:55:29
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-07-02 11:56:15
 */
define('fieryactivitylist',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    require('jquery');
    require('doT');
    require('easing');
    var fieryactivity = {},
        Common = require('common');

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * moralculturemanage.init()
     */
    fieryactivity.init = function(){


        fieryactivity.initMoralCultureManageData();
        //对编辑模板初始设置
        fieryactivity.initEditModal();
        //保存项目
        fieryactivity.saveProject();
    };
    module.exports=fieryactivity;
});