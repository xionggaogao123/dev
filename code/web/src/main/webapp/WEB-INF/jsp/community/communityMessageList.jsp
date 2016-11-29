<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" uri="http://www.dreamlu.net/tags/jsp-layout.tld" %>
<%--==============头部===================--%>
<%@ include file="../common/head.jsp" %>
<%-- 填充head --%>
<layout:override name="head">
    <title>复兰后台管理</title>
</layout:override>
<%-- 填充content --%>
<layout:override name="content">
    <input value="${communityId}" id="communityId" hidden>
    <div class="f-cont">
        <div class="hd-nav">
            <c:if test="${login == false}">
                <div class="login-mk-btn">
                    <div class="d1" onclick="window.open('/mall/register.do')"></div>
                    <div class="d2"></div>
                </div>
            </c:if>
            <span class="hd-green-cur">我的社区</span>
                <%--<span>找学习</span>--%>
                <%--<span>找玩伴</span>--%>
        </div>
    </div>
    <div class="hd-cont-f hd-cont-f1">
        <div class="det-left">
            <div class="act-details">
                <div class="com-left-s" id="content">
                </div>
                <div class="new-page-links"></div>
            </div>
        </div>
        <div class="com-right">
            <div class="com-right-s clearfix">
                <div class="com-tit">我的社区<c:if test="${login == true}"><span class="com-set-my-btn" onclick="window.open('/community/communitySet.do')"></span>
                </c:if></div>
                <ul class="ul-my-com clearfix" id="myCommunity">
                </ul>
            </div>
        </div>
    </div>
    </div>

    <!--=============底部版权=================-->
    <%@ include file="../common/footer.jsp" %>

    <%--环信消息通知--%>
    <div class="hx-notice">
        <span class="sp2" id="hx-icon"></span>
        <span class="sp3" id="hx-msg-count">您有0条未读消息</span>
    </div>


    <div class="wind-yins">
        <p class="p1">隐私设置<em>×</em></p>
        <label><input type="radio" name="ys-set">所有人可见</label>
        <label><input type="radio" name="ys-set">人认证好友可见</label>
        <label><input type="radio" name="ys-set">尽仅自己可见</label>
        <p class="p2">
            <button class="btn1">确认</button>
            <button class="btn2">取消</button>
        </p>
    </div>
    <!--报名提示start-->
    <div class="sign-alert">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>您确定要参加该活动吗？</span>
            <input type="text" placeholder="备注：">
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure">确认</button>
            <button class="alert-btn-esc">取消</button>
        </div>
    </div>
    <!--报名提示end-->
    <!--取消报名提示start-->
    <span communityId="${communityId}" type="${type}" id="temp">
    <div class="esc-alert">
        <p class="alert-title">提示<em>×</em></p>
        <div class="alert-main">
            <span>您确认要取消参加该活动吗？</span>
        </div>
        <div class="alert-btn">
            <button class="alert-btn-sure">确认</button>
            <button class="alert-btn-esc">取消</button>
        </div>
    </div>
    <!--取消报名提示end-->
    <div class="bg"></div>
