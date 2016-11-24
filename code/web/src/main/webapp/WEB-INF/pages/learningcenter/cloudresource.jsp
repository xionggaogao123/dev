
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>同步资源</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">

    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static/css/yunziyuan_avi.css?v=2015041603" rel="stylesheet" />
    
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>


</head>
<body>


    <!--#head-->
    <%@ include file="../common_new/head.jsp" %>
    <!--/#head-->
    
    <div id="YCourse_player" class="player-container" style="position:fixed;top:50%;left:50%;z-index:999999999999999999">
		<div id="player_div" class="player-div" style="background-color: white;box-shadow: 0 10px 25px #000;box-sizing: content-box;border-radius: 4px;padding: 15px;display: none"></div>
        <span onclick="closeCloudView()" class="player-close-btn" id="player-close-btn" style="display: none"></span>
		<div id="sewise-div"
			style="display: none; width: 800px; height: 450px; max-width: 800px;background-color: white;box-shadow: 0 10px 25px #000;box-sizing: content-box;border-radius: 4px;padding: 15px">
			<script type="text/javascript"
				src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
            <span onclick="closeCloudView()" class="player-close-btn"></span>

		</div>

	</div>

	<script type="text/javascript">
		SewisePlayer.setup({
			server : "vod",
			type : "m3u8",
			skin : "vodFlowPlayer",
			logo : "none",
			lang : "zh_CN",
			topbardisplay : 'disabled',
			videourl : ""
		});


	</script>


    <!--#content-->
    <div id="content" class="clearfix">
        <%--<div class="movie">--%>
        <%--<img src="../images/movie.png" height="26" width="43" alt="">--%>
        <%--录课神器--%>
        <%--</div>--%>

        <!--.col-left-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!--/.col-left-->

        <!--.col-right-->
        <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>

        <div class="coll-right">
            
            
            <input type="hidden" id="isStudent" value="${isStudent}"/>

            <div class="tab-head clearfix">
            
            
                <div class="uo">
	                <%--<ul>
	                    <li class="currt"><a href="#">云课程</a></li>
	                    <li><a href="/lesson/teacher.do">备课空间</a></li>
	                    <li><a href="/lesson/class.do">班级课程</a></li>
	                    <li><a href="/lesson/school.do">校本资源</a></li>
	                    <li><a href="/lesson/league.do">联盟资源</a></li>
	                </ul>--%>
	                
	                  <c:choose>
							<c:when test="${!roles:isStudentOrParent(sessionValue.userRole)}">
		                            <div style="position: absolute;right: -100px;top: -27px">
                                        <a class="cloud-ss" href="/lesson/teacher.do?fastNewLesson=true">
                                            <img src="/img/K6KT/recorder-luke.png"/>
                                        </a><br>
                                        <a class="cloud-ss" href="/lesson/teacher.do?fastNewLesson=true">
                                            <img src="/img/K6KT/recorder-beike.png"/>
                                        </a>
                                            <%--    <span class="fast_load"><img src="/img/cloud_up.png"><span><a onclick="fastnewlessonDialog();">快速上传至备课空间</a></span></span>--%>
                                            <%--  <span class="fast_load"><img src="/img/cloud_down.png"><span><a href="/upload/resources/云课程清单.zip">下载云课程清单</a></span></span>--%>
                                        <div class="cloud-ss" >
                                            <a href="/upload/resources/云课程清单.zip" style="display: inline-block;height: 90px;">
                                                <img src="/img/K6KT/recorder-qingdan.jpg"/>
                                            </a>
                                        </div>
					                </div>
							</c:when>
							<c:otherwise>
					
							</c:otherwise>
					</c:choose>
                </div>
                
                <div class="xiala1">
                	<span id="55d41e47e0b064452581269a" class="current" onclick="changeNianduan('55d41e47e0b064452581269a')">小学</span>
                	<span id="55d41e47e0b064452581269c"  onclick="changeNianduan('55d41e47e0b064452581269c')">初中</span>
                	<span id="55d41e47e0b064452581269e" onclick="changeNianduan('55d41e47e0b064452581269e')">高中</span>
                	<div class="search">
                		<input id="input_name" type="text" placeholder="SEARCH..." onblur="changeSearch()"  /><button onclick="search()"></button>
                	</div>
                </div>
                <div class="kind">
                	<dl>
                		<dt style="display:none;">
                			<div class="ud_more">
                				<em>内容</em>
                				<em>|</em>
                			</div>
                			<div class="ud_morem">
	                			<span id="ty1" onclick="changeType('1')" >知识点</span>
	                			<span id="ty2" onclick="changeType('2')" class="hoverClass" >同步教材</span>
                			</div>
                		</dt>
                		
                		
                		<!-- subject -->
                		<dt>
                			<div class="ud_more">
                				<em>学科</em>
                				<em>|</em>
                			</div>
			             <script id="subject_script" type="text/x-dot-template">
						{{~it:value:index}}
                               {{  if(index==0){ }}
                                <span class="hoverClass" id="{{=value.idStr}}" onclick="changeSubject('{{=value.idStr}}')">{{=value.value}}</span>
                               {{ }else{ }}
                                  <span id="{{=value.idStr}}" onclick="changeSubject('{{=value.idStr}}')">{{=value.value}}</span>
                               {{ } }}
						{{~}}
					  </script>
                			<div class="ud_morem hwk1" id="subject_div">
                			   <span id="55d41e47e0b064452581269f" class="hoverClass" onclick="changeSubject('55d41e47e0b064452581269f')">语文</span>
							   <span id="55d41e47e0b06445258126a0" onclick="changeSubject('55d41e47e0b06445258126a0')">数学</span>
                               <span id="55d41e47e0b06445258126a1" onclick="changeSubject('55d41e47e0b06445258126a1')">英语</span>
                               <span onclick="changeSubject('5642f52d63e7cc71a47e3b01')" id="5642f52d63e7cc71a47e3b01">&#65279;海外学习推荐</span>
                			</div>
                		</dt>
                		
                		<!-- jiaocai -->
                		<dt id="jiaocai_li" >
                			<div class="ud_more">
                				<em>教材</em>
                				<em>|</em>
                			</div>
                			<script id="chubanshe_script" type="text/x-dot-template">
						{{~it:value:index}}
                           {{  if(index==0){ }}
                                <span class="hoverClass" id="{{=value.idStr}}" onclick="changeEtype('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ }else{ }}
                                <span id="{{=value.idStr}}" onclick="changeEtype('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ } }}
						{{~}}
					</script>
                			<div class="ud_morem hwk2" id="chubanshe_div">
                			   <span id="55d41e47e0b0644525819105" class="hoverClass" onclick="changeEtype('55d41e47e0b0644525819105')">小学语文人民教育出版社_新课标</span>
                			</div>
                		</dt>
                		
                		
                		
                		<!-- 年级 -->
                		<dt id="grade_li">
                			<div class="ud_more">
                				<em>年级</em>
                				<em>|</em>
                			</div>
                			<script id="grade_script" type="text/x-dot-template">
						{{~it:value:index}}
                           {{  if(index==0){ }}
                                <span class="hoverClass" id="{{=value.idStr}}" onclick="changeGrade('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ }else{ }}
                                <span id="{{=value.idStr}}" onclick="changeGrade('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ } }}
						{{~}}
					</script>
                			<div class="ud_morem hwk2" id="grade_div">
                			<span id="55d41e47e0b0644525819107" class="hoverClass" onclick="changeGrade('55d41e47e0b0644525819107')">一年级上</span>
							<span id="55d41e47e0b0644525819143" onclick="changeGrade('55d41e47e0b0644525819143')">一年级下</span>
							<span id="55d41e47e0b064452581917a" onclick="changeGrade('55d41e47e0b064452581917a')">二年级上</span>
							<span id="55d41e47e0b06445258191b1" onclick="changeGrade('55d41e47e0b06445258191b1')">二年级下</span>
							<span id="55d41e47e0b06445258191e6" onclick="changeGrade('55d41e47e0b06445258191e6')">三年级上</span>
							<span id="55d41e47e0b064452581921b" onclick="changeGrade('55d41e47e0b064452581921b')">三年级下</span>
							<span id="55d41e47e0b0644525819250" onclick="changeGrade('55d41e47e0b0644525819250')">四年级上</span>
							<span id="55d41e47e0b0644525819285" onclick="changeGrade('55d41e47e0b0644525819285')">四年级下</span>
							<span id="55d41e47e0b06445258192ba" onclick="changeGrade('55d41e47e0b06445258192ba')">五年级上</span>
							<span id="55d41e47e0b06445258192ed" onclick="changeGrade('55d41e47e0b06445258192ed')">五年级下</span>
							<span id="55d41e47e0b0644525819320" onclick="changeGrade('55d41e47e0b0644525819320')">六年级上</span>
							<span id="55d41e47e0b0644525819353" onclick="changeGrade('55d41e47e0b0644525819353')">六年级下</span>
                			</div>
                		</dt>
                		
                		
                		
                		<!-- 单元 -->
                		<dt id="danyuan_li"  >
                			<div class="ud_more">
                				<em>单元</em>
                				<em>|</em>
                			</div>
                			<script id="danyuan_script" type="text/x-dot-template">
						{{~it:value:index}}
                           {{  if(index==0){ }}
                                <span class="hoverClass" id="{{=value.idStr}}" onclick="selectdy('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ }else{ }}
                                <span id="{{=value.idStr}}" onclick="selectdy('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ } }}
						{{~}}
					</script>
                			<div class="ud_morem hwk2" id="danyuan_div">
                			
                			    <span id="55d41e47e0b0644525819108" class="hoverClass" onclick="selectdy('55d41e47e0b0644525819108')">汉语拼音</span>
								<span id="55d41e47e0b0644525819117" onclick="selectdy('55d41e47e0b0644525819117')">识字（一）</span>
								<span id="55d41e47e0b064452581911d" onclick="selectdy('55d41e47e0b064452581911d')">课文一</span>
								<span id="55d41e47e0b0644525819124" onclick="selectdy('55d41e47e0b0644525819124')">课文二</span>
								<span id="55d41e47e0b064452581912b" onclick="selectdy('55d41e47e0b064452581912b')">识字（二）</span>
								<span id="55d41e47e0b0644525819131" onclick="selectdy('55d41e47e0b0644525819131')">课文三</span>
								<span id="55d41e47e0b0644525819138" onclick="selectdy('55d41e47e0b0644525819138')">课文四</span>
								<span id="55d41e47e0b064452581913f" onclick="selectdy('55d41e47e0b064452581913f')">期中</span>
								<span id="55d41e47e0b0644525819141" onclick="selectdy('55d41e47e0b0644525819141')">期末</span>
                			</div>
                		</dt>
                		
                		
                		
                			<!-- 单元 -->
                		<dt id="zhangjie_li" >
                			<div class="ud_more">
                				<em>课</em>
                				<em>|</em>
                			</div>
                			<script id="zhangjie_script" type="text/x-dot-template">
						{{~it:value:index}}
                           {{  if(index==0){ }}
                                <span class="hoverClass" id="{{=value.idStr}}" onclick="selectSubCond('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ }else{ }}
                                <span id="{{=value.idStr}}" onclick="selectSubCond('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ } }}
						{{~}}
					</script>
					
					
					
					<script id="zhangjie_script_more" type="text/x-dot-template">
						{{~it:value:index}}
                           <span id="{{=value.idStr}}" onclick="selectSubCond('{{=value.idStr}}')">{{=value.value}}</span>
						{{~}}
					</script>
					
					
					
					
					
                			<div class="ud_morem hwk2" id="zhangjie_div">
                			    <span id="55d41e47e0b0644525819109" class="hoverClass" onclick="selectSubCond('55d41e47e0b0644525819109')">1a o e</span>
								<span id="55d41e47e0b064452581910a" onclick="selectSubCond('55d41e47e0b064452581910a')">2i u ü</span>
								<span id="55d41e47e0b064452581910b" onclick="selectSubCond('55d41e47e0b064452581910b')">3b p m f</span>
								<span id="55d41e47e0b064452581910c" onclick="selectSubCond('55d41e47e0b064452581910c')">4d t n l</span>
								<span id="55d41e47e0b064452581910d" onclick="selectSubCond('55d41e47e0b064452581910d')">5g k h</span>
								<span id="55d41e47e0b064452581910e" onclick="selectSubCond('55d41e47e0b064452581910e')">6j q x</span>
								<span id="55d41e47e0b064452581910f" onclick="selectSubCond('55d41e47e0b064452581910f')">7z c s</span>
								<span id="55d41e47e0b0644525819110" onclick="selectSubCond('55d41e47e0b0644525819110')">8zh ch sh r</span>
								<span id="55d41e47e0b0644525819111" onclick="selectSubCond('55d41e47e0b0644525819111')">9ai ei ui</span>
								<span id="55d41e47e0b0644525819112" onclick="selectSubCond('55d41e47e0b0644525819112')">10ao ou iu</span>
								<span id="55d41e47e0b0644525819113" onclick="selectSubCond('55d41e47e0b0644525819113')">11ie üe er</span>
								<span id="55d41e47e0b0644525819114" onclick="selectSubCond('55d41e47e0b0644525819114')">12an en in un ün</span>
								<span id="55d41e47e0b0644525819115" onclick="selectSubCond('55d41e47e0b0644525819115')">13ang eng ing ong</span>
								<span id="55d41e47e0b0644525819116" onclick="selectSubCond('55d41e47e0b0644525819116')">小结</span>
                               
                			</div>
                			 <i id="showmore_i" class="ud_botton" onclick="selectdyMore()" style="display: none;">更多</i>
                		</dt>
                		
                		
                		
                		
                		<!-- kwscope -->
                		<dt id="kw_scope_li" style="display:none;">
                			<div class="ud_more">
                				<em>知识面</em>
                				<em>|</em>
                			</div>
                			<script id="kw_scope_script" type="text/x-dot-template">
						{{~it:value:index}}
                           {{  if(index==0){ }}
                                <span class="hoverClass" id="{{=value.idStr}}" onclick="selectScope('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ }else{ }}
                                <span id="{{=value.idStr}}" onclick="selectScope('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ } }}
						{{~}}
					</script>
                			<div class="ud_morem hwk2" id="kw_scope_div">
                			<span id="55d41e47e0b0644525813ffc" class="hoverClass" onclick="selectScope('55d41e47e0b0644525813ffc')">拼音</span>
							<span id="55d41e47e0b0644525814004" onclick="selectScope('55d41e47e0b0644525814004')">识字与写字</span>
							<span id="55d41e47e0b0644525814018" onclick="selectScope('55d41e47e0b0644525814018')">词语</span>
							<span id="55d41e47e0b0644525814037" onclick="selectScope('55d41e47e0b0644525814037')">句子</span>
							<span id="55d41e47e0b0644525814058" onclick="selectScope('55d41e47e0b0644525814058')">阅读</span>
							<span id="55d41e47e0b064452581406b" onclick="selectScope('55d41e47e0b064452581406b')">口语训练</span>
							<span id="55d41e47e0b0644525814078" onclick="selectScope('55d41e47e0b0644525814078')">综合性学习</span>
							<span id="55d41e47e0b0644525814092" onclick="selectScope('55d41e47e0b0644525814092')">写作</span>
							<span id="55d41e47e0b06445258140ad" onclick="selectScope('55d41e47e0b06445258140ad')">其他</span>
                			</div>
                		</dt>
                		
                		
                		
                		
                				<!-- kwpoint -->
                		<dt id="kw_point_li" style="display:none;">
                			<div class="ud_more">
                				<em>知识点</em>
                				<em>|</em>
                			</div>
                	  <script id="kw_point_script" type="text/x-dot-template">
						{{~it:value:index}}
                             {{  if(index==0){ }}
                                <span class="hoverClass" id="{{=value.idStr}}" onclick="selectSubCond('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ }else{ }}
                                <span id="{{=value.idStr}}" onclick="selectSubCond('{{=value.idStr}}')">{{=value.value}}</span>
                             {{ } }}
						{{~}}
					   </script>
                			<div class="ud_morem hwk3" id="kw_point_div">
                			<span onclick="selectSubCond('55d41e47e0b0644525813ffd')" id="55d41e47e0b0644525813ffd" class="hoverClass">认识拼音</span>
                			</div>
                		</dt>
                		
                		
                		
                		
                		<!-- kwpoint -->
                		<dt id="type_li" style="display:none;">
                			<div class="ud_more">
                				<em>内容</em>
                				<em>|</em>
                			</div>
                			<div class="ud_morem hwk3" id="type_div">
                			<span id="type_-1" class="hoverClass"  onclick="changeFileType(-1)">全部</span>
                			<span id="type_1" onclick="changeFileType(1)">视频</span>
                			<span id="type_3" onclick="changeFileType(3)">WORD</span>
                			<span id="type_4" onclick="changeFileType(4)">PPT</span>
                			<span id="type_5" onclick="changeFileType(5)">PDF</span>
                			</div>
                		</dt>
                		
                		
                		
                	</dl>
                	
                	
                	
                	
                </div>
                <div class="up_down" id="down" onclick="showMore()" style="display:none">
	            <span id="showMore_text">更多选项</span>    
	                <img src="../images/more_down.png" height="10" width="16" alt="" >
               	</div>
             
            </div>
            <!-- 试看，推送部分 -->
            
             <script id="res_script" type="text/x-dot-template">

						{{~it:value:index}}
                             <div class="tuisong_left">
					           <div class="Img">
						         <img src="{{=value.imageUrl}}" alt="">
					           </div>
					           <div class="xinxi">
						         <p class="hu">
							         <span title="{{=value.type}}" class="span1">{{=value.type}}</span>
						         </p>
						         <p>
							         <em class="la1"></em>
							         <span class="sp">试看<span id="v_{{=value.id}}">{{=value.viewCount}}</span>次</span>
							         <em class="la2"></em>
							         <span class="sp">推送<span id="p_{{=value.id}}">{{=value.pushCount}}</span>次</span>
						         </p>
                             
                                 {{ if(value.fileTypeInt==2 || value.fileTypeInt==3 || value.fileTypeInt==4 || value.fileTypeInt==5 || value.fileTypeInt==18 ){ }}
                                      <a id="{{=value.id}}" href="/cloudres/view/swf.do?id={{=value.id}}" target="_blank" class="st1" ">试看</a>
                                 {{ } }}
                             
						         {{ if(value.fileTypeInt==1 ){ }}
                                      <a id="{{=value.id}}" href="javascript:viewText('{{=value.id}}')" class="st1" video-length="{{=value.id}}"  vurl="{{=value.url}}" vid="{{=value.id}}">试看</a>
                                 {{ } }}

                                 {{ if( (value.fileTypeInt>=8 && value.fileTypeInt<=17) || value.fileTypeInt==6 ||  value.fileTypeInt==100){ }}
                                      <a id="{{=value.id}}" href="javascript:cloudResPlay('{{=value.id}}')" class="st1" video-length="{{=value.id}}"  vurl="{{=value.url}}" vid="{{=value.id}}">试看</a>
                                 {{ } }}

                                 {{ if( value.fileTypeInt==7){ }}
                                      <a id="{{=value.id}}" vid="{{=value.id}}"  vurl="{{=value.url}}"  onclick="tryPlaySwf($(this),'{{=value.id}}');"  video-length="0" class="st1" href="javascript:void(0)" >试看</a>
                                 {{ } }}

                                     
                                 {{ if(jQuery("#isStudent").val()=="0") {  }}
                                         <a <%----%> class="st2">推送
                                             <ul class="push-select">
                                                 <li onclick="javascript:setCurrentResId('{{=value.id}}');">推送至备课空间成为新课程</li>
                                                 <li class="push-lessin-li" onclick="javascript:currentresId='{{=value.id}}'">推送至备课空间成为课程课件</li>
                                             </ul>
                                         </a>
                                 {{ } }}
						        
					         </div>
				          </div>
						{{~}}

		</script>
			<div class="tuisong clearfix hwk10" id="res_div">

				
			</div>
            <!-- 试看，推送部分 -->
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

            <%--推送成课程课件--%>
              <div class="wind-push-lesson" >
                <div class="wind-top">
                    推送至备课空间成为课程课件
                    <span>×</span>
                </div>
                <div class="left-ztree">
                <ul id="teacherDirUl1" class="ztree dir-tree" style="height:400px;"></ul>
             
                
                
                
                
                </div>
                <div class="video-list">
                    <div class="list-top">
                        <span id="lesson_count">0</span>个课程
                    </div>
                    <div id="backlist" class="video-fa">
                    
                      
                       
                    </div>
                </div>
                <div class="btn-vo">
                    <button class="btn-ok" onclick="pushToMultiBackups()">确定</button>
                    <button class="btn-no">取消</button>
                </div>
            </div>
        <!--/.col-right-->
        <%--bg--%>
        <div class="bg"></div>

    </div>
    <!--/#content-->




    <div id="treeContainer" style="display:none;">
  	    <div class="treeContainer-top">
  	    推送至备课空间成为新课程
  	    </div>
  	    <div class="treeContainer-right" onclick="closeDialog()">x</div>
		<ul id="teacherDirUl" class="ztree dir-tree"></ul>
		<div class="treeCOntainer-bottom">
            <botton onclick="closeDialog()">取消</botton>
			<botton class="tree-cur"  onclick="submitPushDir()">确定</botton>

		</div>
	</div>

					
	
	
	
	 <div id="txt_res_div" class="cloud-PPT" style="display:none;">
        <div class="cloud-PPT-top">
            <em>查看资源</em>
            <i onclick="hideTxtRes()">x</i>
        </div>
        <div id="txt_container" class="cloud-PPT-info">

        </div>
        <div class="cloud-PPT-bottom">
        </div>
    </div>
    <div class="bg"></div>
    
					
    <!--#foot-->
     <%@ include file="../common_new/foot.jsp" %>
    <!--#foot-->

<link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css" />
<link rel="stylesheet" href="/static/css/dialog.css" type="text/css"/>
<script type="text/javascript" src="/static/js/sharedpart.js"></script>
<script type="text/javascript" src="/static/js/cloudres.js"></script>


<script type="text/javascript">


//根据FileType
function viewSwf(type,id)
{
	var iWidth=800; //弹出窗口的宽度;
	var iHeight=600; //弹出窗口的高度;
	var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
	var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
	//doc,docx,
	if(type==2 || type==3 || type==4 || type==5 || type==7  || type==18)
	{
		increase(id,0);
		window.open('/cloudres/view/swf.do?id=' + id,"newwindow","height=500,width=800,scrollbars=no,location=no,menubar=no,status=no,titlebar=no,toolbar=no,resizable=no,top="+iTop+",left="+iLeft);
	}
}



function viewText(id)
{
	$.ajax({
        url: "/cloudres/view/txt.do",
        type: "get",
        data: {
            "id": id,
        },
        success: function(response) {
        	if(response.code=="200")
        		{
        		increase(id,0);
        		jQuery("#txt_res_div").show();
        		jQuery("#txt_container").html(response.message);
        		}
        },
        error: function(e) {
            
        }
    })
	
}

function hideTxtRes()
{
	jQuery("#txt_res_div").hide();
}


$(document).ready(function($) {
    $(".tree-cur").click(function() {
        $(".bg").hide();
    });
});


var currentresId=null;
function setCurrentResId(id)
{
	currentresId=id;
	jQuery("#treeContainer").show();
    jQuery(".bg").show();
}

function closeDialog()
{
	jQuery("#treeContainer").hide();
    jQuery(".bg").hide();
}


var trees=$.fn.zTree.init($('#teacherDirUl'), {
    async: {
        enable: true,
        url: '/dir/find.do?type=BACK_UP'
    },
    data: {
        simpleData: {
            enable: true,
            pIdKey: 'parentId',
            rootPId: 0
        }
    },
    callback: {
        onRename: addDir
    },
    check: {
        enable: true,
        chkboxType: {
            "Y": "",
            "N": ""
        }
    },
    view: {
        selectedMulti: true,
        addHoverDom: renderTreeNewButton,
        removeHoverDom: removeTreeNewButton
    }
});


var trees1=$.fn.zTree.init($('#teacherDirUl1'), {
	async : {
		enable : true,
		url : '/dir/find.do?type=BACK_UP'
	},
	data : {
		simpleData : {
			enable : true,
			pIdKey : 'parentId',
			rootPId : 0
		}
	},
	callback : {
		onRename : addDir,
		onCheck: treeonCheck
	},
	check : {
		enable : true,
		chkboxType : {
			"Y" : "",
			"N" : ""
		}
	},
	view : {
		selectedMulti : true,
		addHoverDom : renderTreeNewButton,
		removeHoverDom : removeTreeNewButton
	}
});

function treeonCheck()
{
	if (!currentresId) {
		return;
	}
	  var selectedNodes = trees1.getCheckedNodes(true);
	var selectIds="";
	 for (var i = 0; i < selectedNodes.length; i++) {
		 selectIds += selectedNodes[i].id+",";
     }
	 
	 
		 
	
		$.ajax({
			url : "/lesson/dir/backups.do",
			type : "post",
			data : {
				"ids" : selectIds,
			},
			success : function(response) {
				
				jQuery("#backlist").empty();
				
				if(response.message && response.message.length>0)
				{	
				 
					
						for(var i=0;i<response.message.length;i++)
							{
								try{
								  var html='';
								     html+=' <div id="'+response.message[i].idStr+'" class="video-s">';
								     if(response.message[i].name)
								     {
								    	   html+='<img src="'+response.message[i].name+'" width="147px" height="117px">';
								     }
								     else
								     {
								    	   html+=' <img src="../../../img/K6KT/txt_vo.png" width="147px" height="117px">';
								     }
								     html+=' <div class="video-tran">';
								     html+=' <span>'+response.message[i].value+'</span>';
								     html+=' </div>';
								     html+=' <div class="gou" ></div>';
								     html+=' <div id="a_'+response.message[i].idStr+'"  class="gou-b" ></div>';
								     html+=' <div id="b_'+response.message[i].idStr+'" class="bg-video" onclick="selectLesson(\''+response.message[i].idStr+'\')"></div>';
								     html+=' </div>';
								     jQuery("#backlist").append(html);
								}catch(ex)
								{
									
								}
							}
						
				}
                $(".video-s .bg-video").click(function(){
                    var id=$(this).attr("flag");
                    $("#a_"+id).toggle();
                });

                $('.video-s .gou').click(function() {
                    $(this).toggleClass('gou-b');
                    $(this).toggleClass('disblock');
                    $(this).next().toggleClass('disblock');
                });
				jQuery("#lesson_count").text(jQuery("#backlist").find(".video-s").length);
				
			},
			error : function(e) {
				
			}
		})
	
}


function pushToMultiBackups()
{
	 var ids="";

	 jQuery("#backlist").find("div").each(function(){
		 
		 if(jQuery(this).attr("class")=="gou-b disblock" && jQuery(this).is(":visible"))
		 {
			 ids+=jQuery(this).attr("id")+",";
		 }
	 });
	 
	 if(!ids)
	 {
		alert("请选择同步资源");
		return ;
	 }
	 
	 $.ajax({
			url : "/cloudres/push/buckups.do",
			type : "post",
			data : {
				 "resourceId":currentresId,
				 "ids" : ids,
			},
			success : function(response) {
				
                   if(response.code=="200")
                   {
                	   $(".wind-push-lesson").fadeOut();
                       $(".bg").fadeOut();
                	   alert("推送已经成功");
                   }else
                	{
                	    alert(response.message);
                	}
			},
			error : function(e) {
				
			}
	})
	
}


function selectLesson(id)
{
	 jQuery("#a_"+id).toggle();
}

function submitPushDir() {
    if (currentresId == null) {
        MessageBox("尚未选择资源！", -1);
    }
    var selectedNodes = trees.getCheckedNodes(true);
    if (selectedNodes.length > 0) {
        MessageBox("推送中...", 0);
        var destDirs = selectedNodes.map(function(node) {
            return node.id;
        }).join(',');
        
        
        $.ajax({
            url: "/cloudres/push.do",
            type: "post",
            data: {
                "id": currentresId,
                "dirIds": destDirs
            },
            success: function(response) {
            	jQuery("#treeContainer").hide();
            	increase(currentresId,1);
            	 MessageBox('推送完成', 1);
            },
            error: function(e) {
                MessageBox('推送失败!', -1);
            }
        })
    } else {
        MessageBox("请选择至少一个文件夹进行推送！", -1);
    }

    return false;
}






</script>


<script type="text/javascript">



	var currentPageID = 1;
	var isStudent = false;
	var vedioSize = function() {
		var newWidth = $(window).width() * 0.8;
		var newHeight = $(window).height() * 0.8;
		//800 x 450
		if (!$("#sewise-div").is(':hidden')) {

			if (newWidth < 800 || newHeight < 450) {
				if (newWidth > newHeight / 0.56) {// newHeight
					$("#sewise-div").css("height", newHeight);
					$("#sewise-div").css("width", newHeight / 0.56);
					$('#YCourse_player').css('margin-left', -newHeight * 0.5);
					$('#YCourse_player').css('margin-top', -newHeight * 0.5);
				} else {//newWidth
					$("#sewise-div").css("width", newWidth);
					$("#sewise-div").css("height", newWidth * 0.56);
					$('#YCourse_player').css('margin-left', -newWidth * 0.5);
					$('#YCourse_player').css('margin-top',
							-newWidth * 0.5 * 0.56);
				}
			} else {
				$("#sewise-div").css("width", 800);
				$("#sewise-div").css("height", 450);
				$('#YCourse_player').css('margin-left', -400);
				$('#YCourse_player').css('margin-top', -225);
			}
		} else if (!$("#player_div").is(':hidden')) {
			if (newWidth < 800 || newHeight < 600) {
				if (newWidth > newHeight / 0.625) {//newHeight
					$("#player_div").css("height", newHeight * 0.8);
					$("#player_div").css("width", newHeight * 0.8 / 0.625);
					$('#YCourse_player').css('margin-left', -newHeight * 0.5);
					$('#YCourse_player').css('margin-top', -newHeight * 0.5);
				} else {
					$("#player_div").css("width", newWidth);
					$("#player_div").css("height", newWidth * 0.625);
					$('#YCourse_player').css('margin-left', -newWidth * 0.5);
					$('#YCourse_player').css('margin-top',
							-newWidth * 0.5 * 0.625);
				}
			} else {
				$("#player_div").css("width", 800);
				$("#player_div").css("height", 600);
				$('#YCourse_player').css('margin-left', -400).css('margin-top',
						-300);
			}
		}
	}
	$(window).resize(vedioSize);
	$(window).load(vedioSize);
</script>


	    
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('cloudresource',function(cloudresource){
    	cloudresource.init();
    });
</script>

</body>
</html>