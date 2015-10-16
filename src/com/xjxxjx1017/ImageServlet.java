package com.xjxxjx1017;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class ImageServlet extends HttpServlet
{
	private static final long serialVersionUID = -1561429950519335923L;

	public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException
	{
		PrintWriter out = res.getWriter( );

		try {
			out.print( getImageName( req ) );
		} catch (Exception e) {
			e.printStackTrace( );
		}
		out.close( );
	}

	public String getAllImageName()
	{

		File imagePath = new File( getServletContext( ).getRealPath( "/images" ) );
		File[] imageFiles = imagePath.listFiles( );
		String rlt = "";
		String getRidOf = "index";
		for ( File image : imageFiles ) {
			if ( image.getName( ).toLowerCase( ).contains( getRidOf.toLowerCase( ) ) == false )
				rlt += image.getName( ) + ",";
		}

		return rlt;
	}

	private String getUnreadImage( File[] imageFiles, String lastImageList )
	{
		Random rn = new Random( );
		int picId = rn.nextInt( imageFiles.length ) + 0;
		String getRidOf = "index";
		if ( imageFiles[picId].getName( ).toLowerCase( ).contains( getRidOf.toLowerCase( ) )
				|| lastImageList.toLowerCase( ).contains( imageFiles[picId].getName( ).toLowerCase( ) ) )
			return getUnreadImage( imageFiles, lastImageList );
		else
			return imageFiles[picId].getName( );
	}

	public String getImageName( HttpServletRequest req )
	{
		File imagePath = new File( getServletContext( ).getRealPath( "/images" ) );
		File[] imageFiles = imagePath.listFiles( );

		// ### using session
		HttpSession session = req.getSession( );
		Integer count;// = (Integer) session.getAttribute( "imageViewCount" );
		int magicNumber = 2;
		int maxImageCount = imageFiles.length - magicNumber;
		String imageNameList;// = (String) session.getAttribute( "imageNameList"
								// );
		String rlt;
		if ( session.isNew( ) == true ) {
			count = 0;
			imageNameList = "";
			rlt = getUnreadImage( imageFiles, imageNameList );
			session.setAttribute( "imageViewCount", count + 1 );
			session.setAttribute( "imageNameList", imageNameList + rlt + "," );
		} else {
			count = (Integer) session.getAttribute( "imageViewCount" );
			imageNameList = (String) session.getAttribute( "imageNameList" );
			rlt = getUnreadImage( imageFiles, imageNameList );
			if ( count < maxImageCount ) {
				session.setAttribute( "imageViewCount", count + 1 );
				session.setAttribute( "imageNameList", imageNameList + rlt + "," );
			} else {
				session.setAttribute( "imageViewCount", 0 );
				session.setAttribute( "imageNameList", "" );
			}
		}

		return rlt;
	}

	public Document loadXMLFromString( String xml ) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance( );
		DocumentBuilder builder = factory.newDocumentBuilder( );
		InputSource is = new InputSource( new StringReader( xml ) );
		return builder.parse( is );
	}
}
