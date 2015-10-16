package com.xjxxjx1017;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xjxxjx1017.thirdparty.DbDataSet;
import com.xjxxjx1017.thirdparty.SystemPlus;
import com.xjxxjx1017.thirdparty.SystemPlusDbHelper;

@WebServlet("/WebContentServlet")
public class WebContentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WebContentServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getWebContent( request, response );
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getWebContent( request, response );
	}

	private void getWebContent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType("text/json");
		String func = request.getParameter( "func" );
		String url = request.getParameter( "url" );
		String str = request.getParameter( "str" );
		if ( url != null )
			SystemPlus.outputFileFromServlet( getServletContext( ), response, url );
		if ( str != null )
			writeStr( str );
		PrintWriter out = response.getWriter();
		String outStr = "";
		if ( func.equals( "create_post" ) )
		{
			outStr = createPost( request.getParameter( "article_id"), 
					request.getParameter( "user_id"), 
					request.getParameter( "parent_post_id"), 
					request.getParameter( "title"), 
					request.getParameter( "content") );
		}
		else if ( func.equals( "create_article" ) )
		{
			outStr = createArticle( request.getParameter( "type"), 
					request.getParameter( "course_id"), 
					request.getParameter( "user_id"),
					request.getParameter( "title"), 
					request.getParameter( "detail") );
		}
		else if ( func.equals( "fetch_article" ) )
		{
			outStr = fetchArticle( request.getParameter(  "course_id" ) );
		}
		else if ( func.equals( "fetch_post" ) )
		{
			outStr = fetchPost( request.getParameter( "article_id") );
		}
		else if ( func.equals( "get_user_name" ) )
		{
			outStr = getUserName( request.getParameter( "user_id") );
		}
		out.println( outStr );
		out.close();
	}
	
	static public String getUserPriviledge( String user_id )
	{
		String SQL = "select * from users where users.id=?";
		String[] sqlParams = { user_id };
		String out = "";
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_EXECUTE, SQL, sqlParams );
		ResultSet rs = db.rs;
		
		try {
			while (rs.next( )) {
				// String id = rs.getString( "id" );
				String priviledge = rs.getString( "privilege" );
				out += priviledge;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SystemPlusDbHelper.endDatabase( db );
		return out;
	}
	
	static public String getUserName( String user_id )
	{
		String SQL = "select * from users where users.id=?";
		String[] sqlParams = { user_id };
		String out = "";
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_EXECUTE, SQL, sqlParams );
		ResultSet rs = db.rs;
		
		try {
			while (rs.next( )) {
				// String id = rs.getString( "id" );
				String firstName = rs.getString( "firstname" );
				String lastName = rs.getString( "lastname" );
				out += firstName + " " + lastName;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SystemPlusDbHelper.endDatabase( db );
		return out;
	}
	
	private String createArticle( String type, String course_id, String user_id, String title, String detail )
	{
		String SQL = "INSERT INTO `onlinestudy`.`articles` (`id`,`type`,`course_id`,`user_id`,`title`,`detail`) VALUES "
				+ "(null,?,?,?,?,?);";
		
		String[] sqlParams = { type, course_id, user_id, title, detail };
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_UPDATE, SQL, sqlParams );
		SystemPlusDbHelper.endDatabase( db );
		
		String rlt = fetchArticle( course_id );
		return rlt;
	}
	
	private String fetchArticle( String course_id )
	{
		String SQL = "select * from `onlinestudy`.`articles` where articles.course_id=?";
		String[] sqlParams = { course_id };
		String out = "";
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_EXECUTE, SQL, sqlParams );
		ResultSet rs = db.rs;

		out += "{\"articles\": [";
		try {
			while (rs.next( )) {
				String id = rs.getString( "id" );
				String type = rs.getString( "type" );
				String user_id = rs.getString( "user_id" );
				String title = rs.getString( "title" );
				String detail = rs.getString( "detail" );
				out += "{\"id\":\"" + id + "\","
						+ "\"type\":\"" + type + "\","
						+ "\"user_id\":\"" + getUserName(user_id) + "\","
						+ "\"title\":\"" + title + "\","
						+ "\"detail\":\"" + detail + "\","
						+ "\"posts\":" + fetchPost(id) + "},";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if ( out.length( ) > 1 )
			out = out.substring( 0, out.length( )-1 );
		out += "]}";

		SystemPlusDbHelper.endDatabase( db );
		return out;
	}
	
	private String createPost( String article_id, String user_id, String parent_post_id, String title, String content )
	{
		String SQL = "INSERT INTO `onlinestudy`.`posts`"
				+ "(`id`, `article_id`, `user_id`, `parent_post_id`, `title`, `content`) VALUES"
				+ "(null,?,?,?,?,?);";
		String[] sqlParams = { article_id, user_id, parent_post_id, title, content };
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_UPDATE, SQL, sqlParams );
		SystemPlusDbHelper.endDatabase( db );
		return fetchPost( article_id );
	}
	
	private String fetchPost( String article_id )
	{
		String SQL = "select * from `onlinestudy`.`posts` where posts.article_id=?";
		String[] sqlParams = { article_id };
		String out = "";
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_EXECUTE, SQL, sqlParams );
		ResultSet rs = db.rs;
		
		out += "[";
		try {
			while (rs.next( )) {
				String id = rs.getString( "id" );
				String user_id = rs.getString( "user_id" );
				String parent_post_id = rs.getString( "parent_post_id" );
				String title = rs.getString( "title" );
				String content = rs.getString( "content" );
				out += "{\"id\":\"" + id + "\","
						+ "\"parent_post_id\":\"" + parent_post_id + "\","
						+ "\"user_id\":\"" + getUserName(user_id) + "\","
						+ "\"title\":\"" + title + "\","
						+ "\"content\":\"" + content + "\"},";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if ( out.length( ) > 1 )
			out = out.substring( 0, out.length( )-1 );
		out += "]";

		SystemPlusDbHelper.endDatabase( db );
		return out;
	}
	
	private void writeStr( String str )
	{
		
	}
}
