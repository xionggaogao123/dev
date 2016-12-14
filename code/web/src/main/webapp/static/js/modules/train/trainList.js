/**
 * Created by admin on 2016/10/27.
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {
    var common = require('common');
    require('pagination');

    var trainList = {};
    var region = $('body').attr('region');
    var sortType = 1;
    var page = 1;
    var lon;
    var lat;
    var flag = false;
    var typeId = $('body').attr('typeId');
    var areaId = $('body').attr('areaId');
    var location = $('body').attr('location');
    var firstLoadType = true;
    var firstLoadArea = true;
    trainList.init = function () {
        getTopItems();
        getTopRegion();
    };
    $(document).ready(function () {
        $('body').on('click', '.d1f span', function () {
            $('.d1f span').removeClass('cur2');
            $(this).addClass('cur2');
            getInstituteList(page);
        });
        $('body').on('click', '.d2f span', function () {
            $('.d2f span').removeClass('cur2');
            $(this).addClass('cur2');
            getInstituteList(page);
        });
        $('body').on('click', '.lesson-menu span', function () {
            $(this).addClass('cur3').siblings('span').removeClass('cur3');
            if ($(this).attr('tip') == 1) {
                sortType = 1;
            } else {
                sortType = 2;
            }
            getInstituteList(page);
        });
        $('body').on('click', '.hide_city_group>div a', function () {
            $('.hide_city_group>div a').removeClass('a-selected');
            $(this).addClass('a-selected');
            $('.city-name').text($(this).attr('regionName'));
            $('.d2f span').removeClass('cur2');
            $('.d2f span').eq(0).addClass('cur2');
            $('#regular').val("");
            getSecondRegions($(this));
            getInstituteList(page);
        })


        $('body').on('click', '#trainTop span', function () {
            $('#trainTop span').removeClass('cur1');
            $(this).addClass('cur1');
            $('.d1f span').removeClass('cur2');
            $('.d1f span').eq(0).addClass('cur2');
            getSecondItemTypes($(this));
            if (flag) {
                getInstituteList(page);
            } else {
                flag = true;
            }
        })

        $('body').on('click', '#search', function () {
            searchForRegular();
        })

        $('body').on('click','#redirectPage',function(){
             var page=$('#num').val();
             if(isNaN(page)){
                 alert("不能输入除了数字外的其他任何信息!");
                 return ;
             }
             var totalPage=$('.new-page-links').data('totalPage');
             if(parseInt(page)>totalPage){
                 alert("输入的不能超过总页数");
                 $('#num').val("");
                 return ;
             }
             getInstituteList(page);
        })


        var map, geolocation;
        //加载地图，调用浏览器定位服务
        if(location){
            $('#trainTop span').eq(0).trigger('click');
            $('a[regionName=' + region + ']').trigger('click');
        }else{
            map = new AMap.Map('containerMap', {
                resizeEnable: true
            });
            map.plugin('AMap.Geolocation', function () {
                geolocation = new AMap.Geolocation({
                    enableHighAccuracy: true,//是否使用高精度定位，默认:true
                    timeout: 10000,          //超过10秒后停止定位，默认：无穷大
                    buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
                    zoomToAccuracy: true,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
                    buttonPosition: 'RB'
                });
                map.addControl(geolocation);
                geolocation.getCurrentPosition();
                AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
                AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
            });
        }
    });

    function onComplete(data) {
        lon = data.position.getLng();
        lat = data.position.getLat();
        $('#trainTop span').eq(0).trigger('click');
        $('a[regionName=' + region + ']').trigger('click');
    }

    function onError() {
        alert("定位失败!请先登录");
    }


    function searchForRegular() {
        var regular = $('#regular').val();
        if (regular == "" || null == regular || regular == undefined) {
            alert("请先输入内容!");
            return;
        }
        $('.d1f span').removeClass('cur2');
        $('.d1f span').eq(0).addClass('cur2');
        $('.d2f span').removeClass('cur2');
        $('.d2f span').eq(0).addClass('cur2');
        getInstituteList(page);
    }

    function getInstituteList(page) {
        var isInit = true;
        var requestData = {};
        requestData.page = page;
        if ($('.hide_city_group>div').find('.a-selected').attr('regionName') == region) {
            requestData.lon = lon;
            requestData.lat = lat;
        }
        requestData.type = $('.d1f').find('.cur2').attr('itemId');
        requestData.area = $('.d2f').find('.cur2').attr('region');
        requestData.region = $('.hide_city_group>div').find('.a-selected').attr('regionId');
        requestData.itemType = $('#trainTop').data($('#trainTop').find('.cur1').text());
        requestData.regular = $('#regular').val();
        requestData.sortType = sortType;
        requestData.pageSize = 25;
        $('.h-load').show();
        $.ajax({
            type: "GET",
            data: requestData,
            url: '/train/getInstitutes.do',
            async: true,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function (resp) {
                $('.h-load').hide();
                $('.new-page-links').html("");
                if (resp.code == "200") {

                    var resultData = resp.message.list;
                    if (resultData.length > 0) {
                        $('.new-page-links').jqPaginator({
                            totalPages: Math.ceil(resp.message.count / resp.message.pageSize) == 0 ? 1 : Math.ceil(resp.message.count / resp.message.pageSize),//总页数
                            visiblePages: 5,//分多少页
                            currentPage: parseInt(page),//当前页数
                            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                            onPageChange: function (n) { //回调函数
                                if (isInit) {
                                    isInit = false;
                                } else {
                                    getInstituteList(n);
                                    $('body,html').animate({scrollTop: 0}, 20);
                                }
                            }
                        });

                        var str="<li><span >到第</span><em><input style='margin: -63px 0px 0px 34px;height: 30px; width: 48px;' id=\"num\">" +
                            "<button id=\"redirectPage\" style='margin: -35px 0 26px 0;width: 37px;'>确定</button></em></li>";
                        $('.new-page-links').data('totalPage',Math.ceil(resp.message.count / resp.message.pageSize) == 0 ? 1 : Math.ceil(resp.message.count / resp.message.pageSize));
                        $('.new-page-links').append(str);
                    }
                    template('#instituteTmpl', '#institute', resultData);
                } else {
                    alert(resp.message);
                }
            }
        });
    }

    function getTopRegion() {
        common.getData('/train/getRegions.do', {level: 2}, function (resp) {
            if (resp.code == "200") {
                var data = resp.message;
                template('#topRegionTmpl', '#topRegion', data);
            }
        })
    }

    function getSecondRegions(obj) {
        var regionId = obj.attr('regionId');
        common.getData('/train/getRegions.do', {regionId: regionId, level: 3}, function (resp) {
            if (resp.code == "200") {
                var data = resp.message;
                template('#regionTmpl', '#region', data);
                if (firstLoadArea) {
                    firstLoadArea = false;
                    if (null != areaId && areaId != undefined && areaId != "") {
                        $('.d2f span').removeClass('cur2');
                        $('.d2f').find('[region=' + areaId + ']').addClass('cur2');
                    }
                }
            }
        })
    }

    function getSecondItemTypes(obj) {
        var name = obj.text();
        var id = $('#trainTop').data(name);
        common.getData('/train/getItemTypes.do', {itemTypeId: id}, function (resp) {
            if (resp.code == "200") {
                var data = resp.message;
                template('#itemTypeTmpl', '#itemType', data);
                if (firstLoadType) {
                    firstLoadType = false;
                    if (null != typeId && typeId != undefined && typeId != "") {
                        $('.d1f span').removeClass('cur2');
                        $('.d1f').find('[itemId=' + typeId + ']').addClass('cur2');
                    }
                }
            }
        })
    }

    //加载模板
    function template(tmpl, ctx, data) {
        common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        })
    }


    function getTopItems() {
        common.getData('/train/getItemTypes.do', {level: 1}, function (resp) {
            if (resp.code == "200") {
                var list = resp.message;
                var str = "";
                for (var i in list) {
                    $('#trainTop').data(list[i].name, list[i].id);
                    str += "<span>" + list[i].name + "</span>";
                }
                str += "<button id=\"search\">搜索</button><input id=\"regular\" type=\"text\">";
                // for(var i in list){
                // 	str+="<option value='"+list[i].id+"'>"+list[i].name+"</option>";
                // }
                // str+="<div class=\"select-arrow\"></div>";
                $('#trainTop').append(str);

                // $('#trainTop').find('span').eq(1).addClass('cur1');
            }
        })
    }

    module.exports = trainList;
});