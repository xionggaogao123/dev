/**
 * Created by guojing on 2016/6/15.
 */
/* global Config */
define('userdoor',['jquery','doT','easing','common','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var userdoor = {},
        Paginator = require('initPaginator'),
        Common = require('common');
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * userdoor.init()
     */
    userdoor.init = function(){



        //设置初始页码
        searchData.page = 1;
        //设置每页数据长度
        searchData.pageSize = 20;

        $(".stukq-main-nav ul li").click(function(){
            searchData.type=$(this).attr("id");
            $(this).parent().find('li').removeClass('stukq-main-active');
            $(this).prop("class","stukq-main-active");
            userdoor.searchDoorData();
        });

        $('#searchBtn').click(function () {
            //设置初始页码
            searchData.page = 1;
            //设置每页数据长度
            searchData.pageSize = 20;
            userdoor.searchDoorData();
        });

        userdoor.searchDoorData();
    };

    var option={};

    //查询参数
    var searchData = {};
    //查询
    userdoor.searchDoorData = function(){

        searchData.name=$('#name').val();
        searchData.selState=$('#selState').val();
        searchData.doorNum=$('#doorNum').val();
        searchData.dateStart=$('#dateStart').val();
        searchData.dateEnd=$('#dateEnd').val();

        Common.getData('/smartCard/searchDoorData.do',searchData,function(rep){
            $('.stukq-data').html('');
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.stukq-data'});
            option.total= rep.total;
            option.pagesize= rep.pageSize;
            option.currentpage=rep.page;
            option.operate=function (totalPage) {
                $('.page-index span').each(function () {
                    $(this).click(function(){
                        if(searchData.page!=$(this).text()){
                            searchData.page=$(this).text();
                            userdoor.searchDoorData();
                        }
                    });
                });
                $('.first-page').click(function(){
                    if(searchData.page!=1) {
                        searchData.page = 1;
                        userdoor.searchDoorData();
                    }
                });
                $('.last-page').click(function(){
                    if(searchData.page!=totalPage) {
                        searchData.page = totalPage;
                        userdoor.searchDoorData();
                    }
                })
            };
            Paginator.initPaginator(option);
        });
    }

    module.exports=userdoor;
});

