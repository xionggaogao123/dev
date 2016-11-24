/**
 * Created by admin on 2016/8/25.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    var optimizationPlate = {};
    optimizationPlate.init = function () {


    }
    $(document).ready(function () {

        $('body').on('click', '#get', function () {
            getValidate();
        });
        $('body').on('click', '#submit', function () {
            var re = {};
            re.pId = $('#sectionId').val();
            re.memo = $('#memo').val();
            Common.getPostData('/forum/addFSections.do', re, function (resp) {
                if (resp.code == 200) {
                } else {
                    alert(resp.message);
                }
            });

        });

    })

    function getValidate() {
        var requestData = {};
        requestData.pId = $('#sectionId').val();
        Common.getData('/forum/sectionId.do', requestData, function (resp) {
            $('#memo').val(resp.memo);
        });
    }

    module.exports = optimizationPlate;
});

