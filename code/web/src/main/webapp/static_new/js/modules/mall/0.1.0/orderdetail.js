define(function(require,exports,module){

    require('jquery');
    require('doT');
    Common = require('common');
    var orderdetail = {};

    orderdetail.init = function(){
        var orderId = $('body').attr('orderId');
        var url="/mall/order/orderDetailInfo.do";
        Common.getData(url,{orderId:orderId},function(rep){
            if(rep){
                $('#expressPrice').text("￥"+(rep.expressPrice / 100.0).toFixed(2));
                $('#voff').text("￥"+(rep.voff / 100.0).toFixed(2));
                $('#exp').text("￥"+(rep.exp / 10.0).toFixed(2));
                $('#totalPrice').text("￥"+(rep.totalPrice / 100.0).toFixed(2));
                $('#name').text(rep.name);
                $('#address').text(rep.address);
                $('#tel').text(rep.tel);

                Common.render({tmpl: $('#goods_script'), data: rep.goodsList, context: '#goodsList'});
            }
        });
    }

    module.exports=orderdetail;
});
