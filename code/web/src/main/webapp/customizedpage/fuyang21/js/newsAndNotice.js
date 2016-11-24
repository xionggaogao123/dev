/**
 * Created by qiangm on 2015/7/8.
 */
$(function(){
    var newsId=getQueryString("newsId");
    //var recomand=getQueryString("recomand");
    getContentById(newsId);
});

function getContentById(newsId)
{
    $.ajax({
        url: "/myschool/getNoticeContent.do",
        type: "post",
        dataType: "json",
        data: {
            newsId: newsId
        },
        success: function (data) {
            var content="";
            var target=$(".news_line .news_right");
            target.empty();
            content+='<div class="news_main_title">'+data.title+'</div>'+
                '<div class="news_main_riqi">'+
                '<em>'+data.strDate+'</em>&nbsp;&nbsp;&nbsp;&nbsp;<em>阅读'+data.readCount+'人</em>'+
                '</div>'+
                '<div class="news_main_info">';
                /*if(data.thumb!="")
                {
                    content+='<img style="width:400px;height:410px" src="'+data.thumb+'" >';
                }*/
                content+='</div>'+
                '<div class="news_main_bottom">'+
                '<p>'+data.content+'</p>'+
                '</div>'+
                '<div class="news_main_f">'+
                /*'<a href="/customizedpage/fuyang21/flippedClassroom.jsp">< 返回首页</a>'+*/
                '<i onclick="switchNews(\''+data.id+'\',\''+data.column+'\',1)">下一条></i>' +
                '<i onclick="switchNews(\''+data.id+'\',\''+data.column+'\',0)">< 上一条&nbsp;&nbsp;&nbsp; </i>'+
                '</div>';
            target.append(content);

        },
        error: function () {
            alert("获取公告失败！");
        },
        complete: function () {

        }
    });
}
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
/**
 * 切换上一页下一页
 * @param newsId 当前新闻ID
 * @param columnId 栏目ID
 * @param recomand 是否是推荐的
 * @param type 上一页0 下一页1
 */
function switchNews(newsId,columnId,type)
{
    $.ajax({
        url: "/myschool/getSwitchContent.do",
        type: "post",
        dataType: "json",
        data: {
            newsId: newsId,
            columnId:columnId,
            type:type
        },
        success: function (data) {
            if(data.title==null||data.title==undefined)
            {
                alert("没有了");
                return;
            }
            var content="";
            var target=$(".news_line .news_right");
            target.empty();

            content+='<div class="news_main_title">'+data.title+'</div>'+
            '<div class="news_main_riqi">'+
            '<em>'+data.strDate+'</em>&nbsp;&nbsp;&nbsp;&nbsp;<em>阅读'+(data.readCount+1)+'人</em>'+
            '</div>'+
            '<div class="news_main_info">';
            if(data.thumb!="")
            {
                content+='<img style="width:400px;height:410px" src="'+data.thumb+'" >';
            }

            content+='</div>'+
            '<div class="news_main_bottom">'+
            '<p>'+data.content+'</p>'+
            '</div>'+
            '<div class="news_main_f">'+
            /*'<a href="/customizedpage/fuyang21/flippedClassroom.jsp">< 返回首页</a>'+*/
            '<i onclick="switchNews(\''+data.id+'\',\''+data.column+'\',1)">下一条></i>' +
            '<i onclick="switchNews(\''+data.id+'\',\''+data.column+'\',0)">< 上一条&nbsp;&nbsp;&nbsp; </i>'+
            '</div>';
            target.append(content);

        },
        error: function () {

            alert("获取公告失败！");
        },
        complete: function () {

        }
    });
}