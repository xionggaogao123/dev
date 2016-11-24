/**
 * Created by admin on 2016/7/11.
 */
define(function (require, exports, module) {
    var level = {};
    require('jquery');
    require('pagination');
    Common = require('common');

    level.init = function () {
        Common.getPostData('/forum/userCenter/getFLevel.do', {}, function (data) {
            Common.render({
                tmpl: $('#FLevelTml'),
                data: data,
                context: '#FLevel'
            });
        });
    }
    $(document).ready(function () {
        $('body').on('click', '.add-new', function () {
            $('#level').show();
        });

        $('body').on('click', '.editSpan', function () {
            var id = $(this).attr('value');
            var param = {};
            param.id = id;
            Common.getData('/forum/userCenter/getFLevelById.do', param, function (resp) {
                $('#level').show();
                $('#levelId').val(resp.id);
                $('#levelStr').val(resp.level);
                $('#startLevel').val(resp.startLevel);
                $('#endLevel').val(resp.endLevel);
                $('#stars').val(resp.stars);
            })
        });

        $('body').on('click', '.deleteSpan', function () {
            var id = $(this).attr('value');
            var param = {};
            param.id = id;
            Common.getPostData('/forum/userCenter/removeFLevelById.do', param, function (resp) {
                if (resp.code == "200") {
                    alert("删除成功");
                    location.href = '/mall/admin/level.do';
                } else {
                    alert(resp.message);
                }
            })
        });

        $('body').on('click', '#cancel', function () {
            $('#level').hide();
        });

        $('body').on('click', '#submit', function () {
            var param = {};
            param.id = $('#levelId').val();
            param.level = $('#levelStr').val();
            param.startLevel = $('#startLevel').val();
            param.endLevel = $('#endLevel').val();
            param.stars = $('#stars').val();
            Common.getPostData('/forum/userCenter/addFLevel.do', param, function (resp) {
                if (resp.code == "200") {
                    alert("添加成功");
                    location.href = '/mall/admin/level.do';
                } else {
                    alert(resp.message);
                }

            })
        });
    });
    module.exports = level;
});