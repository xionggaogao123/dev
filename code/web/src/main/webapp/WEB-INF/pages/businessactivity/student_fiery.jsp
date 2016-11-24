<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>平台运营活动</title>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="css/fiery.css">
<script type="text/javascript" src="/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript">
	function showActivity(id) {
		jQuery(".fiery-left-bottom").hide();
		jQuery(".divcss").hide();
		jQuery("#" + id + "_detail").show();
	}
</script>
<script type="application/javascript">
    function Fiery(){
       jQuery(".divcss").hide();
       jQuery(".fiery-left-bottom").show();
    }
</script>
</head>


<body>


<%@ include file="../common_new/head.jsp" %>
 <div style="width:1000px;margin:0 auto;">
<div id="content_main_container">
    <div id="content_main">
     
       <%@ include file="../common_new/col-left.jsp" %>
        
        <div id="right-container">
		     <div class="fiery-left">
		<div class="fiery-left-top">
			<span class="fiery-left-top-huodong">火热活动</span>
		</div>
		<div class="fiery-left-bottom">
			<ul>
                <div>
                    <li id="activity9" onclick="showActivity('activity9')">
                        <div class="fiery-left-li">
                            <img src="img/fiery-dian.jpg"> <span
                                class="fiery-left-bottom-I">[进行中]</span> <span
                                class="fiery-left-bottom-II">时光漫游----我与爸爸妈妈童年不同样 </span> <span
                                class="fiery-left-bottom-III"></span>
                        </div>
                        <div class="fiery-right-li">
                            <span>05-15——06-15</span>
                        </div>
                    </li>
                </div>
                <div>
                    <li id="activity1" onclick="showActivity('activity1')">
                        <div class="fiery-left-li">
                            <img src="img/fiery-dian.jpg"> <span
                                class="fiery-left-bottom-I">[进行中]</span> <span
                                class="fiery-left-bottom-II">爱心公益，传递能量 </span> <span
                                class="fiery-left-bottom-III"></span>
                        </div>
                        <div class="fiery-right-li">
                            <span>长期</span>
                        </div>
                    </li>
                </div>
				<%--<div>
					<li id="activity5" onclick="showActivity('activity5')">
						<div class="fiery-left-li">
							<img src="img/fiery-dian.jpg"> <span
								class="fiery-left-bottom-I">[进行中]</span> <span
								class="fiery-left-bottom-II">智慧校园大使征集 领千元奖金</span> <span
								class="fiery-left-bottom-III"></span>
						</div>
						<div class="fiery-right-li">
							<span>02.13——04-30</span>
						</div>
					</li>
				</div>
                <div>
                    <li id="activity3" onclick="showActivity('activity3')">
                        <div class="fiery-left-li">
                            <img src="img/fiery-dian.jpg"> <span
                                class="fiery-left-bottom-I">[进行中]</span> <span
                                class="fiery-left-bottom-II">我和春天有个约会 </span> <span
                                class="fiery-left-bottom-III"></span>
                        </div>
                        <div class="fiery-right-li">
                            <span>03-31——05-31</span>
                        </div>
                    </li>
                </div>
                <div>
                    <li id="activity10" onclick="showActivity('activity10')">
                        <div class="fiery-left-li">
                            <img src="img/fiery-dian.jpg"> <span
                                class="fiery-left-bottom-I">[进行中]</span> <span
                                class="fiery-left-bottom-II">快来晒出你的儿童节校园活动 </span> <span
                                class="fiery-left-bottom-III"></span>
                        </div>
                        <div class="fiery-right-li">
                            <span>06-01——06-10</span>
                        </div>
                    </li>
                </div>--%>
			</ul>
		</div>
		<!--=========================================跳转页面=======================================-->

	<%--	<div class="divcss" id="activity0_detail" >
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
                <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;min-height: 400px;width:900px;">
                    <div class="fiery-skip-top">
                        <span class="fiery-skip-I">发布微校园动态，在线领取教师进修基金</span>
                        <span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;"id="fiery_01" onclick="Fiery()" >x</span><br>
                        <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                    </div>
                    <div class="fiery-skip-bottom">
                        <div class="fiery-skip-botton-I">
                            <span class="fiery-left-botton-II">活动时间：</span><span
                                class="fiery-left-botton-III">02.03——03-30</span>
                        </div>
                        <div class="fiery-skip-botton-I">
                            <span class="fiery-left-botton-II">活动奖励：</span></span><span
                                class="fiery-left-botton-III">在线领取教师进修基金</span>
                        </div>
                        <div class="fiery-skip-botton-I">
                            <span class="fiery-left-botton-II">活动描述：</span></span><span
                                class="fiery-left-botton-III">老师在“微校园”中发布动态，每位老师将获得1元教师进修基金（每位老师每日最多记3条），本活动自2月3日至3月30日截止。</span>
                        </div>
                        <div class="fiery-skip-botton-I">
                            <span class="fiery-left-botton-II">奖金领取方式：</span>
                            <span class="fiery-left-botton-III">进入“电子超市”，点击“我要提现”，即可将您的奖金转入个人支付宝或者银行卡账户。</span>
                        </div>
                    </div>
                </div>

		</div>--%>


        <div class="divcss" id="activity1_detail">

            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 55%;left: 50%;margin-left: -456px;margin-top: -310px;z-index: 99;background-color: white;height: 620px;width:900px;">
                <div class="fiery-skip-top">
                    <span class="fiery-skip-I">爱心公益，传递能量！</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><br>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span><span
                            class="fiery-left-botton-III">长期</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II"></span>
                        <span class="fiery-left-botton-III">集合社会各界爱心人士的力量，让每个孩子都享有平等的学习机会！