</layout:override>
<layout:override name="script">
    <%-- 填充script --%>

    <script type="text/javascript">
    function download(url, fileName) {
        $.ajax({
            url: "/forum/loginInfo.do?date=" + new Date(),
            type: "get",
            dataType: "json",
            async: false,
            data: {},
            success: function (resp) {
                var flag = resp.login;
                if (flag) {
                    location.href = "/commondownload/downloadFile.do?remoteFilePath=" + url + "&fileName=" + fileName;
                }else{
                    $('.store-register').fadeToggle();
                    $('.bg').fadeToggle();
                }
            }
        })
    }
   </script>
    <!--============登录================-->
    <%@ include file="../common/login.jsp" %>
    <script src="/static/js/sea.js"></script>
    <script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script type="text/javascript"
            src="/static/js/modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1"></script>
    <script type="text/javascript" src="/static/js/modules/core/0.1.0/jquery-upload/jquery.fileupload.js"></script>

    <script>
        seajs.use('/static/js/modules/community/communityMessageList.js', function (communityMessageList) {
            communityMessageList.init();
        });
    </script>


    <!-- js template -->
    <script type="text/template" id="myCommunityTmpl">
        {{~it:value:index}}
        <li>
            <a href="/community/communityPublish?communityId={{=value.id}}"><img src="{{=value.logo}}"></a>
            <p>{{=value.name}}</p>
        </li>
        {{~}}
    </script>

    <script type="text/template" id="memberTmpl">
        {{~it:value:index}}
        <li>
            <img src="{{=value.avator}}">
            <p>{{=value.nickName}}</p>
            {{?value.role==2}}
            <p class="p-lel2">{{=value.roleStr}}</p>
            <span class="sp-gly"></span>
            {{??value.role==1}}
            <p class="p-lel1">{{=value.roleStr}}</p>
            <span class="sp-bzr"></span>
            {{??}}
            <p>{{=value.roleStr}}</p>
            {{?}}
        </li>
        {{~}}
    </script>

    <script type="text/template" id="meansTmpl">
        <div class="com-tit">学习资料</div>
        {{~it:value:index}}
        <div class="notice-container clearfix">
            <div class="notice-holder">
                <img src="{{=value.imageUrl}}">
                <p>{{=value.nickName}}</p>
                <p class="p1">{{=value.roleStr}}</p>
            </div>
            <div class="notice-cont">
                <p class="p-zl p1" style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
                <p class="p-hw">
                    {{~value.attachements:attachment:i}}
                    <span class="sp-hw">{{=attachment.flnm}}<a><span
                            onclick="download('{{=attachment.url}}','{{=attachment.flnm}}')" style="cursor: pointer">下载</span></a></span>
                    {{~}}
                </p>
                <p class="p-infor">
                    <span>消息来源：{{=value.nickName}}</span>
                    <span>发表时间：{{=value.time}}</span>
                    <a><span class="detail" value="{{=value.id}}" style="cursor: pointer">详情</span></a>
                </p>
            </div>
        </div>
        {{~}}
    </script>

    <script type="text/template" id="homeworkTmpl">
        <div class="com-tit">作业</div>
        {{~it:value:index}}
        <div class="notice-container clearfix">
            <div class="notice-holder">
                <img src="{{=value.imageUrl}}">
                <p>{{=value.nickName}}</p>
                <p class="p1">{{=value.roleStr}}</p>
            </div>
            <div class="notice-cont">
                <p class="p-zy p1" style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
                <p class="p-cont">{{=value.content}}<span
                        class="sp-more">{{?value.content.length>=143}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
                <p class="p-infor">
                    <span>消息来源：{{=value.nickName}}</span>
                    <span>发表时间：{{=value.time}}</span>
                </p>
            </div>
            <%--<div class="notice-bm">--%>
                <%--<button class="commit" itemId="{{=value.id}}">提交作业</button>--%>
                <%--<span>已提交（{{=value.partInCount}}）人</span>--%>
            <%--</div>--%>
        </div>
        {{~}}
    </script>

    <script type="text/template" id="announcementTmpl">
        <div class="com-tit" id="announce_all">社区通知</div>
        {{~it:value:index}}
        <div class="notice-container clearfix">
            <div class="notice-holder">
                <img src="{{=value.imageUrl}}">
                <p>{{=value.nickName}}</p>
                <p class="p1">{{=value.roleStr}}</p>
            </div>
            <div class="notice-cont">
                <p class="p-tz p1" style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
                <p class="p-cont">{{=value.content}}<span
                        class="sp-more">{{?value.content.length>=143}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
                <p class="p-infor">
                    <span>消息来源：{{=value.nickName}}</span>
                    <span>发表时间：{{=value.time}}</span>
                </p>
            </div>
        </div>
        {{~}}
    </script>


    <script type="text/template" id="shareTmpl">
        <div class="com-tit">火热分享</div>
        {{~it:value:index}}
        <div class="notice-container clearfix">
            <div class="notice-holder">
                <img src="{{=value.imageUrl}}">
                <p>{{=value.nickName}}</p>
                <p class="p1">{{=value.roleStr}}</p>
            </div>
            <div class="notice-cont">
                <p class="p-fx p1" style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
                <p class="p-cont">{{=value.content}}<span
                        class="sp-more">{{?value.content.length>=143}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
                <p class="p-img clearfix">
                    {{~value.images:image:i}}
                    <img src="{{=image.url}}">
                    {{~}}
                </p>
                <p class="p-infor">
                    <span>消息来源：{{=value.nickName}}</span>
                    <span>发表时间：{{=value.time}}</span>
                    <span>详情</span>
                </p>
            </div>
            <%--<div class="notice-bm">--%>
                <%--<button class="commit" itemId="{{=value.id}}">我要分享</button>--%>
                <%--<span>已分享（{{=value.partInCount}}）人</span>--%>
            <%--</div>--%>
        </div>
        {{~}}
    </script>


    <script type="text/template" id="activityTmpl">
        <div class="com-tit">组织活动报名</div>
        {{~it:value:index}}
        <div class="notice-container clearfix">
            <div class="notice-holder">
                <img src="{{=value.imageUrl}}">
                <p>{{=value.nickName}}</p>
                <p class="p1">{{=value.roleStr}}</p>
            </div>
            <div class="notice-cont">
                <p class="p-hd p1" style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
                <p class="p-cont">{{=value.content}}<span
                        class="sp-more">{{?value.content.length>=143}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
                <p class="p-infor">
                    <span>消息来源：{{=value.nickName}}</span>
                    <span>发表时间：{{=value.time}}</span>
                </p>
            </div>
            <div class="notice-bm">
                <button class="commit" itemId="{{=value.id}}">我要报名</button>
                <span>已报名（<em>{{=value.partInCount}}</em>）人</span>
            </div>
        </div>
        {{~}}
    </script>


    <script type="text/template" id="materialsTmpl">
        <div class="com-tit">学习用品</div>
        {{~it:value:index}}
        <div class="notice-container clearfix">
            <div class="notice-holder">
                <img src="{{=value.imageUrl}}">
                <p>{{=value.nickName}}</p>
                <p class="p1">{{=value.roleStr}}</p>
            </div>
            <div class="notice-cont">
                <p class="p-tj p1" style="cursor: pointer" onclick="window.open('/community/communityDetail?detailId={{=value.id}}')">{{=value.title}}</p>
                <p class="p-cont">{{=value.content}}<span
                        class="sp-more">{{?value.content.length>=143}}...<em class="spread">[展开全文]</em>{{?}}</span></p>
                <p class="p-infor">
                    <span>消息来源：{{=value.nickName}}</span>
                    <span>发表时间：{{=value.time}}</span>
                </p>
            </div>
        </div>
        {{~}}
    </script>

    <!-- end js template -->
</layout:override>
<%@ include file="_layout.jsp" %>