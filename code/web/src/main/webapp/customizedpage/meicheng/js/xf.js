$(document).ready(function () {
	var xf_winwidth = $(window).width();
	var xf_winheight = $(window).height();
	$('.xf_wrap').css('min-height',xf_winheight)
});
$(window).on('resize',function(){
	var xf_winwidth = $(window).width();
	var xf_winheight = $(window).height();
	$('.xf_wrap').css('min-height',xf_winheight)
});
