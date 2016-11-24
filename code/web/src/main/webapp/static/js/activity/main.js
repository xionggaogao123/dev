/**
 * Crayola colors in JSON format
 * from: https://gist.github.com/jjdelc/1868136
 */
var colors =
[
    {
        "hex": "",
        "label": "来我家做作业和玩",
        "rgb": "(239, 222, 205)"
    },
    {
        "hex": "",
        "label": "来小区一起玩",
        "rgb": "(205, 149, 117)"
    },
    {
        "hex": "",
        "label": "生日party",
        "rgb": "(253, 217, 181)"
    },
    {
        "hex": "",
        "label": "外出踏青旅游",
        "rgb": "(120, 219, 226)"
    },
    {
        "hex": "",
        "label": "户外打球",
        "rgb": "(135, 169, 107)"
    },
    {
        "hex": "",
        "label": "聚餐",
        "rgb": "(255, 164, 116)"
    }
];

$(function () {
  $('#nope').autocompleter({
        // marker for autocomplete matches
        highlightMatches: true,

        // object to local or url to remote search
        source: colors,

        // custom template
        template: '{{ label }} <span></span>',

        // show hint
        hint: true,

        // abort source if empty field
        empty: false,

        // max results
        limit: 5,

        callback: function (value, index, selected) {
            if (selected) {
                $('.icon').css('background-color', selected.hex);
            }
        }
    });
});

function lxfScroll(main,titleli,alt,speed){
    var lxfscroll = $(main);
    var ul = lxfscroll.find("ul");
    var li = lxfscroll.find("li");
    var tli = $(titleli);
    var alt = $(alt);
    var cutspeed = 1;//切换的速度
    var autospeed = speed;//自动播放的速度
    var n = 0;
    var imgwidth = li.find("img").attr("width");//获取图片高度
    var lilength = li.length;//获取图片数量
    var timer;
    li.eq(0).clone().appendTo(ul);
    /* 标题按钮事件 */
    function hoverscroll() {
        tli.mouseenter(function(){
            var index = tli.index($(this));
            var lipoint = index*imgwidth;
            var imgTitle = li.find("img").eq(index).attr("alt");
            alt.text(imgTitle);
            tli.removeClass("cur");
            $(this).addClass("cur");
            ul.stop(true,false).animate({"left":-lipoint+"px"},cutspeed);
        });
    };
    /* 自动轮换 */
    function autoroll() {
        /*最后一个回到第一个的时候*/
        if(n >= lilength) {tli.removeClass("cur").eq(0).addClass("cur"); ul.stop(true,false).css({left:"0px"});n = 0;};
        var lipoint = n*imgwidth;
        var imgTitle = li.find("img").eq(n).attr("alt");
        ul.stop(true,false).animate({"left":-lipoint+"px"},cutspeed);
        tli.removeClass("cur").eq(n).addClass("cur");
        if(n >= lilength){tli.removeClass("cur").eq(0).addClass("cur"); };
        alt.text(imgTitle);
        n++;
        timer = setTimeout(autoroll, autospeed);
        if(n >= lilength) {alt.text(li.find("img").eq(0).attr("alt"));};
    };
    /* 鼠标悬停即停止自动轮换 */
    function stoproll() {
        li.hover(function() {
                clearTimeout(timer);
                n = $(this).prevAll().length+1;
            }
            , function() {
                timer = setTimeout(autoroll, autospeed);
            });
        tli.hover(function() {
                clearTimeout(timer);
                n = $(this).prevAll().length+1;
            }
            , function() {
                timer = setTimeout(autoroll, autospeed);
            });
    };
    hoverscroll();
    autoroll();//启动自动播放功能
    stoproll();//启动鼠标悬停功能
};