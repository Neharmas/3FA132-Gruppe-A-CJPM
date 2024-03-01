package ressource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.server.StartServer;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class testCustomerAPI {
    // Server
    final String pack = "dev.bsinfo.ressource";
    String url = "http://localhost:8080/rest";
    
    @BeforeAll
    @DisplayName("Start Api Server")
    static public void run() {
		StartServer instance = StartServer.getInstance();
		instance.run();
		DBConnect db = DBConnect.getConnection();
		db.removeAllTables();
		db.createAllTables();
	}

    // Api
    private static CustomerAPI customerApi;
    
    private String ln1 = "ln1";
    private String ln2 = "ln2";
    private String fn1 = "fn1";
    private String fn2 = "fn2";
    private Long id1 = 1L;
    private Long id2 = 2L;

  	DCustomer shouldBe = new DCustomer(id1, ln1, fn1);
	DCustomer shouldBe2 = new DCustomer(id2, ln2, fn2);

    @Test
    @Order(1)
    @DisplayName("Test Constructor")
	public void testCustomerAPI() {
    	customerApi = new CustomerAPI();
    	System.out.print(customerApi);
		assertNotNull(customerApi);
	}

    @Test
    @Order(3)
    @DisplayName("Test 'create' Endpoint")
	public void testcreate() throws IOException, InterruptedException {
		String json = ObjToJSON.convert(shouldBe);

		HttpResponse<String> response = HTTPRequestBuilder.create(url, HTTPRequestBuilder.ResourceTypes.CUSTOMER, json);

		int statusCode = response.statusCode();
		assertEquals(statusCode, 200);
		DCustomer responseBody = new ObjectMapper().readValue(response.body(), new TypeReference<>() {});
		assertTrue(shouldBe.equals(responseBody));

    	DCustomer created = customerApi.get(id1);
		assertTrue(created.equals(shouldBe));
	}
    
    @Test
    @Order(4)
    @DisplayName("Test 'get/{id}' Endpoint")
	public void testget() throws IOException, InterruptedException {
		HttpResponse<String> response = HTTPRequestBuilder.get(url,
				HTTPRequestBuilder.ResourceTypes.CUSTOMER,
				id1.toString());
		DCustomer existing = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});

		response = HTTPRequestBuilder.get(url,
				HTTPRequestBuilder.ResourceTypes.CUSTOMER,
				id2.toString());

		assertEquals(response.body(), "");
		assertTrue(shouldBe.equals(existing));
	}    

    @Test
    @Order(5)
    @DisplayName("Test 'get/all' Endpoint")
	public void testgetAll() throws IOException, InterruptedException {
		customerApi.create(shouldBe2);

		HttpResponse<String> HTTPresponse = HTTPRequestBuilder.get(url, HTTPRequestBuilder.ResourceTypes.CUSTOMER, "all");
		List<DCustomer> response = new ObjectMapper().readValue(HTTPresponse.body(), new TypeReference<>(){});

    	List<DCustomer> listShouldBe = Arrays.asList(shouldBe, shouldBe2);
		assertEquals(response.toString(), listShouldBe.toString());
	}
	@Test
	@Order(6)
	@DisplayName("Test 'edit' Endpoint")
	public void testEdit() throws IOException, InterruptedException{
		DCustomer newCustomer = new DCustomer(
				1L,
				"newLastName",
				"newLastName"
		);

		String json = ObjToJSON.convert(newCustomer);

		HttpResponse<String>response = HTTPRequestBuilder.edit(url, HTTPRequestBuilder.ResourceTypes.CUSTOMER, json);

		int statusCode = response.statusCode();
		assertEquals(statusCode, 200);
		DCustomer responseBody = new ObjectMapper().readValue(response.body(), new TypeReference<>(){});
		assertTrue(newCustomer.equals(responseBody));

		// Assert NULL
		newCustomer.setId(999L);

		json = ObjToJSON.convert(newCustomer);

		response = HTTPRequestBuilder.edit(url, HTTPRequestBuilder.ResourceTypes.CUSTOMER, json);

		statusCode = response.statusCode();
		assertEquals(statusCode, 204);
	}
    @Test
    @Order(7)
    @DisplayName("Test 'delete/{id}' Endpoint")
	public void testdelete() throws IOException, InterruptedException {
		HTTPRequestBuilder.delete(
				url,
				HTTPRequestBuilder.ResourceTypes.CUSTOMER,
				id1.toString());
		HTTPRequestBuilder.delete(
				url,
				HTTPRequestBuilder.ResourceTypes.CUSTOMER,
				id2.toString());

		assertTrue(customerApi.getAll().isEmpty());
	}

}
