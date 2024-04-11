package dev.hv.console;

import dev.hv.console.exceptions.InvalidArgumentException;
import dev.hv.console.exceptions.NoValidFileNameException;
import dev.hv.console.exceptions.NoValidFormatException;
import dev.hv.console.exceptions.NoValidTableNameException;
import dev.hv.console.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgsParser {
    ArrayList<String> preprocessArguments(String[] args) throws InvalidArgumentException {
        if (!validateNumberOfArgs(args)) {
            throw new InvalidArgumentException("");
        }
        return convertStringArgsToArrayList(args);
    }
    /**
     * Parses the arguments and filters the Filename behind the --output=/-o or --input=/-i
     * @param args
     * @return the filename or "NoFileNameProvided"
     */
    private boolean validateNumberOfArgs(String[] args) {
        int empty = 0;
        int tooMany = 5;
        return (args.length != empty) && (args.length <= tooMany);
    }

    public static String getValidFileName(ArrayList<String> providedArgs) throws NoValidFileNameException {
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
        throw new NoValidFileNameException("No Valid Fil Name Provided");
    }

    public static FileFormat getValidFileFlag(ArrayList<String> providedArgs) throws NoValidFormatException {
        for (FileFormat format : FileFormat.values()) {
            if (providedArgs.contains(format.getFlag())) {
                return format;
            }
        }
        throw new NoValidFormatException("No Valid Format was found in your arguments.");
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
        for (String entry : args) {
            for (Table tableName: Table.values()) { // use validTableNames.size() instead if time for more generical use
                if (entry.equalsIgnoreCase(tableName.toString())) { // THIS IS NOT AT ALL A GOOD CHECK OR ... ANYHTING
                    return tableName.toString();
                }
            }
        }
        throw new NoValidTableNameException("Could not find a tablename. please use one of " + Arrays.toString(Table.values()));
    }

    ArrayList<String> convertStringArgsToArrayList(String[] args) {
        return new ArrayList<>(List.of(args)); //if i understand .asList returns a list that stays synced with the og-array, lol.
    }

    /**
     * checks wether or not there is at least one flag provided in the arguments (e.g. --input / -o)
     * @param providedArgs
     * @return
     */
    boolean flagProvided(ArrayList<String> providedArgs) {
        for (String args: providedArgs) {
            if (args.startsWith("-")) return true;
        }
        return false;
    }
}
