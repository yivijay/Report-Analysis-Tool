<%@page import="com.mm.core.ResultExtractEngine"%>
<%
	String lRowsData="";
	String pProjectName = request.getParameter("pProjectName");
	String pTestID = request.getParameter("pTestID");
	String TxnName = request.getParameter("TxnName");
	lRowsData = ResultExtractEngine.getRowsData(pProjectName,pTestID,TxnName);
	response.setContentType("text/xml");	
%>
<%=lRowsData%>