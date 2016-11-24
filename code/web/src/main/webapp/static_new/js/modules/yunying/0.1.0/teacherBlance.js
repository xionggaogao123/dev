/**
 * Created by qiangm on 2015/7/3.
 */
var currentPage=1;//当前页
$(function () {

    $('body').on('click', '.list-delete', function () {
        //var belong = $(this).parents('.list-container').attr('belong');
        //deleteNewsColumn($(this));
        $(".news_manage_bf").css("display", "block");
        $(".news_manage_cg2").css("display", "block");
        $("#columnName2").val($(this).parent().attr('name'));
        $("#userid2").val($(this).parent().attr('uid'));
        editNewsColumn($(this),2);
    });
    $('body').on('click', '.list-edit', function () {
        $(".news_manage_bf").css("display", "block");
        $(".news_manage_cg").css("display", "block");

        $("#userid").val($(this).parent().attr('uid'));
        $("#columnName").val($(this).parent().attr('name'));
        //$("#columnDir").val($(this).parent().attr('dir'));
        editNewsColumn($(this),1);
    });
    $(".new_button_I").on('click', function () {
        getColumnList(1);
    });
    $(".newsColum_chooseAll").on("click", function () {
        //alert($(".newsColum_chooseAll").is(':checked'));
        //alert($("input[name='newsColumnTitle']:checked"));
        if ($(".newsColum_chooseAll").is(':checked')) {
            $("input[name='newsColumnItem']").prop("checked", true);
            //$("[name = newsColumnItem]:checkbox").attr("checked", true);
        }
        else {
            $("input[name='newsColumnItem']").prop("checked", false);
            //$("[name = newsColumnItem]:checkbox").attr("checked", false);
        }
    });
    getColumnList(1);
});
$(function () {
    $(".new_button_II").click(function () {
        $(".news_manage_bf").css("display", "block");
        $(".news_manage_cg").css("display", "block");
    })
});
$(function () {
    $(".news_close").click(function () {
        $("#columnName").val("");
        // $("#columnDir").val("");
        // $("#connent").val("");
        $("#columnName2").val("");
        // $("#columnDir2").val("");
        // $("#connent2").val("");
        $(".news_manage_bf").css("display", "none");
        $(".news_manage_cg").css("display", "none");
        $(".news_manage_cg2").css("display", "none");
        $(".content_manage_cg").css("display", "none");

    })
});
$(function () {
    $(".new_button_V").click(function () {
        $(".news_manage_bf").css("display", "block");
        $(".content_manage_cg").css("display", "block");


    })
});
// 初始化高度
function initHeight() {
    var h = document.documentElement.clientHeight;
    $('.news_manage_top').css('minHeight', h - 160);
}
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
 * 获取栏目列表
 */
