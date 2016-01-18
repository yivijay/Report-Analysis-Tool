

<html>
<head>

<title>Results Dashboard</title>
<script language="JavaScript">
function validateForm()
{

	if(document.uploaderForm.file.value==0)
	{
		alert("Please upload a zip file.");
		document.uploaderForm.file.focus();
		return false;
	}	
}
</script>
</head>
<body>
<form name="uploaderForm" action="/AdministratorModule/jsp/Upload.jsp"
	METHOD="POST" onSubmit="return validateForm()"
	enctype="multipart/form-data"><basefont face="calibri">

<table width="875" align="center" cellpadding="6" cellspacing="6" border="0" >
	<tr>
		<td align="left" colspan="2"><b>Note:</b> Please upload the zip
		file of results which contains all the metrics collected as a part of
		test results.<br>
		</td>
	</tr>
	<tr>
		<td align="left">Upload zip file</td>
		<td><input type="file" name="file" id="file" size="60" /> <br />
		</td>
	</tr>
	<tr>
		<td align='center' colspan='2'><br>
		<br>
		<br>
		<br>
		<INPUT TYPE=SUBMIT VALUE="UPLOAD"></td>
	</tr>
</table>
<div id="MESSAGEDIV"></div> 
<br>
</form>
</body>
</html>
