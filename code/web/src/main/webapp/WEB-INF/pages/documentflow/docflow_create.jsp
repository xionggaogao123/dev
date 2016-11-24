<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
<title></title>
<meta charset="utf-8">
<link rel="stylesheet" href="/static/css/inform.css">
<link rel="stylesheet" href="/static/css/style.css">
<script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript"
	src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript"
	src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
<script type="text/javascript"
	src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
<script type="text/javascript" src="/static/js/swfobject.js"></script>
<script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
<script type="text/javascript" src="/static/js/swfobject.js"></script>
<script type="text/javascript" src="/static/js/recorder.js"></script>
<script type="text/javascript" src="/static/js/main.js"></script>
<script type="text/javascript" src="/static/js/template.js"></script>
<script type="text/javascript" src="/static/js/myexam.js"></script>
<script type="text/javascript" src="/static/js/WdatePicker.js"></script>
<script type="text/javascript" src="/static/js/experienceScore.js"></script>







<script type="text/javascript">


    $(function(){
    	 $("#compile_in").css("display","block");
    })
    
     var createInfo={"all":"","users":"","docs":[],"docsNames":[],"voics":[]};  
    
     $(function(){
        $("#compile-but").click(function(){
            $(".contacts-main").css("display","block");
            $("#bg").css("display","block");
        })
        
        
         $(".infrom-YX").hover(function(){
            $(".valid-time").show();
            $(".valid-timeT").show();
        },function(){
        $(".valid-time").hide();
        $(".valid-timeT").hide();
        })
        
        
        
        /*  上传附件   ****************************************/
        $('#file_attach').fileupload({
            url: '/commonupload/doc/upload.do',
            start: function(e) {
                $('#fileuploadLoading').show();
            },
            done: function(e, data) {
             var info =data.result.message[0];
             createInfo.docs.push(info.fileKey);
             createInfo.docsNames.push(info.fileName);
             var html='<div id="'+info.id+'">';
             html+='<img src="/img/notic/inform-complie.png">';
             html+='<div>';
             html+='<span>'+info.fileName+'</span><span class="inform-kb"></span>';
             html+='<span class="inform-YY-SC" onclick="deleteDoc(\''+info.id+'\',\''+info.fileKey+'\')">删除</span>';
             html+='</div>';
             html+='</div>';
            	
             jQuery(".infrom-FJ").append(html);
             jQuery(".infrom-FJ").show();
            },
            fail: function (e, data) {

            },
            always: function (e, data) {
                $('#fileuploadLoading').hide();
            }
        });
        
    })
    
    
    function deleteDoc(id,filekey)
    {
    	jQuery("#"+id).remove();
    	
    	var index=jQuery.inArray(filekey, createInfo.docs);
    	if(index>0)
    	{
    		createInfo.docs.splice(index, 1); 
    		createInfo.docsNames.splice(index,1);
    	}
    }

    var foo = true;
    function showflash(container) {
        var mc = new FlashObject("/static/plugins/audiorecorder.swf", "recorderApp", "350px", "23px", "8");
        mc.setAttribute("id", "recorderApp");
        mc.setAttribute("name", "recorderApp");

        mc.addVariable("uploadAction", "/commonupload/doc/upload.do");
        mc.addVariable("fileName", "audio");
        mc.addVariable("recordTime", 10 * 60 * 1000);
        mc.addVariable("appName", "recorderApp");
        mc.write("myContent");
        if (foo) {
        	
   
            $(container).append($('#recorder'));
            $('#recorder .sanjiao').show();
            $("#myContent").show();
            foo = false;
        } else {
      
            $("#myContent").hide();
            $('#recorder .sanjiao').hide();
            foo = true;
        }
    }
    function loadRecorder(ob) {
        $('.recorder').attr('id', '');
        $(ob).next().attr('id', 'recorder').show();
        $('#recorder record_button').click();
    }

    
    
    function getusets(type,ist)
    {
    	 $.ajax({
             url: '/notice/users.do?type='+type+"&ist="+ist,
             type: 'get',
             contentType: 'application/json',
             success: function (res) {
               {
            	   var $contain=jQuery("#teacher_user_group");
            	   if(type== 4 && ist==-1)
            	   {
            		   $contain=jQuery("#dep_user_group");
            	   }  
            	  $contain.empty();
            	  for(var i=0;i<res.length;i++)
            	  {
                	 var html =createUserGroup(res[i],type);
                	 $contain.append(html);
            	  } 
               }
             }
         });
    }
    
    
    function toggleGroup(id)
    {
    	var cl=jQuery("#k_"+id).prop('className');
    	if(cl.indexOf("fa-caret-right")>0)
    	{
    		 $("#k_"+id).addClass("fa-caret-down").removeClass('fa-caret-right');
    		jQuery("#"+id+" li:gt(0)").show();
    	}
    	else
    	{
    		 $("#k_"+id).addClass("fa-caret-right").removeClass('fa-caret-down');
     		jQuery("#"+id+" li:gt(0)").hide();
    	}
    	
    }
    
    
    function showGroup(id)
    {
    	$("#k_"+id).addClass("fa-caret-down").removeClass('fa-caret-right');
 		jQuery("#"+id+" li:gt(0)").show();
    }
    
    
    
    function searchName(us,input)
    {
    	var text=jQuery(input).val();
    	if(!text)
    	{
    		jQuery("#"+us).find("ul").show();
    	}
    	else
    	{
    		jQuery("#"+us).find("ul").each(function(){
    			var is=isContanitText(jQuery(this).attr("id"),text);
    			if(is)
    			{
    				  showGroup(jQuery(this).attr("id"));
    			}
    			else
    				{
    				   jQuery("#"+jQuery(this).attr("id")).hide();
    				}
    		});
    	}
    }
    
    function isContanitText(id,t)
    {
    	jQuery("#"+id+" li:gt(0)").each(function(){
    		var text=jQuery(this).text();
    		if(text.indexOf(t)>0)
    			return true;
    	});
    	return false;
    }
    
    function selectAllUser(obj,id)
    {
    	var text=jQuery(obj).text();
    	if(text=='全选')
    	{
    		jQuery("#"+id+" li:gt(0)").each(function(){
        		jQuery(this).trigger("click");
        	});
    		jQuery(obj).text("已全选");
    		jQuery(obj).unbind();
    	}
    	
    }
    
    function createUserGroup(obj,ty)
    {
    	try
    	{
	    	 var groupInfo =obj.t;
	    	 var users=obj.list;
	    	 
	    	 var html='<ul id="'+groupInfo.idStr+ty+'">';
	    	 html+='<li class="contacts-even SM"  >';
	    	 html+='<div class="comtacts-sv" name="comtacts-sv">';
	    	 html+='<i id="k_'+groupInfo.idStr+ty+'" class="comtacts-ss fa fa-caret-right" id="comtacts-ss" onclick="toggleGroup(\''+groupInfo.idStr+ty+'\')"></i>';
	    	 html+='</div>';
	    	 html+='<img src="/img/notic/contacts-WJ.png"><span>'+groupInfo.value+'</span>';
             html+='<span class="teacher_user_QX" onclick="selectAllUser(this,\''+groupInfo.idStr+ty+'\')">全选</span>';
	    	 html+='</li>';
	    	 
	    	 for(var j=0;j<users.length;j++)
	    	 {
	    		 html+='<li id="li_'+users[j].idStr+'" class="contacts-odd XL" onclick="selectUser(\''+users[j].idStr+'\',\''+users[j].value+'\')">';
	    		 html+=users[j].value;
	    		 html+=' </li>';
	         }
	    	 html+='</ul>';
	    	 return html;
    	}catch(x)
    	{
    		
    	}
    }
   
    
    function selectAll(tag)
    {
    	var text="全校老师";
    	var u=jQuery("#selectUser1").val();
    	var index=u.indexOf(text);
    	if(index<0)
    	{
    	 createInfo.all=createInfo.all+tag+",";
    	 jQuery("#selectUser1").val(u+" "+text);
    	}
    }
    
    function selectUser(id,user)
    {
    	var u=jQuery("#selectUser1").val();
    	var index=u.indexOf(user);
    	if(index<0)
    	{
    		  createInfo.users=createInfo.users+id+",";
    	      jQuery("#selectUser1").val(u+" "+user);
    	}
    }
    
    
    function sureUser()
    {
    	var u=jQuery("#selectUser1").val();
    	if(!u)
    	{
    		alert("请选择用户");
    		return;
    	}
        jQuery("#user_input").val(u);
        
        $(".contacts-main").css("display","none");
        $("#bg").css("display","none");
    }
    
    
    function sureUserCancel()
    {
    	jQuery("#selectUser1").val("");
    	createInfo.all="";
    	createInfo.users="";
        $(".contacts-main").css("display","none");
        $("#bg").css("display","none");
    }
    
    
    
    function userChoose(ug)
    {
    	jQuery(".contacts-bottom-H").removeClass("contacts-bottom-H");
    	jQuery("#"+ug+"_li").addClass("contacts-bottom-H");
    	var gs=["xiaoyuan_div","dep_div","teacher_div"];
    	
    	for(var i=0;i<=2;i++)
    	{
    		var s=gs[i];
    		if(s==ug)
    		{
    			 jQuery("#"+s).show();
    		}
    		else
    		{
    			 jQuery("#"+s).hide();
    		}
    	}
    	
    	if(ug=="teacher_div")
    	{
    		 getusets(1,1);
    	}
    	if(ug=="dep_div")
    	{
    		getusets(4,-1);
    	}
    }
    
    function submit()
    {
    	var title=jQuery("#title_input").val();
    	if(!title || title.length>50)
    		{
    		 alert("标题内容非空，并且不超过50字");
    		 return;
    		}
    	jQuery("#title").val(title);
    	jQuery("#all").val(createInfo.all)
    	if(!createInfo.all && !createInfo.users)
    	{
    		alert("请添加公文流转接收者");
    		return;
    	}	
    	jQuery("#users").val(createInfo.users)
    	jQuery("#content").val(getContent())
    	
    	var voicePath=$('.voice').attr('url');
    	if(voicePath)
    	{
    	  jQuery("#voiceFile").val(voicePath)
    	}
    	jQuery("#docFile").val(getDocFile())
    	jQuery("#docNames").val(getDocNames())
    	
    	
    	var ischeck=jQuery("#isSyn_check").prop("checked");
    	if(ischeck)
    	{
    		jQuery("#isSyn").val("1");
    		 var beginTime;
    		 var endTime;
    		 try
    		 {
    			    if(!jQuery("#bTime").val() || !jQuery("#eTime").val())
    			    {
    			    	 alert("请正确选择开始时间");
    			    	 return;
    			    }
    	
    		         beginTime=getTime(jQuery("#bTime").val());
    		   
    		         endTime=getTime(jQuery("#eTime").val());
    		         
    		         if(beginTime>=endTime)
    		         {
    		        	     alert("请正确选择时间");
    				         return;
    		         }
    		         
    		         jQuery("#beginTime").val(beginTime);
    		         jQuery("#endTime").val(endTime);
    		        
    		 }catch(x)
    		 {
    		        alert("请正确选择时间");
    		        return;
    		 }
    	}
    	jQuery("#noticeForm").submit();
        //scoreManager("发送公文流转", 1);
    }
    
    
    function getContent()
    {
    	return ue.getContent();
    }
    
 
    function getDocFile()
    {
    	var fs="";
    	for(var f in createInfo.docs )
    	{
    		 fs=fs+createInfo.docs[f]+",";
    	}
    	return fs;
    }
    
    function getDocNames()
    {
    	var fs="";
    	for(var f in createInfo.docsNames )
    	{
    		 fs=fs+createInfo.docsNames[f]+"|";
    	}
    	return fs;
    }
    
    
    function getTime(s)
	{
		try
		{
		  var str=s+":00";
		  str=str.replace(/-/g,"/");
	      var longValue=new Date(str).getTime();
	      return longValue; 
		}catch(x){
			
		} 
		return -1;
	}
	
    function cancel()
    {
    	jQuery(".inform-popup-I").show();
    }
	
    
    function hideUserGroup()
    {
    	 $(".contacts-main").css("display","none");
         $("#bg").css("display","none");
    }
    
    function goIndex()
    {
    	location.href="/notice/docflow.do";
    }
    
    </script>
