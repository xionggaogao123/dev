<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>复兰教育社区--${postTitle}</title>
    <!-- css -->
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <link href="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet"
          type="text/css" media="screen">
    <link rel="stylesheet" type="text/css" href="/static/js/modules/forum/third-party/video-js/video-js.min.css"/>
    <link rel="stylesheet" href="/static/dist/mpreview/css/MPreview.css">
    <link rel="stylesheet" type="text/css" href="/static/dist/musicplayer/APlayer.min.css">
    <link rel="stylesheet" type="text/css" href="/static/css/postDetail.css"/>
    <!--script -->
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-browser.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/jquery.qqFace.js"></script>
    <script src="http://static.polyv.net/file/polyvplayer_v2.0.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/js/modules/forum/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/js/modules/forum/ueditor.all.js"></script>
    <script type="text/javascript" charset="utf-8" src="/static/js/modules/forum/ueditor.parse.js"></script>
    <script type="text/javascript" src="/static/js/swfobject.js"></script>
    <script type="text/javascript" src="/static/js/recorder.js"></script>
    <script type="text/javascript" src="/static/js/main.js"></script>
    <script type="text/javascript" src="/static/js/template.js"></script>

    <script type="text/javascript" src="/static/js/modules/forum/third-party/video-js/video.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/third-party/html5media.min.js"></script>

    <script>
        window.onload = function () {
            var oTop = document.getElementById("to_top");
            var screenw = document.documentElement.clientWidth || document.body.clientWidth;
            var screenh = document.documentElement.clientHeight || document.body.clientHeight;
            oTop.style.left = screenw - oTop.offsetWidth + "px";
            oTop.style.top = screenh - oTop.offsetHeight + "px";
            window.onscroll = function () {
                var scrolltop = document.documentElement.scrollTop || document.body.scrollTop;
                oTop.style.top = screenh - oTop.offsetHeight + scrolltop + "px";
            }
            oTop.onclick = function () {
                document.documentElement.scrollTop = document.body.scrollTop = 0;
            }
        }
    </script>
</head>
<body style="background: #f7f7f7">
<input id="pSectionId" type="hidden" value="${pSectionId}">
<input id="postId" type="hidden" value="${postId}">
<input id="personId" type="hidden" value="${personId}">
<input id="userId" type="hidden" value="${userId}">
<input id="pageN" type="hidden" value="${page}">
<input id="floor" type="hidden" value="${floor}">
<input id="timeText" type="hidden" value="${timeText}">
<input id="InSet" type="hidden" value="${InSet}">
<input id="cate" type="hidden" value="${cate}">
<input id="sortType" type="hidden" value="${sortType}">
<input id="reward" type="hidden" value="${reward}">
<input id="drt" type="hidden" value="${drt}">
<input id="sol" type="hidden">
<input id="kkl" type="hidden" value="${sol}">

<%@ include file="../common/head.jsp" %>

<div class="wind-circle" style="display: none;z-index: 99999999">
    <p>提示</p>
    <div class="cir-f">
        <em class="windciri"></em>
        <i>处理中...</i>
    </div>
</div>
<div class="wind-num" style="display: none;z-index: 99999999">
    <p class="p1">温馨提示<em id="cancel">×</em></p>
    <p class="p2">
        尊敬的参赛用户：<br>为了获奖时方便与您联系，请输入手机号码<br><input type="text" id="phone">
    </p>
    <p class="p3">
        <button class="btn-ok">确认</button>
        <button class="btn-no">取消</button>
    </p>
</div>

<span id="pageS" hidden>分享页面</span>

<div class="container" style="position: relative;">
    <div class="post-back-top">
        <div class="d1" id="to_top">
            <em class="em11"></em>
            <span>返回顶部</span>
        </div>
        <div class="d1" onclick="llEvent()">
            <em class="em12"></em>
            <span id="pinlun">我要评论</span>
        </div>
    </div>
    <script type="text/javascript">
        function llEvent() {
            var h = $(document).height() - $(window).height();
            $(document).scrollTop(h);
        }
    </script>
    <p class="postdetail-top-p" id="pSeDetail">
    </p>
    <p class="clearfix">
        <span class="detail-new" id="expressTheme">发表新主题</span>
        <span class="detail-recall" id="ssl" onclick="Event()">&nbsp;&nbsp;&nbsp;回复</span>
    </p>
    <div class="height10"></div>
    <div class="bordere5"></div>
    <div class="post-fa clearfix">
        <div class="post-left fl" id="postDetailData">

        </div>
        <div class="post-right" id="postRight">

        </div>
    </div>
    <div id="sel-type">
        <div class="s1" id="skl"><img src="/static/images/forum/forunm_time.png"> 最新参赛作品排序</div>
        <div class="s2" id="sll"><img src="/static/images/forum/zan_shi.png">受欢迎热度排序</div>
    </div>
    <div id="listReply">
    </div>

    <div class="new-page-links"></div>
    <div class="div-nofound1" id="notFound">
        <h3>抱歉，没有找到与<em>"${param.regular}"</em>相关的回帖</h3>
        <p>建议您：</p>
        <p>1、查看是否回帖</p>
        <p>2、条件查询查询不到帖子</p>
    </div>
    <div class="post-fa clearfix">

        <div class="area-fb" id="areahh">发表评论区</div>
        <div class="post-left fl">
            <div class="post-headpic">
                <img onclick="window.open('/forum/userCenter/user.do')" style="cursor: pointer"
                <c:if test="${avatar ==null||avatar =='http://7xiclj.com1.z0.glb.clouddn.com/'}"> width="120"
                     height="120" src="/static/images/forum/lt_top_banner.png"</c:if>
                <c:if test="${avatar !=null&&avatar !='http://7xiclj.com1.z0.glb.clouddn.com/'}"> src="${avatar}"
                     </c:if>width="120" height="120">
            </div>
        </div>
        <div class="post-right">
            <div class="recall-notallow" <c:if test="${login==true}">style="display: none;"</c:if>> 您需要登录后才可以回帖<a
                    id="ddd">&nbsp;&nbsp;登陆</a> | <a onclick="window.open('/account/register.do')">注册</a></div>
            <script id="editor" type="text/plain" name="content" style="width:100%;height:200px;"></script>
            <script type="text/javascript">
                UE.getEditor('editor', {
                    toolbars: [
                        ['source',//源码
                            'bold',
                            'snapscreen', //截图
                            'italic', //斜体
                            'underline', //下划线
                            'strikethrough', //删除线
                            'subscript', //下标
                            'fontborder', //字符边框
                            'superscript', //上标
                            'formatmatch',
                            'emotion', //表情
                            'fontfamily', //字体
                            'fontsize', //字号
                            'paragraph', //段落格式
                            'spechars', //特殊字符
                            'justifyleft', //居左对齐
                            'justifyright', //居右对齐
                            'justifycenter', //居中对齐
                            'justifyjustify', //两端对齐
                            'link', //超链接
                            'unlink', //取消链接
                            'scrawl', //涂鸦
                            'simpleupload', //单图上传
                            'insertimage', //多图上传
                            'insertvideo', //视频
                            'attachment' //附件
                        ]],
                    autoHeightEnabled: true,
                    autoFloatEnabled: true,
                    allowDivTransToP: false
                });
                UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
                UE.Editor.prototype.getActionUrl = function (action) {
                    if (action == 'uploadvideo') {
                        return '/forum/userCenter/uploadVideo.do';
                    } else if (action == 'uploadimage') {
                        return '/forum/userCenter/uploadImage.do';
                    } else if (action == 'uploadfile') {
                        po
                        return '/forum/userCenter/uploadFile.do';
                    } else {
                        return this._bkGetActionUrl.call(this, action);
                    }
                }
            </script>
            <div class="join-cont" <c:if test="${InSet!=1||login==false}">style="display: none"</c:if>>
                <div class="sp1">创建参赛者</div>
                <div class="sp2">参赛者</div>
                <div class="sp3">
                    <%--bord--%>
                    <p class="p1"></p>
                    <p class="p2" id="participateList">
                    </p>
                </div>
                <div class="sp4" style="display: none">有没有要选择的参赛者?参赛者可作为选填项</div>
                <%--<div class="sp5">*此信息仅用作官方联系参赛者使用，不对外公开。</div>--%>
            </div>
            <button class="btn-hf" id="comment">发表回复</button>
            <span id="title" style="display: none">回复的帖子不能为空，请重新回复！</span>
        </div>
    </div>
    <div class="post-wind">
        <h2>提示<em>×</em></h2>
        <div class="quest">
            <p class="qu-p1"></p>
            <p class="qu-p2"></p>
        </div>
        <div class="btn-qud" id="btnConfirm">确定</div>
        <div class="btn-qux" id="btnCancel">取消</div>
    </div>
    <div class="wind-recall">
        <div class="wind-recall-s">
            <div id="recall">
            </div>
            <div class="recall-cont">
                <script id="myEditor" type="text/plain" name="content" style="width:100%;height:100px;"></script>
            </div>
            <div class="btn-recall-f">
                <button id="postClick">参与/回复主题</button>
            </div>
        </div>
    </div>
