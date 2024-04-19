package dev.hv.console;

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
import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.Handle;

import java.util.*;

public class DBDAO {
    private final DBConnect db = DBConnect.getConnection();

    LinkedHashMap<String, Object> readTable(String tablename) {
        LinkedHashMap<String, Object> table = new LinkedHashMap<>();
        Response response;

        switch (tablename) {
            case "Customer":
                response = new CustomerAPI().getAll();
                ArrayList<DCustomer> customerList = (ArrayList<DCustomer>) response.getEntity();
                for (DCustomer obj : customerList) {
                    table.put(String.valueOf(obj.getID()), obj);
                }
                break;
            case "User":
                response = new UserAPI().getAll();
                ArrayList<DUser> users = (ArrayList<DUser>) response.getEntity();
                for (DUser u : users) {
                    table.put(String.valueOf(u.getID()), u);
                }
                break;
            case "Reading":
                response = new ReadingAPI().getAll();
                ArrayList<DReading> readings= (ArrayList<DReading>) response.getEntity();
                for (DReading reading : readings) {
                    table.put(String.valueOf(reading.getID()), reading);
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
