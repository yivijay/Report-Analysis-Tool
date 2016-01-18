<%@page import="com.mm.core.ResultExtractEngine"%>
<%@page import="com.mm.core.ReportGenerate"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.mm.core.PropertyConf"%>
<html>
<head>
<title>Results Dashboard</title>
<script>
		function getpopup(PrjName,tstID,grType){
		if(grType=="lrstats")
		{
		var url="/AdministratorModule/jsp/GraphPlots.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&grType="+grType;
		popupWindow = window.open(url,'popUpWindow','height=500,width=1200,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}		
		if(grType=="wily")
		{
		var url="/AdministratorModule/jsp/GraphPlots.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&grType="+grType;
		popupWindow = window.open(url,'popUpWindow','height=500,width=1200,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}
		if(grType=="ganglia")
		{
		var url="/AdministratorModule/jsp/GraphPlots.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&grType="+grType;
		popupWindow = window.open(url,'popUpWindow','height=500,width=1200,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}
		if(grType=="perfmon")
		{
		var url="/AdministratorModule/jsp/GraphPlots.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&grType="+grType;
		popupWindow = window.open(url,'popUpWindow','height=500,width=1200,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}				
		if(grType=="publish")
		{
		var url="/AdministratorModule/jsp/GraphPlots.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&grType="+grType;
		popupWindow = window.open(url,'popUpWindow2','height=300,width=1000,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}		
		}
	</script>
</head>

<%
	String tok=request.getParameter("tok");
	System.out.println(tok);
	String TestID="";
	String ProjectName="";
	if(tok.equals("12345"))
	{
		TestID=session.getAttribute("TestID").toString();
		ProjectName=session.getAttribute("ProjectName").toString();
	}
	else
	{
	TestID=request.getParameter("TestID");
	ProjectName=request.getParameter("ProjectName");
	session.setAttribute( "TestID", TestID );
	session.setAttribute( "ProjectName", ProjectName );
	session.setMaxInactiveInterval(60000);

	}
	String ltestDetailsData=ResultExtractEngine.getTestDetails(ProjectName,TestID);
	String ltestSummaryData=ResultExtractEngine.getTestSummary(ProjectName,TestID);
	String ltestStatusData=ResultExtractEngine.getTestStatus(ProjectName,TestID);
	String ltestObsData=ResultExtractEngine.getTestObservations(ProjectName,TestID);
%>

<body>
<basefont face="calibri">
<form name="admReportform" ACTION="/AdministratorModule/jsp/Reportupd.jsp"
	METHOD="POST"" >
<h2><font color=BLUE>Test Report</font></h2>

<div align="right"><a
	href="javascript:getpopup('<%=ProjectName%>','<%=TestID%>','publish')">Publish</a></div>
<div align="left"><a target="blank"
	href="/AdministratorModule/jsp/Analysis.jsp?ProjectName=<%=ProjectName%>&TestID=<%=TestID%>">Analyse
Results</a></div>
<div align="right"><a target="blank"
	href="/AdministratorModule/jsp/repGen.jsp?ProjectName=<%=ProjectName%>&TestID=<%=TestID%>">Download
PDF Report</a></div>
<br>
<div align="right"><INPUT TYPE=SUBMIT VALUE="UPDATE RESULTS"></div>
<br>
<br>
<h3>TEST ID: <u><i><%=TestID%></i></u> PROJECT NAME: <u><i><%=ProjectName%></i></u></h3>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Test Details:</u></b></td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><textarea rows="4" cols="100" name="test_Details"><%=ltestDetailsData%></textarea>

		<br>
		</td>
	</tr>
</table>
<br>
<b><u>Test Status:</u></b> <input type="text" size="20"
	name="testStatus" value="<%=ltestStatusData%>"> <br>
<br>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Test Summary:</u></b></td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><textarea rows="4" cols="100" name="test_Summary"><%=ltestSummaryData%></textarea>

		<br>
		</td>
	</tr>
</table>

<hr>

<h3><font color=BLUE>OBSERVATIONS</font></h3>
<br>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Test Observations:</u></b></td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><textarea rows="8" cols="150" name="test_Obs"><%=ltestObsData%></textarea>

		<br>
		</td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Test Statistics:</u></b></td>
	</tr>
</table>
<%
				String l_StRows = "";
				String l_Tokens ="";
				String testStats=ResultExtractEngine.getLRStatistics(ProjectName,TestID);
				%> <%=testStats%> <br>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Load Runner Statistics:</u></b></td>
	</tr>
</table>
<br>
<a
	href="javascript:getpopup('<%=ProjectName%>','<%=TestID%>','lrstats')">
Load Runner Graphs </a> <br>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>Transaction Response Time:</td>
	</tr>
</table>
<%String lResult=ResultExtractEngine.getLRtransactionRespTime(ProjectName,TestID);%>
<%=lResult%> <br>
<%
String Tokens = ResultExtractEngine.checkResultsExistence(TestID,ProjectName);
if(Tokens.contains("wily"))
{
%>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Wily Statistics:</u></b></td>
	</tr>
</table>
<br>
<a href="javascript:getpopup('<%=ProjectName%>','<%=TestID%>','wily')">
Wily Graphs </a> <br>
<table cellspacing=0 cellpadding=4 border=2 bordercolor=#224466
	width=80%>
	<tr bgcolor=#CCCCCC>
		<td colspan=0><b><font size="2"> Agent/Server Name </font></b></td>
		<td colspan=0><b><font size="2"> Resource Name </font></b></td>
		<td colspan=0><b><font size="2"> Metric Name </font></b></td>
		<td colspan=0><b><font size="2"> Value Type </font></b></td>
		<td colspan=0><b><font size="2"> Metric Value </font></b></td>
	</tr>
	<%
				l_StRows = ResultExtractEngine.getWilyStatistics(ProjectName,TestID);
				l_Tokens ="";
				StringTokenizer stObj1;
				stObj1 = new StringTokenizer(l_StRows , ",");
				while(stObj1.hasMoreTokens())
				{
					l_Tokens=stObj1.nextToken();
					StringTokenizer stCols = new StringTokenizer(l_Tokens , "#");
					%>
	<tr bgcolor=#CCFFFF>
		<%
					while(stCols.hasMoreTokens())
					{
				%><td colspan=0 align="left"><font size="2"><%=stCols.nextToken()%></font></td>
		<%
					}%>
	</tr>
	<%
				}
				%>

</table>
<br>
<%
}
if(Tokens.contains("ganglia"))
{
	lResult=ResultExtractEngine.getGangliaStatistics(ProjectName,TestID);	
%>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Ganglia Statistics:</u></b></td>
	</tr>
</table>
<br>
<a
	href="javascript:getpopup('<%=ProjectName%>','<%=TestID%>','ganglia')">
Ganglia Graphs </a> <br>
<%=lResult%> <%
}
if(Tokens.contains("perfmon"))
{
	lResult=ResultExtractEngine.getPerfmonStatistics(ProjectName,TestID);	
%><br>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Perfmon Statistics:</u></b></td>
	</tr>
</table>
<br>
<a
	href="javascript:getpopup('<%=ProjectName%>','<%=TestID%>','perfmon')">
Perfmon Graphs </a> <br>
<%=lResult%> <%
}
%> <br>
<br>
<a href="/AdministratorModule/jsp/index.jsp">Back</a></form>
</body>
</html>
