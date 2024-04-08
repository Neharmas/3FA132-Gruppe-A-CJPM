package dev.hv.console;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Jdbi;
import org.json.JSONArray;
import org.json.JSONObject;

import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.ressource.UserAPI;
import dev.bsinfo.server.StartServer;
import dev.bsinfo.server.test;
import dev.hv.db.init.DBConnect;

/** 
 * Try To Implement the 'Template Method'-Pattern:

 * If the arguments to the program are valid then
    Do necessary pre-processing
    For every line in the input
        Do necessary input processing
    Do necessary post-processing
	Otherwise
    Show the user a friendly usage message

  * PreProcess(): -validNumberOfArgs(args), -flagProvided(args)
	ProcessLine(): -determine help,delete,export or import
	PostProcess()
	Usage()
	Main()
 */

public class Console implements IConsole {
	private static DBConnect db = DBConnect.getConnection();
	
	private int requiredArgsDelete = 1;
	private ArrayList<String> requiredParamsDelete = new ArrayList<String>() {{ 
		// add("-d");
		add("--delete");
	}};

	private int requiredArgsHelp= 1;
	private ArrayList<String> requiredParamsHelp = new ArrayList<String>() {{ 
		add("-h");
		// add("--help");
	}};

	private int requiredArgsExport = 3;
	private ArrayList<String> requiredParamsExport = new ArrayList<String>() {{ 
		//+ <tableName>
		add("export");
		add("-o"); // with <fileOut>
		add("--output=");
	}};

	private int requiredArgsImport = 3;
	private ArrayList<String> requiredParamsImport = new ArrayList<String>() {{ 
		//+ <tableName>
		add("import");
		add("-i"); // with <fileIn>
		add("--input=");
	}};

	private int minFlags = 0;
	private int maxFlags = 1;
	private ArrayList<String> validFileFormatFlags = new ArrayList<String>() {{ 
		add("-c"); // .csv
		add("-j"); // .json
		add("-t"); // .txt
		add("-x"); // .xml
	}};

	private ArrayList<String> options = new ArrayList<String>() {{ 
		add("export");
		add("import");
	}};
	
	private ArrayList<String> customerKeys = new ArrayList<String>() {{ 
		add("id");
		add("firstname");
		add("lastname");
	}};
	
	private ArrayList<String> userKeys = new ArrayList<String>() {{ 
		add("id");
		add("firstname");
		add("lastname");
		add("token");
//		add("password");
	}};
	
	private ArrayList<String> readingKeys = new ArrayList<String>() {{ 
		add("comment");
		add("customer"); //Foreign key customer id
		add("kindofmeter");
		add("metercount");
		add("meterid");
		add("substitute");
		add("dateofreading");
	}};

	private boolean validNumberOfArgs(String[] args) {
		return (0 < args.length && args.length < 5);
	}
	
	private boolean flagProvided(ArrayList<String> providedArgs) {
		return providedArgs.contains("-");
	}
	
	private String getTableName(JSONObject table) {
		String tablename;
		
		tablename = table.names().toString(); //["<tablename>"]
		tablename = tablename.substring(2, tablename.length() - 2);
		
		return tablename;
	}
			
	private String getValidFileName(ArrayList<String> providedArgs) {
		String filename;
		int c = 0;
		for (String entry: providedArgs) {
			if (entry.contains("--output=")) {
					filename = entry.substring(9); // '='
					return filename;
			} 
			if (entry.contains("-o")) {
				filename = providedArgs.get(c+1);
				return filename;
			}
			if (entry.contains("--input=")) {
					filename = entry.substring(8); // '='
					return filename;
			} 
			if (entry.contains("-i")) {
				filename = providedArgs.get(c+1);
				return filename;
			}
			c++;
		}
		return "NoFilenameProvided";
	}
	
	private String getValidFileFlag(ArrayList<String> providedArgs) {
		ArrayList<String> validFileFlag = new ArrayList<String>();
		validFileFlag.add("-c");
		validFileFlag.add("-j");
		validFileFlag.add("-x");
		validFileFlag.add("-t");
		for (String entry : providedArgs) {
			for (int i = 0;  i <= 3; i++) {
				if (entry.contains(validFileFlag.get(i))) {
						return entry;
				}
			}
		}
		return "NoFlagProvided";
	}
	
	private String getValidFileExtension(String filename) {
		ArrayList<String> validFileExtension = new ArrayList<String>();
		validFileExtension.add(".csv");
		validFileExtension.add(".jso"); // 
		validFileExtension.add(".xml");
		validFileExtension.add(".txt");
		for (int i = 1;  i <= validFileExtension.size(); i++) {
			if (filename.subSequence(filename.length() - 3, filename.length()).equals(validFileExtension.get(i))) {
					return validFileExtension.get(i);
			}
		}
		return "NoExtensionProvided";
	}
	
	private String getValidTablename(ArrayList<String> providedArgs) {
		ArrayList<String> validTableNames = new ArrayList<String>();
		validTableNames.add("Customer");
		validTableNames.add("User");
		validTableNames.add("Reading");
		for (String entry : providedArgs) {
			for (int i = 0;  i <= 3; i++) { // use validTableNames.size() instead if time for more generical use
				if (entry.contains(validTableNames.get(i))) {
						return entry;
				}
			}
		}
		return "NoMatch";
	}

