define('studentlist',['jquery','doT','common','initPaginator'],function(require,exports,module){
	 var studentlist = {};
     require('jquery');
     require('doT');
     common = require('common');
     Paginator = require('initPaginator');
	
	 
     studentlist.init=function(){

	 }
     
     
     addGoodPaper=function (uid)
     {
    	 var url="/exam/g/add.do?did="+jQuery("#did").val()+"&uid="+uid;
    	 
    	 common.getData(url,{},function(rep){
			 if(rep.code=="200")
			 {
					 jQuery("#"+uid+"_1").show();
					 jQuery("#"+uid+"_img").show();
					 jQuery("#"+uid+"_2").hide();
			 }
		 });
     }
     
     removeGoodPaper=function (uid)
     {
    	 var url="/exam/g/remove.do?did="+jQuery("#did").val()+"&uid="+uid;
    	 
    	 common.getData(url,{},function(rep){
			 if(rep.code=="200")
			 {
				 jQuery("#"+uid+"_1").hide();
				 jQuery("#"+uid+"_img").hide();
				 jQuery("#"+uid+"_2").show();
			 }
		 });
     }
     
     
     gotoPapers= function (){
         window.location.href = "/exam/index.do";
     }
     
     
     module.exports=studentlist;
});
