/**
 *
 *                        _oo0oo_
 *                       o8888888o
 *                       88" . "88
 *                       (| -_- |)
 *                       0\  =  /0
 *                     ___/`---'\___
 *                   .' \\|     |// '.
 *                  / \\|||  :  |||// \
 *                 / _||||| -:- |||||- \
 *                |   | \\\  -  /// |   |
 *                | \_|  ''\---/''  |_/ |
 *                \  .-\__  '-'  ___/-. /
 *              ___'. .'  /--.--\  `. .'___
 *           ."" '<  `.___\_<|>_/___.' >' "".
 *          | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *          \  \ `_.   \_ __\ /__ _/   .-` /  /
 *      =====`-.____`.___ \_____/___.-`___.-'=====
 *                        `=---='
 *
 *
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *                佛祖保佑         永无BUG
 *
 *
 *
 * Created by Tony on 2014/11/3.
 */
$(function () {
    $.fn.zTree.init($("#teacherDirUl"), {
        data: {
            simpleData: {
                enable: true,
                pIdKey: 'parent',
                rootPid: 0
            }
        },
        callback: {
            onClick: function (evt, treeId, node) {
                showLessons(node.id, node.name);
            }
        }
    }, dirNodes);
});

function showLessons(dirId, dirName) {
    $('.course-list-container').load('/masLessons.do', {
        dirId: dirId
    }, function () {
        $('#dirName').text(dirName);
    });
}