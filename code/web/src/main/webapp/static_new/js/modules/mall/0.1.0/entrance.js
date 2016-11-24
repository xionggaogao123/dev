/**
 * Created by admin on 2016/6/2.
 */
define(function(require,exports,module) {
    var Common = require('common');
    require('jquery');
    var sortType = 2;

    (function() {
        getSection();
    })();

    function getSection(){
            $('.fSection').each(function(){
                var sid=$(this).attr('value');
                var that=this;
                Common.getData("/forum/fSection.do", {level:1,id:sid}, function(resp) {
                    Common.render({
                        tmpl: '#ContentTml',
                        data: resp,
                        context:that,
                        overwrite: 1
                    });

                    $('.p3').each(function(){
                        var id=$(this).attr('value');
                        var that=this;
                        Common.getData("/forum/fSectionCount.do", {id:id}, function(resp) {
                            Common.render({
                                tmpl: '#ScTml',
                                data: resp,
                                context:that,
                                overwrite: 1
                            });
                        });

                    });
                });

            });
    }
});