<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script language="JavaScript">
function validateForm()
{
	if(document.newUser.firstname.value==0)
	{
		alert("Please Enter First Name");
		document.newUser.firstname.focus();
		return false;
	}
	if(document.newUser.lastname.value==0)
	{
		alert("Please Enter Last Name");
		document.newUser.lastname.focus();
		return false;
	}
	if(document.newUser.userid.value==0)
	{
		alert("Please Enter UserID");
		document.newUser.userid.focus();
		return false;
	}
	if(document.newUser.npassword.value==0)
	{
		alert("Please Enter New Password");
		document.newUser.npassword.focus();
		return false;
	}
	if(document.newUser.cpassword.value==0)
	{
		alert("Please Enter Confirm Password");
		document.newUser.cpassword.focus();
		return false;
	}
	if(document.newUser.npassword.value!=document.newUser.cpassword.value)
	{
		alert("New and Confirm Password are not matching");
		document.newUser.npassword.value="";
		document.newUser.cpassword.value="";
		document.newUser.npassword.focus();		
		return false;
	}
}
</script>
<link type="text/css" rel="stylesheet" href="/AdministratorModule/css/Login.css"/>
</head>
<body>
<body background="/AdministratorModule/image/bg.jpg">
<form name="newUser" ACTION="/AdministratorModule/jsp/newUser.jsp" METHOD="POST" onSubmit="return validateForm()">

<%
String value=request.getParameter("val");
if(value.equals("true"))
{
%>
<div class="floatRight">

<table width="600" align="center" cellspacing="0"
	border="0">
	
	  <tr>
	<th align="left"><h4>Create a New account</h4></th> 
	</tr>
	<tr>
		<td align="justify">
		<h4>First Name</h4>
		</td>
		<td align="justify"><input type="text" name="firstname" placeholder="First Name"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Last Name</h4>
		</td>
		<td align="justify"><input type="text" name="lastname" placeholder="Last Name"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Choose your Username</h4>
		</td>
		<td align="justify"><input type="text" name="userid" placeholder="Userid"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Create Password</h4>
		</td>
		<td align="justify"><input type="password" name="npassword" placeholder="New Password"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Confirm Password</h4>
		</td>
		<td align="justify"><input type="password" name="cpassword" placeholder="Confirm Password"></input>
		</td>
	</tr>
	<tr>
		<td align='center' colspan='2'>
		<INPUT TYPE=SUBMIT VALUE="Sign Up"></td>
	</tr>
</table>
<table width="200" align="center" cellspacing="0"
	border="0">
	<tr>
		<td align='center'>
		<h5>UserID created successfully</h5>
		<td align='center'>
		<a href="/AdministratorModule/jsp/Login.jsp?valsess="><h5>Login</h5></a></td>
	</tr>
</table>

</div>

<%
}
else if(value.equals("false"))
{
%>
<div class="floatRight">

<table width="600" align="center" cellspacing="0"
	border="0">
	
	  <tr>
	<th align="left"><h4>Create a New account</h4></th> 
	</tr>
	<tr>
		<td align="justify">
		<h4>First Name</h4>
		</td>
		<td align="justify"><input type="text" name="firstname" placeholder="First Name"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Last Name</h4>
		</td>
		<td align="justify"><input type="text" name="lastname" placeholder="Last Name"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Choose your Username</h4>
		</td>
		<td align="justify"><input type="text" name="userid" placeholder="Userid"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Create Password</h4>
		</td>
		<td align="justify"><input type="password" name="npassword" placeholder="New Password"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Confirm Password</h4>
		</td>
		<td align="justify"><input type="password" name="cpassword" placeholder="Confirm Password"></input>
		</td>
	</tr>
	<tr>
		<td align='center' colspan='2'>
		<INPUT TYPE=SUBMIT VALUE="Sign Up"></td>
	</tr>
</table>
<table width="400" align="center" cellspacing="0"
	border="0">
	<tr>
		<td align='center'>
		<h5>UserID already exists. Please fill form again</h5>
	</tr>
</table>

</div>
<%
}
else
{
%>
<div class="floatRight">
<table width="600" align="center" cellspacing="0"
	border="0">
	
	  <tr>
	<th align="left"><h4>Create a New account</h4></th> 
	</tr>
	<tr>
		<td align="justify">
		<h4>First Name</h4>
		</td>
		<td align="justify"><input type="text" name="firstname" placeholder="First Name"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Last Name</h4>
		</td>
		<td align="justify"><input type="text" name="lastname" placeholder="Last Name"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Choose your Username</h4>
		</td>
		<td align="justify"><input type="text" name="userid" placeholder="Userid"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Create Password</h4>
		</td>
		<td align="justify"><input type="password" name="npassword" placeholder="New Password"></input>
		</td>
	</tr>
	<tr>
		<td align="justify">
		<h4>Confirm Password</h4>
		</td>
		<td align="justify"><input type="password" name="cpassword" placeholder="Confirm Password"></input>
		</td>
	</tr>
	<tr>
		<td align='center' colspan='2'>
		<INPUT TYPE=SUBMIT VALUE="Sign Up"></td>
	</tr>
</table>
</div>

<%
}
%>


</body>
</html>