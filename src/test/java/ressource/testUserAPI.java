package ressource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.bsinfo.ressource.UserAPI;
import dev.bsinfo.server.StartServer;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DUser;
import org.jdbi.v3.core.Handle;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testUserAPI {
    String url = "http://localhost:8080/rest";
    private static StartServer instance;
    private static UserAPI api;
    HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    @DisplayName("Start Api Server")
    static public void run() {
        // Server
        instance = StartServer.getInstance();
        instance.run();
        api = new UserAPI();
    }
    DUser user = new DUser(1L,
        "testLastName",
        "testFirstName",
        "testToken",
        "testPassWord"
    );

    @Test
    @Order(1)
    @DisplayName("Test Create User")
    public void testCreate() throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/user/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        int statusCode = response.statusCode();
        assertEquals(statusCode, 200);
        DUser responseBody = mapper.readValue(response.body(), new TypeReference<DUser>(){});
        assertTrue(user.equals(responseBody));
    }

    @Test
    @Order(2)
    @DisplayName("Test GetAll")
    public void testGetAll() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/user/get/all"))
                .GET()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        assertEquals(200, statusCode);
        
        ObjectMapper mapper = new ObjectMapper();

        DUser responseBody = mapper.readValue(response.body(), new TypeReference<List<DUser>>(){}).getFirst();
        assertTrue(user.equals(responseBody));
    }

    @Test
    @Order(3)
    @DisplayName("Test Get By ID")
    public void testGet() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/user/get/1"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        assertEquals( 200, statusCode);
        
        ObjectMapper mapper = new ObjectMapper();

        DUser responseBody = mapper.readValue(response.body(), new TypeReference<DUser>(){});
        assertTrue(user.equals(responseBody));
    }
    
    @Test
    @Order(3)
    @DisplayName("Test Edit User")
    public void testEdit() throws IOException, InterruptedException{
        ObjectMapper mapper = new ObjectMapper();
        DUser newUser = new DUser(
            1L,
            "EditName",
            "EditLastName",
            "editToken",
            "editPassword"
        );
        
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(newUser);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url + "/user/edit"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        int statusCode = response.statusCode();
        assertEquals(statusCode, 200);
        DUser responseBody = mapper.readValue(response.body(), new TypeReference<DUser>(){});
        assertTrue(newUser.equals(responseBody));
        
        // Assert NULL
        newUser.setId(999L);
        
        ow = mapper.writer().withDefaultPrettyPrinter();
        json = ow.writeValueAsString(newUser);
        
        request = HttpRequest.newBuilder()
            .uri(URI.create(url + "/user/edit"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build();
        
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        statusCode = response.statusCode();
        assertEquals(statusCode, 204);
    }
    @Test
    @Order(4)
    @DisplayName("Delete User")
    public void test_delete() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url + "/user/delete/1"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        int statusCode = response.statusCode();
        assertEquals( 204, statusCode);
        assertTrue(api.getAll().isEmpty());
    }
    
    @AfterAll
    @DisplayName("Delete all Users")
    public static void deleteAll()
    {
        Handle handle = DBConnect.getConnection().getJdbi().open();
        handle.execute("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='User'");
        handle.execute("DELETE FROM User");
        handle.close();
    }
}
