var colCount = 0;
var colWidth = 0;
var margin = 20;
var windowWidth = 0;
var blocks = [];
var leftOffset = 0;
var topOffset = 0;

$(function(){
	$(window).resize(setupBlocks);
	loadContent();
});

function setupBlocks() {

	leftOffset = $("#post_content").offset().left;
	topOffset = $("#post_content").offset().top;
	console.log(leftOffset + "," + topOffset);
	
	windowWidth = $("#post_content").width();// $(window).width() - leftOffset;
	colWidth = $('.block').outerWidth();
	blocks = [];

	// Calculate the margin so the blocks are evenly spaced within the window
	colCount = Math.floor(windowWidth/(colWidth+margin*2));
	spaceLeft = (windowWidth - ((colWidth*colCount)+(margin*(colCount-1)))) / 2;
	//console.log(spaceLeft);
	
	for(var i=0;i<colCount;i++){
		blocks.push(margin);
	}
	positionBlocks();
}

function positionBlocks() {
	$('.block').each(function(){
		var min = Array.min(blocks);
		var index = $.inArray(min, blocks);
		var leftPos = margin+(index*(colWidth+margin)) + leftOffset;
		$(this).css({
			'left':leftPos+spaceLeft+'px',
			'top': topOffset + min+'px'
		});
		blocks[index] = min+$(this).outerHeight()+margin;
	});	
}

// Function to get the Min value in Array
Array.min = function(array) {
    return Math.min.apply(Math, array);
};

function loadContent() {

	var course_id = systemGetUrlParam("course_id");			
	
	var data = "func=fetch_article"
		+ "&course_id=" + course_id;
	
	console.log( data );
	$.ajax({
		type : "POST",
		url : "webcontentservlet",
		data : data,
		dataType : "json", // return type
		success : function(data) {
			console.log( data.toString() );
			createContent( data, false );
		},
		error : function(data) {
			console.log("database_error" + data.toString());
		}
	});
}

function createContentSingle( value, isPrepend )
{
	var str = value.detail;
	var comment = value.posts;
	var article_id = value.id;
	var type = value.type;
	if ( type == "forum" )
	{
		if ( isPrepend )
			$("#post_content").prepend(getContentForum(str, comment, article_id));
		else
			$("#post_content").append(getContentForum(str, comment, article_id));
		create_post( comment, article_id );
		$('#toggle_button_' + article_id).click( forum_toggle_button_click );
		$('#reply_button_expand_' + article_id).click( forum_toggle_button_click );
		$('#reply_button_' + article_id).click( forum_post_button_click );
	}
	else if ( type == "assignment" )
	{
		if ( isPrepend )
			$("#post_content").prepend(getContentAssignment(str, comment, article_id));
		else
			$("#post_content").append(getContentAssignment(str, comment, article_id));
		create_post( comment, article_id );
		$('#toggle_button_' + article_id).click( forum_toggle_button_click );
		$('#reply_button_expand_' + article_id).click( forum_toggle_button_click );
		$('#reply_button_' + article_id).click( forum_post_button_click );
		bind_upload_widget('#fileupload_', article_id);
		$.get("http://localhost/MatchingGame/fileservlet?type=check" + 
				"&name=" + Cookies2.get('user_id') +
				"&article_id=" + article_id, setFilesInfor( article_id ));
	}
}

function createContentLast( data, isPrepend )
{
	var value = data.articles[data.articles.length-1];
	createContentSingle( value, isPrepend );
	setupBlocks();
}

function createContent( data, isPrepend ){
	
	for ( var i = data.articles.length-1; i >= 0; i-- )
	{
		var index = i;
		var value = data.articles[i];
		createContentSingle( value, isPrepend );
	}
	
	setupBlocks();
}

function setFilesInfor( article_id )
{
	return function(data) {
		$("#files_information_" + article_id).html(data);
		setupBlocks();
	}
}

