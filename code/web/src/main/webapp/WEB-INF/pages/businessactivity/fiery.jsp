<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>--%>
<%@page import="com.pojo.user.UserRole"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>平台运营活动</title>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="/static/css/fiery.css">
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
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
    $(function(){
        $("tr:odd").css("background","#F2F2F2")
    })
</script>
    <style type="text/css">
        table
        {
            border-collapse: collapse;
            border: none;

        }
       .fiery-skip-botton-I td
        {
            border: 1px solid #dddddd;
           min-width: 100px;
        }
        .fiery-skip-botton-I th
        {
            border: 1px solid #dddddd;
            text-align: center;
        }
    </style>
</head>


<body>


<%@ include file="../common_new/head.jsp" %>
<div style="background: #ffffff">
 <div style="width:1200px;margin:0 auto;overflow: visible;">
<div id="content_main_container" style="overflow: visible;">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>
        
        
        <div id="right-container">
		     <div class="fiery-left">
		<div class="fiery-left-top">
			<span class="fiery-left-top-huodong">火热活动</span>
		</div>
		<div class="fiery-left-bottom">
			<ul>
                <!--============================学生页面广告================================-->
                <c:choose>
                   <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                       <div>
                           <li id="activity25" onclick="showActivity('activity25')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[进行中]</span> <span
                                       class="fiery-left-bottom-II">I Love China--有奖问答活动</span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>10-1——10-31</span>
                               </div>
                           </li>
                       </div>
                       <div>
                           <li id="activity24" onclick="showActivity('activity24')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[进行中]</span> <span
                                       class="fiery-left-bottom-II">我的中秋我做主，妙语连珠创意出</span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>9-25——10-10</span>
                               </div>
                           </li>
                       </div>
                       <div>
                           <li id="activity1" onclick="showActivity('activity1')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[进行中]</span> <span
                                       class="fiery-left-bottom-II">爱心公益，传递能量 </span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>长期</span>
                               </div>
                           </li>
                       </div>
                       <div>
                           <li id="activity16" onclick="showActivity('activity16')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[进行中]</span> <span
                                       class="fiery-left-bottom-II">暑期限时领取宠物精灵活动</span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>07-10——09-30</span>
                               </div>
                           </li>
                       </div>
                       <div>
                           <li id="activity23" onclick="showActivity('activity23')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[已结束]</span> <span
                                       class="fiery-left-bottom-II">教师节，让我们一起帮老师上头条！</span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>9-1——9-15</span>
                               </div>
                           </li>
                       </div>
                       <div>
                           <li id="activity17" onclick="showActivity('activity17')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[已结束]</span> <span
                                       class="fiery-left-bottom-II">玩转暑期任务 赢取开学大礼</span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>07-06——08-20</span>
                               </div>
                           </li>
                       </div>
                       <div>
                           <li id="activity21" onclick="showActivity('activity21')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[已结束]</span> <span
                                       class="fiery-left-bottom-II">【暑期来了】一样的陪伴、别样的温情</span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>8-15——8-31</span>
                               </div>
                           </li>
                       </div>
                       <div>
                           <li id="activity20" onclick="showActivity('activity20')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[已结束]</span> <span
                                       class="fiery-left-bottom-II">【暑期来了】手拉手晒友谊 </span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>8-15——8-31</span>
                               </div>
                           </li>
                       </div>
                       <%--<div>
                           <li id="activity19" onclick="showActivity('activity19')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[已结束]</span> <span
                                       class="fiery-left-bottom-II">【暑期来了】拍拍我家的一角 </span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>07-21——08-14</span>
                               </div>
                           </li>
                       </div>
                       <div>
                           <li id="activity18" onclick="showActivity('activity18')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[已结束]</span> <span
                                       class="fiery-left-bottom-II">【暑期来了】晒晒我的暑期学习成果 </span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>07-21——08-14</span>
                               </div>
                           </li>
                       </div>--%>

                       <%--<div>
                           <li id="activity12" onclick="showActivity('activity12')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[已结束]</span> <span
                                       class="fiery-left-bottom-II">【暑期来了】我是家务小能手 </span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>07-01——07-20</span>
                               </div>
                           </li>
                       </div>--%>
                      <%-- <div>
                           <li id="activity13" onclick="showActivity('activity13')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[已结束]</span> <span
                                       class="fiery-left-bottom-II">【暑期来了】我是运动小达人</span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>07-01——07-20</span>
                               </div>
                           </li>
                       </div>