function getColumnList(page) {
    currentPage=page;
    //切换分页后，多选框全部清空
    $("[name = newsColumnItem]:checkbox").attr("checked", false);
    $(".newsColum_chooseAll").attr("checked", false);
    var targetContent = $('.news_manage_info .news_manage_info_top');
    var target = $('.news_manage_info .news_manage_info_middle');
    target.remove();
    $.ajax({
        url: '/yunying/selUserInfo.do',
        type: 'post',
        dataType: 'json',
        data: {
            schoolId:'',
            username: $('#name').val(),
            jinyan:$('#jysel').val(),
            page: page,
            pageSize: 20
        },
        success: function (data) {
            target.remove()
            var option = {
                total: data.total,
                pagesize: data.pageSize,
                currentpage: data.page,
                operate: function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).attr('onclick', 'getColumnList(' + $(this).text() + ');')
                    });
                    $('.first-page').attr('onclick', 'getColumnList(1);');
                    $('.last-page').attr('onclick', 'getColumnList(' + totalPage + ');');
                }
            }
            var list = data.rows;
            for (var i = list.length-1; i >=0; i--) {
                var jy = '禁言';
                if (list[i].jinyan==1) {
                    jy = '解除禁言';
                }
                var content = '';
                content += '<dd class="news_manage_info_middle" name=' + list[i].userName + ' exp='+list[i].experienceValue+' balance='+list[i].balance+' uid='+list[i].id+'>' +
                '<em class="list-username" style="cursor: pointer;">' +
                list[i].userName +
                '</em>' +
                '<em>' +
                list[i].mainClassName +
                '</em>' +
                '<em>' +
                list[i].schoolName +
                '</em>' +
                '<em>' +
                list[i].mobileNumber +
                '</em>' +
                '<em>' +
                list[i].email +
                '</em>' +
                '<em>' +
                list[i].experienceValue +
                '</em>' +
                '<em>' +
                list[i].balance +
                '</em>' +
                '<button class="list-edit" style="margin-left: 10px;" onclick="editNewsColumn(' + list[i].id.toString() + ',' + $(this) + ')">编辑EXP</button>' +
                '<button class="list-delete news_manage_button" onclick="editNewsColumn(' + list[i].id.toString() + ',' + $(this) + ')" class="news_manage_button">编辑余额</button>' +
                '<button class="list-muzzled" style="margin-left: 10px;">'+jy+'</button>' +
                '<a class="list-blance" target="_blank" href="/yunying/balanceList.do?userId=' + list[i].id.toString() + '">余额历史</a>'+
                '</dd>';
                targetContent.after(content);
            }
            initPaginator(option);
            $('.list-username').on('click', function () {
                var dom = $(this);
                window.open("/experience/studentScoreList.do?studentId="+dom.parent().attr('uid'));

            });
            $(".list-muzzled").on('click', function () {
                var dom = $(this);
                    $.ajax({
                        url: '/yunying/jinyan.do',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            userid:dom.parent().attr('uid')
                        },
                        success: function (data) {
                            if(data.jinyan==0) {
                                dom.html("禁言");
                                alert("解除禁言！");
                            } else {
                                dom.html("解除禁言");
                                alert("禁言成功！");
                            }
                        }
                    });
            });

        },
        error: function () {
            console.log('selSchoolSubject error');
        }
    });
}

/**
 * 编辑栏目
 */

function editNewsColumn(target,type) {
    // $("#columnDir").val('');
    // $("#columnDir2").val('');
    // $("#connent").val('');
    // $("#connent2").val('');
    $(".edit-commit-btn").off('click');
    $(".edit-commit-btn").on('click', function () {
        var dir;
        var url;
        var userid;
        var content;
        if(type==1) {
            dir = $.trim($("#columnDir").val());
            userid = $("#userid").val();
            content = $.trim($("#connent").val());
            url = "/yunying/modifyExpValue.do";
        } else {
            dir = $.trim($("#columnDir2").val());
            userid = $("#userid2").val();
            content = $.trim($("#connent2").val());
            url = "/yunying/modifyBalance.do";
        }
        if (dir == "") {
            alert("请输入！");
            return;
        }
        if (content=="") {
            alert("备注不能为空！");
            return;
        }
        else {
            $.ajax({
                url: url,
                type: "post",
                dataType: "json",
                data: {
                    userid: userid,
                    exp: dir,
                    content:content
                },
                success: function (data) {
                    if (data.flg) {
                        alert("添加成功！");
                        getColumnList(1);
                        ////<dd class="news_manage_info_middle" name='+list[i].columnName+' dir='+list[i].columnDir+' sid=' + list[i].id+ '>'+
                        //target.parent().find('em').text(name);
                        //target.parent().attr("name", name);
                        //target.parent().attr("dir", dir);
                        hideAlert();
                    }
                    else{
                        alert("修改失败");
                    }
                },
                error: function () {

                    alert("修改失败！");
                }
                /*complete: function () {
                 hideAlert();
                 }*/
            });
        }
    });
}
function hideAlert() {
    $(".news_manage_bf").css("display", "none");
    $(".news_manage_cg").css("display", "none");
    $(".news_manage_cg2").css("display", "none");
    $(".content_manage_cg").css("display", "none");

}

