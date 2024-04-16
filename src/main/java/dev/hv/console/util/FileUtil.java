package dev.hv.console.util;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Helper Class with static methods to handle Files. I just added the static to everything. Let's not get started on refactoring this too soon pls. As long as it works idfc that it's uglier than .
 * Possible TODO: turn static since we never really need an instance of this class, only single methods
 */
public class FileUtil {

    /**
     *
     * @param filePath - the string representation of the relative path you want to parse
     * @return
     * @throws IOException
     */
    public static Path parseStringToPathOld(String filePath) throws IOException {
        //unify delimiter (this could probably be done better with System.pathseperator or smth)
        filePath = filePath.replace("\\", "/");
        ArrayList<String> split = new ArrayList<>(List.of(filePath.split("/")));

        String fileName = split.removeLast();
        String onlyStringPath = String.join("/", split);

        System.out.println("P: " + onlyStringPath);

        Path createdPath = Files.createDirectories(Paths.get(
                Paths.get("").toAbsolutePath().toString(), onlyStringPath));

        Path absolutePath = Paths.get(String.valueOf(createdPath), fileName);

        System.out.println("Created Folders For File: " + absolutePath);
        return absolutePath;
    }

    /**
     * Takes a String-Path and creates a absolute-path as Path from it.
     * @param filePath - expecte do be seperated by / or \\
     * @return Path absolutePath
     */
    public static Path parseStringToPath(String filePath) {
        return Paths.get(Paths.get("").toAbsolutePath().toString(),filePath);
    }

