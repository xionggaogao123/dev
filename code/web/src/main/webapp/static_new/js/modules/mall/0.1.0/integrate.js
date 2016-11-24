/**
 * Created by admin on 2016/5/24.
 */
define(function(require,exports,module) {
    var Common = require('common');
    require('jquery');
    var sortType = 2;
    var page = 1;

    (function(){
        goodsList(page);//商品列表

    })();



    function goodsList(page){
        var state = $('body').attr('state');
        if(state == ''){
            state = 0;
        }

        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 99;
        requestData.state = state;
        requestData.activity= 0;
        Common.getData("/mall/integrateGoods.do", requestData, function(resp){
            var goods = resp.list;

            if(goods.length == 0){
                $('#notFound').show();
            }else{
                $('#notFound').hide();
            }
            Common.render({
                tmpl: '#goodsListTmpl',
                data: goods,
                context:'#goodsList',
                overwrite: 1
            });

            for(var i in goods){
                setPrice(goods[i]);
            }

        })

    }


    function setPrice(goods){
        var basePrice = goods.price;
        var minPrice = basePrice;
        var maxPrice = basePrice;
        var kinds = goods.kindDTOList;
        for(var i in kinds){
            var minSpecPrice = kinds[i].specList[0].price;
            var maxSpecPrice = 0;

            var specs = kinds[i].specList;
            for(var j in specs){
                var specPrice = specs[j].price;
                if(specPrice > maxSpecPrice){
                    maxSpecPrice = specPrice;
                }
                if(specPrice < minSpecPrice){
                    minSpecPrice = specPrice;
                }
            }


            minPrice += minSpecPrice;
            maxPrice += maxSpecPrice;

        }

        if(minPrice == maxPrice){
            $('#' + goods.goodsId + ' em').text(''+ minPrice/100);
        } else {
            $('#' + goods.goodsId + ' em').text(''+ minPrice/100 + ' - ' + maxPrice/100);
        }
    }
});