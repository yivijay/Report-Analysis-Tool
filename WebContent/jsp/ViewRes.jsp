<%@page import="com.mm.core.ResultExtractEngine"%>
<%@page import="java.util.StringTokenizer"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test Report</title>
<script language="JavaScript" src="../FusionCharts/AJAXInteraction.js"></SCRIPT>
<script language="JavaScript" src="../FusionCharts/ChartUtil.js"></script>
<script language="JavaScript">
<%
HttpSession sestemp = request.getSession();
if (sestemp.getAttribute("login")==null)
{
%>
<jsp:forward page="/jsp/Login.jsp?valsess=iv" />
<%
}
%>
function validateForm()
{
if(document.form1.ProjectName.selectedIndex==0)
{
alert("Please select a Project.");
document.form1.ProjectName.focus();
return false;
}
if(document.form1.TestID.selectedIndex==0)
{
alert("Please select a Test ID.");
document.form1.TestID.focus();
return false;
}


}
			function updateselect(){
			if(document.form1.ProjectName.selectedIndex==0)
			{
					location.reload();
			}
			else
			{
				var Current = document.form1.ProjectName.selectedIndex;
  				var pPrjName =document.form1.ProjectName.options[Current].text;
				var lAJAXInteraction;
				var lXMLObject;
				var lDiv;
				var lRoot;
				var lTableURL = "/AdministratorModule/jsp/testDetails.jsp?ProjectName="+pPrjName;
				lAJAXInteraction = new AJAXInteraction(lTableURL, null, true);
				lXMLObject = lAJAXInteraction.doPost();
				lRoot = lXMLObject.responseXML.documentElement;			
				lDiv = document.getElementById("optionData");
				clearDIV("optionData");
				lDiv.innerHTML = lXMLObject.responseText;
		//		window.open("/AdministratorModule/jsp/RowsScanned.jsp")	
			}	
		}
</script>

</head>
<body>
<basefont face="calibri">
<form name="form1" ACTION="/AdministratorModule/jsp/Report.jsp?tok="
	METHOD="POST" onSubmit="return validateForm()">
<h1><font size=+2 color=BLUE>Test Results Dashboard</font></h1>
<hr>
<table width="875" align="center" cellpadding="0" cellspacing="0"
	border="0" style="Border-top: #EEEEEE 1px solid;">
	<tr>
		<td>
		<h4>Please Select the Project Name</h4>
		</td>
		<td><SELECT name="ProjectName"
			onchange='updateselect(this.form.ProjectName)'>
			<OPTION VALUE="Select">--Select--</OPTION>
			<%
				String l_PrNames = ResultExtractEngine.getProjectName();
				String l_Tokens ="";
				StringTokenizer stObj;
				stObj = new StringTokenizer(l_PrNames , ",");
				while(stObj.hasMoreTokens())
				{
					l_Tokens=stObj.nextToken();
				%>

			<OPTION VALUE="<%=l_Tokens%>"><%=l_Tokens%></OPTION>
			<%					
				}
				%>

		</select></td>
	</tr>

	<tr>
		<td>
		<h4>Please Select the Test ID</h4>
		</td>
		<td>
		<div id="optionData"><SELECT name="TestID">
			<OPTION VALUE="Select">--Select--</OPTION>
		</select></div>
		<div id="MESSAGEDIV"></div>
		</td>
	</tr>
</table>
<br>
<br>
<table width="875" align="center" cellpadding="0" cellspacing="0"
	border="0" style="Border-top: #EEEEEE 1px solid;">
	<tr>
		<td align='right' colspan='2'><INPUT TYPE=SUBMIT VALUE="SUBMIT"></td>
	</tr>
</table>
<br>
<br>
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