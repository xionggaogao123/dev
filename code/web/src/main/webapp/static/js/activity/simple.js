function go2search(){
    var url='/activity/friendSearch.do';
    var keywords=$("#keywords").val();
    keywords=encodeURI(encodeURI(keywords));
    url+='?keywords='+keywords;
    window.location.href=url;
}
function go2href(url){
    window.location.href=url;
}

//  制作分页div
    function fenye(reqPage,data,k){
        if(data.rows.length<1){
            return "";
        }
        var div='<div id="" style="float: left;margin-bottom: 20px;width: 760px;text-align: center;">';
        var htm='';
        var pageCount;
        if(data.total%10==0){
            pageCount=data.total/10;
        }else{
            pageCount=Math.floor(data.total/10)+1
        }
//        少于5页  全部显示  不显示首页 末页
        if(pageCount<5 ){
            htm+='<div id="foot-fenye" style="display:inline-block;"><ul id="" style="text-align: center">';
            for(var i=0;i<pageCount;i++){
                if((i+1)==reqPage){
                    htm += '<li ><a style="color: #999999;background-color: #F5F5F5" href="javascript:go2pageX(' + (i + 1) + ',' + k + ')" >' + (i + 1) + '</a></li>';
                }else {
                    htm += '<li><a href="javascript:go2pageX(' + (i + 1) + ',' + k + ')" >' + (i + 1) + '</a></li>';
                }
            }
            htm+='</ul>';
        }if(pageCount>=5){//大于5页
            //如果第一页  不显示首页   如果最后一页不显示 末页
            if(reqPage==1){
                htm+='<div id="foot-fenye" style="display:inline-block;"><ul id="" style="text-align: center">';
            }else {
                htm+='<div id="foot-fenye" style="display:inline-block;"><ul id="" style="text-align: center"><li><a href="javascript:go2pageX(1,'+k+')">首页</a></li>';
            }

            if(reqPage<=3){
                for(var i=1;i<reqPage+3;i++){
                    if(reqPage==i){
                        htm+='<li><a style="color: #999999;background-color: #F5F5F5" href="javascript:go2pageX('+i+','+k+')" >'+i+'</a></li>';
                    }else{
                        htm+='<li><a href="javascript:go2pageX('+i+','+k+')" >'+i+'</a></li>';
                    }
                }
                htm+='<li><a>&gt;</a></li>';
            }else if(pageCount-reqPage<=3){
                htm+='<li><a>&lt;</a></li>';
                for(var i=reqPage-3;i<=pageCount;i++){
                    if(reqPage==i){
                        htm+='<li><a style="color: #999999;background-color: #F5F5F5" href="javascript:go2pageX('+i+','+k+')" >'+i+'</a></li>';
                    }else {
                        htm+='<li><a href="javascript:go2pageX('+i+','+k+')" >'+i+'</a></li>';
                    }
                }

            }else {
                htm+='<li><a>&lt;</a></li>';
                for(var i=reqPage-2;i<reqPage+3;i++){
                    if(reqPage==i){
                        htm+='<li><a style="color: #999999;background-color: #F5F5F5"  href="javascript:go2pageX('+i+','+k+')" >'+i+'</a></li>';
                    }else {
                        htm+='<li><a href="javascript:go2pageX('+i+','+k+')" >'+i+'</a></li>';
                    }
                }
                htm+='<li><a>&gt;</a></li>';
            }
            if(reqPage==pageCount){
                htm+='</ul>';
            }else{
                htm+='<li><a href="javascript:go2pageX('+pageCount+','+k+')">末页'+pageCount+'</a></li>';
            }

        }
        htm+='</div>';
        div+=htm;
        div+='</div>';
        return div;


    }
    function go2pageX(page,k){
           if(k==1){
               fridendsActivity(page);
           }else if(k==2){
               recommendActivity(page);
           }else if(k==3){
               myJoinActivity(page);
           }else if(k==4){
                myPromoteActivity(page);
           }else if(k==5){
               actTrackList(page);
           }
    }
    //跳转活动详情页面
    function actDetail(actId){
        //window.location.href="/activity/view/"+actId;
    	window.location.href="/activity/view.do?actId="+actId;
    }
    function changeBackgroundImg(change2white){
        $('.title').removeAttr("style");
        $('#'+change2white).css("background-color","#ffffff");
        $('#'+change2white).css("border-top","2px solid #3D87C6");
        $('#'+change2white).css("border-top-right-radius","2px");
        $('#'+change2white).css("border-top-left-radius","2px");
        $("#"+change2white).css("color","#000000");
        //$("#right ").css("overflow","visible");
    }
