<%@page import="com.fulaan.utils.QiniuFileUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.pojo.exercise.ExerciseAnswerDTO"%>
<%@page import="com.pojo.app.IdValuePairDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<style type="text/css" media="screen">
    html, body  { height:100%; }
    body { margin:0; padding:0; overflow:auto; }
    #flashContent { display:none; }
</style>
<head>
    <title>查看修改-复兰科技</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/question.css">
    <link rel="stylesheet" type="text/css" href="/static/css/teacherSheet.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/teacher_configuration.css">
    <script type="text/javascript" src="/static/js/jquery.js"></script>
    <script>

    </script>
    <%--==============================================--%>
</head>
<body>
<div class="task_title">
    <div class="task_title_main">
       <a class="task_title_main_I" href="javascript:history.go(-1)">返回</a>
    </div>
</div>

<div class="task_center_main">
<input type="hidden" id="swfExists" name="swfExists" value="${swfExists}"/>
    <div class="task_center_left">
    
       <c:if test="${isShow}">
        <div style="width: 770px;height: 60px;" id="configuration_img">
            <a href="javaScript:previousPage()"><img src="/img/te_first.jpg" style="margin-left: 100px;"></a>
            <a href="javaScript:nextPage()"><img src="/img/te_last.png" style="float: right;margin-right: 50px;"></a>
        </div>
        </c:if>
        
        
        
        <div class="modify_2_left_I">
            <span onclick="changeSwfUrl(1)" class="modify_2_left_II">试卷</span>
            <span onclick="changeSwfUrl(2)" class="modify_2_left_III">解析</span>
        </div>
        <div class="task_center_left_I" style="position: absolute" id="showPdf">
            <a id="viewerPlaceHolder" style="width:645px;height:950px;display:inline" class="configuration_main_left_I"></a>
        </div>
    </div>
    <div class="task_center_left_right">
        <div class="task_center_left_right_I">
            <div class="answer_I">
                              答题卡
            </div>

            <% 
            
            List<ExerciseAnswerDTO> list =( List<ExerciseAnswerDTO>)request.getAttribute("itemList");
	        if(null!=list && list.size()>0)
	        {
	        	for(ExerciseAnswerDTO dto:list)
	        	{
	        		if(dto.getTitleType()==1) //选择题
	        		{
	        			%>
	        			 <div class="answer_center_left_right_II">
			                <div class="answer_center_left_right_II_I">
			                    <span><%=dto.getTitleId() %></span>
			                    <span>选择题</span>
			                    <span><%=dto.getTotalScore() %>分</span>
			                    <span class="check_I">得分:</span><span class="check_II"><%=dto.getScore() %></span>
			                </div>
			                <div class="answer_center_left_right_II_II">
			                    <span>答案</span>
			                    <div class="answer_center_left_right_II_III">
			                       <span class="check_II_I"><%=dto.getUserAnswer() %> </span>
			                    </div>
			                    <span>正确答案</span>
			                    <div class="answer_center_left_right_II_III">
			                        <span class="check_II_II"><%=dto.getRightAnswer() %></span>
			                    </div>
			                </div>
			            </div>
	        			
	        			<%
	        		}
	        		
	        		if(dto.getTitleType()==3) //判断题
	        		{
	        			%>
	        			<div class="answer_center_left_right_II">
			                <div class="answer_center_left_right_II_I">
			                    <span><%=dto.getTitleId()%></span>
			                    <span>判断题</span>
			                    <span><%=dto.getTotalScore() %>分</span>
			                    <span class="check_I">得分:</span><span class="check_II"><%=dto.getScore() %></span>
			                </div>
			                <div class="answer_center_left_right_II_II">
			                    <span>答案</span>
			                    <div class="answer_center_left_right_II_III">
			                        <% if("1".equals(dto.getUserAnswer())){ %>
			                         <span class="check_II_I">对</span>
			                         <%} else { %>
			                         <span class="check_II_I">错</span>
			                         <%}  %>
			                    </div>
			                    <span>正确答案</span>
			                    <div class="answer_center_left_right_II_III">
                                    <% if("1".equals(dto.getRightAnswer())){ %>
                                    <span class="check_II_II">对</span>
                                    <%} else if("0".equals(dto.getRightAnswer())) { %>
                                    <span class="check_II_II">错</span>
                                    <%}
                                    else
                                    {
                                    	%>
                                    	 <span class="check_II_II">没有作答</span>
                                    	<%
                                    }
                                    %>
			                    </div>
			                </div>
			            </div>
	        			<%
	        		}
	        		
	        		if(dto.getTitleType()==4) ////填空
	        		{
	        			%>
	        			
	        			 <div class="answer_center_left_right_II">
			                <div class="answer_center_left_right_II_I">
			                   <span><%=dto.getTitleId() %></span>
			                    <span>填空题</span>
			                   <span><%=dto.getTotalScore() %>分</span>
			                    <span class="check_I">得分:</span><span class="check_II"><%=dto.getScore() %></span>
			                </div>
			                <div class="answer_center_left_right_II_II">
			                    <span>答案</span>
			                    <div class="answer_center_left_right_II_III">
			                        <span class="check_III_I"><%=dto.getUserAnswer() %></span>
			                    </div>
			                    <span>正确答案</span>
			                    <div class="answer_center_left_right_II_III">
			                        <span class="check_III_II"><%=dto.getRightAnswer()%></span>
			                    </div>
			                </div>
			            </div>
	        			<%
	        		}
	        		if(dto.getTitleType()==5) ////主观题
	        		{
	        			%>
	        			            <div class="answer_center_left_right_II">
						                <div class="answer_center_left_right_II_I">
						                   <span><%=dto.getTitleId() %></span>
						                    <span>主观题</span>
						                    <span><%=dto.getTotalScore() %>分</span>
						                    <span class="check_I">得分:</span><span class="check_II"><%=dto.getScore()!=-1?dto.getScore():"<span style='color:red'>未批改</span>" %></span>
						                </div>
						                <div class="answer_center_left_right_II_II">
						                    <span>答案</span>
						                    <div class="answer_center_left_right_VVV_III">
						                        <span class="check_III_I"><%=dto.getUserAnswer() %></span>
						                    </div>
						                </div>
						              <% 
						               if(dto.getImageList().size()>0)
						               {
						            	   %>
						            	    <div class="answer_center_left_right_VV">
							                 <%
							                  for(IdValuePairDTO imagedto :dto.getImageList())
							                  {
							                	 String imagePath= QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, imagedto.getValue().toString());
							                	  %>
							                        <img class="answer_center_left_right_VV_I" src="<%=imagePath %>">
							                	  <%
							                  }
							                 %>
							                </div>
						            	   <%
						               }
						              %>
						            </div>
	        			<%
	        		}
	        	}
	        }
            
            %>
        </div>
    </div>
