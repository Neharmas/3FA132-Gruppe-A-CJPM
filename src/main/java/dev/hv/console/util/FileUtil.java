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

    private static String getTableNameFromJSON(JSONObject table) {
        String tablename;

        tablename = table.names().toString(); //["<tablename>"]
        tablename = tablename.substring(2, tablename.length() - 2);

        return tablename;
    }

    private static final ArrayList<String> validFileFormatFlags = new ArrayList<>() {{
        add("-c"); // .csv
        add("-j"); // .json
        add("-t"); // .txt
        add("-x"); // .xml
    }};

    private static final ArrayList<String> options = new ArrayList<>() {{
        add("export");
        add("import");
    }};

    private static final ArrayList<String> customerKeys = new ArrayList<>() {{
        add("id");
        add("firstname");
        add("lastname");
    }};

    private static final ArrayList<String> userKeys = new ArrayList<>() {{
        add("id");
        add("firstname");
        add("lastname");
        add("token");
//		add("password");
    }};

    private static final ArrayList<String> readingKeys = new ArrayList<>() {{
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

    public static void exportTableToConsole(LinkedHashMap<String, Object> table) {
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
