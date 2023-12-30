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
    @GET
    @Path("form")
    @Produces(MediaType.TEXT_HTML)
    public InputStream getForm() {
        return getClass().getClassLoader().getResourceAsStream("reading-form.html");
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
    public void create(@FormParam("comment") String comment,
                       @FormParam("customer") Long customerID,
                       @FormParam("kindofmeter") String kindofmeter,
                       @FormParam("metercount") double metercount,
                       @FormParam("meterid") String meterid,
                       @FormParam("substitute") String substitute,
                       @FormParam("dateofreading") Long dateofreading
                       )
    {
        boolean enableSubstitute = "on".equalsIgnoreCase(substitute);

        DCustomer customer = customerDAO.findById(customerID);
        DReading reading = new DReading(comment, customer, kindofmeter, metercount, meterid, enableSubstitute, dateofreading);
        readingDAO.insert(reading);
    }

    @GET
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Long id)
    {
        readingDAO.delete(id);
    }
}