//发起活动
    function launchActivity(){
        changeBackgroundImg("launchActivity");
        $("#content").empty();
        var html='';
        html+=' <div class="contentArea w">'+
        '<div class="contentArea_l">'+
        '<fieldset><h4>发起活动</h4></fieldset>'+
        '<fieldset> <div class="field"> <div class="icon"></div> ' +
        '<input type="text" name="nope" id="nope" maxlength="40" /> </div> ' +
        '</fieldset> ' +
        '<fieldset> <div class="date clearfix"> ' +
        '<div class="startDate "><input id="dt"class="input" value="开始时间：" readonly="true"/></div> ' +
        '<div class="endDate" style="position: relative;left: -105px;"><input id="ivi" class="input" value="结束时间：" readonly="true"/></div> </div> ' +
        '</fieldset> ' +
        '<fieldset> <input type=" text" class="address" value="地点：" id="address" /> </fieldset> ' +
        '<fieldset> <textarea id="defArea" class="explain">说明（选填）：</textarea> </fieldset> ' +
        '<fieldset> <div class="select-view"> ' +
        '<select id="pref_noapply" name="pref_noapply" class=""> ' +
        '<option value="PUBLIC" selected="selected">公开</option> ' +
        '<option value="INVITE_FRIEND">仅邀请的好友可见</option> ' +
        '<option value="FRIEND">好友可见</option> ' +
        '</select> </div> <div class="active-num"> <input type="text" value="人数：" id="num" /> </div> ' +
        '</fieldset> ' +
        '<fieldset> <div class="addCover"> <a id="add_image" href="javascript:addImage()">+添加封面</a> ' +
        '<img id="upload_img" src="" width="82" height="123" alt="" style="display:none;"/> ' +
        '<a id="replace_a" style="display:none;" href="javascript:addImage();" class="change">替换图片</a> ' +
        '</div> <input type="file" id="file" name="file" style="display:none;" onchange="ajaxFileUpload();"  accept="image/gif, image/jpeg, image/bmp,image/jpg" /> ' +
        '</fieldset> ' +
        '<fieldset> <div class="inviteFriend"><a href="javascript:loadFriends();">+邀请好友</a></div> </fieldset> ' +
        '<fieldset> <div class="operateBtn"> ' +
        '<a href="javascript:void(0);" class="cancel_btn">取消</a> ' +
        '<a href="javascript:addActivity();" class="sure_btn">确定</a> </div> ' +
        '</fieldset> ' +
        '</div> ' +
        '<div class="contentArea_r"> ' +
        '<h5 class="question"><em>Q：</em><span>什么是活动？</span></h5> ' +
        '<div class="answer" style="width: 200px;"> ' +
        '<em style="float: left">A：</em> ' +
        '<p>活动可以基于兴趣与学习产生的互动，比如找小伙伴一起打羽毛、找小伙伴一起学习等都可以算作一次活动。</p> ' +
        '</div> <h5 class="question"><em>Q：</em><span>可以利用活动做什么？</span></h5> ' +
        '<div class="answer" style="width: 200px;"> ' +
        '<em style="float: left">A：</em> ' +
        '<p>通过活动，您可以认识其他有相同兴趣和爱好的同学，并且' +
        '可以和同学们进行线下的聚会，同时可以锻炼发起人同学的活动组织以及协调能力。</p> ' +
        '</div> ' +
        '<h5 class="question"><em>Q：</em><span>我在发起活动的时候应该注意什么？</span></h5> <div class="answer" style="width: 200px;"> ' +
        '<em style="float: left">A：</em> <p>发起活动时，您需要告诉其他同学活动的时间、具体的地址、联系人等信息，除此之外，描述详细的活动内容会让同样感兴趣的同学注意到。</p> </div> </div> ' +
        '</div>';
        $("#content").append(html);
        initLaunchActivity();
        dateKit();
    }