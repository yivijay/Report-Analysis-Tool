<html>
<head>
<%
HttpSession sesobject = request.getSession();
if (sesobject.getAttribute("login")==null)
{
%>
<jsp:forward page="/jsp/Login.jsp?valsess=iv"></jsp:forward>
<%
}
%>
</head>

<body>
<basefont face="calibri">
<img src="/AdministratorModule/image/Cycle1.jpg" alt="SDLC" height="500" width="960">
</body>
</html>
