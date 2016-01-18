<%@ page import="java.io.*,java.util.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.io.output.*"%>
<%@page import="com.mm.core.PropertyConf"%>
<%@page import="com.mm.core.ResultsUploaderEngine"%>
<%
File file ;
int maxFileSize = 50000 * 1024;
int maxMemSize = 50000 * 1024;
String TestID=session.getAttribute("TestID").toString();
String ProjectName=session.getAttribute("ProjectName").toString();
String appToken=session.getAttribute("appToken").toString();
String filePath = PropertyConf.getProperty("FileDir")+"\\"+ProjectName+"_"+TestID;
ResultsUploaderEngine.FileFolderStruct(filePath);
String contentType = request.getContentType();
if ((contentType.indexOf("multipart/form-data") >= 0)) {
DiskFileItemFactory factory = new DiskFileItemFactory();
factory.setSizeThreshold(maxMemSize);
factory.setRepository(new File(PropertyConf.getProperty("tempDir")));
ServletFileUpload upload = new ServletFileUpload(factory);
upload.setSizeMax( maxFileSize );
try{
List fileItems = upload.parseRequest(request);
Iterator i = fileItems.iterator();
while ( i.hasNext () )
{
FileItem fi = (FileItem)i.next();
if ( !fi.isFormField () )
{
String fieldName = fi.getFieldName();
String fileName = fi.getName();
boolean isInMemory = fi.isInMemory();
long sizeInBytes = fi.getSize();
if( fileName.lastIndexOf("\\") >= 0 )
{
	file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\"))) ;
}
else
{
	file = new File(filePath +"\\"+ fileName.substring(fileName.lastIndexOf("\\")+1)) ;
}
fi.write(file) ;
System.out.println("Uploaded Filepath: "+filePath+" file name "+fileName+" appToken:::::::::::::::::: "+appToken+"\n\n\n");
}
}
}catch(Exception ex) {
System.out.println(ex);
}
}else{
}

if(appToken.equals("infosess"))
{
%>
<jsp:forward page="/jsp/MiniAppsProcessor.jsp?appToken=infosess&stageToken=processor" />
<%
}
else if(appToken.equals("dbsnpst"))
{
%>
<jsp:forward page="/jsp/MiniAppsProcessor.jsp?appToken=dbsnpst&stageToken=processor" />
<%
}
else if(appToken.equals("mysqlslow"))
{
%>
<jsp:forward page="/jsp/MiniAppsProcessor.jsp?appToken=mysqlslow&stageToken=processor" />
<%
}
%>

