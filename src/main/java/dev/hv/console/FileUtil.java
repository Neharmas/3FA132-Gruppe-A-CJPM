package dev.hv.console;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtil {
    void writeFile(String content, String filename) throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(filename));
        w.write(content);
        w.close();
    }

    String layoutCSV(JSONObject table) {
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

    String layoutXML(JSONObject table) {
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

    String layoutText(JSONObject table) {
        String text, tablename,header = "", rows = "";
        ArrayList<String> keys = new ArrayList<String>();

        tablename = getTableNameFromJSON(table);

        switch(tablename) {
            case "Customer": keys = customerKeys;
            case "User": keys = userKeys;
            case "Reading": keys = readingKeys;
        }

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

    String layoutJSON(JSONObject json) {
        String raw = json.toString();
        String layouted;

        raw = new StringBuffer(raw).insert(1, "\n  ").toString();
        layouted = raw.replace("[", "\n    [\n\t");
        layouted = layouted.replace("},{", "}, \n\t{");
        layouted = layouted.replace("]", "\n    ]\n");

        return layouted;
    }

    private String getTableNameFromJSON(JSONObject table) {
        String tablename;

        tablename = table.names().toString(); //["<tablename>"]
        tablename = tablename.substring(2, tablename.length() - 2);

        return tablename;
    }

    private final ArrayList<String> validFileFormatFlags = new ArrayList<String>() {{
        add("-c"); // .csv
        add("-j"); // .json
        add("-t"); // .txt
        add("-x"); // .xml
    }};

    private final ArrayList<String> options = new ArrayList<String>() {{
        add("export");
        add("import");
    }};

    private final ArrayList<String> customerKeys = new ArrayList<String>() {{
        add("id");
        add("firstname");
        add("lastname");
    }};

    private final ArrayList<String> userKeys = new ArrayList<String>() {{
        add("id");
        add("firstname");
        add("lastname");
        add("token");
//		add("password");
    }};

    private final ArrayList<String> readingKeys = new ArrayList<String>() {{
        add("comment");
        add("customer"); //Foreign key customer id
        add("kindofmeter");
        add("metercount");
        add("meterid");
        add("substitute");
        add("dateofreading");
    }};
}
