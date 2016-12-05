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
    seekMate.init = function () {

    };

    function getMates(page) {
        seekMateParm.page = page;
        var init = true;
        common.getData('/mate/getPlayMates.do', seekMateParm, function (resp) {
            if (resp.code == "200") {
                template('#mateBox', '#near-mates', resp.message.result);
                $('.mate-count').text(resp.message.totalCount);
                page = resp.page;
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

        var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function (r) {
            if (this.getStatus() == BMAP_STATUS_SUCCESS) {

                lon = r.point.lng;
                lat = r.point.lat;
                var requestParm = {
                    lon: r.point.lng,
                    lat: r.point.lat
                };

                seekMateParm.lon = r.point.lng;
                seekMateParm.lat = r.point.lat;
                getMates(1);
                getActivitys();
                $.ajax({
                    url: '/mate/updateMateData.do',
                    data: requestParm
                })
            }
            else {
                alert('failed' + this.getStatus());
            }
        }, {enableHighAccuracy: true});

        bindClick();

        if (isLogin) {
            getUserTag();
        }
    });

    function bindClick() {

        $('.mate-publish .b1').click(function () {
            publishActivity();
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

            if(!isLogin) {
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
                $('.wind-act-det .p6').empty();
                for (var i = 0; i < selectActivity.signSheets.length; i++) {
                    $('.wind-act-det .p6').append("<img src='" + selectActivity.signSheets[i].avatar + "'</img>");
                }
            }

            $('.wind-act-det').fadeIn();
            $('.bg').fadeIn();
        });

        $('body').on('click','.act-already-join',function () {

            alert('haha');
        });

        $('body').on('click','.act-already-push',function () {

            alert('haha');
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
        }, function () {
            scrollTimer = setInterval(function () {
                scrollNews($("#nearNews"));
            }, 2000);
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
                if ($(this).hasClass('gre-cur')) {
                    $(this).removeClass('gre-cur');
                } else {
                    $(this).addClass('gre-cur');
                }
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

        $('.mate-timed span').click(function () {
            $('.mate-timed span').each(function () {
                $(this).removeClass('gre-cur');
            });
            $(this).addClass('gre-cur');
            var ons = $(this).attr('value');
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
        $('.biaoq-selected span').each(function () {
            var code = $(this).attr('code');
            var tag = $(this).text();
            var userTag = {
                code: code,
                tag: tag
            };

            $.ajax({
                url: '/community/pushUserTag.do',
                contentType: "application/json",
                dataType: 'json',
                type: 'post',
                data: JSON.stringify(userTag),
                success: function () {
                    $('.bg').fadeOut();
                    $('.wind-biaoq').fadeOut();
                }
            })
        })
    }

    function publishActivity() {
        var tag = $('.wind-act-fq .p3 select').val();
        var title = $('.wind-act-fq .p3 input').val();
        var desc = $('.wind-act-fq textarea').val();
        var date = $('#datepicker').val();
        var hour = $('.wind-act-fq .time-hour').val();
        var mins = $('.wind-act-fq .time-mins').val();

        date = date + ' ' + hour + ':' + mins;
        var requestParm = {
            lon: lon,
            lat: lat,
            acode: tag,
            title: title,
            desc: desc,
            endTime: date
        };
        common.getData('/activity/publish.do', requestParm, function (resp) {
            if (resp.code == 200) {
                $('.wind-act-fq').fadeOut();
                $('.bg').fadeOut();
            }
        });
    }

    function getActivitys() {
        var requestParm = {
            lon: lon,
            lat: lat
        };
        common.getData('/activity/nearActivitys.do', requestParm, function (resp) {
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
        common.getData('/activity/sign.do', requestParm, function (resp) {
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