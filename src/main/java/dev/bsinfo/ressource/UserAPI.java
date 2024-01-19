package dev.bsinfo.ressource;

import dev.hv.db.dao.UserDAO;
import dev.hv.db.init.DBConnect;

import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DUser;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.io.InputStream;
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
    @GET
    @Path("form")
    @Produces(MediaType.TEXT_HTML)
    public InputStream getForm() {
        return getClass().getClassLoader().getResourceAsStream("user-form.html");
    }

    @PUT
    @Path(("edit"))
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public DUser edit(DUser user) {
        if (userDAO.findById(user.getId()) != null) {
            userDAO.update(user);
            System.out.println("Updated customer " + user);
            return user;
        } else {
            //TODO Error message?
            System.out.println("Couldn't find customer with id: " + user.getId());
            return null;
        }
    }

    @GET
    @Path("get/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DUser> getAll()
    {
        return userDAO.getAll();
    }

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public DUser get(@PathParam("id") Long id)
    {
        return userDAO.findById(id);
    }

    /*@POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public DUser create(DUser user)
    {
        userDAO.insert(user);
        return user;
    }*/

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public DUser create(DUser user)
    {
        userDAO.insert(user);
        Long lastID = userDAO.getLastInsertedId().longValue();
        user.setId(lastID);
        if(!user.equals(get(lastID)))
            return null;
        return user;
    }

    @GET
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Long id)
    {
        userDAO.delete(id);
    }
}
