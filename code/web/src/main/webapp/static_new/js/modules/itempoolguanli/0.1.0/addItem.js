/**
 * Created by liwei on 15/9/06.
 */
/**
 * @author 李伟
 * @module  增加题目
 * @description
 * 添加题库操作
 */
/* global Config */
define(['common','imguploader','fileuploader'],function(require,exports,module){
    /**
     *初始化参数
     */
    var addItem = {html:''},
        Common = require('common'),
        fileuploader = require('fileuploader'),
        imguploader = require('imguploader');

    /**
	 * 公共变量
	 */
	var allOptionHtml = "<option value='ALL'>--请选择--</option>";
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * ziyuan.init()
     */

    addItem.init = function(){


        //图片上传
        imguploader();
        //文件上传
        fileuploader();

        //实例化编辑器
        var um = UE.getEditor('myEditor',{
            initialFrameHeight:280
        });

        var um = UE.getEditor('myEditor2',{
            initialFrameHeight:280
        });
        var um = UE.getEditor('myEditor3',{
            initialFrameHeight:280
        });
        var htmlDom = '<div class="form-col">'+
        '    <div class="form-head clearfix">'+
        '        <h4>题目分类属性</h4>'+
        '        <span>同步教材和综合知识点都需同时填写 <em>删除</em></span>'+
        '    </div>'+
        '    <div class="form-main clearfix complex-value-class">'+
        '        <dl>'+
        '            <dt>同步教材版本</dt>'+
        '            <dd>'+
        '                <label>学段</label>'+
        '                <select class="versionTermTypeSelection" subClass="versionSubjectSelection" typeInt="1">'+
        '                </select>'+
        '            </dd>'+
        '            <dd>'+
        '                <label>学科</label>'+
        '                <select class="versionSubjectSelection" subClass="bookVertionSelection" typeInt="2">'+
        '                </select>'+
        '            </dd>'+
        '            <dd>'+
        '                <label>教材版本</label>'+
        '                <select class="bookVertionSelection" subClass="gradeSelection" typeInt="3">'+
        '                </select>'+
        '            </dd>'+
        '            <dd>'+
        '                <label>年纪/课本</label>'+
        '                <select class="gradeSelection" subClass="chapterSelection" typeInt="4">'+
        '                </select>'+
        '            </dd>'+
        '            <dd>'+
        '                <label>章</label>'+
        '                <select class="chapterSelection" subClass="partSelection" typeInt="5">'+
        '                </select>'+
        '            </dd>'+
        '            <dd>'+
        '                <label>节</label>'+
        '                <select class="partSelection" subClass="0" typeInt="6">'+
        '                </select>'+
        '            </dd>'+
        '        </dl>'+
        '        <dl>'+
        '            <dt>综合知识点</dt>'+
        '            <dd>'+
        '                <label>学段</label>'+
        '                <select class="knowledgeTermtypeSelection" subClass="knowledgeSubjectSelection" typeInt="1">'+
        '                </select>'+
        '            </dd>'+
        '            <dd>'+
        '                <label>学科</label>'+
        '                <select class="knowledgeSubjectSelection" subClass="knowledgeAreaSelection" typeInt="2">'+
        '                </select>'+
        '            </dd>'+
        '            <dd>'+
        '                <label>知识面</label>'+
        '                <select class="knowledgeAreaSelection" subClass="knowledgePointSelection" typeInt="7">'+
        '                </select>'+
        '            </dd>'+
        '            <dd>'+
        '                <label>知识点</label>'+
        '                <select class="knowledgePointSelection" subClass="littleknowledgePointSelection" typeInt="8">'+
        '                </select>'+
        '            </dd>'+
        '            <dd>'+
        '                <label>知识点</label>'+
        '                <select class="littleknowledgePointSelection" subClass="0" typeInt="8">'+
        '                </select>'+
        '            </dd>'+
        '        </dl>'+
        '    </div>'+
        '</div>';
        
       
        
        $('.add-ziyuan-btn').on('click',function(){
            $('.add-ziyuan-col').append(htmlDom);
          //初始化新加入的属性
            findAndFillEmptySelection();
        });

        $('.add-ziyuan-col').on('click','.form-head em',function(){
            $(this).closest('.form-col').remove();
        })
        /**
         * 下拉菜单更改
         */
    	 $("#quesType").on("change",function(){
    		 var _this = $(this);
    		 var checkAnswerHtml='<div class="answer-check clearfix check-id">'+
             '    <label><input type="checkbox" name="answer" value="对"/>对</label>'+
             '    <label><input type="checkbox" name="answer" value="错"/>错</label>'+
             '    <em>X</em>'+
             '</div>';
    		 
    		 var chooseAnswerHtml = '<div class="answer-check addCheck choose-id">'+
    		 '    		                         <label><input type="checkbox" value="A"/>A</label>'+
    		 '    		                         <label><input type="checkbox" value="B"/>B</label>'+
    		 '    		                         <label><input type="checkbox" value="C"/>C</label>'+
    		 '    		                         <label><input type="checkbox" value="D"/>D</label>'+
    		 '    		                         <div class="addItems" data-attr="A_B_C_D">'+
    		 '    		                             <span  class="icon-plus"><img src="../static_new/images/add.png"></span>'+
    		 '    		                             <span  class="icon-minus"><img src="../static_new/images/add1.png"></span>'+
    		 '    		     						<em>X</em>'+
    		 '    		           				</div>'+
    		 '    		        </div>';
    		 
    		 var blanksAnswerHtml='<div class="answer-check clearfix blank-id">'+
    		 '    <input type="text" class="answer-txt" />'+
    		 '    <em>X</em>'+
    		 '</div>';
    		 var select_content = _this.find('option:selected').val();
        	 if("0"== select_content){
        		 $("#answer_form").hide();
        	 }else{
        		 $("#answer_form").show();
        	 };
        	 if("5" == select_content){
        		 $(".add-answer").hide();
        	 }else{
        		 $(".add-answer").show();
        	 }
    		 if("3"== select_content){
    			 $("#check_id").show().siblings('.answer-check').hide();
                 addItem.html = checkAnswerHtml;
        		 return;
        	 };
        	 if("1"== select_content){
        		 $("#choose_id").show().siblings('.answer-check').hide();
                 addItem.html = chooseAnswerHtml;
         		 return;
        	 };
        	 if("4"== select_content){
        		 $("#blanks_id").show().siblings('.answer-check').hide();
                 addItem.html = blanksAnswerHtml;
          		 return;
        	 };
        	 if("5"== select_content){
        		 $("#zhug_id").show().siblings('.answer-check').hide();
                 addItem.html = '';
        		 $(".ask-txt").off("change");
        		 return;
        	 };
        	 
    	 });

        $('.add-answer').on('click',function(){
             $('.answer-col').append(addItem.html);
             
         });
         $('.form-main').on('click','.answer-check em',function(){
             $(this).closest('.answer-check').remove();
         });
         
          
         /*
          * 选择题加减号
          */
        
         function additem(arr,sarr){
             var varr= [];
             for(var i=0;i<arr.length;i++){
                 for(var j=0;j<sarr.length;j++){
                     if(sarr[i] != arr[i]){
                         varr.push(arr[i]);
                         break;
                     }
                 }
             }
             return varr;
         }

        function addjian(type,_this){
            var _this = _this,
                sarr = _this.parent().attr('data-attr').split('_'),
                varr = additem(['A','B','C','D','E','F','G'],sarr);
            _this.parent().siblings('label').remove();

            var num = parseInt(_this.parent().parent().attr('num'));
            if(type){
                if(varr[0]){
                    _this.parent().parent().attr('num', num+1);
                    sarr.push(varr[0]);
                }
            }else{
                if(sarr.length>1){
                    _this.parent().parent().attr('num', num-1);
                    sarr.pop();//删除当前数组并返回最后一个元素
                }
            }
            _this.parent().attr('data-attr',sarr.join('_'));
            for(var i in sarr){
                _this.parent().before('<label><input type="checkbox" value="'+sarr[i]+'"/>'+sarr[i]+'</label>');
            }
        }

          $('.answer-col').on('click','.addItems span.icon-plus',function(){
        	  addjian(1,$(this));
          });
          
          $('.answer-col').on('click','.addItems span.icon-minus',function(){
        	  addjian(0,$(this));
          });
          
        /**
         * 所有下拉菜单在这里
         */
        //初始化页面开始时的下拉或新增的下拉
        function findAndFillEmptySelection(){
        	var allVersionRootSelections = $(".versionTermTypeSelection");
        	var allKnowledgeRootSelections = $(".knowledgeTermtypeSelection");
        	for(var i = 0;i < allVersionRootSelections.length;i++){
        		var subVersionSelection = $(allVersionRootSelections[i]);
        		var subKnowledgeSelection = $(allKnowledgeRootSelections[i]);
        		if(subVersionSelection.find("option").length == 0 && subKnowledgeSelection.find("option").length == 0){
        			//TODO  挨个初始化
        			queryAndeFillSelection("versionTermTypeSelection","ALL",subVersionSelection);
        			queryAndeFillSelection("knowledgeTermtypeSelection","ALL",subKnowledgeSelection);
        		}
        	}
        }
      //下拉菜单查询填充方法
        function queryAndeFillSelection(selectionClass,parentObjId,$parentDom){
        	if(selectionClass == "0"){
        		return;
        	}
//        	var $selection = $("#" + selectionId);
        	var $selection = $($parentDom.closest(".complex-value-class").find("." + selectionClass).get(0));
        	if(parentObjId == "ALL" && selectionClass != "versionTermTypeSelection" && selectionClass !="knowledgeTermtypeSelection"){
        		if(selectionClass != "0"){
            		resetLittleBrothers($selection);
            	}
        		return;
        	}
        	var querySelectionObj = {};
        	querySelectionObj.type = $selection.attr("typeInt");
        	querySelectionObj.parentId = parentObjId;
        	$.ajax({
        		type: "POST",
                url: "/resourceDictionary/querySubResourcesForManager.do",  
                data: querySelectionObj,
                dataType: "json",
                success: function(data){
                	fillSelection($selection,data.datas);
                	if($selection.attr("subClass") != "0"){
                		var $needResetSel = $($parentDom.closest(".complex-value-class").find("." + $selection.attr("subClass")).get(0));
                		resetLittleBrothers($needResetSel);
                	}
                } 
        	});
        }
        
        //填充下拉菜单的方法
        function fillSelection($selectionDom,srcDatas){
        	var targetSelection = $selectionDom;
        	targetSelection.html(allOptionHtml);
        	for(var i=0;i<srcDatas.length;i++){
        		var subData = srcDatas[i];
        		targetSelection.append("<option value='" + subData.id + "'>" + subData.name + "</option>");
        	}
        }
       //置空弟弟们
        function resetLittleBrothers($targetSel){
        	if($targetSel.length == 0){
        		return;
        	}else{
        		$targetSel.html(allOptionHtml);
        		if($targetSel.attr("subClass") != "0"){
        			var $needResetSel = $($targetSel.closest(".complex-value-class").find("." + $targetSel.attr("subClass")).get(0));
            		resetLittleBrothers($needResetSel);
        		}
        	}
        }
        //给所有资源字典类型选择下拉菜单增加事件 
        $('body').on('change','.complex-value-class select',function(){
        	queryAndeFillSelection($(this).attr("subClass"),$(this).val(),$(this));
        	$(this).find("[value|='ALL']").remove();
        });
        //页面开始时的初始化
        findAndFillEmptySelection();

        
        /**
         * 新增按钮的方法
         */
        $('#add').on('click',function(){
        	 
        	//获取相应字段值
        	var addObj = {};
            var itemType=$("#quesType").val();
        	if("0"==itemType){
        		alert("请选择题型！");
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
            addObj.itemType=itemType;
            addObj.level = level;
            addObj.score = score;
            
        	var verStr = "";
        	var knoStr = "";
        	//检查信息是否填写完整并获取相应字段
        	var $allSelections = $('.complex-value-class');
        	for(var i=0;i<$allSelections.length;i++){
        		var $subSel = $($allSelections[i]);
                addObj.stage = $subSel.find('.versionTermTypeSelection').val();
                addObj.subject = $subSel.find('.versionSubjectSelection').val();
        		if($subSel.find('.partSelection').val() == "ALL" || $subSel.find('.littleknowledgePointSelection').val() == "ALL"){
        			alert("请将属性信息填写完整！");
        			return;
        		}
        		verStr = verStr + $subSel.find('.partSelection').val();
        		knoStr = knoStr + $subSel.find('.littleknowledgePointSelection').val();
        		if(i != ($allSelections.length - 1)){
        			verStr = verStr + ",";
        			knoStr = knoStr + ",";
        		}
        	}
            addObj.book = verStr;
            addObj.knowledge = knoStr;
        	
        	//题目内容
            addObj.question=UE.getEditor('myEditor').getContent();
            if(addObj.question == null|| addObj.question == ""){
            	alert("请填写题目内容！");
            	return;
            }
            //选择
            //正确答案
            var rightAnswer=[];
            if('1'==itemType){
                addObj.sc = $('.answer-col .choose-id').first().attr("num");
                var choose_count=$('.answer-col .choose-id');
                for(var n = 0; n < choose_count.length; n++){
                    if(choose_count.eq(n).find("input[type=checkbox]:checked").val()==null||choose_count.eq(n).find("input[type=checkbox]:checked").val()==""){
                        if(n==0){
                            alert("请填写正确答案！");
                            return;
                        }
                        alert("请将答案填写完整！");//对新增一条答案的空值判断
                        return;
                    }
                }
                var xuanzeAnswer = "";
                var zc= 0;//选择题答案个数
                for (var j = 0; j < choose_count.length; j++) {
                    var arr=[];
                    choose_count.eq(j).find("input[type=checkbox]:checked").each(function(){
                        if(xuanzeAnswer==""){
                            xuanzeAnswer = $(this).val()
                        }else{
                            xuanzeAnswer += "," + $(this).val();
                        }
                        zc++;
                    });
                    if(j != choose_count.length - 1){
                        xuanzeAnswer = xuanzeAnswer + choose_count.eq(j).find("input").length + "_";
                    }
                }
                rightAnswer.push(xuanzeAnswer);
            }
            //填空题//填空提交验证是否为空
            if('4'==itemType){
                var txt=$('.blank-id input');// 获取所有文本框
                for (var i = 0; i < txt.length; i++) {
                    if($(txt[i]).val()==null || $(txt[i]).val() ==""){
                        if(i==0){
                            alert("请填写正确答案！");
                            return;
                        }
                        alert("请将答案填写完整！");
                        return;
                    }
                    rightAnswer.push($(txt[i]).val()); // 将文本框的值添加到数组中
                }
            }
            if('3'==itemType){
                var radi=$('.check-id :checked');
                var chooseid=$('.check-id');
                for (var i = 0; i < chooseid.length; i++) {
                    if($(radi[i]).val()==null||$(radi[i]).val()==""){
                        if(i==0){
                            alert("请填写正确答案！");
                            return;
                        }
                        alert("请将答案填写完整！");
                        return;
                    }
                }
                for (var i = 0; i < radi.length; i++) {
                    rightAnswer.push($(radi[i]).val());
                }

            }
            addObj.answer=rightAnswer.join(',');

            //主观题
    		if("5"==itemType){
                addObj.answer=UE.getEditor('myEditor3').getContent();
                if(addObj.answer==null||addObj.answer==""){
                    alert("请填写正确答案！");
                    return;
                }
    		}
    		//答案解析
        	addObj.parse=UE.getEditor('myEditor2').getContent();
            //检查题目类型、内容、正确答案和答案解析
        	if(addObj.parse == null || addObj.parse == ""){
            	alert("请填写答案解析！");
            	return;
            }
            if(!confirm("是否确认新增？")){
                return;
            }
            addObj.zc = zc;
        	$.ajax({
        		type: "POST",
                url: "/itempool/saveItem.do",
                data: addObj,
                dataType: "json",
                success: function(data){
                	if(data.code == "200"){
                		location.href = "/itempool/frontList.do";
                	}else{
                		alert("操作失败,请联系管理员！");
                	}
                } 
        	});
        });
        $('.reset-btn').on('click',function(){
        	location.href = "/itempool/frontList.do";
        });
        $('body').on('click','.check-id input',function(){
        	if($(this)[0].checked){
        		$(this).parent().siblings().find('input').get(0).checked = false;
        	}
        })

    };


    (function(){
        addItem.init();
    })();

    module.exports = addItem;
    
})













