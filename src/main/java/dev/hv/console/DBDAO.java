package dev.hv.console;

import dev.hv.db.init.DBConnect;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class DBDAO {
    private final DBConnect db = DBConnect.getConnection();

    private JSONArray listMapToJSON(List<Map<String, Object>> table) {
        JSONArray json = new JSONArray(table);
        return json;
    }
    JSONObject readTable(String tablename) {
        JSONArray tableListJSON = listMapToJSON(db.readTable(tablename));

        String buildString = "{" + tablename + ": " + tableListJSON + "}";
        //System.out.println(buildString);

        JSONObject wholeTableJSON = new JSONObject(buildString);

        return wholeTableJSON;
    }

    public DBConnect getDb() {
        return db;
    }


    public void insertTestData() {
        db.insertTestData();
    }
    public void createAllTables() {
        db.createAllTables();
    }
    public void removeAllTables() {
        db.removeAllTables();
    }
}
