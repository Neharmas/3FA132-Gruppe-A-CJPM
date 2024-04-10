package dev.hv.console;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Exporter {
    /**
     * processes the export-command
     * @param args
     * @return 0 in case of an error or 1 otherwise
     */
    private FileUtil fileUtil;
    private DBDAO dbdao;
    private ArgsParser argsParser;
    Exporter(ArgsParser argsParser, FileUtil fileUtil, DBDAO dbdao) {
        this.argsParser = argsParser;
        this.fileUtil = fileUtil;
        this.dbdao = dbdao;
    }
    boolean processExport(ArrayList<String> args) throws IOException {
        // check if tablename valid
        String tableName = ArgsParser.getValidTableNameIfExists(args);
        if (tableName.equals("NoMatch")) {
            System.out.println("Invalid Table name: Choose 'Customer', 'User' or 'Reading'");
            return false; //print help();
        }
        JSONObject table = new JSONObject();
        table = dbdao.readTable(tableName);
        // size = 2 -> exportTableToConsole
        if (args.size() == 2) {
            exportTableToConsole(table);
        }
        // flags provided ?-> abort : exportTable
        if (argsParser.flagProvided(args)) {
            String filename;
            filename = this.argsParser.getValidFileName(args);
            System.out.println("F-name: " + filename);
            if (filename.equals("NoFilenameProvided")) {
                System.out.println("Please provide a filename!");
                return false;
            }
            exportTable(args, table, filename);
        }
        return true;
    }

    private void exportTableToCSV(JSONObject table, String filename) throws IOException {
        String layouted = this.fileUtil.layoutCSV(table);
        this.fileUtil.writeFile(layouted, filename);
    }

    private void exportTableToTxt(JSONObject table, String filename) throws IOException {
        String layouted = this.fileUtil.layoutText(table);
        this.fileUtil.writeFile(layouted, filename);
    }

    private void exportTableToXML(JSONObject table, String filename) throws IOException {
        String layouted = this.fileUtil.layoutXML(table);
        this.fileUtil.writeFile(layouted, filename);
    }

    private void exportTableToJSON(JSONObject table, String filename) throws IOException {
        String layouted = this.fileUtil.layoutJSON(table);
        this.fileUtil.writeFile(layouted, filename);
    }

    private void exportTableToConsole(JSONObject table) {
        String layouted = this.fileUtil.layoutJSON(table);
        System.out.println(layouted);
    }

    public void exportTable(ArrayList<String> convertedArgs, JSONObject table, String filename) throws IOException {
        switch(this.argsParser.getValidFileFlag(convertedArgs)) {
            case "-c":
                exportTableToCSV(table, filename);
                break;
            case "-x":
                exportTableToXML(table, filename);
                break;
            case "-t":
                exportTableToTxt(table, filename);
                break;
            case "-j":
            default: exportTableToJSON(table, filename);
        }
    }

}
