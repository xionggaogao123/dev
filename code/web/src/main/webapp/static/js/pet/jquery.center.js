$(document).ready(function(e) {
    jQuery.fn.center=function(){
		    this.css("position","fixed");
			this.css("top",($(window).height()-this.height())/2);
			this.css("left",($(window).width()-this.width())/2+$(window).scrollLeft()+"px");
			return this;
		}
});