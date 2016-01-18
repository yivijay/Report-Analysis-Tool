<%@page import="com.mm.core.ResultsAnalysisEngine"%>
<%
	String lRowsData="";
	String ProjectName = request.getParameter("ProjectName");
	String TestID = request.getParameter("TestID");
	String LR_CounterName = request.getParameter("LR_CounterName");
	String StartTime = request.getParameter("StartTime");
	String EndTime = request.getParameter("EndTime");
	String LRCNTRDATA = request.getParameter("LRCNTRDATA");
	String WilyTokens = request.getParameter("WilyTokens");
	String GangliaTokens = request.getParameter("GangliaTokens");
	String PerfmonTokens = request.getParameter("PerfmonTokens");
	lRowsData = ResultsAnalysisEngine.AnalysisPageLoadChartFromTimer(ProjectName,TestID,StartTime,EndTime,LR_CounterName,LRCNTRDATA,WilyTokens,GangliaTokens,PerfmonTokens);
//	System.out.println(lRowsData);
	response.setContentType("text/xml");	
%>
<%=lRowsData%>