function getContentAssignment( str, commentList, index )
{
	var user_privilege = Cookies2.get("privilege");
	
	var strVar="";
	strVar += "<div class=\"block\" data-postid=\"" + index + "\" style=\"left: 245px; top: 260px;\">";
	strVar += "   <p>" + str + "<\/p>";

	strVar += "";	// write something here to add a assignment submit section
	strVar += "<div id=\"files_information_" + index + "\"><\/div>";
	
	strVar += "   <tmp id=\"toggle_content_A_" + index + "\" style=\"display: none;\">";
	
	if ( user_privilege == "student" )
	{

		strVar += "      <hr style=\"border-color: #lightgrey;\">";
		strVar += "         <div class=\"container\" style=\"width:100%\">";
		strVar += "				<h4>Upload assignments<\/h4>";
		strVar += "				<!-- The file upload form used as target for the file upload widget -->";
		strVar += "				<form id=\"fileupload_" + index + "\" action=\"\/\/jquery-file-upload.appspot.com\/\" method=\"POST\" enctype=\"multipart\/form-data\">";
		strVar += "					<!-- Redirect browsers with JavaScript disabled to the origin page -->";
		strVar += "					<noscript>";
		strVar += "						<input type=\"hidden\" name=\"redirect\" value=\"https:\/\/blueimp.github.io\/jQuery-File-Upload\/\">";
		strVar += "					<\/noscript>";
		strVar += "					<!-- The fileupload-buttonbar contains buttons to add\/delete files and start\/cancel the upload -->";
		strVar += "					<div class=\"row fileupload-buttonbar\">";
		strVar += "						<div class=\"col-lg-7\">";
		strVar += "							<!-- The fileinput-button span is used to style the file input field as button -->";
		strVar += "							<span class=\"btn btn-success fileinput-button\"> <i class=\"glyphicon glyphicon-plus\"><\/i> <span>Add files...<\/span> <input type=\"file\" name=\"files[]\" multiple>";
		strVar += "							<\/span>";
		strVar += "							<button type=\"submit\" class=\"btn btn-primary start\">";
		strVar += "								<i class=\"glyphicon glyphicon-upload\"><\/i> <span>Start upload<\/span>";
		strVar += "							<\/button>";
		strVar += "							<button type=\"reset\" class=\"btn btn-warning cancel\">";
		strVar += "								<i class=\"glyphicon glyphicon-ban-circle\"><\/i> <span>Cancel upload<\/span>";
		strVar += "							<\/button>";
		strVar += "							<button type=\"button\" class=\"btn btn-danger delete\">";
		strVar += "								<i class=\"glyphicon glyphicon-trash\"><\/i> <span>Delete<\/span>";
		strVar += "							<\/button>";
		strVar += "							<input type=\"checkbox\" class=\"toggle\">";
		strVar += "							<!-- The global file processing state -->";
		strVar += "							<span class=\"fileupload-process\"><\/span>";
		strVar += "						<\/div>";
		strVar += "						<!-- The global progress state -->";
		strVar += "						<div class=\"col-lg-5 fileupload-progress fade\">";
		strVar += "							<!-- The global progress bar -->";
		strVar += "							<div class=\"progress progress-striped active\" role=\"progressbar\" aria-valuemin=\"0\" aria-valuemax=\"100\">";
		strVar += "								<div class=\"progress-bar progress-bar-success\" style=\"width: 0%;\"><\/div>";
		strVar += "							<\/div>";
		strVar += "							<!-- The extended global progress state -->";
		strVar += "							<div class=\"progress-extended\">&nbsp;<\/div>";
		strVar += "						<\/div>";
		strVar += "					<\/div>";
		strVar += "					<!-- The table listing the files available for upload\/download -->";
		strVar += "					<table role=\"presentation\" class=\"table table-striped\">";
		strVar += "						<tbody class=\"files\"><\/tbody>";
		strVar += "					<\/table>";
		strVar += "				<\/form>";
		strVar += "			<\/div>";
	}
	strVar += "      <hr style=\"border-color: #lightgrey;\">";
	strVar += "      <post id=\"post_" + index + "\"><\/post>";
	strVar += "      <textarea title=\"What's on your mind?\" class=\"input input_area\" placeholder=\"Type here to reply.\" style=\"float: left; width: 100%;\" autocomplete=\"off\" data-postid=\"" + index + "\" id=\"input_area_" + index + "\" ><\/textarea>";
	strVar += "      <button class=\"reply_button\" data-postid=\"" + index + "\" id=\"reply_button_" + index + "\" style=\"float: right;\">Reply<\/button>	";
	strVar += "      <br><br>	<br>						";
	
	strVar += "      <hr style=\"border-color: #lightgrey;\">";
	strVar += "   <\/tmp>";
	strVar += "   <button class=\"toggle_button\" data-postid=\"" + index + "\" id=\"toggle_button_" + index + "\" style=\"float: right;\" data-count=" + commentList.length + ">Comment(" + commentList.length + ") v<\/button>	";
	strVar += "   <tmp id=\"toggle_content_B_" + index + "\" style=\"display: inline;\">";
	strVar += "      <button class=\"reply_button_expand\" data-postid=\"" + index + "\" id=\"reply_button_expand_" + index + "\" style=\"float: right;\">Reply<\/button>				";
	strVar += "   <\/tmp>";
	strVar += "<\/div>";
	return strVar;
}

