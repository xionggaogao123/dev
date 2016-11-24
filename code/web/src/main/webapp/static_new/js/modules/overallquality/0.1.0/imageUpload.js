
/* global Config */
define(function(require,exports,module){

	function imgUpload(sfn,efn){
	    (function($){//图片上传
	        var $list = $('#fileList'),
	        // 优化retina, 在retina下这个值是2
	            ratio = window.devicePixelRatio || 1,

	        // 缩略图大小
	            thumbnailWidth = 100 * ratio,
	            thumbnailHeight = 100 * ratio,
				fileId,
	        // Web Uploader实例
	            uploader;

	        // 初始化Web Uploader
	        uploader = WebUploader.create({
	        	//验证文件总数量
	            fileNumLimit:1,
	            // 自动上传。
	            auto: true,
	            // swf文件路径
	            swf: '../js/Uploader.swf',
	            // 文件接收服务端。
	            server: '/qualityitem/addPic.do',
	            // 选择文件的按钮。可选。
	            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	            pick: '#filePicker',
	            // 只允许选择文件，可选。
	            accept: {
	                title: 'Images',
	                extensions: 'gif,jpg,jpeg,bmp,png',
	                mimeTypes: 'image/*'
	            }
	        });

	        //$('#filePicker').append('<p class="img-script">支持的文件格式：gif、jpg、jpe、bmp、png，支持文件大小：未限制</p>');

	        // 当有文件添加进来的时候
	        uploader.on( 'fileQueued', function( file ) {
				if(fileId) uploader.removeFile(fileId);
				fileId = file.id;
	            var $li = $(
	                    '<div id="' + file.id + '" class="file-item thumbnail">' +
	                        '<img>' +
	                        '</div>'
	                ),
	                $img = $li.find('img');

	            $list.html( $li );

	            // 创建缩略图
	            uploader.makeThumb( file, function( error, src ) {
	                if ( error ) {
	                    $img.replaceWith('<span>不能预览</span>');
	                    return;
	                }

	                $img.attr( 'src', src );
	            }, thumbnailWidth, thumbnailHeight );
	        });

	        // 文件上传过程中创建进度条实时显示。
	        uploader.on( 'uploadProgress', function( file, percentage ) {
	            var $li = $( '#'+file.id ),
	                $percent = $li.find('.progress span');

	            // 避免重复创建
	            if ( !$percent.length ) {
	                $percent = $('<p class="progress"><span></span></p>')
	                    .appendTo( $li )
	                    .find('span');
	            }

	            $percent.css( 'width', percentage * 100 + '%' );
	        });

	        // 文件上传成功，给item添加成功class, 用样式标记上传成功。
	        uploader.on( 'uploadSuccess', function( file ,response) {
	        	sfn && sfn(response);
	            $( '#'+file.id ).addClass('upload-state-done');
	        });
	 
	        uploader.on( 'uploadError', function( file ,response) {
	            var $li = $( '#'+file.id ),
	                $error = $li.find('div.error');

	            // 避免重复创建
	            if ( !$error.length ) {
	                $error = $('<div class="error"></div>').appendTo( $li );
	            }
	            efn && efn(response);	
	            $error.text('上传失败');
	        });

	        // 完成上传完了，成功或者失败，先删除进度条。
	        uploader.on( 'uploadComplete', function( file ) {
	            $( '#'+file.id ).find('.progress').remove();
	        });
	    })(jQuery)
	}
    module.exports = imgUpload;
})