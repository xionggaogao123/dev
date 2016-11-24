<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>我的微课-复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <link rel="stylesheet" href="/static/css/new-theme-common.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/chat.css">
    <link rel="stylesheet" type="text/css" href="/static/css/groupchat/chat-index.css">
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/js/template.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>
    <script type="text/javascript">
        $(function(){
            $('#groupchat-li').addClass('current');
            $('#waitLoginmodal').modal('show');
            $('.bgd').show();
        });
    </script>

    <script type="text/javascript" src="/static/js/chat.js"></script>
    <script type='text/javascript' src='/static/js/groupchat/strophe-custom-1.0.0.js'></script>
    <script type="text/javascript" src="/static/js/groupchat/easemob.im-1.0.0.js"></script>
    <script type="text/javascript" src="/static/js/groupchat/bootstrap.js"></script>
    <script type="text/javascript">

        var nb_role = '${sessionValue.userRole}';
        var nb_role2 = 1;
        var nb_role3 = 0;
        if(${roles:isStudent(sessionValue.userRole)}){
            nb_role = 0;
        } else {
            nb_role = 1;
        }
        if(${roles:isHeadmaster(sessionValue.userRole)}){
            nb_role2 = 0;
        } else {
            nb_role2 = 1;
            if(${roles:isParent(sessionValue.userRole)}){
                nb_role3 = 0;
            } else {
                nb_role3 = 1;
            }
        }
        var nb_username = '${sessionValue.userName}';
        var nickname =  '${sessionValue.userName}';
        var imageUrl =  '${sessionValue.maxAvatar}';
        var userId =  '${sessionValue.id}';
        var userRole = '${sessionValue.userRole}';
        var nb_chatid = '${sessionValue.chatid}';
        conn = new Easemob.im.Connection();
        //初始化连接
        conn.init({
            //当连接成功时的回调方法
            onOpened: function () {
                handleOpen(conn);
            },
            //当连接关闭时的回调方法
            onClosed: function () {
                handleClosed();
            },
            //收到文本消息时的回调方法
            onTextMessage: function (message) {
                handleTextMessage(message);
            },
            //收到表情消息时的回调方法
            onEmotionMessage: function (message) {
                handleEmotion(message);
            },
            //收到图片消息时的回调方法
            onPictureMessage: function (message) {
                handlePictureMessage(message);
            },
            //收到音频消息的回调方法
            onAudioMessage: function (message) {
                handleAudioMessage(message);
            },
            onLocationMessage: function (message) {
                handleLocationMessage(message);
            },
            //收到联系人订阅请求的回调方法
            onPresence: function (message) {
                handlePresence(message);
            },
            //收到联系人信息的回调方法
            onRoster: function (message) {
                handleRoster(message);
            },
            //异常时的回调方法
            onError: function (message) {
                handleError(message);
            }
        });
        function openGroupChat() {
            if (!(conn.isOpened() || conn.isOpening())) {
                conn.open({
                    user: '${sessionValue.chatid}',
                    pwd: 'fulankeji',
                    //连接时提供appkey
                    //todo:appkey
                    //appKey: '${appKey}'
                    appKey:'fulan#education'
                });
            }
            showChatUI();
        }
        $(function () {
            openGroupChat();
        });
    </script>

</head>
<body>
<!-- 页头 -->
<%@ include file="../common_new/head.jsp" %>


