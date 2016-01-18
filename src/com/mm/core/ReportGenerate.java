package com.mm.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

public class ReportGenerate
{
	static int fileCounts=0;
	
	
	public static String checkReportExistence(String pProjectName,String pTestID)
	{
		String filePath = PropertyConf.getProperty("ReportDir");
		String check=SearchFilePresent(filePath,pProjectName+"_"+pTestID);
		StringBuilder lData= new StringBuilder();
		lData.append("<h3><font color=BLUE>Test Report</font></h3><br>");
		lData.append("TEST ID: <b>"+pTestID+"</b> PROJECT NAME: <b>"+pProjectName+"</b><br>");

		if(check.equals("yes"))
		{
			lData.append("The PDF report of this test is already existing.<br><br>Please click on below link to download the existing report.<br>");
			//Link to existing report for download
			lData.append("<a href=\"/AdministratorModule/jsp/repGen1.jsp?ProjectName="+pProjectName+"&TestID="+pTestID+"&Selection=exrep\">Download Existing PDF Report</a>");
			lData.append("<br><br>If you want to regenerate the report. Please click below link. <i>(<b>Please note</b>: It may take a few minutes for generating the report.)</i><br>");
			//Link to regenerate the report for download
			lData.append("<a href=\"/AdministratorModule/jsp/repGen1.jsp?ProjectName="+pProjectName+"&TestID="+pTestID+"&Selection=regenrep\">Re-Generate PDF Report</a>");
		}
		else
		{
			lData.append("<br><br>Please click below link to generate the PDF report. <i>(<b>Please note</b>: It may take a few minutes for generating the report.)</i><br>");
			//Link to generate report for download
			lData.append("<a href=\"/AdministratorModule/jsp/repGen1.jsp?ProjectName="+pProjectName+"&TestID="+pTestID+"&Selection=gennewrep\">Generate PDF Report</a>");
		}
		return lData.toString();
	}
	public static String SearchFilePresent(String fold,String pattern)
	{
		File f1 = new File(fold);
		int count = 0; 
		if (!f1.isDirectory()) 
		{}
		else
		{
			File[] files = f1.listFiles();
			if (files != null) 
			{     
				for (File f: files)  
				{
					if(f.getName().contains(pattern))
					{
						count =count+1;   
					}	
				}  
			}
		}
		if(count==0)
		{
			return "no";
		}
		else
		{
			return "yes";
		}		
	}
	public static String SearchImagesPresent(String pProjectName,String pTestID,String pattern)
	{
		String filePath = PropertyConf.getProperty("ImageDir")+"\\"+pProjectName+"_"+pTestID;
		File dir = new File (filePath);
		if(dir.exists())
		{
			File f1 = new File(filePath);
			int count = 0; 
			if (!f1.isDirectory()) 
			{}
			else
			{
				File[] files = f1.listFiles();
				if (files != null) 
				{     
					for (File f: files)  
					{
						if(f.getName().contains(pattern))
						{
							count =count+1;   
						}	
					}  
				}
			}
			if(count==0)
			{
				return "no";
			}
			else
			{
				return "yes";
			}
		}
		else
		{
			return "dirnotpresent";
		}
	}
	
	public static void Downloadregenrep(String pProjectName,String pTestID)
	{
		String filePath = PropertyConf.getProperty("ReportDir");
		String output=SearchImagesPresent( pProjectName, pTestID, "png");
		deleteFiles(filePath,pProjectName+"_"+pTestID);
		if(output.equals("dirnotpresent"))
		{
			ReportGenerator(pProjectName,pTestID);
		}
		else if(output.equals("no"))
		{
			ReportGenerator(pProjectName,pTestID);
		}
		else if(output.equals("yes"))
		{
			createHTMLReport(pProjectName,pTestID);
		}
		
	}
		
	public static void ReportGenerator(String pProjectName,String pTestID)
	{
		createImages(pProjectName,pTestID);
		createHTMLReport(pProjectName,pTestID);
	}	
	
	public static void FileFolderStruct(String ProjectName,String TestID)
	{
		String filePath = PropertyConf.getProperty("ImageDir")+"\\"+ProjectName+"_"+TestID;
		File dir = new File (filePath);
		if(dir.exists())
		{
			DeleteFileFolder(filePath);
		}
		new File(filePath).mkdir();
		if(dir.exists())
		{
			System.out.println("Created Directory:"+filePath);
		}

	}
	public static void DeleteFileFolder(String path) {
		File file = new File(path);
		if(file.exists())
		{
			do{
				delete(file);
			}while(file.exists());
		}else
		{
			System.out.println("File or Folder not found : "+path);
		}
	}	
	public static void delete(File file)
	{
		if(file.isDirectory())
		{
			String fileList[] = file.list();
			if(fileList.length == 0)
			{
				System.out.println("Deleting Directory : "+file.getPath());
				file.delete();
			}else
			{
				int size = fileList.length;
				for(int i = 0 ; i < size ; i++)
				{
					String fileName = fileList[i];
					System.out.println("File path : "+file.getPath()+" and name :"+fileName);
					String fullPath = file.getPath()+"/"+fileName;
					File fileOrFolder = new File(fullPath);
					System.out.println("Full Path :"+fileOrFolder.getPath());
					delete(fileOrFolder);
				}
			}
		}else
		{
			System.out.println("Deleting file : "+file.getPath());
			file.delete();
		}
	}
	public static void deleteFiles(String fold,String pattern)
	{
		File f1 = new File(fold);
		if (!f1.isDirectory()) 
		{}
		else
		{
			File[] files = f1.listFiles();
			if (files != null) 
			{     
				for (File f: files)  
				{
					if(f.getName().contains(pattern))
					{
						f.delete();   
					}	
				}  
			}
		}		
	}

