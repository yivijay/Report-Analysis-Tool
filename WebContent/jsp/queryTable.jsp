<%@page import="com.mm.core.ResultExtractEngine"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results Dashboard</title>
<%
	String TestID=request.getParameter("TestID");
	String ProjectName=request.getParameter("ProjectID");
	String grType=request.getParameter("grType");
	String lResult="";

	if(grType.equals("wily"))
	{
		String ClusterName=request.getParameter("ClusterName");
		String Resource_Name=request.getParameter("Resource_Name");
		String Metric_Name=request.getParameter("Metric_Name");

		if(Metric_Name.equals("Utilization  (process)")|Metric_Name.equals("Utilization (process)"))
			{Metric_Name="Utilization % (process)";}
		lResult=ResultExtractEngine.getWilyGraphsTableData(ProjectName,TestID,ClusterName,Resource_Name,Metric_Name);
	}
	else if(grType.equals("ganglia"))
	{
		String ServerName=request.getParameter("ServerName");
		String CounterName=request.getParameter("CounterName");
		lResult=ResultExtractEngine.getGangliaGraphsTableData(ProjectName,TestID,ServerName,CounterName);
	}
	else if(grType.equals("perfmon"))
	{
		String CounterName=request.getParameter("CounterName");
		lResult=ResultExtractEngine.getPerfmonGraphsTableData(ProjectName,TestID,CounterName);
	}

%>
</head>
<body>
<basefont face="calibri">
<%=lResult%>
</body>
</html>