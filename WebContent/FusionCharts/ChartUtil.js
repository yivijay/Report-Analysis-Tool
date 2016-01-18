function handleHTMLResponse(pXMLObject){
	var lCount;
	if (pXMLObject.getResponseHeader("Content-Type").indexOf("text/html") == 0){
		if (pXMLObject.responseText.indexOf("<TITLE>Login</TITLE>") > 0){
			window.location.href = "/TCSFramework/jsp/global/AccessDenied.jsp";
			return false;
		}
	}
	return true;
}

function clearDIV(pDivName){		
	var lDIV;
	var lCount;
	
	lDIV = document.getElementById(pDivName);
	if (pDivName == "AddEdit"){
		if (lDIV.nodeName == "form" || lDIV.nodeName == "FORM")	{
			lDIV = getNodeDeep("div", "id", pDivName, lDIV);
		}
	}
	if (!lDIV){
		return;
	}
	if (lDIV == null){
		return;
	}
	if (lDIV.childNodes != null){
		if (lDIV.childNodes.length > 0){
			for(lCount = lDIV.childNodes.length - 1; lCount >= 0; lCount--)	{
				lDIV.removeChild(lDIV.childNodes[lCount]);
			}
		}
	}
}

function windowWidth(){
	if (window.innerWidth)
	    return (window.innerWidth);                     
    if (document.documentElement.clientWidth)
	    return document.documentElement.clientWidth;    
    if (document.body.clientWidth)
	    return document.body.clientWidth;               
}