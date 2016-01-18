<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@page import="com.mm.core.PropertyConf"%>
<%
	
try{
String TestID="";
String ProjectName="";
TestID=session.getAttribute("TestID").toString();
ProjectName=session.getAttribute("ProjectName").toString();

String txtFileNameVariable=ProjectName+"_"+TestID+"_Report.pdf";
String locationVariable=PropertyConf.getProperty("ReportDir");
String PathVariable="";
PathVariable = locationVariable+"\\"+txtFileNameVariable;
BufferedInputStream bufferedInputStream = null;
try
{
    bufferedInputStream = new BufferedInputStream(new FileInputStream(PathVariable));
}
catch(FileNotFoundException fnfe)
{
    fnfe.printStackTrace();
}
File f=new File(locationVariable, txtFileNameVariable);
String fileType = txtFileNameVariable.substring(txtFileNameVariable.indexOf(".")+1,txtFileNameVariable.length());
if (fileType.trim().equalsIgnoreCase("txt"))
{
 response.setContentType("text/plain");
} 
else if (fileType.trim().equalsIgnoreCase("doc"))
{
 response.setContentType("application/msword");
} 
else if (fileType.trim().equalsIgnoreCase("xls"))
{
 response.setContentType("application/vnd.ms-excel");
} 
else if (fileType.trim().equalsIgnoreCase("pdf"))
{
 response.setContentType("application/pdf");
} 
else 
{
 response.setContentType( "application/octet-stream" );
}
String original_filename = txtFileNameVariable;
response.setHeader( "Content-Disposition", "attachment; filename=\"" + original_filename + "\"" );
try
{
    int anInt=0;    
    while((anInt=bufferedInputStream.read())!=-1)
        out.write(anInt);
    out.flush();
    bufferedInputStream.close();
//   OutputStream output = response.getOutputStream();
//    while((anInt=bufferedInputStream.read())!=-1)
//        output.write(anInt);
//    output.flush();
}
catch(IOException ioe)
{
    ioe.printStackTrace();
}
}
catch(Exception e)
{
out.println("This is the Error " +e.getMessage());
}
%>


