// JavaScript Document
;(function($){
		$.fn.extend({
				popup:function(option){
						var defaults = {
							oWidth:900,
							oHeight:500,
							url:null
						}
						var option = $.extend(defaults,option);
						return this.each(function(){
								var _oWidth = option.oWidth;
								var _oHeight = option.oHeight;
								var _url  = option.url;
								var oLeft = ($(window).width() - _oWidth) / 2;
								var oTop = ($(window).height() - _oHeight) / 2 + $(document).scrollTop();
								var onOff = true;
									$(this).click(function(){
										
											if($('#maskLayer').length ==0){
											$('body').append('<div id="maskLayer" style="display:none;z-index:99;'
																				+'left:0;position:absolute;top: 0;filter:alpha(opacity=30);'
																				+ 'background:#000;moz-opacity:0.3;opacity:.30"></div>'
																				+'<div id="maskWrap_bg"><div id="maskWrap" style="z-index:100;'
																				+ 'left:' + oLeft + 'px;position:fixed;background:#fff;'
																				+'height:' + _oHeight + 'px;top:' + oTop + 'px;width:' + _oWidth + 'px">'
																				+'<span class="pop_close_btn" style="position: absolute;cursor: pointer;'
																				+ 'outline: none;top:163px;left:186px;z-index: 100;width: 87px;height: 33px;'
																				+'overflow: hidden;background-image: url(feedback-close.png);'
																				+ '_background: none;display:none;"></span></div></div>');
										 }
												var _mask = $('#maskLayer');
												var _wrap = $('#maskWrap');
												var _close = $('.pop_close_btn');
										_mask.css({
												"width":$(window).outerWidth(),
												"height":$(document.body).height()
											})

													var _mask = $('#maskLayer');
													var _wrap = $('#maskWrap');
													var _close = $('.pop_close_btn');
													var _if = $('#maskWrapiframe');
													if(_url != null){
													_wrap.append('<iframe frameborder="0" style="width:' + _oWidth + 'px;height:' 
																				+ _oHeight + 'px;" name="maskWrapiframe" id="maskWrapiframe" '
																				+ 'src="' + _url + '" hspace="0" ></iframe>')
													}
			
													_mask.show();
													_wrap.show();
													_close.show();
													
													_close.on('click',function(){
													var _mask = $('#maskLayer');
													var _wrap = $('#maskWrap');
													var _close = $('.pop_close_btn');
													var _if = $('#maskWrapiframe');
													_if.remove();
													_mask.remove();
													_wrap.remove();
													_close.remove();
											})

									})
								
									
						})
						
				}
		})
})(jQuery)