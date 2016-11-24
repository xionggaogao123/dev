 /**
 * Created by guojing on 2015/4/15.
 */
 /**
  * @author 郭靖
  * @module  管理统计
  * @description
  * 管理统计模块
  */
 /* global Config */
 define('funUseCount',['jquery','doT','easing','common','manageCountUtils','echarts','initPaginator'],function(require,exports,module){
     /**
      *初始化参数
      */
     var funUseCount = {},
         Common = require('common'),
         Util = require('manageCountUtils'),
         Paginator = require('initPaginator');
         require('echarts');
     var selParam = {};
     /**
      * @func init
      * @desc 页面初始化
      * @example
      * funUseCount.init()
      */
     funUseCount.init = function(){
         if (GetQueryString("a")!="10000") {


         }
         //设置初始页码
         selParam.page = 1;
         //设置每页数据长度
         selParam.size = 10;
         funUseCount.changeTimeArea();
         funUseCount.changeGrade();
         funUseCount.submitFun();
         $("#timeArea").change(function(){
             funUseCount.changeTimeArea();
         });
         $("#role").change(function(){
             funUseCount.changeUserRole();
         });
         $("#gradeId").change(function(){
             funUseCount.changeGrade();
         });
         $("#submitBtn").click(function(){
             funUseCount.submitFun();
         });
     };

     /**
      * @func chartInit
      * @desc 渲染图表
      * @example
      * chengJi.chartInit(id,{})
      */
     funUseCount.chartInit = function(id,opt){
         var _chart = echarts.init($('#'+id)[0]);
         _chart.setOption(opt);
     }

     funUseCount.changeTimeArea = function(){
         var selData = {};
         selData.timeArea = $("#timeArea").val();
         Common.getPostData('/manageCount/getTimeAreaVal.do', selData,function(rep){
             $('#dateStart').val(rep.dateStart);
             $('#dateEnd').val(rep.dateEnd);
         });
     }

     funUseCount.changeUserRole = function(){
         var selData = {};
         selData.role = $("#role").val();
         Common.getPostData('/manageCount/getRoleFunValue.do', selData,function(rep){
             var list = rep.roleFuns;
             var html="";
             $.each(list,function(i,item){
                 html+='<option value="'+item.key+'">'+item.value+'</option>';
             });
             $("#funId").html(html);
         });
     }

     funUseCount.changeGrade = function(){
         var selData = {};
         selData.schoolId=$("#schoolId").val();
         selData.gradeId=$("#gradeId").val();
         var gradeName=$("#gradeId").find("option:selected").text();
         if(selData.gradeId!=""){
             Common.getPostData('/manageCount/getGradeClassValue.do', selData,function(rep){
                 var list = rep.classList;
                 var html='<option value="">'+gradeName+'全部班级</option>';
                 $.each(list,function(i,item){
                     html+='<option value='+item.id+'>'+item.className+'</option>';
                 });
                 $("#classId").html(html);
             });
         }else{
             var html='<option value="">'+gradeName+'全部班级</option>';
             $("#classId").html(html);
         }
     }

     funUseCount.titleChange = function (){
         var funName=$("#funId").find("option:selected").text();
         var html1="<span>1</span>&nbsp;&nbsp;&nbsp; "+funName+"排行榜";
         $("#title1").html(html1);
         $("#title11").text(funName);
         $("#title113").text(funName+"量");
         var html2="<span>2</span>&nbsp;&nbsp;&nbsp; "+funName+"统计";
         $("#title2").html(html2);

         var role=$("#role").val();
         var roleText=$("#role").find("option:selected").text();
         if(role==2||roleText=="老师"){
             $("#divTitle1").text(funName+"(老师)统计");
             $("#spanTitle1").text("老师姓名");
         }else if(role==1||roleText=="学生"){
             $("#spanTitle1").text("学生姓名");
             $("#divTitle1").text(funName+"(学生)统计");
         }else{
             $("#spanTitle1").text("家长姓名");
             $("#divTitle1").text(funName+"(家长)统计");
         }
         var spanTitle2=funName.substring(0,funName.length-1)+"总数";
         $("#spanTitle2").text(spanTitle2);

     }

     funUseCount.submitFun = function() {
         MessageBox('图表加载中...', 0);
         selParam.schoolId=$("#schoolId").val();
         selParam.gradeId=$("#gradeId").val();
         selParam.classId=$("#classId").val();
         selParam.role=$("#role").val();
         selParam.funId=$("#funId").val();
         selParam.dateStart=$("#dateStart").val();
         selParam.dateEnd=$("#dateEnd").val();
         funUseCount.titleChange();
         funUseCount.getSchoolRankingList();
         funUseCount.seachUseTimeDist();
         funUseCount.getPagingQueryList();
         ClosePromptDialog();
     }

     funUseCount.getSchoolRankingList = function(){
         Common.getPostData('/manageCount/rankingList.do', selParam,function(rep){
             var result={};
             var number=new Array("第一名","第二名","第三名","第四名","第五名","第六名","第七名","第八名","第九名","第十名");
             result.list=[];
             var list = rep.list;
             $.each(list,function(i,item){
                 var obj=new Object();
                 obj.number=number[i];
                 obj.userId=item.userId;
                 obj.name=item.name;
                 obj.newCountTotal=item.newCountTotal;
                 result.list[i]=obj;
             });
             $('#mainTdiv1').html('');
             Common.render({tmpl: $('#j-tmpl1'), data: result.list, context: '#mainTdiv1'});
         });
     }


     funUseCount.seachUseTimeDist = function() {
         Common.getPostData('/manageCount/useTimeDistMap.do', selParam, function(data){
             var rawdata = {};
             rawdata.title_text = "访问分析";
             rawdata.title_subtext = $("#currTime").val();

             rawdata.time_area=[];
             rawdata.people_count=[];
             var list=data.list;
             for(var i in list) {
                 rawdata.time_area[i]=list[i].createTime;
                 rawdata.people_count[i]=list[i].newCountTotal;
             }
             var chardata1 = Util.buildChartData('gltj_fwfx','',rawdata);
             funUseCount.chartInit('main1',chardata1);
         });
     }

     var option={};
     funUseCount.getPagingQueryList = function(){
         Common.getData('/manageCount/pagingQuery.do',selParam,function(rep){
             $('#mainTdiv2').html('');
             Common.render({tmpl: $('#j-tmpl2'), data: rep.content, context: '#mainTdiv2'});
             var total = rep.totalElements;
             option.total= total;
             option.pagesize= selParam.size;
             option.currentpage=selParam.page;
             option.operate=function (totalPage) {
                 $('.page-index span').each(function () {
                     $(this).click(function(){
                         if(selParam.page!=parseInt($(this).text())){
                             selParam.page=parseInt($(this).text());
                             funUseCount.getPagingQueryList();
                         }
                     });
                 });
                 $('.first-page').click(function(){
                     if(selParam.page!=1) {
                         selParam.page = 1;
                         funUseCount.getPagingQueryList();
                     }
                 });
                 $('.last-page').click(function(){
                     if(selParam.page!=totalPage) {
                         selParam.page = totalPage;
                         funUseCount.getPagingQueryList();
                     }
                 })
             }
             Paginator.initPaginator(option);
         });
         /*
          * 绑定查看明细按钮
          * */
         $('.detail').bind("click",function(event){
             var id= $(this).attr('userId');
             var name= $(this).attr('userName');
             funUseCount.gotoDetail(id,name);
         });

     }

     funUseCount.gotoDetail = function(id,name){
         var schoolId=$("#schoolId").val();
         var role=$("#role").val();
         var funId=$("#funId").val();
         var dateStart=$("#dateStart").val();
         var dateEnd=$("#dateEnd").val();
         var param="schoolId="+schoolId+"&operateUserId="+id+"&userName="+name+"&role="+role+"&funId="+funId+"&dateStart="+dateStart+"&dateEnd="+dateEnd;
         var url="/manageCount/functionUsePage.do?"+param;
         Common.goTo(url);
     }
     function GetQueryString(name)
     {
         var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
         var r = window.location.search.substr(1).match(reg);
         if(r!=null)return  unescape(r[2]); return null;
     }
     module.exports=funUseCount;
 });