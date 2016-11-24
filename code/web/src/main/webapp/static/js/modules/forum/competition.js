/**
 * Created by admin on 2016/7/20.
 */
define(function (require, exports, module) {
    var Common = require('common');
    var competition = {};
    require('jquery');
    $(document).ready(function () {

        getFPost("/forum/fPostActivityAll.do", '#talentTml');

        $("#onActivity").click(function () {
            getFPost("/forum/fPostsActivity.do", '#talentTml');
        });

        $("#offActivity").click(function () {
            getFPost("/forum/fPostsOffActivity.do", '#OfftalentTml');
        });

        $("#goCompetition").click(function () {
            location.href = '/forum/competitionIndex';
        });

    });

    function getFPost(url, template) {
        var requestData = {};
        requestData.sortType = 2;
        requestData.page = 1;
        requestData.classify = -1;
        requestData.cream = -1;
        requestData.gtTime = 0;
        requestData.postSection = "";
        requestData.pageSize = 25;
        requestData.inSet = 1;
        Common.getData(url, requestData, function (resp) {
            var total = resp.list;
            Common.render({
                tmpl: template,
                data: total,
                context: '#talentList',
                overwrite: true
            });
        })
    }

    module.exports = competition;
});
