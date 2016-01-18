<html>
<head>
<%
HttpSession sesobject = request.getSession();
if (sesobject.getAttribute("login")==null)
{
%>
<jsp:forward page="/jsp/Login.jsp?valsess=iv"></jsp:forward>
<%
}
%>
<script language="JavaScript"> 

function validateForm()
{
	if(document.resUploadForm.Field_prjName.value==0)
	{
		alert("Please Enter Project Name.");
		document.resUploadForm.Field_prjName.focus();
		return false;
	}	
	if(document.resUploadForm.Field_testID.value==0)
	{
		alert("Please Enter Test ID.");
		document.resUploadForm.Field_testID.focus();
		return false;
	}
}
function howTo()
{
	 var lTableURL = "/AdministratorModule/jsp/GraphPlots.jsp?grType=HOWTOLRVU";
	 popupWindow = window.open(lTableURL,'popUpWindow9','height=500,width=1200,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')

}
</script>
</head>
<body>
<form name="resUploadForm" action="/AdministratorModule/jsp/uploader1.jsp"
	METHOD="POST" onSubmit="return validateForm()"><basefont
	face="calibri">
	
<table width="875" align="center" cellpadding="6" cellspacing="6"
	border="0">
	<tr>
		<td align="left">Project Name</td>
		<td><input type="text" name="Field_prjName" size="60"></input></td>
	</tr>
	<tr>
		<td align="left">Test ID</td>
		<td><input type="text" name="Field_testID" size="60"></input></td>
	</tr>
	<tr>
		<td align="left" colspan='2'><br>
		</td>
	</tr>
	<tr bgcolor=#E0DFDB border="1">
		<td align="left" colspan='2'><br>
		<b>Note: </b>Click <b><i>(<a href="javascript:howTo()">Here</a>)</i></b>
		for referring steps to export the Load Runner "<i><b>Running
		Vusers</b></i>" Data.</td>
	</tr>
	<tr>
		<td align="left">Load Runner Results</td>
		<td><input type="checkbox" name="LRStatsFlag"
			value="LR Statistics" checked onclick="return false"> Note: <i>Please
		make sure that the file extension is ".xls"</i></td>
	</tr>
	<tr>
		<td align="left" colspan='2'><br>
		Please check from the following options,<br>
		if the results which you want to upload contains any/all of these
		types of counters:</td>
	</tr>
	<tr>
		<td align="left">Wily Results</td>
		<td><input type="checkbox" name="WilyStatsFlag"
			value="Wily Statistics"> Note: <i>Please make sure that
		the file extension is ".wily"</i></td>
	</tr>
	<tr>
		<td align="left">Ganglia Results</td>
		<td><input type="checkbox" name="GangliaStatsFlag"
			value="Ganglia Statistics"> Note: <i>Please make sure
		that the file extension is ".ganglia"</i></td>
	</tr>
	<tr>
		<td align="left">Perfmon Results</td>
		<td><input type="checkbox" name="PerfmonStatsFlag"
			value="Perfmon Statistics"> Note: <i>Please make sure
		that the file extension is ".perfmon"</i></td>
	</tr>
	<tr>
		<td align="left">VmStat Results</td>
		<td><input type="checkbox" name="VmStatStatsFlag"
			value="VmStat Statistics"> Note: <i>Please make sure that
		the file extension is ".vmstat"</i></td>
	</tr>
	<tr>
		<td align='center' colspan='2'><br>
		<br>
		<br>
		<br>
		<INPUT TYPE=SUBMIT VALUE="NEXT >>"></td>
	</tr>
</table>
<div id="MESSAGEDIV"></div>
<br>

</form>
</body>
</html>
