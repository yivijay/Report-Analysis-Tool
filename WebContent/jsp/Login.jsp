<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dashboard</title>
<script language="JavaScript">
function validateForm()
{
	if(document.LoginForm.Field_userid.value==0)
	{
		alert("Please Enter User ID.");
		document.LoginForm.Field_userid.focus();
		return false;
	}
	if(document.LoginForm.Field_userpass.value==0) 
	{
		alert("Please Enter Password.");
		document.LoginForm.Field_userpass.focus();
		return false;
	}
}
</script>
<link type="text/css" rel="stylesheet" href="/AdministratorModule/css/Login.css"/>
</head>
<body background="/AdministratorModule/image/bg.jpg">

<basefont face="timesnewroman">
<form name="LoginForm" ACTION="/AdministratorModule/jsp/auth.jsp" METHOD="POST" onSubmit="return validateForm()">

<%
String toggle=request.getParameter("valsess"); 
if(toggle.equals("iv"))
{
%>


<div class="floatLeft"> 

<table width="300" align="center" cellspacing="0"
	border="0">
	<!-- <tr>
	<th align="right"><h6>Invalid</h6></th>
	<th align="left"><h6> Login Credentials</h6></th> 
	</tr> -->
	
	

	<tr>
		<td align="justify">
		<h4>User ID</h4>
		</td>
		<td align="justify"><input type="text" name="Field_userid" placeholder="Userid"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Password</h4>
		</td>
		<td align="justify"><input type="password" name="Field_userpass" placeholder="Password"></input>
		</td>
	</tr>
	
	<tr>
		<td align='center'>
		<INPUT TYPE=SUBMIT VALUE="Log In"></td>
		<td align='center'>
		<a href="/AdministratorModule/jsp/newAccount.jsp?val=">Sign Up?</a></td> 
	</tr>
</table>
</div> 


<table width="300" align="center" cellspacing="0"
	border="0">
	<tr>
		<td align='center'>
		<h4>Invalid Login Credentials</h4>
	</tr>
</table> 
<%
}
else if (toggle.equals("logout"))
{
	HttpSession sesobject = request.getSession();
	sesobject.invalidate();
%>

<jsp:forward page="/jsp/Login.jsp?valsess="></jsp:forward>
<%
}
	
else
{
%>
<div class="floatLeft">
<table width="300" align="center" cellspacing="0"
	border="0">
	<tr>
	<!--  <th align="center"><h4>Login</h4></th> -->
	</tr>
	<tr>
		<td align="justify">
		<h4>User ID</h4>
		</td>
		<td align="justify"><input type="text" name="Field_userid" placeholder="Userid"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Password</h4>
		</td>
		<td align="justify"><input type="password" name="Field_userpass" placeholder="Password"></input>
		</td>
	</tr>
	<tr>
		<td align='center'>
		<INPUT TYPE=SUBMIT VALUE="Log In"></td>
		<td align='center'>
		<a href="/AdministratorModule/jsp/newAccount.jsp?val=">Sign Up?</a></td> 
	</tr>
</table>
</div> 

<%
}
%>
<!-- <div class="floatRight">
<table width="500" align="right" cellspacing="0"
	border="0" bgcolor="#C0C0C0">
	
	  <tr>
	<th align="left"><h4>Create a New account</h4></th> 
	</tr>
	<tr>
		<td align="justify">
		<h4>First Name</h4>
		</td>
		<td align="justify"><input type="text" name="Field_userid" placeholder="First Name"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Last Name</h4>
		</td>
		<td align="justify"><input type="text" name="Field_userpass" placeholder="Last Name"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Choose your Username</h4>
		</td>
		<td align="justify"><input type="text" name="Field_userpass" placeholder="Userid"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Create Password</h4>
		</td>
		<td align="justify"><input type="password" name="Field_userpass" placeholder="New Password"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Confirm Password</h4>
		</td>
		<td align="justify"><input type="password" name="Field_userpass" placeholder="Confirm Password"></input>
		</td>
	</tr>
	<tr>
		<td align='center' colspan='2'>
		<INPUT TYPE=SUBMIT VALUE="Sign Up"></td>
	</tr>
</table>
</div>
 -->

</script>
</form>
</body>
</html>