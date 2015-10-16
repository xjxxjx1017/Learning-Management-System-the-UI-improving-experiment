$(function() {
//	var Cookies2 = Cookies.noConflict();
//	Cookies2.set('email', 'abcd', { expires: 14 });
//	Cookies2.set('password', 'efgh', { expires: 14 } );
//	var email_last_time = "" + Cookies2.get('email');
//	var password_last_time = "" + Cookies2.get('password');
//	$("#txt_firstname_signup").val( email_last_time );
//	$("#txt_lastname_signup").val( password_last_time );
	
	$("#btn_submit_signup").on("click", function(e) {
	    e.preventDefault();
	    var s = "http://localhost/MatchingGame/accountservlet?type=signup";
	    s += "&type=" + $("#txt_email_signup").val();
	    s += "&type=" + $("#txt_password_signup").val();
	    s += "&type=" + $("#txt_firstname_signup").val();
	    s += "&type=" + $("#txt_lastname_signup").val();
	    s += "&type=" + $('input[name=privilege_signup]:checked', '#type_select_form_signup').val();
	    alert(s);
		$.get(s, function(data) {
			alert("Sign up successfully");
		});
	});
});