var currentpage = 1;
var type = null;
$(function() {
	getWeikeList();
});

function getWeikeList() {
    $('#weike-container').addClass('hardloadingb');
    $.ajax({
	   url:'/lesson/voteLessons.do', 
	   type:'get',
   	   success:function(data){
	   		$('#weike-container').removeClass('hardloadingb');
	        var html ='';
	        for(var i = 0; i < data.length; i ++){
	        	html += '<dl class="weike-item" wid="'+ data[i].id +'" onmouseover="showplay(this)" onmouseout="hideplay(this)">' +
           				'<dt><img src="'+ data[i].imageUrl+'"/></dt>'+
           				'<dd class="weike-title">'+ data[i].courseName +'</dd>'+
				'<dd class="play" onclick="playTheMovie(\'' + data[i].videoUrl + '\')"><img src="/business/ypxx/special/ah/img/play_icon.png"/></dd>' +
           			'</dl>';
	        }
	        $('#weike-container').html(html);
	        
	        //resetPaginator(Math.ceil(data.total/data.pageSize));
   	   }
   });
}
var isFlash = false;

function playTheMovie(videoURL) {
	var $player_container = $("#intro-player");
	$player_container.fadeIn();
	try {
		SewisePlayer.toPlay(videoURL, "", 0, true);
	} catch (e) {
		playerReady.videoURL = videoURL;
		isFlash = true;
	}
}

function playerReady(name){
	if(isFlash){
		SewisePlayer.toPlay(playerReady.videoURL, "", 0, true);
	}
}

function closeTheMovie() {
	var $player_container = $("#intro-player");
	$player_container.fadeOut();
	try {
		SewisePlayer.doStop();
	} catch (e) {
	}

}

function showplay(obj){
	$(obj).find('.play').show();
	$(obj).find('dt').addClass('translayer');
}
function hideplay(obj){
	$(obj).find('.play').hide();
	$(obj).find('dt').removeClass('translayer');
}
