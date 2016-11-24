/* 
* @Author: Tony
* @Date:   2015-11-19 11:40:23
* @Last Modified by:   Tony
* @Last Modified time: 2015-12-23 10:57:05
*/

'use strict';


define(function(require,exports,module){
	var tiku = {};
	var Common = require('common');
	require('pagination');
	var state = 'book';
	var pool = 1;//1云题库，2教师题库，3校本题库
	var itemId = '';//被操作题目的id
	var currentPage = 1;//当前页码
	
   (function(){



	   $('.top-nav li').click(function(){
		   $(this).addClass('top1').siblings('li').removeClass("top1");
		   pool = $(this).attr('pool');
		   //if(1 != pool){
			//   $('.new-page-links').show();
		   //} else {
			//   $('.new-page-links').hide();
		   //}
		   var title = ['','教师','校本','教师'];
		   $('.wind-tc .tc-cont1').text('是否推送到'+title[pool]+'题库？');
		   query(1);
	   })

		$(".top-sx li").click(function(){
			$(this).addClass('top2').siblings('li').removeClass("top2");
			query(1);
		});
		$(".top-sx li:nth-child(1)").click(function(){//切换到同步教材
			$(".sx-cont1").show();
			$(".sx-cont2").hide();
			state = 'book';
		});
		$(".top-sx li:nth-child(2)").click(function(){//切换到知识点
			$(".sx-cont2").show();
			$(".sx-cont1").hide();
			state = 'knowledge';
		});
	   $('body').on('click', '.sx-list span', function(){
		   $(this).addClass('top3').siblings('span').removeClass("top3");
	   })
		$(".tc-cont2 .btn-no").click(function(){
			$(".bg").fadeOut();
			$(".wind-tc").fadeOut();
		});
	   $('body').on('click', '.ti-top .btn-ts', function(){//推送
		   itemId = $(this).parent().attr('id');
		   $(".bg").fadeIn();
		   $(".wind-tc").fadeIn();
	   })
	   $('body').on('click', '.ti-top .btn-x', function(){//删除
		   var msg = '确认删除？';
		   if(confirm(msg)){
			   itemId = $(this).parent().attr('id');
			   remove();
		   }
	   })
	   $('body').on('click', '.btn-bj', function(){//编辑
		   itemId = $(this).parent().attr('id');
		   window.open('/itempool/editMulItem.do?ids=' + itemId + '&show=0');
	   })
		$(".wind-tc .btn-ok").click(function(){//确认推送
			push();
		});
		$(".wind-tccg .btn-ok").click(function(){
			$(".wind-tccg").fadeOut();
			$(".bg").fadeOut();
		});

	   $('#regular').click(function(){
			query(1);
	   })
	   //==============================填充属性 begin===================================//
	   $('body').on('click', '.dl-sx div span', function(){
		   if('' == $(this).attr('value')){
			   query(1);
			   return;
		   }
		   var subId = $(this).parent().attr('subId');
		   var sub = $('body').find('#' + subId);

		   var requestData = {};
		   requestData.xd = $(this).attr('value');
		   requestData.type = sub.attr('typeInt');
		   requestData.st=1;
		   
		   Common.getDataAsync('/testpaper/subject.do',requestData,function(data){
			   if(requestData.type < 5){
				   Common.render({
					   tmpl: '#selectionTmpl',
					   data: data,
					   context:'#' + subId,
					   overwrite: 1
				   });
			   } else {
				   Common.render({
					   tmpl: '#selectionTmpl2',
					   data: data,
					   context:'#' + subId,
					   overwrite: 1
				   });
			   }
		   })
		   query(1);
	   })

	   $('body').on('click','.ml-title',function(){
		   $(this).attr('checked', true);//选中
		   $(this).parent().siblings().each(function(index, ele){
			   $(ele).find('div').attr('checked', false);
			   $(ele).find('li').removeClass('top4');
		   })
		   var requestData = {};
		   requestData.xd = $(this).attr('value');
		   requestData.type = $(this).closest('dl').attr('typeInt')-0+1;
		   requestData.st=1;
		   Common.getData('/testpaper/subject.do',requestData,function(data){
			   Common.render({
				   tmpl: '#selectionTmpl3',
				   data: data,
				   context:'#' + requestData.xd,
				   overwrite: 1
			   });

		   })
		   $(this).next().slideToggle();
		   $(this).toggleClass("arrow-down");
		   query(1);
	   })

	   $('body').on('click','.ml-ul>li', function(){
		   $(this).addClass('top4').siblings().removeClass('top4');
		   query(1);
	   })
      //==============================填充属性 end===================================//
		query(1);



   })()

	tiku.query = function(){
		query(1);
	}

	function query(page){
		var termType = '';//学段
		var subject = '';//学科
		var book = '';//教材版本
		var grade = '';//年级
		var chapter = '';//章
		var part = '';//节
		var area = '';//知识面
		var point = '';//知识点
		var select = '';
		var isLast = false;
		var isBook = false;
		var regular = $('#regular').siblings('input').val();
		var itemType = '';//题目类型

		for(var i=0; i<1; i++){
			if('book' == state){//按教材查询
				isBook = true;
				itemType= $('#itemType>span[class=top3]').attr('value');
				termType = formate($('#TermType span[class=top3]').attr('value'));
				if('' == termType){ select = '';break;}
				subject = formate($('#subject span[class=top3]').attr('value'));
				if('' == subject){select = termType; break;}
				book = formate($('#bookVertion span[class=top3]').attr('value'));
				if('' == book){select = subject; break;}
				grade = formate($('#grade span[class=top3]').attr('value'));
				if('' == grade){select = book;break;}
				chapter = formate($('#chapter div[checked=checked]').attr('value'));
				if('' == chapter){select = grade; break;}
				part = formate($('.sx-cont1 .ml-ul li[class=top4]').attr('value'));
				if('' == part){select = chapter;} else {select = part; isLast = true;}
			} else if('knowledge' == state){//按知识点查询
				isBook = false;
				itemType= $('#itemType1>span[class=top3]').attr('value');
				termType = formate($('#TermType1 span[class=top3]').attr('value'));
				if('' == termType){select = '';break;}
				subject = formate($('#subject1 span[class=top3]').attr('value'));
				if('' == subject){select = termType; break;}
				area = formate($('#area div[checked=checked]').attr('value'));
				if('' == area){select = subject; break;}
				point = formate($('.sx-cont2 .ml-ul li[class=top4]').attr('value'));
				if('' == point){select = area;} else {select = point; isLast = true;}
			}
		}
		//if('book' == state){//按教材查询
		//	alert('学段' + termType + '  学科' + subject + '  教材' + book + '  年级' + grade +
		//	'  章' + chapter + '  节' + part);
		//} else if('knowledge' == state){//按知识点查询
		//	alert('学段' + termType + '  学科' + subject + '  知识面' + area + '  知识点' + point);
		//}
		var isInit = true;

		var requestData = {};
		requestData.itemType = itemType;
		requestData.level = -1;
		requestData.type = pool;
		requestData.select = select;
		requestData.isLast = isLast;
		requestData.isBook = isBook;
		requestData.page = page;
		requestData.pageSize = 10;
		requestData.regular = regular;
		Common.getDataAsync('/itempool/getItems.do', requestData, function(resp){
			$('.new-page-links').html("");
			if(resp.dtoList.length > 0) {
				//分页方法
				$('.new-page-links').jqPaginator({
					totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
					visiblePages: 10,//分多少页
					currentPage: parseInt(page),//当前页数
					first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
					prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
					next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
					last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
					page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
					onPageChange: function (n) { //回调函数
						if (isInit) {
							isInit = false;
						} else {
							currentPage = n;
							query(n);
						}
					}
				});
			}

			Common.render({
				tmpl: '#itemListTmpl',
				data: resp.dtoList,
				context:'#itemList',
				overwrite: 1
			});

			if(1 == pool){
				$('.btn-x').hide();
				$('.btn-bj').hide();
			} else if(4 == pool) {
				$('.btn-ts').hide();
				$('.btn-x').hide();
				$('.btn-bj').hide();
			} else if(3 == pool){
				if($('body').attr('userRole') != 'headMaster'){
					$('.btn-x').hide();
				}
				$('.btn-bj').hide();
			}
		})
	}

	function formate(data){
		return data == null ? '' : data;
	}

	function push(){
		Common.getData('/itempool/push.do', {id:itemId, type:pool}, function(resp){
			if('200' != resp.code){
				$('.wind-tccg .tc-cont1').text('推送失败');
			} else {
				$('.wind-tccg .tc-cont1').text('推送成功');
			}

			$(".wind-tc").fadeOut();
			$(".wind-tccg").fadeIn();
			$(".wind-tccg .btn-ok").css("margin-left","159px");
		})
	}

	function remove(){
		Common.getData('/itempool/remove.do', {id:itemId, type:pool}, function(resp){
			if('200' == resp.code){
				alert("删除成功");
				query(currentPage);
			} else {
				alert("删除失败");
			}
		})
	}

	module.exports = tiku;
});