上海复兰科技携手U来公益为偏远地区的孩子们提供优质教育资源。现诚邀富有爱心的小学老师志愿者（语文、数学、英语、音乐、美术学科）加入U来“智播计划”公益活动！



</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">爱心传递形式：</span>
                        <span class="fiery-left-botton-III" >

通过在线直播课堂的形式，教师与学生之间，通过摄像头与麦克风实现网络互动交流，完全达到网上模拟教室功能。学生可以随时提问，老师可以点名发言，当地学校的老师作为课堂助理，同步提高教学水平。
</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">爱心课程安排：</span>
                        <span class="fiery-left-botton-III">
志愿者老师可以在家里，办公室或教室为偏远地区的孩子上课，当地学校会有一位老师作为助理老师与志愿者老师配合课程进度， 志愿者老师可以根据自身情况安排课时等。
</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">爱心报名方式：</span>
                        <span class="fiery-left-botton-III">
请志愿者老师将您的姓名，科目，年级，手机、QQ和邮箱发送到bill.wu@fulaan.com，或者拨打电话400 820 6735 联系吴先生。

！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-III">
爱心公益，期待您的参与！
                        </span>
                    </div>
                    <div style="text-align: center">
                        <img src="/activity/img/fiery_IM.png" width="500px;" alt="">
                    </div>
                </div>
            </div>

        </div>



        <div class="divcss" id="activity2_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 35%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block">轻松推送课程，在线领取教师进修基金</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><br>
                    <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span><span
                            class="fiery-left-botton-III">02.03——03-30</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span></span><span
                            class="fiery-left-botton-III">在线领取教师进修基金 </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span></span><span
                            class="fiery-left-botton-III">老师将云课程（或备课空间、校本资源）推送到“班级课程”，每位老师将获得1元教师进修基金（每位老师每日最多记3条，重复推送课程记一条），本活动自2月3日至3月30日截止。</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">奖金领取方式：</span>
                        <span class="fiery-left-botton-III">进入“电子超市”，点击“我要提现”，即可将您的奖金转入个人支付宝或者银行卡账户。</span>
                    </div>
                </div>
            </div>
		</div>



        <div class="divcss" id="activity3_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;;margin-top: -200px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">


                <div class="fiery-skip-top">
                    <span class="fiery-skip-I">我和春天有个约会 </span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><br>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span><span
                            class="fiery-left-botton-III">2015年03月31日 -2015年05月31日</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">地点：</span></span><span
                            class="fiery-left-botton-III">k6kt平台朋友圈</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">说明：</span></span><span
                            class="fiery-left-botton-III">春天不是读书天：鸟语树尖，花笑西园；放个纸鸢，飞上半天；放牛塘边，赤脚种田。 亲爱的同学，让我们暂放手中的笔，去摸一摸柔嫩的青草与柳枝；将目光从黑板移向窗外，去看一看纷飞的黄鹂鸟与迎春花。让我们去跑，去跳，去触碰，去记录，让春天在我们的笔尖流淌，让春光在我们的镜头前停驻。 无论是文字还是图片，与我们一起分享你眼中的春色吧！
                        </span>
                    </div>
                </div>
            </div>
        </div>







        <div class="divcss" id="activity5_detail">

            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 60%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I">智慧校园大使征集 领千元奖金 </span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span><span
                            class="fiery-left-botton-III">海选期(用户制作上传视频期间)：02.13——4.30</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span></span><span
                            class="fiery-left-botton-III" style="display: inline-block;vertical-align: top;">一等奖1名：800元现金奖励<br>
                                                              二等奖3名：500元现金奖励<br>
                                                              三等奖5名：300元现金奖励</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span></span><span
                            class="fiery-left-botton-III">复兰科技智慧校园大使全平台征集活动开始啦，只要您熟悉k6kt平台功能，快来参加吧~另有千元现金大奖等你来啦哦~<br>
                           <span class="fiery-left-botton-II">  活动流程：</span><br>
                                                                ①老师或校领导，录制K6KT功能点的不超过5分钟的操作演示使用视频，配上优美磁性的声音解说，上传至各自的“备课空间”，再点击“推送”按钮， 推送到“联盟资源”下的“复兰杯”，选择“智慧校园大使”文件夹，即完成视频上传。<br>

                                                                ②老师可使用专业录课软件，也可使用K6KT平台自带的“录课神器”进行视频录制。<br>

                                                                ③视频海选结束后，由复兰科技团队选出功能演示最优的20部视频入围。<br>

                                                                ④10部入围视频将在全k6kt平台的「投票选举」中进行投票，并确定最终获奖名单（获奖者名单由70%的k6kt用户投票+30%的复兰科技评委会投票组成，综合分数排名前9的用户将获奖)。<br>

                                                                ⑤确认获奖名单，活动结束。<br>

                                                                </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">奖金领取方式：</span>
                        <span class="fiery-left-botton-III">进入“电子超市”，点击“我要提现”，即可将您的奖金转入个人支付宝或者银行卡账户。</span>
                    </div>
                </div>
            </div>
		</div>




        <div class="divcss" id="activity10_detail">

            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I">快来晒出你的儿童节校园活动 </span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span><span
                            class="fiery-left-botton-III">06.01——06.10</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span></span><span
                            class="fiery-left-botton-III" style="display: inline-block;vertical-align: top;">所有参与用户赠送10点经验值
                                                            </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span></span><span
                            class="fiery-left-botton-III">六一儿童节快到啦！小伙伴们快来k6kt平台微校园、微家园晒出你的儿童节校园活动，我们会赠送10点经验值哟！

                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：</span></span><span
                            class="fiery-left-botton-III">①活动期间，小伙伴们在k6kt平台微校园、微家园晒出儿童节校园活动，以照片或视频形式上传，并附加文字表达自己对于此次活动的心情或感悟。<br>
                                                                ②活动结束后，我们的工作人员将会确认参与名单，将经验值一一赠送至小伙伴们的账户中。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">查看获赠经验值：</span>
                        <span class="fiery-left-botton-III">点击“小宠物”，查看历史积分即可。</span>
                    </div>
                </div>
            </div>
        </div>




        <div class="divcss" id="activity9_detail">

            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I">时光漫游----我与爸爸妈妈童年不同样</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">5-15——6-15</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">时光漫游奖：10名，神秘奖品一份</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">幸福普照奖：所有参与活动的用户，经验值50点</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span></span><span
                            class="fiery-left-botton-III">六一儿童节即将到来，为感恩父母为孩子们营造的幸福童年，k6kt小助手邀请小伙伴们在微校园、微家园上传自己的照片和父母的童年同龄照，是不是会感觉时光穿越了呢？<br>
                           <span class="fiery-left-botton-II">  活动流程：</span><br>
                                                              ①活动期间，小伙伴们在k6kt平台微校园、微家园上传自己和父母的童年同龄照。<br>
                                                              ②活动结束后，我们的工作人员将会依据获赞数评出前十名，依次划分一、二、三等奖，并在活动结束后五个工作日内派发奖品及经验值。

                                                                </span>
                    </div>
                </div>
            </div>
        </div>


</div>


	</div>
        </div>
			
     </div>
<div>

</div>
</div>



</body>
</html>