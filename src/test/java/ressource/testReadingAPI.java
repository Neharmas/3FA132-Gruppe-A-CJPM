package ressource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.server.StartServer;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import org.jdbi.v3.core.Handle;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.http.HttpResponse;
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
    DCustomer dCustomer2 = new DCustomer(2L, "newLastname", "newFirstname");
    String kindofmeter = "ab";
    double metercount = 1.0;
    String meterid = "abc";
    Long dateofreading = 1L;

    DReading shouldBe1 = new DReading(id,comment,dCustomer,kindofmeter,metercount,meterid, true,dateofreading);
    DReading shouldBe2 = new DReading(id,comment,dCustomer2,kindofmeter,metercount,meterid, true,dateofreading);

    @Test
    @Order(1)
    @DisplayName("Test Constructor")
    public void testReadingAPI() {
        System.out.print(readingAPI);
        assertNotNull(readingAPI);
        customerAPI.create(dCustomer);
        customerAPI.create(dCustomer2);
    }

    @Test
    @Order(2)
    @DisplayName("Test 'create' Endpoint")
    public void testcreate() throws IOException, InterruptedException {
        String json = ObjToJSON.convert(shouldBe1);
        HttpResponse<String> response = HTTPRequestBuilder.create(url, HTTPRequestBuilder.ResourceTypes.READING, json);
        DReading newReading = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});

        int statusCode = response.statusCode();
        assertEquals(200, statusCode);

        assertTrue(newReading.equals(shouldBe1));

        DReading falseCustomerReading = shouldBe1;
        falseCustomerReading.setCustomer(new DCustomer());

        json = ObjToJSON.convert(falseCustomerReading);
        response = HTTPRequestBuilder.create(url, HTTPRequestBuilder.ResourceTypes.READING, json);
        assertEquals("Couldn't create Reading: The customer doesnt exist.", response.body());
    }

    @Test
    @Order(3)
    @DisplayName("Test 'get/{id}' Endpoint")
    public void testget() throws IOException, InterruptedException {
        HttpResponse<String> response = HTTPRequestBuilder.get(url, HTTPRequestBuilder.ResourceTypes.READING, id.toString());

        int statusCode = response.statusCode();
        assertEquals(statusCode, 200);

        DReading existing = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});

        response = HTTPRequestBuilder.get(url, HTTPRequestBuilder.ResourceTypes.READING, id1.toString());

        assertEquals(response.body(), "");
        assertTrue(shouldBe1.equals(existing));
    }


    @Test
    @Order(4)
    @DisplayName("Test 'get/all' Endpoint")
    public void testgetAll() throws IOException, InterruptedException {
        readingAPI.create(shouldBe2);

        HttpResponse<String> response = HTTPRequestBuilder.get(url, HTTPRequestBuilder.ResourceTypes.READING, "all");

        int statusCode = response.statusCode();
        assertEquals(statusCode, 200);
        List<DReading> listResponse = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
        List<DReading> shouldBeList = Arrays.asList(shouldBe1, shouldBe2);

        assertEquals(shouldBeList.toString(), listResponse.toString());
    }

    @Test
    @Order(5)
    @DisplayName("Test 'edit' Endpoint")
    public void testEdit() throws IOException, InterruptedException{
        DReading newReading = new DReading(id,comment,dCustomer,kindofmeter,metercount,meterid, false,dateofreading);
        String json = ObjToJSON.convert(newReading);

        HttpResponse<String>response = HTTPRequestBuilder.edit(url, HTTPRequestBuilder.ResourceTypes.READING, json);

        int statusCode = response.statusCode();
        assertEquals(200, statusCode);
        DReading responseBody = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
        assertEquals(newReading.toString(), responseBody.toString());

        // Assert NULL
        newReading.setID(999L);
        json = ObjToJSON.convert(newReading);
        response = HTTPRequestBuilder.edit(url, HTTPRequestBuilder.ResourceTypes.READING, json);
        statusCode = response.statusCode();
        assertEquals(204,statusCode);

        // Assert Customer Null
        newReading.setID(1L);
        newReading.setCustomer(new DCustomer(999L, null, null));
        json = ObjToJSON.convert(newReading);
        response = HTTPRequestBuilder.edit(url, HTTPRequestBuilder.ResourceTypes.READING, json);
        statusCode = response.statusCode();
        assertEquals(200, statusCode);
    }
    @Test
    @Order(6)
    @DisplayName("Test 'delete/{id}' Endpoint")
    public void testdelete() throws IOException, InterruptedException {
        HTTPRequestBuilder.delete(url,
                HTTPRequestBuilder.ResourceTypes.READING,
                id1.toString());

        assertNull(readingAPI.get(id1).getEntity());
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
