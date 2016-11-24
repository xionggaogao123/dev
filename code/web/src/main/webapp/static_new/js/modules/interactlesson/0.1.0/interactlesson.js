define('interactlesson',['jquery','doT','common'],function(require,exports,module){
	
	 var interactlesson = {};
    
	 require('jquery');
     require('doT');
     common = require('common');
      
     var param={};
     classlist=function()
	 {
		 var url="/interactLesson/classes.do";
		 common.getData(url,{},function(rep){
			 if(rep.length==0)
				 {
				  location.href="/interactLesson/empty.do?version=1a";
				 }
			 if(rep.length>0)
			 {
				 common.render({tmpl: $('#class_script'), data: rep, context: '#class_ui'});
				 param.classId=rep[0].idStr;
				 videolist();
			 }
		 });
	 }
	 
     
     
     videolist=function()
	 {
    	 jQuery("#vodeo_div").empty();
		 var url="/interactLesson/list.do?classId="+param.classId;
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
			 {
				 common.render({tmpl: $('#video_script'), data: rep, context: '#vodeo_div'});
			 }
		 });
	 }
     
     
     removeLesson=function(id)
     {
    	 
    	 var d = window.confirm("确定删除该视频吗");
    	 if(d)
    	 {
	    	 var url="/interactLesson/remove.do?id="+id;
			 common.getData(url,{},function(rep){
				 if(rep.code=="200")
				 {
					 jQuery("#"+id).remove();
				 }
				 else
				 {
					  alert(rep.message);
				 }
			 });
    	}
     }
     
     
     
     pushLesson=function(id)
     {
    	 
    	 var d = window.confirm("确定要推送此课程吗");
    	 if(d)
    	 {
	    	 var url="/interactLesson/push.do?id="+id;
			 common.getData(url,{},function(rep){
				 if(rep.code=="200")
				 {
					 jQuery("#push_"+id).remove();
					 alert("推送成功");
				 }
				 else
				 {
					  alert(rep.message);
				 }
			 });
    	}
     }
	 
     
     chanageClass=function(id)
     {
    	 jQuery("#"+param.classId).removeClass();
    	 param.classId=id;
    	 jQuery("#"+id).addClass("cur currt");
    	 videolist();
     }
     
     
     interactlesson.init=function(){

    	 classlist();
	 }
     interactlesson.init1=function(){

	 }
     
     module.exports=interactlesson;
});
