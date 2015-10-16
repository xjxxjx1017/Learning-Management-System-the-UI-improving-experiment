/*
 * jQuery File Upload Plugin JS Example 8.9.1
 * https://github.com/blueimp/jQuery-File-Upload
 *
 * Copyright 2010, Sebastian Tschan
 * https://blueimp.net
 *
 * Licensed under the MIT license:
 * http://www.opensource.org/licenses/MIT
 */

/* global $, window */

$(function() {
	'use strict';

	// Initialize the jQuery File Upload widget:
	$('#fileupload').fileupload({
		// Uncomment the following to send cross-domain cookies:
		// xhrFields: {withCredentials: true},
		url : 'fileservletbinded'
	}).bind('fileuploadsubmit', function(e, data) {
		// The example input, doesn't have to be part of the upload
		// form:
		var twitter = $('#input_check');
		data.formData = {
			twitter : twitter.val(),
			article_id : 123
		};
	});

	// Enable iframe cross-domain access via redirect option:
	$('#fileupload').fileupload('option', 'redirect',
			window.location.href.replace(/\/[^\/]*$/, '/cors/result.html?%s'));
//
//	if (window.location.hostname === 'blueimp.github.io') {
//	} else {
//		// Load existing files:
//		$('#fileupload').addClass('fileupload-processing');
//		$.ajax({
//			// Uncomment the following to send cross-domain cookies:
//			// xhrFields: {withCredentials: true},
//			url : $('#fileupload').fileupload('option', 'url'),
//			dataType : 'json',
//			context : $('#fileupload')[0]
//		}).always(function() {
//			$(this).removeClass('fileupload-processing');
//		}).done(function(result) {
//			$(this).fileupload('option', 'done').call(this, $.Event('done'), {
//				result : result
//			});
//		});
//	}

});
