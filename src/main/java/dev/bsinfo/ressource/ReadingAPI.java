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
        if (readingDAO.findById(reading.getID()) == null) {
            System.out.println("Couldn't find reading with id: " + reading.getID());
            return null;
        }
        DCustomer customer = (DCustomer) reading.getCustomer();
        //check if customer exits/get customer by id
        customer = customerDAO.findById(customer.getID());
        if (customer == null) {
            System.out.println("Couldn't find customer (id-check): "
                    + reading.getCustomer().getID() +" will not be changed.");
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
    	//if the response doesn't have a customer, something is a bit wrong:
    	if (reading.getCustomer() == null) {
    		return Response.serverError().encoding("No customer sent with the reading. Please try again").build();
    	}
    	
        DCustomer customer = customerDAO.findById(reading.getCustomer().getID());
        //if the customer doesnt exist yet, we create it.
        //create customer and set the name 
        if (customer == null) {
        	//if the name and first_name are empty, we replace them with EMTPY. This can be changed later, as it isn't really best practice
        	if (reading.getCustomer().getFirstName() == "") reading.getCustomer().setFirstName("EMPTY");
        	if (reading.getCustomer().getLastName() == "") reading.getCustomer().setLastName("EMPTY");

        	
        	customerDAO.insert((DCustomer)reading.getCustomer()); //the id doesn't have to stay the same on insert!
            
        	//technically could be simplified a bit by just resetting the id (how woould the other data change?
        	reading.setCustomer(customerDAO.findById(Long.valueOf(customerDAO.getLastInsertedId())));
        
        //if the customer (id) already exists, we update name and firstname
        } else {
        	customerDAO.update(customer);
        }
            
        
        readingDAO.insert(reading);
        Long lastID = readingDAO.getLastInsertedId().longValue();
        reading.setID(lastID);
        
        
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
