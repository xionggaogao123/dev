<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>管理中心</title>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css"/>
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <style type="text/css">
        .account-input {
            vertical-align: top;;
            border: 1px solid darkgray;
            height: 28px;
            line-height: 22px;
            border-radius: 4px;
            width: 180px;
            padding: 0 10px;
            margin: 0 5px;
        }

        .header-cont .ha4 {
            background: #E8E8E8;
            color: #3764a0;
        }

        .postTitle {
        }

        .postID {
        }
    </style>
    <%--<script type="text/javascript" src="/static/js/manageCenter.js"></script>--%>
</head>
<body>
<%@ include file="../common/head.jsp" %>
<div class="container">
    <div class="set-left">
        <h2>管理中心</h2>
        <ul class="ul-forumset">
            <li class="li1 li-cur"><img src="/static/images/forum/gray_jing.png">管理举报</li>
            <li class="li2"><img src="/static/images/forum/gray_peo.png">管理用户</li>
            <li class="li3"><img src="/static/images/forum/gray_com.png">论坛主题管理</li>
        </ul>
    </div>

    <div class="set-right">
        <div class="right-xx right-r">
            <p class="p-forumset">
                <span>管理举报</span>
            </p>
            <div class="clearfix jubao">
                <span class="s1 span-curr">最新举报</span>
                <span class="s2">已处理举报</span>
            </div>
            <div class="jb-new jb-s1">
                <div class="d1">
                    <p><i></i> 可以根据举报真实情况，给举报者加减积分</p>
                    <p><i></i>多人举报时奖惩第一个举报人</p>
                </div>
                <table class="tab-jb" id="tabJ">
                </table>
                <p class="p-opt" id="poP">
                    <label><input type="checkbox" id="btn1">全选</label>
                    <button id="dee" value="1">删除</button>
                    <button id="deal">处理选中</button>
                </p>
                <div id="notFound" style="display: none">
                    没有举报信息！
                </div>
            </div>

            <%--<div class="new-page-links" style="display: none" id="pp"></div>--%>
            <div class="jb-new jb-s2">
                <table class="tab-jb" id="tabJl">
                </table>
                <p class="p-opt" id="poPl">
                    <label><input type="checkbox" id="btn2">全选</label>
                    <button id="dde" value="2">删除</button>
                    <%--<button>处理选中</button>--%>
                </p>
            </div>
            <div class="new-page-links"></div>

        </div>
        <div class="right-tz right-r">
            <p class="p-forumset">
                <span>管理用户</span>
            </p>
            <div class="clearfix jubao">
                <span class="s1 span-curr">搜索</span>
                <span class="s2">清理</span>
            </div>
            <div class="jb-new jb-s1">
                <div class="d1">
                    <p><i></i>通过用户管理，您可以进行编辑会员资料、用户组、积分以及删除会员等操作；</p>
                    <p><i></i>输入框可以使用通配符*，多个值之间用半角逗号“，”隔开。</p>
                </div>
                <%--<p class="p-input">--%>
                <%--<span>用户名:</span>--%>
                <%--<input type="text" >--%>
                <%--</p>--%>
                <p class="p-input">
                    <span>用户UID:</span>
                    <input type="text" id="userId">
                </p>
                <p class="p-input">
                    <span></span>
                    <button class="btn-search1">搜索</button>
                </p>
            </div>

            <div class="jb-new jb-s2">
                <div class="d1">
                    <p><i></i>通过用户管理，您可以进行编辑会员资料、用户组、积分以及删除会员等操作；</p>
                    <p><i></i>输入框可以使用通配符*，多个值之间用半角逗号“，”隔开。</p>
                </div>
                <%--<p class="p-input">--%>
                <%--<span>用户名:</span>--%>
                <%--<input type="text">--%>
                <%--</p>--%>
                <p class="p-input">
                    <span>用户UID:</span>
                    <input type="text" id="dll">
                </p>
                <p class="p-input">
                    <span></span>
                    <button class="btn-x12">删除用户</button>
                </p>
            </div>
            <div class="right-sea">
                <p class="p-back"><span>< 返回上一页</span></p>
                <p id="ac">共搜索到<em><span id="as"></span>名</em>符合条件的用户</p>
                <table class="sea-tab" id="seaTab">

                </table>
                <p class="sea-opt" id="af">
                    <label><input type="checkbox" id="asa">全选</label>
                    <button id="dluser">删除</button>
                </p>
                <div class="new-page-links" id="lom"></div>
                <div id="notFound1" style="display: none">
                    没有用户信息！
                </div>
            </div>

        </div>
        <div class="right-xt right-r">
            <p class="p-forumset p-reback">
                <span class="spa1">论坛主题管理</span>
                <span class="reback spa2">主题回收站</span>
            </p>
            <div class="myt-msg clearfix jb-new jb-s1 m-theme">
                <p class="p1">
                    <span class="span1">最新<i><span id="ppo"></span></i>条</span>
                    <em class="em1">版块</em>
                    <select id="ss">

                    </select>
                    <em class="em1">发帖时间</em>
                    <input type="text" id="startTime" class="in1 Wdate account-input"
                           onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="">
                    <em class="em2">-</em>
                    <input type="text" id="endTime" class="in1 Wdate account-input"
                           onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" value="">
                    <em class="em3">标题关键词</em>
                    <input type="text" class="in2" id="rr">
                    <button id="hhj">搜索</button>
                </p>
                <table class="tab-mtheme" id="nnh">

                </table>
                <p class="p2" id="dds">
                    <label>
                        <input type="checkbox" id="allSelect">全选
                    </label>
                    <button id="dlk">删除</button>
                    <button class="btn-move" id="move">移动所选</button>
                </p>
                <div class="new-page-links" id="postPage"></div>
                <div id="notFound2" style="display: none">
                    没有用户信息！
                </div>
            </div>
            <div class="jb-new jb-s2 m-theme">
                <p class="p1">
                    <span class="span1">最新<i><span id="ppol"></span></i>条</span>
                    <em class="em1">版块</em>
                    <select id="ssl">

                    </select>
                    <em class="em1">发帖时间</em>
                    <input type="text" id="startPostTime" class="in1 Wdate account-input"
                           onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="">
                    <em class="em2">-</em>
                    <input type="text" id="endPostTime" class="in1 Wdate account-input"
                           onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startPostTime\')}'})"
                           value="">
                    <em class="em3">标题关键词</em>
                    <input type="text" class="in2" id="rrl">
                    <button id="hhl">搜索</button>
                    <span class="span2">*回收站自动清理30天以上的帖子</span>
                </p>
                <table class="tab-mtheme" id="llk">

                </table>
                <p class="p2" id="ddsl">
                    <label>
                        <input type="checkbox" id="aad">全选
                    </label>
                    <button class="btn-move" id="recover">恢复主题</button>
                </p>
                <div class="new-page-links" id="postPagel"></div>
                <div id="notFound3" style="display: none">
                    没有用户信息！
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<%--移动主题弹窗--%>
<div class="wind-move">
    <p class="p1">移动主题<em>×</em></p>
    <p class="p2">
        <span>从原在版块：</span>
        <span class="f-span" id="originSection"></span>
    </p>
    <p class="p2">
        <span>移动到版块：</span>
        <select id="ssk">

        </select>
    </p>
    <p class="p2">
        <button class="btn1" id="confirmss">确认</button>
        <button class="btn2">取消</button>
    </p>
