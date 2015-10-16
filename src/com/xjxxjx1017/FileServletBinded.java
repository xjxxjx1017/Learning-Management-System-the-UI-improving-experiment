package com.xjxxjx1017;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xjxxjx1017.thirdparty.SystemPlus;
import com.xjxxjx1017.utils.FileMeta;
import com.xjxxjx1017.utils.FileMetaWrapper;
import com.xjxxjx1017.utils.FileMultipartRequestHandler;

//import com.fasterxml.jackson.databind.ObjectMapper;

//this to be used with Java Servlet 3.0 API
@MultipartConfig
public class FileServletBinded extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	// this will store uploaded files
	private static List<FileMeta> files = new LinkedList<FileMeta>( );

	/***************************************************
	 * URL: /upload doPost(): upload the files and other parameters
	 ****************************************************/

	protected void doPost( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		// ### Uploading functionality...

		// 1. Upload File Using Java Servlet API
		// files.addAll(MultipartRequestHandler.uploadByJavaServletAPI(request));

		// 1. Upload File Using Apache FileUpload
		// ### meta: response for all the newly uploaded files
		List<FileMeta> meta = FileMultipartRequestHandler.uploadByApacheFileUpload( request );
		files.addAll( meta );

		// Remove some files
		while (files.size( ) > 20) {
			files.remove( 0 );
		}

		// 2. Set response type to json
		response.setContentType( "application/json" );

		// 3. Convert List<FileMeta> into JSON format
		ObjectMapper mapper = new ObjectMapper( );
		FileMetaWrapper wrapper = new FileMetaWrapper( );
		wrapper.files = meta;

		for ( FileMeta m : meta ) {
			SystemPlus.file2databaseMeta( m );
		}
		// ### send all list (json) to client as response
		// 4. Send result to client
		mapper.writeValue( response.getOutputStream( ), wrapper );
	}

	/***************************************************
	 * URL: /upload?f=value doGet(): get file of index "f" from List
	 * <FileMeta> as an attachment
	 ****************************************************/
	protected void doGet( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{
		// ### Downloading functionality...
		// 1. Get f from URL upload?f="?"
		String value = request.getParameter( "f" );
		if ( value == null )
			return;

		// 2. Get the file of index "f" from the list "files"
		FileMeta getFile = files.get( Integer.parseInt( value ) );

		try {
			// 3. Set the response content type = file content type
			response.setContentType( getFile.getType( ) );

			// 4. Set header Content-disposition
			response.setHeader( "Content-disposition", "attachment; filename=\"" + getFile.getName( ) + "\"" );

			// 5. Copy file inputstream to response outputstream
			InputStream input = getFile.getContent( );
			OutputStream output = response.getOutputStream( );
			byte[] buffer = new byte[1024 * 10];

			for ( int length = 0; (length = input.read( buffer )) > 0; ) {
				output.write( buffer, 0, length );
			}

			output.close( );
			input.close( );
		} catch (IOException e) {
			e.printStackTrace( );
		}

	}
}
