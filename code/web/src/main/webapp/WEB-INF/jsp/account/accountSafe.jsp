<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>账户安全</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/account/setCenter.css">
</head>
<body>

<jsp:include page="../common/head.jsp"/>

<div class="set-container">
    <div class="set-tit">账户中心</div>
    <div>
        <ul class="set-left-l">
            <li class="li444">
                <div>
                    <div class="d-img im1"></div>
                    <div class="d-txt">基本资料</div>
                </div>
            </li>
            <li>
                <div>
                    <div class="d-img im2"></div>
                    <div class="d-txt">扩展资料</div>
                </div>
            </li>
            <li>
                <div>
                    <div class="d-img im3"></div>
                    <div class="d-txt">账户安全</div>
                </div>
            </li>
            <li>
                <div>
                    <div class="d-img im4"></div>
                    <div class="d-txt">关联第三方帐号</div>
                </div>
            </li>
        </ul>
        <div class="set-right-r right-1">
            <div class="tit">
                <span>基本资料</span>
            </div>
            <ul class="ul-infor">
                <li class="clearfix">
                    <div class="l1">用户名 :</div>
                    <div class="l2 username"></div>
                </li>
                <li class="clearfix">
                    <div class="l1">昵称 :</div>
                    <div class="l2">
                        <input type="text" class="in1 nickname">
                    </div>
                </li>
                <li class="clearfix">
                    <div class="l1">性别 :</div>
                    <div class="l2 sex-div">
                        <label><input type="radio" name="sex" value="1">男</label>
                        <label><input type="radio" name="sex" value="0">女</label>
                        <label><input type="radio" name="sex" value="-1">保密</label>
                    </div>
                </li>
                <li class="clearfix">
                    <div class="l1">头像 :</div>
                    <div class="l2">
                        <div class="li-img">
                            <img src="" class="avatar">
                            <span class="edit-img">编辑头像</span>
                        </div>
                    </div>
                </li>
                <li class="clearfix">
                    <div class="l1">生日 :</div>
                    <div class="l2">
                        <select name="year" class="sel_year" id="sel_year" rel="2016" required></select> 年
                        <select name="month" class="sel_month" id="sel_month" rel="2" required></select> 月
                        <select name="day" class="sel_day" id="sel_day" rel="14" required></select> 日
                        <span class="sp2">（出生年份为保密）</span>
                    </div>
                </li>
                <li class="clearfix">
                    <div class="l1"></div>
                    <div class="l2">
                        <button>保存</button>
                    </div>
                </li>
            </ul>
        </div>
        <div class="set-right-r right-2" hidden>
            <div class="tit">
                <span>扩展资料<em>增加你的扩展资料，将显示在你的个人名片中，并可以让更多的朋友了解和认识你</em></span>
            </div>
            <div class="bq-tit">兴趣爱好标签</div>
            <div class="clearfix bq-list bq-tags">
            </div>
            <div class="bq-tit">休闲时间标签</div>

            <div class="clearfix bq-list bq-times">
            </div>
            <div class="h25"></div>
            <div class="">
                <button class="btn-save">保存</button>
                <button class="btn-no">取消</button>
            </div>
        </div>
        <div class="set-right-r right-3" hidden>
            <div class="tit">
                <span>账户安全<em>请尽早完善账户安全信息，更好地保护您的数据安全！</em></span>
            </div>
            <ul class="ul-safe">
                <li>
                    <span>登录密码：</span>
                    <span>已设置</span>
                    <button class="btn-xg-psw">修改</button>
                </li>
                <li>
                    <span>验证邮箱：</span>
                    <span id="verify-email"></span>
                    <button class="btn-xg-email" hidden>修改</button>
                </li>
                <li>
                    <span>验证手机：</span>
                    <span id="verify-phone"></span>
                    <button class="btn-xg-phone" hidden>修改</button>
                </li>
            </ul>
        </div>
        <div class="set-right-r right-4" hidden>
            <div class="tit">
                <span>关联第三方帐号<em>关联第三方帐号也可以登录</em></span>
            </div>
            <div>
                <div class="d-qq dq fl third-qq">
                    <img src="" class="avatar">
                    <span class="sp1">
                                <img class="img1" src="/static/images/account/white_qq.png">
                            </span>
                    <button class="btn1">立即关联</button>
                </div>
                <div class="d-qq dw fr third-wechat">
                    <img src="" class="avatar">
                    <span class="sp2">
                                <img class="img2" src="/static/images/account/white_wechat.png">
                            </span>
                    <button class="btn2">解除关联</button>
                </div>
            </div>
        </div>
    </div>