--%>


                      <%-- <div>
                           <li id="activity9" onclick="showActivity('activity9')">
                               <div class="fiery-left-li">
                                   <img src="/images/fiery-dian.jpg"> <span
                                       class="fiery-left-bottom-I">[已结束]</span> <span
                                       class="fiery-left-bottom-II">时光漫游----我与爸爸妈妈童年不同样 </span> <span
                                       class="fiery-left-bottom-III"></span>
                               </div>
                               <div class="fiery-right-li">
                                   <span>05-15——06-15</span>
                               </div>
                           </li>
                       </div>--%>

		           </c:when>

                  <cc:otherwise>
                      <!--============================老师页面广告================================-->
                      <div>
                          <li id="activity25" onclick="showActivity('activity25')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[进行中]</span> <span
                                      class="fiery-left-bottom-II">I Love China--有奖问答活动</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>10-1——10-31</span>
                              </div>
                          </li>
                      </div>
                      <div>
                          <li id="activity24" onclick="showActivity('activity24')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[进行中]</span> <span
                                      class="fiery-left-bottom-II">我的中秋我做主，妙语连珠创意出</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>9-25——10-10</span>
                              </div>
                          </li>
                      </div>


                      <div>
                          <li id="activity22" onclick="showActivity('activity22')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[进行中]</span> <span
                                      class="fiery-left-bottom-II">精品微课，你我分享</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>9-1——9-30</span>
                              </div>
                          </li>
                      </div>

                      <div>
                          <li id="activity1" onclick="showActivity('activity1')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[进行中]</span> <span
                                      class="fiery-left-bottom-II">爱心公益，传递能量 </span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>长期</span>
                              </div>
                          </li>
                      </div>
                      <div>
                          <li id="activity16" onclick="showActivity('activity16')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[进行中]</span> <span
                                      class="fiery-left-bottom-II">暑期限时领取宠物精灵活动</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>07-10——09-30</span>
                              </div>
                          </li>
                      </div>
                      <div>
                          <li id="activity14" onclick="showActivity('activity14')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[进行中]</span> <span
                                      class="fiery-left-bottom-II">教师进修基金活动升级啦--手机客户端在线参与</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>07-10——09-30</span>
                              </div>
                          </li>
                      </div>
                      <div>
                          <li id="activity15" onclick="showActivity('activity15')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[进行中]</span> <span
                                      class="fiery-left-bottom-II">教师进修基金活动升级啦--网页版在线参与</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>07-10——09-30</span>
                              </div>
                          </li>
                      </div>


                      <div>
                          <li id="activity23" onclick="showActivity('activity23')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[已结束]</span> <span
                                      class="fiery-left-bottom-II">教师节，让我们一起帮老师上头条！</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>9-1——9-15</span>
                              </div>
                          </li>
                      </div>
                      <div>
                          <li id="activity21" onclick="showActivity('activity21')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[已结束]</span> <span
                                      class="fiery-left-bottom-II">【暑期来了】一样的陪伴、别样的温情</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>8-15——8-31</span>
                              </div>
                          </li>
                      </div>
                      <div>
                          <li id="activity20" onclick="showActivity('activity20')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[已结束</span> <span
                                      class="fiery-left-bottom-II">【暑期来了】手拉手晒友谊 </span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>8-15——8-31</span>
                              </div>
                          </li>
                      </div>
                      <div>
                          <li id="activity17" onclick="showActivity('activity17')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[已结束]</span> <span
                                      class="fiery-left-bottom-II">玩转暑期任务 赢取开学大礼</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>07-06——08-20</span>
                              </div>
                          </li>
                      </div>
                      <div>
                          <li id="activity11" onclick="showActivity('activity11')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[已结束]</span> <span
                                      class="fiery-left-bottom-II"><span style="font-weight: bold"></span>2015暑期教师摄影大赛 </span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>07-01——08-10</span>
                              </div>
                          </li>
                      </div>


                     <%-- <div>
                          <li id="activity12" onclick="showActivity('activity12')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[已结束]</span> <span
                                      class="fiery-left-bottom-II">【暑期来了】我是家务小能手 </span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>07-01——07-20</span>
                              </div>
                          </li>
                      </div>
                      <div>
                          <li id="activity13" onclick="showActivity('activity13')">
                              <div class="fiery-left-li">
                                  <img src="/images/fiery-dian.jpg"> <span
                                      class="fiery-left-bottom-I">[已结束]</span> <span
                                      class="fiery-left-bottom-II">【暑期来了】我是运动小达人</span> <span
                                      class="fiery-left-bottom-III"></span>
                              </div>
                              <div class="fiery-right-li">
                                  <span>07-01——07-20</span>
                              </div>
                          </li>
                      </div>




                    <div>
                        <li id="activity2" onclick="showActivity('activity2')">
                            <div class="fiery-left-li">
                                <img src="/images/fiery-dian.jpg"> <span
                                    class="fiery-left-bottom-I">[已结束]</span> <span
                                    class="fiery-left-bottom-II"><span style="font-weight: bold"></span>发布微校园状态，在线领取教师进修基金 </span> <span
                                    class="fiery-left-bottom-III"></span>
                            </div>
                            <div class="fiery-right-li">
                                <span>05-09——06-30</span>
                            </div>
                        </li>
                    </div>

                    <div>
                        <li id="activity6" onclick="showActivity('activity6')">
                            <div class="fiery-left-li">
                                <img src="/images/fiery-dian.jpg"> <span
                                    class="fiery-left-bottom-I">[已结束]</span> <span
                                    class="fiery-left-bottom-II"><span style="font-weight: bold"></span>布置作业，在线领取教师进修基金 </span> <span
                                    class="fiery-left-bottom-III"></span>
                            </div>
                            <div class="fiery-right-li">
                                <span>05-09——06-30</span>
                            </div>
                        </li>
                    </div>

                    <div>
                        <li id="activity7" onclick="showActivity('activity7')">
                            <div class="fiery-left-li">
                                <img src="/images/fiery-dian.jpg"> <span
                                    class="fiery-left-bottom-I">[已结束]</span> <span
                                    class="fiery-left-bottom-II"><span style="font-weight: bold"></span>推送课程，在线领取教师进修基金 </span> <span
                                    class="fiery-left-bottom-III"></span>
                            </div>
                            <div class="fiery-right-li">
                                <span>05-09——06-30</span>
                            </div>
                        </li>
                    </div>

                    <div>
                        <li id="activity8" onclick="showActivity('activity8')">
                            <div class="fiery-left-li">
                                <img src="/images/fiery-dian.jpg"> <span
                                    class="fiery-left-bottom-I">[已结束]</span> <span
                                    class="fiery-left-bottom-II"><span style="font-weight: bold"></span>发布通知，在线领取教师进修基金 </span> <span
                                    class="fiery-left-bottom-III"></span>
                            </div>
                            <div class="fiery-right-li">
                                <span>05-09——06-30</span>
                            </div>
                        </li>
                    </div>
                  <div>
                      <li id="activity9" onclick="showActivity('activity9')">
                          <div class="fiery-left-li">
                              <img src="/images/fiery-dian.jpg"> <span
                                  class="fiery-left-bottom-I">[已结束]</span> <span
                                  class="fiery-left-bottom-II">时光漫游----我与爸爸妈妈童年不同样 </span> <span
                                  class="fiery-left-bottom-III"></span>
                          </div>
                          <div class="fiery-right-li">
                              <span>05-15——06-15</span>
                          </div>
                      </li>
                  </div>--%>

                  </cc:otherwise>
                </c:choose>


			</ul>
		</div>
		<!--=========================================跳转页面=======================================-->


        <div class="divcss" id="activity2_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block">发布微校园状态，在线领取教师进修基金</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><br>
                    <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">应广大教师的强烈要求，针对每所学校的“在线领取教师进修基金”活动延期一个月，要参与活动的老师抓紧啦！赠人玫瑰手有余香，记得告知身边老师一起参与哦！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span><span
                            class="fiery-left-botton-III">2015年5月9日—2015年6月30日</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动内容：</span></span><span
                            class="fiery-left-botton-III">老师每次在K6KT平台上“发布微校园状态”，即可获得1元教师进修基金，最多获得3元/天。所有参与的老师都可获得100经验值。 </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">注意：</span></span>
                        <span class="fiery-left-botton-III">
                            <dl>
                                <dd>1、发布的内容不要重复哦！为了让更多的老师获得教师进修基金，因此每位老师最多可获得200元的教师进修基金；</dd>
                                <dd>2、我们将在活动结束后的一周内进行进修基金及经验值的发放，请老师注意账户信息；</dd>
                                <dd>3、如果您要提现，进入“电子超市”—>“个人账户”—> “我要提现”，输入信息即可申请提现到支付宝或银行卡账户</dd>
                            </dl>
                        </span>
                    </div>
                </div>
            </div>
        </div>

        <div class="divcss" id="activity6_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block">布置作业，在线领取教师进修基金</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><br>
                    <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">应广大教师的强烈要求，针对每所学校的“在线领取教师进修基金”活动延期一个月，要参与活动的老师抓紧啦！赠人玫瑰手有余香，记得告知身边老师一起参与哦！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span><span
                            class="fiery-left-botton-III">2015年5月9日—2015年6月30日</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动内容：</span></span><span
                            class="fiery-left-botton-III">老师每次在K6KT平台上“布置作业”，即可获得1元教师进修基金。最多获得3元/天。所有参与的老师都可获得100经验值 </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动规则：</span></span>
                        <span class="fiery-left-botton-III">
                            <dl>
                                <dd>1、布置相同作业推送到不同班级记1条，为了让更多的老师获得教师进修基金，因此每位老师最多可获得200元的教师进修基金；</dd>
                                <dd>2、作业名称和作业内容缺一不可，否则不作为发奖条件；</dd>
                                <dd>3、我们将在活动结束后的一周内进行进修基金及经验值的发放，请老师注意账户信息；</dd>
                                <dd>4、如果您要提现，进入“电子超市”—>“个人账户”—> “我要提现”，输入信息即可申请提现到支付宝或银行卡账户。</dd>
                            </dl>
                        </span>
                    </div>
                </div>
            </div>
        </div>

        <div class="divcss" id="activity7_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block">推送课程，在线领取教师进修基金</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><br>
                    <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">应广大教师的强烈要求，针对每所学校的“在线领取教师进修基金”活动延期一个月，要参与活动的老师抓紧啦！赠人玫瑰手有余香，记得告知身边老师一起参与哦！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span><span
                            class="fiery-left-botton-III">2015年5月9日—2015年6月30日</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动内容：</span></span><span
                            class="fiery-left-botton-III">老师每次在K6KT平台上“推送课程”，即可获得1元教师进修基金。最多获得3元/天。所有参与的老师都可获得100经验值 </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动规则：</span></span>
                        <span class="fiery-left-botton-III">
                            <dl>
                                <dd>1、相同课程推送到不同班级记1条，为了让更多的老师获得教师进修基金，因此每位老师最多可获得200元的教师进修基金；</dd>
                                <dd>2、将云课程（或备课空间、校本资源）推送到“班级课程”成功后算1次推送课程；</dd>
                                <dd>3、我们将在活动结束后的一周内进行进修基金及经验值的发放，请老师注意账户信息；</dd>
                                <dd>4、如果您要提现，进入“电子超市”—>“个人账户”—> “我要提现”，输入信息即可申请提现到支付宝或银行卡账户。</dd>
                            </dl>
                        </span>
                    </div>
                </div>
            </div>
        </div>

        <div class="divcss" id="activity8_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block">发布通知，在线领取教师进修基金</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><br>
                    <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">应广大教师的强烈要求，针对每所学校的“在线领取教师进修基金”活动延期一个月，要参与活动的老师抓紧啦！赠人玫瑰手有余香，记得告知身边老师一起参与哦！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span><span
                            class="fiery-left-botton-III">2015年5月9日—2015年6月30日</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动内容：</span></span><span
                            class="fiery-left-botton-III">老师每次在K6KT平台上“发布通知”，即可获得1元教师进修基金。最多获得3元/天。所有参与的老师都可获得100经验值。 </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动规则：</span></span>
                        <span class="fiery-left-botton-III">
                            <dl>
                                <dd>1、相同通知发送到不同班级记1条，为了让更多的老师获得教师进修基金，因此每位老师最多可获得200元的教师进修基金；</dd>
                                <dd>2、通知名称和通知内容缺一不可，否则不作为发奖条件；</dd>
                                <dd>3、我们将在活动结束后的一周内进行进修基金及经验值的发放，请老师注意账户信息；</dd>
                                <dd>4、如果您要提现，进入“电子超市”—>“个人账户”—> “我要提现”，输入信息即可申请提现到支付宝或银行卡账户。</dd>
                            </dl>
                        </span>
                    </div>
                </div>
            </div>
        </div>






		<div class="divcss" id="activity1_detail">

            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 55%;left: 50%;margin-left: -456px;margin-top: -310px;z-index: 99;background-color: white;height: 620px;width:900px;">
                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">爱心公益，传递能量！</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><br>
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
请志愿者老师将您的姓名，科目，年级，手机、QQ和邮箱发送到coffee.wei@fulaan.com，或者拨打电话400 820 6735 联系人赵女士。

