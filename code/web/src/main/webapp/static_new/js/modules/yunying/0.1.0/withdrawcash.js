/**
 * Created by yan on 2015/4/22.
 */

$(function () {
    searchNews();
    $('body').on('click', '#seachindex', function () {
        getNewsList(1);
    });
    $('body').on('click', '#download', function () {
        window.location.href="/yunying/exportWithdraw.do?begintime="+$("#bTime").val()+"&endtime="+$("#eTime").val()+"&tname="+$('#tname').val();
    });
});


// 分页初始化
function initPaginator(option) {
    var totalPage = '';
    if (option.total == 0)
        $('.page-paginator').hide();
    else
        $('.page-paginator').show();
    $('.page-index').empty();
    if (option.total % option.pagesize == 0) {
        totalPage = option.total / option.pagesize;
    } else {
        totalPage = parseInt(option.total / option.pagesize) + 1;
    }
    buildPaginator(totalPage, option.currentpage);
    option.operate(totalPage);
}
function buildPaginator(totalPage, currentPage) {
    if (totalPage > 5) {
        if (currentPage < 4) {
            for (var i = 1; i <= 5; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
            $('.page-index').append('<i>···</i>');
        } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
            $('.page-index').append('<i>···</i>');
            for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
            $('.page-index').append('<i>···</i>');
        } else {
            $('.page-index').append('<i>···</i>');
            for (var i = totalPage - 4; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
        }
    } else {
        for (var i = 1; i <= totalPage; i++) {
            if (i == currentPage) {
                $('.page-index').append('<span class="active">' + i + '</span>');
            } else {
                $('.page-index').append('<span>' + i + '</span>');
            }
        }
    }
}
/**
 * 搜索新闻
 */
function searchNews() {
    getNewsList(1);
}
/**
 * 获取列表
 * @param page
 */
function getNewsList(page) {
    var targetContent = $('.news_manage_info .content_manage_info_top');
    var target = $('.news_manage_info .content_manage_info_top2');
    target.remove();
    var begintime = $("#bTime").val();
    var endtime = $("#eTime").val();

    $.ajax({
        url: '/yunying/selTeacherWithdraw.do',
        type: 'post',
        dataType: 'json',
        data: {
            tname:$("#tname").val(),
            begintime:begintime,
            endtime:endtime,
            page: page,
            pageSize: 20
        },
        success: function (data) {
            target.remove();
            var option = {
                total: data.total,
                pagesize: data.pageSize,
                currentpage: data.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).attr('onclick', 'getNewsList(' + $(this).text() + ');')
                    });
                    $('.first-page').attr('onclick', 'getNewsList(1);');
                    $('.last-page').attr('onclick', 'getNewsList(' + totalPage + ');');
                }
            };
            var list = data.rows;
            for (var i = list.length - 1; i >= 0; i--) {
                var content = '';
                var status = '';
                if (list[i].status==1) {
                    status = '<select class="status"><option value="1">已提现</option><option value="0">未提现</option></select>';
                } else {
                    status = '<select class="status"><option value="0">未提现</option><option value="1">已提现</option></select>';
                }
                content += '<dd class="content_manage_info_top2" nid=' + list[i].id + '>' +
                '<em style="width: 140px;">' + list[i].name + '</em>' +
                '<em style="width: 200px;">' + list[i].schoolname + '</em>' +
                '<em style="width: 70px;">' + list[i].cash + '</em>' +
                '<em style="width: 220px;">' + list[i].paypalAccount + '</em>' +
                '<em style="width: 150px;">' + list[i].username + '</em>' +
                '<em style="width: 180px;">' + list[i].cardnum + '</em>' +
                '<em style="width: 100px;">' + list[i].phone + '</em>' +
                '<em style="width: 270px;">' + list[i].openbank + '</em>' +
                '<em style="width: 160px;">' + list[i].time + '</em>' +
                '<em style="width: 100px;">'+status+'</em>' +
                '<em style="width: 160px;"><input class="beizhu" value="'+list[i].beiZhu+'"></em>' +
                '</dd>';
                targetContent.after(content);
            }
            $(".beizhu").on("blur",function()
            {
                var dom = $(this);
                $.ajax({
                    url: '/yunying/updateBeiZhu.do',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        id:dom.parent().parent().attr('nid'),
                        beiZhu:dom.val()
                    },
                    success: function (data) {
                        if (data.flg) {
                            alert("修改成功！");
                        } else {
                            alert("修改失败！");
                        }
                    }
                });
            });
            $(".status").on("change",function()
            {
                var dom = $(this);
                $.ajax({
                    url: '/yunying/updateStatus.do',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        id:dom.parent().parent().attr('nid'),
                        status:dom.val()
                    },
                    success: function (data) {
                        if (data.flg) {
                            alert("选择成功！");
                        } else {
                            alert("选择失败！");
                        }
                    }
                });
            });
            initPaginator(option);
        },
        error: function () {
            console.log('selSchoolSubject error');
        }
    });
}


function hideAlert() {
    $(".news_manage_bf").css("display", "none");
    $(".content_manage_cg").css("display", "none");
}

