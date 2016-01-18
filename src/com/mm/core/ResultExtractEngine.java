package com.mm.core;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;

public class ResultExtractEngine {
	
	public static String getGenerateLink(String ProjectName,String TestID)
	{
		Connection conn = null;
		String dbUserName = PropertyConf.getProperty("UserName");
		String dbPassword = PropertyConf.getProperty("UserPassword");
		String dbUrl = PropertyConf.getProperty("DatabaseURL");
		String insertSql="";
		String url="I";
		String hostName="";
		String subject="",body="";
		String SesValID=RandomsesValID();
		StringBuilder lData = new StringBuilder();
		try {Class forNam = Class.forName(PropertyConf.getProperty("Driver"));
			try {forNam.newInstance();
			//hostName=InetAddress.getLocalHost().getHostName();
			hostName=InetAddress.getLocalHost().getHostAddress();} 		
			catch(Exception ae){ae.printStackTrace();}} 
		catch (ClassNotFoundException ex){ex.printStackTrace();}
		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			Statement st = conn.createStatement();
			System.out.println("Database connection establish...!");
			insertSql = "INSERT into sesvalmetadata VALUES (\""+ProjectName+"\", \""+TestID+"\", \""+SesValID+"\");";                    
			int val = st.executeUpdate(insertSql);} 
		catch (SQLException ex){System.out.println("Cannot connect to database server...!!");}
		finally{if(conn != null){try {conn.close();System.out.println ("Database connection terminated...!!!");} catch (SQLException ex) {}}}
		subject=ProjectName+" :: "+TestID+" :: VPT Results";
		url="http://"+hostName+":8080/AdministratorModule/jsp/pubReport.jsp?sesvalID="+SesValID;
		body="Hi,%0AThe results of the performance test conducted for Project: "+ProjectName+"%0Ais available for your review.%0A%0AKindly refer to the below link to view the complete test results of test : "+TestID+"%0A%0A"+url+"%0A%0A%0AMany Thanks!";
		lData.append("<h3><font color=BLUE>Test Report</font></h3><br>");
		lData.append("Please share the below link with the Project team for reviewing the results of test : <b>"+TestID+"</b> for project : <b>"+ProjectName+"</b>:<br>");
		lData.append("<table cellpadding=\"4\" cellspacing=\"0\" border=\"1\" bordercolor= #224466><tr bgcolor= #CCFFFF><td><br>");
		lData.append("<a href=\""+url+"\">"+url+"</a>");		
		lData.append("</td></tr></table><br>");
		lData.append("<table width=\"90%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><br>");
		lData.append("<a href=\"mailto:?Subject="+subject+"&Body="+body+"\" target=\"_top\">Send Mail</a>");		
		lData.append("</td><td><br><a href=\"/AdministratorModule/jsp/repGen.jsp?ProjectName="+ProjectName+"&TestID="+TestID+"\">Generate PDF Report</a></td></tr></table><br>");

