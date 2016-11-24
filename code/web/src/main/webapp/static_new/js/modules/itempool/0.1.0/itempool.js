define('itempool',['jquery','doT','common'],function(require,exports,module){
	 var itempool = {};
     require('jquery');
     require('doT');
     common = require('common');
      
	 var grades={
			   "2":[
			        {"id":"1","name":"一年级"},
			        {"id":"2","name":"二年级"},
			        {"id":"3","name":"三年级"},
			        {"id":"4","name":"四年级"},
			        {"id":"5","name":"五年级"},
			        {"id":"6","name":"六年级"},
			       ],
			   "4":[
				    {"id":"7","name":"初一"},
				    {"id":"8","name":"初二"},
				    {"id":"9","name":"初三"},
				   ],
			   "8":[
					{"id":"10","name":"高一"},
					{"id":"11","name":"高二"},
					{"id":"12","name":"高三"},
				   ],
     }
      
	 var subjects3=[
	                {"id":"1","name":"语文"},
	                {"id":"2","name":"数学"},
	                {"id":"3","name":"外语"},
	               ];
	 
	 var subjects5=[
	                {"id":"1","name":"语文"},
	                {"id":"2","name":"数学"},
	                {"id":"3","name":"外语"},
	                {"id":"4","name":"物理"},
	                {"id":"5","name":"化学"},
	               ];
	 
	 
	 var gradeSubjects={"1":subjects3,"2":subjects3,"3":subjects3,"4":subjects3,"5":subjects3,"6":subjects3,"7":subjects5,"8":subjects5,"9":subjects5,"10":subjects5,"11":subjects5,"12":subjects5,}
	 var param={"ty":"1",  "nianduan":"55d41e47e0b064452581269a",  "grade":"1","subject":"1","cond":"","sunCond":"","itemType":"","eType":"1","currentIndex":0};
	 
	 var itemIds=[];
	 
	 changeNianduan=function(n)
	 {
		 jQuery("#kw_scope_div,#kw_point_div,#chubanshe_div,#grade_div,#danyuan_div,#zhangjie_div").empty();
		 param.nianduan=n;
		 showSubject();
	 }
	 
	 showSubject=function()
	 {
		 jQuery("#subject_div").empty();
		 var url="/testpaper/subject.do?xd="+param.nianduan;
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
			 {
				 common.render({tmpl: $('#subject_script'), data: rep, context: '#subject_div'});
				 param.subject=rep[0].idStr;
				 if(param.ty=="1")
				 {
					   kwScope();
				 }
				 if(param.ty=="2")
				 {
					 shubanshe(param.subject);
				 }
			 }
		 });
	 }
	 
	 changeSubject=function(sub)
	 {
		 param.subject=sub;
		 param.cond="";
		 param.sunCond="";
		 
		 jQuery("#kw_scope_div,#kw_point_div,#chubanshe_div,#grade_div,#danyuan_div,#zhangjie_div").empty();
		 if(param.ty=="1")
		 {
			   kwScope();
		 }
		 if(param.ty=="2")
		 {
			 shubanshe(sub);
		 }
	 }
	 
	 
	 
	 shubanshe= function(b)
	 {
		
		 jQuery("#chubanshe_div").empty();
		 var url="/testpaper/subject.do?xd="+b+"&type=3";
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
			 {
					   param.cond=rep[0].idStr;
					   common.render({tmpl: $('#chubanshe_script'), data: rep, context: '#chubanshe_div'});
					   param.eType=rep[0].idStr;
					   grade();
			 }
		 });
	 }
	 
	 changeEtype=function(type){
		 jQuery("#grade_div,#danyuan_div,#zhangjie_div").empty();
		 param.eType=type;
		 grade();
	 }
	 
	 
	 grade= function()
	 {
		 jQuery("#grade_div").empty();
		 var url="/testpaper/subject.do?xd="+param.eType+"&type=4";
		 common.getData(url,{},function(rep){
			 jQuery("#grade_div").empty();
			 if(rep.length>0)
			 {
					  
					   param.grade=rep[0].idStr;
					   common.render({tmpl: $('#grade_script'), data: rep, context: '#grade_div'});
					   danyuan();
					   
			 }
		 });
	 }
	 
	 changeGrade=function(n)
	 {
		 jQuery("#danyuan_div,#zhangjie_div").empty();
		 param.grade=n;
		 danyuan();
	 }
	 
	 danyuan= function()
	 {
		 jQuery("#danyuan_div").empty();
		 var url="/testpaper/subject.do?xd="+param.grade+"&type=5";
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
			 {
					   param.cond=rep[0].idStr;
					   common.render({tmpl: $('#danyuan_script'), data: rep, context: '#danyuan_div'});
					   selectdy(param.cond);
			 }
		 });
	 }
	 
	 
	 selectdy=function(dy)
	 {
		 jQuery("#zhangjie_div").empty();
		 var url="/testpaper/subject.do?xd="+dy+"&type=6";
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
			 {
					   common.render({tmpl: $('#zhangjie_script'), data: rep, context: '#zhangjie_div'});
			 }
		 });
	 }
	 
	 selectSubCond=function(obj)
	 {
		 var id =jQuery(obj).attr("id");
		 if(jQuery(obj).is(":checked"))
		 {
		  param.sunCond=param.sunCond+id+",";
		 }
		 else
		  {
			 param.sunCond=param.sunCond.replace(id,"");
		  }
	 }
	 
	 
	 
	 /**
	  * 得到知识面
	  */
	 kwScope= function()
	 {
		 jQuery("#kw_scope_div,#kw_point_div").empty();
		 var url="/testpaper/subject.do?xd="+param.subject+"&type=7";
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
			 {
					   param.cond=rep[0].idStr;
					   common.render({tmpl: $('#kw_scope_script'), data: rep, context: '#kw_scope_div'});
					   kwPoint();
			 }
		 });
	 }
	 
	 /**
	  * 选择知识面
	  * @param obj
	  */
	 selectScope=function(obj)
	 {
		 var id =jQuery(obj).attr("id");
		 param.cond=id;
		 kwPoint();
	 }
	 
	 /**
	  * 得到知识点
	  */
	 kwPoint= function()
	 {
		 jQuery("#kw_point_div").empty();
		 var url="/testpaper/subject.do?xd="+param.cond;
		 common.getData(url,{},function(rep){
			common.render({tmpl: $('#kw_point_script'), data: rep, context: '#kw_point_div'});
			
			for(var i=0;i< rep.length;i++)
			{
				var kw=rep[i];
				if(param.sunCond.indexOf(kw.idStr)>=0)
				{
					jQuery("#"+kw.idStr).attr("checked","checked");
				}
			}
		});
	 }
	 
	 
	 
	 changeType=function(i)
	 {
		 param.ty=i;
		 param.cond="";
		 param.sunCond="";
		 jQuery(".current2").removeClass("current2");
		 jQuery("#type"+i).addClass("current2");
		 
		 if(i=="1")
		 {
			 jQuery("#kw_scope_li,#kw_point_li").show();
			 jQuery("#jiaocai_li,#zhangjie_li,#grade_li,#danyuan_li").hide();
			 itempool.kwScope();
	     }
		 else
		 {
			 jQuery("#kw_scope_li,#kw_point_li").hide();
			 jQuery("#jiaocai_li,#zhangjie_li,#grade_li,#danyuan_li").show();
			 //itempool.zhangjie();
			 shubanshe(param.subject);
		 }
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	
	
	 
	 
	 itempool.zhangjie=function()
	 {
		 jQuery("#zhangjie_div").empty();
		 var url="/testpaper/bs/list.do?subject="+param.subject+"&eType="+param.eType+"&grade="+param.grade;
		 common.getData(url,{},function(rep){
				common.render({tmpl: $('#zhangjie_script'), data: rep, context: '#zhangjie_div'});
				for(var i=0;i< rep.length;i++)
				{
					var kw=rep[i];
					if(param.sunCond.indexOf(kw.idStr)>=0)
					{
						jQuery("#"+kw.idStr).attr("checked","checked");
					}
				}
			});
	 }
	 
	

	 
	 
	
	
	 
	 selectItemType=function(obj)
	 {
		 var id =jQuery(obj).attr("value");
		 if(jQuery(obj).is(":checked"))
		 {
		  param.itemType=param.itemType+id+",";
		 }
		 else
		  {
			 param.itemType=param.itemType.replace(id,"");
		  }
	 }
	 
	 
	
	
	 

	 
	 //开始答题 
	 beginAnswerItem=function()
	 {
		 if(!param.sunCond)
		 {
			 var alertText=param.eType=="1"?"请选择知识点":"请选择章节";
			 alert(alertText);
			 return;
		 }
		 
		 if(!param.itemType)
		 {
			 alert("请选择题目类型");
			 return;
		 }
		 var isDoned=0;
		 if(jQuery("#tong26").is(":checked")) isDoned=1;
		 var url="/itempool/items/ids.do?types="+param.itemType+"&subject="+param.subject+"&isDoned="+isDoned;
		 if( param.ty=="1")
		 {
			 url+="&cltys="+param.sunCond;
		 }
		 else
		 {
			 url+="&bs="+param.sunCond;
		 }
		 
		 common.getData(url,{},function(res){
			 
			 if("200"==res.code)
			 {
				 itemIds=res.message;
				 param.currentIndex=0;
				 if(itemIds.length>0)
				 {
				   itemDetail();
				   jQuery("#Main").hide();
				   jQuery("#TU1").show();
				 }
				 else
				 {
					 alert("没有题目,请重新选择条件查询");
				 }
		     }
			 else
			 {
				 alert(res.message);
		     }
		 });
	 }
	 
	 itemDetail=function()
	 {
		 jQuery("#TU1").empty();
		 if(param.currentIndex>=itemIds.length)
		 {
			 alert("没有题目了");
			 return;
		 }
			
		 var id=itemIds[param.currentIndex];
		 common.getData("/itempool/items/detail.do",{"id":id},function(res){
			 res.totalItem=itemIds.length;
			 res.currentIndex=param.currentIndex+1;
			 common.render({tmpl: $('#itemDetail_script'), data: res, context: '#TU1'});
		 });
		 param.currentIndex=param.currentIndex+1;
	 }
	 
	 checkAnswer=function()
	 {
		 jQuery("#allok").show();
	 }
	 
	 addErrorPool=function()
	 {
		 var id=itemIds[param.currentIndex-1];
		 var myAmswer=jQuery("#myAnswer").val();
		 common.getData("/itempool/add.do",{"itemId":id,"answer":myAmswer},function(res){
			 if("200"==res.code)
			 {
				 itemDetail();
			 }
		 });
	 }
	 
	 itempool.init=function(){
		 showSubject();
	 }
	
     module.exports=itempool;
});
