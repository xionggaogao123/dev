//    我参加的活动
    function myJoinActivity(reqPage,userId){
        changeBackgroundImg("myJoinActivity");
        var page;
        if(reqPage==null || reqPage==undefined || reqPage<1){
            page=1;
        }else{
            page=reqPage;
        }
        $("#content").empty();
        var html=' <div id="center_right_title_text"> '
        $.ajax({
            url: "/activity/myAttendActivity.do",
            type: "post",
            dataType: "json",
            data: {
                page:page,
                userId:userId,
                pageSize:10
            },
            success: function (data) {
                if(data!=null || data!=undefined){
                    for(var i=0;i<data.rows.length;i++){
                        if(data.rows[i].memberCount==null){
                            data.rows[i].memberCount=0;
                        }
                        var htm='<div id="friend_content1" style="float: left;margin-bottom: 20px;overflow:visible;" flag="'+data.rows[i].id+'">' +
                                '<img class="image1" onclick="actDetail(\''+data.rows[i].id+'\')" src="'+data.rows[i].coverImage+'"  style="width: 174px;height: 135px;" />' +
                                '<div style="float: right;position:relative;left:-245px;overflow: visible;"><a style="color: #000000;overflow: hidden;height: 20px;width: 11em;display: inline-block" href="javascript:actDetail(\''+data.rows[i].id+'\')" ><span id="text_1">'+data.rows[i].name+'</span></a>' +
                                '<span class="text_3">&nbsp;&nbsp; &nbsp;发起人：'+data.rows[i].organizerName+'</span>'+ '<br><br>'+
                                '<span class="text_1">时间：'+data.rows[i].strEventStartDate+'-'+data.rows[i].strEventStartDate+'</span><br>'+
                                '<span class="text_1" style="overflow: hidden;height: 15px;width: 15em;display: inline-block;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow:hidden" >地点：'+data.rows[i].location+'</span><br>'+
                                '<span class="text_1" style="overflow: hidden;height: 15px;width: 15em;display: inline-block;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow:hidden">说明：'+data.rows[i].description+'</span><br><br>'+
                                '<span class="text_1"><a class="A" href="">'+data.rows[i].memberCount+'</a>人参加&nbsp;&nbsp;</span>'+
                                '<span class="text_1"><a class="A" href="">'+data.rows[i].discuss+'</a>个讨论&nbsp;&nbsp;</span>'+
                                '<span class="text_1"><a class="A" href="">'+data.rows[i].image+'</a>张图片&nbsp;&nbsp;</span>' +
                                '<a class="tui" href="javascript:quitActivity('+data.rows[i].id+')"><span style="float: right">退出</span></a></div>' +
                                '</div>';
                        html+=htm;
                    }
                    html+=fenye(page,data,3);//3表示我参加的活动
                    html+='</div>';
                }else{
                }
                $("#content").append(html);
            }
        });
    }