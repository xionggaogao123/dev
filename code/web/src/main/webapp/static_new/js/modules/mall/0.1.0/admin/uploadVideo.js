/**
 * Created by wangkaidong on 2016/3/31.
 */
define(function(require,exports,module){
    var uploadVideo = {};
    require('jquery');
    require('doT');
    require('pagination');
    Common = require('common');


    uploadVideo.init = function(page){
        $('#listCtx').empty();
        Common.getData('/mall/admin/video.do',{page:page,pageSize:20},function(data){
            Common.render({
                tmpl:$('#listJS'),
                data:data.videoList,
                context:'#listCtx'
            });
            paginator(data);
        });
        $('#category').empty();
        Common.getData('/mall/admin/eGoodsCategorys.do',{},function(data){
            Common.render({
                tmpl:$('#categoryJS'),
                data:data,
                context:'#category'
            });
        });
    }

    /**
     * 分页
     * */
    paginator = function(data){
        var isInit = true;
        $('.new-page-links').html("");
        if(data.videoList.length > 0){
            $('.new-page-links').jqPaginator({
                totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                visiblePages: 10,//分多少页
                currentPage: parseInt(data.page),//当前页数
                first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                onPageChange: function (n){ //回调函数
                    if (isInit) {
                        isInit = false;
                    } else {
                        uploadVideo.init(n);
                    }
                }
            });
        }
    }

    $(document).ready(function(){
        $('body').on('click','#add',function(){
            videoDetail();
        });

        $('body').on('click','#submit',function(){
            var form = $('form');
            var data = form.serializeArray();
            var url = '/mall/admin/video.do';
            $.ajax({
                url:url,
                data:data,
                type:'POST',
                success:function(resp){
                    if(resp){
                        if(resp.code == "200"){
                            alert(resp.message);
                            $('form')[0].reset();
                            $('.addVideo').hide();
                            uploadVideo.init(1);
                        }else{
                            alert(resp.message);
                        }
                    }
                }
            });

        });
        $('body').on('click','#cancel',function(){
            $('.addVideo').hide();
        });
    });

    videoDetail = function (id) {
        var data = {};
        if(id){
            var url = '/mall/admin/video/' + id + '.do';
            $.ajax({
                url: url,
                type: 'get',
                data: {},
                async: false,
                success: function (resp) {
                    if(resp){
                        data = resp;
                    }
                }
            });
        }

        if(data){
            $('#id').val(data.id);
            $('#name').val(data.name);
            $('#title').val(data.title);
            $('#category').val(data.category);
            $('#text').val(data.text);
            $('#videoId').val(data.videoId);
            $('#videoImageUrl').val(data.videoImageUrl);
            $('#imageUrl').val(data.imageUrl);
        }
        $('.addVideo').show();
    }


    module.exports = uploadVideo;
});