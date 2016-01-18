function formatt()
{
    maxchars=150;
	for(x=1;x<=20;x++)
	{
		if(document.getElementById("TD"+x))
		{
			counter=x;
		}		
	}
	for(y=1;y<=counter;y++)
	{
		  strbuff=document.getElementById("TD"+y).innerHTML;
	  subarr=new Array(parseInt(strbuff.length/maxchars+1)) 
	  newstr='';
	  startI = 0;
	  indx1=0;
	  indx2=0;
	  str='';
	
	  for (i=0;i<subarr.length;i++)
	   {
		  var s=strbuff.substr(startI,maxchars);
		    if(s.charAt(maxchars)==',' || s.charAt(maxchars)=='\n' || s.charAt(maxchars)==' ')
		  {
		    subarr[i]=strbuff.substr(startI,maxchars);
	        startI+=maxchars;
		  }
		  else
		  {
		    indx1=s.lastIndexOf(' ');
			indx2=s.lastIndexOf(',');
			if(indx1>indx2)
			{ 
			  subarr[i]=strbuff.substr(startI,indx1);
			  startI+=indx1;
			}
	   		else
			{
			  subarr[i]=strbuff.substr(startI,indx2);
			  startI+=indx2;
			} 
		 }  
		}
		for (i=0;i<subarr.length-1;i++)
	   {
	      newstr+=subarr[i]+
		  '<br />';
	   }
	   str+=subarr[subarr.length-1];
	   document.getElementById("TD"+y).innerHTML=newstr+str;
	}


}