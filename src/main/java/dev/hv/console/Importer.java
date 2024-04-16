package dev.hv.console;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.ressource.UserAPI;
import dev.hv.console.exceptions.NoValidFileNameException;
import dev.hv.console.exceptions.NoValidFormatException;
import dev.hv.console.exceptions.NoValidTableNameException;
import dev.hv.console.util.FileFormat;
import dev.hv.console.util.FileUtil;
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
    public boolean checkArguments() {
        try {
            tableName = ArgsParser.getValidTableNameIfExists(args);
            fileName = ArgsParser.getValidFileName(args);
            format = ArgsParser.getValidFileFlag(args);
            
            if (format == FileFormat.TXT) {
                System.out.println("File Format .txt is not valid for imports.");
                return false;
            }
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
        /*TODO THIS COULD GO INTO super()*/
        // check if tablename valid
        this.args = args;
        if (!checkArguments()) return false;
        try {
            importTable(args, tableName, fileName);
        } catch (IOException e) {
            System.out.println("Could not write the table due to an IOException. Are you lacking permissions?");
            return false;
        }
        return true;
    }

    private void importTableFromCSV(String filename, String table) throws IOException {
        Reader reader = Files.newBufferedReader(Path.of(filename));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> allLines = csvReader.readAll();
        allLines.removeFirst(); //The first line is the header
        
        String[] values = new String[0];
        for (String[] allLine : allLines) {
           String line = Arrays.toString(allLine);
           values = line.substring(1, line.length() - 1).split(";");
        }
        
        /*if only there was a way of not wri    ting the same code 3 times [nah i am kidding, honestly, generics suck ass too and are no fun to implement AT ALL. But we can if you guys want.]*/
        switch (table) {
            case "User":
                    UserAPI userAPI = new UserAPI();
                    userAPI.create(new DUser(values[2], values[1], values[3], values[4])); //why is the id index 2?
                break;
            case "Customer":
                    CustomerAPI customerAPI = new CustomerAPI();
                    customerAPI.create(new DCustomer(values[1], values[2]));
                break;
            case "Reading":
                 //TODO this is obviously wrong but idc
                    ReadingAPI readingAPI = new ReadingAPI();
                    readingAPI.create(new DReading(values[3], new DCustomer(Long.parseLong(values[7]), "", ""),
                        values[5], Double.parseDouble(values[0]),
                        values[2], Boolean.parseBoolean(values[6]), Long.parseLong(values[1])));
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
                    System.out.println(user.toString().concat(" imported"));
                }
                break;
            case "Customer":
                DCustomer[] customers = mapper.readValue(new File(filename), DCustomer[].class);
                for (DCustomer customer : customers) {
                    dbdao.insertCustomer(customer);
                    System.out.println(customer.toString().concat(" imported"));
                }
                break;
            case "Reading":
                DReading[] readings = mapper.readValue(new File(filename), DReading[].class);
                for (DReading reading : readings) {
                    dbdao.insertReading(reading);
                    System.out.println(reading.toString().concat(" imported"));
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
                    System.out.println(user.toString().concat(" imported"));
                }
                break;
            case "Customer":
                DCustomer[] customers = objectMapper.readValue(new File(filename), DCustomer[].class);
                for (DCustomer customer : customers) {
                    dbdao.insertCustomer(customer);
                    System.out.println(customer.toString().concat(" imported"));
                }
                break;
            case "Reading":
                DReading[] readings = objectMapper.readValue(new File(filename), DReading[].class);
                for (DReading reading : readings) {
                    dbdao.insertReading(reading);
                    System.out.println(reading.toString().concat(" imported"));
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
            case JSON:
                importTableFromJSON(filename, table);
                break;
            case TXT:
               importTableFromText(filename, table);
                break;
            default:
                System.out.println("Unrecognized Format: ".concat(format.getExtension()));
        }
    }

}
