//动态
    function actTrackList(reqPage,userId){
        changeBackgroundImg("actTrackList");
        var page;
        if(reqPage==null || reqPage==undefined || reqPage<1){
            page=1;
        }else{
            page=reqPage;
        }
        $("#content").empty();
        var url="/activity/actTrackList.do";
        $.ajax({
            url: url,
            type: "post",
            dataType: "json",
            data: {
                page:page,
                userId:userId,
                pageSize:10
            },
            success: function (data) {
                if(data!=null || data!=undefined){
                    var k='';
                    var html=k+' <div id="center_right_title_text"> ';
                    for(var i=0;i<data.rows.length;i++){
                        var acttrack=data.rows[i];
                        var ht='';
                        if(acttrack.type=="PROMOTE"){
                            if(acttrack.activity.memberCount==null){
                                acttrack.activity.memberCount=0;
                            }
                            ht+='<div id="center_right_title_text" style="overflow:visible;margin-left:15px;">'+
                            '<div id="text"><img src="'+acttrack.userImgUrl+'" style="height: 68px;width: 68px;" /></div>'+
                            '<div id="I"><span id="II">'+acttrack.userName+'</span><br/><span id="III">'+acttrack.userName+' 发起了新活动</span></div>'+
                            '<div id="picture" style="overflow:visible;"><img onclick="actDetail(\''+acttrack.activity.id+'\')" src="'+acttrack.activity.coverImage+'" style="width: 174px;height: 125px;"/>'+
                            '<a href="javascript:actDetail(\''+acttrack.activity.id+'\')"><span id="text_1" style="overflow: hidden;width: 10em;height: 20px;display: inline-block;">'+
                            acttrack.activity.name+'</span></a><br><br>'+
                            '<span class="text_1">时间：'+acttrack.activity.strEventStartDate+'至<br>'+acttrack.activity.strEventEndDate+'</span><br/><br/>'+
                                '<span class="text_1"style="width: 15em;overflow:hidden;height:15px;display: inline-block;white-space:nowrap;' +
                            'text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow:hidden">地点：'+acttrack.activity.location+'</span><br/>'+
                                '<span class="text_1" style="width: 15em;overflow:hidden;height:15px;display: inline-block;white-space:nowrap;' +
                            'text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow:hidden">说明：'+acttrack.activity.description+'</span><br/>'+
                                '<span class="text_1"><a >'+acttrack.activity.memberCount+'</a> 人参加&nbsp;&nbsp;</span>'+
                                '<span class="text_1"><a>'+acttrack.activity.discuss+'</a> 个讨论&nbsp;&nbsp;</span>'+
                                '<span class="text_1"><a>'+acttrack.activity.image+'</a> 张图片&nbsp;&nbsp;</span>'+
                                '</div><div style="clear: both;"></div><div id="links"><div id="links_left"><span>';
                            if(acttrack.fromDevice=="FromPC"){
                                ht+='来自PC端';
                            }else {
                                ht+'来自移动端';
                            }
                            ht+=acttrack.timeMsg+'</span></div>';
                            ht+='<div id="links_right"><a href="javascript:actDetail(\''+acttrack.activity.id+'\')"><span >活动详情</span></a></div>'+
                                '</div><div style="clear: both "></div><hr> </div>';
                        }else if(acttrack.type=="FRIEND"){

                            ht+='<div id="center_right_title_text" style="overflow:visible;margin-left:15px;"><div id="text">'+
                            '<img src="'+acttrack.userImgUrl+'" style="height: 68px;width: 68px;" />'+
                            '</div><div id="I"><span id="II">'+acttrack.userName+'</span><br/>'+
                            '<span id="III">'+acttrack.userName+' 与 '+acttrack.relateUserName+' 成为好友</span>'+
                            '</div><div style="clear: both;"></div><div id="links"><div id="links_left"><span>';
                            if(acttrack.fromDevice=="FromPC"){
                                ht+='来自PC端';
                            }else {
                                ht+'来自移动端';
                            }
                            ht+=acttrack.timeMsg+'</span></div></div><div style="clear: both "></div> <hr></div>';

                        }else if(acttrack.type=="ATTEND"){
                            ht+='<div id="center_right_title_text" style="overflow:visible;margin-left:15px;"><div id="text"><img src="'+acttrack.userImgUrl+'" style="height: 68px;width: 68px;" /></div>'+
                            '<div id="I"><span id="II">'+acttrack.userName+'</span><br/><span id="III">'+acttrack.userName+' 参加了 '+acttrack.activity.name+'活动 </span>'+
                            '</div><div style="clear: both;"></div><div id="links"><div id="links_left"><span>';
                            if(acttrack.fromDevice=="FromPC"){
                                ht+='来自PC端';
                            }else {
                                ht+'来自移动端';
                            }
                            ht+=acttrack.timeMsg+'</span> </div></div><div style="clear: both "></div> <hr> </div>';
                        }else if(acttrack.type=="REPLY"){
                            if(acttrack.activity.memberCount==null){
                                acttrack.activity.memberCount=0;
                            }
                            ht+='<div id="center_right_title_text" style="overflow:visible;margin-left:15px;">'+
                            '<div id="text"><img src="'+acttrack.userImgUrl+'" style="height: 68px;width: 68px;" /></div>'+
                            '<div id="I"><span id="II">'+acttrack.userName+'</span><br/><span id="III">'+acttrack.userName+' 回复了 ' +
                            '<a style="color:blue;" href="javascript:actDetail(\''+acttrack.activity.id+'\')">'+acttrack.activity.name+'</a></span></div>'+
                            '<div id="picture" style="overflow:visible;"><img onclick="actDetail(\''+acttrack.activity.id+'\')" src="'+acttrack.activity.coverImage+'" style="width: 174px;height: 125px;"/>'+
                            '<a href="javascript:actDetail(\''+acttrack.activity.id+'\')"><span id="text_1">'+acttrack.activity.name+'</span></a><br><br>'+
                            '<span class="text_1">时间：'+acttrack.activity.strEventStartDate+'至<br>'+acttrack.activity.strEventEndDate+'</span><br/><br/>'+
                            '<span class="text_1" style="overflow:hidden;width:15em;height:15px;display: inline-block;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow:hidden">地点：'+acttrack.activity.location+'</span><br/>'+
                            '<span class="text_1" style="overflow:hidden;width:15em;height:15px;display: inline-block;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow:hidden">说明：'+acttrack.activity.description+'</span><br/>'+
                            '<span class="text_1"><a href="">'+acttrack.activity.memberCount+'</a> 人参加&nbsp;&nbsp;</span>'+
                            '<span class="text_1"><a href="">'+acttrack.activity.discuss+'</a> 个讨论&nbsp;&nbsp;</span>'+
                            '<span class="text_1"><a href="">'+acttrack.activity.image+'</a> 张图片&nbsp;&nbsp;</span>'+
                            '</div><div style="clear: both;"></div><div id="links"><div id="links_left"><span>';
                            if(acttrack.fromDevice=="FromPC"){
                                ht+='来自PC端';
                            }else {
                                ht+'来自移动端';
                            }
                            ht+=acttrack.timeMsg+'</span></div>';
                            ht+='<div id="links_right"><a href="javascript:actDetail(\''+acttrack.activity.id+'\')"><span >活动详情</span></a></div>'+
                            '</div><div style="clear: both "></div><hr> </div>';
                        }
                        html+=ht;
                    }
                    html+=fenye(page,data,5);//5表示动态
                    html+='</div>';
                }else{ }
                $("#content").append(html);
            }
        });
    }