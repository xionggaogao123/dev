<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2014/12/18
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%--候选人身份信息模板--%>
<script id="candidate-role-template" type="text/html">

    {{if user.role}}
    {{if user.role & 2}}
    老师
    {{else if user.role & 4}}
    家长
    {{else if user.role & 1}}
    {{classInfo.className}}
    {{else}}
    校长
    {{/if}}
    {{/if}}
</script>
<%--候选人列表模板--%>
<script id="candidate-list-template" type="text/html">
    {{if voting}}

    {{each candidates as candidate candidateId}}

    <li class="candidate" user-id="{{candidate.id}}"  style="width: 160px;height: 105px;">
        <dl>
            <dd onclick="viewElect('{{id}}','{{candidate.id}}')" style="cursor: pointer;"><img
                    src="{{candidate.user.imgUrl}}" alt="" style="width:60px;height:60px;"/></dd>

            <dt>
            <p>{{candidate.name}}</p></dt>
            <dt>
            <p class="ellipsis" style="font-size: 12px;" title="{{include 'candidate-role-template' candidate}}">
                {{include 'candidate-role-template' candidate}}</p></dt>
       {{if voteRight}}
			{{if candidate.voted}}
            <dt class="tp-done">已投</dt>
            {{else if ballotCount > voted}}
            <dt>
                {{if begin}}
                <button class="tp-btn tp-tpbtn" type="button" onclick="vote(this)">投ta一票</button>
                {{else}}
                <button class="tp-btn tp-tpbtn " type="button" style="cursor:not-allowed;">未开始投票</button>
                {{/if}}
            </dt>
            {{/if}}
		{{/if}}
            <dt>
            <p class="tp-piao-count">{{candidate.ballots?candidate.ballots.length:0}}票</p></dt>
        </dl>
    </li>
    {{/each}}
    {{else}}
    {{each candidates as candidate candidateId}}
    <li class="candidate" user-id="{{candidate.id}}"  style="width: 160px;height: 105px;">
        <dl>
            <dd><img src="{{candidate.user.imgUrl}}" alt="" style="width:60px;height:60px;"/></dd>

            <dt>
            <p>{{candidate.name}}</p></dt>
            <dt>
            <p>{{include 'candidate-role-template' candidate}}</p></dt>

            <dt>
            <p class="tp-piao">
                {{candidate.ballots?candidate.ballots.length:0}}票</p></dt>
            {{if candidate.ballots != null && candidate.ballots.indexOf(${currentUser.id}) >= 0}}
            <dt class="tp-done">已投</dt>
            {{/if}}
        </dl>
    </li>
    {{/each}}
    {{/if}}
