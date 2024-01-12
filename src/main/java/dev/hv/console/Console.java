package dev.hv.console;

import java.io.File;

import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.ressource.UserAPI;
import dev.hv.db.init.DBConnect;


public class Console implements IConsole {
	private DBConnect db = DBConnect.getConnection();

	private void validateNumberOfArgs(String[] args) {
		int empty = 0;
		int toMany = 5;
		if (!(args.length == empty || args.length >= toMany)) {
			help();
		}
	}

	private void readArgs(String[] args) {
		validateNumberOfArgs(args);
		for(int i = 0; i < args.length; i++) {
			
		}
	}
	private void validateArgs(String[] args) {}
	private void executeArgs(String[] args) {}

	public static void main(String[] args) {
		Console c = new Console();
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
				+ "import [-f] <table> = import <table> from <file> into db" + "\n"
				+ " -i, --input=<file> = define <file> which should be imported" + "\n" + "\n"
				+ "--delete = drop all tables" + "\n"
				+ "--help = print this menu" + "\n";
		String helpFooter = "Report bugs to https://github.com/xKURDOx !";
		System.out.println(helpHeader + helpIntro + helpDescription + helpFooter);
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
}