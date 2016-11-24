$(function() {
/*index 轮播图*/
	var $key=0;  
	var $key1=0;      
	var timer=setInterval(autoplay, 5000);
	var timer1=setInterval(autoplay1, 3000);

	
	function autoplay(){
		$key++;
		if($key>4)
		{
			$key=1;  
			$("#index-banner").css("left",0);
		}
		
		$("#index-banner").stop().animate({left:-$key*1280},2000);
	};
	
	$(".right").click(function(event) {
	     autoplay();//右侧和左侧按钮效果一样
	});

	$(".left").click(function(event) {//左侧按钮开始
		$key--;
		if($key<0)
		{
			$key=3; 
			$("#index-banner").css("left","-5120px");
		}
	
		$("#index-banner").stop().animate({left:-$key*1280},2000);
	});
	
    $(".index-bannerbox").hover(function() {//  清除定时器 
      	clearInterval(timer);
      }, function() {
      	clearInterval(timer);  
      	timer=setInterval(autoplay, 5000);
    });

 /*  index-news部分  */
     $(".btnnum span").click(function(){
			$(".newsleft-top-dl dl").eq($(this).index()).show().siblings().hide();
					
	});



/*================= 下面的商标轮番 =====================*/
   function autoplay1(){
	   $key1++;
		if($key1>3)
		{
			$key1=0;  
			$(".wsp").animate({"margin-left":0},500);
		}
		
		$(".wsp").stop().animate({"margin-left":-$key1*185},500);
	   };
	      $(".view").hover(function() {//  清除定时器 
      	clearInterval(timer1);
      }, function() {
      	clearInterval(timer1);  
      	timer1=setInterval(autoplay1, 3000);
    });


});