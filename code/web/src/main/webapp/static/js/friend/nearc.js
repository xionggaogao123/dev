/*
* @Author: Tony
* @Date:   2016-09-19 17:15:53
* @Last Modified by:   Tony
* @Last Modified time: 2016-09-20 17:42:18
*/

'use strict';
$(document).ready(function(){
	$('.hd-nav span').click(function(){
		$(this).addClass('hd-green-cur').siblings('.hd-nav span').removeClass('hd-green-cur');
	})
	$('.hd-nav span:nth-child(1)').click(function(){
		$('.hd-cont-f1').show();
		$('.hd-cont-f2').hide();
	});
	$('.hd-nav span:nth-child(2)').click(function(){
		$('.hd-cont-f1').hide();
		$('.hd-cont-f2').show();
	});
	$('.wind-yins .p1 em,.wind-yins .p2 .btn2').click(function(){
		$('.wind-yins').fadeOut();
		$('.bg').fadeOut();
	});
	$('.p-all-hb > .btn2').click(function(){
		$('.wind-yins').fadeIn();
		$('.bg').fadeIn();
	});
	$('.hd-cont-f > .p1 span').click(function(){
		$(this).addClass('hd-cf-cur2').siblings('.hd-cont-f > .p1 span').removeClass('hd-cf-cur2');
	})
})