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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Converter {
    public static String convertJSONToXML(JSONArray jsonArray, String name) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);

        System.out.println(jsonArray);

        //TODO URGENCY: HIGH: The testData (somehow) accept meterid and such as 22ss but idk if they create the table wrong or if the testdata is bad
        //and this probably has to be checked a) with the docs or b) with the frontend.
        String xmlString = switch (name) {
            case "User" -> {
                List<DUser> users = Arrays.asList(new ObjectMapper().readValue(jsonArray.toString(), DUser[].class));
                yield xmlMapper.writer().withRootName("Users").writeValueAsString(users);
            }
            case "Customer" -> {
                List<DCustomer> customers = Arrays.asList(new ObjectMapper().readValue(jsonArray.toString(), DCustomer[].class));
                yield xmlMapper.writer().withRootName("Customers").writeValueAsString(customers);
            }
            default -> {
                List<DReading> readings = Arrays.asList(new ObjectMapper().readValue(jsonArray.toString(), DReading[].class));
                yield xmlMapper.writer().withRootName("Readings").writeValueAsString(readings);
            }
        };
        return xmlString;
    }
    public static String convertJSONToCSV(JSONArray jsonArray, String name) throws IOException {
        StringWriter writer = new StringWriter(); // Write the CSV data to a string
        CSVWriter csvWriter = new CSVWriter(writer);

        String[] header = null; // We'll dynamically create the header

        // Iterating through the jsonArray
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            Map<String, String> row = new HashMap<>(); // Store values for a single row
            for (String key : object.keySet()) {
                Object value = object.get(key);
                row.put(key, value != null ? value.toString() : "");
            }

            if (header == null) {
                header = row.keySet().toArray(new String[0]);
                csvWriter.writeNext(header); // Write the header first
            }

            csvWriter.writeNext(row.values().toArray(new String[0]));
        }

        csvWriter.close();
        return writer.toString(); // Return the generated CSV content
    }

    public static String convertJSONToTXT(JSONArray jsonArray) throws IOException {
        StringBuilder sb = new StringBuilder();

        // Customize as needed:
        String separator = " | "; // Or any separator between fields
        String newLine = "\n";    // Line separator

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            for (String key : object.keySet()) {
                Object value = object.get(key);
                sb.append(key).append(": ").append(value != null ? value.toString() : "");
                sb.append(separator);
            }
            sb.append(newLine);
        }

        return sb.toString(); // Return the formatted text
    }

}
