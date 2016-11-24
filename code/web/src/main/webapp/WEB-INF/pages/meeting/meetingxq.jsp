<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>我的会务/活动</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/meeting/hyandhd.css">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script language="javascript" type="text/javascript" src="/static_new/js/modules/calendar/0.1.0/WdatePicker.js"></script>
  <script type='text/javascript' src='/static/js/groupchat/strophe-custom-1.0.0.js'></script>
  <script type="text/javascript" src="/static/js/groupchat/easemob.im-1.0.0.js"></script>
  <script type="text/javascript" src="/static/js/groupchat/bootstrap.js"></script>
  <script type="text/javascript" src="/static/js/chat.js"></script>
  <script type="text/javascript">
    var chatUsers = '';
    var nb_username = '${sessionValue.userName}';
    var nickname =  '${sessionValue.userName}';
    var imageUrl =  '${sessionValue.maxAvatar}';
    var userId =  '${sessionValue.id}';
    var userRole = '${sessionValue.userRole}';
    var nb_chatid = '${sessionValue.chatid}';
    var curRoomId = '${roomId}';
    var curUserId = userId;
    var groupFlagMark = "group&-";
    var curChatUserId = "group&-${roomId}";
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
    //处理连接时函数,主要是登录成功后对页面元素做处理
    var handleOpen = function(conn) {
        //从连接中获取到当前的登录人注册帐号名
        curUserId = conn.context.userId;
        //获取当前登录人的联系人列表
        conn.getRoster({
            success : function(roster) {
                // 页面处理
                var curroster;
                for ( var i in roster) {
                    var ros = roster[i];
                    //both为双方互为好友，要显示的联系人,from我是对方的单向好友
                    if (ros.subscription == 'both'
                            || ros.subscription == 'from') {
                        bothRoster.push(ros);
                    } else if (ros.subscription == 'to') {
                        //to表明了联系人是我的单向好友
                        toRoster.push(ros);
                    }
                }

                conn.setPresence();

                conn.join({
                    roomId : curRoomId
                });
            }
        });

    };
    var handleLocationMessage = function(message) {
      var from = message.from;
      var addr = message.addr;
      var ele = appendMsg(from, from, addr);
      return ele;
    };
    //easemobwebim-sdk中收到联系人订阅请求的处理方法，具体的type值所对应的值请参考xmpp协议规范
    var handlePresence = function(e) {
      //（发送者希望订阅接收者的出席信息），即别人申请加你为好友
      if (e.type == 'subscribe') {
        if (e.status) {
          if (e.status.indexOf('resp:true') > -1) {
            agreeAddFriend(e.from);
            return;
          }
        }
        var subscribeMessage = e.from + "请求加你为好友。\n验证消息：" + e.status;
        showNewNotice(subscribeMessage);
        $('#confirm-block-footer-confirmButton').click(function() {
          //同意好友请求
          agreeAddFriend(e.from);//e.from用户名
          //反向添加对方好友
          conn.subscribe({
            to : e.from,
            message : "[resp:true]"
          });
          $('#confirm-block-div-modal').modal('hide');
        });
        $('#confirm-block-footer-cancelButton').click(function() {
          rejectAddFriend(e.from);//拒绝加为好友
          $('#confirm-block-div-modal').modal('hide');
        });
        return;
      }
      //(发送者允许接收者接收他们的出席信息)，即别人同意你加他为好友
      if (e.type == 'subscribed') {
        toRoster.push({
          name : e.from,
          jid : e.fromJid,
          subscription : "to"
        });
        return;
      }
      //（发送者取消订阅另一个实体的出席信息）,即删除现有好友
      if (e.type == 'unsubscribe') {
        //单向删除自己的好友信息，具体使用时请结合具体业务进行处理
        delFriend(e.from);
        return;
      }
      //（订阅者的请求被拒绝或以前的订阅被取消），即对方单向的删除了好友
      if (e.type == 'unsubscribed') {
        delFriend(e.from);
        return;
      }
    };
    var handleTextMessage = function(message) {
      //清空登录前打开的聊天框
//      $('div[id^="null-group&-"]').remove();
      var from = message.from;//消息的发送者
      var mestype = message.type;//消息发送的类型是群组消息还是个人消息
      var messageContent = message.data;//文本消息体
      //TODO  根据消息体的to值去定位那个群组的聊天记录
      var room = message.to;
      if (mestype == 'groupchat') {
        if (curRoomId==room) {
            appendMsg(message.from, message.to, messageContent, mestype);
        }
      } else {
        appendMsg(from, from, messageContent);
      }
    };
    //异常情况下的处理方法
    var handleError = function(e) {
      if (curUserId == null) {
//			hiddenWaitLoginedUI();
//        alert(e.msg + ",请重新登录");
//			showLoginUI();
      } else {
        var msg = e.msg;
        if (e.type == EASEMOB_IM_CONNCTION_SERVER_CLOSE_ERROR) {
          if (msg == "") {
            alert("服务器断开连接,可能是因为在别处登录");
          } else {
            alert("服务器断开连接");
          }
        } else {
          alert(msg);
        }
      }
    };
    //选择联系人的处理
    var getContactLi = function(chatUserId) {
      return document.getElementById(chatUserId);
    };
    //显示聊天记录的统一处理方法
    var appendMsg = function(who, contact, message, chattype) {
      //who的头像
      if (who == null) {
        return;
      }

//      var contactUL = document.getElementById("contactlistUL");
//      if (contactUL.children.length == 0) {
//        return null;
//      }
      var cid = contact;
      if (contact.indexOf("group&-")!=-1) {
        cid = contact.replace("group&-","");
      }

      var contactDivId = contact;
      if (chattype) {
        contactDivId = groupFlagMark + contact;
      }
//      var contactLi = getContactLi(contactDivId);
//      if (contactLi == null) {
//        if (unknowContact[contact]) {
//          return;
//        }
//        //alert("陌生人" + who + "的消息,忽略");
//        unknowContact[contact] = true;
//        return null;
//      }

      // 消息体 {isemotion:true;body:[{type:txt,msg:ssss}{type:emotion,msg:imgdata}]}
      var localMsg = null;
      if (typeof message == 'string') {
        localMsg = Easemob.im.Helper.parseTextMessage(message);
        localMsg = localMsg.body;
      } else {
        localMsg = message.data;
      }
      var date = new Date();
      var time = date.toLocaleTimeString();

      //根据who获取头像
      var myimg = $.grep(chatUsers,function(value,index){
        if(value.chatid == who){
          return value.imgUrl;
        }
      });
//      if (contact==curRoomId && !$(".chatRight").is(':hidden')) {
//        $.ajax({
//          url:'/groupchat/updateStatus.do',
//          data:{
//            roomid:curRoomId
//          },
//          type:'post',
//          success:function(data){
//          }
//        });
//      }
      /* var mypic = '';
       var myname = '';
       if(myimg != null && myimg.length > 0){
       mypic = myimg[0].maxImageURL;
       myname = myimg[0].userName;
       } */
      //"<p1 class='chat-time'>" + time + "<b></b><br/></p1>" ,
//      var headstr = ["<p1 class='chat-time'>" + time + "<b></b><br/></p1>" , "<p2><img class='chat-img' src='" + myimg[0].imgUrl + "'/>   <span></span>" + "   </p2>","<p4 class=\"chat-name\"> "+ myimg[0].userName +"</p4>"];
//      var header = $(headstr.join(''));
        var headstr ="<li><span>"+myimg[0].userName+"<em>" + time + "</em></span>";
//      var lineDiv = document.createElement("div");
//      for ( var i = 0; i < header.length; i++) {
//        var ele = header[i];
//        lineDiv.appendChild(ele);
//      }
      var messageContent = localMsg;
      for ( var i = 0; i < messageContent.length; i++) {
        var msg = messageContent[i];
        var type = msg.type;
        var data = msg.data;
        if (type == "emotion") {
          var eletext = "<p3><img src='" + data + "'/></p3>";
          var ele = $(eletext);
          for ( var j = 0; j < ele.length; j++) {
            lineDiv.appendChild(ele[j]);
          }
        }  else {
            headstr += "<p>" + data + "</p></li>";
//          var eletext = "<p3>" + data + "</p3>";
//          var ele = $(eletext);
//          ele[0].setAttribute("class", "chat-content-p3");
//          ele[0].setAttribute("className", "chat-content-p3");
          /*if (curUserId == who) {
           ele[0].style.backgroundColor = "#EBEBEB";
           }*/
//          for ( var j = 0; j < ele.length; j++) {
//            lineDiv.appendChild(ele[j]);
//          }
        }
      }
//      if (curChatUserId.indexOf(contact) < 0 || $('.chatRight').is(':hidden')) {
//        //contactLi.style.backgroundColor = "green";
//        var $notreadDom = $(contactLi).children('.notreadMsg-count');
//        var notreadCount = $notreadDom.html() != '' ? ($notreadDom.html().indexOf('+') < 0 ? parseInt($notreadDom.html()):100):0;
//        if(notreadCount< 100){
//          $notreadDom.html(notreadCount+1);
//        }else{
//          $notreadDom.html(notreadCount+'<b>+</b>');
//        }
//      }
//      $('#contactlistUL').prepend(contactLi);
//      var msgContentDiv = getContactChatDiv(contactDivId);
        $('.meeting-talk-ul').append(headstr);
//      if (curUserId == who) {
//        lineDiv.style.textAlign = "right";
//        $(lineDiv).addClass('msg-info-right');
//        $(lineDiv).find('p2 img').addClass('chat-img-right');
//        $(lineDiv).find('p3').addClass('chat-content-p3-right');
//        $(lineDiv).find('p4').addClass('chat-name-right');
//      } else {
//        lineDiv.style.textAlign = "left";
//        $(lineDiv).addClass('msg-info-left');
//        $(lineDiv).find('p2 img').addClass('chat-img-left');
//        $(lineDiv).find('p3').addClass('chat-content-p3-left');
//        $(lineDiv).find('p4').addClass('chat-name-left');
//      }


//      var create = false;
//      if (msgContentDiv == null) {
//        msgContentDiv = createContactChatDiv(contactDivId);
//        create = true;
//      }
//      msgContentDiv.appendChild(lineDiv);
//      if (create) {
//        //document.getElementById(msgCardDivId).appendChild(msgContentDiv);
//        $(msgContentDiv).hide();
//        $('#null-nouser').after(msgContentDiv);
//      }
//      msgContentDiv.scrollTop = msgContentDiv.scrollHeight;
//      return lineDiv;

    };
    var _interval;
    var sendText = function() {

      //未登录成功,增加waiting样式
      if (curUserId == null) {
        if($('#send_text').next('img').length == 0){
          $('#send_text').after('<img src="/img/loading4.gif">');
        }
        //尝试重发
        if(reSendCount > 0){
          _interval = setInterval(resendMsg,2500);
          function resendMsg(){
            sendText();
            reSendCount --;
          }
        }else{
          clearInterval(_interval);
        }
        return;
      }else{
        $('#send_text').next('img').remove();
        //删除登录前的聊天框
//        $('div[id^="null-group&-"]').remove();
        clearInterval(_interval);
      }
      textSending = true;

      var msg = $('.talk-enter').val();

      if (msg == null || msg.length == 0) {
        return;
      }
      $.ajax({
        url:'/meeting/addMessage.do',
        data:{
            content:msg,
            meetId:$('#mid').val()
        },
        type:'post',
        success:function(data){
        }
      });
      var to = curChatUserId;
      if (to == null) {
        return;
      }
      var options = {
        to : to,
        msg : msg,
        type : "chat",
        ext:{"attr1":nickname,"attr2":imageUrl}
      };
      // 群组消息和个人消息的判断分支
      if (curChatUserId.indexOf(groupFlagMark) >= 0) {
        options.type = 'groupchat';
        options.to = curRoomId;
      }
      //easemobwebim-sdk发送文本消息的方法 to为发送给谁，meg为文本消息对象
      conn.sendTextMessage(options);

      //当前登录人发送的信息在聊天窗口中原样显示
      var msgtext = msg.replace(/\n/g, '<br>');
      appendMsg(curUserId, to, msgtext);
//      turnoffFaces_box();
      $('.talk-enter').val('');
      $('.talk-enter').focus();

    };
    //easemobwebim-sdk中处理出席状态操作
    var handleRoster = function(rosterMsg) {
      for ( var i = 0; i < rosterMsg.length; i++) {
        var contact = rosterMsg[i];
        if (contact.ask && contact.ask == 'subscribe') {
          continue;
        }
        if (contact.subscription == 'to') {
          toRoster.push({
            name : contact.name,
            jid : contact.jid,
            subscription : "to"
          });
        }
        //app端删除好友后web端要同时判断状态from做删除对方的操作
        if (contact.subscription == 'from') {
          toRoster.push({
            name : contact.name,
            jid : contact.jid,
            subscription : "from"
          });
        }
        if (contact.subscription == 'both') {
          var isexist = contains(bothRoster, contact);
          if (!isexist) {
            var newcontact = document.getElementById("contactlistUL");
            var lielem = document.createElement("li");
            lielem.setAttribute("id", contact.name);
            lielem.setAttribute("class", "offline");
            lielem.setAttribute("className", "offline");
            lielem.onclick = function() {
              chooseContactDivClick(this);
            };
            /*var imgelem = document.createElement("img");
             imgelem.setAttribute("src", "/img/youjian_2.png");
             lielem.appendChild(imgelem);*/
            $(lielem).append('<i class="fa fa-wechat fa-2x"></i>');
            var spanelem = document.createElement("span");

            spanelem.innerHTML = contact.name;
            lielem.appendChild(spanelem);

            newcontact.appendChild(lielem);
            bothRoster.push(contact);
          }
        }
        if (contact.subscription == 'remove') {
          var isexist = contains(bothRoster, contact);
          if (isexist) {
            removeFriendDomElement(contact.name);
          }
        }
      }
    };
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
//      showChatUI();
        $.ajax({
          url:'/meeting/selMeetingDetail.do',
          data:{
              meetId:$('#mid').val()
          },
          type:'post',
          success:function(data){
              chatUsers = data.rows.chatUsers;
          }
        });

    }
    $(function () {
      openGroupChat();
    });
    </script>
