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
function validateForm(form1)
{
    if(document.form1.TestID.checked == false) 
    {
	      alert ('You didn\'t choose any of the checkboxes!');
          return false;
    }
    else
    {    
        return true;
    }
/*
var checked_count = $('input:TestID:checked').length;
alert(checked_count + " checkboxes are checked");

if(!document.form1.TestID.checked)
{
alert("Please select atleast 3 Tests!!");
document.form1.SelectAll_Counters.focus();
return false;
}*/
}
	function toggle(source)
 	{
		var checkboxes;
		checkboxes = document.getElementsByName('TestID');
		for(var i=0, n=checkboxes.length;i<n;i++)
		{
			checkboxes[i].checked = source.checked;
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
		var lTableURL = "/AdministratorModule/jsp/tempDIVData.jsp?ProjectName="+pPrjName+"&type=DATASAMPLEGETTEST";
		lAJAXInteraction = new AJAXInteraction(lTableURL, null, true);
		lXMLObject = lAJAXInteraction.doPost();
		lRoot = lXMLObject.responseXML.documentElement;			
		lDiv = document.getElementById("optionData");
		clearDIV("optionData");
	//	alert(lXMLObject.responseText);
		lDiv.innerHTML = lXMLObject.responseText;	
	}	
	}
</script>

</head>
<body>
<basefont face="calibri">
<form name="form1" ACTION="/AdministratorModule/jsp/Sampler.jsp" METHOD="POST" onSubmit="return validateForm(this)">
<a href="/AdministratorModule/jsp/adminHome.jsp">Home</a>
<h1><font size=+2 color=BLUE>Test Results Dashboard</font></h1>
<hr>
<table width="875" align="center" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>
		<h4>Please Select the Project Name</h4>
		</td>
		<td><SELECT name="ProjectName" onchange='updateselect(this.form.ProjectName)'>
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
</table>
<br><br><br>
<div id="optionData" align="center"></div>
<div id="MESSAGEDIV"></div>

<br>
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