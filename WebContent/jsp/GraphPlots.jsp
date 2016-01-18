<%@page import="com.mm.core.ResultExtractEngine"%>
<%@page import="com.mm.core.ResultsAnalysisEngine"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results Dashboard</title>
<script language="JavaScript" src="../FusionCharts/AJAXInteraction.js"></SCRIPT>
<script language="JavaScript" src="../FusionCharts/FusionCharts.js"></script>
<script language="JavaScript" src="../FusionCharts/ChartUtil.js"></script>
<script language="JavaScript" src="../FusionCharts/formatter.js"></script>
<script>
		function getpopupwily(PrjName,tstID,ClusterName,Resource_Name,Metric_Name){
		var url="/AdministratorModule/jsp/queryTable.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&grType=wily"+"&ClusterName="+ClusterName+"&Resource_Name="+Resource_Name+"&Metric_Name="+Metric_Name;
		popupWindow = window.open(url,'popUpWindow1','height=500,width=1200,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}
		function getpopupgang(PrjName,tstID,ClusterName,ServerName){
		var url="/AdministratorModule/jsp/queryTable.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&grType=ganglia"+"&CounterName="+ClusterName+"&ServerName="+ServerName;
		popupWindow = window.open(url,'popUpWindow1','height=500,width=1200,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}
		function getpopupperf(PrjName,tstID,ClusterName){
		var url="/AdministratorModule/jsp/queryTable.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&grType=perfmon"+"&CounterName="+ClusterName;
		popupWindow = window.open(url,'popUpWindow1','height=500,width=1200,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}
		function getpopupthpt(PrjName,tstID){
			var url="/AdministratorModule/jsp/GraphPlots.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&grType=throughput";
			popupWindow = window.open(url,'popUpWindow7','height=700,width=1300,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}
		function getpopuperrors(PrjName,tstID,txnName){
			var url="/AdministratorModule/jsp/GraphPlots.jsp?ProjectID="+PrjName+"&TestID="+tstID+"&txnName="+txnName+"&grType=errors";
			popupWindow = window.open(url,'popUpWindow8','height=700,width=1300,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}
		
	</script>

<%
	String grType=request.getParameter("grType");
	String lResult="";
	if(grType.equals("wily"))
	{
		String TestID=request.getParameter("TestID");
		String ProjectName=request.getParameter("ProjectID");
		lResult=ResultExtractEngine.getWilyGraphs(ProjectName,TestID);
	}
	else if(grType.equals("ganglia"))
	{
		String TestID=request.getParameter("TestID");
		String ProjectName=request.getParameter("ProjectID");
		lResult=ResultExtractEngine.getGangliaGraphs(ProjectName,TestID);
	}
	else if(grType.equals("publish"))
	{
		String TestID=request.getParameter("TestID");
		String ProjectName=request.getParameter("ProjectID");
		lResult=ResultExtractEngine.getGenerateLink(ProjectName,TestID);
	}
	else if(grType.equals("lrstats"))
	{
		String TestID=request.getParameter("TestID");
		String ProjectName=request.getParameter("ProjectID");
		lResult=ResultExtractEngine.getLRGraphs(ProjectName,TestID);
	}
	else if(grType.equals("perfmon"))
	{
		String TestID=request.getParameter("TestID");
		String ProjectName=request.getParameter("ProjectID");
		lResult=ResultExtractEngine.getPerfmonGraphs(ProjectName,TestID);
	}
	else if(grType.equals("throughput"))
	{
		String TestID=request.getParameter("TestID");
		String ProjectName=request.getParameter("ProjectID");
		lResult=ResultExtractEngine.getThroughputGraph(ProjectName,TestID);
	}
	else if(grType.equals("errors"))
	{
		String TestID=request.getParameter("TestID");
		String ProjectName=request.getParameter("ProjectID");
		String Txn_Name= request.getParameter("txnName");
		lResult=ResultExtractEngine.getErrorGraph(ProjectName,TestID,Txn_Name);
	}
	else if(grType.equals("ANALYSISFROMRESPGRAPH"))
	{
		String TestID="";
		String ProjectName="";
		ProjectName = request.getParameter("ProjectName");
		TestID = request.getParameter("TestID");
		String TxnName = request.getParameter("TxnName");
		String StartTime = request.getParameter("StartTime");
		String EndTime = request.getParameter("EndTime");
		String LRCNTRDATA = request.getParameter("LRCNTRDATA");
		String WilyTokens = request.getParameter("WilyTokens");
		String GangliaTokens = request.getParameter("GangliaTokens");
		String PerfmonTokens = request.getParameter("PerfmonTokens");
		lResult = ResultsAnalysisEngine.AnalysisLoadFromRespGraph(ProjectName,TestID,TxnName,StartTime,EndTime,LRCNTRDATA,WilyTokens,GangliaTokens,PerfmonTokens);
	}
	if(grType.equals("HOWTOLRVU"))
	{
		lResult=ResultsAnalysisEngine.HowToLRRunningVusers();		
	}
%>
</head>
<body>
<basefont face="calibri">
<%=lResult%>
</body>
</html>