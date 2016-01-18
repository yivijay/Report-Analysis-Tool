<%@page import="com.mm.core.user_Authentication"%>
<%
String userID=request.getParameter("Field_userid");
String userPass=request.getParameter("Field_userpass");
String lResult=user_Authentication.validateLogin(userID,userPass);
response.setContentType("text/xml");	
%>
<HTML>
<BODY>
<script>
<%
if(lResult.equals("val"))
{
	HttpSession sesobject = request.getSession();
	sesobject.setAttribute("login",true);
	String name = user_Authentication.getUName(userID);
	sesobject.setAttribute("username", name);
%>
<jsp:forward page="/html/Index1.html" />
parent.location.reload(true);
<%
}
else
{
%>
<jsp:forward page="/jsp/Login.jsp?valsess=iv" />
<%
}
%>
</script>
</BODY>
</HTML>
