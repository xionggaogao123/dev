define('department',['jquery','doT','common','select2'],function(require,exports,module){
	 var department = {};
     require('jquery');
     require('doT');
	require('select2')
     common = require('common');
      
     var param={"depId":"55a6348063e70dd891e37bd5"};
     /**
	  * 得到知识面
	  */
     department.list= function()
	 {
		 jQuery("#department_li").empty();
		 var url="/myschool/department/list.do";
		 common.getData(url,{},function(rep){
			 if(rep.length>0)
			 {
			   common.render({tmpl: $('#department_script'), data: rep, context: '#department_li'});
			 }
		 });
	 }
	
     department.init=function(){
    	 department.list();
	 }
	
     departmentadd=function()
     {
    	 var name=jQuery("#department_name").val();
    	 if(!name)
    	 {
    		 alert("请输入部门名称");
    		 return;
         }
    	 closeDiv();
    	 var url="/myschool/department/add.do";
    	 common.getData(url,{"name":name},function(rep){
			
		 });
    	 department.list();
     }
     
     
     departmentdel=function(id)
     {
    	 if(!id)
    	 {
    		 return;
         }
    	 var url="/myschool/department/delete.do";
    	 common.getData(url,{"id":id},function(rep){
			
		 });
    	 department.list();
     }
     
     
     departmentdetail=function(id)
     {
    	 if(!id)
    	 {
    		 return;
         }
    	 var url="/myschool/department/delete.do";
    	 common.getData(url,{"id":id},function(rep){
			
		 });
     }
     
     showDiv=function(){
    	 jQuery("#department_name").val("");
    	 jQuery("#create_div").show();
    	 jQuery(".edit-container").show();
    	 jQuery(".modal-bg").show();
     }
     
     closeDiv=function(){
    	 jQuery("#create_div,.edit-container,.modal-bg").hide();
     }
     
     
     department.depmembers=function()
     {
    	 var depId=jQuery("#depId").val();
    	 param.depId=depId;
    	 jQuery("#member_div").empty();
		 var url="/myschool/department/detail.do";
		 common.getData(url,{"id":param.depId},function(rep){
			 jQuery("#dep_name").text(rep.dep.value);
			 if(rep.member.length>0)
			 {
			   common.render({tmpl: $('#member_script'), data: rep.member, context: '#member_div'});
			 }
		 });
     }
     
     
     showAddMember=function()
     {
    	 jQuery(".edit-container").show();
    	 jQuery("#addMember_div").show();
    	 jQuery(".modal-bg").show();
    	 var url="/user/school/teacher.do";
    	 common.getData(url,{},function(rep){
			 jQuery("#department_user").empty();
			 if(rep.length>0)
			 {
			   common.render({tmpl: $('#department_user_script'), data: rep, context: '#department_user'});
				 $('#department_user').select2();
			 }
		 });
     }
     
     hideAddMember=function()
     {
    	 jQuery(".edit-container,#addMember_div").hide();
     }
     
     addMember=function()
     { 
    	
    	
    	 hideAddMember();
    	 var url="/myschool/department/member/add.do";
		 var userIds = $('#department_user').select2('val');
		 var uid = userIds.toString();
		 if(uid == ''){
			 return false;
		 }
         common.getData(url,{"id":param.depId,"uid":uid},function(res){
    		if(res.code!="200")
    		{
    			  alert(res.message);
    			  return;
    		  }
    		department.depmembers();
		 });
    	 
     }
     
     gotodetail=function(id)
     {
    	 common.goTo("/myschool/manager/department/members.do?id="+id);
     }
     
     gotoDepartList=function()
     {
    	 common.goTo("/myschool/manager/department.do");
     }
     
     delMember=function(memberId)
     {
    	 var url="/myschool/department/member/del.do";
    	 common.getData(url,{"id":param.depId,"memberId":memberId},function(){
		 });
    	 department.depmembers();
     }
     
     gotoFile=function()
     {
    	common.goTo("/myschool/department/file.do?id="+param.depId);
     }
     module.exports=department;
});
