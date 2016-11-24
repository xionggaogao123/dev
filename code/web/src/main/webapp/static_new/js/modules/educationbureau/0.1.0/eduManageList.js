/**
 * Created by guojing on 2015/11/13.
 */
/* global Config */
define('eduManageList',['jquery','doT','easing','common','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var eduManageList = {},
        Common = require('common'),
        Paginator = require('initPaginator');
    var selParam = {};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * eduManageList.init()
     */
    eduManageList.init = function(){

        //设置初始页码
        selParam.page = 1;
        //设置每页数据长度
        selParam.size = 15;
        $('#province').change(function(){
            selParam.parentId = $('#province').val();
            var childId="city";
            eduManageList.getChangeRegion(childId);
        });
        /*$('#city').change(function(){
            selParam.parentId = $('#city').val();
            var childId="county";
            eduManageList.getChangeRegion(childId);
        });*/
        $(".search-btn").click(function(event) {
            selParam.eduName=$("#eduName").val();
            selParam.province=$("#province").val();
            selParam.city=$("#city").val();
            //selParam.county=$("#county").val();
            eduManageList.eduManageListPage()
        });
        $(".add-btn").click(function(event) {
            eduManageList.addEducationPage();
        });
        eduManageList.eduManageListPage();
    };

    eduManageList.addEducationPage = function(){
        window.location.href="/education/gotoAddEducation.do";
    }

    eduManageList.getChangeRegion = function(childId){
        if(selParam.parentId!="") {
            Common.getData('/education/getRegionList.do', selParam, function (rep) {
                var list = rep.regions;
                var html = '<option value="">请选择</option>';
                $.each(list, function (i, item) {
                    html += '<option value="' + item.id + '">' + item.name + '</option>';
                });
                $("#" + childId).html(html);
            });
        }else{
            var html='<option value="">请选择</option>';
            $("#"+childId).html(html);
        }
    }

    eduManageList.editEducationPage = function(id){
        window.location.href="/education/gotoEditEducation.do?id="+id;
    }

    var option={};
    eduManageList.eduManageListPage = function(){
        Common.getData('/education/selEducationList.do',selParam,function(rep){
            $('.table').html('');
            Common.render({tmpl:$('#table'),data:rep,context:'.table'});
            $('.tr-class').dblclick(function(event){
                var id = $(this).attr('id');
                eduManageList.editEducationPage(id);
            });
            option.total= rep.total;
            option.pagesize= rep.pageSize;
            option.currentpage=rep.page;
            option.operate=function (totalPage) {
                $('.page-index span').each(function () {
                    $(this).click(function(){
                        if(rep.page!=parseInt($(this).text())){
                            rep.page=parseInt($(this).text());
                            eduManageList.eduManageListPage();
                        }
                    });
                });
                $('.first-page').click(function(){
                    if(rep.page!=1) {
                        rep.page = 1;
                        eduManageList.eduManageListPage();
                    }
                });
                $('.last-page').click(function(){
                    if(rep.page!=totalPage) {
                        rep.page = totalPage;
                        eduManageList.eduManageListPage();
                    }
                })
            }
            Paginator.initPaginator(option);
        });

        /*
         * 绑定处理按钮
         * */
        $('.infrom-edit').bind("click",function(event){
            var id = $(this).attr('eduId');
            eduManageList.editEducationPage(id);
        });

        /*
         * 绑定删除按钮
         * */
        $('.infrom-delete').bind("click",function(event){
            var id = $(this).attr('eduId');
            eduManageList.delteEducationPage(id);
        });

        /*
         * 绑定开通云平台
         * */
        $('.infrom-openCloud').bind("click",function(event){
            var id = $(this).attr('eduId');
            eduManageList.educationOpenCloud(id);
        });

        /*
         * 绑定关闭云平台
         * */
        $('.infrom-closeCloud').bind("click",function(event){
            var id = $(this).attr('eduId');
            eduManageList.educationCloseCloud(id);
        });
    }

    /*
     * 删除一条信息
     * */
    eduManageList.delteEducationPage = function(id) {
        if (confirm('确认删除此条信息！')) {
            var param = {};
            param.id=id;
            Common.getData('/education/delEducation.do',param ,function(){}
                /*function(rep){
                 if (rep.score) {
                 scoreManager(rep.scoreMsg, rep.score);
                 }
                 }*/
            );
            eduManageList.eduManageListPage();
        }
    }

    /*
     * 开通云平台
     * */
    eduManageList.educationOpenCloud = function(id) {
        var param = {};
        param.id=id;
        Common.getData('/education/educationCloud.do',param ,function(){}
            /*function(rep){
             if (rep.score) {
             scoreManager(rep.scoreMsg, rep.score);
             }
             }*/
        );
        eduManageList.eduManageListPage();
    }


    /*
     * 关闭云平台
     * */
    eduManageList.educationCloseCloud = function(id) {
        var param = {};
        param.id=id;
        Common.getData('/education/educationCloud.do',param ,function(){}
            /*function(rep){
             if (rep.score) {
             scoreManager(rep.scoreMsg, rep.score);
             }
             }*/
        );
        eduManageList.eduManageListPage();
    }


    module.exports=eduManageList;
});

