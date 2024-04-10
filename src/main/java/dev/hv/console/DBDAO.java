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
