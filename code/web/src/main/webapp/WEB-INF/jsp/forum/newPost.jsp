<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>发帖</title>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <style>
        .lunt-index-hide {
            display: none;
        }
    </style>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/js/modules/forum/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/js/modules/forum/ueditor.all.js"></script>
    <script type="text/javascript" src="/static/vue/vue.min.js"></script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <%--<script type="text/javascript" charset="utf-8" src="/static/js/modules/forum/lang/zh-cn/zh-cn.js"></script>--%>
</head>
<body>
<%@ include file="../common/head.jsp" %>
<input id="pSectionId" type="hidden" value="${pSectionId}">
<input id="personId" type="hidden" value="${userId}">
<input id="commentContent" type="hidden" value="${comment}">
<input id="classifyContent" type="hidden" value="${classify}">
<input id="postTitleContent" type="hidden" value="${postTitle}">
<input id="postId" type="hidden" value="${postId}">
<div class="container">
    <p class="new-top-p" id="pSeDetail">
    </p>
    <div class="new-main">
        <div class="new-title">
            <span class="cur-fb span1">发表帖子</span>
            <span class="span2">发表投票</span>
            <span class="span3">发布悬赏</span>
        </div>
        <div class="vote" style="display: none">
            <p class="p1">
                选项标题:<input type="text" id="llo">
                <%--还可以输入<em>80</em>个字--%>
            </p>
            <p class="p2">选项：最多可填写10个选项</p>
            <div class="vote-item clearfix" id="voteItems">
                <div class="item-vote">
                    <p>最多可选<input type="text" class="in1" id="optionCount" value="1">项</p>
                    <%--<p>记票天数<input type="text" class="in1">天</p>--%>
                    <%--<p><label><input type="checkbox">投票后结果可见</label></p>--%>
                    <%--<p><label><input type="checkbox">公开投票参与人</label></p>--%>
                </div>
                <p class="p3">
                    <input type="text" class="voteI">
                    <em class="em2"></em>
                </p>
                <p class="p3">
                    <input type="text" class="voteI">
                    <em class="em2"></em>
                </p>
                <p class="p3">
                    <input type="text" class="voteI">
                    <em class="em2"></em>
                </p>
            </div>
            <button class="btn-new">增加一项</button>
        </div>
        <div class="reward">
            <p>悬赏价格：<input type="text" id="fscore">积分</p>
            <p>价格不能低于1，您有<em>${forumScore}</em>积分</p>
            <p>30天后如果您仍未设置最佳答案，版主有权带为您选择</p>
        </div>
        <p class="new-select">
            <select id="classify">
                <option value="0">选择主题分类</option>
                <c:if test="${userPermission >= 16384}">
                    <option value="1">官方公告</option>
                    <option value="2">精彩活动</option>
                </c:if>
                <option value="5">闲聊灌水</option>
                <option value="6">原创文章</option>
                <option value="7">其他</option>
            </select>
            <input id="postTitle">
            <span id="spanTitle" hidden>标题不能为空</span>
        </p>
        <div>
            <script id="editor" type="text/plain" name="content" style="width:1000px;height:500px;"></script>
            <script type="text/javascript">
                var ue = UE.getEditor('editor');
                UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
                UE.Editor.prototype.getActionUrl = function (action) {
                    if (action == 'uploadvideo') {
                        return '/forum/userCenter/uploadVideo.do';
                    } else if (action == 'uploadimage') {
                        return '/forum/userCenter/uploadImage.do';
                    } else {
                        return this._bkGetActionUrl.call(this, action);
                    }
                }
            </script>
            <span id="title" hidden>内容不能为空</span>
        </div>
        <input type="hidden" value="${forumScore}" id="forumScore">
        <div class="div-award">
            <div class="btn-award" id="btn">回帖奖励</div>
            <div class="award-cont" id="cont">
                <p class="pp1">
                    <span>每次回帖奖励：</span>
                    <input type="text" id="rewardScore" onchange="javascript:rewardScore(this)">
                    <span>积分，奖励：</span>
                    <input type="text" id="rewardCount" onchange="javascript:rewardCount(this)">
                    <span>次，每人最多可以获得</span>
                    <select id="sle">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        <option value="10">10</option>
                    </select>
                    次
                </p>
                <p class="pp2">
                    <span>总额：<em class="red-award"><span id="total">0</span></em> 积分，您有积分 <em
                            class="red-award">${forumScore}</em></span>
                </p>
            </div>
        </div>
        <p class="p-fj">
            <button id="comment">发表帖子</button>
            <%--本版积分规则--%>
        </p>
    </div>
