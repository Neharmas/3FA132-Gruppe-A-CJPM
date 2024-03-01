package model;

import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
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
public class testDReading {

    private String comment = "testcomment";

    private Long id = 1L;

    private DCustomer customer = new DCustomer();
    private String kindofmeter = "testmeter";
    private double metercount = 1;
    private String meterId = "1";
    private Boolean substitute = false;
    private Long dateofreading = 211222L;

    private DReading[] DReadings = {null, null, null};

    @Test
    @Order(1)
    @DisplayName("Test Reading constructor")
    public void test_Reading()
    {
        DReadings[0] = new DReading();
        assertNotNull(DReadings[0]);
        
        DReadings[1] = new DReading(id, comment, customer, kindofmeter, metercount, meterId, substitute, dateofreading);
        assertNotNull(DReadings[1]);

        DReadings[2] = new DReading(comment, customer, kindofmeter, metercount, meterId, substitute, dateofreading);
        assertNotNull(DReadings[2]);
    }
    @Test
    @Order(2)
    @DisplayName("Test Equals")
    public void test_equals()
    {
        assertFalse(DReadings[0].equals(DReadings[1]));
        DReadings[2].setId(id);
        assertTrue(DReadings[1].equals(DReadings[2]));
    }

    @Test
    @Order(2)
    @DisplayName("Test print Date of Reading")
    public void test_printDateOfReading()
    {
        // Wait for Function to be written
        for(int i = 0; i<DReadings.length; i++)
        {
            String result = null;
            assertEquals(result, DReadings[i].printDateofreading());

        }
    }
}
