/**
 * Created by liwei on 15/7/12.
 */
/**
 * @author 李伟
 * @module  考务管理操作
 * @description
 * 考务管理操作
 */
/* global Config */
define(['common', 'ajaxfileupload', 'pagination','treeview'], function (require, exports, module) {

    //考试管理
    var EXAM_MANAGE = {
        $form: $("#examForm"),
        $currDate: $("#currDate"),
        $detailExamSubjectTitle: $("#detailExamSubjectTitle"),
        $queryByGrade: $("#queryByGrade"),
        $examList: $("#examList"),
        $pagination: $('#exam-manage-pagination'),
        $examId: $("#examId"),
        $examDate: $("#examDate"),
        $examType: $("#examType"),
        $examRemark: $("#examRemark"),
        $examName: $("#examName"),
        $model: $("#model"),
        $gradeList: $("#gradeList"),
        $examGradeId: $("#examGradeId"),
        $examGradeName: $("#examGradeName"),
        $gradeSubjectContainer: $("#gradeSubjectContainer"),
        $selectAllSubject: $("#selectAllSubject"),
        $selectedSubjects: $("#selectedSubjects"),
        $detailExamSubjectList: $("#detailExamSubjectList"),
        $back: $(".back-link", $("#kaoshi-view")),
        $detailExamSubjectListTemp: $('<tr><td></td><td></td><td></td><td></td><td></td></tr>'),
        $selectedSubjectTemp: $("<tr><td><input type='hidden' name='examSubjectDTOs.id'>" +
            "<input type='hidden' name='examSubjectDTOs.subjectId'>" +
            "<input type='text' readonly name='examSubjectDTOs.subjectName' style='text-align:center;border: none;background-color: #FFF'></td>" +
            "<td><input type='text' name='examSubjectDTOs.fullMarks' style='width:78px;text-align:center;border: none;background-color: #FFF'></td>" +
            "<td><input type='text' name='examSubjectDTOs.youXiuScore' style='width:78px;text-align:center;border: none;background-color: #FFF'></td>" +
            "<td><input type='text' name='examSubjectDTOs.failScore' style='width:78px;text-align:center;border: none;background-color: #FFF'></td>" +
            "<td><input type='text' name='examSubjectDTOs.diFenScore' style='width:78px;text-align:center;border: none;background-color: #FFF'></td>" +
            "<td><input type='text' name='examSubjectDTOs.examDate' onfocus='WdatePicker({minDate:\"%y-%M-%d\",maxDate:\"{%y+100}-%M-%d\"});' style='width:98px;text-align:center;border: none;background-color: #FFF'></td>" +
            "<td><input type='text' name='examSubjectDTOs.time' style='width:98px;text-align:center;border: none;background-color: #FFF'></td></tr>"),
        selectSubArray: [],
        $examListTemp: $('<tr><td></td><td></td><td></td><td></td><td><span class="cview">点击查看</span></td><td><span class="showConfigExamItemWindow">设置</span></td>' +
            //'<td width="210px"><span class="postscript-class"></span></td>' +
        '<td><a href="javascript:void(0)" class="icon-edit"></a>|' +
            '<a href="javascript:void(0)" class="icon-del" ></a> </td></tr>'),
        $submitExamForm: $("#submit-exam-form"),
        examPageNow:null,
        loadList: function (page) {
            var grade = EXAM_MANAGE.$examGradeId.val();
            if (grade) {
                $.ajax({
                    url: "/exam1/loadExam.do?_=" + new Date(),
                    type: "get",
                    datatype: "json",
                    data: {gradeId: grade, page: page || 1},
                    success: function (data) {
                        if (!data) {
                            return false;
                        }
                        if (data.code == "200") {
                            EXAM_MANAGE.$examList.empty();
                            EXAM_MANAGE.$pagination.empty();
                            var examList = data.message.list;
                            var examInfo;
                            var temp;
                            for (var i = 0, len = examList.length; i < len; i++) {
                                examInfo = examList[i];
                                temp = EXAM_MANAGE.$examListTemp.clone();
                                temp.data(kwgl.PAGE_CACHE_KEY, examInfo);
                                $("td:eq(0)", temp).text(i + 1);
                                $("td:eq(1)", temp).text(examInfo.name);
                                $("td:eq(2)", temp).text(examInfo.examTypeName);
                                $("td:eq(3)", temp).text(examInfo.date);
                                $("td:eq(6) span", temp).html(examInfo.remark);
                                $("td:eq(6) span", temp).attr("title",examInfo.remark);
                                EXAM_MANAGE.$examList.append(temp);
                            }
                            if (data.message.count > 0) {
                                EXAM_MANAGE.$pagination.data("trigger", false);
                                EXAM_MANAGE.$pagination.jqPaginator({
                                    totalPages: data.message.count,//总页数
                                    visiblePages: 10,//分多少页
                                    currentPage: page > data.message.count ? data.message.count : page,//当前页数
                                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                                    onPageChange: function (n) {
                                        if (EXAM_MANAGE.$pagination.data("trigger")) {
                                            EXAM_MANAGE.loadList(n);
                                            EXAM_MANAGE.examPageNow = n;
                                        }
                                    }

                                });
                                EXAM_MANAGE.$pagination.data("trigger", true);
                            }
                        } else {
                            alert(data.message);
                        }
                    }
                });
            }
        },
        resetKwglForm: function () {
            //清楚缓存的内容
            EXAM_MANAGE.selectSubArray = [];
            EXAM_MANAGE.$examId.val("");
            EXAM_MANAGE.$examDate.val(EXAM_MANAGE.$currDate.val());
            EXAM_MANAGE.$examName.val("");
            EXAM_MANAGE.$examType.val("0");
            EXAM_MANAGE.$examRemark.val("");
            EXAM_MANAGE.$selectedSubjects.empty();
            $("input.gradeSubjects:checked", EXAM_MANAGE.$form).prop("checked", false);
            EXAM_MANAGE.$selectAllSubject.prop("checked", false);
        },
        addSelectSubject: function (subject) {
            if (EXAM_MANAGE.isSelect(subject.subjectId)) {
                return;
            }
            EXAM_MANAGE.selectSubArray.push(subject.subjectId);
            var temp = EXAM_MANAGE.$selectedSubjectTemp.clone();
            temp.data("subject", subject).attr("sid", subject.subjectId);
            $("input[name='examSubjectDTOs.id']", temp).val(subject.id ? subject.id : "");
            $("input[name='examSubjectDTOs.subjectId']", temp).val(subject.subjectId);
            $("input[name='examSubjectDTOs.subjectName']", temp).val(subject.subjectName);
            $("input[name='examSubjectDTOs.fullMarks']", temp).val(subject.fullMarks ? subject.fullMarks : 100);
            $("input[name='examSubjectDTOs.youXiuScore']", temp).val(subject.youXiuScore ? subject.youXiuScore : 90);
            $("input[name='examSubjectDTOs.failScore']", temp).val(subject.failScore ? subject.failScore : 60);
            $("input[name='examSubjectDTOs.diFenScore']", temp).val(subject.diFenScore ? subject.diFenScore : 30);
            $("input[name='examSubjectDTOs.examDate']", temp).val(subject.examDate ? subject.examDate : EXAM_MANAGE.$examDate.val());
            $("input[name='examSubjectDTOs.time']", temp).val(subject.time ? subject.time : "");
            EXAM_MANAGE.$selectedSubjects.append(temp);
        },
        removeSelectSubject: function (sid) {
            $("tr[sid=" + sid + "]", EXAM_MANAGE.$selectedSubjects).remove();
            //删除缓存标识
            EXAM_MANAGE.selectSubArray.splice($.inArray(sid, EXAM_MANAGE.selectSubArray), 1);
        },
        clearSelectSubject: function () {
            EXAM_MANAGE.selectSubArray = [];
            EXAM_MANAGE.$selectedSubjects.empty();
            EXAM_MANAGE.$gradeSubjectContainer.empty();
            EXAM_MANAGE.$selectAllSubject.prop("checked", false);
        },
        addGradeSubject: function (subject) {
            EXAM_MANAGE.$gradeSubjectContainer.append('<label><input type="checkbox" class="gradeSubjects" value="' + subject.id + '">' + subject.name + '</label>');
        },
        isSelect: function (sid) {
            return $.inArray(sid, EXAM_MANAGE.selectSubArray) != -1;
        }
    };
    EXAM_MANAGE.init = function () {
    	/**
    	 * 以下是小分录入
    	 */
    	$("#examItemScoreList").delegate(".examItemScoreImportSpanClass","click",function(){
    		$("#uploadExamItemScoreInput").val("");
    		$("#importExamItemScoreBtn").attr("examId", $(this).closest("tr").attr("examId"));
    		$("#importExamItemScoreBtn").attr("subjectId",$(this).closest("tr").attr("subjectId"));  
    		//自己模拟弹窗
    		$("#bgbgbgbg").show();
    		$("#importExamItemScoreWindow").show();
//    		Common.dialog($('#importExamItemScoreWindow'));
    	});
    	//绑定导入窗口取消按钮
    	$("#cancelImportExamItenScore").click(function(){
    		$("#bgbgbgbg").hide();
    		$("#importExamItemScoreWindow").hide();
    	});
    	
    	$(document).delegate(".showConfigExamItemScoreWindow","click",function(){
//    		$(".class-edit").hide();
//    		$(".class-table").hide();
//    		$(".exam-item").show();
    		
    		$('.exam-item-score').show().siblings().hide();
    		
    		var exam = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
    		$("#examItemScoreTitle").html(exam.gradeName + exam.name + "/ 小分录入");
    		var subjects = exam.examSubjectDTO;
    		var $examItemList = $("#examItemScoreList");
    		$examItemList.empty();
    		for(var i=0;i<subjects.length;i++){
    			var subject = subjects[i];
    			var htmlStr = "<tr examId='" + exam.id + "' subjectId='" + subject.subjectId + "'><td>" + subject.subjectName + "</td>" + "<td>" + subject.fullMarks + "</td>"
    			              + "<td><span class='examItemScoreImportSpanClass'>导入</span></td></tr>";
    			$examItemList.append(htmlStr);
    		}
    		
    	});
    	//导出模板
    	$("#exportExamItemScoreModelBtn").click(function(){
    		$("#eId").val($("#importExamItemScoreBtn").attr("examId"));
    		$("#sId").val($("#importExamItemScoreBtn").attr("subjectId"));
        	$("#exportExamItemScoreModelForm").submit();
        });
    	//导入按钮导入方法开始
        $("#importExamItemScoreBtn").click(function(){
        	if($("#uploadExamItemScoreInput").val() == null || $("#uploadExamItemScoreInput").val() == ""){
        		alert("请先选择要上传的文件！");
        		return;
        	}
        	$("#bgbgbgbg").hide();
    		$("#importExamItemScoreWindow").hide(); 
        	var examId = $(this).attr("examId");
        	var subjectId = $(this).attr("subjectId");
        	//新增等待框
        	setTimeout(function(){
        		Common.dialog($('#waitWindow_score'));
        	},500);
//            Common.dialog($('#waitWindow_score'));
//            $('.bg-dialog').width($(document).width()).height($(document).height()).show(); 
//            document.getElementById("bgbgbgbg").style.display = "block";
        	$.ajaxFileUpload({
                url: '/examItemScore/importExamItemScore.do', //用于文件上传的服务器端请求地址
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'uploadExamItemScoreInput', //文件上传域的ID
                dataType: 'json', //返回值类型 一般设置为json
                param: {examId : examId,subjectId:subjectId},
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if (data.code != 200) {
                        $("#overWindowH_score").html("小分分数导入失败！" + data.message);
                        Common.dialog($('#overWindow_score'));
                    } else {
                    	$("#overWindowH_score").html("小分分数导入成功！");
                        Common.dialog($('#overWindow_score'));
                    } 
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                	 $("#overWindowH_score").html("小分分数导入失败！" + data.message);
                     Common.dialog($('#overWindow_score'));
                }
            });
        });
    	
    	
    	/**
    	 * 以下是小分设置
    	 */
        //显示详细页面
        $("#examItemList").delegate(".examItemViewSpanClass","click",function(){
        	var examId = $(this).closest("tr").attr("examId");
        	var subjectId = $(this).closest("tr").attr("subjectId");
        	$.ajax({
        		type: "POST",
                url: "/examItem/queryExamItemDetail.do",
                data: {examId : examId,subjectId:subjectId},
                dataType: "json",
                success: function(data){
                	kwgl.treeview(data.treeData);
                }
        	});
        	kwgl.treeview("");
        	Common.dialog($('#examItemDetail'));
        });
        
    	$("#examItemList").delegate(".examItemImportSpanClass","click",function(){
    		$("#uploadExamItemInput").val("");
    		$("#importExamItemBtn").attr("examId", $(this).closest("tr").attr("examId"));
    		$("#importExamItemBtn").attr("subjectId",$(this).closest("tr").attr("subjectId")); 
    		//模拟弹窗
    		$("#bgbgbgbg").show();
    		$("#importExamItemWindow").show();
//    		Common.dialog($('#importExamItemWindow'));
    	});
    	//弹窗取消按钮
    	$("#cancelImportExamItem").click(function(){
    		$("#bgbgbgbg").hide();
    		$("#importExamItemWindow").hide(); 
    	});
    	//导出模板
    	$("#exportExamItemModelBtn").click(function(){
    		$("#eIdForItem").val($("#importExamItemBtn").attr("examId"));
    		$("#sIdForItem").val($("#importExamItemBtn").attr("subjectId"));
        	$("#exportExamItemModelForm").submit();
        });
    	//导入按钮导入方法开始
        $("#importExamItemBtn").click(function(){
        	if($("#uploadExamItemInput").val() == null || $("#uploadExamItemInput").val() == ""){
        		alert("请先选择要上传的文件！");
        		return;
        	}
        	$("#bgbgbgbg").hide();
    		$("#importExamItemWindow").hide();
        	var examId = $(this).attr("examId");
        	var subjectId = $(this).attr("subjectId");
        	$.ajaxFileUpload({
                url: '/examItem/importExamItem.do', //用于文件上传的服务器端请求地址
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'uploadExamItemInput', //文件上传域的ID
                dataType: 'json', //返回值类型 一般设置为json
                param: {examId : examId,subjectId:subjectId},
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if (data.code != 200) {
                        alert(data.message);
                    } else {
                        alert("导入成功");
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    alert("导入失败!" + data.message);
                }
            });
        });
    	
    	EXAM_MANAGE.$examList.delegate(".showConfigExamItemWindow","click",function(){
//    		$(".class-edit").hide();
//    		$(".class-table").hide();
//    		$(".exam-item").show();
    		
    		$('.exam-item').show().siblings().hide();
    		
    		var exam = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
    		$("#examItemTitle").html(exam.gradeName + exam.name + "/ 小分结构设置");
    		var subjects = exam.examSubjectDTO;
    		var $examItemList = $("#examItemList");
    		$examItemList.empty();
    		for(var i=0;i<subjects.length;i++){
    			var subject = subjects[i];
    			var htmlStr = "<tr examId='" + exam.id + "' subjectId='" + subject.subjectId + "'><td>" + subject.subjectName + "</td>" + "<td>" + subject.fullMarks + "</td>"
    			              + "<td><span class='examItemImportSpanClass'>导入</span></td><td><span class='examItemViewSpanClass'>查看</span></td></tr>";
    			$examItemList.append(htmlStr);
    		}
    		
    	});
        $('.newTable').delegate('.cview', 'click', function () {
            Common.dialog($('#viewKaoshiId'));
            EXAM_MANAGE.$detailExamSubjectList.empty();
            var exam = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            var data = exam.examSubjectDTO;
            EXAM_MANAGE.$detailExamSubjectTitle.text(exam.name);
            for (var i = 0, len = data.length; i < len; i++) {
                var temp = EXAM_MANAGE.$detailExamSubjectListTemp.clone();
                $("td:eq(0)", temp).text(data[i].subjectName);
                $("td:eq(1)", temp).text(data[i].fullMarks);
                $("td:eq(2)", temp).text(data[i].examDate);
                $("td:eq(3)", temp).text(data[i].weekDay);
                $("td:eq(4)", temp).text(data[i].time);
                EXAM_MANAGE.$detailExamSubjectList.append(temp);
            }
        });

        $("#newExamInfo").click(function () {
            if (!EXAM_MANAGE.$examType.val()) {
                alert("请选择年级");
            }
            EXAM_MANAGE.resetKwglForm();
        });
        $('.back-link').on('click', function () {
            EXAM_MANAGE.resetKwglForm();
            return false;
        });

        EXAM_MANAGE.$queryByGrade.click(function () {
            if (EXAM_MANAGE.$examGradeId.val()) {
                EXAM_MANAGE.loadList();
            }
        });
        EXAM_MANAGE.$submitExamForm.click(function () {
        	var newExamName = $.trim($('#examName').val());
        	if(newExamName == ""){
        		alert("请填写考试名称！");
        		return;
        	}
        	if(newExamName.length > 15){
    			alert("考试名称过长！最长15个字！");
    			return;
    		}
        	var newSubjectHtml = $.trim($('#selectedSubjects').html());
        	if(newSubjectHtml == ""){
        		alert("请选择考试科目！");
        		return;
        	}
        	if($('#examDate').val() == ""){
        		alert("请填写考试时间！");
        		return;
        	}
            $.ajax({
                url: "/exam1/save.do",
                type: "post",
                datatype: "json",
                data: EXAM_MANAGE.$form.serialize(),
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        EXAM_MANAGE.$back.trigger("click");
                        if(EXAM_MANAGE.examPageNow == null){
                        	 EXAM_MANAGE.loadList();
                        }else{
                        	 EXAM_MANAGE.loadList(EXAM_MANAGE.examPageNow);
                        }
                    } else {
                        alert(data.message);
                    }
                }
            });
        });
        //科目全选按钮事件
        EXAM_MANAGE.$selectAllSubject.click(function () {
            if (this.checked) {
                //获取所有的科目，设置为已选择
                var selSubjects = $("input.gradeSubjects", EXAM_MANAGE.$form);
                for (var i = 0, len = selSubjects.length; i < len; i++) {
                    EXAM_MANAGE.addSelectSubject({
                        subjectId: selSubjects.eq(i).val(),
                        subjectName: selSubjects.eq(i).parent().text()
                    })
                }
            } else {
                //获取选中的科目，设置为未选择
                var sel = $("input.gradeSubjects:checked", EXAM_MANAGE.$form);
                for (var i = 0, len = sel.length; i < len; i++) {
                    EXAM_MANAGE.removeSelectSubject(sel.eq(i).attr("sid"));
                }
                EXAM_MANAGE.$selectedSubjects.empty();
            }

            //设置科目的状态
            $("input.gradeSubjects").prop("checked", this.checked);
        });

        //科目复选框选择事件
        EXAM_MANAGE.$form.delegate("input.gradeSubjects", "click", function () {
            if (this.checked) {
                //设置全选按钮的状态
                if ($("input.gradeSubjects:checked", EXAM_MANAGE.$form).length == $("input.gradeSubjects").length) {
                    EXAM_MANAGE.$selectAllSubject.prop("checked", true);
                }
                //判断下方表格是否存在此课程,如果不存在，在下方表格中添加数据
                EXAM_MANAGE.addSelectSubject({subjectId: this.value, subjectName: $(this).parent().text()});
            }
            else {
                EXAM_MANAGE.$selectAllSubject.prop("checked", false);
                EXAM_MANAGE.removeSelectSubject(this.value);
            }
        });
        EXAM_MANAGE.$examList.delegate(".icon-del", "click", function () {
            var data = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            if (confirm("确定要删除此记录吗?")) {
                $.ajax({
                    url: "/exam1/delete.do",
                    type: "get",
                    datatype: "json",
                    data: {examId: data.id},
                    success: function (data) {
                        if (!data) {
                            return false;
                        }
                        if (data.code == "200") {
                            EXAM_MANAGE.loadList();
                        } else {
                            alert(data.message);
                        }
                    }
                });
            }
        });
        EXAM_MANAGE.$examList.delegate(".icon-edit", "click", function () {
            $('.class-table').hide();
            $('.exam-item').hide();
            $('.class-edit').show();
            EXAM_MANAGE.resetKwglForm();
            var exam = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            var data = exam.examSubjectDTO;
            EXAM_MANAGE.$examId.val(exam.id);
            EXAM_MANAGE.$examName.val(exam.name);
            EXAM_MANAGE.$examDate.val(exam.date);
            EXAM_MANAGE.$examGradeId.val(exam.gradeId);
            EXAM_MANAGE.$examGradeName.val(exam.gradeName);
            EXAM_MANAGE.$examType.val(exam.examType);
            EXAM_MANAGE.$examRemark.val(exam.remark);
            EXAM_MANAGE.$model.find("input:radio[value='"+exam.type+"']").prop("checked", true);
            for (var i = 0, len = data.length; i < len; i++) {
                if ($("input.gradeSubjects[value='" + data[i].subjectId + "']").length != 0) {
                    EXAM_MANAGE.addSelectSubject(data[i]);
                    $("input.gradeSubjects[value='" + data[i].subjectId + "']").prop("checked","checked");
                }
            }
        });
        EXAM_MANAGE.$gradeList.change(function () {
            EXAM_MANAGE.clearSelectSubject();
            var value = $(this).children('option:selected').val();
            EXAM_MANAGE.$examGradeId.val(value);
            EXAM_MANAGE.$examGradeName.val($(this).children('option:selected').text());
            $.ajax({
                url: "/exam1/gradeSubject.do",
                type: "get",
                datatype: "json",
                data: {gradeId: value},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        var subjects = data.message;
                        for (var i = 0, len = subjects.length; i < len; i++) {
                            EXAM_MANAGE.addGradeSubject(subjects[i]);
                        }
                        EXAM_MANAGE.loadList();
                    }
                }
            });
        });
    };

    //成绩管理
    var SCORE_LIST = {
        $ScoreView: $("#chengji-view"),
        $genTemplateByClass: $("#genTemplateByClass"),
        $back: $(".back-link", $("#chengji-view")),
        $examList: $("#scoreViewExamList"),
        $pagination: $("#score-list-pagination"),
        $scoreGradeList: $("#scoreGradeList"),
        $scoreListQueryBtn: $("#scoreListQuery"),
        $scoreInputClassAdmin: $("#scoreInputClassAdmin"),
        $scoreByClassInputClassTeacher: $("#scoreByClassInputClassTeacher"),
        $scoreInputByClassSubjectAdmin: $("#scoreInputByClassSubjectAdmin"),
        $scoreByClassInputSubjectTeacher: $("#scoreByClassInputSubjectTeacher"),
        $filterSubjectInClassBtnAdmin: $("#filterSubjectInClassBtnAdmin"),
        $filterSubjectInClassBtnTeacher: $("#filterSubjectInClassBtnTeacher"),
        $scoreListTitleByClass: $("#scoreListTitleByClass"),
        $scoreListContentByClass: $("#scoreListContentByClass"), 
        $kaochangLuruRoomlistAdmin: $("#kaochang-luru-roomlist-admin"),
        $kaochangLuruRoomlistTeacher: $("#kaochang-luru-roomlist-teacher"),
        $kaochangLuruSubjectsAdmin: $("#kaochang-luru-subjects-admin"),
        $kaochangLuruSubjectsTeacher: $("#kaochang-luru-subjects-teacher"),
        $kaochangLuruConfirmTeacher: $("#kaochang-luru-confirm-teacher"),
        $kaochangLuruConfirmAdmin: $("#kaochang-luru-confirm-admin"),
        $scoreViewClass: $("#chakan-score-class"),
        $scoreViewSubjectContainer: $("#chakan-score-subject"),
        $ScoreViewListTitle: $("#scoreViewListTitle"),
        $ScoreViewListContent: $("#scoreViewListContent"),
        $chakanScoreOrderBy: $("#chakan-score-orderby"),
        $viewScoreExport: $("#viewscoreexport"),
        $inputByClassTitle: $("#inputByClassTitle"),
        $scoreViewTitle: $("#scoreViewTitle"),
        $importByClassForm: $("#importByClassForm"),
        $selectImportFileByClass: $("#selectImportFileByClass"),
        $inputByClassSelectedSubject: [],
        $inputByExamRoomSelectedSubject: [],
        $chengjiViewTitle: $("#chengjiViewTitle"),
        $kaochangLuruTitle: $("#kaochang-luru-title"),
        $kaochangLuruRoomlist: $("#kaochang-luru-roomlist"),
        $kaochangLuruSubjects: $("#kaochang-luru-subjects"),
        $kaochangLuruExport: $("#kaochang-luru-export"),
        $kaochangLuruImport: $("#kaochang-luru-import"),
        $kaochangLuruDatalist: $("#kaochang-luru-datalist"),
        $kaochangLuruTitles: $("#kaochang-luru-titles"),
        $examListTemp: $('<tr><td></td><td></td><td></td>' +
            '<td><span class="nj-set">录入</span></td>' +
            '<td><span class="bj-set">录入</span></td>' +
            '<td><span class="kc-set">录入</span></td>' +
            '<td><span class="showConfigExamItemScoreWindow">录入</span></td>' +
            '<td><span class="chakan-view">查看</span></td>' +
            '<td><span class="qk-set">设置</span></td>' +
            '<td><span class="kf-set">设置</span></td></tr>'),
        loadList: function (page) {
            SCORE_LIST.$examList.empty();
            SCORE_LIST.$pagination.empty();
            var grade = SCORE_LIST.$scoreGradeList.val();
            if (grade) {
                $.ajax({
                    url: "/exam1/loadExam.do?_=" + new Date(),
                    type: "get",
                    datatype: "json",
                    data: {gradeId: grade, page: page || 1},
                    success: function (data) {
                        if (!data) {
                            return false;
                        }
                        if (data.code == "200") {
                            var examList = data.message.list;
                            var examInfo;
                            var temp;
                            for (var i = 0, len = examList.length; i < len; i++) {
                                examInfo = examList[i];
                                temp = SCORE_LIST.$examListTemp.clone();
                                temp.data(kwgl.PAGE_CACHE_KEY, examInfo);
                                $("td:eq(0)", temp).text(i + 1);
                                $("td:eq(1)", temp).text(examInfo.name);
                                $("td:eq(2)", temp).text(examInfo.date);

                                if (kwgl.onlyTeacher) {
                                    if (!examInfo.open) {
                                        //如果为开放，则删除录入功能入口
                                        $("td:eq(3),td:eq(4),td:eq(5)", temp).empty();
                                    }
                                    $("td:eq(7),td:eq(8)", temp).remove();
                                    temp.append("<td>" + (examInfo.open ? "开放" : "关闭") + "</td>");
                                }
                                SCORE_LIST.$examList.append(temp);
                            }
                            if (data.message.count > 0) {
                                SCORE_LIST.$pagination.data("trigger", false);
                                SCORE_LIST.$pagination.jqPaginator({
                                    totalPages: data.message.count,//总页数
                                    visiblePages: 10,//分多少页
                                    currentPage: page > data.message.count ? data.message.count : page,//当前页数
                                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                                    onPageChange: function (n) {
                                        if (SCORE_LIST.$pagination.data("trigger")) {
                                            SCORE_LIST.loadList(n);
                                        }
                                    }
                                });
                                SCORE_LIST.$pagination.data("trigger", true);
                            }
                        } else {
                            alert(data.message);
                        }
                    }
                });
            }
        },
        getScoreText: function (score) {
            return score.showType == 1 ? "<em class='disbtn'>缺</em>" : score.showType == 2 ? "<em class='disbtn'>免</em>" : score.score;
        },
        editScoreData: function () {
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            kwgl.$commonFormDataContainer.empty();
            var classId = SCORE_LIST.getInputClassForRole();
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='classId' value='" + classId + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.id + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='gradeId' value='" + data.gradeId + "'>");
            SCORE_LIST.$inputByClassSelectedSubject = SCORE_LIST.getInputByClassSubjectForRole();
            for (var i = 0, len = SCORE_LIST.$inputByClassSelectedSubject.length; i < len; i++) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='subject' value='" + SCORE_LIST.$inputByClassSelectedSubject[i] + "'>");
            }
            $.ajax({
                url: "/score1/detail.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: kwgl.$commonFormDataContainer.serialize(),
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        SCORE_LIST.appendByClassTitleForRole();
                        SCORE_LIST.appendByClassDataListForRole(data.message);
                    } else {
                        //alert(data.message);
                    }
                }
            });
        },
        viewScoreData: function () {
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            kwgl.$commonFormDataContainer.empty();
            var selSubject = SCORE_LIST.getViewSelectSubject();
            var selClass = SCORE_LIST.getViewSelectClass();
            if (selSubject.length == 0 || selClass.length == 0) {
                return;
            }
            for (var i = 0, len = selClass.length; i < len; i++) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='classId' value='" + selClass[i] + "'>");
            }
            for (var i = 0, len = selSubject.length; i < len; i++) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='subject' value='" + selSubject[i] + "'>");
            }
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.id + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='gradeId' value='" + data.gradeId + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='order' value='" + SCORE_LIST.$chakanScoreOrderBy.val() + "'>");
            $.ajax({
                url: "/score1/detail.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: kwgl.$commonFormDataContainer.serialize(),
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        SCORE_LIST.appendViewScoreTitle();
                        SCORE_LIST.appendViewScoreContent(data.message);
                    } else {
                        //alert(data.message);
                    }
                }
            });
        },
        appendViewScoreTitle: function () {
            SCORE_LIST.$ScoreViewListTitle.empty();
            SCORE_LIST.$ScoreViewListContent.empty();
            //根据考试信息和选中的科目构建表头
            var examSubject = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY).examSubjectDTO;
            var selSubject = SCORE_LIST.getViewSelectSubject();
            var title = [];
            title.push("<th width='80'>姓名</th>");
            title.push("<th width='80'>班级</th>");
            title.push("<th width='80'>合计</th>");
            for (var i = 0, len = selSubject.length; i < len; i++) {
                for (var j = 0, lenj = examSubject.length; j < lenj; j++) {
                    if (selSubject[i] == examSubject[j].subjectId) {
                        title.push("<th width='70'>" + examSubject[j].subjectName + "</br>(" + examSubject[j].fullMarks + ")</th>");
                    }
                }
            }
            SCORE_LIST.$ScoreViewListTitle.append(title.join(""));
        },
        appendViewScoreContent: function (data) {
            var selSubject = SCORE_LIST.getViewSelectSubject();
            var dataArray = [];
            if (data && data.length != 0) {
                var score;
                for (var i = 0, len = data.length; i < len; i++) {
                    score = data[i];
                    dataArray.push("<tr>");
                    dataArray.push("<td width='80'>" + score.studentName + "</td>");
                    dataArray.push("<td width='80'>" + score.className + "</td>");
                    dataArray.push("<td width='80'>" + score.sum + "</td>");
                    for (var j = 0, lenJ = selSubject.length; j < lenJ; j++) {
                        dataArray.push("<td width='70'>" + SCORE_LIST.getScoreText(score.examScore[selSubject[j]]) + "</td>");
                    }
                    dataArray.push("</tr>"); 
                }
                SCORE_LIST.$ScoreViewListContent.append(dataArray.join(""));
            }
        },
        getViewSelectSubject: function () {
            var ret = [];
            var selList = $(":checkbox:checked", SCORE_LIST.$scoreViewSubjectContainer);
            //如果当前没有选中的科目，默认为全选
            for (var i = 0, len = selList.length; i < len; i++) {
                ret.push(selList.eq(i).val());
            }
            return ret;
        },
        getViewSelectClass: function () {
            var ret = [];
            var selList = $(":checkbox:checked", SCORE_LIST.$scoreViewClass);
            //如果当前没有选中的科目，默认为全选
            for (var i = 0, len = selList.length; i < len; i++) {
                ret.push(selList.eq(i).val());
            }
            return ret;
        },
        loadScoreByExamroom: function () {
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            kwgl.$commonFormDataContainer.empty();
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='roomId' value='" + SCORE_LIST.getInputRoomForRole() + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.id + "'>");
            SCORE_LIST.$inputByExamRoomSelectedSubject = SCORE_LIST.getInputByExamRoomSelectSubject();
            for (var i = 0, len = SCORE_LIST.$inputByExamRoomSelectedSubject.length; i < len; i++) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='subject' value='" + SCORE_LIST.$inputByClassSelectedSubject[i] + "'>");
            }
            $.ajax({
                url: "/score1/detailByRoom.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: kwgl.$commonFormDataContainer.serialize(), 
                success: function (data) {
                    if (!data) {
                        return false;
                    } 
                    if (data.code == "200") {
                        SCORE_LIST.appendByRoomTitleForRole();
                        SCORE_LIST.appendByRoomDataListForRole(data.message);
                    } else {
                        //alert(data.message);
                    }
                }
            });
        },
        appendInputByClassSubjectForRole: function (subjects) {
            if (kwgl.onlyTeacher) {
                var temp;
                SCORE_LIST.$scoreByClassInputSubjectTeacher.empty();
                for (var i = 0, len = subjects.length; i < len; i++) {
                    temp = $("<option value='" + subjects[i].subjectId + "'>" + subjects[i].subjectName + "</option>").data(kwgl.PAGE_CACHE_KEY, subjects[i]);
                    SCORE_LIST.$scoreByClassInputSubjectTeacher.append(temp);
                }
            } else {
                //管理员的科目列表为复选框
                $("label", SCORE_LIST.$scoreInputByClassSubjectAdmin).remove();
                for (var i = 0, len = subjects.length; i < len; i++) {
                    $('<label><input type="checkbox" value="' + subjects[i].subjectId
                        + '"/>' + subjects[i].subjectName + '</label>').data("info", subjects[i])
                        .insertBefore($(".gray-line-btn", SCORE_LIST.$scoreInputByClassSubjectAdmin));
                }
            }
        },
        getInputByClassSubjectForRole: function () {
            var ret = [];
            if (kwgl.onlyTeacher) {
                ret.push(SCORE_LIST.$scoreByClassInputSubjectTeacher.val());
            } else {
                var selList = $(":checkbox:checked", SCORE_LIST.$scoreInputByClassSubjectAdmin);
                if (selList.length == 0) {
                    selList = $(":checkbox", SCORE_LIST.$scoreInputByClassSubjectAdmin);
                }
                //如果当前没有选中的科目，默认为全选
                for (var i = 0, len = selList.length; i < len; i++) {
                    //如果为非教师角色或者数据为可编辑状态
                    ret.push(selList.eq(i).val());
                }
            }
            return ret;
        },
        getInputByExamRoomSelectSubject: function () {
            var ret = [];
            if (kwgl.onlyTeacher) {
                ret.push(SCORE_LIST.$kaochangLuruSubjectsTeacher.val());
            } else {
                var selList = $(":checkbox:checked", SCORE_LIST.$kaochangLuruSubjectsAdmin);
                if (selList.length == 0) {
                    selList = $(":checkbox", SCORE_LIST.$kaochangLuruSubjectsAdmin);
                }
                //如果当前没有选中的科目，默认为全选
                for (var i = 0, len = selList.length; i < len; i++) {
                    //如果为非教师角色或者数据为可编辑状态
                    ret.push(selList.eq(i).val());
                }
            }
            return ret;
        },
        appendRoomForRole: function (room) {
            var target = kwgl.onlyTeacher ? SCORE_LIST.$kaochangLuruRoomlistTeacher : SCORE_LIST.$kaochangLuruRoomlistAdmin;
            target.empty();
            for (var d in room) {
                target.append('<option value="' + d + '">' + room[d].name + '</option>');
            }
        },
        getInputRoomForRole: function () {
            return kwgl.onlyTeacher ? SCORE_LIST.$kaochangLuruRoomlistTeacher.val() : SCORE_LIST.$kaochangLuruRoomlistAdmin.val();
        },
        appendInputByRoomSubjectForRole: function (subject) {
            if (kwgl.onlyTeacher) {
                var temp;
                SCORE_LIST.$kaochangLuruSubjectsTeacher.empty();
                for (var i = 0, len = subject.length; i < len; i++) {
                    temp = $("<option value='" + subject[i].subjectId + "'>" + subject[i].subjectName + "</option>")
                        .data(kwgl.PAGE_CACHE_KEY, subject[i]);
                    SCORE_LIST.$kaochangLuruSubjectsTeacher.append(temp);
                }
            } else {
                //管理员的科目列表为复选框
                $("label", SCORE_LIST.$kaochangLuruSubjectsAdmin).remove();
                for (var i = 0, len = subject.length; i < len; i++) {
                    $('<label><input type="checkbox" value="' + subject[i].subjectId
                        + '"/>' + subject[i].subjectName + '</label>').data("info", subject[i])
                        .insertBefore($(".gray-line-btn", SCORE_LIST.$kaochangLuruSubjectsAdmin));
                }
            }
        },
        appendClassForRole: function (clazz) {
            var target = kwgl.onlyTeacher ? SCORE_LIST.$scoreByClassInputClassTeacher : SCORE_LIST.$scoreInputClassAdmin;
            target.empty();
            for (var i = 0, len = clazz.length; i < len; i++) {
                target.append('<option value="' + clazz[i].idStr + '">' + clazz[i].value + '</option>');
            }
        },
        getInputClassForRole: function () {
            return kwgl.onlyTeacher ? SCORE_LIST.$scoreByClassInputClassTeacher.val() : SCORE_LIST.$scoreInputClassAdmin.val();
        },
        appendByRoomDataListForRole: function (data) {
            var selSubject = SCORE_LIST.getInputByExamRoomSelectSubject();
            var dataArray = [];
            if (data && data.length != 0) {
                var score;
                for (var i = 0, len = data.length; i < len; i++) {
                    score = data[i];
                    dataArray.push("<tr>");
                    dataArray.push("<td width='50'>" + (i + 1) + "</td>");
                    dataArray.push("<td width='80'>" + score.studentName + "</td>");
                    dataArray.push("<td width='100'>" + score.examRoomName + "</td>");
                    dataArray.push("<td width='80'>" + score.className + "</td>");
                    for (var j = 0, lenJ = selSubject.length; j < lenJ; j++) {
                        dataArray.push("<td width='70' sid='" + score.id +
                            "' suid='" + selSubject[j] + "'>");
                        if (score.examScore[selSubject[j]].showType == 1) {
                            dataArray.push('<em class="disbtn">缺</em>');
                        } else if (score.examScore[selSubject[j]].showType == 2) {
                            dataArray.push('<em class="disbtn">免</em>');
                        } else {
                            dataArray.push("<input style='text-align:center;border:0px;width:60px;background:#FFF'" +
                                " type='text' name='subjectScore' value='" + score.examScore[selSubject[j]].score + "'>");
                        }
                        dataArray.push("</td>");
                        if (kwgl.onlyTeacher) {
                            if (score.examScore[selSubject[j]].showType == 1) {
                                dataArray.push("<td width='110'><em class='disbtn curbtn que'>缺</em></td>");
                            } else {
                                dataArray.push("<td width='110'><em class='disbtn que'>缺</em></td>");
                            }
                            if (score.examScore[selSubject[j]].showType == 2) {
                                dataArray.push("<td width='110'><em class='disbtn mian curbtn'>免</em></td>");
                            } else {
                                dataArray.push("<td width='110'><em class='disbtn mian'>免</em></td>");
                            }
                        }
                    }
                    dataArray.push("</tr>");
                }
                SCORE_LIST.$kaochangLuruDatalist.append(dataArray.join(""));
            }
        },
        appendByRoomTitleForRole: function () {
            SCORE_LIST.$kaochangLuruTitles.empty();
            SCORE_LIST.$kaochangLuruDatalist.empty();
            //根据考试信息和选中的科目构建表头
            var examSubject = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY).examSubjectDTO;
            var selSubject = SCORE_LIST.getInputByExamRoomSelectSubject();
            var title = [];
            title.push("<th width='50'>#</th>");
            title.push("<th width='80'>姓名</th>");
            title.push("<th width='100'>考场</th>");
            title.push("<th width='80'>班级</th>");
            for (var i = 0, len = selSubject.length; i < len; i++) {
                for (var j = 0, lenj = examSubject.length; j < lenj; j++) {
                    if (selSubject[i] == examSubject[j].subjectId) {
                        title.push("<th width='70'>" + examSubject[j].subjectName + "</br>(" + examSubject[j].fullMarks + ")</th>");
                    }
                }
            }
            if (kwgl.onlyTeacher) {
                title.push("<th width='70'>缺考</th><th width='70'>免考</th>");
            }
            SCORE_LIST.$kaochangLuruTitles.append(title.join(""));
        },
        appendByClassTitleForRole: function () {
            SCORE_LIST.$scoreListTitleByClass.empty();
            SCORE_LIST.$scoreListContentByClass.empty();
            //根据考试信息和选中的科目构建表头
            var examSubject = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY).examSubjectDTO;
            var selSubject = SCORE_LIST.getInputByClassSubjectForRole();
            var title = [];
            title.push("<th width='50'>#</th>");
            title.push("<th width='80'>姓名</th>");
            title.push("<th width='80'>班级</th>");
            for (var i = 0, len = selSubject.length; i < len; i++) {
                for (var j = 0, lenj = examSubject.length; j < lenj; j++) {
                    if (selSubject[i] == examSubject[j].subjectId) {
                        title.push("<th width='70'>" + examSubject[j].subjectName + "</br>(" + examSubject[j].fullMarks + ")</th>");
                    }
                }
            }
            if (kwgl.onlyTeacher) {
                title.push("<th width='70'>缺考</th><th width='70'>免考</th>");
            }
            SCORE_LIST.$scoreListTitleByClass.append(title.join(""));
        },
        appendByClassDataListForRole: function (data) {
            //根据选中的科目和成绩信息构建内容
            var selSubject = SCORE_LIST.getInputByClassSubjectForRole();
            var dataArray = [];
            if (data && data.length != 0) {
                var score;
                for (var i = 0, len = data.length; i < len; i++) {
                    score = data[i];
                    dataArray.push("<tr>");
                    dataArray.push("<td width='50'>" + (i + 1) + "</td>");
                    dataArray.push("<td width='80'>" + score.studentName + "</td>");
                    dataArray.push("<td width='80'>" + score.className + "</td>");
                    for (var j = 0, lenJ = selSubject.length; j < lenJ; j++) {
                        dataArray.push("<td width='70' sid='" + score.id + 
                            "' suid='" + selSubject[j] + "'>");
                        if (score.examScore[selSubject[j]].showType == 1) {
                            dataArray.push('<em class="disbtn">缺</em>');
                        } else if (score.examScore[selSubject[j]].showType == 2) { 
                            dataArray.push('<em class="disbtn">免</em>');
                        } else {
                            dataArray.push("<input style='text-align:center;border:0px;width:60px;background:#FFF'" +
                                " type='text' name='subjectScore' value='" + score.examScore[selSubject[j]].score + "'>");
                        }
                        dataArray.push("</td>");
                        if (kwgl.onlyTeacher) {
                            if (score.examScore[selSubject[j]].showType == 1) {
                                dataArray.push("<td width='70'><em class='disbtn curbtn que'>缺</em></td>");
                            } else {
                                dataArray.push("<td width='70'><em class='disbtn que'>缺</em></td>"); 
                            }
                            if (score.examScore[selSubject[j]].showType == 2) {
                                dataArray.push("<td width='70'><em class='disbtn mian curbtn'>免</em></td>");
                            } else {
                                dataArray.push("<td width='70'><em class='disbtn mian'>免</em></td>");
                            }
                        }
                    }
                    dataArray.push("</tr>");
                }
                SCORE_LIST.$scoreListContentByClass.append(dataArray.join(""));
            }
        }
    };
    SCORE_LIST.init = function () {
        SCORE_LIST.$kaochangLuruDatalist.delegate("em.mian", "click", function () {
            var $this = $(this);
            var parent = $this.closest("tr");
            var target = $("td:eq(4)", parent);
            var flag = $this.hasClass("curbtn") ? 0 : 2;
            $.ajax({
                url: "/score1/updateQMStatus.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: {
                    scoreId: target.attr("sid"),
                    subjectId: target.attr("suid"),
                    showType: flag
                },
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        $this.toggleClass("curbtn");
                        parent.find(".que").removeClass("curbtn");
                        if (flag == 2) {
                            target.html('<em class="disbtn">免</em>');
                        } else {
                            target.html("<input style='text-align:center;border:0px;width:60px;background:#FFF'" +
                                " type='text' name='subjectScore' value='" + 0 + "'>");
                        }
                    }
                }
            });
        });

        SCORE_LIST.$kaochangLuruDatalist.delegate("em.que", "click", function () {
            var $this = $(this);
            var parent = $this.closest("tr");
            var target = $("td:eq(4)", parent);
            var flag = $this.hasClass("curbtn") ? 0 : 1;
            $.ajax({
                url: "/score1/updateQMStatus.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: {
                    scoreId: target.attr("sid"),
                    subjectId: target.attr("suid"),
                    showType: flag
                },
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        $this.toggleClass("curbtn");
                        parent.find(".mian").removeClass("curbtn");
                        if (flag == 1) {
                            target.html('<em class="disbtn">缺</em>');
                        } else {
                            target.html("<input style='text-align:center;border:0px;width:60px;background:#FFF'" +
                                " type='text' name='subjectScore' value='" + 0 + "'>");
                        }
                    }
                }
            });
        });


        SCORE_LIST.$scoreListContentByClass.delegate("em.mian", "click", function () {
            var $this = $(this);
            var parent = $this.closest("tr");
            var target = $("td:eq(3)", parent);
            var flag = $this.hasClass("curbtn") ? 0 : 2;
            $.ajax({
                url: "/score1/updateQMStatus.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: {
                    scoreId: target.attr("sid"),
                    subjectId: target.attr("suid"),
                    showType: flag
                },
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        $this.toggleClass("curbtn");
                        parent.find(".que").removeClass("curbtn");
                        if (flag == 2) {
                            target.html('<em class="disbtn">免</em>');
                        } else {
                            target.html("<input style='text-align:center;border:0px;width:60px;background:#FFF'" +
                                " type='text' name='subjectScore' value='" + 0 + "'>");
                        }
                    }
                }
            });
        });

        SCORE_LIST.$scoreListContentByClass.delegate("em.que", "click", function () {
            var $this = $(this);
            var parent = $this.closest("tr");
            var target = $("td:eq(3)", parent);
            var flag = $this.hasClass("curbtn") ? 0 : 1;
            $.ajax({
                url: "/score1/updateQMStatus.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: {
                    scoreId: target.attr("sid"),
                    subjectId: target.attr("suid"),
                    showType: flag
                },
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        $this.toggleClass("curbtn");
                        parent.find(".mian").removeClass("curbtn");
                        if (flag == 1) {
                            target.html('<em class="disbtn">缺</em>');
                        } else {
                            target.html("<input style='text-align:center;border:0px;width:60px;background:#FFF'" +
                                " type='text' name='subjectScore' value='" + 0 + "'>");
                        }
                    }
                }
            });
        });
        SCORE_LIST.$kaochangLuruConfirmTeacher.add(SCORE_LIST.$kaochangLuruConfirmAdmin).click(function () {
            SCORE_LIST.loadScoreByExamroom();
        });
        SCORE_LIST.$kaochangLuruRoomlistAdmin.add(SCORE_LIST.$kaochangLuruRoomlistTeacher).change(function () {
            SCORE_LIST.loadScoreByExamroom();
        });
        $("#beginImportByClassBtn").click(function () {
            $.ajaxFileUpload({
                url: '/score1/import.do', //用于文件上传的服务器端请求地址
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'uploadFileByClass', //文件上传域的ID
                dataType: 'json', //返回值类型 一般设置为json
                param: {examId: SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY).id},
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if (data.code != 200) {
                        alert(data.message);
                    } else {
                        alert("考试成绩导入成功");
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    alert("成绩导入失败");
                }
            });
        });
        $("#beginImportByGradeBtn").click(function () {
            $.ajaxFileUpload({
                url: '/score1/import.do', //用于文件上传的服务器端请求地址
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'uploadFileByGrade', //文件上传域的ID
                dataType: 'json', //返回值类型 一般设置为json
                param: {examId: SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY).id},
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if (data.code != 200) {
                        alert(data.message);
                    } else {
                        alert("考试成绩导入成功");
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    alert("成绩导入失败");
                }
            });
        });
        $("#beginImportByExamRoomBtn").click(function () {
            $.ajaxFileUpload({
                url: '/score1/import.do', //用于文件上传的服务器端请求地址
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'uploadFileByExamRoom', //文件上传域的ID
                dataType: 'json', //返回值类型 一般设置为json
                param: {examId: SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY).id},
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if (data.code != 200) {
                        alert(data.message);
                    } else {
                        alert("考试成绩导入成功");
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    alert("成绩导入失败");
                }
            });
        });
        SCORE_LIST.$genTemplateByClass.click(function () {
            kwgl.$commonFormDataContainer.empty();
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            if (!data || !SCORE_LIST.getInputClassForRole()) {
                alert("请选择班级");
                return;
            }
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='classId' value='" + SCORE_LIST.getInputClassForRole() + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.id + "'>");
            kwgl.$commonFormDataContainer.attr("action", "/score1/tempByClass.do");
            kwgl.$commonFormDataContainer.submit();
        });
        $("#genTemplateByGrade").click(function () {
            kwgl.$commonFormDataContainer.empty();
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            if (!data) {
                alert("找不到考试");
                return;
            }
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.id + "'>");
            kwgl.$commonFormDataContainer.attr("action", "/score1/tempByGrade.do");
            kwgl.$commonFormDataContainer.submit();
        });
        $("#genTemplateByExamRoom").click(function () {
            kwgl.$commonFormDataContainer.empty();
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            if (!data || !SCORE_LIST.getInputRoomForRole()) {
                alert("请选择考场");
                return;
            }
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='roomId' value='" + SCORE_LIST.getInputRoomForRole() + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.id + "'>");
            kwgl.$commonFormDataContainer.attr("action", "/score1/tempByExamRoom.do");
            kwgl.$commonFormDataContainer.submit();
        });
        SCORE_LIST.$kaochangLuruImport.click(function () {
            $("#uploadFileByExamRoom").prop("value", "");
            Common.dialog($('#daoruByExamId'));
        });
        $("#importScoreByClass").click(function () {
            $("#uploadFileByClass").prop("value", "");
            Common.dialog($('#daoruByClass'));
        });
        SCORE_LIST.$scoreViewSubjectContainer.delegate(":checkbox", "click", function () {
            SCORE_LIST.$ScoreViewListTitle.empty();
            SCORE_LIST.$ScoreViewListContent.empty();
            SCORE_LIST.viewScoreData();
        });
        SCORE_LIST.$scoreViewClass.delegate(":checkbox", "click", function () {
            SCORE_LIST.$ScoreViewListTitle.empty();
            SCORE_LIST.$ScoreViewListContent.empty();
            SCORE_LIST.viewScoreData();
        });
        SCORE_LIST.$chakanScoreOrderBy.change(function () {
            SCORE_LIST.$ScoreViewListTitle.empty();
            SCORE_LIST.$ScoreViewListContent.empty();
            SCORE_LIST.viewScoreData();
        });
        SCORE_LIST.$filterSubjectInClassBtnAdmin.add(SCORE_LIST.$filterSubjectInClassBtnTeacher).click(function () {
            SCORE_LIST.editScoreData();
        });
        SCORE_LIST.$scoreGradeList.change(function () {
            SCORE_LIST.loadList();
        });
        SCORE_LIST.$scoreListQueryBtn.click(function () {
            SCORE_LIST.loadList();
        });
        SCORE_LIST.$viewScoreExport.click(function () {
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            kwgl.$commonFormDataContainer.empty();
            var selSubject = SCORE_LIST.getViewSelectSubject();
            var selClass = SCORE_LIST.getViewSelectClass();
            if (selSubject.length == 0 || selClass.length == 0) {
                return;
            }
            for (var i = 0, len = selClass.length; i < len; i++) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='classId' value='" + selClass[i] + "'>");
            }
            for (var i = 0, len = selSubject.length; i < len; i++) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='subject' value='" + selSubject[i] + "'>");
            }
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.id + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='gradeId' value='" + data.gradeId + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='order' value='" + SCORE_LIST.$chakanScoreOrderBy.val() + "'>");
            kwgl.$commonFormDataContainer.attr("action", "/score1/exportByClass.do");
            kwgl.$commonFormDataContainer.submit();
        });

        $('.cj-lvru .green-btn').on('click', function () {
            $("[name=openStatus][value=0]", EXAM_SETTING.$piliangsetIdForm).trigger("click");
            Common.dialog($('#piliangsetId'), function (obj) {
                $(obj).find('.pop-btn span.active').off().on('click', function () {
                    if ($("[name=openStatus]:checked", EXAM_SETTING.$piliangsetIdForm).val() == 1
                        && !EXAM_SETTING.checkSelectDate(EXAM_SETTING.$piliangsetIdFormBegin.val(), EXAM_SETTING.$piliangsetIdFormEnd.val())) {
                        return;
                    }
                    $.ajax({
                        url: "/exam1/updateOpenStatus.do?_=" + new Date(),
                        type: "post",
                        datatype: "json",
                        data: EXAM_SETTING.$piliangsetIdForm.serialize(),
                        success: function (data) {
                            if (!data) {
                                return false;
                            }
                            if (data.code == "200") {
                                var allSubject = EXAM_SETTING.$examSubjectList.children("tr");
                                var data;
                                var target;
                                for (var i = 0, len = allSubject.length; i < len; i++) {
                                    target = allSubject.eq(i);
                                    data = target.data(EXAM_SETTING.$CACHE_DATA_KEY);
                                    data.openBeginTime = EXAM_SETTING.$piliangsetIdFormBegin.val();
                                    data.openEndTime = EXAM_SETTING.$piliangsetIdFormEnd.val();
                                    data.openStatus = $("[name=openStatus]:checked", EXAM_SETTING.$piliangsetIdForm).val();
                                    $("td:eq(1)", target).text(data.openStatus == 0 ? "关闭" : "开放");
                                    $("td:eq(2)", target).text(data.openBeginTime ? data.openBeginTime : "");
                                    $("td:eq(3)", target).text(data.openEndTime ? data.openEndTime : "");
                                    target.data(EXAM_SETTING.$CACHE_DATA_KEY, data);
                                }
                                SCORE_LIST.loadList();
                                Common.dialog.close($(obj));
                            } else {
                                //alert(data.message?data.message:"录入授权设置错误");
                            }
                        }
                    });
                })
            });
        });

        SCORE_LIST.$ScoreView.delegate('.qk-set', 'click', function () {
            var data = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            EXAM_SETTING.$qmkaoTitle.html(data.name + " / 缺免考设置");
            EXAM_SETTING.$qmkaoTboday.empty();
            SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY, data);
            $.ajax({
                url: "/score1/getClass.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: {examId: data.id, gradeId: data.gradeId},
                success: function (data) {
                    $('.class-table').hide();
                    $('.exam-item').hide();
                    $('.qmkao-view').show();
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        var data = data.message;
                        var clazz = data.clazz;
                        if (clazz && clazz.length == 0) {
                            return;
                        }
                        if (data.subject && data.subject.length == 0) {
                            return;
                        }
                        EXAM_SETTING.$qmkaoClass.empty();
                        EXAM_SETTING.$qmkaoClass.append('<option value="ALL">全部</option>');
                        for (var i = 0, len = clazz.length; i < len; i++) {
                            EXAM_SETTING.$qmkaoClass.append('<option value="' + clazz[i].idStr + '">' + clazz[i].value + '</option>');
                        }
                        EXAM_SETTING.$qmkaoSubject.empty();
                        for (var i = 0, len = data.subject.length; i < len; i++) {
                            EXAM_SETTING.$qmkaoSubject.append('<option value="' + data.subject[i].subjectId + '">'
                                + data.subject[i].subjectName + '</option>');
                        }
                        EXAM_SETTING.loadQmList();
                    }
                }
            });
        });
        SCORE_LIST.$scoreInputClassAdmin.change(function () {
            SCORE_LIST.$filterSubjectInClassBtnAdmin.trigger("click");
        });
        SCORE_LIST.$scoreListContentByClass.add(SCORE_LIST.$kaochangLuruDatalist).delegate("input[name=subjectScore]", "change", function () {
        	var targetDom = this;
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            var parent = $(this).parent();
            if($(targetDom).val() == ""){
            	$(targetDom).val("");
            	targetDom.focus();
        		alert("成绩不可为空，请输入成绩！");
        	}
            $.ajax({
                url: "/score1/save.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: {
                    examId: data.id,
                    scoreId: parent.attr("sid"),
                    subjectId: parent.attr("suid"),
                    examScore: this.value
                },
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code != "200") {
                    	$(targetDom).val("");
                    	targetDom.focus();
                        alert(data.message);
                    }
                }
            });
        });
        SCORE_LIST.$ScoreView.delegate('.bj-set', 'click', function () {
            var data = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            SCORE_LIST.$inputByClassTitle.html(data.name + " / 成绩录入");
            SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY, data);
            $.ajax({
                url: "/score1/getClass.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: {examId: data.id, gradeId: data.gradeId},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        SCORE_LIST.$scoreListTitleByClass.empty();
                        SCORE_LIST.$scoreListContentByClass.empty();
                        var data = data.message;
                        if (data.clazz && data.clazz.length == 0) {
                            alert("本次考试没有与您相关的班级");
                            return;
                        }
                        if (data.subject && data.subject.length == 0) {
                            alert("本次考试没有与您相关的课程");
                            return;
                        }
                        SCORE_LIST.appendClassForRole(data.clazz);
                        SCORE_LIST.appendInputByClassSubjectForRole(data.subject);
                        $('.class-luru').show().siblings().hide();
                    }
                }
            });
        });

        SCORE_LIST.$ScoreView.delegate('.nj-set', 'click', function () {
            var data = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY, data);
            $("#uploadFileByGrade").prop("value", "");
            Common.dialog($('#daoruByGrade'));
        });

        SCORE_LIST.$ScoreView.delegate('.kc-set', 'click', function () {
            var data = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            SCORE_LIST.$kaochangLuruTitle.html(data.name + " / 成绩录入");
            SCORE_LIST.$kaochangLuruDatalist.empty();
            SCORE_LIST.$kaochangLuruTitles.empty();
            SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY, data);
            $.ajax({
                url: "/score1/inputByExamRoom.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: {examId: data.id},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        var data = data.message;
                        if (data.room && data.room.length == 0) {
                            alert("本次考试没有分配考场");
                            return;
                        }
                        if (data.subject && data.subject.length == 0) {
                            alert("本次考试没有与您相关的课程");
                            return;
                        }
                        //考场列表
                        SCORE_LIST.appendRoomForRole(data.room);
                        //科目列表
                        SCORE_LIST.appendInputByRoomSubjectForRole(data.subject);
                        $('.kaochang-luru').show().siblings().hide();
                    }
                }
            });
        });

        SCORE_LIST.$ScoreView.delegate('.chakan-view', 'click', function () {
            SCORE_LIST.$ScoreViewListTitle.empty();
            SCORE_LIST.$ScoreViewListContent.empty();
            var data = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            SCORE_LIST.$scoreViewTitle.html(data.name + " / 成绩查看");
            SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY, data);
            $.ajax({
                url: "/score1/getClass.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: {examId: data.id, gradeId: data.gradeId},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        var data = data.message;
                        var clazz = data.clazz;
                        SCORE_LIST.$scoreViewClass.empty();

                        if (clazz && clazz.length == 0) {
                            alert("本次考试没有与您相关的班级");
                            return;
                        }
                        if (data.subject && data.subject.length == 0) {
                            alert("本次考试没有与您相关的课程");
                            return;
                        }
                        $('.chakan-luru').show().siblings().hide();
                        for (var i = 0, len = clazz.length; i < len; i++) {
                            SCORE_LIST.$scoreViewClass.append('<label><input value="' + clazz[i].idStr + '" type="checkbox"/>' + clazz[i].value + '</label>');
                        }
                        SCORE_LIST.$scoreViewSubjectContainer.empty();
                        for (var i = 0, len = data.subject.length; i < len; i++) {
                            SCORE_LIST.$scoreViewSubjectContainer.append('<label><input type="checkbox" value="' + data.subject[i].subjectId
                                + '"/>' + data.subject[i].subjectName + '</label>');
                        }
                    } else {
                        alert(data.message);
                    }
                }
            });
        });
        $("#exportByClassAtInput").click(function () {
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            kwgl.$commonFormDataContainer.empty();
            var subjects = SCORE_LIST.$inputByClassSelectedSubject.length == 0 ?
                SCORE_LIST.getInputSelectSubject() : SCORE_LIST.$inputByClassSelectedSubject;
            if (subjects.length == 0) {
                return;
            }
            for (var i = 0, len = subjects.length; i < len; i++) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='subject' value='" + subjects[i] + "'>");
            }
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='classId' value='" + SCORE_LIST.getInputClassForRole() + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='gradeId' value='" + data.gradeId + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.id + "'>");
            kwgl.$commonFormDataContainer.attr("action", "/score1/exportByClass.do");
            kwgl.$commonFormDataContainer.submit();
        });
        $("#kaochang-luru-export").click(function () {
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            kwgl.$commonFormDataContainer.empty();
            var subjects = SCORE_LIST.$inputByExamRoomSelectedSubject.length == 0 ?
                SCORE_LIST.getInputByExamRoomSelectSubject() : SCORE_LIST.$inputByExamRoomSelectedSubject;
            if (subjects.length == 0) {
                return;
            }
            for (var i = 0, len = subjects.length; i < len; i++) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='subject' value='" + subjects[i] + "'>");
            }
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='roomId' value='" + SCORE_LIST.getInputRoomForRole() + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.id + "'>");
            kwgl.$commonFormDataContainer.attr("action", "/score1/exportByRoom.do");
            kwgl.$commonFormDataContainer.submit();
        });
    }

    //等级设置部分
    var LEVEL = {
        queryAllLevels: function () {
            $.ajax({
                url: "/level/queryLevel.do",
                type: "post",
                datatype: "json",
                data: {},
                success: function (data) {
                    var $levelList = $("#levelList");
                    data = eval("(" + data + ")");
                    $levelList.empty();
                    var htmlStr = "";
                    for (var i = 0; i < data.levels.length; i++) {
                        var levelObj = data.levels[i];
                        htmlStr = htmlStr + "<tr id='" + levelObj.id + "'><td style='width:40px'>" + (i + 1) + "</td><td><input class='inputOfTable' value='" + levelObj.levelName + "'/></td>" +
                            "<td><input class='inputOfTable' value='" + levelObj.scoreRange + "'/><span>%<span></td><td><a href='#' class='icon-del'></a></td></tr>";
                    }
                    $levelList.html(htmlStr);
                }
            });
        }
    };
    LEVEL.init = function () {
        LEVEL.queryAllLevels();
        $("#levelList").delegate('.inputOfTable', 'change', function () {
            var ln = $(this).parent().parent().find("input").get(0).value;
            var sr = $(this).parent().parent().find("input").get(1).value;
            $.ajax({
                url: "/level/updateLevel.do",
                type: "post",
                datatype: "json",
                data: {levelId: $(this).parent().parent().attr("id"), levelName: ln, scoreRange: sr},
                success: function () {
                    //LEVEL.queryAllLevels();
                }
            });
        });
        $("#levelList").delegate('.icon-del', 'click', function () {
            if (confirm("是否确认删除？")) {
                $.ajax({
                    url: "/level/deleteLevel.do",
                    type: "post",
                    datatype: "json",
                    data: {levelId: $(this).parent().parent().attr("id")},
                    success: function () {
                        LEVEL.queryAllLevels();
                    }
                });
            }
        });
        $("#dengji-view").delegate(".green-btn", "click", function () {
            $.ajax({
                url: "/level/addLevel.do",
                type: "post",
                datatype: "json",
                data: {},
                success: function () {
                    LEVEL.queryAllLevels();

                }
            });
        });
    };

    //考场资源部分
    var EXAM_ROOM = {
        queryAllExamRooms: function () {
            $.ajax({
                url: "/examRoom/queryExamRoom.do",
                type: "post",
                datatype: "json",
                data: {},
                success: function (data) {
                    var $examRoomList = $("#examRoomList");
                    data = eval("(" + data + ")");
                    $examRoomList.empty();
                    var htmlStr = "";
                    for (var i = 0; i < data.examRooms.length; i++) {
                        var examRoomObj = data.examRooms[i];
                        htmlStr = htmlStr + "<tr id='" + examRoomObj.id + "'><td style='width:40px'>" + (i + 1) + "</td><td><input class='inputOfTable' value='" + examRoomObj.examRoomNumber + "'/></td>" +
                            "<td><input class='inputOfTable' value='" + examRoomObj.examRoomName + "'/></td><td><input class='inputOfTable' value='" + examRoomObj.examRoomSitNumber + "'/></td>" +
                            "<td><input class='inputOfTable' value='" + examRoomObj.examRoomPostscript + "'/></td><td><a href='#' class='icon-del'></a></td></tr>";
                    }
                    $examRoomList.html(htmlStr);
                }
            });
        }
    };
    EXAM_ROOM.init = function () {
        EXAM_ROOM.queryAllExamRooms();
        $("#examRoomList").delegate('.inputOfTable', 'change', function () {
            var updateExamRoomObj = {};
            updateExamRoomObj.examRoomNumber = $(this).parent().parent().find("input").get(0).value;
            updateExamRoomObj.examRoomName = $(this).parent().parent().find("input").get(1).value;
            updateExamRoomObj.examRoomSitNumber = $(this).parent().parent().find("input").get(2).value;
            updateExamRoomObj.examRoomPostscript = $(this).parent().parent().find("input").get(3).value;
            updateExamRoomObj.examRoomId = $(this).parent().parent().attr("id");
            $.ajax({
                url: "/examRoom/updateExamRoom.do",
                type: "post",
                datatype: "json",
                data: updateExamRoomObj,
                success: function (data) {
                    //LEVEL.queryAllLevels();
                }
            });
        });
        $("#examRoomList").delegate('.icon-del', 'click', function () {
            if (confirm("删除考场会对使用该考场的考试造成影响，需要重新安排考场，是否确认删除？")) {
                $.ajax({
                    url: "/examRoom/deleteExamRoom.do",
                    type: "post",
                    datatype: "json",
                    data: {examRoomId: $(this).parent().parent().attr("id")},
                    success: function () {
                        EXAM_ROOM.queryAllExamRooms();
                    }
                });
            }
        });
        $("#kaochang-view").delegate(".green-btn", "click", function () {
            $.ajax({
                url: "/examRoom/addExamRoom.do",
                type: "post",
                datatype: "json",
                data: {},
                success: function () {
                    EXAM_ROOM.queryAllExamRooms();
                }
            });
        });
    };

    //设置
    var EXAM_SETTING = {
        $scoreShouquanTitle: $("#score-shouquan-setting"),
        $back: $("#exam-setting-luru-back"),
        $examSubjectList: $("#score-input-openlist"),
        $scoreOpenTimeTable: $("#scoreOpenTimeTable"),
        $lurusetIdTitle: $("#lurusetId-title"),
        $CACHE_DATA_KEY: "cache_data",
        $lurusetIdForm: $("#lurusetIdForm"),
        $lurusetIdFormExamId: $("#lurusetIdForm-examId"),
        $lurusetIdFormSetTarget: null,
        $lurusetIdFormId: $("#lurusetIdForm-Id"),
        $lurusetIdFormBegin: $("#lurusetIdForm-begin"),
        $lurusetIdFormEnd: $("#lurusetIdForm-end"),
        $piliangsetIdForm: $("#piliangsetIdForm"),
        $piliangsetIdFormBegin: $("#piliangsetIdForm-begin"),
        $piliangsetIdFormEnd: $("#piliangsetIdForm-end"),
        $piliangsetIdFormExamId: $("#piliangsetIdForm-examId"),
        $examSubjcetListTmep: $('<tr><td></td><td></td><td></td><td></td><td><span class="kaifang-set">设置</span></td></tr>'),
        $qmkaoTitle: $("#qmkao-title"),
        $qmkaoSubject: $("#qmkao-subject"),
        $qmkaoClass: $("#qmkao-class"),
        $qmkaoShowtype: $("#qmkao-showtype"),
        $qmkaoConfirm: $("#qmkao-confirm"),
        $qmkaoTboday: $("#qmkao-tboday"),
        $pagination: $("#qmkao-pagination"),
        $qmkaoListTmp: $('<tr><td></td><td></td><td><em class="disbtn que">缺</em></td><td><em class="disbtn mian">免</em></td></tr>'),
        loadQmList: function (page) {
            EXAM_SETTING.$qmkaoTboday.empty();
            EXAM_SETTING.$pagination.empty();
            var subject = EXAM_SETTING.$qmkaoSubject.val();
            if (subject) {
                $.ajax({
                    url: "/score1/qmstatus.do?_=" + new Date(),
                    type: "get",
                    datatype: "json",
                    data: {
                        examId: SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY).id,
                        classId: EXAM_SETTING.$qmkaoClass.val(),
                        subject: EXAM_SETTING.$qmkaoSubject.val(),
                        showType: EXAM_SETTING.$qmkaoShowtype.val(),
                        page: page || 1
                    },
                    success: function (data) {
                        if (!data) {
                            return false;
                        }
                        if (data.code == "200") {
                            var scoreList = data.message.list;
                            var scoreInfo;
                            var temp;
                            for (var i = 0, len = scoreList.length; i < len; i++) {
                                scoreInfo = scoreList[i];
                                temp = EXAM_SETTING.$qmkaoListTmp.clone();
                                temp.data(EXAM_SETTING.$CACHE_DATA_KEY, scoreInfo);
                                $("td:eq(0)", temp).text(scoreInfo.studentName);
                                $("td:eq(1)", temp).text(scoreInfo.className);
                                if (scoreInfo.examScore[EXAM_SETTING.$qmkaoSubject.val()].showType == 1) {
                                    $("td:eq(2)>em", temp).addClass("curbtn");
                                }
                                if (scoreInfo.examScore[EXAM_SETTING.$qmkaoSubject.val()].showType == 2) {
                                    $("td:eq(3)>em", temp).addClass("curbtn");
                                }
                                EXAM_SETTING.$qmkaoTboday.append(temp);
                            }
                            if (data.message.count > 0) {
                                EXAM_SETTING.$pagination.data("trigger", false);
                                EXAM_SETTING.$pagination.jqPaginator({
                                    totalPages: data.message.count,//总页数
                                    visiblePages: 10,//分多少页
                                    currentPage: page > data.message.count ? data.message.count : page,//当前页数
                                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                                    onPageChange: function (n) {
                                        if (EXAM_SETTING.$pagination.data("trigger")) {
                                            EXAM_SETTING.loadQmList(n);
                                        }
                                    }
                                });
                                EXAM_SETTING.$pagination.data("trigger", true);
                            }
                        } else {
                            alert(data.message);
                        }
                    }
                });
            }
        },
        checkSelectDate: function (begin, end) {
            if (parseInt(begin.replace(/-/g, "")) > parseInt(end.replace(/-/g, ""))) {
                alert("开始时间不能晚于结束时间");
                return false;
            }
            return true;
        }
    };
    EXAM_SETTING.init = function () {
        $("[name=openStatus]", EXAM_SETTING.$lurusetIdForm).click(function () {
            if ($(this).is(":checked") && this.value == 0) {
                EXAM_SETTING.$lurusetIdFormBegin.val("");
                EXAM_SETTING.$lurusetIdFormEnd.val("");
                EXAM_SETTING.$lurusetIdFormBegin.attr("disabled", "disabled");
                EXAM_SETTING.$lurusetIdFormEnd.attr("disabled", "disabled");
            } else {
                EXAM_SETTING.$lurusetIdFormBegin.val(EXAM_MANAGE.$currDate.val());
                EXAM_SETTING.$lurusetIdFormEnd.val(EXAM_MANAGE.$currDate.val());
                EXAM_SETTING.$lurusetIdFormBegin.removeAttr("disabled");
                EXAM_SETTING.$lurusetIdFormEnd.removeAttr("disabled");
            }
        });
        $("[name=openStatus]", EXAM_SETTING.$piliangsetIdForm).click(function () {
            if ($(this).is(":checked") && this.value == 0) {
                EXAM_SETTING.$piliangsetIdFormBegin.val("");
                EXAM_SETTING.$piliangsetIdFormEnd.val("");
                EXAM_SETTING.$piliangsetIdFormBegin.attr("disabled", "disabled");
                EXAM_SETTING.$piliangsetIdFormEnd.attr("disabled", "disabled");
            } else {
                EXAM_SETTING.$piliangsetIdFormBegin.val(EXAM_MANAGE.$currDate.val());
                EXAM_SETTING.$piliangsetIdFormEnd.val(EXAM_MANAGE.$currDate.val());
                EXAM_SETTING.$piliangsetIdFormBegin.removeAttr("disabled");
                EXAM_SETTING.$piliangsetIdFormEnd.removeAttr("disabled");
            }
        });
        EXAM_SETTING.$qmkaoTboday.delegate(".que", "click", function () {
            var $this = $(this);
            var target = $this.closest("tr");
            $.ajax({
                url: "/score1/updateQMStatus.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: {
                    scoreId: target.data(EXAM_SETTING.$CACHE_DATA_KEY).id,
                    subjectId: EXAM_SETTING.$qmkaoSubject.val(),
                    showType: $this.hasClass("curbtn") ? 0 : 1
                },
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        $this.toggleClass("curbtn");
                        $this.closest("tr").find(".mian").removeClass("curbtn");
                    }
                }
            });
        });
        EXAM_SETTING.$qmkaoTboday.delegate(".mian", "click", function () {
            var $this = $(this);
            var target = $this.closest("tr");
            $.ajax({
                url: "/score1/updateQMStatus.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: {
                    scoreId: target.data(EXAM_SETTING.$CACHE_DATA_KEY).id,
                    subjectId: EXAM_SETTING.$qmkaoSubject.val(),
                    showType: $this.hasClass("curbtn") ? 0 : 2
                },
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        $this.toggleClass("curbtn");
                        $this.closest("tr").find(".que").removeClass("curbtn");
                    }
                }
            });
        });
        EXAM_SETTING.$qmkaoClass
            .add(EXAM_SETTING.$qmkaoSubject)
            .add(EXAM_SETTING.$qmkaoShowtype)
            .change(function () {
                EXAM_SETTING.loadQmList();
            });
        EXAM_SETTING.$qmkaoConfirm.click(function () {
            EXAM_SETTING.loadQmList();
        });
        $("#scrore-setting-batch").click(function () {
            EXAM_SETTING.$piliangsetIdFormBegin.val(EXAM_MANAGE.$currDate.val());
            EXAM_SETTING.$piliangsetIdFormEnd.val(EXAM_MANAGE.$currDate.val());
            var data = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            $(":radio[name='openStatus'][value='0']", EXAM_SETTING.$piliangsetIdForm).trigger("click");
            EXAM_SETTING.$piliangsetIdFormExamId.val(data.id);
        });
        kwgl.$pageContent.delegate('.kf-set', 'click', function () {
            var data = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY, data);
            EXAM_SETTING.KF_SETTING_TARGET = $(this).closest("tr");
            EXAM_SETTING.$scoreShouquanTitle.html(data.name + " / 录入授权设置");
            var subjects = data.examSubjectDTO;
            EXAM_SETTING.$examSubjectList.empty();
            var temp;
            for (var i = 0, len = subjects.length; i < len; i++) {
                temp = EXAM_SETTING.$examSubjcetListTmep.clone();
                $("td:eq(0)", temp).text(subjects[i].subjectName);
                $("td:eq(1)", temp).text(subjects[i].openStatus == 0 ? "关闭" : "开放");
                $("td:eq(2)", temp).text(subjects[i].openBeginTime ? subjects[i].openBeginTime : "");
                $("td:eq(3)", temp).text(subjects[i].openEndTime ? subjects[i].openEndTime : "");
                EXAM_SETTING.$examSubjectList.append(temp.data(EXAM_SETTING.$CACHE_DATA_KEY, subjects[i]));
            }
        });
        EXAM_SETTING.$scoreOpenTimeTable.delegate('.kaifang-set', 'click', function () {
            Common.dialog($('#lurusetId'), function (obj) {
                $(obj).find('.pop-btn span.active').off().on('click', function () {
                    if ($("[name=openStatus]:checked", EXAM_SETTING.$lurusetIdForm).val() == 1
                        && !EXAM_SETTING.checkSelectDate(EXAM_SETTING.$lurusetIdFormBegin.val(), EXAM_SETTING.$lurusetIdFormEnd.val())) {
                        return;
                    }
                    $.ajax({
                        url: "/exam1/updateOpenStatus.do?_=" + new Date(),
                        type: "post",
                        datatype: "json",
                        data: EXAM_SETTING.$lurusetIdForm.serialize(),
                        success: function (data) {
                            if (!data) {
                                return false;
                            }
                            if (data.code == "200") {
                                var data = EXAM_SETTING.$lurusetIdFormSetTarget.data(EXAM_SETTING.$CACHE_DATA_KEY);
                                data.openBeginTime = EXAM_SETTING.$lurusetIdFormBegin.val();
                                data.openEndTime = EXAM_SETTING.$lurusetIdFormEnd.val();
                                data.openStatus = $("[name=openStatus]:checked", EXAM_SETTING.$lurusetIdForm).val();
                                EXAM_SETTING.$lurusetIdFormSetTarget.data(EXAM_SETTING.$CACHE_DATA_KEY, data);
                                $("td:eq(1)", EXAM_SETTING.$lurusetIdFormSetTarget).text(data.openStatus == 1 ? "开启" : "关闭");
                                $("td:eq(2)", EXAM_SETTING.$lurusetIdFormSetTarget).text(data.openBeginTime ? data.openBeginTime : "");
                                $("td:eq(3)", EXAM_SETTING.$lurusetIdFormSetTarget).text(data.openEndTime ? data.openEndTime : "");
                                SCORE_LIST.loadList();
                                Common.dialog.close($(obj));
                            } else {
                                //alert(data.message?data.message:"录入授权设置错误");
                            }
                        }
                    });
                })
            });
            var examInfo = SCORE_LIST.$ScoreView.data(kwgl.PAGE_CURR_CACHE_KEY);
            var data = $(this).closest("tr").data(EXAM_SETTING.$CACHE_DATA_KEY);
            EXAM_SETTING.$lurusetIdFormSetTarget = $(this).closest("tr");
            EXAM_SETTING.$lurusetIdTitle.html(data.subjectName + " — 录入授权设置");
            EXAM_SETTING.$lurusetIdFormExamId.val(examInfo.id);
            EXAM_SETTING.$lurusetIdFormId.val(data.id);
            $("[name='openStatus'][value=" + data.openStatus + "]", EXAM_SETTING.$lurusetIdForm).prop("checked", true);
            $("[name='openStatus'][value=" + data.openStatus + "]", EXAM_SETTING.$lurusetIdForm).trigger("click");
            if (data.openStatus == 1) {
                //如果是开放状态，修改开放时间
                EXAM_SETTING.$lurusetIdFormBegin.val(data.openBeginTime ? data.openBeginTime : EXAM_MANAGE.$currDate.val());
                EXAM_SETTING.$lurusetIdFormEnd.val(data.openEndTime ? data.openEndTime : EXAM_MANAGE.$currDate.val());
            }
        });
        EXAM_SETTING.$back.click(function () {
            SCORE_LIST.loadList();
        });
    };


    //考试安排
    var ARRANGE_EXAM = {
        $paikaochangTitle: $("#paikaochang-title"),
        $paikaochangDetailGrade: $("#paikaochang-arranged-detail-grade"),
        $paikaochangDetailSumstu: $("#paikaochang-arranged-detail-sumstu"),
        $paikaochangDetailArrstu: $("#paikaochang-arranged-detail-arrangedstu"),
        $paikaochangDetailUnArrstu: $("#paikaochang-arranged-detail-unarrstu"),
        $paikaochangDetailExamCap: $("#paikaochang-arranged-detail-examcap"),
        $paikaochangDetailStatus: $("#paikaochang-arranged-status"),
        $paikaochangArrangedDetailExamname: $("#paikaochang-arranged-detail-examname"),
        $paikaochangArrangedExamroomlist: $("#paikaochang-arranged-examroomlist"),
        $paikaochangArrangedSelectedexamroom: $("#paikaochang-arranged-selectedexamroom"),
        $paikaochangSelectedexamroomTemp: $('<tr><td></td><td></td><td></td><td></td><td><a href="javascript:void(0)" class="icon-del"></a></td></tr>'),
        $paikaochangExamRoomListTemp: $('<tr><td></td><td></td><td></td><td><em></em></td></tr>'),
        $kaoshengViewContainer: $("#kaosheng-view-container"),
        $examroomSelect: $("#examroom-select"),
        $classSelect: $("#examroom-class"),
        $examroomSelectField: $("#examroom-select-field"),
        $classSelectField: $("#examroom-class-field"),
        $kaoshengViewConfirm: $("#kaosheng-view-confirm"),
        $arrangeExamTBODY: $("#arrange-exam-tbody"),
        $kaoshengViewTitle: $("#kaosheng-view-title"),
        $kaoshengViewExport: $("#kaosheng-view-export"),
        $kaoshengViewExportSeat: $("#kaosheng-view-export-seat"),
        $kaoshengViewArrangedTitle: $("#kaosheng-view-arranged-title"),
        $kaoshengViewArrangedContent: $("#kaosheng-view-arranged-list"),
        $pagination: $("#arrange-exam-pagination"),
        $arrangeExamGrade: $("#arrange-exam-grade"),
        $arrangeExamTemp: $('<tr><td></td><td></td><td></td><td>未完成</td><td><span class="lock-arrange">未锁定</span></td>' +
            '<td><span class="arrange-examroom" style="margin-right: 10px;"></span><a href="javascript:void(0)" class="kaosheng-view">查看</a></td></tr>'),
        loadExam: function (page) {
            ARRANGE_EXAM.$arrangeExamTBODY.empty();
            ARRANGE_EXAM.$pagination.empty();
            var grade = ARRANGE_EXAM.$arrangeExamGrade.val();
            if (grade) {
                $.ajax({
                    url: "/exam1/loadExam.do?_=" + new Date(),
                    type: "get",
                    datatype: "json",
                    data: {gradeId: grade, page: page || 1},
                    success: function (data) {
                        if (!data) {
                            return false;
                        }
                        if (data.code == "200") {
                            var examList = data.message.list;
                            var examInfo;
                            var temp;
                            for (var i = 0, len = examList.length; i < len; i++) {
                                examInfo = examList[i];
                                temp = ARRANGE_EXAM.$arrangeExamTemp.clone();
                                temp.data(kwgl.PAGE_CACHE_KEY, examInfo);
                                $("td:eq(0)", temp).text(i + 1);
                                $("td:eq(1)", temp).text(examInfo.name);
                                $("td:eq(2)", temp).text(examInfo.date);
                                $("td:eq(3)", temp).html(examInfo.isArrangeComplate == 1 ? "已完成" : "未完成");
                                if(!kwgl.onlyTeacher){
                                    $("td:eq(4) span.lock-arrange", temp).html(examInfo.isLock == 1 ? "<a href='javascript:void(0)'>已锁定</a>" : "<a href='javascript:void(0)'>未锁定</a>");
                                    $("td:eq(5) span.arrange-examroom", temp).html(examInfo.isLock == 1 ? "排考场" : '<a class="arrange-examroom-trigger" href="javascript:void(0)">排考场</a>');
                                }
                                ARRANGE_EXAM.$arrangeExamTBODY.append(temp);
                            }
                            if (data.message.count > 0) {
                                ARRANGE_EXAM.$pagination.data("trigger", false);
                                ARRANGE_EXAM.$pagination.jqPaginator({
                                    totalPages: data.message.count,//总页数
                                    visiblePages: 10,//分多少页
                                    currentPage: page > data.message.count ? data.message.count : page,//当前页数
                                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                                    onPageChange: function (n) {
                                        if (ARRANGE_EXAM.$pagination.data("trigger")) {
                                            ARRANGE_EXAM.loadExam(n);
                                        }
                                    }
                                });
                                ARRANGE_EXAM.$pagination.data("trigger", true);
                            }
                        } else {
                            alert(data.message);
                        }
                    }
                });
            }
        },
        loadArrangedDetail: function (data, type) {
            var subs = data[0].zoubanSubject;
            var html = '';
            var html1 = '';
            for(var i=0; i<subs.length; i++){
                html +=  '<td><div class="div100">'+subs[i].name+'</div></td>';
                html1 +=  '<th width="100"><div class="div100">'+subs[i].name+'</div></th>';
            }

            $('#kaosheng-view-arranged-title-tr').empty();
            if(type == 'class'){
                $('#kaosheng-view-arranged-title-tr').append('<th width="120"><div class="div120">班级</div></th><th width="120"><div class="div120">考场名称</div></th><th width="120"><div class="div120">考场编号</div></th><th width="120"><div class="div120">考号</div></th><th width="100"><div class="div100">考生姓名</div></th>');
            } else {
                $('#kaosheng-view-arranged-title-tr').append('<th width="120"><div class="div120">考场名称</div></th><th width="120"><div class="div120">考场编号</div></th><th width="120"><div class="div120">班级</div></th><th width="120"><div class="div120">考号</div></th><th width="100"><div class="div100">考生姓名</div></th>');
            }
            $('#kaosheng-view-arranged-title-tr').append(html1);

            var tmp = type == "class" ? $("<tr><td class='classname'></td><td class='roomname'></td><td class='roomcode'></td><td></td><td></td>"+html+"</tr>") :
                $("<tr><td class='roomname'></td><td class='roomcode'></td><td class='classname'></td><td></td><td></td>"+html+"</tr>");
            //加载考生排考信息
            var clone;
            for (var i = 0; i < data.length; i++) {
                clone = tmp.clone();
                $(".classname", clone).text(data[i].className);
                $(".roomname", clone).text(data[i].examRoomName);
                $(".roomcode", clone).text(data[i].examRoomNumber);
                $("td:eq(3)", clone).text(data[i].examNumber);
                $("td:eq(4)", clone).text(data[i].studentName);
                var zbsubs = data[i].zoubanSubject;
                for(var k=0; k<zbsubs.length; k++){
                    var num = k - 0 + 5;
                    var type = '';
                    if(zbsubs[k].ty == 1){
                        type='等级考';
                        $("td:eq("+num+")", clone).text(type).attr("style", "background: #ff8a00");
                    } else if(zbsubs[k].ty == 2){
                        type='合格考';
                        $("td:eq("+num+")", clone).text(type);
                    } else {
                        $("td:eq("+num+")", clone).text(type);
                    }
                }
                ARRANGE_EXAM.$kaoshengViewArrangedContent.append(clone);
            }
        }
    };
    ARRANGE_EXAM.init = function () { 
    	//绑定确认按钮重新读取列表
    	$("#reoladByGradeBtn").click(function(){ARRANGE_EXAM.loadExam(1)});
        $(".paikaochang-arranged-detail-begin").click(function () {
            var selectedExamroom = $("tr", ARRANGE_EXAM.$paikaochangArrangedSelectedexamroom);
            if (selectedExamroom.length == 0) {
                alert("请选择启用的考场");
                return;
            }
            //校验容量和学生总人数
            var countStu = parseInt(ARRANGE_EXAM.$paikaochangDetailSumstu.text());
            var cap = parseInt(ARRANGE_EXAM.$paikaochangDetailExamCap.text());
            if (countStu > cap) {
                alert("启用考场的总容量无法完成所有考生的编排");
                return;
            }
            kwgl.$commonFormDataContainer.empty();
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='"
                + ARRANGE_EXAM.$arrangeExamTBODY.data(kwgl.PAGE_CURR_CACHE_KEY).data(kwgl.PAGE_CACHE_KEY).id + "'>");
            kwgl.$commonFormDataContainer.append("<input type='hidden' name='type' value='"
            + $(this).attr('ty') + "'>");
            for (var i = 0, len = selectedExamroom.length; i < len; i++) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='examroom' value='"
                    + selectedExamroom.eq(i).data(kwgl.PAGE_CACHE_KEY).id + "'>");
            }
            
            //新增等待框
            $("#waitWindowInfo").html("正在排考场，请等待...");
            Common.dialog($('#waitWindow'));
            $.ajax({
                url: "/exam1/arrange.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: kwgl.$commonFormDataContainer.serialize(),
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        var arrangedResult = data.message;
                        var room, count = 0;
                        //更新考生编排信息
                        for (var i = 0, len = selectedExamroom.length; i < len; i++) {
                            room = selectedExamroom.eq(i).data(kwgl.PAGE_CACHE_KEY);
                            if (arrangedResult[room.id] && room.examRoomSitNumber == arrangedResult[room.id]) {
                                $("td:eq(3)", selectedExamroom.eq(i)).text("已满");
                                count += arrangedResult[room.id];
                            } else {
                                $("td:eq(3)", selectedExamroom.eq(i)).text("已排" + arrangedResult[room.id] + "人");
                                count += arrangedResult[room.id];
                            }
                        }
                        //考生总数
                        var stuCount = parseInt(ARRANGE_EXAM.$paikaochangDetailSumstu.text());
                        //已排考场人数
                        ARRANGE_EXAM.$paikaochangDetailArrstu.text(count);
                        //未排考场人数
                        ARRANGE_EXAM.$paikaochangDetailUnArrstu.text(stuCount - count);
                        //排考场状态
                        ARRANGE_EXAM.$paikaochangDetailStatus.text(stuCount - count <= 0 ? "已完成" : "未完成");

                        //更新列表状态
                        $("td:eq(3)", ARRANGE_EXAM.$arrangeExamTBODY.data(kwgl.PAGE_CURR_CACHE_KEY)).html(stuCount - count <= 0 ? "已完成" : "未完成"); 
                        //安排完毕提示框 
                        $("#overWindowH").html("考场安排完毕！");
                        Common.dialog($('#overWindow')); 
                    } else {
                    	$("#overWindowH").html(data.message);
                        Common.dialog($('#overWindow'));
//                        alert(data.message);
                    }
                }
            });
        });
        $("#paikaochang-arranged-detail-viewbtn").click(function () {
        	if($("#paikaochang-arranged-status").html() == "未完成"){
        		alert("还未安排考场，无法查看！"); 
        		return;
        	}
            $('#kaosheng-view-arranged-title-tr').empty();
            $('#kaosheng-view-arranged-title-tr').append('<th width="120">班级</th><th>考场名称</th><th width="120">考场编号</th><th width="120">考号</th><th width="100">考生姓名</th>');

            //获取排考场时缓存的数据
            var target = ARRANGE_EXAM.$arrangeExamTBODY.data(kwgl.PAGE_CURR_CACHE_KEY);

            //设置查看考生安排时需要的数据
            ARRANGE_EXAM.$kaoshengViewContainer.data(kwgl.PAGE_CURR_CACHE_KEY, target);

            //考试信息
            var examInfo = target.data(kwgl.PAGE_CACHE_KEY);
            ARRANGE_EXAM.$kaoshengViewTitle.text(examInfo.name + " / 考生安排查看");
            $.ajax({
                url: "/exam1/loadClassAndExamRoom.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: {examId: examInfo.id},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        ARRANGE_EXAM.$classSelectField.empty();
                        ARRANGE_EXAM.$examroomSelectField.empty();
                        var clazz = data.message.class;
                        ARRANGE_EXAM.$classSelectField.append("<option value='ALL'>全部</option>");
                        for (var i = 0, len = clazz.length; i < len; i++) {
                            ARRANGE_EXAM.$classSelectField.append("<option value='" + clazz[i].id + "'>" + clazz[i].className + "</option>");
                        }

                        var room = data.message.room;
                        ARRANGE_EXAM.$examroomSelectField.append("<option value='ALL'>全部</option>");
                        for (var i = 0, len = room.length; i < len; i++) {
                            ARRANGE_EXAM.$examroomSelectField.append("<option value='" + room[i].id + "'>" + room[i].examRoomName + "</option>");
                        }
                        $("[name=apview][value=examroom]", ARRANGE_EXAM.$kaoshengViewContainer).trigger("click");
                        $('.kaosheng-edit').show().siblings().hide();
                    } else {
                        alert(data.message);
                    }
                }
            });
        });
        //移除考试考场
        ARRANGE_EXAM.$paikaochangArrangedSelectedexamroom.delegate(".icon-del", "click", function () {
            var target = $(this).closest("tr");
            //是否已经分配过考场
            if (ARRANGE_EXAM.isArranged && !confirm("移除考场后需要对所有重新编排所有考生，是否继续？")) {
                return;
            }
            ARRANGE_EXAM.isArranged = false;
            var data = target.data(kwgl.PAGE_CACHE_KEY);
            var temp = ARRANGE_EXAM.$paikaochangExamRoomListTemp.clone();
            var tds = $("td", temp);
            tds.eq(0).text(data.examRoomNumber);
            tds.eq(1).text(data.examRoomName);
            tds.eq(2).text(data.examRoomSitNumber);
            //从已选考场删除
            target.remove();
            //将此数据转入未选考场
            ARRANGE_EXAM.$paikaochangArrangedExamroomlist.append(temp.data(kwgl.PAGE_CACHE_KEY, data));
            //设置启用考场的容量
            ARRANGE_EXAM.$paikaochangDetailExamCap.text(parseInt(ARRANGE_EXAM.$paikaochangDetailExamCap.text()) - data.examRoomSitNumber);
            //已排考场人数
            ARRANGE_EXAM.$paikaochangDetailArrstu.text(0);
            //未排考场人数
            ARRANGE_EXAM.$paikaochangDetailUnArrstu.text(ARRANGE_EXAM.$paikaochangDetailSumstu.text());
            //排考场状态
            ARRANGE_EXAM.$paikaochangDetailStatus.text("未完成");
            $("tr", ARRANGE_EXAM.$paikaochangArrangedSelectedexamroom).each(function () {
                $("td:eq(3)", this).text("");
            });
        });
        //启用考场按钮
        ARRANGE_EXAM.$paikaochangArrangedExamroomlist.delegate("em", "click", function () {
            var target = $(this).closest("tr");
            var examrooom = target.data(kwgl.PAGE_CACHE_KEY);
            var temp = ARRANGE_EXAM.$paikaochangSelectedexamroomTemp.clone();
            $("td:eq(0)", temp).text(examrooom.examRoomNumber);
            $("td:eq(1)", temp).text(examrooom.examRoomName);
            $("td:eq(2)", temp).text(examrooom.examRoomSitNumber);
            //从未选考场删除
            target.remove();
            //将数据转入已选考场
            ARRANGE_EXAM.$paikaochangArrangedSelectedexamroom.append(temp.data(kwgl.PAGE_CACHE_KEY, examrooom));

            //增加已选考场的容量
            ARRANGE_EXAM.$paikaochangDetailExamCap.text(parseInt(ARRANGE_EXAM.$paikaochangDetailExamCap.text()) + examrooom.examRoomSitNumber);
        });
        //考试安排中年级切换
        ARRANGE_EXAM.$arrangeExamGrade.change(function () {
            ARRANGE_EXAM.loadExam();
        });

        //导出考试考场安排信息
        ARRANGE_EXAM.$kaoshengViewExport.click(function () {
            var data = ARRANGE_EXAM.$kaoshengViewConfirm.data("query");
            kwgl.$commonFormDataContainer.empty();
            if (data) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.examId + "'>");
                if (data.classId) {
                    kwgl.$commonFormDataContainer.append("<input type='hidden' name='classId' value='" + data.classId + "'>");
                }
                if (data.roomId) {
                    kwgl.$commonFormDataContainer.append("<input type='hidden' name='roomId' value='" + data.roomId + "'>");
                }

                kwgl.$commonFormDataContainer.attr("action", "/exam1/exportArrangeInfo.do");
                kwgl.$commonFormDataContainer.submit();
            }
        });

        //导出考试考场安排信息座次
        ARRANGE_EXAM.$kaoshengViewExportSeat.click(function () {
            var data = ARRANGE_EXAM.$kaoshengViewConfirm.data("query");
            kwgl.$commonFormDataContainer.empty();
            if (data) {
                kwgl.$commonFormDataContainer.append("<input type='hidden' name='examId' value='" + data.examId + "'>");
                if (data.classId) {
                    kwgl.$commonFormDataContainer.append("<input type='hidden' name='classId' value='" + data.classId + "'>");
                }
                if (data.roomId) {
                    kwgl.$commonFormDataContainer.append("<input type='hidden' name='roomId' value='" + data.roomId + "'>");
                }

                kwgl.$commonFormDataContainer.attr("action", "/exam1/exportArrangeInfo1.do");
                kwgl.$commonFormDataContainer.submit();
            }
        });

        //查询条件类型切换
        $(":radio[name=apview]", ARRANGE_EXAM.$kaoshengViewContainer).click(function () {
            ARRANGE_EXAM.$kaoshengViewArrangedContent.empty();
            if (this.value == "class") {
                ARRANGE_EXAM.$classSelect.css({"display": "inline"});
                ARRANGE_EXAM.$examroomSelect.css({"display": "none"});
                ARRANGE_EXAM.$classSelectField.children(":first").prop("selected", "selected");
                $("th:eq(0)", ARRANGE_EXAM.$kaoshengViewArrangedTitle).text("班级");
                $("th:eq(1)", ARRANGE_EXAM.$kaoshengViewArrangedTitle).text("考场名称");
                $("th:eq(2)", ARRANGE_EXAM.$kaoshengViewArrangedTitle).text("考场编号");
            } else {
                ARRANGE_EXAM.$classSelect.css({"display": "none"});
                ARRANGE_EXAM.$examroomSelect.css({"display": "inline"});
                ARRANGE_EXAM.$examroomSelectField.children(":first").prop("selected", "selected");
                $("th:eq(0)", ARRANGE_EXAM.$kaoshengViewArrangedTitle).text("考场名称");
                $("th:eq(1)", ARRANGE_EXAM.$kaoshengViewArrangedTitle).text("考场编号");
                $("th:eq(2)", ARRANGE_EXAM.$kaoshengViewArrangedTitle).text("班级");
            }
        });

        //查询条件变更
        ARRANGE_EXAM.$kaoshengViewConfirm.click(function () {
            var param = {};
            ARRANGE_EXAM.$kaoshengViewArrangedContent.empty();
            var type = $("[name=apview]:checked", ARRANGE_EXAM.$kaoshengViewContainer).val();
            var target = ARRANGE_EXAM.$kaoshengViewContainer.data(kwgl.PAGE_CURR_CACHE_KEY);
            param.examId = target.data(kwgl.PAGE_CACHE_KEY).id;

            if (type == "class") {
                //班级
                param.classId = ARRANGE_EXAM.$classSelectField.val();
            } else {
                //考试编码
                param.roomId = ARRANGE_EXAM.$examroomSelectField.val();
            }

            $(this).data("type", type).data("query", param);
            $.ajax({
                url: "/exam1/arrangeDetail.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: param,
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        ARRANGE_EXAM.loadArrangedDetail(data.message, ARRANGE_EXAM.$kaoshengViewConfirm.data("type"));
                    } else {
                        alert(data.message);
                    }
                }
            });
        });

        //排考场按钮点击
        ARRANGE_EXAM.$arrangeExamTBODY.delegate(".arrange-examroom-trigger", "click", function () {
            var examInfo = $(this).closest("tr").data(kwgl.PAGE_CACHE_KEY);
            //设置标题
            ARRANGE_EXAM.$paikaochangTitle.text(examInfo.gradeName + " / " + examInfo.name + " 排考场");
            //设置年级
            ARRANGE_EXAM.$paikaochangDetailGrade.text(examInfo.gradeName);

            ARRANGE_EXAM.$arrangeExamTBODY.data(kwgl.PAGE_CURR_CACHE_KEY, $(this).closest("tr"));
            $.ajax({
                url: "/exam1/arrangeInfo.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: {examId: examInfo.id},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        var arrangeInfo = data.message;
                        //考生总数
                        ARRANGE_EXAM.$paikaochangDetailSumstu.text(arrangeInfo.countStu);
                        //已排考场人数
                        ARRANGE_EXAM.$paikaochangDetailArrstu.text(arrangeInfo.countStu - arrangeInfo.countUnArr);
                        //未排考场人数
                        ARRANGE_EXAM.$paikaochangDetailUnArrstu.text(arrangeInfo.countUnArr);
                        //排考场状态
                        ARRANGE_EXAM.$paikaochangDetailStatus.text(arrangeInfo.countUnArr == 0 ? "已完成" : "未完成");
                        //考试名称
                        ARRANGE_EXAM.$paikaochangArrangedDetailExamname.text(arrangeInfo.examInfo.name);
                        var schoolExamRoom = arrangeInfo.examroom;
                        var temp, tds, data, usedSeat;
                        var countCap = 0;
                        ARRANGE_EXAM.$paikaochangArrangedSelectedexamroom.empty();
                        ARRANGE_EXAM.$paikaochangArrangedExamroomlist.empty();
                        if (arrangeInfo.examInfo.roomUsed) {
                            ARRANGE_EXAM.isArranged = true;
                        }
                        for (var i = 0, len = schoolExamRoom.length; i < len; i++) {
                            data = schoolExamRoom[i];
                            if (arrangeInfo.examInfo.roomUsed && arrangeInfo.examInfo.roomUsed[data.id]) {
                                usedSeat = arrangeInfo.examInfo.roomUsed[data.id].usedSeat;
                                temp = ARRANGE_EXAM.$paikaochangSelectedexamroomTemp.clone();
                                $("td:eq(0)", temp).text(data.examRoomNumber);
                                $("td:eq(1)", temp).text(data.examRoomName);
                                $("td:eq(2)", temp).text(data.examRoomSitNumber);
                                $("td:eq(3)", temp).text(usedSeat == data.examRoomSitNumber ? "已满" : "已排" + usedSeat + "人");
                                countCap += data.examRoomSitNumber;
                                ARRANGE_EXAM.$paikaochangArrangedSelectedexamroom.append(temp.data(kwgl.PAGE_CACHE_KEY, data));
                            } else {
                                temp = ARRANGE_EXAM.$paikaochangExamRoomListTemp.clone();
                                tds = $("td", temp);
                                tds.eq(0).text(data.examRoomNumber);
                                tds.eq(1).text(data.examRoomName);
                                tds.eq(2).text(data.examRoomSitNumber);
                                ARRANGE_EXAM.$paikaochangArrangedExamroomlist.append(temp.data(kwgl.PAGE_CACHE_KEY, data));
                            }
                        }
                        //已选考场总容量
                        ARRANGE_EXAM.$paikaochangDetailExamCap.text(countCap);
                        $('.paikaochang').show().siblings().hide();
                    } else {
                        alert(data.message);
                    }
                }
            });
        });
        ARRANGE_EXAM.lockText = ["<a href='javascript:void(0)'>未锁定</a>", "<a href='javascript:void(0)'>已锁定</a>"];
        ARRANGE_EXAM.arrangeExamText = ['<a class="arrange-examroom-trigger" href="javascript:void(0)">排考场</a>', "排考场"];
        //锁定按钮点击
        ARRANGE_EXAM.$arrangeExamTBODY.delegate(".lock-arrange", "click", function () {
            var examInfo = $(this).closest("tr");
            var isLock = examInfo.data(kwgl.PAGE_CACHE_KEY).isLock == 0 ? 1 : 0;
            $.ajax({
                url: "/exam1/lockExam.do?_=" + new Date(),
                type: "post",
                datatype: "json",
                data: {examId: examInfo.data(kwgl.PAGE_CACHE_KEY).id, isLock: isLock},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        $("td:eq(4) .lock-arrange", examInfo).html(ARRANGE_EXAM.lockText[isLock]);
                        $("td:eq(5) span.arrange-examroom", examInfo).html(ARRANGE_EXAM.arrangeExamText[isLock]);
                        //修改缓存标志
                        examInfo.data(kwgl.PAGE_CACHE_KEY).isLock = isLock;
                    } else {
                        alert(data.message);
                    }
                }
            });
        });
        //查看按钮点击
        ARRANGE_EXAM.$arrangeExamTBODY.delegate(".kaosheng-view", "click", function () {
        	if($(this).parent().parent().find("td").get(3).innerHTML == "未完成"){
        		alert("还未安排考场，无法查看！");
        		return;
        	}

            $('#kaosheng-view-arranged-title-tr').empty();
            $('#kaosheng-view-arranged-title-tr').append('<th width="120">班级</th><th>考场名称</th><th width="120">考场编号</th><th width="120">考号</th><th width="100">考生姓名</th>');
        	
            var target = $(this).closest("tr");
            var data = ARRANGE_EXAM.$kaoshengViewConfirm.removeData("query");
            ARRANGE_EXAM.$kaoshengViewContainer.data(kwgl.PAGE_CURR_CACHE_KEY, target);
            //考试信息
            var examInfo = target.data(kwgl.PAGE_CACHE_KEY);
            ARRANGE_EXAM.$kaoshengViewTitle.text(examInfo.name + " / 考生安排查看");
            $.ajax({
                url: "/exam1/loadClassAndExamRoom.do?_=" + new Date(),
                type: "get",
                datatype: "json",
                data: {examId: examInfo.id},
                success: function (data) {
                    if (!data) {
                        return false;
                    }
                    if (data.code == "200") {
                        ARRANGE_EXAM.$classSelectField.empty();
                        ARRANGE_EXAM.$examroomSelectField.empty();
                        var clazz = data.message.class;
                        ARRANGE_EXAM.$classSelectField.append("<option value='ALL'>全部</option>");
                        for (var i = 0, len = clazz.length; i < len; i++) {
                            ARRANGE_EXAM.$classSelectField.append("<option value='" + clazz[i].id + "'>" + clazz[i].className + "</option>");
                        }

                        var room = data.message.room;
                        ARRANGE_EXAM.$examroomSelectField.append("<option value='ALL'>全部</option>");
                        for (var i = 0, len = room.length; i < len; i++) {
                            ARRANGE_EXAM.$examroomSelectField.append("<option value='" + room[i].id + "'>" + room[i].examRoomName + "</option>");
                        }
                        $("[name=apview][value=examroom]", ARRANGE_EXAM.$kaoshengViewContainer).trigger("click");
                        $('.kaosheng-edit').show().siblings().hide();
                    } else {
                        alert(data.message);
                    }
                }
            });
        });
    }
    /**
     *初始化参数
     */
    var kwgl = {
        $pageContent: $("#page-content"),
        PAGE_CACHE_KEY: "data-cache",
        PAGE_CURR_CACHE_KEY: "curr",
        $commonFormDataContainer: $("#commonFormDataContainer")
    };
    var Common = require('common');

    require("ajaxfileupload");
    require("pagination");
    require('treeview');
    kwgl.teacherFunc = function () {
        kwgl.onlyTeacher = true;
    };

    Array.prototype.remove = function (dx) {
        if (isNaN(dx) || dx > this.length) {
            return false;
        }
        this.splice(dx, 1);
    }

    
    
    
    /***
     * @func treeview
     * @desc 树节点
     * @example
     * qcdj.treeview([{},{}])
     */

    kwgl.treeview = function(zNodes){
        var setting = {
            edit: {
                enable: false,
                showRemoveBtn: false,
                showRenameBtn: false
            },
            view: {
                
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
            }

        };
        function showIconForTree(treeId, treeNode) {
            return !treeNode.isParent;
        };
        var newCount = 1, className = 'dark';
        $.fn.zTree.init($('#treeDemo'), setting, zNodes);
    };
    
    
    
    
    
    
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiangCheng.init()
     */
    kwgl.init = function () {


        Common.tab('click', $('.tab-head'))

        kwgl.$pageContent.delegate('.class-add .green-btn, .icon-edit, .kf-set', 'click', function () {
            $('.class-table').hide();
            $('.exam-item').hide();
            $('.class-edit').show();
            return false;
        });

        $('.back-link').on('click', function () {
            $('.class-table').show().siblings().hide();
            return false;
        });

        $('.chakan-view').on('click', function () {
            $('.chakan-luru').show().siblings().hide();
            return false;
        });
        $(function(){
            $(".sup-tab li").click(function(){
                $(".sup-tab li").removeClass("m-cur");
                $(this).addClass("m-cur");
                $(".set-main>div").hide();
                var name = $(this).attr("id");
                $("#" + "tab-" + name).show();
            })
        })
        $("#tabScoreInput").click(function () {
//            if (!SCORE_LIST.isLoaded) {
//                SCORE_LIST.isLoaded = true;
                SCORE_LIST.loadList();
//            }
        });
        $("#tabLevelSetting").click(function () {
            if (!LEVEL.isLoaded) {
                LEVEL.isLoaded = true;
                LEVEL.queryAllLevels();
            }
        });
        $("#tabStudentArrange").click(function () {
            if (!ARRANGE_EXAM.isLoaded) {
                ARRANGE_EXAM.isLoaded = true;
                ARRANGE_EXAM.loadExam();
            }
        });
        $("#tabExamroomSetting").click(function () {
            if (!EXAM_ROOM.isLoaded) {
                EXAM_ROOM.isLoaded = true;
                EXAM_ROOM.queryAllExamRooms();
            }
        });

        //是否只开启教师角色
        if (!kwgl.onlyTeacher) {
            //考试列表
            EXAM_MANAGE.init();
            EXAM_MANAGE.loadList();

            //考试等级设置
            LEVEL.init();

            //考试设置-授权
            EXAM_SETTING.init();

            //考场资源设置
            EXAM_ROOM.init();


        } else {
            $("#tabScoreInput").text("成绩录入").trigger("click");
            $("th:eq(7),th:eq(8)", SCORE_LIST.$chengjiViewTitle).remove();
            $("tr", SCORE_LIST.$chengjiViewTitle).append("<th width='100'>是否开放</th>");
        }
        //成绩列表
        SCORE_LIST.init();
        //SCORE_LIST.loadList();
        //考生安排
        ARRANGE_EXAM.init();
    };

    module.exports = kwgl;
})
