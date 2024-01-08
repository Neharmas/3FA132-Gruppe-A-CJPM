package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

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


    private String newcomment = "newtestcomment";

    private Long newid = 7L;

    private DCustomer newcustomer = new DCustomer();
    private String newkindofmeter = "newtestmeter";
    private double newmetercount = 6;
    private String newmeterId = "5";
    private Boolean newsubstitute = true;
    private Long newdateofreading = 110521L;

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
    @DisplayName("Test all setters")
    public void test_setter()
    {
        for(int i = 0; i<DReadings.length; i++)
        {
            // arrange / act
            DReadings[i].setId(newid);
            DReadings[i].setComment(newcomment);
            DReadings[i].setCustomer(newcustomer);
            DReadings[i].setKindofmeter(newkindofmeter);
            DReadings[i].setMetercount(newmetercount);
            DReadings[i].setMeterid(newmeterId);
            DReadings[i].setSubstitute(newsubstitute);
            DReadings[i].setDateofreading(newdateofreading);

            // assert
            assertEquals(newid, DReadings[i].getId());
            assertEquals(newcomment, DReadings[i].getComment());
            assertEquals(newcustomer, DReadings[i].getCustomer());
            assertEquals(newkindofmeter, DReadings[i].getKindofmeter());
            assertEquals(newmetercount, DReadings[i].getMetercount());
            assertEquals(newmeterId, DReadings[i].getMeterid());
            assertEquals(newsubstitute, DReadings[i].getSubstitute());
            assertEquals(newdateofreading, DReadings[i].getDateofreading());
        }
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
