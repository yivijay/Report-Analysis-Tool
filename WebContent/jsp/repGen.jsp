<%@page import="com.mm.core.ReportGenerate"%>
<html>
<head>
<title>Results Dashboard</title>
</head>
<body>
<basefont face="calibri">
<%
String ProjectName=request.getParameter("ProjectName");
String TestID=request.getParameter("TestID");
String output=ReportGenerate.checkReportExistence(ProjectName, TestID);
%>
<%=output%>
</body>
</html>