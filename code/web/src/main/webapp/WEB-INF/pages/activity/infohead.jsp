<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.pojo.user.UserRole"%>
<link rel="stylesheet" type="text/css"
	href="../static/css/common/info-banner.css" />

<script type="text/javascript" src="../static/js/common/info-banner.js"></script>
<div id="info-banner-container" style="display: none">
	<div class="info-banner">
		<img src="${userInfo.imgUrl}" />
		<div class="control-bar">
		<c:choose>
		<c:when test="${bannerType eq 'headmaster'}">
					<span class="item active">微校园</span>
					<span class="item">微家园</span>
					<span class="item">班级</span>
					<span class="item">老师</span>
					<span class="item">通知</span>
				</c:when>
				<c:when test="${bannerType eq 'userCenter'}">
					<a href="/message"><span
						class="item    ${menuIndex==0?'active':'' }">我的私信</span></a>
					<a href="/basic"><span
						class="item ${menuIndex==1?'active':'' }">个人设置</span></a>
					<a href="/business/reverse/user/userHelp.jsp"><span
						class="item  ${menuIndex==2?'active':'' }">用户手册</span></a>
					<a href="/friendcircle/friendlist"><span
						class="item  ${menuIndex==3?'active':'' }">我的好友</span></a>
					<c:if test="${role:isTeacher(userInfo.role)}">
						<a href="/myColleagues"><span
							class="item  ${menuIndex==4?'active':'' }">我的同事</span></a>
					</c:if>
				</c:when>
				<c:when
					test="${param.bannerType eq 'homePage' && template eq 'student' && studentList eq 'List'}">
					<c:forEach items="${classList}" var="classInfo">
						<span cid="${classInfo.classid}"
							classtype="${classInfo.classtype}" class="item class-item">${classInfo.classname}</span>
					</c:forEach>
				</c:when>
				<c:when
					test="${bannerType eq 'coursePage' && template eq 'student'}">
					<a href="/cloudClass"><span
						class="item ${menuIndex==0?'active-l':'' }">云课程</span></a>
					<a href="/student/course"><span
						class="item ${menuIndex==1?'active':'' }">班级课程</span></a>
				</c:when>
				<c:when test="">
					<a href="/petbag"><span id="petbag"
						class="item ${menuIndex==0?'active':'' }">宠物背包</span></a>
					<a href="/petCenter"><span id="petcenter"
						class="item ${menuIndex==1?'active':'' }">宠物中心</span></a>
					<a href="/studentScoreList"><span id="scoreList"
						class="item ${menuIndex==2?'active':'' } ">历史积分</span></a>
				</c:when>
			</c:choose>


		</div>

		<c:choose>
			<c:when test="${role:isStudentOrParent(userInfo.role)}">
				<a href="/business/reverse/user/userHelp.jsp#studentScore"
					class="help-score">如何获取经验值</a>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when
						test="${role:isHeadmaster(userInfo.role)|| role:isK6ktHelper(userInfo.role)}">
						<a href="/business/reverse/user/userHelp.jsp#headmasterScore"
							class="help-score">如何获取经验值</a>
					</c:when>
					<c:otherwise>
						<a href="/business/reverse/user/userHelp.jsp#teacherScore"
							class="help-score">如何获取经验值</a>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		<div id="pettalk_area" style="display: none;">
			<span id="arrow"></span> <i id="msgclose" class="fa fa-times-circle"></i>
			<span id="talkmsg"></span>
		</div>
		<a href="/petbag" class="pet">
			<div id="choosenPet">
				<c:choose>
					<c:when
						test="${userInfo.experienceValue >= 0 && userInfo.experienceValue < 50}">
						<img src="../img/tree-1.png" />
					</c:when>
					<c:when test="${userInfo.experienceValue >= 50}">
						<img src="" />
						<input id="petid" value type="hidden" />
						<script type="text/javascript">
							getPetInfo();
						</script>
					</c:when>
				</c:choose>
			</div>
		</a>
		<c:if test="${bannerType eq 'homePage' && template eq 'teacher'}">
			<a id="look_ad" onclick="lookDialog()"><img
				src="../img/look_ad.png" /></a>
		</c:if>
		<span onclick="changePetName()" class="change-name">改名</span>
		<div class="pet-name"
			title="${userInfo.petName==null?userInfo.petName:userInfo.userName+'的成长树'}">
			${userInfo.petName==null?userInfo.petName:userInfo.userName+'的成长树'}</div>

		<input class="input-name" type="text"
			onkeydown="petNameInputKeyDown(event)" onblur="petNameInputBlur()">
		<!-- <a href="/petbag"> -->
		<div class="user-exp-div">
			<img class="user-exp" src="/img/exp.png"> <span
				class="exp-title">经验值</span>
			<div class="exp-number">
				<span>${userInfo.experienceValue}</span>
			</div>
		</div>
		<!-- </a> -->
	</div>
</div>

