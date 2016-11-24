define(function(require,exports,module){
    history = {};
    require('jquery');
    require('doT');
    Common = require('common');

    history.init = function(){
        getCollectionList();
    }

    $(document).ready(function(){
        $(window).scroll(function () {
            if ($(window).scrollTop() > 100) {
                $(".backtop").fadeIn(1000);
            }
            else {
                $(".backtop").fadeOut(1000);
            }
        });

        $(".backtop").click(function () {
            $('body,html').animate({scrollTop: 0}, 1000);
            return false;
        });

        $('body').on('click','.delete',function(){
            var logId = $(this).parents('li').attr('id');
            var li = $(this).parents('li');
            var url = '/mall/userCenter/deleteLog/' + logId + '.do';
            Common.getData(url,{},function(data){
                if(data && data.code == "200"){
                    li.remove();
                }
            });
        });
    });


    function getCollectionList(){
        var url = '/mall/userCenter/historyList.do';
        Common.getData(url,{},function(data){
            Common.render({
                tmpl:'#goodsListTmpl',
                context:'#goodsListCtx',
                data:data.historyList,
                overwrite:1
            });
        });
    }


    module.exports = history;
});
