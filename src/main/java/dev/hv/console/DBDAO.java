package dev.hv.console;

import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.dao.ReadingDAO;
import dev.hv.db.dao.UserDAO;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import dev.hv.db.model.DUser;
import org.jdbi.v3.core.Handle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class DBDAO {
    private final DBConnect db = DBConnect.getConnection();

    private JSONArray listMapToJSON(List<Map<String, Object>> table) {
        JSONArray json = new JSONArray(table);
        return json;
    }

    public JSONArray listmap_to_json_string(List<Map<String, Object>> list)
    {
        JSONArray json_arr=new JSONArray();
        for (Map<String, Object> map : list) {
            JSONObject json_obj=new JSONObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                try {
                    json_obj.put(key,value);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            json_arr.put(json_obj);
        }
        return json_arr;
    }
    JSONArray readTable(String tablename) {
        List<Map<String, Object>> table = db.readTable(tablename);
        JSONArray table_json = listmap_to_json_string(table);
        //JSONArray tableListJSON = listMapToJSON();
        //String buildString = "{" + tablename + ": " + tableListJSON + "}";
        //System.out.println(buildString);

        System.out.println(table_json);
        return table_json;
    }

    void insertCustomer(DCustomer customer) {
        this.db.getJdbi().installPlugins();
        Handle handle = this.db.getJdbi().open();
        CustomerDAO customerDAO = handle.attach(CustomerDAO.class);
        customerDAO.insert(customer);
        handle.close();
    }

    void insertUser(DUser user) {
        this.db.getJdbi().installPlugins();
        Handle handle = this.db.getJdbi().open();
        UserDAO userDAO = handle.attach(UserDAO.class);
        userDAO.insert(user);
        handle.close();
    }

    void insertReading(DReading reading) {
        this.db.getJdbi().installPlugins();
        Handle handle = this.db.getJdbi().open();
        ReadingDAO readingDAO = handle.attach(ReadingDAO.class);
        readingDAO.insert(reading);
        handle.close();
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
