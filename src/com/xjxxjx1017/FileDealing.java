package com.xjxxjx1017;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xjxxjx1017.thirdparty.DbDataSet;
import com.xjxxjx1017.thirdparty.SystemPlus;
import com.xjxxjx1017.thirdparty.SystemPlusDbHelper;
import com.xjxxjx1017.utils.FileMeta;

public class FileDealing
{
	static public void doGetTempGenerateFileListUIForUser( HttpServletRequest request, HttpServletResponse response )
			throws IOException
	{
		String name = request.getParameter( "name" );
		String articleId = request.getParameter( "article_id" );
		PrintWriter writer = response.getWriter( );
		ArrayList<FileMeta> list = SystemPlus.findFileTableByUserPrivilegeFake( name, articleId );
		
		writer.println( "<table style=\"width:100%\">" );
		for ( FileMeta file : list ) {
			writer.println( "<tr>" );
			writer.println( "<td>" + file.getName( )
					+ "</\td>  <td>" + WebContentServlet.getUserName( file.getTwitter( ) ) );
			writer.println( "</\td><td><a href=\"" + file.getUrl( ) + "\" download=\"" + file.getName( ) + "\">"
					+ "<button class=\"btn btn-large btn-primary\" type=\"submit\">Download</button>" + "</a>" );
			writer.println( "</\td><td><a href=\"" + file.getDeleteUrl( ) + "\">"
					+ "<button class=\"btn btn-danger delete\" type=\"submit\">Delete</button>" + "</a></\td>" + "<br>" );
			writer.println( "</\tr>" );
		}
		writer.println( "</\table>" );
		writer.close( );
	}

	static public void doGetDelete( HttpServletRequest request, HttpServletResponse response )
	{
		// get upload id from URL's parameters
		String uploadId = request.getParameter( "id" );

		String sql = "DELETE FROM files_upload WHERE upload_id=?";
		String[] params = { uploadId };
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_UPDATE, sql, params );

		SystemPlusDbHelper.endDatabase( db );
	}
	

    private static final int BUFFER_SIZE = 4096;   
	static public void doGetDownload( ServletContext context, HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        // get upload id from URL's parameters
        String uploadId = request.getParameter("id");
        
        String[] params = { uploadId };
        String sql = "SELECT * FROM files_upload WHERE upload_id = ?";
        DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_EXECUTE, sql, params );
         
        try {
            if (db.rs.next()) {
                // gets file name and file blob data
                String fileName = db.rs.getString("file_name");
                Blob blob = db.rs.getBlob("file_data");
                InputStream inputStream = blob.getBinaryStream();
                int fileLength = inputStream.available();
                 
                System.out.println("fileLength = " + fileLength);
 
                // sets MIME type for the file download
                String mimeType = context.getMimeType(fileName);
                if (mimeType == null) {        
                    mimeType = "application/octet-stream";
                }              
                 
                // set content properties and header attributes for the response
                response.setContentType(mimeType);
                response.setContentLength(fileLength);
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", fileName);
                response.setHeader(headerKey, headerValue);
 
                // writes the file to the client
                OutputStream outStream = response.getOutputStream();
                 
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                 
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                 
                inputStream.close();
                outStream.close();             
            } else {
                // no file found
                response.getWriter().print("File not found for the id: " + uploadId);  
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.getWriter().print("SQL Error: " + ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            response.getWriter().print("IO Error: " + ex.getMessage());
        } 
        SystemPlusDbHelper.endDatabase( db );
    }
}
