package org.edu.usc;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.ContentHandler;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class Test {

	static StringWriter sw = new StringWriter();
	public static void main(String[] args)
	{
		ParseTSV tsv=new ParseTSV();
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream("D:/IR/assignment1/computrabajo-ar-20121106.tsv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//BodyContentHandler handler=new BodyContentHandler(-1);
		JSONTableContentHandler handler=new JSONTableContentHandler();
		
		/*SAXTransformerFactory factory = (SAXTransformerFactory)
		SAXTransformerFactory.newInstance();
		TransformerHandler handler = null;
		try {
			handler = factory.newTransformerHandler();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
		handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "no");
		handler.setResult(new StreamResult(sw)); */ 
		Metadata metadata = new Metadata(); 
		
		try {
			tsv.parse(is, handler, metadata, new ParseContext());
		} catch (IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
