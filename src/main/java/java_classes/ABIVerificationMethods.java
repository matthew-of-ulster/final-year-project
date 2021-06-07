package java_classes;

import java.util.ArrayList;

import static java_classes.UserABI.databaseHelper;
import static java_classes.UserABI.arrayList;
import static java_classes.UserABI.listView;
import static java_classes.UserABI.arrayAdapter;

public class ABIVerificationMethods {

    //return an arraylist of static ABIs
    public static ArrayList<ABI> getABIs(){
            arrayList = databaseHelper.getAllText();

            ABI ABI1a = new ABI(Action.nalert, Status.na, Measurement.PR, -1, Comparator.greater_than, 100, false);
            ABI ABI1b = new ABI(Action.alert, Status.na, Measurement.PR, -1, Comparator.less_than, 101, false);
            ABI ABI2a = new ABI(Action.nalert, Status.na, Measurement.RR, -1, Comparator.greater_than, 100, false);
            ABI ABI2b = new ABI(Action.alert, Status.na, Measurement.RR, -1, Comparator.less_than, 101, false);
            ABI ABI3a = new ABI(Action.nalert, Status.def, Measurement.NA, -1, Comparator.na, -1, false);
            ABI ABI3b = new ABI(Action.alert, Status.ndef, Measurement.NA, -1, Comparator.na, -1, false);
            ABI ABI4a = new ABI(Action.nalert, Status.na, Measurement.DR, -1, Comparator.less_than, 100, false);
            ABI ABI4b = new ABI(Action.alert, Status.na, Measurement.DR, -1, Comparator.greater_than, 99, false);
            ABI ABI5a = new ABI(Action.nalert, Status.na, Measurement.DR, -1, Comparator.less_than, 100, false);
            ABI ABI5b = new ABI(Action.alert, Status.na, Measurement.DR, -1, Comparator.greater_than, 99, false);
            ABI ABI6 = new ABI(Action.reject, Status.auth, Measurement.NA, -1, Comparator.na, -1, false);
            ABI ABI7 = new ABI(Action.accept, Status.nauth, Measurement.NA, -1, Comparator.na, -1, false);
            ABI ABI8a = new ABI(Action.nalert, Status.na, Measurement.IR, -1, Comparator.greater_than, 50, false);
            ABI ABI8b = new ABI(Action.alert, Status.na, Measurement.IR, -1, Comparator.less_than, 51, false);
            ABI ABI9a = new ABI(Action.alert, Status.na, Measurement.DA, -1, Comparator.equals, 50, false);
            ABI ABI9b = new ABI(Action.nalert, Status.na, Measurement.DA, -1, Comparator.nequal, 50, false);
            ABI ABI10 = new ABI(Action.communicate, Status.nauth, Measurement.NA, -1, Comparator.na, -1, false);
            ABI ABI11 = new ABI(Action.ncommunicate, Status.auth, Measurement.NA, -1, Comparator.na, -1, false);
            ABI ABI12a = new ABI(Action.alert, Status.healthy, Measurement.NA, -1, Comparator.na, -1, false);
            ABI ABI12b = new ABI(Action.nalert, Status.nhealthy, Measurement.NA, -1, Comparator.na, -1, false);

            ArrayList<ABI> ABIs = new ArrayList<ABI>();
            ABIs.add(ABI1a);
            ABIs.add(ABI1b);
            ABIs.add(ABI2a);
            ABIs.add(ABI2b);
            ABIs.add(ABI3a);
            ABIs.add(ABI3b);
            ABIs.add(ABI4a);
            ABIs.add(ABI4b);
            ABIs.add(ABI5a);
            ABIs.add(ABI5b);
            ABIs.add(ABI6);
            ABIs.add(ABI7);
            ABIs.add(ABI8a);
            ABIs.add(ABI8b);
            ABIs.add(ABI9a);
            ABIs.add(ABI9b);
            ABIs.add(ABI10);
            ABIs.add(ABI11);
            ABIs.add(ABI12a);
            ABIs.add(ABI12b);

            for(int i = 0;i<arrayList.size();i++){
                ABI tempABI = StringToABI(String.valueOf(arrayList.get(i)));
                ABIs.add(tempABI);
            }
            //Add any found abis to the arraylist
            return ABIs;
        }

