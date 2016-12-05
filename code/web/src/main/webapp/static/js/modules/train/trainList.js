/*
* @Author: Tony
* @Date:   2016-11-30 10:36:33
* @Last Modified by:   Tony
* @Last Modified time: 2016-11-30 15:28:46
*/

'use strict';
    $(function(){
    	$('.d1f span').click(function(){
    		$('.d1f span').removeClass('cur2');
    		$(this).addClass('cur2');
    	});
    	$('.d2f span').click(function(){
    		$('.d2f span').removeClass('cur2');
    		$(this).addClass('cur2');
    	});
    	$('.lesson-menu span').click(function(){
    		$(this).addClass('cur3').siblings('span').removeClass('cur3');
    	});
		$('.hide_city_group>div a').click(function(){
			$('.hide_city_group>div a').removeClass('a-selected');
			$(this).addClass('a-selected');
		})
    })