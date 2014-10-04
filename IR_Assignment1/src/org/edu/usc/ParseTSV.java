package org.edu.usc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import au.com.bytecode.opencsv.CSVReader;


public class ParseTSV extends AbstractParser{

	XHTMLContentHandler xchHandler=null;
	private static final Set<MediaType> SUPPORTED_TYPES = 
	Collections.singleton(MediaType.text("tab-separated-values"));
	Hashtable<String, Boolean> ht = new Hashtable<String, Boolean>();
	Set<String> urlSet=new HashSet<String>();
	//public static final String CSV_MIME_TYPE = "text/tab-separated-values";
	
	@Override
	public Set<MediaType> getSupportedTypes(ParseContext context) {
	return SUPPORTED_TYPES;
	}

	private static final String[] HEADER_NAMES = {
		"postedDate",
		"location1",
		"department",
		"title",
		"salary",
		"start",
		"duration",
		"jobtype",
		"applications",
		"company",
		"contactPerson",
		"phoneNumber",
		"faxNumber",
		"location2",
		"latitude",
		"firstSeenDate",
		"url",
		"lastSeenDate"
		};
	
	@Override
	public void parse(InputStream inputStream, ContentHandler handler, Metadata metadata,
			ParseContext context) throws IOException, SAXException, TikaException {
		
		//XHTMLContentHandler xchHandler=new XHTMLContentHandler(handler, metadata);
		xchHandler=new XHTMLContentHandler(handler, metadata);
		
		//BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		CSVReader csvReader=new CSVReader(new BufferedReader(new InputStreamReader(inputStream)), '\t');
		
		xchHandler.startDocument();		
		xchHandler.startElement("table");
		
		xchHandler.startElement("th");
		xchHandler.characters("postedDate");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("location1");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("department");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("title");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("salary");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("start");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("duration");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("jobtype");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("applications");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("company");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("contactPerson");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("phoneNumber");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("faxNumber");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("location2");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("latitude");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("longitude");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("firstSeenDate");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("url");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("lastSeenDate");
		xchHandler.endElement("th");		
		
		String cell=new String();
		
		String[] nextLine;		
		
		int count=0;
		while ((nextLine = csvReader.readNext()) != null)
		{
			count++;
			//System.out.println("Line length:"+nextLine.length);
			if(nextLine.length<20)
			{
				System.out.println("error.Fields are less than 20:"+nextLine[0]+".length:"+nextLine.length);
				System.out.println("Line count:"+count);
				continue;
			}			
			
			//if(metadata.get("enableDeduplication").equals("0") || !ht.containsKey(nextLine[18]))
			if(metadata.get("enableDeduplication").equals("0") || !urlSet.contains(nextLine[18]))
			{
				if(nextLine[18].trim().equals(""))
					continue;
					
				urlSet.add(nextLine[18]);
					
				xchHandler.startElement("tr");
				
				for(int k=0;k<nextLine.length;k++)
				{
					xchHandler.startElement("td");
					cell=nextLine[k];					
					
					if(cell.equals("")||cell==null)
							xchHandler.characters(" ");
					else
							xchHandler.characters(cell);					
					
					xchHandler.endElement("td");
				}
				xchHandler.endElement("tr");
			}
		}
		xchHandler.endElement("table");
		
		xchHandler.endDocument();
		
	}

}