</script>
<%--选举投票列表模板--%>
<script id="electList" type="text/html">
    {{each content as elect index}}
    <div class="tp-con elect" elect-id="{{elect.id}}" publisher="{{elect.publisher}}">
        <div class="tp-con-top">
            <div class="tp-head" onclick="viewElect('{{elect.id}}')">
                <div class="tp-title">{{elect.name}}</div>
                {{if elect.voting}}
                <div class="tp-status tp-going" style="width: 70px">进行中</div>
                {{else}}
                <div class="tp-status tp-end" style="width: 70px">已结束</div>
                {{/if}}
            </div>
			<div style="margin-bottom: 5px;overflow: hidden;">
				<div class="tp-con-item tp-title-bar">投票规则：</div>
            	<div class="tp-rule">
                {{if elect.description}}
                {{elect.description.length > 200?elect.description.substring(0,200)+'...':elect.description}}
                {{/if}}
            	</div>
				<div style="clear:both;"></div>
			</div>
            <p><span class="tp-con-item">范围：</span>
                {{if elect.classIds == '' || elect.classIds == null || elect.classIds.length == 0}}
                全校
                {{else}}
                {{each elect.classes as classInfo}}
                <span>{{classInfo.className}}</span>
                {{/each}}
                {{/if}}
            </p>
			<p><span class="tp-con-item">参选范围：</span>
                {{if (!elect.teacherEligible && !elect.studentEligible && !elect.parentEligible)}}
				<span>自定义</span>
                {{else}}
					{{if elect.teacherEligible}}<span>老师</span>{{/if}}
					{{if elect.studentEligible}}<span>学生</span>{{/if}}
					{{if elect.parentEligible}}<span>家长</span>{{/if}}
                {{/if}}
            </p>
			<p><span class="tp-con-item">投票范围：</span>
                {{if elect.teacherVotable}}<span>老师</span>{{/if}}
				{{if elect.studentVotable}}<span>学生</span>{{/if}}
				{{if elect.parentVotable}}<span>家长</span>{{/if}}
            </p>
			{{if elect.ballotCount}}
			<p><span class="tp-con-item">每人票数：</span>
                <span>{{elect.ballotCount}}票</span>
            </p>
			{{/if}}
            <p><span class="tp-con-item">开始日期：</span>{{elect.startDate | day}}</p>
            <p><span class="tp-con-item">结束日期：</span>{{elect.endDate | day}}</p>
			<p><span class="tp-con-item">发起人：</span>{{elect.publishUser.nickName}}</p>
        </div>
        {{if elect.voting}}
        <div class="tp-con-people clearfix">
            <div class="tp-con-item tp-hxr tpz">候选人：</div>
            <div class="tp-p-detail">
                <ul class="clearfix brief">
                    {{if !elect.candidates || elect.candidates.length == 0}}
                    <span class="no-candidates">目前没有候选人哦~~快快报名吧!</span>
                </ul>
                {{else}}
                {{include 'candidate-list-template' elect}}
                </ul>
                {{if elect.candidates.length > 9}}
                <a class="expand-candidate-btn" onclick="expandCandidates(this)">
                    更多{{elect.candidates.length}}名候选人↓
                </a>
                {{/if}}
                {{/if}}
                {{if elect.voting}}
                <p class="tp-p-detail-btn">
                    {{if elect.signed}}
                    <button class="tp-btn tp-cancel" type="button" onclick="cancleSignup('{{elect.id}}',this)">
                        退出参选
                    </button>
                    {{else if elect.eligRight}}
                    <button class="tp-btn tp-handle" type="button" onclick="signup(this)">报名</button>
                    {{/if}}
                </p>
                {{else}}

                <p class="tp-p-detail-btn">
                    <button class="tp-btn tp-handle tp-morebtn" type="button" onclick="viewElect('{{elect.id}}')">更多详情
                    </button>
                </p>
                {{/if}}
            </div>
        </div>
        {{else}}
        <!-- 投票人票数 -->
        <div class="tp-con-people tp-res">
            <ul class="clearfix">
                {{if elect.candidates == null || elect.candidates.length == 0}}
                <span class="no-candidates">Oh no,竟然没有人参加竞选~~</span>
                {{else}}
                {{each elect.candidates as candidate candidatesIndex}}
                <li>
                    <dl>
                        <dd><img src="{{candidate.user.imgUrl}}" alt=""/></dd>
                        <dt class="tp-candidate-name">{{candidate.name}}</dt>
                        <dt class="tp-class">{{include 'candidate-role-template' candidate}}</dt>
                        <dt class="tp-bar">
                        <div class="tp-num ">
                            <div class="num-bar bar1" style="width:{{candidate.percent}}%"></div>
                            <span style="left:{{candidate.percent < 18? 18:(candidate.percent > 95?95:candidate.percent)}}%">{{candidate.ballots?candidate.ballots.length:0}}票</span>
                        </div>

                        </dt>
                    </dl>
                </li>
                {{/each}}
                {{/if}}
            </ul>
        </div>
        <!-- 投票人票数 -->
        {{/if}}
        {{if elect.voting}}
        <div class="tp-bm-box">
            <textarea class="tp-form-control" placeholder="请输入参选内容，点击下方按钮还可以添加图片、语音和视频！"></textarea>

            <div class="tp-bm-tool clearfix">
                <div class="tpz">
                    <label class="upload-tp-img" for="candidate-img-uploader-{{elect.id}}" style="cursor:pointer;"><img
                            src="/img/tp_tool_img.png" alt="" title="发表图片"/></label>
                    <a href="javascript:void(0);" onclick="showRecordflash(this,'{{elect.id}}')"><img
                            src="/img/tp_tool_yy.png" alt="" title="录制语音"/></a>
                    <a href="javascript:void(0);" onclick="chooseVideo(this)"><img src="/img/tp_tool_vedio.png" alt=""
                                                                                   title="上传视频"/></a>
                </div>
                <div class="size-zero">
                    <input id="candidate-img-uploader-{{elect.id}}" type="file" class="imgforvote" multiple="multiple"
                           accept="image/*"/>
                    <input id="candidate-vedio-uploader-{{elect.id}}" type="file" class="videoforvote"
                           accept="vedio/*"/>
                </div>
                <div style="padding-top: 10px;position: absolute;z-index: 50000;margin: 22px 0 0 18px;display: none;"
                     id="recordflash">
                    <div class="sanjiao"
                         style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;"></div>
                    <div id="myContent-{{elect.id}}">


                    </div>
                    <form id="uploadVoiceForm" name="uploadVoiceForm" action="/elect/upload.do">
                        <input name="authenticity_tokens" value="xxxxx" type="hidden">
                        <input name="file" value="1" type="hidden">
                        <input name="formats" value="json" type="hidden">
                    </form>
                </div>
                <div class="tpy">
                    <p class="tp-p-detail-btn">
                        <button class="tp-btn tp-handle" type="button" onclick="runForElect(this)">报名</button>
                    </p>
                </div>
            </div>
            <div class="attcmt-container">
                <div class="vote-img-container">
                    <ul></ul>
                </div>
                <div class="vote-audio-container">
                    <ul></ul>
                </div>
                <div class="vote-vedio-container">
                    <ul></ul>
                </div>
            </div>
        </div>
        {{/if}}
    </div>
    {{/each}}
