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
<br><br>
<h2><font color=BLUE>Mini Apps</font></h2>
<br>
<table align="center" cellpadding="6" cellspacing="6" 
	border="0">
	<tr>
		<td align="left"><i> <a
			href="/AdministratorModule/jsp/MiniAppsProcessor.jsp?appToken=infosess&stageToken=initor">Informatica Session Log Parser</a> </i></td>
		<td>Use this link to access Informatica Session Log Parser.</td>
	</tr>
	<tr>
		<td align="left"><i> <a
			href="/AdministratorModule/jsp/MiniAppsProcessor.jsp?appToken=dbsnpst&stageToken=initor">DB2 Snapshot Parser</a> </i></td>
		<td>Use this link to access DB2 Snapshot Parser.</td>
	</tr>
<!-- 	<tr>
		<td align="left"><i> <a
			href="/AdministratorModule/jsp/MiniAppsProcessor.jsp?appToken=mysqlslow&stageToken=initor">MySQL Slow Query Log Parser</a> </i></td>
		<td>Use this link to access MySQL Slow Query Log Parser.</td>
	</tr>	
	 -->
</table>
<div id="MESSAGEDIV"></div>
<br>

</body>
</html>