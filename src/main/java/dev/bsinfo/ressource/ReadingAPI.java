package dev.bsinfo.ressource;

import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.dao.ReadingDAO;
import dev.hv.db.init.DBConnect;

import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.print.attribute.standard.Media;
import java.io.InputStream;
import java.util.List;

@Path("reading")
public class ReadingAPI {
    ReadingDAO readingDAO;
    CustomerDAO customerDAO;
    Jdbi jdbi = null;
    Handle handle = null;

    public ReadingAPI()
    {
        jdbi = DBConnect.getConnection().getJdbi();
        jdbi.installPlugins();

        handle = jdbi.open();

        readingDAO = handle.attach(ReadingDAO.class);
        customerDAO = handle.attach(CustomerDAO.class);
    }
    
    @PUT
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public Response edit(DReading reading) {
        if (readingDAO.findById(reading.getId()) == null) {
            System.out.println("Couldn't find reading with id: " + reading.getId());
            return null;
        }
        DCustomer customer = (DCustomer) reading.getCustomer();
        //check if customer exits/get customer by id
        customer = customerDAO.findById(customer.getId());
        if (customer == null) {
            System.out.println("Couldn't find customer (id-check): "
                    + reading.getCustomer().getId() +" will not be changed.");
        } else {
            //reset the customer to the one saved in the db (the data sent could contain a dummy-customer)
            reading.setCustomer(customer);
        }


        readingDAO.update(reading);
        return Response.ok(reading, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("get/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        return Response.ok(readingDAO.getAll(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id)
    {

        return Response.ok(readingDAO.findById(id), MediaType.APPLICATION_JSON).build();
    }


    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public Response create(DReading reading)
    {
        /*boolean enableSubstitute = "true".equalsIgnoreCase(substitute);

        DCustomer customer = customerDAO.findById(customerID);
        DReading reading = new DReading(comment, customer, kindofmeter, metercount, meterid, enableSubstitute, dateofreading);
        */
        
        DCustomer customer = customerDAO.findById(reading.getCustomer().getId());
        if (customer == null) {
            System.out.println("Es konnte kein Reading kreaiert werden, weil der Customer nicht existiert");
            //ToDo: This actually is a required functionality
            return Response.serverError().entity("Couldn't create Reading: The customer doesnt exist.").build();
        }
        readingDAO.insert(reading);
        Long lastID = readingDAO.getLastInsertedId().longValue();
        reading.setId(lastID);
        
        
        return Response.ok(reading, MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id)
    {

        return Response.ok(readingDAO.delete(id), MediaType.APPLICATION_JSON).build();
    }
}
