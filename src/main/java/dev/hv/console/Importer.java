package dev.hv.console;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hv.console.util.FileUtil;
import dev.hv.console.util.NoValidTableNameException;
import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import dev.hv.db.model.DUser;
import dev.hv.db.model.IDCustomer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Importer {
    private DBDAO dbdao;
    private ArgsParser argsParser;
    Importer(ArgsParser argsParser, DBDAO dbdao) {
        this.argsParser = argsParser;
        this.dbdao = dbdao;
    }
    boolean processImport(ArrayList<String> args) throws IOException, NoValidTableNameException {
        // check if tablename valid
        String tableName = ArgsParser.getValidTableNameIfExists(args);

        // size = 2 -> exportTableToConsole
        /*
        if (args.size() == 2) {
            exportTableToConsole(table);
        }*/
        // flags provided ?-> abort : exportTable
        if (argsParser.flagProvided(args)) {
            String filename;
            filename = this.argsParser.getValidFileName(args);
            System.out.println("F-name: " + filename);
            if (filename.equals("NoFilenameProvided")) {
                System.out.println("Please provide a filename!");
                return false;
            }
            importTable(args, tableName, filename);
        }
        return true;
    }

    private void importTableFromCSV(String filename) throws IOException {
        //String layouted = FileUtil.layoutCSV(table);
        //FileUtil.writeFile(layouted, filename);
    }

    private void importTableFromText(String filename) throws IOException {
        //String layouted = FileUtil.layoutText(table);
        //FileUtil.writeFile(layouted, filename);
    }

    private void importTableFromXML(String filename) throws IOException {
        //String layouted = FileUtil.layoutXML(table);
        //FileUtil.writeFile(layouted, filename);
    }

    private void importTableFromJSON(String filename, String table) throws IOException {
        /*sb else can refactor this:*/
        ObjectMapper objectMapper = new ObjectMapper();
        /*if only there was a way of not writing the same code 3 times [nah i am kidding, honestly, generics suck ass too and are no fun to implement AT ALL. But we can if you guys want.]*/
        switch (table) {
            case "User":
                List<DUser> users = Arrays.asList(objectMapper.readValue(new File(filename), DUser[].class));
                for (DUser user : users) {
                    dbdao.insertUser(user);
                }
                break;
            case "Customer":
                List<DCustomer> customers = Arrays.asList(objectMapper.readValue(new File(filename), DCustomer[].class));
                for (DCustomer customer : customers) {
                    dbdao.insertCustomer(customer);
                }
                break;
            case "Reading":
                List<DReading> readings = Arrays.asList(objectMapper.readValue(new File(filename), DReading[].class));
                for (DReading reading : readings) {
                    dbdao.insertReading(reading);
                }
                break;
        }
    }



    public void importTable(ArrayList<String> convertedArgs, String table, String filename) throws IOException {
        switch(this.argsParser.getValidFileFlag(convertedArgs)) {
            case "-c":
                importTableFromCSV(filename);
                break;
            case "-x":
                importTableFromXML(filename);
                break;
            case "-t":
                importTableFromText(filename);
                break;
            case "-j":
            default: importTableFromJSON(filename, table);
        }
    }

}
