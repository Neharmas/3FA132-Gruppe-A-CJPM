package init;

import dev.hv.db.init.DBConnect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class testDBConnect
{
    private static DBConnect test_instance = null;
    private Jdbi test_jdbi = null;
    
    @Test
    @BeforeAll
    @DisplayName("Set Class Singleton instance")
    public static void test_getConnection() {
        test_instance = DBConnect.getConnection();
        assertNotNull(test_instance);
    }

    @Test
    @Order(1)
    @DisplayName("Connect to Database")
    public void test_getJdbi()
    {
        this.test_jdbi = test_instance.getJdbi();
        assertNotNull(test_jdbi);
    }

    @Test
    @Order(2)
    @DisplayName("Create all Tables")
    public void test_createAllTables()
    {
    	try {
    		test_instance.createAllTables();
    	}
        catch(Exception UnableToCreateStatementException) {
        	UnableToCreateStatementException.printStackTrace();
        }
 
    	final Handle handle = this.test_jdbi.open();
    	
    	List<Map<String, Object>> result = handle
    	.createQuery("SELECT * from KUNDEN")
		.mapToMap()
		.list();
    	
    	result.stream().forEach(e -> System.out.println(e));
    	
		handle.close();
    }

    @Test
    @AfterAll
    @DisplayName("Remove all Tables")
    public static void test_removeAllTables()
    {
        try {
        	test_instance.removeAllTables();
        }
        catch(Exception UnableToCreateStatementException) {
        	UnableToCreateStatementException.printStackTrace();
        }
    }
    
    @Test
    @DisplayName("Test JUnit")
    public void isOne()
    {
        assertEquals(1, 1);
    }
}