    //return an arraylist of dynamic ABIs (from the database)
    public static ArrayList<ABI> getUserABIs(){

        arrayList.clear();
        arrayList.addAll(databaseHelper.getAllText());

        ArrayList<ABI> ABIs = new ArrayList<ABI>();
        for(int i = 0;i<arrayList.size();i++){
            ABI tempABI = StringToABI(String.valueOf(arrayList.get(i)));
            ABIs.add(tempABI);
        }
        //Add any found abis to the arraylist
        return ABIs;
    }

    //add a dynamic ABI to the database
    public static String add(ABI user) {
            String returnString = "User ABI ";
            ArrayList<ABI> ABIs = getABIs();
            boolean accepted=false;

            boolean stop=true;
                int limit = ABIs.size();
                for(int i =0; i<limit;i++) {
                    accepted= VerifyNewABI(ABIs.get(i), user);
                    if (!accepted) {
                        returnString = "Contradicts ABI: " + (i + 1);
                        break;
                    }else {
                        returnString = "Accepted.";
                        if(i==limit-1) {
                            String text = userABIToString(user);
                            if (databaseHelper.addText(text)) {//Clear EditText
                                //Display Toast Message
                                //CLear ArrayList
                                arrayList.clear();
                                arrayList.addAll(databaseHelper.getAllText());
                                //Refresh ListView
                                arrayAdapter.notifyDataSetChanged();
                                listView.invalidateViews();
                                listView.refreshDrawableState();
                            }ABIs.add(user);
                        }
                    }
                }
                returnString += ("\nABI Total Size: " + ABIs.size());



            return returnString;
        }

        //Convert an ABI of type ABI to the STring format used to store ABIs in the database
        public static String userABIToString(ABI user) {
            String returnString = "";
        switch (user.action) {
            case alert:
                returnString += "alert ";
                break;
            case nalert:
                returnString += "nalert ";
                break;
            case accept:
                returnString += "accept ";
                break;
            case reject:
                returnString += "reject ";
                break;
            case communicate:
                returnString += "communicate ";
                break;
            case ncommunicate:
                returnString += "ncommunicate ";
                break;
        }
        switch (user.status) {
            case na:
                returnString += "na ";
                break;
            case def:
                returnString += "def ";
                break;
            case ndef:
                returnString += "ndef ";
                break;
            case auth:
                returnString += "auth ";
                break;
            case nauth:
                returnString += "nauth ";
                break;
            case healthy:
                returnString += "healthy ";
                break;
            case nhealthy:
                returnString += "nhealthy ";
                break;
        }
        switch (user.var1) {
            case NA:
                returnString += "NA ";
                break;
            case PR:
                returnString += "PR ";
                break;
            case RR:
                returnString += "RR ";
                break;
            case DR:
                returnString += "DR ";
                break;
            case IP:
                returnString += "IP ";
                break;
            case IR:
                returnString += "IR ";
                break;
            case DA:
                returnString += "DA ";
                break;
        }
        returnString+= user.amount + " ";
        switch (user.comp) {
            case na:
                returnString += "na ";
                break;
            case greater_than:
                returnString += "gt ";
                break;
            case less_than:
                returnString += "lt ";
                break;
            case equals:
                returnString += "eq ";
                break;
            case nequal:
                returnString += "neq ";
                break;
        }
        returnString += "50";

        if(user.SR1){
            returnString+= " t";
        }else{
            returnString+= " f";
        }

            if(user.SR2){
                returnString+= " t";
            }else{
                returnString+= " f";
            }

            if(user.SR3){
                returnString+= " t";
            }else{
                returnString+= " f";
            }

            if(user.SR4){
                returnString+= " t";
            }else{
                returnString+= " f";
            }

            if(user.SR5){
                returnString+= " t";
            }else{
                returnString+= " f";
            }

            return returnString;
    }