</div>

<div class="bg"></div>
<!-- 修改密码弹窗 -->
<div class="wind-psw windd edit-pass">
    <p class="p1">修改登录密码<em>×</em></p>
    <p class="p2">
        <span class="sp1">原密码：</span>
        <span class="sp2">（请先验证原密码）</span>
        <span class="sp3 verify-pass" style="display: none"> 密码不正确！</span>
    </p>
    <input type="password" title="" class="password">
    <p class="p2">
        <span class="sp1">新密码：</span>
        <span class="sp2">（最小长度为6个字符）</span>
        <span class="sp3 verify-pass-n" style="display: none"> 密码格式不正确！</span>
    </p>
    <input type="password" title="" class="n-password">
    <p class="p2">
        <span class="sp1">确认新密码：</span>
        <span class="sp2">（最小长度为6个字符）</span>
        <span class="sp3 verify-pass-r" style="display: none"> 两次密码不一致！</span>
    </p>
    <input type="password" title="" class="n-r-password">
    <p class="p-btn-ok">确定</p>
</div>


<!-- 设置手机弹窗 -->
<div class="wind-phone windd">
    <p class="p1">设置手机号<em>×</em></p>
    <p class="p2">
        <span class="sp1">请输入手机号码：</span>
        <span class="sp3 phone-tip" style="display: none"> </span>
    </p>
    <input type="text" title="" id="phone">
    <p class="p2">
        <span class="sp1">图片验证码：</span>
    </p>
    <input type="text" style="display: inline;width: 178px" id="verifyCode">
    <img id="imgObj" alt="" src="/verify/verifyCode.do"/>
    <p class="p2">
        <span class="sp1">请输入验证码：</span>
    </p>
    <div class="d-yz">
        <input type="text" title="" class="code" id="code">
        <span id="sendText">发送验证码</span>
    </div>
    <p class="p-btn-ok">确定</p>
</div>


<!-- 修改邮箱 -->
<div class="wind-email1 windd" id="verify-wind">
    <p class="p1">密码验证<em>×</em></p>
    <p class="p2">
        <span class="sp1">请输入网站登录密码以验证身份：</span>
        <span class="sp3" style="display: none;"> 密码不正确！</span>
    </p>
    <input type="password" title="" class="password">
    <p class="p-btn-ok">确认</p>
</div>

<div class="wind-email2 windd">
    <p class="p1">修改邮箱验证<em>×</em></p>
    <p class="p2">
        <span class="sp1">请填写新邮箱地址：</span>
        <span class="sp3" style="display:none;"> 邮箱格式不正确！</span>
    </p>
    <input type="text" title="" class="email-input">
    <p class="p-btn-ok">确认</p>
</div>

<div class="windd" id="isRemoveBind">
    <p class="p1" style="text-align: center">是否确认解绑<em>×</em></p>
     <p class="p2">注意：若您的账号是新建的将会被清空</p>
    <p class="p-btn-ok" style="margin-top: 50px;margin-bottom: 20px">确认</p>
</div>

<%@include file="../common/footer.jsp" %>

</body>

<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/account/accountSafe.js', function (accountSafe) {
        accountSafe.init();
    });
</script>
</html>