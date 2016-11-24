/**
 * Created by admin on 2016/5/25.
 */

define(function(require,exports,module){
    var Common = require('common');
    require('jquery');
    var sortType = 2;
    var login = $('body').attr('login')=='true';

    var page=1;

    (function(){
        getCategories();
       //商品列表
        $('.nav-list .nv:first').addClass('cur-white');

        $('#category .cat:first').addClass('newcur')
        var initValue='56eb6a1d0cf234ce7e479c24';
        goodsList(page,'',6,0,-1);

        goodsList(page,'',6,-1,0);

        goodsList(page,initValue,8,-1,-1);

        $('.nav-list .nv:first').children('.list3').addClass('show');

        $('.nav-list .nv').click(function(){
            $('.nav-list .nv:first').removeClass('cur-white');
            var goodsCategory=$(this).attr('value');
            var activity=-1;
            if(goodsCategory=="ALL"){
                activity=0;
            }
            goodsList(page,goodsCategory,6,activity,-1);
            $(that).addClass('cur-white').siblings('.nav-list .nv').removeClass('cur-white');
            $('.list3').removeClass('show');
            $(that).children('.list3').addClass('show');
        });

        $('#category .cat').click(function(){
            var goodsCategory=$(this).attr('value');
            var activity=-1;
            if(goodsCategory=="ALL"){
                activity=0;
            }
            goodsList(page,goodsCategory,8,activity,-1);
            $(this).addClass('newcur').siblings('#category .cat').removeClass('newcur');
        });


        $('.ul-new-nav li').click(function(){
            $(this).addClass('newcur').siblings('.ul-new-nav li').removeClass('newcur');
        })

        var str="";

        function GetTime(){
            str="";
            str=str+"本场倒计时&nbsp;";
            var EndTime=new Date('2016/06/02 00:00:00');
            var NowTime=new Date();
            var t=EndTime.getTime()-NowTime.getTime();

            var day=0;
            var hour=0;
            var minute=0;
            var second=0;

            if(t>0){
                day=Math.floor(t/1000/60/60/24);
                hour=Math.floor(t/1000/60/60%24);
                minute=Math.floor(t/1000/60%60);
                second=Math.floor(t/1000%60);
                if(day<10){
                    str+="\<em>0</em>";
                    str=str+"\<em>"+day+"\</em>&nbsp;天";
                }else{
                    var day1=parseInt(day/10);
                    var day2=day%10;
                    str=str+"\<em>"+day1+"\</em>";
                    str=str+"\<em>"+day2+"\</em>&nbsp;天";
                }
                if(hour<10){
                    str+="\<em>0</em>";
                    str=str+"\<em>"+hour+"\</em>&nbsp;时";
                }else{
                    var hour1=parseInt(hour/10);
                    var hour2=hour%10;
                    str=str+"\<em>"+hour1+"\</em>";
                    str=str+"\<em>"+hour2+"\</em>&nbsp;时";
                }
                if(minute<10){
                    str+="\<em>0</em>";
                    str=str+"\<em>"+minute+"\</em>&nbsp;分";
                }else{
                    var minute1=parseInt(minute/10);
                    var minute2=minute%10;
                    str=str+"\<em>"+minute1+"\</em>";
                    str=str+"\<em>"+minute2+"\</em>&nbsp;分";
                }
                if(second<10){
                    str+="\<em>0</em>";
                    str=str+"\<em>"+second+"\</em>&nbsp;秒&nbsp;";
                }else{
                    var second1=parseInt(second/10);
                    var second2=second%10;
                    str=str+"\<em>"+second1+"\</em>";
                    str=str+"\<em>"+second2+"\</em>&nbsp;秒&nbsp;";
                }
            }else{
                str="";
            }
            $('#djs').html(str);
        }
        setInterval(GetTime,0);

    })();

    //(function(){
    //    var categoryId = $('#categoryId').val();
    //    if(categoryId){
    //        $('#goodscategory').find('li').each(function(){
    //            if($(this).attr('value') == categoryId){
    //                $(this).trigger('click');
    //            }
    //        });
    //    }
    //})();

    function goodsList(page,goodsCategory,pageSize,activity,groupPurchase){
        var state = $('body').attr('state');
        if(state == ''){
            state = 0;
        }
        sessionStorage.setItem("page", page);
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = pageSize;
        requestData.state = state;
        requestData.goodsCategory = goodsCategory;//商品分类
        requestData.activity=activity;
        requestData.groupPurchase=groupPurchase;
        requestData.regular = $('body').attr('regular');

        Common.getData("/mall/goods.do", requestData, function(resp){
            var goods = resp.list;

            if(pageSize == 6){
                if(groupPurchase == -1){
                    if(goodsCategory==""){
                        Common.render({
                            tmpl: '#goodsListTmpl',
                            data: goods,
                            context:'#ALL',
                            overwrite: 1
                        });
                    }else{
                        Common.render({
                            tmpl: '#goodsListTmpl',
                            data: goods,
                            context:'#'+goodsCategory,
                            overwrite: 1
                        });
                    }
                }else if(groupPurchase ==0){
                    Common.render({
                        tmpl: '#groupPurchaseTmpl',
                        data: goods,
                        context:'#groupPurchase',
                        overwrite: 1
                    });
                }

            }else if(pageSize == 8){
                Common.render({
                    tmpl: '#goodsList',
                    data: goods,
                    context:'#ulNew',
                    overwrite: 1
                });
            }


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
            $('#' + goods.goodsId + ' em').text('￥'+ minPrice/100);
        } else {
            $('#' + goods.goodsId + ' em').text('￥'+ minPrice/100 + ' - ' + maxPrice/100);
        }
    }

    function getCategories(){
        Common.getData("/mall/categories.do", {}, function(resp){
            Common.render({
                tmpl: '#goodscategoryTmpl',
                data: resp.goodsCategories,
                context:'#goodscategory',
                overwrite: 1
            });
            Common.render({
                tmpl: '#categoryTmpl',
                data: resp.goodsCategories,
                context:'#category',
                overwrite: 1
            });

            Common.render({
                tmpl: '#listAcTml',
                data: resp.goodsCategories,
                context:'#listAc',
                overwrite: 1
            });

            $('.listData').each(function(){
                var parentId=$(this).attr('value');
                var that=this;
                Common.getData("/mall/categories.do", {level:2,parentId:parentId}, function(resp) {
                    Common.render({
                        tmpl: '#listTml',
                        data: resp.goodsCategories,
                        context:that,
                        overwrite: 1
                    });
                });
            });

        })
    }

});
