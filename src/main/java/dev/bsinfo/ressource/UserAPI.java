package dev.bsinfo.ressource;

import dev.hv.db.dao.UserDAO;
import dev.hv.db.init.DBConnect;

import dev.hv.db.model.DUser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

@Path("user")
public class UserAPI {
    UserDAO userDAO;
    Jdbi jdbi = null;
    Handle handle = null;

    public UserAPI()
    {
        jdbi = DBConnect.getConnection().getJdbi();
        jdbi.installPlugins();

        handle = jdbi.open();

        userDAO = handle.attach(UserDAO.class);
    }
    
    @PUT
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public Response edit(DUser user) {
        if (userDAO.findById(user.getId()) != null) {
            userDAO.update(user);
            System.out.println("Updated customer " + user);
            return Response.ok(user, MediaType.APPLICATION_JSON).build();
        }
        //TODO Error message?
        return Response.serverError().entity("Couldn't find customer with id: " + user.getId()).build();
    }

    @GET
    @Path("get/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        return Response.ok(userDAO.getAll(), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id)
    {
        return Response.ok(userDAO.findById(id), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public Response create(DUser user)
    {
        userDAO.insert(user);
        Long lastID = userDAO.getLastInsertedId().longValue();
        user.setId(lastID);
        
        return Response.ok(user, MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id)
    {
        return Response.ok(userDAO.delete(id), MediaType.APPLICATION_JSON_TYPE).build();
    }
}
