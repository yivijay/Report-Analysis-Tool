<%@page import="com.mm.core.ResultsUploaderEngine"%>
<%
	String testDetails=request.getParameter("test_Details");
	String testSummary=request.getParameter("test_Summary");
	String testObservations=request.getParameter("test_Obs");
	String testStatus=request.getParameter("testStatus");
	String TestID=session.getAttribute("TestID").toString();
	String ProjectName=session.getAttribute("ProjectName").toString();
	ResultsUploaderEngine.updateReportOBStoDB(TestID,ProjectName,testDetails,testSummary,testObservations,testStatus);	
%>
<jsp:forward page="Report.jsp?tok=12345" />