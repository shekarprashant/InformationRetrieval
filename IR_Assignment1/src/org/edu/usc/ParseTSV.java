package org.edu.usc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
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

	
	private static final Set<MediaType> SUPPORTED_TYPES = 
	Collections.singleton(MediaType.text("tab-separated-values"));

	//public static final String CSV_MIME_TYPE = "text/tab-separated-values";
	
	@Override
	public Set<MediaType> getSupportedTypes(ParseContext context) {
	return SUPPORTED_TYPES;
	}

	@Override
	public void parse(InputStream inputStream, ContentHandler handler, Metadata metadata,
			ParseContext context) throws IOException, SAXException, TikaException {
		
		//XHTMLContentHandler xchHandler=new XHTMLContentHandler(handler, metadata);
		XHTMLContentHandler xchHandler=new XHTMLContentHandler(handler, metadata);
		
		//BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		CSVReader csvReader=new CSVReader(new BufferedReader(new InputStreamReader(inputStream)), '\t');
		
		xchHandler.startDocument();		
		xchHandler.startElement("table");
		
		xchHandler.startElement("th");
		xchHandler.characters("Date");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("2");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("3");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("4");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("5");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("6");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("7");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("8");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("9");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("10");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("11");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("12");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("13");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("14");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("15");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("16");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("17");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("18");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("19");
		xchHandler.endElement("th");
		
		xchHandler.startElement("th");
		xchHandler.characters("20");
		xchHandler.endElement("th");
		
		String line=new String();
		String[] splitLine=null;
		String cell=new String();
		
		String[] nextLine;		
		//while((line=)!=null)
		//for(int j=0;j<allLines.length;j++)
		int count=0;
		while ((nextLine = csvReader.readNext()) != null)
		{
			count++;
			//System.out.println("Line length:"+nextLine.length);
			if(nextLine.length!=20)
			{
				System.out.println("error:"+nextLine[0]+".length:"+nextLine.length);
			}
			xchHandler.startElement("tr");
			for(int k=0;k<nextLine.length;k++)
			{
			cell=nextLine[k];
			if(cell.equals("")||cell==null)
			{
				System.out.println("empty cell.Line count:"+count);
			}
			xchHandler.startElement("td");
			xchHandler.characters(cell);
			xchHandler.endElement("td");		
				
			}
			xchHandler.endElement("tr");						
		}
		xchHandler.endElement("table");
		
		xchHandler.endDocument();
		
		String xhtml = Test.sw.toString();
        System.out.println("xhtml:"+xhtml);
		
	}

}
