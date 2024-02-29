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
    @DisplayName("Test to String")
    public void test_toString()
    {
        // arrange
        String equals = "DUser [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", token=" + token
                + ", password=" + password + "]";

        // act
        String firstUser = DUsers[0].toString();
        String secondUser = DUsers[1].toString();
        String thirdUser = DUsers[2].toString();

        // assert
        assertEquals(new DUser().toString(), firstUser);
        assertEquals(equals, secondUser);
        
        equals = "DUser [id=null" + ", firstname=" + firstname + ", lastname=" + lastname + ", token=" + token
            + ", password=" + password + "]";
        
        assertEquals(equals, thirdUser);
    }

    @Test
    @Order(3)
    @DisplayName("Test equals To another User")
    public void test_equals()
    {
        DUser user = new DUser(1L, "ln", "fn", "tk", "pw");
        DUser user2 = new DUser(1L, "ln", "fn", "tk", "pw");
        assertTrue(user.equals(user2));
    }
	
}
