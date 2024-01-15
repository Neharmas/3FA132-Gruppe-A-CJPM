package dev.hv.console;

import java.io.File;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.ressource.UserAPI;
import dev.hv.db.init.DBConnect;


public class Console implements IConsole {
	private DBConnect db = DBConnect.getConnection();

	//empty private constructor to keep this class 'secret'.
	//(there also is no way to access this instance from outside)
	private Console(String[] args) {
		System.out.println(Arrays.toString(args));
		if (!validateNumberOfArgs(args)) {
			help();
		} else {
			loop();
		}
	};

	/**
	 * main-loop of the cli
	 * technically the instructions dont ask for this but i would still implement a loop.
	 * Which means the main program would run something like this:
	 * - gets started via java -jar dev.hv.console.Console
	 * - first argument-check (reading the args from the void-main)
	 * print error or run command
	 * - main runs and starts the loop.
	 * asks for a new command; which will start the chain again.
	 *
	 * Still needs to be implemented tho
	 * @return 0 for success, 1 for error, 2: timeout, 3: request not send, 4: init failed
	 */
	private int loop() {
		Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				System.out.println("Please input a line");
				long then = System.currentTimeMillis();
				String line = scanner.nextLine();
				long now = System.currentTimeMillis();
				System.out.printf("Waited %.3fs for user input%n", (now - then) / 1000d);
				System.out.printf("User input was: %s%n", line);
				if (line.equalsIgnoreCase("exit")) { return 0;}
			}
		} catch(IllegalStateException | NoSuchElementException e) {
			// System.in has been closed
			System.out.println("System.in was closed; exiting");
			return 1;
		}
	}

	private boolean validateNumberOfArgs(String[] args) {
		int empty = 0;
		int tooMany = 5;
		return (args.length != empty) && (args.length <= tooMany);
	}

	private void readArgs(String[] args) {
		validateNumberOfArgs(args);
		for(int i = 0; i < args.length; i++) {

		}
	}
	private void validateArgs(String[] args) {}
	private void executeArgs(String[] args) {}

	@Override
	public void help() {
		StringBuilder sb = new StringBuilder();
		String helpHeader = "GruppeA \t cli-rest-client \t " + System.getProperty("java.version") + "\n";
		String helpIntro = "Usage: java " + System.getProperty("user.dir") + "[options] [-f] [-cmd/--command]" + "\n";
		String helpDescription = "export [-f] <table> = prints <table> in terminal" + "\n"
				+ " -o, --output=<file> \t = prints <table> in <file>" + "\n"
				+ "\t -c = as .csv" + "\n"
				+ "\t -j = as .json" + "\n"
				+ "\t -x = as .xml" + "\n"
				+ "\t -t = as .txt" + "\n"
				+ "import [-f] <table> = import <table> from <file> into db" + "\n"
				+ " -i, --input=<file> = define <file> which should be imported" + "\n" + "\n"
				+ "--delete = drop all tables" + "\n"
				+ "--help = print this menu" + "\n";
		String helpFooter = "Report bugs to https://github.com/xKURDOx !";
		sb.append(helpHeader).append(helpIntro).append(helpDescription).append(helpFooter);
		System.out.println(sb);
	}

	@Override
	public void deleteAllTables() {
		db.removeAllTables();
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

	public static void main(String[] args) {
		Console c = new Console(args);
	}
}