package com.mm.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

public class MiniAppsEngine {
	
	// Redirection control functions	
	public static String GenerateMiniAppPage(String appToken)
	{
		String lResult="";
		if(appToken.equals("infosess"))
		{
			lResult=GenerateInfoSessMiniAppPage();
		}
		else if(appToken.equals("dbsnpst"))
		{
			lResult=GenerateDBSPMiniAppPage();
		}
		else if(appToken.equals("mysqlslow"))
		{
			lResult="";
		}			
		return lResult;
	}
	public static String GenerateMiniAppUploaderPage(String appToken)
	{
		String lResult="";
		if(appToken.equals("infosess"))
		{
			lResult=GenerateInfoSessUploaderPage();
		}
		else if(appToken.equals("dbsnpst"))
		{
			lResult=GenerateDBSPUploaderPage();
		}
		return lResult;
	}	
	public static String GenerateMiniAppProcessorPage(String ProjectName,String TestID, String appToken)
	{
		String lResult="";
		if(appToken.equals("infosess"))
		{
			lResult=GenerateInfoSessProcessorPage(ProjectName,TestID);
		}
		else if(appToken.equals("dbsnpst"))
		{
			lResult=GenerateDBSPProcessorPage(ProjectName,TestID);
		}
		return lResult;
	}	
	public static void GenerateMiniAppPDFReport(String ProjectName,String TestID, String appToken)
	{
		if(appToken.equals("infosessrep"))
		{
			GenerateInfoSessPDFReport(ProjectName,TestID);
		}
	}	

	
	
	
	

	
	// Main Page Loader Functions
	public static String GenerateInfoSessMiniAppPage()
	{
		StringBuilder lResult= new StringBuilder();
		lResult.append("<script language=\"JavaScript\">function validateForm(){if(document.resUploadForm.Field_prjName.value==0){alert(\"Please Enter Project Name.\");document.resUploadForm.Field_prjName.focus();return false;}if(document.resUploadForm.Field_testID.value==0){alert(\"Please Enter Test ID.\");document.resUploadForm.Field_testID.focus();return false;}}</script>");
		lResult.append("<form name=\"resUploadForm\" action=\"/AdministratorModule/jsp/MiniAppsProcessor.jsp?appToken=infosess&stageToken=uploader\"	METHOD=\"POST\" onSubmit=\"return validateForm()\">");
		lResult.append("<table width=\"875\" align=\"center\" cellpadding=\"6\" cellspacing=\"6\" border=\"0\"><tr><td align=\"left\">Project Name</td><td><input type=\"text\" name=\"Field_prjName\" size=\"60\"></input></td></tr><tr><td align=\"left\">Test ID</td><td><input type=\"text\" name=\"Field_testID\" size=\"60\"></input></td></tr><tr><td align=\"left\" colspan='2'><br></td></tr><tr><td align='center' colspan='2'><br><br><br><br><INPUT TYPE=SUBMIT VALUE=\"NEXT >>\"></td></tr></table><div id=\"MESSAGEDIV\"></div></form>");
		return lResult.toString();
	}
	public static String GenerateDBSPMiniAppPage()
	{
		StringBuilder lResult= new StringBuilder();
		lResult.append("<script language=\"JavaScript\">function validateForm(){if(document.resUploadForm.Field_prjName.value==0){alert(\"Please Enter Project Name.\");document.resUploadForm.Field_prjName.focus();return false;}if(document.resUploadForm.Field_testID.value==0){alert(\"Please Enter Test ID.\");document.resUploadForm.Field_testID.focus();return false;}}</script>");
		lResult.append("<form name=\"resUploadForm\" action=\"/AdministratorModule/jsp/MiniAppsProcessor.jsp?appToken=dbsnpst&stageToken=uploader\"	METHOD=\"POST\" onSubmit=\"return validateForm()\">");
		lResult.append("<table width=\"875\" align=\"center\" cellpadding=\"6\" cellspacing=\"6\" border=\"0\"><tr><td align=\"left\">Project Name</td><td><input type=\"text\" name=\"Field_prjName\" size=\"60\"></input></td></tr><tr><td align=\"left\">Test ID</td><td><input type=\"text\" name=\"Field_testID\" size=\"60\"></input></td></tr><tr><td align=\"left\" colspan='2'><br></td></tr><tr><td align='center' colspan='2'><br><br><br><br><INPUT TYPE=SUBMIT VALUE=\"NEXT >>\"></td></tr></table><div id=\"MESSAGEDIV\"></div></form>");
		return lResult.toString();
	}

	
	
	
	// Uploader functions
	public static String GenerateInfoSessUploaderPage()
	{
		StringBuilder lResult= new StringBuilder();
		lResult.append("<script language=\"JavaScript\">function validateForm(){if(document.uploaderForm.file.value==0){alert(\"Please upload a zip file.\");document.uploaderForm.file.focus();return false;}}</script>");
		lResult.append("<form name=\"uploaderForm\" action=\"/AdministratorModule/jsp/MiniAppUpload.jsp\"	METHOD=\"POST\" onSubmit=\"return validateForm()\" enctype=\"multipart/form-data\"><basefont face=\"calibri\">");
		lResult.append("<br><br><br><table width=\"875\" align=\"center\" cellpadding=\"6\" cellspacing=\"6\" border=\"0\"><tr><td align=\"left\" colspan=\"2\"><b>Note:</b> Please upload the zip file of the informatica session logs test results.<br></td></tr><tr><td align=\"left\">Upload zip file</td><td><input type=\"file\" name=\"file\" id=\"file\" size=\"60\" /> <br /></td></tr><tr><td align='center' colspan='2'><br><br><br><br><INPUT TYPE=SUBMIT VALUE=\"UPLOAD\"></td></tr></table><div id=\"MESSAGEDIV\"></div><br></form>");
		return lResult.toString();
	}
	public static String GenerateDBSPUploaderPage()
	{
		StringBuilder lResult= new StringBuilder();
		lResult.append("<script language=\"JavaScript\">function validateForm(){if(document.uploaderForm.file.value==0){alert(\"Please upload a zip file.\");document.uploaderForm.file.focus();return false;}}</script>");
		lResult.append("<form name=\"uploaderForm\" action=\"/AdministratorModule/jsp/MiniAppUpload.jsp\"	METHOD=\"POST\" onSubmit=\"return validateForm()\" enctype=\"multipart/form-data\"><basefont face=\"calibri\">");
		lResult.append("<br><br><br><table width=\"875\" align=\"center\" cellpadding=\"6\" cellspacing=\"6\" border=\"0\"><tr><td align=\"left\" colspan=\"2\"><b>Note:</b> Please upload the zip file of the DB2 snapshots.<br></td></tr><tr><td align=\"left\">Upload zip file</td><td><input type=\"file\" name=\"file\" id=\"file\" size=\"60\" /> <br /></td></tr><tr><td align='center' colspan='2'><br><br><br><br><INPUT TYPE=SUBMIT VALUE=\"UPLOAD\"></td></tr></table><div id=\"MESSAGEDIV\"></div><br></form>");
		return lResult.toString();
	}
	
	
	
	
	// Processor functions
	public static String GenerateInfoSessProcessorPage(String ProjectName,String TestID)
	{
		StringBuilder lResult= new StringBuilder();
		String filePath = PropertyConf.getProperty("FileDir")+"\\"+ProjectName+"_"+TestID;
		String[] fNamearr = ResultsUploaderEngine.filesinafolder(filePath,"zip");
		ResultsUploaderEngine.doUnzipFiles(filePath+"\\"+fNamearr[0],filePath);
		ResultsUploaderEngine.deleteFiles(filePath,"zip");
		int fileCounts = filesinafolder(filePath,"log");
		String[] logNamearr = ResultsUploaderEngine.filesinafolder(filePath,"log");
		String StartTime="";
		String EndTime="";
		String WorkFlowName="";
		String SessionName="";
		lResult.append("The Output of Informatica Session Logs for PROJECT NAME:  <b>"+ProjectName+"</b> TEST ID: <b>"+TestID+"</b><br><hr>");
		lResult.append("<table align=\"center\" cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"3\">WorkFlow Name</font></b></td><td colspan=0><b><font size=\"3\">Session Name</font></b></td><td colspan=0><b><font size=\"3\">Start Time</font></b></td><td colspan=0><b><font size=\"3\">End Time</font></b></td><td colspan=0><b><font size=\"3\">Elapsed Time</font></b></td></tr>");
		int successLogs=0,FailLogs=0,TotalLogs=0;
		for(int i=0;i<fileCounts;i++)
		{
			String temp = GetInfoSessDetails(filePath+"\\"+logNamearr[i]);
			StringTokenizer StObj1= new StringTokenizer(temp,"*");
			WorkFlowName=StObj1.nextToken();
			SessionName=StObj1.nextToken();
			StartTime=StObj1.nextToken();
			EndTime=StObj1.nextToken();
			int duration=0;
			if(StartTime.equals("STNF"))
			{
				StartTime="Start Time Not Found";
				TotalLogs=TotalLogs+1;
				FailLogs=FailLogs+1;
			}
			else if(EndTime.equals("ETNF"))
			{
				EndTime="End Time Not Found";
				TotalLogs=TotalLogs+1;
				FailLogs=FailLogs+1;
			}
			else
			{
				duration=getDuration(StartTime,EndTime);
				TotalLogs=TotalLogs+1;
				successLogs=successLogs+1;
			}			
			lResult.append("<tr bgcolor= #CCFFFF >");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+WorkFlowName.toUpperCase()+"</font></td>");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+SessionName.toUpperCase()+"</font></td>");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+StartTime+"</font></td>");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+EndTime+"</font></td>");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+duration+" (seconds)</font></td>");
			lResult.append("</tr>");
		}
		lResult.append("</table><br>");
		lResult.append("<u><b>Success Details</b></u><br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"3\">Session Logs</font></b></td><td colspan=0><b><font size=\"3\">Total Count</font></b></td></tr>");
		lResult.append("<tr bgcolor= #CCFFFF >");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">Success Logs</font></td>");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+successLogs+"</font></td>");
		lResult.append("</tr>");
		lResult.append("<tr bgcolor= #CCFFFF >");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">Error Logs</font></td>");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+FailLogs+"</font></td>");
		lResult.append("</tr>");
		lResult.append("<tr bgcolor= #CCFFFF >");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">Total Logs</font></td>");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+TotalLogs+"</font></td>");
		lResult.append("</tr>");		
		lResult.append("</table><br>");

		
		StringBuilder htmlGen= new StringBuilder();
		htmlGen.append("<html><body><basefont face=\"calibri\"><h2><font color=BLUE>Mini Apps</font></h2><br>");
		htmlGen.append(lResult.toString());
		htmlGen.append("</body></html>");
		String imgDir=PropertyConf.getProperty("ImageDir");
		String outfile=imgDir+"\\"+ProjectName+"_"+TestID+"_"+"Report.html";
		try {
			FileWriter bw=new FileWriter(outfile);
			bw.append(htmlGen.toString());
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResultsUploaderEngine.deleteFiles(filePath,"log");
		return lResult.toString();
	}	
	public static String GetInfoSessDetails(String filename)
	{
		StringBuilder lResult= new StringBuilder();
		String StartTime="";
		String EndTime="";
		String WorkFlowName="";
		String SessionName="";
		BufferedReader is = null;
		try
		{
			is = new BufferedReader(new FileReader(filename));
			String filein;
			while ((filein = is.readLine()) != null)
			{
				if(filein.contains("TM_6014"))
				{
					StringTokenizer StObj= new StringTokenizer(filein,"[");
					StObj.nextToken();
					String temp=StObj.nextToken();
					StringTokenizer StObj1= new StringTokenizer(temp,"]");
					SessionName=StObj1.nextToken();
					temp=StObj.nextToken();
					StartTime=temp.replace("]","").replace(".","");
				}
				else if(filein.contains("TM_6685"))
				{
					StringTokenizer StObj= new StringTokenizer(filein,"[");
					StObj.nextToken();
					String temp=StObj.nextToken();
					StringTokenizer StObj1= new StringTokenizer(temp,"]");
					WorkFlowName=StObj1.nextToken();
				}
				else if(filein.contains("TM_6020"))
				{
					StringTokenizer StObj= new StringTokenizer(filein,"[");
					StObj.nextToken();
					StObj.nextToken();
					EndTime=StObj.nextToken().replace("]","").replace(".","");
				}					
			}
			is.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{			
			e.printStackTrace();
		}
		if(StartTime.equals(""))
		{
			StartTime="STNF";
		}
		if(EndTime.equals(""))
		{
			EndTime="ETNF";
		}
		lResult.append(WorkFlowName+"*");
		lResult.append(SessionName+"*");
		lResult.append(StartTime+"*");
		lResult.append(EndTime);
		System.out.println(lResult.toString());
		return lResult.toString();
	}	

	public static String GenerateDBSPProcessorPage(String ProjectName,String TestID)
	{
		StringBuilder lResult= new StringBuilder();
		String filePath = PropertyConf.getProperty("FileDir")+"\\"+ProjectName+"_"+TestID;
		String[] fNamearr = ResultsUploaderEngine.filesinafolder(filePath,"zip");
		ResultsUploaderEngine.doUnzipFiles(filePath+"\\"+fNamearr[0],filePath);
		ResultsUploaderEngine.deleteFiles(filePath,"zip");
		int fileCounts = filesinafolder(filePath,"log");
		String[] logNamearr = ResultsUploaderEngine.filesinafolder(filePath,"log");
		String StartTime="";
		String EndTime="";
		String WorkFlowName="";
		String SessionName="";
		lResult.append("The Output of Informatica Session Logs for PROJECT NAME:  <b>"+ProjectName+"</b> TEST ID: <b>"+TestID+"</b><br><hr>");
		lResult.append("<table align=\"center\" cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"3\">WorkFlow Name</font></b></td><td colspan=0><b><font size=\"3\">Session Name</font></b></td><td colspan=0><b><font size=\"3\">Start Time</font></b></td><td colspan=0><b><font size=\"3\">End Time</font></b></td><td colspan=0><b><font size=\"3\">Elapsed Time</font></b></td></tr>");
		int successLogs=0,FailLogs=0,TotalLogs=0;
		for(int i=0;i<fileCounts;i++)
		{
			String temp = GetInfoSessDetails(filePath+"\\"+logNamearr[i]);
			StringTokenizer StObj1= new StringTokenizer(temp,"*");
			WorkFlowName=StObj1.nextToken();
			SessionName=StObj1.nextToken();
			StartTime=StObj1.nextToken();
			EndTime=StObj1.nextToken();
			int duration=0;
			if(StartTime.equals("STNF"))
			{
				StartTime="Start Time Not Found";
				TotalLogs=TotalLogs+1;
				FailLogs=FailLogs+1;
			}
			else if(EndTime.equals("ETNF"))
			{
				EndTime="End Time Not Found";
				TotalLogs=TotalLogs+1;
				FailLogs=FailLogs+1;
			}
			else
			{
				duration=getDuration(StartTime,EndTime);
				TotalLogs=TotalLogs+1;
				successLogs=successLogs+1;
			}			
			lResult.append("<tr bgcolor= #CCFFFF >");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+WorkFlowName.toUpperCase()+"</font></td>");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+SessionName.toUpperCase()+"</font></td>");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+StartTime+"</font></td>");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+EndTime+"</font></td>");
			lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+duration+" (seconds)</font></td>");
			lResult.append("</tr>");
		}
		lResult.append("</table><br>");
		lResult.append("<u><b>Success Details</b></u><br><br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466><tr bgcolor= #CCCCCC ><td colspan=0><b><font size=\"3\">Session Logs</font></b></td><td colspan=0><b><font size=\"3\">Total Count</font></b></td></tr>");
		lResult.append("<tr bgcolor= #CCFFFF >");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">Success Logs</font></td>");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+successLogs+"</font></td>");
		lResult.append("</tr>");
		lResult.append("<tr bgcolor= #CCFFFF >");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">Error Logs</font></td>");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+FailLogs+"</font></td>");
		lResult.append("</tr>");
		lResult.append("<tr bgcolor= #CCFFFF >");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">Total Logs</font></td>");
		lResult.append("<td colspan=0 align=\"left\"><font size=\"2\">"+TotalLogs+"</font></td>");
		lResult.append("</tr>");		
		lResult.append("</table><br>");

		
		StringBuilder htmlGen= new StringBuilder();
		htmlGen.append("<html><body><basefont face=\"calibri\"><h2><font color=BLUE>Mini Apps</font></h2><br>");
		htmlGen.append(lResult.toString());
		htmlGen.append("</body></html>");
		String imgDir=PropertyConf.getProperty("ImageDir");
		String outfile=imgDir+"\\"+ProjectName+"_"+TestID+"_"+"Report.html";
		try {
			FileWriter bw=new FileWriter(outfile);
			bw.append(htmlGen.toString());
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResultsUploaderEngine.deleteFiles(filePath,"log");
		return lResult.toString();
	}


	
	
	
	
	// Report Generation Functions
	public static void GenerateInfoSessPDFReport(String ProjectName,String TestID)
	{
			String imgDir=PropertyConf.getProperty("ImageDir");
			String repDir=PropertyConf.getProperty("ReportDir");
			try {
				FileWriter bw2=new FileWriter(imgDir+"\\"+ProjectName+"_"+TestID+"_"+"Report.bat");
				bw2.append("cd "+imgDir);
				bw2.append(System.getProperty("line.separator"));
				bw2.append("topdf "+ProjectName+"_"+TestID+"_"+"Report.html "+repDir+"\\"+ProjectName+"_"+TestID+"_"+"Report.pdf");
				bw2.append(System.getProperty("line.separator"));
				bw2.append("exit");
				bw2.close();

				//  topdf  will come here....
				String[] command = {"cmd.exe", "/C", "Start", imgDir+"\\"+ProjectName+"_"+TestID+"_"+"Report.bat"};            
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
		 		ReportGenerate.deleteFiles(imgDir,ProjectName+"_"+TestID);

			} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static int filesinafolder(String fold,String ext)
	{
		int fileCounts=0;
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
						fileCounts =fileCounts+1;   
					}	
				}  
			}
		}		
		return fileCounts;
	}
	public static int getDuration(String steadyStStart,String steadyStEnd)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		int lTable=0;
		String StartTime=ResultsUploaderEngine.DateformatConverter("EEE MMM dd HH:mm:ss yyyy","yyyy-MM-dd HH:mm:ss",steadyStStart);
		String EndTime=ResultsUploaderEngine.DateformatConverter("EEE MMM dd HH:mm:ss yyyy","yyyy-MM-dd HH:mm:ss",steadyStEnd);
		String lQuery = "SELECT UNIX_TIMESTAMP('"+EndTime+"') - UNIX_TIMESTAMP('"+StartTime+"') as output;";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				lTable=Integer.parseInt(lResultSet.getString("output"));
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
	//	System.out.println(lTable);
		return lTable;
	}
}
