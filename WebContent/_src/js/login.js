

$(function() {
	// ### do this only for demo, huge safety problem
	var Cookies2 = Cookies.noConflict();

	// ### buttons
	$("#btn_submit").on("click", function(e) {
		e.preventDefault();
		var s = "http://localhost/MatchingGame/accountservlet?type=login";
		s += "&type=" + $("#txt_email").val();
		s += "&type=" + $("#txt_password").val();
		alert(s);
		$.get(s, function(data) {
			var ss = "";
			var obj = jQuery.parseJSON( data );
			console.log( data.toString() );
			if (obj["ID"] == null) {
				ss += "Wrong password or target account doesn't exist.\n";
				ss += data;
				alert(ss);
			}
			else
			{
				ss += "success";
				ss += obj.toString();
				alert( ss );
				// ### do this only for demo, huge safety problem
				// var Cookies2 = Cookies.noConflict(); // redefine cause null point bug
				Cookies2.set('email', $("#txt_email").val(), { expires: 14 });
				Cookies2.set('password', $("#txt_password").val(), { expires: 14 } );
				Cookies2.set('sign_in_state', 'true', { expires: 1 } );
				Cookies2.set('sign_in_name', $("#txt_email").val(), { expires: 1 } );
				Cookies2.set('user_id', obj["ID"], { expires: 1 } );
				Cookies2.set('user_name', obj["FirstName"] + " " + obj["LastName"], { expires: 1 } );
				Cookies2.set('privilege', obj["Privilege"], { expires: 1 } );
				
				location.href = "http://localhost/MatchingGame/index.html";
			}
		});
	});
});