function getContentForum( str, commentList, index )
{
	var strVar="";
	strVar += "<div class=\"block\" data-postid=\"" + index + "\" style=\"left: 245px; top: 260px;\">";
	strVar += "   <p>" + str + "<\/p>";
	strVar += "   <tmp id=\"toggle_content_A_" + index + "\" style=\"display: none;\">";
	strVar += "      <hr style=\"border-color: #lightgrey;\">";
	strVar += "      <post id=\"post_" + index + "\"><\/post>";
	strVar += "      <textarea title=\"What's on your mind?\" class=\"input input_area\" placeholder=\"Type here to reply.\" style=\"float: left; width: 100%;\" autocomplete=\"off\" data-postid=\"" + index + "\" id=\"input_area_" + index + "\" ><\/textarea>";
	strVar += "      <button class=\"reply_button\" data-postid=\"" + index + "\" id=\"reply_button_" + index + "\" style=\"float: right;\">Reply<\/button>	";
	strVar += "      <br><br>	<br>						";
	strVar += "      <hr style=\"border-color: #lightgrey;\">";
	strVar += "   <\/tmp>";
	strVar += "   <button class=\"toggle_button\" data-postid=\"" + index + "\" id=\"toggle_button_" + index + "\" style=\"float: right;\" data-count=" + commentList.length + ">Comment(" + commentList.length + ") v<\/button>	";
	strVar += "   <tmp id=\"toggle_content_B_" + index + "\" style=\"display: inline;\">";
	strVar += "      <button class=\"reply_button_expand\" data-postid=\"" + index + "\" id=\"reply_button_expand_" + index + "\" style=\"float: right;\">Reply<\/button>				";
	strVar += "   <\/tmp>";
	strVar += "<\/div>";

	return strVar;
}

function create_post_last( commentList, article_id )
{
	var i = commentList.length - 1;
	var strVar="";
	strVar += "      <p>";
	strVar += "         " + commentList[i].user_id + " Said: <br> " + commentList[i].content;
	strVar += "      <\/p>";
	strVar += "      <hr style=\"border-color: #lightgrey;\">";
	$("#post_" + article_id).append( strVar );
	setupBlocks();
}

function create_post( commentList, article_id )
{
	for ( var i = 0; i < commentList.length; i++ )
	{
		var strVar="";
		strVar += "      <p>";
		strVar += "         " + commentList[i].user_id + " Said: <br> " + commentList[i].content;
		strVar += "      <\/p>";
		strVar += "      <hr style=\"border-color: #lightgrey;\">";
		$("#post_" + article_id).append( strVar );
	}
}

function forum_post_button_click( btn ){
	var postid = btn.target.getAttribute('data-postid');
	var course_title = systemGetUrlParam("title");
	var course_id = systemGetUrlParam("id");
	var course_type = systemGetUrlParam("type");
	var course_course_id = systemGetUrlParam("course_id");

	var article_id = postid;						
	var user_id = Cookies2.get('user_id');
	var parent_post_id = postid;				
	var title = $("#input_area_" + postid).val();	// ### Fake Data for title
	var content = title;		
	
	var data = "func=create_post"
		+ "&article_id=" + article_id	
		+ "&user_id=" + user_id
		+ "&parent_post_id=" + parent_post_id	
		+ "&title=" + title
		+ "&content=" + content;
	
	console.log( data );
	$.ajax({
		type : "POST",
		url : "webcontentservlet",
		data : data,
		dataType : "json", // return type
		success : createPostSuccess( article_id ),
		error : function(data) {
			console.log("database_error" + data.toString());
		}
	});
}

function createPostSuccess( article_id )
{	
	return function( data ) {
		create_post_last( data, article_id );
	}
}

function forum_toggle_button_click( btn ) {
	var postid = btn.target.getAttribute('data-postid');
	var count = btn.target.getAttribute('data-count');
	var hidden = $("#toggle_content_A_"+postid).css( "display" ); // ### value = inline or none
	if ( hidden == 'inline' )
	{
		$("#toggle_content_A_"+postid).css( "display", "none" );
		$("#toggle_content_B_"+postid).css( "display", "inline" );
		$("#toggle_button_"+postid).html( "Comment(" + count + ") v" );
	}
	else if ( hidden == 'none' )
	{
		$("#toggle_content_A_"+postid).css( "display", "inline" );
		$("#toggle_content_B_"+postid).css( "display", "none" );
		$("#toggle_button_"+postid).html( "Collapse ^" );
	}
		
	//console.log("Perfect" + postid + hidden);
	setupBlocks();
}

function bind_upload_widget( name, id )
{
	var widgetName = name +id;	
	
	// Initialize the jQuery File Upload widget:
	$(widgetName).fileupload({
		url : 'fileservletbinded'
	}).bind('fileuploadsubmit', bind_upload_widget_d(id)).bind('fileuploaddone', bind_upload_finish_call(id));

	// Enable iframe cross-domain access via redirect option:
	$(widgetName).fileupload('option', 'redirect',
			window.location.href.replace(/\/[^\/]*$/, '/cors/result.html?%s'));
}

function bind_upload_finish_call(id) {
	return function(e, data) {
		createDownloadLink_last(id);
	};
}

function createDownloadLink_last( article_id )
{
	$.get("http://localhost/MatchingGame/fileservlet?type=check" + 
			"&name=" + Cookies2.get('user_id') +
			"&article_id=" + article_id, setFilesInfor( article_id ));
}

function bind_upload_widget_d(id) {
	return function(e, data) {
		data.formData = {
				twitter : Cookies2.get('user_id'),
				article_id : id
			};
	};
}


