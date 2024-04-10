package dev.hv.console;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import dev.hv.console.util.FileUtil;
import dev.hv.console.util.NoValidTableNameException;
import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import dev.hv.db.model.DUser;
import dev.hv.db.model.IDCustomer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

    private void importTableFromCSV(String filename, String table) throws IOException {
        Reader reader = Files.newBufferedReader(Path.of(filename));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> allLines = csvReader.readAll();
        allLines.removeFirst(); //The first line is the header

        //todo zu verschachtelt gaeafasefarfqa
        switch (table) {
            case "User":
                for (String[] lines : allLines) {
                    //breaks down the size-1-array into an actual array
                    //(lines look like [id; lastname; firstname] for _some_ reason)
                    lines = lines[0].split("; ");
                    DUser user = new DUser(lines[1], lines[2], lines[3], lines[4]);
                    dbdao.insertUser(user);
                }
                break;
            case "Customer":
                //there _should_ be a smarter/better way for this by just generating beans from the csv but honestly that didnt
                //work out of the box and this does.
                for (String[] lines : allLines) {
                    //breaks down the size-1-array into an actual array
                    //(lines look like [id; lastname; firstname] for _some_ reason)
                    lines = lines[0].split("; ");
                    System.out.println(lines[0]);
                    DCustomer customer = new DCustomer(lines[1], lines[2]);
                    dbdao.insertCustomer(customer);
                }
                break;
            case "Reading":
                for (String[] lines : allLines) {
                    //breaks down the size-1-array into an actual array
                    //(lines look like [id; lastname; firstname] for _some_ reason)
                    lines = lines[0].split("; ");
                    //TODO this is not a job for today. To post the readings we will probalby have to filter the reading-csv a bit differnt than we do for customer and user but since the file-formats are wrong already (I would have to check them now, too; specifially the xml because the export EXPORTS a wrong-formatted xml; should be easy tho.), I really dont want to do this today.
                    //DReading reading = new DReading(lines[1], lines[2], lines[3], lines[4]);
                    //dbdao.insertReading(reading);
                }
                break;
        }
    }

    private void importTableFromText(String filename, String table) throws IOException {
        System.out.println("Please implement me, Senpai!");
    }

    private void importTableFromXML(String filename, String table) throws IOException {
        XmlMapper mapper = new XmlMapper();
        /*if only there was a way of not writing the same code 3 times [nah i am kidding, honestly, generics suck ass too and are no fun to implement AT ALL. But we can if you guys want.]*/
        switch (table) {
            case "User":
                DUser[] users = mapper.readValue(new File(filename), DUser[].class);
                for (DUser user : users) {
                    dbdao.insertUser(user);
                }
                break;
            case "Customer":
                DCustomer[] customers = mapper.readValue(new File(filename), DCustomer[].class);
                for (DCustomer customer : customers) {
                    dbdao.insertCustomer(customer);
                }
                break;
            case "Reading":
                DReading[] readings = mapper.readValue(new File(filename), DReading[].class);
                for (DReading reading : readings) {
                    dbdao.insertReading(reading);
                }
                break;
        }
    }

    private void importTableFromJSON(String filename, String table) throws IOException {
        /*sb else can refactor this:*/
        ObjectMapper objectMapper = new ObjectMapper();
        /*if only there was a way of not writing the same code 3 times [nah i am kidding, honestly, generics suck ass too and are no fun to implement AT ALL. But we can if you guys want.]*/
        switch (table) {
            case "User":
                DUser[] users = objectMapper.readValue(new File(filename), DUser[].class);
                for (DUser user : users) {
                    dbdao.insertUser(user);
                }
                break;
            case "Customer":
                DCustomer[] customers = objectMapper.readValue(new File(filename), DCustomer[].class);
                for (DCustomer customer : customers) {
                    dbdao.insertCustomer(customer);
                }
                break;
            case "Reading":
                DReading[] readings = objectMapper.readValue(new File(filename), DReading[].class);
                for (DReading reading : readings) {
                    dbdao.insertReading(reading);
                }
                break;
        }
    }



    public void importTable(ArrayList<String> convertedArgs, String table, String filename) throws IOException {
        switch(this.argsParser.getValidFileFlag(convertedArgs)) {
            case "-c":
                importTableFromCSV(filename, table);
                break;
            case "-x":
                importTableFromXML(filename, table);
                break;
            case "-t":
                importTableFromText(filename, table);
                break;
            case "-j":
            default: importTableFromJSON(filename, table);
        }
    }

}
