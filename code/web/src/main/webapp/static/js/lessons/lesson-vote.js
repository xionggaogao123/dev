/**
 * Created by Niu Xin on 14-10-14.
 */

function submitScore() {
    var score = $('#vote-score').val();
    score = parseInt(score);
    if (score >= 0 && score <= 100) {
        $('.vote-section.new-vote .submit-score').fadeOut('fast');
        $('#vote-score').attr('readonly', 'readonly');
        $.ajax({
            url: "/vote/vote.do",
            data: {'lessonId': courseid, 'score': score},
            type: "post"
        });
    } else {
        MessageBox("请输入正确的分值。", -1);
    }
}

function showScore(data) {
    if (data.avgScore) {
        $('#avg-score').val(data.avgScore.toFixed(2));
    } else {
        $('#avg-score').val('评分人数不足').css('width', '80');
        $('.vote-result').contents().eq(3).remove();
    }
}

function showNewScore() {
    $('.vote-section.new-vote .submit-score').show();
    $('#vote-score').removeAttr('readonly');
}

$(function () {
    if ($('.vote-section.vote-result').length) {
        //平均分
        $.getJSON('/vote/avgScore.do', {'lessonId': courseid}, function (data) {
            showScore(data);
        });
    }
    if ($('.vote-section.new-vote').length) {
        //评分区
        $.getJSON('/vote/checkVote.do', {'lessonId': courseid}, function (response) {
            if (response.isVoteLesson) {
                if (response.voted) {
                    $('#vote-score').val(response.score);
                } else {
                    showNewScore();
                }
            } else {
                $('#vote-section').remove();
            }
        });
    }
});