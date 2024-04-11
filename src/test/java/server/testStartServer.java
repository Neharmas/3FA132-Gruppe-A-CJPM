package server;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

import dev.bsinfo.server.RESTServer;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class testStartServer {
	private static RESTServer instance;
    final String pack = "dev.bsinfo.ressource";
    String url = "http://localhost:8080/rest";
    
	@Test
    @Order(1)
    @DisplayName("Test Singelton")
	public void testgetInstance() {
		instance = RESTServer.getInstance();
		assertNotNull(instance);
	}
	
	
		@Test
  	@Order(2)
  	@DisplayName("Test Connection")
		public void testrun() {
			assertTrue(instance.run());
			instance.close();
		}
		@Test
		@Order(3)
		@DisplayName("Test Connection with Parameters")
		public void testRunParam(){
			assertTrue(instance.run(url, pack));
		}
		@Test
		@Order(4)
		@DisplayName("Stop Server")
		public void stopServer() {
		instance.close();
	}
}