		return lData.toString();
	}
	public static String RandomsesValID()
	{
		Random random = new Random();
		String temp="";
		int j=0;
		while(j<=20)	
		{	
			int pick = random.nextInt(126);
			if(pick>47 & pick<123)
			{
				if(pick==58||pick==59||pick==60||pick==61||pick==62||pick==63||pick==64||pick==91||pick==92||pick==93||pick==94||pick==95||pick==96)
				{
					
				}
				else
				{
					temp= temp+((char)pick);		    
					j++;
				}

			}
		}
		Date dt = new Date();
		return dt.getSeconds()+dt.getHours()+temp+dt.getMinutes();
	}
	
	public static String getProjectDetailsForPubReport(String sesVALID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		boolean lExist=false;
		String lQuery = "SELECT Project_Name,Test_ID FROM sesvalmetadata where sesVal_ID=\""+sesVALID+"\";";
		String lTableName="";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lExist=lResultSet.next();
			if (lExist)
			{
			lTableName = lResultSet.getString("Project_Name")+","+lResultSet.getString("Test_ID");			
			}
			else
			{
				lTableName ="DoesnotExist";
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
		return lTableName;
	}	
		
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

	public static String getLRStatistics(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;			
		String lQuery = "select Counter_Name,Counter_Value from lr_stats_details_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
		String Counter_Name="";
		String Counter_Value="";
		StringBuilder lData = new StringBuilder();

		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lData.append("<table cellpadding=\"4\" cellspacing=\"0\" border=\"2\" bordercolor= #224466>");
			while (lResultSet.next()){
				Counter_Name = lResultSet.getString("Counter_Name");
				Counter_Value = lResultSet.getString("Counter_Value");
				if (Counter_Name.contains("Total Throughput"))
				{
					Double TPH=Double.parseDouble(Counter_Value);
					lData.append("<tr bgcolor= #CCCCCC >");
					lData.append("<td align=\"left\"><font size=\"2\">"+Counter_Name+"</font></td>");
					lData.append("<td align=\"left\"><font size=\"2\">"+TPH.longValue()+"</font></td>");
					lData.append("</tr>");
				}
				else
				{
					lData.append("<tr bgcolor= #CCCCCC >");
					lData.append("<td align=\"left\"><font size=\"2\">"+Counter_Name+"</font></td>");
					lData.append("<td align=\"left\"><font size=\"2\">"+Counter_Value+"</font></td>");
					lData.append("</tr>");
				}
			}
			lData.append("</table>");
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
	public static String getLRtransactionRespTime(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;			
		String lQuery = "SELECT Transaction_Name,Min_Value,Avg_Value,Max_Value,NintyPercent,Pass_Value,Fail_Value,Stop_Value from lr_txns_details_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
		StringBuilder lData = new StringBuilder();
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Transaction Name </font></b></td><td colspan=0><b><font size=\"2\">Minimum</font></b></td><td colspan=0><b><font size=\"2\">Average</font></b></td><td colspan=0><b><font size=\"2\">Maximum</font></b></td><td colspan=0><b><font size=\"2\">90th Percentile</font></b></td><td colspan=0><b><font size=\"2\">Pass Count</font></b></td><td colspan=0><b><font size=\"2\">Fail Count</font></b></td><td colspan=0><b><font size=\"2\">Stop Count</font></b></td></tr>");
			while (lResultSet.next())
			{
				lData.append("<tr bgcolor= #CCFFFF >");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("Transaction_Name")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("Min_Value")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("Avg_Value")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("Max_Value")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("NintyPercent")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("Pass_Value")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("Fail_Value")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("Stop_Value")+"</font></td>");
				lData.append("</tr>");
			}
			lData.append("</table>");	
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
	public static String getLRGraphs(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		ResultSet lResultSet1 = null;
		ResultSet lResultSet2 = null;
		boolean respExist=false;
		String lQuery="";
		StringBuilder lData = new StringBuilder();		
		lData.append("<h2><font color=BLUE>Load Runner Graphs</font></h2><br><h3> TEST ID: <u><i>"+pTestID+"</i></u> PROJECT NAME: <u><i>"+pProjectName+"</i></u></h3><hr>");
		String Counter_Name="",Counter_Value="";
		String chartDataXML="";
		String Pass="",Fail="",Stop="";
		int counter=0;
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lQuery="SELECT Counter_Name,Counter_Value FROM lr_stats_details_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
			lResultSet = lStatement.executeQuery(lQuery);
			lData.append("<table width=\"90%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
			lData.append("<tr><td><b>Total Throughput</b></td><td><b>Total Hits</b></td></tr><tr>");
			while (lResultSet.next())
			{
				Counter_Name = lResultSet.getString("Counter_Name");
				Counter_Value = lResultSet.getString("Counter_Value");
				if(Counter_Name.contains("Total Throughput"))
				{
					Double temp1 =Double.parseDouble(Counter_Value);
					StringBuilder lGraph = new StringBuilder();
					respExist=checkTableExistence(pTestID,pProjectName,"throughput");
					if(respExist)
					{
						lGraph.append("<chart caption='Total Throughput (bytes)' xAxisName='Elapsed Time' yAxisName='Total Throughput' showValues='0' formatNumberScale='0' showBorder='1'clickURL='javascript:getpopupthpt(\\\""+pProjectName+"\\\",\\\""+pTestID+"\\\")'>");
						respExist=false;
					}
					else
					{
						lGraph.append("<chart caption='Total Throughput (bytes)' xAxisName='Elapsed Time' yAxisName='Total Throughput' showValues='0' formatNumberScale='0' showBorder='1'>");
					}
					
					lGraph.append("<set name='"+Counter_Name+"' value='"+temp1+"' hoverText='"+temp1+" (bytes)'/>");
					lGraph.append("</chart>");
					chartDataXML=lGraph.toString();
					
					lData.append("<td><div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
					lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/Column3D.swf\", \"myChartId_"+counter+"\", \"600\", \"300\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
					lData.append("</td>");
					counter=counter+1;
					
				}
				else if(Counter_Name.contains("Total Hits"))
				{
					StringBuilder lGraph = new StringBuilder();
					lGraph.append("<chart caption='Total Hits' xAxisName='Elapsed Time' yAxisName='Total Hits' showValues='0' formatNumberScale='0' showBorder='1'>");
					lGraph.append("<set name='"+Counter_Name+"' value='"+Double.parseDouble(Counter_Value)+"' hoverText='"+Counter_Value+"'/>");
					lGraph.append("</chart>");
					chartDataXML=lGraph.toString();
					
					lData.append("<td><div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
					lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/Column3D.swf\", \"myChartId_"+counter+"\", \"600\", \"300\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
					lData.append("</td>");
					counter=counter+1;					
				}
				else if(Counter_Name.contains("Total Pass"))
				{
					Pass=lResultSet.getString("Counter_Value");
				}
				else if(Counter_Name.contains("Total Fail"))
				{
					Fail=lResultSet.getString("Counter_Value");
				}
				else if(Counter_Name.contains("Total Stop"))
				{
					Stop=lResultSet.getString("Counter_Value");
				}

			}
			lData.append("</tr></table>");
			lData.append("<br><table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
			lData.append("<tr><td colspan=2><b>Transactional Success Chart</b></td></tr><tr><td><b>OVER-ALL</b></td><td><b>PER TRANSACTION</b></td></tr><tr>");
			StringBuilder lGraph1 = new StringBuilder();
			lGraph1.append("<chart caption='Transactional Success Chart' xAxisName='Elapsed Time' yAxisName='Transactional Success Chart' showValues='0' formatNumberScale='0' showBorder='1'>");
			lGraph1.append("<set label='Transactions Passed' value='"+Pass+"' hoverText='Total Passed Transactions: "+Pass+"'/>");
			lGraph1.append("<set label='Transactions Failed' value='"+Fail+"' hoverText='Total Failed Transactions: "+Fail+"'/>");
			lGraph1.append("<set label='Transactions Stopped' value='"+Stop+"' hoverText='Total Stopped Transactions: "+Stop+"'/>");			
			lGraph1.append("</chart>");
			chartDataXML=lGraph1.toString();			
			lData.append("<td><div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
			lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/Pie3D.swf\", \"myChartId_"+counter+"\", \"600\", \"300\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
			lData.append("</td>");
			counter=counter+1;
			
			StringBuilder lGraph3 = new StringBuilder();
			StringBuilder Categories = new StringBuilder();
			StringBuilder DataseriesP = new StringBuilder();
			StringBuilder DataseriesF = new StringBuilder();
			StringBuilder DataseriesS = new StringBuilder();
			//labelDisplay='ROTATE' slantLabels='1'
			lGraph3.append("<chart palette='2' caption='Transactional Success Chart'  showLabels='0' shownames='1' showValues='0' formatNumberScale='0' showBorder='1' alternateHGridColor='FCB541' alternateHGridAlpha='20' divLineColor='FCB541' divLineAlpha='50' plotSpacePercent='45' canvasBorderColor='666666' baseFontColor='666666' lineColor='FCB541' useRoundEdges='1'>");
			Categories.append("<categories>");
			DataseriesP.append("<dataset seriesName='Passed Transactions' color='33E491' showValues='0'>");
			DataseriesF.append("<dataset seriesName='Failed Transactions' color='C22025' showValues='0'>");
			DataseriesS.append("<dataset seriesName='Stopped Transactions' color='C29420' showValues='0'>");
			lQuery="SELECT Transaction_Name,Pass_Value,Fail_Value,Stop_Value from lr_txns_details_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
			lResultSet2 = lStatement.executeQuery(lQuery);
			int cntr=1;
			respExist=checkTableExistence(pTestID,pProjectName,"errors");
			while (lResultSet2.next())
			{
				String txnName=lResultSet2.getString("Transaction_Name");
				String Pass_Value=lResultSet2.getString("Pass_Value");
				String Fail_Value=lResultSet2.getString("Fail_Value");
				String Stop_Value=lResultSet2.getString("Stop_Value");				
				Categories.append("<category label='Txn_"+cntr+"'/>");
				DataseriesP.append("<set value='"+Pass_Value+"' hoverText='Txn Name: "+txnName+" | Passed Transactions Count : "+Pass_Value+"'/>");
				if(respExist)
				{
					DataseriesF.append("<set value='"+Fail_Value+"' hoverText='Txn Name: "+txnName+" | Failed Transactions Count : "+Fail_Value+"' link='javascript:getpopuperrors(\\\""+pProjectName+"\\\",\\\""+pTestID+"\\\",\\\""+txnName+"\\\")'/>");
				}
				else
				{
					DataseriesF.append("<set value='"+Fail_Value+"' hoverText='Txn Name: "+txnName+" | Failed Transactions Count : "+Fail_Value+"'/>");
				}
				DataseriesS.append("<set value='"+Stop_Value+"' hoverText='Txn Name: "+txnName+" | Stopped Transactions Count : "+Stop_Value+"'/>");	
				cntr=cntr+1;
			}
			respExist=false;
			Categories.append("</categories>");
			DataseriesP.append("</dataset>");
			DataseriesF.append("</dataset>");
			DataseriesS.append("</dataset>");
			lGraph3.append(Categories.toString());
			lGraph3.append(DataseriesP.toString());
			lGraph3.append(DataseriesF.toString());
			lGraph3.append(DataseriesS.toString());
			lGraph3.append("</chart>");
			chartDataXML=lGraph3.toString();
			lData.append("<td><div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
			lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/ScrollStackedColumn2D.swf\", \"myChartId_"+counter+"\", \"600\", \"300\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script></td></tr></table>");
			counter=counter+1;
			
			///Running Vusers will come here
			respExist=checkTableExistence(pTestID,pProjectName,"runningvusers");
			if(respExist)
			{
				lData.append("<br><table width=\"90%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
				lData.append("<tr><td><b>Running Vusers Summary</b></td></tr><tr><td>");
				lData.append(getRunningVusersGraph(pProjectName,pTestID));		
				lData.append("</td></tr></table>");
			}
			respExist=false;
			lQuery="SELECT Transaction_Name,Avg_Value from lr_txns_details_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
			lData.append("<br><table width=\"90%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
			lData.append("<tr><td><b>Transaction Summary</b></td></tr><tr>");
			StringBuilder lGraph2 = new StringBuilder();
			lGraph2.append("<chart caption='Average Transaction Response Time' showLabels='0' xAxisName='Transactions' yAxisName='Average Transaction Response Time' showValues='0' formatNumberScale='0' showBorder='1'>");
			lResultSet1 = lStatement.executeQuery(lQuery);
			respExist=checkTableExistence(pTestID,pProjectName,"responsetime");
			if(respExist)
			{
				int ctx=1;
				while (lResultSet1.next())
				{
					String TxnName= lResultSet1.getString("Transaction_Name");
					String Avg_Value=lResultSet1.getString("Avg_Value");
					lGraph2.append("<set label='Txn_"+ctx+"' value='"+Avg_Value+"' hoverText='Transaction Name: "+TxnName+", Average Value: "+Avg_Value+"' link='javascript:updatechart(\\\""+pProjectName+"\\\",\\\""+pTestID+"\\\",\\\""+TxnName+"\\\")'/>");
					ctx=ctx+1;
				}
			}
			else
			{
				int ctx=1;
				while (lResultSet1.next())
				{
					String TxnName= lResultSet1.getString("Transaction_Name");
					String Avg_Value=lResultSet1.getString("Avg_Value");
					lGraph2.append("<set label='Txn_"+ctx+"' value='"+Avg_Value+"' hoverText='Transaction Name: "+TxnName+", Average Value: "+Avg_Value+"'/>");
					ctx=ctx+1;
				}
			}
			lGraph2.append("</chart>");
			chartDataXML=lGraph2.toString();			
			lData.append("<td><div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
			lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/Column3D.swf\", \"myChartId_"+counter+"\", \"1200\", \"350\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
			lData.append("</td></tr></table>");
			counter=counter+1;
			if(respExist)
			{
				lData.append("<script>function updatechart(pProjectName,pTestID,TxnName){var lDataURL = \"/AdministratorModule/jsp/RowsScanned.jsp?pProjectName=\"+pProjectName+\"&pTestID=\"+pTestID+\"&TxnName=\"+TxnName;lDataURL = escape(lDataURL);var lChartObject = getChartFromId(\"RowsScanned\");lChartObject.setDataURL(lDataURL);}</script>");
				lData.append("<table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"Border-top:#EEEEEE 1px solid;\"><tr><td><div id='RowsScannedDiv' align='center'>Chart.</div><script type=\"text/javascript\">var chart_RowsScanned = new FusionCharts(\"../FusionCharts/ZoomLine.swf?ChartNoDataText=Please select a Transaction.\", \"RowsScanned\", \"1200\", \"350\", \"0\", \"1\");chart_RowsScanned.setDataXML(\"<chart></chart>\");chart_RowsScanned.render(\"RowsScannedDiv\");</script></td></tr></table><br>");
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
				if(lResultSet1 != null){
					lResultSet1.close();					
				}
				if(lResultSet2 != null){
					lResultSet2.close();					
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
	public static String getThroughputGraph(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		StringBuilder lData = new StringBuilder();
		String lQuery = "";
		Connection lConnection = null;
		Statement lStatement = null;
		String chartDataXML="";
		int counter=0;
		lData.append("<h2><font color=BLUE>Throughput Graph</font></h2><br><h3> TEST ID: <u><i>"+pTestID+"</i></u> PROJECT NAME: <u><i>"+pProjectName+"</i></u></h3><hr>");
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();			
			lData.append("<br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC >");
			StringBuilder lGraph = new StringBuilder();
			StringBuilder Categories = new StringBuilder();
			StringBuilder Dataseries = new StringBuilder();
			ResultSet lResultSet = null;
			lQuery = "SELECT Throughput,DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate FROM throughput_stats_"+pProjectName+"_"+pTestID+" order by lDate;";
			lResultSet = lStatement.executeQuery(lQuery);
			lData.append("<td>");
			lData.append("<div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
			lGraph.append("<chart caption='Throughput metric data' xAxisName='Elapsed Time' yAxisName='Throughput (b)' compactDataMode='1' dataSeparator='|' paletteThemeColor='5D57A5' divLineColor='5D57A5' divLineAlpha='40' vDivLineAlpha='40' allowPinMode='1' labelHeight='50'>");
			Categories.append("<categories>");
			Dataseries.append("<dataset seriesname='Throughput'>");
			while(lResultSet.next())
			{
				Double temp1 =Double.parseDouble(lResultSet.getString(1));
				Dataseries.append(temp1+"|");
				Categories.append(lResultSet.getString(2)+"|");							
			}
			Categories.append("</categories>");
			Dataseries.append("</dataset>");
			lGraph.append(Categories.toString());
			lGraph.append(Dataseries.toString());
			lGraph.append("</chart>");
			chartDataXML=lGraph.toString();
			try{
				if(lResultSet != null){
					lResultSet.close();					
				}
			}
			catch(Exception ae){}
			lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/ZoomLine.swf\", \"myChartId_"+counter+"\", \"1200\", \"350\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
			lData.append("</td></tr>");
			counter=counter+1;
			lData.append("</table>");						
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
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
	public static String checkResultsExistence(String TestID,String ProjectName)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		String lDbUsed = PropertyConf.getProperty("DatabaseUsed");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lTable="";
		String lQuery ="show tables where Tables_in_"+lDbUsed+" like '%"+ProjectName+"_"+TestID+"%';";
		StringBuilder lData = new StringBuilder();
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				lTable=lResultSet.getString(1);
				if(lTable.contains("wily"))
					lData.append("wily,");
				if(lTable.contains("ganglia"))
					lData.append("ganglia,");
				if(lTable.contains("perfmon"))
					lData.append("perfmon,");
				if(lTable.contains("vmstat"))
					lData.append("vmstat,");
				if(lTable.contains("responsetime"))
					lData.append("responsetime,");
				if(lTable.contains("throughput"))
					lData.append("throughput,");
				if(lTable.contains("runningvusers"))
					lData.append("runningvusers,");
				if(lTable.contains("errors"))
					lData.append("errors,");
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
		System.out.println(lData);
		return lData.toString();
	}
	public static boolean checkTableExistence(String TestID,String ProjectName,String types)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		String lDbUsed = PropertyConf.getProperty("DatabaseUsed");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		boolean value=false;
		String lQuery = "show tables where Tables_in_"+lDbUsed+" like '%"+types+"_stats_"+ProjectName+"_"+TestID+"%';";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			if(lResultSet.next())
			{
				value=true;
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
		return value;
	}
	public static String getErrorGraph(String pProjectName,String pTestID, String Txn_Name){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		StringBuilder lData = new StringBuilder();
		String lQuery = "";
		Connection lConnection = null;
		Statement lStatement = null;
		String chartDataXML="";
		int counter=0;
		lData.append("<h2><font color=BLUE>Error Graph</font></h2><br><h3> TEST ID: <u><i>"+pTestID+"</i></u> PROJECT NAME: <u><i>"+pProjectName+"</i></u></h3><hr>");
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();			
			lData.append("<br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC >");
			StringBuilder lGraph = new StringBuilder();
			StringBuilder Categories = new StringBuilder();
			StringBuilder Dataseries = new StringBuilder();
			ResultSet lResultSet = null;
			lQuery = "(SELECT Error_Type as Errors,DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate FROM errors_stats_"+pProjectName+"_"+pTestID+" where Txn_Name='"+Txn_Name+"')UNION(SELECT null as Errors, DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate FROM responsetime_stats_"+pProjectName+"_"+pTestID+"  where Transaction_Name='"+Txn_Name+"') order by lDate;";
			lResultSet = lStatement.executeQuery(lQuery);
			lData.append("<td>");
			lData.append("<div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
			lGraph.append("<chart caption='Error metric data for Transaction: "+Txn_Name+"' xAxisName='Elapsed Time' yAxisName='Errors' compactDataMode='1' dataSeparator='|' paletteThemeColor='5D57A5' divLineColor='5D57A5' divLineAlpha='40' vDivLineAlpha='40' allowPinMode='1' labelHeight='50'>");
			Categories.append("<categories>");
			Dataseries.append("<dataset seriesname='Errors'>");
			while(lResultSet.next())
			{
				String ColR=lResultSet.getString(1);
				if(ColR==null|ColR=="")
				{ColR="0";}
				else if(ColR!="")
				{ColR="1";}
				Dataseries.append(ColR+"|");
				Categories.append(lResultSet.getString(2)+"|");							
			}
			Categories.append("</categories>");
			Dataseries.append("</dataset>");
			lGraph.append(Categories.toString());
			lGraph.append(Dataseries.toString());
			lGraph.append("</chart>");
			chartDataXML=lGraph.toString();
			try{
				if(lResultSet != null){
					lResultSet.close();					
				}
			}
			catch(Exception ae){}
			lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/ZoomLine.swf\", \"myChartId_"+counter+"\", \"1200\", \"350\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
			lData.append("</td></tr>");
			counter=counter+1;
			lData.append("</table>");						
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
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
	
	public static String getRowsData(String pProjectName,String pTestID,String TxnName){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lQuery = "select Transaction_Response_Time,DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate from responsetime_stats_"+pProjectName+"_"+pTestID+" where Transaction_Name ='"+TxnName+"' order by Scenario_Elapsed_Time;";
		StringBuilder lData = new StringBuilder();
		StringBuilder Categories = new StringBuilder();
		StringBuilder Dataseries = new StringBuilder();
		try{
			lData.append("<chart xAxisName='Elapsed Time' yAxisName='Response Time' compactDataMode='1' dataSeparator='|' paletteThemeColor='5D57A5' divLineColor='5D57A5' divLineAlpha='40' vDivLineAlpha='40' allowPinMode='1' labelHeight='50'>");
			Categories.append("<categories>");
			Dataseries.append("<dataset seriesname='"+TxnName+"'>");
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next()){
				Categories.append(lResultSet.getString("lDate")+"|");
				Dataseries.append(lResultSet.getString("Transaction_Response_Time")+"|");
			}
			Categories.append("</categories>");
			Dataseries.append("</dataset>");
			lData.append(Categories.toString());
			lData.append(Dataseries.toString());
			lData.append("</chart>");
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
	public static String getRunningVusersGraph(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		StringBuilder lData = new StringBuilder();
		String lQuery = "";
		Connection lConnection = null;
		Statement lStatement = null;
		String chartDataXML="";
		String counter="rvu";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();			
			lData.append("<br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466><tr>");
			StringBuilder lGraph = new StringBuilder();
			StringBuilder Categories = new StringBuilder();
			StringBuilder Dataseries = new StringBuilder();
			ResultSet lResultSet = null;
			lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,vusers FROM runningvusers_stats_"+pProjectName+"_"+pTestID+") UNION (SELECT distinct(DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s')) AS lDate,null as vusers FROM throughput_stats_"+pProjectName+"_"+pTestID+") order by lDate;";
	//		lQuery = "SELECT vusers,DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate FROM runningvusers_stats_"+pProjectName+"_"+pTestID+" order by lDate;";
			lResultSet = lStatement.executeQuery(lQuery);
			lData.append("<td>");
			lData.append("<div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
//			lGraph.append("<chart caption='Running Vusers metric data' xAxisName='Elapsed Time' yAxisName='Running Vusers' compactDataMode='1' dataSeparator='|' paletteThemeColor='5D57A5' divLineColor='5D57A5' connectNullData='1' divLineAlpha='40' vDivLineAlpha='40' allowPinMode='1' labelHeight='50'>");
			lGraph.append("<chart caption='Running Vusers metric data' connectNullData='1' xAxisName='Elapsed Time' showValues='0' divLineAlpha='100' numVDivLines='4' vDivLineAlpha='0' showAlternateVGridColor='1' alternateVGridAlpha='5' canvasPadding='0'>");
			Categories.append("<categories>");
//			Dataseries.append("<dataset seriesname='Running Vusers'>");
			Dataseries.append("<axis title='Running Vusers' titlePos='left' tickWidth='10' divlineisdashed='1' color='F6BD0F'><dataset color='F6BD0F' seriesName='Running Vusers [N]'>");
			while(lResultSet.next())
			{
			//	Categories.append(lResultSet.getString(1)+"|");							
				Categories.append("<category label='"+lResultSet.getString(1)+"' />");							
				String ColR=lResultSet.getString(2);
				if(ColR==null|ColR=="")
				{ColR="";}
			//	Dataseries.append(ColR+"|");
				Dataseries.append("<set value='"+ColR+"' />");
			}
			Categories.append("</categories>");
//			Dataseries.append("</dataset>");
			Dataseries.append("</dataset></axis>");
			lGraph.append(Categories.toString());
			lGraph.append(Dataseries.toString());
			lGraph.append("</chart>");
			chartDataXML=lGraph.toString();
			try{
				if(lResultSet != null){
					lResultSet.close();					
				}
			}
			catch(Exception ae){}
//			lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/ZoomLine.swf\", \"myChartId_"+counter+"\", \"1200\", \"350\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
			lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/MultiAxisLine.swf\", \"myChartId_"+counter+"\", \"1200\", \"350\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
			lData.append("</td></tr>");
			lData.append("</table>");						
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
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
	
	public static String getTestDetails(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;			
		String lQuery = "select test_Details from test_obs_det_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
		String test_Details="";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next()){
				test_Details = lResultSet.getString("test_Details");
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
		return test_Details;
	}
	public static String getTestObservations(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;			
		String lQuery = "select test_Observation from test_obs_det_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
		StringBuilder test_Details=new StringBuilder();
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lResultSet.next();
			test_Details.append(lResultSet.getString("test_Observation"));
									
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
		return test_Details.toString();
	}	
	public static String getTestStatus(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;			
		String lQuery = "select test_status from test_obs_det_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
		String test_Details="";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next()){
				test_Details = lResultSet.getString("test_status");
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
		return test_Details;
	}
	public static String getTestSummary(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;			
		String lQuery = "select test_summary from test_obs_det_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
		String test_Details="";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next()){
				test_Details = lResultSet.getString("test_summary");
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
		return test_Details;
	}
	public static String getTestObservationsPubRep(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;			
		String lQuery = "select test_Observation from test_obs_det_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
		StringBuilder test_Details=new StringBuilder();
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lResultSet.next();
			String temp=lResultSet.getString("test_Observation");
			StringTokenizer stObj = new StringTokenizer(temp,"\n");
			while(stObj.hasMoreTokens())
			{
				test_Details.append(stObj.nextToken()+"<br>");
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
		return test_Details.toString();
	}
		
	public static String getWilyStatistics(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement_sum = null;
		Statement lStatement_avg = null;
		ResultSet lResultSet_sum = null;
		ResultSet lResultSet_avg = null;	
		String lQuery_avg="SELECT agent_name,resource_name,metric_name, avg(metric_value) as Metric_Avg FROM wily_stats_"+pProjectName+"_"+pTestID+" where metric_name!=\"Responses Per Interval\" group by metric_name,resource_name,agent_name order by metric_name,resource_name,agent_name;";
		String lQuery_sum="SELECT agent_name,resource_name,metric_name, sum(metric_value) as Metric_Sum FROM wily_stats_"+pProjectName+"_"+pTestID+" where metric_name=\"Responses Per Interval\" group by metric_name,resource_name,agent_name order by metric_name,resource_name,agent_name;";
		StringBuilder lData = new StringBuilder();
		String Agent_Name="",Resource_Name="",Metric_Name="", Metric_Sum="", Metric_Avg="";

		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement_sum = lConnection.createStatement();
			lStatement_avg = lConnection.createStatement();
			lResultSet_sum = lStatement_sum.executeQuery(lQuery_sum);
			lResultSet_avg = lStatement_avg.executeQuery(lQuery_avg);
			while (lResultSet_sum.next()){
				Agent_Name = lResultSet_sum.getString("agent_name");
				Resource_Name = lResultSet_sum.getString("resource_name");
				Metric_Name = lResultSet_sum.getString("metric_name");
				Metric_Sum = lResultSet_sum.getString("Metric_Sum");
				lData.append(Agent_Name+"#"+Resource_Name+"#"+Metric_Name+"#Total Count#"+Metric_Sum);
				lData.append(",");	
			}
			while (lResultSet_avg.next()){
				Agent_Name = lResultSet_avg.getString("agent_name");
				Resource_Name = lResultSet_avg.getString("resource_name");
				Metric_Name = lResultSet_avg.getString("metric_name");
				Metric_Avg = lResultSet_avg.getString("Metric_Avg");
				lData.append(Agent_Name+"#"+Resource_Name+"#"+Metric_Name+"#Average Value#"+Metric_Avg);
				lData.append(",");	
			}

		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
				if(lResultSet_sum != null){
					lResultSet_sum.close();					
				}
				if(lResultSet_avg != null){
					lResultSet_avg.close();					
				}
				if(lStatement_sum != null){
					lStatement_sum.close();					
				}
				if(lStatement_avg != null){
					lStatement_avg.close();					
				}
				if(lConnection != null){
					lConnection.close();					
				}
			}
			catch(Exception ae){

			}
		}
	//	System.out.println(lData.toString());
		return lData.toString();
	}
	public static String getWilyGraphs(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lQuery="SELECT agent_name,resource_name,metric_name FROM wily_stats_"+pProjectName+"_"+pTestID+" group by metric_name,resource_name,agent_name order by agent_name,resource_name,metric_name;";
		StringBuilder lData = new StringBuilder();		
		lData.append("<h2><font color=BLUE>Wily Graphs</font></h2><br><h3> TEST ID: <u><i>"+pTestID+"</i></u> PROJECT NAME: <u><i>"+pProjectName+"</i></u></h3><hr>");
		String Agent_Name="",Resource_Name="",Metric_Name="";
		String unqHostName="",ClusterName="",chartDataXML="";
		int counter=0;
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next())
			{
				Agent_Name = lResultSet.getString("agent_name");
				if(Agent_Name.equals(unqHostName))
				{}
				else
				{
					unqHostName=Agent_Name;
					StringTokenizer st= new StringTokenizer(Agent_Name, "/");
					st.nextToken();
					ClusterName=st.nextToken();					
					lData.append("<h3>"+ClusterName+"</h3><br>");
				}
				lData.append("<table width=\"875\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
				Resource_Name = lResultSet.getString("resource_name");
				lData.append("<tr><td><b>Resource Name: </b> <i>"+Resource_Name+"</i> ");
				Metric_Name = lResultSet.getString("metric_name");
				lData.append("<b> Metric Name: </b><i>"+Metric_Name+"</i><br><br></td></tr>");
				//clickURL='javascript:getpopupwily(\\\""+pProjectName+"\\\",\\\""+pTestID+"\\\",\\\""+ClusterName+"\\\",\\\""+Resource_Name+"\\\",\\\""+Metric_Name+"\\\")'
				lData.append("<tr><td><div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
				lData.append("<a href= \"javascript:getpopupwily('"+pProjectName+"','"+pTestID+"','"+ClusterName+"','"+Resource_Name+"','"+Metric_Name.replace("%","%2525")+"')\" >Raw Data</a><br>");
				try
				{
					StringBuilder lGraph = new StringBuilder();
					Statement lStatement1 = null;
					ResultSet lResultSet1 = null;
					String lQuery1="SELECT Metric_Value,DATE_FORMAT(TimeStamp, '%d-%m-%Y %h:%i:%s') AS lDate FROM wily_stats_"+pProjectName+"_"+pTestID+" where Metric_Name='"+Metric_Name+"' and Resource_Name='"+Resource_Name+"' and Agent_Name like '%"+ClusterName+"%' order by TimeStamp;";
					lStatement1 = lConnection.createStatement();
					lResultSet1 = lStatement1.executeQuery(lQuery1);
					StringBuilder Categories= new StringBuilder();
					StringBuilder Dataseries= new StringBuilder();
					Categories.append("<categories>");
					Dataseries.append("<dataset seriesname='"+Metric_Name+"'>");
					lGraph.append("<chart caption='"+Resource_Name+" metric data for "+ClusterName+"' xAxisName='Elapsed Time' yAxisName='"+Metric_Name+"' compactDataMode='1' dataSeparator='|' paletteThemeColor='5D57A5' divLineColor='5D57A5' divLineAlpha='40' vDivLineAlpha='40' allowPinMode='1' labelHeight='50'>");
					int cnt=1;
					while (lResultSet1.next())
					{
						Categories.append(lResultSet1.getString("lDate")+"|");
						Dataseries.append(lResultSet1.getString("Metric_Value")+"|");
					//	String temp=lResultSet1.getString("Metric_Value");
					//	lGraph.append("<set name='"+cnt+"' value='"+temp+"' hoverText='"+temp+"'/>");
						cnt=cnt+1;
					//	lData.append("lQuery1 is :---- "+lQuery1+" -----Result is: ---- "+lResultSet1.getString(1)+"<br>");
					}
					Categories.append("</categories>");
					Dataseries.append("</dataset>");
					lGraph.append(Categories.toString());
					lGraph.append(Dataseries.toString());
					lGraph.append("</chart>");

					if(lResultSet1 != null){
						lResultSet1.close();					
					}
					if(lStatement1 != null){
						lStatement1.close();					
					}
					chartDataXML=lGraph.toString();
				}
				catch(Exception ae){
					ae.printStackTrace();
				}
				lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/ZoomLine.swf\", \"myChartId_"+counter+"\", \"850\", \"400\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
				lData.append("</td></tr></table><br>");
				counter=counter+1;
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
	//	System.out.println(lData.toString());
		return lData.toString();
	}
	public static String getWilyGraphsTableData(String pProjectName,String pTestID,String ClusterName,String Resource_Name,String Metric_Name){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		Statement lStatement1 = null;
		ResultSet lResultSet1 = null;
		String lQuery="";
		StringBuilder lData = new StringBuilder();		
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lData.append("<h3><font color=BLUE>STATISTICS:</font></h3><b>Project Name:</b> <i>"+pProjectName+"</i><br><b>TestID:</b> <i>"+pTestID+"</i><br><b>Cluster:</b> <i>"+ClusterName+"</i><br><b>Resource Name:</b> <i>"+Resource_Name+"</i><br><b>Metric Name:</b> <i>"+Metric_Name+"</i><br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Minimum</font></b></td><td colspan=0><b><font size=\"2\">Maximum</font></b></td><td colspan=0><b><font size=\"2\">");
			if(Metric_Name.contains("Responses Per Interval"))
			{	lData.append("Total Count");
				lQuery="SELECT min(Metric_Value) as min_val,max(Metric_Value) as max_val, sum(Metric_Value) as val FROM wily_stats_"+pProjectName+"_"+pTestID+" where Metric_Name='"+Metric_Name+"' and Resource_Name='"+Resource_Name+"' and Agent_Name like '%"+ClusterName+"%' ";}
			else
			{	lData.append("Average Value");
				lQuery="SELECT min(Metric_Value) as min_val,max(Metric_Value) as max_val, avg(Metric_Value) as val FROM wily_stats_"+pProjectName+"_"+pTestID+" where Metric_Name='"+Metric_Name+"' and Resource_Name='"+Resource_Name+"' and Agent_Name like '%"+ClusterName+"%' ";}
			lData.append("</font></b></td></tr><tr bgcolor= #CCFFFF >");
			lStatement1 = lConnection.createStatement();
			lResultSet1 = lStatement1.executeQuery(lQuery);
			lResultSet1.next();
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet1.getString("min_val")+"</font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet1.getString("max_val")+"</font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet1.getString("val")+"</font></td>");

			lData.append("</tr></table><br><hr><br><h3><font color=BLUE>RAW DATA</font></h3>");
			
			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Time Stamp</font></b></td><td colspan=0><b><font size=\"2\">"+Metric_Name+"</font></b></td></tr>");
	
			lQuery="SELECT DATE_FORMAT(TimeStamp, '%d-%m-%Y %h:%i:%s') AS lDate,Metric_Value FROM wily_stats_"+pProjectName+"_"+pTestID+" where Metric_Name='"+Metric_Name+"' and Resource_Name='"+Resource_Name+"' and Agent_Name like '%"+ClusterName+"%' ";
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next())
			{
				lData.append("<tr bgcolor= #CCFFFF >");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("lDate")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("Metric_Value")+"</font></td>");
				lData.append("</tr>");				
			}
			lData.append("</table>");	
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
	//	System.out.println(lData.toString());
		return lData.toString();
	}	
		
	public static String getGangliaGraphs(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		StringBuilder lData = new StringBuilder();
		String lQuery = "";
		Connection lConnection = null;
		Statement lStatement = null;
		int colCount=gettotColCount(pProjectName,pTestID,"ganglia");
		String[] colNames = gettotColumns(pProjectName,pTestID,"ganglia");
		String[] MetricCounterNames = new String[100];
		String[] MetricCounterHeads = new String[5];//for the get propertytypes
		String headerType="";
		String chartDataXML="";
		int counter=0;
		int MetricCounterHeadscount=5;
		MetricCounterHeads[0]=PropertyConf.getProperty("GangliaCPU");
		MetricCounterHeads[1]=PropertyConf.getProperty("GangliaLoad");
		MetricCounterHeads[2]=PropertyConf.getProperty("GangliaMemory");
		MetricCounterHeads[3]=PropertyConf.getProperty("GangliaNetwork");
		MetricCounterHeads[4]=PropertyConf.getProperty("GangliaPackets");
		lData.append("<h2><font color=BLUE>Ganglia Graphs</font></h2><br><h3> TEST ID: <u><i>"+pTestID+"</i></u> PROJECT NAME: <u><i>"+pProjectName+"</i></u></h3><hr>");
		for(int i=1;i<(colCount-1);i++)
		{
			StringTokenizer stObj= new StringTokenizer(colNames[i],"_");
			stObj.nextToken();
			String temp1="";
			while(stObj.hasMoreTokens())
			{
				if(temp1.equals(""))
					temp1=temp1+stObj.nextToken();
				else
					temp1=temp1+"_"+stObj.nextToken();
			}
			MetricCounterNames[i]=temp1;			
		}
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();			
			for(int j=0;j<MetricCounterHeadscount;j++)
			{
				int Flag=0;
				if(j==0)
				{	headerType="CPU Counters";
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						Flag=1;
					}
				}
				}
				else if(j==1)
				{headerType="LOAD Counters";
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						Flag=1;
					}
				}
				}
				else if(j==2)
				{headerType="MEMORY Counters";
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						Flag=1;
					}
				}
				}
				else if(j==3)
				{headerType="NETWORK Counters";
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						Flag=1;
					}
				}
				}
				else if(j==4)
				{headerType="PACKETS Counters";
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						Flag=1;
					}
				}
				}
				if(Flag==1)
					lData.append("<br><b>"+headerType+"</b><br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Server Name</font></b></td><td colspan=0><b><font size=\"2\">Counter Name</font></b></td><td colspan=0><b><font size=\"2\">Graph</font></b></td></tr>");
				for(int k=1;k<(colCount-1);k++)
				{					
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						StringBuilder lGraph = new StringBuilder();
						StringBuilder Categories = new StringBuilder();
						StringBuilder Dataseries = new StringBuilder();
						StringTokenizer stObj= new StringTokenizer(colNames[k],"_");
						String temp=stObj.nextToken();	
						lData.append("<tr><td colspan=0><b><font color=BLUE size=\"2\">"+temp+"</font></b></td><td colspan=0><b><font size=\"2\">"+MetricCounterNames[k]+"</font></b></td>");
						ResultSet lResultSet = null;
						lQuery = "SELECT "+colNames[k]+",DATE_FORMAT(Sample_time, '%d-%m-%Y %h:%i:%s') AS lDate FROM ganglia_stats_"+pProjectName+"_"+pTestID+" order by sample_time;";
						lResultSet = lStatement.executeQuery(lQuery);
						lData.append("<td>");
						//clickURL='javascript:getpopupgang(\\\""+pProjectName+"\\\",\\\""+pTestID+"\\\",\\\""+MetricCounterNames[k]+"\\\",\\\""+temp+"\\\")'
						lData.append("<a href= \"javascript:getpopupgang('"+pProjectName+"','"+pTestID+"','"+MetricCounterNames[k]+"','"+temp+"')\" >Raw Data</a><br>");
						lData.append("<div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
						lGraph.append("<chart caption='"+MetricCounterNames[k]+" metric data for "+temp+"' xAxisName='Elapsed Time' yAxisName='"+MetricCounterNames[k]+"' compactDataMode='1' dataSeparator='|' paletteThemeColor='5D57A5' divLineColor='5D57A5' divLineAlpha='40' vDivLineAlpha='40' allowPinMode='1' labelHeight='50'>");
						Categories.append("<categories>");
						Dataseries.append("<dataset seriesname='"+MetricCounterNames[k]+"'>");
					//	int cnt=1;
						while(lResultSet.next())
						{								
							Dataseries.append(lResultSet.getString(1)+"|");
							Categories.append(lResultSet.getString(2)+"|");
						//	Double temp1 =Double.parseDouble(lResultSet.getString(1));
						//	lGraph.append("<set name='"+cnt+"' value='"+temp1+"' hoverText='"+temp1+"'/>");
						//	cnt=cnt+1;							
						}
						Categories.append("</categories>");
						Dataseries.append("</dataset>");
						lGraph.append(Categories.toString());
						lGraph.append(Dataseries.toString());
						lGraph.append("</chart>");
						chartDataXML=lGraph.toString();
						try{
							if(lResultSet != null){
								lResultSet.close();					
							}
						}
						catch(Exception ae){}
						lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/ZoomLine.swf\", \"myChartId_"+counter+"\", \"900\", \"350\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
						lData.append("</td></tr>");
						counter=counter+1;

					}
				}
				lData.append("</table>");
			}			
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
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
	public static String getGangliaStatistics(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		StringBuilder lData = new StringBuilder();
		String lQuery = "";
		Connection lConnection = null;
		Statement lStatement = null;
		int colCount=gettotColCount(pProjectName,pTestID,"ganglia");
		String[] colNames = gettotColumns(pProjectName,pTestID,"ganglia");
		String[] MetricCounterNames = new String[100];
		String[] MetricCounterHeads = new String[5];//for the get propertytypes
		String headerType="";
		int MetricCounterHeadscount=5;
		MetricCounterHeads[0]=PropertyConf.getProperty("GangliaCPU");
		MetricCounterHeads[1]=PropertyConf.getProperty("GangliaLoad");
		MetricCounterHeads[2]=PropertyConf.getProperty("GangliaMemory");
		MetricCounterHeads[3]=PropertyConf.getProperty("GangliaNetwork");
		MetricCounterHeads[4]=PropertyConf.getProperty("GangliaPackets");
		for(int i=1;i<(colCount-1);i++)
		{
			StringTokenizer stObj= new StringTokenizer(colNames[i],"_");
			stObj.nextToken();
			String temp1="";
			while(stObj.hasMoreTokens())
			{
				if(temp1.equals(""))
				temp1=temp1+stObj.nextToken();
				else
					temp1=temp1+"_"+stObj.nextToken();
			}
			MetricCounterNames[i]=temp1;			
		}
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();			
			for(int j=0;j<MetricCounterHeadscount;j++)
			{
				int Flag=0;
				if(j==0)
				{	headerType="CPU Counters";
					for(int k=1;k<(colCount-1);k++)
					{
						if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
						{
							Flag=1;
						}
					}
				}
				else if(j==1)
				{headerType="LOAD Counters";
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						Flag=1;
					}
				}
				}
				else if(j==2)
				{headerType="MEMORY Counters";
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						Flag=1;
					}
				}
				}
				else if(j==3)
				{headerType="NETWORK Counters";
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						Flag=1;
					}
				}
				}
				else if(j==4)
				{headerType="PACKETS Counters";
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						Flag=1;
					}
				}
				}
				if(Flag==1)
					lData.append("<b>"+headerType+"</b><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Server Name</font></b></td><td colspan=0><b><font size=\"2\">Counter Name</font></b></td><td colspan=0><b><font size=\"2\">Average Value</font></b></td></tr>");
				for(int k=1;k<(colCount-1);k++)
				{
					if(MetricCounterHeads[j].contains(MetricCounterNames[k]))
					{
						StringTokenizer stObj= new StringTokenizer(colNames[k],"_");
						String temp=stObj.nextToken();	
						lData.append("<tr bgcolor= #CCFFFF ><td colspan=0><b><font color=BLUE size=\"2\">"+temp+"</font></b></td><td colspan=0><b><font size=\"2\">"+MetricCounterNames[k]+"</font></b></td>");
						ResultSet lResultSet = null;
						lQuery = "SELECT avg("+colNames[k]+") FROM ganglia_stats_"+pProjectName+"_"+pTestID+";";
						lResultSet = lStatement.executeQuery(lQuery);
						lResultSet.next();
						lData.append("<td colspan=0><b><font size=\"2\">"+lResultSet.getString(1)+"</font></b></td>");
						lData.append("</tr>");
						try{
							if(lResultSet != null){
								lResultSet.close();					
							}
						}
						catch(Exception ae){}
					}
				}
				lData.append("</table>");
				}			
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
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
	public static int gettotColCount(String pProjectName,String pTestID,String types){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lQuery = "SELECT distinct(Col_Cnt) FROM "+types+"_stats_"+pProjectName+"_"+pTestID;
		int colCount=0;
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next()){
				colCount=lResultSet.getInt(1);
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
		return colCount;
	}
	public static String[] gettotColumns(String pProjectName,String pTestID,String types){
		int i=0;
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lQuery = "SELECT column_name FROM INFORMATION_SCHEMA.Columns where TABLE_NAME =\""+types+"_stats_"+pProjectName+"_"+pTestID+"\"";
		String fNamearr[] = new String[400];
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next()){
				fNamearr[i]=lResultSet.getString(1);
				i=i+1;
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
		return fNamearr;
	}
	public static String getGangliaGraphsTableData(String pProjectName,String pTestID,String ServerName,String CounterName){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		Statement lStatement1 = null;
		ResultSet lResultSet1 = null;
		String lQuery="";
		StringBuilder lData = new StringBuilder();		
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lData.append("<h3><font color=BLUE>STATISTICS:</font></h3><b>Project Name:</b> <i>"+pProjectName+"</i><br><b>TestID:</b> <i>"+pTestID+"</i><br><b>Server Name:</b> <i>"+ServerName+"</i><br><b>Counter Name:</b> <i>"+CounterName+"</i><br><br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Minimum</font></b></td><td colspan=0><b><font size=\"2\">Maximum</font></b></td><td colspan=0><b><font size=\"2\">");
			lData.append("Average Value");
			lQuery="SELECT min("+ServerName+"_"+CounterName+") as min_val,max("+ServerName+"_"+CounterName+") as max_val, avg("+ServerName+"_"+CounterName+") as val FROM ganglia_stats_"+pProjectName+"_"+pTestID+";";
			lData.append("</font></b></td></tr><tr bgcolor= #CCFFFF >");
			lStatement1 = lConnection.createStatement();
			lResultSet1 = lStatement1.executeQuery(lQuery);
			lResultSet1.next();
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet1.getString("min_val")+"</font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet1.getString("max_val")+"</font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet1.getString("val")+"</font></td>");

			lData.append("</tr></table><br><hr><br><h3><font color=BLUE>RAW DATA</font></h3>");
			
			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Time Stamp</font></b></td><td colspan=0><b><font size=\"2\">"+CounterName+"</font></b></td></tr>");
	
			lQuery="SELECT DATE_FORMAT(Sample_time, '%d-%m-%Y %h:%i:%s') AS lDate,"+ServerName+"_"+CounterName+" as val FROM ganglia_stats_"+pProjectName+"_"+pTestID+" order by sample_time;";
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next())
			{
				lData.append("<tr bgcolor= #CCFFFF >");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("lDate")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getFloat("val")+"</font></td>");
				lData.append("</tr>");				
			}
			lData.append("</table>");	
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
	//	System.out.println(lData.toString());
		return lData.toString();
	}
	
	public static String getPerfmonStatistics(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		StringBuilder lData = new StringBuilder();
		String lQuery = "";
		Connection lConnection = null;
		Statement lStatement = null;
		int colCount=gettotColCount(pProjectName,pTestID,"perfmon");
		String[] colNames = gettotColumns(pProjectName,pTestID,"perfmon");
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();			
			lData.append("<br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Server Name and Counter Name</font></b></td><td colspan=0><b><font size=\"2\">Average Value</font></b></td></tr>");
			for(int k=1;k<(colCount-1);k++)
			{
				lData.append("<tr bgcolor= #CCFFFF ><td colspan=0><b><font color=BLUE size=\"2\">"+colNames[k]+"</font></b></td>");
				ResultSet lResultSet = null;
				lQuery = "SELECT avg("+colNames[k]+") FROM perfmon_stats_"+pProjectName+"_"+pTestID+";";
			//	System.out.println(lQuery);
				lResultSet = lStatement.executeQuery(lQuery);
				lResultSet.next();
				lData.append("<td colspan=0><b><font size=\"2\">"+lResultSet.getFloat(1)+"</font></b></td>");
				lData.append("</tr>");
				try{
					if(lResultSet != null){
						lResultSet.close();					
					}
				}
				catch(Exception ae){}
			}
			lData.append("</table>");

		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
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
	public static String getPerfmonGraphs(String pProjectName,String pTestID){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		StringBuilder lData = new StringBuilder();
		String lQuery = "";
		Connection lConnection = null;
		Statement lStatement = null;
		int colCount=gettotColCount(pProjectName,pTestID,"perfmon");
		String[] colNames = gettotColumns(pProjectName,pTestID,"perfmon");		
		String chartDataXML="";
		int counter=0;
		lData.append("<h2><font color=BLUE>Perfmon Graphs</font></h2><br><h3> TEST ID: <u><i>"+pTestID+"</i></u> PROJECT NAME: <u><i>"+pProjectName+"</i></u></h3><hr>");
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();			
			lData.append("<br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Server Name and Counter Name</font></b></td><td colspan=0><b><font size=\"2\">Graph</font></b></td></tr>");
				for(int k=1;k<(colCount-1);k++)
				{					
						StringBuilder lGraph = new StringBuilder();
						StringBuilder Categories = new StringBuilder();
						StringBuilder Dataseries = new StringBuilder();
						lData.append("<tr><td colspan=0><b><font color=BLUE size=\"2\">"+colNames[k]+"</font></b></td>");
						ResultSet lResultSet = null;
						lQuery = "SELECT "+colNames[k]+",DATE_FORMAT(Sample_time, '%d-%m-%Y %h:%i:%s') AS lDate FROM perfmon_stats_"+pProjectName+"_"+pTestID+" order by sample_time;";
						lResultSet = lStatement.executeQuery(lQuery);
						lData.append("<td>");
						lData.append("<a href= \"javascript:getpopupperf('"+pProjectName+"','"+pTestID+"','"+colNames[k]+"')\" >Raw Data</a><br>");
						// clickURL='javascript:getpopupperf(\\\""+pProjectName+"\\\",\\\""+pTestID+"\\\",\\\""+colNames[k]+"\\\")'
						lData.append("<div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
						lGraph.append("<chart caption='"+colNames[k]+" metric data' xAxisName='Elapsed Time' yAxisName='"+colNames[k]+"' compactDataMode='1' dataSeparator='|' paletteThemeColor='5D57A5' divLineColor='5D57A5' divLineAlpha='40' vDivLineAlpha='40' allowPinMode='1' labelHeight='50'>");
						Categories.append("<categories>");
						Dataseries.append("<dataset seriesname='"+colNames[k]+"'>");
						while(lResultSet.next())
						{
							Dataseries.append(lResultSet.getString(1)+"|");
							Categories.append(lResultSet.getString(2)+"|");							
						//	Double temp1 =Double.parseDouble(lResultSet.getString(1));
						//	lGraph.append("<set name='"+cnt+"' value='"+temp1+"' hoverText='"+temp1+"'/>");													
						}
						Categories.append("</categories>");
						Dataseries.append("</dataset>");
						lGraph.append(Categories.toString());
						lGraph.append(Dataseries.toString());
						lGraph.append("</chart>");
						chartDataXML=lGraph.toString();
						try{
							if(lResultSet != null){
								lResultSet.close();					
							}
						}
						catch(Exception ae){}
						lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/ZoomLine.swf\", \"myChartId_"+counter+"\", \"900\", \"350\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
						lData.append("</td></tr>");
						counter=counter+1;					
				}
				lData.append("</table>");
						
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
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
	public static String getPerfmonGraphsTableData(String pProjectName,String pTestID,String CounterName){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		Statement lStatement1 = null;
		ResultSet lResultSet1 = null;
		String lQuery="";
		StringBuilder lData = new StringBuilder();		
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lData.append("<h3><font color=BLUE>STATISTICS:</font></h3><b>Project Name:</b> <i>"+pProjectName+"</i><br><b>TestID:</b> <i>"+pTestID+"</i><br><b>Server Name\\Metric Name:</b> <i>"+CounterName+"</i><br><br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Minimum</font></b></td><td colspan=0><b><font size=\"2\">Maximum</font></b></td><td colspan=0><b><font size=\"2\">");
			lData.append("Average Value");
			lQuery="SELECT min("+CounterName+") as min_val,max("+CounterName+") as max_val, avg("+CounterName+") as val FROM perfmon_stats_"+pProjectName+"_"+pTestID+";";
			lData.append("</font></b></td></tr><tr bgcolor= #CCFFFF >");
			lStatement1 = lConnection.createStatement();
			lResultSet1 = lStatement1.executeQuery(lQuery);
			lResultSet1.next();
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet1.getString("min_val")+"</font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet1.getString("max_val")+"</font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet1.getString("val")+"</font></td>");

			lData.append("</tr></table><br><hr><br><h3><font color=BLUE>RAW DATA</font></h3>");

			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Time Stamp</font></b></td><td colspan=0><b><font size=\"2\">"+CounterName+"</font></b></td></tr>");

			lQuery="SELECT DATE_FORMAT(Sample_time, '%d-%m-%Y %h:%i:%s') AS lDate,"+CounterName+" as val FROM perfmon_stats_"+pProjectName+"_"+pTestID+" order by sample_time;";
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next())
			{
				lData.append("<tr bgcolor= #CCFFFF >");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getString("lDate")+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+lResultSet.getFloat("val")+"</font></td>");
				lData.append("</tr>");				
			}
			lData.append("</table>");	
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
		//	System.out.println(lData.toString());
		return lData.toString();
	}
}
