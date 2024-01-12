package server;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

import dev.bsinfo.server.StartServer;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class testStartServer {
	private static StartServer instance;
    final String pack = "dev.bsinfo.ressource";
    String url = "http://localhost:8080/rest";
    
	@Test
    @Order(1)
    @DisplayName("Test Singelton")
	public void testgetInstance() {
		instance = StartServer.getInstance();
		assertNotNull(instance);
	}
	
	
	@Test
    @Order(2)
    @DisplayName("Test Connection")
	public void testrun() {
		assertTrue(instance.run());
	}
	@Test
	@Order(3)
	@DisplayName("Stop Server")
	public static void stopServer() {
		instance.close();
	}
}