        //Convert an ABI from the String format used to store ABIs in the database to an ABI of type ABI
        public static ABI StringToABI(String text) {
            ABI ABI = new ABI();
            String[] split = text.split(" ");
            switch(split[0]) {
                case "alert":
                    ABI.action = Action.alert;
                    break;
                case "nalert":
                    ABI.action = Action.nalert;
                    break;
                case "accept":
                    ABI.action = Action.accept;
                    break;
                case "reject":
                    ABI.action = Action.reject;
                    break;
                case "communicate":
                    ABI.action = Action.communicate;
                    break;
                case "ncommunicate":
                    ABI.action = Action.ncommunicate;
                    break;
            }
            switch(split[1]) {
                case "na":
                    ABI.status = Status.na;
                    break;
                case "def":
                    ABI.status = Status.def;
                    break;
                case "ndef":
                    ABI.status = Status.ndef;
                    break;
                case "auth":
                    ABI.status = Status.auth;
                    break;
                case "nauth":
                    ABI.status = Status.nauth;
                    break;
                case "healthy":
                    ABI.status = Status.healthy;
                    break;
                case "nhealthy":
                    ABI.status = Status.nhealthy;
                    break;
            }
            switch(split[2]) {
                case("NA"):
                    ABI.var1 = Measurement.NA;
                    break;
                case("PR"):
                    ABI.var1= Measurement.PR;
                    break;
                case("RR"):
                    ABI.var1= Measurement.RR;
                    break;
                case("DR"):
                    ABI.var1= Measurement.DR;
                    break;
                case("IP"):
                    ABI.var1= Measurement.IP;
                    break;
                case("IR"):
                    ABI.var1= Measurement.IR;
                    break;
                case("DA"):
                    ABI.var1= Measurement.DA;
                    break;
            }
            ABI.amount=Integer.valueOf(split[3]);
            switch(split[4]) {
                case ("gt"):
                    ABI.comp= Comparator.greater_than;
                    break;
                case("na"):
                    ABI.comp= Comparator.na;
                    break;
                case("lt"):
                    ABI.comp= Comparator.less_than;
                    break;
                case("eq"):
                    ABI.comp= Comparator.equals;
                    break;
                case("neq"):
                    ABI.comp= Comparator.nequal;
                    break;
            }

            ABI.var2=Integer.valueOf(split[5]);


            if(split[6].equals("t")) {
                ABI.SR1=true;
            }else {
                ABI.SR1=false;
            }

            if(split[7].equals("t")) {
                ABI.SR2=true;
            }else {
                ABI.SR2=false;
            }

            if(split[8].equals("t")) {
                ABI.SR3=true;
            }else {
                ABI.SR3=false;
            }

            if(split[9].equals("t")) {
                ABI.SR4=true;
            }else {
                ABI.SR4=false;
            }

            if(split[10].equals("t")) {
                ABI.SR5=true;
            }else {
                ABI.SR5=false;
            }

            return ABI;



        }

