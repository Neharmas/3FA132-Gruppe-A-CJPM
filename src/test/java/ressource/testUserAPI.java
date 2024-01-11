package ressource;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testUserAPI {
    String url = "http://localhost:8080/rest";
    private static StartServer instance;
    private static UserAPI api;

    @BeforeAll
    @DisplayName("Start Api Server")
    static public void run() {
        // Server
        instance = StartServer.getInstance();
        instance.run();
        api = new UserAPI();
    }


    private static Handle handle = null;

    private testUserAPI()
    {
        handle = DBConnect.getConnection().getJdbi().open();
    }

    @Test
    @Order(1)
    @DisplayName("Test Create User")
    public void testCreate() throws IOException, InterruptedException {
        String formData = "firstname=testFirstName&lastname=testLastName&password=testPassWord&token=testToken";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/user/create"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        assertEquals(204, statusCode);
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
    }
}
