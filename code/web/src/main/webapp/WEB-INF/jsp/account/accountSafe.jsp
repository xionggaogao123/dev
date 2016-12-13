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
        <ul class="set-left">
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
        <div class="set-right right-1">
            <div class="tit">
                <span>基本资料</span>
            </div>
            <ul class="ul-infor">
                <li class="clearfix">
                    <div class="l1">用户名 :</div>
                    <div class="l2">shawn</div>
                </li>
                <li class="clearfix">
                    <div class="l1">昵称 :</div>
                    <div class="l2">
                        <input type="text" class="in1">
                    </div>
                </li>
                <li class="clearfix">
                    <div class="l1">性别 :</div>
                    <div class="l2">
                        <label><input type="radio" name="sex">男</label>
                        <label><input type="radio" name="sex">女</label>
                    </div>
                </li>
                <li class="clearfix">
                    <div class="l1">头像 :</div>
                    <div class="l2">
                        <div class="li-img">
                            <img src="">
                            <span class="edit-img">编辑头像</span>
                        </div>
                    </div>
                </li>
                <li class="clearfix">
                    <div class="l1">生日 :</div>
                    <div class="l2">
                        <select>
                            <option>2015</option>
                        </select>
                        年
                        <select>
                            <option>2015</option>
                        </select>
                        月
                        <select>
                            <option>2015</option>
                        </select>
                        日
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
        <div class="set-right right-2" hidden>
            <div class="tit">
                <span>扩展资料<em>增加你的扩展资料，将显示在你的个人名片中，并可以让更多的朋友了解和认识你</em></span>
            </div>
            <div class="bq-tit">兴趣爱好标签</div>
            <div class="clearfix bq-list">
                <span>旅行</span>
                <span>英语</span>
                <span>阅读</span>
                <span>足球</span>
                <span class="oracur2">篮球</span>
                <span>羽毛球</span>
                <span>网球</span>
                <span>乒乓球</span>
                <span>轮滑</span>
                <span>跆拳道</span>
                <span>旅行</span>
                <span class="oracur2">跳绳</span>
                <span>歌唱</span>
                <span>表演</span>
                <span>舞蹈</span>
                <span>美术</span>
                <span>钢琴</span>
                <span>其他运动</span>
                <span>古筝</span>
                <span>二胡</span>
                <span>小提琴</span>
                <span>笛子</span>
                <span>架子鼓</span>
                <span>围棋</span>
                <span>跳棋</span>
                <span>象棋</span>
                <span>桥牌</span>
                <span>演讲</span>
                <span>航海</span>
                <span>航模</span>
                <span>机器人</span>
                <span>爱宠</span>
                <span>旅行</span>
                <span>旅行</span>
            </div>
            <div class="bq-tit">休闲时间标签</div>

            <div class="clearfix bq-list">
                <span>周六08:00~11:00</span>
                <span class="oracur2">周六11:00~14:00</span>
                <span>周六14:00~17:00</span>
                <span class="oracur2">周日08:00~11:00</span>
                <span>周日11:00~14:00</span>
                <span>周日14:00~17:00</span>
            </div>
            <div class="h25"></div>
            <div class="">
                <button class="btn-save">保存</button>
                <button class="btn-no">取消</button>
            </div>
        </div>
        <div class="set-right right-3" hidden>
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
                    <span>shawn@qq.com</span>
                    <button class="btn-xg-email">修改</button>
                </li>
                <li>
                    <span>验证手机：</span>
                    <span>137****8745<em>未设置</em></span>
                    <button class="btn-xg-phone">修改</button>
                </li>
            </ul>
        </div>
        <div class="set-right right-4" hidden>
            <div class="tit">
                <span>关联第三方帐号<em>关联第三方帐号也可以登录</em></span>
            </div>
            <div>
                <div class="d-qq dq fl">
                    <img src="">
                    <span class="sp1"><!--class="sp2"时显示为已关联状态-->
                                <img class="img1" src="/static/images/account/white_qq.png">
                            </span>
                    <button class="btn1">立即关联</button>
                </div>
                <div class="d-qq dw fr">
                    <img src="">
                    <span class="sp2"><!--class="sp1"时显示为未关联状态-->
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
<div class="wind-psw windd">
    <p class="p1">修改登录密码<em>×</em></p>
    <p class="p2">
        <span class="sp1">原密码：</span>
        <span class="sp2">（请先验证原密码）</span>
        <span class="sp3"> 密码不正确！</span>
    </p>
    <input type="text" title="">
    <p class="p2">
        <span class="sp1">新密码：</span>
        <span class="sp2">（最小长度为6个字符）</span>
    </p>
    <input type="text" title="">
    <p class="p2">
        <span class="sp1">新密码：</span>
        <span class="sp2">（最小长度为6个字符）</span>
    </p>
    <input type="text" title="">
    <p class="p-btn-ok">确定</p>
</div>


<!-- 设置手机弹窗 -->
<div class="wind-phone windd">
    <p class="p1">设置手机号<em>×</em></p>
    <p class="p2">
        <span class="sp1">请输入手机号码：</span>
    </p>
    <input type="text" title="">
    <p class="p2">
        <span class="sp1">请输入验证码：</span>
    </p>
    <div class="d-yz">
        <input type="text" title="">
        <span>发送验证码</span>
    </div>
    <p class="p-btn-ok">确定</p>
</div>


<!-- 修改邮箱 -->
<div class="wind-email1 windd">
    <p class="p1">密码验证<em>×</em></p>
    <p class="p2">
        <span class="sp1">请输入网站登录密码以验证身份：</span>
        <span class="sp3"> 密码不正确！</span>
    </p>
    <input type="text" title="">
    <p class="p-btn-ok">确认</p>
</div>
<div class="wind-email2 windd">
    <p class="p1">修改邮箱验证<em>×</em></p>
    <p class="p2">
        <span class="sp1">请填写新邮箱地址：</span>
        <span class="sp3"> 邮箱格式不正确！</span>
    </p>
    <input type="text" title="">
    <p class="p-btn-ok">确认</p>
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