<%@page import="com.mm.core.ResultsAnalysisEngine"%>
<%@page import="java.util.StringTokenizer"%>
<html>
<head>
<title>Results Dashboard</title>
<script language="JavaScript" src="../FusionCharts/AJAXInteraction.js"></SCRIPT>
<script language="JavaScript" src="../FusionCharts/FusionCharts.js"></script>
<script language="JavaScript" src="../FusionCharts/ChartUtil.js"></script>
<script language="JavaScript" src="../FusionCharts/formatter.js"></script>

<script language="JavaScript">
	function updatechart(ProjectName,TestID,TxnName,StartTime,EndTime,LRCNTRDATA,WilyTokens,GangliaTokens,PerfmonTokens)
	{
	 var lTableURL = "/AdministratorModule/jsp/GraphPlots.jsp?ProjectName="+ProjectName+"&TestID="+TestID+"&TxnName="+TxnName+"&StartTime="+StartTime+"&EndTime="+EndTime+"&LRCNTRDATA="+LRCNTRDATA+"&WilyTokens="+WilyTokens+"&GangliaTokens="+GangliaTokens+"&PerfmonTokens="+PerfmonTokens+"&grType=ANALYSISFROMRESPGRAPH";
	 popupWindow = window.open(lTableURL,'popUpWindow6','height=500,width=1200,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
	}
	function steadyState(steadyStStart,steadyStEnd)
	{
		document.form1.testStartTime.value=steadyStStart;
		document.form1.testEndTime.value=steadyStEnd;
		alert("Please click on Apply Filter link to apply changes!!!");
	}	
	function reset_Timer()
	{
		location.reload();
	}	
	function updateDIV_Timer(ProjectName,TestID,LR_CounterName,LRCNTRDATA,WilyTokens,GangliaTokens,PerfmonTokens)
	{
		if(LR_CounterName=="Response_Time")
		{
		var StartTime =document.form1.testStartTime.value;
	 	var EndTime =document.form1.testEndTime.value;
	 	var lAJAXInteraction;
	 	var lXMLObject;
	 	var lDiv;
	 	var lRoot;	 	
		var lDataURL = "/AdministratorModule/jsp/RowsScanned2.jsp?ProjectName="+ProjectName+"&TestID="+TestID+"&LR_CounterName="+LR_CounterName+"&StartTime="+StartTime+"&EndTime="+EndTime+"&LRCNTRDATA="+LRCNTRDATA+"&WilyTokens="+WilyTokens+"&GangliaTokens="+GangliaTokens+"&PerfmonTokens="+PerfmonTokens;
	 	lAJAXInteraction = new AJAXInteraction(lDataURL, null, true);
	 	lXMLObject = lAJAXInteraction.doPost();
	 	lRoot = lXMLObject.responseXML.documentElement;	
		var lChartObject = getChartFromId("myChartId_LR");									
		lChartObject.setDataXML(lXMLObject.responseText);
		lDiv = document.getElementById("optionData2");
	 	clearDIV("optionData2");	
		}		 		 	
	}	
</script>
</head>
<body>
<form name="form1" ACTION="" METHOD="POST">
<%
String ProjectName=request.getParameter("ProjectName");
String TestID=request.getParameter("TestID");
String resTokens=ResultsAnalysisEngine.checkResultsExistence(TestID,ProjectName);
String WilyTokens="",GangliaTokens="",PerfmonTokens="";
String TimerToken=ResultsAnalysisEngine.getStartEndTime(TestID,ProjectName);
StringTokenizer stObj=new StringTokenizer(TimerToken,",");
String StartTime=stObj.nextToken();
String EndTime=stObj.nextToken();
String LR_CounterName=request.getParameter("LR_CounterName");
String LRCNTRDATA=request.getParameter("LRCNTRDATA");
if(resTokens.contains("wily"))
{
	String[] WilyCounters=request.getParameterValues("WilyCounters");
	for(int i=0;i<WilyCounters.length;i++)
	{
		WilyTokens=WilyTokens+WilyCounters[i]+",";
	}
}
if(resTokens.contains("ganglia"))
{
	String[] GangliaCounters=request.getParameterValues("gangliaCounters");
	for(int i=0;i<GangliaCounters.length;i++)
	{
		GangliaTokens=GangliaTokens+GangliaCounters[i]+",";
	}
}
if(resTokens.contains("perfmon"))
{
	String[] PerfmonCounters=request.getParameterValues("perfmonCounters");
	for(int i=0;i<PerfmonCounters.length;i++)
	{
		PerfmonTokens=PerfmonTokens+PerfmonCounters[i]+",";
	}
}
%> <basefont face="calibri"> <b><font color=BLUE>ANALYSIS</font></b>
<div align="right"><a
	href="/AdministratorModule/jsp/Analysis.jsp?ProjectName=<%=ProjectName%>&TestID=<%=TestID%>">Back</a></div>
<br>
PROJECT NAME (<b><%=ProjectName%></b>) TEST ID (<b><%=TestID%></b>)<br>
<br>
<div id="optionData">
<%
if(LR_CounterName.equals("Response_Time"))
{
	if(resTokens.contains("responsetime"))
	{
		String chartData=ResultsAnalysisEngine.AnalysisPageLoadChartString(ProjectName,TestID,StartTime,EndTime,LR_CounterName,LRCNTRDATA,WilyTokens,GangliaTokens,PerfmonTokens);
		String steadyStStart="";
		String steadyStEnd="";
		if(resTokens.contains("runningvusers"))
		{
			String steadyStTokens=ResultsAnalysisEngine.getStartEndSteadyState(TestID,ProjectName);
			StringTokenizer stState= new StringTokenizer(steadyStTokens,",");
			steadyStStart=stState.nextToken();
			steadyStEnd=stState.nextToken();
		}

%>
<div align="center">
<table cellpadding="4" cellspacing="0" border="1" bordercolor=#224466>
	<tr bgcolor=#CCFFFF>
		<td><b>TIME FILTER</b> Start Time: <input type="text" size="17"
			name="testStartTime" value="<%=StartTime%>"> End Time: <input
			type="text" size="17" name="testEndTime" value="<%=EndTime%>">

		<i> <a
			href="javascript:updateDIV_Timer('<%=ProjectName%>','<%=TestID%>','<%=LR_CounterName%>','<%=LRCNTRDATA%>','<%=WilyTokens%>','<%=GangliaTokens%>','<%=PerfmonTokens%>')">Apply
		Filter</a> <a href="javascript:reset_Timer()">Reset Filter</a> <%	if(resTokens.contains("runningvusers"))	{ %>
		<a
			href="javascript:steadyState('<%=steadyStStart%>','<%=steadyStEnd%>')">Steady
		State</a> <% } %> </i></td>
	</tr>
</table>
</div>
<b>AVERAGE RESPONSE TIME</b> <br>
<table width="90%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>
		<div id="chartdiv_LR" align="center">DIV id: ChartID_LR.</div>
		<script type="text/javascript">
var myChart_LR = new FusionCharts("../FusionCharts/Column3D.swf", "myChartId_LR", "1200", "350", "0", "1");
myChart_LR.setDataXML("<%=chartData%>");
myChart_LR.render("chartdiv_LR");
</script></td>
	</tr>
</table>
<%
	}
	else
	{
%> <b>Average Response Time data doesn't exist for this test.<br>
Please upload the Load Runner response time raw data for accessing this
functionality.</b> <%
	}			
}
else if(LR_CounterName.equals("Throughput"))
{
	if(resTokens.contains("throughput"))
	{
		String formData=ResultsAnalysisEngine.AnalysisLoadFromOTC(ProjectName ,TestID ,LR_CounterName,StartTime,EndTime,LRCNTRDATA,WilyTokens,GangliaTokens,PerfmonTokens);
%> <%=formData%> <%
	}
	else
	{
%> <b>Throughput data doesn't exist for this test.<br>
Please upload the Load Runner Throughput raw data for accessing this
functionality.</b> <%
	}			
}
else if(LR_CounterName.equals("Running_Vusers"))
{
	if(resTokens.contains("runningvusers"))
	{
		String formData=ResultsAnalysisEngine.AnalysisLoadFromOTC(ProjectName ,TestID ,LR_CounterName,StartTime,EndTime,LRCNTRDATA,WilyTokens,GangliaTokens,PerfmonTokens);
%> <%=formData%> <%
	}
	else
	{
%> <b>Running Vusers data doesn't exist for this test.<br>
Please upload the Load Runner Running Vusers raw data for accessing this
functionality.</b> <%
	}			
}
else if(LR_CounterName.equals("Errors"))
{
	if(resTokens.contains("errors"))
	{
		String formData=ResultsAnalysisEngine.AnalysisLoadFromOTC(ProjectName ,TestID ,LR_CounterName,StartTime,EndTime,LRCNTRDATA,WilyTokens,GangliaTokens,PerfmonTokens);
%> <%=formData%> <%
	}
	else
	{
%> <b>Errors data doesn't exist for this test.<br>
Please upload the Load Runner Errors raw data for accessing this
functionality.</b> <%
	}			
}	
%>
</div>
<br>
<br>
<div id="optionData2"></div>
<div id="MESSAGEDIV"></div>
<input type="hidden" id="refreshed" value="no"> <script
	type="text/javascript">
onload=function(){
var e=document.getElementById("refreshed");
if(e.value=="no")e.value="yes";
else{e.value="no";location.reload();}
}
</script></form>
</body>
</html>