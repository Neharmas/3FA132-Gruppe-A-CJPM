package model;

import dev.hv.db.model.DUser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS) //otherwise 'static' would be required to update/use same value in multiple tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class testDUser {

    private long id = 1;
    private String lastname = "testlastname";
    private String firstname = "testfirstname";
    private String token = "testToken";
    private String password = "testPassword";

    private long newId = 4;
    private String newLastname = "testNewlastname";
    private String newFirstname = "testNewfirstname";
    private String newToken = "testNewToken";
    private String newPassword = "testNewPassword";

    DUser test_DUser = null;
    DUser test_IDDUser = null;
    DUser test_NameDUser = null;

    DUser[] DUsers = {test_DUser, test_IDDUser, test_NameDUser};
    @Test
    @Order(1)
    @DisplayName("Test User constructor")
    public void test_User()
    {
        // arrange / act
        DUsers[0] = new DUser();
        // assert
        assertNotNull(DUsers[0]);

        // arrange / act
        DUsers[1] = new DUser(id, lastname, firstname, token, password);
        // assert
        assertNotNull(DUsers[1]);

        // arrange / act
        DUsers[2] = new DUser(lastname, firstname, token, password);
        // assert
        assertNotNull(DUsers[2]);
    }
    @Test
    @Order(2)
    @DisplayName("Test all setters")
    public void test_setter()
    {
        for(int i = 0; i<DUsers.length; i++)
        {
            // arrange / act
            DUsers[i].setId(newId);
            DUsers[i].setFirstname(newFirstname);
            DUsers[i].setLastname(newLastname);
            DUsers[i].setToken(newToken);
            DUsers[i].setPassword(newPassword);

            // assert
            assertEquals(newId, DUsers[i].getId());
            assertEquals(newFirstname, DUsers[i].getFirstname());
            assertEquals(newLastname, DUsers[i].getLastname());
            assertEquals(newToken, DUsers[i].getToken());
            assertEquals(newPassword, DUsers[i].getPassword());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Test all setters")
    public void test_toString()
    {
        // arrange
        String equals = "DUser [id=" + newId + ", firstname=" + newFirstname + ", lastname=" + newLastname + ", token=" + newToken
                + ", password=" + newPassword + "]";

        // act
        String firstUser = DUsers[0].toString();
        String secondUser = DUsers[1].toString();
        String thirdUser = DUsers[2].toString();

        // assert
        assertEquals(equals, firstUser);
        assertEquals(equals, secondUser);
        assertEquals(equals, thirdUser);
    }

    @Test
    @Order(4)
    @DisplayName("Test equals To another User")
    public void test_equals()
    {
        DUser user = new DUser(1L, "ln", "fn", "tk", "pw");
        DUser user2 = new DUser(1L, "ln", "fn", "tk", "pw");
        assertTrue(user.equals(user2));
    }
	
}
