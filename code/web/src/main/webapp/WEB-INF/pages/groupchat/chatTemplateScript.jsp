<%--
  Created by IntelliJ IDEA.
  User: 
  Date: 2014/12/24
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<script id="contact-group-template" type="text/html">
	
	<li id="contact_friend">
		<div class="new_center_main_popup_main_LT_title">
			<i class="fa fa-caret-right fa-2x" onclick="listToggle(this)"></i>
			我的好友
    		<img src="/img/jiahao_2.png" onclick="addAllToGroup(this)">
    	</div>
    	<ul class="group-list">
        {{if friendList && friendList.length > 0}}
		{{each friendList as friend friendId}}
    		<li user-id="{{friend.id}}">
            	<div class="new_center_main_popup_main_LT_info" user-id="{{friend.chatid}}" onclick="addToCharGroup(this,'{{friend.chatid}}','{{friend.imgUrl}}','{{friend.nickName}}')">
            		<img src="{{friend.imgUrl}}">
                	<span>{{friend.nickName}}</span>
            	</div>
        	</li>
		{{/each}}
        {{/if}}
    	</ul>
	</li>
	{{if nb_role2 == 1 && nb_role3==1}}
	<li id="contact_student">
		<div class="new_center_main_popup_main_LT_title">
			<i class="fa fa-caret-right fa-2x" onclick="listToggle(this)"></i>
			{{nb_role == 0 ? '同学':'学生'}}
    		<img src="/img/jiahao_2.png" onclick="addAllToGroup(this)">
    	</div>
    	<ul class="group-list">
        {{if studentsList && studentsList.length > 0}}
		{{each studentsList as student studentId}}
    		<li user-id="{{student.id}}">
            	<div class="new_center_main_popup_main_LT_info" user-id="{{student.chatid}}" onclick="addToCharGroup(this,'{{student.chatid}}','{{student.imgUrl}}','{{student.nickName}}')">
            		<img src="{{student.imgUrl}}">
                	<span>{{student.nickName}}</span>
            	</div>
        	</li>
		{{/each}}
        {{/if}}
	{{/if}}
    	</ul>
	</li>

{{if nb_role == 1 }}
	<li id="contact_teacher">
		<div class="new_center_main_popup_main_LT_title">
			<i class="fa fa-caret-right fa-2x" onclick="listToggle(this)"></i>
			老师
    		<img src="/img/jiahao_2.png" onclick="addAllToGroup(this)">
    	</div>
    	<ul class="group-list">
        {{if teachersList && teachersList.length > 0}}
		{{each teachersList as teacher teacherId}}
    		<li user-id="{{teacher.id}}">
            	<div class="new_center_main_popup_main_LT_info" user-id="{{teacher.chatid}}" onclick="addToCharGroup(this,'{{teacher.chatid}}','{{teacher.imgUrl}}','{{teacher.nickName}}')">
            		<img src="{{teacher.imgUrl}}">
                	<span>{{teacher.nickName}}</span>
            	</div>
        	</li>
		{{/each}}
        {{/if}}
    	</ul>
	</li>
	{{/if}}


	{{if nb_role == 1 }}
	<li id="contact_president">
		<div class="new_center_main_popup_main_LT_title">
			<i class="fa fa-caret-right fa-2x" onclick="listToggle(this)"></i>
			校领导
    		<img src="/img/jiahao_2.png" onclick="addAllToGroup(this)">
    	</div>
    	<ul class="group-list">
        {{if presidentList && presidentList.length > 0}}
		{{each presidentList as president presidentId}}
    		<li user-id="{{president.id}}">
            	<div class="new_center_main_popup_main_LT_info" user-id="{{president.chatid}}" onclick="addToCharGroup(this,'{{president.chatid}}','{{president.imgUrl}}','{{president.nickName}}')">
            		<img src="{{president.imgUrl}}">
                	<span>{{president.nickName}}</span>
            	</div>
        	</li>
		{{/each}}
        {{/if}}
    	</ul>
	</li>
	{{if nb_role2 == 1 }}
    <li id="contact_parent">
        <div class="new_center_main_popup_main_LT_title">
            <i class="fa fa-caret-right fa-2x" onclick="listToggle(this)"></i>
            家长
            <img src="/img/jiahao_2.png" onclick="addAllToGroup(this)">
        </div>
        <ul class="group-list">
        {{if parentsList && parentsList.length > 0}}
        {{each parentsList as parent parentId}}
            <li user-id="{{parent.id}}">
                <div class="new_center_main_popup_main_LT_info" user-id="{{parent.chatid}}" onclick="addToCharGroup(this,'{{parent.chatid}}','{{parent.imgUrl}}','{{parent.nickName}}')">
                    <img src="{{parent.imgUrl}}">
                    <span>{{parent.nickName}}</span>
                </div>
            </li>
        {{/each}}
        {{/if}}
	{{/if}}
        </ul>
    </li>

	{{/if}}
	
</script>

<script id="chat-group-list-template" type="text/html">	
{{if data && data.length > 0}}
{{each data as value index}}
	<li group-id="{{value.id}}" onclick="openChatPanel('{{value.id}}')">
       <div class="center_main_chat_info">
            
            <i class="fa fa-wechat fa-2x"></i>
            <span>{{value.groupname}}</span>
            <span class="center_main_chat_O">1</span>
       </div>
    </li>
{{/each}}
{{/if}}
</script>

