<%@page import="com.mm.core.ResultsAnalysisEngine"%>
<html>
<head>
<title>Results Dashboard</title>
<script language="JavaScript" src="../FusionCharts/AJAXInteraction.js"></SCRIPT>
<script language="JavaScript" src="../FusionCharts/ChartUtil.js"></script>
<script language="JavaScript">
	function validateForm()
	{
		if(document.form1.LR_CounterName.selectedIndex==0)
		{
			alert("Please select LoadRunner Counter which you want to Correlate!!");
			document.form1.LR_CounterName.focus();
			return false;
		}
	}
	function updateselect()
	{
		if(document.form1.LR_CounterName.selectedIndex==0)
		{
				location.reload();
		}
		else
		{
			var Current = document.form1.LR_CounterName.selectedIndex;
			var pCntrName =document.form1.LR_CounterName.options[Current].text;
			var lAJAXInteraction;
			var lXMLObject;
			var lDiv;
			var lRoot;
			var lTableURL = "/AdministratorModule/jsp/tempDIVData.jsp?pCntrName="+pCntrName+"&type=LRCOUNTERDATA";
			lAJAXInteraction = new AJAXInteraction(lTableURL, null, true);
			lXMLObject = lAJAXInteraction.doPost();
			lRoot = lXMLObject.responseXML.documentElement;			
			lDiv = document.getElementById("optionData");
			clearDIV("optionData");
			lDiv.innerHTML = lXMLObject.responseText;
	//		window.open("/AdministratorModule/jsp/RowsScanned.jsp")	
	 	}
	}
	function toggle(source,Type)
 	{
		var checkboxes;
		if(Type=='Wily')
		{
			checkboxes = document.getElementsByName('WilyCounters');
		}
		if(Type=='Perfmon')
		{
			checkboxes = document.getElementsByName('perfmonCounters');
		}
		if(Type=='Ganglia')
		{
			checkboxes = document.getElementsByName('gangliaCounters');
		}
		for(var i=0, n=checkboxes.length;i<n;i++)
		{
			checkboxes[i].checked = source.checked;
		}	
 	}	
</script>
</head>
<body>
<%
String ProjectName=request.getParameter("ProjectName");
String TestID=request.getParameter("TestID");
String output="";
%>

<form name="form1"
	ACTION="/AdministratorModule/jsp/AnalyseResults.jsp?ProjectName=<%=ProjectName%>&TestID=<%=TestID%>"
	METHOD="POST" onSubmit="return validateForm()"><basefont
	face="calibri">

<h3><font color="blue">LOADRUNNER COUNTERS</font></h3>
Please select LoadRunner Counter which you want to Correlate <br>
<SELECT name="LR_CounterName"
	onchange='updateselect(this.form.LR_CounterName)'>
	<OPTION VALUE="Select">--Select--</OPTION>
	<OPTION VALUE="Response_Time">Response_Time</OPTION>
	<OPTION VALUE="Throughput">Throughput</OPTION>
	<OPTION VALUE="Running_Vusers">Running_Vusers</OPTION>
	<OPTION VALUE="Errors">Errors</OPTION>
</SELECT> <br>
<h3><font color="blue">CORRELATE WITH:</font></h3>
<hr>
<br>
<h4><font color="blue">LOADRUNNER COUNTERS</font></h4>
Please select LoadRunner Counter with which you want to Correlate the
above selected counter: <br>
<div id="optionData"><SELECT name="LRCNTRDATA">
	<OPTION VALUE="Select">--Select--</OPTION>
</select></div>
<div id="MESSAGEDIV"></div>

<%
output=ResultsAnalysisEngine.generateAnalysisFilter(ProjectName,TestID);
%> <%=output%> <br>
<br>
<table width="875" align="center" cellpadding="0" cellspacing="0"
	border="0" style="Border-top: #EEEEEE 1px solid;">
	<tr>
		<td align='right' colspan='2'><INPUT TYPE=SUBMIT
			VALUE="CORRELATE"></td>
	</tr>
</table>
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