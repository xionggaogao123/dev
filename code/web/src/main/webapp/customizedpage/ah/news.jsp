<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
        <meta name="renderer" content="webkit">
        <title>复兰科技 K6KT-快乐课堂</title>
        <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
        <link rel="stylesheet" type="text/css" href="/customizedpage/ah/css/style.css"/>
        <link rel="stylesheet" type="text/css" href="/customizedpage/ah/css/main.css"/>
        <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css" />
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
        <script src="/static/js/jquery.nivo.slider.js"></script>
        <script src='/customizedpage/ah/js/indexjq.js'></script>
        <%--<script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>--%>
        <script type='text/javascript' src='/static/js/k6kt-index.js'></script>
        <style>
        .login-bar {
			border-bottom: 3px solid #decaaa;
		}
        </style>

        <script>
            var type = ${param.type},
                    owner = ${param.owner};
        </script>
    </head>
    <body>
        <%@ include file="/WEB-INF/pages/common/ypxxhead.jsp"%>
		<div id="intro-player">
			<div id="player_div" style="width: 800px; height: 450px;">
                <script type="text/javascript" src="/plugins/sewiseplayer/sewise.player.min.js"></script>
			</div>
            <script type="text/javascript">
                SewisePlayer.setup({
                    server: "vod",
                    type: "m3u8",
                    skin: "vodFlowPlayer",
                    logo: "none",
                    lang: "zh_CN",
                    topbardisplay: 'disabled',
                    videourl: ''
                });
            </script>
			<span onclick="closeTheMovie()" class="close-dialog"><i class="fa fa-times-circle fa-lg"></i></span>
		</div>
        <div class="main-container">
            <div class="login-bar">
                <div class='title-bar-container' style="height:105px;">
                    <img class="title-logo" src="/business/customizedpage/special/ah/img/ah_k6kt.png">
                    <a class="login-btn" href="javascript:;">登 录</a>
                    <input class="input-password" type="password" placeholder="密码" tabindex="2">
                    <input class="input-account" type="text" placeholder="邮箱/手机号/用户名" tabindex="1">
                    <div id="tips-msg">
                        <a class="password-error">密码错误</a>
                        <a class="forget-pass" href='#'>忘记密码？</a>
                        <a class="username-error">用户名不存在</a>
                    </div>
                </div>
            </div>
        <!--     <div class="content-container">
            </div> -->
    <div style="width: 100%;background: #ffffff;">
        <div style="width: 900px;margin:0 auto;height: auto;background: #ffffff;overflow: visible;padding-top: 30px;">
            <div style="text-align: center;line-height: 2em;font-weight: bold;overflow: visible;">
                <dl style="float: none;font-size: 20px;">
                    <dt>在全省“基于微课的翻转课堂项目研究”观摩</dt>
                    <dd>研讨座谈会上的讲话摘要</dd>
                    <dd>程艺</dd>
                </dl>
            </div>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                安徽省“基于微课的翻转课堂项目研究”观摩研讨现场会今天在马鞍山二十二中召开，这次会议安排的内容丰富，既有专家学术报告、听课及教学研讨，又有实验阶段性成果交流和下一阶段项目研究工作的部署；因此，可以说这个会议开得非常及时、非常成功。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                刚才，我听了胡学平校长的一节翻转课，听了参加实验的学校代表发言；胡学平校长课上的很精彩，大家发言讲的也很好。下面就翻转课堂实验改革，谈几点感受。
            </p>
            <div style="font-size: 18px;font-weight: bold;text-indent: 2em;margin-top: 30px;overflow: visible">
                一、翻转课堂是对传统课堂的全面改革
            </div>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                翻转课堂作为一种产生于美国的新的教学模式，近年来迅速在我国成为又一重大课堂教学改革的象征性符号；它发展迅猛，已呈现“燎原”之势。翻转课堂缘何在中国如此迅速火热？什么是翻转课堂？翻转课堂翻转了什么？这些都是我们从事教育工作的人应该思考的问题。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                所谓“翻转课堂”，是指把“老师白天在教室上课，学生晚上回家做作业”的教学结构翻转过来，构建“学生白天在教室完成知识吸收与掌握的知识内化过程，晚上回家学习新知识”的教学结构。我认为，翻转课堂的核心问题，就是它是对教学流程的再造——尽管翻转课堂会涉及到内容的存储。一是翻转课堂对课堂教学的形式结构进行了颠倒，由传统的“课堂讲解＋课后作业”颠倒为“课前学习＋课堂研究”，
                与此相对应的先教后学的传统教学流程就颠倒为先学后教；二是翻转课堂改革了课堂教学的实质结构——教师、学生、教学内容的关系及空间结构，翻转课堂中，教师由传统的讲台上的权威，成为学生自主学习的指导者，学生真正地回到了学习的中心，成为知识的建构者；三是信息技术的使用，扩大了传统意义上“教”与“学”的空间。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                长期以来，我们的课堂教学改革基本思路就是对传统“教师中心、教室中心、知识中心”的颠覆，但主要集中在“课上”，对于从“课上＋课下”整体课堂观进行的改革尚未普遍触及。翻转课堂这一课堂形式的新变化，无异于是对传统课堂发生的一次深度裂变，为我们课堂教学改革提供了新的思路和方向。
            </p>
            <div style="font-size: 18px;font-weight: bold;text-indent: 2em;margin-top: 30px;overflow: visible">
                二、翻转课堂实施中要谨防“技术至上”误区
            </div>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                翻转课堂是由信息技术触发而且靠信息技术支撑的新的教学模式，在翻转课堂的教学流程中，供学生自主学习的教师授课的“微视频”，成了翻转课堂不可或缺的组成部分，这需要信息技术的深度介入以实现文本、图像、照片、动画、数据等教学信息在电脑、Pad等终端的可视化呈现，将课前与课上的时空限制打破并翻转过来。信息技术在课堂教学结构变革中的力量是不可小觑的，它为翻转课堂提供了丰富的教学资源和全方位的交互形式。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                我认为，翻转课堂是教育信息化的高级版本。如果说教育信息化最基本的追求是信息的传输，那么再到一个高度就是可以把信息通过信息化传输到一些接收不到的地方，比如说远端、远程教学，那么更高一个阶段就是翻转课堂。如果说，过去我们在课堂上利用信息化手段叫电化教学，这个电化教学为什么它没有产生一些革命性教育变化，我看核心问题就是把技术（信息技术）当作辅助手段。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                无论是历史的经验还是当下的理论和实践，均能证明，如若将教育教学改革完全寄托在信息技术层面，不仅是不理智的，也是危险的。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                纵览在我国发生频繁的诸多教学改革风潮——这些风潮中，我们不乏新的理念、新的模式，也不缺改革的勇气和决心，——为何难以从根本上解决课堂教学的痼疾？尽管在后来的反思中，我们找到的原因是多方面的，但决定课堂改革的关键因素——教师，概莫能外地被纳入影响教学改革成败的关键要素之中。审视当下的翻转课堂实践，有些教育行政部门或学校领导，将打造丰富的、优质的微视频或微课资源作为翻转课堂推进的“重”中之重；他们认为，
                如果优质的微课资源覆盖了中小学大部分学科的大部分内容，诸如包括几乎全部的学习重点、难点、疑点内容，而且这些资源能够共享，即便某些教师缺乏微课制作的时间和能力，那么，学生在家长或学校的督促下，课前自学微课，到了课堂上教师就不必重复讲解，主要进行答疑解惑、组织讨论、强化练习、安排活动，传统的课堂教学结构自然而然就被翻转过来了。这种将信息技术凌驾于教师之上甚至可以取代教师实现课堂翻转的想法和做法，尽管是有市场的，但却是不理智的。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                我认为，翻转课堂中教师和学生角色的颠倒使得教师责任更大了，更需要教师的责任心、爱心和专业技能。因此，可以断言：没有教师的观念转变、能力跟进、躬行践履，就没有翻转课堂的成功；“物化”的资源建设固然重要，实践中教师的“专业支持”更为关键。因此，决定课堂改革的关键因素——教师，在这场改革中，要得到更多的能力提升；教师专业成长的内容，要得到更大的丰富。
            </p>
            <div style="font-size: 18px;font-weight: bold;text-indent: 2em;margin-top: 30px;overflow: visible">
                三、信息化时代课堂教学中要注意处理好几个关系
            </div>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                今天对于胡学平校长讲的课，我觉得讲得非常好，正面的我就不评价了，但是我注意到胡学平校长课堂教学中，知识深度和宽度的关系把握的较好。我想，信息化条件下的教学改革要注意处理好三个关系：
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                一要处理好深度和宽度关系。这是我们必须要把握的，不同的年级、不同的年龄阶段学生要有不同的体现。信息化时代容易把知识变得很宽泛、很杂，但是在深度上面会有所疏忽。以数学为例，常态下的数学老师在黑板上演算，解题技巧会讲得很多，哪个地方一点点、小拐拐都讲得很清楚。进入信息时代以后，信息量比较大，这就要求教师在海量的信息中，如何去关注一定的知识深度——因为这影响到他们学生的考试（我们也要关心考试，关注学生的成绩）。
                我们希望翻转课堂能够把学生真正的素质能力提高——当然，它不是一个学期就能看出效果的——也许通过这个阶段的翻转课堂培养出来的学生，在解决各类问题，甚至包括他们在未来的工作中间，他的思维方式都有所改变；那么，我们要的就是这个效果！这就是我觉得教学内容的深度和宽度关系差别，一定要处理好。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                二是要处理好灵活和规范关系。传统的教学，一般严谨规范，但灵活性、趣味性不够。信息技术应用后，教学形式和手段灵活多样了；但同时，我们要注意不能因为灵活过度而导致教学内容、形式上散乱，破坏了知识的系统性传授。最近我在合肥学校听了一位新疆班老师的课，讲的也是高一的课，是一元二次方程，一元二次不等式。我感觉他的整个教学体系是规范有序的，但灵活不够。我认为课堂教学灵活性是非常需要的，因为灵活性是激发学生思维的很好的方式。
                你们当校长的可能都有这样体会，凡是在学校里面，好动的、爱打打球的——不管球打得怎么样，这些学生不会是差学生；虽然他学习成绩也未必是数一、数二的，但是他绝对不会是差学生。思维的灵活性在信息化的时代能够做到，但我们要把握一点：不要把灵活性变成一个散漫的、变成一个抛光的、变成一个万精油的东西，看起来，在课堂上小孩说出话来像是啥都懂：从宇宙到分子，从国际到国内，一来真格的，啥都不懂。我认为，灵活利用信息化，要更加强化课堂教学的灵活性，这个还是需要的。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                三是要处理好引导和思考的关系。信息化时代，翻转课堂中如何体现教师的引导与学生的思考？过去传统的教学方式，学生在班级听老师讲，讲完之后回去通过做作业来消化；翻转课堂改变了这个流程：翻转课堂首先在课前就已经把学生的主动性突出出来，课堂上更多的是教师在解说和生生、师生的互动。这种互动不是“你问我答”的传统意义上的那种“互动”，那是老师对课堂的把握：怎么引导才能让学生在课堂上最大化达成学习目标？平时教学，我怎么让学生去思考的？假如不用翻转课堂我能这样做吗？
                如果平常教学，我就能够达到这样的境界，我何苦要用翻转课堂？
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 18px;text-align: justify;margin-top: 30px;">
                从学生接受知识来看，如果不用信息技术手段，今天的课堂很难实现“当堂练习消化、个性化解惑、师生和生生互动，学生深度学习”等，因此，我们一定要用翻转课堂做“我过去想做而做不到”的事情，而且效果达到“想达到姑且达不到”的境界！这，可能就是我们要研究的核心问题。
            </p>
            <p style="text-indent: 2em;line-height: 2em;font-size: 16px;text-align: right;margin-top: 30px;width: 900px;font-weight: bold;">
                转载自《马鞍山教育信息》特刊第6期 马鞍山市教育局办公室（总第2330期）2015年4月26日
            </p>
        </div>
    </div>
            <!-- <div class='center-container' style="margin-top: 30px; margin-bottom: 30px; text-align:center;overflow:hidden;">
		        <div id="example" scroll="no" style="width:500px"></div>
		    </div> -->
        </div>
        <!-- 页尾 -->
        <%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>
        <!-- 页尾 -->
    </body>
</html>