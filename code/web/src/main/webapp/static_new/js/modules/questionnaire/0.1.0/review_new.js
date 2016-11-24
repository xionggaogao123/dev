/**
 * Created by fl on 2015/12/1.
 */
define(['common'],function(require,exports,module){
    var Common = require('common');
    require('jquery');

    (function(){
        var quesId = sessionStorage.getItem('quesId');
        var userId = sessionStorage.getItem('userId');
        var date = new Date();
        Common.getData('/questionnaire/view.do?time='+date.getTime(),{id:quesId},function(resp){

            $('#name').text(resp.name);
            $('#name').prop('title',resp.name);

            var response= resp.docUrl;
            if(!!window.ActiveXObject || "ActiveXObject" in window){
                if(response.substr(response.length-3) == 'pdf') {
                    previewSwf(response.substr(0,response.length-3) + 'swf');
                    $('iframe').hide();
                    $('#showPdf').show();
                } else {
                    $('iframe').show();
                    $('#showPdf').hide();
                    $('iframe').prop('src', resp.docUrl);
                }

            } else {
                $('iframe').show();
                $('#showPdf').hide();
                $('iframe').prop('src', resp.docUrl);
            }

            Common.render({
                tmpl: '#list_tmpl',
                data: resp.answerSheet,
                context: '#list',
                overwrite: 1
            })

            for(var i in resp.respondents){
                if(i == userId){
                    showData(resp.respondents[i]);
                }
            }

        })


    })()

    function showData(data){
        for(var i in data){
            var ele = $('#list').find('li[index="'+i+'"]');
            var type = ele.attr('ty');
            var item = data[i];
            if(1 == type){//单选
            ele.find('input[index="'+item+'"]').prop('checked', true);
            } else if(2 == type){//多选
                for(var j in item){
                    ele.find('input[index="'+item[j]+'"]').prop('checked', true);
                }
            } else if(3 == type){//问答
                ele.find('textarea').text(item);
            } else if(4 == type){//打分
                ele.find('option[index="'+item+'"]').prop('selected', true);
            }
        }
    }

    function previewSwf(url){
        var fp = new FlexPaperViewer(
            '/static/plugins/flexpaper/FlexPaperViewer',
            'viewerPlaceHolder', { config : {
                SwfFile : escape(url),
                Scale : 0.6,
                ZoomTransition : 'easeOut',
                ZoomTime : 0.5,
                ZoomInterval : 0.2,
                FitPageOnLoad : true,
                FitWidthOnLoad : false,
                FullScreenAsMaxWindow : false,
                ProgressiveLoading : false,
                MinZoomSize : 0.2,
                MaxZoomSize : 5,
                SearchMatchAll : false,
                InitViewMode : 'SinglePage',
                ViewModeToolsVisible : true,
                ZoomToolsVisible : true,
                NavToolsVisible : true,
                CursorToolsVisible : true,
                SearchToolsVisible : true,
                localeChain: 'en_US'
            }});
    }


});