/**
 * Created by qiangm on 2015/7/8.
 */
$(document).ready(function(){
    var intervalID;
    var curLi;
    //var schoolId=$("#hexieluSchoolId").val();
    var schoolId="安徽省阜阳市第二十一中学";
   /* $(".middle_left li a").mouseover(function(){
        curLi=$(this);
        intervalID=setInterval(onMouseOver,250);//鼠标移入的时候有一定的延时才会切换到所在项，防止用户不经意的操作
    });
    function onMouseOver(){
        $(".middle_right_I").removeClass("middle_right_I");


        if($(".middle_left li a").index(curLi)==0)
        {
            $("#fuyang21_type").text("公告首页");
            getListByType(1,10,schoolId,"notice");
        }
        else if($(".middle_left li a").index(curLi)==1)
        {
            $("#fuyang21_type").text("新闻首页");
            getListByType(1,10,schoolId,"news");
        }
        $(".middle_right_II").eq($(".middle_left li a").index(curLi)).addClass("middle_right_I");
        $(".cur").removeClass("cur");
        curLi.addClass("cur");
    }
    $(".middle_left li a").mouseout(function(){
        clearInterval(intervalID);
    });
*/
   /* $(".nav li a").click(function(){//鼠标点击也可以切换
        clearInterval(intervalID);
        $(".middle_right_I").removeClass("middle_right_I");
        if($(".middle_left li a").index(curLi)==0)
        {
            $("#fuyang21_type").text("公告首页");
            getListByType(1,10,schoolId,"notice");
        }
        else if($(".middle_left li a").index(curLi)==1)
        {
            $("#fuyang21_type").text("新闻首页");
            getListByType(1,10,schoolId,"news");
        }
        $(".middle_right_II").eq($(".middle_left li a").index(curLi)).addClass("middle_right_I");
        $(".cur").removeClass("cur");
        curLi.addClass("cur");
    });*/

});
$(function(){
    var column=getQueryString("type");//notice或news
    var schoolId="安徽省阜阳市第二十一中学";
    if(column=="notice")
    {
        $("#fuyang21_type").text("公告首页");
    }
    else if(column=="news")
    {
        $("#fuyang21_type").text("新闻首页");
    }
    //var schoolId=$("#hexieluSchoolId").val();
    getListByType(1,10,schoolId,column);
});

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
function getListByType(page,pageSize,schoolId,type)
{
    var typeName="";
    if(type=="notice")
    {
        $(".middle_left li a").eq(0).addClass("cur");
        $(".middle_left li a").eq(1).removeClass("cur");
        typeName="公告";
    }
    else if(type=="news")
    {
        $(".middle_left li a").eq(0).removeClass("cur");
        $(".middle_left li a").eq(1).addClass("cur");
        typeName="新闻";
    }
    $.ajax(
        {
            url: "/myschool/getNoticeAndNewsList.do",
            type: "post",
            dataType: "json",
            data: {
                page:page,
                pageSize:pageSize,
                schoolId:schoolId,
                type: typeName
            },
            success: function (data) {

                var option = {
                    total: data.total,
                    pagesize: data.pageSize,
                    currentpage: data.page,
                    type:type,
                    operate: function (totalPage) {
                        $('.page-index span').each(function () {
                            $(this).attr('onclick', 'getListByType(' + $(this).text() + ','+pageSize+',\''+',\''+schoolId+'\','+'\''+type+'\');');
                        });
                        $('.first-page').attr('onclick', 'getListByType(1,'+pageSize+',\''+ ','+pageSize+',\''+schoolId+'\','+'\''+type+'\');');
                        $('.last-page').attr('onclick', 'getListByType(' + totalPage + ','+pageSize+',\''+schoolId+'\','+'\''+type+'\');');
                    }
                }

                var target_recomand=$(".recomand_notice");
                var target_recent=$(".recent_notice");
                target_recomand.empty();
                target_recent.empty();
                var infocontent1="";


                infocontent1+='<dt>'+
                    '<em>推荐'+typeName+'</em>'+
                    '</dt>';
                target_recomand.append(infocontent1);
                for(var i=0;i<data.pinned.length;i++)
                {
                    var content='<span class="content_manage_info_sp">【置顶】</span>'+data.pinned[i].title;
                    var infocontent="";
                    infocontent+='<dd>'+
                        '<a href="/customizedpage/fuyang21/'+type+'_fuyang21.jsp?newsId='+data.pinned[i].id+'">'+content+'</a>'+
                        '<i>'+data.pinned[i].strDate+'</i>'+
                        '</dd>';
                    target_recomand.append(infocontent);

                }
                var infocontent1="";
                infocontent1+='<dt>'+
                 '<em>最新'+typeName+'</em>'+
                '</dt>';
                target_recent.append(infocontent1);
                for(var i=0;i<data.notPinned.length;i++)
                {
                    var content=data.notPinned[i].title;
                    if(data.notPinned[i].pinned==1)
                    {
                        content='<span class="content_manage_info_sp">【置顶】</span>'+content;
                    }
                    var infocontent="";
                    infocontent+='<dd>'+
                    '<a href="/customizedpage/fuyang21/'+type+'_fuyang21.jsp?newsId='+data.notPinned[i].id+'">'+content+'</a>'+
                    '<i>'+data.notPinned[i].strDate+'</i>'+
                    '</dd>';
                    target_recent.append(infocontent);

                }
                initPaginator(option);
            },
            error: function () {

                alert("获取失败！");
            },
            complete: function () {

            }
        }
    );
}
// 分页初始化
function initPaginator(option) {
    var totalPage = '';
    if (option.total == 0)
        $('.notice-page-paginator').hide();
    else
        $('.notice-page-paginator').show();
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