</div>
<div class="monkey-hap" style="display: none">
    发帖成功！<br>获得积分<i>3</i>点经验值
    <span class="monkey-x"></span>
</div>
<div class="wind-postok">发帖成功！经验值<i>+3</i>，积分<i>+3</i></div>
<!--=============底部版权===============f==-->
<%@ include file="../common/footer.jsp" %>
<!--删除商品弹出框-->
<div class="bg"></div>

<script id="pTml" type="text/template">
    {{~it:value:index}}
    <em></em>
    <span>&nbsp;>&nbsp;<span style="cursor: pointer" onclick="window.open('/forum')">论坛</span>&nbsp;>&nbsp;<span
            style="cursor: pointer" onclick="window.open('/forum/postIndex.do?pSectionId={{=value.fSectionId}}')">{{=value.sectionName}}</span></span>
    {{~}}
</script>
<script type="text/javascript">
    var rewardFlag = false;
    var content;
    var postTitle;
    var plainText;
    var tu;
    var classifyV = 0;
    var voteStr = "";
    var ue = UE.getEditor('editor');
    var commentContent = $('#commentContent').val();
    var classifyContent = $('#classifyContent').val();
    UE.getEditor('editor').ready(function () {
        //this是当前创建的编辑器实例
        content = UE.getEditor('editor').getContent();
        if (commentContent) {
            UE.getEditor('editor').setContent(commentContent);
        }
    })

    function reward() {
        rewardFlag = !rewardFlag;
    }
    $(function () {

        $('.new-title .span1').click(function () {
            $(this).addClass('cur-fb').siblings('.new-title span').removeClass('cur-fb');
            $('.vote').hide();
            $('.reward').hide();
            $('#llo').attr('llo', "");
            $('.div-award').show();
        });
        $('.new-title .span2').click(function () {
            $(this).addClass('cur-fb').siblings('.new-title span').removeClass('cur-fb');
            $('.vote').show();
            $('.reward').hide();
            $('#llo').attr('llo', "llo");
            $('.div-award').hide();
        });
        $('.new-title .span3').click(function () {
            $(this).addClass('cur-fb').siblings('.new-title span').removeClass('cur-fb');
            $('.vote').hide();
            $('.reward').show();
            $('#llo').attr('llo', "xsx");
            $('.div-award').hide();
        });
        $('body').on('click', '.vote-item .p3 .em2', function () {
//          $(this).parent().hide();
            $(this).parent().remove();
        });
        $('.btn-award').click(function () {
            $('.award-cont').toggleClass('award-show');
            $(this).toggleClass('award-bw');
            reward();
        });
        $('.vote .btn-new').click(function () {
            var test = document.getElementById("voteItems"),
                    ele = test.firstChild,
                    count = 0;

            while (ele) {
                if (ele.nodeType == 1 && ele.nodeName == "P") {
                    count++;
                }
                ele = ele.nextSibling;
            }
            if (count >= 10) {
                alert("最多10项");
                return;
            }
            var item = '<p class="p3"><input type="text" class="voteI"><em class="em2"></em></p>';
            $('.vote-item').append(item);
        });

        $('#classify').change(function () {

            classifyV = $(this).val();
            if (classifyV == 6) {
                $(".div-award").hide();
                return;
            }
            $(".div-award").show();
        });

        $('#sle').change(function () {
            $(this).prev().prev().prev().prev().removeAttr('disabled');
            $(this).prev().prev().removeAttr('disabled');
            var v = $(this).prev().prev().prev().prev().val();
            if (v == undefined || v == "") {
                alert("请先输入每次回帖奖励!");
                $(this).val(1);
                return;
            }
            var vo = $(this).prev().prev().val();
            if (vo == undefined || vo == "") {
                alert("请先奖励次数！");
                $(this).val(1);
                return;
            }
            var t = $(this).val();
            if (parseInt(t) > parseInt(vo)) {
                $(this).val(vo);
            }
        });

        var pSectionId = $('#pSectionId').val();
        var personId = $('#personId').val();
        var postId = $('#postId').val();

        var postTitleContent = $('#postTitleContent').val();

        if (postId) {
            $('#postTitle').val(postTitleContent);
            $("#classify").val(classifyContent);
            classifyV = classifyContent;
        } else {
            postId = "";
        }
        $('#comment').click(function () {
            if (validate()) {
                var requestData = {};
                requestData.classify = classifyV;
                requestData.comment = content;
                requestData.postSectionId = pSectionId;
                requestData.postTitle = postTitle;
                requestData.plainText = plainText;
                requestData.draught = 1;
                requestData.postId = postId;
                requestData.imageStr = "";
                requestData.videoStr = "";
                if ($('#llo').attr('llo') == "llo") {
                    if ($('#llo').val() == "") {
                        alert("选项标题不能为空");
                        return;
                    }
                    requestData.voteContent = $('#llo').val();
                    if (!vote()) {
                        return;
                    }
                    var optionCount = $('#optionCount').val();
                    if (optionCount == "" || optionCount == undefined) {
                        alert("不能为空!");
                        return;
                    }
                    if (isNaN(optionCount)) {
                        alert("请输入数值!");
                        return;
                    }
                    requestData.voteSelect = voteStr;
                    requestData.optionCount = optionCount;
                    $.ajax({
                        type: "post",
                        data: requestData,
                        url: '/forum/addVote.do',
                        async: false,
                        dataType: "json",
                        traditional: true,
                        success: function (result) {
                            $('#spanTitle').hide();
                            $('#title').hide();
                            redC();
                            $('.wind-postok').fadeIn(2000, function () {
                                location.href = '/forum/postIndex.do?pSectionId=' + pSectionId + "&ti=" + new Date().getTime();
                            });
                        }
                    });
                } else if ($('#llo').attr('llo') == "xsx") {
                    var offeredScore = $('#fscore').val();
                    if (offeredScore == "" || offeredScore == undefined) {
                        alert("悬赏积分不能为空！");
                        return;
                    }
                    if (isNaN(offeredScore)) {
                        alert("请输入数值!");
                        $('#fscore').val("");
                        return;
                    }

                    if(parseInt(offeredScore)<=0){
                        alert("悬赏积分不能小于0!");
                        $('#fscore').val("");
                        return;
                    }


                    requestData.offeredScore = offeredScore;
                    $.ajax({
                        type: "post",
                        data: requestData,
                        url: '/forum/addOfferedPost.do',
                        async: false,
                        dataType: "json",
                        traditional: true,
                        success: function (result) {
                            $('#spanTitle').hide();
                            $('#title').hide();
                            redC();
                            $('.wind-postok').fadeIn(2000, function () {
                                location.href = '/forum/postIndex.do?pSectionId=' + pSectionId + "&ti=" + new Date().getTime();
                            });
                        }
                    });

                } else {
                    $('.wind-postok').fadeIn();
                    $('.bg').fadeIn();
                    if (rewardFlag) {
                        if ($('#rewardScore').val() == "" || $('#rewardScore').val() == undefined) {
                            alert("请填写每次奖励积分！");
                            return;
                        }
                        if ($('#rewardCount').val() == "" || $('#rewardCount').val() == undefined) {
                            alert("请填写奖励次数！");
                            return;
                        }
                        requestData.rewardScore = $('#rewardScore').val();
                        requestData.rewardCount = $('#rewardCount').val();
                        requestData.rewardMax = $('#sle option:selected').val();
                        requestData.type = 3;
                        $.ajax({
                            type: "post",
                            data: requestData,
                            url: '/forum/addFRewardPost.do',
                            async: false,
                            dataType: "json",
                            traditional: true,
                            success: function (result) {
                                $('#spanTitle').hide();
                                $('#title').hide();
                                redC();
                                $('.wind-postok').fadeIn(2000, function () {
                                    location.href = '/forum/postIndex.do?pSectionId=' + pSectionId + "&ti=" + new Date().getTime();
                                });
                            }
                        });
                    } else {
                        $.ajax({
                            type: "post",
                            data: requestData,
                            url: '/forum/addFPost.do',
                            async: false,
                            dataType: "json",
                            traditional: true,
                            success: function (result) {
                                $('#spanTitle').hide();
                                $('#title').hide();
                                redC();
                                $('.wind-postok').fadeIn(2000, function () {
                                    location.href = '/forum/postIndex.do?pSectionId=' + pSectionId + "&ti=" + new Date().getTime();
                                });
                            }
                        });
                    }
                }
            }

        });

    });
    function rewardCount(obj) {
        var vo = $(obj).prev().prev().val();
        if (vo == undefined || vo == "") {
            alert("请先输入每次回帖奖励");
            $(obj).val("");
            return;
        }
        var v = $(obj).val();
        if (v == undefined) {
            alert("输入不能为空！");
            return;
        }
        if (isNaN(v)) {
            alert("请输入数字！");
            $(obj).val("");
            return;
        }
        var t = parseInt(v) * parseInt(vo);
        var forumScore = parseInt($('#forumScore').val());
        if (t > forumScore) {
            alert("输入总数大于总积分");
            var temp = forumScore % parseInt(vo);
            var k;
            if (temp == 0) {
                k = Math.ceil(forumScore / parseInt(vo));
            } else {
                k = Math.ceil(forumScore / parseInt(vo)) - 1;
            }
            $(obj).val(k);
            var total = k * parseInt(vo);
            $("#total").html(total);
        } else {
            $("#total").html(t);
        }
    }

    function rewardScore(obj) {
        var vl = $(obj).next().next().val();
        if (vl != "" || vl != undefined) {
            $(obj).next().next().val("");
        }
        var v = $(obj).val();
        if (v == undefined) {
            alert("输入不能为空！");
            return;
        }
        if (isNaN(v)) {
            alert("请输入数字！");
            $(obj).val("");
            return;
        }

        var forumScore = parseInt($('#forumScore').val());
        if (parseInt(v) > forumScore) {
            alert("输入不能大于所拥有的积分");
            $(obj).val("1");
        }


    }
    function redC() {
        $('.bg').fadeIn();
    }
    function vote() {
        $('.voteI').each(function () {
            var item = $(this).val();
            if (item != "") {
                if (voteStr == "") {
                    voteStr = item;
                } else {
                    voteStr = voteStr + "," + item;
                }
            }
        });
        if (voteStr == "" || voteStr == ",") {
            alert("选项不能为空");
            return false;
        }
        return true;
    }
    function validate() {
        postTitle = $('#postTitle').val();
        if (postTitle == "") {
            $('#spanTitle').show();
            return false;
        }
        if (UE.getEditor('editor').getContent() == "") {
            $('#spanTitle').hide();
            $('#title').show();
            return false;
        }
        content = UE.getEditor('editor').getContent();
        plainText = UE.getEditor('editor').getContentTxt();
        return true;
    }
    //实例化编辑器
    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例

</script>
<!--============登录================-->
<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/newPost.js');
</script>
</body>
</html>