</div>
<div class="m-wind-notice">
    <p class="p1">提示<em>×</em></p>
    <p class="p2">本操作不可恢复，</p>
    <p class="p2">您确定要删除符合条件的多个用户吗？</p>
    <p class="p3">
        <button class="btnok">确认</button>
        <button class="btnno">取消</button>
    </p>
</div>
<div class="m-wind-users">
    <p class="p1">编辑用户组<em>×</em></p>
    <p class="p2">*修改用户的积分会造成普通会员等级的变化，请仔细设置各项积分</p>
    <p class="p3">
        <span>用户组：</span>
        <select id="selectYear">
            <option value="1">Lv.1</option>
            <option value="2">Lv.2</option>
            <option value="3">Lv.3</option>
            <option value="4">Lv.4</option>
            <option value="5">Lv.5</option>
            <option value="6">Lv.6</option>
            <option value="7">Lv.7</option>
            <option value="8">Lv.8</option>
            <option value="9">Lv.9</option>
            <option value="10">Lv.10</option>
            <option value="11">Lv.11</option>
            <option value="12">Lv.12</option>
        </select>
        <span class="lian"><a onclick="window.open('/forum/forumLevel.do')"></a></span>
    </p>
    <p class="p3">
        <span>经验值：</span>
        <input type="text" id="at">
    </p>
    <p class="p3">
        <span></span>
        <button class="btn1" id="tt">确定</button>
        <button class="btn2">取消</button>
    </p>
