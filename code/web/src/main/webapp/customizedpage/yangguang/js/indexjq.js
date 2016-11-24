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
function playTheMovie(url) {
	var $player_container = $("#intro-player");
	$player_container.fadeIn();
	var player = polyvObject('#player_div').videoPlayer({
		'width': '800',
		'height': '450',
		'vid': url.match(/.+(?=\.swf)/)[0].replace(/.+\//, '')
	});
}

function closeMovie() {
	var $player_container = $("#intro-player");
	$player_container.fadeOut();
	$('#player_div').empty();

}

function showplay(obj){
	$(obj).find('.play').show();
	$(obj).find('dt').addClass('translayer');
}
function hideplay(obj){
	$(obj).find('.play').hide();
	$(obj).find('dt').removeClass('translayer');
}
