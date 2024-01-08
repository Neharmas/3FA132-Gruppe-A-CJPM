package model;

import dev.hv.db.model.DCustomer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

@TestInstance(Lifecycle.PER_CLASS) //otherwise 'static' would be required to update/use same value in multiple tests
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class testDCustomer {
    private long id = 1;
    private String lastname = "lastname";
    private String firstname = "firstname";

    private String newFirstName = "Firstname";
    private String newLastName = "Lastname";
    private long newId = 999L;


    private DCustomer simple_customer, name_customer, id_customer;

    /*
    @Test
    @Order(3)
    @DisplayName("Test simple constructor")
    public void test_simple_DCustomer()
    {
        simple_customer = new DCustomer();
        assertNotNull(simple_customer);
    }

    @Test
    @Order(2)
    @DisplayName("Test names constructor")
    public void test_name_DCustomer()
    {
        name_customer = new DCustomer(lastname, firstname);
        assertNotNull(name_customer);
    }*/

    @Test
    @Order(1)
    @DisplayName("Test id constructor")
    public void test_id_DCustomer()
    {
        // arrange / act
        simple_customer = new DCustomer();
        // assert
        assertNotNull(simple_customer);

        // arrange / act
        name_customer = new DCustomer(lastname, firstname);
        // assert
        assertNotNull(name_customer);

        // arrange / act
        id_customer = new DCustomer(id, lastname, firstname);
        // assert
        assertNotNull(id_customer);
    }

    @Test
    @Order(4)
    @DisplayName("Test names constructor")
    public void test_toString()
    {
        // arrange
        String matches = "DCustomer [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + "]";

        // act
        String entry = id_customer.toString();

        //assert
        assertEquals(entry, matches);
    }

    @Test
    @Order(5)
    @DisplayName("Test all setters")
    public void test_setter()
    {
        // arrange
        String matches = "DCustomer [id=" + newId + ", firstname=" + newFirstName + ", lastname=" + newLastName + "]";

        // act
        id_customer.setFirstname(newFirstName);
        id_customer.setLastname(newLastName);
        id_customer.setId(newId);

        // assert
        String newName = id_customer.toString();
        assertEquals(newName, matches);
    }

    @Test
    @Order(6)
    @DisplayName("Test all getters")
    public void test_getter()
    {
        // arrange
        String matches = "DCustomer [id=" + id_customer.getId() + ", firstname=" + id_customer.getFirstname() + ", lastname=" + id_customer.getLastname() + "]";

        // act
        id_customer.setFirstname(newFirstName);
        id_customer.setLastname(newLastName);
        id_customer.setId(newId);

        // assert
        String newName = id_customer.toString();
        assertEquals(newName, matches);
    }

    @Test
    @Order(7)
    @DisplayName("Test Compare To")
    public void test_compareTo()
    {
        // arrange / act
        name_customer.setId(newId);
        int result = id_customer.compareTo(name_customer);
        // assert
        assertEquals(0, result);

        // arrange / act
        name_customer.setId(1000L);
        result = id_customer.compareTo(name_customer);
        // assert
        assertEquals(1, result);

        // arrange / act
        name_customer.setId(1L);
        result = id_customer.compareTo(name_customer);
        // assert
        assertEquals(-1, result);

    }
}