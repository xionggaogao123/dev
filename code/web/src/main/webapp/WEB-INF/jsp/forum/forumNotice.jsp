<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>通知中心</title>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <%--<script type="text/javascript" src="/static/js/modules/forum/forumnotice.js"></script>--%>
    <style type="text/css">
        .header-cont .ha4 {
            background: #E8E8E8;
            color: #3764a0;
        }
    </style>
</head>
<body login="${login}" menuItem="${menuItem}" userId="${userId}">
<%@ include file="../common/head.jsp" %>
<div class="container">
    <div class="set-left">
        <h2>通知</h2>
        <ul class="ul-forumset">
            <li class="li1 li-cur" value="1"><img src="/static/images/forum/img_noticenotice.png">我的消息</li>
            <li class="li2" value="2"><img src="/static/images/forum/img_noticemypost.png">我的帖子</li>
            <li class="li3" value="3"><img src="/static/images/forum/img_noticelab.png">系统提醒</li>
            <li class="li4" value="4"><img src="/static/images/forum/img_noticenewfri.png">好友请求</li>
        </ul>
    </div>

    <div class="set-right">
        <div class="right-xx right-r" id="Inf" style="display: none">
            <p class="p-forumset">
                <span>私人消息</span>
                <em class="em1"><&nbsp;返回消息列表</em>
            </p>
            <div class="sx-list" id="fMessage">
            </div>

            <div class="sx-li" id="messagesList">
            </div>

            <div class="sx-new clearfix">
                <div class="sj-left">收件人:</div>
                <div class="sj-right">
                    <textarea class="area1"></textarea>
                    <span class="span1">选择好友</span>
                    <span class="span2">多个用户使用逗号、分号或会车提示系统分开</span>
                    <div class="fri-sel">
                        <p class="p1">
                            <select>
                                <option>全部好友</option>
                            </select>
                        </p>
                        <div class="fri-list">
                            <label>
                                <input type="checkbox">张三
                            </label>
                            <label>
                                <input type="checkbox">张三
                            </label>
                            <label>
                                <input type="checkbox">张三
                            </label>
                            <label>
                                <input type="checkbox">张三
                            </label>
                        </div>
                        <p class="p1">
                            <button>关闭</button>
                        </p>
                    </div>
                </div>
                <div class="clearfix"></div>
                <div class="sj-left">内容:</div>
                <div class="sj-right">
                    <div class="sj-right-edit"></div>
                    <button class="fs-sj-right">发送</button>
                </div>
            </div>
        </div>

        <div class="right-tz right-r" id="post">
        </div>

        <div class="right-xt right-r" id="sys" style="display: none">
        </div>

        <div class="right-qq right-r" id="friend" style="display: none">
        </div>

    </div>
</div>

<%@ include file="../common/footer.jsp" %>
<%@ include file="../common/login.jsp" %>