</div>
<%--举报弹窗--%>
<div class="wind-jb">
    <p class="p1">举报<em>×</em></p>
    <p>请选择举报理由:</p>
    <label><input type="radio" class="i1" name="reason-jb" value="广告垃圾">广告垃圾</label>
    <label><input type="radio" class="i1" name="reason-jb" value="违规内容">违规内容</label>
    <label><input type="radio" class="i1" name="reason-jb" value="恶意灌水">恶意灌水</label>
    <label><input type="radio" class="i1" name="reason-jb" value="重复发帖">重复发帖</label>
    <label><input type="radio" class="i2" name="reason-jb" value="其他">其他</label>
    <textarea placeholder="您可以输入150字..." id="ggg"></textarea>
    <button class="btn-jb-o">确认</button>
    <button class="btn-jb-x">取消</button>
</div>
<!--=============底部版权=================-->
<%@ include file="../common/footer.jsp" %>
<%@ include file="../common/login.jsp" %>

<div class="bg"></div>
<%--打伤弹窗--%>
<div class="wind-ds" id="windBox">
    <p>积分打赏<em>×</em></p>
    <div class="div-jf">
        <span class="spa1">超过最大积分值，请重新输入</span>
        <span class="spa2">您有<i id="formScore">${formScore}</i>点积分</span>
        <span class="spa3">积分：</span>
        <input type="text" id="inputScore">
    </div>
    <div class="div-btn-jf">
        <button class="btn-dss">打赏</button>
        <button class="btn-qxs">取消</button>
    </div>
</div>

<div class="wind-ds" id="goodBox">
    <p>Good Boy<em>×</em></p>
    <div class="div-jf">
        <input type="text" id="input">
    </div>
    <div class="div-btn-jf">
        <button id="goodButton">确定</button>
    </div>
</div>

<%--发送消息弹窗--%>
<div class="add-wind sendInf" style="display: none">
    <p class="p1">发送消息<em>×</em></p>
    <img src="/static/images/forum/single_dog.png">
    <p class="p2">给<em><span id="aa"></span></em>发送消息，消息：</p>
    <input type="text" id="content">
    <p class="p3">(消息内容不能为空)</p>
    <div class="clearfix1"></div>
    <button id="confirmSend">确认</button>
</div>

<div class="wind-postok">回帖成功！经验值<i>+1</i>，积分<i>+1</i></div>
<div class="wind-postokl">回帖成功！额外奖赏积分<i>+${rewardScore}</i></div>

<script type="text/javascript">
    function Event() {
        var h = $(document).height() - $(window).height();
        $(document).scrollTop(h);
    }
    function voteCheck(obj) {
        var voteCount = $('#voteCount').attr("value");
        var voteLength = $("input[type='checkbox']:checked").length;
        if (voteCount == voteLength) {
            $("input[type='checkbox']").not("input:checked").attr('disabled', 'disabled');
        } else {
            $("input[type='checkbox']").removeAttr('disabled');
        }
    }
</script>

