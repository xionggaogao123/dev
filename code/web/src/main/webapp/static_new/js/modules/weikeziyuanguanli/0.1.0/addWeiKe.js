/**
 * Created by liwei on 15/9/06.
 */
/**
 * @author 李伟
 * @module  添加资源
 * @description
 * 添加资源操作
 */
/* global Config */
define("addWeiKe",['common','imguploader','videoUploadTwo'],function(require,exports,module){
    /**
     *初始化参数
     */
    var addWeiKe = {},
        Common = require('common'),
		videoUpload = require('videoUploadTwo'),
        imguploader = require('imguploader');

    /**
	 * 公共变量
	 */
	var $termType,$subject,$grade,$type;
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * addWeiKe.init()
     */

    addWeiKe.init = function(){


		$termType=$("#termTypeSel");
		$subject = $('#subjectSel');
		$grade=$('#gradeSel');
		$type = $('#typeSel');

		var typeInt=$termType.attr("typeInt");
		addWeiKe.queryAndFill(typeInt);
		$termType.change(function(){
			typeInt=$termType.attr("typeInt");
			addWeiKe.queryAndFill(typeInt);
		});

		$subject.val($("#subjectType").val());
		typeInt=$subject.attr("typeInt");
		addWeiKe.queryAndFill(typeInt);
		$subject.change(function(){
			typeInt=$subject.attr("typeInt");
			addWeiKe.queryAndFill(typeInt);
		});

		$grade.val($("#gradeType").val());

		$type.val($("#type").val());

        //图片上传
    	imguploader(
			function(successResponse){
				if("200" == successResponse.code){
					$("#coverId").val(successResponse.data);
				}
			},
			function(errorResponse){
				if("500" == successResponse.code){
					alert(errorResponse.data);
				}
		}
		);
        //文件上传
		videoUpload(
			function(successResponse){
				if("200" == successResponse.code){
					$("#videoId").val(successResponse.data);
				}
			},
			function(errorResponse){
				if("500" == successResponse.code){
					alert(errorResponse.data);
				}
		}
		);
        
        /**
         * 取消按钮方法
         */
        $('#cancelBtn').on('click',function(){
			location.href = "/cloud/cloudLessonGit.do";
        })
        
        /**
         * 新增按钮的方法
         */
        $('#commitUploadBtn').on('click',function(){

			//获取相应字段值
			var addObj = {};
			addObj.termType = $termType.val();
			if(addObj.termType== ""){
				alert("请选择学段！");
				return;
			}
			addObj.subject = $subject.val();
			addObj.subjectText = $subject.find("option:selected").text();
			if(addObj.subject== ""){
				alert("请选择学科！");
				return;
			}
			addObj.grade = $grade.val();
			addObj.type = $type.val();
			if(addObj.grade== ""&&addObj.type== ""){
				if(typeHide){
					alert("请选择年级！");
				}
				if(gradeHide){
					alert("请选择类别！");
				}
				return;
			}
        	//检查封面和文件上传情况
        	if($('#coverId').val() == ""&&$('#imageUrl').val() == ""){
        		alert("请上传封面！");
        		return;
        	}
        	if($('#videoId').val() == ""&&$('#oldVideoId').val() == ""){
        		alert("请上传微课课件！");
        		return;
        	}

			if(!confirm("是否确认添加？")){
				return;
			}
        	addObj.coverId = $('#coverId').val();
        	addObj.videoId = $('#videoId').val();
			var videoName=$('#thelistVideo').find(".info").text();
			videoName=videoName.substr(0,videoName.lastIndexOf("."));
			addObj.videoName=videoName;
			$.ajax({
				type: "POST",
				url: "/cloud/addMicroLesson.do",
				data: addObj,
				dataType: "json",
				success: function(data){
					if(data.code == "200"){
						alert("操作完成！");
						location.href = "/cloud/cloudLessonGit.do";
					}else{
						alert("操作失败,请联系管理员！");
					}
				}
			});
        });
    };

	var gradeHide= false;
	var typeHide= false;

	//查询填充上方的东西
	addWeiKe.queryAndFill = function(typeInt){
		var selData={};
		var html="<option value=''>--请选择--</option>";
		var schoolType = $termType.val();
		var subjectId = $subject.val()|| null;
		if (subjectId == 53 || subjectId == 47 || subjectId == 51 || subjectId == 52) {
			$grade.html(html);
		}
		if(typeInt==1){
			$subject.html(html);
		}
		if(typeInt==1||typeInt==2){
			$grade.html(html);
			$type.html(html);
			selData.schoolType=schoolType;
			selData.subject=subjectId;
			Common.getPostData('/cloud/infos1.do', selData,function(data){
				if(typeInt==1){
					var subjectList = data.subject;
					for(var i=0;i<subjectList.length;i++){
						var subObj = subjectList[i];
						$subject.append("<option value='"+ subObj.id +"'>" + subObj.value +"</option>");
					}
				}

				var gradeList = data.grade;
				if (gradeList.length >0) {
					gradeHide= false;
					$grade.parent().show();
					for (var i = 0; i < gradeList.length; i++) {
						var subObj = gradeList[i];
						$grade.append("<option value='"+ subObj.id +"'>" + subObj.value +"</option>");
					}
				}else{
					gradeHide= true;
					$grade.parent().hide();
				}

				var pointList = data.type;
				if (pointList.length > 0) {
					typeHide= false;
					$type.parent().show();
					for (var i = 0; i < pointList.length; i++) {
						var subObj = pointList[i];
						$type.append("<option value='"+ subObj.id +"'>" + subObj.name +"</option>");
					}
				}else{
					typeHide= true;
					$type.parent().hide();
				}
			});
		}
	};

    module.exports = addWeiKe;
})
