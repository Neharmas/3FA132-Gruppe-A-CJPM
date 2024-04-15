package dev.hv.console;

import dev.hv.console.exceptions.NoValidFileNameException;
import dev.hv.console.exceptions.NoValidFormatException;
import dev.hv.console.exceptions.NoValidTableNameException;
import dev.hv.console.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Exporter implements Command {
    /**
     * processes the export-command
     *
     * @param args
     * @return 0 in case of an error or 1 otherwise
     */
    private final DBDAO dbdao;
    String tableName = null;
    String fileName = ""; //this currently is the whole path idk how much i like this.
    FileFormat format;
    ArrayList<String> args;

    Exporter(ArgsParser argsParser, DBDAO dbdao) {
        this.dbdao = dbdao;
    }


    @Override
    public boolean checkArguments() {
        try {
            tableName = ArgsParser.getValidTableNameIfExists(args);
            fileName = ArgsParser.getValidFileName(args);
            format = ArgsParser.getValidFileFlag(args);
        } catch (NoValidTableNameException e) {
            System.out.println("Could not process export. No valid table name provided. Exiting");
            return false;
        } catch (NoValidFileNameException e) {
            System.out.println("Could not process export. No valid file name provided. Exiting.");
            return false;
        } catch (NoValidFormatException e) {
            System.out.println("Could not process export. No valid file Format provided. Exiting.");
            return false;
        }

        return true;

    }

    @Override
    public boolean process(ArrayList<String> args) {
        // check if tablename valid
        this.args = args;
        if (!checkArguments()) return false;

        LinkedHashMap<String, Object> table = dbdao.readTable(tableName);
        // size = 2 -> exportTableToConsole
        if (args.size() == 2) {
            System.out.println(table.toString());
        }
        // flags provided ?-> abort : exportTable

        try {
            exportTable(args, table, fileName);
        } catch (IOException e) {
            System.out.println("Could not write the table due to an IOException. Are you lacking permissions?");
            return false;
        }
        return true;
    }

    /*
     * SINCE XMLS require names we need them here. We will just use the table-name mostly.
     * And add a 's' to name the whole collection
     * */
    //this cascading of tableName and such is so annoying and wouldnt be neccessary if we would first parse all arguments and create a object...
    public void exportTable(ArrayList<String> convertedArgs, LinkedHashMap<String, Object> table, String filename) throws IOException {
        switch (format) {
            case CSV:
                FileUtil.exportTableToCSV(table, filename);
                break;
            case XML:
                FileUtil.exportTableToXMLFile(table, tableName, filename);
                break;
            case TXT:
                FileUtil.exportTableToTxt(table, filename);
                break;
            case JSON:
            default:
                FileUtil.exportTableToJSONFile(table, filename);
        }
    }
}
