<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
<%
HttpSession sesobject = request.getSession();
//String s1=(String)sesobject.getAttribute("login");
if (sesobject.getAttribute("login")!=null) {
	String name = (String)sesobject.getAttribute("username");
	//system.out.println(name);
%>
<h2><font color=black><p style="text-align:center;">Welcome to Results Dashboard</p></font></h2>
<h4><font color=black><p style="text-align:left;">Welcome <%= name %></p></font></h2>
<%	
}
else 
{
%>
<h2><font color=black><p style="text-align:center;">Welcome to Results Dashboard</p></font></h2>
<%
} 
%>

</body>
</html>