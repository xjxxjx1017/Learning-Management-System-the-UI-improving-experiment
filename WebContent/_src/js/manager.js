

$(function() {
	// An object to define utility functions and global variables on:
	$.miniMenu = new Object();
	// An object to define internal stuff for the plugin:
	$.miniMenu.i = new Object();
	$.miniMenu.i.savedText = "";
	// $.get("http://localhost/MatchingGame/test2", function(data) {
	// $.miniMenu.i.savedText += data;
	// });
	$.get("http://localhost/MatchingGame/accountservlet", function(data) {
		$.miniMenu.i.savedText += data;
	});
	$("#input_display").css("")
	$("#input_button").click(function() {
		var input = $("#input_submit").val();
		$.miniMenu.i.savedText += input + "<br/>";
		$("#input_display").html($.miniMenu.i.savedText);
		$("#input_submit").val("");
	});
});

$(function() {
	$.get("http://localhost/MatchingGame/accountservlet?type=test", function(
			data) {
		$.miniMenu.i.users = data;
		$("#viz_users").html($.miniMenu.i.users);
	});
	$("#btn_delete")
			.on(
					"click",
					function(e) {
						e.preventDefault();
						$
								.get(
										"http://localhost/MatchingGame/accountservlet?type=manager&type=delete",
										function(data) {
											alert("All data deleted");
										});
					});
});

$(function() {
	$("#change_background").click(function() {
		$.get("http://localhost/MatchingGame/image", function(data) {
			var ss = "url('images/" + data + "')";
			$("body").css("background-image", ss);
			// $("body").css( "background-image", "url('images/" + data + "')"
			// );
		});
	});
});

$(function() {
	var dataset = [], tmpDataset = [], i, j;

	for (i = 0; i < 5; i++) {
		for (j = 0, tmpDataset = []; j < 3; j++) {
			tmpDataset.push("Row:" + i + ",Col:" + j);
		}
		dataset.push(tmpDataset);
	}

	d3.select("#viz").append("table").style("border-collapse", "collapse")
			.style("border", "2px black solid")

			.selectAll("tr").data(dataset).enter().append("tr")

			.selectAll("td").data(function(d) {
				return d;
			}).enter().append("td").style("border", "1px black solid").style(
					"padding", "10px").on("mouseover", function() {
				d3.select(this).style("background-color", "grey")
			}).on("mouseout", function() {
				d3.select(this).style("background-color", "white")
			}).text(function(d) {
				return d;
			}).style("font-size", "12px");
});

/*
$(function() {
	var imagePrefix = "image";
	var imagePostfix = ".jpg";
	var imlocation = "images/";
	var sequence = [ 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8 ];
	var finished = 0;
	var finishedTag = [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ];
	var rsequence = [];
	shuffle(sequence);

	document
			.write("<table id='game' border = 5 bordercolor = red align = center>");
	var count = 0;
	for (var i = 0; i < 4; i++) {
		document.write("<tr>");
		for (var j = 0; j < 4; j++) {
			var r = Math.floor(Math.random() * 8 + 1);
			document.write("<td " + "id=grid" + [ i * 4 + j ]
					+ " class = 'cells' >");
			count++;
			document.write("<img " + "id=pic" + [ i * 4 + j ]
					+ " class='autoResizeImage' src='" + imlocation
					+ imagePrefix + sequence[i * 4 + j] + imagePostfix + "' >");
			document.write("</td>");
		}
		document.write("</tr>");
	}
	document.write("</table>");

	$("img").hide();

	$("td").click(function() {
		// $(this).show();
		$($(this).find("img")).fadeIn(300).delay(1000).fadeOut(300);
		itemClicked($($(this).find("img")));
	});

	function getTime() {
		var date = new Date();
		return pad(date.getHours(), 2) + ':' + pad(date.getMinutes(), 2) + ':'
				+ pad(date.getSeconds(), 2);
	}

	function pad(number, length) {
		var str = '' + number;
		while (str.length < length) {
			str = '0' + str;
		}
		return str;
	}

	function resetButton(button) {
		setTimeout(function() {
			$(button).removeClass('ui-btn-active');
		}, 500);
	}

	// ---------------------------------------------

	function itemClicked(item) {
		var curId = $(item).attr('id');
		var count = 0;
		for (var i = 0; i < 16; i++) {
			var visible = $('#pic' + i).is(":visible");
			if (visible)
				count++;
		}
		if (count - finished == 2) {
			var tryMatch = [];
			for (var i = 0; i < 16; i++) {
				var visible = $('#pic' + i).is(":visible");
				if (visible && finishedTag[i] == 0)
					tryMatch.push(i);
			}
			rlt = sequence[tryMatch[0]] == sequence[tryMatch[1]];
			// console.log( "" + sequence[tryMatch[0]] + "," +
			// sequence[tryMatch[1]] );
			// console.log( rlt );
			if (rlt == true) {
				finished += 2;
				finishedTag[tryMatch[0]] = 1;
				finishedTag[tryMatch[1]] = 1;
				$('#pic' + tryMatch[0]).stop(true, true);
				$('#pic' + tryMatch[0]).show();
				$('#grid' + tryMatch[0]).unbind("click");
				$('#pic' + tryMatch[1]).stop(true, true);
				$('#pic' + tryMatch[1]).show();
				$('#grid' + tryMatch[1]).unbind("click");
			}
		}
		if (finished == 16)
			alert("Game finished. Congradulations!");
	}

	function shuffle(o) {
		for (var j, x, i = o.length; i; j = Math.floor(Math.random() * i), x = o[--i], o[i] = o[j], o[j] = x)
			;
		return o;
	}
});*/