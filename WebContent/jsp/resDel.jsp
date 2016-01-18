
<%
	String TestID=request.getParameter("TestID");
	String ProjectName=request.getParameter("ProjectName");
	com.mm.core.ResultsDeleteEngine.DeleteTestResults(TestID,ProjectName);
%>
<jsp:forward page="resDelete.jsp" />