</script>
<%--查看选举投票模板--%>
<script type="text/html" id="viewelect">
    <div class="tp-con elect" elect-id="{{id}}">
        <div class="tp-con-top">
            <div class="tp-head">
                <div class="tp-title">{{name}}</div>
				{{if '${sessionValue.id}' == publisher}}
				<span class="delete-elect" onclick="deleteElect('{{id}}')">删除</span>
				{{/if}}
                {{if voting}}
                <div class="tp-status tp-going" style="width: 70px;">进行中</div>
                {{else}}
                <div class="tp-status tp-end" style="width: 70px;">已结束</div>
                {{/if}}
            </div>

			<div style="margin-bottom: 5px;overflow:visible">
				<div class="tp-con-item tp-title-bar">投票规则：</div>
            	<div class="tp-rule">
                {{if description}}
                {{description.length > 200?description.substring(0,200)+'...':description}}
                {{/if}}
            	</div>
				<div style="clear:both;"></div>
			</div>

            <p><span class="tp-con-item">范围：</span>
                {{if classIds == '' || classIds == null || classIds.length == 0}}
                全校
                {{else}}
                {{each classes as classId classIndex}}
                <span>{{classId.className}}</span>
                {{/each}}
                {{/if}}
            </p>

			<p><span class="tp-con-item">参选范围：</span>
                {{if (!teacherEligible && !studentEligible && !parentEligible)}}
				<span>自定义</span>
                {{else}}
					{{if teacherEligible}}<span>老师</span>{{/if}}
					{{if studentEligible}}<span>学生</span>{{/if}}
					{{if parentEligible}}<span>家长</span>{{/if}}
                {{/if}}
            </p>
			<p><span class="tp-con-item">投票范围：</span>
                {{if teacherVotable}}<span>老师</span>{{/if}}
				{{if studentVotable}}<span>学生</span>{{/if}}
				{{if parentVotable}}<span>家长</span>{{/if}}
            </p>
			{{if ballotCount}}
			<p><span class="tp-con-item">每人票数：</span>
                <span>{{ballotCount}}票</span>
            </p>
			{{/if}}
            <p><span class="tp-con-item">开始日期：</span>{{startDate | day}}</p>
            <p><span class="tp-con-item">结束日期：</span>{{endDate | day}}</p>
			<p><span class="tp-con-item">发起人：</span>{{publishUser.nickName}}</p>
        </div>
        {{if voting}}
        <div class="tp-con-people clearfix tp-goning">
            <div class="tp-con-item tp-hxr tpz">参选人：</div>
            <div class="tp-p-detail">

                <ul class="clearfix">
                    {{if candidates == null || candidates.length == 0}}
                    <span class="no-candidates">目前没有候选人哦~~快快报名吧!</span>
                    {{else}}
                    {{include 'candidate-list-template' elect}}
                    {{/if}}
                </ul>

                <p class="tp-p-detail-btn">
                    {{if signed}}
                    <button class="tp-btn tp-cancel" type="button" onclick="cancleSignup('{{id}}',this)">
                        退出参选
                    </button>
                    {{else if eligRight}}
                    <button class="tp-btn tp-handle" type="button" onclick="signup(this)">报名</button>
                    {{/if}}
                </p>

            </div>
        </div>
        {{else}}
        <!-- 投票人票数 -->
        <div class="tp-con-people tp-res">
            <ul class="clearfix">
                {{if candidates == null || candidates.length == 0}}
                <span class="no-candidates">Oh no,竟然没有人参加竞选~~</span>
                {{else}}
                {{each candidates as candidate candidatesIndex}}
                <li>
                    <dl>
                        <dd><img src="{{candidate.user.maxImageURL}}" alt=""/></dd>
                        <dt class="tp-candidate-name">{{candidate.name}}</dt>
                        <dt class="tp-class">
                            {{candidate.classInfo?candidate.classInfo.className:candidate.user.role?'老师':'学生'}}
                        </dt>
                        <dt class="tp-bar">
                        <div class="tp-num ">
                            <div class="num-bar bar1" style="width:{{candidate.percent}}%"></div>
                            <span style="left:{{candidate.percent < 18? 18:(candidate.percent > 95?95:candidate.percent)}}%">{{candidate.ballots?candidate.ballots.length:0}}票</span>
                        </div>

                        </dt>
                    </dl>
                </li>
                {{/each}}
                {{/if}}
            </ul>
        </div>
        <!-- 投票人票数 -->
        {{/if}}
        <!-- 报名框 -->

        {{if voting}}
        <div class="tp-bm-box {{(candidates != null && candidates['${sessionValue.id}'] != null)?'hidden':''}}">
            <textarea class="tp-form-control" placeholder="请输入参选内容，点击下方按钮还可以添加图片、语音和视频！"></textarea>

            <div class="tp-bm-tool">
                <div class="tpz">
                    <label class="upload-tp-img" for="candidate-img-uploader-{{id}}"><img src="/img/tp_tool_img.png"
                                                                                          alt=""/></label>
                    <a href="javascript:void(0);" onclick="showRecordflash(this,'{{id}}')"><img
                            src="/img/tp_tool_yy.png" alt=""/></a>
                    <a href="javascript:void(0);" onclick="chooseVideo(this)"><img src="/img/tp_tool_vedio.png" alt=""/></a>
                </div>
                <div class="size-zero">
                    <input id="candidate-img-uploader-{{id}}" type="file" class="imgforvote" multiple="multiple"
                           accept="image/*"/>
                    <input id="candidate-vedio-uploader-{{id}}" type="file" class="videoforvote" accept="vedio/*"/>
                </div>
                <div style="padding-top: 10px;position: absolute;z-index: 50000;margin: 22px 0 0 18px;display: none;"
                     id="recordflash">
                    <div class="sanjiao"
                         style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;"></div>
                    <div id="myContent-{{id}}">


                    </div>
                </div>
                <div class="tpy">
                    <p class="tp-p-detail-btn">
                        <button class="tp-btn tp-handle" type="button" onclick="runForElect(this)">报名</button>
                    </p>
                </div>
            </div>
            <div class="attcmt-container">
                <div class="vote-img-container">
                    <ul></ul>
                </div>
                <div class="vote-audio-container">
                    <ul></ul>
                </div>
                <div class="vote-vedio-container">
                    <ul></ul>
                </div>
            </div>
        </div>
        {{/if}}
        <%--<!-- 报名框 -->--%>

        {{include 'intro-list-template'}}
    </div>

