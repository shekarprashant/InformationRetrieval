package org.edu.usc;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class JSONTableContentHandler extends DefaultHandler{
	
	JSONArray array=new JSONArray();
	JSONObject object=null;
	List<Integer> headerNames=new ArrayList<Integer>();
	int row=0,cell=0,headerCount=0;
	@Override
	public void startElement(String uri, String local, String name, Attributes attributes)	
	{
		if(local.equalsIgnoreCase("th"))
		{
			headerNames.add(++headerCount);
		}
		else if(local.equalsIgnoreCase("td"))
		{
			cell++;
		}
		else if(local.equalsIgnoreCase("tr"))
		{
			row++;
		}			
	}
	
	@Override
	public void endElement(String uri,String local, String name)
	{
		if(local.equalsIgnoreCase("tr"))
		{
			if(cell==20)
			{
				cell=0;
			}
		}
	}
	
	@Override
	public void characters(char[] ch, int start,int length)
	{
		
		String str=null;
		if(cell==1)
		{
			object=new JSONObject();
			str=getStringFromCharArray(ch);
			object.put(headerNames.get(cell-1),str);
			str="";
		}
		else if(cell==20)
		{
			str=getStringFromCharArray(ch);
			object.put(headerNames.get(cell-1),str);
			str="";
			array.add(object);
			object=null;
		}
		else if(row>=1)
		{
			str=getStringFromCharArray(ch);
			object.put(headerNames.get(cell-1),str);
			str="";
		}	
		
	}
	
	private String getStringFromCharArray(char[] ch)
	{
		String str="";
		
		for(Character chr:ch)
		{
			str+=chr;
		}
		return str;
	}
	
	
}
