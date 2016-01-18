<%@page import="com.mm.core.ResultExtractEngine"%>
<%
	String lProjectName = request.getParameter("ProjectName");
	String optionData = ResultExtractEngine.getTestsList(lProjectName);
	response.setContentType("text/xml");	
%>
<%=optionData%>