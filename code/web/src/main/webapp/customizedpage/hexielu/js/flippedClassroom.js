/**
 * Created by qiangm on 2015/7/7.
 */
$(function(){
    //var schoolId=$("#hexieluSchoolId").val();
    var schoolId="安徽省阜阳市颍东区和谐路小学";
    getNews(schoolId);
    getNotice(schoolId);
});
//获取新闻
function getNews(schoolId)
{
    $.ajax(
        {
            url: "/myschool/getNoticeAndNews.do",
            type: "post",
            dataType: "json",
            data: {
                schoolId:schoolId,
                type: "新闻"
            },
            success: function (data) {
                var targetinfo=$(".newsleft-top-dl");
                targetinfo.empty();
                var targetbutton=$(".btnnum");
                targetbutton.empty();
                for(var i=0;i<data.length;i++)
                {
                    var infocontent="";
                    var buttoncontent="";
                    var digest="";//200字
                    if(data[i].digest!="")
                    {
                        digest=data[i].digest.substring(0,200);
                        digest+="...";
                    }
                    else{
                        digest=removeHTMLTag(data[i].content).substring(0,200);
                        digest+="...";
                    }
                    infocontent+='<dl >'+
                        '<dt><img src="'+data[i].thumb+'" /></dt>'+
                        '<dd>'+
                        '<h5><a href="/customizedpage/hexielu/news_hexielu.jsp?newsId='+data[i].id+'">'+data[i].title.substring(0,60)+'</a></h5>'+
                        '<p>'+digest+'</p>'+
                        '<a class="btn-more" href="/customizedpage/hexielu/hexielu.jsp?type=news"></a>'+
                        '</dd>'+
                        '</dl>';
                    targetinfo.append(infocontent);
                    buttoncontent+='<span class="btn'+(i+1)+'"></span>';
                    targetbutton.append(buttoncontent);
                }
                $(".btnnum span").on("click",function(){
                    $(".newsleft-top-dl dl").eq($(this).index()).show().siblings().hide();

                });
            },
            error: function () {

                alert("获取公告失败！");
            },
            complete: function () {

            }
        }
    );
}
/**
 * 过滤html标签
 * @param str
 * @returns {*}
 */
function removeHTMLTag(str) {
    str = str.replace(/<\/?[^>]*>/g,''); //去除HTML tag
    str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
    //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
    str=str.replace(/ /ig,'');//去掉
    return str;
}
//获取公告，五条
function getNotice(schoolId)
{
    $.ajax(
        {
            url: "/myschool/getNoticeAndNews.do",
            type: "post",
            dataType: "json",
            data: {
                schoolId:schoolId,
                type: "公告"
            },
            success: function (data) {
                var target=$(".hexielunews");
                target.empty();
                var contenthead='<dt><span>【公告】</span><a class="bg-more" href="/customizedpage/hexielu/hexielu.jsp?type=notice"></a></dt>';
                target.append(contenthead);
                for(var i=0;i<data.length;i++)
                {
                    var date=data[i].strDate.substring(0,10);
                    var infocontent="";
                    infocontent+='<dd>'+
                        '<a href="/customizedpage/hexielu/notice_hexielu.jsp?newsId='+data[i].id+'"><span>'+data[i].title+'</span><em>'+date+'</em></a>'+
                        '</dd>';
                    target.append(infocontent);
                }
            },
            error: function () {

                alert("获取新闻失败！");
            },
            complete: function () {

            }
        }
    );
}
