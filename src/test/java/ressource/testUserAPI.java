package ressource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bsinfo.ressource.UserAPI;
import dev.bsinfo.server.RESTServer;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DUser;
import org.jdbi.v3.core.Handle;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testUserAPI {
    String url = "http://localhost:8080/rest";
    private static RESTServer instance;
    private static UserAPI api;

    @BeforeAll
    @DisplayName("Start Api Server")
    static public void run() {
        // Server
        instance = RESTServer.getInstance();
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
        String json = ObjToJSON.convert(user);

        HttpResponse<String>response = HTTPRequestBuilder.create(url, HTTPRequestBuilder.ResourceTypes.USER, json);

        int statusCode = response.statusCode();
        assertEquals(statusCode, 200);
        DUser responseBody =  new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
        assertTrue(user.equals(responseBody));
    }

    @Test
    @Order(2)
    @DisplayName("Test Get By ID")
    public void testGet() throws IOException, InterruptedException {
        HttpResponse<String>response = HTTPRequestBuilder.get(
                url,
                HTTPRequestBuilder.ResourceTypes.USER,
                user.getID().toString());

        int statusCode = response.statusCode();
        assertEquals( 200, statusCode);

        DUser responseBody = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
        assertTrue(user.equals(responseBody));
    }

    @Test
    @Order(3)
    @DisplayName("Test GetAll")
    public void testGetAll() throws IOException, InterruptedException {
        DUser newUser = new DUser(2L, "newLastName", "newFirstName", "newToken", "newPassword");
        api.create(newUser);
        HttpResponse<String>response = HTTPRequestBuilder.get(url, HTTPRequestBuilder.ResourceTypes.USER,"all");

        int statusCode = response.statusCode();
        assertEquals(200, statusCode);

        List<DUser> responseBody = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
        List<DUser> fullList = Arrays.asList(user, newUser);

        assertEquals(fullList.toString(), responseBody.toString());
    }
    
    @Test
    @Order(4)
    @DisplayName("Test Edit User")
    public void testEdit() throws IOException, InterruptedException{
        DUser newUser = new DUser(
            1L,
            "EditName",
            "EditLastName",
            "editToken",
            "editPassword"
        );

        String json = ObjToJSON.convert(newUser);

        HttpResponse<String> response = HTTPRequestBuilder.edit(url, HTTPRequestBuilder.ResourceTypes.USER, json);

        int statusCode = response.statusCode();
        assertEquals(statusCode, 200);
        DUser responseBody = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
        assertTrue(newUser.equals(responseBody));
        
        // Assert NULL
        newUser.setID(999L);

        json = ObjToJSON.convert(newUser);

        response = HTTPRequestBuilder.edit(url, HTTPRequestBuilder.ResourceTypes.USER, json);


        statusCode = response.statusCode();
        assertEquals(statusCode, 500);
    }
    @Test
    @Order(5)
    @DisplayName("Delete User")
    public void test_delete() throws IOException, InterruptedException {
        api.delete(2L);
        HttpResponse<String>response = HTTPRequestBuilder.delete(url,
                HTTPRequestBuilder.ResourceTypes.USER,
                user.getID().toString());
        
        int statusCode = response.statusCode();
        assertEquals( 200, statusCode);
        ArrayList<DUser> test = (ArrayList<DUser>) api.getAll().getEntity();
        assertTrue(test.isEmpty());
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
