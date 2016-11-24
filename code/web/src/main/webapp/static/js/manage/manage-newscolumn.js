/**
 * Created by qiangm on 2015/7/3.
 */
var currentPage=1;//当前页
$(function () {
    //initHeight();
    $(".new_button_II").on("click", function () {
        $("#columnName").val("");
        $("#columnDir").val("");


        addNewsColumn();
    });
    $('body').on('click', '.list-delete', function () {
        var belong = $(this).parents('.list-container').attr('belong');
        deleteNewsColumn($(this));
    });
    $('body').on('click', '.list-edit', function () {
        $(".news_manage_bf").css("display", "block");
        $(".news_manage_cg").css("display", "block");


        $("#columnName").val($(this).parent().attr('name'));
        $("#columnDir").val($(this).parent().attr('dir'));
        //var belong = $(this).parents('.list-container').attr('belong');
        editNewsColumn($(this));
    });
    $(".new_button_I").on('click', function () {
        deleteGroupColumn();
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
        $("#columnDir").val("");
        $(".news_manage_bf").css("display", "none");
        $(".news_manage_cg").css("display", "none");
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
        url: '/myschool/newscolumnlist.do',
        type: 'post',
        dataType: 'json',
        data: {
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
                var content = '';
                content += '<dd class="news_manage_info_middle" name=' + list[i].columnName + ' dir=' + list[i].columnDir + ' sid=' + list[i].id + '>' +
                '<input type="checkbox" name="newsColumnItem">' +
                '<em>' +
                list[i].columnName +
                '</em>' +
                '<button class="list-edit" onclick="editNewsColumn(' + list[i].id.toString() + ',' + $(this) + ')">编辑</button>' +
                '<button class="list-delete news_manage_button" onclick="deleteNewsColumn(' + list[i].id.toString() + ',' + $(this) + ')" class="news_manage_button">删除</button>' +
                '</dd>';
                targetContent.after(content);
            }
            initPaginator(option);


        },
        error: function () {
            console.log('selSchoolSubject error');
        }
    });
}
/**

 * 批量删除栏目
 */
function deleteGroupColumn() {
    var nidList = "";
    $("input[name='newsColumnItem']:checked").each(function (i) {
        nidList += $(this).parent().attr('sid') + ",";
    });
    if (nidList != "") {
        nidList = nidList.substr(0, nidList.length - 1);
    }
    if (confirm("确定要删除选择的所有新闻栏目吗？")) {
        $.ajax({
            url: '/myschool/delManyNewsColumn.do',
            type: 'post',
            dataType: 'json',
            data: {
                newsIds: nidList
            },
            success: function (data) {
                var result = "成功删除" + data.delete + "条栏目";
                if (data.notdelete.length > 0) {
                    result += "\r\n因已存在栏目下新闻，无法删除栏目有：";
                    for (var i = 0; i < data.notdelete.length; i++) {
                        result += "\r\n" + data.notdelete[i];
                    }
                    result += "\r\n请删除新闻后再删除该栏目";

                }
                alert(result);
                getColumnList(currentPage);
                /*if (data == true) {
                 alert("删除成功");
                 getColumnList(1);
                 } else {
                 alert("删除失败");
                 }*/
            },
            error: function () {
                alert("删除失败");
            }
        });
    }
}
/**
 * 删除栏目
 */
function deleteNewsColumn(target) {


    if (confirm("确定要删除该新闻栏目？")) {
        $.ajax({
            url: '/myschool/deleteNewsColumn.do',
            type: 'post',
            dataType: 'json',
            data: {
                newsColumnId: target.parent().attr('sid')
            },
            success: function (data) {
                if (data == true) {
                    alert("删除成功");
                    target.parent().remove();
                } else {
                    alert("该栏目下已经发布新闻，请删除新闻后删除该栏目");

                }
            },
            error: function () {
                alert("删除失败");
            }
        });
    }
}
/**
 * 编辑栏目
 */

function editNewsColumn(target) {
    $(".edit-commit-btn").off('click');
    $(".edit-commit-btn").on('click', function () {
        var name = $.trim($("#columnName").val());
        var dir = $.trim($("#columnDir").val());
        if (name == "" || dir == "") {
            alert("请填写完整！");
            return;
        }
        if(name.length>6)
        {
            alert("栏目名长度超过6,请删减！");
            return;
        }
        if(dir.length>20)
        {
            alert("栏目目录长度超过20，请删减!");
            return;
        }
        else {
            $.ajax({
                url: "/myschool/updatenewscolumn.do",
                type: "post",
                dataType: "json",
                data: {
                    columnId: target.parent().attr('sid'),
                    columnName: name,
                    columnDir: dir
                },
                success: function (data) {
                    if (data == 0) {
                        alert("修改成功！");
                        //<dd class="news_manage_info_middle" name='+list[i].columnName+' dir='+list[i].columnDir+' sid=' + list[i].id+ '>'+
                        target.parent().find('em').text(name);
                        target.parent().attr("name", name);
                        target.parent().attr("dir", dir);
                        hideAlert();
                    }
                    else if(data==1){
                        alert("修改失败,栏目名不可重复");
                    }
                    else {
                        alert("修改失败,栏目目录不可重复");
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
/**
 * 添加栏目
 */
function addNewsColumn() {
    $(".edit-commit-btn").off('click');
    $(".edit-commit-btn").on('click', function () {
        var name = $.trim($("#columnName").val());
        var dir = $.trim($("#columnDir").val());
        if (name == "" || dir == "") {
            alert("请填写完整！");
        }
        else if(name.length>6)
        {
            alert("栏目名长度超过6,请删减！");
        }
        else if(dir.length>20)
        {
            alert("栏目目录长度超过20，请删减!");
        }
        else {
            $.ajax({
                url: "/myschool/addnewscolumn.do",
                type: "post",
                dataType: "json",
                data: {
                    columnName: name,
                    columnDir: dir
                },
                success: function (data) {
                    if (data == 0) {
                        alert("添加成功！");
                        getColumnList(1);
                        hideAlert();
                    }
                    else if(data==1){
                        alert("添加失败,该栏目名已经创建");
                    }
                    else {
                        alert("添加失败,该栏目目录已经创建");
                    }
                },
                error: function () {

                    alert("添加失败！");
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
    $(".content_manage_cg").css("display", "none");

}

