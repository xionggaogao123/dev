var currentpage = 1;
var type = null;
var lesson={"high":
	[{"courseName":"陈晓明 地理 地理信息技术在区域地理研究中的应用",
		"id":7633
	},
		{"courseName":"金红艺 地理 地球运动 第一讲",
			"id":7634
		},
		{"courseName":"林章和 地理 人口的空间变化",
			"id":7635
		},
		{"courseName":"后勇军 化学 化学实验专题复习四第一讲",
			"id":7636
		},
		{"courseName":"唐荣宏 化学 电解质溶液中离子浓度大小的比较",
			"id":7637
		},
		{"courseName":"王华龙 化学 有机化学高考复习七",
			"id":7638
		},
		{"courseName":"陶勇 生物 遗传系谱图",
			"id":7639
		},
		{"courseName":"王忠文 生物 生物育种方式",
			"id":7640
		},
		{"courseName":"俞仁凤 语文 第一讲 高考作文的特点",
			"id":7641
		},
		{"courseName":"詹克文 语文 第十讲 图文转换",
			"id":7642
		},
		{"courseName":"赵杰 语文 第一讲 一般论述类文章考点复习",
			"id":7643
		},
		{"courseName":"何蓉  政治 货币流通规律与纸币发行",
			"id":7644
		},
		{"courseName":"李丽 政治 文化对人的影响",
			"id":7645
		},
		{"courseName":"马丽娜 政治 我国政府的职能",
			"id":7646
		},
		{"courseName":"王亮 政治 矛盾的同一性和斗争性",
			"id":7647
		},
		{"courseName":"吴望民 政治 高考“四种能力”解说",
			"id":7648
		},
		{"courseName":"章健 政治 我国的国家性质",
			"id":7649
		},
		{"courseName":"Unit2",
			"id":7650
		}
	],
	"middle":
		[
//		 {"courseName":"初中数学解题秘籍——方法篇",
//			"id":7631
//		},
//			{"courseName":"二次函数概念、图象和性质（9年级）",
//				"id":7630
//			},
//			{"courseName":"先秦政治制度的演变",
//				"id":7626
//			}
			{"courseName":"中考冲刺复习-语文01",
				"id":11540
			},
			{"courseName":"中考冲刺复习-语文02",
				"id":11542
			},
			{"courseName":"中考冲刺复习-语文03",
				"id":11543
			},
			{"courseName":"中考冲刺复习-语文04",
				"id":11544
			},
			{"courseName":"中考冲刺复习-数学01",
				"id":11523
			},
			{"courseName":"中考冲刺复习-数学02",
				"id":11528
			},
			{"courseName":"中考冲刺复习-数学03",
				"id":11534
			},
			{"courseName":"中考冲刺复习-数学04",
				"id":11535
			},
			{"courseName":"中考冲刺复习-数学05",
				"id":11541
			},
			{"courseName":"中考冲刺复习-英语01",
				"id":11701
			},
			{"courseName":"中考冲刺复习-英语02",
				"id":11697
			},
			{"courseName":"中考冲刺复习-英语03",
				"id":11699
			},
			{"courseName":"中考冲刺复习-英语04",
				"id":11700
			},
			{"courseName":"中考冲刺复习-物理01",
				"id":11536
			},
			{"courseName":"中考冲刺复习-物理02",
				"id":11537
			},
			{"courseName":"中考冲刺复习-物理03",
				"id":11538
			},
			{"courseName":"中考冲刺复习-物理04",
				"id":11539
			},
			{"courseName":"中考冲刺复习-化学01",
				"id":11600
			},
			{"courseName":"中考冲刺复习-化学02",
				"id":11606
			},
			{"courseName":"中考冲刺复习-化学03",
				"id":11607
			},
			{"courseName":"中考冲刺复习-化学04",
				"id":11608
			},
			{"courseName":"中考冲刺复习-历史01",
				"id":11479
			},
			{"courseName":"中考冲刺复习-历史02",
				"id":11480
			},
			{"courseName":"中考冲刺复习-历史03",
				"id":11481
			},
			{"courseName":"中考冲刺复习-历史04",
				"id":11482
			},
			{"courseName":"中考冲刺复习-政治01",
				"id":11478
			},
			{"courseName":"中考冲刺复习-政治02",
				"id":11477
			},
			{"courseName":"中考冲刺复习-政治03",
				"id":11476
			},
			{"courseName":"中考冲刺复习-政治04",
				"id":11530
			}
			],
	"primary":
		[
			{"courseName":"分数计算之换元与缩放",
				"id":7610
			},
			{"courseName":"简单排列",
				"id":7611
			},
			{"courseName": "数一数和算一算",
				"id":7612
			},
			{"courseName":"速算与巧算之四则运算技巧提高",
				"id":7613
			}
		],

	"quality":[
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
	if(type == 'high'){
		$('#content-title').html('高中');
	}else if(type == 'middle'){
		$('#content-title').html('初中');
	}
	else if(type == 'primary'){
		$('#content-title').html('小学');
	}else if(type == 'quality'){
		$('#content-title').html('素质教育');
	}

	getWeikeList(type);
	
});


function getWeikeList(type) {
	$('#weike-container').addClass('hardloadingb');
	var lessonType=null;
	if(type=='high'){
		lessonType=lesson.high;
	}else if(type=='middle'){
		lessonType=lesson.middle;
	}else if(type=="primary"){
		lessonType=lesson.primary;
	}else {
		lessonType=lesson.quality;
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
					html += '<dl class="weike-item" onmouseover="showplay(this)" onmouseout="hideplay(this)">' +
					'<dt><img src="http://www.k6kt.com' + info.imageUrl + '"/></dt>' +
					'<dd class="weike-title">' + info.name + '</dd>' +
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
