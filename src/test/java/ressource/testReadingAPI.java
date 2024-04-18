package ressource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.ressource.ReadingAPI;
import dev.bsinfo.server.RESTServer;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import org.jdbi.v3.core.Handle;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testReadingAPI {
    String url = "http://localhost:8080/rest";

    @BeforeAll
    @DisplayName("Start Api Server")
    static public void run() {
        RESTServer instance = RESTServer.getInstance();
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
    DReading shouldBe2 = new DReading(id1,comment,dCustomer2,kindofmeter,metercount,meterid, true,dateofreading);
    
    
    Stream<DReading> generateDReading(){
        return Stream.of(
            null,
            new DReading(1L,"a", dCustomer, "ab",1.0,"abc", true, 1L),
            shouldBe2,
            new DReading(3L,"a", null, "ab",1.0,"abc", true, 1L),
            new DReading(3L,"a", new DCustomer(3L, "lastName", ""), "ab",1.0,"abc", true, 1L),
            new DReading(4L,"a", new DCustomer(4L, "", ""), "ab",1.0,"abc", true, 1L),
            new DReading(5L,"a", new DCustomer(5L, "falseLastName", "falseFirstName"), "ab",1.0,"abc", true, 1L),
            new DReading(6L,"a",  new DCustomer(6L, "", "firstname"), "ab",1.0,"abc", true, 1L)
            );
    }
    
    @Test
    @Order(1)
    @DisplayName("Test Constructor")
    public void testReadingAPIConstructor() {
        assertNotNull(readingAPI);
        customerAPI.create(dCustomer);
        customerAPI.create(dCustomer2);
    }
    @ParameterizedTest
    @Order(2)
    @MethodSource("generateDReading")
    @DisplayName("Test 'create' Endpoint")
    public void testCreate(DReading reading) throws IOException, InterruptedException {
        String json = ObjToJSON.convert(reading);
        HttpResponse<String> response = HTTPRequestBuilder.create(url, HTTPRequestBuilder.ResourceTypes.READING, json);
        
        
        if (reading == null || reading.getCustomer() == null) {
            assertEquals(500, response.statusCode());
            return;
        }else{
            int statusCode = response.statusCode();
            assertEquals(200, statusCode);
        }
        if (reading.getCustomer().getFirstName().isEmpty())
            reading.getCustomer().setFirstName("EMPTY");
        
        if (reading.getCustomer().getLastName().isEmpty())
            reading.getCustomer().setLastName("EMPTY");
        DReading newReading = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
        System.out.println(newReading.getID());
        
        
        assertEquals(newReading, reading);
    }
    @Test
    @Order(3)
    @DisplayName("Test 'get/{id}' Endpoint")
    public void testGet() throws IOException, InterruptedException {
        HttpResponse<String> response = HTTPRequestBuilder.get(url, HTTPRequestBuilder.ResourceTypes.READING, id.toString());

        int statusCode = response.statusCode();
        assertEquals(statusCode, 200);

        DReading existing = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
        
        
        assertEquals(shouldBe1, existing);
    }


    @Test
    @Order(4)
    @DisplayName("Test 'get/all' Endpoint")
    public void testGetAll() throws IOException, InterruptedException {
        readingAPI.delete(3L);
        readingAPI.delete(4L);
        readingAPI.delete(5L);
        readingAPI.delete(6L);
        HttpResponse<String> response = HTTPRequestBuilder.get(url, HTTPRequestBuilder.ResourceTypes.READING, "all");
        
        int statusCode = response.statusCode();
        assertEquals(200, statusCode);
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
    public void testDelete() throws IOException, InterruptedException {
        HTTPRequestBuilder.delete(url,
                HTTPRequestBuilder.ResourceTypes.READING,
                id1.toString());

        assertNull(readingAPI.get(id1).getEntity());
    }
    
    @ParameterizedTest
    @MethodSource("generateDReading")
    @DisplayName("Test Parameterized Tests")
    public void testpara(DReading hello){
        if (hello == null)
            return;
        System.out.println(hello);
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
