/*
 * @Author: Voyage
 * @Date:   2016-05-30 11:28:27
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-06-02 14:07:03
 */

'use strict';
$(document).ready(function () {
    $('.ul-forumset li').click(function () {
        $(this).addClass('li-cur').siblings('.ul-forumset li').removeClass('li-cur');
    });
    $('.ul-forumset li:nth-child(1)').click(function () {
        $('.right-zl').show();
        $('.right-aq').hide();
    });
    $('.ul-forumset li:nth-child(2)').click(function () {
        $('.right-zl').hide();
        $('.right-aq').show();
    })
})