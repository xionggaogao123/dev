$(function(){
	/*02 cloud 页面的js*/

	$(".cloudbannerbox ol li").mouseover(function(event) {
				var $cloud=$(this).index(); 
				$(this).addClass('current').siblings().removeClass();
				$(".cloudbannerbox ul li").eq($cloud).show().siblings().hide();
				$ckey=$cloud;
	})

	var $ckey=0; 
	var ctimer=setInterval(autoplay02, 3000);
    function autoplay02(){
				$ckey++; 
				if($ckey>3){ 				
					$ckey=0;
				}

				$(".cloudbannerbox ol li").eq($ckey).addClass('current').siblings().removeClass();
				$(".cloudbannerbox ul li").eq($ckey).show().siblings().hide();

	}

	$(".cloudbannerbox").hover(function() {//  清除定时器 
      	clearInterval(timer);
      }, function() {
      	clearInterval(timer);  
      	ctimer=setInterval(autoplay02, 3000);
    });


	$(".arrow01").bind('click', function(event) {
		$aa=0
		var $aa=$(this).index()-1;
		$(".hide").eq($aa).toggle();
	});
 /*$(".title").bind("click",function(){
       $(this).siblings(".list").slideToggle();
       $(this).children(".arrow").toggleClass("rotate-arrow");
   });*/


})