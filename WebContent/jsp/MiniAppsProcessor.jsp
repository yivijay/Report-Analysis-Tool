<%@page import="com.mm.core.MiniAppsEngine"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mini Apps</title>
</head>
<%
String lResult="";
String appToken = request.getParameter("appToken");
String stageToken = request.getParameter("stageToken");
String ProjectName="";
String TestID="";
if(stageToken.equals("initor"))
{
	lResult=MiniAppsEngine.GenerateMiniAppPage(appToken);	
}
else if(stageToken.equals("uploader"))
{
	ProjectName = request.getParameter("Field_prjName");
	TestID = request.getParameter("Field_testID");
	session.setAttribute( "TestID", TestID );
	session.setAttribute( "ProjectName", ProjectName );
	session.setAttribute( "appToken", appToken );
	session.setMaxInactiveInterval(60000);
	lResult=MiniAppsEngine.GenerateMiniAppUploaderPage(appToken);	
}
else if(stageToken.equals("processor"))
{
	TestID=session.getAttribute("TestID").toString();
	ProjectName=session.getAttribute("ProjectName").toString();
	lResult=MiniAppsEngine.GenerateMiniAppProcessorPage(ProjectName,TestID,appToken);
}
%>
<body>
<basefont face="calibri">
<div align="left"><a href="javascript:window.close();">Close</a></div>
<%
if(stageToken.equals("processor"))
{
%>
<div align="right"><a href="/AdministratorModule/jsp/repGen1.jsp?ProjectName=<%=ProjectName%>&TestID=<%=TestID%>&Selection=infosessrep">Generate PDF Report</a></div>
<%
}
%>
<h2><font color=BLUE>Mini Apps</font></h2>
<%=lResult %>

</body>
</html>