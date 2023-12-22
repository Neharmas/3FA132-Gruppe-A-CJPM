package dev.hv.rest.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;


@Path("world")
public class HelloWorlRessource {

	@Path("hello")
	@GET
	//@Produces(MediaType.TEXT_PLAIN)
	public String getHelloWorld() {
		return "Hello World";
	}
	
}
