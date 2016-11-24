define(function(require,exports,module){
    collection = {};
    require('jquery');
    require('doT');
    Common = require('common');

    collection.init = function(){
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
            var id = $(this).parents('li').attr('id');
            var url = '/mall/userCenter/deleteLog/' + id + '.do';
            var li = $(this).parents('li');
            Common.getData(url,{},function(data){
                if(data && data.code == "200"){
                    li.remove();
                }
            });
        });
    });


    function getCollectionList(){
        var url = '/mall/userCenter/collectionList.do';
        Common.getPostData(url,{},function(data){
            Common.render({
                tmpl:'#goodsListTmpl',
                context:'#goodsListCtx',
                data:data.collectionList,
                overwrite:1
            });
        });
    }

    module.exports = collection;
});
