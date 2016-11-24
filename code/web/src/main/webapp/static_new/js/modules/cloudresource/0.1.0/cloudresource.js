
define('cloudresource',['jquery','doT','common','initPaginator'],function(require,exports,module){
	
	
    jQuery(document).ready(function($) {
    	
  
    	/*$('.xinxi .st2').click(function(){
    		alert("a");
    	})*/
        	
        
        
        
        $('.video-s .gou').click(function(){
            $(this).toggleClass('gou-b');
            $(this).toggleClass('disblock');
            $(this).next().toggleClass('disblock');
        });
       
        $(".btn-vo .btn-no").click(function(){
            $(".wind-push-lesson").fadeOut();
            $(".bg").fadeOut();
            
            
           
        });
        $('.wind-top span').click(function(){
            $(".wind-push-lesson").fadeOut();
            $(".bg").fadeOut();
            
        });
        
        
        
        
    });
	 var cloudresource = {};
     require('jquery');
     require('doT');
     common = require('common');
     Paginator = require('initPaginator');
	 var param={"ty":"2", 
			    "nianduan":"55d41e47e0b064452581269a",
			    "grade":"1",
			    "subject":"55d41e47e0b064452581269f",
			    "cond":"55d41e47e0b0644525813ffc",
			    "sunCond":"55d41e47e0b0644525819109",
			    "itemType":"",
			    "eType":"55d41e47e0b0644525819105",
			    "currentIndex":0,
			    "totalPage":0,
			    "searchName":"",
			    "filetype":-1,
			    "more":1, //隐藏
			    "dypage":0
			    };
	 
	 var countArr=[29,411,24,37,40,32,76,29,411,24,37,40,32,76,23,28,34,30,25,31,26,28,26,29,411,24,37,40,32,76];
	 
	 
	 var dyArr=["5642fdc663e7744b8017bbed",
	            "5642fdc663e7744b8017bbec",
	            "5642fdc663e7744b8017bbde",
	            "5642fdc663e7744b8017bbe5",
	            "5642fdc663e7744b8017bbe3",
	            "5642fdc663e7744b8017bbdf",
	            "5642fdc663e7744b8017bbda",
	            "5642ff2663e7a366a9a5fc1c",
	            "5642ff2663e7a366a9a5fc1a",
	            "5642ff2663e7a366a9a5fc21",
	            "5642ff2663e7a366a9a5fc20",
	            "5642ff2663e7a366a9a5fc28",
	            "5642ff2663e7a366a9a5fc29",
	            "5642ff2663e7a366a9a5fc2a",
	            "5655752663e79141a6b07845",
	            "565573fe63e790200fa26b9d",
	            "565573fe63e790200fa26b9e",
	            "565573fe63e790200fa26b9f",
	            "565573fe63e790200fa26b9b",
	            "565573fe63e790200fa26b9c",
	            "565573fd63e790200fa26a99",
	            "565573fd63e790200fa26a98",
	            "565573fd63e790200fa26a97",
	            "5642f67d63e7cc71a47e3b13",
	            "5642f67d63e7cc71a47e3b14",
	            "5642f67d63e7cc71a47e3b19",
	            "5642f67d63e7cc71a47e3b1a",
	            "5642f67d63e7cc71a47e3b1e",
	            "5642f67d63e7cc71a47e3b1c",
	            "5642f67c63e7cc71a47e3b11"
	            ];
	 
	 
	 
	 changeType=function(i)
	 {
		 if(param.ty!=i)
		 {
			 jQuery("#ty"+param.ty).removeClass("hoverClass");
			 jQuery("#ty"+i).addClass("hoverClass");
			 
			 param.ty=i;
			 param.cond="";
			 param.sunCond="";
			 
			 //kw_point_li,grade_li,danyuan_li,zhangjie_li
			 if(i=="1")
			 {
				 jQuery("#kw_scope_li").show();
				
				 if(param.more==1)
				 {
					jQuery("#kw_point_li").hide();
					jQuery("#type_li").hide();
				 }
				 else
				 {
					jQuery("#kw_point_li").show();
					jQuery("#type_li").show();
				 }
				 jQuery("#jiaocai_li").hide();
				 jQuery("#grade_li").hide();
				 jQuery("#danyuan_li").hide();
				 jQuery("#zhangjie_li").hide();
				 kwScope();
		     }
			 else
			 {
				 jQuery("#kw_scope_li").hide();
				 jQuery("#kw_point_li").hide();
				 jQuery("#jiaocai_li").show();
				 
				 if(param.more==1)
				 {
					 jQuery("#grade_li").hide();
				     jQuery("#danyuan_li").hide();
					 jQuery("#zhangjie_li").hide();
					 jQuery("#type_li").hide();
				 }
				 else
				 {
					 jQuery("#grade_li").show();
					 jQuery("#danyuan_li").show();
					 jQuery("#zhangjie_li").show();
					 jQuery("#type_li").show();
				 }

				 shubanshe(param.subject);
			 }
		 }	 
	 }
	 
	 var moreType=1;
	 showMore=function()
	 {
		 if(param.ty=="1")
		 {
			 if(param.more==1)
			 {
				jQuery("#kw_point_li").show();
				 jQuery("#type_li").show();
				 jQuery("#showMore_text").text("收起选项");
                 jQuery("#down img").attr("src", "../images/more_up.png");
             }
			 else
			 {
				jQuery("#kw_point_li").hide();
				 jQuery("#type_li").hide();
				 jQuery("#showMore_text").text("更多选项");
                 jQuery("#down img").attr("src", "../images/more_down.png");
			 }
			 
			 
	     }
		 else
		 {
			 if(param.more==1)
			 {
				// jQuery("#grade_li").show();
			     jQuery("#danyuan_li").show();
				 jQuery("#zhangjie_li").show();
				 jQuery("#type_li").show();
				 jQuery("#showMore_text").text("收起选项");
                 jQuery("#down img").attr("src", "../images/more_up.png");
			 }
			 else
			 {
				// jQuery("#grade_li").hide();
				 jQuery("#danyuan_li").hide();
				 jQuery("#zhangjie_li").hide();
				 jQuery("#type_li").hide();
				 jQuery("#showMore_text").text("更多选项");
                 jQuery("#down img").attr("src", "../images/more_down.png");
			 }

			// shubanshe(param.subject);
		 }
		 param.more=1-param.more;
	 }
	 
	 changeNianduan=function(n)
	 {
		 jQuery(".current").removeClass("current");
		 jQuery("#"+n).addClass("current");
		 jQuery("#kw_scope_div").empty();
		 jQuery("#kw_point_div").empty();
		 param.nianduan=n;
		 showSubject();
	 }
	 
	 showSubject=function()
	 {
		 jQuery("#subject_div").empty();
		 var url="/testpaper/subject.do?st=2&xd="+param.nianduan;
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
		 jQuery("#"+param.subject).removeClass("hoverClass");
		 jQuery("#"+sub).addClass("hoverClass");
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
		 var url="/testpaper/subject.do?st=2&xd="+b+"&type=3";
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
		 
		 jQuery("#"+param.eType).removeClass("hoverClass");
		 jQuery("#"+type).addClass("hoverClass");
		 
		 param.eType=type;
		 grade();
	 }
	 
	 
	 grade= function()
	 {
		 jQuery("#grade_div").empty();
		 var url="/testpaper/subject.do?st=2&xd="+param.eType+"&type=4";
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
		 jQuery("#"+param.grade).removeClass("hoverClass");
		 jQuery("#"+n).addClass("hoverClass");
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
		 
		 
		var index= jQuery.inArray(dy,dyArr);
		if(index>=0)
		{
			jQuery("#showmore_i").show();
		}
		else
		{
			jQuery("#showmore_i").hide();
		}
		 
		 
		 
		 jQuery("#"+param.cond).removeClass("hoverClass");
		 jQuery("#"+dy).addClass("hoverClass");
		 param.cond=dy;
		 param.dypage=0;
		 jQuery("#zhangjie_div").empty();
		 var url="/testpaper/subject.do?xd="+dy+"&type=6&skip=0&limit=20";
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
			 {
					   common.render({tmpl: $('#zhangjie_script'), data: rep, context: '#zhangjie_div'});
					   param.sunCond=rep[0].idStr;
					   loadres(1);
					  // loadPage(1);
					   
			 }
		 });
	 }
	 
	 
	 
	 selectdyMore=function()
	 {
		 param.dypage=param.dypage+1;

		 var url="/testpaper/subject.do?st=2&xd="+param.cond+"&type=6&skip="+ (param.dypage*20)+"&limit=20";
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
			 {
					   common.render({tmpl: $('#zhangjie_script_more'), data: rep, context: '#zhangjie_div'});
			 }
		 });
		 
		 var index= jQuery.inArray(param.cond,dyArr);
		 if(index>=0)
		 {
			 var count=countArr[index];
			 var acount=jQuery("#zhangjie_div").find("span").length;
			 if(acount>=count)
			 {
				 jQuery("#showmore_i").hide();
			 }
		 }
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 selectSubCond=function(id)
	 {
		 jQuery("#"+param.sunCond).removeClass("hoverClass");
		 jQuery("#"+id).addClass("hoverClass");
		 param.sunCond=id;
		 loadres(1);
		// loadPage(1);
	 }
	 
	 /**
	  * 得到知识面
	  */
	 kwScope= function()
	 {
		 jQuery("#kw_scope_div,#kw_point_div").empty();
		 var url="/testpaper/subject.do?st=2&xd="+param.subject+"&type=7";
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
	 selectScope=function(id)
	 {
		 
		 jQuery("#"+param.cond).removeClass("hoverClass");
		 jQuery("#"+id).addClass("hoverClass");
		 param.cond=id;
		 kwPoint();
	 }
	 
	 /**
	  * 得到知识点
	  */
	 kwPoint= function()
	 {
		 jQuery("#kw_point_div").empty();
		 var url="/testpaper/subject.do?st=2&xd="+param.cond;
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
				 {
					common.render({tmpl: $('#kw_point_script'), data: rep, context: '#kw_point_div'});
					param.sunCond=rep[0].idStr;
					 loadres(1);
					// loadPage(1);
				 }
		});
	 }
	 
	 loadres=function(current)
	 {
		 jQuery("#res_div").empty();
		 var url="/cloudres/list.do";
		 var par={"kwId":"","bkId":"","name":param.searchName,"type":param.filetype,"skip":(current-1)*12,limit:12};
		 if(param.ty=="1")
			 par.kwId= param.sunCond;
		 else
			 par.bkId= param.sunCond;
		 common.getData(url,par,function(rep){
			 if(rep.length>0)
			 {
					common.render({tmpl: $('#res_script'), data: rep, context: '#res_div'});
					
					
					  $('.xinxi .st2').on("click",function(){
				            $(this).toggleClass('btn-use-open');
				            $(this).children(".push-select").slideToggle();
				        });
					  
					  
					  $('.push-lessin-li').click(function(){
				            $('.wind-push-lesson').fadeIn();
				            $(".bg").fadeIn();
				            
				            jQuery("#backlist").empty();
				            jQuery("#lesson_count").text("0");
				            
				            trees1.checkAllNodes(false);
				           
				            
				        });
					  $('.tuisong_left').mouseleave(function(){
				            $(this).children().children().children(".push-select").slideUp();
				        })
			 }
		});
		 
		 
		 loadPage(current);
	 }
	 
	 loadPage=function(current)
	 {
		 
		 if(current==1)
			 {
				 var url="/cloudres/pagecount.do";
				 var par={"kwId":"","bkId":"","name":param.searchName,"type":param.filetype};
				 if(param.ty=="1")
					 par.kwId= param.sunCond;
				 else
					 par.bkId= param.sunCond;
				 common.getData(url,par,function(rep){
					 param.totalPage=rep.message;
				});
			 }
		 
		 initPage(current);
		
		 
	 }
	 
	 changeSearch=function()
	 {
		 param.searchName=jQuery("#input_name").val();
	 }
	 
	 
	 search=function()
	 {
		 var name=jQuery("#input_name").val();
		 //if(param.searchName)
		 {
			 param.searchName=name;
			 loadres(1);
			 //loadPage(1);
		 }
	 }
	 
	 
	 keyDown=function(event)
	 {
		if( event.keyCode == 13)
		{
			search();
		}
	 }
	 
	 
	 initPage=function(page)
	 {
		 var option = {
                 total:  param.totalPage,
                 pagesize: 12,
                 currentpage: page,
                 
                 
                 
                 operate: function (totalPage) {
                     $('.page-index span').each(function () {
                    	 $(this).off("click");
                         $(this).click(function () {
                         	loadres($(this).text());
                         });
                     });
                     $('.first-page').off("click");
                     $('.first-page').click(function () {
                     	loadres(1);
                     });
                     $('.last-page').off("click");
                     $('.last-page').click(function () {
                     	 loadres(totalPage);
                     });
                 }
             };
             Paginator.initPaginator(option);
	 }
	 
	 changeFileType =function (type)
	 {
		 if(type!=param.filetype)
		 {
			 jQuery("#type_"+param.filetype).removeClass("hoverClass");
			 jQuery("#type_"+type).addClass("hoverClass");
			 param.filetype=type;
			 loadres(1);
			 //loadPage(1);
		 }	 
	 }
	 
	 
	 cloudresource.init=function(){
		 loadres(1);
		// loadPage(1);

	    	 //loadPage();
	    	 $('#input_name').keydown(function (event) {
	    	        if (event.which == 13) {
	    	        	search();
	    	        }
	    	 });

	 }
     
     module.exports=cloudresource;
});
