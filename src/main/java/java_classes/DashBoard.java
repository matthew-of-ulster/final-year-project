package java_classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import static java_classes.ConnectBroker.mqttClient;

public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        //Button to open broker
        ImageButton broker = (ImageButton) findViewById(R.id.brokerButton);
        broker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, ConnectBroker.class);
                startActivity(intent);
            }
        });

        //Button to open page of behaviour rules
        ImageButton ruleButton = (ImageButton) findViewById(R.id.ruleTableButton);
        ruleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, BehaviourRuleTable.class);
                startActivity(intent);
            }
        });

        //Button to open state machine
        ImageButton FSMButton = (ImageButton) findViewById(R.id.FSMButton);
        FSMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
try {
    if (mqttClient.mqttAndroidClient.isConnected()) {
        //Button to open state machine
        Intent intent = new Intent(DashBoard.this, FSM.class);
        startActivity(intent);
    }else
    {
        Toast.makeText(DashBoard.this, "Not Connected To Broker",
                Toast.LENGTH_SHORT).show();
    }
}catch(NullPointerException e){
    Toast.makeText(DashBoard.this, "You Must First Connect To The Broker",
            Toast.LENGTH_SHORT).show();
}
            }
        });

        //Button to open security requirements
        ImageButton addSRButton = (ImageButton) findViewById(R.id.addSRBUtton);
        addSRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, AddSeqReq.class);
                startActivity(intent);
            }
        });


        //Button to open security requirement toggles
        ImageButton srToggleButton = (ImageButton) findViewById(R.id.srToggleButton);
        srToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, SeqReqToggle.class);
                startActivity(intent);
            }
        });


        //Button to open page to add dynamic ABI
        ImageButton userABIButton = (ImageButton) findViewById(R.id.userABIButton);
        userABIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoard.this, UserABI.class);
                startActivity(intent);
            }
        });
    }
}
