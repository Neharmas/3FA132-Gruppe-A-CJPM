package init;

import dev.hv.db.init.DBConnect;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

@TestInstance(Lifecycle.PER_CLASS) //otherwise 'static' would be required to update/use same value in multiple tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testDBConnect
{
    
    private DBConnect test_instance = null;
    private Handle handle;

    private Jdbi jdbi = null;
    private String[] allTables = {"Customer","User", "Lesen"};
    
    private int count = 0;
    
    @RepeatedTest(2)
    public void test_set()
    {
    	count += 1;
    	System.out.println(count);
    }
    
    @BeforeAll
    @RepeatedTest(2)
    @DisplayName("Set Class Singleton instance")
    public void test_getConnection() {
    	
        test_instance = DBConnect.getConnection();
        
        assertNotNull(test_instance);
    }

    @Test
    @Order(1)
    @DisplayName("Connect to Database")
    public void test_getJdbi()
    {
        jdbi = test_instance.getJdbi();
        assertNotNull(jdbi);
    }
    
    @Test
    @Order(2)
    @DisplayName("Open Handler")
    public void test_openHandler()
    {
    	handle = test_instance.getJdbi().open();
        assertNotNull(handle);
    }
    
    
    
    public void call_createAllTables() {
    	test_instance.createAllTables();
    }

    public boolean test_if_table_was_created(String table)
    {	
    	
		List<Map<String, Object>> results = handle
				.createQuery("SELECT name FROM sqlite_master WHERE type='table';")
				.mapToMap()
				.list();
		boolean doesTableExists;
		doesTableExists =  results.stream().anyMatch(map-> map.containsValue(table));
		
		return doesTableExists;
    }
    
    
    @Test
    @Order(3)
    @DisplayName("Test if tables were created")
    public void tests_createAllTables() {
    	call_createAllTables();
    	
    	for (int i=0; i<allTables.length; i++) {
    		System.out.println(allTables[i]);
    		assertTrue(test_if_table_was_created(allTables[i]), allTables[i] + " ist nicht als Tabelle vorhanden");
    	}
    }
    
    
    @Test
    @Order(4)
    @DisplayName("Test Insert")
    public void test_insert()
    {
    	test_instance.insertTestData();
    	//handle.execute("INSERT INTO CUSTOMER VALUES(NULL, 'Mustermann', 'Maximillian')");
    	//handle.execute("INSERT INTO USER VALUES(NULL, 'Mustermann', 'Maximillian')");
    	//handle.execute("INSERT INTO LESEN VALUES(NULL, 'Müller', 'Andreas', '65342', '53216')");
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
        test_instance.removeAllTables();
        
        List<Map<String, Object>> results = handle
    			.createQuery("SELECT * FROM sqlite_master WHERE type='table';")
    			.mapToMap()
    			.list();
		
		assertFalse(results.isEmpty());
    }
    
    @Test
    @Order(7)
    @DisplayName("Close Handler")
    public void test_closeHandler()
    {
    	handle.close();
    }

    
}
