package dao;

import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import dev.hv.db.dao.CustomerDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS) //otherwise 'static' would be required to update/use same value in multiple tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testCustomerDAO {
    CustomerDAO customerDAO;
    DCustomer[] customers = {null, null, null};
    private static DBConnect test_instance = null;
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
    @DisplayName("Insert Customer Object")
    public void testInsert()
    {
        // arrange
        Handle handle = test_instance.getJdbi().open();
        customerDAO = handle.attach(CustomerDAO.class);

        // act
        for(int i = 0; i< customers.length; i++) {
            customers[i] = new DCustomer(i+1, "lastname" + i, "firstname" + i);
            customerDAO.insert(customers[i]);
        }

        // assert
        List<Map<String, Object>> results = handle
                .createQuery("SELECT * FROM customer;")
                .mapToMap()
                .list();

        assertEquals(3, results.size());
    }

    @Test
    @Order(3)
    @DisplayName("Update Customer")
    public void testUpdate()
    {
        // arrange
        Handle handle = test_instance.getJdbi().open();
        DCustomer newCustomer = new DCustomer(1, "Ajomale", "Christopher");

        // act
        customerDAO.update(1L, newCustomer);

        // assert
        List<Map<String, Object>> results = handle
                .createQuery("SELECT * FROM customer;")
                .mapToMap()
                .list();
        assertEquals(newCustomer.getLastname(), results.getFirst().get("lastname"));
        assertEquals(newCustomer.getFirstname(), results.getFirst().get("firstname"));
        assertEquals(1, results.getFirst().get("id"));

        customers[0] = newCustomer;
    }

    @Test
    @Order(4)
    @DisplayName("Delete Customer")
    public void testDelete()
    {
        // act
        customerDAO.delete(1L);
        boolean exists = customerDAO.findById(1L) != null;

        // assert
        assertFalse(exists);

        customers = new DCustomer[]{customers[1], customers[2]};
    }

    @Test
    @Order(5)
    @DisplayName("Get all Customers")
    public void testGetAll()
    {
        // act
        List<DCustomer> listCustomer = customerDAO.getAll();

        // assert
        assertEquals(2, listCustomer.size());
    }
    @AfterAll
    @DisplayName("Delete all Customers")
    public static void deleteAll()
    {
        Handle handle = test_instance.getJdbi().open();
        handle.execute("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='Customer'");
        handle.execute("DELETE FROM Customer");
        handle.close();
    }
}
