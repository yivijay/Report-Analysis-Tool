<%@page import="com.mm.core.ResultExtractEngine"%>
<%@page import="java.util.StringTokenizer"%>
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
		}
	</script>
</head>

<%
	String sesVALID=request.getParameter("sesvalID");
	String Output=ResultExtractEngine.getProjectDetailsForPubReport(sesVALID);
%>


<body>
<%
	if(Output.equals("DoesnotExist"))
	{
%>
<br>
<br>
<h4><font color=RED>Invalid Session ID<br>
The Results of this test are either deleted or does not exist.</font></h4>
<%	}
	else
	{
	StringTokenizer st = new StringTokenizer(Output , ",");
	String ProjectName=st.nextToken();
	String TestID=st.nextToken();
	String ltestDetailsData=ResultExtractEngine.getTestDetails(ProjectName,TestID);
	String ltestSummaryData=ResultExtractEngine.getTestSummary(ProjectName,TestID);
	String ltestStatusData=ResultExtractEngine.getTestStatus(ProjectName,TestID);
	String ltestObsData=ResultExtractEngine.getTestObservationsPubRep(ProjectName,TestID);
%>
<basefont face="calibri">
<h2><font color=BLUE>Test Report</font></h2>
<br>
<h3>TEST ID: <u><i><%=TestID%></i></u> PROJECT NAME: <u><i><%=ProjectName%></i></u></h3>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Test Details:</u></b></td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><font color=BLUE><%=ltestDetailsData%></font> <br>
		</td>
	</tr>
</table>
<br>
<b><u>Test Status:</u></b>
<font color=BLUE><%=ltestStatusData%></font>
<br>
<br>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Test Summary:</u></b></td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><font color=BLUE><%=ltestSummaryData%></font> <br>
		</td>
	</tr>
</table>

<hr>

<h3><font color=BLUE>OBSERVATIONS</font></h3>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Test Observations:</u></b></td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><font color=BLUE><%=ltestObsData%></font> <br>
		</td>
	</tr>
</table>
<br>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Test Statistics:</u></b></td>
	</tr>
</table>
<%
				String l_StRows = "";
				String l_Tokens ="";
				String testStats=ResultExtractEngine.getLRStatistics(ProjectName,TestID);
				%>
<%=testStats%>
<br>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td><b><u> Load Runner Statistics:</u></b></td>
	</tr>
</table>
<br>
<a
	href="javascript:getpopup('<%=ProjectName%>','<%=TestID%>','lrstats')">
Load Runner Graphs </a>
<br>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>Transaction Response Time:</td>
	</tr>
</table>
<%String lResult=ResultExtractEngine.getLRtransactionRespTime(ProjectName,TestID);%>
<%=lResult%>
<br>
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
Wily Graphs </a>
<br>
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
Ganglia Graphs </a>
<br>
<%=lResult%>
<%
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
Perfmon Graphs </a>
<br>
<%=lResult%>
<%
}
}
%>
<br>
</body>
</html>