<style type="text/css">
    #ueditor_0{
        width: 480px !important;
        height: 100px !important;
    }
</style>

<script type="application/javascript">
var ue=new UE.ui.Editor({

});
ue.render('ueditor_0');
</script>
</head>
<body>
	<div class="inform-all">
		<!-- 页头 -->
		<%@ include file="../common_new/head.jsp" %>
		<!-- 页头 -->
		<div class="informm-main">
			<!--左侧导航-->
			<%@ include file="../common_new/col-left.jsp" %>
			<!--===========================编辑公文流转======================================-->
			<div class="inform-main">

				<form id="noticeForm" action="/notice/newdocflow.do" method="POST">
					<input type="hidden" id="title" name="title" value="">
					<input type="hidden" id="all" name="all" value="-1"> 
					<input type="hidden" id="isSyn" name="isSyn" value="-1"> 
					<input type="hidden" id="users" name="users" value=""> 
					<input type="hidden" id="content" name="content" value=""> 
					<input type="hidden" id="beginTime" name="beginTime" value=""> 
					<input type="hidden" id="endTime" name="endTime" value=""> 
					<input type="hidden" id="voiceFile" name="voiceFile" value=""> 
					<input type="hidden" id="docFile" name="docFile" value="">
					<input type="hidden" id="docNames" name="docNames" value=""> 

				</form>
				<!--===========================编辑公文流转===============================-->
				<div class="compile-main" id="compile_in">
					<div class="compile-top">
						<span>收件人</span><input type="" name=" " value="" id="user_input">
						<button id="compile-but">通讯录</button>
					</div>
					<div class="compile-top">
						<span>标题</span> <input id="title_input" class="compile-top-input"
							type="" name="" value="">
					</div>
					<div class="compile-hr">
						<span>编辑公文流转的内容</span>
					</div>



					<div class="inform-BJ">
						<script id="container" name="content" type="text/plain">
       
            </script>
						<!-- 配置文件 -->
						<script type="text/javascript"
							src="/static/plugins/ueditor/ueditor.config.js"></script>
						<!-- 编辑器源码文件 -->
						<script type="text/javascript"
							src="/static/plugins/ueditor/ueditor.all.js"></script>
						<!-- 实例化编辑器 -->
						<script type="text/javascript">
		        var ue = UE.getEditor('container');
		        
		    </script>
					</div>


					<div class="compile-add">
						<div id="recordercontainer1"
							style="clear: both; margin-top: 8px; margin-left: 55px;float: left">
							<div id="recorder" class="">
								<div class="area">
									<span class="a12" onclick="showflash('recordercontainer1')">
										<img src="/img/mic.png" style="width: 19px; height: 19px;" />
										添加语音
									</span>

									<div
										style="padding-top: 10px; position: absolute; z-index: 50000;">
										<div class="sanjiao"
											style="width: 0; height: 0; border-left: 8px solid transparent; border-right: 8px solid transparent; border-bottom: 10px solid rgb(100, 100, 100); position: absolute; display: inline-block; top: 0px; left: 10px; display: none;"></div>
										<div id="myContent"></div>
									</div>
								</div>
								<form id="uploadForm" name="uploadForm"
									action="audiosave.jsp?userId=${currentUser.id}&voicetype=0&isinform=1">
									<input name="authenticity_token" value="xxxxx" type="hidden">
									<input name="upload_file[parent_id]" value="1" type="hidden">
									<input name="format" value="json" type="hidden">
								</form>
							</div>
							
							
							
							
							
						</div>
						<div id="attacher"
							style="overflow: hidden; margin-top: 12px; margin-left: 20px;">
							<label for="file_attach" style="cursor: pointer"> <img
								src="/img/fileattach.png" /> 添加附件
							</label> <img src="/img/loading4.gif" id="fileuploadLoading"
								style="display: none;" />
							<div style="width: 0; height: 0; overflow: visible">
								<input id="file_attach" type="file" name="file" value="添加附件"
									size="1" style="width: 0; height: 0; opacity: 0">
							</div>
						</div>
					</div>
                       
                       
                       
                       <div id="voice_notice" class="infrom-YY" style="display:none;">
                       
                       
                            <!-- 
                            <div></div> 
				            <img src="/img/notic/BF.png">
				            <div>
				                <span></span>
				                <span class="inform-YY-SC">删除</span>
				            </div>
				 -->
				        </div>


					<div class="infrom-FJ" style="display: none;"></div>
					<div class="inform-bottom">
						<div class="inform-bottom-left">
							<input id="isSyn_check" type="checkbox" name=""><span>同步到日历</span>
						</div>
						<div class="inform-bottom-middle">
							<img src="/img/notic/inform-time.png"><span
								class="infrom-YX">有效时间</span><span class="testB">(选填)</span> <span>开始</span><input
								type="text" id="bTime"
								onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"> <span>结束</span><input
								type="text" id="eTime"
								onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
						</div>
						<div class="infrom-right">
							<button onclick="submit()">发送</button>
							<button id="QX" onclick="cancel()">取消</button>
						</div>
					</div>


					<!--===========================有效时间说明==================================-->
					<div class="valid-time">
						<span class="valid-time-I">*有效时间</span> <span>有效时间是同步到日历中显示的时间，有效时间过后公文流转中自动显示为失效时间</span>
					</div>
					<div class="valid-timeT"></div>
				</div>
			</div>

			<div id="bg" class="bg"></div>
			<div class="contacts-main" style="display: none;">

				<div class="contacts-top">
					<span class="contacts-top-left">添加收件人</span> <span
						class="contacts-top-right-I">
						<button class="contacts-top-right" onclick="sureUserCancel()">取消</button>
						<button class="contacts-top-right-II" onclick="sureUser()">确定</button>
					</span>
				</div>
				<div class="contacts-IN">
					<input id="selectUser1">
				</div>

				<div class="contacts-middle">
					<ul>
						<li id="xiaoyuan_div_li" class="contacts-bottom-H" onclick="userChoose('xiaoyuan_div')">校园</li>
						<li id="dep_div_li"  onclick="userChoose('dep_div')">部门</li>
						<li id="teacher_div_li" onclick="userChoose('teacher_div')">老师</li>
					</ul>
				</div>
				<div id="xiaoyuan_div">
					<div class="contacts-bottom">
						<div class="contacts-bottom-text">
							<ul id="ul">
								<li class="contacts-odd" onclick="selectAll(1)">
									<div class="comtacts-sv" name="comtacts-sv"></div> <span>全校老师</span>
								</li>
							</ul>
						</div>
					</div>
				</div>


				<div id="dep_div" style="display: none">
					<div class="contacts-bottom-nav">
						<ul>
							<li>部门</li>
						</ul>
					</div>
					<div class="contacts-bottom">
						<div id="dep_user_group" class="contacts-bottom-text"></div>
					</div>
				</div>

				
				<div id="teacher_div" style="display: none">
					<div class="contacts-pl" style="display: none;">
						<input placeholder="输入关键字检索" onchange="searchName('teacher_user_group',this)" >
					</div>
					<div class="contacts-bottom-nav">
						<ul>
							<li onclick="getusets(1,1)">学科</li>
							<li onclick="getusets(2,1)">班级</li>
							<li onclick="getusets(3,1)">班主任</li>
						</ul>
					</div>
					<div class="contacts-bottom">
						<div id="teacher_user_group" class="contacts-bottom-text"></div>
					</div>
				</div>
				<div id="student_div" style="display: none">
					<div class="contacts-pl" style="display: none;">
						<input placeholder="输入关键字检索" >
					</div>
					<div class="contacts-bottom-nav">
						<ul>
							<li>班级</li>
						</ul>
					</div>
					<div class="contacts-bottom">
						<div id="student_user_group" class="contacts-bottom-text"></div>
					</div>
				</div>
				<div id="parent_div" style="display: none">
					<div class="contacts-pl" style="display: none;">
						<input placeholder="输入关键字检索" >
					</div>
					<div class="contacts-bottom-nav">
						<ul>
							<li>班级</li>
						</ul>
					</div>
					<div class="contacts-bottom">
						<div id="parent_user_group" class="contacts-bottom-text"></div>
					</div>
				</div>
			</div>


			<!--===============================編輯取消弹出框=======================================-->
			<div class="inform-popup-I">
				<div class="inform-popup-top-I">
					<span>提示</span>
				</div>
				<div class="inform-popup-middle-I">
					<span>取消后将不保存草稿，您确定要取消编辑吗？</span>
				</div>
				<div class="infrom-popup-bottom-I">
					<button id="infrom-bottom-QX"
						onclick="goIndex()">取消编辑</button>
					<button id="infrom-bottom-BQ"
						onclick="javascript:jQuery('.inform-popup-I').hide();">不取消</button>
				</div>
			</div>
		</div>
	</div>
    <jsp:include page="../common_new/foot.jsp"></jsp:include>
</body>
</html>
