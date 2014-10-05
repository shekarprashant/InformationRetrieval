package org.edu.usc;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JSONTableContentHandler extends DefaultHandler {
	private JsonArray jsonArrayResult = new JsonArray();
	private JsonObject jsonObject = null;
	private List<String> headerNames = new ArrayList<String>();
	private int cell = 0;
	private int currentColumnCount = 0;
	private boolean isHeader = false;
	
	public void setTotalColumnsInARow(int currentColumnCount)
	{
		this.currentColumnCount = currentColumnCount;
	}
	
	public int getArraySize()
	{
		return jsonArrayResult.size();
	}
	
	public JsonArray getJsonArrayResult()
	{
		return jsonArrayResult;
	}
	
	@Override
	public void startElement(String uri, String local, String name,
			Attributes attributes) throws SAXException {
		if (local.equalsIgnoreCase("th")) {			
			isHeader = true;
		} else if (local.equalsIgnoreCase("td")) {			
			cell++;	
			isHeader = false;
		}
	}

	@Override
	public void endElement(String uri, String local, String name)
			throws SAXException {
		
		if (local.equalsIgnoreCase("tr")) {
			
			if (cell == currentColumnCount)
				cell = 0;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		
		if(currentColumnCount <= 0)
			return;
		
		if(isHeader)
		{
			headerNames.add(new String(ch));
			return;
		}

		String str = null;
		if (cell == 1) {
			jsonObject = new JsonObject();
			str = getStringFromCharArray(ch);
			jsonObject.addProperty(headerNames.get(cell - 1), str.trim());
		}
		else if(cell == currentColumnCount)
		{
			str = getStringFromCharArray(ch);
			jsonObject.addProperty(headerNames.get(cell - 1), str.trim());
			jsonArrayResult.add(jsonObject);
			jsonObject = null;
		} else {
			str = getStringFromCharArray(ch);
			jsonObject.addProperty(headerNames.get(cell - 1), str.trim());
		}
	}

	private String getStringFromCharArray(char[] ch) {
		String str = "";

		for (Character chr : ch) {
			str += chr;
		}
		return str;
	}

}