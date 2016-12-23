<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<span applyName="${nickName}" id="apply" hidden></span>

<div class="near-container clearfix">
    <div class="find-center-top clearfix">
        <c:if test="${login == true}">
            <div class="huodong-ss">
                <div class="line-l"></div>
                <div class="hd1">
                    <p class="p1">我的玩伴</p>
                    <p class="p2" style="cursor: pointer;"
                       onclick="window.open('/community/myPartners.do')">${friendCount}</p>
                    <p class="p3">
                        <a href="/forum/userCenter/user.do">
                            <button>论坛空间</button>
                        </a>
                    </p>
                </div>
                <div class="hd2">
                    <p class="p1">我的关注</p>
                    <p class="p2" style="cursor: pointer;"
                       onclick="window.open('/community/friendList.do')">${concernCount}</p>
                    <p class="p3">
                        <button id="chat">加玩伴</button>
                    </p>
                </div>
            </div>
        </c:if>
        <img src="/static/images/community/pc_find_center.jpg" class="img1">
        <c:if test="${login == true}">
            <img src="${avatar}" class="img2" style="cursor: pointer" onclick="window.open('/account/accountSafe')">
            <p class="p1">
                <span class="sp1">${nickName}</span>
                <span class="sp2">
                    <span>
                        <em class="em1"></em>
                        <em class="em2">ID:${packageCode}</em>
                    </span>
                </span>
                <span class="sp3 mine-er"></span>
            </p>
            <p class="p4" id="myTags">
                <c:forEach items="${tags}" var="tag">
                    <em>${tag}</em>
                </c:forEach>
                <em id="editTag">编辑标签</em>
            </p>

            <p class="p4 pt" id="myOns">
            </p>
        </c:if>
    </div>
</div>
<div class="wind-biaoq">
    <p class="p1">我的标签<em>×</em></p>
    <p class="p2">推荐标签<em style="color: #888;font-size: 12px;">（最多可选择6个标签）</em></p>
    <div class="bq-list tj-list clearfix" id="myTxt">
        <span code="100" tag="旅行">旅行</span>
        <span code="101" tag="英语">英语</span>
        <span code="102" tag="阅读">阅读</span>
        <span code="103" tag="足球">足球</span>
        <span code="104" tag="篮球">篮球</span>
        <span code="105" tag="羽毛球">羽毛球</span>
        <span code="106" tag="网球">网球</span>
        <span code="107" tag="乒乓球">乒乓球</span>
        <span code="108" tag="轮滑">轮滑</span>
        <span code="109" tag="跆拳道">跆拳道</span>
        <span code="110" tag="跳绳">跳绳</span>
        <span code="111" tag="歌唱">歌唱</span>
        <span code="112" tag="表演">表演</span>
        <span code="113" tag="舞蹈">舞蹈</span>
        <span code="114" tag="美术">美术</span>
        <span code="115" tag="钢琴">钢琴</span>
        <span code="116" tag="古筝">古筝</span>
        <span code="117" tag="二胡">二胡</span>
        <span code="118" tag="小提琴">小提琴</span>
        <span code="119" tag="笛子">笛子</span>
        <span code="120" tag="架子鼓">架子鼓</span>
        <span code="121" tag="围棋">围棋</span>
        <span code="122" tag="跳棋">跳棋</span>
        <span code="123" tag="象棋">象棋</span>
        <span code="124" tag="桥牌">桥牌</span>
        <span code="125" tag="演讲">演讲</span>
        <span code="126" tag="航模">航模</span>
        <span code="127" tag="航海">航海</span>
        <span code="128" tag="机器人">机器人</span>
    </div>
    <p class="p2">已选标签</p>
    <div class="bq-list clearfix" id="selected">
    </div>
    <p class="p3">
        <button class="btn1" id="btn1">确认添加</button>
        <button class="btn-add-no">取消添加</button>
    </p>
</div>

<div class="wind-ons">
    <p class="p1">选择时间段<em>×</em></p>
    <div class="bq-list tj-list clearfix ons-div">

    </div>
    <p class="p3">
        <button class="btn1">确认添加</button>
        <button class="btn-add-no">取消添加</button>
    </p>
</div>

<div class="wind wind-erw">
    <div class="d-x">×</div>
    <div class="d1">
        <img src="http://www.fulaan.com/static/images/community/upload.png">
        <p class="dp1">希曼</p>
        <p class="dp2">UID:100123</p>
    </div>
    <div class="d2">
        <img src="http://7xiclj.com1.z0.glb.clouddn.com/5846026bde04cb17e0693f7b" class="ddimg">
    </div>
    <div class="d3">
        <p style="text-align: center">请使用“复兰教育社区APP”扫描二维码<br>添加玩伴
        </p>
    </div>
</div>

<div class="wind wind-jwb">
    <p class="p1">加为玩伴<em>×</em></p>
    <p class="p2">
        <input type="text" id="jwbUid" placeholder="输入用户名/昵称/ID">
        <button class="btn1">查找</button>
    </p>
    <div id="jwbLoad" style="width: 85px;height: 20px;margin-left: 151px;display: none">正在查找中...</div>
    <div class="d1" id="jwbUser" hidden>
        <div class="d1-s1">查找结果
            <em class="em-green">（共找到<span id="jwbCount"></span>位相关用户）</em>
        </div>
        <script id="jwbUserListTmpl" type="text/template">
            {{~it:value:index}}
            <li>
                <img src="{{=value.avator}}">
                <p>{{=value.userName}}</p>
                <button class="jwbUserId" userId="{{=value.userId}}">加玩伴</button>
            </li>
            {{~}}
        </script>
        <ul class="d1-ul" id="jwbUserList">
        </ul>
    </div>
    <div class="new-page-links" id="jwb" hidden></div>
</div>


<script src="/static/js/sea.js"></script>
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>

<script>
    seajs.use('/static/js/modules/community/communityHead.js', function (communityHead) {
        communityHead.init();
    });

</script>