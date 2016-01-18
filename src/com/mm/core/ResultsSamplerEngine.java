package com.mm.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class ResultsSamplerEngine {
	
	public static int getSteadyStateDuration(String TestID,String ProjectName)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		String steadyStStart="";
		String steadyStEnd="";
		String steadyStTokens=ResultsAnalysisEngine.getStartEndSteadyState(TestID,ProjectName);
		StringTokenizer stState= new StringTokenizer(steadyStTokens,",");
		steadyStStart=stState.nextToken();
		steadyStEnd=stState.nextToken();
		int lTable=0;
		String lQuery = "SELECT UNIX_TIMESTAMP('"+steadyStEnd+"') - UNIX_TIMESTAMP('"+steadyStStart+"') as output;";
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
			while (lResultSet.next()){
				lTableName = lResultSet.getString(1);
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
	public static double getThroughput(String ProjectName,String TestID,String StartTime,String EndTime)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		double lData=0.0;
		String lQuery="SELECT sum(Throughput) as TPT FROM throughput_stats_"+ProjectName+"_"+TestID+" where Scenario_Elapsed_Time>='"+StartTime+"' and Scenario_Elapsed_Time<='"+EndTime+"';";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				lData=Double.parseDouble(lResultSet.getString("TPT"));
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
		return lData;		
	}
	public static int getMAXVusers(String TestID,String ProjectName)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		int MAXVusers=0;
		String lTable="";
		String lQuery = "SELECT counter_value FROM lr_stats_details_"+ProjectName+" where Test_ID='"+TestID+"' and counter_name like '%Running Vusers%';";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			lResultSet.next();
			lTable=lResultSet.getString("counter_value");
			Double db= Double.parseDouble(lTable);
			MAXVusers=db.intValue();
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
		return MAXVusers;
	}
	public static double getAvgThroughputFullDuration(String TestID,String ProjectName)
	{
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;
		double lTable=0.0;
		String lQuery = "SELECT counter_value FROM lr_stats_details_"+ProjectName+" where Test_ID='"+TestID+"' and counter_name like '%Average Throughput%';";
		try{
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				lTable=Double.parseDouble(lResultSet.getString("counter_value"));
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
		return lTable;
	}	
	
	public static String getTestsHTMLFilter(String lProjectName){
		StringBuilder lData = new StringBuilder();		
		String testTokens=getTestsList(lProjectName);
		StringTokenizer stObj1= new StringTokenizer(testTokens,",");
		int tokenCount=0;
		String validTestTokens ="";
		while(stObj1.hasMoreTokens())
		{
			String TestID=stObj1.nextToken();
			String resTokens=ResultsAnalysisEngine.checkResultsExistence(TestID,lProjectName);
			if(resTokens.contains("runningvusers") & resTokens.contains("throughput"))
			{
				validTestTokens=validTestTokens+TestID+",";
			}
		}
		StringTokenizer stObj= new StringTokenizer(validTestTokens,",");
		tokenCount=stObj.countTokens();
		int [] Vuserarr = new int[tokenCount];
//		System.out.println(tokenCount);		
		if(tokenCount<4)
		{
			lData.append("<b>There should be atleast 3 tests for same scenario with different user load along with <font color=blue><i>single user test results</i></font>.<br> (<font color=blue> with <i>Running Vusers</i> and <i>Throughput</i> data</font> ) uploaded in system.</b>");			
		}
		else
		{				
			lData.append("<h4>Please Select the Tests you want correlate ( <i>Select atleast 3</i> ) ( <input type=\"checkbox\" name=\"SelectAll_Counters\" onClick=\"toggle(this)\" checked ><i><u>Toggle All</u></i> )</h4><br>");
			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466 width=90%>");
			lData.append("<tr bgcolor= #CCCCCC >");
			lData.append("<td colspan=0><b><font size=\"2\">#</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">TEST ID</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">Vusers</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">Test Start Time</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">Test End Time</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">Average Throughput(Full Duration)</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">Steady State Start Time</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">Steady State End Time</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">Steady State Duration</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">Average Throughput(Steady State)</font></b></td>");
			lData.append("<td colspan=0><b><font size=\"2\">Selection</font></b></td>");
			lData.append("</tr>");
			int hCount=1;
			int vuCount=0;
			while(stObj.hasMoreTokens())
			{
				lData.append("<tr bgcolor= #CCFFFF >");
				lData.append("<td colspan=0><font size=\"2\">"+hCount+"</font></td>");
				String TestID=stObj.nextToken();
				lData.append("<td colspan=0><font size=\"2\">"+TestID+"</font></td>");
				Vuserarr[vuCount]=getMAXVusers(TestID,lProjectName);
				lData.append("<td colspan=0><font size=\"2\">"+Vuserarr[vuCount]+"</font></td>");				
				String StTokens=ResultsAnalysisEngine.getStartEndTime(TestID,lProjectName);
				StringTokenizer st= new StringTokenizer(StTokens,",");
				String Start=st.nextToken();
				String End=st.nextToken();
				lData.append("<td colspan=0><font size=\"2\">"+Start+"</font></td>");
				lData.append("<td colspan=0><font size=\"2\">"+End+"</font></td>");				
				lData.append("<td colspan=0><font size=\"2\">"+getAvgThroughputFullDuration(TestID,lProjectName)+" (bytes/sec)</font></td>");
				String steadyStTokens=ResultsAnalysisEngine.getStartEndSteadyState(TestID,lProjectName);
				StringTokenizer stState= new StringTokenizer(steadyStTokens,",");
				String steadyStStart=stState.nextToken();
				String steadyStEnd=stState.nextToken();
				double stStateTPT=getThroughput(lProjectName,TestID,steadyStStart,steadyStEnd);
				int stStateDuration=getSteadyStateDuration(TestID,lProjectName);
				double stStateTPS=stStateTPT/stStateDuration;
				DecimalFormat f = new DecimalFormat("##.00");
				lData.append("<td colspan=0><font size=\"2\">"+steadyStStart+"</font></td>");
				lData.append("<td colspan=0><font size=\"2\">"+steadyStEnd+"</font></td>");
				lData.append("<td colspan=0><font size=\"2\">"+stStateDuration+" sec</font></td>");
				lData.append("<td colspan=0><font size=\"2\">"+f.format(stStateTPS)+" (bytes/sec)</font></td>");
				lData.append("<td colspan=0><font size=\"2\"><input type=\"checkbox\" name=\"TestID\" value=\""+TestID+"\" checked ></font></td>");
				lData.append("</tr>");
				hCount=hCount+1;
				vuCount=vuCount+1;
			}
			lData.append("</table>");
			int flag=0;
			for(int i=0;i<vuCount;i++)
			{
				if(Vuserarr[i]==1)
				{
					flag=1;
				}
			}
			System.out.println(flag);
			if(flag==1)
			{
				lData.append("<br><br><br><br><table width=\"875\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align='right' colspan='2'><INPUT TYPE=SUBMIT VALUE=\"SUBMIT\"></td></tr></table>");
			}
			else
			{
				lData.append("<br><br><br><br><table width=\"875\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align='center' colspan='2'><font color=red><b>NO RESULTS FOUND WITH SINGLE USER RUN<br>UNABLE TO PROCEED!!</b></font></td></tr></table>");
			}
		}
		return lData.toString();
	}	

	public static void getUSLCalcFile(String dirPath)
	{
		String Repo = PropertyConf.getProperty("Repository");
		String fileName="USLcalculator.xls";		
		ResultsUploaderEngine.FileFolderStruct(dirPath);
		File Source= new File(Repo+"\\"+fileName);
		File Destination= new File(dirPath+"\\"+fileName);
		try
		{
			FileUtils.copyFile(Source,Destination);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

	public static String buildSamplerResponse(String lProjectName,String TestIDTokens)
	{
		String Repo = PropertyConf.getProperty("Repository");
		String dirPath = Repo+"\\"+lProjectName;
		int noOfModelEntries=10;
		int incrementor=15;
		//GET Calculator
		getUSLCalcFile(dirPath);
		//GET VUSERS File
		getVusersinOrder(lProjectName,TestIDTokens,dirPath);
		updateSamplerFile(lProjectName,TestIDTokens,dirPath,noOfModelEntries,incrementor);
		updateQuadCoefftoFile(lProjectName,TestIDTokens,dirPath);
		String resp=buildSamplerTable(lProjectName,TestIDTokens,dirPath,noOfModelEntries,incrementor);
		ResultsUploaderEngine.DeleteFileFolder(dirPath);
		return resp;
	}

	
	public static void updateSamplerFile(String lProjectName,String TestIDTokens,String dirPath,int noOfModelEntries, int incrementor)
	{
		int rowStarter=2;
		int colStarter=0;		
		StringTokenizer testTokenizer= new StringTokenizer(TestIDTokens,",");
		int testCounts=testTokenizer.countTokens();
		int MAXVusers=0;
		String VuserFileName=dirPath+"\\vuserOrder.txt";
		String USLCalcFileName=dirPath+"\\USLcalculator.xls";
		BufferedReader is = null;
		try
		{	
			/* code for fetching values*/
			is = new BufferedReader(new FileReader(VuserFileName));
			String filein="";
			String[] TestNames=new String[testCounts];
			int[] VuserValues= new int[testCounts];
			double[] throughputValues = new double[testCounts];
			double[] tpsValues = new double[testCounts];
			int i=0;
		//	DecimalFormat f = new DecimalFormat("##.00");
			while ((filein = is.readLine()) != null)
			{
				StringTokenizer stObj= new StringTokenizer(filein,",");
				TestNames[i]=stObj.nextToken();
				Double db= Double.parseDouble(stObj.nextToken());
				VuserValues[i]=db.intValue();
				MAXVusers=VuserValues[i];
				String steadyStTokens=ResultsAnalysisEngine.getStartEndSteadyState(TestNames[i],lProjectName);
				StringTokenizer stState= new StringTokenizer(steadyStTokens,",");
				String steadyStStart=stState.nextToken();
				String steadyStEnd=stState.nextToken();
				throughputValues[i]=getThroughput(lProjectName,TestNames[i],steadyStStart,steadyStEnd);
				int stStateDuration=getSteadyStateDuration(TestNames[i],lProjectName);
				tpsValues[i]=throughputValues[i]/stStateDuration;
				i=i+1;
			}
			is.close();
			
			/* code for file update*/
	        InputStream input = new FileInputStream(USLCalcFileName);
	        HSSFWorkbook wb = new HSSFWorkbook(input);
	        HSSFSheet sheet = wb.getSheetAt(0);
	        
	        /*  This is for fisrt 2 coulmns and last 2 columns Users (N)	X(N) */
	        HSSFRow row;
	        HSSFCell cell;
			for(int leftLooper=0;leftLooper<testCounts;leftLooper++)
			{
		        colStarter=0;
		        row = sheet.getRow(rowStarter+leftLooper);
		        cell = row.getCell(colStarter);
		        if(cell == null)
		        	 cell = row.createCell(colStarter); 
		        cell.setCellType(Cell.CELL_TYPE_NUMERIC);  
		        cell.setCellValue(VuserValues[leftLooper]);
		        
		        colStarter=1;
		        row = sheet.getRow(rowStarter+leftLooper);
		        cell = row.getCell(colStarter);
		        if(cell == null)
		        	 cell = row.createCell(colStarter); 
		        cell.setCellType(Cell.CELL_TYPE_NUMERIC);  
//		        cell.setCellValue(f.format(tpsValues[leftLooper]));
		        cell.setCellValue(tpsValues[leftLooper]);
		        
		        colStarter=11;
		        row = sheet.getRow(rowStarter+leftLooper);
		        cell = row.getCell(colStarter);
		        if(cell == null)
		        	 cell = row.createCell(colStarter); 
		        cell.setCellType(Cell.CELL_TYPE_NUMERIC);  
		        cell.setCellValue(VuserValues[leftLooper]);

		        colStarter=14;
		        row = sheet.getRow(rowStarter+leftLooper);
		        cell = row.getCell(colStarter);
		        if(cell == null)
		        	 cell = row.createCell(colStarter); 
		        cell.setCellType(Cell.CELL_TYPE_NUMERIC);  
//		        cell.setCellValue(f.format(tpsValues[leftLooper]));
		        cell.setCellValue(tpsValues[leftLooper]);
			}
			/*  This is for extrapolation Users (N) in last col */
			
			rowStarter=rowStarter+testCounts;
			int Vuserinc=MAXVusers;
			for(int leftLooper=0;leftLooper<noOfModelEntries;leftLooper++)
			{
				Vuserinc=Vuserinc+incrementor;
		        colStarter=11;
		        row = sheet.getRow(rowStarter+leftLooper);
		        cell = row.getCell(colStarter);
		        if(cell == null)
		        	 cell = row.createCell(colStarter); 
		        cell.setCellType(Cell.CELL_TYPE_NUMERIC);  
		        cell.setCellValue(Vuserinc);
			}
	

	        // Write the output to a file  
	        FileOutputStream fileOut = new FileOutputStream(USLCalcFileName);  
	        wb.write(fileOut);  
	        fileOut.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void updateQuadCoefftoFile(String lProjectName,String TestIDTokens,String dirPath)
	{
		int rowStarter=2;
		int colStarter=0;		
		StringTokenizer testTokenizer= new StringTokenizer(TestIDTokens,",");
		int testCounts=testTokenizer.countTokens();
		String USLCalcFileName=dirPath+"\\USLcalculator.xls";
		try
		{	
			/* code for fetching values*/
			double[] x = new double [testCounts];
			double[] y = new double [testCounts];
			InputStream input = new FileInputStream(USLCalcFileName);
			HSSFWorkbook wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			String temp="";
			for(int leftLooper=0;leftLooper<testCounts;leftLooper++)
			{
				colStarter=5;
				temp=output(sheet, rowStarter+leftLooper, colStarter);
				x[leftLooper]=Double.parseDouble(temp);
				colStarter=6;
				temp=output(sheet, rowStarter+leftLooper, colStarter);
				y[leftLooper]=Double.parseDouble(temp);
			}
			/* CODE FOR A,B,C will come here dude!!! */
			String CoeffTokens=PolynomialRegression.Caller(x,y);
			StringTokenizer CoefTok=new StringTokenizer(CoeffTokens,",");
			String CoeffA=CoefTok.nextToken();
			String CoeffB=CoefTok.nextToken();
			System.out.println(CoeffA);
			System.out.println(CoeffB);
			HSSFRow row;
			HSSFCell cell;				        
			colStarter=8;
			//A
			row = sheet.getRow(2);
			cell = row.getCell(colStarter);
			if(cell == null)
				cell = row.createCell(colStarter); 
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);  
			cell.setCellValue(Double.parseDouble(CoeffA)); 
			//B
			row = sheet.getRow(3);
			cell = row.getCell(colStarter);
			if(cell == null)
				cell = row.createCell(colStarter); 
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);  
			cell.setCellValue(Double.parseDouble(CoeffB));
			// Write the output to a file  
			FileOutputStream fileOut = new FileOutputStream(USLCalcFileName);  
			wb.write(fileOut);  
			fileOut.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	public static void getVusersinOrder(String ProjectName,String TestTokens,String dirPath)
	{
		String outFile=dirPath+"\\vuserOrder.txt";
		String lDatabaseURL = PropertyConf.getProperty("DatabaseURL");
		String lDriver = PropertyConf.getProperty("Driver");
		String lUserName = PropertyConf.getProperty("UserName");
		String lUserPassword = PropertyConf.getProperty("UserPassword");
		Connection lConnection = null;
		Statement lStatement = null;
		ResultSet lResultSet = null;		
		int i=0;
		StringTokenizer stObj = new StringTokenizer(TestTokens,",");
		int TokenLength=stObj.countTokens();
		StringBuilder queryBuilder=new StringBuilder();
		queryBuilder.append("SELECT Test_ID, Counter_Value FROM lr_stats_details_"+ProjectName+" where test_id in (");
		while(stObj.hasMoreTokens())
		{
			if(i==TokenLength-1)
			{
				queryBuilder.append("'"+stObj.nextToken()+"'");
			}
			else
			{
				queryBuilder.append("'"+stObj.nextToken()+"',");
			}
			i=i+1;
		}
		queryBuilder.append(") and counter_name like '%Running Vusers%' order by counter_value*1;");
		String lQuery = queryBuilder.toString();
		System.out.println(lQuery);
		try{
			FileWriter bw=new FileWriter(outFile);
			Class.forName(lDriver);
			lConnection = DriverManager.getConnection(lDatabaseURL, lUserName, lUserPassword);
			lStatement = lConnection.createStatement();
			lResultSet = lStatement.executeQuery(lQuery);
			while(lResultSet.next())
			{
				bw.append(lResultSet.getString("Test_ID")+","+lResultSet.getString("Counter_Value"));
				bw.append(System.getProperty("line.separator"));
			}
			bw.close();
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
	public static String buildSamplerTable(String lProjectName,String TestIDTokens,String dirPath,int noOfModelEntries, int incrementor)
	{
		StringBuilder lData= new StringBuilder();
		StringTokenizer testTokenizer= new StringTokenizer(TestIDTokens,",");
		int testCounts=testTokenizer.countTokens();
		String VuserFileName=dirPath+"\\vuserOrder.txt";
		String USLCalcFileName=dirPath+"\\USLcalculator.xls";
		int MAXVusers=0;
		BufferedReader is = null;
		try
		{
			is = new BufferedReader(new FileReader(VuserFileName));
			String filein="";
			String[] TestNames=new String[testCounts];
			int[] VuserValues= new int[testCounts];
			double[] throughputValues = new double[testCounts];
			double[] tpsValues = new double[testCounts];
			int i=0;
			DecimalFormat f = new DecimalFormat("##.00");
			while ((filein = is.readLine()) != null)
			{
				StringTokenizer stObj= new StringTokenizer(filein,",");
				TestNames[i]=stObj.nextToken();
				Double db= Double.parseDouble(stObj.nextToken());
				VuserValues[i]=db.intValue();
				MAXVusers=VuserValues[i];
				String steadyStTokens=ResultsAnalysisEngine.getStartEndSteadyState(TestNames[i],lProjectName);
				StringTokenizer stState= new StringTokenizer(steadyStTokens,",");
				String steadyStStart=stState.nextToken();
				String steadyStEnd=stState.nextToken();
				throughputValues[i]=getThroughput(lProjectName,TestNames[i],steadyStStart,steadyStEnd);
				int stStateDuration=getSteadyStateDuration(TestNames[i],lProjectName);
				tpsValues[i]=throughputValues[i]/stStateDuration;
			//	System.out.println(throughputValues[i]+"\n"+stStateDuration+"\n"+tpsValues[i]+"\n"+i+"\n");
				i=i+1;
			 
			}
			is.close();
	        InputStream input = new FileInputStream(USLCalcFileName);
	        HSSFWorkbook wb = new HSSFWorkbook(input);
	        HSSFSheet sheet = wb.getSheetAt(0);
	        HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
//	        output(sheet, rownr, colnr);
			int rowStarter=2;
			int colStarter=0;		
        
			lData.append("<h3><font color=BLUE>DATA SAMPLING: </font>Project Name: <font color=BLUE><i>"+lProjectName+"</i></font></h3>");
			
			lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
			lData.append("<tr>");
			lData.append("<td valign=\"top\">");
			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466>");
			lData.append("<tr bgcolor= #CCCCCC >");
			lData.append("<td colspan=2 align=\"center\"><font size=\"2\"><b>1 Measurements</b></font></td>");
			lData.append("<td colspan=3 align=\"center\"><font size=\"2\"><b>2 Real Capacity</b></font></td>");
			lData.append("<td colspan=2 align=\"center\"><font size=\"2\"><b>3 Regression</b></font></td>");
			lData.append("<td colspan=2 align=\"center\"><font size=\"2\"><b>4 Coefficients</b></font></td>");
			lData.append("<td colspan=2 align=\"center\"><font size=\"2\"><b>5 Conversion</b></font></td>");
			lData.append("</tr>");
			lData.append("<tr bgcolor= #CCCCCC >");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Measured</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Throughput</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Relative Capacity</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Efficiency</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Inverse</b></font></td>");
			lData.append("<td colspan=2 align=\"center\"><font size=\"2\"><b>Linear Deviation</b></font></td>");
			lData.append("<td colspan=2 align=\"center\"><font size=\"2\"><b>TrendLine Coefficients</b></font></td>");
			lData.append("<td colspan=2 align=\"center\"><font size=\"2\"><b>Super Serial</b></font></td>");
			lData.append("</tr>");
			lData.append("<tr bgcolor= #CCCCCC >");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Users(N)</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>X(N)</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>C(N)=X(N)/X(1)</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>C/N</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>N/C</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>N-1</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>(N/C)-1</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Coefficient</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Value</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Parameter</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Value</b></font></td>");
			lData.append("</tr>");
			StringBuilder lGraph_LD = new StringBuilder();
			StringBuilder Categories_LD = new StringBuilder();
			StringBuilder Dataseries_LD = new StringBuilder();
			lGraph_LD.append("<chart palette='4' caption='Linear deviation trend line (parabola)' xAxisName='Effective Users (N- 1)'  yAxisName='Deviation'  numdivlines='4' lineThickness='2' showValues='0' formatNumberScale='1' decimals='1' anchorRadius='2' shadowAlpha='50' shownames='1' showBorder='1' alternateHGridColor='FCB541' alternateHGridAlpha='20' divLineColor='FCB541' divLineAlpha='50' canvasBorderColor='666666' lineColor='FCB541' useRoundEdges='1'>");
			Categories_LD.append("<categories>");
			Dataseries_LD.append("<dataset seriesName='Deviation' color='A66EDD' anchorBorderColor='A66EDD' anchorRadius='4'>");
			String NMax="";
			String TpTMax="";			
			for(int leftLooper=0;leftLooper<testCounts;leftLooper++)
			{
				lData.append("<tr>");
				lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+VuserValues[leftLooper]+"</font></td>");
				lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+f.format(tpsValues[leftLooper])+"</font></td>");
				colStarter=2;
				lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+output(sheet, rowStarter+leftLooper, colStarter)+"</font></td>");
				colStarter=3;
				lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+output(sheet, rowStarter+leftLooper, colStarter)+"</font></td>");
				colStarter=4;
				lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+output(sheet, rowStarter+leftLooper, colStarter)+"</font></td>");
				colStarter=5;
				Categories_LD.append("<category label='"+(VuserValues[leftLooper]-1)+"'/>");
				lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+(VuserValues[leftLooper]-1)+"</font></td>");
				colStarter=6;
				String temp=output(sheet, rowStarter+leftLooper, colStarter);
				Dataseries_LD.append("<set value='"+temp+"'/>");
				lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+temp+"</font></td>");
				if(leftLooper==0)
				{
					lData.append("<td colspan=0 align=\"center\" bgcolor= #F5D886 ><font size=\"2\">a</font></td>");
					colStarter=8;
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+output(sheet, rowStarter+leftLooper, colStarter)+"</font></td>");
					lData.append("<td colspan=0 align=\"center\" bgcolor= #94F586 ><font size=\"2\"><b>&alpha;</b></font></td>");
					colStarter=10;
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+output(sheet, rowStarter+leftLooper, colStarter)+"</font></td>");
				}
				else if(leftLooper==1)
				{
					lData.append("<td colspan=0 align=\"center\" bgcolor= #F5D886 ><font size=\"2\">b</font></td>");
					colStarter=8;
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+output(sheet, rowStarter+leftLooper, colStarter)+"</font></td>");
					lData.append("<td colspan=0 align=\"center\" bgcolor= #94F586 ><font size=\"2\"><b>&beta;</b></font></td>");
					colStarter=10;
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+output(sheet, rowStarter+leftLooper, colStarter)+"</font></td>");
				}
				else if(leftLooper==2)
				{
					lData.append("<td colspan=0 align=\"center\" bgcolor= #F5D886 ><font size=\"2\">c</font></td>");
					colStarter=8;
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+output(sheet, rowStarter+leftLooper, colStarter)+"</font></td>");
					lData.append("<td colspan=0 align=\"center\" bgcolor= #86F5EC ><font size=\"2\">N (Max)</font></td>");
					colStarter=10;					
					StringTokenizer st= new StringTokenizer(output(sheet, rowStarter+leftLooper, colStarter),".");
					NMax=st.nextToken();
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+NMax+"</font></td>");
				}
				else if(leftLooper==3)
				{
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">&nbsp;</font></td>");
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">&nbsp;</font></td>");
					lData.append("<td colspan=0 align=\"center\" bgcolor= #86F5EC ><font size=\"2\">N (Opt)</font></td>");
					colStarter=10;
					StringTokenizer st= new StringTokenizer(output(sheet, rowStarter+leftLooper, colStarter),".");
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+st.nextToken()+"</font></td>");
				}
				else
				{
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">&nbsp;</font></td>");
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">&nbsp;</font></td>");
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">&nbsp;</font></td>");
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">&nbsp;</font></td>");
				}		
				lData.append("</tr>");
			}
			Categories_LD.append("</categories>");
			Dataseries_LD.append("</dataset>");
			lGraph_LD.append(Categories_LD.toString());
			lGraph_LD.append(Dataseries_LD.toString());
			lGraph_LD.append("</chart>");
			
			lData.append("</table>");
			lData.append("</td>");

			lData.append("<td valign=\"top\">");
			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466>");
			lData.append("<tr bgcolor= #CCCCCC >");
			lData.append("<td colspan=5 align=\"center\"><font size=\"2\"><b>Predicted Capacity</b></font></td>");
			lData.append("</tr>");
			lData.append("<tr bgcolor= #CCCCCC >");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Users</b></font></td>");
			lData.append("<td colspan=2 align=\"center\"><font size=\"2\"><b>Modeled</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Measured</b></font></td>");
			lData.append("<td rowspan=2 align=\"center\"><font size=\"2\"><b>Scale Factor</b></font></td>");
			lData.append("</tr>");
			lData.append("<tr bgcolor= #CCCCCC >");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>(N)</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Capacity C(N)</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Throughput X(N)</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Throughput X(N)</b></font></td>");
			lData.append("</tr>");
			int Vuserinc=MAXVusers;
			
			StringBuilder lGraph_PC = new StringBuilder();
			StringBuilder Categories_PC = new StringBuilder();
			StringBuilder DataseriesME_PC = new StringBuilder();
			StringBuilder DataseriesMO_PC = new StringBuilder();
			lGraph_PC.append("<chart palette='4' caption='Projected Scalability' xAxisName='Virtual users (N)'  yAxisName='Throughput(X)' numdivlines='4' lineThickness='2' showValues='0' formatNumberScale='1' decimals='1' anchorRadius='2' shadowAlpha='50' shownames='1' showBorder='1' alternateHGridColor='FCB541' alternateHGridAlpha='20' divLineColor='FCB541' divLineAlpha='50' canvasBorderColor='666666' lineColor='FCB541' useRoundEdges='1'>");
			Categories_PC.append("<categories>");
			DataseriesME_PC.append("<dataset seriesName='Measured (X)' color='A66EDD' anchorBorderColor='A66EDD' anchorRadius='4'>");
			DataseriesMO_PC.append("<dataset seriesName='Modeled (X)' color='F6BD0F' anchorBorderColor='F6BD0F' anchorRadius='4'>");

			for(int leftLooper=0;leftLooper<noOfModelEntries;leftLooper++)
			{
		        lData.append("<tr>");
		        double nume=0.0;
		        int deno=0;
		        if(leftLooper<testCounts)
		        {					
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+VuserValues[leftLooper]+"</font></td>");
					deno=VuserValues[leftLooper];
					Categories_PC.append("<category label='"+deno+"'/>");
		        }
		        else
		        {
					Vuserinc=Vuserinc+incrementor;
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+Vuserinc+"</font></td>");
					deno=Vuserinc;
					Categories_PC.append("<category label='"+deno+"'/>");
		        }
		        colStarter=12;
		        nume= Double.parseDouble(output(sheet, rowStarter+leftLooper, colStarter));
				lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+output(sheet, rowStarter+leftLooper, colStarter)+"</font></td>");
		        colStarter=13;
		        String temp2=output(sheet, rowStarter+leftLooper, colStarter);
				lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+temp2+"</font></td>");
				DataseriesMO_PC.append("<set value='"+temp2+"'/>");
		        if(leftLooper<testCounts)
		        {
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">"+f.format(tpsValues[leftLooper])+"</font></td>");
					DataseriesME_PC.append("<set value='"+f.format(tpsValues[leftLooper])+"'/>");
		        }
		        else
		        {
					lData.append("<td colspan=0 align=\"center\"><font size=\"2\">&nbsp;</font></td>");
					DataseriesME_PC.append("<set value=''/>");
		        }
		        double value=(nume*100)/deno;
				lData.append("<td colspan=0 bgcolor= #"+getColorCode(value)+" align=\"center\"><font size=\"2\">"+f.format(value)+"%</font></td>");
				lData.append("</tr>");
			}
			TpTMax=output(sheet,38,13);
