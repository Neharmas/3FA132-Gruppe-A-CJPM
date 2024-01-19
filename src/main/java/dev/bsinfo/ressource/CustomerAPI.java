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

    /**
     * Updates an user with the given id in the database
     * @param customer
     * @return DCustomer or null
     */
    @PUT
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public DCustomer edit(DCustomer customer) {
        if (customerDAO.findById(customer.getId()) == null) {
            System.out.println("Couldn't find customer with id: " + customer.getId());
            return null;
        }
        else {
            customerDAO.update(customer);
            System.out.println("Updated customer " + customer.toString());
            return customer;
        }
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public DCustomer create(DCustomer cus)
    {
        Long lastID = customerDAO.insert(cus).longValue();
        
        cus.setId(lastID);
        if(!cus.isEqualTo(get(lastID)))
            return null;
        
        return cus;
    }

    @GET
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Long id)
    {
        customerDAO.delete(id);
    }
}
