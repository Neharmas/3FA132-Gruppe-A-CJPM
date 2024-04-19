package dao;

import dev.hv.db.dao.UserDAO;
import dev.hv.db.init.DBConnect;
import dev.hv.db.model.DUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Handle;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS) //otherwise 'static' would be required to update/use same value in multiple tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class testUserDAO {
    static UserDAO userDAO;
    DUser[] users = {null, null, null};
    private DBConnect test_instance = null;
    @Test
    @Order(1)
    @DisplayName("Setup Connection")
    public void setupConnection()
    {
        // arrange
        test_instance = DBConnect.getConnection();

        // act
        test_instance.getJdbi().installPlugins();
        test_instance.createAllTables();

        // assert
        assertNotNull(test_instance);
    }

    @Test
    @Order(2)
    @DisplayName("Insert User Data")
    public void testinsert()
    {
        // arrange
        Handle handle = test_instance.getJdbi().open();
        userDAO = handle.attach(UserDAO.class);

        // act
        for(int i = 0; i< users.length; i++) {
            users[i] = new DUser(i + 1, "Ajomale", "Christopher", "token", "password");
            userDAO.insert(users[i]);
        }

        // assert
        List<Map<String, Object>> results = handle
                .createQuery("SELECT * FROM user;")
                .mapToMap()
                .list();

        assertEquals(3, results.size());
    }

    @Test
    @Order(3)
    @DisplayName("Update User")
    public void testUpdate()
    {
        // arrange
        Handle handle = test_instance.getJdbi().open();
        DUser user = new DUser(2, "Hasan", "Mouawia", "newToken", "newPassword");

        // act
        userDAO.update(2L, user);

        // assert
        List<Map<String, Object>> results = handle
                .createQuery("SELECT * FROM user;")
                .mapToMap()
                .list();

        assertEquals("Mouawia", results.get(1).get("firstname") );

        users[1] = user;
    }

    @Test
    @Order(4)
    @DisplayName("Delete User")
    public void testDelete()
    {
        // act
        userDAO.delete(1L);
        boolean exists = userDAO.findById(1L) != null;

        // assert
        assertFalse(exists);

        users = new DUser[]{users[1], users[2]};
    }

    @Test
    @Order(5)
    @DisplayName("Get all Users")
    public void testGetAll()
    {
        // act
        List<DUser> listUsers = userDAO.getAll();

        // assert
        assertEquals(2, listUsers.size());
    }

    @AfterAll
    @DisplayName("Delete All Users")
    public void deleteAll()
    {
        Handle handle = DBConnect.getConnection().getJdbi().open();
        handle.execute("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='User'");
        handle.execute("DELETE FROM User");
        handle.close();
    }
}