        //check if two ABIs of type ABI contradict one another
        public static boolean VerifyNewABI(ABI ABI, ABI user) {
            if (ABI.var1 == user.var1) {																	//Same Measurement
                if(user.var1 != Measurement.NA) {
                    if(ABI.action == user.action) {																//Same Action

                        if(ABI.comp == user.comp) {																//Same Comparator
                            if(user.comp == Comparator.less_than && user.var2>ABI.var2) {
                                return false;

                            }else if(user.comp == Comparator.greater_than && user.var2<ABI.var2) {
                                return false;
                            }else if(user.comp == Comparator.equals && user.var2 != ABI.var2) {
                                return false;
                            }else if(user.comp == Comparator.nequal && user.var2 == ABI.var2) {
                                return false;
                            }

                        }else if(ABI.comp== Comparator.less_than) {
                            if(user.comp== Comparator.greater_than) {
                                return false;
                            }else if(user.comp== Comparator.equals && user.var2>=ABI.var2) {
                                return false;
                            }else if(user.comp== Comparator.nequal&&user.var2<ABI.var2) {
                                return false;
                            }

                        }else if(ABI.comp== Comparator.greater_than) {
                            if(user.comp== Comparator.less_than) {
                                return false;
                            }else if(user.comp== Comparator.equals && user.var2<=ABI.var2) {
                                return false;
                            }else if(user.comp== Comparator.nequal&&user.var2>ABI.var2) {
                                return false;
                            }
                        }else if(ABI.comp== Comparator.equals) {
                            if(user.comp!= Comparator.equals) {
                                return false;
                            }

                        }

                    }else if((ABI.action== Action.alert && user.action== Action.nalert)||(ABI.action== Action.nalert && user.action== Action.alert)) {
                        if(ABI.comp == user.comp) {																//Same Comparator
                            if(user.comp == Comparator.less_than) {
                                return false;

                            }else if(user.comp == Comparator.greater_than) {
                                return false;
                            }else if(user.comp == Comparator.equals && user.var2 == ABI.var2) {
                                return false;
                            }else if(user.comp == Comparator.nequal && user.var2 != ABI.var2) {
                                return false;
                            }

                        }else if(ABI.comp== Comparator.less_than) {
                            if(user.comp== Comparator.greater_than && user.var2<ABI.var2) {
                                return false;
                            }else if(user.comp== Comparator.equals && user.var2<ABI.var2) {
                                return false;
                            }else if(user.comp== Comparator.nequal) {
                                return false;
                            }

                        }else if(ABI.comp== Comparator.greater_than) {
                            if(user.comp== Comparator.less_than && user.var2>ABI.var2) {
                                return false;
                            }else if(user.comp== Comparator.equals && user.var2>ABI.var2) {
                                return false;
                            }else if(user.comp== Comparator.nequal) {
                                return false;
                            }
                        }else if(ABI.comp== Comparator.equals) {
                            if(user.comp== Comparator.less_than && user.var2>ABI.var2) {
                                return false;
                            }else if(user.comp== Comparator.greater_than && user.var2<ABI.var2) {
                                return false;
                            }else if(user.comp== Comparator.nequal && user.var2!=ABI.var2) {
                                return false;
                            }

                        }
                    }
                }else { //The type is the same but not a Measurement (Measurement.na)
                    boolean difAlert = (ABI.action== Action.alert&&user.action== Action.nalert) ||(ABI.action== Action.nalert&&user.action== Action.alert) ;
                    boolean difAccept = (ABI.action== Action.accept&&user.action== Action.reject) ||(ABI.action== Action.reject&&user.action== Action.accept) ;
                    boolean difComm = (ABI.action== Action.communicate&&user.action== Action.ncommunicate) ||(ABI.action== Action.ncommunicate&&user.action== Action.communicate) ;
                    if(ABI.action == user.action) {
                        if(ABI.status== Status.def && user.status== Status.ndef) {
                            return false;
                        }else if(ABI.status== Status.ndef && user.status== Status.def) {
                            return false;
                        }else if(ABI.status== Status.auth && user.status== Status.nauth) {
                            return false;
                        }else if(ABI.status== Status.nauth && user.status== Status.auth) {
                            return false;
                        }else if(ABI.status== Status.healthy && user.status== Status.nhealthy) {
                            return false;
                        }else if(ABI.status== Status.nhealthy && user.status== Status.healthy) {
                            return false;
                        }
                    }else if(difAlert || difAccept || difComm) {
                        if(ABI.status==user.status) {
                            return false;
                        }
                    }

                }
            }//check next please



            return true;
        }

    }


