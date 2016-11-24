/**
 * 
 */


$(document).ready(function(){
	myErrorItem();
});

var itemDetail = {}; //题目详情
function changeContent(tag) {
	jQuery(".current1").removeClass("current1");
	jQuery("#" + tag).addClass("current1");
	if ("tiku_li" == tag) {
		jQuery("#Main").show();
		jQuery("#error_div,#TU1,#det_error_div").hide();
	} else {
		jQuery("#Main,#TU1,#det_error_div").hide();
		jQuery("#error_div").show();
		myErrorItem();
	}
}

function myErrorItem() {
	$.ajax({
		url : '/itempool/subject/count.do',
		type : 'get',
		contentType : 'application/json',
		success : function(res) {
			if (res.length > 0) {
				for (var i = 0; i < res.length; i++) {
					try {
						jQuery("#e_subject_" + res[i].id).text(res[i].value);
					} catch (x) {
					}
				}
			}
		}
	});

}

function showErrorItem(sub) {
	var count = parseInt(jQuery("#e_subject_" + sub).text());
	if (count <= 0) {
		return;
	}
	itemDetail.sub = sub;
	itemDetail.direction = -1;
	$.ajax({
		url : '/itempool/erritem/scope.do?sub=' + sub,
		type : 'get',
		contentType : 'application/json',
		success : function(res) {
			jQuery("#error_kw").empty();
			if (res.length > 0) {
				for (var i = 0; i < res.length; i++) {
					var html = "<span id=\"err_" + res[i].idStr + "\">"
							+ res[i].value + "</span>";
					jQuery("#error_kw").append(html);
				}
			}
		}
	});
	try {
		loadItemDetail(1);
	} catch (x) {
	}

	jQuery("#Main,#TU1,#error_div").hide();
	jQuery("#det_error_div").show();
}

function loadItemDetail(order) {

	jQuery(".student_information_new_info_S").removeClass(
			"student_information_new_info_S").addClass(
			"student_information_new_info_SS");
	jQuery("#error_sort_" + order).removeClass(
			"student_information_new_info_SS").addClass(
			"student_information_new_info_S");

	itemDetail.order = order;//默认乱序
	$.ajax({
		url : '/itempool/erritem/detail.do?order=' + order + "&subject="
				+ itemDetail.sub,
		type : 'get',
		contentType : 'application/json',
		success : function(res) {
			itemDetail.itemId = res.id;
			bindData(res, true);
		}
	});
}

function nextItemDetail(direction) {
	jQuery('#answer_div').hide();
	$.ajax({
		url : '/itempool/erritem/detail.do?order=' + itemDetail.order
				+ "&itemId=" + itemDetail.itemId + "&direction=" + direction
				+ "&subject=" + itemDetail.sub,
		type : 'get',
		contentType : 'application/json',
		success : function(res) {
			itemDetail.itemId = res.id;
			bindData(res, false);
		}
	});
}

function deleteItem() {
	$.ajax({
		url : '/itempool/erritem/delete.do?item=' + itemDetail.itemId,
		type : 'get',
		contentType : 'application/json',
		success : function(res) {
			if (res.code == '200') {
				var cur = parseInt(jQuery("#error_count_current").text());
				var total = parseInt(jQuery("#error_c_total").text());
				if (cur != total) {
					jQuery(".error_count_total").text(total - 1);
					jQuery("#error_count_current").text(cur - 1);
					nextItemDetail(1);
				} else {
					loadItemDetail(itemDetail.order);
				}
			}
		}
	});
}

function bindData(res, isf) {
	try {
		if (isf) {
			jQuery(".error_count_total").text(res.totalCount);
		}

		if (!isf) {
			var total = parseInt(jQuery("#error_c_total").text());
			var current = parseInt(jQuery("#error_count_current").text());

			if (total > current) {
				jQuery("#error_count_current").text(current + 1);
			}
		} else {
			jQuery("#error_count_current").text(1);
		}

		jQuery("#error_count").text(res.count);
		jQuery("#error_time").text(res.time);
		jQuery("#error_level").text(res.level);
		jQuery("#error_type").text(res.itemType);
		jQuery("#error_kwp").text(res.kw);
		if (itemDetail.kwid) {
			jQuery("#err_" + itemDetail.kwid).find("span").remove();
		}
		jQuery("#err_" + res.kwId).prepend("<span>√</span>");
		jQuery("#error_scope").text(res.score);
		jQuery("#answer").html(res.answer);
		jQuery("#tigan").html(res.item);

		jQuery("#myAnswer").html("我的答案：<span>" + res.myAnswer + "</span>");
		itemDetail.kwid = res.kwId;

	} catch (x) {

	}
}