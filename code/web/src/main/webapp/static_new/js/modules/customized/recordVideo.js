/**
 * Created by admin on 2016/8/23.
 */
define(function(require,exports,module){
    require('jquery');
    require('pagination');


    var Common = require('common');

    var page=1;
    $(document).ready(function(){
        //var date=getNowFormatDate();
        getRecord(page);

        $('body').on('click','#select',function(){
            getRecord(page);
        });

        $('body').on('click','.remove',function(){
            if (!confirm("确认要删除？")) {
               return;
            }
            var videoId=$(this).attr('videoId');
            var requestData={};
            requestData.videoId=videoId;
            Common.getData('/customized/removeVideo.do',requestData,function(resp){
                if(resp.code==200){
                    getRecord(page);
                }else{
                    alert(resp.message);
                }
            });
        });


    });

    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
            //+ " " + date.getHours() + seperator2 + date.getMinutes()
            //+ seperator2 + date.getSeconds();
        return currentdate;
    }

    function getRecord(page){
        var requestData={};
        requestData.date=$('#startDateInput').val();
        requestData.page=page;
        requestData.app=$('#kl').val();
        requestData.pageSize=15;
        Common.getData('/customized/getRecord.do',requestData,function(result){
            var isInit = true;
            $('.new-page-links').html("");
            if(result.list.length > 0){
                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(result.count / result.pageSize) == 0 ? 1 : Math.ceil(result.count / result.pageSize),//总页数
                    visiblePages: 10,//分多少页
                    currentPage: parseInt(result.page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getRecord(n);
                        }
                    }
                });
            }
            Common.render({
                tmpl:'#tmpl',
                context:'#videoList',
                data:result.list,
                overwrite:1
            });
        })
    }

})