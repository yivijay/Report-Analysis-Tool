<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Results Dashboard</title>
</head>
<body>
<basefont face="calibri">
<br>
<%
String TestID=session.getAttribute("TestID").toString();
String ProjectName=session.getAttribute("ProjectName").toString();
String lResult="";
int LRStatsFlag,WilyStatsFlag,GangliaStatsFlag,PerfmonStatsFlag,VmStatStatsFlag;

if(session.getAttribute("LRStatsFlag")==null)
{	LRStatsFlag=0;}
else
{	LRStatsFlag=1;}
if(session.getAttribute("WilyStatsFlag")==null)
{	WilyStatsFlag=0;}
else
{	WilyStatsFlag=1;}
if(session.getAttribute("GangliaStatsFlag")==null)
{	GangliaStatsFlag=0;}
else
{	GangliaStatsFlag=1;}
if(session.getAttribute("PerfmonStatsFlag")==null)
{	PerfmonStatsFlag=0;}
else
{	PerfmonStatsFlag=1;}
if(session.getAttribute("VmStatStatsFlag")==null)
{	VmStatStatsFlag=0;}
else
{	VmStatStatsFlag=1;}

System.out.println("session Attribute TestID :  " + TestID);
System.out.println("session Attribute ProjectName :  " + ProjectName);
System.out.println("session Attribute LRStatsFlag :  " + LRStatsFlag);
System.out.println("session Attribute WilyStatsFlag :  " + WilyStatsFlag);
System.out.println("session Attribute GangliaStatsFlag :  " + GangliaStatsFlag);
System.out.println("session Attribute PerfmonStatsFlag :  " + PerfmonStatsFlag);
System.out.println("session Attribute VmStatStatsFlag :  " + VmStatStatsFlag);
lResult=com.mm.core.ResultsUploaderEngine.manageResultsUpload(TestID,ProjectName,LRStatsFlag,WilyStatsFlag,GangliaStatsFlag,PerfmonStatsFlag,VmStatStatsFlag);
%>
<%=lResult%>
<br>

</body>
</html>