<script type="text/template" id="recordTml">
    <p class="p-forumset">
        <span>帖子</span>
    </p>
    {{~it:value:index}}
    <div class="myt-msg clearfix">
        {{?value.imageSrc!=null&&value.imageSrc !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="{{=value.imageSrc}}">
        {{?}}
        {{?value.imageSrc==null||value.imageSrc =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="/static/images/forum/head_picture1.png">
        {{?}}
        <div class="tz-right">
            <p class="p1">{{=value.timeText}}</p>
            {{?value.logRecord==1}}
            <p class="p2">{{=value.content}}</p>
            {{?}}
            {{?value.logRecord==2}}
            <p class="p2">{{=value.content}}<em><span class="redirect" value="{{=value.fRecordId}}"
                                                      style="cursor: pointer">查看</span></em></p>
            {{?}}
        </div>
    </div>
    {{~}}
</script>

<script type="text/template" id="friendTml">
    <p class="p-forumset">
        <span>好友请求</span>
    </p>
    {{~it:value:index}}
    <div class="myt-msg clearfix">
        {{?value.avatar!=null&&value.avatar !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="{{=value.avatar}}">
        {{?}}
        {{?value.avatar==null||value.avatar =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
        <img src="/static/images/forum/head_picture1.png">
        {{?}}
        <div class="tz-right">
            <p class="p1">{{=value.time}}天前</p>
            <p class="p2">{{=value.nickName}}请求添加你为好友</p>
            <p class="p4">
                {{?value.content==null}}
                <em>留言为空！</em>
                {{?}}
                {{?value.content!=null}}
                <em>留言：{{=value.content}}</em>
                {{?}}
                {{?value.accepted==0}}
                <button class="btn-js" acceptId="{{=value.id}}">接受</button>
                <button class="btn-jj" refuseId="{{=value.id}}">拒绝</button>
                {{?}}
                {{?value.accepted==1}}
                已接受
                {{?}}
                {{?value.accepted==2}}
                已拒绝
                {{?}}
            </p>
        </div>
    </div>
    {{~}}
</script>

<script type="text/template" id="fMessageTml">
    {{~it:value:index}}
    <div class="li-sr clearfix">
        <div class="img-sr-f">
            {{?value.imageSrc!=null&&value.imageSrc !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
            <img src="{{=value.imageSrc}}">
            {{?}}
            {{?value.imageSrc==null||value.imageSrc =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
            <img src="/static/images/forum/head_picture1.png">
            {{?}}
            {{?value.count>=2}}
            <em class="sr-n-em"></em>
            {{?}}
        </div>
        <div class="xx-box">
            <p class="p1"><i>{{=value.nickName}}</i>对{{=value.acceptName}}说：</p>
            <p class="p2"><span class="messageReply" value="{{=value.userId}}">{{=value.content}}</span></p>
            <p class="p3">
                <em>{{=value.time}}天前</em>
                <span class="span-set">
                                        共{{=value.count}}条
                                        <span class="em-set">&nbsp;&nbsp;    <em><span class="messageReply" type="{{=value.acceptType}}"
                                                                                       {{?value.acceptType==2}}
                                                                                       value="{{=value.userId}}" acceptName="{{=value.nickName}}"
                                                                                       {{??}}
                                                                                       value="{{=value.personId}}" acceptName="{{=value.acceptName}}"
                                                                                       {{?}}>回复</span></em>
                                        </span>
                                    </span>
            </p>
        </div>
    </div>
    {{~}}
</script>

<script type="text/template" id="fMessagesTml">
    <p class="p-ti">
        共有<em class="em1"><span id="count"></span></em>条与<em class="em2"><span id="nick"></span></em>的交谈记录
        <em class="em3" id="listEm">
            <em class="em4" id="tou">全部删除</em>
        </em>
    </p>
    {{~it:value:index}}
    <div class="li-sr clearfix">
        <div class="img-sr-f">
            {{?value.imageSrc!=null&&value.imageSrc !='http://7xiclj.com1.z0.glb.clouddn.com/'}}
            <img src="{{=value.imageSrc}}">
            {{?}}
            {{?value.imageSrc==null||value.imageSrc =='http://7xiclj.com1.z0.glb.clouddn.com/'}}
            <img src="/static/images/forum/head_picture1.png">
            {{?}}
        </div>
        <div class="xx-box">
            <p class="p1">
                <i>{{=value.nickName}}</i>
                <span class="span-set">
                                        <em class="em-bg ll">
                                          <em class="em4 lll" value="{{=value.id}}">删除</em>
                                        </em>
                                    </span>
            </p>
            <p class="p2">{{=value.content}}</p>
            <p class="p3">
                <em>{{=value.time}}天前</em>
            </p>
        </div>
    </div>
    {{~}}
    <div class="li-sr clearfix">
        <div class="img-sr-f">
            <img src="/static/images/forum/single_dog.png">
        </div>
        <div class="xx-box">
            <input class="hf-xx" type="text" id="valhf">
            <button class="btn-fs" id="sendOne">发送</button>
        </div>
    </div>
</script>


<script type="text/template" id="fSystemTml">
    <p class="p-forumset">
        <span>系统提醒</span>
    </p>
    {{~it:value:index}}
    <div class="myt-msg clearfix">
        <img src="/static/images/fulaan_logo.png">
        <div class="tz-right">
            {{?value.content==""}}
            <p class="p2">
                尊敬的{{=value.nickName}}，您已经注册成为复兰教育商城的会员，请您在发表言论时，遵守当地法律法规。
                如果您有什么疑问可以联系管理员，Email: kf@fulaan.com
            </p>
            {{?}}
            <p>{{=value.content}}</p>
            <p>复兰科技</p>
            <p>{{=value.timeText}}</p>
            <p class="p1">{{=value.time}}天前</p>
        </div>
    </div>
    {{~}}
</script>

<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/forumnotice.js');
</script>
</body>
</html>
