package dev.hv.console;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.CSVReader;
import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.ressource.UserAPI;
import dev.hv.console.exceptions.NoValidFileNameException;
import dev.hv.console.exceptions.NoValidFormatException;
import dev.hv.console.exceptions.NoValidTableNameException;
import dev.hv.console.util.FileFormat;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import dev.hv.db.model.DUser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Importer implements Command {
    private final DBDAO dbdao;
    String tableName = null;
    String fileName = ""; //this currently is the whole path idk how much i like this.
    FileFormat format;
    ArrayList<String> args;

    Importer(ArgsParser argsParser, DBDAO dbdao) {
        this.dbdao = dbdao;
    }

    /**
     * this can be smartified by better generalization
     * @return
     */
    @Override
    public boolean loadArguments() {
        try {
            tableName = ArgsParser.getValidTableNameIfExists(args);
            fileName = ArgsParser.getValidFileName(args);
            format = ArgsParser.getValidFileFlag(args);
            if (format == FileFormat.TXT) {
                System.out.println("File Format .txt is not valid for imports.");
                return true;
            }
        } catch (NoValidTableNameException e) {
            System.out.println("Could not process export. No valid table name provided. Exiting");
            return true;
        } catch (NoValidFileNameException e) {
            System.out.println("Could not process export. No valid file name provided. Exiting.");
            return true;
        } catch (NoValidFormatException e) {
            System.out.println("Could not process export. No valid file Format provided. Exiting.");
            return true;
        }

        return false;

    }

    @Override
    public void process(ArrayList<String> args) {
        /*TODO THIS COULD GO INTO super()*/
        // check if tablename valid
        this.args = args;
        if (loadArguments()) return;
        try {
            importTable(args, tableName, fileName);
        } catch (IOException e) {
            System.out.println("Could not write the table due to an IOException. Are you lacking permissions?");
        }
    }

    private void importTableFromCSV(String filename, String table) throws IOException {
        Reader reader = Files.newBufferedReader(Path.of(filename));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> allLines = csvReader.readAll();
        allLines.removeFirst(); //The first line is the header

        switch (table) {
            case "User":
                for (String[] line: allLines) {
                    UserAPI userAPI = new UserAPI();
                    userAPI.create(new DUser(line[2], line[1], line[3], line[4])); //2 and 1 [first and last name are split but only bc the api-parameter are in the 'wrong' order, but the files are fine :thumb_up:.
                }
                break;
            case "Customer":
                for (String[] line: allLines) {
                    CustomerAPI customerAPI = new CustomerAPI();
                    customerAPI.create(new DCustomer(line[2], line[1]));
                }
                break;
            case "Reading":
                for (String[] line: allLines) {
                    //TODO this is obviously wrong but idc
                    ReadingAPI readingAPI = new ReadingAPI();
                    DCustomer customer = new DCustomer(Long.parseLong(line[7]), "", "");
                    DReading reading = new DReading(line[1], customer,
                            line[5], Double.parseDouble(line[0]),
                            line[2], Boolean.parseBoolean(line[6]), Long.parseLong(line[1]));

                    readingAPI.create(reading);
                    }
                break;
        }
    }

    private void importTableFromText(String filename, String table) throws IOException {
        JSONArray jsonArray = new JSONArray();
        BufferedReader reader = new BufferedReader(new FileReader(filename)); // Read the .txt file

        String line;
        while ((line = reader.readLine()) != null) {
            JSONObject object = new JSONObject();

            String[] keyValuePairs = line.split("\\|"); // Split by your chosen separator
            for (String pair : keyValuePairs) {
                String[] keyValue = pair.split("\\s*:\\s*"); // Split key-value at ":"
                if (keyValue.length == 2) {
                    object.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }

            jsonArray.put(object);
        }

        reader.close();
        //return jsonArray;
        //turn json to table:

        ObjectMapper mapper = new ObjectMapper();
        for (int c = 0; c < jsonArray.length(); c++) {
            JSONObject object = jsonArray.getJSONObject(c);
            switch (table) {
                case "User":
                    DUser user = mapper.readValue(object.toString(), DUser.class);
                    dbdao.insertUser(user);
                    break;
                case "Customer":
                    DCustomer customer = mapper.readValue(object.toString(), DCustomer.class);
                    dbdao.insertCustomer(customer);
                    break;
                case "Reading":
                    DReading reading = mapper.readValue(object.toString(), DReading.class);
                    dbdao.insertReading(reading);
                    break;
            }
        }
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
        switch (format) {
            case CSV:
                importTableFromCSV(filename, table);
                break;
            case XML:
                importTableFromXML(filename, table);
                break;
            case TXT:
               importTableFromText(filename, table);
                break;
            case JSON:
            default:
                importTableFromJSON(filename, table);
        }
    }

}