	private ArrayList<String> convertArgs(String[] args) {
		ArrayList<String> providedArgs = new ArrayList<String>();
		for(int i = 0; i < args.length; i++) {
			providedArgs.add(args[i]);
		}
		return providedArgs;
	}
	
	private void runHelpOrDelete(ArrayList<String> convertedArgs) {
		if (convertedArgs.contains("--delete")) 
			deleteAllTables();
		else 
			help();
	}
	
	private void processOption(ArrayList<String> convertedArgs) {
		if (convertedArgs.contains("export")) {
			processExport(convertedArgs);
		} else if (convertedArgs.contains("import")) {
			//processImport(convertedArgs);
		} else
			help();
	}
	
	private void processImport(ArrayList<String> convertedArgs) {
		// count of Args is valid ?
		if (convertedArgs.size() == 2) {
			help();
		}
		// flags provided ?-> abort : exportTable
		if (flagProvided(convertedArgs)) {
			// getValidFilename
			String filename = getValidFileName(convertedArgs);
			if (filename == "NoFilenameProvided") {
				System.out.println("Please provide a filename!");
				help();
			}
			// get filetype by .extention
			String filetype = getValidFileExtension(filename);
			if (filetype == "NoExtensionProvided") {
				System.out.println("Please provide a valid file extention!");
				help();
			}
			// file exists ?
			// read <filetype> (sowie layout<filetype> bei export)
			// getTableName -> table name out of 3 ?
			// table already exists ?-> addEntrys() : createTable(s)
			// write table to db
			
		}
		help();
	}
	
	private void processExport(ArrayList<String> convertedArgs) {
		// check if tablename valid
		String tableName = getValidTablename(convertedArgs);
		if (tableName == "NoMatch") {
			System.out.println("Invalid Table name: Choose 'Customer', 'User' or 'Reading'");
			help();
		}
		JSONObject table = new JSONObject();
		table = readTable(tableName);
		// size = 2 -> exportTableToConsole
		if (convertedArgs.size() == 2) {
			exportTableToConsole(table);
		}
		// flags provided ?-> abort : exportTable
		if (flagProvided(convertedArgs)) {
			String filename;
			filename = getValidFileName(convertedArgs);
			if (filename == "NoFilenameProvided") {
				System.out.println("Please provide a filename!");
				help();
			}
			exportTable(convertedArgs, table, filename);
		}
	}
	
	private String layoutCSV(JSONObject table) {
		String csv, tablename, header = "", rows = "";
		ArrayList<String> keys = new ArrayList<String>();
		
		tablename = getTableName(table);
		
		System.out.println("Tablename: " + tablename);
		switch(tablename) {
			case "Customer": keys = customerKeys; break;
			case "User": keys = userKeys; break; 
			case "Reading": keys = readingKeys; break;
		}

		for (String element : keys) {
			header = header + element + ";";
		}
		header = header.substring(0, header.length() - 1);

		// append Column Values Accordingly
		int entrys = table.getJSONArray(tablename).length();
		String row = "";
		for (int i=0; i < entrys; i++) {
			row = row + "\n";
			for (String key: keys) {
				String value = table.getJSONArray(tablename).getJSONObject(i).get(key).toString();
				row = row + value + "; ";
			}
			row = row.substring(0, row.length() - 2);
		}
		
		csv = header + row + "\n";
		
		return csv;
	}

	private String layoutXML(JSONObject table) {
		String xml, tablename, header = "", rows = "";
		ArrayList<String> keys = new ArrayList<String>();
		
		tablename = getTableName(table);

		switch(tablename) {
			case "Customer": keys = customerKeys; break;
			case "User": keys = userKeys; break; 
			case "Reading": keys = readingKeys; break;
		}

		// set Heading Accordingly
		header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
		header = header + "<" + tablename + ">" + "\n";

		// append Column Values Accordingly
		int entrys = table.getJSONArray(tablename).length();
		String row = "";
		for (int i=0; i < entrys; i++) {
			row = row + "  <Entry> \n";
			for (String key: keys) {
				String value = table.getJSONArray(tablename).getJSONObject(i).get(key).toString();
				row = row + "\t<" + key + ">" + value + "</" + key + ">\n";
			}
			row = row + "  </Entry> \n";
		}

		String footer = "</" + tablename + ">" + "\n";
		xml = header + row + footer;
		
		return xml;
	}


	private String layoutText(JSONObject table) {
		String text, tablename,header = "", rows = "";
		ArrayList<String> keys = new ArrayList<String>();
		
		tablename = getTableName(table);

		switch(tablename) {
			case "Customer": keys = customerKeys; break;
			case "User": keys = userKeys; break; 
			case "Reading": keys = readingKeys; break;
		}

		// set Header Accordingly
		for (String element : keys) {
			header = header + element + " | ";
		}
		header = header.substring(0, header.length() - 2);
		header = header + "\n============================================ \n";

		// append Column Values Accordingly
		int entrys = table.getJSONArray(tablename).length();
		String row = "";
		for (int i=0; i < entrys; i++) {
			for (String key: keys) {
				String value = table.getJSONArray(tablename).getJSONObject(i).get(key).toString();
				row = row + value + " | ";
			}
			row = row.substring(0, row.length() - 2);
			row = row + "\n-------------------------------------------- \n";
		}
		
		text = header + row;
		
		return text;
	}


