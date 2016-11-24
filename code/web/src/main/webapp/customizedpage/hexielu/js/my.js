$(function() {
	var $key=0;    //是用来控制图片滚动的
	var $circle=0; //是用来控制小圆点的


	var timer=setInterval(autoplay, 3000);
	function autoplay(){

		$key++;
		if($key>4)//因为我们是5张图片，所以索引是4
		{
			$key=1; //因为我们第五张已经看过了，此时要播放的是第2张图片，索引值是1 
			$("#index-banner").css("left",0);//如果满足的条件，瞬时间 让ul 左边是0
		}
		
		$("#index-banner").stop().animate({left:-$key*1280},3000);
	}

	/*左侧按钮开始*/
	$(".left").click(function(event) {
		$key--;
		if($key<0)//因为我们是1张图片，所以如果小于0
		{
			$key=3; //我们要跳到的是第4张图片上，因此索引是3
			$("#index-banner").css("left","-5120px");//利用css快速跳转的特点，先跳到第5张上的位置
		}
	
		$("#index-banner").stop().animate({left:-$key*1280},3000);
	});


	$(".right").click(function(event) {
		 autoplay(); // 因为定时器和右边按钮是一样的，所以直接调用函数即可
	});

	

      /*  清除定时器 */
      $(".index-bannerbox").hover(function() {
      	clearInterval(timer);
      }, function() {
      	clearInterval(timer);  // 纯个人经验  开启定时器之前，首先清除定时器
      	timer=setInterval(autoplay, 3000);
      });


      /*  news部分  */
      $(".btnnum span").click(function(){
					
					$(".newsleft-top-dl dl").eq($(this).index()).show().siblings().hide();
					
				})


});