package init;

import dev.hv.db.init.DBConnect;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import static org.junit.Assert.*;

public class testDBConnect
{
    private static DBConnect test_instance = null;
    private Jdbi test_jdbi = null;
    @Test
    @BeforeAll
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
        test_jdbi = DBConnect.getConnection().getJdbi();
        assertNotNull(test_jdbi);
    }

    @Test
    @Order(2)
    @DisplayName("Create all Tables")
    public void test_createAllTables()
    {
        DBConnect.getConnection().createAllTables();

        Handle handle;
        handle = DBConnect.getConnection().getJdbi().open();

        try {
            handle.execute("SELECT * FROM KUNDEN");
        }
        catch(Exception UnableToCreateStatementException) {
            fail();
        }
        handle.close();
    }

    @Test
    @AfterAll
    @DisplayName("Remove all Tables")
    public void test_removeAllTables()
    {
        try {
            DBConnect.getConnection().removeAllTables();
        }
        catch(Exception UnableToCreateStatementException) {
            fail();
        }
    }
}
