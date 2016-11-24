/*
 * @Author: Tony
 * @Date:   2015-08-14 16:02:24
 * @Last Modified by:   Tony
 * @Last Modified time: 2015-08-14 17:13:56
 */

'use strict';
define(['doT','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var micromatch = {},
        Common = require('common');
    var micromatchData = {};
    var invitedFriendList = {data: []};
    var newinvitedFriendList = {data: []};

    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiaoyujv_start.init()
     */
    micromatch.init = function(){
        invitedFriendList = {data: []};
        if (GetQueryString("a")!="10000") {


        }
        micromatch.MicrolessonFileUpload();
        micromatch.MicroMatchAdress();
        $(".kaishi").click(function(event) {
            micromatch.AddMicroMatch();
        });
        $("#typesuer").click(function(event) {
            if ($.trim($('#classifyc').val()) =='') {
                alert("请输入分类。");
                return;
            }
            $('#mtype').append('<dd><em>'+$('#classifyc').val()+'</em><i onclick="deltype(this)"></i></dd>');
            $('#classifyc').val('');
            $(".gay,.adtp").fadeOut();
        // 插入分类
        });
        $("#typesuer2").click(function(event) {
            if ($.trim($('#classifyc2').val()) =='') {
                alert("请输入打分分类。");
                return;
            }
            if ($.trim($('#score').val()) =='') {
                alert("请输入最高分值。");
                return;
            }
            $('#stype').append('<dd><em>'+$('#classifyc2').val()+'</em><em>'+$('#score').val()+'</em><i onclick="deltype(this)"></i></dd>');
            $('#classifyc2').val('');
            $('#score').val('');
            $(".gay,.adtp2").fadeOut();
            // 插入分类
        });

        $("body").on('click', '.usreli', function () {
            var userId = $(this).attr("userId");
            var userName = $(this).attr("userName");
            invitedFriend(userId, userName);
        });

    };

    /**
     * 邀请好友
     * @param userId
     * @param userName
     * @param userImg
     */
    function invitedFriend(userId, userName) {
        for (var i = 0; i < invitedFriendList.data.length; i++) {
            if (invitedFriendList.data[i].id == userId) {
                invitedFriendList.data.splice(i, 1);
                initChoosedFriend();
                return;
            }
        }
        invitedFriendList.data.push({
            userName: userName,
            id: userId
        });
        initChoosedFriend();
    }

    /**
     * 初始化已经选择的好友
     */
    function initChoosedFriend() {
        var allFriend = "";
        for (var i = 0; i < invitedFriendList.data.length; i++) {
            allFriend += invitedFriendList.data[i].userName + ";";
        }
        if (allFriend != "")
            allFriend = allFriend.substring(0, allFriend.length - 1);
        $("#matchusers").val(allFriend);
    }

    /*添加评委的弹出层*/
    $(function() {
        var L=0;
        var T=0;
        var TL=0;
        var TT=0;
        L=$(window).width()/2-$(".hylb").width()/2;
        T=$(window).height()/2-$(".hylb").height()/2;
        TL=$(window).width()/2-$(".adtp").width()/2;
        TT=$(window).height()/2-$(".adtp").height()/2;
        $(".hylb").css({
            'left':L,
            'top':T
        });
        $(".adtp").css({
            'left':TL,
            'top':TT
        });
        $(".gay").height($(document).height());
        $(".tops span").click(function(event) {
            $('#matchusers').val('');
            invitedFriendList = {data: []};
            /* Act on the event */
            $(".gay,.hylb").fadeIn();

        });
        $(".hylb .gb").click(function(event) {
            /* Act on the event */
            $(".gay,.hylb").fadeOut();
        });

        /*下拉部分*/
        $(".bj").next("ol").css({
            "display": 'none'
        });
        $(".bj").click(function(event) {
            $(this).toggleClass('jb').next("ol").toggle();
        });
        /*下拉部分*/

        $(".hylb .gry").click(function(event) {
            /* Act on the event */
            $(".gay,.hylb").fadeOut();
        });

        $(".hylb .sureuser").click(function(event) {
            newinvitedFriendList = invitedFriendList;
            $('#roteuser').val($('#matchusers').val());
            /* Act on the event */
            $(".gay,.hylb").fadeOut();

        });

        $("#img-x").click(function(event) {
            /* Act on the event */
            document.getElementById("matchpic").src="";
            $("#img-x").hide();
            $("#matchpic").hide();
        });

        $("#addtype").click(function(event) {
            $(".gay,.adtp").fadeIn();
        });
        $("#scoretype").click(function(event) {
            $(".gay,.adtp2").fadeIn();
        });

        $(".tygry").click(function(event) {
            /* Act on the event */
            $(".gay,.adtp").fadeOut();
        });
        $(".tygry2").click(function(event) {
            /* Act on the event */
            $(".gay,.adtp2").fadeOut();
        });
    });
    /*添加评委的弹出层*/

    micromatch.AddMicroMatch = function() {

        if ($("#matchname").val()=='') {
            alert('请输入比赛名称！');
            return;
        }

        micromatchData.matchname=$("#matchname").val();
        var content = UE.getEditor('ueditor').getContent();
        if (content=='') {
            alert('请输入比赛说明！');
            return;
        }
        if ($("#matchpic").attr('src')=='') {
            alert('请上传比赛图片！');
            return;
        }
        micromatchData.content=content;
        micromatchData.path=$("#matchpic").attr('src');
        micromatchData.begintime=$("#begintime").val();
        micromatchData.endtime=$("#endtime").val();
        micromatchData.scorebegintime=$("#scorebegintime").val();
        micromatchData.scoreendtime=$("#scoreendtime").val();
        if ($("#begintime").val()==''|| $("#endtime").val()=='' || $("#scorebegintime").val()==''||$("#scoreendtime").val()=='') {
            alert('时间不能为空！');
            return;
        }

        if(micromatch.getTime($("#begintime").val())>=micromatch.getTime($("#endtime").val()))
        {
            alert("请正确选择参赛时间！");
            return;
        }
        if(micromatch.getTime($("#scorebegintime").val())>=micromatch.getTime($("#scoreendtime").val()))
        {
            alert("请正确选择打分时间！");
            return;
        }
        if(micromatch.getTime($("#scoreendtime").val())<micromatch.getTime($("#endtime").val()))
        {
            alert("打分结束时间不能早于参赛结束时间！");
            return;
        }

        var users='';
        if (newinvitedFriendList.data.length==0) {
            alert('评委不能为空！');
            return;
        }
        for (var i = 0; i < newinvitedFriendList.data.length; i++) {
            users += newinvitedFriendList.data[i].id + ',';
        }
        micromatchData.userlist=users;
        var type='';
        var ems = $("#mtype em");
        if(ems == '' || ems.length==0) {
            alert('分类不能为空！');
            return;
        }

        for (var i=0;i<ems.length;i++) {
            type += ems[i].innerHTML+',';
        }
        var stype='';
        var dds = $("#stype em");
        if (dds!=''&&dds.length!=0) {
            for (var i=0;i<dds.length/2;i++) {
                stype += dds[2*i].innerHTML+';'+dds[2*i+1].innerHTML+',';
            }
        }
        micromatchData.matchtypelist=type;
        micromatchData.scoretypelist=stype;
        Common.getPostData('/microlesson/addmicromatch.do', micromatchData,function(rep){
            if (rep.result) {
                if (GetQueryString("a")!="10000") {
                    location.href = '/microlesson/micropage.do';
                } else {
                    location.href = '/microlesson/micropage.do?a=10000';
                }

            } else {
                alert("新建失败！");
            }
        });
    }

    micromatch.getTime = function(s)
    {
        try
        {
            var str=s+":00";
            str=str.replace(/-/g,"/");
            var longValue=new Date(str).getTime();
            return longValue;
        }catch(x){

        }
        return -1;
    }

    /*
     * 上传附件信息
     * */
    micromatch.MicrolessonFileUpload = function(id) {
        /*
         * 点击附件按钮
         * */
        $('#image-upload').click(function(event){
            Common.fileUpload('#image-upload','/microlesson/addMatchPic.do','#picuploadLoading',function(e,response){
                var result = response.result;
                var rdata = typeof  result == 'string' ? $.parseJSON(result) : result[0] ? $.parseJSON(result[0].documentElement.innerText) : result;
                if (rdata.result) {
                    var url = rdata.path[0];
                    //$('#img-container ul').append('<li><a class="fancybox" href="' + url + '"data-fancybox-group="home" title="预览"><img class="micobolg-img" src="' + url +'?imageView/1/h/60/w/60'+ '"></a> <i class="ixh"></i></li>');
                    //$('#micoblog_content').attr('placeholder', '分享图片');
                    //$('.fancybox').fancybox();
                    $('#matchpic').show();
                    $('#img-x').show();
                    document.getElementById("matchpic").src=url;

                }
            });
        });
    }

    micromatch.MicroMatchAdress = function() {
        Common.getData('/microlesson/microMatchAdress.do', micromatchData,function(rep){
            $('.adress').html('');
            Common.render({tmpl: $('#adress_templ'), data: rep, context: '.adress'});

        });

    }
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }


    micromatch.init();
});