    /**
     * The method that attempts (!) to write the file.
     * @param content
     * @param file
     * @throws IOException when the call to .writeFile doesnt work. Can also cause other eceptions, due to calls
     * to Files.createDirectories(). [catching them better would require reworks...]
     */
    public static void writeFile(String content, Path file) throws IOException {
        //Files.createDirectories(path.get);
        Path onlyPath = file.getParent();
        if (onlyPath != null) {
            System.out.println("Parent directories detected in path. checking folder existence...");
            try {
                Files.createDirectories(onlyPath);
            } catch (IOException e) {
                System.out.println("Error creating directories for file. ".concat(file.toString()));
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("No directory-path found. continue...");
        }

        System.out.println("File-Path: " + file);
        Files.write(file, Collections.singleton(content), StandardCharsets.UTF_8);
    }

    public static String layoutCSV(JSONObject table) {
        String csv, tablename, header = "", rows = "";
        ArrayList<String> keys = new ArrayList<String>();

        tablename = getTableNameFromJSON(table);

        System.out.println("Tablename: " + tablename);
        switch (tablename) {
            case "Customer": keys = customerKeys; break;
            case "User": keys = userKeys; break;
            case "Reading": keys = readingKeys; break;
        }

        for (String element : keys) {
            header = header + element + ";";
        }
        header = header.substring(0, header.length() - 1);

        // append Column Values Accordingly
        int entrys = table.getJSONArray(tablename).length();
        String row = "";
        for (int i=0; i < entrys; i++) {
            row = row + "\n";
            for (String key: keys) {
                String value = table.getJSONArray(tablename).getJSONObject(i).get(key).toString();
                row = row + value + "; ";
            }
            row = row.substring(0, row.length() - 2);
        }

        csv = header + row + "\n";

        return csv;
    }

    public static String layoutXML(JSONObject table) {
        String xml, tablename, header = "", rows = "";
        ArrayList<String> keys = new ArrayList<String>();

        tablename = getTableNameFromJSON(table);

        switch(tablename) {
            case "Customer": keys = customerKeys; break;
            case "User": keys = userKeys; break;
            case "Reading": keys = readingKeys; break;
        }

        // set Heading Accordingly
        header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
        header = header + "<" + tablename + ">" + "\n";

        // append Column Values Accordingly
        int entrys = table.getJSONArray(tablename).length();
        String row = "";
        for (int i=0; i < entrys; i++) {
            row = row + "  <Entry> \n";
            for (String key: keys) {
                String value = table.getJSONArray(tablename).getJSONObject(i).get(key).toString();
                row = row + "\t<" + key + ">" + value + "</" + key + ">\n";
            }
            row = row + "  </Entry> \n";
        }

        String footer = "</" + tablename + ">" + "\n";
        xml = header + row + footer;

        return xml;
    }

    public static String layoutText(JSONObject table) {
        String text, tablename,header = "", rows = "";
        ArrayList<String> keys = new ArrayList<String>();

        tablename = getTableNameFromJSON(table);

        keys = switch (tablename) {
            case "Customer" -> customerKeys;
            case "User" -> userKeys;
            case "Reading" -> readingKeys;
            default -> throw new IllegalStateException("Unexpected value: " + tablename); //this shouldn#t be possible in the first place bc we check the tablename beforehand... but if we would write cleaner code, this would be a better idea
        };

        // set Header Accordingly
        for (String element : keys) {
            header = header + element + " | ";
        }
        header = header.substring(0, header.length() - 2);
        header = header + "\n============================================ \n";

        // append Column Values Accordingly
        int entrys = table.getJSONArray(tablename).length();
        String row = "";
        for (int i=0; i < entrys; i++) {
            for (String key: keys) {
                String value = table.getJSONArray(tablename).getJSONObject(i).get(key).toString();
                row = row + value + " | ";
            }
            row = row.substring(0, row.length() - 2);
            row = row + "\n-------------------------------------------- \n";
        }

        text = header + row;

        return text;
    }

    public static String layoutJSON(JSONObject json) {
        String raw = json.toString();
        String layouted;

        raw = new StringBuffer(raw).insert(1, "\n  ").toString();
        layouted = raw.replace("[", "\n    [\n\t");
        layouted = layouted.replace("},{", "}, \n\t{");
        layouted = layouted.replace("]", "\n    ]\n");

        return layouted;
    }

    private static String getTableNameFromJSON(JSONObject table) {
        String tablename;

        tablename = table.names().toString(); //["<tablename>"]
        tablename = tablename.substring(2, tablename.length() - 2);

        return tablename;
    }

    private static final ArrayList<String> validFileFormatFlags = new ArrayList<String>() {{
        add("-c"); // .csv
        add("-j"); // .json
        add("-t"); // .txt
        add("-x"); // .xml
    }};

    private static final ArrayList<String> options = new ArrayList<String>() {{
        add("export");
        add("import");
    }};

    private static final ArrayList<String> customerKeys = new ArrayList<String>() {{
        add("id");
        add("firstname");
        add("lastname");
    }};

    private static final ArrayList<String> userKeys = new ArrayList<String>() {{
        add("id");
        add("firstname");
        add("lastname");
        add("token");
//		add("password");
    }};

    private static final ArrayList<String> readingKeys = new ArrayList<String>() {{
        add("comment");
        add("customer"); //Foreign key customer id
        add("kindofmeter");
        add("metercount");
        add("meterid");
        add("substitute");
        add("dateofreading");
    }};

    public static void exportTableToXMLFile(LinkedHashMap<String, Object> table, String tableName, Path file) throws IOException {
        //String layouted = FileUtil.layoutXML(table);
        String convertedXML = Converter.convertJSONToXML(table, tableName);
        FileUtil.writeFile(convertedXML, file);
    }

    // TODO: TO JSON AND PROBABLY XML
    public static void exportTableToJSONFile(LinkedHashMap<String, Object> table, Path filepath) throws IOException {
        String convertedJSON = Converter.mapToJSON(table);
        FileUtil.writeFile(convertedJSON, filepath);
    }

    public static void exportTableToConsole(LinkedHashMap<String, Object> table) throws IOException {
        String convertedTxt = Converter.convertJSONToTXT(table);
        System.out.println(convertedTxt);
    }

    public static void exportTableToCSV(LinkedHashMap<String, Object> table, Path filepath) throws IOException {
        String convertedCSV = Converter.convertJSONToCSV(table);
        FileUtil.writeFile(convertedCSV, filepath);
    }

    public static void exportTableToTxt(LinkedHashMap<String, Object> table, Path filename) throws IOException {
        System.out.println(table);
        String convertedTxt = Converter.convertJSONToTXT(table);
        FileUtil.writeFile(convertedTxt, filename);
    }
}