</div>
<div class="m-wind-forbidden">
    <p class="p1">禁止用户<em id="cancelFor">×</em></p>
    <p class="p2">*选正常状态可以恢复该用户的普通身份</p>
    <p class="p3 clearfix">
        <span>禁止用户名：</span>
        <em class="em1"><span id="dor"></span></em>
    </p>
    <p class="p3 clearfix">
        <span>当前状态：</span>
        <em class="em2"><span id="dorr"></span></em>
    </p>
    <p class="p3 clearfix">
        <span>禁止类型：</span>
        <label class="lab1"><input type="radio" name="type-forbit" value="0">正常状态</label>
        <label><input type="radio" name="type-forbit" value="1">禁止发言</label>
        <label><input type="radio" name="type-forbit" value="2">禁止访问</label>
    </p>
    <p class="p3 clearfix">
        <span>禁止时间：</span>
        <select class="input1" id="banTime">
            <option value="-1"></option>
            <option value="0">永久</option>
            <option value="1">一天</option>
            <option value="2">两天</option>
            <option value="3">三天</option>
            <option value="4">四天</option>
            <option value="5">五天</option>
            <option value="7">一周</option>
            <option value="30">一个月</option>
            <option value="180">半年</option>
        </select>
        <em class="em3">有效期过后系统自动解除限制</em>
    </p>
    <p class="p3 clearfix">
        <span>禁止理由：</span>
        <textarea id="banReason"></textarea>
    </p>
    <p class="p3 clearfix">
        <span></span>
        <button class="btn1" id="bbb">确认</button>
        <button class="btn2" id="lll">取消</button>
    </p>

</div>
<div class="bg"></div>
<%@ include file="../common/footer.jsp" %>

<script type="text/template" id="ssTml">
    <option value="">全部板块</option>
    {{~it:value:index}}
    <option value="{{=value.fSectionId}}">{{=value.name}}</option>
    {{~}}
</script>

<script type="text/template" id="sslTml">
    <option value="">全部板块</option>
    {{~it:value:index}}
    <option value="{{=value.fSectionId}}">{{=value.name}}</option>
    {{~}}
</script>

<script type="text/template" id="sskTml">
    {{~it:value:index}}
    <option value="{{=value.fSectionId}}">{{=value.name}}</option>
    {{~}}
</script>

<script type="text/template" id="nnhTml">
    <thead>
    <tr>
        <th class="th1"></th>
        <th class="th2">标题</th>
        <th class="th3">版块</th>
        <th class="th4">作者</th>
        <th class="th5">回复/浏览</th>
        <th class="th6">最后发表</th>
    </tr>
    </thead>
    <tbody>
    {{~it:value:index}}
    <tr>
        <td class="td1"><input type="checkbox" name="checkPost" class="checkPost" value="{{=value.merge}}"></td>
        <td class="td2">
            <i class="postTitle">{{=value.postTitle}}</i>
            <input class="postID" type="hidden" value="{{=value.fpostId}}">
        </td>
        <td>{{=value.postSectionName}}</td>
        <td>{{=value.personName}}</td>
        <td>{{=value.commentCount}}/{{=value.scanCount}}</td>
        <td>{{=value.timeText}}</td>
    </tr>
    <option value="{{=value.fSectionId}}">{{=value.name}}</option>
    {{~}}
    </tbody>
</script>

