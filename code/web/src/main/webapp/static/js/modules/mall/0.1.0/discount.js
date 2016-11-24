/**
 * Created by admin on 2016/6/12.
 */
/**
 * Created by admin on 2016/5/24.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');

    (function () {
        getCategories();

    })();

    function getCategories() {
        Common.getData("/mall/categories.do", {}, function (resp) {

            Common.render({
                tmpl: '#listAcTml',
                data: resp.goodsCategories,
                context: '#listAc',
                overwrite: 1
            });

            $('.listData').each(function () {
                var parentId = $(this).attr('value');
                var that = this;
                Common.getData("/mall/categories.do", {level: 2, parentId: parentId}, function (resp) {
                    Common.render({
                        tmpl: '#listTml',
                        data: resp.goodsCategories,
                        context: that,
                        overwrite: 1
                    });
                });
            });

        })
    }

});