define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');

    var Common = require('common');


    (function(){



        chengJi.init();

        $('button.inputscore').on('click',function(){

            Common.goTo('/score/teacher/editscore.do');
        })


    })()

})