<script type="text/template" id="llkTml">
    <thead>
    <tr>
        <th class="th1"></th>
        <th class="th2">标题</th>
        <th class="th3">版块</th>
        <th class="th4">作者</th>
        <th class="th5">回复/浏览</th>
        <th class="th6">最后发表</th>
    </tr>
    </thead>
    <tbody>
    {{~it:value:index}}
    <tr>
        <td class="td1"><input type="checkbox" name="checkPost" class="checkPost" value="{{=value.merge}}"></td>
        <td class="td2">
            <i>{{=value.postTitle}}</i>
        </td>
        <td>{{=value.postSectionName}}</td>
        <td>{{=value.personName}}</td>
        <td>{{=value.commentCount}}/{{=value.scanCount}}</td>
        <td>{{=value.timeText}}</td>
    </tr>
    <option value="{{=value.fSectionId}}">{{=value.name}}</option>
    {{~}}
    </tbody>
</script>

<script type="text/template" id="tabJlTml">
    <thead>
    <tr>
        <th class="th1"></th>
        <th class="th2">举报详情</th>
        <th class="th3">举报人</th>
        <th class="th4">操作</th>
    </tr>
    </thead>
    <tbody>
    {{~it:value:index}}
    <tr>
        <td><input type="checkbox" name="check" class="check" value="{{=value.fpostId}}"></td>
        <td>
            <p>主题内容：<span style="cursor: pointer;color: #03aaf4"
                          onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}&personId={{=value.personId}}')">{{=value.postTitle}}</span>
            </p>
            <p>处理时间：{{=value.timeText}}</p>
            <p>举报理由：</p>
            {{~value.reportedDTOList:reported:i}}
            <p>{{=reported.nickName}}:{{=reported.reason}}</p>
            {{~ }}
        </td>
        <td>{{=value.reportedUserName}}</td>
        <td class="td-op">
            <p>
                <span class="s1">经验值:</span>
                <input type="text" value="{{=value.reportedExperience}}">
            </p>
            <p>
                <span class="s1">留言:</span>
                <textarea>{{=value.reportedComment}}</textarea>
            </p>
        </td>
    </tr>
    {{~}}
    </tbody>
</script>

<script type="text/template" id="tabJTml">
    <thead>
    <tr>
        <th class="th1"></th>
        <th class="th2">举报详情</th>
        <th class="th3">举报人</th>
        <th class="th4">操作</th>
    </tr>
    </thead>
    <tbody>
    {{~it:value:index}}
    <tr class="tr">
        <td><input type="checkbox" name="check" class="check" value="{{=value.fpostId}}"></td>
        <td>
            <p>主题内容：<span style="cursor: pointer;color: #03aaf4"
                          onclick="window.open('/forum/postDetail.do?pSectionId={{=value.postSectionId}}&postId={{=value.fpostId}}&personId={{=value.personId}}')">{{=value.postTitle}}</span>
            </p>
            <p>举报时间：{{=value.timeText}}</p>
            <p>举报理由：</p>
            {{~value.reportedDTOList:reported:i}}
            <p>{{=reported.nickName}}:{{=reported.reason}}</p>
            {{~ }}
        </td>
        <td>{{=value.reportedUserName}}</td>
        <td class="td-op">
            <p>
                <span class="s1">经验值:</span>
                <input type="text" placeholder="不奖惩(只能填写数字)">
            </p>
            <p>
                <span class="s1">留言:</span>
                <textarea>{{=value.reportedComment}}</textarea>
            </p>
        </td>
    </tr>
    {{~}}
    </tbody>
</script>

<script type="text/template" id="seaTabTml">
    <thead>
    <tr>
        <th class="th1"></th>
        <th class="th2">用户名</th>
        <th class="th3">经验值</th>
        <th class="th4">发帖数</th>
        <th class="th5">用户组</th>
        <th class="th6">操作</th>
    </tr>
    </thead>
    <tbody>
    {{~it:value:index}}
    <tr>
        <td class="td1"><input type="checkbox" name="checkUser" class="checkUser" value="{{=value.userId}}"></td>
        <td class="td2">{{=value.nickName}}</td>
        <td class="td3">{{=value.experience}}</td>
        <td class="td4">{{=value.postCount}}</td>
        <td class="td5">{{=value.level}}</td>
        <td class="td6">
            <span class="span1" id="span1" value="{{=value.together}}">用户组</span>
            <span class="span2" id="span2" value="{{=value.ban}}">禁止用户</span>
        </td>
    </tr>
    {{~}}
    </tbody>
</script>

<script src="/static/js/sea.js"></script>
<!-- Custom js -->
<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('/static/js/modules/forum/manageCenter.js');
</script>
</body>
</html>
