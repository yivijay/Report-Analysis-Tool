package com.mm.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ResultsUploaderEngine {
	static int fileCounts=0;

	public static void FileFolderStruct(String filePath)
	{
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

	public static String manageResultsUpload(String TestID,String ProjectName,int LRStatsFlag,int WilyStatsFlag,int GangliaStatsFlag,int PerfmonStatsFlag,int VmStatStatsFlag)
	{
		String lResult="";
		String filePath = PropertyConf.getProperty("FileDir")+"\\"+ProjectName+"_"+TestID;		
		lResult=checkResultsExistence(TestID,ProjectName);
		System.out.println("lResult::::::::::: "+lResult);
		if(lResult.equals(""))
		{		
			String[] fNamearr = filesinafolder(filePath,"zip");
			doUnzipFiles(filePath+"\\"+fNamearr[0],filePath);
			deleteFiles(filePath,"zip");
			lResult=BuildResponseString(filePath,TestID,ProjectName,LRStatsFlag,WilyStatsFlag,GangliaStatsFlag,PerfmonStatsFlag,VmStatStatsFlag);
		}
		else
		{
			deleteFiles(filePath,"zip");
		}
		return lResult.toString();
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
	
	public static String BuildResponseString(String filePath,String TestID,String ProjectName,int LRStatsFlag,int WilyStatsFlag,int GangliaStatsFlag,int PerfmonStatsFlag,int VmStatStatsFlag)
	{
		StringBuilder lData = new StringBuilder();
		String temp="";
		int mismatch_LR=2,mismatch_WI=2,mismatch_GA=2,mismatch_PM=2,mismatch_VS=2;
		lData.append("The zip file details for results of<br> PROJECT NAME:  <b>"+ProjectName+"</b><br> TEST ID: <b>"+TestID+"</b><br><br><hr>");
		lData.append("<table align=\"center\" cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=80%><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"2\">Result Type</font></b></td><td colspan=0><b><font size=\"2\">Option Selected</font></b></td><td colspan=0><b><font size=\"2\">File Presence</font></b></td><td colspan=0><b><font size=\"2\">File Name</font></b></td><td colspan=0><b><font size=\"2\">Mismatch Found</font></b></td></tr>");
		lData.append("");

		//LR Stats
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"2\">Load Runner Results</font></td>");
		if(LRStatsFlag==1)
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");	
		else
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");
		temp=SearchFilePresent(filePath,"xls");	
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+temp.toUpperCase()+"</font></td>");
		if(temp.equals("yes"))
		{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">");
			String[] fNamearr = filesinafolder(filePath,"xls");
			for(int i=0;i<fileCounts;i++)
			{
				lData.append((i+1)+". "+fNamearr[i]+"<br>");
			}
			fNamearr = filesinafolder(filePath,"csv");
			for(int i=0;i<fileCounts;i++)
			{
				lData.append((i+1)+". "+fNamearr[i]+"<br>");
			}

			lData.append("</font></td>");
		}
		else{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NULL</font></td>");
		}			
		if((LRStatsFlag==1 & temp.equals("yes")) | (LRStatsFlag==0 & temp.equals("no")) )
		{	mismatch_LR=0;
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");}
		else{
			mismatch_LR=1;
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");
		}



		//Wily Stats
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"2\">Wily Results</font></td>");
		if(WilyStatsFlag==1)
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");	
		else
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");
		temp=SearchFilePresent(filePath,"wily");	
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+temp.toUpperCase()+"</font></td>");
		if(temp.equals("yes"))
		{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">");
			String[] fNamearr = filesinafolder(filePath,"wily");
			for(int i=0;i<fileCounts;i++)
			{
				lData.append((i+1)+". "+fNamearr[i]+"<br>");
			}
			lData.append("</font></td>");
		}
		else{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NULL</font></td>");
		}			
		if((WilyStatsFlag==1 & temp.equals("yes")) | (WilyStatsFlag==0 & temp.equals("no")) )
		{	mismatch_WI=0;
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");
		}
		else{
			mismatch_WI=1;
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");
		}



		//Ganglia Stats
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"2\">Ganglia Results</font></td>");
		if(GangliaStatsFlag==1)
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");	
		else
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");
		temp=SearchFilePresent(filePath,"ganglia");	
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+temp.toUpperCase()+"</font></td>");
		if(temp.equals("yes"))
		{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">");
			String[] fNamearr = filesinafolder(filePath,"ganglia");
			for(int i=0;i<fileCounts;i++)
			{
				lData.append((i+1)+". "+fNamearr[i]+"<br>");
			}
			lData.append("</font></td>");
		}
		else{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NULL</font></td>");
		}			
		if((GangliaStatsFlag==1 & temp.equals("yes")) | (GangliaStatsFlag==0 & temp.equals("no")) )
		{
			mismatch_GA=0;
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");}
		else{
			mismatch_GA=1;
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");
		}



		//Perfmon Stats
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"2\">Perfmon Results</font></td>");
		if(PerfmonStatsFlag==1)
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");	
		else
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");
		temp=SearchFilePresent(filePath,"perfmon");	
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+temp.toUpperCase()+"</font></td>");
		if(temp.equals("yes"))
		{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">");
			String[] fNamearr = filesinafolder(filePath,"perfmon");
			for(int i=0;i<fileCounts;i++)
			{
				lData.append((i+1)+". "+fNamearr[i]+"<br>");
			}
			lData.append("</font></td>");
		}
		else{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NULL</font></td>");
		}			
		if((PerfmonStatsFlag==1 & temp.equals("yes")) | (PerfmonStatsFlag==0 & temp.equals("no")) )
		{mismatch_PM=0;
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");}
		else
		{mismatch_PM=1;
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");}





		//VMStat Stats
		lData.append("<tr bgcolor= #CCFFFF ><td colspan=0 align=\"left\"><font size=\"2\">VMSTAT Results</font></td>");
		if(VmStatStatsFlag==1)
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");	
		else
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");
		temp=SearchFilePresent(filePath,"vmstat");	
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">"+temp.toUpperCase()+"</font></td>");
		if(temp.equals("yes"))
		{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">");
			String[] fNamearr = filesinafolder(filePath,"vmstat");
			for(int i=0;i<fileCounts;i++)
			{
				lData.append((i+1)+". "+fNamearr[i]+"<br>");
			}
			lData.append("</font></td>");
		}
		else{
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NULL</font></td>");
		}			
		if((VmStatStatsFlag==1 & temp.equals("yes")) | (VmStatStatsFlag==0 & temp.equals("no")) )
		{mismatch_VS=0;
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">NO</font></td>");}
		else
		{mismatch_VS=1;
		lData.append("<td colspan=0 align=\"left\"><font size=\"2\">YES</font></td>");}
		lData.append("</tr></table><br><hr>");

		if(LRStatsFlag==0 & WilyStatsFlag==0 & GangliaStatsFlag==0 & PerfmonStatsFlag==0 & VmStatStatsFlag==0)
		{
			lData.append("<div align=center> Please select atleast one option and upload corresponding results file. <a href=\"/AdministratorModule/jsp/resUpload.jsp\">Go Back</a></div>");
		}		
		else
		{
			if(mismatch_LR==1 | mismatch_WI==1 | mismatch_GA==1 | mismatch_PM==1 | mismatch_VS==1)
			{
				lData.append("<div align=center> Mismatch found in Options selected and file types (in zip file), Please select the options properly and update the results zip file. <a href=\"/AdministratorModule/jsp/resUpload.jsp\">Go Back</a></div>");
				deleteFiles(filePath,"xls");
				deleteFiles(filePath,"wily");
				deleteFiles(filePath,"perfmon");
				deleteFiles(filePath,"ganglia");
				deleteFiles(filePath,"vmstat");
			}
			else
			{
				if(PerfmonStatsFlag==1)
				{
					lData.append("<script language=\"JavaScript\">function validateForm(){if(document.LoginForm.perfmoncounters.value==0){alert(\"Please Enter Perfmon Counter Details.\");document.LoginForm.perfmoncounters.focus();return false;}}</script>");
					lData.append("<form name=\"LoginForm\" ACTION=\"/AdministratorModule/jsp/UpdatetoDB.jsp\" METHOD=\"POST\" onSubmit=\"return validateForm()\">");
					lData.append("<div> Since you have PERFMON files in your resultset, so please enter the perfmon counters you want show in report in comma separated format in below text box.<br> for e.g. <font color=BLUE><i>Processor(_Total)\\% Processor Time,Memory\\Available MBytes</i></font> etc.<br><br><b>Note:</b> Since perfmon shows a lot of counters which may not be relevant from report perspective so please enter specific counters to get filtered results. For example just typing <b>processor</b> may retrieve a lot of counters which may not be relevant.");
					lData.append("<br><br><input type=\"text\" size=\"90\" name=\"perfmoncounters\" value=\"\"></div><br>");
					lData.append("<div align=center> No Mismatch found in file types, Please proceed with result upload. <INPUT TYPE=SUBMIT VALUE=\"PROCEED\"></div></form>");

				}
				else
				{
					lData.append("<form name=\"LoginForm\" ACTION=\"/AdministratorModule/jsp/UpdatetoDB.jsp\" METHOD=\"POST\">");

					lData.append("<div align=center> No Mismatch found in file types, Please proceed with result upload. <INPUT TYPE=SUBMIT VALUE=\"PROCEED\"></div></form>");					
				
				}
			}
		}



		return lData.toString();
	}
	public static String SearchFilePresent(String fold,String ext)
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
					if(f.getName().toLowerCase().endsWith("."+ext))
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
	public static void doUnzipFiles(String zipFileName,String directoryPath) {
		try {
			ZipFile zf = new ZipFile(zipFileName);
			System.out.println("Archive:  " + zipFileName);

			for (Enumeration entries = zf.entries(); entries.hasMoreElements();)
			{

				ZipEntry zipEntry = (ZipEntry)entries.nextElement();
				String zipEntryName = directoryPath+"\\"+zipEntry.getName();
				System.out.println("  inflating: " + zipEntryName);                
				int lastDirSep;
				if ( (lastDirSep = zipEntryName.lastIndexOf('/')) > 0 ) {
					String dirName = zipEntryName.substring(0, lastDirSep);
					System.out.println("  dirname: " + dirName);
					(new File(dirName)).mkdirs();
				}
				if (!zipEntryName.endsWith("/")) {
					OutputStream out = new FileOutputStream(zipEntryName);
					InputStream in = zf.getInputStream(zipEntry);

					byte[] buf = new byte[1024];
					int len;
					while((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.close();
					in.close();
				}
			}
			zf.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public static String[] filesinafolder(String fold,String ext)
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
					if(f.getName().toLowerCase().endsWith("."+ext))
					{
						fNamearr[fileCounts]=f.getName();
						fileCounts =fileCounts+1;   
					}	
				}  
			}
		}		
		return fNamearr;
	}
	public static void deleteFiles(String fold,String ext)
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
					if(f.getName().endsWith("."+ext))
					{
						f.delete();   
					}	
				}  
			}
		}		
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
		Statement lStatement1 = null;
		ResultSet lResultSet1 = null;
		boolean lExist=false;
		String lQuery = "show tables where Tables_in_"+lDbUsed+" like '%"+ProjectName+"%';";
		StringBuilder lData = new StringBuilder();
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lExist=lResultSet.next();
			if (lExist)
			{
				lQuery="select distinct(Test_ID) from lr_stats_details_"+ProjectName+" where test_id='"+TestID+"';";
				lStatement1 = lConnection.createStatement();
				lResultSet1 = lStatement1.executeQuery(lQuery);
				lExist=lResultSet1.next();
				if (lExist)
				{
					lData.append("The Results with same TestID already exists in the database for the provided Project Name.<br><br><a href=\"/AdministratorModule/jsp/resUpload.jsp\">Go Back</a>");
				}
				else
				{
					lData.append("");
				}					
			}
			else
			{
				lData.append("");
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
				if(lResultSet1 != null){
					lResultSet1.close();					
				}
				if(lStatement1 != null){
					lStatement1.close();					
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

	public static boolean SearchFilePresentBool(String filename,String loc)
	{
		File f1 = new File(loc);
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
					if(f.getName().toLowerCase().contains(filename.toLowerCase()))
					{
					//	System.out.println(f.getName());
						count =count+1;   
					}	
				}  
			}
		}
		if(count==0)
		{
			return false;
		}
		else
		{
			return true;
		}		
	}
	public static String checkLRExistence(String ProjectName)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		String lDbUsed = PropertyConf.getProperty("DatabaseUsed");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		boolean lExist=false;
		String lQuery = "show tables where Tables_in_"+lDbUsed+" like '%"+ProjectName+"%';";
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
	
	public static String manageDBResultsUpload(String TestID,String ProjectName,int LRStatsFlag,int WilyStatsFlag,int GangliaStatsFlag,int PerfmonStatsFlag,int VmStatStatsFlag, String PFMTags)
	{
		String lResult="Project Name: <b>"+ProjectName+"</b><br>Test ID: <b>"+TestID+"</b><br>Results Uploaded Sussessfully!!";
		String filePath = PropertyConf.getProperty("FileDir")+"\\"+ProjectName+"_"+TestID;
		try
		{
		createResultsTables(filePath,TestID,ProjectName,LRStatsFlag,WilyStatsFlag,GangliaStatsFlag,PerfmonStatsFlag,VmStatStatsFlag,PFMTags);
		updateResultsFiles(filePath,TestID,ProjectName,LRStatsFlag,WilyStatsFlag,GangliaStatsFlag,PerfmonStatsFlag,VmStatStatsFlag,PFMTags);
		String[] fNamearr;
		fNamearr=filesinafolder(filePath,"sql");
		for(int i=0;i<fileCounts;i++)
		{
			concatenateFiles(filePath+"\\"+fNamearr[i],filePath+"\\FINAL_RESULTS.sql");
		}
		//deleteFiles(filePath,"wily");

		uploadResultsFilestoDB(filePath,TestID,ProjectName);
		}
		catch (Exception e)
		{			
			e.printStackTrace();
			lResult=e.getMessage();
		}
		return lResult.toString();
	}
	
	public static void createResultsTables(String filePath,String TestID,String ProjectName,int LRStatsFlag,int WilyStatsFlag,int GangliaStatsFlag,int PerfmonStatsFlag,int VmStatStatsFlag,String PFMTags)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		String lQuery="";
		String LRExistence=checkLRExistence(ProjectName);
		try 
		{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			System.out.println("Database connection establish...!");
			Statement lStatement1 = lConnection.createStatement();
			if(LRStatsFlag==1 & LRExistence.equals(""))
			{

				lQuery="CREATE TABLE  `lr_txns_details_"+ProjectName+"` (`Test_ID` varchar(200) default NULL,`Transaction_Name` varchar(100) default NULL,`Min_Value` float default NULL,`Avg_Value` float default NULL,`Max_Value` float default NULL,`NintyPercent` float default NULL,`Pass_Value` int(10) unsigned default NULL,`Fail_Value` int(10) unsigned default NULL,`Stop_Value` int(10) unsigned default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;";
				lStatement1.executeUpdate(lQuery);
				lQuery="CREATE TABLE  `lr_stats_details_"+ProjectName+"` (`Test_ID` varchar(200) default NULL,`Counter_Name` varchar(200) default NULL,  `Counter_Value` varchar(200) default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;";
				lStatement1.executeUpdate(lQuery);
				lQuery="CREATE TABLE  `test_obs_det_"+ProjectName+"` (`Test_ID` varchar(200) default NULL,`test_Observation` varchar(5000) default NULL,`test_Details` varchar(2500) default NULL,`test_summary` varchar(2000) default NULL,`test_status` varchar(70) default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;";
				lStatement1.executeUpdate(lQuery);
			}
			//LR TABLES
			
			if(SearchFilePresentBool("Response Time",filePath))
			{
				lQuery="CREATE TABLE  `responsetime_stats_"+ProjectName+"_"+TestID+"` (`Scenario_Elapsed_Time` datetime default NULL,`Transaction_Response_Time` float default NULL,`Transaction_Name` varchar(150) default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;";
				lStatement1.executeUpdate(lQuery);
			}
			if(SearchFilePresentBool("Errors",filePath))
			{
				lQuery="CREATE TABLE  `errors_stats_"+ProjectName+"_"+TestID+"` (`Error_Type` varchar(100) default NULL,`Scenario_Elapsed_Time` datetime default NULL,`Txn_Name` varchar(100) default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;";
				lStatement1.executeUpdate(lQuery);
			}
			if(SearchFilePresentBool("Throughput",filePath))
			{
				lQuery="CREATE TABLE  `throughput_stats_"+ProjectName+"_"+TestID+"` (`row_num` int(10) default NULL,`Scenario_Elapsed_Time` datetime default NULL,`Throughput` double default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;";
				lStatement1.executeUpdate(lQuery);
			}
			if(SearchFilePresentBool("Running",filePath))
			{
				lQuery="CREATE TABLE  `runningvusers_stats_"+ProjectName+"_"+TestID+"` (`row_num` int(10) default NULL,`Scenario_Elapsed_Time` datetime default NULL,`vusers` int(10) default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;";
				lStatement1.executeUpdate(lQuery);
			}
			//WILY TABLES
			
			if(WilyStatsFlag==1)
			{
				lQuery="CREATE TABLE  `wily_stats_"+ProjectName+"_"+TestID+"` (`Agent_Name` varchar(200) default NULL,  `Resource_Name` varchar(200) default NULL,  `Metric_Name` varchar(200) default NULL,  `TimeStamp` datetime default NULL,  `Metric_Value` double default NULL, `Counter_Number` varchar(50) default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;";
				lStatement1.executeUpdate(lQuery);
			}
			//GANGLIA TABLES
			
			if(GangliaStatsFlag==1)
			{
				lQuery=createGangliaTable(filePath,TestID,ProjectName);
				lStatement1.executeUpdate(lQuery);

			}
			//PERFMON TABLES
			
			if(PerfmonStatsFlag==1)
			{
				lQuery=createPerfmonTable(filePath,TestID,ProjectName,PFMTags);
				lStatement1.executeUpdate(lQuery);

			}
			if(VmStatStatsFlag==1)
			{
				
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
	public static String createPerfmonTable(String filePath,String TestID,String ProjectName, String PFMTags)
	{
		StringTokenizer stObj= new StringTokenizer(PFMTags,",");
		int PFMTokCounts=0;
		String[] PFMTokens=new String[30];
		while(stObj.hasMoreTokens())
		{
			PFMTokens[PFMTokCounts]=stObj.nextToken();
			PFMTokCounts=PFMTokCounts+1;
		}
		StringBuilder lData = new StringBuilder();
		String[] fNamearr = filesinafolder(filePath,"perfmon");
		lData.append("CREATE TABLE  `perfmon_stats_"+ProjectName+"_"+TestID+"` ( ");
		lData.append(" `Sample_time` datetime default NULL,");
		for(int i=0;i<fileCounts;i++)
		{
			BufferedReader is = null;
			try
			{
				is = new BufferedReader(new FileReader(filePath+"\\"+fNamearr[i]));
				String Line1;
				Line1= is.readLine();
				StringTokenizer stLine1;
				stLine1=new StringTokenizer(Line1, ",");
				stLine1.nextToken();
				while(stLine1.hasMoreTokens())
				{
					String temp=stLine1.nextToken();
					for(int tt=0;tt<PFMTokCounts;tt++)
					{
						if(temp.toLowerCase().contains(PFMTokens[tt].toLowerCase()))
						{
							String cols=temp.replace(" ","").replace("\\","").replace("%","").replace("\"", "").replace("(", "").replace(")", "").replace("-","").replace(":","").replace(".","").replace("/","_");
							lData.append(" `"+cols+"` float default NULL,");
						//	lData.append(" `"+temp+"` varchar(50) default NULL,");
						}						
					}					
				}
				is.close();
			}
			catch (Exception e)
			{			
				e.printStackTrace();
			}
		} 
		lData.append(" `Col_Cnt` int(1) unsigned default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
		return lData.toString();
	}
	public static String createGangliaTable(String filePath,String TestID,String ProjectName)
	{
		StringBuilder lData = new StringBuilder();
		String[] fNamearr = filesinafolder(filePath,"ganglia");
		lData.append("CREATE TABLE  `ganglia_stats_"+ProjectName+"_"+TestID+"` ( ");
		lData.append(" `Sample_time` datetime default NULL,");
		for(int i=0;i<fileCounts;i++)
		{
			BufferedReader is = null;
			try
			{
				is = new BufferedReader(new FileReader(filePath+"\\"+fNamearr[i]));
				String Line1,Line2,HostName;
				Line1= is.readLine();
				Line2= is.readLine();
				StringTokenizer stLine1,stLine2;
				stLine2=new StringTokenizer(Line2, ",");
				stLine1=new StringTokenizer(Line1, ",");
				stLine2.nextToken();
				HostName=stLine2.nextToken();
				stLine1.nextToken();
				stLine1.nextToken();
				while(stLine1.hasMoreTokens())
				{
					lData.append(" `"+HostName+"_"+stLine1.nextToken()+"` float default NULL,");
				}
				is.close();
			}
			catch (Exception e)
			{			
				e.printStackTrace();
			}
		} 
		lData.append(" `Col_Cnt` int(1) unsigned default NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
		return lData.toString();
	}
	
	public static void updateResultsFiles(String filePath,String TestID,String ProjectName,int LRStatsFlag,int WilyStatsFlag,int GangliaStatsFlag,int PerfmonStatsFlag,int VmStatStatsFlag,String PFMTags)
	{
		String[] fNamearr;
		try
		{
			if(LRStatsFlag==1)
			{
				// GET STAR END TIME FROM FILE:
				String startEndTimeTokens=getStartEndTimeFromLR(filePath);
				
				// code for extracting LR Results.			
				fNamearr=filesinafolder(filePath,"xls");
				for(int i=0;i<fileCounts;i++)
				{
					String fName=filePath+"\\"+fNamearr[i];
					if(fName.toLowerCase().contains("summary"))
					{
						// code for extracting LR Test Statistics.	
						extractLR(fName,TestID,ProjectName);					
						// code for extracting LR Transactional Analysis.
						extractLRTxn(fName,TestID,ProjectName);
					}
					else if(fName.toLowerCase().contains("response time"))
					{						
						extractLRrespTime(fName,TestID,ProjectName,startEndTimeTokens);
					}
					else if(fName.toLowerCase().contains("errors"))
					{
						extractLRError(fName,TestID,ProjectName,startEndTimeTokens);
					}
					else if(fName.toLowerCase().contains("throughput"))
					{
						extractLRThroughput(fName,TestID,ProjectName,startEndTimeTokens);
					}

				}
				fNamearr=filesinafolder(filePath,"csv");
				for(int i=0;i<fileCounts;i++)
				{
					String fName=filePath+"\\"+fNamearr[i];
					if(fName.toLowerCase().contains("running"))
					{
						// code for extracting LR Running Vusers Stats.	
						extractrunningVusers(fName,TestID,ProjectName);
					}
				}
				deleteFiles(filePath,"xls");
				deleteFiles(filePath,"csv");
			}
			if(WilyStatsFlag==1)
			{
				// code for extracting Wily Stats.
				fNamearr=filesinafolder(filePath,"wily");
				for(int i=0;i<fileCounts;i++)
				{
					readFile(filePath+"\\"+fNamearr[i], TestID, ProjectName,"Wily_"+i);
				}
				for(int i=0;i<fileCounts;i++)
				{
					concatenateFiles(filePath+"\\"+fNamearr[i]+"_temp",filePath+"\\Wily_Stats.sql");
				}
				deleteFiles(filePath,"wily");
				deleteFiles(filePath,"wily_temp");
			}
			if(GangliaStatsFlag==1)
			{
				// code for extracting Ganglia Stats.
				createGangliaFiles(filePath,TestID,ProjectName);
				deleteFiles(filePath,"ganglia");
				deleteFiles(filePath,"ganglia_temp");
			}
			if(PerfmonStatsFlag==1)
			{
				// code for extracting Perfmon Stats.
				createPerfmonFiles(filePath,TestID,ProjectName,PFMTags);				
				deleteFiles(filePath,"perfmon");
				deleteFiles(filePath,"perfmon_1");
			}
			if(VmStatStatsFlag==1)
			{
				// code for extracting VmStat Stats.
				deleteFiles(filePath,"vmstat");
			}
			deleteFiles(filePath,"final");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static int getFileLength(String FileName)
	{
		int lineCount = 0;
		BufferedReader is = null;
		try{is = new BufferedReader(new FileReader(FileName));
			while ((is.readLine()) != null)
			{lineCount=lineCount+1;}
			is.close();}
		catch (Exception e)
		{e.printStackTrace();}
		
		return lineCount;
	}
	public static void createGangliaFiles(String filePath,String TestID,String ProjectName) {
		String[] fNamearr;
		fNamearr=filesinafolder(filePath,"ganglia");
		int initCount=0,minValue=0;
		int[] fLength = new int[fileCounts];
		for(int i=0;i<fileCounts;i++)
		{
			fLength[i]=getFileLength(filePath+"\\"+fNamearr[i]);
		}
		minValue=fLength[0];
		for(int i=1;i<fileCounts;i++)
		{
			if(fLength[i]<minValue)
			minValue=fLength[i];			
		}
		for(int i=0;i<fileCounts;i++)
		{
			BufferedReader is = null;
			String outfile=filePath+"\\"+fNamearr[i]+"_temp";
			int flenCount=0;
			try
			{
				is = new BufferedReader(new FileReader(filePath+"\\"+fNamearr[i]));
				FileWriter bw=new FileWriter(outfile);
				String furLines;
				is.readLine();
				flenCount=flenCount+1;
				while ((furLines= is.readLine()) != null)
				{
					if(flenCount<minValue)
					{
						StringTokenizer stLine=new StringTokenizer(furLines, ",");
						if(initCount==0)
						{bw.append(stLine.nextToken()+",");}
						else
						{stLine.nextToken();}
						stLine.nextToken();
						while(stLine.hasMoreTokens())
						{bw.append(stLine.nextToken()+",");}
						bw.append(System.getProperty("line.separator"));
					}
					flenCount=flenCount+1;
				}
				is.close();
				bw.close();
			}
			catch (Exception e)
			{			
				e.printStackTrace();
			}
			initCount=1;
		}
		fNamearr=filesinafolder(filePath,"ganglia_temp");
		try
		{
			mergeFiles(filePath,fNamearr,TestID,ProjectName,"ganglia");
			ConvertSql(filePath, TestID, ProjectName, "ganglia");
		}
		catch (Exception e)
		{			
			e.printStackTrace();
		}
	}
	public static void mergeFiles(String dir,String[] fNamearr,String TestID,String ProjectName, String types) throws IOException
	{
		int rowCount=0;
		int colCount=getTablecolCount(TestID,ProjectName,types);
		String fileName[]=new String [30];
		String filein[]=new String [30];
		BufferedReader is[] = new BufferedReader[20];
		String outfile=dir+"\\"+types+".final";
		String items[][]=new String[2400][30];
		FileWriter bw;
		try
		{
			bw = new FileWriter(outfile);
			for(int i=0;i<fileCounts;i++)
			{
				fileName[i]=dir+"\\"+fNamearr[i];					
				is[i] = new BufferedReader(new FileReader(fileName[i]));
				int count=0;
				while ((filein[i] = is[i].readLine()) != null)
				{
					items[count][i]=filein[i];
					count = count+1;       
				}
				is[i].close();
				rowCount=count;
			}
			for(int j=0;j<rowCount;j++)
			{
				for(int i=0;i<fileCounts;i++)
				{
					bw.append(items[j][i]);
				}
				bw.append(""+colCount);
				bw.append(System.getProperty("line.separator"));
			}			
			bw.append(System.getProperty("line.separator"));
			bw.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{			
			e.printStackTrace();
		}
		
		//delete last line
		RandomAccessFile f = new RandomAccessFile(outfile, "rw");
		long length = f.length() - 1;
		byte b;
		do {                     
			length -= 1;
			f.seek(length);
			b = f.readByte();
		} while(b != 10);
		f.setLength(length+1);
		f.close();
	}
	public static void ConvertSql(String dir, String TestID,String ProjectName, String types) throws IOException
	{
		String outfile=dir+"\\"+types+".sql";
		String infile=dir+"\\"+types+".final";
		BufferedReader is = null;
		int totCols=getTablecolCount(TestID,ProjectName,types);
		try
		{
			is = new BufferedReader(new FileReader(infile));
			FileWriter bw=new FileWriter(outfile);
			String furLines;
			if(types.equals("perfmon"))
			is.readLine();
			while ((furLines= is.readLine()) != null)
			{
				bw.append("INSERT into "+types+"_stats_"+ProjectName+"_"+TestID+" VALUES ('");
				StringTokenizer stLine=new StringTokenizer(furLines, ",");
				String temp=stLine.nextToken();
				if(types.equals("ganglia"))
				{bw.append(DateformatConverter("yyyy/MM/dd HH:mm:ss","yyyy-MM-dd HH:mm:ss",temp)+"',");}
				if(types.equals("perfmon"))
				{bw.append(DateformatConverter("MM/dd/yyyy HH:mm:ss.SSS","yyyy-MM-dd HH:mm:ss",temp)+"',");}				
				int currCols=2;
				while(stLine.hasMoreTokens())
				{
					if(currCols<totCols)
						bw.append(stLine.nextToken()+",");
					else
						bw.append(stLine.nextToken());
					currCols=currCols+1;
				}
				bw.append(");");				
				bw.append(System.getProperty("line.separator"));							
			}
			is.close();
			bw.close();
		}
		catch (Exception e)
		{			
			e.printStackTrace();
		}
	}
	
	public static int getTablecolCount(String TestID,String ProjectName, String types)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		int colCount=0;
		String lQuery = "SELECT Count(*) FROM INFORMATION_SCHEMA.Columns where TABLE_NAME = '"+types+"_stats_"+ProjectName+"_"+TestID+"';";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lResultSet.next();
			colCount=lResultSet.getInt(1);
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
	public static void concatenateFiles(String fileName,String output) {
		RandomAccessFile file = null;
		String line = null;
		FileWriter f= null;

		try {
			f= new FileWriter(output, true);
			file = new RandomAccessFile(fileName, "r");
			while ((line = file.readLine()) != null) {
				f.append(line);
				f.append(System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException fnf) {
			System.err.println("File: " + fileName + " not found.");
		} catch (Exception e) {
			System.err.println(e.toString());
		} finally {
			if (file != null) {
				try {
					file.close();
					f.close();
				} catch (IOException io) {
				}
			}
		}
	}
	public static void readFile(String filename,String TestID,String ProjectName, String CounterCount)
	{
		BufferedReader is = null;
		String outfile=filename+"_temp";
		int totalcolumnCount=0,selectedcolumnCount=0;
		String totalcolunmsarr[] = new String[100];
		String selectedcolunmsarr[] = new String[100];
		try
		{
			is = new BufferedReader(new FileReader(filename));
			FileWriter bw=new FileWriter(outfile);
			String filein;
			filein= is.readLine();
			filein= is.readLine();
			StringTokenizer st;
			st=new StringTokenizer(filein, ",");
			while(st.hasMoreTokens())
			{ 				
				totalcolunmsarr[totalcolumnCount]=st.nextToken();
				totalcolumnCount=totalcolumnCount+1;				
			}
			StringTokenizer st1;
			st1=new StringTokenizer("3,4,5,8,13", ",");
			while(st1.hasMoreTokens())
			{ 				
				selectedcolunmsarr[selectedcolumnCount]=st1.nextToken();
				selectedcolumnCount=selectedcolumnCount+1;				
			}

			while ((filein = is.readLine()) != null)
			{
				int WilyTSCount=1;
				bw.append("INSERT into wily_stats_"+ProjectName+"_"+TestID+" VALUES (\"");
				StringTokenizer st2;
				int selectedvaluecount=0;
				String selectedvaluesarr[] = new String[100];
				st2=new StringTokenizer(filein, ",");
				while(st2.hasMoreTokens())
				{ 	
					selectedvaluesarr[selectedvaluecount]=st2.nextToken();
					selectedvaluecount=selectedvaluecount+1;				
				}
				for(int i=0;i<selectedcolumnCount;i++)
				{
					if(WilyTSCount==4)
					{
						String temp=selectedvaluesarr[Integer.parseInt(selectedcolunmsarr[i])];
						StringTokenizer stObj=new StringTokenizer(temp," ");
						int tokCount=1;
						String temp2=stObj.nextToken();
						while(stObj.hasMoreTokens())
						{
							if(tokCount==4)
							{
								stObj.nextToken();	
							}
							else
							{
								temp2=temp2+" "+stObj.nextToken();
							}			
							tokCount=tokCount+1;
						}
						bw.append(DateformatConverter("EEE MMM dd HH:mm:ss yyyy", "yyyy-MM-dd HH:mm:ss",temp2));
					}
					else
					{
						bw.append(selectedvaluesarr[Integer.parseInt(selectedcolunmsarr[i])]);
					}
					WilyTSCount=WilyTSCount+1;
					bw.append("\", \"");
				}
				bw.append(CounterCount+"\");");
				bw.append(System.getProperty("line.separator"));
			}
			is.close();
			bw.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{			
			e.printStackTrace();
		}
	}
	public static void extractLRTxn(String fileName,String TestID,String ProjectName) throws IOException
	{
		InputStream input = new FileInputStream(fileName);
		HSSFWorkbook wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		int setPosition=0,sepStarter=0,j=0;
		FileWriter bw=new FileWriter(fileName+"_LRTxn.sql");
		Iterator rows = sheet.rowIterator(); 
		while(rows.hasNext())
		{ 
			HSSFRow row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			if(setPosition==1)
			{
				bw.append("INSERT into lr_txns_details_"+ProjectName+" VALUES (\"");
				bw.append(TestID);
				bw.append("\", \"");
			}
			while(cells.hasNext())
			{                	
				HSSFCell cell = (HSSFCell) cells.next();
				j=cell.getCellNum()+1;
				if(cell.toString().contains(PropertyConf.getProperty("LRTxnStPt")))
				{
					setPosition=1;
					break;
				}
				if(cell.toString().equals(" "))
				{
					setPosition=0;
					break;
				}
				if(setPosition==1)
				{
					sepStarter=1;
					switch(j)
					{
					case 1:
						bw.append(cell.toString());
						bw.append("\", ");
						break;
					case 2:
						break;
					case 3:
						bw.append(cell.toString());
						bw.append(", ");
						break;
					case 4:
						bw.append(cell.toString());
						bw.append(", ");
						break;
					case 5:
						bw.append(cell.toString());
						bw.append(", ");
						break;
					case 6:
						break;
					case 7:
						bw.append(cell.toString());
						bw.append(", ");
						break;
					case 8:
						bw.append(cell.toString());
						bw.append(", ");
						break;
					case 9:
						bw.append(cell.toString());
						bw.append(", ");
						break;
					case 10:
						bw.append(cell.toString()+");");
						break;
					}					
				}
			}//columns
			if(setPosition==1 & sepStarter==1)
			{
				bw.append(System.getProperty("line.separator"));
			}
		}//row	        
		bw.close();
		//delete last line
		RandomAccessFile f = new RandomAccessFile(fileName+"_LRTxn.sql", "rw");
		long length = f.length() - 1;
		byte b;
		do {                     
			length -= 1;
			f.seek(length);
			b = f.readByte();
		} while(b != 10);
		f.setLength(length+1);
		f.close();
	}
	public static void extractLR(String fileName, String TestID, String ProjectName) throws IOException
	{
		int rownr=0,colnr = 0,i=0,j=0,Flag=0;
		InputStream input = new FileInputStream(fileName);
		HSSFWorkbook wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		String LRStatsvaluearr[] = new String[50];
		String LRStatscolcountarr[] = new String[50];
		StringTokenizer st1,st2;
		st1=new StringTokenizer(PropertyConf.getProperty("LRStats"), ",");		
		while(st1.hasMoreTokens())
		{ 
			LRStatsvaluearr[i]=st1.nextToken();
			i=i+1;				
		}
		st2=new StringTokenizer(PropertyConf.getProperty("LRStatsCount"), ",");
		while(st2.hasMoreTokens())
		{ 
			LRStatscolcountarr[j]=st2.nextToken();
			j=j+1;	
		}
		fileName=fileName+"_LRStats.sql";
		FileWriter bw=new FileWriter(fileName);
		for(int l=0;l<i;l++)
		{
			StringBuilder sb =new StringBuilder();
			rownr = findRow(sheet, LRStatsvaluearr[l]);
			colnr = findCol(sheet, LRStatsvaluearr[l]);
			HSSFRow row = sheet.getRow(rownr);
			colnr=colnr+Integer.parseInt(LRStatscolcountarr[l]);
			HSSFCell cell = row.getCell(colnr);
			sb.append("INSERT into lr_stats_details_"+ProjectName+" VALUES (\"");
			if(Flag==0)
			{
				sb.append(TestID);
				sb.append("\", \"");
				sb.append(LRStatsvaluearr[l]);
				sb.append("\", \"");
				sb.append(cell.toString().substring(8)+"\");");
				bw.append(sb.toString());
				bw.append(System.getProperty("line.separator"));
				Flag=1;
			}
			else
			{
				sb.append(TestID);
				sb.append("\", \"");
				sb.append(LRStatsvaluearr[l]);
				sb.append("\", \"");
				sb.append(cell.toString()+"\");");
				bw.append(sb.toString());
				bw.append(System.getProperty("line.separator"));
			}
		}
		bw.close();
	}
	private static int findRow(HSSFSheet sheet, String cellContent) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim().contains(cellContent)) {
						return row.getRowNum();  
					}
				}
			}
		}               
		return 0;
	}
	private static int findCol(HSSFSheet sheet, String cellContent) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					if (cell.getRichStringCellValue().getString().trim().contains(cellContent)) {
						return cell.getColumnIndex();  
					}
				}
			}
		}               
		return 0;
	}

	public static void extractrunningVusers(String filename,String TestID,String ProjectName)
	{
		BufferedReader is = null;
		String outfile=filename+"_LRRVU.sql";
		try
		{
			is = new BufferedReader(new FileReader(filename));
			FileWriter bw=new FileWriter(outfile);
			String filein;
			filein= is.readLine();
			while ((filein = is.readLine()) != null)
			{
				if(filein.equals(""))
				{					
				}
				else
				{
					bw.append("INSERT into runningvusers_stats_"+ProjectName+"_"+TestID+" VALUES (");
					StringTokenizer st2;
					st2=new StringTokenizer(filein, ",");
					bw.append(st2.nextToken()+",");
					bw.append("'"+DateformatConverter("MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss",st2.nextToken())+"',");
					bw.append(st2.nextToken()+");");
					bw.append(System.getProperty("line.separator"));
				}
			}
			is.close();
			bw.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{			
			e.printStackTrace();
		}
	}	
	public static void extractLRrespTime(String fileName, String TestID,String ProjectName,String startEndTimeTokens) throws IOException
	{
		StringTokenizer stObj=new StringTokenizer(startEndTimeTokens,",");
		String StartTime=stObj.nextToken();
		InputStream input = new FileInputStream(fileName);
		HSSFWorkbook wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		int j=0;
		FileWriter bw=new FileWriter(fileName+"_LRresponsetime.sql");
		Iterator rows = sheet.rowIterator();
		rows.next();
		Double cellValue=0.0;
		int cellToint=0;
		while(rows.hasNext())
		{
			StringBuilder sb= new StringBuilder();
			sb.append("INSERT into responsetime_stats_"+ProjectName+"_"+TestID+" VALUES (\"");
			HSSFRow row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			while(cells.hasNext())
			{                	
				HSSFCell cell = (HSSFCell) cells.next();
				j=cell.getCellNum()+1;
				switch(j)
				{
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					cellValue=Double.parseDouble(cell.toString());
					cellToint=cellValue.intValue();
					sb.append(AddTimetoTimeStamp("yyyy-MM-dd HH:mm:ss",StartTime,cellToint));
				//	sb.append(cell.toString());
					sb.append("\",");
					break;
				case 9:
					sb.append(cell.toString());
					sb.append(",");
					break;
				case 10:
					sb.append("'"+cell.toString()+"'");
					break;
				}
			}//columns
			sb.append(");");
			bw.append(sb.toString());
			bw.append(System.getProperty("line.separator"));			
		}//row	        
		bw.close();
		//delete last line
		RandomAccessFile f = new RandomAccessFile(fileName+"_LRresponsetime.sql", "rw");
		long length = f.length() - 1;
		byte b;
		do {                     
			length -= 1;
			f.seek(length);
			b = f.readByte();
		} while(b != 10);
		f.setLength(length+1);
		f.close();
	}
	public static void extractLRError(String fileName, String TestID,String ProjectName,String startEndTimeTokens) throws IOException
	{
		StringTokenizer stObj=new StringTokenizer(startEndTimeTokens,",");
		String StartTime=stObj.nextToken();
		InputStream input = new FileInputStream(fileName);
		HSSFWorkbook wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		int j=0;
		FileWriter bw=new FileWriter(fileName+"_LRerror.sql");
		Iterator rows = sheet.rowIterator();
		rows.next();
		Double cellValue=0.0;
		int cellToint=0;
		while(rows.hasNext())
		{
			StringBuilder sb= new StringBuilder();
			sb.append("INSERT into errors_stats_"+ProjectName+"_"+TestID+" VALUES (");
			HSSFRow row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			while(cells.hasNext())
			{                	
				HSSFCell cell = (HSSFCell) cells.next();
				j=cell.getCellNum()+1;
				switch(j)
				{
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					sb.append("'"+cell.toString()+"',");
					break;
				case 8:
					break;
				case 9:
					cellValue=Double.parseDouble(cell.toString());
					cellToint=cellValue.intValue();
					sb.append("'"+AddTimetoTimeStamp("yyyy-MM-dd HH:mm:ss",StartTime,cellToint)+"',");
					break;
				case 10:
					break;
				case 11:
					sb.append("'"+cell.toString()+"'");
					break;
				}
			}//columns
			sb.append(");");
			bw.append(sb.toString());
			bw.append(System.getProperty("line.separator"));			
		}//row	        
		bw.close();
		
		//delete last line
/*	
		RandomAccessFile f = new RandomAccessFile(fileName+"_LRerror.sql", "rw");
		long length = f.length() - 1;
		byte b;
		do {                     
			length -= 1;
			f.seek(length);
			b = f.readByte();
		} while(b != 10);
		f.setLength(length+1);
		f.close();
*/
	}
	public static void extractLRThroughput(String fileName, String TestID,String ProjectName,String startEndTimeTokens) throws IOException
	{
		StringTokenizer stObj=new StringTokenizer(startEndTimeTokens,",");
		String StartTime=stObj.nextToken();
		InputStream input = new FileInputStream(fileName);
		HSSFWorkbook wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		int j=0;
		FileWriter bw=new FileWriter(fileName+"_LRerror.sql");
		Iterator rows = sheet.rowIterator();
		rows.next();
		Double cellValue=0.0;
		int cellToint=0;
		int row_num=1;
		while(rows.hasNext())
		{
			
			StringBuilder sb= new StringBuilder();
			sb.append("INSERT into throughput_stats_"+ProjectName+"_"+TestID+" VALUES ("+row_num+",");
			HSSFRow row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			while(cells.hasNext())
			{                	
				HSSFCell cell = (HSSFCell) cells.next();
				j=cell.getCellNum()+1;
				switch(j)
				{
				case 1:
					cellValue=Double.parseDouble(cell.toString());
					cellToint=cellValue.intValue();
					sb.append("'"+AddTimetoTimeStamp("yyyy-MM-dd HH:mm:ss",StartTime,cellToint)+"',");
					break;
				case 2:
					sb.append(cell.toString());
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					break;
				}
			}//columns
			sb.append(");");
			bw.append(sb.toString());
			bw.append(System.getProperty("line.separator"));			
			row_num=row_num+1;
		}//row	        
		bw.close();
		
		//delete last line
/*	
		RandomAccessFile f = new RandomAccessFile(fileName+"_LRerror.sql", "rw");
		long length = f.length() - 1;
		byte b;
		do {                     
			length -= 1;
			f.seek(length);
			b = f.readByte();
		} while(b != 10);
		f.setLength(length+1);
		f.close();
*/
	}
	public static void createPerfmonFiles(String dirName,String TestID,String ProjectName,String PFMTags) {
		String[] fNamearr;
		try
		{
			fNamearr=filesinafolder(dirName,"perfmon");
			int minValue=0;
			int[] fLength = new int[fileCounts];
			for(int i=0;i<fileCounts;i++)
			{
				fLength[i]=getFileLength(dirName+"\\"+fNamearr[i]);
			}
			minValue=fLength[0];
			for(int i=1;i<fileCounts;i++)
			{
				if(fLength[i]<minValue)
				minValue=fLength[i];			
			}
			for(int i=0;i<fileCounts;i++)
			{
				readFilePFM(i,dirName+"\\"+fNamearr[i],PFMTags,minValue);
			}
			fNamearr=filesinafolder(dirName,"perfmon_1");
			mergeFiles(dirName,fNamearr,TestID,ProjectName,"perfmon");
			ConvertSql(dirName, TestID, ProjectName, "perfmon");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*			FileReader fr = new FileReader(dirName+"\\perfmon.final"); 
			BufferedReader br = new BufferedReader(fr); 
			FileWriter fw = new FileWriter(dirName+"\\outfile.txt"); 
			String line;
			while((line = br.readLine()) != null)
			{ 
			    line = line.trim(); // remove leading and trailing whitespace
			    if (!line.equals("")) // don't write out blank lines
			    {
			        fw.write(line, 0, line.length());
			    }
			    
			} 
			fr.close();
			fw.close();*/
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public static void readFilePFM(int initor,String filename,String PFMTags,int minValue)
	{
		String outfile=filename+"_1";
		StringTokenizer stObj= new StringTokenizer(PFMTags,",");
		int PFMTokCounts=0;
		StringBuilder lData = new StringBuilder();
		String[] PFMTokens=new String[30];
		while(stObj.hasMoreTokens())
		{
			PFMTokens[PFMTokCounts]=stObj.nextToken();
			PFMTokCounts=PFMTokCounts+1;
		}
		int totalcolumnCount=0,selectedcolumnCount=0;
		String totalcolunmsarr[] = new String[1000];
		String selectedcolunmsarr[] = new String[100];
		try
		{
			BufferedReader is = new BufferedReader(new FileReader(filename));
			FileWriter bw=new FileWriter(outfile);
			int flenCount=0;
			String filein;
			filein= is.readLine();
			flenCount=flenCount+1;
			StringTokenizer st;
			st=new StringTokenizer(filein, ",");
			while(st.hasMoreTokens())
			{ 	
				String temp=st.nextToken();
				totalcolunmsarr[totalcolumnCount]=temp;				
				for(int tt=0;tt<PFMTokCounts;tt++)
				{
					if(temp.toLowerCase().contains(PFMTokens[tt].toLowerCase()))
					{
						lData.append(totalcolumnCount+",");
					}						
				}
				totalcolumnCount=totalcolumnCount+1;
			}
			String countersReq = lData.toString();
			System.out.println(countersReq);
			if(initor==0)
			{
				countersReq="0,"+countersReq;
			}
			StringTokenizer st1;
			st1=new StringTokenizer(countersReq, ",");
			while(st1.hasMoreTokens())
			{ 				
				selectedcolunmsarr[selectedcolumnCount]=st1.nextToken();
				bw.append(totalcolunmsarr[Integer.parseInt(selectedcolunmsarr[selectedcolumnCount])]);
				bw.append(",");
				selectedcolumnCount=selectedcolumnCount+1;				
			}	
			bw.append(System.getProperty("line.separator"));
			filein= is.readLine();
			flenCount=flenCount+1;
			while ((filein = is.readLine()) != null)
			{
				if(flenCount<minValue)
				{

					StringTokenizer st2;
					int selectedvaluecount=0;
					String selectedvaluesarr[] = new String[1000];
					st2=new StringTokenizer(filein, ",");
					while(st2.hasMoreTokens())
					{ 	
						selectedvaluesarr[selectedvaluecount]=st2.nextToken();
						selectedvaluecount=selectedvaluecount+1;				
					}
					for(int i=0;i<selectedcolumnCount;i++)
					{
						bw.append(selectedvaluesarr[Integer.parseInt(selectedcolunmsarr[i])].replace("\"",""));
						bw.append(",");
					}	
					bw.append(System.getProperty("line.separator"));
				}
				flenCount=flenCount+1;
			}
			is.close();
			bw.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{			
			e.printStackTrace();
		}		
	}
	
	public static void uploadResultsFilestoDB(String filePath,String TestID,String ProjectName)
	{
		String dbUserName = PropertyConf.getProperty("UserName");
		String dbPassword = PropertyConf.getProperty("UserPassword");
		String dbschema = PropertyConf.getProperty("DatabaseUsed");		
		executeScript(dbschema, dbUserName, dbPassword, filePath+"\\FINAL_RESULTS.sql",true);
		/*
			String[] fNamearr;
			// code for updating Results to DB.
			fNamearr=filesinafolder(filePath,"final");
			for(int i=0;i<fileCounts;i++)
			{
				updateDB(filePath+"\\"+fNamearr[i],TestID,ProjectName);
			}
		*/
		deleteFiles(filePath,"sql");
		updateObservationsDB(TestID,ProjectName);
		String resTokens=ResultsAnalysisEngine.checkResultsExistence(TestID,ProjectName);
		System.out.println("\n\n\n\n\n\n\n\nTestID:::::::::::::::::::::::::::::::::::::::::::: \n"+TestID+"\n\n\n\n\n\n\n\n\n");
		System.out.println("\n\n\n\n\n\n\n\n\n ProjectName:::::::::::::::::::::::::::::::::::::::::::: \n"+ProjectName+"\n\n\n\n\n\n\n\n\n");

		System.out.println("\n\n\n\n\n\n\n\n\nresTokens:::::::::::::::::::::::::::::::::::::::::::: \n"+resTokens+"\n\n\n\n\n\n\n\n\n");
		if(resTokens.contains("runningvusers"))
		{
			updateSteadyStateTimings(TestID,ProjectName);
		}		
	}
	
	public static String executeScript (String dbname, String dbuser, String dbpassword, String scriptpath, boolean verbose) 
	{
		String output = null;
		String mySQLEX = PropertyConf.getProperty("MySQLPath");
		try {
			String[] cmd = new String[]{mySQLEX,
					dbname,
					"--user=" + dbuser,
					"--password=" + dbpassword,
					"-e",
					"\"source " + scriptpath + "\""

			};
			System.err.println(cmd[0] + " " + cmd[1] + " " +
					cmd[2] + " " + cmd[3] + " " +
					cmd[4] + " " + cmd[5]);
			Process proc = Runtime.getRuntime().exec(cmd);
			if (verbose) {
				InputStream inputstream = proc.getInputStream();
				InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
				BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

				// read the output
				String line;
				while ((line = bufferedreader.readLine()) != null) {
					System.out.println(line);
				}

				// check for failure
				try {
					if (proc.waitFor() != 0) {
						System.out.println("exit value = " + proc.exitValue());
					}
				}
				catch (InterruptedException e) {
					System.err.println(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public static void updateDB(String fileName,String TestID,String ProjectName)
	{
		Connection conn = null;
		String dbUserName = PropertyConf.getProperty("UserName");
		String dbPassword = PropertyConf.getProperty("UserPassword");
		String dbUrl = PropertyConf.getProperty("DatabaseURL");
		String insertSql="";        
		try {
			Class forNam = Class.forName(PropertyConf.getProperty("Driver"));
			try 
			{
				forNam.newInstance();
			} 		
			catch(Exception ae)
			{
				ae.printStackTrace();
			}
		} 
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			System.out.println("Database connection establish...!");
			if(fileName.contains("Wily_Stats.final"))
			{
				BufferedReader is = null;
				is = new BufferedReader(new FileReader(fileName));
				String filein;	
				Statement st = conn.createStatement();
				while ((filein = is.readLine()) != null)
				{
					StringTokenizer st2;
					int selectedvaluecount=0;
					String selectedvaluesarr[] = new String[10];
					st2=new StringTokenizer(filein, ",");
					while(st2.hasMoreTokens())
					{ 	
						selectedvaluesarr[selectedvaluecount]=st2.nextToken();
						selectedvaluecount=selectedvaluecount+1;				
					}
					insertSql = "INSERT into wily_stats_"+ProjectName+"_"+TestID+" VALUES (\""+selectedvaluesarr[0]+"\", \""+selectedvaluesarr[1]+"\", \""+selectedvaluesarr[2]+"\", \""+selectedvaluesarr[3]+"\", "+selectedvaluesarr[4]+");";                    
					int val = st.executeUpdate(insertSql);					
				}
				is.close();
				st.close();
			}
			else if(fileName.contains("_LRStats.final"))
			{
				BufferedReader is = null;
				is = new BufferedReader(new FileReader(fileName));
				String filein;	
				Statement st = conn.createStatement();
				while ((filein = is.readLine()) != null)
				{
					StringTokenizer st2;
					int selectedvaluecount=0;
					String selectedvaluesarr[] = new String[10];
					st2=new StringTokenizer(filein, ",");
					while(st2.hasMoreTokens())
					{ 	
						selectedvaluesarr[selectedvaluecount]=st2.nextToken();
						selectedvaluecount=selectedvaluecount+1;				
					}
					insertSql = "INSERT into lr_stats_details_"+ProjectName+" VALUES (\""+selectedvaluesarr[0]+"\", \""+selectedvaluesarr[1]+"\", \""+selectedvaluesarr[2]+"\");";                    
					int val = st.executeUpdate(insertSql);					
				}
				is.close();
				st.close();
			}
			else if(fileName.contains("_LRTxn.final"))
			{
				BufferedReader is = null;
				is = new BufferedReader(new FileReader(fileName));
				String filein;	
				Statement st = conn.createStatement();
				while ((filein = is.readLine()) != null)
				{
					StringTokenizer st2;
					int selectedvaluecount=0;
					String selectedvaluesarr[] = new String[15];
					st2=new StringTokenizer(filein, ",");
					while(st2.hasMoreTokens())
					{ 	
						selectedvaluesarr[selectedvaluecount]=st2.nextToken();
						selectedvaluecount=selectedvaluecount+1;				
					}
					insertSql = "INSERT into lr_txns_details_"+ProjectName+" VALUES (\""+selectedvaluesarr[0]+"\", \""+selectedvaluesarr[1]+"\", "+selectedvaluesarr[2]+", "+selectedvaluesarr[3]+", "+selectedvaluesarr[4]+", "+selectedvaluesarr[5]+", "+selectedvaluesarr[6]+", "+selectedvaluesarr[7]+", "+selectedvaluesarr[8]+");";                    
					int val = st.executeUpdate(insertSql);
					
				}
				is.close();
				st.close();
			}
			else if(fileName.contains("ganglia.final"))
			{
				BufferedReader is = null;
				is = new BufferedReader(new FileReader(fileName));
				String filein;
				int totCols=getTablecolCount(TestID,ProjectName,"ganglia");
				Statement st = conn.createStatement();
				while ((filein = is.readLine()) != null)
				{
					StringBuilder lData = new StringBuilder();
					lData.append("INSERT into ganglia_stats_"+ProjectName+"_"+TestID+" VALUES ('");
					StringTokenizer st2;
					int currCols=0;
					st2=new StringTokenizer(filein, ",");
					lData.append(st2.nextToken()+"',");
					currCols=currCols+2;
					while(st2.hasMoreTokens())
					{ 	
						if(currCols<totCols)
							lData.append(st2.nextToken()+",");
						else
							lData.append(st2.nextToken());
						currCols=currCols+1;						
					}
					lData.append(");");
					insertSql = lData.toString();					
					int val = st.executeUpdate(insertSql);					
				}
				is.close();
				st.close();
			}
			else if(fileName.contains("perfmon.final"))
			{
				BufferedReader is = null;
				is = new BufferedReader(new FileReader(fileName));
				String filein;
				filein = is.readLine();
				int totCols=getTablecolCount(TestID,ProjectName,"perfmon");
				Statement st = conn.createStatement();
				while ((filein = is.readLine()) != null)
				{
					StringBuilder lData = new StringBuilder();
					lData.append("INSERT into perfmon_stats_"+ProjectName+"_"+TestID+" VALUES ('");
					StringTokenizer st2;
					int currCols=0;
					st2=new StringTokenizer(filein, ",");
					lData.append(st2.nextToken()+"',");
					currCols=currCols+2;
					while(st2.hasMoreTokens())
					{ 	
						if(currCols<totCols)
							lData.append(st2.nextToken()+",");
						else
							lData.append(st2.nextToken());
						currCols=currCols+1;						
					}
					lData.append(");");
					insertSql = lData.toString();
					//	System.out.println(insertSql);
					int val = st.executeUpdate(insertSql);					
				}
				is.close();
				st.close();
			}
			else if(fileName.contains("_LRresponsetime.final"))
			{
				BufferedReader is = null;
				is = new BufferedReader(new FileReader(fileName));
				String filein;
				filein = is.readLine();				
				Statement st = conn.createStatement();
				while ((filein = is.readLine()) != null)
				{					
					StringTokenizer st2=new StringTokenizer(filein, ",");
					insertSql="INSERT into responsetime_stats_"+ProjectName+"_"+TestID+" VALUES ("+st2.nextToken()+","+st2.nextToken()+",'"+st2.nextToken()+"');";
					//	System.out.println(insertSql);
					int val = st.executeUpdate(insertSql);					
				}
				is.close();
				st.close();
			}
		} 
		catch (SQLException ex) 
		{
			System.out.println("Cannot connect to database server...!!");            
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{			
			e.printStackTrace();
		}
		finally{
			if(conn != null){
				try {
					conn.close();
					System.out.println ("Database connection terminated...!!!");
				} catch (SQLException ex) {
				}
			}
		}

	}

	public static void updateObservationsDB(String TestID,String ProjectName)
	{
		Connection conn = null;
		String dbUserName = PropertyConf.getProperty("UserName");
		String dbPassword = PropertyConf.getProperty("UserPassword");
		String dbUrl = PropertyConf.getProperty("DatabaseURL");
		String insertSql="";
		
		try {
			Class forNam = Class.forName(PropertyConf.getProperty("Driver"));
			try 
			{
				forNam.newInstance();
			} 		
			catch(Exception ae)
			{
				ae.printStackTrace();
			}
		} 
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			Statement st = conn.createStatement();
			System.out.println("Database connection establish...!");
			insertSql = "INSERT into test_obs_det_"+ProjectName+" VALUES (\""+TestID+"\", \"Please update the test observations and your analysis here.....\nand click on UPDATE RESULTS button\", \"Please update the test details and the objective of this test here.....\nand click on UPDATE RESULTS button\", \"Please update the summary of the test here.....\nand click on UPDATE RESULTS button\", \"TBD\");";                    
			int val = st.executeUpdate(insertSql);					
	
			} 
		catch (SQLException ex) 
		{
			System.out.println("Cannot connect to database server...!!");            
		}
		finally{
			if(conn != null){
				try {
					conn.close();
					System.out.println ("Database connection terminated...!!!");
				} catch (SQLException ex) {
				}
			}
		}
	}
	public static void updateReportOBStoDB(String TestID,String ProjectName,String testDetails,String testSummary,String testObservations,String testStatus)
	{
		Connection conn = null;
		String dbUserName = PropertyConf.getProperty("UserName");
		String dbPassword = PropertyConf.getProperty("UserPassword");
		String dbUrl = PropertyConf.getProperty("DatabaseURL");
		String insertSql="";
		
		try {
			Class forNam = Class.forName(PropertyConf.getProperty("Driver"));
			try 
			{
				forNam.newInstance();
			} 		
			catch(Exception ae)
			{
				ae.printStackTrace();
			}
		} 
		catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
			Statement st = conn.createStatement();
			System.out.println("Database connection establish...!");
			insertSql = "UPDATE test_obs_det_"+ProjectName+" set test_Observation=\""+testObservations+"\", test_Details=\""+testDetails+"\", test_summary=\""+testSummary+"\", test_status=\""+testStatus+"\" where Test_ID=\""+TestID+"\";";                    
			int val = st.executeUpdate(insertSql);					
			} 
		catch (SQLException ex) 
		{
			System.out.println("Cannot connect to database server...!!");            
		}
		finally{
			if(conn != null){
				try {
					conn.close();
					System.out.println ("Database connection terminated...!!!");
				} catch (SQLException ex) {
				}
			}
		}
	}
	public static String updateSteadyStateTimings(String TestID,String ProjectName)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		ResultSet lResultSet1 = null;
		String ststateStartTime="";
		String ststateEndTime="";
		String lQuery = "";
		StringBuilder lData = new StringBuilder();
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lQuery = "select scenario_elapsed_time from runningvusers_stats_"+ProjectName+"_"+TestID+" where vusers=(SELECT counter_value FROM lr_stats_details_"+ProjectName+" where Test_ID='"+TestID+"' and counter_name like '%Running Vusers%') limit 1;";
			lResultSet = lStatement.executeQuery(lQuery);
			lResultSet.next();
			ststateStartTime=lResultSet.getString(1);
			lQuery = "select scenario_elapsed_time from runningvusers_stats_"+ProjectName+"_"+TestID+" where scenario_elapsed_time>'"+ststateStartTime+"' limit 1;";
			lResultSet1 = lStatement.executeQuery(lQuery);
			lResultSet1.next();
			ststateEndTime=lResultSet1.getString(1);
			lQuery = "INSERT into lr_stats_details_"+ProjectName+" VALUES ('"+TestID+"','Steady State Start Time','"+ststateStartTime+"')";
			lStatement.executeUpdate(lQuery);
			lQuery = "INSERT into lr_stats_details_"+ProjectName+" VALUES ('"+TestID+"','Steady State End Time','"+ststateEndTime+"')";
			lStatement.executeUpdate(lQuery);
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
			catch(Exception ae){

			}
		}
		System.out.println(lData);
		return lData.toString();
	}
		
	/*
	 * DATE TIME CHANGER CODE
	 * 
	 * 
	 */
	
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

	/*
	public static void DateFormatCaller()
	{
		//ganglia
		DateformatConverter("yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "2013/12/19 04:50:36");
		//Perfmon
		DateformatConverter("MM/dd/yyyy HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "12/19/2013 04:50:36.921");
		//wily               Mon Oct 28 09:16:39 2013
		DateformatConverter("EEE MMM dd HH:mm:ss Z yyyy", "yyyy-MM-dd HH:mm:ss", "Thu Dec 19 04:50:36 BST 2013");
		
		//////Token Code
		String temp="Thu Dec 19 04:50:36 BST 2013";
		StringTokenizer stObj=new StringTokenizer(temp," ");
		int tokCount=1;
		String temp2=stObj.nextToken();
		while(stObj.hasMoreTokens())
		{
			if(tokCount==4)
			{
				stObj.nextToken();	
			}
			else
			{
				temp2=temp2+" "+stObj.nextToken();
			}			
			tokCount=tokCount+1;
		}
		System.out.println(temp2);
		//For Start End Time
		DateformatConverter("dd/MM/yyyyHH:mm:ss", "yyyy-MM-dd HH:mm:ss", "14/03/201309:52:13");
	//	System.out.println(temp+"\n"+temp.replaceAll("[BST]",""));	
	}	*/

	public static String getStartEndTimeFromLR(String filePath) throws IOException
	{
		int rownr=0,colnr = 0;
		String fileName="";
		String[] fNamearr=filesinafolder(filePath,"xls");
		for(int i=0;i<fileCounts;i++)
		{
			String fName=filePath+"\\"+fNamearr[i];
			if(fName.toLowerCase().contains("summary"))
			{
				fileName=fName;
			}
		}
		InputStream input = new FileInputStream(fileName);
		HSSFWorkbook wb = new HSSFWorkbook(input);
		HSSFSheet sheet = wb.getSheetAt(0);
		String LRStatsvaluearr = "Period";
		String LRStatscolcountarr = "0";
		rownr = findRow(sheet, LRStatsvaluearr);
		colnr = findCol(sheet, LRStatsvaluearr);
		HSSFRow row = sheet.getRow(rownr);
		colnr=colnr+Integer.parseInt(LRStatscolcountarr);
		HSSFCell cell = row.getCell(colnr);
		StringTokenizer hyphenext=new StringTokenizer(cell.toString().substring(8).replace(" ",""),"-");
		String StartTime= DateformatConverter("dd/MM/yyyyHH:mm:ss", "yyyy-MM-dd HH:mm:ss",hyphenext.nextToken());
		String EndTime= DateformatConverter("dd/MM/yyyyHH:mm:ss", "yyyy-MM-dd HH:mm:ss",hyphenext.nextToken());
		return StartTime+","+EndTime;
		//return cell.toString().substring(8);
	}
	public static String AddTimetoTimeStamp(String Format,String timestamp,int Seconds)
	{
		String TS="";
		try
		{
		SimpleDateFormat OriginalFormat = new SimpleDateFormat(Format,Locale.ENGLISH);
		Date Dt= OriginalFormat.parse(timestamp);
		Calendar cal = Calendar.getInstance();
		cal.setTime(Dt);
		cal.add(Calendar.SECOND, Seconds);
		Dt= cal.getTime();
		TS=OriginalFormat.format(Dt);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}		
		return TS;
	}
}
