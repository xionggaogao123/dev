<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2016/8/30
  Time: 17:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Audio Player: Responsive and Touch-friendly demo by Osvaldas Valutis</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="description" content="Demo of Audio Player: Responsive and Touch-friendly"/>
    <meta name="robots" content="all">
    <meta name="viewport" content="width=device-width,initial-scale=1"/>
    <link rel="canonical" href="/examples/audio-player-responsive-and-touch-friendly">
    <link rel="icon" href="/theme/img/favicon.ico"/>
    <link rel="stylesheet" href="/static/css/forum/_reset.css"/>
    <link rel="stylesheet" href="/static/css/forum/main.css"/>
    <link rel="stylesheet" href="/static/css/forum/css.css?family=Advent+Pro">
    <style>

        html {
            height: 100%;
        }

        body {
            min-height: 100%;
            font-family: 'Advent Pro', sans-serif;
            font-weight: 500;
            background-color: #c3d5d3;
            padding: 3.75em 1.875em; /* 60 30 */
        }

        strong {
            font-weight: 700;
        }

        #wrapper {
            width: 30em; /* 480 */
            height: 8.75em; /* 140 */
            position: absolute;
            top: 50%;
            left: 50%;
            margin: -4.375em 0 0 -15em; /* 70 240 */
        }

        h1 {
            font-family: 'Advent Pro', sans-serif;
            font-size: 1.688em; /* 27 */
            font-weight: 500;
            text-align: center;
            margin-bottom: 0.741em; /* 20 */
        }

        h1,
        h1 a,
        #copyright,
        #copyright a {
            color: #fff;
            text-shadow: 1px 1px 0 rgba(0, 0, 0, .1);
        }

        h1 a,
        #copyright a {
            -webkit-transition: text-shadow .25s ease;
            -moz-transition: text-shadow .25s ease;
            -ms-transition: text-shadow .25s ease;
            -o-transition: text-shadow .25s ease;
            transition: text-shadow .25s ease;
        }

        h1 a:hover,
        h1 a:focus,
        #copyright a:hover,
        #copyright a:focus {
            color: #fff;
            text-shadow: 1px 1px 0 rgba(0, 0, 0, .3);
        }

        h1 em {
            font-family: Baskerville, serif;
        }

        #copyright {
            font-size: 0.875em; /* 14 */
            text-align: center;
            margin-top: 1.429em; /* 20 */
        }

        #copyright a {
            font-weight: 700;
        }

        /*
            PLAYER
        */

        .audioplayer {
            height: 2.5em; /* 40 */
            color: #fff;
            text-shadow: 1px 1px 0 #000;
            border: 1px solid #222;
            position: relative;
            z-index: 1;
            background: #333;
        }

        /* mini mode (fallback) */

        .audioplayer-mini {
            width: 2.5em; /* 40 */
            margin: 0 auto;
        }

        /* player elements: play/pause and volume buttons, played/duration timers, progress bar of loaded/played */

        .audioplayer > div {
            position: absolute;
        }

        /* play/pause button */

        .audioplayer-playpause {
            width: 2.5em; /* 40 */
            height: 100%;
            text-align: left;
            text-indent: -9999px;
            cursor: pointer;
            z-index: 2;
            top: 0;
            left: 0;
        }

        .audioplayer:not(.audioplayer-mini) .audioplayer-playpause {
            border-right: 1px solid #555;
            border-right-color: rgba(255, 255, 255, .1);
        }

        .audioplayer-mini .audioplayer-playpause {
            width: 100%;
        }

        .audioplayer-playpause:hover,
        .audioplayer-playpause:focus {
            background-color: #222;
        }

        .audioplayer-playpause a {
            display: block;
        }

        .audioplayer-stopped .audioplayer-playpause a {
            width: 0;
            height: 0;
            border: 0.5em solid transparent; /* 8 */
            border-right: none;
            border-left-color: #fff;
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            margin: -0.5em 0 0 -0.25em; /* 8 4 */
        }

        .audioplayer-playing .audioplayer-playpause a {
            width: 0.75em; /* 12 */
            height: 0.75em; /* 12 */
            position: absolute;
            top: 50%;
            left: 50%;
            margin: -0.375em 0 0 -0.375em; /* 6 */
        }

        .audioplayer-playing .audioplayer-playpause a:before,
        .audioplayer-playing .audioplayer-playpause a:after {
            width: 40%;
            height: 100%;
            background-color: #fff;
            content: '';
            position: absolute;
            top: 0;
        }

        .audioplayer-playing .audioplayer-playpause a:before {
            left: 0;
        }

        .audioplayer-playing .audioplayer-playpause a:after {
            right: 0;
        }

        /* timers */

        .audioplayer-time {
            width: 4.375em; /* 70 */
            height: 100%;
            line-height: 2.375em; /* 38 */
            text-align: center;
            z-index: 2;
            top: 0;
        }

        .audioplayer-time-current {
            border-left: 1px solid #111;
            border-left-color: rgba(0, 0, 0, .25);
            left: 2.5em; /* 40 */
        }

        .audioplayer-time-duration {
            border-right: 1px solid #555;
            border-right-color: rgba(255, 255, 255, .1);
            right: 2.5em; /* 40 */
        }

        .audioplayer-novolume .audioplayer-time-duration {
            border-right: 0;
            right: 0;
        }

        /* progress bar of loaded/played */

        .audioplayer-bar {
            height: 0.875em; /* 14 */
            background-color: #222;
            cursor: pointer;
            z-index: 1;
            top: 50%;
            right: 6.875em; /* 110 */
            left: 6.875em; /* 110 */
            margin-top: -0.438em; /* 7 */
        }

        .audioplayer-novolume .audioplayer-bar {
            right: 4.375em; /* 70 */
        }

        .audioplayer-bar div {
            width: 0;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
        }

        .audioplayer-bar-loaded {
            background-color: #333;
            z-index: 1;
        }

        .audioplayer-bar-played {
            background: #007fd1;
            z-index: 2;
        }

        /* volume button */

        .audioplayer-volume {
            width: 2.5em; /* 40 */
            height: 100%;
            border-left: 1px solid #111;
            border-left-color: rgba(0, 0, 0, .25);
            text-align: left;
            text-indent: -9999px;
            cursor: pointer;
            z-index: 2;
            top: 0;
            right: 0;
        }

        .audioplayer-volume:hover,
        .audioplayer-volume:focus {
            background-color: #222;
        }

        .audioplayer-volume-button {
            width: 100%;
            height: 100%;
        }

        .audioplayer-volume-button a {
            width: 0.313em; /* 5 */
            height: 0.375em; /* 6 */
            background-color: #fff;
            display: block;
            position: relative;
            z-index: 1;
            top: 40%;
            left: 35%;
        }

        .audioplayer-volume-button a:before,
        .audioplayer-volume-button a:after {
            content: '';
            position: absolute;
        }

        .audioplayer-volume-button a:before {
            width: 0;
            height: 0;
            border: 0.5em solid transparent; /* 8 */
            border-left: none;
            border-right-color: #fff;
            z-index: 2;
            top: 50%;
            right: -0.25em;
            margin-top: -0.5em; /* 8 */
        }

        .audioplayer:not(.audioplayer-muted) .audioplayer-volume-button a:after {
            /* "volume" icon by Nicolas Gallagher, http://nicolasgallagher.com/pure-css-gui-icons */
            width: 0.313em; /* 5 */
            height: 0.313em; /* 5 */
            border: 0.25em double #fff; /* 4 */
            border-width: 0.25em 0.25em 0 0; /* 4 */
            left: 0.563em; /* 9 */
            top: -0.063em; /* 1 */
            -webkit-border-radius: 0 0.938em 0 0; /* 15 */
            -moz-border-radius: 0 0.938em 0 0; /* 15 */
            border-radius: 0 0.938em 0 0; /* 15 */
            -webkit-transform: rotate(45deg);
            -moz-transform: rotate(45deg);
            -ms-transform: rotate(45deg);
            -o-transform: rotate(45deg);
            transform: rotate(45deg);
        }

        /* volume dropdown */

        .audioplayer-volume-adjust {
            height: 6.25em; /* 100 */
            cursor: default;
            position: absolute;
            left: 0;
            right: -1px;
            top: -9999px;
            background: #333;
        }

        .audioplayer-volume:not(:hover) .audioplayer-volume-adjust {
            opacity: 0;
        }

        .audioplayer-volume:hover .audioplayer-volume-adjust {
            top: auto;
            bottom: 100%;
        }

        .audioplayer-volume-adjust > div {
            width: 40%;
            height: 80%;
            background-color: #222;
            cursor: pointer;
            position: relative;
            z-index: 1;
            margin: 30% auto 0;
        }

        .audioplayer-volume-adjust div div {
            width: 100%;
            height: 100%;
            position: absolute;
            bottom: 0;
            left: 0;
            background: #007fd1;
        }

        .audioplayer-novolume .audioplayer-volume {
            display: none;
        }

        /* CSS3 decorations */

        body {
            -webkit-box-shadow: inset 0 0 18.75em rgba(0, 0, 0, .5); /* 300 */
            -moz-box-shadow: inset 0 0 18.75em rgba(0, 0, 0, 5); /* 300 */
            box-shadow: inset 0 0 18.75em rgba(0, 0, 0, .5); /* 300 */
        }

        .audioplayer {
            -webkit-box-shadow: inset 0 1px 0 rgba(255, 255, 255, .15), 0 0 1.25em rgba(0, 0, 0, .5); /* 20 */
            -moz-box-shadow: inset 0 1px 0 rgba(255, 255, 255, .15), 0 0 1.25em rgba(0, 0, 0, .5); /* 20 */
            box-shadow: inset 0 1px 0 rgba(255, 255, 255, .15), 0 0 1.25em rgba(0, 0, 0, .5); /* 20 */
        }

        .audioplayer-volume-adjust {
            -webkit-box-shadow: -2px -2px 2px rgba(0, 0, 0, .15), 2px -2px 2px rgba(0, 0, 0, .15);
            -moz-box-shadow: -2px -2px 2px rgba(0, 0, 0, .15), 2px -2px 2px rgba(0, 0, 0, .15);
            box-shadow: -2px -2px 2px rgba(0, 0, 0, .15), 2px -2px 2px rgba(0, 0, 0, .15);
        }

        .audioplayer-bar,
        .audioplayer-volume-adjust > div {
            -webkit-box-shadow: -1px -1px 0 rgba(0, 0, 0, .5), 1px 1px 0 rgba(255, 255, 255, .1);
            -moz-box-shadow: -1px -1px 0 rgba(0, 0, 0, .5), 1px 1px 0 rgba(255, 255, 255, .1);
            box-shadow: -1px -1px 0 rgba(0, 0, 0, .5), 1px 1px 0 rgba(255, 255, 255, .1);
        }

        .audioplayer-volume-adjust div div,
        .audioplayer-bar-played {
            -webkit-box-shadow: inset 0 0 5px rgba(255, 255, 255, .5);
            -moz-box-shadow: inset 0 0 5px rgba(255, 255, 255, .5);
            box-shadow: inset 0 0 5px rgba(255, 255, 255, .5);
        }

        .audioplayer-playpause,
            /*.audioplayer-volume a*/
            /*{*/
            /*-webkit-filter: drop-shadow( 1px 1px 0 #000 );*/
            /*-moz-filter: drop-shadow( 1px 1px 0 #000 );*/
            /*-ms-filter: drop-shadow( 1px 1px 0 #000 );*/
            /*-o-filter: drop-shadow( 1px 1px 0 #000 );*/
            /*filter: drop-shadow( 1px 1px 0 #000 );*/
            /*}*/
        .audioplayer,
        .audioplayer-volume-adjust {
            background: -webkit-gradient(linear, left top, left bottom, from(#444), to(#222));
            background: -webkit-linear-gradient(top, #444, #222);
            background: -moz-linear-gradient(top, #444, #222);
            /*background: -ms-radial-gradient( top, #444, #222 );*/
            background: -o-linear-gradient(top, #444, #222);
            background: linear-gradient(to bottom, #444, #222);
        }

        .audioplayer-bar-played {
            background: -webkit-gradient(linear, left top, right top, from(#007fd1), to(#c600ff));
            background: -webkit-linear-gradient(left, #007fd1, #c600ff);
            background: -moz-linear-gradient(left, #007fd1, #c600ff);
            /*background: -ms-radial-gradient( left, #007fd1, #c600ff );*/
            background: -o-linear-gradient(left, #007fd1, #c600ff);
            background: linear-gradient(to right, #007fd1, #c600ff);
        }

        .audioplayer-volume-adjust div div {
            background: -webkit-gradient(linear, left bottom, left top, from(#007fd1), to(#c600ff));
            background: -webkit-linear-gradient(bottom, #007fd1, #c600ff);
            background: -moz-linear-gradient(bottom, #007fd1, #c600ff);
            /*background: -ms-radial-gradient( bottom, #007fd1, #c600ff );*/
            background: -o-linear-gradient(bottom, #007fd1, #c600ff);
            background: linear-gradient(to top, #007fd1, #c600ff);
        }

        .audioplayer-bar,
        .audioplayer-bar div,
        .audioplayer-volume-adjust div {
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            border-radius: 4px;
        }

        .audioplayer {
            -webkit-border-radius: 2px;
            -moz-border-radius: 2px;
            border-radius: 2px;
        }

        .audioplayer-volume-adjust {
            -webkit-border-top-left-radius: 2px;
            -webkit-border-top-right-radius: 2px;
            -moz-border-radius-topleft: 2px;
            -moz-border-radius-topright: 2px;
            border-top-left-radius: 2px;
            border-top-right-radius: 2px;
        }

        .audioplayer *,
        .audioplayer *:before,
        .audioplayer *:after {
            -webkit-transition: color .25s ease, background-color .25s ease, opacity .5s ease;
            -moz-transition: color .25s ease, background-color .25s ease, opacity .5s ease;
            -ms-transition: color .25s ease, background-color .25s ease, opacity .5s ease;
            -o-transition: color .25s ease, background-color .25s ease, opacity .5s ease;
            transition: color .25s ease, background-color .25s ease, opacity .5s ease;
        }

        /* responsiveness */

        @media only screen and ( max-width: 32.5em ) /* 520 */ {
            body {
                -webkit-box-shadow: inset 0 0 9.375em rgba(0, 0, 0, .5); /* 150 */
                -moz-box-shadow: inset 0 0 9.375em rgba(0, 0, 0, 5); /* 150 */
                box-shadow: inset 0 0 9.375em rgba(0, 0, 0, .5); /* 150 */
            }

            #wrapper {
                width: 100%;
                height: auto;
                position: static;
                padding: 1.25em; /* 20 */
                margin: 0;
            }
        }

    </style>
</head>

<body>


<div id="wrapper">

    <h1>Audio Player:</strong> Responsive <em>&amp;</em> Touch-friendly</h1>
    <%--http://doc.k6kt.com/57c553ad71f0561158867920.wav--%>
    <audio preload="auto" controls>
        <source src="${musicUrl}"/>
    </audio>

    <div id="copyright">Music by <a href="mailto:honeyrec@gmail.com">DJ Betas</a></div>

</div>


<script src="/static/js/modules/forum/main.js"></script>

<script src="/static/js/modules/forum/audioplayer.js"></script>
<script>
    $(function () {
        $('audio').audioPlayer();
    });
</script>

</body>
</html>