</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-III">
爱心公益，期待您的参与！
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">对坚持一个学年的老师，我们可以提供一次与学生面对面上课的机会，补助差旅费用。
对工作之余拿出时间志愿上课的老师，我们可提供课时费补助。</span>
                    </div>
                    <div style="text-align: center">
                        <img src="/images/fiery_IM.png" width="500px;" alt="">
                    </div>
                </div>
            </div>

		</div>

		<div class="divcss" id="activity3_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;;margin-top: -200px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">


                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">我和春天有个约会 </span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><br>
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
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">智慧校园大使征集 领千元奖金 </span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span>
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
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">快来晒出你的儿童节校园活动 </span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span>
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
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">时光漫游----我与爸爸妈妈童年不同样</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span>
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





        <div class="divcss" id="activity11_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">2015暑期教师摄影大赛</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                    <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">7-01——8-10</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span>
                        <span class="fiery-left-botton-III">一等奖1名、二等奖3名、三等奖6名<br>
                                                                    一、二、三等奖可获价值不等的丰富奖品<br>
                                                                    每位参与的教师奖励50点经验值
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">2015暑期教师摄影大赛活动开始啦！只要老师对摄影感兴趣并且熟悉K6KT平台功能，快来参加吧！有丰富礼品等你来拿哦~</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：</span>
                        <span class="fiery-left-botton-III">1、老师将自己的摄影作品发布到家校互动微校园模块中，并附加文字主题“#教师摄影大赛#”。<br>
                                                                  2、平台工作人员在活动截止后评选出优秀作品并在投票选举模块中组织投票。<br>
                                                                  3、根据投票结果确定获奖名单。<br>
                                                                  4、发放奖品，活动结束。~
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">奖金领取方式：</span>
                        <span class="fiery-left-botton-III">获奖名单确定后，小助手会通过私信方式通知。</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="divcss" id="activity12_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">【暑期来了】我是家务小能手</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">7-01——7-20</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span>
                        <span class="fiery-left-botton-III">神秘大奖10份 <br>
                                                                  每位参与的学生奖励20点经验值
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">暑期来了，做个为妈妈分担家务的勤劳小能手吧！只要你熟悉K6KT平台微家园功能，快来参与吧！神秘大奖等你拿哦！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：</span>
                        <span class="fiery-left-botton-III">1、学生到家校互动微家园模块，发布以”我是家务小能手“为主题的微家园状态，要求在文字前加上”#家务小能手#“主题，并附上自己做家务（擦桌子、扫地、烧饭等）照片。<br>
                                                                  2、平台工作人员在活动截止后统计点赞数并确定获奖学生名单。<br>
                                                                  3、发放奖品，活动结束。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">奖金领取方式：</span>
                        <span class="fiery-left-botton-III">获奖名单确定后，小助手会通过私信方式通知。</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="divcss" id="activity13_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">【暑期来了】我是运动小达人</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">7-01——7-20</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span>
                        <span class="fiery-left-botton-III">神秘大奖10份 <br>
                                                                  每位参与的学生奖励20点经验值
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">暑期来了，做个运动小达人吧！只要你熟悉K6KT平台微家园功能，快来参与吧！神秘大奖等你拿哦！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：</span>
                        <span class="fiery-left-botton-III">1、学生到家校互动微家园模块，发布以”我是运动小达人“为主题的微家园状态，要求在文字前加上”#运动小达人#“主题，并附自己暑期运动锻炼（跑步、游泳、打球等）照片。<br>
                                                                  2、平台工作人员在活动截止后统计点赞数并确定获奖学生名单。<br>
                                                                  3、发放奖品，活动结束。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">奖金领取方式：</span>
                        <span class="fiery-left-botton-III">获奖名单确定后，小助手会通过私信方式通知。</span>
                    </div>
                </div>
            </div>
        </div>


        <div class="divcss" id="activity14_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">教师进修基金活动升级啦！</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                    <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">K6KT最新Android、IOS手机版已经上线！快来通过手机客户端参与在线领取教师进修基金活动吧！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">7-10——9-30</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动内容：</span>
                        <span class="fiery-left-botton-III">活动期间，老师每次通过K6KT手机客户端“发布微校园状态”，即可获得1元教师进修基金，最多获得3元/天。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动规则：</span>
                        <span class="fiery-left-botton-III">1、发布的内容不要重复哦！为了让更多的老师获得教师进修基金，因此每位老师最多可获得200元的教师进修基金；<br>
                                                                  2、我们将在活动结束后的一周内进行进修基金的发放，请老师注意账户信息；<br>
                                                                  3、如果您要提现，进入“电子超市”—>“个人账户”—> “我要提现”，输入信息即可申请提现到支付宝或银行卡账户。</span>
                    </div>
                </div>
            </div>
        </div>


        <div class="divcss" id="activity15_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">教师进修基金活动升级啦！</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                    <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">7-10——9-30</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动内容：</span>
                        <span class="fiery-left-botton-III">活动期间，老师每次通过K6KT网页版“布置作业”，且至少20名学生在线提交作业或回复作业完成，可视为学生与老师有效互动，即可获得5元教师进修基金。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动规则：</span>
                        <span class="fiery-left-botton-III">1、为了让更多的老师获得教师进修基金，因此每位老师最多可获得200元的教师进修基金；<br>
                                                                  2、作业名称和作业内容缺一不可，否则不作为发奖条件；<br>
                                                                  3、我们将在活动结束后的一周内进行进修基金及经验值的发放，请老师注意账户信息；<br>
                                                                  4、如果您要提现，进入“电子超市”—>“个人账户”—> “我要提现”，输入信息即可申请提现到支付宝或银行卡账户。</span>
                    </div>
                </div>
            </div>
        </div>



        <div class="divcss" id="activity16_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;height: 480px;width: 900px;overflow: visible;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">暑期限时领取宠物精灵活动</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom" style="overflow: visible">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">7-10——9-30</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">精灵猴兄妹这个暑期来啦！只要小伙伴在活动期间，经验值增长50点以上，即可兑换宠物。让它们陪伴你度过暑期的美好时光，给你即将到来的新学期学习增添智慧哦！
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">宠物精灵形象：</span>
                        <span class="fiery-left-botton-III">我们是聪明机灵、活泼好动的银河系宠物，来陪伴小主人开心过暑假啦！
                        <div >
                            <img src="/images/fiery-16-1.jpg" alt="">
                            <img src="/images/fiery-16-2.jpg" alt="">
                        </div>
                         <span class="fiery-left-botton-III" style="padding-left: 145px;">精灵猴兄妹
                        </span>

                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">兑换条件：</span>
                        <span class="fiery-left-botton-III">在活动期间内，经验值增长50点并达到孵化条件后，就可以把精灵猴兄妹领回“家”哦！</span>
                    </div>
                </div>
            </div>
        </div>



        <div class="divcss" id="activity17_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;height: 545px;width: 900px;overflow: visible;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">玩转暑期任务 赢取开学大礼</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom" style="overflow: visible">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">7-06——8-20</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">暑期到啦！K6KT平台邀你参与玩转暑期任务，赢取开学大礼活动。童鞋们只要完成三个小任务即可获赠开学大礼，仅限前一百位哦！
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：  </span>
                        <span class="fiery-left-botton-III">1、参与活动，完成以下任务。<br>
                                                                  任务一：有自己设置的头像。<br>
                                                                  任务二：在活动期间，经验值增长50点以上。<br>
                                                                  任务三：至少参与两个【暑期来了】系列活动。<br>
                                                                  2、任务完成后，请第一时间私信小助手并提供礼品邮寄地址和联系方式。<br>
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">开学大礼：</span>
                        <span class="fiery-left-botton-III">迪士尼文具礼盒</span>
                        <div style="text-align: center">
                            <img src="/images/fiery-17.jpg" alt="">
                        </div>

                    </div>
                </div>
            </div>
        </div>


        <div class="divcss" id="activity18_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;height: 545px;width: 900px;overflow: visible;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">【暑期来了】晒晒我的暑期学习成果</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom" style="overflow: visible">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">7-21——8-14</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span>
                        <span class="fiery-left-botton-III">神秘大奖10份 <br>
                                                                  每位参与的学生奖励20点经验值</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">来这里晒晒暑期学习成果吧！只要你熟悉K6KT平台微家园功能，快来参与吧！神秘大奖等你拿哦！
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：  </span>
                        <span class="fiery-left-botton-III">1、学生到家校互动微家园模块，发布以”暑期学习成果“为主题的微家园状态，要求在文字前加上”#暑期学习成果#“主题，并附照片（习题、写作、书法、绘画等）。<br>
                                                                  2、平台工作人员在活动截止后统计点赞数并确定获奖学生名单。<br>
                                                                  3、发放奖品，活动结束。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">奖金领取方式： </span>
                        <span class="fiery-left-botton-III">获奖名单确定后，小助手会通过私信方式通知。</span>

                    </div>
                </div>
            </div>
        </div>


        <div class="divcss" id="activity19_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;height: 545px;width: 900px;overflow: visible;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">【暑期来了】拍拍我家的一角</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom" style="overflow: visible">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">7-21——8-14</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span>
                        <span class="fiery-left-botton-III">神秘大奖10份 <br>
                                                                  每位参与的学生奖励20点经验值</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">喜欢拍照的童鞋们，快来拍拍你温馨的家！只要你熟悉K6KT平台微家园功能，快来参与吧！神秘大奖等你拿哦！
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：  </span>
                        <span class="fiery-left-botton-III">1、学生到家校互动微家园模块，发布以”我家的一角“为主题的微家园状态，要求在文字前加上”#我家的一角#“主题，并附照片（干净整洁的卧室、书房、阳台等等）。<br>
                                                                  2、平台工作人员在活动截止后统计点赞数并确定获奖学生名单。<br>
                                                                  3、发放奖品，活动结束。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">奖金领取方式： </span>
                        <span class="fiery-left-botton-III">获奖名单确定后，小助手会通过私信方式通知。</span>

                    </div>
                </div>
            </div>
        </div>

        <div class="divcss" id="activity20_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;height: 400px;width: 900px;overflow: visible;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">【暑期来了】手拉手晒友谊</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom" style="overflow: visible">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">8-15——8-31</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span>
                        <span class="fiery-left-botton-III">十份温暖友谊奖
                                                                  每位参与的学生奖励20点经验值</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">小助手邀请你来参与“手拉手晒友谊”活动，快叫上你的好友来一起写下你们对这份友谊的珍惜之情。只要你熟悉微家园功能，快来参与吧！温暖友谊奖等你拿哦！
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：  </span>
                        <span class="fiery-left-botton-III">1、要求学生邀请一位同校好友一起到家校互动->微家园中发布以”手拉手晒友谊“为主题的状态，要求在文字前加上”#手拉手晒友谊#“主题，并附上自己与好友合影一张。两个人都要发哦！<br>
                                                                 2、平台工作人员在活动截止后统计点赞数并确定获奖学生名单。<br>
                                                                 3、小助手私信获奖通知、发放奖品。
                        </span>
                    </div>
                </div>
            </div>
        </div>


        <div class="divcss" id="activity21_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;height: 400px;width: 900px;overflow: visible;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">【暑期来了】一样的陪伴、别样的温情</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom" style="overflow: visible">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">8-15——8-31</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span>
                        <span class="fiery-left-botton-III">十份温情陪伴奖
                                                                  每位参与的学生奖励20点经验值</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">为鼓励童鞋们利用暑期时间多陪伴家中长辈，为他们做些力所能及的事情，小助手邀请你来参与“温情陪伴”活动。只要你熟悉微家园功能，快来参与吧！温情陪伴奖等你拿哦！
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：  </span>
                        <span class="fiery-left-botton-III">1、学生到家校互动->微家园中发布以”温情陪伴“为主题的状态，要求在文字前加上”#温情陪伴#“主题，并附上自己陪伴家中长辈（或其他老人）的照片。<br>
                                                                  2、平台工作人员在活动截止后统计点赞数并确定获奖学生名单。<br>
                                                                  3、小助手私信获奖通知、发放奖品。
                        </span>
                    </div>
                </div>
            </div>
        </div>


        <div class="divcss" id="activity22_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -300px;z-index: 99;background-color: white;min-height: 400px;width: 900px;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">精品微课，你我分享</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                    <span class="fiery-skip-II" style="color: red">亲，该页面仅教师可见哦~~  </span>
                </div>
                <div class="fiery-skip-bottom">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">9-1——9-30</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动内容：</span>
                        <span class="fiery-left-botton-III">为了使全国的学生能够看到老师的精彩课程，K6KT平台开展“精品微课，你我分享”活动。教师将自己制作的微视频课程上传到平台“电子超市”，与全国的教师、学生、家长分享。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span>
                        <span class="fiery-left-botton-III">
                            1、教师在活动期间上传的第一个课程作品，审核合格后，奖励10元。<br>
                            2、活动期间，教师的课程作品每被购买一次，平台补贴1元，每位老师活动期间最多可获100元补贴。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：</span>
                        <span class="fiery-left-botton-III">1、老师到电子超市精品微课栏目中上传自己的课程作品，视频、课件均可。（教师对所传作品的知识产权拥有处置权）<br>
                                                                  2、平台工作人员在活动结束后统计每位老师课程作品的已购买次数。<br>
                                                                  3、将补贴发放至老师电子超市账户中。<br>
                                                                  4、鼓励老师将参与活动的课程定为0元，使更多的学生能够分享您的优质课程。<br>
                                                                  TIPS: 视频可用平台录课神器录制。丰富、新颖的课程内容有助于提高购买次数。
                        </span>
                    </div>
                </div>
            </div>
        </div>



        <div class="divcss" id="activity23_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;height: 420px;width: 900px;overflow: visible;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">教师节，让我们一起帮老师上头条！</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                    <div class="fiery-zf">
                        <span>----写祝福，送经验值啦！</span>
                    </div>
                </div>
                <div class="fiery-skip-bottom" style="overflow: visible">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">9-1——9-15</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：</span>
                        <span class="fiery-left-botton-III">每位参与的学生奖励30点经验值
            有精彩回复的（即获得10条他人不同回复的）奖励50点经验值
            “上头条”的教师可获一份神秘礼品</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">金秋九月，第31个教师节如期而至。童鞋们，你见识过的夫子里，最美的、最帅的、最个性的、最亲和的……文艺范儿的、运动型儿的、摄友群儿、驴侠派儿的……课堂上的糗事囧事，讲台上的夫子绝活，铭记于心的师生对话。你，想不想倾诉？愿不愿分享？如果你想“曝光”他们，就来参加“帮老师上头条”活动吧，讲述你与夫子间的故事，在微家园写下对老师想说的话+老师姓名，帮自己的恩师上头条。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：  </span>
                        <span class="fiery-left-botton-III">1、要求学生到家校互动->微家园中发布以”帮老师上头条“为主题的状态，要求在文字前加上”#帮老师上头条#“主题，写下对老师想说的话+老师姓名。<br>
                                                                  2、平台工作人员经过评选后在教师节当天将该帖置于头条。点赞数越多，上头条的可能性越大哦！<br>
                                                                  3、活动结束后，参与的童鞋将收到经验值奖励，“上头条”的老师将收到一份神秘礼品哦。
                        </span>
                    </div>
                </div>
            </div>
        </div>







        <div class="divcss" id="activity24_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;height: 420px;width: 900px;overflow: visible;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">我的中秋我做主，妙语连珠创意出</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom" style="overflow: visible">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">9-25——10-10</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">参与对象：</span>
                        <span class="fiery-left-botton-III">学生</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">中秋佳节月儿圆，圆出全家聚一堂。9月27日是传统节日--中秋节，K6KT特开展创意活动--我的中秋我做主，妙语连珠创意出，请童鞋们发挥才子本色，妙语连珠完成造句，晒出你的中秋style！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动详情： </span>
                        <span class="fiery-left-botton-III">造句示范：中秋节，我__________过中秋，看着_______，吃着________，希望___________!<br>
                            请童鞋们根据以上示范，填写空格，组成一句妙语祝福，并发布在家校互动->微家园中，并在文字前添加“#中秋妙语#”主题。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：  </span>
                        <span class="fiery-left-botton-III">参与并符合要求的学生奖励20点经验值。
                        </span>
                    </div>
                </div>
            </div>
        </div>







        <div class="divcss" id="activity25_detail">
            <div style="background-color: #000000;height: 10000px;width: 10000px;position: fixed;left: 50%;top: 50%;margin-left: -5000px;margin-top: -5000px;z-index: 9999;opacity: 0.5;filter:alpha(opacity=50)"></div>
            <div style="z-index: 9999999!important;display: block;position: fixed;top: 50%;left: 50%;margin-left: -456px;margin-top: -200px;z-index: 99;background-color: white;height: 420px;width: 900px;overflow: visible;">

                <div class="fiery-skip-top">
                    <span class="fiery-skip-I" style="margin: 10px 0 10px 0;display: inline-block;">I Love China--有奖问答活动</span><span style="position: absolute;right: 30px;top:-5px;font-size: 20px;cursor: pointer;" onclick="Fiery()" >x</span><span class="fiery-skip-II"></span><br>
                </div>
                <div class="fiery-skip-bottom" style="overflow: visible">
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动时间：</span>
                        <span class="fiery-left-botton-III">10-1——10-31</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">参与对象：</span>
                        <span class="fiery-left-botton-III">学生</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动描述：</span>
                        <span class="fiery-left-botton-III">2015年10月1日，我们即将迎来伟大祖国的66岁生日。为了弘扬爱国主义精神，培养历史责任感、使命感，增强童鞋们的责任意识，K6KT开展国庆特别活动--I Love China有奖问答。只要你熟悉K6KT平台功能，快来参与吧！经验值奖励等你来拿哦！</span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动流程：  </span>
                        <span class="fiery-left-botton-III">1、学生到家校互动->问卷调查中参与I Love China有奖问答活动，认真回答并成功提交问卷。<br>
2、活动结束后，平台发放经验值奖励。调查问卷成绩优秀的学生名单会在微校园中公布出来。
                        </span>
                    </div>
                    <div class="fiery-skip-botton-I">
                        <span class="fiery-left-botton-II">活动奖励：  </span>
                        <span class="fiery-left-botton-III">参与并符合要求的学生奖励20点经验值。
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

</div>

</body>
</html>

