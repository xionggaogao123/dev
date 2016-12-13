define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var seekMate = {};
    var scrollTimer;
    var isLogin = false;
    var seekMateParm = {
        pageSize: 10
    };
    var lon;
    var lat;
    var activitys = [];
    var myTags = [];
    seekMate.init = function () {
        getAllSortType();
    };

    function getMates(page) {
        seekMateParm.page = page;
        var init = true;
        common.getData('/mate/getPlayMates.do', seekMateParm, function (resp) {

            if (resp.code == "200") {
                template('#mateBox', '#near-mates', resp.message.result);
                $('.mate-count').text(resp.message.totalCount);
                page = resp.page;
                if(resp.message.totalPages == 0) {
                    return;
                }
                $('.new-page-links').jqPaginator({
                    totalPages: resp.message.totalPages,//总页数
                    visiblePages: 10,//分多少页
                    currentPage: resp.message.page,//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (init) {
                            init = false;
                        } else {
                            getMates(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            } else {
                alert(resp.message);
            }
        });
    }

    $(document).ready(function () {

        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                isLogin = resp.login;
            }
        });

        var map, geolocation;
        //加载地图，调用浏览器定位服务
        map = new AMap.Map('container', {
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

        bindClick();

        if (isLogin) {
            getUserTag();
        }
    });

    function bindClick() {

        $('.mate-publish .b1').click(function () {
            publishActivity();
        });

        $('.mate-publish .b2').click(function () {

            $('.wind-act-fq').fadeOut();
            $('.bg').fadeOut();
        });

        $('.btn-wdbq').click(function () {
            if (!isLogin) {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            } else {
                $('.bg').fadeIn();
                $('.wind-biaoq').fadeIn();
            }

        });
        $('.wind-act-det .p1 em,.wind-act-det .p7 .b2').click(function () {

            $('.wind-act-det').fadeOut();
            $('.bg').fadeOut();
        });

        $('.wind-act-fq .p1 em,.wind-act-fq .p4 .b2').click(function () {
            $('.wind-act-fq').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '.near-li button', function () {
            var userId = $(this).attr('value');
            if (isLogin) {
                window.open('/webim/index.do?userId=' + userId, '__blank');
            } else {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            }
        });

        $('.btn-fbgb').click(function () {

            if (!isLogin) {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            } else {
                $('.wind-act-fq').fadeIn();
                $('.bg').fadeIn();
            }

        });

        $('body').on('click', '.wind-act-det .b1', function () {

            signActivty();
        });

        $('body').on('click', '.ul-nearnews li button', function () {

            if (!isLogin) {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
                return;
            }
            var value = $(this).attr('value');
            var selectActivity;

            for (var i = 0; i < activitys.length; i++) {
                if (activitys[i].acid === value) {
                    selectActivity = activitys[i];
                }
            }
            if (selectActivity) {
                $('.wind-act-det .p5 span').text(selectActivity.signCount);
                $('.wind-act-det .p4 span').text(selectActivity.description);
                $('.wind-act-det .p2 i').text(selectActivity.title);
                $('.wind-act-det .p3 span').text(selectActivity.activityTime);
                $('.wind-act-det').attr('acid', selectActivity.acid);
                $('.wind-act-det .p2 span em span').text(selectActivity.activityTheme.data);
                $('.wind-act-det .p6').empty();
                for (var i = 0; i < selectActivity.signSheets.length; i++) {
                    $('.wind-act-det .p6').append("<img src='" + selectActivity.signSheets[i].avatar + "'</img>");
                }
            }

            $('.wind-act-det').fadeIn();
            $('.bg').fadeIn();
        });

        $('body').on('click', '.act-already-push,.act-already-join', function () {

            if (isLogin) {
                window.open('/community/communityAllType.do?target=activity', '__blank');
            } else {
                $('.store-register').fadeToggle();
                $('.bg').fadeToggle();
            }

        });

        $('body').on('click', '.wind-biaoq em', function () {
            $('.bg').fadeOut();
            $('.wind-biaoq').fadeOut();
        });

        $('body').on('click', '.wind-biaoq em,.wind-biaoq .btn-add-no', function () {
            $('.bg').fadeOut();
            $('.wind-biaoq').fadeOut();
        });

        $('body').on('click', '.wind-biaoq .btn1', function () {
            updateTags();
        });

        $('.biaoq-all span').click(function () {
            if ($(this).hasClass('bq-cur')) {
                $(this).removeClass('bq-cur');
                var code = $(this).attr('code');
                $('.biaoq-selected span').each(function () {
                    if ($(this).attr('code') === code) {
                        $(this).remove();
                    }
                });
            } else {
                if ($('.biaoq-selected span').length >= 6) {
                    alert('标签只能添加6个');
                    return;
                }
                $(this).addClass('bq-cur');
                $('.biaoq-selected').append($(this).clone());
            }
        });

        $("#nearNews").hover(function () {
            clearInterval(scrollTimer);
            scrollTimer = false;
        }, function () {
            if (!scrollTimer) {
                scrollTimer = setInterval(function () {
                    scrollNews($("#nearNews"));
                }, 3000);
            }
        }).trigger("mouseleave");

        function scrollNews(obj) {
            var $self = obj.find("ul");
            var lineHeight = $self.find("li:first").height();
            $self.animate({
                "marginTop": -lineHeight - 21 + "px"
            }, 1000, function () {
                $self.css({
                    marginTop: 0
                }).find("li:first").appendTo($self);
            })
        }

        $('.mate-xingqu span').click(function () {
            var value = $(this).attr('value');
            if (value) {
                $('.mate-xingqu span').each(function () {
                    $(this).removeClass('gre-cur');
                });
                $(this).addClass('gre-cur');
            } else {
                $('.mate-xingqu span').each(function () {
                    if ($(this).attr('value')) {
                        $(this).removeClass('gre-cur');
                    }
                });
                $(this).addClass('gre-cur');
            }
            var tags = '';
            $('.mate-xingqu span.gre-cur').each(function () {
                var code = $(this).attr('code');
                if (code) {
                    tags += code + ','
                }
            });
            seekMateParm.tags = tags;
            getMates(1);
        });

        $('.mate-aged span').click(function () {
            $('.mate-aged span').each(function () {
                $(this).removeClass('gre-cur');
            });
            $(this).addClass('gre-cur');
            var aged = $(this).attr('value');
            seekMateParm.aged = aged;
            getMates(1);
        });

        $('.mate-distance span').click(function () {
            $('.mate-distance span').each(function () {
                $(this).removeClass('gre-cur');
            });
            $(this).addClass('gre-cur');
            var distance = $(this).attr('value');
            seekMateParm.distance = distance;
            getMates(1);
        });

        $('body').on('click', ".mate-timed span", function () {
            var value = $(this).attr('value');
            if (value == '-1') {
                $(this).addClass('gre-cur');
                $('.mate-timed span').each(function () {
                    if ($(this).attr('value') != '-1') {
                        $(this).removeClass('gre-cur');
                    }
                });
                seekMateParm.ons = '';
            } else {
                $('.mate-timed span').each(function () {
                    if ($(this).attr('value') == '-1') {
                        $(this).removeClass('gre-cur');
                    }
                });
                $(this).addClass('gre-cur');
            }
            var ons = '';
            $('.mate-timed span').each(function () {
                if ($(this).attr('value') != '-1') {
                    if ($(this).hasClass('gre-cur')) {
                        ons += $(this).attr('value') + ',';
                    }
                }
            });
            seekMateParm.ons = ons;
            getMates(1);
        });
    }

    function getUserTag() {
        $.ajax({
            type: "GET",
            url: '/mate/getMyTags.do',
            success: function (resp) {

                $('.biaoq-all span').each(function () {
                    var code = $(this).attr('code');
                    for (var i = 0; i < resp.message.length; i++) {
                        myTags.push(resp.message[i]);
                        if (code == resp.message[i].code) {
                            $(this).addClass('bq-cur');
                        }
                    }
                });

                for (i = 0; i < resp.message.length; i++) {
                    var biaoq = "<span class='bq-cur' code='" + resp.message[i].code + "' >" + resp.message[i].tag + "</span>";
                    $('.biaoq-selected').append(biaoq);
                }

            }
        });
    }

    function updateTags() {
        var tags = '';
        $('.biaoq-selected span').each(function () {
            var code = $(this).attr('code');
            tags += code + ',';
        });

        $.ajax({
            url: '/mate/updateMateData.do',
            type: 'get',
            data: {
                tags: tags
            },
            success: function () {
                $('.bg').fadeOut();
                $('.wind-biaoq').fadeOut();
            }
        })
    }

    function publishActivity() {
        var tag = $('.wind-act-fq .p3 select').val();
        var title = $('.wind-act-fq .p3 input').val();
        var desc = $('.wind-act-fq textarea').val();
        var date = $('#datepicker').val();
        var hour = $('.wind-act-fq .time-hour').val();
        var mins = $('.wind-act-fq .time-mins').val();

        if (title === '') {
            alert("标题不能为空");
            return;
        }

        if (desc === '') {
            alert("描述不能为空");
            return;
        }

        if (date === '') {
            alert("时间不能为空");
            return;
        }
        date = date + ' ' + hour + ':' + mins;
        var requestParm = {
            lon: lon,
            lat: lat,
            acode: tag,
            title: title,
            desc: desc,
            endTime: date
        };

        common.getData('/factivity/publish.do', requestParm, function (resp) {
            if (resp.code == 200) {
                $('.wind-act-fq').fadeOut();
                $('.bg').fadeOut();

                alert('发布成功');

                var count = $('.act-already-push i').text();
                $('.act-already-push i').text(++count);
                getActivitys();

            }
             else {
                alert(resp.message);
            }
        });
    }

    function getActivitys() {
        var requestParm = {
            lon: lon,
            lat: lat
        };
        common.getData('/factivity/nearActivitys.do', requestParm, function (resp) {
            if (resp.code == "200") {
                template('#activityBox', '#nearnewsBox', resp.message.result);
                activitys = resp.message.result;
            }
        });
    }

    function signActivty() {
        var requestParm = {
            acid: $('.wind-act-det').attr('acid')
        };
        common.getData('/factivity/sign.do', requestParm, function (resp) {
            if (resp.code == "200") {
                if (resp.message) {
                    alert("报名成功");
                    $('.wind-act-det').fadeOut();
                    $('.bg').fadeOut();
                } else {
                    alert('您已经报名了');
                }
            }
        });
    }

    //解析定位结果
    function onComplete(data) {
        var str = ['定位成功'];
        str.push('经度：' + data.position.getLng());
        str.push('纬度：' + data.position.getLat());
        str.push('精度：' + data.accuracy + ' 米');
        str.push('是否经过偏移：' + (data.isConverted ? '是' : '否'));

        seekMateParm.lon = data.position.getLng();
        seekMateParm.lat = data.position.getLat();
        lon = data.position.getLng();
        lat = data.position.getLat();

        var requestParm = {
            lon: data.position.getLng(),
            lat: data.position.getLat()
        };
        $.ajax({
            url: '/mate/updateMateData.do',
            data: requestParm
        });

        getMates(1);
        getActivitys();
    }

    //解析定位错误信息
    function onError(data) {
        alert("定位失败");
    }

    function getAllSortType() {
        common.getData('/mate/sortType.do', {}, function (resp) {
            if (resp.code == "200") {
                for (var i = 0; i < resp.message.tags.length; i++) {
                    $('.wind-act-fq select.theme').append('<option value="' + resp.message.tags[i].code + '">' + resp.message.tags[i].data + '</option>');
                    $('.biaoq-all').append('<span code="' + resp.message.tags[i].code + '">' + resp.message.tags[i].data + '</span>');
                }
                var result = [];
                result.push(resp.message);
                template('#menuTmpl', '#nearMenu', result);
            }
        });
    }


    //加载模板
    function template(tmpl, ctx, data) {
        common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        });
    }

    module.exports = seekMate;
});