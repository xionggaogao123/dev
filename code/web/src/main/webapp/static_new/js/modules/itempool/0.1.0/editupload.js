
/*
* @Author: Tony
* @Date:   2015-12-23 10:56:01
* @Last Modified by:   Tony
* @Last Modified time: 2015-12-23 11:47:26
*/

'use strict';


define(function(require,exports,module){
	var Common = require('common');
	var quesType = 0;
	var pi = 0;//属性索引
	var index = 0;//答案选项索引
	var itemId = '';//被操作题目的id
	var num = 0;//批量上传时总题目数

	var ue = UE.getEditor('myEditor',{maximumWords:20000, initialFrameHeight:200});
	var ue1 = UE.getEditor('myEditor1',{maximumWords:20000, initialFrameHeight:200});
	var ue2 = UE.getEditor('myEditor2',{maximumWords:20000, initialFrameHeight:200});
   (function(){



	   //$('#myEditor2').text('<img src="http://7xiclj.com1.z0.glb.clouddn.com/head-8094a1c9-86f0-4454-b45b-fa452fd00c42.jpg"/>');

	   $('.btn-wc').click(function(){
		   var notSaved = false;
		   $('.tr2 td').each(function(index, ele){
			   if(index>0 && !$(ele).hasClass('gou')){
				   notSaved = true;
			   }
		   })
		   if(notSaved){
			   alert("还有未保存的题目, 请先保存再上传！");
				return;
		   }
		   window.opener.tiku.query();
		   window.close();
	   })

	   //切换题目
	   $('body').on('click', '.tr1 td', function(){
		   var i = $(this).attr("i");
		   if(i > 0){
			   $(this).addClass('td-bg').siblings().removeClass('td-bg');
			   $('.tr2 td[i='+i+']').addClass('td-bg').siblings().removeClass('td-bg');
			   showDetail($(this).attr('id'));
		   }
	   })
		//添加题目
	   $('body').on('click', '.btn-add', function(){
		   num += 1;
		   $('.tr1').append('<td i="'+num+'" id="">'+transfer(num)+'</td>');
		   $('.tr2').append('<td i="'+num+'"></td>');
	   })
		//删除题目
	   $('body').on('click', '.btn-del', function(){
		   var id = $('.tr1 .td-bg').attr('id');
		   if(null == id){
			   alert("请先选中一道题！");
			   return false;
		   }
		   var message = "确认删除本题？";
		   if(confirm(message)){
			   //var id = $('.tr1 td[i='+num+']').attr('id');
			   if("" != id){
				   Common.getData('/itempool/remove.do', {id:id, type:2}, function(resp){

				   })
			   }
			   $('.tr1 .td-bg').remove();
			   $('.tr2 .td-bg').remove();
			   //num -= 1;
		   }
	   })

	   //增加分类属性
	   $('body').on('click', '#addProp', function(){
		   pi += 1;
		   $('#copy #pi').text('题目分类属性' + transfer(pi));
		   $('#paste').append($('#copy').html());
	   })
		//删除分类属性
	   $('body').on('click', '.delProp', function(){
		   pi -= 1;
		   $(this).closest('.clearfix').remove();
	   })

	   //题目类型变化时
	   $('body').on('change', '#quesType', function(){
		   quesType = $('#quesType').val();
		   $('.ans').hide();
		   $('.ans'+quesType).show();
	   })
	   //增加选择题选项
	   $('body').on('click', '.xz-add', function(){
		   index += 1;
		   $('#xzPaste').append($('#xzCopy').html());
	   })
	   //增加填空题选项
	   $('body').on('click', '.tk-add', function(){
		   index += 1;
		   $('#tkPaste').append($('#tkCopy').html());
	   })
	   //增加判断题选项
	   $('body').on('click', '.pd-add', function(){
		   index += 1;
		   $('#pdCopy input').each(function(idx, ele){
			   $(ele).prop('name','input-pd'+index);
		   })
		   var pdCopy = $('#pdCopy').html();
		   $('#pdPaste').append(pdCopy);
	   })
	   //删除题目选项
	   $('body').on('click', '.del', function(){
		   index -= 1;
		   $(this).closest('.copy').remove();
	   })

	   //选择题增加选项
	   $('body').on('click', '.add', function(){
		   var num = $(this).parent().attr('num');
		   if(num > 6){
			   return;
		   }
		   var letter = getLetter(num);
		   var html = '<label><input type="checkbox" idx="'+num+'" value="'+letter+'">'+letter+'</label>';
		   $(this).parent().find('d').append(html);
		   $(this).parent().attr('num', num-0+1);
	   })
	   //选择题减少选项
	   $('body').on('click', '.cut', function(){
		   var num = $(this).parent().attr('num')-1;
		   if(num < 1){
			   return;
		   }
		   $(this).parent().find('input:checkbox[idx='+num+']').parent().remove();
		   $(this).parent().attr('num', num);
	   })

	   function getLetter(index){
		   var letter = ['A','B','C','D','E','F','G'];
		   return letter[index];
	   }

	   //==============================填充属性 begin===================================//
	   $('body').on('change', '#paste select', function(){
		   query($(this));
	   })
	   //==============================填充属性 end===================================//
		//保存
	   $('body').on('click', '.btn-bc', function(){
		   save();
	   })

	   //编辑回显
	   if(0==show && ''!=ids){
		   $('#tm').text("编辑题目");
		   showDetail(ids);
	   }
	   if(1 == show){//批量上传
		   ids = sessionStorage.getItem("ids");
		   var idList = ids.split(',');
		   var idNameList = [];
		   for(var i in idList){
			   var idName = {};
			   num = i-0+1;
			   idName.id = idList[i];
			   idName.name = transfer(num);
			   idNameList.push(idName);
		   }
		   render({
			   tmpl: '#idListTmpl',
			   data: idNameList,
			   context:$('.e-contab'),
			   overwrite: 1
		   });
		   $('.tr1 td[i=1]').addClass('td-bg');
		   $('.tr2 td[i=1]').addClass('td-bg');
		   showDetail(idList[0]);

		   defaultXueduanAndSubject();
	   } else {//快速上传
		   $(".e-cont12").hide();
		   defaultXueduanAndSubject();
	   }

   })()

	function defaultXueduanAndSubject(){
		Common.getData('/itempool/getXueduanAndSubject.do',{},function(data){
			//alert(data.xueduan + "   " + data.subjectName);
			$('.versionTermTypeSelection').val(data.xueduan);
			query($('.versionTermTypeSelection'));
			$('.knowledgeTermtypeSelection').val(data.xueduan);
			query($('.knowledgeTermtypeSelection'));
			$('.versionSubjectSelection').find('option').each(function(){
				//alert($(this).text())
				if($(this).text() == data.subjectName){
					$(this).attr("selected", true);
					query($('.versionSubjectSelection'));
					query($('.knowledgeSubjectSelection'));
				}
			})
			$('.knowledgeSubjectSelection').find('option').each(function(){
				if($(this).text() == data.subjectName){
					$(this).attr("selected", true);
					query($('.knowledgeSubjectSelection'));
				}
			})
		})
	}

	function query($ele){
		if('' == $ele.val()){
			return;
		}
		var $parent = $ele.closest('.e-cont3');
		var subClass = $ele.attr('subClass');
		var sub = $parent.find('.' + subClass);

		var requestData = {};
		requestData.xd = $ele.val();
		requestData.type = sub.attr('typeInt');
		Common.getData('/testpaper/subject.do',requestData,function(data){
			render({
				tmpl: '#selectionTmpl',
				data: data,
				context:sub,
				overwrite: 1
			});
		})
	}

	function showDetail(id){
		itemId = id;
		if('' != id){
			Common.getData('/itempool/getItemDetail.do', {id:id}, function(resp){
				echo(resp);
			})
		}

	}

	function transfer(n){//阿拉伯数字转成汉字
		var num = ['','一','二','三','四','五','六','七','八','九'];
		var num2 = ['','十','百','千'];
		var hanzi = [];
		for(var i=0; n>0; i++){
			var yushu = n%10;
			hanzi[i] = num[yushu];
			n = (n-yushu)/10;
		}
		var ret = '';
		for(var i=hanzi.length-1; i>=0; i--){
			ret += hanzi[i] + num2[i];
		}
		return ret;
	}


	function echo(data){
		$('#quesType').val(data.itemType);
		$('#level').val(data.level);
		$('#score').val(data.score);

		//属性
		for(var i=0; i<data.bookList.length-1; i++){
			pi += 1;
			$('#copy #pi').text('题目分类属性' +transfer(pi));
			$('#paste').append($('#copy').html());
		}
		$('#paste>div').each(function(index, ele){
			var bookprop = data.bookList[index];
			var knowledgeprop = data.knowledgeList[index];
			if(null != bookprop) {
				$(ele).find('.versionTermTypeSelection').val(bookprop[0].idStr);query($(ele).find('.versionTermTypeSelection'));
				$(ele).find('.versionSubjectSelection').val(bookprop[1].idStr);query($(ele).find('.versionSubjectSelection'));
				$(ele).find('.bookVertionSelection').val(bookprop[2].idStr);query($(ele).find('.bookVertionSelection'));
				$(ele).find('.gradeSelection').val(bookprop[3].idStr);query($(ele).find('.gradeSelection'));
				$(ele).find('.chapterSelection').val(bookprop[4].idStr);query($(ele).find('.chapterSelection'));
				$(ele).find('.partSelection').val(bookprop[5].idStr);

				//$(ele).find('.versionSubjectSelection').append('<option value="' + bookprop[1].idStr + '" selected>' + bookprop[1].value + '</option>');
				//$(ele).find('.bookVertionSelection').append('<option value="' + bookprop[2].idStr + '" selected>' + bookprop[2].value + '</option>');
				//$(ele).find('.gradeSelection').append('<option value="' + bookprop[3].idStr + '" selected>' + bookprop[3].value + '</option>');
				//$(ele).find('.chapterSelection').append('<option value="' + bookprop[4].idStr + '" selected>' + bookprop[4].value + '</option>');
				//$(ele).find('.partSelection').append('<option value="' + bookprop[5].idStr + '" selected>' + bookprop[5].value + '</option>');

				$(ele).find('.knowledgeTermtypeSelection').val(knowledgeprop[0].idStr);query($(ele).find('.knowledgeTermtypeSelection'));
				$(ele).find('.knowledgeSubjectSelection').val(knowledgeprop[1].idStr);query($(ele).find('.knowledgeSubjectSelection'));
				$(ele).find('.knowledgeAreaSelection').val(knowledgeprop[2].idStr);query($(ele).find('.knowledgeAreaSelection'));
				$(ele).find('.knowledgePointSelection').val(knowledgeprop[3].idStr);query($(ele).find('.knowledgePointSelection'));
				$(ele).find('.littleknowledgePointSelection').val(knowledgeprop[4].idStr);

				//$(ele).find('.knowledgeSubjectSelection').append('<option value="' + knowledgeprop[1].idStr + '" selected>' + knowledgeprop[1].value + '</option>');
				//$(ele).find('.knowledgeAreaSelection').append('<option value="' + knowledgeprop[2].idStr + '" selected>' + knowledgeprop[2].value + '</option>');
				//$(ele).find('.knowledgePointSelection').append('<option value="' + knowledgeprop[3].idStr + '" selected>' + knowledgeprop[3].value + '</option>');
				//$(ele).find('.littleknowledgePointSelection').append('<option value="' + knowledgeprop[4].idStr + '" selected>' + knowledgeprop[4].value + '</option>');
			}
		})

		var itemType = data.itemType;
		var answer = data.answer;
		$('.ans').hide();
		$('.ans'+itemType).show();
		var answers = answer.split(',');
		if(1 == itemType){//选择题
			for(var i=0; i<answers.length-1; i++){
				index += 1;
				$('#xzPaste').append($('#xzCopy').html());
			}
			$('#xzPaste>div').each(function(index, ele){
				var ans = answers[index].split('');
				for(var i=0; i<ans.length; i++){
					$(ele).find('input:checkbox[value='+ans[i]+']').attr('checked', true);
				}
			})
		} else if(3 == itemType){//判断题
			for(var i=0; i<answers.length-1; i++){
				index += 1;
				$('#pdCopy input').each(function(idx, ele){
					$(ele).prop('name','input-pd'+index);
				})
				var pdCopy = $('#pdCopy').html();
				$('#pdPaste').append(pdCopy);
			}
			$('#pdPaste>div').each(function(index, ele){
				$(ele).find('input:radio[value='+answers[index]+']').attr('checked', true);
			})
		}else if(4 == itemType){//填空题
			for(var i=0; i<answers.length-1; i++){
				index += 1;
				$('#tkPaste').append($('#tkCopy').html());
			}
			$('#tkPaste>div').each(function(index, ele){
				$(ele).find('input').val(answers[index]);
			})
		}else if(5 == itemType){//主观题

		}
		ue1.ready(function(){
			ue1.setContent(answer);
		})
		ue.ready(function(){
			ue.setContent(data.question);
		})
		ue2.ready(function(){
			ue2.setContent(data.parse);;
		})


	}

	function save(){
		var itemType = $('#quesType').val();
		if(0 == itemType){
		   alert("请选择题型");
		   return;
		}
		var level = $('#level').val();
		if(0 == level){
		   alert("请选择难度");
		   return;
		}
		var score = $('#score').val();
		if("" == score){
		   alert("请输入分值");
		   return;
		}
		var stage = '';
		var subject = '';
		var book = $('#paste>div').map(function(index, ele){
			stage = $(ele).find('.versionTermTypeSelection').val();
			subject = $(ele).find('.versionSubjectSelection').val();
			return $(ele).find('.partSelection').val();
		}).get().join(',');
		if('' == book){
			alert("请完善同步教材版本");
			return;
		}

		var knowledge = $('#paste>div').map(function(index, ele){
			return $(ele).find('.littleknowledgePointSelection').val();
		}).get().join(',');
		if('' == knowledge){
			alert("综合知识点");
			return;
		}

		var question = ue.getContent();
		if("" == question){
		   alert("请填写题目内容");
		   return;
		}
		var parse = ue2.getContent();
		var answer = '';
		var zc = 0;//选择题答案个数
		if(1 == itemType){//选择题
		   answer = $('#xzPaste>div').map(function(index, ele){
			   return $(ele).find('input:checkbox:checked').map(function(i, e){
				   zc = i + 1;
				   return $(e).val();
			   }).get().join('');
		   }).get().join(',');
		} else if(3 == itemType){//判断题
		   answer = $('#pdPaste>div').map(function(index, ele){
			   return $(ele).find('input:radio:checked').val();
		   }).get().join(',');
		}else if(4 == itemType){//填空题
		   answer = $('#tkPaste>div').map(function(index, ele){
			   return $(ele).find('input').val();
		   }).get().join(',');
		}else if(5 == itemType){//主观题
		   answer = ue1.getContent();
		}
		//alert(answer);
		if('' == answer){
		   alert("请填写答案");
		   return;
		}

		//var props = [];
		//$('#paste>div').each(function(index, ele){
		//	var prop = {};
		//	prop.xueduan = $(ele).find('.versionTermTypeSelection').val();
		//	prop.subject = $(ele).find('.versionSubjectSelection').val();
		//	prop.book = $(ele).find('.bookVertionSelection').val();
		//	prop.grade = $(ele).find('.gradeSelection').val();
		//	prop.chapter = $(ele).find('.chapterSelection').val();
		//	prop.part = $(ele).find('.partSelection').val();
		//	prop.area = $(ele).find('.knowledgeAreaSelection').val();
		//	prop.point = $(ele).find('.knowledgePointSelection').val();
		//	props.push(prop);
		//})

		var requestData = {};

		requestData.id = itemId;
		requestData.stage = stage;
		requestData.subject = subject;
		requestData.itemType = itemType;
		requestData.level = level;
		requestData.score = score;
		requestData.book = book;
		requestData.knowledge = knowledge;
		requestData.question = question;
		requestData.answer = answer;
		requestData.parse = parse;
		requestData.zc = zc;
		requestData.sc = $('#xzPaste').find('.an-xzt').first().attr("num");

		Common.getPostData('/itempool/save.do', requestData, function(resp){
			if('200' == resp.code){
				if(1 == show){
					$('.tr2 .td-bg').removeClass('td-bg').addClass('gou');
					$('.tr1 .td-bg').prop('id',resp.message);
					itemId = resp.message;
				}
				alert("保存成功");
				if(0 == show){
					window.opener.tiku.query();
					window.close();
				}
			} else {
				alert("保存失败");
			}
		})


	}

	function render(cfg){
		var self = this,
			_data = cfg.data,
			dom = '',
			context = cfg.context,
			callback = cfg.callback,
			_tmpl = doT.template($(cfg.tmpl).html());
		if(cfg.overwrite){
			context.empty();
		}
		if(_tmpl){
			dom = $($.trim(_tmpl(_data))).appendTo(context);
		}else{
			console.log("对应的模块不存在!");
		}
		callback && callback.call(this,{data: _data,dom:dom});
	};



});