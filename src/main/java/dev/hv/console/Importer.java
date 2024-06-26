package dev.hv.console;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
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
    String tableName = null;
    String fileName = ""; //this currently is the whole path idk how much i like this.
    FileFormat format;
    ArrayList<String> args;
    

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
                //System.out.println("File Format .txt is not valid for imports.");
                //return false;
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
    public void process(ArrayList<String> args) {
        /*TODO THIS COULD GO INTO super()*/
        // check if tablename valid
        this.args = args;
        if (!loadArguments()) return;
        try {
            importTable(tableName, fileName);
        } catch (IOException e) {
            System.out.println("Could not write the table due to an IOException. Are you lacking permissions?");
        } catch (CsvException e) {
          throw new RuntimeException(e);
        }
    }

    private void importTableFromCSV(String filename, String table) throws IOException, CsvException {
        Reader reader = Files.newBufferedReader(Path.of(filename));
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> allLines = csvReader.readAll();
        allLines.removeFirst(); //The first line is the header
        
        String[][] values = new String[allLines.size()][];
        for (int i = 0; i < allLines.size(); i++){
           
           String line = Arrays.toString(allLines.get(i));
           values[i] = line.substring(1, line.length() - 1).split(",");
        }
        
        /*if only there was a way of not wri    ting the same code 3 times [nah i am kidding, honestly, generics suck ass too and are no fun to implement AT ALL. But we can if you guys want.]*/
        switch (table) {
            case "User":
                UserAPI userAPI = new UserAPI();
                for (String[] user : values){
                    if(user.length <= 1)
                        continue;
                    userAPI.create(new DUser(user[2].trim(), user[1].trim(), user[3].trim(), user[4].trim())); //why is the id index 2?
                }
                break;
            case "Customer":
                CustomerAPI customerAPI = new CustomerAPI();
                for (String[] customer : values) {
                    if(customer.length <= 1)
                        continue;
                    customerAPI.create(new DCustomer(customer[1].trim(), customer[2].trim()));
                    System.out.println(new DCustomer(customer[1].trim(), customer[2].trim()));
                }
                break;
            case "Reading":
                //TODO this is obviously wrong but idc
                ReadingAPI readingAPI = new ReadingAPI();
                for (String[] reading : values) {
                    if(reading.length <= 1)
                        continue;
                    
                    Long id = Long.parseLong(reading[0].trim());
                    String comment = reading[1].trim();
                    DCustomer customer = new DCustomer(Long.parseLong(reading[2].trim()), reading[4].trim(), reading[3].trim());
                    String kindOfMeter = reading[5].trim();
                    Double meterCount = Double.parseDouble(reading[6].trim());
                    String meterID = reading[7].trim();
                    Boolean substitute = Boolean.parseBoolean(reading[8].trim());
                    Long dateOfReading = Long.parseLong(reading[9].trim());
                    
                    DReading read = new DReading(
                        id,
                        comment,
                        customer,
                        kindOfMeter,
                        meterCount,
                        meterID,
                        substitute,
                        dateOfReading
                    );
                    readingAPI.create(read);
                    System.out.println(read.toString().concat(" imported"));
                }
                
                break;
        }
    }

    private void importTableFromText(String filename, String table) throws IOException {
        JSONArray jsonArray = new JSONArray();
        BufferedReader reader = new BufferedReader(new FileReader(filename)); // Read the .txt file
        
        int count = 0;
        String line;
        String[] keys = new String[0];
        while ((line = reader.readLine()) != null) {
            JSONObject object = new JSONObject();
            count++;
            if (count == 1) {
                keys = line.split("\\|");
                continue;
            }
            
            String[] values = line.split("\\|"); // Split by your chosen separator
            for (int i = 0; i < values.length - 1; i++){
                if (keys[i].equals(" customer ")) {
                    JSONObject customer = new JSONObject();
                    customer.put("ID", values[i].trim());
                    customer.put("firstName", values[i + 1].trim());
                    customer.put("lastName", values[i + 2].trim());
                    
                    object.put("customer", customer);
                    i += 2;
                    continue;
                }
                object.put(keys[i].trim(), values[i].trim());
            }
            jsonArray.put(object);
            
            
        }

        reader.close();
        //turn json to table:
        
        ObjectMapper mapper = new ObjectMapper();
        for (int c = 0; c < jsonArray.length(); c++) {
            JSONObject object = jsonArray.getJSONObject(c);
            switch (table) {
                case "User":
                    DUser user = mapper.readValue(object.toString(), DUser.class);
                    UserAPI userAPI = new UserAPI();
                    userAPI.create(user);
                    System.out.println(user.toString().concat(" imported"));
                    break;
                case "Customer":
                    DCustomer customer = mapper.readValue(object.toString(), DCustomer.class);
                    CustomerAPI customerAPI = new CustomerAPI();
                    customerAPI.create(customer);
                    System.out.println(customer.toString().concat(" imported"));
                    break;
                case "Reading":
                    DReading reading = mapper.readValue(object.toString(), DReading.class);
                    ReadingAPI readingAPI = new ReadingAPI();
                    readingAPI.create(reading);
                    System.out.println(reading.toString().concat(" imported"));
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
                UserAPI userAPI = new UserAPI();
                for (DUser user : users) {
                    userAPI.create(user);
                    System.out.println(user.toString().concat(" imported"));
                }
                break;
            case "Customer":
                DCustomer[] customers = mapper.readValue(new File(filename), DCustomer[].class);
                CustomerAPI customerapi = new CustomerAPI();
                for (DCustomer customer : customers) {
                    customerapi.create(customer);
                    System.out.println(customer.toString().concat(" imported"));
                }
                break;
            case "Reading":
                DReading[] readings = mapper.readValue(new File(filename), DReading[].class);
                ReadingAPI readingAPI = new ReadingAPI();
                for (DReading reading : readings) {
                    readingAPI.create(reading);
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
                UserAPI userAPI = new UserAPI();
                for (DUser user : users) {
                    userAPI.create(user);
                    System.out.println(user.toString().concat(" imported"));
                }
                break;
            case "Customer":
                DCustomer[] customers = objectMapper.readValue(new File(filename), DCustomer[].class);
                CustomerAPI customerapi = new CustomerAPI();
                for (DCustomer customer : customers) {
                    customerapi.create(customer);
                    System.out.println(customer.toString().concat(" imported"));
                }
                break;
            case "Reading":
                DReading[] readings = objectMapper.readValue(new File(filename), DReading[].class);
                ReadingAPI readingAPI = new ReadingAPI();
                for (DReading reading : readings) {
                    readingAPI.create(reading);
                    System.out.println(reading.toString().concat(" imported"));
                }
                break;
        }
    }

    public void importTable(String table, String filename) throws IOException, CsvException {
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
