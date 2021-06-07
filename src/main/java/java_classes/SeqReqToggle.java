package java_classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


import java.util.ArrayList;

import static android.text.TextUtils.concat;

public class SeqReqToggle extends AppCompatActivity {

    private static final String TAG = "SeqReqToggle";
    public static boolean intForFSM = false;
    public static boolean avaForFSM = false;
    public static boolean secForFSM = false;
    public static boolean cs1ForFSM = false;
    public static boolean cs2ForFSM = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seq_req_select);

        //access buttons and switches

        final Button integrityDes = (Button) findViewById(R.id.des1);
        final Switch intSwitch = (Switch)findViewById(R.id.intSwitch);
        intSwitch.setChecked(true);

        final Button availabilityDes = (Button) findViewById(R.id.des2);
        final Switch avaSwitch = (Switch)findViewById(R.id.avaSwitch);
        avaSwitch.setChecked(true);

        final Button securityDes = (Button) findViewById(R.id.des3);
        final Switch secSwitch = (Switch)findViewById(R.id.secSwitch);
        secSwitch.setChecked(true);

        final Button cusDes1 = (Button) findViewById(R.id.desCus1);
        final Switch cusSwitch1 = (Switch)findViewById(R.id.cusSwitch1);
        cusSwitch1.setChecked(false);

        final Button cusDes2 = (Button) findViewById(R.id.desCus2);
        final Switch cusSwitch2 = (Switch)findViewById(R.id.cusSwitch2);
        cusSwitch2.setChecked(false);

        //testStrings for static seqReq
        final String testString1 ="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas bibendum sed felis id imperdiet. Duis dapibus aliquet leo ac porttitor. Nam consectetur pharetra aliquet. Praesent tincidunt, nisl et accumsan varius, erat lorem venenatis ligula, sit amet mattis ligula mauris eget diam. Fusce id erat libero. Vestibulum ut tempor mauris, sed feugiat ante. Praesent interdum magna vel vulputate pellentesque. Morbi at lectus et justo sodales tempor eu quis felis. Aliquam commodo ipsum a felis dignissim luctus. Proin sed venenatis elit. Cras consectetur mollis risus, eu mollis sem fermentum sodales. Donec nec cursus dolor. Vivamus pellentesque, nisi quis lacinia tincidunt, eros risus mattis nisi, vel tincidunt est urna sed libero. Ut quis convallis tortor, vitae malesuada felis.";
        final String testString2 ="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas bibendum sed felis id imperdiet. Duis dapibus aliquet leo ac porttitor. Nam consectetur pharetra aliquet. ";
        final String testString3 ="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas bibendum sed felis id imperdiet. ";

        final TextView printSeqReq = (TextView) findViewById(R.id.seqReqDes);
        final String[] stringToSet = {""};


        intSwitch.setChecked(ConnectBroker.checkedInt);
        avaSwitch.setChecked(ConnectBroker.checkedAva);
        secSwitch.setChecked(ConnectBroker.checkedSec);
        cusSwitch1.setChecked(ConnectBroker.cusSec1);
        cusSwitch2.setChecked(ConnectBroker.cusSec2);


        //Change ?-textView to testString
        integrityDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringToSet[0] = opToSeq(ConnectBroker.opString, "integrity");
                printSeqReq.setText(stringToSet[0]);
            }
        });
        availabilityDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringToSet[0] = opToSeq(ConnectBroker.opString, "availability");
                printSeqReq.setText(stringToSet[0]);
            }
        });
        securityDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringToSet[0] = opToSeq(ConnectBroker.opString, "security");
                printSeqReq.setText(stringToSet[0]);
            }
        });
        cusDes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringToSet[0] = opToSeq(ConnectBroker.opString, "security");
                printSeqReq.setText("No Example For User Defined Security Requirement");
            }
        });
        cusDes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringToSet[0] = opToSeq(ConnectBroker.opString, "security");
                printSeqReq.setText("No Example For User Defined Security Requirement");
            }
        });








        //navigate to add sequrity requirement


        //Initialise DatabaseHelper
        DatabaseHelperSR databaseHelper1 = new DatabaseHelperSR(SeqReqToggle.this);

        ArrayList test;

        //Add Database Values to ArrayList
        test = databaseHelper1.getAllText();

        //determine whether or not to show dynamic security requirements
        if(test.size()>0) {
            String seqReqCus1 = (String) test.get(0);
            cusDes1.setVisibility(View.VISIBLE);
            cusSwitch1.setVisibility(View.VISIBLE);
            cusSwitch1.setText(seqReqCus1);

        }
        if(test.size()>1) {
            String seqReqCus2 = (String) test.get(1);
            cusDes2.setVisibility(View.VISIBLE);
            cusSwitch2.setVisibility(View.VISIBLE);
            cusSwitch2.setText(seqReqCus2);

        }
        if(test.size()==0) {
            cusDes1.setVisibility(View.INVISIBLE);
            cusSwitch1.setVisibility(View.INVISIBLE);
            cusDes2.setVisibility(View.INVISIBLE);
            cusSwitch2.setVisibility(View.INVISIBLE);

        }



        //button to return to dashboard
        Button to_op = (Button) findViewById(R.id.toggle_confirm);
        to_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked button");

                Intent intent = new Intent(SeqReqToggle.this, DashBoard.class);
                startActivity(intent);

                //set security requirment booleans based off their toggles
                if (secSwitch.isChecked()) {
                    secForFSM = true;
                    ConnectBroker.checkedSec=true;
                } else {
                    secForFSM = false;
                    ConnectBroker.checkedSec=false;
                }

                if (avaSwitch.isChecked()) {
                    avaForFSM = true;
                    ConnectBroker.checkedAva=true;
                } else {
                    avaForFSM = false;
                    ConnectBroker.checkedAva=false;
                }

                if (intSwitch.isChecked()) {
                    intForFSM = true;
                    ConnectBroker.checkedInt=true;
                } else {
                    intForFSM = false;
                    ConnectBroker.checkedInt=false;
                }


                if (cusSwitch1.isChecked()) {
                    cs1ForFSM = true;
                    ConnectBroker.cusSec1=true;
                } else {
                    cs1ForFSM = false;
                    ConnectBroker.cusSec1=false;

                }

                if (cusSwitch2.isChecked()) {
                    cs2ForFSM = true;
                    ConnectBroker.cusSec2=true;
                } else {
                    cs2ForFSM = false;
                    ConnectBroker.cusSec2=false;

                }


            }



        });



    }

    public String opToSeq(String opString, String type) {
        String securityRequirement = "";

       String[] arrSplit = opString.split("\\;",0);


        for (int i=0;i<arrSplit.length;i++) {
            String opStringArray = arrSplit[i];

            securityRequirement = recogKeywords(opStringArray,type);

            if(securityRequirement.isEmpty()){
                securityRequirement = "No seqReq to show";
            } else {
                return securityRequirement;
            }


       }
        return securityRequirement;
    }
    //Method to recognise keywords wihin each String in order to produce sequrity requirements
    public String recogKeywords(String opStringArray, String type) {


        String[] opStringWordArray = opStringArray.split(" ", 0);
        String phrase1 = "";
        String phrase2 = "";
        String phrase3 = "";
        String phrase4 = "";
        String phrase5 = "";
        String phrase6 = "";
        String phrase7 = "";
        //boolean keyword flags
        boolean alert = false;
        boolean halt = false;
        boolean unfit = false;
        boolean ready = false;
        boolean communicate = false;
        boolean rate = false;
        boolean dosage = false;
        boolean perform = false;
        boolean IDS = false;
        boolean pressed = false;
        boolean less = false;
        boolean security = false;
        boolean availability = false;
        boolean integrity = false;
        boolean kill = false;

        String securityString = "The PCA must";
        //this for loop checks each word in the operational profile sentence for keywords and checks appropriate booleans to true
        for (int i = 0; i < opStringWordArray.length; i++) {
            String thisWord = opStringWordArray[i].toString().toLowerCase();
            if (thisWord.equals("alert")) {
                alert = true;
            }
            if (thisWord.equals("halt")) {
                halt = true;
            }
            if (thisWord.equals("unfit")) {
                unfit = true;
            }
            if (thisWord.equals("ready")) {
                ready = true;
            }
            if (thisWord.equals("communicate")) {
                communicate = true;
            }
            if (thisWord.equals("rate")) {
                rate = true;
            }
            if (thisWord.equals("dosage")) {
                dosage = true;
            }
            if (thisWord.equals("perform")) {
                perform = true;
            }
            if (thisWord.equals("ids")) {
                IDS = true;
            }
            if (thisWord.equals("less")) {
                less = true;
            }
            if (thisWord.equals("pressed") || thisWord.equals("pressed,")) {
                pressed = true;
            }
            if (type.equals("security")) {
                security = true;
            }
            if (type.equals("availability")) {
                availability = true;
            }
            if (type.equals("integrity")) {
                integrity = true;
            }

        }
        //set phrases if appropriate booleans are true
        if (alert == true & halt == true & unfit == true & kill) {
            phrase1 = (" raise an alert to designated personnel and halt analgesic injection if the patient's medical condition is unfit for analgesic injection");
        }
        if (alert == true & halt == true & ready == true & kill) {
            phrase1 = (" raise an alert to designated personnel and halt analgesic injection if the PCA is not ready for analgesic injection");
        }
        if (communicate == true & rate == true & dosage == true & security == true) {
            phrase1 = (" only communicate with authorized personnel regarding the injection rate and dosage of medicine");
        }
        if (perform == true & IDS == true & integrity == true) {
            phrase1 = (" perform correct IDS functions");
        }
        if (pressed == true & rate == true & dosage == true & less == true & availability == true) {
            phrase1 = (" inject a specified dosage of medicine when the injection button is pressed, if the patient controlled injection rate is less than or equal to the specified injection rate");
        }
        if (phrase1.isEmpty()) {
            return phrase1;
        }else{
        securityString = (String) concat(securityString, phrase1, phrase2, phrase3, phrase4, phrase5, phrase6, phrase7);
        }
        return securityString;


    }
}