	private void exportTableToCSV(JSONObject table, String filename) {
		String layouted = layoutCSV(table);
		writeFile(layouted, filename);
	}
	
	private void exportTableToTxt(JSONObject table, String filename) {
		String layouted = layoutText(table);
		writeFile(layouted, filename);
	}

	private void exportTableToXML(JSONObject table, String filename) {
		String layouted = layoutXML(table);
		writeFile(layouted, filename);
	}
	
	private void exportTableToJSON(JSONObject table, String filename) {
		String layouted = layoutJSON(table);
		writeFile(layouted, filename);
	}	

	private void exportTableToConsole(JSONObject table) {
		String layouted = layoutJSON(table);
		System.out.println(layouted);
	}

	private void writeFile(String content, String filename) {
		try { 
			BufferedWriter w = new BufferedWriter(new FileWriter(filename));
			w.write(content);
			w.close();
		} catch (IOException e) {
			System.out.print("Error");
			help();
		}
	}
	
	public void exportTable(ArrayList<String> convertedArgs, JSONObject table, String filename) {
		switch(getValidFileFlag(convertedArgs)) {
			case "-c": exportTableToCSV(table, filename + ".csv");
			case "-x": exportTableToXML(table, filename + ".xml");
			case "-t": exportTableToTxt(table, filename + ".txt");
			case "-j":
			default: exportTableToJSON(table, filename + ".json");
		}
	}
	
	private JSONArray listMapToJSON(List<Map<String, Object>> table) {
		JSONArray json = new JSONArray(table);
		return json;
	}
	
	private JSONObject readTable(String tablename) {
		JSONArray tableListJSON = listMapToJSON(db.readTable(tablename));
		
		String buildString = "{" + tablename + ": " + tableListJSON + "}";
		//System.out.println(buildString);

		JSONObject wholeTableJSON = new JSONObject(buildString);
		
		return wholeTableJSON;
	}
	
	private String layoutJSON(JSONObject json) {
		String raw = json.toString();
		String layouted;
		
		raw = new StringBuffer(raw).insert(1, "\n  ").toString();
		layouted = raw.replace("[", "\n    [\n\t");
		layouted = layouted.replace("},{", "}, \n\t{");
		layouted = layouted.replace("]", "\n    ]\n");
		
		return layouted;
	}
	
	private void preproccess(String[] args) {
		ArrayList<String> convertedArgs;
		if (validNumberOfArgs(args)) {
			convertedArgs = convertArgs(args);
			switch(convertedArgs.size()) {
				case 1: runHelpOrDelete(convertedArgs);
				case 2: 
				default: processOption(convertedArgs); 
			}
		} else
			help();
	}
	

	@Override
	public void help() { 
		String helpHeader = "GruppeA \t cli-rest-client \t " + System.getProperty("java.version") + "\n";
		String helpIntro = "Usage: java " + System.getProperty("user.dir") + "[options] [-f] [-cmd/--command]" + "\n";
		String helpDescription = "export [-f] <table> = prints <table> in terminal" + "\n"
				+ " -o, --output=<file> \t = prints <table> in <file>" + "\n"
				+ "\t -c = as .csv" + "\n"
				+ "\t -j = as .json" + "\n"
				+ "\t -x = as .xml" + "\n"
				+ "\t -t = as .txt" + "\n"
				+ "import [-i/--input=] <file> = import <table> from <file> into db" + "\n"
				+ " -i, --input=<file> = define <file> which should be imported" + "\n" + "\n"
				+ "--delete = drop all tables" + "\n"
				+ "--help = print this menu" + "\n";
		String helpFooter = "Report bugs to https://github.com/xKURDOx !";
		System.out.println(helpHeader + helpIntro + helpDescription + helpFooter);
	}

	@Override
	public void deleteAllTables() {
		db.removeAllTables();
		System.out.println("purged all Tables");
	}

	@Override
	public File exportTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void importTable(File inputfile) {
		// TODO Auto-generated method stub
	}

	private void processImport() {}

	public static void main(String[] args) {
		Console c = new Console();
		StartServer server = StartServer.getInstance();
		server.run();
		

		Jdbi jdbi = db.getJdbi();
		
		c.deleteAllTables();

		db.createAllTables();
		db.insertTestData();

		JSONObject user, customer, reading = new JSONObject();
		user = c.readTable("User");
		customer = c.readTable("Customer");
		reading = c.readTable("Reading");
		
		c.exportTableToConsole(reading);
		c.exportTableToCSV(reading, "reading.csv");
		c.exportTableToTxt(reading, "reading.txt");
		c.exportTableToXML(reading, "reading.xml");
		
	}
	
}