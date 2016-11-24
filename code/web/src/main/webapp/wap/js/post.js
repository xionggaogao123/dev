/*
 * @Author: Tony
 * @Date:   2016-08-03 18:06:01
 * @Last Modified by:   Tony
 * @Last Modified time: 2016-08-03 18:08:09
 */

'use strict';
$(document).ready(function () {
    $('.redzan').click(function () {
        if (this.src.match("white")) {
            this.src = 'images/red_heart.png';
        }
        else {
            this.src = "images/red_heart.png";
        }
    })
})