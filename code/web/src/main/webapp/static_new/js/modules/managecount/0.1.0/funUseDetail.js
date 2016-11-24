/**
 * Created by guojing on 2015/11/13.
 */
/* global Config */
define('funUseDetail',['jquery','doT','easing','common','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var funUseDetail = {},
        Common = require('common'),
        Paginator = require('initPaginator');
    var selParam = {};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * funUseDetail.init()
     */
    funUseDetail.init = function(){


        //设置初始页码
        selParam.page = 1;
        //设置每页数据长度
        selParam.size = 15;
        selParam.operateUserId=$("#operateUserId").val();
        selParam.role=$("#role").val();
        selParam.funId=$("#funId").val();
        selParam.dateStart=$("#dateStart").val();
        selParam.dateEnd=$("#dateEnd").val();
        funUseDetail.funUseDetailPage()
    };

    var option={};
    funUseDetail.funUseDetailPage = function(){
        Common.getData('/manageCount/functionUseDetailPage.do',selParam,function(rep){
            var html='<li class="title">'+$("#userName").val()+'的'+$("#funName").val()+'明细</li>';
            $.each(rep.content,function(i,item){
                var text=item.name;
                var text1="";
                if(text==null){
                    text="";
                }
                if(text.length>30){
                    text1=text.substring(0,30)+"...";
                }else{
                    text1=text;
                }
                text=text.replace(new RegExp("<br>", 'g'),"");
                text1=text1.replace(new RegExp("<br>", 'g'),"");
                html+='<li title="'+text+'">'+text1+'<span>'+item.createTime+'</span></li>';
            });
            $("#mainUl").html(html);

            var total = rep.totalElements;
            option.total= total;
            option.pagesize= selParam.size;
            option.currentpage=selParam.page;
            option.operate=function (totalPage) {
                $('.page-index span').each(function () {
                    $(this).click(function(){
                        if(selParam.page!=parseInt($(this).text())){
                            selParam.page=parseInt($(this).text());
                            funUseDetail.funUseDetailPage();
                        }
                    });
                });
                $('.first-page').click(function(){
                    if(selParam.page!=1) {
                        selParam.page = 1;
                        funUseDetail.funUseDetailPage();
                    }
                });
                $('.last-page').click(function(){
                    if(selParam.page!=totalPage) {
                        selParam.page = totalPage;
                        funUseDetail.funUseDetailPage();
                    }
                })
            }
            Paginator.initPaginator(option);
        });
    }
    module.exports=funUseDetail;
});

