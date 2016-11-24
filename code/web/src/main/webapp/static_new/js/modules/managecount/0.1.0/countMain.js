/**
 * Created by guojing on 2015/11/13.
 */
/* global Config */
define('countMain',['jquery','doT','easing','common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var countMain = {},
        Common = require('common');
        require('pagination');
    var selParam = {};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * countMain.init()
     */
    countMain.init = function(){


        $(".ah-cou").hide();
        var provinceId=$("#provinceId").val();
        selParam.provinceId=provinceId;
        $(".ah-city div span").click(function(){
            var cityId=$(this).attr("cityId");
            selParam.cityId=cityId;
            if(cityId==""){
                selParam.countyId="";
                $(".ah-cou").hide();
            }else{
                countMain.getChangeCityRegion(cityId);
                $(".ah-cou").show();
            }
            selParam.page = 1;
            countMain.getSchoolValueList();
            $(this).addClass("sp-ho").siblings().removeClass("sp-ho");
        });


        $(".ah-stype div span").click(function(){
            selParam.schoolType=$(this).attr("schoolType");
            selParam.page = 1;
            countMain.getSchoolValueList();
            $(this).addClass("sp-schooltype").siblings().removeClass("sp-schooltype");
        });

        //设置初始页码
        selParam.page = 1;
        //设置每页数据长度
        selParam.pageSize = 30;
        countMain.getSchoolValueList();
    }

    countMain.getChangeCityRegion = function(cityId){
        selParam.parentId=cityId
        Common.getData('/education/getRegionList.do',selParam,function(rep){
            var list = rep.regions;
            var html='<span countyId="" class="sp-county">全部</span>';
            $.each(list,function(i,item){
                html+='<span countyId="'+item.id+'">'+item.name+'</span>';
            });
            selParam.countyId="";
            $(".ah-cou div").html(html);

            $(".ah-cou div span").click(function(){
                selParam.countyId=$(this).attr("countyId");
                selParam.page = 1;
                countMain.getSchoolValueList();
                $(this).addClass("sp-county").siblings().removeClass("sp-county");
            });
        });
    }

    countMain.getSchoolValueList = function(){
        Common.getData('/manageCount/getSchoolValueList.do',selParam,function(rep){
            $('.sub-info-list').html('');
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.sub-info-list'});
            if(rep.total>0) {
                var totalPage = Math.ceil(rep.total / rep.pageSize) == 0 ? 1 : Math.ceil(rep.total / rep.pageSize);
                $('#pageDiv').jqPaginator({
                    totalPages: totalPage,//总页数
                    visiblePages: 5,//分多少页
                    currentPage: rep.page,//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        //点击页码的查询
                        //          	            	alert('当前第' + n + '页');
                        if (n != selParam.page) {
                            selParam.page = n;
                            countMain.getSchoolValueList();
                        }
                    }
                });
            }
        });
    }
    module.exports=countMain;
});

