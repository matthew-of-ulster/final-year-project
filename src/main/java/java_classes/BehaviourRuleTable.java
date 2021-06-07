package java_classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

public class BehaviourRuleTable extends AppCompatActivity {

    private static final String TAG = "op_profile";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.behaviour_rule_list);

        //Initialise DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(BehaviourRuleTable.this);

        ArrayList test;

        //Create all behavior rules
        BehRule one = new BehRule("If patient is unfit raise alert to designated personnel",true,true,true);
        BehRule two = new BehRule("If patient is unfit hold analgesic injection",true,true,true);
        BehRule three = new BehRule("If PCA is not ready raise alert to designated personnel",true,true,true);
        BehRule four = new BehRule("If PCA is not ready hold analgesic injection",true,true,true);
        BehRule five = new BehRule("Only accept authorised commands",true,true,true);

        BehRule six = new BehRule("Perform analgesic injection at the correct rate",true,false,false);
        BehRule seven = new BehRule("Perform analgesic injection at the correct dosage",true,false,false);
        BehRule eight = new BehRule("Communicate with authorized personnel only",false,false,true);
        BehRule nine = new BehRule("Run process of administering analgesia to patient when requested ",false,true,false);

        //Improve by adding all behRules to an array initially and for loop through it
        ArrayList<BehRule> beRules = new ArrayList<BehRule>();

        //seq req toggles, 1 is true
        if(SeqReqToggle.intForFSM && !SeqReqToggle.secForFSM && !SeqReqToggle.avaForFSM) {
            beRules.add(one);
            beRules.add(two);
            beRules.add(three);
            beRules.add(four);
            beRules.add(five);
            beRules.add(six);
            beRules.add(seven);
        }else if(SeqReqToggle.secForFSM && !SeqReqToggle.avaForFSM && !SeqReqToggle.intForFSM){
            beRules.add(one);
            beRules.add(two);
            beRules.add(three);
            beRules.add(four);
            beRules.add(five);
            beRules.add(eight);
        }else if (SeqReqToggle.avaForFSM && !SeqReqToggle.intForFSM && !SeqReqToggle.secForFSM){
            beRules.add(one);
            beRules.add(two);
            beRules.add(three);
            beRules.add(four);
            beRules.add(five);
            beRules.add(nine);
        //seq req toggles 2 true
        } else if(SeqReqToggle.intForFSM && SeqReqToggle.secForFSM && !SeqReqToggle.avaForFSM) {
            beRules.add(one);
            beRules.add(two);
            beRules.add(three);
            beRules.add(four);
            beRules.add(five);
            beRules.add(six);
            beRules.add(seven);
            beRules.add(eight);
        }else if(SeqReqToggle.secForFSM && SeqReqToggle.avaForFSM && !SeqReqToggle.intForFSM){
            beRules.add(one);
            beRules.add(two);
            beRules.add(three);
            beRules.add(four);
            beRules.add(five);
            beRules.add(eight);
            beRules.add(nine);
        }else if (SeqReqToggle.avaForFSM && SeqReqToggle.intForFSM && !SeqReqToggle.secForFSM){
            beRules.add(one);
            beRules.add(two);
            beRules.add(three);
            beRules.add(four);
            beRules.add(five);
            beRules.add(six);
            beRules.add(seven);
            beRules.add(nine);
            //all true
        } else if(SeqReqToggle.intForFSM && SeqReqToggle.secForFSM && SeqReqToggle.avaForFSM) {
            beRules.add(one);
            beRules.add(two);
            beRules.add(three);
            beRules.add(four);
            beRules.add(five);
            beRules.add(six);
            beRules.add(seven);
            beRules.add(eight);
            beRules.add(nine);
            //all false
        }else {

        }

        //add all BR textviews
        TextView br1 = (TextView) findViewById(R.id.br1);
        TextView br2 = (TextView) findViewById(R.id.br2);
        TextView br3 = (TextView) findViewById(R.id.br3);
        TextView br4 = (TextView) findViewById(R.id.br4);
        TextView br5 = (TextView) findViewById(R.id.br5);
        TextView br6 = (TextView) findViewById(R.id.br6);
        TextView br7 = (TextView) findViewById(R.id.br7);
        TextView br8 = (TextView) findViewById(R.id.br8);
        TextView br9 = (TextView) findViewById(R.id.br9);
        TextView br10 = (TextView) findViewById(R.id.br10);
        ArrayList<TextView> BRTV = new ArrayList<TextView>();
        BRTV.add(br1);
        BRTV.add(br2);
        BRTV.add(br3);
        BRTV.add(br4);
        BRTV.add(br5);
        BRTV.add(br6);
        BRTV.add(br7);
        BRTV.add(br8);
        BRTV.add(br9);
        BRTV.add(br10);

        //add all SR textview
        TextView sr1 = (TextView) findViewById(R.id.sr1);
        TextView sr2 = (TextView) findViewById(R.id.sr2);
        TextView sr3 = (TextView) findViewById(R.id.sr3);
        TextView sr4 = (TextView) findViewById(R.id.sr4);
        TextView sr5 = (TextView) findViewById(R.id.sr5);
        TextView sr6 = (TextView) findViewById(R.id.sr6);
        TextView sr7 = (TextView) findViewById(R.id.sr7);
        TextView sr8 = (TextView) findViewById(R.id.sr8);
        TextView sr9 = (TextView) findViewById(R.id.sr9);
        TextView sr10 = (TextView) findViewById(R.id.sr10);
        ArrayList<TextView> SRTV = new ArrayList<TextView>();
        SRTV.add(sr1);
        SRTV.add(sr2);
        SRTV.add(sr3);
        SRTV.add(sr4);
        SRTV.add(sr5);
        SRTV.add(sr6);
        SRTV.add(sr7);
        SRTV.add(sr8);
        SRTV.add(sr9);
        SRTV.add(sr10);


        for (int i=0; i<beRules.size();i++){
            String seqReqs ="";
            BRTV.get(i).setTextSize(11);
            SRTV.get(i).setTextSize(11);
            BRTV.get(i).setWidth(150);
            SRTV.get(i).setWidth(150);
            BRTV.get(i).setText(beRules.get(i).rule);
            BRTV.get(i).setVisibility(View.VISIBLE);
            SRTV.get(i).setVisibility(View.VISIBLE);
            if(beRules.get(i).hasAva){
                seqReqs += "Availability ";
            }
            if(beRules.get(i).hasInt){
                seqReqs += "Integrity ";
            }
            if(beRules.get(i).hasSec){
                seqReqs += "Security ";
            }
            SRTV.get(i).setText(seqReqs);
        }
        //Add Database Values to ArrayList
        test = databaseHelper.getAllText();


      Button secondButton = (Button) findViewById(R.id.rtnStart);

        //Button to return user to dashboard
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked button");

                Intent intent = new Intent(BehaviourRuleTable.this, DashBoard.class);
                startActivity(intent);
            }
        });
}


}



