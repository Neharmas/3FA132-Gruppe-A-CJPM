package dev.hv.console;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.ressource.UserAPI;
import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.dao.ReadingDAO;
import dev.hv.db.dao.UserDAO;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import dev.hv.db.model.DUser;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.Handle;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.*;

public class DBDAO {
    private final DBConnect db = DBConnect.getConnection();

    /*
    public JSONArray listmap_to_json_string(List<LinkedHashMap<String, Object>> list)
    {
        JSONArray json_arr = new JSONArray();
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
    }*/

    LinkedHashMap<String, Object> readTable(String tablename) {
        LinkedHashMap<String, Object> table = new LinkedHashMap<>();
        Response response = null;
        ArrayList<?> arrayList = null;
        
        switch (tablename) {
            case "Customer":
                response = new CustomerAPI().getAll();
                arrayList = (ArrayList<?>) response.getEntity();
                for (Object obj : arrayList) {
                    DCustomer c = (DCustomer) obj;
                    table.put(String.valueOf(c.getID()), c);
                }
                break;
            case "User":
                response = new UserAPI().getAll();
                arrayList = (ArrayList<?>) response.getEntity();
                for (Object obj : arrayList) {
                    DUser u = (DUser) obj;
                    table.put(String.valueOf(u.getID()), u);
                }
                break;
            case "Reading":
                response = new ReadingAPI().getAll();
                arrayList = (ArrayList<?>) response.getEntity();
                for (Object obj : arrayList) {
                    DReading r = (DReading) obj;
                    table.put(String.valueOf(r.getID()), r);
                }
                break;
            default:
                System.out.println("Table doesn't exist");
                return null;
        }
        return table;
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
