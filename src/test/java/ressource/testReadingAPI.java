package ressource;

import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.server.StartServer;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        DBConnect db = DBConnect.getConnection();
        db.removeAllTables();
        db.createAllTables();
    }

    // Api
    private static ReadingAPI readingAPI ;

    Long id = 1L;
    Long id1 = 2L;
    String comment = "a";
    DCustomer dCustomer = new DCustomer(1L,"lastname", "firstname");
    Long dCustomerID = 1L;
    String kindofmeter = "ab";
    double metercount = 1.0;
    String meterid = "abc";
    String sub = "true";
    Long dateofreading = 1L;

    DReading shouldBe = new DReading(id,comment,dCustomer,kindofmeter,metercount,meterid, false,dateofreading);

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
        new CustomerAPI().create(new DCustomer("lastname", "firstname"));
        readingAPI.create(comment,dCustomerID,kindofmeter,metercount,meterid,sub,dateofreading);
        DReading created = readingAPI.get(id);
        assertTrue(created.isEqualTo(shouldBe));
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
    public void testgetById() {
        new CustomerAPI().create(new DCustomer("lastname", "firstname"));
        readingAPI.create(comment,dCustomerID,kindofmeter,metercount,meterid,sub,dateofreading);

        List<DReading> response = readingAPI.getAll();
        List<DReading> shouldBe = Arrays.asList(new DReading(id,comment,dCustomer,kindofmeter,metercount,meterid,false,dateofreading),
                                                    new DReading(id1,comment,dCustomer,kindofmeter,metercount,meterid,false,dateofreading));

        assertEquals(response.toString(), shouldBe.toString());
    }


    @Test
    @Order(6)
    @DisplayName("Test 'delete/{id}' Endpoint")
    public void testdelete() {
        readingAPI.delete(id1);
        assertNull(readingAPI.get(id1));
    }
}
