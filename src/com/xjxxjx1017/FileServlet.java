package com.xjxxjx1017;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xjxxjx1017.thirdparty.SystemPlus;

@WebServlet("/FileServlet")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FileServlet() {
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

	private void performTask(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		Map<String, String[]> paramMap = request.getParameterMap( );
		String[] params = paramMap.get( "type" );
		if ( params != null && params.length > 0 )
		{
			String type = params[0];
			switch ( type )
			{
				case "delete":
					FileDealing.doGetDelete( request, response );
					break;
				case "check":
					FileDealing.doGetTempGenerateFileListUIForUser( request, response );
					break;
				case "download":
					FileDealing.doGetDownload( getServletContext(), request, response );
				default:
					SystemPlus.print( "Command not found" );
					break;
			}
		}
	}

}
