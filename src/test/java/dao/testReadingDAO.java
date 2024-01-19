package dao;

import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.dao.ReadingDAO;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import dev.hv.db.model.DUser;
import dev.hv.db.model.IDCustomer;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS) //otherwise 'static' would be required to update/use same value in multiple tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testReadingDAO {
    ReadingDAO readingDAO;
    DReading[] readings = {null, null, null};
    private DBConnect test_instance = null;
    @Test
    @Order(1)
    @DisplayName("Setup Connection")
    public void setupConnection()
    {
        // arrange
        test_instance = DBConnect.getConnection();

        // act
        test_instance.getJdbi().installPlugins();
        test_instance.createAllTables();

        // assert
        assertNotNull(test_instance);
    }
    @Test
    @Order(2)
    @DisplayName("Insert Reading Object")
    public void testInsert()
    {
        // arrange
        Handle handle = test_instance.getJdbi().open();
        readingDAO = handle.attach(ReadingDAO.class);

        // act
        for(int i = 0; i< readings.length; i++) {
            DCustomer customer = new DCustomer(i+1,"Ajomale", "Christopher");
            readings[i] = new DReading( "comment", customer, "kindofmeter", 2, "meterid", false, 19122023L);
            readingDAO.insert(readings[i]);
        }


        // assert
        List<Map<String, Object>> results = handle
                .createQuery("SELECT * FROM reading WHERE meterid = meterid;")
                .mapToMap()
                .list();

        assertEquals("meterid", results.getFirst().get("meterid"));

    }

    @Test
    @Order(3)
    @DisplayName("Update Reading")
    public void testUpdate()
    {
        // arrange
        Handle handle = test_instance.getJdbi().open();
        DCustomer newCustomer = new DCustomer(1L, "lastname1", "firstname1");
        DReading newReading = new DReading(1L, "newcomment", newCustomer, "newkindofmeter", 4, "newmeterid", true, 19122025L);

        // act
        readingDAO.update(1L, newReading);

        // assert
        List<Map<String, Object>> results = handle
                .createQuery("SELECT * FROM reading;")
                .mapToMap()
                .list();

        assertEquals("newkindofmeter", results.getFirst().get("kindofmeter") );

        readings[0] = newReading;
    }

    @Test
    @Order(4)
    @DisplayName("Delete Reading")
    public void testDelete()
    {
        // act
        readingDAO.delete(1L);
        boolean exists = readingDAO.findById(1L) != null;
        List<DReading> test = readingDAO.getAll();
        // assert
        assertFalse(exists);

        readings = new DReading[]{readings[1], readings[2]};
    }

    @Test
    @Order(5)
    @DisplayName("Get all Readings")
    public void testGetAll()
    {
        // act
        List<DReading> listReadings = readingDAO.getAll();
        // assert
        assertEquals(2, listReadings.size());
    }
}

