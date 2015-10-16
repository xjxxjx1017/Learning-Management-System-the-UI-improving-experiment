

var Cookies2 = Cookies.noConflict();
var state = Cookies2.get('sign_in_state');
if (state != "true")
	location.href = "http://localhost/MatchingGame/index_login.html";
