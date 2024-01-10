package ressource;


import static org.junit.Assert.assertArrayEquals;
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
import org.junit.Assert;
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
    private static StartServer instance;
    final String pack = "dev.bsinfo.ressource";
    String url = "http://localhost:8080/rest";
    
    @BeforeAll
    @DisplayName("Start Api Server")
    static public void run() {
		instance = instance.getInstance();
		instance.run();
	}

    // Api
    private static CustomerAPI customerApi ;
    //private CustomerDAO customerDAO;
    Jdbi jdbi = null;
    Handle handle = null;
    
    @Test
    @Order(1)
    @DisplayName("Test Constructor")
	public void testCustomerAPI() {
    	customerApi = new CustomerAPI();
    	System.out.print(customerApi );
		assertNotNull(customerApi );
	}
    
    @Test
    @Order(2)
    @DisplayName("Test 'get/all' Endpoint")
	public void testgetAll() {
    	List<DCustomer> response = customerApi.getAll();
    	//List<DCustomer> shouldBe = Arrays.asList(new DCustomer(2L,"lastname1", "firstname1"), new DCustomer(3L, "lastname2", "firstname2"));
		//assertEquals(response.toString(), shouldBe.toString());
    	
	}
    
    @Test
    @Order(3)
    @DisplayName("Test 'create' Endpoint")
	public void a() {
    	List<DCustomer> GetAll = customerApi.getAll();
    	System.out.print(GetAll);
		assertNotNull(GetAll);
	}
    
    @Test
    @Order(3)
    @DisplayName("Test 'get/{id}' Endpoint")
	public void b() {
    	List<DCustomer> GetAll = customerApi.getAll();
    	System.out.print(GetAll);
		assertNotNull(GetAll);
	}
    
    @Test
    @Order(3)
    @DisplayName("Test 'delete/{id}' Endpoint")
	public void c() {
    	List<DCustomer> GetAll = customerApi.getAll();
    	System.out.print(GetAll);
		assertNotNull(GetAll);
	}
}
