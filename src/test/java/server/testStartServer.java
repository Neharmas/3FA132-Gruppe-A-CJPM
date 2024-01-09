package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import dev.bsinfo.server.StartServer;

class testStartServer {
	private static StartServer instance;
    final String pack = "dev.bsinfo.ressource";
    String url = "http://localhost:8080/rest";
    
	@Test
    @Order(1)
    @DisplayName("Test Singelton")
	public void testgetInstance() {
		assertNotNull(instance.getInstance());
	}
	
	
	@Test
    @Order(2)
    @DisplayName("Test Connection")
	public void testrun() {
		instance = instance.getInstance();
		assertTrue(instance.run());
	}
}
