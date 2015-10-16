$(function() {

	var initial = true; // ### used to manually adjust navigation bar

	// ### load main content
	function updateCourseContent() {
		var course_title = systemGetUrlParam("title");
		var course_id = systemGetUrlParam("id");
		var course_type = systemGetUrlParam("type");
		var course_course_id = systemGetUrlParam("course_id");
		var user_id = Cookies2.get('user_id');
		var course_detail = "<div> <tmp class=" + course_type + "><h1>" + course_title + "</h1><br>"
						+ "<h4>Welcome to the course!</h4><br></tmp>";

		var strVar="";
		var index = "course_title_1";
		var user_privilege = Cookies2.get('privilege');
		if ( user_privilege == "teacher")
		{
			strVar += "   <tmp id=\"toggle_content_A_" + index + "\" style=\"display: inline;\">";
			strVar += "      <textarea title=\"What's on your mind?\" class=\"input input_area\" placeholder=\"Type here to create a new post.\" style=\"float: left; width: 100%;\" autocomplete=\"off\" data-postid=\"" + index + "\" id=\"input_area_" + index + "\" ><\/textarea>";
			strVar += "      <button class=\"post_button\" data-postid=\"" + index + "\" id=\"post_button_" + index + "\" style=\"float: right;\">Post<\/button><br>	";  
			strVar += "      <form id=\"type_select_form\">";
			strVar += "      	<input type=\"radio\" name=\"post_type\" value=\"forum\" checked class=\"post_radio\" data-postid=\"" + index + "\" id=\"post_radio_A" + index + "\" > Forum";
			strVar += "      	<input type=\"radio\" name=\"post_type\" value=\"assignment\" class=\"post_radio\" data-postid=\"" + index + "\" id=\"post_radio_B" + index + "\" > Assignment";
			strVar += "      <\/form>";
			strVar += "      <br>	";
			strVar += "   <\/tmp>";
		}
		course_detail += strVar + "</div>";

		var CONTENT_MAIN = "#content_main";
		if (course_title == null || course_id == null)
		{ 
//			course_detail = "Welcome to the course page for COMP"
//				+ course_course_id;
//			initial = false; // ### no need to manually adjust navigation bar
		}
			
		$(CONTENT_MAIN).html(course_detail);
		
		$('#type_select_form input').on('change', function() {
			console.log( $('input[name=post_type]:checked', '#type_select_form').val() );
		});
		
		// ### click, post button, send ajax, refresh website
		$("#post_button_" + index).click( function() {
			var type = $('input[name=post_type]:checked', '#type_select_form').val();			
			var course_id = course_course_id;
			var title = $("#input_area_" + index).val();		// ### !!! currently, can not set title, extra input box needed
			var detail = $("#input_area_" + index).val();
			var data = "func=create_article"
				+ "&type=" + type	
				+ "&course_id=" + course_id
				+ "&user_id=" + user_id
				+ "&title=" + title	
				+ "&detail=" + detail;
			console.log( data );
			$.ajax({
				type : "POST",
				url : "webcontentservlet",
				data : data,
				dataType : "json", // return type
				success : function( data ) {
					//location.reload();
					createContentLast( data, true );
				},
				error : function(data) {
					console.log("database_error" + data.toString());
				}
			});
		} );
	}

	updateCourseContent();

	var w = 200, h = 500, x = d3.scale.linear().range([ 0, w ]), y = d3.scale
			.linear().range([ 0, h ]);

	var vis = d3.select("#content_add").append("div").attr("class", "chart")
			.style("width", w + "px").style("height", h + "px").append(
					"svg:svg").attr("width", w).attr("height", h);

	var partition = d3.layout.partition().value(function(d) {
		return d.size;
	});

	var filename = "/MatchingGame/_src/json/course_"
			+ systemGetUrlParam("course_id") + ".json";

	d3.json(
		filename,
		function(root) {
		    
			var g = vis.selectAll("g").data(partition.nodes(root))
					.enter().append("svg:g").attr(
							"transform",
							function(d) {
								return "translate(" + x(d.y) + ","
										+ y(d.x) + ")";
							}).on("click", click);

			var kx = w / root.dx, ky = h / 1;

			g.append("svg:rect").attr("width", root.dy * kx).attr(
					"height", function(d) {
						return d.dx * ky;
					}).attr("class", function(d) {
				if (d.children != null)
					return "parent";
				else
					return d.type; // set the type of the rect
			});

			g.append("svg:text").attr("transform", transform).attr(
					"dy", ".35em").style("opacity", function(d) {
				return d.dx * ky > 12 ? 1 : 0;
			}).text(function(d) {
				return d.name;
			})

			d3.select(window).on("click", function() {
				click(root);
			})

			function click(d) {
				if (!d.children && initial == false) {
					// ### Jump to some link
					var fileName = 'index_course';
					if ( d.type == 'forum' )
						fileName = 'index_course_forum';
					if ( d.type == 'assignment' )
						fileName = 'index_course_assignment';
//					location.href = "http://localhost/MatchingGame/" + fileName 
//							+ ".html?course_id="
//							+ systemGetUrlParam("course_id")
//							+ "&type="
//							+ d.type
//							+ "&title="
//							+ d.title + "&id=" + d.id;
					return;
				}

//				kx = (d.y ? w - 40 : w) / (1 - d.y);
//				ky = h / d.dx;
//				x.domain([ d.y, 1 ]).range([ d.y ? 40 : 0, w ]);
//				y.domain([ d.x, d.x + d.dx ]);
//
//				var t = g.transition().duration(
//						750).attr(
//						"transform",
//						function(d) {
//							return "translate(" + x(d.y) + ","
//									+ y(d.x) + ")";
//						});
//
//				t.select("rect").attr("width", d.dy * kx).attr(
//						"height", function(d) {
//							return d.dx * ky;
//						});
//
//				t.select("text").attr("transform", transform)
//						.style("opacity", function(d) {
//							return d.dx * ky > 12 ? 1 : 0;
//						});

				if ( d3.event != null && d3.event.stopPropagation != null )
					d3.event.stopPropagation();
			}

			function transform(d) {
				return "translate(8," + d.dx * ky / 2 + ")";
			}

			// ### navigate to current clicked item
			if (initial == true) {
				var course_type = systemGetUrlParam("type");
				var course_id = systemGetUrlParam("id");
				var course_course_id = systemGetUrlParam("course_id");
				$.each(root.children, function( index, value ) {
					var nodeParent = value;
					$.each(value.children, function( index2, value2 ) {
						var node = value2;
						if (course_type == node.type
								&& course_id == node.id) {
							if ( initial == true )
							{
								console.log("zooming:" + node + "," + course_type + "," + node.type + "," + course_id + "," + node.id + ",");
								click(nodeParent);
								initial = false;
							}
						}
					});
				});
			}
			
			initial = false; // ### stop manually adjust navigation bar no matter success or not
		});
});