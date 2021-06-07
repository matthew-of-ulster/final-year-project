package final_project;

import org.junit.Test;

import java_classes.ABI;
import java_classes.Action;
import java_classes.Comparator;
import java_classes.Measurement;
import java_classes.Status;


import static java_classes.ABIVerificationMethods.StringToABI;
import static java_classes.ABIVerificationMethods.VerifyNewABI;
import static java_classes.ABIVerificationMethods.userABIToString;
import static org.junit.Assert.assertEquals;

public class VerifyABI {
    ABI testABI = new ABI();
    @Test
    public void ABItoStringTest(){
        String expected = "alert na NA -1 na 50 f f f f f";
        assertEquals(expected, userABIToString(testABI));
    }

    @Test
    public void StringToABITest(){
        String testString = "alert na NA -1 na 50 f f f f f";
        assertEquals(StringToABI(testString).getString(),testABI.getString());
    }

    @Test
    public void sameCompTestFalse(){
        ABI ABI1 = new ABI();
        ABI user = new ABI();
        ABI1.var1 = Measurement.DA;
        user.var1 = Measurement.DA;
        ABI1.comp = Comparator.equals;

        assertEquals(false,VerifyNewABI(ABI1,user));
    }
    @Test
    public void sameCompTestTrue(){
        ABI ABI1 = new ABI();
        ABI user = new ABI();
        ABI1.var1 = Measurement.DA;
        user.var1 = Measurement.DA;
        ABI1.comp = Comparator.equals;
        user.comp = Comparator.equals;
        assertEquals(true,VerifyNewABI(ABI1,user));
    }

    @Test
    public void greaterCompTestFalse(){
        ABI ABI1 = new ABI();
        ABI user = new ABI();
        ABI1.var1 = Measurement.DA;
        user.var1 = Measurement.DA;
        ABI1.comp = Comparator.greater_than;
        user.comp = Comparator.less_than;

        assertEquals(false,VerifyNewABI(ABI1,user));
    }
    @Test
    public void greaterCompTestTrue(){
        ABI ABI1 = new ABI();
        ABI user = new ABI();
        ABI1.var1 = Measurement.DA;
        user.var1 = Measurement.DA;
        ABI1.comp = Comparator.greater_than;
        user.comp = Comparator.equals;
        user.var2 = 201;
        assertEquals(true,VerifyNewABI(ABI1,user));
    }

    @Test
    public void oppositeActionTestFalse(){
        ABI ABI1 = new ABI();
        ABI user = new ABI();
        ABI1.var1 = Measurement.IR;
        user.var1 = Measurement.IR;
        ABI1.action = Action.alert;
        user.action = Action.nalert;
        ABI1.comp = Comparator.greater_than;
        user.comp = Comparator.greater_than;

        assertEquals(false,VerifyNewABI(ABI1,user));
    }
    @Test
    public void oppositeActionTestTrue(){
        ABI ABI1 = new ABI();
        ABI user = new ABI();
        ABI1.var1 = Measurement.IR;
        user.var1 = Measurement.IR;
        ABI1.action = Action.alert;
        user.action = Action.nalert;
        ABI1.comp = Comparator.equals;
        user.comp = Comparator.equals;
        user.var2 = 201;

        assertEquals(true,VerifyNewABI(ABI1,user));
    }

    @Test
    public void oppositeStatusTestFalse(){
        ABI ABI1 = new ABI();
        ABI user = new ABI();
        ABI1.action = Action.alert;
        user.action = Action.alert;
        ABI1.status = Status.def;
        user.status = Status.ndef;

        assertEquals(false,VerifyNewABI(ABI1,user));
    }
    @Test
    public void oppositeStatusTestTrue(){
        ABI ABI1 = new ABI();
        ABI user = new ABI();
        ABI1.action = Action.alert;
        user.action = Action.alert;
        ABI1.status = Status.def;
        user.status = Status.def;

        assertEquals(true,VerifyNewABI(ABI1,user));
    }

}