	public static void createImages(String pProjectName,String pTestID)
	{
		String delay=PropertyConf.getProperty("PJSDelay");
		FileFolderStruct(pProjectName,pTestID);
		String Tokens = ResultExtractEngine.checkResultsExistence(pTestID,pProjectName);
		getLRGraphsScreenCaps(pProjectName,pTestID,delay);
		if(Tokens.contains("wily"))
		{
			getWilyGraphsScreenCaps(pProjectName,pTestID,delay);
		}
		if(Tokens.contains("ganglia"))
		{
			getGangliaGraphsScreenCaps(pProjectName,pTestID,delay);
		}
		if(Tokens.contains("perfmon"))
		{
			getPerfmonGraphsScreenCaps(pProjectName,pTestID,delay);
		}		
	}	
	
	
	public static void getWilyGraphsScreenCaps(String pProjectName,String pTestID, String delay){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String lQuery="SELECT agent_name,resource_name,metric_name FROM wily_stats_"+pProjectName+"_"+pTestID+" group by metric_name,resource_name,agent_name order by agent_name,resource_name,metric_name;";
		String Agent_Name="",Resource_Name="",Metric_Name="";
		String unqHostName="",ClusterName="",chartDataXML="";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			String imgDir=PropertyConf.getProperty("ImageDir");
//			String phantomJ=imgDir+"\\phantomjs.exe";
//			String renderJS=imgDir+"\\render.js";
			int counter=1;
			while (lResultSet.next())
			{
				StringBuilder lData = new StringBuilder();
				String outfile=imgDir+"\\"+"temp_Wily"+counter+".html";
				String renderfile=imgDir+"\\"+"render_Wily"+counter+".js";
				FileWriter bw=new FileWriter(outfile);
				Agent_Name = lResultSet.getString("agent_name");
				if(Agent_Name.equals(unqHostName))
				{}
				else
				{
					unqHostName=Agent_Name;
					StringTokenizer st= new StringTokenizer(Agent_Name, "/");
					st.nextToken();
					ClusterName=st.nextToken();					
				}
				Resource_Name = lResultSet.getString("resource_name");
				Metric_Name = lResultSet.getString("metric_name");
				lData.append("<html><head><script type=\"text/javascript\" src=\"FusionCharts.js\"></script><script type=\"text/javascript\" src=\"jquery.min.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.Charts.js\"></script><style type=\"text/css\" media=\"screen\">object.FusionCharts:focus, embed.FusionCharts:focus {outline: none}</style><style type=\"text/css\">body {margin: 0;padding: 0;}</style></head>");
				lData.append("<body><div id=\"chartdiv\"></div>");
				try
				{
					StringBuilder lGraph = new StringBuilder();
					Statement lStatement1 = null;
					ResultSet lResultSet1 = null;
					String lQuery1="SELECT Metric_Value FROM wily_stats_"+pProjectName+"_"+pTestID+" where Metric_Name='"+Metric_Name+"' and Resource_Name='"+Resource_Name+"' and Agent_Name like '%"+ClusterName+"%';";
					lStatement1 = lConnection.createStatement();
					lResultSet1 = lStatement1.executeQuery(lQuery1);
					
					lGraph.append("<chart caption='"+Resource_Name+" metric data for "+ClusterName+"' xAxisName='Elapsed Time' yAxisName='"+Metric_Name+"' showValues='0' formatNumberScale='0' showBorder='1'>");
					int cnt=1;
					while (lResultSet1.next())
					{
						String temp=lResultSet1.getString(1);
						lGraph.append("<set name='"+cnt+"' value='"+temp+"'/>");
						cnt=cnt+1;
					}
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
				lData.append("<script type=\"text/javascript\">var myChart = new FusionCharts(\"Line.swf\", \"myChartId\", \"600\", \"250\", \"0\", \"0\");myChart.setDataXML(\""+chartDataXML+"\");myChart.render(\"chartdiv\");</script>");
				lData.append("</body></html>");
				bw.append(lData.toString());
				bw.close();
				FileWriter bw1=new FileWriter(renderfile);
				String pngAdd=pProjectName+"_"+pTestID+"\\\\WILY_IMG_"+counter+".png";
				bw1.append("var page = require('webpage').create(),webpageURLWithChart  = 'temp_Wily"+counter+".html',outputImageFileName = '"+pngAdd+"',delay = "+delay+";page.open(webpageURLWithChart, function () {window.setTimeout(function () {page.render(outputImageFileName);phantom.exit();}, delay);});");
				bw1.close();
				counter=counter+1;
			}
			FileWriter bw2=new FileWriter(imgDir+"\\"+"runPhantom_Wily.bat");
			bw2.append("cd "+imgDir);
			bw2.append(System.getProperty("line.separator"));
			for(int i=1;i<counter;i++)
			{
				bw2.append("phantom render_Wily"+i+".js");
				bw2.append(System.getProperty("line.separator"));
			}
			bw2.append("exit");
			bw2.close();
			
		//  Phantom js will come here....
		//	System.out.println("cmd /c start "+imgDir+"\\"+"runPhantom_Wily.bat");
            String[] command = {"cmd.exe", "/C", "Start", imgDir+"\\"+"runPhantom_Wily.bat"};            
		    Process process = Runtime.getRuntime().exec(command);		    
		    int exitStatus = process.waitFor();
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader (process.getInputStream()));
		    String currentLine=null;
		    StringBuilder stringBuilder = new StringBuilder(exitStatus==0?"SUCCESS:":"ERROR:");
		    currentLine= bufferedReader.readLine();
		    while(currentLine !=null)
		    {
		        stringBuilder.append(currentLine);
		        currentLine = bufferedReader.readLine();
		    }
		    System.out.println(stringBuilder.toString());
		    deleteFiles(imgDir,"_Wily");
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
	}
	public static void getGangliaGraphsScreenCaps(String pProjectName,String pTestID, String delay){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");		
		String lQuery = "";
		Connection lConnection = null;
		Statement lStatement = null;
		int colCount=gettotColCount(pProjectName,pTestID,"ganglia");
		String[] colNames = gettotColumns(pProjectName,pTestID,"ganglia");
		String[] MetricCounterNames = new String[100];
		String chartDataXML="";
		String imgDir=PropertyConf.getProperty("ImageDir");
		int counter=1;
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
			for(int k=1;k<(colCount-1);k++)
			{
				StringBuilder lData = new StringBuilder();
				String outfile=imgDir+"\\"+"temp_Ganglia"+counter+".html";
				String renderfile=imgDir+"\\"+"render_Ganglia"+counter+".js";
				FileWriter bw=new FileWriter(outfile);

				lData.append("<html><head><script type=\"text/javascript\" src=\"FusionCharts.js\"></script><script type=\"text/javascript\" src=\"jquery.min.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.Charts.js\"></script><style type=\"text/css\" media=\"screen\">object.FusionCharts:focus, embed.FusionCharts:focus {outline: none}</style><style type=\"text/css\">body {margin: 0;padding: 0;}</style></head>");
				lData.append("<body><div id=\"chartdiv\"></div>");
				StringBuilder lGraph = new StringBuilder();
				StringTokenizer stObj= new StringTokenizer(colNames[k],"_");
				String temp=stObj.nextToken();	
				ResultSet lResultSet = null;
				lQuery = "SELECT "+colNames[k]+" FROM ganglia_stats_"+pProjectName+"_"+pTestID+";";
				lResultSet = lStatement.executeQuery(lQuery);
				lGraph.append("<chart caption='"+MetricCounterNames[k]+" metric data for "+temp+"' xAxisName='Elapsed Time' yAxisName='"+MetricCounterNames[k]+"' showValues='0' formatNumberScale='0' showBorder='1'>");
				int cnt=1;
				while(lResultSet.next())
				{							
					Double temp1 =Double.parseDouble(lResultSet.getString(1));
					lGraph.append("<set name='"+cnt+"' value='"+temp1+"'/>");
					cnt=cnt+1;							
				}
				lGraph.append("</chart>");
				chartDataXML=lGraph.toString();
				try{
					if(lResultSet != null){
						lResultSet.close();					
					}
				}
				catch(Exception ae){}				
				lData.append("<script type=\"text/javascript\">var myChart = new FusionCharts(\"Line.swf\", \"myChartId\", \"600\", \"250\", \"0\", \"0\");myChart.setDataXML(\""+chartDataXML+"\");myChart.render(\"chartdiv\");</script>");
				lData.append("</body></html>");
				bw.append(lData.toString());
				bw.close();
				FileWriter bw1=new FileWriter(renderfile);
				String pngAdd=pProjectName+"_"+pTestID+"\\\\GANGLIA_"+colNames[k]+".png";
				bw1.append("var page = require('webpage').create(),webpageURLWithChart  = 'temp_Ganglia"+counter+".html',outputImageFileName = '"+pngAdd+"',delay = "+delay+";page.open(webpageURLWithChart, function () {window.setTimeout(function () {page.render(outputImageFileName);phantom.exit();}, delay);});");
				bw1.close();
				counter=counter+1;
			}
			FileWriter bw2=new FileWriter(imgDir+"\\"+"runPhantom_Ganglia.bat");
			bw2.append("cd "+imgDir);
			bw2.append(System.getProperty("line.separator"));
			for(int i=1;i<counter;i++)
			{
				bw2.append("phantom render_Ganglia"+i+".js");
				bw2.append(System.getProperty("line.separator"));
			}
			bw2.append("exit");
			bw2.close();
			//  Phantom js will come here....
	            String[] command = {"cmd.exe", "/C", "Start", imgDir+"\\"+"runPhantom_Ganglia.bat"};            
			    Process process = Runtime.getRuntime().exec(command);		    
			    int exitStatus = process.waitFor();
			    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader (process.getInputStream()));
			    String currentLine=null;
			    StringBuilder stringBuilder = new StringBuilder(exitStatus==0?"SUCCESS:":"ERROR:");
			    currentLine= bufferedReader.readLine();
			    while(currentLine !=null)
			    {
			        stringBuilder.append(currentLine);
			        currentLine = bufferedReader.readLine();
			    }
			    System.out.println(stringBuilder.toString());
			    deleteFiles(imgDir,"_Ganglia");
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
	}
	public static void getPerfmonGraphsScreenCaps(String pProjectName,String pTestID, String delay){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		String lQuery = "";
		Connection lConnection = null;
		Statement lStatement = null;
		int colCount=gettotColCount(pProjectName,pTestID,"perfmon");
		String[] colNames = gettotColumns(pProjectName,pTestID,"perfmon");		
		String chartDataXML="";
		int counter=1;
		try{
			String imgDir=PropertyConf.getProperty("ImageDir");
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();			
				for(int k=1;k<(colCount-1);k++)
				{
					StringBuilder lData = new StringBuilder();
					String outfile=imgDir+"\\"+"temp_Perfmon"+counter+".html";
					String renderfile=imgDir+"\\"+"render_Perfmon"+counter+".js";
					FileWriter bw=new FileWriter(outfile);
					lData.append("<html><head><script type=\"text/javascript\" src=\"FusionCharts.js\"></script><script type=\"text/javascript\" src=\"jquery.min.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.Charts.js\"></script><style type=\"text/css\" media=\"screen\">object.FusionCharts:focus, embed.FusionCharts:focus {outline: none}</style><style type=\"text/css\">body {margin: 0;padding: 0;}</style></head>");
					lData.append("<body><div id=\"chartdiv\"></div>");
					StringBuilder lGraph = new StringBuilder();
					ResultSet lResultSet = null;
					lQuery = "SELECT "+colNames[k]+" FROM perfmon_stats_"+pProjectName+"_"+pTestID+";";
					lResultSet = lStatement.executeQuery(lQuery);
					lGraph.append("<chart caption='"+colNames[k]+" metric data' xAxisName='Elapsed Time' yAxisName='"+colNames[k]+"' showValues='0' formatNumberScale='0' showBorder='1'>");
					int cnt=1;
					while(lResultSet.next())
					{							
						Double temp1 =Double.parseDouble(lResultSet.getString(1));
						lGraph.append("<set name='"+cnt+"' value='"+temp1+"'/>");
						cnt=cnt+1;							
					}
					lGraph.append("</chart>");
					chartDataXML=lGraph.toString();
					try{
						if(lResultSet != null){
							lResultSet.close();					
						}
					}
					catch(Exception ae){}
					lData.append("<script type=\"text/javascript\">var myChart = new FusionCharts(\"Line.swf\", \"myChartId\", \"600\", \"250\", \"0\", \"0\");myChart.setDataXML(\""+chartDataXML+"\");myChart.render(\"chartdiv\");</script>");
					lData.append("</body></html>");
					bw.append(lData.toString());
					bw.close();
					FileWriter bw1=new FileWriter(renderfile);
					String pngAdd=pProjectName+"_"+pTestID+"\\\\PERFMON_"+colNames[k]+".png";
					bw1.append("var page = require('webpage').create(),webpageURLWithChart  = 'temp_Perfmon"+counter+".html',outputImageFileName = '"+pngAdd+"',delay = "+delay+";page.open(webpageURLWithChart, function () {window.setTimeout(function () {page.render(outputImageFileName);phantom.exit();}, delay);});");
					bw1.close();
					counter=counter+1;					
				}
				FileWriter bw2=new FileWriter(imgDir+"\\"+"runPhantom_Perfmon.bat");
				bw2.append("cd "+imgDir);
				bw2.append(System.getProperty("line.separator"));
				for(int i=1;i<counter;i++)
				{
					bw2.append("phantom render_Perfmon"+i+".js");
					bw2.append(System.getProperty("line.separator"));
				}
				bw2.append("exit");
				bw2.close();
				//  Phantom js will come here....
		            String[] command = {"cmd.exe", "/C", "Start", imgDir+"\\"+"runPhantom_Perfmon.bat"};            
				    Process process = Runtime.getRuntime().exec(command);		    
				    int exitStatus = process.waitFor();
				    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader (process.getInputStream()));
				    String currentLine=null;
				    StringBuilder stringBuilder = new StringBuilder(exitStatus==0?"SUCCESS:":"ERROR:");
				    currentLine= bufferedReader.readLine();
				    while(currentLine !=null)
				    {
				        stringBuilder.append(currentLine);
				        currentLine = bufferedReader.readLine();
				    }
				    System.out.println(stringBuilder.toString());
				    deleteFiles(imgDir,"_Perfmon");


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
	}
	public static void getLRGraphsScreenCaps(String pProjectName,String pTestID, String delay){
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		ResultSet lResultSet1 = null;
		String lQuery="";
		String Counter_Name="",Counter_Value="";
		String chartDataXML="";
		String Pass="",Fail="",Stop="";
		int counter=1;
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			String imgDir=PropertyConf.getProperty("ImageDir");
			lQuery="SELECT Counter_Name,Counter_Value FROM lr_stats_details_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
			lResultSet = lStatement.executeQuery(lQuery);
			while (lResultSet.next())
			{
				Counter_Name = lResultSet.getString("Counter_Name");
				Counter_Value= lResultSet.getString("Counter_Value");
				if(Counter_Name.contains("Total Throughput"))
				{
					String outfile=imgDir+"\\"+"temp_LR"+counter+".html";
					String renderfile=imgDir+"\\"+"render_LR"+counter+".js";
					FileWriter bw=new FileWriter(outfile);
					
					StringBuilder lData = new StringBuilder();		
					lData.append("<html><head><script type=\"text/javascript\" src=\"FusionCharts.js\"></script><script type=\"text/javascript\" src=\"jquery.min.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.Charts.js\"></script><style type=\"text/css\" media=\"screen\">object.FusionCharts:focus, embed.FusionCharts:focus {outline: none}</style><style type=\"text/css\">body {margin: 0;padding: 0;}</style></head>");
					lData.append("<body><div id=\"chartdiv\"></div>");
					
					StringBuilder lGraph = new StringBuilder();
					lGraph.append("<chart caption='Total Throughput' xAxisName='Elapsed Time' yAxisName='Total Throughput' showValues='1' formatNumberScale='0' showBorder='1'>");
					lGraph.append("<set name='"+Counter_Name+"' value='"+Double.parseDouble(Counter_Value)+"' hoverText='"+Counter_Value+"'/>");
					lGraph.append("</chart>");
					chartDataXML=lGraph.toString();
					
					lData.append("<script type=\"text/javascript\">var myChart = new FusionCharts(\"Column3D.swf\", \"myChartId\", \"600\", \"250\", \"0\", \"0\");myChart.setDataXML(\""+chartDataXML+"\");myChart.render(\"chartdiv\");</script>");
					lData.append("</body></html>");
					bw.append(lData.toString());
					bw.close();
					FileWriter bw1=new FileWriter(renderfile);
					String pngAdd=pProjectName+"_"+pTestID+"\\\\LR_"+Counter_Name+".png";
					bw1.append("var page = require('webpage').create(),webpageURLWithChart  = 'temp_LR"+counter+".html',outputImageFileName = '"+pngAdd+"',delay = "+delay+";page.open(webpageURLWithChart, function () {window.setTimeout(function () {page.render(outputImageFileName);phantom.exit();}, delay);});");
					bw1.close();
					counter=counter+1;					
				}
				else if(Counter_Name.contains("Total Hits"))
				{
					String outfile=imgDir+"\\"+"temp_LR"+counter+".html";
					String renderfile=imgDir+"\\"+"render_LR"+counter+".js";
					FileWriter bw=new FileWriter(outfile);
					
					StringBuilder lData = new StringBuilder();		
					lData.append("<html><head><script type=\"text/javascript\" src=\"FusionCharts.js\"></script><script type=\"text/javascript\" src=\"jquery.min.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.Charts.js\"></script><style type=\"text/css\" media=\"screen\">object.FusionCharts:focus, embed.FusionCharts:focus {outline: none}</style><style type=\"text/css\">body {margin: 0;padding: 0;}</style></head>");
					lData.append("<body><div id=\"chartdiv\"></div>");

					StringBuilder lGraph = new StringBuilder();
					lGraph.append("<chart caption='Total Hits' xAxisName='Elapsed Time' yAxisName='Total Hits' showValues='1' formatNumberScale='0' showBorder='1'>");
					lGraph.append("<set name='"+Counter_Name+"' value='"+Double.parseDouble(Counter_Value)+"' hoverText='"+Counter_Value+"'/>");
					lGraph.append("</chart>");
					chartDataXML=lGraph.toString();
					
					lData.append("<script type=\"text/javascript\">var myChart = new FusionCharts(\"Column3D.swf\", \"myChartId\", \"600\", \"250\", \"0\", \"0\");myChart.setDataXML(\""+chartDataXML+"\");myChart.render(\"chartdiv\");</script>");
					lData.append("</body></html>");
					bw.append(lData.toString());
					bw.close();
					FileWriter bw1=new FileWriter(renderfile);
					String pngAdd=pProjectName+"_"+pTestID+"\\\\LR_"+Counter_Name+".png";
					bw1.append("var page = require('webpage').create(),webpageURLWithChart  = 'temp_LR"+counter+".html',outputImageFileName = '"+pngAdd+"',delay = "+delay+";page.open(webpageURLWithChart, function () {window.setTimeout(function () {page.render(outputImageFileName);phantom.exit();}, delay);});");
					bw1.close();
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
			String outfile=imgDir+"\\"+"temp_LR"+counter+".html";
			String renderfile=imgDir+"\\"+"render_LR"+counter+".js";
			FileWriter bw=new FileWriter(outfile);
			
			StringBuilder lData1 = new StringBuilder();	
			lData1.append("<html><head><script type=\"text/javascript\" src=\"FusionCharts.js\"></script><script type=\"text/javascript\" src=\"jquery.min.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.Charts.js\"></script><style type=\"text/css\" media=\"screen\">object.FusionCharts:focus, embed.FusionCharts:focus {outline: none}</style><style type=\"text/css\">body {margin: 0;padding: 0;}</style></head>");
			lData1.append("<body><div id=\"chartdiv\"></div>");
			
			StringBuilder lGraph1 = new StringBuilder();
			lGraph1.append("<chart caption='Transactional Success Chart' xAxisName='Elapsed Time' yAxisName='Transactional Success Chart' showValues='1' formatNumberScale='0' showBorder='1'>");
			lGraph1.append("<set label='Transactions Passed' value='"+Pass+"'/>");
			lGraph1.append("<set label='Transactions Failed' value='"+Fail+"'/>");
			lGraph1.append("<set label='Transactions Stopped' value='"+Stop+"'/>");			
			lGraph1.append("</chart>");
			chartDataXML=lGraph1.toString();
			
			lData1.append("<script type=\"text/javascript\">var myChart = new FusionCharts(\"Pie3D.swf\", \"myChartId\", \"600\", \"250\", \"0\", \"0\");myChart.setDataXML(\""+chartDataXML+"\");myChart.render(\"chartdiv\");</script>");
			lData1.append("</body></html>");
			bw.append(lData1.toString());
			bw.close();
			FileWriter bw1=new FileWriter(renderfile);
			String pngAdd=pProjectName+"_"+pTestID+"\\\\LR_Transactional Success Chart.png";
			bw1.append("var page = require('webpage').create(),webpageURLWithChart  = 'temp_LR"+counter+".html',outputImageFileName = '"+pngAdd+"',delay = "+delay+";page.open(webpageURLWithChart, function () {window.setTimeout(function () {page.render(outputImageFileName);phantom.exit();}, delay);});");
			bw1.close();
			counter=counter+1;
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			StringBuilder lData2 = new StringBuilder();
			outfile=imgDir+"\\"+"temp_LR"+counter+".html";
			renderfile=imgDir+"\\"+"render_LR"+counter+".js";
			FileWriter bwc=new FileWriter(outfile);

			lData2.append("<html><head><script type=\"text/javascript\" src=\"FusionCharts.js\"></script><script type=\"text/javascript\" src=\"jquery.min.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.js\"></script><script type=\"text/javascript\" src=\"FusionCharts.HC.Charts.js\"></script><style type=\"text/css\" media=\"screen\">object.FusionCharts:focus, embed.FusionCharts:focus {outline: none}</style><style type=\"text/css\">body {margin: 0;padding: 0;}</style></head>");
			lData2.append("<body><div id=\"chartdiv\"></div>");
			
			lQuery="SELECT Transaction_Name,Avg_Value from lr_txns_details_"+pProjectName+" where Test_ID =\""+pTestID+"\";";
			StringBuilder lGraph2 = new StringBuilder();
			lGraph2.append("<chart caption='Average Transaction Response Time' xAxisName='Elapsed Time' yAxisName='Average Transaction Response Time' showValues='0' formatNumberScale='0' showBorder='1'>");
			lResultSet1 = lStatement.executeQuery(lQuery);
			while (lResultSet1.next())
			{
				String TxnName= lResultSet1.getString("Transaction_Name");
				String Avg_Value=lResultSet1.getString("Avg_Value");
				lGraph2.append("<set label='"+TxnName+"' value='"+Avg_Value+"'/>");
			}
			lGraph2.append("</chart>");
			chartDataXML=lGraph2.toString();
			lData2.append("<script type=\"text/javascript\">var myChart = new FusionCharts(\"Column3D.swf\", \"myChartId\", \"600\", \"250\", \"0\", \"0\");myChart.setDataXML(\""+chartDataXML+"\");myChart.render(\"chartdiv\");</script>");
			lData2.append("</body></html>");
			bwc.append(lData2.toString());
			bwc.close();
			FileWriter bw1c=new FileWriter(renderfile);
			pngAdd=pProjectName+"_"+pTestID+"\\\\LR_Average Transaction Response Time.png";
			bw1c.append("var page = require('webpage').create(),webpageURLWithChart  = 'temp_LR"+counter+".html',outputImageFileName = '"+pngAdd+"',delay = "+delay+";page.open(webpageURLWithChart, function () {window.setTimeout(function () {page.render(outputImageFileName);phantom.exit();}, delay);});");
			bw1c.close();			
			counter=counter+1;

			FileWriter bw2=new FileWriter(imgDir+"\\"+"runPhantom_LR.bat");
			bw2.append("cd "+imgDir);
			bw2.append(System.getProperty("line.separator"));
			for(int i=1;i<counter;i++)
			{
				bw2.append("phantom render_LR"+i+".js");
				bw2.append(System.getProperty("line.separator"));
			}
			bw2.append("exit");
			bw2.close();
			
			//  Phantom js will come here....
            String[] command = {"cmd.exe", "/C", "Start", imgDir+"\\"+"runPhantom_LR.bat"};            
		    Process process = Runtime.getRuntime().exec(command);		    
		    int exitStatus = process.waitFor();
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader (process.getInputStream()));
		    String currentLine=null;
		    StringBuilder stringBuilder = new StringBuilder(exitStatus==0?"SUCCESS:":"ERROR:");
		    currentLine= bufferedReader.readLine();
		    while(currentLine !=null)
		    {
		        stringBuilder.append(currentLine);
		        currentLine = bufferedReader.readLine();
		    }
		    System.out.println(stringBuilder.toString());
		    deleteFiles(imgDir,"_LR");			
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
				if(lStatement != null){
					lStatement.close();					
				}
				if(lConnection != null){
					lConnection.close();					
				}
			}
			catch(Exception ae){}
		}
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

	
	public static void createHTMLReport(String pProjectName,String pTestID)
	{
		StringBuilder lData = new StringBuilder();
		lData.append("<html><head><title>Results Dashboard</title></head><body><basefont face=\"calibri\"><h2><font color=BLUE>TEST REPORT</font></h2><br><h3> TEST ID: <u><i>"+pTestID+"</i></u> PROJECT NAME: <u><i>"+pProjectName+"</i></u></h3>");
		lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><h3><b><u>Test Details:</u></b></h3></td></tr></table><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><font color=BLUE>"+ResultExtractEngine.getTestDetails(pProjectName,pTestID)+"</font><br></td></tr></table><br>");
		lData.append("<b><u><h3>Test Status:</h3></u></b><font color=BLUE>"+ResultExtractEngine.getTestStatus(pProjectName,pTestID)+"</font><br><br>");
		lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><h3><b><u>Test Summary:</u></b></h3></td></tr></table><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><font color=BLUE>"+ResultExtractEngine.getTestSummary(pProjectName,pTestID)+"</font><br></td></tr></table><hr>");
		lData.append("<h3><font color=BLUE>OBSERVATIONS</font></h3><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><h4><b><u>Test Observations:</u></b></h4></td></tr></table><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><font color=BLUE>"+ResultExtractEngine.getTestObservations(pProjectName,pTestID)+"</font><br></td></tr></table><br>");
		lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><h4><b><u>Test Statistics:</u></b></h4></td></tr></table>");
		String l_StRows = "";
		lData.append(ResultExtractEngine.getLRStatistics(pProjectName,pTestID));
		String l_Tokens ="";		
		lData.append("<br><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><h4><b><u>Load Runner Statistics:</u></b></h4></td></tr></table><br>");
		lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td>Transaction Response Time:</td></tr></table>"+ResultExtractEngine.getLRtransactionRespTime(pProjectName,pTestID)+"<br>");
		String Tokens = ResultExtractEngine.checkResultsExistence(pTestID,pProjectName);
		if(Tokens.contains("wily"))
		{
			lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><h4><b><u>Wily Statistics:</u></b></h4></td></tr></table><br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Agent/Server Name</font></b></td><td colspan=0><b><font size=\"2\">Resource Name</font></b></td><td colspan=0><b><font size=\"2\">Metric Name</font></b></td><td colspan=0><b><font size=\"2\">Value Type</font></b></td><td colspan=0><b><font size=\"2\">Metric Value</font></b></td></tr>");
			l_StRows = ResultExtractEngine.getWilyStatistics(pProjectName,pTestID);
			l_Tokens ="";
			StringTokenizer stObj1;
			stObj1 = new StringTokenizer(l_StRows , ",");
			while(stObj1.hasMoreTokens())
			{
				l_Tokens=stObj1.nextToken();
				StringTokenizer stCols = new StringTokenizer(l_Tokens , "#");
				lData.append("<tr bgcolor= #CCFFFF >");
				while(stCols.hasMoreTokens())
				{
					lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+stCols.nextToken()+"</font></td>");
				}
				lData.append("</tr>");
			}
			lData.append("</table><br>");
		}
		if(Tokens.contains("ganglia"))
		{
			lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><h4><b><u>Ganglia Statistics:</u></b></h4></td></tr></table><br><br>"+ResultExtractEngine.getGangliaStatistics(pProjectName,pTestID));
		}
		if(Tokens.contains("perfmon"))
		{
			lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td><h4><b><u>Perfmon Statistics:</u></b></h4></td></tr></table><br>"+ResultExtractEngine.getPerfmonStatistics(pProjectName,pTestID));
		}
		lData.append("<br><hr>");
		
		//////////////////////////Now Graphs Starts............
		
		lData.append("<h3><font color=BLUE>GRAPHS</font></h3>");
		lData.append("<h4><b>Load Runner Graphs:</b></h4><br>");
		lData.append(createImageHTMLsrc(pProjectName,pTestID,"LR_"));
		if(Tokens.contains("wily"))
		{
			lData.append("<h4><b>Wily Introscope Graphs:</b></h4><br>");
			lData.append(createImageHTMLsrc(pProjectName,pTestID,"WILY_"));
		}
		if(Tokens.contains("ganglia"))
		{
			lData.append("<h4><b>Ganglia Graphs:</b></h4><br>");
			lData.append(createImageHTMLsrc(pProjectName,pTestID,"GANGLIA_"));
		}
		if(Tokens.contains("perfmon"))
		{
			lData.append("<h4><b>Perfmon Graphs:</b></h4><br>");
			lData.append(createImageHTMLsrc(pProjectName,pTestID,"PERFMON_"));
		}		
		lData.append("</body></html>");
		String imgDir=PropertyConf.getProperty("ImageDir");
		String repDir=PropertyConf.getProperty("ReportDir");
		String outfile=imgDir+"\\"+pProjectName+"_"+pTestID+"_"+"Report.html";
		try {
			FileWriter bw=new FileWriter(outfile);
			bw.append(lData.toString());
			bw.close();
			FileWriter bw2=new FileWriter(imgDir+"\\"+pProjectName+"_"+pTestID+"_"+"Report.bat");
			bw2.append("cd "+imgDir);
			bw2.append(System.getProperty("line.separator"));
			bw2.append("topdf "+pProjectName+"_"+pTestID+"_"+"Report.html "+repDir+"\\"+pProjectName+"_"+pTestID+"_"+"Report.pdf");
			bw2.append(System.getProperty("line.separator"));
			bw2.append("exit");
			bw2.close();
			
			//  topdf  will come here....
            String[] command = {"cmd.exe", "/C", "Start", imgDir+"\\"+pProjectName+"_"+pTestID+"_"+"Report.bat"};            
		    Process process = Runtime.getRuntime().exec(command);		    
		    int exitStatus = process.waitFor();
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader (process.getInputStream()));
		    String currentLine=null;
		    StringBuilder stringBuilder = new StringBuilder(exitStatus==0?"SUCCESS:":"ERROR:");
		    currentLine= bufferedReader.readLine();
		    while(currentLine !=null)
		    {
		        stringBuilder.append(currentLine);
		        currentLine = bufferedReader.readLine();
		    }
		    System.out.println(stringBuilder.toString());
		    deleteFiles(imgDir,pProjectName+"_"+pTestID);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String[] filesinafolder(String fold,String pattern)
	{
		fileCounts=0;
		String fNamearr[] = new String[70];
		File f1 = new File(fold);
		if (!f1.isDirectory()) 
		{}
		else
		{
			File[] files = f1.listFiles();			
			if (files != null) 
			{

				for (File f: files)  
				{
					if(f.getName().toLowerCase().contains(pattern.toLowerCase()))
					{
						fNamearr[fileCounts]=f.getName();
						fileCounts =fileCounts+1;   
					}	
				}  
			}
		}		
		return fNamearr;
	}
	public static String createImageHTMLsrc(String pProjectName,String pTestID,String pType)
	{
		/* to access image
		 * imgFilesPath+"\\"+fNamearr[i]
		 * */
		String imgDir=PropertyConf.getProperty("ImageDir");
		String imgFilesPath=imgDir+"\\"+pProjectName+"_"+pTestID;
		String[] fNamearr = filesinafolder(imgFilesPath,pType);
		int modVal=fileCounts%2;
		int divVal=fileCounts/2;
		int looper=0,reset=0,j=0;
		if(modVal==0)
			looper=divVal;
		else
			looper=divVal+1;
		StringBuilder lData = new StringBuilder();
		lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
		for(int i=0;i<looper;i++)
		{
			if(reset<2)
			{
				lData.append("<tr>");
			}
			lData.append("<td><img src=\""+imgFilesPath+"\\"+fNamearr[j]+"\"></img></td>");
			j=j+1;
			lData.append("<td><img src=\""+imgFilesPath+"\\"+fNamearr[j]+"\"></img></td>");
			j=j+1;
			if(reset<2)
			{
				lData.append("</tr>");
			}			
			reset=reset+1;
			if(reset==2)
			reset=0;
		}
		lData.append("</table>");
		return lData.toString();
	}

}