</head>
<body>
<input hidden="hidden" id="mid" value="${meetId}">
<!--#head-->
<%@ include file="../common_new/head.jsp"%>
<!--/#head-->

<!--#content-->

<div id="content" class="clearfix">
  <div class="meetingjl-con">
    <h3 class="hyandhd-h3">${name}</h3>
    <div class="hyandhd-path">
      <span class="path-start">会务活动列表</span>
      <em>&gt;</em>
      <span>会议详情</span>
    </div>
    <div class="hyandhd-main clearfix">
      <div class="meetingxq-title">
        <c:if test="${flag}">
            <span class="meeting-JL"></span>
        </c:if>
          <c:if test="${check==1}">
            <a href="javascript:;" class="meeting-past">签到</a>
              <a href="javascript:;" class="meeting-leave" style="display: none;">签退</a>
          </c:if>
          <c:if test="${check==2 && isshow==1}">
            <a href="javascript:;" class="meeting-leave">签退</a>
          </c:if>
          <input hidden="hidden" id="show" value="${isshow}">
      </div>
      <div class="meetingxq-left">
        <ul class="meeting-ul clearfix">
          <li class="meeting-active"  id="HWYC">会务议程</li>
          <li id="FYRSX">发言人顺序</li>
          <li id="HYCL">会议材料</li>
          <li id="CHRY">参会人员</li>
          <li id="TP">投票</li>
          <li id="YT">议题</li>
        </ul>
        <div class="li-con">
          <!--======================参会人员start===========================-->
          <div class="meetingxq-list" id="tab-CHRY">
            <div class="meetingxq-qd meeting-CHRY_I">
              <span class="meeting-qd">已签到</span>
              <ul class="meeting-ren-list check-userlist">

              </ul>
            </div>
            <div class="meetingxq-wqd meeting-CHRY_II">
              <span class="meeting-wqd">未签到</span>
              <ul class="meeting-ren-list nocheck-userlist">

              </ul>
            </div>
            <div class="meeting-lx">
              <span class="meeting-lx-title"></span>
              <span class="meeting-mobile">手机：<em id="phone"></em></span>
              <span class="meeting-email">邮箱：<em id="email"></em></span>
            </div>
          </div>
          <!--======================参会人员end===========================-->
          <!--======================发言人顺序start===========================-->
          <div class="meetingxq-turn" id="tab-FYRSX">

          </div>
          <!--======================发言人顺序end===========================-->
          <!--======================会议材料start===========================-->
          <div class="meetingxq-upload" id="tab-HYCL">
            <%--<button class="meeting-upload-btn">上传</button>--%>
            <span class="iconvideo">
                            上传
                <input type="file" name="file_upload" id="file_upload"/>
                </span>
            <ul class="upload-list">

            </ul>
          </div>
          <!--======================会议材料end===========================-->
          <!--======================会议议程start===========================-->
          <div class="meeting-yc" id="tab-HWYC">
            <dl class="hyandhd-dl">
              <dt class="dl-title">会议议程</dt>
              <dd id="meetProcess"></dd>
            </dl>
          </div>
          <!--======================会议议程end===========================-->
          <!--======================投票start===========================-->
          <div class="meeting-TP" id="tab-TP">
            <span class="meetiong-TP-IMG"></span>
            <dl class="meeting-TP-dl">

            </dl>
            <script type="text/template" id="meeting-TP-dl_templ">
              {{ if(it.rows.voteDTOList.length>0){ }}
              {{ for (var i = 0, l = it.rows.voteDTOList.length; i < l; i++) { }}
              {{var obj=it.rows.voteDTOList[i];}}
              <dd class="dd-vote" vid="{{=obj.id}}" stus="{{=obj.status}}">
                <p>{{=obj.name}}</p>
                <em>
                  {{?obj.status==0}}
                  进行中
                  {{??}}
                  结束
                  {{?}}
                </em>
              </dd>
              {{ } }}
              {{ } }}
              <span class="meetiong-more">已没有更多</span>
            </script>
          </div>
          <!--======================投票end===========================-->
          <!--=======================议题start=========================-->
          <div class="meeting-YT" id="tab-YT">
              <div class="YT-info">

              </div>
              <script type="text/template" id="YT-info_templ">
                  {{ if(it.rows.issueList.length>0){ }}
                  {{ for (var i = 0, l = it.rows.issueList.length; i < l; i++) { }}
                  {{var obj=it.rows.issueList[i];}}
                  <li>
                      <em class="YT-ing">{{?i==0}}进行中{{??}}结束{{?}}</em><span>{{=obj.value}}</span>
                      {{?obj.type==1}}
                      <i class="YT-del" sid="{{=obj.id}}">删除</i>
                      {{?}}
                  </li>
                  {{ } }}
                  {{ } }}
              </script>
              <div class="YT-bottom">
                  <span id="showIss">新建议题</span>
              </div>
          </div>
          <!--=======================议题end===========================-->
        </div>
      </div>
      <div class="meetimgxq-right">
        <div  class="meetimgxq-right-I">
          <div class="meeting-right-title">
            <span>会议讨论</span>
          </div>
          <ul class="meeting-talk-ul">

          </ul>
          <div class="meeting-talk-enter clearfix">
            <textarea class="talk-enter" id="talkInputId"></textarea>
            <button id="send_text" onclick="sendText()">发送</button>
          </div>
        </div>
        <div  class="meetimgxq-right-II" >
          <div class="meetingxq-II" id="meetimgxq-right-II">
            <em class="meetiongxq-JX"></em>
            <div class="meetiongxq-name">
              <label><em id="voteUserName"></em><em id="time"></em></label>
              <p id="voteName"></p>
            </div>
            <ul class="vote-desc">

            </ul>
            <script type="text/template" id="vote-desc_templ">
              {{ if(it.rows.chooseDTOList.length>0){ }}
              {{ for (var i = 0, l = it.rows.chooseDTOList.length; i < l; i++) { }}
              {{var obj=it.rows.chooseDTOList[i];}}
              <li><input type="radio" name="radio" value="{{=obj.id}}"><p>{{=obj.name}}</p></li>
              {{ } }}
              {{ } }}
            </script>
            <div class="meetiongxq-tp">
              <input hidden="hidden" id="voteid">
              <span class="submit-vote">投票</span>
            </div>
            <div class="meetiongxq-del">
              <span class="del-vote"><em>X</em>删除投票</span>
            </div>
          </div>

          <div  class="meetimgxq-right-II" id="meetimgxq-right-III">
            <div class="meetingxq-II">
              <em class="meetiongxq-JX"></em>
              <div class="meetiongxq-name">
                <label><em id="voteUserName2"></em><em id="time2"></em></label>
                <p id="voteName2"></p>
              </div>
              <ul class="vote-desc2">

              </ul>
              <script type="text/template" id="vote-desc_templ2">
                {{ if(it.rows.chooseDTOList.length>0){ }}
                {{ for (var i = 0, l = it.rows.chooseDTOList.length; i < l; i++) { }}
                {{var obj=it.rows.chooseDTOList[i];}}
                <li cid="{{=obj.id}}" cnm="{{=obj.name}}">{{?obj.chooseId==obj.id}}<img src="/static_new/images/meeting/meeting-rig.jpg">{{?}}<p>{{=obj.name}}</p><em>{{=obj.count}}票</em></li>
                {{ } }}
                {{ } }}
              </script>
            </div>
            <div class="meetiongxq-del">
              <span class="del-vote2"><em>X</em>删除投票</span>
              <span><img src="/static_new/images/meeting/meetingxq-prople.jpg">参与投票的人员<i id="cnt">(19)</i></span>
            </div>
          </div>
        </div>
      </div>
    </div>


  </div>
  <!--/#content-->
  <!--====================弹出框====================-->
  <!--======================发起新投票start==============================-->
  <div class="meeting-FQTP">
    <div class="meeting-top">
      <span>发起新投票</span>
      <i>X</i>
    </div>
    <div class="meeting-FQTP-M">
      <dl>
        <dd>
          <em>投票主题</em>
          <textarea placeholder="请填写投票内容说明" id="theme"></textarea>
        </dd>
        <dl id="chooselist">
          <dd>
            <em>投票选项</em>
            <input id="">
            <%--<i>X</i>--%>
          </dd>
        </dl>
        <dd>
          <span id="addChoose">+添加选项</span>
        </dd>
        <dd class="meeting-FQTP-bottom">
          <span class="FQTP-QD">确定</span><span class="FQTP-QX">取消</span>
        </dd>
      </dl>
    </div>
  </div>
  <!--======================发起新投票end==============================-->
  <!--======================投票详情start==============================-->
  <div class="meeting-TPXQ">
    <div class="meeting-top">
      <span>投票详情</span>
      <i>X</i>
    </div>
    <div class="meeting-TPXQ-M">
      <div class="meetiong-TPXQ-sk"></div>
    </div>
    <div class="meetion-TPXQ-bottom">
      <em>投改选项的人：</em>
      <ul class="users">

      </ul>
      <script type="text/template" id="users_temp">
        {{ if(it.users.length>0){ }}
        {{ for (var i = 0, l = it.users.length; i < l; i++) { }}
        {{var obj=it.users[i];}}
        <li>
          <img src="{{=obj.imgUrl}}">
          <em>{{=obj.userName}}</em>
        </li>
        {{ } }}
        {{ } }}
      </script>
    </div>
  </div>
  <!--======================投票详情end==============================-->
  <!--==================新建议题弹出框start=====================-->
  <div class="YT-popup">
      <div class="YT-top">
          <em>新建议题</em><i id="closeIssue">X</i>
      </div>
      <div class="YT-popup-info">
          <em>议题：</em><input placeholder="请输入议题" id="issue">
      </div>
      <div class="YT-popup-bottom">
          <span id="sureIssue">确定</span>
      </div>
  </div>
  <!--==================新建议题弹出框end=======================-->
</div>
  <div class="zhiban-meng"></div>
  <!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
  <!--#foot-->

<!--=================添加模板end=====================-->
<%--<%@ include file="/static_new/easymob/easymob-webim1.0/index.jsp" %>--%>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('meetxq');
</script>
</body>
</html>
