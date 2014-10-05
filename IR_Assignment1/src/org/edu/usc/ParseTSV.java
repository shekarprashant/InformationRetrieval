package org.edu.usc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

	private static final long serialVersionUID = 1L;
	
	private XHTMLContentHandler xchHandler = null;
	private static final Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.text("tab-separated-values"));
	private Set<String> keySet = new HashSet<String>();
	
	public static final String ENABLE_DEDUPLICATION = "enableDeduplication";
	public static final String HEADER_NAMES = "headerNames";
	public static final String KEY_INDEXES = "keyIndexes";
	public static final String IGNORE_INDEXES = "ignoreIndexes";
	
	@Override
	public Set<MediaType> getSupportedTypes(ParseContext context) {
		return SUPPORTED_TYPES;
	}

	
	@Override
	public void parse(InputStream inputStream, ContentHandler handler, Metadata metadata,
			ParseContext context) throws IOException, SAXException, TikaException {
		
		xchHandler=new XHTMLContentHandler(handler, metadata);
		xchHandler.startDocument();		
		xchHandler.startElement("table");

		String headerNames = metadata.get(HEADER_NAMES);		
		if(headerNames == null)
			throw new NullPointerException();
		
		String enableDeduplication = metadata.get(ENABLE_DEDUPLICATION);		
		if(enableDeduplication == null)
			throw new NullPointerException();
		
		boolean dedupFlag = enableDeduplication.equals("1");
		
		String keyIndexes = metadata.get(KEY_INDEXES);
		String ignoreIndexes = metadata.get(IGNORE_INDEXES);
		
		
		String[] headerNamesArray = headerNames.split("\t");
		JSONTableContentHandler jsonHandler=(JSONTableContentHandler)handler;
		jsonHandler.setTotalColumnsInARow(headerNamesArray.length);
		for(int i=0; i < headerNamesArray.length; i++)
		{
			xchHandler.startElement("th");
			xchHandler.characters(headerNamesArray[i]);
			xchHandler.endElement("th");
		}
		
		
		List<Integer> keyIndexList = null;
		if (keyIndexes != null)
		{	
			String[] keyIndexesArray = keyIndexes.split("\t");
			keyIndexList = new ArrayList<>();
			for(int i=0; i < keyIndexesArray.length; i++)
			{
				int keyIndex = Integer.parseInt(keyIndexesArray[i]);
				if(keyIndex >= 0)
					keyIndexList.add(keyIndex);
			}
		}
		
		List<Integer> ignoreIndexList = null;
		if (keyIndexes != null)
		{	
			String[] ignoreIndexArray = ignoreIndexes.split("\t");
			ignoreIndexList = new ArrayList<>();
			for(int i=0; i < ignoreIndexArray.length; i++)
			{
				int ignoreIndex = Integer.parseInt(ignoreIndexArray[i]);
				if(ignoreIndex >= 0)
					ignoreIndexList.add(ignoreIndex);
			}
		}
		
		
		String cell;		
		String[] nextLine;		
		
		CSVReader csvReader=new CSVReader(new BufferedReader(new InputStreamReader(inputStream)), '\t');
		
		while ((nextLine = csvReader.readNext()) != null)
		{
	
			
			jsonHandler.setTotalColumnsInARow(nextLine.length - ignoreIndexList.size());
			
			StringBuffer keyStringBuffer = new StringBuffer();
			String keyString = null;
			if (keyIndexes == null)
			{
				for(int i = 0; i < nextLine.length; i++)
					keyStringBuffer.append(nextLine[i]);
			}
			else
			{
				for(Integer keyIndex: keyIndexList)
				{
					if (nextLine.length > keyIndex)
						keyStringBuffer.append(nextLine[keyIndex]);
				}
			}
			if(keyStringBuffer.length() > 0)
				keyString = keyStringBuffer.toString();
			
			if(!dedupFlag || !keySet.contains(keyString))
			{
				if(dedupFlag) 
					keySet.add(keyString);
				
				xchHandler.startElement("tr");
				
				for(int k = 0; k < nextLine.length; k++)
				{

					if(ignoreIndexList != null && ignoreIndexList.contains(k))
						continue;

					xchHandler.startElement("td");					
					cell = nextLine[k];					
					if(cell == null || cell.equals(""))
							xchHandler.characters(" ");
					else
							xchHandler.characters(cell);					
					
					xchHandler.endElement("td");
				}
				xchHandler.endElement("tr");
			}
		}
		csvReader.close();
		
		xchHandler.endElement("table");		
		xchHandler.endDocument();
		
	}

}