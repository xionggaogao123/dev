!function(e){function t(o){if(n[o])return n[o].exports;var a=n[o]={exports:{},id:o,loaded:!1};return e[o].call(a.exports,a,a.exports,t),a.loaded=!0,a.exports}var n={};return t.m=e,t.c=n,t.p="./",t(0)}({0:function(e,t,n){var o,a;(function(e){"use strict";var r="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},i=n(215),c=n(216);window.WebIM=WebIM||{},WebIM.WebRTC=WebIM.WebRTC||{},WebIM.WebRTC.Call=c,WebIM.WebRTC.Util=i,"object"===r(e)&&"object"===r(e.exports)?e.exports=WebIM.WebRTC:(o=[],a=function(){return WebIM.WebRTC}.apply(t,o),!(void 0!==a&&(e.exports=a)))}).call(t,n(214)(e))},214:function(e,t){e.exports=function(e){return e.webpackPolyfill||(e.deprecate=function(){},e.paths=[],e.children=[],e.webpackPolyfill=1),e}},215:function(e,t){"use strict";function n(){}var o="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e};!function(){var e="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");Math.uuid=function(t,n){var o,a=e,r=[];if(n=n||a.length,t)for(o=0;o<t;o++)r[o]=a[0|Math.random()*n];else{var i;for(r[8]=r[13]=r[18]=r[23]="-",r[14]="4",o=0;o<36;o++)r[o]||(i=0|16*Math.random(),r[o]=a[19==o?3&i|8:i])}return r.join("")},Math.uuidFast=function(){for(var t,n=e,o=new Array(36),a=0,r=0;r<36;r++)8==r||13==r||18==r||23==r?o[r]="-":14==r?o[r]="4":(a<=2&&(a=33554432+16777216*Math.random()|0),t=15&a,a>>=4,o[r]=n[19==r?3&t|8:t]);return o.join("")},Math.uuidCompact=function(){return"xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g,function(e){var t=16*Math.random()|0,n="x"==e?t:3&t|8;return n.toString(16)})}}();var a=function(){function e(e,n){var o=[];o.push(e);for(var a in n)o.push(n[a]);t.log.apply(t,o)}var t=this,n={TRACE:0,DEBUG:1,INFO:2,WARN:3,ERROR:4,FATAL:5},o=["TRACE","DEBUG","INFO","WARN","ERROR","FATAL"];this.log=function(){var e=arguments[0];e=arguments[0]="["+o[e]+"] ";arguments[1];console.log.apply(console,arguments)},this.trace=function(){this.log&&e(n.TRACE,arguments)},this.debug=function(){this.log&&e(n.DEBUG,arguments)},this.info=function(){this.log&&e(n.INFO,arguments)},this.warn=function(){this.log&&e(n.WARN,arguments)},this.error=function(){this.log&&e(n.ERROR,arguments)},this.fatal=function(){this.log&&e(n.FATAL,arguments)}};n.prototype.logger=new a,n.prototype.parseJSON=function(e){return JSON.parse(e)};var r=(n.prototype.stringifyJSON=function(e){return JSON.stringify(e)},{}),i=r.toString,c=r.hasOwnProperty,s=c.toString,d=s.call(Object);n.prototype.isPlainObject=function(e){var t,n;return!(!e||"[object Object]"!==i.call(e))&&(!(t=Object.getPrototypeOf(e))||(n=c.call(t,"constructor")&&t.constructor,"function"==typeof n&&s.call(n)===d))};n.prototype.isArray=Array.isArray,n.prototype.isEmptyObject=function(e){var t;for(t in e)return!1;return!0},n.prototype.type=function(e){return null==e?e+"":"object"===("undefined"==typeof e?"undefined":o(e))||"function"==typeof e?r[i.call(e)]||"object":"undefined"==typeof e?"undefined":o(e)},n.prototype.extend=function(){var e,t,n,a,r,i,c=this,s=arguments[0]||{},d=1,u=arguments.length,l=!1;for("boolean"==typeof s&&(l=s,s=arguments[d]||{},d++),"object"===("undefined"==typeof s?"undefined":o(s))||c.isFunction(s)||(s={}),d===u&&(s=this,d--);d<u;d++)if(null!=(e=arguments[d]))for(t in e)n=s[t],a=e[t],s!==a&&(l&&a&&(c.isPlainObject(a)||(r=c.isArray(a)))?(r?(r=!1,i=n&&c.isArray(n)?n:[]):i=n&&c.isPlainObject(n)?n:{},s[t]=c.extend(l,i,a)):void 0!==a&&(s[t]=a));return s},n.prototype.hasLocalStorage=function(e){return null!=localStorage.getItem(e)&&"{}"!=localStorage.getItem(e)},n.prototype.toggleClass=function(e,t){return e.hasClass(t)?void e.removeClass(t):void e.addClass(t)},n.prototype.setCookie=function(e,t,n){var o=new Date;o.setTime(o.getTime()+60*n*60*1e3),document.cookie=e+"="+escape(t)+";expires="+o.toGMTString()},n.prototype.getCookie=function(e){var t=document.cookie.match(new RegExp("(^| )"+e+"=([^;]*)(;|$)"));return null!=t?unescape(t[2]):null},n.prototype.parseURL=function(e){var t=new RegExp("(^|&)"+e+"=([^&]*)(&|$)","i"),n=window.location.search.substr(1).match(t);return null!=n?unescape(n[2]):null},e.exports=new n},216:function(e,t,n){"use strict";var o=n(215),a=n(217),r=n(218),i=n(219),c=n(220),s=r.RouteTo,d=r.Api,u=o.logger,l={api:null,connection:null,pattern:null,listener:{onAcceptCall:function(e,t){},onRinging:function(e){},onTermCall:function(){}},mediaStreamConstaints:{audio:!0,video:!0},init:function(){var e=this;if("undefined"==typeof e.connection)throw"Caller need a instance of Easemob.im.Connection";e.api=e.api||new d({imConnection:e.connection,rtcHandler:new a({imConnection:e.connection})}),e.api.onInitC=function(){e._onInitC.apply(e,arguments)}},makeVideoCall:function(e){var t=this,n={};o.extend(n,t.mediaStreamConstaints),t.call(e,n)},makeVoiceCall:function(e){var t=this,n={};o.extend(n,t.mediaStreamConstaints),t.mediaStreamConstaints.video=!1,t.call(e,n)},acceptCall:function(){var e=this;e.pattern.accept()},endCall:function(e){var t=this;t.pattern.termCall()},call:function(e,t){var n=this;n.callee=n.api.jid(e);var o=new s({rtKey:"",success:function(e){u.debug("iq to server success",e)},fail:function(e){u.debug("iq to server error",e),n.onError(e)}});n.api.reqP2P(o,t.video?1:0,t.audio?1:0,e,function(e,t){n._onGotServerP2PConfig(e,t),n.pattern.initC(n.mediaStreamConstaints)})},_onInitC:function(e,t,n,o,a){var r=this;r.callee=e,r._rtcCfg=t.rtcCfg,r._WebRTCCfg=t.WebRTC,r.sessId=t.sessId,r.rtcId=t.rtcId,r.switchPattern(),r.pattern._onInitC(e,t,n,o,a)},_onGotServerP2PConfig:function(e,t){var n=this;0==t.result&&(n._p2pConfig=t,n._rtcCfg=t.rtcCfg,n._rtcCfg2=t.rtcCfg2,n.sessId=t.sessId,n.rtcId="Channel_webIM",n._rtKey=n._rtkey=t.rtKey||t.rtkey,n._rtFlag=n._rtflag=t.rtFlag||t.rtflag,n._WebRTCCfg=t.WebRTC,n.admtok=t.admtok,n.tkt=t.tkt,n.switchPattern())},switchPattern:function(){var e=this;!e._WebRTCCfg&&(e.pattern=new c({callee:e.callee,_p2pConfig:e._p2pConfig,_rtcCfg:e._rtcCfg,_rtcCfg2:e._rtcCfg2,_rtKey:e._rtKey||e._rtkey,_rtFlag:e._rtFlag||e._rtflag,_sessId:e.sessId,_rtcId:e.rtcId,webRtc:new i({onGotLocalStream:e.listener.onGotLocalStream,onGotRemoteStream:e.listener.onGotRemoteStream,onError:e.listener.onError}),api:e.api,onAcceptCall:e.listener&&e.listener.onAcceptCall||function(){},onRinging:e.listener&&e.listener.onRinging||function(){},onTermCall:e.listener&&e.listener.onTermCall||function(){}}))}};e.exports=function(e){o.extend(!0,this,l,e||{}),this.init()}},217:function(e,t,n){"use strict";var o=n(215),a=o.logger,r="urn:xmpp:media-conference",i={_apiCallbacks:{},imConnection:null,init:function(){var e,t=this,n=t.imConnection;n.addHandler=function(o,i,c,s,d,u,l){"function"!=typeof e&&(e=function(e){try{t.handleRtcMessage(e)}catch(e){throw a.error(e.stack||e),e}return!0},n.addHandler(e,r,"iq","set",null,null),n.addHandler(e,r,"iq","get",null,null)),n.context.stropheConn.addHandler(o,i,c,s,d,u,l)}},handleRtcMessage:function(e){var t=this,n=(e.getAttribute("id"),e.getAttribute("from")||"");n.lastIndexOf("/")>=0&&(n=n.substring(0,n.lastIndexOf("/")));var r=e.getElementsByTagName("rtkey")[0].innerHTML,i=e.getElementsByTagName("sid")[0].innerHTML;(t._fromSessionID||(t._fromSessionID={}))[n]=i;var c=e.getElementsByTagName("content"),s=(e.getElementsByTagName("stream_type")[0].innerHTML,c[0].innerHTML),d=o.parseJSON(s),u=d,l=d.tsxId;if(t.ctx=d.ctx,a.debug("Recv [op = "+u.op+"]\r\n json :",e),u.sdp&&("string"==typeof u.sdp&&(u.sdp=o.parseJSON(u.sdp)),u.sdp.type&&(u.sdp.type=u.sdp.type.toLowerCase())),u.cands){"string"==typeof u.cands&&(u.cands=o.parseJSON(u.cands));for(var f=0;f<u.cands.length;f++)"string"==typeof u.cands[f]&&(u.cands[f]=o.parseJSON(u.cands[f])),u.cands[f].sdpMLineIndex=u.cands[f].mlineindex,u.cands[f].sdpMid=u.cands[f].mid,delete u.cands[f].mlineindex,delete u.cands[f].mid}if(u.rtcCfg&&"string"==typeof u.rtcCfg&&(u.rtcCfg=o.parseJSON(u.rtcCfg)),u.rtcCfg2&&"string"==typeof u.rtcCfg2&&(u.rtcCfg2=o.parseJSON(u.rtcCfg2)),u.WebRTC&&"string"==typeof u.WebRTC&&(u.WebRTC=o.parseJSON(u.WebRTC)),l&&t._apiCallbacks[l])try{t._apiCallbacks[l].callback&&t._apiCallbacks[l].callback(n,u)}catch(e){throw e}finally{delete t._apiCallbacks[l]}else t.onRecvRtcMessage(n,u,r,l,i);return!0},onRecvRtcMessage:function(e,t,n,r,i){a.debug(" form : "+e+" \r\n json :"+o.stringifyJSON(rtcJSON))},convertRtcOptions:function(e){var t=e.data.sdp;if(t){var n={type:t.type,sdp:t.sdp};t=n,t.type=t.type.toUpperCase(),t=o.stringifyJSON(t),e.data.sdp=t}var a=e.data.cands;if(a){if(o.isArray(a));else{var r=[];r.push(a),a=r}for(var i in a)if(a[i]instanceof RTCIceCandidate){var c={type:"candidate",candidate:a[i].candidate,mlineindex:a[i].sdpMLineIndex,mid:a[i].sdpMid};a[i]=o.stringifyJSON(c)}e.data.cands=a}var s=e.data.rtcCfg;s&&"string"!=typeof s&&(e.data.rtcCfg=o.stringifyJSON(s));var d=e.data.WebRTC;d&&"string"!=typeof d&&(e.data.WebRTC=o.stringifyJSON(d))},sendRtcMessage:function(e,t,n){var i=this,c=i.imConnection,s=e.tsxId||c.getUniqueId(),d=e.to||c.domain,u=e.sid||i._fromSessionID&&i._fromSessionID[d];u=u||((i._fromSessionID||(i._fromSessionID={}))[d]=c.getUniqueId("CONFR_"));var l=e.rtKey||e.rtkey;l||(l="");var f=e.rtflag;f||(f=1),t.data||(t.data={}),t.data.tsxId=s,i.ctx&&(t.data.ctx=i.ctx),i.convertRtcOptions(t);var p="VIDEO",g=e.id||c.getUniqueId("CONFR_"),C=$iq({id:g,to:d,from:c.context.jid,type:e.type||"get"}).c("query",{xmlns:r}).c("MediaReqExt").c("rtkey").t(l).up().c("rtflag").t(f).up().c("stream_type").t(p).up().c("sid").t(u).up().c("content").t(o.stringifyJSON(t.data));a.debug("Send IQ [op = "+t.data.op+"] : \r\n",C.tree()),n&&(i._apiCallbacks[s]={callback:n});var S=function(t){e.success(t)}||function(e){a.debug("send result. op:"+t.data.op+".",e)},m=function(t){e.fail(t)}||function(e){a.debug(e)};c.context.stropheConn.sendIQ(C.tree(),S,m)}},c=function(e){o.extend(!0,this,i,e||{}),this.init()};e.exports=c},218:function(e,t,n){"use strict";var o="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},a=n(215),r=a.logger,i={rtFlag:1,success:function(e){},fail:function(e){}},c=function e(t){if(!(this instanceof e)){var n=function(e){var t=this;a.extend(!0,t,e||{})};return a.extend(!0,n.prototype,i,t||{}),n}var o=this;a.extend(!0,o,i,t||{})};t.RouteTo=c;var s={imConnection:null,rtcHandler:null,events:{0:"onReqP2P",1:"onNewCfr",2:"onDelCfr",3:"onReqTkt",100:"onPing",101:"onPong",102:"onInitC",103:"onReqC",104:"onAcptC",105:"onTcklC",106:"onAnsC",107:"onTermC",300:"onEvEnter",301:"onEvExit",302:"onEvPub",303:"onEvUnpub",304:"onEvMems",204:"onEvClose",onServerError:"onServerError"},register:function(e){if("object"===("undefined"==typeof e?"undefined":o(e)))for(var t in e)this.bind(t,e[t])},bind:function(e,t){var n,o=this;(n=o.events[e])?o[n]=t:(n=o.events[e]="on_"+e,o[n]=t)},jid:function(e){return e.indexOf(this.imConnection.context.appKey)>=0?e:this.imConnection.context.appKey+"_"+e+"@"+this.imConnection.domain},reqP2P:function(e,t,n,o,a){r.debug("req p2p ...");var i=this,c={data:{op:0,video:t,audio:n,peer:o}};i.rtcHandler.sendRtcMessage(e,c,a)},newCfr:function(e,t,n,o){r.debug("newCfr ...");var a=this,i={data:{op:1}};t&&(i.data.reqTkt=t),n&&(i.data.password=n),a.rtcHandler.sendRtcMessage(e,i,o)},enter:function(e,t,n,o,a,i,c){r.debug("enter ...");var s=this,d={data:{op:200}};t&&(d.data.WebRTCId=t),n&&(d.data.reqMembers=n),o&&(d.data.tkt=o),a&&(d.data.nonce=a),i&&(d.data.digest=i),s.rtcHandler.sendRtcMessage(e,d,c)},ping:function(e,t,n){r.debug("ping ...");var o=this,a={data:{op:100}};t&&(a.data.sessId=t),o.rtcHandler.sendRtcMessage(e,a,n)},reqTkt:function(e,t,n){r.debug("reqTkt ...");var o=this,a={data:{op:3}};t&&(a.data.WebRTCId=t),o.rtcHandler.sendRtcMessage(e,a,n)},initC:function(e,t,n,o,a,i,c,s,d,u,l,f){r.debug("initC ...");var p=this,g={data:{op:102}};t&&(g.data.WebRTCId=t),n&&(g.data.tkt=n),o&&(g.data.sessId=o),a&&(g.data.rtcId=a),i&&(g.data.pubS=i),c&&(g.data.subS=c),s&&(g.data.sdp=s),d&&(g.data.cands=d),u&&(g.data.rtcCfg=u),l&&(g.data.WebRTC=l),p.rtcHandler.sendRtcMessage(e,g,f)},tcklC:function(e,t,n,o,a,i){r.debug("tcklC ...");var c=this,s={data:{op:105}};t&&(s.data.sessId=t),n&&(s.data.rtcId=n),o&&(s.data.sdp=o),a&&(s.data.cands=a),c.rtcHandler.sendRtcMessage(e,s,i)},ansC:function(e,t,n,o,a,i){r.debug("ansC ...");var c=this,s={data:{op:106}};t&&(s.data.sessId=t),n&&(s.data.rtcId=n),o&&(s.data.sdp=o),a&&(s.data.cands=a),c.rtcHandler.sendRtcMessage(e,s,i)},acptC:function(e,t,n,o,a,i,c){r.debug("acptC ...");var s=this,d={data:{op:104}};t&&(d.data.sessId=t),n&&(d.data.rtcId=n),o&&(d.data.sdp=o),a&&(d.data.cands=a),i&&(d.data.ans=i),s.rtcHandler.sendRtcMessage(e,d,c)},getMems:function(e,t,n,o){r.debug("getMems ...");var a=this,i={data:{op:203}};t&&(i.data.WebRTCId=t),n&&(i.data.sessId=n),a.rtcHandler.sendRtcMessage(e,i,o)},subC:function(e,t,n,o,a){r.debug("subC ...");var i=this,c={data:{op:205}};t&&(c.data.sessId=t),n&&(c.data.rtcId=n),o&&(c.data.subS=o),i.rtcHandler.sendRtcMessage(e,c,a)},usubC:function(e,t,n,o){r.debug("usubC ...");var a=this,i={data:{op:206}};t&&(i.data.sessId=t),n&&(i.data.rtcId=n),a.rtcHandler.sendRtcMessage(e,i,o)},termC:function(e,t,n,o){r.debug("termC ...");var a=this,i={data:{op:107}};t&&(i.data.sessId=t),n&&(i.data.rtcId=n),a.rtcHandler.sendRtcMessage(e,i,o)},exit:function(e,t,n,o){r.debug("exit ...");var a=this,i={data:{op:201}};t&&(i.data.WebRTCId=t),n&&(i.data.sessId=n),a.rtcHandler.sendRtcMessage(e,i,o)},delCfr:function(e,t,n,o){r.debug("delCfr ...");var a=this,i={data:{op:2}};t&&(i.data.WebRTCId=t),n&&(i.data.admtok=n),a.rtcHandler.sendRtcMessage(e,i,o)}};t.Api=function(e){function t(e,t,o,a,i){if(0!=t.result&&n.onServerError)n.onServerError.call(n,e,t,o,a,i);else{var c;n.events[t.op]&&(c=n[n.events[t.op]])?c.call(n,e,t,o,a,i):r.info("can not handle(recvRtcMessage) the op: "+t.op,t)}}var n=this;a.extend(!0,this,s,e||{}),this.rtcHandler.onRecvRtcMessage=t}},219:function(e,t,n){"use strict";var o=n(215),a=o.logger,r={headerSection:null,audioSection:null,videoSection:null,_parseHeaderSection:function(e){var t=e.indexOf("m=audio");return t>=0?e.slice(0,t):(t=e.indexOf("m=video"),t>=0?e.slice(0,t):e)},_parseAudioSection:function(e){var t=e.indexOf("m=audio");if(t>=0){var n=e.indexOf("m=video");return e.slice(t,n<0?e.length:n)}},_parseVideoSection:function(e){var t=e.indexOf("m=video");if(t>=0)return e.slice(t)},spiltSection:function(e){var t=this;t.headerSection=t._parseHeaderSection(e),t.audioSection=t._parseAudioSection(e),t.videoSection=t._parseVideoSection(e)},removeSSRC:function(e){for(var t=[],n=e.split(/a=ssrc:[^\n]+/g),o=0;o<n.length;o++)"\n"!=n[o]&&t.push(n[o]);return t.join("\n")},updateHeaderMsidSemantic:function(e){var t=this,n="a=msid-semantic: WMS "+e,o=t.headerSection.split(/a=msid\-semantic: WMS.*/g),a=[];switch(o.length){case 1:a.push(o[0]);break;case 2:a.push(o[0]),a.push(n),a.push("\n");break;case 3:a.push(o[0]),a.push(n),a.push("\n"),a.push(o[2]),a.push("\n")}return t.headerSection=a.join("")},updateAudioSSRCSection:function(e,t,n,o){var a=this;a.audioSection&&(a.audioSection=a.removeSSRC(a.audioSection)+a.ssrcSection(e,t,n,o))},updateVideoSSRCSection:function(e,t,n,o){var a=this;a.videoSection&&(a.videoSection=a.removeSSRC(a.videoSection)+a.ssrcSection(e,t,n,o))},getUpdatedSDP:function(){var e=this,t="";return e.headerSection&&(t+=e.headerSection),e.audioSection&&(t+=e.audioSection),e.videoSection&&(t+=e.videoSection),t},parseMsidSemantic:function(e){var t=this,n=/a=msid\-semantic: WMS (\S+)/gi,o=t._parseLine(e,n);return o&&2==o.length&&(t.msidSemantic={line:o[0],WMS:o[1]}),t.msidSemantic},ssrcSection:function(e,t,n,o){var a=["a=ssrc:"+e+" cname:"+t,"a=ssrc:"+e+" msid:"+n+" "+o,"a=ssrc:"+e+" mslabel:"+n,"a=ssrc:"+e+" label:"+o,""];return a.join("\n")},parseSSRC:function(e){var t=this,n=new RegExp("a=(ssrc):(\\d+) (\\S+):(\\S+)","ig"),o=t._parseLine(e,n);if(o){for(var a={lines:[],updateSSRCSection:t.ssrcSection},r=0;r<o.length;r++){var i=o[r];if(i.indexOf("a=ssrc")>=0)a.lines.push(i);else switch(i){case"ssrc":case"cname":case"msid":case"mslabel":case"label":a[i]=o[++r]}}return a}},_parseLine:function(e,t){for(var n,o=[];null!=(n=t.exec(e));)for(var a=0;a<n.length;a++)o.push(n[a]);if(o.length>0)return o}},i=function(e){o.extend(this,r),this.spiltSection(e)},c={mediaStreamConstaints:{audio:!0,video:!0},localStream:null,rtcPeerConnection:null,offerOptions:{offerToReceiveAudio:1,offerToReceiveVideo:1},createMedia:function(e,t){function n(e){a.debug(" got local stream"),o.localStream=e;var n=o.localStream.getVideoTracks(),r=o.localStream.getAudioTracks();n.length>0&&a.debug(" Using video device: "+n[0].label),r.length>0&&a.debug(" Using audio device: "+r[0].label),t?t(o,e):o.onGotStream(e)}var o=this;return e&&"function"==typeof e&&(t=e,e=null),a.debug(" begin create media ......"),navigator.mediaDevices.getUserMedia(e||o.mediaStreamConstaints).then(n).then(o.onCreateMedia).catch(function(e){a.debug("getUserMedia() error: ",e),o.onError(e)})},setLocalVideoSrcObject:function(e){this.onGotLocalStream(e),a.debug(" you can see yourself !")},createRtcPeerConnection:function(e){var t=this;e&&e.iceServers||(e=null),a.debug(" begin create RtcPeerConnection ......"),t.startTime=window.performance.now();var n=t.rtcPeerConnection=new RTCPeerConnection(e);a.debug(" Created local peer connection object",n),n.onicecandidate=function(e){t.onIceCandidate(e)},n.onicestatechange=function(e){t.onIceStateChange(e)},n.oniceconnectionstatechange=function(e){t.onIceStateChange(e)},n.onaddstream=function(e){t._onGotRemoteStream(e)}},_uploadLocalStream:function(){this.rtcPeerConnection.addStream(this.localStream),a.debug("Added local stream to RtcPeerConnection")},createOffer:function(e,t){var n=this;return n._uploadLocalStream(),a.debug(" createOffer start..."),n.rtcPeerConnection.createOffer(n.offerOptions).then(function(t){n.offerDescription=t,a.debug(" Offer from \n"+t.sdp),a.debug(" setLocalDescription start"),n.rtcPeerConnection.setLocalDescription(t).then(n.onSetLocalSessionDescriptionSuccess,n.onSetSessionDescriptionError).then(function(){(e||n.onCreateOfferSuccess)(t)})},t||n.onCreateSessionDescriptionError)},createPRAnswer:function(e,t){var n=this;return a.info(" createPRAnswer start"),n.rtcPeerConnection.createAnswer().then(function(t){a.debug(" _____________PRAnswer from :\n"+t.sdp),t.type="pranswer",t.sdp=t.sdp.replace(/a=recvonly/g,"a=inactive"),n.prAnswerDescription=t,a.debug(" inactive PRAnswer from :\n"+t.sdp),a.debug(" setLocalDescription start"),n.rtcPeerConnection.setLocalDescription(t).then(n.onSetLocalSuccess,n.onSetSessionDescriptionError).then(function(){var o=new i(t.sdp);o.updateHeaderMsidSemantic("MS_0000"),o.updateAudioSSRCSection(1e3,"CHROME0000","MS_0000","LABEL_AUDIO_1000"),o.updateVideoSSRCSection(2e3,"CHROME0000","MS_0000","LABEL_VIDEO_2000"),t.sdp=o.getUpdatedSDP(),a.debug(" Send PRAnswer from :\n"+t.sdp),(e||n.onCreatePRAnswerSuccess)(t)})},t||n.onCreateSessionDescriptionError)},createAnswer:function(e,t){var n=this;return n._uploadLocalStream(),a.info(" createAnswer start"),n.rtcPeerConnection.createAnswer().then(function(t){a.debug(" _____________________Answer from :\n"+t.sdp),t.type="answer";var o=new i(t.sdp),r=o.parseMsidSemantic(o.headerSection),c=o.parseSSRC(o.audioSection),s=o.parseSSRC(o.videoSection);o.updateAudioSSRCSection(1e3,"CHROME0000",r.WMS,c.label),o.updateVideoSSRCSection(2e3,"CHROME0000",r.WMS,s.label),t.sdp=o.getUpdatedSDP(),n.answerDescription=t,a.debug(" Answer from :\n"+t.sdp),a.debug(" setLocalDescription start"),n.rtcPeerConnection.setLocalDescription(t).then(n.onSetLocalSuccess,n.onSetSessionDescriptionError).then(function(){var o=new i(t.sdp);o.updateHeaderMsidSemantic("MS_0000"),o.updateAudioSSRCSection(1e3,"CHROME0000","MS_0000","LABEL_AUDIO_1000"),o.updateVideoSSRCSection(2e3,"CHROME0000","MS_0000","LABEL_VIDEO_2000"),t.sdp=o.getUpdatedSDP(),a.debug(" Send Answer from :\n"+t.sdp),(e||n.onCreateAnswerSuccess)(t)})},t||n.onCreateSessionDescriptionError)},close:function(){var e=this;try{e.rtcPeerConnection&&e.rtcPeerConnection.close()}catch(e){}e.localStream&&e.localStream.getTracks().forEach(function(e){e.stop()}),e.localStream=null},addIceCandidate:function(e){var t=this;if(t.rtcPeerConnection){a.debug(" Add ICE candidate: \n",e);var n=o.isArray(e)?e:[];!o.isArray(e)&&n.push(e);for(var r=0;r<n.length;r++)e=n[r],t.rtcPeerConnection.addIceCandidate(new RTCIceCandidate(e)).then(t.onAddIceCandidateSuccess,t.onAddIceCandidateError)}},setRemoteDescription:function(e){var t=this;return a.debug(" setRemoteDescription start. "),e=new RTCSessionDescription(e),t.rtcPeerConnection.setRemoteDescription(e).then(t.onSetRemoteSuccess,t.onSetSessionDescriptionError)},iceConnectionState:function(){var e=this;return e.rtcPeerConnection.iceConnectionState},onCreateMedia:function(){a.debug("media created.")},_onGotRemoteStream:function(e){a.debug("onGotRemoteStream.",e),this.onGotRemoteStream(e.stream),a.debug("received remote stream, you will see the other.")},onGotStream:function(e){a.debug("on got a local stream")},onSetRemoteSuccess:function(){a.info(" onSetRemoteSuccess complete")},onSetLocalSuccess:function(){a.info(" setLocalDescription complete")},onAddIceCandidateSuccess:function(){a.debug(" addIceCandidate success")},onAddIceCandidateError:function(e){a.debug(" failed to add ICE Candidate: "+e.toString())},onIceCandidate:function(e){a.debug(" onIceCandidate : ICE candidate: \n"+e.candidate)},onIceStateChange:function(e){a.debug(" onIceStateChange : ICE state change event: ",e)},onCreateSessionDescriptionError:function(e){a.error(" Failed to create session description: "+e.toString())},onCreateOfferSuccess:function(e){a.debug(" create offer success")},onCreatePRAnswerSuccess:function(e){a.debug(" create answer success")},onCreateAnswerSuccess:function(e){a.debug(" create answer success")},onSetSessionDescriptionError:function(e){a.error(" onSetSessionDescriptionError : Failed to set session description: "+e.toString())},onSetLocalSessionDescriptionSuccess:function(){a.debug(" onSetLocalSessionDescriptionSuccess : setLocalDescription complete")}};e.exports=function(e){o.extend(!0,this,c,e||{})}},220:function(e,t,n){"use strict";var o=n(215),a=n(218).RouteTo,r=o.logger,i=a({success:function(e){r.debug("iq to server success",e)},fail:function(e){r.debug("iq to server error",e)}}),c={_pingIntervalId:null,_p2pConfig:null,_rtcCfg:null,_rtcCfg2:null,_rtKey:null,_rtFlag:null,webRtc:null,api:null,callee:null,consult:!1,init:function(){var e=this;e.api.onTcklC=function(){e._onTcklC.apply(e,arguments)},e.api.onAcptC=function(){e._onAcptC.apply(e,arguments)},e.api.onAnsC=function(){e._onAnsC.apply(e,arguments)},e.api.onTermC=function(){e._onTermC.apply(e,arguments)},e.webRtc.onIceCandidate=function(){e._onIceCandidate.apply(e,arguments)},e.webRtc.onIceStateChange=function(){e._onIceStateChange.apply(e,arguments)}},_ping:function(){function e(){var e=new i({to:t.callee,rtKey:t._rtKey});t.api.ping(e,t._sessId,function(e,t){r.debug("ping result",t)})}var t=this;t._pingIntervalId=window.setInterval(e,59e3)},initC:function(e){var t=this;t.createLocalMedia(e)},createLocalMedia:function(e){var t=this;t.consult=!1,t.webRtc.createMedia(e,function(e,n){e.setLocalVideoSrcObject(n),t.webRtc.createRtcPeerConnection(t._rtcCfg),t.webRtc.createOffer(function(e){t._onGotWebRtcOffer(e)})})},_onGotWebRtcOffer:function(e){var t=this,n=new i({to:t.callee,rtKey:t._rtKey});t.api.initC(n,null,null,t._sessId,t._rtcId,null,null,e,null,t._rtcCfg2,null,function(e,t){r.debug("initc result",t)}),t._ping()},_onAcptC:function(e,t){var n=this;r.info("_onAcptC : recv pranswer. "),(t.sdp||t.cands)&&(t.sdp&&n.webRtc.setRemoteDescription(t.sdp),t.cands&&n._onTcklC(e,t),n._onHandShake(e,t),n.onAcceptCall(e,t))},onAcceptCall:function(e,t){},_onAnsC:function(e,t){var n=this;r.info("_onAnsC : recv answer. "),t.sdp&&n.webRtc.setRemoteDescription(t.sdp)},_onInitC:function(e,t,n,o,a){var r=this;r.consult=!1,r.callee=e,r._rtcCfg2=t.rtcCfg,r._rtKey=n,r._tsxId=o,r._fromSid=a,r._rtcId=t.rtcId,r._sessId=t.sessId,r.webRtc.createRtcPeerConnection(r._rtcCfg2),t.sdp&&r.webRtc.setRemoteDescription(t.sdp),t.cands&&r._onTcklC(e,t),r.webRtc.createPRAnswer(function(e){r._onGotWebRtcPRAnswer(e)})},_onGotWebRtcPRAnswer:function(e){var t=this,n=new i({tsxId:t._tsxId,to:t.callee,rtKey:t._rtKey});t._onHandShake(),t.api.acptC(n,t._sessId,t._rtcId,e,null,1),t._ping(),setTimeout(function(){t.onRinging(t.callee)},2e3)},onRinging:function(e){},accept:function(){function e(){r.info("createAndSendAnswer : ...... "),t.webRtc.createAnswer(function(e){var n=new i({tsxId:t._tsxId,to:t.callee,rtKey:t._rtKey});t.api.ansC(n,t._sessId,t._rtcId,e,null)})}var t=this;t.webRtc.createMedia(function(t,n){t.setLocalVideoSrcObject(n),e()})},_onHandShake:function(e,t){var n=this;n.consult=!0,r.info("hand shake over. may switch cands."),setTimeout(function(){n._onTcklC(e,t)},100),setTimeout(function(){n._onIceCandidate()},100)},_onTcklC:function(e,t){var n=this;if(n.consult)r.info("recv and add cands."),n._recvCands&&n._recvCands.length>0&&n.webRtc.addIceCandidate(n._recvCands),t&&t.cands&&n.webRtc.addIceCandidate(t.cands);else if(t&&t.cands&&t.cands.length>0){for(var o=0;o<t.cands.length;o++)(n._recvCands||(n._recvCands=[])).push(t.cands[o]);r.debug("[_onTcklC] temporary memory[recv] ice candidate. util consult = true")}},_onIceStateChange:function(e){var t=this;e&&r.debug(t.webRtc.iceConnectionState()+" |||| ice state is "+e.target.iceConnectionState)},_onIceCandidate:function(e){var t=this;if(t.consult){var n=function(e){r.debug("send ice candidate...");var n=new i({to:t.callee,rtKey:t._rtKey});e&&t.api.tcklC(n,t._sessId,t._rtcId,null,e)};t._cands&&t._cands.length>0&&(n(t._cands),t._cands=[]),e&&e.candidate&&n(e.candidate)}else e&&e.candidate&&(t._cands||(t._cands=[])).push(e.candidate),r.debug("[_onIceCandidate] temporary memory[send] ice candidate. util consult = true")},termCall:function(){var e=this;e._pingIntervalId&&window.clearInterval(e._pingIntervalId);var t=new i({to:e.callee,rtKey:e._rtKey});e.hangup||e.api.termC(t,e._sessId,e._rtcId),e.webRtc.close(),e.hangup=!0,e.onTermCall()},_onTermC:function(){var e=this;e.hangup=!0,e.termCall()},onTermCall:function(){}};e.exports=function(e){var t=this;o.extend(!0,this,c,e||{}),t.init()}}});