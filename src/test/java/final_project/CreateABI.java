package final_project;

import org.junit.Test;

import java_classes.ABI;
import java_classes.Action;
import java_classes.Comparator;
import java_classes.Measurement;
import java_classes.Status;

import static org.junit.Assert.assertEquals;

public class CreateABI {
    ABI testABI = new ABI();
    @Test
    public void defaultABINameTest(){
        assertEquals("unnamed", testABI.name);
    }

    @Test
    public void defaultABIStatusTest(){
        assertEquals(Status.na, testABI.status);
    }

    @Test
    public void defaultABIActionTest(){
        assertEquals(Action.alert, testABI.action);
    }

    @Test
    public void defaultABIVar1Test(){
        assertEquals(Measurement.NA, testABI.var1);
    }
    @Test
    public void defaultABIAmountTest(){
        assertEquals(-1, testABI.amount);
    }
    @Test
    public void defaultABICompTest(){
        assertEquals(Comparator.na, testABI.comp);
    }

    @Test
    public void defaultABIVar2Test(){
        assertEquals(-1, testABI.var2);
    }

    @Test
    public void defaultABISR1Test(){
        assertEquals(false, testABI.SR1);
    }

    @Test
    public void defaultABISR2Test(){
        assertEquals(false, testABI.SR2);
    }

    @Test
    public void defaultABISR3Test(){
        assertEquals(false, testABI.SR3);
    }

    @Test
    public void defaultABISR4Test(){
        assertEquals(false, testABI.SR4);
    }

    @Test
    public void defaultABISR5Test(){
        assertEquals(false, testABI.SR5);
    }

    @Test
    public void printDefault(){
        String expected = "Name: "+testABI.name+"\nStatus: "+testABI.status+ "\nAction: " + testABI.action+"\nMeasurement: "
                +testABI.var1+"\n amount: "+ testABI.amount + "\nComparator: "+testABI.comp;
        assertEquals(expected, testABI.getString());
    }
}
