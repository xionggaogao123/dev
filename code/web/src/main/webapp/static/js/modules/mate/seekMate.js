define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');
    var seekMate = {};
    var scrollTimer;
    var seekMateParm = {
        pageSize: 10
    };
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

        var geolocation = new BMap.Geolocation();
        geolocation.getCurrentPosition(function (r) {
            if (this.getStatus() == BMAP_STATUS_SUCCESS) {
                var requestParm = {
                    lon: r.point.lng,
                    lat: r.point.lat
                };

                seekMateParm.lon = r.point.lng;
                seekMateParm.lat = r.point.lat;
                getMates(1);
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
        getUserTag();

    });

    function bindClick() {

        $('.btn-wdbq').click(function () {
            $('.bg').fadeIn();
            $('.wind-biaoq').fadeIn();
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
            $('.wind-act-fq').fadeIn();
            $('.bg').fadeIn();
        });
        $('body').on('click', '.ul-nearnews li button', function () {
            $('.wind-act-det').fadeIn();
            $('.bg').fadeIn();
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