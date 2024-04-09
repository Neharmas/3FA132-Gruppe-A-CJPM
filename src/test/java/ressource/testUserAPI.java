package ressource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.bsinfo.ressource.UserAPI;
import dev.bsinfo.server.RESTServer;
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
    private static RESTServer instance;
    private static UserAPI api;
    private static Handle handle = null;

    private testUserAPI()
    {
        handle = DBConnect.getConnection().getJdbi().open();
    }
    @BeforeAll
    @DisplayName("Start Api Server")
    static public void run() {
        // Server
        instance = RESTServer.getInstance();
        instance.run();
        api = new UserAPI();
    }

    @Test
    @Order(1)
    @DisplayName("Test Create User")
    public void testCreate() throws IOException, InterruptedException {

        DUser user = new DUser(
                1L,
                "testLastName",
                "testFirstName",
                "testToken",
                "testPassWord"
        );

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(user);

        // TODO: turn DUser into JSON string or something
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/user/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        assertEquals(200, statusCode);

        DUser otherUser = api.get(1L);

        assertTrue(user.equals(otherUser));
    }

    @Test
    @Order(2)
    @DisplayName("Test GetAll")
    public void testGetAll() throws IOException, InterruptedException
    {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/user/get/all"))
                .GET()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        assertEquals(200, statusCode);

        DUser user = new DUser(
                1L,
                "testLastName",
                "testFirstName",
                "testToken",
                "testPassWord"
        );
        ObjectMapper mapper = new ObjectMapper();

        DUser responseBody = mapper.readValue(response.body(), new TypeReference<List<DUser>>(){}).getFirst();
        assertTrue(user.equals(responseBody));
    }

    @Test
    @Order(3)
    @DisplayName("Test Get By ID")
    public void testGet() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/user/get/1"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        assertEquals( 200, statusCode);

        DUser user = new DUser(
                1L,
                "testLastName",
                "testFirstName",
                "testToken",
                "testPassWord"
        );
        ObjectMapper mapper = new ObjectMapper();

        DUser responseBody = mapper.readValue(response.body(), new TypeReference<DUser>(){});
        assertTrue(user.equals(responseBody));
    }

    @AfterAll
    @DisplayName("Delete all Users")
    public static void deleteAll()
    {
        List<DUser> users = api.getAll();
        for(DUser user: users)
        {
            api.delete(user.getId());
        }
        handle.execute("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='User'");
        handle.close();
    }
}