<div style="display: none;">
	<ul id="petmsg-list">
		<li data-id="1">主人我等了你好久，你终于来了；</li>
		<li data-id="2">我是很笨，但是我很认真，让我陪主人一起学习吧；</li>
		<li data-id="3">主人，不要忘了我呀；</li>
		<li data-id="4">好久没见到主人了，我好激动，么么哒！</li>
		<li data-id="5">好好学习，天天向上，么么哒！</li>
		<li data-id="6">主人，快点加油哦，隔壁同学的学习进度快超过我们啦!</li>
		<li data-id="7">只要路是对的，就不怕路远。</li>
		<li data-id="8">没有天生的信心，只有不断培养的信心。</li>
		<li data-id="9">主人,你想我了吗?</li>
		<li data-id="10">今天我想了你888次,比昨天多了一次哦</li>
		<li data-id="11">主人,吃饱了总得干点什么，老师推送的课程你都学完了嘛？</li>
		<li data-id="12">不耻下问,一直是我学习的动力. </li>
		<li data-id="13">主人,我已经下定决心跟你过一辈子了,让我多陪陪你吧. </li>
		<li data-id="14">主人,我去看视频学习了。</li>
		<li data-id="15">吃饱了,穿暖了,也该给我打扮打扮了吧,主人?</li> 
		<li data-id="16">主人我希望快快长大,这样我就能学到更多更有用的东西了</li>
		<li data-id="17">学习光荣,学习伟大,学习是我最基本的需要. </li>
		<li data-id="18">主人对我真好啊.我最喜欢主人</li>
		<li data-id="19">你再不来看我，我要告你虐待宠物了。</li>
		<li data-id="20">锄禾日当午，汗滴禾下土。努力挣积分，个个皆辛苦. </li>
		<li data-id="21">主人，慢了慢了，速度，速度. </li>
		<li data-id="22">主人，老师说我的腰围和我的智商一样.</li>
		<li data-id="23">主人 我是不是你的骄傲啊 </li>
		<li data-id="24">我是主人的小乖乖,呵呵 </li>
		<li data-id="25">我要睡觉觉,请小小声,Zzz </li>
		<li data-id="26">一年前的这个时候,主人正在干嘛呢?好像时间倒流,再回去看看哦</li>
		<li data-id="27">保护生命之水，需要从节约用水做起，我洗澡从来不用水，嘿嘿。</li>
		<li data-id="28">身体是学习的资本，所有呢，主人要好好照顾自己哦~</li>
		<li data-id="29">主人，别怪我说你，老师布置的作业可做完了咩~</li>
		<li data-id="30">脖子扭扭，屁股扭扭，主人跟我一起来做运动吧!</li>
		<li data-id="31">主人，你发几张照片给我看看吧，伦家想你了~</li>
		<li data-id="32">生活如此美妙，我却如此烦躁，这样不好，不好~</li>
		<li data-id="33">早睡早起身体好，主人早上赖床了嘛~</li>
		<li data-id="34">主人，相信我！学习是一份很有前途的事业~</li>
		<li data-id="35">主人，你说，飞机飞那么高都撞不到星星，是不是因为星星会闪吖~</li>
		<li data-id="36">不抱有一丝幻想，不放弃一点机会，不停止一日努力~</li>
		<li data-id="37">只有脚踏实地的人，才能够说：路，就在我的脚下。</li>
		<li data-id="38">主人，下次放学后，你和小伙伴出去玩的时候能带上我咩~</li>
		<li data-id="39">主人，乘你青春风发，努力撒播幸福的种子吧!</li>
		<li data-id="40">主人，跟我谈谈你的理想吧，么么哒~</li>
		<li data-id="41">主人，辞海是海嘛，里面都有什么鱼呀~</li>
		<li data-id="42">主人，你可别在光线不好的地方学习哦，容易伤眼睛。</li>
		<li data-id="43">主人，只要你多看一些视频学习，我就能升级啦，啦啦啦~</li>
		<li data-id="44">我还差一点就升级了耶，主人快帮帮我吧~</li>
		<li data-id="45">时间可以磨去棱角，有些支持却永远磨不掉唻!</li>
		<li data-id="46">主人，你说胆是不是人身上最大的器官呢，要不然人家怎么说“胆大包天”呢~</li>
		<li data-id="47">主人，你说我为什么老师盼着放假呢？</li>
		<li data-id="48">主人，我们都要讲究卫生，勤洗手这样才能更好的保护身体哦</li>
		<li data-id="49">主人，有不会的问题记得要去问老师和同学哟~</li>
		<li data-id="50">主人，谢谢你陪我一起学习，真的~</li>
		<li data-id="51">人生嘛，吃一堑，长一智，伤害就是明白，失败就是懂得，主人你说是不是这个道理!</li>
		<li data-id="52">主人，隔壁小胖的宠物等级快超过我啦，快加油学习哦!</li>
		<li data-id="53">主人，为什么我的等级升的这么慢吖~是不是你偷懒啦~~~</li>
		<li data-id="54">我要升级啦！万万没想到，啦啦啦啦啦~~~~</li>
		<li data-id="55">主人，我愿用一身的肥肉换你学业进步，还有人比我对你更好的吗！!</li>
		<li data-id="56">帆的自豪，是能在风浪中挺起胸膛。主人，你以我为荣，不，我以你为荣！嘿嘿</li>
		<li data-id="57">你既然认准一条道路，何必去打听要走多久。走自己的路，让别人说去吧~~</li>
		<li data-id="58">主人，你的小伙伴周末会来找你玩嘛？</li>
		<li data-id="59">只要有信心，人永远不会挫败。主人你说是不是？</li>
		<li data-id="60">信心来自于实力，实力来自于勤奋。主人不要在学习上输给其他同学哦，我给你加油呢!加油，加油。。。</li>
		<li data-id="61">主人，无止境的温柔我只能对你一个人。么么哒~~</li>
		<li data-id="62">主人，考试前你会紧张嘛</li>
		<li data-id="63">长了青春痘才算有过青春，主人，你有没有长青春痘嘛~</li>
		<li data-id="64">主人，你经常都学习了什么课程呀？教教我吧。</li>
		<li data-id="65">你们的偶像是明星，我的偶像是卫星，啦啦啦!</li>
		<li data-id="66">主人，你是喜欢EXO还是TFboys多一些呢？</li>
		<li data-id="67">小孩子才分对错，成年人只看利弊，哼，我只分对错，当小孩子倒是挺好的~</li>
		<li data-id="68">主人，你可不要打游戏哦，视频还没有学完呢!</li>
		<li data-id="69">出门走好路，出口说好话，出手做好事。明天又是新的一天呢？</li>
		<li data-id="70">咦，是不是我看错了，主人，你们老师又推荐课程了。</li>
		<li data-id="71">成功的法则极为简单，但简单并不代表容易。 学习就很不容易耶~</li>
		<li data-id="72">主人，你们学校有没有比较好玩的事情呀~~</li>
		<li data-id="73">主人，微校园里面的那群人又聊起来了，你也去凑凑热闹吧!</li>
		<li data-id="74">“听麻麻的话，听妈妈的话 别让她受伤 想快快长大 才能保护她”主人我歌唱的好不好？</li>
		<li data-id="75">主人，你的作业提交了吗？老师会检查的，可别忘了哟~~~</li>
		<li data-id="76">主人，你看了同学排行了吗，最近要加油了哦~~~</li>
		<li data-id="77">主人，你有试过给小伙伴发送私信了吗？</li>
		<li data-id="78">主人，上次看的那个视频内容你学会了吗，要不再学一遍吧!</li>
		<li data-id="79">亲爱的主人,我很勤快的,今天又学习了不少知识呢!</li>
		<li data-id="80">主人,我去学习了. 回头再找你聊天啊</li>
		<li data-id="81">出来混，始终是要还的 ，主人，还有什么要吩咐的?!</li>
		<li data-id="82">我是主人最爱的小宠物，我最爱学习呢!</li>
		<li data-id="83">主人，今天我心情非常好，快点带我一起去学习吧!</li>
		<li data-id="84">一八得八，二八一十六、三八妇女节.主人，我算的没错吧!</li>
		<li data-id="85">又开始上课了，我要开启战斗模式啦~~</li>
		<li data-id="86">玉不琢，不成器。若不学，不知礼，做错事，勇承认，尊长辈。习礼仪。</li>
		<li data-id="87">主人，累了的时候，给自己一个鼓励的微笑，让疼痛不那么彻底；寂寞的时候，对着镜子来一个微笑，告诉自己，其实你并不孤单。</li>
		<li data-id="88">主人，谢谢你，我比所有人的宠物都幸福!</li>
		<li data-id="89">追梦的日子，感谢有你的鼓励，高兴的日子，感谢有你的倾听，失落的日子，感谢有你的陪伴，烦恼的日子，感谢有你的开导，谢谢你，我最爱的小伙伴！~~~</li>
		<li data-id="90">好怀念放假啊，只要作业做完了，就可以出去玩了。</li>
		<li data-id="91">-_-#,主人你是不是偷懒了？</li>
		<li data-id="92">世上并没有用来鼓励工作努力的赏赐，所有的赏赐都只是被用来奖励工作成果的。主人好好学习，就是对我最大的奖励，么么哒~~~~~</li>
		<li data-id="93">再长的路，一步步也能走完，再短的路，不迈开双脚也无法到达。走啊走。。。走啊走。。。</li>
		<li data-id="94">有志者自有千计万计，无志者只感千难万难。小主，你是有志者吗？</li>
		<li data-id="95">成绩差了也不怕，只要有信心撑腰，下次可以考的更好。</li>
		<li data-id="96">静坐常思己过，闲谈莫论人非。主人，你说我说的对不对嘛？~~~</li>
		<li data-id="97">只要不放弃努力和追求，小草也有点缀春天的价值，何况是热爱学习的人呢~~</li>
		<li data-id="98">我也需要自己的空间，你快去找你的同学去玩，别来烦我了~~</li>
	</ul>
</div>