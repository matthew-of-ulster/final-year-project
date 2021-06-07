package final_project;

import org.junit.Test;

import java_classes.ABI;
import java_classes.Action;
import java_classes.Comparator;
import java_classes.Measurement;
import java_classes.Status;

import static java_classes.FSM.checkDA;
import static java_classes.FSM.checkDR;
import static java_classes.FSM.checkIR;
import static java_classes.FSM.checkPR;
import static java_classes.FSM.checkRR;
import static java_classes.FSM.checkStats;
import static java_classes.FSM.isABI1;
import static java_classes.FSM.isABI10;
import static java_classes.FSM.isABI11;
import static java_classes.FSM.isABI12;
import static java_classes.FSM.isABI2;
import static java_classes.FSM.isABI3;
import static java_classes.FSM.isABI4;
import static java_classes.FSM.isABI6;
import static java_classes.FSM.isABI7;
import static java_classes.FSM.isABI8;
import static java_classes.FSM.isABI9;
import static java_classes.FSM.isUserABITripped;
import static org.junit.Assert.assertEquals;

public class FSMTesting {
    ABI testABI = new ABI();
    @Test
    public void checkStatsTrueNaAlertTest(){
        testABI.status=Status.na;
        String[] msg= new String[]{"stat na","action alert"};
        assertEquals(true, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsFalseNaAlertTest(){
        testABI.status=Status.na;
        String[] msg= new String[]{"stat cat","action alert"};
        assertEquals(false, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsTrueDefNalertTest(){
        testABI.action=Action.nalert;
        testABI.status=Status.def;
        String[] msg= new String[]{"stat def","action nalert"};
        assertEquals(true, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsFalseDefNalertTest(){
        testABI.action=Action.nalert;
        testABI.status=Status.def;
        String[] msg= new String[]{"stat cat","action nalert"};
        assertEquals(false, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsTrueNdefAcceptTest(){
        testABI.action=Action.accept;
        testABI.status=Status.ndef;
        String[] msg= new String[]{"stat ndef","action accept"};
        assertEquals(true, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsFalseNdefAcceptTest(){
        testABI.action=Action.accept;
        testABI.status=Status.ndef;
        String[] msg= new String[]{"stat ndef","action nalert"};
        assertEquals(false, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsTrueAuthNacceptTest(){
        testABI.action=Action.reject;
        testABI.status=Status.auth;
        String[] msg= new String[]{"stat auth","action naccept"};
        assertEquals(true, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsFalseAuthNacceptTest(){
        testABI.action=Action.reject;
        testABI.status=Status.nauth;
        String[] msg= new String[]{"stat auth","action naccept"};
        assertEquals(false, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsTrueNauthComTest(){
        testABI.action=Action.communicate;
        testABI.status=Status.nauth;
        String[] msg= new String[]{"stat nauth","action com"};
        assertEquals(true, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsFalseNauthComTest(){
        testABI.action=Action.communicate;
        testABI.status=Status.nauth;
        String[] msg= new String[]{"stat nauth","action commmm"};
        assertEquals(false, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsTrueHealthyNcomTest(){
        testABI.action=Action.ncommunicate;
        testABI.status=Status.healthy;
        String[] msg= new String[]{"stat healthy","action ncom"};
        assertEquals(true, checkStats(testABI,msg));
    }

    @Test
    public void checkStatsFalseHealthyNcomTest(){
        testABI.action=Action.ncommunicate;
        testABI.status=Status.healthy;
        String[] msg= new String[]{"stat nauth","action commmm"};
        assertEquals(false, checkStats(testABI,msg));
    }

    @Test
    public void checkPRTrueAlertLTTest(){
        testABI.action=Action.alert;
        testABI.comp=Comparator.less_than;
        testABI.amount=20;
        String[] msg= new String[]{"PR 0","action alert"};
        assertEquals(true, checkPR(testABI,msg));
    }

    @Test
    public void checkPRFalseAlertLTTest(){
        testABI.action=Action.alert;
        testABI.comp=Comparator.less_than;
        testABI.amount=20;
        String[] msg= new String[]{"PR 30","action alert"};
        assertEquals(false, checkPR(testABI,msg));
    }

    @Test
    public void checkPRTrueNalertGTTest(){
        testABI.action=Action.nalert;
        testABI.comp=Comparator.greater_than;
        testABI.amount=20;
        String[] msg= new String[]{"PR 30","action nalert"};
        assertEquals(true, checkPR(testABI,msg));
    }

    @Test
    public void checkPRFalseNalertGTTest(){
        testABI.action=Action.nalert;
        testABI.comp=Comparator.greater_than;
        testABI.amount=20;
        String[] msg= new String[]{"PR 30","action falert"};
        assertEquals(false, checkPR(testABI,msg));
    }

    @Test
    public void checkPRTrueAcceptEqTest(){
        testABI.action=Action.accept;
        testABI.comp=Comparator.equals;
        testABI.amount=20;
        String[] msg= new String[]{"PR 20","action accept"};
        assertEquals(true, checkPR(testABI,msg));
    }

    @Test
    public void checkPRFalseAcceptEqTest(){
        testABI.action=Action.accept;
        testABI.comp=Comparator.equals;
        testABI.amount=25;
        String[] msg= new String[]{"PR 20","action accept"};
        assertEquals(false, checkPR(testABI,msg));
    }

    @Test
    public void checkPRTrueNacceptNeqTest(){
        testABI.action=Action.reject;
        testABI.comp=Comparator.nequal;
        testABI.amount=20;
        String[] msg= new String[]{"PR 21","action naccept"};
        assertEquals(true, checkPR(testABI,msg));
    }

    @Test
    public void checkPRFalseNacceptNeqTest(){
        testABI.action=Action.reject;
        testABI.comp=Comparator.nequal;
        testABI.amount=20;
        String[] msg= new String[]{"PR 20","action alert"};
        assertEquals(false, checkPR(testABI,msg));
    }

    @Test
    public void checkPRTrueComLTTest(){
        testABI.action=Action.communicate;
        testABI.comp=Comparator.less_than;
        testABI.amount=20;
        String[] msg= new String[]{"PR 19","action com"};
        assertEquals(true, checkPR(testABI,msg));
    }

    @Test
    public void checkPRFalseComLTTest(){
        testABI.action=Action.communicate;
        testABI.comp=Comparator.less_than;
        testABI.amount=20;
        String[] msg= new String[]{"IR 21","action com"};
        assertEquals(false, checkPR(testABI,msg));
    }

    @Test
    public void checkPRTrueNcomEqTest(){
        testABI.action=Action.ncommunicate;
        testABI.comp=Comparator.equals;
        testABI.amount=20;
        String[] msg= new String[]{"PR 20","action ncom"};
        assertEquals(true, checkPR(testABI,msg));
    }

    @Test
    public void checkPRFalseNcomEqTest(){
        testABI.action=Action.ncommunicate;
        testABI.comp=Comparator.equals;
        testABI.amount=20;
        String[] msg= new String[]{"PR 21","action ncom"};
        assertEquals(false, checkPR(testABI,msg));
    }

    @Test
    public void checkRRTrueAlertEqTest(){
        testABI.action=Action.alert;
        testABI.comp=Comparator.equals;
        testABI.amount=20;
        String[] msg= new String[]{"RR 20","action alert"};
        assertEquals(true, checkRR(testABI,msg));
    }

    @Test
    public void checkRRFalseAlertEqTest(){
        testABI.action=Action.alert;
        testABI.comp=Comparator.equals;
        testABI.amount=20;
        String[] msg= new String[]{"RR 21","action alert"};
        assertEquals(false, checkPR(testABI,msg));
    }

    @Test
    public void checkDATrueAcceptLTTest(){
        testABI.action=Action.accept;
        testABI.comp=Comparator.less_than;
        testABI.amount=20;
        String[] msg= new String[]{"DA 19","action accept"};
        assertEquals(true, checkDA(testABI,msg));
    }

    @Test
    public void checkDAFalseAcceptLTTest(){
        testABI.action=Action.accept;
        testABI.comp=Comparator.less_than;
        testABI.amount=20;
        String[] msg= new String[]{"DA 20","action accept"};
        assertEquals(false, checkDA(testABI,msg));
    }

    @Test
    public void checkIRTrueComNeqTest(){
        testABI.action=Action.communicate;
        testABI.comp=Comparator.nequal;
        testABI.amount=20;
        String[] msg= new String[]{"IR 19","action com"};
        assertEquals(true, checkIR(testABI,msg));
    }

    @Test
    public void checkIRFalseComNeqTest(){
        testABI.action=Action.communicate;
        testABI.comp=Comparator.nequal;
        testABI.amount=20;
        String[] msg= new String[]{"IR 20","action ncom"};
        assertEquals(false, checkIR(testABI,msg));
    }

    @Test
    public void checkDRTrueNcomEqTest(){
        testABI.action=Action.ncommunicate;
        testABI.comp=Comparator.equals;
        testABI.amount=20;
        String[] msg= new String[]{"DR 20","action ncom"};
        assertEquals(true, checkDR(testABI,msg));
    }

    @Test
    public void isABI1AlertTrueTest(){
        String[] msg= new String[]{"PR 0","action alert"};
        assertEquals(true, isABI1(msg));
    }
    @Test
    public void isABI1NalertTrueTest(){
        String[] msg= new String[]{"PR 500","action nalert"};
        assertEquals(true, isABI1(msg));
    }
    @Test
    public void isABI1AlertFalseTest(){
        String[] msg= new String[]{"PR 500","action alert"};
        assertEquals(false, isABI1(msg));
    }
    @Test
    public void isABI1NalertFalseTest(){
        String[] msg= new String[]{"PR 0","action nalert"};
        assertEquals(false, isABI1(msg));
    }
    @Test
    public void isABI2AlertTrueTest(){
        String[] msg= new String[]{"RR 0","action alert"};
        assertEquals(true, isABI2(msg));
    }
    @Test
    public void isABI2NalertTrueTest(){
        String[] msg= new String[]{"RR 500","action nalert"};
        assertEquals(true, isABI2(msg));
    }
    @Test
    public void isABI2AlertFalseTest(){
        String[] msg= new String[]{"RR 500","action alert"};
        assertEquals(false, isABI2(msg));
    }
    @Test
    public void isABI2NalertFalseTest(){
        String[] msg= new String[]{"RR 0","action nalert"};
        assertEquals(false, isABI2(msg));
    }

    @Test
    public void isABI3AcceptTrueTest(){
        String[] msg= new String[]{"stat def","action accept"};
        assertEquals(true, isABI3(msg));
    }
    @Test
    public void isABI3NAcceptTrueTest(){
        String[] msg= new String[]{"stat ndef","action naccept"};
        assertEquals(true, isABI3(msg));
    }
    @Test
    public void isABI3AcceptFalseTest(){
        String[] msg= new String[]{"stat ndef","action accept"};
        assertEquals(false, isABI3(msg));
    }
    @Test
    public void isABI3NAcceptFalseTest(){
        String[] msg= new String[]{"stat def","action naccept"};
        assertEquals(false, isABI3(msg));
    }

    @Test
    public void isABI4AlertTrueTest(){
        String[] msg= new String[]{"placeholder 100","action alert"};
        assertEquals(true, isABI4(msg));
    }
    @Test
    public void isABI4NalertTrueTest(){
        String[] msg= new String[]{"RR 0","action nalert"};
        assertEquals(true, isABI4(msg));
    }
    @Test
    public void isABI4AlertFalseTest(){
        String[] msg= new String[]{"RR 0","action alert"};
        assertEquals(false, isABI4(msg));
    }
    @Test
    public void isABI4NalertFalseTest(){
        String[] msg= new String[]{"RR 100","action nalert"};
        assertEquals(false, isABI4(msg));
    }


    @Test
    public void isABI6TrueTest(){
        String[] msg= new String[]{"stat auth","action naccept"};
        assertEquals(true, isABI6(msg));
    }
    @Test
    public void isABI6FalseTest(){
        String[] msg= new String[]{"stat nauth","action naccept"};
        assertEquals(false, isABI6(msg));
    }

    @Test
    public void isABI7TrueTest(){
        String[] msg= new String[]{"stat nauth","action accept"};
        assertEquals(true, isABI7(msg));
    }
    @Test
    public void isABI7FalseTest(){
        String[] msg= new String[]{"stat nauth","action naccept"};
        assertEquals(false, isABI7(msg));
    }

    @Test
    public void isABI8AcceptTrueTest(){
        String[] msg= new String[]{"IR 100","action accept"};
        assertEquals(true, isABI8(msg));
    }
    @Test
    public void isABI8NacceptTrueTest(){
        String[] msg= new String[]{"IR 0","action naccept"};
        assertEquals(true, isABI8(msg));
    }
    @Test
    public void isABI8AcceptFalseTest(){
        String[] msg= new String[]{"IR 0","action accept"};
        assertEquals(false, isABI8(msg));
    }
    @Test
    public void isABI8NacceptFalseTest(){
        String[] msg= new String[]{"IR 100","action naccept"};
        assertEquals(false, isABI8(msg));
    }

    @Test
    public void isABI9AcceptTrueTest(){
        String[] msg= new String[]{"DA 0","action accept"};
        assertEquals(true, isABI9(msg));
    }
    @Test
    public void isABI9NacceptTrueTest(){
        String[] msg= new String[]{"DA 50","action naccept"};
        assertEquals(true, isABI9(msg));
    }
    @Test
    public void isABI9AcceptFalseTest(){
        String[] msg= new String[]{"DA 50","action accept"};
        assertEquals(false, isABI9(msg));
    }
    @Test
    public void isABI9NacceptFalseTest(){
        String[] msg= new String[]{"DA 250","action naccept"};
        assertEquals(false, isABI9(msg));
    }

    @Test
    public void isABI10TrueTest(){
        String[] msg= new String[]{"stat nauth","action com"};
        assertEquals(true, isABI10(msg));
    }
    @Test
    public void isABI10FalseTest(){
        String[] msg= new String[]{"stat nauth","action ncom"};
        assertEquals(false, isABI10(msg));
    }

    @Test
    public void isABI11TrueTest(){
        String[] msg= new String[]{"stat auth","action ncom"};
        assertEquals(true, isABI11(msg));
    }
    @Test
    public void isABI11FalseTest(){
        String[] msg= new String[]{"stat nauth","action ncom"};
        assertEquals(false, isABI11(msg));
    }


    @Test
    public void isABI12AlertTrueTest(){
        String[] msg= new String[]{"stat healthy","action alert"};
        assertEquals(true, isABI12(msg));
    }
    @Test
    public void isABI12AlertFalseTest(){
        String[] msg= new String[]{"stat healthy","action naccept"};
        assertEquals(false, isABI12(msg));
    }

    @Test
    public void isABI12NalertTrueTest(){
        String[] msg= new String[]{"stat nhealthy","action nalert"};
        assertEquals(true, isABI12(msg));
    }
    @Test
    public void isABI12NalertFalseTest(){
        String[] msg= new String[]{"stat nhealthy","action alert"};
        assertEquals(false, isABI12(msg));
    }

    @Test
    public void isUserTrippedNATrueTest(){
        testABI.status = Status.nhealthy;
        testABI.var1=Measurement.NA;
        String[] msg= new String[]{"stat nhealthy","action alert"};
        assertEquals(true, isUserABITripped(testABI,msg));
    }
    @Test
    public void isUserTrippedNAFalseTest(){
        testABI.action=Action.accept;
        testABI.status = Status.nhealthy;
        testABI.var1=Measurement.NA;
        String[] msg= new String[]{"stat nhealthy","action nalert"};
        assertEquals(false, isUserABITripped(testABI,msg));
    }
}
