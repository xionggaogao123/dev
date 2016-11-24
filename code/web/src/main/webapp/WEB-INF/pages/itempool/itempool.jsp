<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>题库资源</title>
        <meta charset="utf-8">
        <link rel="stylesheet" type="text/css" href="/static/css/itempool/tuku.css">
        <link rel="stylesheet" type="text/css" href="/static/css/itempool/student_information.css"/>
        <link rel="stylesheet" type="text/css" href="/static/css/homepage.css">
        <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
        <script type="text/javascript" src="/static/js/itempool/erroritempool.js"></script>
    </head>
    <body>
    <!-- 页头 -->
    <div style="height: 1215px;;">
<%@ include file="../common_new/head.jsp"%>
<!-- 页头 -->
<div class="itempool_main">
    <div class="main">
    
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->
        <!--广告-->
        <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>
        <!--广告-->
    	<div class="tu_right">
    		<div class="tu_head" style="display:none;">
    			<ul>
    				<li  class="current1" id="tiku_li" onclick="changeContent('tiku_li')"><a href="javascript:">题库</a></li>
    				<li  id="errorItem_li" onclick="changeContent('errorItem_li')"><a href="javascript:;">错题本</a></li>
    			</ul>
    		</div>
    		<div class="tu_main" id="Main">
    			<div class="tu_main_top">
    				<ul>
    					<li id="type1" class="current2"><a href="javascript:changeType('1');">综合知识点选题</a></li>
    					<li id="type2"><a href="javascript:changeType('2');">同步教材选题</a></li>
    				</ul>
    			</div>
    			<div class="tu_main_bottom">
    				<ul>
    					<li>
    						<div class="l">年段：</div>
    						<div class="r">
    							<span>
    								<input type="radio" id="tong1" name="duan" onchange="changeNianduan('55d41e47e0b064452581269a')" checked="checked">
    								<label for="tong1">小学(62465)</label>
    							</span>
    							<span>
    								<input type="radio" id="tong2" name="duan" onchange="changeNianduan('55d41e47e0b064452581269c')">
    								<label for="tong2">初中(92306)</label>
    							</span>
    							<span>
    								<input type="radio" id="tong3" name="duan" onchange="changeNianduan('55d41e47e0b064452581269e')">
    								<label for="tong3">高中(259450)</label>
    							</span>
    						</div>
    					</li>
    					
    					
    					
    					
    					
    					<script id="subject_script" type="text/x-dot-template">
						{{~it:value:index}}
                               {{  if(index==0){ }}
                                  <span>
    								 <input type="radio" id="subject_{{=value.idStr}}" name="ke" value="{{=value.idStr}}" checked="checked" onchange="changeSubject('{{=value.idStr}}')">
    								 <label for="tong4">{{=value.value}}</label>
    						      </span>
                               {{ }else{ }}
                                  <span>
    								 <input type="radio" id="subject_{{=value.idStr}}"  name="ke" value="{{=value.idStr}}" onchange="changeSubject('{{=value.idStr}}')">
    								 <label for="tong4" title="{{=value.value}}">{{=value.value}}</label>
    						      </span>
                               {{ } }}
						{{~}}
					  </script>
    					
    					<li>
    						<div class="l">学科：</div>
    						<div class="r" id="subject_div">
    						 <span>
    								<input type="radio" id="subject_1" name="ke" checked="checked" onchange="changeSubject(1)">
    								<label for="subject_1">语文</label>
    							</span>
    							<span>
    								<input type="radio" id="subject_2" name="ke" onchange="changeSubject(2)">
    								<label for="subject_2">数学</label>
    							</span>
    							<span>
    								<input type="radio" id="subject_3" name="ke" onchange="changeSubject(3)">
    								<label for="subject_3">英语</label>
    							</span>
    						
    						</div>
    					</li>
						<!--========================start综合知识点时出现=========================-->
						
						
					<script id="kw_scope_script" type="text/x-dot-template">
						{{~it:value:index}}
                           {{  if(index==0){ }}
                              <span>
    								<input type="radio" id="{{=value.idStr}}" name="mian" checked="checked" onchange="selectScope(this)">
    								<label for="{{=value.idStr}}" title="{{=value.value}}">{{=value.value}}</label>
    						  </span>
                             {{ }else{ }}
                               <span>
    								<input type="radio" id="{{=value.idStr}}" name="mian" onchange="selectScope(this)">
    								<label for="{{=value.idStr}}" title="{{=value.value}}">{{=value.value}}</label>
    						  </span>
                             {{ } }}
						{{~}}
					</script>
					
					
					
						<li id="kw_scope_li">
    						<div class="l">知识面：</div>
    						<div class="r" id="kw_scope_div">
    						    
    						</div>
    					</li>
    					
    					
    					<script id="kw_point_script" type="text/x-dot-template">
						{{~it:value:index}}
                               <span>
    								<input type="checkbox" id="{{=value.idStr}}" name="mian" onchange="selectSubCond(this)">
    								<label for="{{=value.idStr}}" title="{{=value.value}}">{{=value.value}}</label>
    						  </span>
						{{~}}
					   </script>
					
					
    					<li id="kw_point_li">
    						<div class="l">知识点：</div>
    						<div class="r" id="kw_point_div">
    							
    						</div>
    					</li>
						<!--========================end综合知识点时出现=========================-->
						
						
						<script id="chubanshe_script" type="text/x-dot-template">
						{{~it:value:index}}


                             {{  if(index==0){ }}
                               <span>
    								<input type="radio"  name="mian" checked="checked" onchange="changeEtype('{{=value.idStr}}')">
    								<label for="tong14"><div class="chang" title="{{=value.value}}">{{=value.value}}</div></label>
    						   </span>
                             {{ }else{ }}
                                <span>
    								<input type="radio"  name="mian" onchange="changeEtype('{{=value.idStr}}')">
    								<label for="tong14"><div class="chang" title="{{=value.value}}">{{=value.value}}</div></label>
    						   </span>
                             {{ } }}

                             
						{{~}}
					   </script>
					   
					   
					   
						<!--========================start同步教材时出现=========================-->
						<li id="jiaocai_li" style="display:none">
    						<div class="l">教材：</div>
    						<div class="r" id="chubanshe_div">
    							
    						</div>
    					</li>
    					
    					
    					
    					<script id="grade_script" type="text/x-dot-template">
						{{~it:value:index}}
                               {{  if(index==0){ }}
                                  <span>
    								 <input type="radio" id="grade_{{=value.idStr}}" name="nian" value="{{=value.idStr}}" checked="checked" onchange="changeGrade('{{=value.idStr}}')">
    								 <label for="tong4" title="{{=value.value}}">{{=value.value}}</label>
    						      </span>
                               {{ }else{ }}
                                  <span>
    								 <input type="radio" id="grade_{{=value.idStr}}"  name="nian" value="{{=value.idStr}}" onchange="changeGrade('{{=value.idStr}}')">
    								 <label for="tong4" title="{{=value.value}}">{{=value.value}}</label>
    						      </span>
                               {{ } }}
						{{~}}
					  </script>
    					
    					
    					<li id="grade_li" style="display:none;">
    						<div class="l">年级：</div>
    						<div class="r" id="grade_div">
    						  
    						</div>
    					</li>
    					
    					
    					
    					
    					
    					
    					
    					
    					
    					
    					
    					
    					
    					
    					<script id="danyuan_script" type="text/x-dot-template">
						{{~it:value:index}}
                               {{  if(index==0){ }}
                                  <span>
    								 <input type="radio"  name="danyuan" value="{{=value.idStr}}" checked="checked" onchange="selectdy('{{=value.idStr}}')">
    								 <label for="tong4" title="{{=value.value}}">{{=value.value}}</label>
    						      </span>
                               {{ }else{ }}
                                  <span>
    								 <input type="radio"  name="danyuan" value="{{=value.idStr}}" onchange="selectdy('{{=value.idStr}}')">
    								 <label for="tong4" title="{{=value.value}}">{{=value.value}}</label>
    						      </span>
                               {{ } }}
						{{~}}
					  </script>
    					
    					<li id="danyuan_li" style="display:none;">
    						<div class="l">单元：</div>
    						<div class="r" id="danyuan_div">
    						  
    						</div>
    					</li>
    					
    					
    					
    					<script id="zhangjie_script" type="text/x-dot-template">
						{{~it:value:index}}
                               <span>
    								<input type="checkbox" id="{{=value.idStr}}" name="mian" onchange="selectSubCond(this)">
    								<label for="{{=value.idStr}}" title="{{=value.value}}">{{=value.value}}</label>
    						  </span>
						{{~}}
					  </script>
    					
    					<li id="zhangjie_li" style="display:none;">
    						<div class="l">课：</div>
    						<div class="r" id="zhangjie_div">
    						
    						</div>
    					</li>
						<!--========================end同步教材时出现=========================-->
    					<li>
    						<div class="l">题型：</div>
    						<div class="r">
    							<span>
    								<input type="checkbox" id="tong18" value="1" onchange="selectItemType(this)">
    								<label for="tong18">选择题</label>
    							</span>
    							<span>
    								<input type="checkbox" id="tong19" value="3" onchange="selectItemType(this)">
    								<label for="tong19">判断题</label>
    							</span>
    							<span>
    								<input type="checkbox" id="tong20" value="4" onchange="selectItemType(this)">
    								<label for="tong20">填空题</label>
    							</span>
    							<span>
    								<input type="checkbox" id="tong21" value="5" onchange="selectItemType(this)">
    								<label for="tong21">主观题</label>
    							</span>
    						</div>
    					</li>
    					
    					<li>
    						<div class="l">已做题：</div>
    						<div class="r">
    							<span>
    								<input type="radio" id="tong25" name="han" checked="checked">
    								<label for="tong25">包含</label>
    							</span>
    							<span>
    								<input type="radio" id="tong26" name="han">
    								<label for="tong26">不包含</label>
    							</span>
    							
    						</div>
    					</li>
    				</ul>
    			</div>
    			<button id="DT" onclick="beginAnswerItem()">开始答题</button>
    		</div>
    		
    		
    		<script id="itemDetail_script" type="text/x-dot-template">
				 <div class="tu1">
                    <h3>题目 <span>{{=it.currentIndex}}/{{=it.totalItem}}</span></h3>
                </div>
                <dl class="xinxi1">
                    <dt>
                                                                              难度：<span>{{=it.level}}</span>
                    </dt>
                    <dt>
                                                                           题型：<span>{{=it.itemType}}</span>
                    </dt>
                    <dt>
                                                                           知识点：<span>{{=it.kw}}</span>
                    </dt>
                    <dt>
                                                                         分值：<span>{{=it.score}}</span>
                    </dt> 
                </dl>
                <!-- 内容 -->
                <div class="connent1">
                 {{=it.item}}
                </div>
                <div class="teare">
                    <textarea placeholder="填写答案" id="myAnswer"></textarea>
                    <button id="wanover" onclick="checkAnswer()">完成</button>

                   
                    <div class="allok" id="allok">
                        <p>{{=it.answer}}</p>
                        <button onclick="itemDetail()">下一题</button>
                        <button onclick="addErrorPool()">加入错题集</button>
                    </div>
                   
                </div>
			</script>
    		
            <div class="tutu1" id="TU1">
            
                
                <!-- 内容 -->
            </div>
            
            
            
            
            <div id="error_div" style="display:none;">
                          <div class="student-information_new_anlaysis">
                        <ul>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_red" onclick="showErrorItem(1)">
                                    <span class="student-information_new_anlaysis_YW_left">语文</span>
                                    <span id="e_subject_1" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_blue" onclick="showErrorItem(2)">
                                    <span class="student-information_new_anlaysis_YW_left">数学</span>
                                    <span id="e_subject_2" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_green" onclick="showErrorItem(3)">
                                    <span class="student-information_new_anlaysis_YW_left">英语</span>
                                    <span id="e_subject_3" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_orange" onclick="showErrorItem(4)">
                                     <span class="student-information_new_anlaysis_YW_left">物理</span>
                                    <span id="e_subject_4" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_purple" onclick="showErrorItem(5)">
                                    <span class="student-information_new_anlaysis_YW_left">化学</span>
                                    <span id="e_subject_5" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_yellow" onclick="showErrorItem(6)">
                                     <span class="student-information_new_anlaysis_YW_left">生物</span>
                                    <span id="e_subject_6" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            
                            
                             <li>
                                <div class="student-information_new_anlaysis_YW information_yellow" onclick="showErrorItem(7)">
                                     <span class="student-information_new_anlaysis_YW_left">地理</span>
                                    <span id="e_subject_7" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            
                             <li>
                                <div class="student-information_new_anlaysis_YW information_yellow" onclick="showErrorItem(8)">
                                     <span class="student-information_new_anlaysis_YW_left">历史</span>
                                    <span id="e_subject_8" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            
                            <li>
                                <div class="student-information_new_anlaysis_YW information_yellow" onclick="showErrorItem(9)">
                                     <span class="student-information_new_anlaysis_YW_left">政治</span>
                                    <span id="subject_9" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                        </ul>
                    </div>
               </div>
                
                
                
                
                       
               <div id="det_error_div" class="student_information_finish_II" style="display:none;">
                <div style="/*display: none*/">
                    <div style="clear: both"></div>
                    <div class="student_information_new_I_top">
                      <!-- 
                        <span class="student_information_CT_top_I">我的练习</span>/
                        <span class="information_I">&nbsp;新建练习</span>
                         -->
                    </div>
                    <div class="student_information_new_CT_zs">
                        <div class="student_information_new_CT_info">
                            知识点
                        </div>
                        <div id="error_kw" class="student_information_new_CT_info_I">
                        
                        </div>
                    </div>
                    <div class="student_information_new_info_II">
                        <ul>
                            <li><span id="error_sort_1" class="student_information_new_info_S" onclick="loadItemDetail(1)">乱序<i class="fa fa-arrow-down"></i></span></li>
                            <li><span id="error_sort_2" class="student_information_new_info_SS" onclick="loadItemDetail(2)">最新错题<i class="fa fa-arrow-down"></i></span></li>
                            <li><span id="error_sort_3" class="student_information_new_info_SS" onclick="loadItemDetail(3)">错次最多<i class="fa fa-arrow-down"></i></span></li>
                        </ul>
                        <span><span class="error_count_total"></span>个结果</span>
                    </div>
                    <div class="student_information_new_info_III">
                        <div class="student_information_new_info_III_top">
                            <div class="student_information_new_III_top_left">
                                <span>题目</span>
                                <span id="error_count_current">1</span>/
                                <span id="error_c_total" class="error_count_total"></span>
                            </div>
                            <div class="student_information_new_III_top_right">
                                <span class="student_information_new_III_I">错误次数：<span id="error_count"></span>次</span>
                                <span>上一次做错：<span id="error_time"></span></span>
                            </div>
                        </div>
                        <div class="student_information_new_V">
                            <dl>
                             
                                <dd>难度：<span id="error_level"></span></dd>
                                <dd>题型：<span id="error_type"></span></dd>
                                <dd>知识点：<span id="error_kwp"></span></dd>
                                <dd>分值：<span id="error_scope"></span></dd>
                            </dl>
                        </div>
                        <div v class="student_information_new_VI" >
                            <span id="tigan"></span>
                        </div>
                        <div v class="student_information_new_VII" id="myAnswer">
                            
                        </div>
                    </div>
                    <div class="student_information_new_xuehui">
                                <span class="student_information_new_VVV" onclick="deleteItem()">我已学会,删除该题</span>
                                <span class="student_information_new_VIII" onclick="nextItemDetail(1)">下一题</span>
                       <%-- <div class="student_information_new_VVV">
                            <span >我已学会</span>
                        </div>
                        <div class="student_information_new_VIII">
                            <span onclick="nextItemDetail(1)">下一题</span>
                        </div>--%>
                    </div>
                    <div class="student_information_new_VV">
                        <span onclick="javascript:jQuery('#answer_div').show();">显示答案</span>
                    </div>
                    <div id="answer_div" class="student_information_new_VVI" style="display:none">
                        <div class="student_information_new_VVI_info">
                            <div class="student_information_new_VVI_answer">
                                <dl>
                                    <dt>答案</dt>
                                    <dd id="answer"></dd>
                                </dl>
                               
                            </div>
                        </div>
                    </div>
                </div>
             </div>
    	</div>
    </div>
</div>
    </div>
    <%@include file="../common_new/foot.jsp" %>

<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('itempool',function(itempool){
    	itempool.init();
    });
</script>
    </body>
</html>