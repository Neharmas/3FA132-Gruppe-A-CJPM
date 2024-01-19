package ressource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.hv.db.model.DUser;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.*;

import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.server.StartServer;
import dev.hv.db.dao.CustomerDAO;
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
    private long id1 = 1L;
    private long id2 = 2L;

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
    @Order(2)
    @DisplayName("Test InputStream")
	public void testInputStream() {
		assertNotNull(customerApi.getForm());
	}

    @Test
    @Order(3)
    @DisplayName("Test 'create' Endpoint")
	public void testcreate() {
    	customerApi.create(shouldBe);

    	DCustomer created = customerApi.get(id1);
		assertTrue(created.isEqualTo(shouldBe));
	}
    
    @Test
    @Order(4)
    @DisplayName("Test 'get/{id}' Endpoint")
	public void testget() {
    	DCustomer existing = customerApi.get(id1);
    	DCustomer nonExisting = customerApi.get(id2);
		assertTrue(shouldBe.isEqualTo(existing) && nonExisting == null);
	}    

    @Test
    @Order(5)
    @DisplayName("Test 'get/all' Endpoint")
	public void testgetAll() {
    	customerApi.create(shouldBe2);

    	List<DCustomer> response = customerApi.getAll();
    	List<DCustomer> listShouldBe = Arrays.asList(shouldBe, shouldBe2);
		assertEquals(response.toString(), listShouldBe.toString());
	}

    @Test
    @Order(6)
    @DisplayName("Test 'delete/{id}' Endpoint")
	public void testdelete() {
		customerApi.delete(id1);
		assertNull(customerApi.get(id1));

		customerApi.delete(id2);
		assertNull(customerApi.get(id2));
	}

}