<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>

        <div id="right-container" style="position: relative;padding-top: 10px">
            <c:choose>
                <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                    <jsp:include page="../common/right_2.jsp"></jsp:include>
                </c:when>
                <c:otherwise>
                    <jsp:include page="../common/right.jsp"></jsp:include>
                </c:otherwise>
            </c:choose>
            <div class="tab-section">
                <a class="tab-button chosen">群组交流</a>
            </div>
            <div class="tab-section-body" style="height: 710px; margin-bottom: 10px">
                <div id="bg" class="bgd" style="display: none;"></div>
                <div class="groupchat-container">

                    <div class="leftcontact" id="leftcontact">
                        <div id="headerimg" class="leftheader">
                            <img src="/img/chat_head.png"/>
                            <span id="login_user" class="login_user_title"> <a class="leftheader-font" href="#" style="text-decoration: none;">群组交流</a></span>
                        </div>
                        <div id="leftmiddle">
                            <div onclick="newGroup()" title="新建群组" class="chat-left-group-item"><i
                                    class="fa fa-plus-circle fa-lg"></i><span style="margin-left: 8px;">新建群组</span>
                            </div>
                        </div>
                        <div id="contractlist"
                             style="overflow: auto;"></div>
                    </div>

                    <div class="chatRight">
                        <div class="chat01_title"></div>
                        <div class="chat-contents">
                            <div class="groupchat-prompt">
                                <img src="/img/groupchat-logo.png"/>欢迎来到群组交流~
                            </div>
                        </div>
                    </div>

                    <div class="chatRight" style="display: none">
                        <div id="chat01">
                            <div class="chat01_title">
                                <ul class="talkTo">
                                    <li id="talkTo" style="margin-left: 120px;width: 60%;"><a href="#"></a></li>
                                    <li style="float: right;cursor: pointer;color: #fff;">
                                        <%--<div class="center_main_popup_main_IIII">--%>
                                        <%--<i class="fa fa-close fa-2x" onclick="$('.chatRight').hide();"></i>--%>
                                        <%--</div>--%>
                                    </li>
                                </ul>
                            </div>

                            <div class="chat-contents">
                                <!-- 对聊框 start-->
                                <div style="border-right: 1px solid #dcdcdc;">
                                    <div id="null-nouser" class="chat01_content"></div>
                                    <div class="chat02">
                                        <div class="chat02_title">

                                            <a class="chat02_title_btn ctb01" onclick="showEmotionDialog()"
                                               title="选择表情"></a>
                                            <a class="chat02_title_btn ctb02" onclick="showfilelist(0,8)"
                                               title="群组文件"></a>
                                            <a class="chat02_title_btn ctb03" title="历史消息" onclick="viewChatHis(this);"></a>
                                            <a class="chat02_title_btn ctb06" title="发送图片" onclick="showSendPic();"></a>
                                            <a class="chat02_title_btn ctb04" onclick="managerGroup()"
                                               title="管理群组"></a>
                                            <a class="chat02_title_btn ctb05" onclick="quitGroup()" title="退出群组"><span>退出群组</span></a>
                                            <label id="chat02_title_t"></label>

                                            <div id="wl_faces_box" class="wl_faces_box">
                                                <div class="wl_faces_content">
                                                    <div class="title">
                                                        <ul>
                                                            <li class="title_name">常用表情</li>
                                                            <li class="wl_faces_close"><span
                                                                    onclick='turnoffFaces_box()'>&nbsp;</span></li>
                                                        </ul>
                                                    </div>
                                                    <div id="wl_faces_main" class="wl_faces_main">
                                                        <ul id="emotionUL">
                                                        </ul>
                                                    </div>
                                                </div>
                                                <div class="wlf_icon"></div>
                                            </div>
                                        </div>
                                        <div id="input_content" class="chat02_content">
                                            <textarea id="talkInputId" style="resize: none;"></textarea>
                                        </div>
                                        <div class="chat02_bar">
                                            <ul>
                                                <li onclick="sendText()" class="center_main_popup_main_bottom_FS"><span
                                                        id="send_text">发&nbsp;&nbsp;送</span></li>
                                            </ul>
                                        </div>

                                        <div style="clear: both;"></div>
                                    </div>
                                </div>
                                <!-- 对聊框 end -->

                                <!-- 成员列表 start -->
                                <div id="member-list-container">
                                    <div class="chat-title-bar">群组成员(<span id="member-count" class="chat-count">0</span>)
                                    </div>
                                    <ul id="group-member-list">

                                    </ul>
                                </div>
                                <!-- 成员列表 end -->

                                <!-- 聊天记录 start -->
                                <div id="history-list-container" style="display: none;">
                                    <div style="overflow: auto;">
                                        <div class="chat-title-bar">历史记录(<span id="history-count"
                                                                               class="chat-count">0</span>条)<span
                                                class="chat-close-btn" onclick="$('.ctb03').trigger('click');">关闭</span>
                                        </div>
                                        <ul id="chat-history-list">
                                            <!-- <li class="history-date">2015-01-01</li>
                                            <li class="history-item">
                                                <p class="chat-person"><span>吴峥</span><span>12:12:12</span></p>
                                                <p class="chat-content">出来聊天</p>
                                            </li>
                                            <li class="history-item">
                                                <p class="chat-person"><span>吴峥</span><span>12:12:12</span></p>
                                                <p class="chat-content">出来聊天</p>
                                            </li>
                                            <li class="history-item">
                                                <p class="chat-person"><span>吴峥</span><span>12:12:12</span></p>
                                                <p class="chat-content">出来聊天</p>
                                            </li> -->
                                        </ul>
                                        <div class="chat-title-bar" style="border-bottom-right-radius: 8px;">
								<span class="chat-hist-btns ft-right">
									<i class="fa fa-angle-double-left fa-lg"
                                       onclick="getChatHistoryRecords(hisTotalPages-1,30,this);"></i>
									<i class="fa fa-angle-left fa-lg"
                                       onclick="getChatHistoryRecords(hisCurPage+1,30,this);"></i>
									<i class="fa fa-angle-right fa-lg"
                                       onclick="getChatHistoryRecords(hisCurPage-1,30,this);"></i>
									<i class="fa fa-angle-double-right fa-lg"
                                       onclick="getChatHistoryRecords(0,30,this);"></i>
								</span>
                                            <span class="ft-right" style="margin-right: 18px;">第<span
                                                    id="his-page-numb">1</span>页</span>

                                            <div style="clear:both;"></div>
                                        </div>

                                    </div>
                                </div>
                                <!-- 聊天记录 end -->
                            </div>
                        </div>

                    </div>

                </div>
                <div id="waitLoginmodal" class="modal hide fade" data-backdrop="static" style="top:50%;height:102px;margin-top:-50px;position: absolute!important;">
                    <img src="/img/waitting.gif">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;正在努力加载中...</img>
                </div>
            </div>


        </div>
    </div>
    <div id="fileModal" class="modal hide" role="dialog"
         aria-hidden="true" data-backdrop="static">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
                    aria-hidden="true">&times;</button>
            <h3>文件选择框</h3>
        </div>
        <div class="modal-body">
            <input type='file' id="fileInput" /> <input type='hidden'
                                                        id="sendfiletype" />
            <div id="send-file-warning"></div>
        </div>
        <div class="modal-footer">
            <button id="fileSend" class="btn btn-primary" onclick="sendFile()">发送</button>
            <button id="cancelfileSend" class="btn" data-dismiss="modal">取消</button>
        </div>
    </div>

    <!-- 页尾 -->
    <%@ include file="../common_new/foot.jsp" %>

    <%--引入群组聊天模板 --%>
    <jsp:include page="/WEB-INF/pages/groupchat/chatTemplateScript.jsp"></jsp:include>
    <%@ include file="/WEB-INF/pages/groupchat/chatPages.jsp" %>
    <%@ include file="/static/easymob/easymob-webim1.0/index.jsp" %>
</body>
</html>