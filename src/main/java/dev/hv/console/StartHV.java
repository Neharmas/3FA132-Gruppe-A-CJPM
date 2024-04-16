package dev.hv.console;

import java.util.*;

import dev.hv.console.exceptions.InvalidArgumentException;

import dev.bsinfo.server.RESTServer;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;

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
	private DBDAO dbdao;
	RESTServer server;

	private String[] originalArgs;
	private ArrayList<String> convertedArgs;
	private ArgsParser argsParser;


	private StartHV() {
	}

	private void initialize(String[] args) {
		this.argsParser = new ArgsParser();
		this.dbdao = new DBDAO();
		
		this.dbdao.getDb().getJdbi();
		this.originalArgs = args;
		
		this.startRESTServer();
		convertAndRunArguments();
	}


	private void convertAndRunArguments() {
		try {
			convertedArgs = argsParser.preprocessArguments(originalArgs);
			processOption(convertedArgs);
		} catch (InvalidArgumentException e) {
			System.out.println("Could not run arguments due to a converting error. " +
					"Did you pass the correct arguments? Try running help");
		}

	}

	/**
	 * Determin which function path to take
	 * @param convertedArgs
	 */
	private void processOption(ArrayList<String> convertedArgs) {
		if (convertedArgs.contains("export")) {
			Exporter ex = new Exporter(dbdao);
			ex.process(convertedArgs);
		} else if (convertedArgs.contains("import")) {
			Importer imp = new Importer(argsParser, dbdao);
			imp.process(convertedArgs);
		} else if (convertedArgs.contains("-h") || convertedArgs.contains("--help"))
			help();
		else if (convertedArgs.contains("delete"))
			deleteAllTables();
	}



	private void loop() {
		Scanner scanner = new Scanner(System.in);
		
		try {
			while (true) {
				System.out.println("Please input new parameters or type exit:");
				String line = scanner.nextLine();
				System.out.printf("User input was: %s%n", line);
				if (line.equalsIgnoreCase("exit")) {
					server.close();
					return;
				}

				originalArgs = line.split(" ");

				convertAndRunArguments();

			}
		} catch(IllegalStateException | NoSuchElementException e) {
			// System.in has been closed
			System.out.println("System.in was closed; exiting");
		}
	}

	private void startRESTServer() {
		this.server = RESTServer.getInstance();
		this.server.run();
	}

	public static void main(String[] args) {
		StartHV c = new StartHV();
		c.initialize(args);
		c.loop();
	}

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
}