/**
 * Created by fl on 2016/6/15.
 */
define(function(require,exports,module){

    var Common = require('common');
    require('pagination');


    (function(){
        getUsers(1);

        $('body').on('click', 'input:checkbox', function(){
            var tr = $(this).parents('tr');
            updateUserInfo(tr);
        })


    })()

    function getUsers(page){
        var requestData = {};
        requestData.page = page;
        requestData.pageSize = 20;
        Common.getDataAsync("/party/users.do", requestData, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                jqPaginator(data);
                Common.render({
                    tmpl: '#dtosTmpl',
                    data: data.dtos,
                    context:'#dtos',
                    overwrite: 1
                });
            }
        })

    }

    function jqPaginator(data){
        var isInit = true;
        $('.new-page-links').html("");
        if(data.dtos.length > 0){
            $('.new-page-links').jqPaginator({
                totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                visiblePages: 10,//分多少页
                currentPage: parseInt(data.page),//当前页数
                first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                onPageChange: function (n) { //回调函数
                    if (isInit) {
                        isInit = false;
                    } else {
                        getUsers(n);
                    }
                }
            });
        }
    }

    function updateUserInfo(tr){
        var partyUserDTO = {};
        partyUserDTO.isPartyMember = tr.find('.pm').is(':checked') ? 1 : 0;
        partyUserDTO.isCenterMember = tr.find('.cm').is(':checked') ? 1 : 0;
        partyUserDTO.isPartySecretary = tr.find('.ps').is(':checked') ? 1 : 0;

        Common.postDataAsync('/party/users/' + tr.attr('id') + '.do', partyUserDTO, function(resp){
            if(resp.code == '500'){
                alert("更改失败");
            }
        })
    }

})