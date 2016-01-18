<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results Dashboard</title>
</head>
<frameset rows="9,82" frameborder="1" border="0" framespacing="0" color="black">
    <frame name="header" src="header.jsp" marginheight="0" marginwidth="0" scrolling="no" noresize>
	<frame name="login" src="/AdministratorModule/jsp/Login.jsp?valsess=" marginheight="0" marginwidth="0" scrolling="yes">
		<%
			HttpSession ses = request.getSession();
		
			if(ses.getAttribute("login")!=null) 
			{
		%>
		<jsp:forward page="/AdministratorModule/WebContent/html/Index1.html"></jsp:forward>
		<%
			}
		%>
    <!--  <frame name="footer" src="footer.html" marginheight="0" marginwidth="0" scrolling="no" noresize> -->
    <noframes>
    <body>
    <p>This page uses frames. The current browser you are using does not support frames.</p>
    </body>
    </noframes>
</frameset>
</html>