</div>
</body>


<script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash.js"></script>
<script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash_debug.js"></script>

<script type="text/javascript" src="/static/js/sharedpart.js"></script>
<script type="text/javascript" src="/static/js/swfobject.js"></script>
<script type="text/javascript" src="/static/js/main.js"></script>
<script type="text/javascript" src="/static/js/ypxxUtility.js"></script>

    <%--pdf 预览 控件--%>
    <script src="/static/js/exercise/plugins/util.js"></script>
    <script src="/static/js/exercise/plugins/api.js"></script>
    <script src="/static/js/exercise/plugins/metadata.js"></script>
    <script src="/static/js/exercise/plugins/canvas.js"></script>
    <script src="/static/js/exercise/plugins/webgl.js"></script>
    <script src="/static/js/exercise/plugins/pattern_helper.js"></script>
    <script src="/static/js/exercise/plugins/font_loader.js"></script>
    <script src="/static/js/exercise/plugins/annotation_helper.js"></script>
    
    
<script type="text/javascript">
    var page=1;
    var swf;
    var pdfUrl='${pdfPath}';
    function renderPage(i,url){
        PDFJS.workerSrc = '/static/js/exercise/plugins/worker_loader.js';
        var can=' <canvas id="the-canvas" />';
        $("#showPdf").empty();
        $("#showPdf").append(can);
        'use strict';
        PDFJS.getDocument(url).then(function(pdf) {
            if(i>pdf.numPages) {
                alert("已翻到末页！")
                i=pdf.numPages;
            }
            pdf.getPage(i).then(function(page) {
                var scale = 1.4;
                var viewport = page.getViewport(scale);
                var canvas = document.getElementById('the-canvas');
                var context = canvas.getContext('2d');
                canvas.height = viewport.height;
                canvas.width = viewport.width;
                var renderContext = {
                    canvasContext: context,
                    viewport: viewport
                };
                page.render(renderContext);
            });
        });
    }
    function previousPage(){
        page=page-1;
        if(page<1) page=1;
        renderPage(page,pdfUrl);
    }
    function nextPage(){
        page+=1;
        renderPage(page,pdfUrl);
    }
    function previewSwf(url){
        var fp = new FlexPaperViewer(
                '/static/plugins/flexpaper/FlexPaperViewer',
                'viewerPlaceHolder', { config : {
                    SwfFile : escape(url),
                    Scale : 0.6,
                    ZoomTransition : 'easeOut',
                    ZoomTime : 0.5,
                    ZoomInterval : 0.2,
                    FitPageOnLoad : true,
                    FitWidthOnLoad : false,
                    FullScreenAsMaxWindow : false,
                    ProgressiveLoading : false,
                    MinZoomSize : 0.2,
                    MaxZoomSize : 5,
                    SearchMatchAll : false,
                    InitViewMode : 'SinglePage',

                    ViewModeToolsVisible : true,
                    ZoomToolsVisible : true,
                    NavToolsVisible : true,
                    CursorToolsVisible : true,
                    SearchToolsVisible : true,

                    localeChain: 'en_US'
                }});
    }
    function changeSwfUrl(num){
    	
    	var swfExists=jQuery("#swfExists").val();
    	
        if(num==1){
            //试卷url
            pdfUrl='${pdfPath}';
            if(isPcPlate() && swfExists==1){
                previewSwf('${swfPath}');
            }else {
                renderPage(1,pdfUrl);
            }
        }else{
            //解析url
            pdfUrl='${ansPdfPath}';
            if(isPcPlate()){
                previewSwf('${ansPath}');
            }else {
                renderPage(1,pdfUrl);
            }

        }
    }
    function isPcPlate(){
        //平台、设备和操作系统
        var system = {
            win: false,
            mac: false,
            xll: false
        };
        //检测平台
        var p = navigator.platform;
        system.win = p.indexOf("Win") == 0;
        system.mac = p.indexOf("Mac") == 0;
        system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);
        //跳转语句
        if (system.win || system.mac || system.xll) {
            return true;
        } else {
            return false;
        }
    }

    $(document).ready(function () {
     
    	changeSwfUrl(1);

    });

    $(function(){
        $('.answer_center_left_right_VV').each(function () {
            ypxxutility.homepage.picswitch2(this, {
                width: 600,
                height: 400
            });
        });


    });


</script>


</html>