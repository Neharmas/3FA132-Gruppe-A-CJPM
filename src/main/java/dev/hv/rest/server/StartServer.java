package dev.hv.rest.server;

import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

public class StartServer {

	public static void main(final String[] args) {
		final String pack = "dev.bsinfo.ressource";
		String url = "http://localhost:8080/rest";
		System.out.println("Start server");
		System.out.println(url);
		//final ResourceConfig rc = new ResourceConfig().packages(pack).register(AuthenticationFilter.class);
		final ResourceConfig rc = new ResourceConfig().packages(pack);
		final HttpServer server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
		System.out.println("Ready for Requests....");

	}
}
