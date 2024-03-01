package model;

import dev.hv.db.model.DCustomer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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

    @Test
    @Order(1)
    @DisplayName("Test id constructor")
    public void test_Constructors()
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
    @Order(2)
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
    @Order(3)
    @DisplayName("Test equals")
    public void test_equals()
    {
        // arrange / act
        assertFalse(name_customer.equals(id_customer));
        name_customer.setId(id);
        assertTrue(name_customer.equals(id_customer));
    }
}