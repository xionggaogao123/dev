/**
 * Created by admin on 2017/11/20.
 */

define(['jquery', 'common', 'doT', 'cal', 'easing','ajaxfileupload'], function (require, exports, module) {
    var Common = require('common');
    require('ajaxfileupload');
    var appMarketIndex={};

    $(document).ready(function () {
        $('body').on('change', '#uploadImportTemplate', function () {
            uploadImportTemplate();
        });
        function uploadImportTemplate() {
            $.ajaxFileUpload({
                url: '/web/backstage/importApkFile.do', //用于文件上传的服务器端请求地址
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'uploadImportTemplate', //文件上传域的ID
                dataType: 'json', //返回值类型 一般设置为json
                param: {},
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if (data.code == "200") {
                        alert("导入成功");
                    } else {
                        alert("导入失败");
                    }

                },
                error: function (data, status, e)//服务器响应失败处理函数
                {

                }
            });
        }
    })
    module.exports = appMarketIndex;
})
