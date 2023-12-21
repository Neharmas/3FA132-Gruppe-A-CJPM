package init;

import dev.hv.db.init.DBConnect;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS) //otherwise 'static' would be required to update/use same value in multiple tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testDBConnect
{
    
    private DBConnect test_instance = null;
    private Handle handle;

    private Jdbi jdbi = null;
    private String[] allTables = {"Customer", "User", "Reading"};
    
    private int count = 0;
    
    @RepeatedTest(2)
    public void test_set()
    {
    	count += 1;
    }
    
    @BeforeAll
    @RepeatedTest(2)
    @DisplayName("Set Class Singleton instance")
    public void test_getConnection() {
    	// act
        test_instance = DBConnect.getConnection();
        // assert
        assertNotNull(test_instance);
    }

    @Test
    @Order(1)
    @DisplayName("Connect to Database")
    public void test_getJdbi()
    {
        // act
        jdbi = test_instance.getJdbi();

        // assert
        assertNotNull(jdbi);
    }
    
    @Test
    @Order(2)
    @DisplayName("Open Handler")
    public void test_openHandler()
    {
        // act
    	handle = test_instance.getJdbi().open();
        // assert
        assertNotNull(handle);
    }

    public void call_createAllTables() {
    	test_instance.createAllTables();
    }

    public boolean test_if_table_was_created(String table)
    {
        boolean doesTableExists;
		List<Map<String, Object>> results = handle
				.createQuery("SELECT name FROM sqlite_master WHERE type='table';")
				.mapToMap()
				.list();

		doesTableExists =  results.stream().anyMatch(map-> map.containsValue(table));
		
		return doesTableExists;
    }
    
    
    @Test
    @Order(3)
    @DisplayName("Test if tables were created")
    public void tests_createAllTables() {
        // act
    	call_createAllTables();

        // assert
        for (String table : allTables) {
            assertTrue(test_if_table_was_created(table), table + " ist nicht als Tabelle vorhanden");
        }
    }
    
    
    @Test
    @Order(4)
    @DisplayName("Test Insert")
    public void test_insert()
    {
        // arrange
        boolean dataInTable;

        // act
    	test_instance.insertTestData();

        // assert
        List<Map<String, Object>> results = handle
                .createQuery("SELECT * FROM customer;")
                .mapToMap()
                .list();

        dataInTable = !results.isEmpty();
        assertTrue(dataInTable);

        results = handle
                .createQuery("SELECT * FROM user;")
                .mapToMap()
                .list();

        dataInTable = !results.isEmpty();
        assertTrue(dataInTable);

        results = handle
                .createQuery("SELECT * FROM reading;")
                .mapToMap()
                .list();

        dataInTable = !results.isEmpty();
        assertTrue(dataInTable);
    }
    
    @Test
    @Order(5)
    @DisplayName("Test Unit")
    public void test_True()
    {
    	assertTrue(true);
    }

    @Test
    @Order(6)
    @DisplayName("Remove all Tables")
    public void test_removeAllTables()
    {
        // arrange
        List<Map<String, Object>> results = null;

        // act
        test_instance.removeAllTables();

        results = handle
    			.createQuery("SELECT * FROM sqlite_master WHERE type='table';")
    			.mapToMap()
    			.list();

        // assert
		assertFalse(results.isEmpty());
    }
    
    @Test
    @Order(7)
    @DisplayName("Close Handler")
    public void test_closeHandler()
    {
        handle.close();
        assertTrue(handle.isClosed());
    }

    
}
