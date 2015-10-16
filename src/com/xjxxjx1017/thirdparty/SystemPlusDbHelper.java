package com.xjxxjx1017.thirdparty;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;


public class SystemPlusDbHelper
{
	static public String DB_PATH = "java:comp/env/jdbc/TestDB";

	static public enum eDB_FUNC
	{
		DB_UPDATE, DB_EXECUTE,
	}
	
	static public DbDataSet initialDatabase()
	{
		DbDataSet db = new DbDataSet();
		try {
			InitialContext ctx = new InitialContext( );
			DataSource ds = (DataSource) ctx.lookup( DB_PATH );
			db.conn = ds.getConnection( );
			db.st = db.conn.createStatement( );
		} catch (Exception ex) {
			ex.printStackTrace( );
		} 
		return db;
	}
	
	static public DbDataSet startDatabase( eDB_FUNC func, String SQL, String[] sqlParams )
	{
		DbDataSet db = initialDatabase();
		try {
			// add parameters
			db.pstmt = db.conn.prepareStatement( SQL );
			for ( int i = 0; i < sqlParams.length; i++ ) {
				db.pstmt.setString( i+1, sqlParams[i] );
			}
			// execute or update
			switch (func)
			{
				case DB_UPDATE:
					int row = db.pstmt.executeUpdate( );
					if ( row > 0 ) 
						SystemPlus.print( "Database updated." );
					db.rs = null;
					break;
				case DB_EXECUTE:
					db.rs = db.pstmt.executeQuery( );
					break;
				default:
					break;
			}

		} catch (Exception ex) {
			ex.printStackTrace( );
		} 
		return db;
	}

	static public void endDatabase( DbDataSet db )
	{
		try {
			if ( db.rs != null )
				db.rs.close( );
		} catch (SQLException e) {
			e.printStackTrace( );
		}
		try {
			if ( db.st != null )
				db.st.close( );
		} catch (SQLException e) {
			e.printStackTrace( );
		}
		try {
			if ( db.conn != null )
				db.conn.close( );
		} catch (SQLException e) {
			e.printStackTrace( );
		}
	}
}
