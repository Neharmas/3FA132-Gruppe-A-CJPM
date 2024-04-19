package dev.bsinfo.ressource;

import dev.hv.db.dao.CustomerDAO;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DCustomer;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

@Path("customer")
public class CustomerAPI {
    CustomerDAO customerDAO;
    Jdbi jdbi = null;
    Handle handle = null;

    public CustomerAPI()
    {
        jdbi = DBConnect.getConnection().getJdbi();
        jdbi.installPlugins(); //TODO THIS METHOD GETS CALLED 3X in the whole code and i am almost once would be fine.

        handle = jdbi.open();

        customerDAO = handle.attach(CustomerDAO.class);
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
    public Response edit(DCustomer customer) {
        if (customerDAO.findById(customer.getID()) == null) {
            System.out.println("Couldn't find customer with id: " + customer.getID());
            //return null;
            return Response.serverError().entity("Couldn't find Customer with id:" + customer.getID()).build();
        }
        else {
            customerDAO.update(customer);
            System.out.println("Updated customer " + customer.toString());
            return Response.ok(customer, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("get/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        return Response.ok(customerDAO.getAll(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {

        DCustomer c = customerDAO.findById(id);
        System.out.println(c);

        if (c == null) {
            return  Response.serverError().entity("Couldn't find Customer with ID: " + id).build();
        } else {
            return  Response.ok(c, MediaType.APPLICATION_JSON).build();
        }

    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public Response create(DCustomer cus)
    {
        customerDAO.insert(cus);
        Long lastID = customerDAO.getLastInsertedId().longValue();
        cus.setID(lastID);
        
        return Response.ok(cus, MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id)
    {
        boolean i = customerDAO.delete(id);
        return Response.ok(i, MediaType.APPLICATION_JSON).build(); //Todo usually this method should return <id> if deleted and smth like 0 if not but rn it just returns the id
    }
}
