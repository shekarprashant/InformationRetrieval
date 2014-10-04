package org.edu.usc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JSONTableContentHandler extends DefaultHandler {
	JSONArray array = new JSONArray();
	JSONObject object = null;
	List<String> headerNames = new ArrayList<String>();
	int row = 0, cell = 0, headerIndex = 0, headerCount = 0,jsonCount =0;

	public int getArraySize()
	{
		return array.size();
	}
	@Override
	public void startElement(String uri, String local, String name,
			Attributes attributes) throws SAXException {
		if (local.equalsIgnoreCase("th")) {
			// super.startElement(null,"th","th",null);
			headerCount++;
			// headerNames.add();
		} else if (local.equalsIgnoreCase("td")) {
			// super.startElement(null,"td","td",null);
			cell++;
			
			if(cell != 3)
				headerIndex++;
			
		} else if (local.equalsIgnoreCase("tr")) {
			// super.startElement(null,"th","th",null);
			row++;
		} else if (local.equalsIgnoreCase("table")) {
			// super.startElement(null,"table","table",null);
		}
	}

	@Override
	public void endElement(String uri, String local, String name)
			throws SAXException {
		
		if (local.equalsIgnoreCase("tr")) {
			// super.endElement(null,"tr","tr");
			if (cell == 20) {
				cell = 0;
				headerIndex = 0;
				jsonCount++;
			}
		}

		else if (local.equalsIgnoreCase("table")) {
			// super.endElement("table","table",null);
		}

		if (local.equalsIgnoreCase("td")) {
			// super.endElement(null,"td","td");
		}
		if (local.equalsIgnoreCase("th")) {
			// super.endElement(null,"th","th");
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
			
		if(headerCount<20)
		{
			switch (headerCount) {
			case 1:
				if(!headerNames.contains("posted"))
					headerNames.add("posted");
				break;
			case 2:
				if(!headerNames.contains("location1"))
				headerNames.add("location1");
				break;
			case 3:
				if(!headerNames.contains("department"))
				headerNames.add("department");
				break;
			case 4:
				if(!headerNames.contains("title"))
				headerNames.add("title");
				break;
			case 5:
				if(!headerNames.contains("salary"))
				headerNames.add("salary");
				break;
			case 6:
				if(!headerNames.contains("start"))
				headerNames.add("start");
				break;
			case 7:
				if(!headerNames.contains("duration"))
				headerNames.add("duration");
				break;
			case 8:
				if(!headerNames.contains("jobtype"))
				headerNames.add("jobtype");
				break;
			case 9:
				if(!headerNames.contains("applications"))
				headerNames.add("applications");
				break;
			case 10:
				if(!headerNames.contains("company"))
				headerNames.add("company");
				break;
			case 11:
				if(!headerNames.contains("contactPerson"))
				headerNames.add("contactPerson");
				break;
			case 12:
				if(!headerNames.contains("phoneNumber"))
				headerNames.add("phoneNumber");
				break;
			case 13:
				if(!headerNames.contains("faxNumber"))
				headerNames.add("faxNumber");
				break;
			case 14:
				if(!headerNames.contains("location2"))
				headerNames.add("location2");
				break;
			case 15:
				if(!headerNames.contains("latitude"))
				headerNames.add("latitude");
				break;
			case 16:
				if(!headerNames.contains("longitude"))
				headerNames.add("longitude");
				break;
			case 17:
				if(!headerNames.contains("firstSeenDate"))
				headerNames.add("firstSeenDate");
				break;
			case 18:
				if(!headerNames.contains("url"))
				headerNames.add("url");
				break;
			case 19:
				if(!headerNames.contains("lastSeenDate"))
				headerNames.add("lastSeenDate");
				break;			
			}
		}
		JSONObject locObject=null;
		String str = null;
		if (cell == 1) {
			object = new JSONObject();
			str = getStringFromCharArray(ch);
			
			object.put(headerNames.get(headerIndex - 1), str.trim());
			
			str = "";
		} else if (cell == 20) {
			str = getStringFromCharArray(ch);
			
			object.put(headerNames.get(headerIndex - 1), str);
			
			str = "";
			//create the json file
			/*String fileName=object.get("url").toString().replace(".", "_").replace('/', '_').replace("\\","_");
			String uglyJson=object.toJSONString();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(uglyJson);
			String beautifulJson=gson.toJson(je);
			String jsonFilePath="/Users/phalkurumashanka/Documents/USC/FALL 2014/CSCI572/Assignments/Assignment1/JSONWithDedup/"+fileName+System.currentTimeMillis()+".json";
			FileWriter jsonFileWriter = null;
			try
			{
				jsonFileWriter = new FileWriter(jsonFilePath);
				 
				jsonFileWriter.write(beautifulJson);
				jsonFileWriter.flush();
				jsonFileWriter.close();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			array.add(object);
			//System.out.println("Array size:"+array.size());
			object = null;
		} else if (row >= 1) {
			str = getStringFromCharArray(ch);
			
			if(cell == 2)
			{
				locObject=new JSONObject();
				locObject.put("city", str);
				object.put(headerNames.get(1), locObject);
			}
			else if (cell == 3)
			{
				locObject=(JSONObject) object.get(headerNames.get(1));	
				locObject.put("state", str);
			}
			else
			{
				object.put(headerNames.get(headerIndex - 1), str.trim());
			}
			str = "";
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
