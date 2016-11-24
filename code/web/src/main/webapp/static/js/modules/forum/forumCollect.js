/*
 * @Author: Voyage
 * @Date:   2016-05-30 11:28:27
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-06-02 14:07:03
 */


define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    $(document).ready(function () {
        $('.ul-forumset li').click(function () {
            $(this).addClass('li-cur').siblings('.ul-forumset li').removeClass('li-cur');
        });
        $('.ul-forumset li:nth-child(1)').click(function () {
            $('.right-zl').show();
            $('.right-aq').hide();
        });
        $('.ul-forumset li:nth-child(2)').click(function () {
            $('.right-zl').hide();
            $('.right-aq').show();
        })

        getPostCollection();

        getSectionCollection();

        $('.delete').click(function () {
            var value = $(this).attr('deleteId');
            var requestPost = {};
            requestPost.collectionId = value;
            Common.getData("/forum/userCenter/removeCollection.do", requestPost, function (resp) {
                if (resp.code == 200) {
                    location.href = "/forum/userCenter/collection.do?&ti=" + new Date().getTime();
                } else {
                    alert(resp.message);
                }
            })

        });
    })

    function getPostCollection() {
        var requestData = {};
        requestData.type = 0;
        Common.getData("/forum/userCenter/getCollections.do", requestData, function (resp) {
            var result = resp.list;
            Common.render({
                tmpl: '#postTml',
                data: result,
                context: '#post',
                overwrite: 1
            });

        })
    }

    function getSectionCollection() {
        var requestData = {};
        requestData.type = 1;
        Common.getData("/forum/userCenter/getCollections.do", requestData, function (resp) {
            var result = resp.list;
            Common.render({
                tmpl: '#sectionTml',
                data: result,
                context: '#section',
                overwrite: 1
            });

        })
    }

})