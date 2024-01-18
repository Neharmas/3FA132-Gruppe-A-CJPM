package dev.hv.console;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.ressource.UserAPI;
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

  * PreProcess()
	ProcessLine()
	PostProcess()
	Usage()
	Main()
 */

public class Console implements IConsole {
	private DBConnect db = DBConnect.getConnection();
	
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

	private boolean validNumberOfArgs(String[] args) {
		return (0 < args.length && args.length < 5);
	}
	
	private boolean flagProvided(String[] args) {
		return Arrays.stream(args).anyMatch("-"::contains);
	}

	private ArrayList<String> convertArgs(String[] args) {
		ArrayList<String> providedArgs = new ArrayList<String>();
		for(int i = 0; i < args.length; i++) {
			providedArgs.add(args[i]);
		}
		return providedArgs;
	}
	
	private void preproccess(String[] args) {
		if (validNumberOfArgs(args) && flagProvided(args))
			// Dann weiter Verarbeitetn
		else
			help();
	}
	
	private void runCommand(String[] args) {
		// Falls --delete delete ansonsten help()
		if (Arrays.stream(args).anyMatch("-"::contains);
		
	}
	private void runOptions(ArrayList<String> providedArgs ) {}
	private void getFlags(String[] args) {}

	private void buildRestCall(String[] args) {}
	private void validateRestCall(String[] args) {}
	private void executeRestCall(String[] args) {}

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