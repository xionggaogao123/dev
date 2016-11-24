define('depFile',['jquery','doT','common','ajaxfileupload'],function(require,exports,module){
	
	 var depFile = {};
     require('jquery');
     require('doT');
     common = require('common');


     var ajaxfu = require('ajaxfileupload');
     
	 addFile=function()
	 {
		 jQuery(".f-middle-info").hide();
		 jQuery(".upload-manage").show();
	 }
	 
	 showList=function()
	 {
		 jQuery(".f-middle-info").show();
		 jQuery(".upload-manage").hide();
	 }
	 
     depFile.init=function(){

    	 var url="/myschool/department/file/list.do?id="+jQuery("#hidden_id").val();
		 common.getData(url,{},function(rep){
			 jQuery("#file_div").empty();
			 if(rep.length>0)
			 {
			   common.render({tmpl: $('#file_script'), data: rep, context: '#file_div'});
			 }
		 });
	 };
	 
	 
	 delFile=function(fid)
	 {
		 var url="/myschool/department/file/remove.do?id="+jQuery("#hidden_id").val()+"&fileId="+fid;
		 common.getData(url,{},function(rep){
			 {
				 depFile.init();
			 }
		 });
	 }
	 
	 upload=function()
	 {
         ajaxfu.ajaxFileUpload({
             url:"/myschool/department/file/add.do?id="+jQuery("#hidden_id").val(),
             secureuri:true,
             fileElementId:'file',
             success: function(data, status){
            	 showList();
            	 depFile.init();
             },
             error: function (data, status, e){

             }
         });
	 }
	
     module.exports=depFile;
});
