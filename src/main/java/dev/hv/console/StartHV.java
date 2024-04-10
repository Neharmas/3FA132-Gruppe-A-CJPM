package dev.hv.console;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.jdbi.v3.core.Jdbi;

import dev.bsinfo.server.RESTServer;

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

public class StartHV {
	private int requiredArgsDelete = 1;
	private DBDAO dbdao;
	private FileUtil fileUtil;
	private Jdbi jdbi;
	private String[] originalArgs;
	private ArrayList<String> convertedArgs;
	private ArgsParser argsParser;


	private StartHV() {
	}

	private void initialize(String[] args) {
		this.fileUtil = new FileUtil();
		this.argsParser = new ArgsParser();
		this.dbdao = new DBDAO();

		this.jdbi = dbdao.getDb().getJdbi();
		this.originalArgs = args;

		this.startRESTServer();
	}

	boolean preprocessArguments() {
		if (!validateNumberOfArgs(originalArgs)) {
			return false;
		}
		convertedArgs = argsParser.convertStringArgsToArrayList(originalArgs);
		return true;
	}

	/**
	 * Basically the "real" main-function of this class. Initializes and starts everything and then reads the passed arguments.
	 * @return 0 if something expected didnt work (mostly input-errors; help was printed most likely). 1 if everything went smooth and -1 in other cases.
	 */
	private int runArguments() {
		//DUMMY-SHIT for the fun.
		/*
		this.deleteAllTables(); //WHY...

		dbdao.createAllTables();
		dbdao.insertTestData();

		JSONObject user, customer, reading = new JSONObject();
		user = dbdao.readTable("User");
		customer = dbdao.readTable("Customer");
		reading = dbdao.readTable("Reading");

		this.exportTableToConsole(reading);
		this.exportTableToCSV(reading, "reading.csv");
		this.exportTableToTxt(reading, "reading.txt");
		this.exportTableToXML(reading, "reading.xml");*/
		deleteAllTables();
		dbdao.createAllTables();
		dbdao.insertTestData();
		System.out.println(Arrays.toString(originalArgs));

		try {
			processOption(convertedArgs);
		} catch (IOException ex) {
			System.out.println("An IOException occured while executing your command (this probably means the writing failed.)");
			ex.printStackTrace();
			return -1;
		}
		return 1;
	}

	/**
	 * Determin which function path to take
	 * @param convertedArgs
	 * @throws IOException
	 */
	private void processOption(ArrayList<String> convertedArgs) throws IOException {
		if (convertedArgs.contains("export")) {
			Exporter ex = new Exporter(argsParser, fileUtil, dbdao);
			if (!ex.processExport(convertedArgs)) help();
		} else if (convertedArgs.contains("import")) {
			Importer imp = new Importer();
			imp.processImport(convertedArgs);
		} else if (convertedArgs.contains("-h") || convertedArgs.contains("--help"))
			help();
		else if (convertedArgs.contains("delete"))
			deleteAllTables();
	}

	private boolean validateNumberOfArgs(String[] args) {
		int empty = 0;
		int tooMany = 5;
        return (args.length != empty) && (args.length <= tooMany);
	}

	private int loop() {
		Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				System.out.println("Please input new parameters or type exit:");
				long then = System.currentTimeMillis();
				String line = scanner.nextLine();
				long now = System.currentTimeMillis();
				System.out.printf("Waited %.3fs for user input%n", (now - then) / 1000d);
				System.out.printf("User input was: %s%n", line);
				if (line.equalsIgnoreCase("exit")) { return 0;}

				originalArgs = line.split(" ");
				if (!preprocessArguments()) {
					help();
				} else {
					runArguments();
				}
				//re-init the cli (this is so dumb)
			}
		} catch(IllegalStateException | NoSuchElementException e) {
			// System.in has been closed
			System.out.println("System.in was closed; exiting");
			return 1;
		}
	}

	private void startRESTServer() {
		RESTServer server = RESTServer.getInstance();
		server.run();
	}

	public static void main(String[] args) {
		StartHV c = new StartHV();
		c.initialize(args);
		if (!c.preprocessArguments()) c.help();
		else c.runArguments();
		c.loop();
	}



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

	/*
	private void runHelpOrDelete(ArrayList<String> convertedArgs) {
		if (convertedArgs.contains("--delete")) 
			deleteAllTables();
		else 
			help();
	}*/

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

	public void deleteAllTables() {
		dbdao.removeAllTables();
		System.out.println("purged all Tables");
	}

	public File exportTable() {
		// TODO Auto-generated method stub
		return null;
	}

	public void importTable(File inputfile) {
		// TODO Auto-generated method stub
	}
}