/**
 * @author Acer OEM User
 */
function AJAXInteraction(url, callback, pSynchronous, errorCallback)
{
	var req = init();
	
	var synchronous;
	if (!pSynchronous)
	{
		synchronous = false;
	}
	else
	{
		synchronous = true;
	}
	req.onreadystatechange = processRequest;
	function init()
	{
		if (window.XMLHttpRequest) {
			return new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			return new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
	/*function processRequest () {
		if (req.readyState == 4) {
			removeInProcess();
			if (req.status == 200 || req.status == 0) {
				if (handleHTMLResponse(req))
				{
					if (callback) callback(req);
				}
			}
			else {
				alert("error posting " + req.status);
			}
		}
	}*/
	function processRequest () {
		if (req.readyState == 4) {
			removeInProcess();
			if (req.status == 200 || req.status == 0) {
				if (handleHTMLResponse(req))
				{
					if (callback) callback(req);
				}
			}
			else {
				if (errorCallback)
				{
					if (errorCallback != null)
					{
						errorCallback(req.status);
					}
					else
					{
						alert("error posting " + req.status);
					}
				}
				else
				{
					alert("error posting " + req.status);
				}
			}
		}
	}
	this.doGet = function() {
		showInProcess();
		
		if (synchronous)
		{
			req.open("GET", url, false);
		}
		else
		{
			req.open("GET", url, true);
		}
		req.send(null);
		if (synchronous)
		{
			removeInProcess();
			if (handleHTMLResponse(req))
			{
				return req;
			}
			else
			{
				return null;
			}
		}
	}
	this.doPost = function(body) {
		showInProcess();
		if (synchronous)
		{
			req.open("POST", url, false);
		}
		else
		{
			req.open("POST", url, true);
		}
		req.setRequestHeader("Content-Encoding", "gzip");
		req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		req.setRequestHeader("MimeType", "text/xml");
		req.send(body);
		if (synchronous)
		{
			removeInProcess();
			if (handleHTMLResponse(req))
			{
				return req;
			}
			else
			{
				return null;
			}
		}
	}
}
function getXMLHttpRequest ()
{
	 if (window.XMLHttpRequest)
	 {
		return new XMLHttpRequest();
	 }
	 else if (window.ActiveXObject)
	 {
		return new ActiveXObject("Microsoft.XMLHTTP");
	 }
}
function processRequest ()
{
	if (req.readyState == 4)
	{
		removeInProcess();
		if (req.status == 200)
		{
			if (handleHTMLResponse(req))
			{
				if (callback) callback(req.get);
			}
		}
	}
}
/*
function showInProcess()
{
	var lMessageDiv;
	lMessageDiv = document.getElementById("MESSAGEDIV");
	lMessageDiv.innerHTML = "<table><tr><td><font color='#000000'>Processing ...</font></td></tr></table>";
	lMessageDiv.style.position = "absolute";
	lMessageDiv.style.top = 0;
	lMessageDiv.style.left = windowWidth() - 100;
	lMessageDiv.style.backgroundColor = "#FF0000";
	lMessageDiv.style.visibility = "visible";
}
*/

function showInProcess()
{
	 var scrollX, scrollY;
      
      if (document.all)
      {
         if (!document.documentElement.scrollLeft)
            scrollX = document.body.scrollLeft;
         else
            scrollX = document.documentElement.scrollLeft;
               
         if (!document.documentElement.scrollTop)
            scrollY = document.body.scrollTop;
         else
            scrollY = document.documentElement.scrollTop;
      }   
      else
      {
         scrollX = window.pageXOffset;
         scrollY = window.pageYOffset;
      }
        
	var lMessageDiv;
	lMessageDiv = document.getElementById("MESSAGEDIV");
	lMessageDiv.innerHTML = "<table><tr><td id=processing><font color='#000000'>Processing ...</font></td></tr></table>";
	lMessageDiv.style.position = "absolute";
	lMessageDiv.style.top = scrollY+50;
	lMessageDiv.style.left = windowWidth() - 100;
	lMessageDiv.style.backgroundColor = "#FEEDAB";
	lMessageDiv.style.visibility = "visible";
}

function removeInProcess()
{
	var lMessageDiv;
	lMessageDiv = document.getElementById("MESSAGEDIV");
	lMessageDiv.innerHTML = "";
	lMessageDiv.style.visibility = "hidden";
}