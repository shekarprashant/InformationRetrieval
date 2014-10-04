package org.edu.usc;

import java.io.BufferedInputStream;
import java.io.File;
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

	public static void main(String[] args) {
		StringWriter sw = new StringWriter();
		long totalCount=0;
		ParseTSV tsv = new ParseTSV();
		File dir = new File("D:/json");
		dir.mkdir();
		File files = new File("D:\\IR\\Data");
		for (File file : files.listFiles()) {
			System.out.println("File name:"+file.getName());
			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// BodyContentHandler handler=new BodyContentHandler(-1);
			JSONTableContentHandler handler = new JSONTableContentHandler();

			/*
			 * SAXTransformerFactory factory = (SAXTransformerFactory)
			 * SAXTransformerFactory.newInstance(); TransformerHandler handler =
			 * null; try { handler = factory.newTransformerHandler(); } catch
			 * (TransformerConfigurationException e1) { // TODO Auto-generated
			 * catch block e1.printStackTrace(); }
			 * handler.getTransformer().setOutputProperty(OutputKeys.METHOD,
			 * "html");
			 * handler.getTransformer().setOutputProperty(OutputKeys.INDENT,
			 * "no"); handler.setResult(new StreamResult(sw));
			 */
			Metadata metadata = new Metadata();
			Boolean dedup = true;
			if (dedup)
				metadata.add("enableDeduplication", "1");
			else
				metadata.add("enableDeduplication", "0");

			try {
				tsv.parse(is, handler, metadata, new ParseContext());
				totalCount+=handler.getArraySize();
				System.out.println("total count:"+totalCount);
				String xhtml = tsv.xchHandler.toString();
				System.out.println("xhtml:" + xhtml);
			} catch (IOException | SAXException | TikaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
