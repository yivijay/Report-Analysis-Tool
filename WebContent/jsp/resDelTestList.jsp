<%@page import="com.mm.core.ResultsDeleteEngine"%>
<%
	String lProjectName = request.getParameter("ProjectName");
	String optionData = ResultsDeleteEngine.getTestsList(lProjectName);
	response.setContentType("text/xml");	
%>
<%=optionData%>