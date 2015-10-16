package com.xjxxjx1017;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xjxxjx1017.thirdparty.SystemPlus;

@WebServlet("/AccountServlet")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AccountServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		performTask(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		performTask(request, response);
	}

	private void performTask(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		Map<String, String[]> paramMap = request.getParameterMap( );
		String[] params = paramMap.get( "type" );
		if ( params != null && params.length > 0 )
		{
			String type = params[0];
			switch ( type )
			{
				case "signup":
					out.println( AccountDealing.signup( params ) );
					break;
				case "login":
					out.println( AccountDealing.login( params ) );
					break;
				case "manager":
					out.println( AccountDealing.manager( params ) );
					break;
				default:
					SystemPlus.print( "Command not found" );
					break;
			}
		}
		out.close();
	}
}
