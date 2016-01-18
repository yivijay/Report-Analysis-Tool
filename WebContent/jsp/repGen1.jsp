<%@page import="com.mm.core.ReportGenerate"%>
<%@page import="com.mm.core.MiniAppsEngine"%>
<html>
<head>
<title>Results Dashboard</title>
</head>
<body>
<%
String ProjectName=request.getParameter("ProjectName");
String TestID=request.getParameter("TestID");
String Selection=request.getParameter("Selection");
if(Selection.equals("exrep"))
{
	//do nothing
}
else if(Selection.equals("regenrep"))
{
	ReportGenerate.Downloadregenrep(ProjectName,TestID);
}
else if(Selection.equals("gennewrep"))
{
	ReportGenerate.ReportGenerator(ProjectName,TestID);
}
else if(Selection.equals("infosessrep"))
{
	MiniAppsEngine.GenerateMiniAppPDFReport(ProjectName,TestID,"infosessrep");
}
%>
<jsp:forward page="/jsp/downloadPDF.jsp" />
</body>
</html>