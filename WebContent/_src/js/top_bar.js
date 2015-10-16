
$(function() {
	var userName = Cookies2.get('sign_in_name');
	//console.log($("#user_name"));
	
	if ( userName != null )
		$("#user_name").text( userName );
	else
		$("#user_name").text( "" );
	$("#btn_logout").on("click", function(e) {
	    e.preventDefault();

		Cookies2.remove('email');
		Cookies2.remove('password');
		Cookies2.remove('sign_in_state');
		Cookies2.remove('sign_in_name');
		
		location.href = "http://localhost/MatchingGame/index_login.html";
	});
});