</script>
<%--候选人详细列表模板--%>
<script id="intro-list-template" type="text/html">
    <div id="intro-list">
        {{each candidates as candidate candidateIndex}}
		<a name="tp-intro-{{candidate.user.id}}" style="visibility: hidden"></a>
        <div class="tp-intro" candidate-id="{{candidate.user.id}}">
            <dl class="tp-intro-title clearfix">
                <dd><img src="{{candidate.user.imgUrl}}" alt="" class="info-user-img"/></dd>
                <dt class="name">{{candidate.name}}</dt>
                <dt class="time">{{candidate.signTime}}</dt>
            </dl>
            <div class="bm-toos-res">

                {{if candidate.manifesto != null && candidate.manifesto != ''}}
                <!-- 竞选宣言 -->
                <div class="vote-manifesto-container">
                    <p>{{candidate.manifesto}}</p>
                </div>
                <!--竞选宣言 -->
                {{/if}}

                {{if candidate.picUrls != null && candidate.picUrls.length > 0}}
                <!-- 图片 -->
                <div class="vote-img-container">
                    <ul>
                        {{each candidate.picUrls as picUrl picUrlIndex}}
                        <li>
                            <a class="fancybox" href="{{picUrl}}" data-fancybox-group="elect" title="预览">
                                <img class="candidate-img" src="{{picUrl}}" alt=""/></a>
                        </li>
                        {{/each}}
                    </ul>
                </div>
                <!-- 图片 -->
                {{/if}}
                <!-- 语音 -->
                {{if candidate.voiceUrl != null}}
                <div class="vote-audio-container">
                    <ul>
                        <p>
                            <a class="voice" onclick="playVoice('{{candidate.voiceUrl}}');" url="{{candidate.voiceUrl}}"
                               value="{{candidate.videoId}}"><img src="/img/yuyin.png" style="width:160px;height:22px;">播放</a>
                        </p>
                    </ul>
                </div>
                {{/if}}
                <!-- 语音 -->


                <!-- 视频 -->
                {{if candidate.videoId != null}}
                <div class="vote-vedio-container">
					<ul>
                    {{if candidate.videoId != null}}
						{{if candidate.video != null}}
                        <li data-id="{{candidate.videoId}}" onclick="playTheMovie('{{candidate.video.url}}')"><img
                                class="candidate-vedio-cover" src="{{candidate.video.imageUrl}}"><img
                                src="/img/play.png" class="video-play-btn"/></li>
						{{else}}
						视频信息正在处理...
						{{/if}}
                    {{/if}}
					</ul>
                </div>
                {{/if}}
                <!-- 视频 -->
            </div>
            {{if ('${sessionValue.id}' == candidate.user.id || '${sessionValue.id}'== publisher) && voting}}
            <button type="button" id="editMyInfo" class="btn btn-warning btn-right" onclick="editMyInfo(this)">修改</button>
            {{/if}}
		
        </div>

