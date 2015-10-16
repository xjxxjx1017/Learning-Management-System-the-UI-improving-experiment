
$(function () {
	$("#btn_check").click(function() {
		$.get("http://localhost/MatchingGame/fileservlet?type=check&name=" + $("#input_check").val(), function(data) {
				$("#files_information").html(data);
			});
		$("#input_check").val("");
	});
});