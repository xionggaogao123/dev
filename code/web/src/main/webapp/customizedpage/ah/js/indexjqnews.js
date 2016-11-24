var currentpage = 1;
var type = null;
var lesson={"new1":
	[{"courseName":" 第一部分 取得的成绩（1）",
		"id":11762
	},
		{"courseName":"第一部分 取得的成绩（2）",
			"id":11763
		},
		{"courseName":"第二部分 存在的问题",
			"id":11764
		},
		{"courseName":"第三部分 工作要求（1）",
			"id":11767
		},
		{"courseName":"第三部分 工作要求（2）",
			"id":11766
		}
	],
	"new2":
		[
			{"courseName":"实践“翻转”教学，让创新成为态度（马鞍山22中 胡学平）",
				"id":11879
			},
			{"courseName":"“微课”创学习新载体 “翻转”领课堂新风向（亳州一中南校 张殿纪）",
				"id":11868
			},
			{"courseName":"且行且思 且思且行 砥砺向前 翻转课堂实践与反思（合肥市望龙学校 张和庆）",
				"id":11872
			},
			{"courseName":"淮南师范附小山南校区翻转课堂的实施（淮南师范附小 刘冬）",
				"id":11870
			},
			{"courseName":"公开课：数学 基本不等式（胡学平）",
				"id":11755
			},
			{"courseName":"公开课：化学 化学反应与能量（周美华）",
				"id":11847
			},
			{"courseName":"公开课：语文“新天下耳目”的东坡词（魏志军）",
				"id":11999
			},
			{"courseName":"公开课：语文 纪昌学射-上（宿松实验小学）",
				"id":11899
			},
			{"courseName":"公开课：语文 纪昌学射-下（宿松实验小学）",
				"id":11928
			}
			],
	"new3":
		[
			{
				"courseName": "基础教育慕课与翻转课堂（1）",
				"id": 11977
			},
			{
				"courseName": "基础教育慕课与翻转课堂（2）",
				"id": 11978
			},
			{
				"courseName": "基础教育慕课与翻转课堂（3）",
				"id": 11979
			},
			{
				"courseName": "基础教育慕课与翻转课堂（4）",
				"id": 11980
			}
		],

	"new4":[
		{
			"courseName": "摆的实验",
			"id": 7614
		},
		{
			"courseName": "反冲实验",
			"id": 7615
		},
		{
			"courseName": "静电实验",
			"id": 7616
		},
		{
			"courseName": "热胀冷缩",
			"id": 7617
		},
		{
			"courseName": "水的实验",
			"id": 7618
		}
	]
}

$(function() {
	type = window.location.href.split('=')[1];
	if(type == 'new1'){
		$('#content-title').html('“基于微课的翻转课堂项目试验的推进”重要讲话');
	}else if(type == 'new2'){
		$('#content-title').html('翻转课堂经验分享');
	}
	else if(type == 'new3'){
		$('#content-title').html('基础教育慕课与翻转课堂——C20慕课联盟学校的实践探索');
	}else if(type == 'new4'){
		$('#content-title').html('素质教育');
	}

	getWeikeList(type);
	
});


function getWeikeList(type) {
	$('#weike-container').addClass('hardloadingb');
	var lessonType=null;
	if(type=='new1'){
		lessonType=lesson.new1;
	}else if(type=='new2'){
		lessonType=lesson.new2;
	}else if(type=="new3"){
		lessonType=lesson.new3;
	}else {
		lessonType=lesson.new4;
	};

var videoIdArr=new Array();
	for(var i=0;i<lessonType.length;i++){
		videoIdArr.push(lessonType[i].id);
	}

	$.ajax(
		{
			url: '/video/list.do',
			type: 'post',
			data: {'videoIds':videoIdArr},
			traditional:true,
			success: function (data) {

				$('#weike-container').removeClass('hardloadingb');
				var html = '';
				for (var i = 0; i < videoIdArr.length; i++) {
					var id =videoIdArr[i];
					var info = data[id];
					if(info!=null){
					html += '<dl class="weike-item" onmouseover="showplay(this)" onmouseout="hideplay(this)" style="background-color: white;">' +
					'<dt><img src="http://www.k6kt.com' + info.imageUrl + '"/></dt>' +
					'<dd class="weike-title" style="color: black;">' + info.name + '</dd>' +
					'<dd class="play" onclick="playTheMovie(\'' + info.url + '\')"><img src="/business/ypxx/special/maanshan2/img/play_icon.png"/></dd>' +
					'</dl>';
					};
					continue;
				}
				$('#weike-container').html(html);
			}
		}
	)
}
	        
	        //resetPaginator(Math.ceil(data.total/data.pageSize));

var isFlash = false;
function playTheMovie(url) {
	var videoType = getVideoType(url);
	var $player_container = $("#intro-player");
	$player_container.fadeIn();
	switch (videoType) {
	case "POLYV":
	var player = polyvObject('#player_div').videoPlayer({
		'width': '800',
		'height': '450',
		'vid': url.match(/.+(?=\.swf)/)[0].replace(/.+\//, '')
	});
	 break;
	case "HLS":
	//	 $('#player_div').hide();
     //    $('#sewise-div').show();
         try {
             SewisePlayer.toPlay(url, "", 0, true);
         } catch (e) {
             playerReady.URL = url;
             isFlash = true;
         }
		break;
	}
	
}

function playerReady(name){
	if(isFlash){
		SewisePlayer.toPlay(playerReady.URL, "", 0, true);
	}
}

function getVideoType(url) {
	if (url.indexOf('polyv.net') > -1) {
        return "POLYV";
    }
    return 'HLS';
}

function closeTheMovie() {
	var $player_container = $("#intro-player");
	$player_container.fadeOut();
//	$('#player_div').empty();
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
