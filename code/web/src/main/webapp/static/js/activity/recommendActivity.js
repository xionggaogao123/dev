//    推荐活动
    function recommendActivity(reqPage,userId){
        changeBackgroundImg("recommendActivity");
        var page;
        if(reqPage==null || reqPage==undefined || reqPage<1){
            page=1;
        }else{
            page=reqPage;
        }
        $("#content").empty();
        var html=' <div id="center_right_title_text"> '
        $.ajax({
            url: "/activity/recommendActivity.do",
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
                        var isfriend=data.rows[i].isFriend;
                        var htm='<div id="friend_content1" style="float: left;margin-bottom: 20px;overflow: visible;">' +
                                '<img class="image1" onclick="actDetail(\''+data.rows[i].id+'\')" src="'+data.rows[i].coverImage+'"  style="width: 174px;height: 135px;" />' +
                                '<div style="float: right;position: relative;left: -65px;overflow: visible;"><a style="color: #000000;position: relative;left:-180px;overflow: hidden;height: 20px;'+
								'width: 11em;display: inline-block" href="javascript:actDetail(\''+data.rows[i].id+'\')" ><span id="text_1" >'+data.rows[i].name+'</span></a>'+
                                '<span class="text_3" style="position: relative;right: 0" >发起人：'+data.rows[i].organizerName+'</span>'+
                                '<a href="javascript:addFriend(\''+data.rows[i].organizer+'\')"><span class="I">' ;
                                if(!isfriend){
                                    htm+='<img  title="加为好友" src="../img/activity/jia.png">';
                                }
                                htm+='</span></a><br><br>'+
                                '<span class="text_1" style="position: relative;left: -180px;">时间：'+data.rows[i].strEventStartDate+'-'+data.rows[i].strEventStartDate+'</span><br>'+
                                '<span class="text_1" style="position: relative;left: -180px;overflow: hidden;height: 15px;width: 15em;display: inline-block;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow:hidden">地点：'+data.rows[i].location+'</span><br>'+
                                '<span class="text_1" style="position: relative;left: -180px;overflow: hidden;height: 15px;width: 15em;display: inline-block;white-space:nowrap;text-overflow:ellipsis;-o-text-overflow:ellipsis;overflow:hidden">说明：'+data.rows[i].description+'</span><br><br>'+
                                '<span class="text_1" style="position: relative;left: -180px;"><a class="A" href="" > '+data.rows[i].memberCount+'</a>人参加&nbsp;&nbsp;</span>'+
                                '<span class="text_1" style="position: relative;left: -180px;"><a class="A" href="" > '+data.rows[i].discuss+'</a>个讨论&nbsp;&nbsp;</span>'+
                                '<span class="text_1" style="position: relative;left: -180px;"><a class="A" href="" > '+data.rows[i].image+'</a>张图片&nbsp;&nbsp;</span></div>' +
                                '</div>';
                        html+=htm;
                    }
                    var fy=fenye(page,data,2);//2表示推荐活动请求
                    html+=fy;
                    html+='</div>';

                }else{
                }
                $("#content").append(html);
            }
        });
        getPetImage();
        lxfScroll(".lxfscroll",".lxfscroll-title li",".lxfscroll-alt",2000);
    }