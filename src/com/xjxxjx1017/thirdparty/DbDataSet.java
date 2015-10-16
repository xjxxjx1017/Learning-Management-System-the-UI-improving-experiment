package com.xjxxjx1017.thirdparty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbDataSet
{
	public ResultSet rs = null;
	public Connection conn = null;
	public Statement st = null;
	public PreparedStatement pstmt = null;
	public DbDataSet() {}
}