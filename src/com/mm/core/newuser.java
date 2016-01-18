package com.mm.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.Statement;

public class newuser 
{	
	public static String validateLogin(String fName, String lName, String pUserID,String pPassEntered)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		Statement lStatement1 = null;
		ResultSet lResultSet = null;
		String lQuery1 = "select user_id, user_password from user_credentials";
		String pUser_Password="";
		String pUser_Userid="";
		StringBuilder lData = new StringBuilder();
		String lQuery = "insert into user_credentials(user_id, user_password, first_name, last_name) values ('"+pUserID+"','"+pPassEntered+"','"+fName+"','"+lName+"')"; 	
		try
		{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lStatement1 = lConnection.createStatement();
			lStatement.executeUpdate(lQuery);	
			lResultSet = lStatement1.executeQuery(lQuery1);
			while (lResultSet.next())
			{
				pUser_Password = lResultSet.getString("user_password");
				pUser_Userid = lResultSet.getString("user_id");
				
				if(pUser_Userid.equals(pUserID))
				{
					lData.append("nval");
				}
				else
				{
					lData.append("val");
				}				
			}
		}
		catch(Exception ae)
		{
			ae.printStackTrace();
			lData.append("nval");
		}
		finally
		{
			try
			{
				
				if(lResultSet != null){
					lResultSet.close();					
				}
				if(lStatement != null)
				{
					lStatement.close();					
				}
				if(lConnection != null)
				{
					lConnection.close();					
				}
			}
			catch(Exception ae)
			{

			}
		}
		return lData.toString();
	}
}
