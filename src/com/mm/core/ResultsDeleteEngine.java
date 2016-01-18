package com.mm.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultsDeleteEngine {
	public static String getProjectName(){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		String lDbUsed = PropertyConf.getProperty("DatabaseUsed");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lQuery = "show tables where Tables_in_"+lDbUsed+" like '%lr_txns_details%';";
		String lTableName;
		StringBuilder lData = new StringBuilder();
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next()){
				lTableName = lResultSet.getString(1).substring(16).toUpperCase();
				lData.append(lTableName+",");				
			}
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
				if(lResultSet != null){
					lResultSet.close();					
				}
				if(lStatement != null){
					lStatement.close();					
				}
				if(lConnection != null){
					lConnection.close();					
				}
			}
			catch(Exception ae){

			}
		}
		return lData.toString();
	}
	public static String getTestsList(String lProjectName){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lQuery = "SELECT distinct(Test_ID) FROM lr_txns_details_"+lProjectName+";";
		String lTableName;
		StringBuilder lData = new StringBuilder();
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lData.append("<SELECT name=\"TestID\"><OPTION VALUE=\"Select\">--Select--</OPTION>");
			while (lResultSet.next()){
				lTableName = lResultSet.getString(1);
				lData.append("<OPTION VALUE=\""+lTableName+"\">"+lTableName+"</OPTION>");	
			}
			lData.append("</SELECT>");
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
				if(lResultSet != null){
					lResultSet.close();					
				}
				if(lStatement != null){
					lStatement.close();					
				}
				if(lConnection != null){
					lConnection.close();					
				}
			}
			catch(Exception ae){

			}
		}
		return lData.toString();
	}

	public static String checkLRExistence(String ProjectName)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		boolean lExist=false;
		String lQuery = "SELECT distinct(Test_ID) FROM lr_txns_details_"+ProjectName+";";
		StringBuilder lData = new StringBuilder();
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lExist=lResultSet.next();
			if (lExist)
			{lData.append("exists");}
			else
			{lData.append("");}
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
				if(lResultSet != null){
					lResultSet.close();					
				}
				if(lStatement != null){
					lStatement.close();					
				}
				if(lConnection != null){
					lConnection.close();					
				}
			}
			catch(Exception ae){

			}
		}
		return lData.toString();
	}
	public static void DeleteTestResults(String TestID,String ProjectName)
	{
		String lQuery="";
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		try 
		{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			System.out.println("Database connection establish...!");
			Statement lStatement1 = lConnection.createStatement();
			lQuery="DROP TABLE IF EXISTS responsetime_stats_"+ProjectName+"_"+TestID+";";
			lStatement1.executeUpdate(lQuery);
			lQuery="DROP TABLE IF EXISTS errors_stats_"+ProjectName+"_"+TestID+";";
			lStatement1.executeUpdate(lQuery);
			lQuery="DROP TABLE IF EXISTS runningvusers_stats_"+ProjectName+"_"+TestID+";";
			lStatement1.executeUpdate(lQuery);
			lQuery="DROP TABLE IF EXISTS throughput_stats_"+ProjectName+"_"+TestID+";";
			lStatement1.executeUpdate(lQuery);
			lQuery="DROP TABLE IF EXISTS ganglia_stats_"+ProjectName+"_"+TestID+";";
			lStatement1.executeUpdate(lQuery);
			lQuery="DROP TABLE IF EXISTS wily_stats_"+ProjectName+"_"+TestID+";";
			lStatement1.executeUpdate(lQuery);
			lQuery="DROP TABLE IF EXISTS perfmon_stats_"+ProjectName+"_"+TestID+";";
			lStatement1.executeUpdate(lQuery);
			lQuery="DROP TABLE IF EXISTS vmstat_stats_"+ProjectName+"_"+TestID+";";
			lStatement1.executeUpdate(lQuery);
			lQuery="Delete from lr_stats_details_"+ProjectName+" where Test_ID = \""+TestID+"\";";
			lStatement1.executeUpdate(lQuery);
			lQuery="Delete from lr_txns_details_"+ProjectName+" where Test_ID = \""+TestID+"\";";
			lStatement1.executeUpdate(lQuery);
			lQuery="Delete from test_obs_det_"+ProjectName+" where Test_ID = \""+TestID+"\";";
			lStatement1.executeUpdate(lQuery);
			lQuery="Delete from sesvalmetadata where Project_Name = \""+ProjectName+"\" and Test_ID = \""+TestID+"\";";
			lStatement1.executeUpdate(lQuery);
			String LRExistence=checkLRExistence(ProjectName);
			if(LRExistence.equals(""))
			{
				lQuery="DROP TABLE IF EXISTS lr_stats_details_"+ProjectName+";";
				lStatement1.executeUpdate(lQuery);
				lQuery="DROP TABLE IF EXISTS lr_txns_details_"+ProjectName+";";
				lStatement1.executeUpdate(lQuery);
				lQuery="DROP TABLE IF EXISTS test_obs_det_"+ProjectName+";";
				lStatement1.executeUpdate(lQuery);
			}
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally{
			if(lConnection != null){
				try {
					lConnection.close();
					System.out.println ("Database connection terminated...!!!");
				} catch (SQLException ex) {
				}
			}
		}
		
		
	}
}
