/**
 * Created by admin on 2016/12/12.
 */
(function($){

    var defaultSettings={
       userId:"name",
       propertyId:"propertyId"
    };

    var methods={
        init:function(options){
            return this.each(function(){
                var $this=$(this);
                var settings=$this.data('customTool');
                if(typeof(settings)=='undefined'){
                    settings=$.extend({},defaultSettings,options);
                    $this.data('customTool',settings);
                }else{
                    settings=$.extend({},settings,options);
                    $this.data('customTool',settings);
                }
            });
        },
        mouseEnter:function(){
            var settings=this.data('customTool');
            var propertyId=settings.propertyId;
            this.closest('p').append('<div class="cickEnter" style="position: relative;top: -37px;left: 58px;color: black;' +
                'width: 200px;background-color: white;" id="'+propertyId+'"><span class="enter" >'+settings.userId+'</span></div>');
        },
        mouseLeave:function(){
            var propertyId=settings.propertyId;
            $(propertyId).remove();
        },
        destroy:function(options){
            return $(this).each(function(){
                var $this=$(this);
                $this.removeData('customTool');
            })
        }
    };

    $.fn.customTool=function () {
        var method=arguments[0];
        if(methods[method]){
            method=methods[method];
            arguments=Array.prototype.slice.call(arguments,1);
        }else if(typeof (method)=='object'||!method){
            method=methods.init;
        }else{
            $.error('Method'+ method+'does not exist on jQuery.customTool');
            return this;
        }
        return method.apply(this,arguments);
    }

})(jQuery);