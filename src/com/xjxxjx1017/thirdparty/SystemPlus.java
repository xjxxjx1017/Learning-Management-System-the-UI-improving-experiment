package com.xjxxjx1017.thirdparty;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.xjxxjx1017.WebContentServlet;
import com.xjxxjx1017.utils.FileMeta;

public class SystemPlus
{
	public static void outputFileFromServlet( ServletContext context, HttpServletResponse response, String path )
	{
		InputStream inStream = context.getResourceAsStream( path ); // relative path, or file name
		try {
			IOUtils.copyLarge( inStream, response.getOutputStream( ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void print( String s )
	{
		System.out.println( s );
	}

	public static String getFullURL( HttpServletRequest request )
	{
		StringBuffer requestURL = request.getRequestURL( );
		String queryString = request.getQueryString( );

		if ( queryString == null ) {
			return requestURL.toString( );
		} else {
			return requestURL.append( '?' ).append( queryString ).toString( );
		}
	}

	private static Boolean FIRST_TIME_RUN = true;

	public static Boolean getIsFirstTimeRun()
	{
		Boolean r = FIRST_TIME_RUN;
		FIRST_TIME_RUN = false;
		return r;
	}

	public static ArrayList<FileMeta> findFileTableByUserPrivilegeFake( String userName, String articleId )
	{
		String userPriviledge = WebContentServlet.getUserPriviledge( userName );
		if ( userPriviledge.equals( "teacher" ) )	// Real check
			return findFileTableByUserPrivilege( "any", "COMP589", "teacher", articleId );
		else
			return findFileTableByUserPrivilege( userName, "COMP589", "student", articleId );
	}

	public static String file2databaseMeta( FileMeta meta )
	{
		return file2database( meta.getContent( ), meta.getName( ), meta.getTwitter( ), "COMP589", "1998-01-01", meta.getArticle_id( ) );
	}

	private static ArrayList<FileMeta> findFileTableByUserPrivilege( String userName, String userCources,
			String userRole, String articleId )
	{
		String sql = "";
		if ( userRole.equals( "teacher" ) )	// Security problems
			sql = "SELECT * FROM files_upload WHERE article_id='" + articleId + "'";
		else
			sql = "SELECT * FROM files_upload WHERE article_id='" + articleId + "' and uploader='" + userName + "'"; 
		String[] sqlParams = {};
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_EXECUTE, sql, sqlParams );
		ResultSet rs = db.rs;

		ArrayList<FileMeta> list = new ArrayList<FileMeta>( );
		try {
			if ( rs == null )
				SystemPlus.print( "File Not Found" );
			else while (rs.next( )) {
				FileMeta meta = new FileMeta( );
				int upload_id = rs.getInt( "upload_id" );
				String file_name = rs.getString( "file_name" );
				Blob file_data = rs.getBlob( "file_data" );
				String uploader = rs.getString( "uploader" );
				int article_id = rs.getInt(  "article_id" );
				// String course_id = rs.getString("course_id");
				// String publish_time = rs.getString("publish_time");
				meta.setName( file_name );
				meta.setFileIdInDatabase( "" + upload_id );
				meta.setTwitter( uploader );
				meta.setContent( file_data.getBinaryStream( ) );
				meta.setArticle_id( article_id );
				list.add( meta );
			}
		} catch (SQLException e) {
			e.printStackTrace( );
		}

		SystemPlusDbHelper.endDatabase( db );
		return list;
	}

	private static String file2database( InputStream inputStream, String file_name, String uploader, String course_id,
			String publish_time, int article_id )
	{
		String resultFileId = "";
		DbDataSet db = SystemPlusDbHelper.initialDatabase( );

		try {

			if ( getIsFirstTimeRun( ) ) {
				String querySetLimit = "SET GLOBAL max_allowed_packet=1048576000;"; // 10
																					// MB
				db.st.execute( querySetLimit );
			}

			String sql = "INSERT INTO `onlinestudy`.`files_upload`"
					+ "(`upload_id`, `file_name`, `file_data`, `uploader`," + "`course_id`, `publish_time`, `article_id`)"
					+ "VALUES (null, ?, ?, ?, ?, ?, ?);";
			db.pstmt = db.conn.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			db.pstmt.setString( 1, file_name );
			db.pstmt.setBlob( 2, inputStream );
			db.pstmt.setString( 3, uploader );
			db.pstmt.setString( 4, course_id );
			db.pstmt.setString( 5, publish_time );
			db.pstmt.setInt( 6, article_id );

			int row = db.pstmt.executeUpdate( );
			if ( row > 0 ) {
				System.out.println( "A contact was inserted with photo image." );
			}

			ResultSet rs = db.pstmt.getGeneratedKeys( );
			rs.next( );
			resultFileId = "" + rs.getInt( 1 );

		} catch (SQLException ex) {
			ex.printStackTrace( );
		}

		SystemPlusDbHelper.endDatabase( db );
		return resultFileId;
	}
}
