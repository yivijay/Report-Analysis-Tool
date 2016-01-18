<%@page import="com.mm.core.ResultsAnalysisEngine"%>
<%@page import="com.mm.core.ResultsSamplerEngine"%>
<%
	String pType = request.getParameter("type");
	String optionData="";
	if(pType.equals("LRCOUNTERDATA"))
	{
		String pCntrName = request.getParameter("pCntrName");
		optionData = ResultsAnalysisEngine.generateLRCounterFilter(pCntrName);
		//System.out.println("LRCOUNTERDATA"+optionData);
	}
	if(pType.equals("DATASAMPLEGETTEST"))
	{
		String ProjectName = request.getParameter("ProjectName");
		optionData = ResultsSamplerEngine.getTestsHTMLFilter(ProjectName);
	//	System.out.println("DATASAMPLEGETTEST \n"+optionData);
	}
	if(pType.equals("TIMEFILTERFOROTHERS"))
	{
		String ProjectName = request.getParameter("ProjectName");
		String TestID = request.getParameter("TestID");
		String TxnName = request.getParameter("TxnName");
		String StartTime = request.getParameter("StartTime");
		String EndTime = request.getParameter("EndTime");
		String LRCNTRDATA = request.getParameter("LRCNTRDATA");
		String WilyTokens = request.getParameter("WilyTokens");
		String GangliaTokens = request.getParameter("GangliaTokens");
		String PerfmonTokens = request.getParameter("PerfmonTokens");
	}
	response.setContentType("text/xml");	
%>
<%=optionData%>