<!-- 参选信息编辑框-->
		{{if ('${sessionValue.id}' == candidate.user.id || '${sessionValue.id}'== publisher) && voting}}
		<div class="tp-bm-box" style="display: none;overflow: hidden;">
            <textarea class="tp-form-control" placeholder="请输入参选内容，点击下方按钮还可以添加图片、语音和视频！">{{(candidate.manifesto != null && candidate.manifesto != '')?candidate.manifesto:''}}</textarea>

            <div class="tp-bm-tool clearfix">
                <div class="tpz">
                    <label class="upload-tp-img" for="candidate-img-uploader-{{candidate.user.id}}" style="cursor:pointer;"><img
                            src="/img/tp_tool_img.png" alt="" title="发表图片"/></label>
                    <a href="javascript:void(0);" onclick="showRecordflash(this,'{{candidate.user.id}}')"><img
                            src="/img/tp_tool_yy.png" alt="" title="录制语音"/></a>
                    <a href="javascript:void(0);" onclick="chooseVideo(this)"><img src="/img/tp_tool_vedio.png" alt=""
                                                                                   title="上传视频"/></a>
                </div>
                <div class="size-zero">
                    <input id="candidate-img-uploader-{{candidate.user.id}}" type="file" class="imgforvote" multiple="multiple"
                           accept="image/*"/>
                    <input id="candidate-vedio-uploader-{{candidate.user.id}}" type="file" class="videoforvote"
                           accept="vedio/*"/>
                </div>
                <div style="padding-top: 10px;position: absolute;z-index: 50000;margin: 22px 0 0 18px;display: none;"
                     id="recordflash">
                    <div class="sanjiao"
                         style="width: 0;height: 0;border-left: 8px solid transparent;border-right: 8px solid transparent;border-bottom: 10px solid rgb(100,100,100);position:absolute;display: inline-block;top:0px;left:10px;"></div>
                    <div id="myContent-{{candidate.user.id}}">


                    </div>
                    <form id="uploadVoiceForm" name="uploadVoiceForm" action="/elect/upload.do">
                        <input name="authenticity_tokens" value="xxxxx" type="hidden">
                        <input name="file" value="1" type="hidden">
                        <input name="formats" value="json" type="hidden">
                    </form>
                </div>
                <div class="tpy">
                    <p class="tp-p-detail-btn">
                        <button class="tp-btn tp-handle" type="button" onclick="updateMyInfo(this,'tp-intro-{{candidate.user.id}}')">确认修改</button>
                    </p>
                </div>
            </div>
            <div class="attcmt-container">	
                <div class="vote-img-container">
                    <ul>
					{{if candidate.picUrls != null && candidate.picUrls.length > 0}}
					{{each candidate.picUrls as picUrl picUrlIndex}}
                        <li>
							<a class="fancybox" href="{{picUrl}}" data-fancybox-group="elect" title="预览">
							<img class="candidate-img" src="{{picUrl}}" alt=""/></a>
							<i class="fa fa-times blog-img-delete"></i>
                        </li>
                    {{/each}}
					{{/if}}
					</ul>
                </div>
                <div class="vote-audio-container">
                    <ul>
					{{if candidate.voiceUrl != null}}
					<p>
                        <a class="voice" onclick="playVoice('{{candidate.voiceUrl}}');" url="{{candidate.voiceUrl}}"
                               value="{{candidate.videoId}}"><img src="/img/yuyin.png" style="width:160px;height:22px;">播放</a>
						<a style="margin-left:20px;" onclick="$(this).closest('p').remove();"><img src="/img/dustbin.png"></a>
                    </p>
					{{/if}}
					</ul>
                </div>
                <div class="vote-vedio-container">
                    <ul>
					{{if candidate.videoId != null}}
						{{if candidate.video != null}}
                        <li data-id="{{candidate.video.id}}">
							<img class="candidate-vedio-cover" src="{{candidate.video.imageUrl}}">
							<img src="/img/play.png" class="video-play-btn" onclick="playTheMovie('{{candidate.video.url}}')"/>
							<i class="fa fa-times blog-img-delete"></i>
						</li>
						{{else}}
						视频信息正在处理...
						{{/if}}
                	{{/if}}
                    </ul>
                </div>
            </div>
        </div>
        {{/if}}
		<!-- 参选信息编辑框-->
        {{/each}}
    </div>
</script>