/**
 * Created by admin on 2016/6/3.
 */
define(function (require, exports, module) {
    require('jquery');
    var newPost = {};
    var Common = require('common');

    //var ue = UE.getEditor('editor');

    $(document).ready(function () {
        var pSectionId = $('#pSectionId').val();
        getData(pSectionId);

        $("#edui1_bottombar").hide();
    })

    function getData(pSectionId) {

        var requestData = {};
        requestData.id = pSectionId;
        Common.getData("/forum/fSectionDetail.do", requestData, function (resp) {

            Common.render({
                tmpl: '#pTml',
                data: resp,
                context: '#pSeDetail',
                overwrite: 1
            });

        })
    }

    module.exports = newPost;
});