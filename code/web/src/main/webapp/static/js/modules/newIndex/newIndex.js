/*
 * @Author: Tony
 * @Date:   2016-10-19 15:48:44
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-10-20 10:23:25
 */
define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    var newIndex = {};

    var IWid=0;
    var widths = [];
    var index = 0;
    newIndex.init = function () {
        getCommunityNews();

        //获取大赛帖子
        getFPost("/forum/fPostsActivity.do", '#talentTml');

        getMyCommunitys();
    }
    $(document).ready(function () {

        var $this = $("#news");
        var scrollTimer;
        $this.hover(function () {
            clearInterval(scrollTimer);
        }, function () {
            scrollTimer = setInterval(function () {
                scrollNews($this);
            }, 2000);
        }).trigger("mouseleave");

        function scrollNews(obj) {
            var $self = obj.find("ul");
            var lineHeight = $self.find("li:nth-child(1)").height();
            $self.animate({
                "marginTop": -lineHeight - 20 + "px"
            }, 684, function () {
                $self.css({
                    marginTop: 0
                }).find("li:first").appendTo($self);
            })
        };



        //进入我的社区
        $('body').on('click', '#enter', function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    if (resp.login) {
                        window.location.href = "/community/communityAllType.do";
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        });

        $('body').on('click','#forumIndex',function () {
           window.open('/mall/index');
        });

        $('body').on('click','#competition',function(){
            window.open('/competition');
        })

        $('body').on('click','#forum',function(){
            window.open('/forum');
        })


        //创建新社区
        $('body').on('click', '#btn1', function () {
            $.ajax({
                url: "/forum/loginInfo.do?date=" + new Date(),
                type: "get",
                dataType: "json",
                data: {},
                success: function (resp) {
                    if (resp.login) {
                        window.location.href = "/communityCreate.do";
                    } else {
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                }
            });
        })

        $(function () {
            var list = $('#talentList');
            var prev = $('#prev-p');
            var next = $('#next-p');
            var index = 1;
            var len = 3;
            var interval = 3000;
            var timer;


            function animate(offset) {
                var left = parseInt(list.css('left')) + offset;
                if (offset > 0) {
                    offset = '+=' + offset;
                }
                else {
                    offset = '-=' + Math.abs(offset);
                }
                list.animate({'left': offset}, 300, function () {
                    if (left > -200) {
                        list.css('left', -718 * len);
                    }
                    if (left < (-718 * len)) {
                        list.css('left', -718);
                    }
                });
            }


            next.bind('click', function () {
                if (list.is(':animated')) {
                    return;
                }
                if (index == 3) {
                    index = 1;
                }
                else {
                    index += 1;
                }
                animate(-718);
            });

            prev.bind('click', function () {
                if (list.is(':animated')) {
                    return;
                }
                if (index == 1) {
                    index = 3;
                }
                else {
                    index -= 1;
                }
                animate(718);
            });


        });
    })
    function getCommunityNews() {
        var param = {};
        param.pageSize = 1;
        common.getData('/community/getAllTypeMessage.do',param,function (resp) {
            if (resp.code == "200") {
                var announcement = resp.message.announcement;
                var activity = resp.message.activity;
                var share = resp.message.share;
                var means = resp.message.means;
                var homework = resp.message.homework;
                var materials = resp.message.materials;
                loadAnnouncement(announcement);
                loadActivity(activity);
                loadShare(share);
                loadMaterials(materials);
                loadMeans(means);
                loadHomework(homework);
            }else{
                // alert(resp.message);
            }
        })
    }

    function getMyCommunitys() {

        var param = {};
        common.getData('/community/myCommunitys',param,function (resp) {

            for(var i=0;i<resp.message.list.length;i++) {
                $('#coms').append("<li class='com-li' value='"+ resp.message.list[i].id +"'>" + resp.message.list[i].name + "</li>");
            }

            $('#coms li').each(function () {
                var mywidth = $(this).width();
                widths.push(mywidth);
            });

            if(widths.length == 1) {
                $('.sp-do').hide();
                $('.sp-up').hide();
            }

            $('.com-li').each(function () {
                var id = $(this).attr('value');
                $(this).click(function () {
                    window.open('/community/communityPublish?communityId=' + id,'_blank');
                });
            });

            $('.sp-do').click(function(){

                if($('#coms').is(':animated'))
                    return;
                var wid=widths[index];
                index++;
                wid = wid + 39;
                IWid += -wid;
                $('#coms').animate({
                    left: IWid + 'px'
                });

                if(index == widths.length - 1) {
                    $(this).hide();
                }
                if(index >0 && index < widths.length) {
                    $('.sp-up').show();
                }
            });

            $('.sp-up').click(function(){
                if($('#coms').is(':animated'))
                    return;
                var wid=widths[--index];
                wid = wid + 39;
                IWid += wid;
                $('#coms').animate({
                    left: IWid + 'px'
                });

                if(index == 0) {
                    $(this).hide();
                }

                if(index > 0 && index < widths.length -1) {
                    $('.sp-do').show();
                }
            });

        });
    }

    function getFPost(url, template) {
        var requestData = {};
        requestData.sortType = 2;
        requestData.page = 1;
        requestData.classify = -1;
        requestData.cream = -1;
        requestData.gtTime = 0;
        requestData.postSection = "";
        requestData.pageSize = 4;
        requestData.inSet = 1;
        common.getData(url, requestData, function (resp) {
            var total = resp.list;
            common.render({
                tmpl: template,
                data: total,
                context: '#talentList',
                overwrite: true
            });
        })
    }


    function loadHomework(data){
        var temp = "";
        for (var i in data) {
            temp += "<li class=\"clearfix\">" +
                "<p class=\"p1\"><em class=\"em1\">作业</em><br>" +
                "<em class=\"em2\">" + data[i].timeStr + "</em> </p>" +
                "<img src=\"/static/images/newIndex/cafe_homework.png\">" +
                "<p class=\"p-red\"></p>" +
                "<span class=\"spa-train\"></span>" +
                "<p class=\"p2\">" +
                " <span class=\"spa-active\">" +
                "<span class=\"sp1\">" + data[i].title + "</span>";
            var attachements = data[i].attachements;
            for (var j in attachements) {
                temp += "<span class=\"sp3\">附件:" + attachements[j].flnm + "</span>";
            }

            temp += "</span></p></li>";
        }
        $('#communityNews').append(temp);
    }
    function loadMeans(data) {
        var temp = "";
        for (var i in data) {
            temp += "<li class=\"clearfix\">" +
                "<p class=\"p1\"><em class=\"em1\">学习资料</em><br>" +
                "<em class=\"em2\">" + data[i].timeStr + "</em> </p>" +
                "<img src=\"/static/images/newIndex/purple_study.png\">" +
                "<p class=\"p-red\"></p>" +
                "<span class=\"spa-train\"></span>" +
                "<p class=\"p2\">" +
                " <span class=\"spa-active\">" +
                "<span class=\"sp1\">" + data[i].title + "</span>";
            var attachements = data[i].attachements;
            for (var j in attachements) {
                temp += "<span class=\"sp3\">附件:" + attachements[j].flnm + "</span>";
            }

            temp += "</span></p></li>";
        }
        $('#communityNews').append(temp);
    }

    function loadMaterials(data) {
        var temp = "";
        for (var i in data) {
            temp += "<li class=\"clearfix\">" +
                "<p class=\"p1\"><em class=\"em1\">学习用品</em><br>" +
                "<em class=\"em2\">" + data[i].timeStr + "</em> </p>" +
                "<img src=\"/static/images/newIndex/green_study.png\">" +
                "<p class=\"p-red\"></p>" +
                "<span class=\"spa-train\"></span>" +
                "<p class=\"p2\">" +
                " <span class=\"spa-active\">" +
                "<span class=\"sp1\">" + data[i].title + "</span>" +
                "<span class=\"sp4\">" + data[i].content + "</span></span></p></li>";
        }
        $('#communityNews').append(temp);
    }

    function loadShare(data) {
        var temp = "";
        for (var i in data) {
            temp += "<li class=\"clearfix\">" +
                "<p class=\"p1\"><em class=\"em1\">火热分享</em><br>" +
                "<em class=\"em2\">" + data[i].timeStr + "</em> </p>" +
                "<img src=\"/static/images/newIndex/blue_share.png\">" +
                "<p class=\"p-red\"></p>" +
                "<span class=\"spa-train\"></span>" +
                "<p class=\"p2\">" +
                " <span class=\"spa-active\">" +
                "<span class=\"sp1\">" + data[i].title + "</span>" +
                "<span class=\"sp3\">" + data[i].content + "</span></span><span>";
            // var images = data[i].images;
            // for (var j in images) {
            //     temp += "<img width='62' height='42' src=\"" + images[j].url + "\">";
            // }

            temp += "</span></p></li>";
        }
        $('#communityNews').append(temp);
    }

    function loadActivity(data) {
        var temp = "";
        for (var i in data) {
            temp += "<li class=\"clearfix\">" +
                "<p class=\"p1\"><em class=\"em1\">组织活动报名</em><br>" +
                "<em class=\"em2\">" + data[i].timeStr + "</em> </p>" +
                "<img src=\"/static/images/newIndex/yellow_community.png\">" +
                "<p class=\"p-red\"></p>" +
                "<span class=\"spa-train\"></span>" +
                "<p class=\"p2\">" +
                " <span class=\"spa-active\">" +
                "<span class=\"sp1\">" + data[i].title + "</span>" +
                "<span class=\"sp2\">" + data[i].content + "</span></span></p></li>";
        }
        $('#communityNews').append(temp);
    }

    function loadAnnouncement(data) {
        var temp = "";
        for (var i in data) {
            temp += "<li class=\"clearfix\">" +
                "<p class=\"p1\"><em class=\"em1\">社区通知</em><br>" +
                "<em class=\"em2\">" + data[i].timeStr + "</em> </p>" +
                "<img src=\"/static/images/newIndex/red_notice.png\">" +
                "<p class=\"p-red\"></p>" +
                "<span class=\"spa-train\"></span>" +
                "<p class=\"p2\">" +
                "<span class=\"spa-notice\">" + data[i].content + "</span></p></li>";
        }
        $('#communityNews').append(temp);
    }

    module.exports = newIndex;
});