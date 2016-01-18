
<%
String ProjectName=request.getParameter("Field_prjName");
String TestID=request.getParameter("Field_testID");

String LRStatsFlag=request.getParameter("LRStatsFlag");
String WilyStatsFlag=request.getParameter("WilyStatsFlag");
String GangliaStatsFlag=request.getParameter("GangliaStatsFlag");
String PerfmonStatsFlag=request.getParameter("PerfmonStatsFlag");
String VmStatStatsFlag=request.getParameter("VmStatStatsFlag");


session.setAttribute( "TestID", TestID );
session.setAttribute( "ProjectName", ProjectName );
session.setAttribute( "LRStatsFlag", LRStatsFlag );
session.setAttribute( "WilyStatsFlag", WilyStatsFlag );
session.setAttribute( "GangliaStatsFlag", GangliaStatsFlag );
session.setAttribute( "PerfmonStatsFlag", PerfmonStatsFlag );
session.setAttribute( "VmStatStatsFlag", VmStatStatsFlag );
session.setMaxInactiveInterval(60000);
response.setContentType("text/xml");
System.out.println(TestID+"\n\n"+ProjectName+"\n\n"+LRStatsFlag+"\n\n"+WilyStatsFlag+"\n\n"+GangliaStatsFlag+"\n\n"+PerfmonStatsFlag+"\n\n"+VmStatStatsFlag+"\n\n");
%>
<jsp:forward page="/jsp/uploader.jsp" />
