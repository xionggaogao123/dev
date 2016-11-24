/**
 * Created by yan on 2015/4/22.
 */

$(function () {
    /*editor = new baidu.editor.ui.Editor({
     */
    /*initialFrameHeight: 223,
     minFrameHeight: 120,*/
    /*
     wordCount: true,
     scaleEnabled: true
     });
     editor.render('ueditor');  //editor为编辑器容器的id*/
    $(".new_button_V").on("click", function () {
        addNews();
    });

    $(".news_close_btn").click(function () {
        $(".news_manage_bf").css("display", "none");
        $(".content_manage_cg").css("display", "none");
    });
    $("#img-x").click(function () {
        $("#img_preview").css("display", "none");
        $("#img_preview").attr("src", "");
        $("#img-x").css("display", "none");
    })
    $(".new_button_V").click(function () {
        $(".news_manage_bf").css("display", "block");
        $(".content_manage_cg").css("display", "block");
        getColumnList();
    });
    $('body').on('click', '.news_manage_button', function () {

        deleteNews($(this));
    });
    $('body').on('click', '.list-edit', function () {
        $(".news_manage_bf").css("display", "block");
        $(".content_manage_cg").css("display", "block");

        //先获取完整信息，再显示出来
        getNewsInfo($(this));
        //var belong = $(this).parents('.list-container').attr('belong');

        editNews($(this));
    });
    $('#image-upload').fileupload({
        url: '/myschool/addNewsPic.do',
        paramName: 'file',
        done: function (e, response) {
            //低版本IE获取text/plain的数据解析
            var result = response.result;
            var data = typeof  result == 'string' ? $.parseJSON(result) : result[0] ? $.parseJSON(result[0].documentElement.innerText) : result;
            if (data.result) {
                var url = data.path[0];

                $("#img_preview").css("display", "block");
                $("#img-x").css("display", "block");
                $("#img_preview").attr("src", url);
            }
            //$('.fancybox').fancybox();
        }
    });
    $(".newsTitle").on("click", function () {

        if ($(".newsTitle").is(':checked')) {
            $("[name = newsItem]:checkbox").prop("checked", true);
        }
        else {
            $("[name = newsItem]:checkbox").prop("checked", false);
        }
    });
    //批量删除
    $(".new_button_I").on("click", function () {
        deleteGroup();
    });
    getColumnListForSearch();

});
/**
 * 批量删除
 */
function deleteGroup() {
    var nidList = "";
    $("input[name='newsItem']:checked").each(function (i) {
        nidList += $(this).parent().attr('nid') + ",";
    });
    if (nidList != "") {
        nidList = nidList.substr(0, nidList.length - 1);
    }
    if (confirm("确定要删除选择的所有新闻吗？")) {
        $.ajax({
            url: '/myschool/delManyNews.do',
            type: 'post',
            dataType: 'json',
            data: {
                newsIds: nidList
            },
            success: function (data) {
                if (data == true) {
                    alert("删除成功");
                    getNewsList(1);
                    //target.parent().remove();
                } else {
                    alert("删除失败");
                }
            },
            error: function () {
                alert("删除失败");
            }
        });
    }
}
/**
 * 获取新闻详细内容
 */
function getNewsInfo(target) {
    clearForm();
    $.ajax({
        url: "/myschool/getOneNewsInfo.do",
        type: 'post',
        dataType: "json",
        data: {
            newsId: target.parent().attr('nid')
        },
        success: function (data) {
            getColumnListWithSelected(data.column);
            $(".content_title").val(data.title);
            if (data.pinned == 1) {
                $(".content_manage_in").prop("checked", true);
            }
            if (data.thumb != "") {
                $("#img_preview").css("display", "block");
                $("#img_preview").attr("src", data.thumb);
                $("#img-x").css("display", "block");
            }
            else {
                $("#img-x").css("display", "none");
            }
            $(".content_manage_te").val(data.digest);
            //$(".content_manage_ter").val(data.content);
            UE.getEditor('ueditor').setContent(data.content);
        },
        error: function () {
            console.log('selSchoolSubject error');
        }
    });
}
/**
 * 添加时先清空添加表单
 */
function clearForm() {

    $(".content_title").val("");

    $(".content_manage_in").attr("checked", false);

    $("#img_preview").css("display", "none");

    $("#img_preview").attr("src", "");

    $(".content_manage_te").val("");
    $("#img-x").css("display", "none");
    //$(".content_manage_ter").val("");
    UE.getEditor('ueditor').setContent("");
}
/**
 * 编辑新闻
 */
