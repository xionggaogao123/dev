/*
* @Author: Tony
* @Date:   2016-11-30 10:36:33
* @Last Modified by:   Tony
* @Last Modified time: 2016-12-01 15:40:02
*/

define(['jquery', 'pagination', 'common'], function (require, exports, module) {

    var common = require('common');
    require('pagination');

    var trainDetail = {};
    trainDetail.init = function () {
    };

    $(document).ready(function () {
        $('body').on('click','.train-nav span',function(){
            $('.train-nav span').removeClass('cur1');
            $(this).addClass('cur1');
        });
        $('body').on('click','.d1f span',function(){
            $('.d1f span').removeClass('cur2');
            $(this).addClass('cur2');
        });
    	$('body').on('click','.d2f span',function(){
    		$('.d2f span').removeClass('cur2');
    		$(this).addClass('cur2');
    	});
    	$('body').on('click','.lesson-menu span',function(){
    		$(this).addClass('cur3').siblings('span').removeClass('cur3');
    	});
        $('body').on('click','.train-detail .nav span',function(){
            $(this).addClass('cur4').siblings('span').removeClass('cur4');
        });
        $('body').on('click','.train-detail .nav .sp1',function(){
            $('.train-infor').show();
            $('.train-pj').hide();
        })
        $('body').on('click','.train-detail .nav .sp2',function(){
            $('.train-infor').hide();
            $('.train-pj').show();
        })
    })

    module.exports = trainDetail;
});