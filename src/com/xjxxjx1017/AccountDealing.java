package com.xjxxjx1017;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.xjxxjx1017.thirdparty.DbDataSet;
import com.xjxxjx1017.thirdparty.SystemPlusDbHelper;

public class AccountDealing
{

	static public String signup( String[] paramValues )
	{
		String SQL = "INSERT INTO `onlinestudy`.`users`"
				+ " (`id`,`email`,`firstname`,`lastname`,`password`,`privilege`,`discription`) " + " VALUES "
				+ "(null,?,?,?,?,?,?);";
		String[] sqlParams = { paramValues[1], paramValues[3], paramValues[4], paramValues[2], paramValues[5],
				"This is a student." };
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_UPDATE, SQL, sqlParams );
		SystemPlusDbHelper.endDatabase( db );
		return "SIGN_UP_RETURN";
	}

	static public String login( String[] paramValues )
	{
		String SQL = "select * from users where users.email=? and users.password=?";
		String[] sqlParams = { paramValues[1], paramValues[2] };
		String out = "";
		DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_EXECUTE, SQL, sqlParams );
		ResultSet rs = db.rs;
		
		try {
			while (rs.next( )) {
				String id = rs.getString( "id" );
				String firstName = rs.getString( "firstname" );
				String lastName = rs.getString( "lastname" );
				String privilege = rs.getString(  "privilege" );
				out += ("{ \"ID\": \"" + id + "\", \"FirstName\": \"" + 
						firstName + "\", \"LastName\": \"" + lastName + 
						"\", \"Privilege\": \"" + privilege + "\" }");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		SystemPlusDbHelper.endDatabase( db );
		return out;
	}
	
	static public String manager( String[] paramValues )
	{
		if ( paramValues[1].equals( "delete" ) ) {
			String SQL = "DELETE FROM users";
			String[] sqlParams = {};
			DbDataSet db = SystemPlusDbHelper.startDatabase( SystemPlusDbHelper.eDB_FUNC.DB_UPDATE, SQL, sqlParams );
			SystemPlusDbHelper.endDatabase( db );
		}
		return "MANAGER_RETURN";
	}
}
