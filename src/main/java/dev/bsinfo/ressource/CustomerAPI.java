package dev.bsinfo.ressource;

import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.io.InputStream;
import java.util.List;

@Path("customer")
public class CustomerAPI {
    CustomerDAO customerDAO;
    Jdbi jdbi = null;
    Handle handle = null;

    public CustomerAPI()
    {
        jdbi = DBConnect.getConnection().getJdbi();
        jdbi.installPlugins();

        handle = jdbi.open();

        customerDAO = handle.attach(CustomerDAO.class);
    }

    @GET
    @Path("form")
    @Produces(MediaType.TEXT_HTML)
    public InputStream getForm() {
        return getClass().getClassLoader().getResourceAsStream("customer-form.html");
    }

    @GET
    @Path("get/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DCustomer> getAll()
    {
        return customerDAO.getAll();
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DCustomer get(@PathParam("id") Long id)
    {
        return customerDAO.findById(id);
    }

    @POST
    @Path("create")
    public void create(@FormParam("firstname") String firstName,
                       @FormParam("lastname") String lastName)
    {
        DCustomer customer = new DCustomer(lastName, firstName);
        customerDAO.insert(customer);
    }

    @GET
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Long id)
    {
        customerDAO.delete(id);
    }
}
