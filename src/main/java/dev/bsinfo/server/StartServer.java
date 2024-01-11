package dev.bsinfo.server;

import java.net.URI;

import dev.hv.db.init.DBConnect;
import jakarta.ws.rs.ProcessingException;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

public class StartServer {
    private static StartServer instance;
    final String pack = "dev.bsinfo.ressource";
    String url = "http://localhost:8080/rest";
    private StartServer() {}
    
    public static StartServer getInstance()
    {
        if (instance == null)
        {
            instance = new StartServer();
            
        }
        return instance;
    }

    public boolean run()
    {
        System.out.println("Start server");
        System.out.println(url);
        final ResourceConfig rc = new ResourceConfig().packages(pack);
        try
        {
            final HttpServer server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
            System.out.println("Ready for Requests....");
            return true;
        }
        catch (ProcessingException e) {
            System.out.println("Server couldn't be initialized.");
        }
        return false;
    }
}
