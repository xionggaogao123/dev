// JavaScript Document

$(function(){
	$(".shouk_main dl").hover(function(){
		
		$(this).addClass("cur");
		$(this).siblings().find("span").addClass("cur");
		$(this).siblings().find("p").addClass("cur");
		
		},function(){
			
		$(this).removeClass("cur");
		$(this).siblings().find("span").removeClass("cur");
		$(this).siblings().find("p").removeClass("cur");

			})
			
				
	})
	
	
$(function(){
	//菜单栏
	
	$(".stu_menu li").click(function(){
		$(this).addClass("cur").siblings().removeClass("cur")
		
		})
		
	$(".zone_out li").hover(function(){
		$(this).toggleClass("cur")
		})
	
	
	})