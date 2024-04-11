package dev.hv.console;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import dev.hv.console.util.FileUtil;
import dev.hv.console.util.NoValidTableNameException;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import dev.hv.db.model.DUser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Exporter {
    /**
     * processes the export-command
     * @param args
     * @return 0 in case of an error or 1 otherwise
     */
    private DBDAO dbdao;
    private ArgsParser argsParser;
    Exporter(ArgsParser argsParser, DBDAO dbdao) {
        this.argsParser = argsParser;
        this.dbdao = dbdao;
    }
    boolean processExport(ArrayList<String> args) throws IOException, NoValidTableNameException {
        // check if tablename valid
        String tableName = ArgsParser.getValidTableNameIfExists(args);
        if (tableName.equals("NoMatch")) {
            System.out.println("Invalid Table name: Choose 'Customer', 'User' or 'Reading'");
            return false; //print help();
        }
        JSONArray table = dbdao.readTable(tableName);
        // size = 2 -> exportTableToConsole
        if (args.size() == 2) {
            System.out.println(table.toString());
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
            exportTable(args, tableName, table, filename);
        }
        return true;
    }

    private void exportTableToCSV(JSONObject table, String filename) throws IOException {
        String layouted = FileUtil.layoutCSV(table);
        FileUtil.writeFile(layouted, filename);
    }

    private void exportTableToTxt(JSONObject table, String filename) throws IOException {
        String layouted = FileUtil.layoutText(table);
        FileUtil.writeFile(layouted, filename);
    }

    /*
    * SINCE XMLS require names we need them here. We will just use the table-name mostly.
    * And add a 's' to name the whole collection
    * */
    private String convertJSONToXML(JSONArray jsonArray, String name) throws JsonProcessingException {
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
    private void exportTableToXMLFile(JSONArray table, String tableName, String filename) throws IOException {
        //String layouted = FileUtil.layoutXML(table);
        String convertedXML = convertJSONToXML(table, tableName);
        FileUtil.writeFile(convertedXML, filename);
    }

    private void exportTableToJSONFile(JSONArray table, String filename) throws IOException {
        //String layouted = FileUtil.layoutJSON(table);
        FileUtil.writeFile(table.toString(), filename);
    }

    private void exportTableToConsole(JSONObject table) {
        String layouted = FileUtil.layoutJSON(table);
        System.out.println(layouted);
    }
    //this cascading of tableName and such is so annoying and wouldnt be neccessary if we would first parse all arguments and create a object...
    public void exportTable(ArrayList<String> convertedArgs, String tableName, JSONArray table, String filename) throws IOException {
        switch(this.argsParser.getValidFileFlag(convertedArgs)) {
            case "-c":
                //TODO fix this: exportTableToCSV(table, filename);
                break;
            case "-x":
                exportTableToXMLFile(table, tableName, filename);
                break;
            case "-t":
                //exportTableToTxt(table, filename);
                break;
            case "-j":
            default: exportTableToJSONFile(table, filename);
        }
    }

}
