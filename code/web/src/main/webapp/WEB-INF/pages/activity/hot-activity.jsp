<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<script type="text/javascript">
    var i = 0;
    function getHotActivity() {
        i++;
        $.ajax({
            url: '/activity/hot.do?topN=' + i + '&id=' + Math.floor(Math.random() * ( 1000 + 1)),
            type: 'post',
            dataType: "json",
            success: function (result) {
                jQuery(".hotActive_list").empty();
                jQuery(result).each(function (i, v) {

                    var html = '<li>';
                    html += '<dl>';
                    html += '<dt><a target="_blank" href="/activity/view.do?actId=' + v.id + '"><img src="' + v.coverImage + '" width="80" height="80" alt="" /></a></dt>';
                    html += '<dd>';
                    html += '<div style="height:19px;width:120px;height:auto;overflow:hidden;margin-top: 5px;">';
                    html += '<h5 >' + v.name + '</h5>';
                    html += '</div>';
                    html += '<span>参与：<em class="join_num">' + v.memberCount + '</em></span>';
                    html += '<span>讨论：<em class="discuss_num">' + v.discuss + '</em>照片：<em class="picture_num">' + v.image + '</em></span>';
                    html += '<dd>';
                    html += '<dl></li>';
                    jQuery(".hotActive_list").append(html);
                });
            }
        });
    }

    $(function () {
        getHotActivity();
    });

</script>
<div class="hotActive" style="overflow: visible">
    <h4>
        <span>热门活动</span>
        <a href="javascript:getHotActivity();" id="refresh"><i id="refresh-icon"></i>换一换</a>
    </h4>
    <ul class="hotActive_list">

    </ul>
</div>