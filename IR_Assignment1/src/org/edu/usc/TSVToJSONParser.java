package org.edu.usc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.SAXException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class TSVToJSONParser {

	private static final String KEY_INDEXES = 
			"18";

	private static final String IGNORE_INDEXES = 
			"4";

	private static final String HEADER_NAMES = 
		"postedDate\t"
		+"location1\t"
		+"department\t"
		+"title\t"
		+"salary\t"
		+"start\t"
		+"duration\t"
		+"jobtype\t"
		+"applications\t"
		+"company\t"
		+"contactPerson\t"
		+"phoneNumber\t"
		+"faxNumber\t"
		+"location2\t"
		+"latitude\t"
		+"longitude\t"
		+"firstSeenDate\t"
		+"url\t"
		+"lastSeenDate";

	
	public static void main(String[] args) {
		long totalCount=0;
		ParseTSV tsv = new ParseTSV();
		
		if(args.length < 3)
		{
			System.out.println("Usage: TSVToJSONParser <Input_File_Directory> <Output_File_Directory> <Deduplication_Flag>");
			System.exit(1);
		}
		
		File outputDirectory = new File(args[1]);
		if(outputDirectory.exists())
			outputDirectory.delete();
		
		outputDirectory.mkdir();
		
		File inputDirectory = new File(args[0]);
		if(inputDirectory == null || !inputDirectory.exists())
		{
			System.out.println("Input Directory does not exist.");
			System.out.println("Usage: TSVToJSONParser <Input_File_Directory> <Output_File_Directory> <Deduplication_Flag(0/1)>");
			System.exit(1);
		}
		boolean deduplicationFlag = (Integer.parseInt(args[2]) == 1);
		
		for (File file : inputDirectory.listFiles()) {
			
			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {				
				e.printStackTrace();
			}
			
			JSONTableContentHandler handler = new JSONTableContentHandler();
			Metadata metadata = new Metadata();

			metadata.add(ParseTSV.ENABLE_DEDUPLICATION, deduplicationFlag?"1":"0");
			metadata.add(ParseTSV.HEADER_NAMES, HEADER_NAMES);
			metadata.add(ParseTSV.KEY_INDEXES, KEY_INDEXES);
			metadata.add(ParseTSV.IGNORE_INDEXES, IGNORE_INDEXES);

			try {
				tsv.parse(is, handler, metadata, new ParseContext());
				totalCount += handler.getArraySize();				
				System.out.println("Number of records parsed so far:"+totalCount);
				
				JsonArray jsonArrayResult = handler.getJsonArrayResult();
				
				for(int i = 0; i < jsonArrayResult.size(); i++)
				{	
					if(i % 100 == 0)
						System.out.println("Writing record number:" + i + " to disk. Total records:" + jsonArrayResult.size());
					JsonElement jsonElement = jsonArrayResult.get(i);
					JsonElement keyElement = jsonElement.getAsJsonObject().get("url");
					String fileName = "NoURL";
					if(keyElement != null)
						fileName = keyElement.toString().replaceAll("[./\\:]", "_").replace("\"", "");
					String jsonString = jsonElement.toString();
					String jsonFilePath = outputDirectory + "\\" + fileName + System.currentTimeMillis()+".json";
					FileWriter jsonFileWriter = null;
					try
					{
						jsonFileWriter = new FileWriter(jsonFilePath);
						 
						jsonFileWriter.write(jsonString);
						jsonFileWriter.flush();
						jsonFileWriter.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException | SAXException | TikaException e) {				
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
			
		}
	}
}