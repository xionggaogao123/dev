/*
* @Author: Tony
* @Date:   2016-11-30 10:36:33
* @Last Modified by:   Tony
* @Last Modified time: 2016-12-01 15:40:02
*/

'use strict';
	
    $(function(){
        $('.train-nav span').click(function(){
            $('.train-nav span').removeClass('cur1');
            $(this).addClass('cur1');
        });
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
        $('.train-detail .nav span').click(function(){
            $(this).addClass('cur4').siblings('span').removeClass('cur4');
        });
        $('.train-detail .nav .sp1').click(function(){
            $('.train-infor').show();
            $('.train-pj').hide();
        })
        $('.train-detail .nav .sp2').click(function(){
            $('.train-infor').hide();
            $('.train-pj').show();
        })

        

    })