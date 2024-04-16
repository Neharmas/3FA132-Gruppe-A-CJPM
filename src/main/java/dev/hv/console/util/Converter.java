package dev.hv.console.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.opencsv.CSVWriter;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import dev.hv.db.model.DUser;
import lombok.val;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class Converter {
    
    public static boolean isNumeric(String str){
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public static String mapToJSON(LinkedHashMap<String, Object> table){
        ObjectMapper mapper = new ObjectMapper();

        // Optional: Customize the mapper for formatting
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true); // Pretty-printing

        // Object to JSON Conversion
        try {
            return mapper.writeValueAsString(table);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        /*
        String separator = System.lineSeparator(); // Or any separator between fields
        String tab = "\t";
        StringBuilder sb = new StringBuilder("[").append(separator);
        for (int i = 0; i < table.size(); i++){
            String valuesToString = table.values().toArray()[i].toString();
            String content = valuesToString.substring(valuesToString.indexOf("[") + 1, valuesToString.lastIndexOf("]"));
            String[] con = content.split(", ");
            
            sb.append(tab).append("{").append(separator);
            for (int j = 0; j < con.length; j++){
                String key = con[j].split("=", 2)[0];
                String value = con[j].split("=", 2)[1];
                sb.append(tab);
                if (!isNumeric(key)){
                    sb.append('"').append(key).append('"');
                } else {
                    sb.append(key);
                }
                sb.append(": ");
                
                if (!isNumeric(value)){
                    sb.append('"').append(value).append('"');
                } else{
                    sb.append(value);
                }
                
                if (con.length == j + 1) {
                    sb.append(separator);
                    continue;
                }
                
                sb.append(",").append(separator);
            }
            sb.append(tab).append("}");
            if (table.size() != i + 1){
                sb.append(",").append(separator);
                continue;
            }
            sb.append(separator);
        }
        sb.append("]");
        return sb.toString();*/
    }
    public static String convertJSONToXML(LinkedHashMap<String, Object> jsonArray, String name) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);
        
        //TODO URGENCY: HIGH: The testData (somehow) accept meterid and such as 22ss but idk if they create the table wrong or if the testdata is bad
        //and this probably has to be checked a) with the docs or b) with the frontend.
        return xmlMapper.writer().withRootName(name).writeValueAsString(jsonArray.values());
    }
    public static String convertJSONToCSV(LinkedHashMap<String, Object> jsonArray) throws IOException {
        StringWriter writer = new StringWriter(); // Write the CSV data to a string
        CSVWriter csvWriter = new CSVWriter(writer);

        String[] header = null; // We'll dynamically create the header

        // Iterating through the jsonArray
        for (int i = 0; i < jsonArray.size(); i++) {
            String valuesToString = jsonArray.values().toArray()[i].toString();
            String content = valuesToString.substring(valuesToString.indexOf("[") + 1, valuesToString.lastIndexOf("]"));
            String[] parts = content.split(", ");
            
            if (header == null) {
                header = new String[parts.length];
                
                // Copy the contents of parts into header
                System.arraycopy(parts, 0, header, 0, parts.length);
                for(int j = 0; j < header.length; j++) {
                    header[j] = header[j].split("=", 2)[0];
                }
                csvWriter.writeNext(header, false); // Write the header first
            }
            
            for(int j = 0; j < parts.length; j++)
                parts[j] = parts[j].split("=", 2)[1];
            
            csvWriter.writeNext(parts, false);
        }

        csvWriter.close();
        return writer.toString(); // Return the generated CSV content
    }

    //TODO: SOMWHERE HERE; THE METER_COUNT GETS LOST IN THE EXPORT...
    public static String convertJSONToTXT(LinkedHashMap<String, Object> stringObjectLinkedHashMap) throws IOException {
        StringBuilder sb = new StringBuilder();

        // Customize as needed:
        String separator = System.lineSeparator(); // Or any separator between fields
        StringBuilder header = null;
        System.out.println(stringObjectLinkedHashMap.toString());
        for (String k: stringObjectLinkedHashMap.keySet()) {
            System.out.println(stringObjectLinkedHashMap.get(k));
        }

        for (int i = 0; i < stringObjectLinkedHashMap.size(); i++) {
            String valuesToString = stringObjectLinkedHashMap.values().toArray()[i].toString();
            String content = valuesToString.substring(valuesToString.indexOf("[") + 1, valuesToString.lastIndexOf("]"));
            String[] parts = content.split(", ");
            
            if (header == null) {
                header = new StringBuilder();
                
                for(int j = 0; j < parts.length; j++) {
                    header.append(parts[j].split("=", 2)[0]).append(" | ");
                }
                sb.append(header.append(separator));
            }
            
            for (int j = 0; j < parts.length; j++)
                sb.append(parts[j].split("=", 2)[1]).append(" | ");
            sb.append(separator);
        }
        return sb.toString(); // Return the formatted text
    }
}
