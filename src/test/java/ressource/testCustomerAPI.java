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

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dev.bsinfo.ressource.CustomerAPI;
import dev.bsinfo.server.StartServer;
import dev.hv.db.dao.CustomerDAO;
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
	}

    // Api
    private static CustomerAPI customerApi ;
    Jdbi jdbi = null;
    Handle handle = null;
    
    private String lastname1 = "lastname1";
    private String lastname2 = "lastname2";
    private String lastname3 = "lastname3";
    private String firstname1 = "firstname1";
    private String firstname2 = "firstname2";
    private String firstname3 = "firstname3";
    private long id1 = 1L;
    private long id2 = 2L;
    private long id3 = 3L;
    

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
    @DisplayName("Test 'get/all' Endpoint")
	public void testgetAll() {
    	List<DCustomer> response = customerApi.getAll();
    	List<DCustomer> shouldBe = Arrays.asList(new DCustomer(2L,"lastname1", "firstname1"), new DCustomer(3L, "lastname2", "firstname2"));
		assertEquals(response.toString(), shouldBe.toString());
	}

    @Test
    @Order(3)
    @DisplayName("Test 'get/{id}' Endpoint")
    //Problem: Es ist nicht klar welche IDs und Einträge in der DB sind
	public void testget() {
    	DCustomer gotten = customerApi.get(2L);
    	DCustomer shouldBe = new DCustomer(2L,"lastname1", "firstname1");
    	System.out.println(shouldBe);
		assertTrue(shouldBe.isEqualTo(gotten));
		// or assertEquals(shouldBe.toString(), gotten.toString()); so wäre isEqualTo methode nicht notwendig
	}

    @Test
    @Order(4)
    @DisplayName("Test 'create' Endpoint")
	public void testcreate() {
    	customerApi.create(firstname3, lastname3);
    	DCustomer created = customerApi.get(id3);
    	System.out.println(created);
    	DCustomer shouldBe = new DCustomer(id3, lastname3, firstname3);
    	System.out.println(shouldBe);
		assertTrue(created.isEqualTo(shouldBe));
	}
    
    @Test
    @Order(5)
    @DisplayName("Test 'delete/{id}' Endpoint")
	public void testdelete() {
    	customerApi.delete(id3);
		assertNull(customerApi.get(id3));
	}
    
    @Test
    @Order(6)
    @DisplayName("Test if Id increment Endpoint")
	public void testIdincrement() {
    	List<DCustomer> response = customerApi.getAll();
    	System.out.println(response);
		assertNull(customerApi.get(id3));
	}
}