function editNews(target) {
    $(".new_publish_btn").off('click');
    $(".new_publish_btn").on('click', function () {
        var title = $.trim($(".content_title").val());
        var column = $(".choose_column").val();
        var pinned = $(".content_manage_in").is(':checked') == true ? 1 : 0;
        var thumb = $.trim($("#img_preview").attr("src"));
        //var thumb = $.trim($(".upload_img")[0].src);
        var digest = $.trim($(".content_manage_te").val());
        //var content= $.trim($(".content_manage_ter").val());
        var content = UE.getEditor('ueditor').getContent();
        if(column == null)
        {
            alert("请先创建栏目");
            return;
        }
        if (title == "" || column == "" || content == "" ) {
            alert("请填写完整！");
        }
        else if (title.length > 60) {
            alert("新闻标题长度超过60字，请删减！");
        }
        else if (digest.length > 200) {
            alert("新闻描述长度超过200字，请删减!");
        }
        else if (pinned == 1 && thumb == "" && $(".choose_column").find("option:selected").text() == "新闻") {
            alert("置顶新闻必须上传图片!");
        }
        else {
            $.ajax({
                url: "/myschool/upnews.do",
                type: "post",
                async: false,
                dataType: "json",
                data: {
                    newsId: target.parent().attr('nid'),
                    columnId: column,
                    title: title,
                    pinned: pinned,
                    thumb: thumb,
                    digest: digest,
                    content: content
                },
                success: function (data) {
                    if (data == true) {
                        alert("修改成功！");
                        getNewsList(1);
                    }
                    else {
                        alert("修改失败！");
                    }
                },
                error: function () {
                    alert("修改失败！");
                },
                complete: function () {
                    hideAlert();
                }
            });
        }
    });
}
/**
 * 删除新闻
 */
