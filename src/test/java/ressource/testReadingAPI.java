package ressource;

import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.server.StartServer;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import org.jdbi.v3.core.Handle;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testReadingAPI {
    // Server
    final String pack = "dev.bsinfo.ressource";
    String url = "http://localhost:8080/rest";

    @BeforeAll
    @DisplayName("Start Api Server")
    static public void run() {
        StartServer instance = StartServer.getInstance();
        instance.run();
        readingAPI = new ReadingAPI();
        customerAPI = new CustomerAPI();
        DBConnect db = DBConnect.getConnection();
        db.removeAllTables();
        db.createAllTables();
    }

    // Api
    private static ReadingAPI readingAPI;
    private static CustomerAPI customerAPI;

    Long id = 1L;
    Long id1 = 2L;
    String comment = "a";
    DCustomer dCustomer = new DCustomer(1L,"lastname", "firstname");
    DCustomer dCustomer2 = new DCustomer(2L,"lastname", "firstname");
    Long dCustomerID = 1L;
    String kindofmeter = "ab";
    double metercount = 1.0;
    String meterid = "abc";
    boolean sub = true;
    Long dateofreading = 1L;

    DReading shouldBe = new DReading(id,comment,dCustomer,kindofmeter,metercount,meterid, true,dateofreading);

    @Test
    @Order(1)
    @DisplayName("Test Constructor")
    public void testReadingAPI() {
        System.out.print(readingAPI);
        assertNotNull(readingAPI);
    }

    @Test
    @Order(2)
    @DisplayName("Test InputStream")
    public void testInputStream() {
        assertNotNull(readingAPI.getForm());
    }

    @Test
    @Order(3)
    @DisplayName("Test 'create' Endpoint")
    public void testcreate() {
        DCustomer customerID = customerAPI.create(new DCustomer("lastname", "firstname"));
        DReading reading = readingAPI.create(new DReading(id, comment, customerID, kindofmeter, metercount, meterid, sub, dateofreading));
        assertTrue(reading.isEqualTo(shouldBe));
    }

    @Test
    @Order(4)
    @DisplayName("Test 'get/{id}' Endpoint")
    public void testget() {
        DReading existing = readingAPI.get(id);
        DReading nonExisting = readingAPI.get(id1);
        assertTrue(shouldBe.isEqualTo(existing) && nonExisting == null);
    }


    @Test
    @Order(5)
    @DisplayName("Test 'get/all' Endpoint")
    public void testgetAll() {
        Long customerId = new CustomerAPI().create(new DCustomer("lastname", "firstname")).getId();
        DReading reading = new DReading(comment, new CustomerAPI().get(customerId), kindofmeter, metercount, meterid, sub, dateofreading);
        readingAPI.create(reading);

        List<DReading> response = readingAPI.getAll();
        List<DReading> shouldBe = Arrays.asList(new DReading(id,comment,dCustomer,kindofmeter,metercount,meterid,sub,dateofreading),
                                                    new DReading(id1,comment,dCustomer2,kindofmeter,metercount,meterid,sub,dateofreading));

        assertEquals(response.toString(), shouldBe.toString());
    }


    @Test
    @Order(6)
    @DisplayName("Test 'delete/{id}' Endpoint")
    public void testdelete() {
        readingAPI.delete(id1);
        assertNull(readingAPI.get(id1));
    }
    
    @AfterAll
    @DisplayName("Delete All Readings")
    public static void deleteAll()
    {
        Handle handle = DBConnect.getConnection().getJdbi().open();
        handle.execute("DELETE FROM Reading");
        handle.execute("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='Reading'");
        handle.execute("DELETE FROM Customer");
        handle.execute("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='Customer'");
        handle.close();
    }
}
