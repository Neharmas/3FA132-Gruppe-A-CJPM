package dev.bsinfo.ressource;

import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.dao.ReadingDAO;
import dev.hv.db.init.DBConnect;

import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

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
    public DReading edit(DReading reading) {
        if (readingDAO.findById(reading.getId()) == null) {
            System.out.println("Couldn't find reading with id: " + reading.getId());
            return null;
        }
        DCustomer customer = reading.getCustomer();
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
        return reading;
    }

    @GET
    @Path("get/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DReading> getAll()
    {
        return readingDAO.getAll();
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DReading get(@PathParam("id") Long id)
    {
        return readingDAO.findById(id);
    }


    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public DReading create(DReading reading)
    {
        /*boolean enableSubstitute = "true".equalsIgnoreCase(substitute);

        DCustomer customer = customerDAO.findById(customerID);
        DReading reading = new DReading(comment, customer, kindofmeter, metercount, meterid, enableSubstitute, dateofreading);
        */
        
        DCustomer customer = customerDAO.findById(reading.getCustomer().getId());
        if (customer == null) {
            System.out.println("Es konnte kein Reading kreaiert werden, weil der Customer nicht existiert");
            return null;
        }
        readingDAO.insert(reading);
        Long lastID = readingDAO.getLastInsertedId().longValue();
        reading.setId(lastID);
        
        
        return reading;
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Long id)
    {
        readingDAO.delete(id);
    }
}