function deleteNews(target) {
    if (confirm("确定要删除该新闻吗？")) {
        $.ajax({
            url: '/myschool/delnews.do',
            type: 'post',
            dataType: 'json',
            data: {
                newsId: target.parent().attr('nid')
            },
            success: function (data) {
                if (data == true) {
                    alert("删除成功");
                    target.parent().remove();
                } else {
                    alert("删除失败");
                }
            },
            error: function () {
                alert("删除失败");
            }
        });
    }
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
 * 搜索新闻
 */
function searchNews() {
    getNewsList(1);
}
/**
 * 获取新闻列表
 * @param page
 */
function getNewsList(page) {
    //切换分页后，多选框全部清空
    $("[name = newsItem]:checkbox").attr("checked", false);
    $(".newsTitle").attr("checked", false);

    var targetContent = $('.news_manage_info .content_manage_info_top');
    var target = $('.news_manage_info .content_manage_info_middle');
    target.remove();
    var columnId = $(".newsColumnSearchItem").val();
    var title = $("#newsTitleSearch").val();

    $.ajax({
        url: '/myschool/newslist.do',
        type: 'post',
        dataType: 'json',
        data: {
            columnId: columnId,
            page: page,
            pageSize: 20,
            title: title
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
                var pinned = "";
                if (list[i].pinned == 1)
                    pinned = "【置顶】";
                var content = '';
                content += '<dd class="content_manage_info_middle" nid=' + list[i].id + '>' +
                '<input type="checkbox" name="newsItem">' +
                    //'<strong>1</strong>' +
                '<p><a onclick="previewNews(\'' + list[i].id + '\')"><span class="content_manage_info_sp">' + pinned + '</span>' + list[i].title + '</a></p>' +
                '<em>' + list[i].column + '</em>' +
                '<i>' + list[i].strDate + '</i>' +
                '<button class="news_manage_button">删除</button>' +
                '<button class="list-edit">编辑</button>' +
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
 * 获取所有的栏目信息，用于分类查询
 */
function getColumnListForSearch() {
    $.ajax({
        url: "/myschool/getColumnList.do",
        type: "post",
        success: function (data) {
            var target = $(".content_manage_br .newsColumnSearchItem");
            target.empty();
            var contentAll = "";
            contentAll += '<option value="-1">全部</option>';
            target.append(contentAll);
            for (var i = 0; i < data.length; i++) {
                var content = "";
                content += '<option value="' + data[i].id + '">' + data[i].columnName + '</option>';
                target.append(content);
            }
            //获取数据内容
            getNewsList(1);
        },
        error: function () {
            alert("error");
        }
    });
}
/**
 * 获取栏目信息
 */
function getColumnList() {
    $.ajax({
        url: "/myschool/getColumnList.do",
        type: "post",
        success: function (data) {
            var target = $(".content_manage_cg .choose_column");
            target.empty();
            for (var i = 0; i < data.length; i++) {
                var content = "";
                content += '<option value="' + data[i].id + '">' + data[i].columnName + '</option>';
                target.append(content);
            }
        },
        error: function () {
            alert("error");
        }
    });
}
/**
 * 获取栏目列表，附带选择项
 * @param selectedId
 */
function getColumnListWithSelected(selectedId) {
    $.ajax({
        url: "/myschool/getColumnList.do",
        type: "post",
        success: function (data) {
            var target = $(".content_manage_cg .choose_column");
            target.empty();
            for (var i = 0; i < data.length; i++) {
                var content = "";
                if (selectedId == data[i].id) {
                    content += '<option value="' + data[i].id + '" selected = "selected">' + data[i].columnName + '</option>';
                }
                else {
                    content += '<option value="' + data[i].id + '">' + data[i].columnName + '</option>';
                }
                target.append(content);
            }
        },
        error: function () {
            alert("error");
        }
    });
}
/**
 * 添加新闻
 */
function addNews() {

    clearForm();
    $(".new_publish_btn").off('click');
    $(".new_publish_btn").on('click', function () {
        var title = $.trim($(".content_title").val());
        var column = $(".choose_column").val();
        var pinned = $(".content_manage_in").is(':checked') == true ? 1 : 0;
        //var thumb = $.trim($("#img_preview")[0].src);
        var thumb = $.trim($("#img_preview").attr("src"));
        var digest = $.trim($(".content_manage_te").val());
        //var content= $.trim($("#ueditor").val());
        var content = UE.getEditor('ueditor').getContent();

        if(column == null)
        {
            alert("请先创建栏目");
            return;
        }
        if (title == "" || column == "" || content == "") {
            alert("请填写完整！");
        }
        else if (title.length > 60) {
            alert("新闻标题长度超过60字，请删减！");
        }
        else if (digest.length > 200) {
            alert("新闻描述长度超过200字，请删减!");
        }
        else if (pinned == 1 && thumb == "" && $(".choose_column").find("option:selected").text() == "新闻") {
            alert("置顶新闻必须上传图片!");
        }
        else {
            $.ajax({
                url: "/myschool/addnews.do",
                type: "post",
                async: false,
                dataType: "json",
                data: {
                    columnId: column,
                    title: title,
                    pinned: pinned,
                    thumb: thumb,
                    digest: digest,
                    content: content
                },
                success: function (data) {
                    if (data == true) {
                        alert("添加成功！");
                        getNewsList(1);
                    }
                    else {
                        alert("添加失败！");
                    }
                },
                error: function () {
                    alert("添加失败！");
                },
                complete: function () {
                    hideAlert();
                }
            });
        }
    });
}
function hideAlert() {
    $(".news_manage_bf").css("display", "none");
    $(".content_manage_cg").css("display", "none");
}


$(function () {
    $('.list-add-news').on('click', function () {
        window.location.href = '';
    });
    $('.news-list').show();
    $('.preview-wrapper').hide();
});
/**
 * 打开文件选择框
 */
function addImage() {
    var ie = navigator.appName == "Microsoft Internet Explorer" ? true : false;
    if (ie) {
        document.getElementById("file").click();
        document.getElementById("filename").value = document.getElementById("file").value;
    } else {
        var a = document.createEvent("MouseEvents");
        a.initEvent("click", true, true);
        document.getElementById("file").dispatchEvent(a);
    }
}

/**
 * 异步上传图片
 * @returns {boolean}
 */
function ajaxFileUpload() {
    $.ajaxFileUpload
    (
        {
            url: '/activity/uploadPic.do',
            secureuri: false,
            fileElementId: 'file',
            dataType: 'text',
            success: function (data, status)  //服务器成功响应处理函数
            {
                if (data != "500") {
                    var k = data.indexOf("<div");
                    if (k > 0) data = data.substring(0, k);
                    //jQuery("#add_image").hide();
                    jQuery("#upload_img,#replace_a").show();
                    jQuery("#upload_img").attr("src", data);
                }
                else {
                    alert("请正确上传活动图片");
                }
            },
            error: function (data, status, e)//服务器响应失败处理函数
            {
                alert("服务器繁忙，请稍后再试");
            }
        }
    );
    return false;
}

function previewNews(obj) {
    $.ajax({
        url: "/myschool/getNoticeContent.do",
        type: 'post',
        dataType: "json",
        data: {
            newsId: obj
        },
        success: function (data) {
            var content = data.content;
            var date = data.strDate;
            $("#content").append(content);
            $("#title").append(data.title);
            $("#newsdate").append(date);
            $(".preview-wrapper").show();
        },
        error: function () {
            console.log('selSchoolSubject error');
        }
    });
}
function closePreview() {
    $(".preview-wrapper").hide();
    $("#content").empty();
    $("#title").empty();
    $("#newsdate").empty();
}