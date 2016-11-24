/* 
* @Author: Tony
* @Date:   2015-12-23 16:02:38
* @Last Modified by:   Tony
* @Last Modified time: 2015-12-23 16:08:13
*/

'use strict';

define(function(require,exports,module){
	var Common = require('common');
	require('ajaxfileupload');
	
   (function(){





		$(".wind-up em").click(function(){
			hide();
		})

	   $('.up-cont2 button').on('click',function() {
		   if ($("#file").val() == "") {
			   alert("请先选择上传文件");
		   } else {
			   show();

			   $.ajaxFileUpload
			   (
				   {
					   url: "/itempool/wordToHtml.do", //用于文件上传的服务器端请求地址
					   secureuri: false, //是否需要安全协议，一般设置为false
					   fileElementId: 'file', //文件上传域的ID
					   dataType: 'json', //返回值类型 一般设置为json

					   success: function (data, status)  //服务器成功响应处理函数
					   {
						   hide();
						   if(data.code == "500") {
							   alert("上传失败！");
						   } else {
							   //alert("上传成功！");
							   sessionStorage.setItem("ids", data.ids);
							   Common.goTo('/itempool/editMulItem.do?ids=&show=1');
						   }
					   },
					   error: function (data, status, e)//服务器响应失败处理函数
					   {
						   hide();
						   alert("上传失败！");
					   }
				   }
			   )
		   }

	   })


   })()

	function hide(){
		$(".bg").fadeOut();
		$(".wind-up").fadeOut();
	}

	function show(){
		$(".bg").fadeIn();
		$(".wind-up").fadeIn();
	}




});