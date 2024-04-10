package dev.hv.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsParser {
    /**
     * Parses the arguments and filters the Filename behind the --output=/-o or --input=/-i
     * @param providedArgs
     * @return the filename or "NoFileNameProvided"
     */
    String getValidFileName(ArrayList<String> providedArgs) {
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

    String getValidFileFlag(ArrayList<String> providedArgs) {
        ArrayList<String> validFileFlags = new ArrayList<>();
        validFileFlags.add("-c");
        validFileFlags.add("-j");
        validFileFlags.add("-x");
        validFileFlags.add("-t");
        for (String entry : providedArgs) {
            if (validFileFlags.contains(entry)) {
                return entry;
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

    /**
     * Reads the passed arguments and returns the valid table name if one is found in them.
     * TODO rework this mess.
     * @param args
     * @return Tablename as String or "NoMatch"
     * @throws NoValidTableNameException if there was no valid tablename provided (in the args)
     */
    static String getValidTableNameIfExists(ArrayList<String> args) throws NoValidTableNameException {
        ArrayList<String> validTableNames = new ArrayList<String>();
        validTableNames.add("Customer");
        validTableNames.add("User");
        validTableNames.add("Reading");
        for (String entry : args) {
            for (String tableName: validTableNames) { // use validTableNames.size() instead if time for more generical use
                if (entry.equalsIgnoreCase(tableName)) { // THIS IS NOT AT ALL A GOOD CHECK OR ... ANYHTING
                    return tableName;
                }
            }
        }
        throw new NoValidTableNameException("Could not find a tablename. please use one of " + validTableNames.toString());
    }
    public static class NoValidTableNameException extends Exception {
        public NoValidTableNameException(String errorMessage) {
            super(errorMessage);
        }
    }
    ArrayList<String> convertStringArgsToArrayList(String[] args) {
        return new ArrayList<>(List.of(args)); //if i understand .asList returns a list that stays synced with the og-array, lol.
    }

    boolean flagProvided(ArrayList<String> providedArgs) {
        for (String args: providedArgs) {
            if (args.startsWith("-")) return true;
        }
        return false;
    }
}