<script id="chat-file-list-template" type="text/html">	
{{if data && data.length > 0}}
{{each data as value index}}
	<li id="{{value.id}}">
                <div class="center_main_popup_main_SX_info">
                    <div class="center_main_popup_main_SX_info_LT">
                        <div>{{value.fileName}}</div>
                        <div class="center_main_popup_main_SX_info_LT_I">
                            <span>{{value.filesize}}kb</span>
                            <span class="center_main_popup_main_SX_info_LT_II">.</span>
                            <span class="downloadtimes">{{value.count}}</span>次下载&nbsp;&nbsp;
                            <span>{{value.username}}</span>&nbsp;&nbsp;
                            <span>{{value.uploadtime}}</span>
                        </div>
                    </div>
                    <div class="center_main_popup_main_SX_info_RT">
                        <span onclick="$(this).closest('li').find('.downloadtimes').html(parseInt($(this).closest('li').find('.downloadtimes').html())+1);"><a onclick="downloadfile('{{value.id}}','{{value.filePath}}?download/{{value.fileName}}',this)" >下载</a></span>
                        <img src="images/xiala.png">

                    </div>
					{{if value.deleteable}}
                    <div class="center_main_popup_main_SX_info_XL">
                        <span onclick="deletefile('{{value.id}}',this)">删除</span>
                    </div>
					{{/if}}
                </div>
            </li>
{{/each}}	
{{/if}}
</script>

<script id="contact-group-seach-template" type="text/html">
	{{if seachList && seachList.length > 0}}
	<li>
    	<ul class="group-list" style="display: block">
		{{each seachList as student studentId}}
    		<li user-id="{{student.id}}">
            	<div class="new_center_main_popup_main_LT_info" user-id="{{student.chatid}}" onclick="addToCharGroup(this,'{{student.chatid}}','{{student.imgUrl}}','{{student.nickName}}')">
            		<img src="{{student.imgUrl}}">
                	<span>{{student.nickName}}</span>
            	</div>
        	</li>
		{{/each}}
    	</ul>
	</li>
	{{/if}}
</script>

<script id="chat-history-list-template" type="text/html">
{{if data && data.length > 0}}
    {{each data as chat chatId}}
    {{if chat.border}}
    <li class="history-date">{{chat.borderdate}}</li>
    {{/if}}
    <li class="history-item">
        <p class="chat-person"><span>{{chat.userName}}</span><span>{{chat.sendDate.substring(10)}}</span></p>
	{{if chat.chatContentList && chat.chatContentList.length >0}}
 		{{each chat.chatContentList as chatcont chatcontentid}}
		{{if chatcont.type=="emotion"}}
		<p3 class="chat-content"><img src="{{chatcont.data}}"></p3>
		{{/if}}
		{{if chatcont.type=="pic"}}
		<p3 class="chat-content"><img src="{{chatcont.data}}" width="200"></p3>
		{{/if}}
		{{if chatcont.type=="txt"}}
 		<p class="chat-content">{{chatcont.data}}</p>
		{{/if}}
		{{/each}}
	 {{/if}}
       
    </li>
    {{/each}}
{{/if}}
</script>

<script id="chat-latest-list-template" type="text/html">
{{if data && data.length > 0}}
    {{each data as latestRcd latestRcdId}}
    <%--{{if latestRcd.groupUserid == ${currentUser.id}}}--%>
	{{if latestRcd.groupUserid == ${sessionValue.chatid}}}
        <div style="text-align: right;" chat-id="{{latestRcd.id}}">
            <p1 class="chat-time">{{latestRcd.sendtime}}<b></b><br></p1>
            <p2><img class="chat-img chat-img-right" src="{{latestRcd.imageUrl}}">   <span></span>   </p2>
            <p4 class="chat-name chat-name-right"> {{latestRcd.userName}}</p4>
	{{if latestRcd.chatContentList && latestRcd.chatContentList.length >0}}
 		{{each latestRcd.chatContentList as chatcont chatcontentid}}
		{{if chatcont.type=="emotion"}}
		<p3 class="chat-content-p3-right"><img src="{{chatcont.data}}"></p3>
		{{/if}}
		{{if chatcont.type=="txt"}}
		<p3 class="chat-content-p3 chat-content-p3-right" classname="chat-content-p3">{{chatcont.data}}</p3>
		{{/if}}
		{{/each}}
	 {{/if}}
        </div>
    {{else}}
        <div style="text-align: left;" chat-id="{{latestRcd.id}}">
            <p1 class="chat-time">{{latestRcd.sendtime}}<b></b><br></p1>
            <p2><img class="chat-img chat-img-left" src="{{latestRcd.imageUrl}}">   <span></span>   </p2>
            <p4 class="chat-name chat-name-left"> {{latestRcd.userName}}</p4>
	{{if latestRcd.chatContentList && latestRcd.chatContentList.length >0}}
 		{{each latestRcd.chatContentList as chatcont chatcontentid}}
		{{if chatcont.type=="emotion"}}
		<p3 class="chat-content-p3-left"><img src="{{chatcont.data}}"></p3>
		{{/if}}
		{{if chatcont.type=="txt"}}
		<p3 class="chat-content-p3 chat-content-p3-left" classname="chat-content-p3">{{chatcont.data}}</p3>
		{{/if}}
		{{/each}}
	 {{/if}}
            
        </div>
    {{/if}}
    {{/each}}
{{/if}}
</script>