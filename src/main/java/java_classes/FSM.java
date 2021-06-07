package java_classes;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;
import static java_classes.ABIVerificationMethods.getABIs;
import static java_classes.ABIVerificationMethods.getUserABIs;
import static java_classes.ConnectBroker.mqttClient;
import static java_classes.SeqReqToggle.avaForFSM;
import static java_classes.SeqReqToggle.cs1ForFSM;
import static java_classes.SeqReqToggle.cs2ForFSM;
import static java_classes.SeqReqToggle.intForFSM;
import static java_classes.SeqReqToggle.secForFSM;

import android.widget.ImageView;
import android.widget.Toast;

public class FSM extends Activity {

    private TextView ABIstrue;
    private TextView MQTTmsg;
    private TextView safeState;
    public boolean halt = true;
    //ABI booleans
    public boolean ABI1 = false;
    public boolean ABI2 = false;
    public boolean ABI3 = false;
    public boolean ABI4 = false;
    public boolean ABI5 = false;
    public boolean ABI6 = false;
    public boolean ABI7 = false;
    public boolean ABI8 = false;
    public boolean ABI9 = false;
    public boolean ABI10 = false;
    public boolean ABI11 = false;
    public boolean ABI12 = false;

    //Measurement theshholds
    public static int PRMax = 100;
    public static int RRMax = 100;
    public static int DRMin = 100;
    public static int IRMax = 50;
    public static int DAEx = 50;
    public ArrayList<Boolean> ABIs = new ArrayList<>();
    public ArrayList<String> states= new ArrayList<>();
    public static String catchIt;
    public String isSafe = "Safe";
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fsm);

        ABIs.add(ABI1);
        ABIs.add(ABI2);
        ABIs.add(ABI3);
        ABIs.add(ABI4);
        ABIs.add(ABI5);
        ABIs.add(ABI6);
        ABIs.add(ABI7);
        ABIs.add(ABI8);
        ABIs.add(ABI9);
        ABIs.add(ABI10);
        ABIs.add(ABI11);
        ABIs.add(ABI12);

        states.add("AB1");
        states.add("AB2");
        states.add("AB3");
        states.add("AB4");
        states.add("AB5");
        states.add("AB6");
        states.add("AB7");
        states.add("AB8");
        states.add("AB9");
        states.add("AB10");
        states.add("AB11");
        states.add("AB12");

        Button go = (Button) findViewById(R.id.goBut);
        ABIstrue = (TextView) findViewById(R.id.ABIsTrue);
        MQTTmsg = (TextView) findViewById(R.id.MQTTmsg);
        safeState = (TextView) findViewById(R.id.safeState);

        imageView=findViewById(R.id.imageview);

        //button to start the state machine
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                if(!catchIt.equals("spaghetti")){
                    callCountdown();
                }

                 Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                imageView.startAnimation(animation);
            }catch(NullPointerException e){
                Toast.makeText(FSM.this, "Must Subscribe To Topic",
                        Toast.LENGTH_SHORT).show();
            }
            }
        });




   }
   //send a message back (if the system detects misbehaviour)
    public void publishMessage() {
        if (mqttClient.mqttAndroidClient.isConnected()) {
            try {
                mqttClient.publishMessage(ConnectBroker.client, "halt all action", 1,"PCA signal return");
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void callCountdown(){
        new CountDownTimer(900000, 500) {

            public void onTick(long millisUntilFinished) {
                //check if the system has previously detected misbehaviour
                if (halt) {
                    String toPrint = "";
                    ArrayList<String> trueABIs;
                    MQTTmsg.setText(catchIt);
                    String[] checkMsg = catchIt.split(";");

                    if (!checkMsg[0].equals("halt all action")) {

                        if(!checkMsg[0].trim().equals("OP")) {
                            //Determine if any ABIs have been detected
                            trueABIs = checkABIs();
                            if (trueABIs.isEmpty()) {
                                toPrint = "None true";
                            } else {
                                halt = true;
                                for (int i = 0; i < trueABIs.size(); i++) {
                                    toPrint += trueABIs.get(i);
                                    toPrint += " ";
                                }
                            }

                        }else{
                            toPrint = "None true";
                        }
                    }

                    safeState.setText(isSafe);
                    String compare = (String) ABIstrue.getText();
                    if(toPrint.equals("")){
                        toPrint = compare;
                    }
                    ABIstrue.setText(toPrint);
                    if (toPrint != "None true" & !checkMsg[0].equals("halt all action")) {
                        publishMessage();
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                        imageView.clearAnimation();
                    }

                }
            }
            public void onFinish() {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                imageView.clearAnimation();
            }

            }.start();
    }
    //call various methods to check if each ABI has been detected
    public ArrayList checkABIs(){
        MQTTmsg.setText(catchIt);
        String[] checkMsg = catchIt.split(";");
        String[] quickCheck = checkMsg[0].split(" ");

        if(intForFSM||avaForFSM||secForFSM) {
            if (quickCheck[0].equals("PR")) ABIs.set(0, isABI1(checkMsg));
            if (quickCheck[0].equals("RR")) ABIs.set(1, isABI2(checkMsg));
            if (quickCheck[0].equals("stat")) ABIs.set(2, isABI3(checkMsg));
            if (quickCheck[0].equals("DR")) ABIs.set(3, isABI4(checkMsg));
            if (quickCheck[0].equals("stat")) ABIs.set(5, isABI6(checkMsg));
            if (quickCheck[0].equals("stat")) ABIs.set(6, isABI7(checkMsg));
        }
        if(intForFSM) {
            if (quickCheck[0].equals("IR")) ABIs.set(7, isABI8(checkMsg));
            if (quickCheck[0].equals("DA")) ABIs.set(8, isABI9(checkMsg));
        }
        if(secForFSM) {
            if (quickCheck[0].equals("stat")) ABIs.set(9, isABI10(checkMsg));
            if (quickCheck[0].equals("stat")) ABIs.set(10, isABI11(checkMsg));
        }
        if(avaForFSM) {
            if (quickCheck[0].equals("stat")) ABIs.set(11, isABI12(checkMsg));
        }


        ArrayList<String> trues = new ArrayList<>();

        int trueCount = 0;
        for(int x = 0; x < ABIs.size(); x++){
            if(ABIs.get(x)){
                trues.add(states.get(x));
                trueCount++;
            }
        }

        ArrayList<ABI> userABIs = getUserABIs();
        if(!userABIs.isEmpty()) {
            int userABINum = 12;
            for (int i = 0; i < userABIs.size(); i++) {
                userABINum++;
                String thisABI = "";
                //check the seq req toggles and the UserABI seq reqs
                boolean matchSRs = (intForFSM && userABIs.get(i).SR1) || (avaForFSM && userABIs.get(i).SR2) || (secForFSM && userABIs.get(i).SR3 || (cs1ForFSM && userABIs.get(i).SR4) || (cs2ForFSM && userABIs.get(i).SR5));

                if (matchSRs && isUserABITripped(userABIs.get(i), checkMsg)) {
                    thisABI += ("AB" + userABINum);
                    trues.add(thisABI);
                    trueCount++;
                }
            }
        }

        if (trueCount>0){
            isSafe = "Unsafe";
        }else{
            isSafe = "Safe";
        }
        return trues;
    }

    public static boolean isUserABITripped(ABI abi,String[] checkMsg){
        boolean returnBool = false;
        switch(abi.var1){
            case NA:
                returnBool = checkStats(abi,checkMsg);
                break;
            case RR:
                returnBool = checkRR(abi,checkMsg);
                break;
            case PR:
                returnBool = checkPR(abi,checkMsg);
                break;
            case DA:
                returnBool = checkDA(abi,checkMsg);
                break;
            case IR:
                returnBool = checkIR(abi,checkMsg);
                break;
            case DR:
                returnBool = checkDR(abi,checkMsg);
                break;
        }
        return returnBool;
    }

    //CHeck if status based ABIs have been detected
    public static boolean checkStats(ABI abi, String[] msg){
        boolean returnBool = false;
        String[] msg1 = msg[0].split(" ");
        String[]msg2 = msg[1].split(" ");
        //Remember to compare against string
            //na, def,ndef, auth, nauth, healthy, nhealthy
            switch(abi.action){
                case alert:
                    switch(abi.status){
                        case na:
                            if(msg1[1].equals("na") && msg2[1].equals("alert")){
                                returnBool=true;
                            }
                            break;
                        case def:
                            if(msg1[1].equals("def") && msg2[1].equals("alert")){
                                returnBool=true;
                            }
                            break;
                        case ndef:
                            if(msg1[1].equals("ndef") && msg2[1].equals("alert")){
                                returnBool=true;
                            }
                            break;
                        case auth:
                            if(msg1[1].equals("auth") && msg2[1].equals("alert")){
                                returnBool=true;
                            }
                            break;
                        case nauth:
                            if(msg1[1].equals("nauth") && msg2[1].equals("alert")){
                                returnBool=true;
                            }
                            break;
                        case healthy:
                            if(msg1[1].equals("healthy") && msg2[1].equals("alert")){
                                returnBool=true;
                            }
                            break;
                        case nhealthy:
                            if(msg1[1].equals("nhealthy") && msg2[1].equals("alert")){
                                returnBool=true;
                            }
                            break;
                    }
                case nalert:
                    switch(abi.status){
                        case na:
                            if(msg1[1].equals("na") && msg2[1].equals("nalert")){
                                returnBool=true;
                            }
                            break;
                        case def:
                            if(msg1[1].equals("def") && msg2[1].equals("nalert")){
                                returnBool=true;
                            }
                            break;
                        case ndef:
                            if(msg1[1].equals("ndef") && msg2[1].equals("nalert")){
                                returnBool=true;
                            }
                            break;
                        case auth:
                            if(msg1[1].equals("auth") && msg2[1].equals("nalert")){
                                returnBool=true;
                            }
                            break;
                        case nauth:
                            if(msg1[1].equals("nauth") && msg2[1].equals("nalert")){
                                returnBool=true;
                            }
                            break;
                        case healthy:
                            if(msg1[1].equals("healthy") && msg2[1].equals("nalert")){
                                returnBool=true;
                            }
                            break;
                        case nhealthy:
                            if(msg1[1].equals("nhealthy") && msg2[1].equals("nalert")){
                                returnBool=true;
                            }
                            break;
                    }
                case accept:
                    switch(abi.status){
                        case na:
                            if(msg1[1].equals("na") && msg2[1].equals("accept")){
                                returnBool=true;
                            }
                            break;
                        case def:
                            if(msg1[1].equals("def") && msg2[1].equals("accept")){
                                returnBool=true;
                            }
                            break;
                        case ndef:
                            if(msg1[1].equals("ndef") && msg2[1].equals("accept")){
                                returnBool=true;
                            }
                            break;
                        case auth:
                            if(msg1[1].equals("auth") && msg2[1].equals("accept")){
                                returnBool=true;
                            }
                            break;
                        case nauth:
                            if(msg1[1].equals("nauth") && msg2[1].equals("accept")){
                                returnBool=true;
                            }
                            break;
                        case healthy:
                            if(msg1[1].equals("healthy") && msg2[1].equals("accept")){
                                returnBool=true;
                            }
                            break;
                        case nhealthy:
                            if(msg1[1].equals("nhealthy") && msg2[1].equals("accept")){
                                returnBool=true;
                            }
                            break;
                    }
                case reject:
                    switch(abi.status){
                        case na:
                            if(msg1[1].equals("na") && msg2[1].equals("naccept")){
                                returnBool=true;
                            }
                            break;
                        case def:
                            if(msg1[1].equals("def") && msg2[1].equals("naccept")){
                                returnBool=true;
                            }
                            break;
                        case ndef:
                            if(msg1[1].equals("ndef") && msg2[1].equals("naccept")){
                                returnBool=true;
                            }
                            break;
                        case auth:
                            if(msg1[1].equals("auth") && msg2[1].equals("naccept")){
                                returnBool=true;
                            }
                            break;
                        case nauth:
                            if(msg1[1].equals("nauth") && msg2[1].equals("naccept")){
                                returnBool=true;
                            }
                            break;
                        case healthy:
                            if(msg1[1].equals("healthy") && msg2[1].equals("naccept")){
                                returnBool=true;
                            }
                            break;
                        case nhealthy:
                            if(msg1[1].equals("nhealthy") && msg2[1].equals("naccept")){
                                returnBool=true;
                            }
                            break;
                    }
                case communicate:
                    switch(abi.status){
                        case na:
                            if(msg1[1].equals("na") && msg2[1].equals("com")){
                                returnBool=true;
                            }
                            break;
                        case def:
                            if(msg1[1].equals("def") && msg2[1].equals("com")){
                                returnBool=true;
                            }
                            break;
                        case ndef:
                            if(msg1[1].equals("ndef") && msg2[1].equals("com")){
                                returnBool=true;
                            }
                            break;
                        case auth:
                            if(msg1[1].equals("auth") && msg2[1].equals("com")){
                                returnBool=true;
                            }
                            break;
                        case nauth:
                            if(msg1[1].equals("nauth") && msg2[1].equals("com")){
                                returnBool=true;
                            }
                            break;
                        case healthy:
                            if(msg1[1].equals("healthy") && msg2[1].equals("com")){
                                returnBool=true;
                            }
                            break;
                        case nhealthy:
                            if(msg1[1].equals("nhealthy") && msg2[1].equals("com")){
                                returnBool=true;
                            }
                            break;
                    }
                case ncommunicate:
                    switch(abi.status){
                        case na:
                            if(msg1[1].equals("na") && msg2[1].equals("ncom")){
                                returnBool=true;
                            }
                            break;
                        case def:
                            if(msg1[1].equals("def") && msg2[1].equals("ncom")){
                                returnBool=true;
                            }
                            break;
                        case ndef:
                            if(msg1[1].equals("ndef") && msg2[1].equals("ncom")){
                                returnBool=true;
                            }
                            break;
                        case auth:
                            if(msg1[1].equals("auth") && msg2[1].equals("ncom")){
                                returnBool=true;
                            }
                            break;
                        case nauth:
                            if(msg1[1].equals("nauth") && msg2[1].equals("ncom")){
                                returnBool=true;
                            }
                            break;
                        case healthy:
                            if(msg1[1].equals("healthy") && msg2[1].equals("ncom")){
                                returnBool=true;
                            }
                            break;
                        case nhealthy:
                            if(msg1[1].equals("nhealthy") && msg2[1].equals("ncom")){
                                returnBool=true;
                            }
                            break;
                    }
            }

        return returnBool;
    }

    //CHeck the various measurement combinations to see if a dynamic ABI has been detected
    public static boolean checkPR(ABI abi, String[] msg){
        boolean returnBool = false;
        String[] msg1 = msg[0].split(" ");
        String[]msg2 = msg[1].split(" ");
        //Remember to compare against string
        //na, def,ndef, auth, nauth, healthy, nhealthy
        if(msg1[0].equals("PR")) {
            switch(abi.action) {

                case alert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case nalert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case accept:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case reject:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case communicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case ncommunicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


            }
        }
        return returnBool;

    }
    public static boolean checkRR(ABI abi, String[] msg){
        boolean returnBool = false;
        String[] msg1 = msg[0].split(" ");
        String[]msg2 = msg[1].split(" ");
        //Remember to compare against string
        //na, def,ndef, auth, nauth, healthy, nhealthy
        if(msg1[0].equals("RR")) {
            switch(abi.action) {

                case alert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case nalert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case accept:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case reject:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case communicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case ncommunicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


            }
        }
        return returnBool;

    }
    public static boolean checkDA(ABI abi, String[] msg){
        boolean returnBool = false;
        String[] msg1 = msg[0].split(" ");
        String[]msg2 = msg[1].split(" ");
        //Remember to compare against string
        //na, def,ndef, auth, nauth, healthy, nhealthy
        if(msg1[0].equals("DA")) {
            switch(abi.action) {

                case alert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) < abi.amount ){
                            returnBool = true;
                        }
                        break;
                        case greater_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case nalert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case accept:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case reject:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case communicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case ncommunicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


            }
        }
        return returnBool;

    }
    public static boolean checkIR(ABI abi, String[] msg){
        boolean returnBool = false;
        String[] msg1 = msg[0].split(" ");
        String[]msg2 = msg[1].split(" ");
        //Remember to compare against string
        //na, def,ndef, auth, nauth, healthy, nhealthy
        if(msg1[0].equals("IR")) {
            switch(abi.action) {

                case alert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case nalert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case accept:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case reject:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case communicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case ncommunicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


            }
        }
        return returnBool;

    }
    public static boolean checkDR(ABI abi, String[] msg){
        boolean returnBool = false;
        String[] msg1 = msg[0].split(" ");
        String[]msg2 = msg[1].split(" ");
        //Remember to compare against string
        //na, def,ndef, auth, nauth, healthy, nhealthy
        if(msg1[0].equals("DR")) {
            switch(abi.action) {

                case alert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("alert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case nalert:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("nalert") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case accept:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("accept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case reject:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("naccept") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }

                case communicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("com") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


                case ncommunicate:
                    switch (abi.comp) {
                        case less_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) < abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case greater_than:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) > abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case equals:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) == abi.amount ){
                                returnBool = true;
                            }
                            break;
                        case nequal:
                            if (msg2[1].equals("ncom") &&  parseInt(msg1[1]) != abi.amount ){
                                returnBool = true;
                            }
                            break;
                    }


            }
        }
        return returnBool;

    }

    //check with each behavior rule to monitor if they are violated
    public static Boolean isABI1(String[] checkMsg) {
        boolean alert = false;
        boolean nalert = false;

        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        int pulseRate =  Integer.parseInt(firstHalf[1]);
        if(secHalf[1].equals("alert")){
            alert=true;
        }
        if(secHalf[1].equals("nalert")){
            nalert = true;
        }

        if(pulseRate>PRMax && nalert) {
            return true;
        }

        if(pulseRate<=PRMax && alert){
            return true;
        }


        return false;
    }
    public static Boolean isABI2(String[] checkMsg) {
        boolean alert = false;
        boolean nalert = false;

        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        int resRate =  Integer.parseInt(firstHalf[1]);
        if(secHalf[1].equals("alert")){
            alert=true;
        }
        if(secHalf[1].equals("nalert")){
            nalert = true;
        }

        if(resRate>RRMax && nalert) {
            return true;
        }

        if(resRate<=RRMax && alert){
            return true;
        }


        return false;
    }
    public static Boolean isABI3(String[] checkMsg) {

        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        if(firstHalf[1].equals("def") && secHalf[1].equals("accept")) {
            return true;
        }

        if(firstHalf[1].equals("ndef") && secHalf[1].equals("naccept")){
            return true;
        }


        return false;
    }
    public static Boolean isABI4(String[] checkMsg) {
        boolean alert = false;
        boolean nalert = false;

        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        int drugAmount =  Integer.parseInt(firstHalf[1]);
        if(secHalf[1].equals("alert")){
            alert=true;
        }
        if(secHalf[1].equals("nalert")){
            nalert = true;
        }

        if(drugAmount<DRMin && nalert) {
            return true;
        }

        if(drugAmount>=DRMin && alert){
            return true;
        }
        return false;
    }
    public static Boolean isABI6(String[] checkMsg) {
        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        if(firstHalf[1].equals("auth") && secHalf[1].equals("naccept")) {
            return true;
        }

        return false;
    }
    public static Boolean isABI7(String[] checkMsg) {
        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");

        if(firstHalf[1].equals("nauth") && secHalf[1].equals("accept")) {
            return true;
        }
        return false;
    }
    public static Boolean isABI8(String[] checkMsg) {

        boolean accept = false;
        boolean naccept = false;

        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        int IR =  Integer.parseInt(firstHalf[1]);
        if(secHalf[1].equals("accept")){
            accept=true;
        }
        if(secHalf[1].equals("naccept")){
            naccept = true;
        }

        if(IR>IRMax && accept) {
            return true;
        }

        if(IR<=IRMax && naccept){
            return true;
        }
        return false;
    }
    public static Boolean isABI9(String[] checkMsg) {
        boolean accept = false;
        boolean naccept = false;

        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        int DA =  Integer.parseInt(firstHalf[1]);
        if(secHalf[1].equals("accept")){
            accept=true;
        }
        if(secHalf[1].equals("naccept")){
            naccept = true;
        }

        if(DA==DAEx && naccept) {
            return true;
        }

        if(DA!=DAEx && accept){
            return true;
        }
        return false;
    }
    public static Boolean isABI10(String[] checkMsg) {
        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        if(firstHalf[1].equals("nauth") && secHalf[1].equals("com")) {
            return true;
        }

        return false;

    }
    public static Boolean isABI11(String[] checkMsg) {
        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        if(firstHalf[1].equals("auth") && secHalf[1].equals("ncom")) {
            return true;
        }

        return false;
    }
    public static Boolean isABI12(String[] checkMsg) {
        String[] firstHalf = checkMsg[0].split(" ");
        String[] secHalf = checkMsg[1].split(" ");


        if(firstHalf[1].equals("healthy") && secHalf[1].equals("alert")) {
            return true;
        }

        if(firstHalf[1].equals("nhealthy") && secHalf[1].equals("nalert")) {
            return true;
        }
        return false;
    }
}

