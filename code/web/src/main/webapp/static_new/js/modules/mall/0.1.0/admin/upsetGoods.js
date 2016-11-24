/**
 * Created by fl on 2016/1/30.
 */
'use strict';
    (function(){
        var app = angular.module('myApp', []);
        app.controller('myCtrl', function($http,$scope) {
            var cs = [{"name":"5", "value":0},{"name":"4", "value":0},{"name":"3", "value":0},{"name":"2", "value":0},{"name":"1", "value":0}];
            var kinds = [{kindId:"", kindName:"", specList: [{id:"", name:"", price:""}] }];
            $scope.goods = {state:2,activity:-1,groupPurchase:-1,popularLevel:0, sellCount:0, commentSummary:cs, kindDTOList:kinds, images:[], html:'', goodsId:'',expressTemplateId:''};
            $scope.expOff = 0;
            $scope.vouOff = 0;

            $scope.goods.goodsId = $('body').attr('goodsId');

            (function(){
                if($scope.goods.goodsId){
                    getGoods();
                }
            })();

            $scope.getGoods = function(){
                getGoods();
            }

            $scope.addKind = function(){
                $scope.goods.kindDTOList.push({kindId:"", kindName:"", specList: [{id:"", name:"", price:""}] });
            }
            $scope.deleteKind = function(){
                var length = $scope.goods.kindDTOList.length;
                if(length > 1){
                    $scope.goods.kindDTOList.pop();
                }
            }
            $scope.add = function(index){
                $scope.goods.kindDTOList[index].specList.push({id:"", name:"", price:""});
            }
            $scope.delete = function(index){
                var length = $scope.goods.kindDTOList[index].specList.length;
                if(length > 1){
                    $scope.goods.kindDTOList[index].specList.pop();
                }
            }
            $scope.submit = function(){
                var expOff = $scope.expOff * $scope.goods.discountPrice / 10;
                $scope.goods.experienceOff = Math.floor(expOff) * 10;
                var vouOff = $scope.vouOff * $scope.goods.discountPrice / 10;
                $scope.goods.voucherOff = Math.floor(vouOff) * 10;
                if($scope.goods.goodsId == ''){//新增
                    $.ajax({
                        url: '/mall/admin/goods.do?',
                        type: 'post',
                        data: angular.toJson($scope.goods),
                        contentType: 'application/json',
                        success: function (result) {
                            alert(result.message);
                        },
                        error: function(e) {
                            alert('添加失败');
                        }
                    });
                } else {//修改
                    $.ajax({
                        url: '/mall/admin/goods/'+$scope.goods.goodsId+'.do?',
                        type: 'PUT',
                        data: angular.toJson($scope.goods),
                        contentType: 'application/json',
                        success: function (result) {
                            alert(result.message);
                        },
                        error: function(e) {
                            console.log(e);
                            alert('添加失败');
                        }
                    });
                }
            }

            $scope.preview = function(){
                var goodsString = JSON.stringify($scope.goods);
                sessionStorage.setItem("goods", goodsString);
                window.open("/mall/admin/preview.do");
            }

            //$('body').removeClass('ng-scope');

            $('#suggestImg').diyUpload({
                url:'/mall/images.do',
                success:function( data ) {
                    $scope.goods.suggestImage = data.message[0].path;
                    console.info( data );
                },
                error:function( err ) {
                    console.info( err );
                //    
                }
            });
            $('#imgs').diyUpload({
                url:'/mall/images.do',
                success:function( data ) {
                    $scope.goods.images.push({id:data.message[0].id, value:data.message[0].path});
                    console.info( data );
                },
                error:function( err ) {
                    console.info( err );
                }
            });
            $('#html').diyUpload({
                url:'/mall/images.do',
                success:function( data ) {
                    $scope.goods.html +=  '<img src="' + data.message[0].path + '">';
                    console.info( data );
                },
                error:function( err ) {
                    console.info( err );
                }
            });


            function getGoods(){
                if('' != $scope.goods.goodsId && null != $scope.goods.goodsId){
                    $http.get('/mall/goods/'+$scope.goods.goodsId+'.do', {
                        params: {}
                    }).then(function (response) {
                        $scope.goods = response.data.dto;

                        var expOff = $scope.goods.experienceOff / $scope.goods.discountPrice;
                        $scope.expOff = expOff.toFixed(2);
                        var vouOff = $scope.goods.voucherOff / $scope.goods.discountPrice;
                        $scope.vouOff = vouOff.toFixed(2);

                    });
                } else {
                    alert("请输入商品ID");
                }
            }

            getGoodsCategory(1,"");

            function getGoodsCategory(level,parentId){
                $http.get('/mall/admin/eGoodsCategorys.do', {
                    params: {level:level,parentId:parentId}
                }).then(function (response) {
                    $scope.goodsCategoryList = response.data;
                });
            }


            getGradeCategory();

            function getGradeCategory(){
                $http.get('/mall/admin/eGradeCategories.do', {
                    params: {}
                }).then(function (response) {
                    $scope.gradeCategoryList = response.data;
                });
            }

            getExpressTemplate();

            function getExpressTemplate(){
                $http.get('/mall/admin/eTemplateList.do', {
                    params: {
                    }
                }).then(function (response) {
                    $scope.expressTemplateList = response.data;
                });
            }

            $('body').on('change', '#category', function() {
                 var parentId=$(this).val();
                 $('#levelCategory').show();
                 $http.get('/mall/admin/eGoodsCategorys.do', {
                    params: {level:2,parentId:parentId}
                 }).then(function (response) {
                    $scope.levelGoodsCategoryList = response.data;
                 });
            });

        });

    })();


    

