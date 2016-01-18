<%@page import="com.mm.core.newuser"%>
<%
String fName=request.getParameter("firstname");
String lName=request.getParameter("lastname");
String userId=request.getParameter("userid");
String nPass=request.getParameter("npassword");
String cPass=request.getParameter("cpassword");
String lResult = newuser.validateLogin(fName,lName,userId,cPass);
response.setContentType("text/xml");
%>
<HTML>
<body>
<%
if(lResult.equals("val"))
{
%>	
	<jsp:forward page="/jsp/newAccount.jsp?val=true" />
<%
}
%>

<%
if(lResult.equals("nval"))
{
%>	
	<jsp:forward page="/jsp/newAccount.jsp?val=false" />
<%
}
%>
</body>
</html>