<script type="text/javascript">
    var content;
    var plainText;
    var pSectionId = $('#pSectionId').val();
    var postId = $('#postId').val();
    var personId = $('#personId').val();
    UE.getEditor('editor');
    UE.getEditor('editor').ready(function () {
        //this是当前创建的编辑器实例
        content = UE.getEditor('editor').getContent();
    });

    $(function () {
        $('#comment').click(function () {
            if($('#InSet').val()==1){
                var count = $('#participateList').data("count");
                if (count == 0) {
                    var login=${login};
                    if(login) {
                        if (validate()) {
                            $('.wind-join').fadeIn();
                            $('.bg').fadeIn();
                            $('.wind-join .btn1').hide();
                            $('.wind-join .btn2').hide();
                            $('.wind-join .btn3').show();
                            $('.wind-join .btn4').show();
                        } else {
                            $('#title').show();
                        }
                    }else{
                        $('.store-register').fadeToggle();
                        $('.bg').fadeToggle();
                    }
                } else {
                    if ($('.join-cont .p1').hasClass('bord')) {
                        goToPost("");
                    } else {
                        if (undefined != $('#comment').data('participateId') &&
                                null != $('#comment').data('participateId') &&
                                "" != $('#comment').data('participateId')) {
                            $('.join-cont .sp4').hide();
                            goToPost($('#comment').data('participateId'));
                        } else {
                            $('.join-cont .p1').addClass('bord');
                            $('.join-cont .sp4').show();
                        }
                    }

                }
            }else{
                goToPost("");
            }

        });

        $('body').on('click', '.wind-join .btn-cancel', function () {
            initJoin();
            $('.wind-join').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '.wind-join .btn4', function () {
            initJoin();
            $('.wind-join').fadeOut();
            $('.bg').fadeOut();
            goToPost("");
        });

        $('body').on('click', '.wind-join .p1 em', function () {
            initJoin();
            $('#comment').removeData('remove');
            $('.wind-join').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '.wind-join .btn3', function () {
            var $name = $('#participateName');
            var $age = $('#participateAge');
            var $relation = $('#participateRelation');
            var $sex = $("[name='sex']:checked");
            var $school = $('#participateSchool');

            if ($.trim($name.val()) == "") {
                $name.css("border", "1px solid #f00");
                $name.next('.nameClass').html('姓名不能为空');
                $name.next('.nameClass').show();
                return;
            } else {
                if ($name.val().length > 5) {
                    $name.css("border", "1px solid #f00");
                    $name.next('.nameClass').html('姓名长度不要超过5个字');
                    $name.next('.nameClass').show();
                }
            }

            if ($age.val() != "") {
                if (isNaN($age.val())) {
                    $('.ageClass').html("填写的年龄必须是数字");
                    $('.ageClass').show();
                    return;
                } else {
                    if (parseInt($age.val()) <= 0) {
                        $('.ageClass').html("填写的年龄不能小于或等于0");
                        $('.ageClass').show();
                        return;
                    }
                }
            }
            if ($.trim($relation.val()) == "") {
//                $name.css("border","1px solid #E6E6E6");
                $relation.css("border", "1px solid #f00");
                $relation.next('.relationRegular').html("联系方式不能为空");
                $relation.next('.relationRegular').show();
                return;
            }
            var phonePattern = /(^(([0+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
            var emailPattern = /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
            if (phonePattern.test($relation.val()) || emailPattern.test($relation.val())) {
            } else {
                $relation.css("border", "1px solid #f00");
                $relation.next('.relationRegular').html("请填写符合规范的邮箱或者手机号");
                $relation.next('.relationRegular').show();
                return;
            }
            var sex = -1;
            if (undefined != sex) {
                sex = $sex.val();
            }
            var joinId = $('.wind-join').data("joinId");
            var param = {
                id: joinId,
                name: $name.val(),
                relation: $relation.val(),
                sex: sex,
                age: $age.val(),
                school: $school.val()
            };
            $.ajax({
                type: "GET",
                data: param,
                url: "/forum/saveParticipator.do",
                async: true,
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (rep) {
                    if (rep.code == "200") {
                        $('#dataLoad').show();
                        goToPost(rep.message);
                    } else {
                        alert(rep.message);
                    }
                }
            });
        });

        $('body').on('blur', '#participateName', function () {
            if ($.trim($('#participateName').val()) != "") {
                $('#participateName').css("border", "1px solid #E6E6E6");
                $('#participateName').next('.nameClass').hide();
                if ($('#participateName').val().length > 5) {
                    $('#participateName').next('.nameClass').html('姓名长度不要超过5个字');
                    $('#participateName').next('.nameClass').show();
                }
            } else {
                $('#participateName').next('.nameClass').html('姓名不能为空');
                $('#participateName').next('.nameClass').show();
            }
        });

        $('body').on('blur', '#participateRelation', function () {
            if ($.trim($('#participateRelation').val())!= "") {
                var phonePattern = /(^(([0+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$)|(^0{0,1}1[3|4|5|6|7|8|9][0-9]{9}$)/;
                var emailPattern = /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
                if (phonePattern.test($('#participateRelation').val()) || emailPattern.test($('#participateRelation').val())) {
                    $('#participateRelation').css("border", "1px solid #E6E6E6");
                    $('#participateRelation').next('.relationRegular').hide();
                }
            }
        });

        $('body').on('blur', '#participateAge', function () {
            if ($('#participateAge').val() == "") {
                $('.ageClass').hide();
            } else {
                if (isNaN($('#participateAge').val())) {
                    $('.ageClass').html("填写的年龄必须是数字");
                    $('.ageClass').show();
                } else {
                    if (parseInt($('#participateAge').val()) <= 0) {
                        $('.ageClass').html("填写的年龄不能小于或等于0");
                        $('.ageClass').show();
                    } else {
                        $('.ageClass').hide();
                    }
                }
            }
        });


        $('body').on('click', '.selectPart', function (e) {
            var id = $(this).attr('participateId');
            var name = $(this).attr('participateName');
            $('.join-cont .p1').html(name);
            $('#comment').data('participateId', id);
            if ($('.join-cont .p1').hasClass('bord')) {
                $('.join-cont .p1').removeClass('bord');
            }
        })

        $('body').on('click', '.join-cont .sp1', function () {
            $('#title').hide();
            $('.wind-join').data("joinId", "");
            $('.wind-join').fadeIn();
            $('.wind-join .btn1').html('保存');
            $('.wind-join .btn-cancel').html('取消');
            $('.wind-join .btn1').show();
            $('.wind-join .btn2').show();
            $('.wind-join .btn3').hide();
            $('.wind-join .btn4').hide();
            $('.bg').fadeIn();
        });

        $('body').on('click', '.join-cont .sp3 .p2 .i2', function (e) {
            var id = $(this).attr('participateId');
            var obj = $('#participateList').data(id);
            $('.wind-join').data("joinId", id);
            $('#participateName').val(obj.name);
            $('#participateRelation').val(obj.relation);
            $('#participateSchool').val(obj.school);
            if (obj.age != 0) {
                $('#participateAge').val(obj.age);
            }
            if (obj.sex != -1) {
                $(":radio[name='sex'][value='" + obj.sex + "']").prop("checked", "checked");
            }
            $('.wind-join').fadeIn();
            $('.bg').fadeIn();
            e.stopPropagation();
        });

    });


    function initJoin() {
        $('#participateName').css("border", "1px solid #E6E6E6");
        $('#participateName').next('.nameClass').hide();
        $('#participateRelation').css("border", "1px solid #E6E6E6");
        $('#participateRelation').next('.relationRegular').hide();
        $('.ageClass').hide();
        $("[name='sex']:checked").attr("checked", false);
        $('#participateSchool').val('');
        $('#participateName').val('');
        $('#participateRelation').val('');
        $('#participateAge').val('');

    }
    function validate() {
        if (UE.getEditor('editor').getContent() == "") {
            return false;
        }
        content = UE.getEditor('editor').getContent();
        plainText = UE.getEditor('editor').getContentTxt();
        return true;
    }

    function validatePhoneNumber() {
        $.ajax({
            url: "/user/isBindPhone.do",
            type: 'get',
            success: function (resp) {
                if (!resp.message.isBind) {
                    $(".bg").fadeIn();
                    $(".wind-num").fadeIn();
                    return;
                }
                comment('/forum/addFReply.do', "");
            }
        });
    }

    function addUserPhone() {
        var mobile = $("#phone").val();
        $.ajax({
            url: "/user/addUserPhone.do",
            type: 'get',
            data: {
                mobile: mobile
            },
            success: function (resp) {
                if (resp.message.bind) {
                    $('.bg').fadeOut();
                    $('.wind-num').fadeOut();
                    comment('/forum/addFReply.do', "");
                } else {
                    alert(resp.message);
                }
            }
        });
    }

    function getParam(participateId) {
        var requestData = {};
        requestData.comment = content;
        requestData.postSectionId = pSectionId;
        requestData.postId = postId;
        requestData.plainText = plainText;
        requestData.postFlagId = 1;
        requestData.postReplyId = postId;
        requestData.personId = personId;
        requestData.nickName = "";
        requestData.content = "";
        requestData.imageStr = "";
        requestData.videoStr = "";
        requestData.audioStr = "";
        if (participateId != "") {
            requestData.participateId = participateId;
        }
        return requestData;
    }

    function comment(url, participateId) {
        $(".bg").fadeIn();

        var requestData = getParam(participateId);

        $.ajax({
            type: "post",
            data: requestData,
            url: url,
            async: false,
            dataType: "json",
            traditional: true,
            success: function (result) {
                if (result.message == "sss") {
                    location.href = "/forum/forumSilenced.do";
                } else {
                    $('#title').hide();
                    var pageCount = $('#pageN').val();
                    if (pageCount) {
                    } else {
                        pageCount = 1;
                    }
                    if ($('#InSet').val() == 1 || ($('#InSet').val() == -1 && $('#cate').val() == 1)) {
                        pageCount = 1;
                    }
                    redC();
//                    $(".wind-circle").fadeOut();

                    $('.wind-postok').fadeIn(2000, function () {
                        $(".bg").fadeOut();
                        location.href = '/forum/postDetail.do?pSectionId=' + pSectionId + '&postId=' + postId + '&personId=' + personId + '&page=' + pageCount + "&ti=" + new Date().getTime();
                    });
                }
            }
        });
    }

    $('.vjs-tech').parent().addClass('vjs-has-started');

    function redC() {
        $('.bg').fadeIn();
        var reward = $('#reward').val();
        if (reward) {
            if (reward == 1) {
                $('.wind-postokl').fadeIn();
            }
        }
    }

    function goToPost(participateId) {
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (!flag) {
                    $('.store-register').fadeToggle();
                    $('.bg').fadeToggle();
                    return;
                }

                if (!validate()) {
                    $('#title').show();
                    return;
                }

                comment('/forum/addFReplyForParticipate.do', participateId);
            }
        });
    }
</script>

<script id="pTml" type="text/template">
    {{~it:value:index}}
    <span>&nbsp;>&nbsp;<span id="cope" style="cursor: pointer" onclick="window.open('/forum')">
        论坛
    </span>
    <span id="shl" style="cursor: pointer"
          onclick="window.open('/forum/postIndex.do?pSectionId={{=value.fSectionId}}')">
        &nbsp;>&nbsp;{{=value.sectionName}}
    </span>
    </span>
    {{~}}
</script>

<script id="participateListTmpl" type="text/template">
    {{~it:value:index}}
    <span><em class="selectPart" participateId="{{=value.id}}" participateName="{{=value.name}}">{{=value.name}}</em>
        <i class="i1" participateId="{{=value.id}}" participateName="{{=value.name}}"></i><i class="i2"
                                                                                             participateId="{{=value.id}}"></i></span>
    {{~}}
</script>

<div id="YCourse_player" class="player-container" style="display: none">
    <div id="player_div" class="player-div"></div>
    <div id="sewise-div"
         style="display: none; width: 630px; height: 360px; max-width: 800px;">
        <script type="text/javascript"
                src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>

        <span class="player-close-btn"></span>
        <script type="text/javascript">
            SewisePlayer.setup({
                server: "vod",
                type: "m3u8",
                skin: "vodFlowPlayer",
                logo: "none",
                lang: "zh_CN",
                topbardisplay: 'enable',
                videourl: ''
            });
        </script>
    </div>
</div>

<script type="text/javascript">

    var isFlash = false;
    function getVideoType(url) {
        if (url.indexOf('polyv.net') > -1) {
            return "POLYV";
        }
        if (url.indexOf('.swf') > 0) {
            return 'FLASH';
        }
        return 'HLS';
    }

    function playerReady(name) {
        if (isFlash) {
            SewisePlayer.toPlay(playerReady.videoURL, "视频", 0, false);
        }
    }

    $('.player-close-btn').click(function () {
        $('#YCourse_player').hide();
        $(".bg").hide();
    });

    function download(url) {
        location.href = "/forum/userCenter/m3u8ToMp4DownLoad.do?filePath=" + url;
    }
    function tryPlayYCourse(url) {
        $("#YCourse_player").show();
        $(".player-close-btn").css({
            "display": 'block'
        });
        var videoSourceType = getVideoType(url);
        $('.bg').fadeIn('fast');
        var $player_container = $("#YCourse_player");
        $player_container.fadeIn();

        if (videoSourceType == "POLYV") {
            $('#sewise-div').hide();
            $('#player_div').show();
            var player = polyvObject('#player_div').videoPlayer({
                'width': '800',
                'height': '450',
                'vid': url.match(/.+(?=\.swf)/)[0].replace(/.+\//, '')
            });
        } else {
            $('#player_div').hide();
            $('#sewise-div').show();
            try {
                SewisePlayer.toPlay(url, "视频", 0, true);
            } catch (e) {
                playerReady.videoURL = url;
                isFlash = true;
            }
        }
    }
</script>

<script id="postRightTml" type="text/template">
    {{~it:value:index}}
    <p class="clearfix post-info">
        <span class="post-title" id="poT">{{=value.postTitle}}</span>
        <span class="fr ddj delzt" id="deletePost" style="display: none">删除该主题</span>
        <span class="fr ddj" id="editPost" style="display: none">编辑</span>
        <span class="fr ddj" id="topSet" style="display: none">置顶</span>
        <span class="fr ddj" id="creamSet" style="display: none">精华</span>
        <span class="fr ddj" id="topNoSet" style="display: none">取消置顶</span>
        <span class="fr ddj" id="creamNoSet" style="display: none">取消精华</span>
        <span class="fr ddj" id="downloadLike" style="display: none">点赞数据</span>
        <span class="fr ddj" id="downloadLikeZip" style="display: none">批量导出</span>
    </p>
    <div class="height10"></div>
    <p class="post-info">
        <img class="fl" src='/static/images/forum/post_page_holder.png'>
        <span class="fl">&nbsp;发表于&nbsp;</span><em class="fl">{{=value.time}}天前</em>&nbsp;
        <select class="fl" id="fpostSearch">
            <option value="1">查看全部</option>
            <option value="2">只看楼主</option>
        </select>

        <button class="fr flbtn" id="through">确定</button>
        <img src="/static/images/forum/post_page_louti.png" class="fr">
        <span class="fr psotinfor-right">楼主&nbsp;&nbsp;电梯直达<input type="text" id="floorll"></span>
    </p>
    <div class="post-cont kl">
        {{?value.voteType=="Type"}}
        <div class="div-tp">
            <p>{{=value.voteContent}}</p>
            <span id="voteCount" value="{{=value.voteOptionCount}}" hidden></span>
            {{~value.voteOptions:option:i}}
            <p>
                <label>
                    {{?value.voteOptionCount==1}}
                    <input class="checkVote" type="radio" name="radio-s" value="{{=i+1}}">{{=i+1}}.{{=option}}
                    {{?}}
                    {{?value.voteOptionCount!=1}}
                    <input class="checkVote" type="checkbox" onchange="voteCheck(this)" name="radio-s" value="{{=i+1}}">{{=i+1}}.{{=option}}
                    {{?}}
                </label>
            </p>
            {{~ }}
            <button class="btn-sub">提交</button>
        </div>
        {{?}}
        {{?value.voteType=="voteType"}}
        <div class="div-tp">
            <p>{{=value.voteContent}}(投票人数<span>{{=value.voteCount}}</span>)</p>
            <ul>
                {{~value.voteMapList:option:i}}
                <li>{{=i+1}}.{{=option.voteItemStr}}</li>
                <li class="li2">
                    <div class="jindu" style="width: {{=option.voteItemPercent}};"></div>
                    <span>{{=option.voteItemPercent}}<i>（{{=option.voteItemCount}}）</i></span>
                </li>
                {{~ }}
            </ul>
        </div>
        {{?}}
        {{?value.type==1}}
        <div class="clearfix div-xs-tit">
            {{?value.offerCompleted==1}}
            <img src="/static/images/forum/reward_already.png">
            {{?}}
            <span class="sp11">{{=value.plainText}}<br><button onclick="Event()">我来回答</button></span>
        </div>
        {{?}}
        {{?value.offerCompleted==1}}
        <p class="best-an">最佳答案</p>
        <div class="best-cont">
            <div class="best-peo">
                {{?value.personImage!=null&&value.imageStr !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
                <img src="{{=value.personImage}}">
                {{?}}
                {{?value.personImage==null||value.personImage =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
                <img src="/static/images/forum/head_picture1.png">
                {{?}}
                <span class="sp1">{{=value.solNickName}}</span>
                <span class="sp2" id="rec" onclick="location.href='/forum/redirectFloor.do?Id={{=value.solutionId}}'">查看完整答案</span>
            </div>
            <p class="best-p">{{=value.solution}}</p>
        </div>
        {{?}}
        {{?value.comment!=null&&value.comment!=""}}
        {{?value.version!=null&&value.version!=""}}
        {{=value.version}}
        {{?}}
        {{?value.version==null||value.version==""}}
        {{=value.content}}
        {{?}}
        {{?}}

        {{?value.comment==null||value.comment==""}}
        {{~value.videoList:video:i}}
        <div class="content-DV" <c:if test="${userPermission==1}">ondblclick="download('{{=video.videoUrl}}')" </c:if>>
            <img class="content-img content-Im   videoshow2" vurl="{{=video.videoUrl}}"
                 src="{{=video.imageUrl}}">
            <img src="/static/images/play.png" class="video-play-btn"
                 onclick="tryPlayYCourse('{{=video.videoUrl}}')">
        </div>
        {{~ }}

        <div>
            {{=value.plainText}}
        </div>

        {{~value.imageList:images:i}}
        <a class="fancybox" href="{{=images}}" data-fancybox-group="home" title="预览">
            <img src="{{=images}}?imageView/1/h/80/w/80"><br/>
        </a>
        {{~ }}
        {{?}}

        {{? value.InSet == 1 || (value.InSet == -1 && value.cate == 1) }}

        <div class="hdphb">
            <span>活动排行榜</span>
        </div>
        <ul class="zan-list clearfix" id="zan-listt">
            {{~value.fReplyDTOList:fReplyDTO:i}}
            <li>
                {{?fReplyDTO.remove!=1}}
                {{?fReplyDTO.imageSrc!=null&&fReplyDTO.imageSrc !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
                <img src="{{=fReplyDTO.imageSrc}}"
                     onclick="location.href='/forum/redirectFloor.do?Id={{=fReplyDTO.fReplyId}}'" width="10"
                     height="10">
                {{?}}
                {{?fReplyDTO.imageSrc==null||fReplyDTO.imageSrc =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
                <img src="/static/images/forum/head_picture1.png"
                     onclick="location.href='/forum/redirectFloor.do?Id={{=fReplyDTO.fReplyId}}'" width="10"
                     height="10">
                {{?}}
                <span class="span1" title="{{=fReplyDTO.replyNickName}}">{{=fReplyDTO.replyNickName}}</span>
                <span class="span2"><img src="/static/images/forum/orange_zan.png">{{=fReplyDTO.praiseCount}}</span>
                {{?}}
            </li>
            {{~ }}
            {{?value.fReplyDTOCount==1}}
            <li class="li2">...</li>
            {{?}}
            <li class="li-diand li-po-ab">...</li>
            <li>
                <button class="show-more-zan zanc1">全部</button>
            </li>
        </ul>
        {{?value.IsLogin==1}}
        <div>我自己的点赞数：{{=value.praiseCount}}</div>
        {{?}}
        {{?}}
        <div class="post-zan clearfix">
            <div class="social socialShare">
                <a class='msb_main'>
                    <img title='分享' src='/static/images/forum/share_core_square.jpg'></a>
                <div class="social_group">
                    <a target='_blank' class="msb_network_button friendQQ">qq</a>
                    <a target='_blank' class='msb_network_button weixin'>weixin</a>
                    <a target='_blank' class='msb_network_button sina'>sina</a>
                    <a target='_blank' class='msb_network_button weixinFriend'>weiFriend</a>
                    <a target='_blank' class='msb_network_button qZone'>qZone</a>
                    <a target='_blank' class='msb_network_button douban'>douban</a>
                </div>
            </div>
            <button class="btn-sc" id="coll">收藏({{=value.collectionCount}})</button>
            <div class="btn-zan" id="zan" zan="true"><img src="/static/images/forum/zan_shi.png">&nbsp;&nbsp;&nbsp;&nbsp;<span
                    id="dds">({{=value.zan}})</span></div>
            <button class="btn-ds">打赏</button>
        </div>
    </div>

    <div class="recall-main {{=value.postId}}" id="recallOne" style="display: none">
        <p class="p1">
            <em class="post-hf em1">回复</em>
            <em class="post-hf em2">收起回复</em>
            <em class="post-jb">举报</em>
        </p>
        <div class="recall-s s1">
            <textarea class="s1t" id="saytext3" name="saytext3"></textarea>
            <p class="p2 clearfix">
                <button class="btn-fb" value="{{=value.postId}}">发表</button>
            </p>
        </div>
        <div class="recall-sf" style="display: none;">
            <div class="recall-s" id="recallOnel">
            </div>
            <div class="recall-s s1">
                <textarea class="s1t" name="saytext2"></textarea>
                <p class="p2 clearfix">
                    <button class="btn-fb" value="{{=value.postId}}">发表</button>
                </p>
            </div>
        </div>
    </div>


    <div class="recall-main {{=value.postId}}" id="recallTwo" style="display: none">
        <p class="p1">
            <em class="post-hf em5">收起回复</em>
            <em class="post-hf em6" style="display: none">回复</em>
            <em class="post-jb">举报</em>
        </p>
        <div class="recall-sf">
            <div class="recall-s" id="recallls">
            </div>
            <div class="recall-s s1">
                <textarea class="s1t" id="saytext2" name="saytext2"></textarea>
                <p class="p2 clearfix">
                    <button class="btn-fb" value="{{=value.postId}}">发表</button>
                </p>
            </div>
        </div>
    </div>
    {{~}}
</script>

<script type="text/template" id="lsReplies">
    {{~it:value:index}}
    {{?index<=4}}
    <div class="lol clearfix">
        {{?value.imageStr!=null&&value.imageStr !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="{{=value.imageStr}}" width="30" height="30">
        {{?}}
        {{?value.imageStr==null||value.imageStr =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="/static/images/forum/head_picture1.png" width="30" height="30">
        {{?}}
        <p class="p1"><em>{{=value.nickName}}&nbsp;:</em>
            {{=value.content}}
            <span class="fr ddj delpl deleteLOL" replyId="{{=value.id}}" rpid="{{=value.rpid}}" style="display: none">删除该评论</span>
        </p>
        <p class="p2 ReplyPost">{{=value.timeText}}<em class="ssfd" value="{{=value.nickName}}">回复</em></p>
    </div>
    {{?}}
    {{?index>4}}
    <div class="lol clearfix llp">
        {{?value.imageStr!=null&&value.imageStr !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="{{=value.imageStr}}" width="30" height="30">
        {{?}}
        {{?value.imageStr==null||value.imageStr =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="/static/images/forum/head_picture1.png" width="30" height="30">
        {{?}}
        <p class="p1"><em>{{=value.nickName}}&nbsp;:</em>
            {{=value.content}}
            <span class="fr ddj delpl deleteLOL" replyId="{{=value.id}}" rpid="{{=value.rpid}}" style="display: none">删除该评论</span>
        </p>
        <p class="p2 ReplyPost">{{=value.timeText}}<em class="ssfd" value="{{=value.nickName}}">回复</em></p>
    </div>
    {{?}}
    {{~}}
    <p class="p2"><span></span><span class="search" style="cursor: pointer">查看更多</span><span
            style="display: none;cursor: pointer" class="soQi">收起</span>&nbsp;&nbsp;&nbsp;
        <button class="btn-want selfWant">我要发言</button>
    </p>
</script>

<script type="text/template" id="postRepliesTml">
    {{~it:value:index}}
    {{?index<=4}}
    <div class="lol clearfix">
        {{?value.imageStr!=null&&value.imageStr !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="{{=value.imageStr}}" width="30" height="30">
        {{?}}
        {{?value.imageStr==null||value.imageStr =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="/static/images/forum/head_picture1.png" width="30" height="30">
        {{?}}
        <p class="p1"><em>{{=value.nickName}}&nbsp;:</em>
            {{=value.content}}
            <span class="fr ddj delpl deleteLOL" replyId="{{=value.id}}" rpid="{{=value.rpid}}" style="display: none">删除该评论</span>
        </p>
        <p class="p2 PostReply">{{=value.timeText}}<em class="ssfd" value="{{=value.nickName}}">回复</em></p>
    </div>
    {{?}}
    {{?index>4}}
    <div class="lol clearfix llp">
        {{?value.imageStr!=null&&value.imageStr !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="{{=value.imageStr}}" width="30" height="30">
        {{?}}
        {{?value.imageStr==null||value.imageStr =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="/static/images/forum/head_picture1.png" width="30" height="30">
        {{?}}
        <p class="p1"><em>{{=value.nickName}}&nbsp;:</em>
            {{=value.content}}
            <span class="fr ddj delpl deleteLOL" replyId="{{=value.id}}" rpid="{{=value.rpid}}" style="display: none">删除该评论</span>
        </p>
        <p class="p2 PostReply">{{=value.timeText}}<em class="ssfd" value="{{=value.nickName}}">回复</em></p>
    </div>
    {{?}}
    {{~}}
    <p class="p2"><span></span><span id="search" style="cursor: pointer">查看更多</span><span
            style="display: none;cursor: pointer" id="soQi">收起</span>&nbsp;&nbsp;&nbsp;
        <button class="btn-want" id="selfWant">我要发言</button>
    </p>
</script>


<script type="text/template" id="replyTml">
    {{~it:value:index}}
    <p class="p1">
        查看:<em>{{=value.scanCount}}</em>&nbsp;|&nbsp;回复:<em>{{=value.replyCount}}</em>
    </p>
    <div id="{{=value.personId}}" value="{{=value.personId}}">

    </div>
    {{~}}
</script>

<script type="text/template" id="pUserTml">
    {{~it:value:index}}
    <p class="p2">{{=value.personName}}</p>
    <div class="post-headpic">
        {{?value.avt!=null}}
        <img onclick="window.open('/forum/personal.do?personId={{=value.personId}}')" src="{{=value.avt}}" width="50"
             height="50">
        {{?}}
        {{?value.avt==null}}
        <img onclick="window.open('/forum/personal.do?personId={{=value.personId}}')"
             src="/static/images/forum/head_picture1.png" width="50" height="50">
        {{?}}
    </div>
    <p class="p3">
        主题 <span style="color: #fd2b2b">{{=value.tc}}</span>&nbsp;&nbsp;|&nbsp;&nbsp;回帖 <span style="color: #fd2b2b">{{=value.rc}}</span>
    </p>
    <p class="p31">
        {{?value.stars==1}}
        <img src="/static/images/forum/level_tong.png">
        {{?}}
        {{?value.stars==2}}
        <img src="/static/images/forum/level_tong.png">
        <img src="/static/images/forum/level_tong.png">
        {{?}}
        {{?value.stars==3}}
        <img src="/static/images/forum/level_sliver.png">
        {{?}}
        {{?value.stars==4}}
        <img src="/static/images/forum/level_sliver.png">
        <img src="/static/images/forum/level_tong.png">
        {{?}}
        {{?value.stars==5}}
        <img src="/static/images/forum/level_sliver.png">
        <img src="/static/images/forum/level_tong.png">
        <img src="/static/images/forum/level_tong.png">
        {{?}}
        {{?value.stars==6}}
        <img src="/static/images/forum/level_sliver.png">
        <img src="/static/images/forum/level_sliver.png">
        {{?}}
        {{?value.stars==7}}
        <img src="/static/images/forum/level_sliver.png">
        <img src="/static/images/forum/level_sliver.png">
        <img src="/static/images/forum/level_tong.png">
        {{?}}
        {{?value.stars==8}}
        <img src="/static/images/forum/level_sliver.png">
        <img src="/static/images/forum/level_sliver.png">
        <img src="/static/images/forum/level_tong.png">
        <img src="/static/images/forum/level_tong.png">
        {{?}}
        {{?value.stars==9}}
        <img src="/static/images/forum/level_golden.png">
        {{?}}
        {{?value.stars==10}}
        <img src="/static/images/forum/level_golden.png">
        <img src="/static/images/forum/level_tong.png">
        {{?}}
        {{?value.stars==11}}
        <img src="/static/images/forum/level_golden.png">
        <img src="/static/images/forum/level_tong.png">
        <img src="/static/images/forum/level_tong.png">
        {{?}}
        {{?value.stars==12}}
        <img src="/static/images/forum/level_golden.png">
        <img src="/static/images/forum/level_sliver.png">
        {{?}}
    </p>
    <p class="p3">Lv.{{=value.stars}}</p>
    <p class="p3">积分&nbsp;&nbsp;{{=value.score}}</p>
    <p class="p4"><span class="xs" value="{{=value.personId}}" personName="{{=value.personName}}">发消息</span></p>
    {{~}}
</script>

<script type="text/template" id="pReplyListTml">
    {{~it:value:index}}
    <div class="post-fa clearfix divReply" id="{{=value.timeText}}">
        <div class="post-left fl reply-left" personId="{{=value.personId}}"></div>
        <div class="post-right">
            <p class="post-info">
                <img class="fl" src='/static/images/forum/post_page_per.png'>
                <span class="fl">&nbsp;发表于&nbsp;</span><em class="fl">{{=value.time}}天前</em>&nbsp;
                <select class="fl replyPerson">
                    <option value="0">选择查看</option>
                    <option value="{{=value.personId}}">只看该作者</option>
                    <option value="1">查看全部</option>
                </select>
                {{?value.floor!=0}}
                <span class="fr fl-ceng">{{=value.floor}}层</span>
                {{?}}
                <span class="fr ddj delpl {{=value.personId}} deleteBox" id="deleteReply" value="{{=value.fReplyId}}"
                      style="display: none">删除该评论</span>
                <span class="fr ddj delpl good" value="{{=value.fReplyId}}" style="display: none">好孩子</span>
                <span class="fr ddj delpl downloadAttach" value="{{=value.fReplyId}}" style="display: none">下载附件</span>
            </p>
            <div class="post-cont ">
                <div class="btn-best" <c:if
                        test="${userId==''||sol==1||dead!=1||personId!=userId}"> style="display:none" </c:if>>
                    <button class="ppl" sol="{{=value.fReplyId}}">最佳答案</button>
                </div>

                <div class="post-bt-hide">

                    {{?value.pdf!=null&&value.pdf!=""}}
                    <a href="#">
                        <img src="/static/images/forum/paf-rt.png" class="img-pdf-rt"
                             url="{{=value.pdf}}" title="全局预览">
                    </a>
                    {{?}}

                    <div style="margin-top: 37px">
                        <div class="jianbian"></div>
                        {{?value.replyComment!=null&&value.replyComment!=""}}
                        {{?value.version!=null&&value.version!=""}}
                        {{=value.version}}
                        {{?}}
                        {{?value.version==null||value.version==""}}
                        {{=value.replyComment}}
                        {{?}}
                        {{?}}

                        {{~value.audioList:audio:i}}
                        <div class="aplayer" url="{{=audio}}" id="player1"></div>
                        {{~}}

                        {{?value.replyComment==null||value.replyComment==""}}
                        {{~value.videoList:video:i}}
                        <div class="content-DV"
                             <c:if test="${userPermission==1}">ondblclick="download('{{=video.videoUrl}}')" </c:if>>
                            <img class="content-img content-Im videoshow2" vurl="{{=video.videoUrl}}"
                                 src="{{=video.imageUrl}}">
                            <img src="/static/images/play.png" class="video-play-btn"
                                 onclick="tryPlayYCourse('{{=video.videoUrl}}')">
                        </div>
                        {{~}}

                        <div>
                            {{=value.plainText}}
                        </div>

                        {{~value.imageList:images:i}}
                        <a class="fancybox" href="{{=images}}" data-fancybox-group="home" title="预览">
                            <img class="content-Im" src="{{=images}}?imageView/1/h/80/w/80">
                            <br/>
                        </a>
                        {{~}}
                        {{?}}
                    </div>
                </div>
                <div class="div-zksq">
                    <button class="btn-zk">展开</button>
                    <button class="btn-sq">收起</button>
                </div>

                <div class="post-zan1 clearfix">
                    <div class="social socialShare">
                        <a class='msb_main'>
                            <img title='分享' src='/static/images/forum/share_core_square.jpg'></a>
                        <div class="social_group">
                            <a target='_blank' class="msb_network_button friendQQ" ti="{{=value.timeText}}">qq</a>
                            <a target='_blank' class='msb_network_button weixin' ti="{{=value.timeText}}">weixin</a>
                            <a target='_blank' class='msb_network_button sina' ti="{{=value.timeText}}">sina</a>
                            <a target='_blank' class='msb_network_button weixinFriend' ti="{{=value.timeText}}">weiFriend</a>
                            <a target='_blank' class='msb_network_button qZone' ti="{{=value.timeText}}">qZone</a>
                            <a target='_blank' class='msb_network_button douban' ti="{{=value.timeText}}">douban</a>
                        </div>
                    </div>
                    <div class="btn-zan1 zanReply" value="{{=value.fReplyId}}"><img
                            src="/static/images/forum/zan_shi.png">
                        <span>({{=value.praiseCount}})</span>
                    </div>
                </div>
            </div>
            {{if(value.repliesFlag==0){ }}
            <div class="recall-main {{=value.fReplyId}}">
                <p class="p1">
                    <em class="post-hf em1">回复</em>
                    <em class="post-hf em2">收起回复</em>
                </p>
                <div class="recall-s s1">
                    <textarea class="s1t" id="saytext" name="saytext"></textarea>
                    <p class="p2 clearfix">
                        <button class="btn-fb" value="{{=value.fReplyId}}">发表</button>
                    </p>
                </div>
                <div class="recall-sf" style="display: none">
                    <div class="recall-s">
                    </div>
                    <div class="recall-s s1">
                        <textarea class="s1t" name="saytext1"></textarea>
                        <p class="p2 clearfix">
                            <button class="btn-fb" value="{{=value.fReplyId}}">发表</button>
                        </p>
                    </div>
                </div>
            </div>
            {{}}}
            {{if(value.repliesFlag==1){ }}
            <div class="recall-main {{=value.fReplyId}}">
                <p class="p1">
                    <em class="post-hf em3">收起回复</em>
                    <em class="post-hf em4">回复</em>
                </p>
                <div class="recall-sf">
                    <div class="recall-s" id="{{=value.fReplyId}}">
                    </div>
                    <div class="recall-s s1">
                        <textarea class="s1t" id="saytext1" name="saytext1"></textarea>
                        <p class="p2 clearfix">
                            <button class="btn-fb" value="{{=value.fReplyId}}">发表</button>
                        </p>
                    </div>
                </div>
            </div>
            {{}}}
        </div>
    </div>
    <div class="wind-besta" style="display: none;z-index: 99999999">
        <p class="p1">提示<em>×</em></p>
        <p class="p2">您确认要将该回复选为<br>最佳答案？</p>
        <p class="p3">
            <button id="llk">确认</button>
            <button id="llj">取消</button>
        </p>
    </div>
    {{~}}
</script>

<div id="dataLoad" style="display:none"><!--页面载入显示-->
    <table width=100% height=100% border=0 align=center valign=middle>
        <tr height=50%><td align=center>&nbsp;</td></tr>
        <tr><td align=center><img src="/static/images/forum/forumLoading.png"/></td></tr>
        <tr><td align=center>数据正在发表中，请稍后......</td></tr>
        <tr height=50%><td align=center>&nbsp;</td></tr>
    </table>
</div>
<div class="wind-join">
    <p class="p1">参赛信息<em>×</em></p>
    <p class="p2">*此信息仅用作官方联系参赛者使用，不对外公开。</p>
    <ul>
        <li>
            <span>姓名</span>
            <input class="inp1" type="text" placeholder="真实姓名" id="participateName">
            <i class="nameClass" style="display: none">姓名不能为空</i>
        </li>
        <li>
            <span>年龄</span>
            <input class="inp2" type="text" placeholder="年龄" id="participateAge">
            <em>性别</em>
            <label>
                <input type="radio" name="sex" value="1">男
            </label>
            <label>
                <input type="radio" name="sex" value="0">女
            </label>
            <i class="ageClass" style="display: none">填写的年龄必须是数字</i>
        </li>
        <li>
            <span>联系方式</span>
            <input class="inp1" type="text" placeholder="常用邮箱或手机号" id="participateRelation">
            <i class="relationRegular" style="display: none">请填写符合规范的邮箱或者手机号</i>
        </li>
        <li>
            <span>就读学校</span>
            <input class="inp1" type="text" placeholder="例：上海市普陀区第一中心小学" id="participateSchool">
        </li>
        <li>
            <button class="btn1">保存</button>
            <button class="btn2 btn-cancel">取消</button>
            <button class="btn3" style="display: none">保存并发表</button>
            <button class="btn4" style="display: none">跳过填写,并发表</button>
        </li>
    </ul>
</div>

<script src="/static/dist/pdfobject/pdfobject.min.js"></script>
<script src="/static/dist/musicplayer/APlayer.min.js"></script>

<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<script>
    seajs.use('/static/js/modules/forum/postDetail.js', function (postDetail) {
        postDetail.init();
    });
</script>
<script type="text/javascript" src="/static/js/modules/core/0.1.0/fancyBox/jquery.fancybox.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $(".fancybox").fancybox({});

    })
</script>
</body>
</html>
