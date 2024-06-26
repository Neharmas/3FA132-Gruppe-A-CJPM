package dev.hv.console;

import dev.hv.console.exceptions.NoValidFileNameException;
import dev.hv.console.exceptions.NoValidFormatException;
import dev.hv.console.exceptions.NoValidTableNameException;
import dev.hv.console.util.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Exporter implements Command {
    /**
     * processes the export-command
     *
     * @param args
     * @return 0 in case of an error or 1 otherwise
     */
    private final DBDAO dbdao;
    private String tableName;
    private Path fileName; //this currently is the whole path idk how much i like this.
    private FileFormat format;
    private ArrayList<String> args;

    Exporter(DBDAO dbdao) {
        this.dbdao = dbdao;
    }


    /**
     * trys to load the {@code args} of this object into the attributes. Might throw some exceptions
     * Gets automatically called by the .process-method before it does anything. [good design lollll]
     */
    @Override
    public boolean loadArguments() {
        try {
            tableName = ArgsParser.getValidTableNameIfExists(args);
            fileName = FileUtil.parseStringToPath(ArgsParser.getValidFileName(args));
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

    /**
     * loads the arguments into attributes by calling loadArguments and then attempts to run them
     * @param args
     */
    @Override
    public void process(ArrayList<String> args) {
        // check if tablename valid
        this.args = args;
        if (!loadArguments()) return;

        LinkedHashMap<String, Object> table = dbdao.readTable(tableName);
        System.out.println(table);
        // size = 2 -> exportTableToConsole
        if (args.size() == 2) {
            System.out.println(table.toString());
        }
        // flags provided ?-> abort : exportTable

        try {
            exportTable(table, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not export the table due to an IOException. Are you lacking permissions?");
        }
    }

    /*
     * SINCE XMLS require names we need them here. We will just use the table-name mostly.
     * And add a 's' to name the whole collection
     * */
    //this cascading of tableName and such is so annoying and wouldnt be neccessary if we would first parse all arguments and create a object...
    private void exportTable(LinkedHashMap<String, Object> table, Path filename) throws IOException {
        //THE way the table arrives here is weird but works so idfc rn. The objects are there already and correct.
        System.out.println("Exporting table: \n".concat(table.toString()));
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
