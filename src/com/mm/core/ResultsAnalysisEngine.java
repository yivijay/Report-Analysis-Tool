package com.mm.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;


public class ResultsAnalysisEngine {
	public static String generateAnalysisFilter(String pProjectName,String pTestID)
	{
		StringBuilder lData= new StringBuilder();
		String Tokens=checkResultsExistence(pTestID,pProjectName);
		lData.append("<br>");
		if(Tokens.contains("wily"))
		{
			lData.append("<h4><font color=\"blue\">WILY COUNTERS</font></h4>");
			lData.append("Available Wily Counters for Correlation:<br>");
			lData.append("<b>Note:</b>Please have atleast one counter selected from the list.<br>");
			lData.append("<br><input type=\"checkbox\" name=\"SelectAll_WilyCounters\" onClick=\"toggle(this,'Wily')\"><i><u>Toggle All</u></i><br>");
			lData.append(getWilyCounterlist(pProjectName,pTestID));
		}
		if(Tokens.contains("ganglia"))
		{
			lData.append("<h4><font color=\"blue\">GANGLIA COUNTERS</font></h4>");
			lData.append("Available Ganglia Counters for Correlation:<br>");
			lData.append("<b>Note:</b>Please have atleast one counter selected from the list.<br>");
			lData.append("<br><input type=\"checkbox\" name=\"SelectAll_GangCounters\" onClick=\"toggle(this,'Ganglia')\"><i><u>Toggle All</u></i><br>");
			lData.append(getGPCounterlist(pProjectName,pTestID,"ganglia"));
		}
		if(Tokens.contains("perfmon"))
		{
			lData.append("<h4><font color=\"blue\">PERFMON COUNTERS</font></h4>");
			lData.append("Available Perfmon Counters for Correlation:<br>");
			lData.append("<b>Note:</b>Please have atleast one counter selected from the list.<br>");
			lData.append("<br><input type=\"checkbox\" name=\"SelectAll_PerfCounters\" onClick=\"toggle(this,'Perfmon')\"><i><u>Toggle All</u></i><br>");
			lData.append(getGPCounterlist(pProjectName,pTestID,"perfmon"));
		}
		if(Tokens.contains("vmstat"))
		{
			
		}
		
		lData.append("");
		lData.append("");
		return lData.toString();
	}
	public static String generateLRCounterFilter(String CounterName)
	{
		StringBuilder lData= new StringBuilder();
		lData.append("<SELECT name=\"LRCNTRDATA\"><OPTION VALUE=\"Select\">--Select--</OPTION>");
		if(CounterName.equals("Response_Time"))
		{
			lData.append("<OPTION VALUE=\"Throughput\">Throughput</OPTION><OPTION VALUE=\"Running_Vusers\">Running_Vusers</OPTION><OPTION VALUE=\"Errors\">Errors</OPTION>");
		}
		if(CounterName.equals("Throughput"))
		{
			lData.append("<OPTION VALUE=\"Running_Vusers\">Running_Vusers</OPTION><OPTION VALUE=\"Errors\">Errors</OPTION>");
		}
		if(CounterName.equals("Running_Vusers"))
		{
			lData.append("<OPTION VALUE=\"Throughput\">Throughput</OPTION><OPTION VALUE=\"Errors\">Errors</OPTION>");
		}
		if(CounterName.equals("Errors"))
		{
			lData.append("<OPTION VALUE=\"Throughput\">Throughput</OPTION><OPTION VALUE=\"Running_Vusers\">Running_Vusers</OPTION>");
		}

		lData.append("</SELECT>");
		return lData.toString();
	}
	public static String getWilyCounterlist(String pProjectName,String pTestID)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement_sum = null;
		ResultSet lResultSet_sum = null;
		String lQuery_sum="SELECT agent_name,resource_name,metric_name,Counter_Number FROM wily_stats_"+pProjectName+"_"+pTestID+" group by metric_name,resource_name,agent_name order by metric_name,resource_name,agent_name;";
		StringBuilder lData = new StringBuilder();
		String Agent_Name="",Resource_Name="",Metric_Name="", Counter_Number="";

		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement_sum = lConnection.createStatement();
			lResultSet_sum = lStatement_sum.executeQuery(lQuery_sum);
			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Agent/Server Name</font></b></td><td colspan=0><b><font size=\"2\">Resource Name</font></b></td><td colspan=0><b><font size=\"2\">Metric Name</font></b></td><td colspan=0><b><font size=\"2\">Selection</font></b></td></tr>");
			while (lResultSet_sum.next())
			{
				lData.append("<tr bgcolor= #CCFFFF >");
				Agent_Name = lResultSet_sum.getString("agent_name");
				Resource_Name = lResultSet_sum.getString("resource_name");
				Metric_Name = lResultSet_sum.getString("metric_name");
				Counter_Number = lResultSet_sum.getString("Counter_Number");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+Agent_Name+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+Resource_Name+"</font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+Metric_Name+"</font></td>");
//				lData.append("<td colspan=0 align=\"left\"><font size=\"2\"><input type=\"checkbox\" name=\"WilyCounters\" value=\""+Counter_Number+"\" checked></font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\"><input type=\"checkbox\" name=\"WilyCounters\" value=\""+Counter_Number+"\" ></font></td>");
				lData.append("</tr>");
			}
			lData.append("</table><br>");
		}
		catch(Exception ae){
			ae.printStackTrace();
		}
		finally{
			try{
				if(lResultSet_sum != null){
					lResultSet_sum.close();					
				}
				if(lStatement_sum != null){
					lStatement_sum.close();					
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
	public static String getGPCounterlist(String pProjectName,String pTestID,String types)
	{
		StringBuilder lData= new StringBuilder();
		String fNamearr[] = new String[400];
		int counterCnt=0;
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lQuery = "SELECT column_name FROM INFORMATION_SCHEMA.Columns where TABLE_NAME =\""+types+"_stats_"+pProjectName+"_"+pTestID+"\"";
		try{
			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Counter Name</font></b></td><td colspan=0><b><font size=\"2\">Selection</font></b></td></tr>");
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next())
			{
				fNamearr[counterCnt]=lResultSet.getString(1);
				counterCnt=counterCnt+1;
			}
			for(int i=1;i<counterCnt-1;i++)
			{
				lData.append("<tr bgcolor= #CCFFFF >");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+fNamearr[i].toUpperCase()+"</font></td>");
//				lData.append("<td colspan=0 align=\"left\"><font size=\"2\"><input type=\"checkbox\" name=\""+types+"Counters\" value=\""+fNamearr[i]+"\" checked></font></td>");
				lData.append("<td colspan=0 align=\"left\"><font size=\"2\"><input type=\"checkbox\" name=\""+types+"Counters\" value=\""+fNamearr[i]+"\" ></font></td>");
				lData.append("</tr>");
			}
			lData.append("</table><br>");
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
		String lQuery = "show tables where Tables_in_"+lDbUsed+" like '%"+ProjectName+"_"+TestID+"%';";
	//	System.out.println(lQuery);
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
	public static String getStartEndTime(String TestID,String ProjectName)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lTable="";
		String lQuery = "SELECT counter_value FROM lr_stats_details_"+ProjectName+" where counter_name like '%Period%' and test_id=\""+TestID+"\";";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				lTable=lResultSet.getString(1);
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
		StringTokenizer hyphenext=new StringTokenizer(lTable.replace(" ",""),"-");
		String StartTime= DateformatConverter("dd/MM/yyyyHH:mm:ss", "yyyy-MM-dd HH:mm:ss",hyphenext.nextToken());
		String EndTime= DateformatConverter("dd/MM/yyyyHH:mm:ss", "yyyy-MM-dd HH:mm:ss",hyphenext.nextToken());
		return StartTime+","+EndTime;
	}	
	public static String DateformatConverter(String OrigFormat,String NewFormat,String OrigDate)
	{
		SimpleDateFormat OriginalFormat = new SimpleDateFormat(OrigFormat,Locale.ENGLISH);
		SimpleDateFormat newFormat = new SimpleDateFormat(NewFormat);
		String FormattedDate="";
		try
		{
		//	OriginalFormat.setTimeZone(TimeZone.getTimeZone("BST"));
			Date date=OriginalFormat.parse(OrigDate);
			FormattedDate=newFormat.format(date);			
		} 
		catch (ParseException e)
		{
			e.printStackTrace();
		}
//		System.out.println(OrigDate+"\n"+FormattedDate);
		return FormattedDate;
	}
		
//////For Response Time
	public static String AnalysisPageLoadChartString(String ProjectName,String TestID,String StartTime,String EndTime,String LR_CounterName,String LRCNTRDATA,String WilyTokens,String GangliaTokens,String PerfmonTokens)
	{	
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet1 = null;
		StringBuilder lGraph2 = new StringBuilder();
		try
		{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			String lQuery="SELECT Transaction_Name,ROUND(Avg(Transaction_RESPONSE_Time), 3) as avgVal FROM responsetime_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"' group by Transaction_Name;";
			lGraph2.append("<chart caption='Average Transaction Response Time' showLabels='0' xAxisName='Transactions' yAxisName='Average Transaction Response Time' showValues='0' formatNumberScale='0' showBorder='1'>");
			int ctx=1;
			lResultSet1 = lStatement.executeQuery(lQuery);
			while (lResultSet1.next())
			{
				String TxnName= lResultSet1.getString("Transaction_Name");
				String Avg_Value=lResultSet1.getString("avgVal");
				lGraph2.append("<set label='Txn_"+ctx+"' value='"+Avg_Value+"' hoverText='Transaction Name: "+TxnName+", Average Value: "+Avg_Value+"' link='javascript:updatechart(\\\""+ProjectName+"\\\",\\\""+TestID+"\\\",\\\""+TxnName+"\\\",\\\""+StartTime+"\\\",\\\""+EndTime+"\\\",\\\""+LRCNTRDATA+"\\\",\\\""+WilyTokens+"\\\",\\\""+GangliaTokens+"\\\",\\\""+PerfmonTokens+"\\\")'/>");
				ctx=ctx+1;
			}
			lGraph2.append("</chart>");
		}
		catch(Exception ae){ae.printStackTrace();}
		finally{try{if(lResultSet1 != null){lResultSet1.close();}if(lStatement != null){lStatement.close();}if(lConnection != null){lConnection.close();}}catch(Exception ae){}}
		return lGraph2.toString();
	}
	public static String AnalysisPageLoadChartFromTimer(String ProjectName,String TestID,String StartTime,String EndTime,String LR_CounterName,String LRCNTRDATA,String WilyTokens,String GangliaTokens,String PerfmonTokens)
	{	
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet1 = null;
		StringBuilder lGraph2 = new StringBuilder();
		try
		{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			String lQuery="SELECT Transaction_Name,ROUND(Avg(Transaction_RESPONSE_Time), 3) as avgVal FROM responsetime_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"' group by Transaction_Name;";
			lGraph2.append("<chart caption='Average Transaction Response Time' showLabels='0' xAxisName='Transactions' yAxisName='Average Transaction Response Time' showValues='0' formatNumberScale='0' showBorder='1'>");
			int ctx=1;
			lResultSet1 = lStatement.executeQuery(lQuery);
			while (lResultSet1.next())
			{
				String TxnName= lResultSet1.getString("Transaction_Name");
				String Avg_Value=lResultSet1.getString("avgVal");
				lGraph2.append("<set label='Txn_"+ctx+"' value='"+Avg_Value+"' hoverText='Transaction Name: "+TxnName+", Average Value: "+Avg_Value+"' link='javascript:updatechart(\""+ProjectName+"\",\""+TestID+"\",\""+TxnName+"\",\""+StartTime+"\",\""+EndTime+"\",\""+LRCNTRDATA+"\",\""+WilyTokens+"\",\""+GangliaTokens+"\",\""+PerfmonTokens+"\")'/>");
				ctx=ctx+1;
			}
			lGraph2.append("</chart>");
		}
		catch(Exception ae){ae.printStackTrace();}
		finally{try{if(lResultSet1 != null){lResultSet1.close();}if(lStatement != null){lStatement.close();}if(lConnection != null){lConnection.close();}}catch(Exception ae){}}
		return lGraph2.toString();
	}
	public static String AnalysisLoadFromRespGraph(String ProjectName ,String TestID ,String TxnName,String StartTime,String EndTime,String LRCNTRDATA,String WilyTokens,String GangliaTokens,String PerfmonTokens)
	{
		StringBuilder lData= new StringBuilder();
		String resTokens=checkResultsExistence(TestID,ProjectName);
		if(LRCNTRDATA.equals("Select"))
		{/*do nothing*/}
		else if(LRCNTRDATA.equals("Throughput"))
		{
			if(resTokens.contains("throughput"))
			{
				lData.append("<br><b><font color=blue>CORRELATION: RESPONSE TIME (<i>"+TxnName.toUpperCase()+"</i>) WITH LR THROUGHPUT DATA</font></b><BR>");
				lData.append("<br>");
				lData.append(generateThroughputDataSeries(ProjectName,TestID,StartTime,EndTime,TxnName));
				lData.append("<br>");
			}
			else
			{		
				lData.append("<b>Throughput data doesn't exist for this test.<br>Please upload the Load Runner Throughput raw data for accessing this functionality.</b><br>");
			}
		}
		else if(LRCNTRDATA.equals("Running_Vusers"))
		{
			if(resTokens.contains("runningvusers"))
			{
				lData.append("<br><b><font color=blue>CORRELATION: RESPONSE TIME (<i>"+TxnName.toUpperCase()+"</i>) WITH LR RUNNING VUSERS DATA</font></b><BR>");
				lData.append("<br>");
				lData.append(generateRunningVUDataSeries(ProjectName,TestID,StartTime,EndTime,TxnName));
				lData.append("<br>");
			}
			else
			{
				lData.append("<b>Running Vusers data doesn't exist for this test.<br>Please upload the Load Runner Running Vusers raw data for accessing this functionality.</b><br>");
			}
		}
		else if(LRCNTRDATA.equals("Errors"))
		{
			if(resTokens.contains("errors"))
			{
				lData.append("<br><b><font color=blue>CORRELATION: RESPONSE TIME (<i>"+TxnName.toUpperCase()+"</i>) WITH LR ERRORS DATA</font></b><BR>");
				lData.append("<br>");
				lData.append(generateErrorDataSeries(ProjectName,TestID,StartTime,EndTime,TxnName));
				lData.append("<br>");
			}
			else
			{
				lData.append("<b>Errors data doesn't exist for this test.<br>Please upload the Load Runner Errors raw data for accessing this functionality.</b><br>");
			}
		}
		if(resTokens.contains("wily"))
		{
			StringTokenizer WilyTokenizer=new StringTokenizer(WilyTokens,",");
			lData.append("<br><b><font color=blue>CORRELATION: RESPONSE TIME (<i>"+TxnName.toUpperCase()+"</i>) WITH WILY COUNTERS</font></b><BR>");
			while(WilyTokenizer.hasMoreTokens())
			{
				String WilycounterName=WilyTokenizer.nextToken();
				lData.append("<br>");
				lData.append(generateWilyCounterNames(ProjectName,TestID,WilycounterName));
				lData.append(generateWilyDataSeries(ProjectName,TestID,StartTime,EndTime,TxnName,WilycounterName));
				lData.append("<br>");
			}
		}
		if(resTokens.contains("ganglia"))
		{
			StringTokenizer GangliaTokenizer=new StringTokenizer(GangliaTokens,",");
			lData.append("<br><b><font color=blue>CORRELATION: RESPONSE TIME (<i>"+TxnName.toUpperCase()+"</i>) WITH GANGLIA COUNTERS</font></b><BR>");
			while(GangliaTokenizer.hasMoreTokens())
			{
				String colName=GangliaTokenizer.nextToken();
				lData.append("<br>");
				lData.append("<b>"+colName+" : </b>");
				lData.append(generateGPDataSeries(ProjectName,TestID,StartTime,EndTime,TxnName,colName,"ganglia"));
				lData.append("<br>");
			}
		}
		if(resTokens.contains("perfmon"))
		{
			StringTokenizer PerfmonTokenizer=new StringTokenizer(PerfmonTokens,",");
			lData.append("<br><b><font color=blue>CORRELATION: RESPONSE TIME (<i>"+TxnName.toUpperCase()+"</i>) WITH PERFMON COUNTERS</font></b><BR>");
			while(PerfmonTokenizer.hasMoreTokens())
			{
				String colName=PerfmonTokenizer.nextToken();
				lData.append("<br>");
				lData.append("<b>"+colName+" : </b>");
				lData.append(generateGPDataSeries(ProjectName,TestID,StartTime,EndTime,TxnName,colName,"perfmon"));
				lData.append("<br>");
			}
		}
    	return lData.toString();
	}	
	public static String generateGPDataSeries(String ProjectName,String TestID,String StartTime,String EndTime,String TxnName,String colName,String types)
	{
		StringBuilder lData= new StringBuilder();
		String chID=ResultExtractEngine.RandomsesValID();
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		lData.append("<div id=\"chartdiv"+chID+"\" align=\"center\">Chart will load here</div>");
		String lQuery="(SELECT DATE_FORMAT(Sample_time, '%d-%m-%Y %h:%i:%s') AS lDate,"+colName+",null as Transaction_Response_Time FROM "+types+"_stats_"+ProjectName+"_"+TestID+" where sample_time>='"+StartTime+"' and sample_time<='"+EndTime+"')UNION(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,null,Transaction_Response_Time FROM responsetime_stats_"+ProjectName+"_"+TestID+"  where Transaction_Name='"+TxnName+"' and Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
		try{
			StringBuilder lGraph= new StringBuilder();			
			StringBuilder Categories= new StringBuilder();
			StringBuilder DataseriesL= new StringBuilder();
			StringBuilder DataseriesR= new StringBuilder();
			lGraph.append("<chart caption='Response Time ("+TxnName.toUpperCase()+") Vs "+colName.toUpperCase()+"' connectNullData='1' xAxisName='Time' showValues='0' divLineAlpha='100' numVDivLines='4' vDivLineAlpha='0' showAlternateVGridColor='1' alternateVGridAlpha='5' canvasPadding='0'>");
			Categories.append("<categories>");
			DataseriesL.append("<axis title='Response Time (s)' titlePos='left' tickWidth='10' divlineisdashed='1' numberSuffix=' s'><dataset seriesName='Response Time [s]'>");
			DataseriesR.append("<axis title='"+types.toUpperCase()+" Stats' titlePos='right' axisOnLeft='0' numDivLines='14' tickWidth='10' divlineisdashed='1' ><dataset seriesName='"+colName.toUpperCase()+"'>");
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				String ColR=lResultSet.getString(colName);
				String ColL=lResultSet.getString("Transaction_Response_Time");
				if(ColR==null|ColR=="")
				{ColR="";}
				if(ColL==null|ColL=="")
				{ColL="";}
				Categories.append("<category label='"+lResultSet.getString("lDate")+"' />");
				DataseriesR.append("<set value='"+ColR+"' />");
				DataseriesL.append("<set value='"+ColL+"' />");
			}
			Categories.append("</categories>");
			DataseriesL.append("</dataset></axis>");
			DataseriesR.append("</dataset></axis>");
			lGraph.append(Categories.toString());
			lGraph.append(DataseriesL.toString());
			lGraph.append(DataseriesR.toString());
			lGraph.append("</chart>");
			lData.append("<script type=\"text/javascript\">var myChart_"+chID+" = new FusionCharts(\"../FusionCharts/MultiAxisLine.swf\", \"myChartId_"+chID+"\", \"1200\", \"450\", \"0\", \"1\");myChart_"+chID+".setDataXML(\""+lGraph.toString()+"\");myChart_"+chID+".render(\"chartdiv"+chID+"\");</script>");
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
	public static String generateWilyDataSeries(String ProjectName,String TestID,String StartTime,String EndTime,String TxnName,String WilycounterName)
	{
		StringBuilder lData= new StringBuilder();
		String lQuery="(SELECT DATE_FORMAT(TimeStamp, '%d-%m-%Y %h:%i:%s') AS lDate, metric_value, null as Transaction_Response_Time FROM wily_stats_"+ProjectName+"_"+TestID+" where counter_number='"+WilycounterName+"' and TimeStamp>='"+StartTime+"' and TimeStamp<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, null, Transaction_Response_Time FROM responsetime_stats_"+ProjectName+"_"+TestID+" where Transaction_Name='"+TxnName+"' and Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
		String chID=ResultExtractEngine.RandomsesValID();
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		try{
			StringBuilder lGraph= new StringBuilder();			
			StringBuilder Categories= new StringBuilder();
			StringBuilder DataseriesL= new StringBuilder();
			StringBuilder DataseriesR= new StringBuilder();
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);			
			lGraph.append("<chart caption='Response Time ("+TxnName.toUpperCase()+") Vs Wily Stats' connectNullData='1' xAxisName='Time' showValues='0' divLineAlpha='100' numVDivLines='4' vDivLineAlpha='0' showAlternateVGridColor='1' alternateVGridAlpha='5' canvasPadding='0'>");
			Categories.append("<categories>");
			DataseriesL.append("<axis title='Response Time (s)' titlePos='left' tickWidth='10' divlineisdashed='1' numberSuffix=' s'><dataset seriesName='Response Time [s]'>");
			DataseriesR.append("<axis title='Wily Stats' titlePos='right' axisOnLeft='0' numDivLines='14' tickWidth='10' divlineisdashed='1' ><dataset seriesName='Wily Stats'>");
		
			while(lResultSet.next())
			{
				String ColR=lResultSet.getString("metric_value");
				String ColL=lResultSet.getString("Transaction_Response_Time");
				if(ColR==null|ColR=="")
				{ColR="";}
				if(ColL==null|ColL=="")
				{ColL="";}
				Categories.append("<category label='"+lResultSet.getString("lDate")+"' />");
				DataseriesR.append("<set value='"+ColR+"' />");
				DataseriesL.append("<set value='"+ColL+"' />");
			}
			Categories.append("</categories>");
			DataseriesL.append("</dataset></axis>");
			DataseriesR.append("</dataset></axis>");
			lGraph.append(Categories.toString());
			lGraph.append(DataseriesL.toString());
			lGraph.append(DataseriesR.toString());
			lGraph.append("</chart>");
			lData.append("<div id=\"chartdiv"+chID+"\" align=\"center\">Chart will load here</div>");			
			lData.append("<script type=\"text/javascript\">var myChart_"+chID+" = new FusionCharts(\"../FusionCharts/MultiAxisLine.swf\", \"myChartId_"+chID+"\", \"1200\", \"450\", \"0\", \"1\");myChart_"+chID+".setDataXML(\""+lGraph.toString()+"\");myChart_"+chID+".render(\"chartdiv"+chID+"\");</script>");
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
	public static String generateWilyCounterNames(String ProjectName,String TestID,String WilycounterName)
	{
		StringBuilder lData= new StringBuilder();
		String lQuery="SELECT distinct agent_name, resource_name,Metric_name FROM testreports.wily_stats_"+ProjectName+"_"+TestID+" where counter_number='"+WilycounterName+"';";
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lResultSet.next();
			String agent_name=lResultSet.getString("agent_name");
			String resource_name=lResultSet.getString("resource_name");
			String metric_name=lResultSet.getString("metric_name");
			lData.append("AGENT NAME: <b>"+agent_name+"</b><br>");
			lData.append("RESOURCE NAME: <b>"+resource_name+"</b><br>");
			lData.append("METRIC NAME: <b>"+metric_name+"</b><br>");
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
	public static String generateErrorDataSeries(String ProjectName,String TestID,String StartTime,String EndTime,String TxnName)
	{
		StringBuilder lData= new StringBuilder();
		String chID=ResultExtractEngine.RandomsesValID();
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		lData.append("<div id=\"chartdiv"+chID+"\" align=\"center\">Chart will load here</div>");
		String lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,error_type as Errors,null as Transaction_Response_Time FROM errors_stats_"+ProjectName+"_"+TestID+" where Txn_Name='"+TxnName+"' and Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"')UNION(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,null,Transaction_Response_Time FROM responsetime_stats_"+ProjectName+"_"+TestID+"  where Transaction_Name='"+TxnName+"' and Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
		System.out.println(lQuery);
		try{
			StringBuilder lGraph= new StringBuilder();			
			StringBuilder Categories= new StringBuilder();
			StringBuilder DataseriesL= new StringBuilder();
			StringBuilder DataseriesR= new StringBuilder();
			lGraph.append("<chart caption='Response Time ("+TxnName.toUpperCase()+") Vs Errors' connectNullData='1' xAxisName='Time' showValues='0' divLineAlpha='100' numVDivLines='4' vDivLineAlpha='0' showAlternateVGridColor='1' alternateVGridAlpha='5' canvasPadding='0'>");
			Categories.append("<categories>");
			DataseriesL.append("<axis title='Response Time (s)' titlePos='left' tickWidth='10' divlineisdashed='1' numberSuffix=' s'><dataset seriesName='Response Time [s]'>");
			DataseriesR.append("<axis title='Errors' titlePos='right' axisOnLeft='0' numDivLines='14' tickWidth='10' divlineisdashed='1' ><dataset seriesName='Errors'>");
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				String ColR=lResultSet.getString("Errors");
				String ColL=lResultSet.getString("Transaction_Response_Time");
				if(ColR==null|ColR=="")
				{ColR="0";}
				else if(ColR!="")
				{ColR="1";}

				if(ColL==null|ColL=="")
				{ColL="";}
				Categories.append("<category label='"+lResultSet.getString("lDate")+"' />");
				DataseriesR.append("<set value='"+ColR+"' />");
				DataseriesL.append("<set value='"+ColL+"' />");
			}
			Categories.append("</categories>");
			DataseriesL.append("</dataset></axis>");
			DataseriesR.append("</dataset></axis>");
			lGraph.append(Categories.toString());
			lGraph.append(DataseriesL.toString());
			lGraph.append(DataseriesR.toString());
			lGraph.append("</chart>");
			lData.append("<script type=\"text/javascript\">var myChart_"+chID+" = new FusionCharts(\"../FusionCharts/MultiAxisLine.swf\", \"myChartId_"+chID+"\", \"1200\", \"450\", \"0\", \"1\");myChart_"+chID+".setDataXML(\""+lGraph.toString()+"\");myChart_"+chID+".render(\"chartdiv"+chID+"\");</script>");
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
	public static int gettotRowCount(String pProjectName,String pTestID,String types){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lQuery = "SELECT count(1) FROM "+types+"_stats_"+pProjectName+"_"+pTestID;
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
	public static String generateThroughputDataSeries(String ProjectName,String TestID,String StartTime,String EndTime,String TxnName)
	{
		StringBuilder lData= new StringBuilder();
		String chID=ResultExtractEngine.RandomsesValID();
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		String Granularity = PropertyConf.getProperty("Granularity");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		lData.append("<div id=\"chartdiv"+chID+"\" align=\"center\">Chart will load here</div>");
		String lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,throughput as Throughput,null as Transaction_Response_Time FROM throughput_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"' and row_num MOD "+Granularity+"=0 order by Scenario_Elapsed_Time)UNION(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,null,Transaction_Response_Time FROM responsetime_stats_"+ProjectName+"_"+TestID+"  where Transaction_Name='"+TxnName+"' and Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
		System.out.println(lQuery);
		try{
			StringBuilder lGraph= new StringBuilder();			
			StringBuilder Categories= new StringBuilder();
			StringBuilder DataseriesL= new StringBuilder();
			StringBuilder DataseriesR= new StringBuilder();
			lGraph.append("<chart caption='Response Time ("+TxnName.toUpperCase()+") Vs Throughput' connectNullData='1' xAxisName='Time' showValues='0' divLineAlpha='100' numVDivLines='4' vDivLineAlpha='0' showAlternateVGridColor='1' alternateVGridAlpha='5' canvasPadding='0'>");
			Categories.append("<categories>");
			DataseriesL.append("<axis title='Response Time (s)' titlePos='left' tickWidth='10' divlineisdashed='1' numberSuffix=' s'><dataset seriesName='Response Time [s]'>");
			DataseriesR.append("<axis title='Throughput (bytes)' titlePos='right' axisOnLeft='0' numDivLines='14' tickWidth='10' divlineisdashed='1' ><dataset seriesName='Throughput'>");
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				String ColR=lResultSet.getString("Throughput");
				String ColL=lResultSet.getString("Transaction_Response_Time");
				if(ColR==null|ColR=="")
				{ColR="";}
				if(ColL==null|ColL=="")
				{ColL="";}
				Categories.append("<category label='"+lResultSet.getString("lDate")+"' />");
				DataseriesR.append("<set value='"+ColR+"' />");
				DataseriesL.append("<set value='"+ColL+"' />");
			}
			Categories.append("</categories>");
			DataseriesL.append("</dataset></axis>");
			DataseriesR.append("</dataset></axis>");
			lGraph.append(Categories.toString());
			lGraph.append(DataseriesL.toString());
			lGraph.append(DataseriesR.toString());
			lGraph.append("</chart>");
			lData.append("<script type=\"text/javascript\">var myChart_"+chID+" = new FusionCharts(\"../FusionCharts/MultiAxisLine.swf\", \"myChartId_"+chID+"\", \"1200\", \"450\", \"0\", \"1\");myChart_"+chID+".setDataXML(\""+lGraph.toString()+"\");myChart_"+chID+".render(\"chartdiv"+chID+"\");</script>");
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
	public static String generateRunningVUDataSeries(String ProjectName,String TestID,String StartTime,String EndTime,String TxnName)
	{
		StringBuilder lData= new StringBuilder();
		String chID=ResultExtractEngine.RandomsesValID();
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		lData.append("<div id=\"chartdiv"+chID+"\" align=\"center\">Chart will load here</div>");
		String lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,vusers as vusers,null as Transaction_Response_Time FROM runningvusers_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"')UNION(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,null,Transaction_Response_Time FROM responsetime_stats_"+ProjectName+"_"+TestID+"  where Transaction_Name='"+TxnName+"' and Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
		System.out.println(lQuery);
		try{
			StringBuilder lGraph= new StringBuilder();			
			StringBuilder Categories= new StringBuilder();
			StringBuilder DataseriesL= new StringBuilder();
			StringBuilder DataseriesR= new StringBuilder();
			lGraph.append("<chart caption='Response Time ("+TxnName.toUpperCase()+") Vs Running Vusers' connectNullData='1' xAxisName='Time' showValues='0' divLineAlpha='100' numVDivLines='4' vDivLineAlpha='0' showAlternateVGridColor='1' alternateVGridAlpha='5' canvasPadding='0'>");
			Categories.append("<categories>");
			DataseriesL.append("<axis title='Response Time (s)' titlePos='left' tickWidth='10' divlineisdashed='1' numberSuffix=' s'><dataset seriesName='Response Time [s]'>");
			DataseriesR.append("<axis title='Running Vusers' titlePos='right' axisOnLeft='0' numDivLines='14' tickWidth='10' divlineisdashed='1' ><dataset seriesName='Running Vusers'>");
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				String ColR=lResultSet.getString("vusers");
				String ColL=lResultSet.getString("Transaction_Response_Time");
				if(ColR==null|ColR=="")
				{ColR="";}
				if(ColL==null|ColL=="")
				{ColL="";}
				Categories.append("<category label='"+lResultSet.getString("lDate")+"' />");
				DataseriesR.append("<set value='"+ColR+"' />");
				DataseriesL.append("<set value='"+ColL+"' />");
			}
			Categories.append("</categories>");
			DataseriesL.append("</dataset></axis>");
			DataseriesR.append("</dataset></axis>");
			lGraph.append(Categories.toString());
			lGraph.append(DataseriesL.toString());
			lGraph.append(DataseriesR.toString());
			lGraph.append("</chart>");
			lData.append("<script type=\"text/javascript\">var myChart_"+chID+" = new FusionCharts(\"../FusionCharts/MultiAxisLine.swf\", \"myChartId_"+chID+"\", \"1200\", \"450\", \"0\", \"1\");myChart_"+chID+".setDataXML(\""+lGraph.toString()+"\");myChart_"+chID+".render(\"chartdiv"+chID+"\");</script>");
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
	
//////For Other Counters
	public static String generateGPDataOTCSeries(String ProjectName,String TestID,String StartTime,String EndTime,String Counter_Name,String colName,String types)
	{
		String lQuery="";
		StringBuilder lData= new StringBuilder();
		String chID=ResultExtractEngine.RandomsesValID();
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		lData.append("<div id=\"chartdiv"+chID+"\" align=\"center\">Chart will load here</div>");
		if(Counter_Name.equals("Throughput"))
		{
			lQuery="(SELECT DATE_FORMAT(Sample_time, '%d-%m-%Y %h:%i:%s') AS lDate,"+colName+",null as Throughput FROM "+types+"_stats_"+ProjectName+"_"+TestID+" where sample_time>='"+StartTime+"' and sample_time<='"+EndTime+"')UNION(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,null,Throughput FROM throughput_stats_"+ProjectName+"_"+TestID+"  where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"' and row_num MOD "+PropertyConf.getProperty("Granularity")+"=0 order by Scenario_Elapsed_Time) order by lDate;";
		}
		else if(Counter_Name.equals("Running_Vusers"))
		{
	 		lQuery="(SELECT DATE_FORMAT(Sample_time, '%d-%m-%Y %h:%i:%s') AS lDate,"+colName+",null as vusers FROM "+types+"_stats_"+ProjectName+"_"+TestID+" where sample_time>='"+StartTime+"' and sample_time<='"+EndTime+"')UNION(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,null,vusers FROM runningvusers_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
		}
		else if(Counter_Name.equals("Errors"))
		{
			lQuery="(SELECT DATE_FORMAT(Sample_time, '%d-%m-%Y %h:%i:%s') AS lDate,"+colName+",null as Errors FROM "+types+"_stats_"+ProjectName+"_"+TestID+" where sample_time>='"+StartTime+"' and sample_time<='"+EndTime+"')UNION(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate,null,error_type as Errors FROM errors_stats_"+ProjectName+"_"+TestID+"  where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
		}

		try{
			StringBuilder lGraph= new StringBuilder();			
			StringBuilder Categories= new StringBuilder();
			StringBuilder DataseriesL= new StringBuilder();
			StringBuilder DataseriesR= new StringBuilder();
			lGraph.append("<chart caption='"+Counter_Name.toUpperCase()+" Vs "+colName.toUpperCase()+"' connectNullData='1' xAxisName='Time' showValues='0' divLineAlpha='100' numVDivLines='4' vDivLineAlpha='0' showAlternateVGridColor='1' alternateVGridAlpha='5' canvasPadding='0'>");
			Categories.append("<categories>");
			DataseriesL.append("<axis title='"+Counter_Name.toUpperCase()+"' titlePos='left' tickWidth='10' divlineisdashed='1'><dataset seriesName='"+Counter_Name.toUpperCase()+"'>");
			DataseriesR.append("<axis title='"+types.toUpperCase()+" Stats' titlePos='right' axisOnLeft='0' numDivLines='14' tickWidth='10' divlineisdashed='1' ><dataset seriesName='"+colName.toUpperCase()+"'>");
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				Categories.append("<category label='"+lResultSet.getString(1)+"' />");
				String ColR=lResultSet.getString(2);
				String ColL=lResultSet.getString(3);
				if(Counter_Name.equals("Errors"))
				{
					if(ColL==null|ColL=="")
					{ColL="0";}
					else if(ColL!="")
					{ColL="1";}
				}
				else
				{
					if(ColL==null|ColL=="")
					{ColL="";}
				}
				if(ColR==null|ColR=="")
				{ColR="";}
				DataseriesR.append("<set value='"+ColR+"' />");
				DataseriesL.append("<set value='"+ColL+"' />");
			}
			Categories.append("</categories>");
			DataseriesL.append("</dataset></axis>");
			DataseriesR.append("</dataset></axis>");
			lGraph.append(Categories.toString());
			lGraph.append(DataseriesL.toString());
			lGraph.append(DataseriesR.toString());
			lGraph.append("</chart>");
			lData.append("<script type=\"text/javascript\">var myChart_"+chID+" = new FusionCharts(\"../FusionCharts/MultiAxisLine.swf\", \"myChartId_"+chID+"\", \"1200\", \"450\", \"0\", \"1\");myChart_"+chID+".setDataXML(\""+lGraph.toString()+"\");myChart_"+chID+".render(\"chartdiv"+chID+"\");</script>");
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
	public static String generateWilyDataOTCSeries(String ProjectName,String TestID,String StartTime,String EndTime,String Counter_Name,String WilycounterName)
	{
		String lQuery="";
		StringBuilder lData= new StringBuilder();
		if(Counter_Name.equals("Throughput"))
		{
			lQuery="(SELECT DATE_FORMAT(TimeStamp, '%d-%m-%Y %h:%i:%s') AS lDate, metric_value, null as Throughput FROM wily_stats_"+ProjectName+"_"+TestID+" where counter_number='"+WilycounterName+"' and TimeStamp>='"+StartTime+"' and TimeStamp<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, null, Throughput FROM throughput_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"' and row_num MOD "+PropertyConf.getProperty("Granularity")+"=0 order by Scenario_Elapsed_Time) order by lDate;";
		}
		else if(Counter_Name.equals("Running_Vusers"))
		{
			lQuery="(SELECT DATE_FORMAT(TimeStamp, '%d-%m-%Y %h:%i:%s') AS lDate, metric_value, null as vusers FROM wily_stats_"+ProjectName+"_"+TestID+" where counter_number='"+WilycounterName+"' and TimeStamp>='"+StartTime+"' and TimeStamp<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, null, vusers FROM runningvusers_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
		}
		else if(Counter_Name.equals("Errors"))
		{
			lQuery="(SELECT DATE_FORMAT(TimeStamp, '%d-%m-%Y %h:%i:%s') AS lDate, metric_value, null as Errors FROM wily_stats_"+ProjectName+"_"+TestID+" where counter_number='"+WilycounterName+"' and TimeStamp>='"+StartTime+"' and TimeStamp<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, null, error_type FROM errors_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
		}
		String chID=ResultExtractEngine.RandomsesValID();
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		try{
			StringBuilder lGraph= new StringBuilder();			
			StringBuilder Categories= new StringBuilder();
			StringBuilder DataseriesL= new StringBuilder();
			StringBuilder DataseriesR= new StringBuilder();
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);			
			lGraph.append("<chart caption='"+Counter_Name.toUpperCase()+" Vs Wily Stats' connectNullData='1' xAxisName='Time' showValues='0' divLineAlpha='100' numVDivLines='4' vDivLineAlpha='0' showAlternateVGridColor='1' alternateVGridAlpha='5' canvasPadding='0'>");
			Categories.append("<categories>");
			DataseriesL.append("<axis title='"+Counter_Name.toUpperCase()+"' titlePos='left' tickWidth='10' divlineisdashed='1'><dataset seriesName='"+Counter_Name.toUpperCase()+"'>");
			DataseriesR.append("<axis title='Wily Stats' titlePos='right' axisOnLeft='0' numDivLines='14' tickWidth='10' divlineisdashed='1' ><dataset seriesName='Wily Stats'>");
		
			while(lResultSet.next())
			{
				Categories.append("<category label='"+lResultSet.getString(1)+"' />");
				String ColR=lResultSet.getString(2);
				String ColL=lResultSet.getString(3);
				if(Counter_Name.equals("Errors"))
				{
					if(ColL==null|ColL=="")
					{ColL="0";}
					else if(ColL!="")
					{ColL="1";}
				}
				else
				{
					if(ColL==null|ColL=="")
					{ColL="";}
				}
				if(ColR==null|ColR=="")
				{ColR="";}
				DataseriesR.append("<set value='"+ColR+"' />");
				DataseriesL.append("<set value='"+ColL+"' />");
			}
			Categories.append("</categories>");
			DataseriesL.append("</dataset></axis>");
			DataseriesR.append("</dataset></axis>");
			lGraph.append(Categories.toString());
			lGraph.append(DataseriesL.toString());
			lGraph.append(DataseriesR.toString());
			lGraph.append("</chart>");
			lData.append("<div id=\"chartdiv"+chID+"\" align=\"center\">Chart will load here</div>");			
			lData.append("<script type=\"text/javascript\">var myChart_"+chID+" = new FusionCharts(\"../FusionCharts/MultiAxisLine.swf\", \"myChartId_"+chID+"\", \"1200\", \"450\", \"0\", \"1\");myChart_"+chID+".setDataXML(\""+lGraph.toString()+"\");myChart_"+chID+".render(\"chartdiv"+chID+"\");</script>");
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
	public static String generateLROTCDataSeries(String ProjectName,String TestID,String StartTime,String EndTime,String Counter_Name,String LRCNTRDATA)
	{
		String lQuery="";
		StringBuilder lData= new StringBuilder();
		String chID=ResultExtractEngine.RandomsesValID();
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		lData.append("<div id=\"chartdiv"+chID+"\" align=\"center\">Chart will load here</div>");
		if(Counter_Name.equals("Throughput"))
		{
			if(LRCNTRDATA.equals("Errors"))
			{
				lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, error_type as Errors, null as Throughput FROM errors_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate , null , Throughput FROM throughput_stats_"+ProjectName+"_"+TestID+"  where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"' and row_num MOD "+PropertyConf.getProperty("Granularity")+"=0 order by Scenario_Elapsed_Time) order by lDate;";
			}
			else if(LRCNTRDATA.equals("Running_Vusers"))
			{
				lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, vusers as vusers, null as Throughput FROM runningvusers_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate , null , Throughput FROM throughput_stats_"+ProjectName+"_"+TestID+"  where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"' and row_num MOD "+PropertyConf.getProperty("Granularity")+"=0 order by Scenario_Elapsed_Time) order by lDate;";
			}
		}
		else if(Counter_Name.equals("Running_Vusers"))
		{
			if(LRCNTRDATA.equals("Errors"))
			{
				lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, error_type as Errors, null as vusers FROM errors_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate , null , vusers FROM runningvusers_stats_"+ProjectName+"_"+TestID+"  where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
			}
			else if(LRCNTRDATA.equals("Throughput"))
			{
				lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, null as Throughput, vusers as vusers FROM runningvusers_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate , Throughput ,null FROM throughput_stats_"+ProjectName+"_"+TestID+"  where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"' and row_num MOD "+PropertyConf.getProperty("Granularity")+"=0 order by Scenario_Elapsed_Time) order by lDate;";
			}
		}
		else if(Counter_Name.equals("Errors"))
		{
			if(LRCNTRDATA.equals("Throughput"))
			{
				lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, null as Throughput, error_type as Errors FROM errors_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate , Throughput ,null FROM throughput_stats_"+ProjectName+"_"+TestID+"  where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"' and row_num MOD "+PropertyConf.getProperty("Granularity")+"=0 order by Scenario_Elapsed_Time) order by lDate;";
			}
			else if(LRCNTRDATA.equals("Running_Vusers"))
			{
				lQuery="(SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate, null as vusers, error_type as Errors FROM errors_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') UNION (SELECT DATE_FORMAT(Scenario_Elapsed_Time, '%d-%m-%Y %h:%i:%s') AS lDate , vusers ,null FROM runningvusers_stats_"+ProjectName+"_"+TestID+"  where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"') order by lDate;";
			}
		}		
		System.out.println(lQuery);
		try{
			StringBuilder lGraph= new StringBuilder();			
			StringBuilder Categories= new StringBuilder();
			StringBuilder DataseriesL= new StringBuilder();
			StringBuilder DataseriesR= new StringBuilder();
			lGraph.append("<chart caption='"+Counter_Name.toUpperCase()+" Vs "+LRCNTRDATA.toUpperCase()+"' connectNullData='1' xAxisName='Time' showValues='0' divLineAlpha='100' numVDivLines='4' vDivLineAlpha='0' showAlternateVGridColor='1' alternateVGridAlpha='5' canvasPadding='0'>");
			Categories.append("<categories>");
			DataseriesL.append("<axis title='"+Counter_Name.toUpperCase()+"' titlePos='left' tickWidth='10' divlineisdashed='1'><dataset seriesName='"+Counter_Name.toUpperCase()+"'>");
			DataseriesR.append("<axis title='"+LRCNTRDATA.toUpperCase()+"' titlePos='right' axisOnLeft='0' numDivLines='14' tickWidth='10' divlineisdashed='1' ><dataset seriesName='"+LRCNTRDATA.toUpperCase()+"'>");
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				Categories.append("<category label='"+lResultSet.getString(1)+"' />");
				String ColR=lResultSet.getString(2);
				String ColL=lResultSet.getString(3);
				if(Counter_Name.equals("Errors"))
				{
					if(ColL==null|ColL=="")
					{ColL="0";}
					else if(ColL!="")
					{ColL="1";}
				}
				else
				{
					if(ColL==null|ColL=="")
					{ColL="";}
				}
				if(LRCNTRDATA.equals("Errors"))
				{
					if(ColR==null|ColR=="")
					{ColR="0";}
					else if(ColR!="")
					{ColR="1";}
				}
				else
				{
					if(ColR==null|ColR=="")
					{ColR="";}
				}
				DataseriesR.append("<set value='"+ColR+"' />");
				DataseriesL.append("<set value='"+ColL+"' />");
			}
			Categories.append("</categories>");
			DataseriesL.append("</dataset></axis>");
			DataseriesR.append("</dataset></axis>");
			lGraph.append(Categories.toString());
			lGraph.append(DataseriesL.toString());
			lGraph.append(DataseriesR.toString());
			lGraph.append("</chart>");
			lData.append("<script type=\"text/javascript\">var myChart_"+chID+" = new FusionCharts(\"../FusionCharts/MultiAxisLine.swf\", \"myChartId_"+chID+"\", \"1200\", \"450\", \"0\", \"1\");myChart_"+chID+".setDataXML(\""+lGraph.toString()+"\");myChart_"+chID+".render(\"chartdiv"+chID+"\");</script>");
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
	public static String AnalysisLoadFromOTC(String ProjectName ,String TestID ,String Counter_Name,String StartTime,String EndTime,String LRCNTRDATA,String WilyTokens,String GangliaTokens,String PerfmonTokens)
	{
		StringBuilder lData= new StringBuilder();
		String resTokens=checkResultsExistence(TestID,ProjectName);
		if(LRCNTRDATA.equals("Select"))
		{/*do nothing*/}
		else if(LRCNTRDATA.equals("Throughput"))
		{
			if(resTokens.contains("throughput"))
			{
				lData.append("<br><b><font color=blue>CORRELATION: "+Counter_Name.toUpperCase()+" WITH LR THROUGHPUT DATA</font></b><BR>");
				lData.append("<br>");
				lData.append(generateLROTCDataSeries(ProjectName,TestID,StartTime,EndTime,Counter_Name,LRCNTRDATA));
				lData.append("<br>");
			}
			else
			{		
				lData.append("<b>Throughput data doesn't exist for this test.<br>Please upload the Load Runner Throughput raw data for accessing this functionality.</b><br>");
			}
		}
		else if(LRCNTRDATA.equals("Running_Vusers"))
		{
			if(resTokens.contains("runningvusers"))
			{
				lData.append("<br><b><font color=blue>CORRELATION: "+Counter_Name.toUpperCase()+" WITH LR RUNNING VUSERS DATA</font></b><BR>");
				lData.append("<br>");
				lData.append(generateLROTCDataSeries(ProjectName,TestID,StartTime,EndTime,Counter_Name,LRCNTRDATA));
				lData.append("<br>");
			}
			else
			{
				lData.append("<b>Running Vusers data doesn't exist for this test.<br>Please upload the Load Runner Running Vusers raw data for accessing this functionality.</b><br>");
			}
		}
		else if(LRCNTRDATA.equals("Errors"))
		{
			if(resTokens.contains("errors"))
			{
				lData.append("<br><b><font color=blue>CORRELATION: "+Counter_Name.toUpperCase()+" WITH LR ERRORS DATA</font></b><BR>");
				lData.append("<br>");
				lData.append(generateLROTCDataSeries(ProjectName,TestID,StartTime,EndTime,Counter_Name,LRCNTRDATA));
				lData.append("<br>");
			}
			else
			{
				lData.append("<b>Errors data doesn't exist for this test.<br>Please upload the Load Runner Errors raw data for accessing this functionality.</b><br>");
			}
		}
		if(resTokens.contains("wily"))
		{
			StringTokenizer WilyTokenizer=new StringTokenizer(WilyTokens,",");
			lData.append("<br><b><font color=blue>CORRELATION: "+Counter_Name.toUpperCase()+" WITH WILY COUNTERS</font></b><BR>");
			while(WilyTokenizer.hasMoreTokens())
			{
				String WilycounterName=WilyTokenizer.nextToken();
				lData.append("<br>");
				lData.append(generateWilyCounterNames(ProjectName,TestID,WilycounterName));
				lData.append(generateWilyDataOTCSeries(ProjectName,TestID,StartTime,EndTime,Counter_Name,WilycounterName));
				lData.append("<br>");
			}
		}
		if(resTokens.contains("ganglia"))
		{
			StringTokenizer GangliaTokenizer=new StringTokenizer(GangliaTokens,",");
			lData.append("<br><b><font color=blue>CORRELATION: "+Counter_Name.toUpperCase()+" WITH GANGLIA COUNTERS</font></b><BR>");
			while(GangliaTokenizer.hasMoreTokens())
			{
				String colName=GangliaTokenizer.nextToken();
				lData.append("<br>");
				lData.append("<b>"+colName+" : </b>");
				lData.append(generateGPDataOTCSeries(ProjectName,TestID,StartTime,EndTime,Counter_Name,colName,"ganglia"));
				lData.append("<br>");
			}
		}
		if(resTokens.contains("perfmon"))
		{
			StringTokenizer PerfmonTokenizer=new StringTokenizer(PerfmonTokens,",");
			lData.append("<br><b><font color=blue>CORRELATION: "+Counter_Name.toUpperCase()+" WITH PERFMON COUNTERS</font></b><BR>");
			while(PerfmonTokenizer.hasMoreTokens())
			{
				String colName=PerfmonTokenizer.nextToken();
				lData.append("<br>");
				lData.append("<b>"+colName+" : </b>");
				lData.append(generateGPDataOTCSeries(ProjectName,TestID,StartTime,EndTime,Counter_Name,colName,"perfmon"));
				lData.append("<br>");
			}
		}
    	return lData.toString();
	}	

//////GET STEADY STATE
	public static String getStartEndSteadyState(String TestID,String ProjectName)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lTable="";
		String StartTime="";
		String EndTime="";
		String lQuery = "SELECT counter_name,DATE_FORMAT(counter_value, '%Y-%m-%d %H:%i:%s') AS lDate FROM lr_stats_details_"+ProjectName+" where counter_name like '%Steady State%' and test_id=\""+TestID+"\";";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				lTable=lResultSet.getString("counter_name");
				if(lTable.contains("Start Time"))
				{
					StartTime=lResultSet.getString("lDate");
				}
				else if(lTable.contains("End Time"))
				{
					EndTime=lResultSet.getString("lDate");
				}
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
//		StartTime= DateformatConverter("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss",StartTime);
//		EndTime= DateformatConverter("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss",EndTime);
		return StartTime+","+EndTime;
	}

//////HOW TO LR RUNNING VUSERS
	public static String HowToLRRunningVusers()
	{
		StringBuilder lData = new StringBuilder();
		lData.append("<h3><font color=BLUE>HOW TO EXTRACT LR RUNNING VUSERS DATA</font></h3><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">#</font></b></td><td colspan=0><b><font size=\"2\">STEP</font></b></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">1</font></td><td colspan=0 align=\"left\"><font size=\"3\">Bring <b><i>Running Vusers</i></b> Graph to Front in <i>LR Analysis Session</i>.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">2</font></td><td colspan=0 align=\"left\"><font size=\"3\">Set <b><i>Granularity</i></b> of the Graph to : <b><i>1</i></b>.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">3</font></td><td colspan=0 align=\"left\"><font size=\"3\"><i>Right Click</i> on the graph <i>(Running Vusers)</i> and select <b><i>Display Options</i></b>.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">4</font></td><td colspan=0 align=\"left\"><font size=\"3\">Select <i><b>\"Absolute Time\"</i></b> (radio button) in <i><b>\"Time Options\"</i></b> section.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">5</font></td><td colspan=0 align=\"left\"><font size=\"3\">Click on <i><b>\"Advanced Button\"</i></b>.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">6</font></td><td colspan=0 align=\"left\"><font size=\"3\">Select <i><b>\"Export\"</i></b> tab.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">7</font></td><td colspan=0 align=\"left\"><font size=\"3\">Select <i><b>\"Data\"</i></b> tab.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">8</font></td><td colspan=0 align=\"left\"><font size=\"3\">Select <i><b>\"Run\"</i></b> from <i><b>\"Series\"</i></b> drop down list.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">9</font></td><td colspan=0 align=\"left\"><font size=\"3\">Select <i><b>\"Text\"</i></b> (radio button) in <i><b>\"Format\"</i></b> section.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">10</font></td><td colspan=0 align=\"left\"><font size=\"3\">Select <i><b>\"Points Index\"</i></b>, <i><b>\"Points Labels\"</i></b>, <i><b>\"Headers\"</i></b> (checkboxes) in <i><b>\"Include\"</i></b> section.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">12</font></td><td colspan=0 align=\"left\"><font size=\"3\">Select <b><i>\"Comma\"</i></b> from <b><i>\"Delimiter\"</i></b> drop down list.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">13</font></td><td colspan=0 align=\"left\"><font size=\"3\">Leave the rest two fields blank.</font></td></tr>");
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"3\">14</font></td><td colspan=0 align=\"left\"><font size=\"3\">Click on save and Save As <font color=blue><i><b>\"Running Vusers.csv\".</b></i></font></font></td></tr>");
		lData.append("</table>");
		return lData.toString();
	}	
}
