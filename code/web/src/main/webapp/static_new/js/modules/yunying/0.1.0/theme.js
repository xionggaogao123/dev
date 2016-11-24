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
        $("#columnDir").val("");
        $("#connent").val("");
        $("#columnName2").val("");
        $("#columnDir2").val("");
        $("#connent2").val("");
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
/**
 * 获取栏目列表
 */
function getColumnList() {
    //currentPage=page;
    //切换分页后，多选框全部清空
    $("[name = newsColumnItem]:checkbox").attr("checked", false);
    $(".newsColum_chooseAll").attr("checked", false);
    var targetContent = $('.news_manage_info_top');
    var target = $('.news_manage_info .news_manage_info_middle');
    target.remove();
    $.ajax({
        url: '/yunying/selTheme.do',
        type: 'post',
        dataType: 'json',
        data: {
        },
        success: function (data) {
            target.remove();
            var list = data.rows;
            for (var i = list.length-1; i >=0; i--) {
                var content = '';
                var but = '';
                if (list[i].delflg==0) {
                    but = "<button class='list-muzzled' style='margin-left: 120px;'>删除</button>";
                } else {
                    but = "<button style='margin-left: 120px;background: yellow;' disabled>已下架</button>";
                }
                content += '<dd class="news_manage_info_middle"  id='+list[i].id+'>' +
                '<em class="list-username" style="cursor: pointer;">' +
                list[i].themedsc +
                '</em>' +
                //'<button class="list-edit" onclick="editNewsColumn(' + list[i].id.toString() + ',' + $(this) + ')">编辑EXP</button>' +
                //'<button class="list-delete news_manage_button" onclick="editNewsColumn(' + list[i].id.toString() + ',' + $(this) + ')" class="news_manage_button">编辑余额</button>' +
                but +
                '</dd>';
                targetContent.after(content);
            }

            $(".list-muzzled").on('click', function () {
                var dom = $(this);
                $.ajax({
                    url: '/homeschool/deleteTheme.do',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        id:dom.parent().attr('id')
                    },
                    success: function (data) {
                        alert("删除成功！");
                        getColumnList();
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
    $("#columnName").val('');
    $(".edit-commit-btn").off('click');
    $(".edit-commit-btn").on('click', function () {
        if ($("#columnName").val() == "") {
            alert("请输入话题！");
            return;
        }
        else {
            $.ajax({
                url: "/homeschool/addTheme.do",
                type: "post",
                dataType: "json",
                data: {
                    theme: $("#columnName").val()
                },
                success: function (data) {
                    if (data.rows) {
                        alert("添加成功！");
                        getColumnList();
                        hideAlert();
                    }
                    else{
                        alert("修改失败");
                    }
                },
                error: function () {

                    alert("修改失败！");
                }

            });
        }
    });
}
function hideAlert() {
    $(".news_manage_bf").css("display", "none");
    $(".news_manage_cg").css("display", "none");

}

