/*
* @Author: Tony
* @Date:   2016-05-30 11:28:27
* @Last Modified by:   Tony
* @Last Modified time: 2016-05-31 14:02:51
*/

'use strict';
$(document).ready(function(){
	$('.type-nav span').click(function(){
		$(this).addClass('span-cur').siblings('.type-nav span').removeClass('span-cur');
	});
	$('.type-nav span:nth-child(1)').click(function(){
		$('.cont-ltbk').show();
		$('.cont-sqrt').hide();
	});
	$('.type-nav span:nth-child(2)').click(function(){
		$('.cont-ltbk').hide();
		$('.cont-sqrt').show();
	})
})