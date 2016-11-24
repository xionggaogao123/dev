<%--
  Created by IntelliJ IDEA.
  User: qiangm
  Date: 2015/7/27
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>好友圈</title>
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <link href="/static_new/css/friendcircle/homepage.css?v=1" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/rome.css?v=1"/>
    <link rel="stylesheet" type="text/css" href="/static_new/css/friendcircle/wanban.css?v=2"/>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body style="overflow-x: hidden">
<%@ include file="../common_new/head.jsp" %>
<input type="hidden" id="hiddenType" value="${hiddenType}"/>

<div id="content" class="clearfix">
    <%@ include file="../common_new/col-left.jsp" %>
    <%@ include file="leftAppend.jsp" %>
    <!--广告-->
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>
    <!--广告-->
    <div class="col-right"><%--ss--%>

        <div class="wanban">
            <div class="wanban-head clearfix">
                <ul>
                    <li class="cur">最近动态</li>
                    <li>好友活动</li>
                    <li>推荐活动</li>
                    <li>我参加的</li>
                    <li>我发表的</li>
                    <li class="mr">发起活动</li>
                </ul>
            </div>
            <!--/.wanban-head-->

            <!--.wanban-main-->
            <div class="wanban-main clearfix">

                <!--.sub-wanban-main最近动态-->
                <div class="listmain sub-wanban-main">
                    <script id="wTmpl" type="text/template">
                        {{~it.data:value:index}}
                        {{?value.type=="PROMOTE"}}
                        <div class="wanban-list clearfix">
                            <img class="wanban-img" src="{{=value.userImgUrl}}"/>

                            <div class="wanban-info">
                                <em>{{=value.userName || '无名氏'}}</em>

                                <p>{{=value.userName || '无名氏'}}发起了新活动</p>

                                <div class="sub-wanban-info clearfix">
                                    <img src="{{=value.activity.coverImage}}"/>
                                    <dl>
                                        <dt>{{=value.activity.name}}</dt>
                                        <dd>
                                            时间：{{=value.activity.strEventStartDate}}至{{=value.activity.strEventEndDate}}
                                        </dd>
                                        <dd>地点：{{=value.activity.location}}</dd>
                                        <dd>说明：{{=value.activity.description}}</dd>
                                        <dd class="sp">
                                                <span><a
                                                        href="javascript:;">{{=value.activity.attendCount}}</a> 人参加</span>
                                            <span><a href="javascript:;">{{=value.activity.discuss}}</a> 个讨论</span>
                                            <span><a href="javascript:;">{{=value.activity.image}}</a> 张图片</span>
                                        </dd>
                                    </dl>
                                </div>
                                <div class="sub-wanban-txt clearfix">
                                       <span>
                                            {{?value.fromDevice=="FromPC"}}
                                            来自PC端
                                            {{??}}
                                            来自移动端
                                            {{?}}
                                            {{=value.timeMsg}}
                                       </span>
                                    <a href="/activity/activityView.do?actId={{=value.activity.id}}"
                                            >活动详情</a>
                                </div>
                            </div>
                        </div>
                        {{??value.type=="FRIEND"}}
                        <div class="wanban-list clearfix">
                            <img class="wanban-img" src="{{=value.userImgUrl}}"/>

                            <div class="wanban-info">
                                <em>{{=value.userName || '无名氏'}}</em>

                                <p>{{=value.userName || '无名氏'}}与 {{=value.relateUserName}} 成为好友</p>

                            </div>

                        </div>
                        {{??value.type=="ATTEND"}}
                        <div class="wanban-list clearfix">
                            <img class="wanban-img" src="{{=value.userImgUrl}}"/>

                            <div class="wanban-info">
                                <em>{{=value.userName || '无名氏'}}</em>

                                <p>{{=value.userName || '无名氏'}}参加了 {{=value.activity.name}}活动</p>

                                <div class="sub-wanban-info clearfix">
                                    <img src="{{=value.activity.coverImage}}"/>
                                    <dl>
                                        <dt>{{=value.activity.name}}</dt>
                                        <dd>
                                            时间：{{=value.activity.strEventStartDate}}至{{=value.activity.strEventEndDate}}
                                        </dd>
                                        <dd>地点：{{=value.activity.location}}</dd>
                                        <dd>说明：{{=value.activity.description}}</dd>
                                        <dd class="sp">
                                                <span><a
                                                        href="javascript:;">{{=value.activity.attendCount}}</a> 人参加</span>
                                            <span><a href="javascript:;">{{=value.activity.discuss}}</a> 个讨论</span>
                                            <span><a href="javascript:;">{{=value.activity.image}}</a> 张图片</span>
                                        </dd>
                                    </dl>
                                </div>
                                <div class="sub-wanban-txt clearfix">
                                       <span>
                                            {{?value.fromDevice=="FromPC"}}
                                            来自PC端
                                            {{??}}
                                            来自移动端
                                            {{?}}
                                            {{=value.timeMsg}}
                                       </span>
                                    <a href="/activity/activityView.do?actId={{=value.activity.id}}"
                                            >活动详情</a>
                                </div>
                            </div>
                        </div>
                        {{??value.type=="REPLY"}}
                        <div class="wanban-list clearfix">
                            <img class="wanban-img" src="{{=value.userImgUrl}}"/>

                            <div class="wanban-info">
                                <em>{{=value.userName || '无名氏'}}</em>

                                <p>{{=value.userName || '无名氏'}}回复了
                                    <a style="color:blue;"
                                       href="/activity/activityView.do?actId={{=value.activity.id}}"
                                            >{{=value.activity.name}}</a></p>

                                <div class="sub-wanban-info clearfix">
                                    <img src="{{=value.activity.coverImage}}"/>
                                    <dl>
                                        <dt>{{=value.activity.name}}</dt>
                                        <dd>
                                            时间：{{=value.activity.strEventStartDate}}至{{=value.activity.strEventEndDate}}
                                        </dd>
                                        <dd>地点：{{=value.activity.location}}</dd>
                                        <dd>说明：{{=value.activity.description}}</dd>
                                        <dd class="sp">
                                                <span><a
                                                        href="javascript:;">{{=value.activity.attendCount}}</a> 人参加</span>
                                            <span><a href="javascript:;">{{=value.activity.discuss}}</a> 个讨论</span>
                                            <span><a href="javascript:;">{{=value.activity.image}}</a> 张图片</span>
                                        </dd>
                                    </dl>
                                </div>
                                <div class="sub-wanban-txt clearfix">
                                       <span>
                                            {{?value.fromDevice=="FromPC"}}
                                            来自PC端
                                            {{??}}
                                            来自移动端
                                            {{?}}
                                            {{=value.timeMsg}}
                                       </span>
                                    <a href="/activity/activityView.do?actId={{=value.activity.id}}"
                                            >活动详情</a>
                                </div>
                            </div>
                        </div>
                        {{?}}
                        {{~}}
                    </script>
                    <div class="recentActivity" style="display:block;  overflow: visible;">
                    </div>
                    <!--.page-links-->
                    <div class="page-paginator">
                        <span class="first-page">首页</span>
                            <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                            </span>
                        <span class="last-page">尾页</span>

                    </div>

                </div>
                <!--/.sub-wanban-main最近动态-->

                <!--/.sub-wanban-main好友活动-->
                <div class="listmain activity" style="display:none;">
                    <script id="hyhd" type="text/template">
                        {{~it.data:value:index}}
                        <div class="active-list clearfix">
                            <img src="{{=value.coverImage}}" alt="" class="huodong">

                            <div class="active-listmain">
                                <a href="/activity/activityView.do?actId={{=value.id}}"
                                   class="tit">{{=value.name}}</a>
                                <ul>
                                    <li>时间：{{=value.strEventStartDate}}至{{=value.strEventEndDate}}</li>
                                    <li>
                                        <span title="{{=value.location}}">地点：{{=value.location}}</span>
                                    </li>
                                    <li>
                                        <span title="{{=value.description}}">说明：{{=value.description}}</span>
                                    </li>
                                </ul>
                                <span><a href="javascript:;">{{=value.attendCount}}</a> 人参加</span>
                                <span><a href="javascript:;">{{=value.discuss}}</a> 个讨论</span>
                                <span><a href="javascript:;">{{=value.image}}</a> 张图片</span>
                            </div>
                            <span class="fa">发起人：{{=value.organizerName}}</span>
                        </div>
                        {{~}}
                    </script>
                    <div class="friendActivity" style="display:block;">
                    </div>
                    <!--.page-links-->
                    <div class="page-paginator">
                        <span class="first-page">首页</span>
                            <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                            </span>
                        <span class="last-page">尾页</span>

                    </div>
                    <!--/.page-links-->
                </div>
                <!--/.sub-wanban-main好友活动-->

                <!--/.sub-wanban-main推荐活动-->
                <div class="listmain activity1" style="display:none;">
                    <script id="tjhd" type="text/template">
                        {{~it.data:value:index}}
                        <div class="active-list clearfix">
                            <img src="{{=value.coverImage}}" alt="" class="huodong">

                            <div class="active-listmain">
                                <a href="/activity/activityView.do?actId={{=value.id}}"
                                   class="tit">{{=value.name}}</a>
                                <ul>
                                    <li>时间：{{=value.strEventStartDate}}至{{=value.strEventEndDate}}</li>
                                    <li>
                                        <span title="{{=value.location}}">地点：{{=value.location}}</span>
                                    </li>
                                    <li>
                                        <span title="{{=value.description}}">说明：{{=value.description}}</span>
                                    </li>
                                </ul>
                                <span><a href="javascript:;">{{=value.attendCount}}</a> 人参加</span>
                                <span><a href="javascript:;">{{=value.discuss}}</a> 个讨论</span>
                                <span><a href="javascript:;">{{=value.image}}</a> 张图片</span>
                            </div>
                            <span class="fa" style="float: left;">发起人：{{=value.organizerName}}</span>
                            {{?!value.isFriend}}
                            <a class="addFriendActivity" fid="{{=value.organizer}}">
                                <img src="../img/activity/jia.png" width="14px" height="14px" alt="" title="加为好友"
                                     class="jhu">
                            </a>
                            {{?}}
                        </div>
                        {{~}}
                    </script>
                    <div class="recomandActivity" style="display:block;">
                    </div>
                    <!--.page-links-->
                    <div class="page-paginator">
                        <span class="first-page">首页</span>
                            <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                            </span>
                        <span class="last-page">尾页</span>

                    </div>
                </div>
                <!--/.sub-wanban-main推荐活动-->

                <!--/.sub-wanban-main我参加的-->
                <div class="listmain activity2" style="display:none;">
                    <script id="wcjd" type="text/template">
                        {{~it.data:value:index}}
                        <div class="active-list clearfix">
                            <img src="{{=value.coverImage}}" alt="" class="huodong">

                            <div class="active-listmain">
                                <a href="/activity/activityView.do?actId={{=value.id}}"
                                   class="tit">{{=value.name}}</a>
                                <ul>
                                    <li>时间：{{=value.strEventStartDate}}至{{=value.strEventEndDate}}</li>
                                    <li>
                                        <span title="{{=value.address}}">地点：{{=value.location}}</span>
                                    </li>
                                    <li>
                                        <span title="{{=value.main}}">说明：{{=value.description}}</span>
                                    </li>
                                </ul>
                                <span><a href="javascript:;">{{=value.attendCount}}</a> 人参加</span>
                                <span><a href="javascript:;">{{=value.discuss}}</a> 个讨论</span>
                                <span><a href="javascript:;">{{=value.image}}</a> 张图片</span>
                            </div>
                            <span class="fa">发起人：{{=value.organizerName}}</span>
                            <a class="delt quitActivity" actid="{{=value.id}}">退出</a>
                        </div>
                        {{~}}
                    </script>
                    <div class="myjoinedactivity" style="display:block;">
                    </div>
                    <!--.page-links-->
                    <div class="page-paginator">
                        <span class="first-page">首页</span>
                            <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                            </span>
                        <span class="last-page">尾页</span>

                    </div>
                </div>
                <!--/.sub-wanban-main我参加的-->

                <!--/.sub-wanban-main我发表的-->
                <div class="listmain activity3" style="display:none;">
                    <script id="wfbd" type="text/template">
                        {{~it.data:value:index}}
                        <div class="active-list clearfix">
                            <img src="{{=value.coverImage}}" alt="" class="huodong">

                            <div class="active-listmain">
                                <a href="/activity/activityView.do?actId={{=value.id}}"
                                   class="tit">{{=value.name}}</a>
                                <ul>
                                    <li>时间：{{=value.strEventStartDate}}至{{=value.strEventEndDate}}</li>
                                    <li>
                                        <span title="{{=value.address}}">地点：{{=value.location}}</span>
                                    </li>
                                    <li>
                                        <span title="{{=value.main}}">说明：{{=value.description}}</span>
                                    </li>
                                </ul>
                                <span><a href="javascript:;">{{=value.attendCount}}</a> 人参加</span>
                                <span><a href="javascript:;">{{=value.discuss}}</a> 个讨论</span>
                                <span><a href="javascript:;">{{=value.image}}</a> 张图片</span>
                                {{?value.status=='CANCEL'}}
                                <i class="qx">活动已删除</i>
                                {{??value.status=='CLOSE'}}
                                <i class="qx">活动已关闭</i>
                                {{??}}
                                <a class="qx cancelHref" actid="{{=value.id}}"><span
                                        style="float: right;right:-160px;">删除活动</span></a>
                                {{?}}
                            </div>
                            <span class="fa">发起人：{{=value.organizerName}}</span>
                        </div>
                        {{~}}
                    </script>
                    <div class="mypromoteactivity" style="display:block;">
                    </div>
                    <!--.page-links-->
                    <div class="page-paginator">
                        <span class="first-page">首页</span>
                            <span class="page-index">
                            <span class="active">1</span>
                            <span>2</span>
                            <span>3</span>
                            <span>4</span>
                            <span>5</span>
                            <i>···</i>
                            </span>
                        <span class="last-page">尾页</span>

                    </div>
                </div>
                <!--/.sub-wanban-main我发表的-->
                <div class="listmain activity4 clearfix" style="display:none;">
                    <!-- <script id="fqhd" type="text/template">
                    </script> -->
                    <div class="left_main">
                        <h6>发起活动</h6>

                        <div class="tianxie">
                            <input type="text" class="ft" placeholder="活动名称：" id="nope">
                            <input type="text" placeholder="开始时间：" id="dt" readonly="true"
                            <%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"--%>>
                            <input type="text" placeholder="结束时间：" id="dt1" class="sd" readonly="true"
                            <%--onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class="sd"--%>>
                            <input type="text" placeholder="地点：" class="ft" id="address">
                            <textarea placeholder="说明（填选）：" id="defArea"></textarea>
                            <select id="pref_noapply">
                                <option value="2">公开</option>
                                <option value="0">仅邀请的好友可参加</option>
                                <option value="1">好友可参加</option>
                            </select>
                            <input type="text" placeholder="人数：" id="num" class="sd"
                                   onkeyup="this.value=this.value.replace(/\D/g,'')"
                                   onafterpaste="this.value=this.value.replace(/\D/g,'')">
                        </div>
                        <div class="tjfm">
                            <a class="addCoverImg" href="javascript:;">+添加封面</a>
                            <img id="coverImage" src="" alt="">
                            <a class="delCoverImg replaceImg" href="javascript:;">删除图片</a>
                            <a class="addCoverImg replaceImg" href="javascript:;">替换图片</a>
                            <input type="file" class="file" id="file" name="file" style="display:none;"
                                   accept="image/gif, image/jpeg, image/bmp,image/jpg"/>
                        </div>
                        <div class="yqhy">

                            <script id="invitedFriendList" type="text/template">
                                {{~it.data:value:index}}
                                <dl uid="{{=value.id}}">
                                    <dt>
                                        <s class="deleteFriend"></s>
                                        <img src="{{=value.userImg}}" alt="">
                                    </dt>

                                    <dd title="{{=value.userName}}">{{=value.userName}}</dd>
                                </dl>
                                {{~}}
                            </script>

                            <a href="javascript:;" id="inviteFriend">+邀请好友</a>
                            <!-- 弹出层 -->
                            <div class="gay"></div>
                            <div class="hylb">
                                <p>邀请好友<span class="gb"></span></p>

                                <div class="hylb_main clearfix">
                                    <div class="hylb_left">
                                        <div class="t">
                                            <span>联系人：</span>
                                            <input type="text" disabled="disabled" id="choosedFriend">
                                        </div>
                                        <div class="b">
                                            <span>内容：</span>
                                            <textarea style="border: 1px solid #DBDBDB;height: 102px;"
                                                      id="pm-content" placeholder="快来参加活动吧！"></textarea>
                                        </div>
                                        <button style="padding-top: 0px;" class="closeWindow">确定</button>
                                    </div>
                                    <div class="hylb_right">
                                        <ul style="width: 100%">
                                            <div class="bj">好友</div>
                                            <script id="friendList" type="text/template">
                                                {{~it.data:value:index}}
                                                <li class="frinedLi" userId="{{=value.id}}"
                                                    userName="{{=value.userName}}" userImg="{{=value.imgUrl}}">
                                                    {{=value.userName}}
                                                </li>
                                                {{~}}
                                            </script>

                                        </ul>
                                    </div>
                                </div>
                            </div>
                            <!-- 弹出层 -->
                        </div>
                        <div class="hou">
                            <button>取消</button>
                            <a href="javascript:;" id="launchActivity">确定</a>
                        </div>
                    </div>
                    <div class="right_main">
                        <p>Q：什么是活动？</p>
                        <span>A：活动可以基于兴趣与学习产生的互动，比如找小伙伴一起打羽毛、找小伙伴一起学习等都可以算作一次活动。</span>

                        <p>Q：可以利用活动做什么？</p>
                        <span>A：通过活动，您可以认识其他有相同兴趣和爱好的同学，并且可以和同学们进行线下的聚会，同时可以锻炼发起人同学的活动组织以及协调能力。</span>

                        <p>Q：我在发起活动的时候应该注意什么？</p>
                        <span>A：发起活动时，您需要告诉其他同学活动的时间、具体的地址、联系人等信息，除此之外，描述详细的活动内容会让同样感兴趣的同学注意到。</span>
                    </div>
                </div>
                <!--/.sub-wanban-main发起活动-->
            </div>
        </div>
    </div>
</div>
<%@ include file="../common_new/foot.jsp" %>
<script src="/static_new/js/sea.js?v=1"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('activityMain', function (activityMain) {
        activityMain.init();
    });
</script>
</body>
</html>