//			System.out.println(output(sheet,38,13)+"\n"+output(sheet,38,12)+"\n"+output(sheet,38,11));
			Categories_PC.append("</categories>");
			DataseriesME_PC.append("</dataset>");
			DataseriesMO_PC.append("</dataset>");
			lGraph_PC.append(Categories_PC.toString());
			lGraph_PC.append(DataseriesME_PC.toString());
			lGraph_PC.append(DataseriesMO_PC.toString());
			lGraph_PC.append("</chart>");

			lData.append("</table>");
			lData.append("</td>");			
			lData.append("</tr>");			
			lData.append("</table>");
			lData.append("<table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466>");
			lData.append("<tr>");
			lData.append("<td bgcolor= #CCCCCC colspan=0 align=\"left\"><font size=\"2\"><b>Load (N<sub>Max</sub>)</b></font></td>");
			lData.append("<td bgcolor= #86F5EC colspan=0 align=\"center\"><font size=\"2\">"+NMax+"</font></td>");
			lData.append("</tr>");			
			lData.append("<tr>");
//			lData.append("<td bgcolor= #CCCCCC colspan=0 align=\"center\"><font size=\"2\"><b>Throughput (X<sub>Max</sub>) at load (N<sub>Max</sub>)</b></font></td>");
			lData.append("<td bgcolor= #CCCCCC colspan=0 align=\"left\"><font size=\"2\"><b>Throughput (X<sub>Max</sub>)</b></font></td>");
			lData.append("<td bgcolor= #86F5EC colspan=0 align=\"center\"><font size=\"2\">"+TpTMax+" (bytes/sec)</font></td>");
			lData.append("</tr>");			
			lData.append("</table>");

			int counter=0;
			String chartDataXML=lGraph_LD.toString();
			lData.append("<table cellpadding=\"0\" cellspacing=\"0\" border=\"1\">");
			lData.append("<tr bgcolor= #CCCCCC >");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Linear deviation</b></font></td>");
			lData.append("<td colspan=0 align=\"center\"><font size=\"2\"><b>Projected Scalability</b></font></td>");
			lData.append("</tr>");			
			lData.append("<tr>");
			lData.append("<td><div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
			lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/MSSpline.swf\", \"myChartId_"+counter+"\", \"600\", \"300\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
			lData.append("</td>");
			counter=1;
			chartDataXML=lGraph_PC.toString();
			lData.append("<td><div id=\"chartdiv_"+counter+"\" align=\"center\">The chart will appear within this DIV. This text will be replaced by the chart.</div>");
			lData.append("<script type=\"text/javascript\">var myChart_"+counter+" = new FusionCharts(\"../FusionCharts/MSSpline.swf\", \"myChartId_"+counter+"\", \"600\", \"300\", \"0\", \"1\");myChart_"+counter+".setDataXML(\""+chartDataXML+"\");myChart_"+counter+".render(\"chartdiv_"+counter+"\");</script>");
			lData.append("</td>");
			lData.append("</tr>");			
			lData.append("</table>");
			
			
			lData.append("<br><table cellspacing= 0 cellpadding= 4 border= 2  bordercolor= #224466>");
/*			lData.append("<tr>");
			lData.append("<td colspan=2 align=\"center\" bgcolor= #CCCCCC ><font size=\"2\"><b>Variables</b></font></td>");
			lData.append("</tr>");
*/
			lData.append("<tr>");
			lData.append("<td colspan=0 align=\"left\" bgcolor= #94F586 ><font size=\"2\"><b>Alpha(&alpha;)</b></font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">Contention Coefficient.</font></td>");
			lData.append("</tr>");			
			lData.append("<tr>");
			lData.append("<td colspan=0 align=\"left\" bgcolor= #94F586 ><font size=\"2\"><b>Beta(&beta;)</b></font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">Coherency Coefficient.</font></td>");
			lData.append("</tr>");	
			lData.append("<tr>");
			lData.append("<td colspan=0 align=\"left\" bgcolor= #94F586 ><font size=\"2\"><b>N</b></font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">User Load</font></td>");
			lData.append("</tr>");
			lData.append("<tr>");
			lData.append("<td colspan=0 align=\"left\" bgcolor= #94F586 ><font size=\"2\"><b>X</b></font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">Throughput</font></td>");
			lData.append("</tr>");
			lData.append("<tr>");
			lData.append("<td colspan=0 align=\"left\" bgcolor= #94F586 ><font size=\"2\"><b>C(N)</b></font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">Capacity for user load (N)</font></td>");
			lData.append("</tr>");				
			lData.append("<tr>");
			lData.append("<td colspan=0 align=\"left\" bgcolor= #94F586 ><font size=\"2\"><b>a b c</b></font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">Quadratic Coefficients (Trendline Parameters)</font></td>");
			lData.append("</tr>");
			lData.append("<tr>");
			lData.append("<td colspan=0 align=\"left\" bgcolor= #94F586 ><font size=\"2\"><b>Load (N<sub>Max</sub>)</b></font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">User Load at which the maximum scalability will occur.</font></td>");
			lData.append("</tr>");
			lData.append("<tr>");
			lData.append("<td colspan=0 align=\"left\" bgcolor= #94F586 ><font size=\"2\"><b>Throughput (X<sub>Max</sub>)</b></font></td>");
			lData.append("<td colspan=0 align=\"left\"><font size=\"2\">Corresponding Throughput X<sub>Max</sub> at load N<sub>Max</sub>.</font></td>");
			lData.append("</tr>");			
			lData.append("</table>");			

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return lData.toString();
	}
    private static String output(HSSFSheet sheet, int rownr, int colnr)
    {
        HSSFRow row = sheet.getRow(rownr);
        HSSFCell cell = row.getCell(colnr);
        DecimalFormat f = new DecimalFormat("#0.0000");
        String op="NV";
        if(cell.getCellType()== Cell.CELL_TYPE_FORMULA)
        {
 //           System.out.println("Formula is " + cell.getCellFormula());
            switch(cell.getCachedFormulaResultType()) {
                case Cell.CELL_TYPE_NUMERIC:
                	op=f.format(cell.getNumericCellValue());
 //               	op=cell.getNumericCellValue()+"";
                    break;
                case Cell.CELL_TYPE_STRING:
                	op=cell.getRichStringCellValue().toString();                	
                    break;
            }
         }
        else if(cell.getCellType()== Cell.CELL_TYPE_NUMERIC)
        {
        	DecimalFormat g = new DecimalFormat("#0.00000");
        	op=g.format(cell.getNumericCellValue());
        }
        return op;
    }
	
    public static String getColorCode(double value)
    {
    	String colCode="";
    	if(value>=90.0&value<=100.0)
    	{colCode="76DD3E";}
    	else if(value>=80.0&value<90.0)
    	{colCode="A3DA4A";}
    	else if(value>=70.0&value<80.0)
    	{colCode="B9E953";}
    	else if(value>=60.0&value<70.0)
    	{colCode="DBF563";}
    	else if(value>=50.0&value<60.0)
    	{colCode="E4F54C";}
    	else if(value>=40.0&value<50.0)
    	{colCode="F7EB43";}
    	else if(value>=30.0&value<40.0)
    	{colCode="EEAB39";}
    	else if(value>=20.0&value<30.0)
    	{colCode="EE5139";}
    	else if(value>=10.0&value<20.0)
    	{colCode="FB3232";}
    	else if(value>=0.0&value<10.0)
    	{colCode="F30D0D";}    	
    	return colCode;    	
    }
}
