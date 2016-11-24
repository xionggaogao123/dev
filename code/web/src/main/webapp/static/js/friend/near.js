/*
* @Author: Tony
* @Date:   2016-09-08 11:03:52
* @Last Modified by:   Tony
* @Last Modified time: 2016-09-12 17:31:01
*/

'use strict';
$(document).ready(function(){
	$('.btn-wdbq').click(function(){
		$('.bg').fadeIn();
		$('.wind-biaoq').fadeIn();
	});
	$('.wind-act-det .p1 em,.wind-act-det .p7 .b2').click(function(){
		$('.wind-act-det').fadeOut();
		$('.bg').fadeOut();
	});
	$('.wind-act-fq .p1 em,.wind-act-fq .p4 .b2').click(function(){
		$('.wind-act-fq').fadeOut();
		$('.bg').fadeOut();
	});
	$('.btn-fbgb').click(function(){
		$('.wind-act-fq').fadeIn();
		$('.bg').fadeIn();
	});
	$('body').on('click','.ul-nearnews li button',function(){
		$('.wind-act-det').fadeIn();
		$('.bg').fadeIn();
	})

	});