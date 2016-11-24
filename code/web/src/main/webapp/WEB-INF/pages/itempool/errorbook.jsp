<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>错题本</title>
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
            
            <div id="error_div">
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
                                    <span id="e_subject_9" class="student-information_new_anlaysis_YW_right">0</span>
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
    </body>
</html>