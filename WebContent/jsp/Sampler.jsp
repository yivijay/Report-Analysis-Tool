<%@page import="java.util.StringTokenizer"%>
<%@page import="com.mm.core.ResultsSamplerEngine"%>
<html>
<head>
<title>Test Report</title>
<script language="JavaScript" src="../FusionCharts/FusionCharts.js"></script>
</head>
<body>
<basefont face="calibri">
<form name="form1" ACTION="" METHOD="POST">
<%
String ProjectName=request.getParameter("ProjectName");
String[] TestID=request.getParameterValues("TestID");
String TestIDTokens="";
for(int i=0;i<TestID.length;i++)
{
	TestIDTokens=TestIDTokens+TestID[i]+",";
}
String output=ResultsSamplerEngine.buildSamplerResponse(ProjectName,TestIDTokens);
%> 
<a href="/AdministratorModule/jsp/adminHome.jsp">Home</a>
<br>

<%=output%>

<div align="right">
<a href="/AdministratorModule/jsp/dataSampler.jsp">Back</a>
</div>
</form>
</body>
</html>