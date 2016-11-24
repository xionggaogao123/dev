/**
 * Created by qiangm on 2015/7/28.
 */
define(function(require,exports,module) {
    require("jquery");
    require("doT");

    var leftbar={};
    var recommendRandom = 1;

    /**
     * 第一次进入时获取推荐好友列表
     * @param page
     */
    function recommendFriend(page) {
        var pageChoose=0;
        if(page==-1)
            pageChoose=recommendRandom;
        $.ajax({
            url: '/friendcircle/recommendedFriends.do',
            type: 'post',
            dataType: "json",
            data: {
                userId: '${wbUserInfo.id}',
                page: pageChoose,
                pageSize: 5
            },
            success: function (result) {
                var htm = '';
                result = result.rows;
                for (var i = 0; i < result.length; i++) {
                    var role = getRole(result[i].role);
                    htm += '<li><img class="fc_stu_img" target-id="' + result[i].id + '" role="' + result[i].role + '" ' +
                    'src="' + result[i].imgUrl + '" style="width:48px;height:48px;"><div class="mingzi" style="overflow: visible;"><a><span style="display: inline-block;-ms-word-break:break-all;word-break:break-all;' +
                    'width: 8em;color:#656565;font-weight: initial">' + result[i].userName + '  ' + role + '</span>' +
                    '<div class="center_left_ziti" style="overflow: visible;"><span>';
                    if (result[i].cityName != null) {
                        htm += result[i].cityName;
                    }
                    if (result[i].schoolName4WB != null) {
                        htm += '  ' + result[i].schoolName4WB;
                    }
                    htm += '</span><div><span>';
                    if (result[i].mainClassName != null) {
                        htm += result[i].mainClassName;
                    }
                    htm += '</span></div></div></a></div><div class="center_left_haoyou"> ' +
                    '<a class="addFriend" fid="\'' + result[i].id + '\'"><span>+好友</span></a></div><div style="clear: both"></div></li>'
                }
                var sk = $("#center_left_li").find("ul").empty();
                $(sk).append(htm);
                recommendRandom += 1;
            }
        });
    }

    /**
     * 根据角色返回角色名
     * @param role
     * @returns {string}
     */
    function getRole(role) {
        var STUDENT = 1;//"学生")
        var TEACHER = 1 << 1;// "老师"),
        var PARENT = 1 << 2;//,"家长"),
        var HEADMASTER = 1 << 3;//"校长"),
        var LEADER_CLASS = 1 << 4;//,"班主任"),
        var K6KT_HELPER = 1 << 5;//,"K6KT小助手"),
        var ADMIN = 1 << 6;//,"管理员"),
        var LEADER_OF_GRADE = 1 << 7;//"年级组长"),
        var LEADER_OF_SUBJECT = 1 << 8;//,"学科组长"),
        var EDUCATION = 1 << 9;//,"教育局"),
        var roles = "";
        if ((role & STUDENT) == STUDENT) {
            roles = "学生";
        } else if ((role & TEACHER) == TEACHER) {
            roles = "老师";
        } else if ((role & HEADMASTER) == HEADMASTER) {
            roles = "校领导";
        } else if ((role & EDUCATION) == EDUCATION) {
            roles = "教育局";
        } else {
            roles = "家长";
        }
        return roles;
    }

    /**
     * 显示左侧导航好友搜索等模块
     */
    function showFriendCircle()
    {
        $("#divCalendar").css("display","none");
        $("#friendcircle").css("display","block");
    }

    leftbar.leftBar=function()
    {
        recommendFriend(recommendRandom);
        showFriendCircle();
        leftbar.init();
    };
    function addFriend(friendId) {
        $.ajax({
            url: '/friendcircle/addFriendApply.do',
            type: 'post',
            dataType: "json",
            data: {

                userId: '${wbUserInfo.id}',
                friendId: friendId
            },
            success: function (result) {
                if (result) {
                    alert("好友申请已发出");
                    recommendFriend();
                } else {
                    alert("添加失败");
                }
            }
        });
    }
    function go2search(){
        var url='/friendcircle/friendSearch.do';
        var keywords=$("#keywords").val();
        keywords=encodeURI(encodeURI(keywords));
        url+='?keyWords='+keywords;
        window.location.href=url;
    }
    function go2href(url){
        window.location.href=url;
    }
    leftbar.init=function(){
        $("body").on("click","#searchIMg",function(){
            go2search();
        });
        $("body").on("keydown","#keywords",function(){
            if(event.keyCode==13){go2search();}
        });
        $("body").on("click","#activityInviteImg",function()
        {
            go2href('/message');
        });
        $("body").on("click","#myFriendImg",function()
        {
            go2href('/friendcircle/getFriendList.do?type=0');
        });
        $("body").on("click","#friendApplyImg",function()
        {
            go2href('/friendcircle/getFriendList.do?type=1');
        });
        $("body").on("click",".recommandFriend",function(){
            recommendFriend(-1);
        });
        $("body").on("click",".recommandFiendList .addFriend",function(){
            var fid=$(this).attr("fid");
            addFriend(fid);
        });
        $("body").on("click",".addFriendActivity",function(){
            var fid=$(this).attr("fid");
            addFriend(fid);
        });
    };
    module.exports=leftbar;
});
