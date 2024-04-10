package dev.hv.console;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper Class with static methods to handle Files. I just added the static to everything. Let's not get started on refactoring this too soon pls. As long as it works idfc that it's uglier than .
 * Possible TODO: turn static since we never really need an instance of this class, only single methods
 */
public class FileUtil {
    public static void createFoldersForFile(String filePath) throws IOException {
        filePath = filePath.replace("\\", "/");
        ArrayList<String> split = new ArrayList<>(List.of(filePath.split("/")));
        split.removeLast();
        //without the last part behind the last / [so that /target/test.txt becomes /target]
        File onlyPath = new File(String.join("/", split));
        System.out.println("P: " + onlyPath);
        System.out.println(onlyPath.mkdirs());


        /*File parentDir = file.getParentFile();
        System.out.println(parentDir);

        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) { // mkdirs() creates all necessary parent directories
                throw new IOException("Failed to create parent directories for: " + filePath);
            }
        }*/
    }
    static void writeFile(String content, String filename) throws IOException {
        //The filename could be a path, which is bad af....
        createFoldersForFile(filename);
        System.out.println("FILENAME: " + filename);
        BufferedWriter w = new BufferedWriter(new FileWriter(filename));
        w.write(content);
        w.close();
    }

    static String layoutCSV(JSONObject table) {
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

    static String layoutXML(JSONObject table) {
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

    static String layoutText(JSONObject table) {
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

    static String layoutJSON(JSONObject json) {
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
}
