package java_classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.ToggleButton;


import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class ConnectBroker extends AppCompatActivity {

    public static boolean hasOP;
    public static String opString = "default";
    public static MqttAndroidClient client;
    private String TAG = "ConnectBroker";
    private String passwordString = "a1";
    private String testtest = "mullan";
    public static PahoMqttClient mqttClient;
    private String clientid = "";
    private Timer myTimer;
    public String brokerURL = "tcp://192.168.0.26";
    public static boolean checkedInt = true;
    public static boolean checkedAva = true;
    public static boolean checkedSec = true;
    public static boolean cusSec1 = true;
    public static boolean cusSec2 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView ip = (TextView)findViewById(R.id.ipAddress);

        //Button to subscribe to a topic
        final Button subButton = (Button) findViewById(R.id.subBut);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: subBut clicked button");
                String msg_new="";
                if (!mqttClient.mqttAndroidClient.isConnected()) {
                }
                //set the topic
                String topic = "PCA signal";
                if (!topic.isEmpty()) {
                    try {
                        //subscribe
                        mqttClient.subscribe(client, topic, 1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        final Button backToDash = (Button) findViewById(R.id.conBut);
        ToggleButton  showIP = (ToggleButton) findViewById(R.id.ShowIP);
        final Button confirm = (Button) findViewById(R.id.confirm);
        final EditText password = (EditText) findViewById(R.id.editTextTextPassword) ;
        final CardView card = (CardView) findViewById(R.id.passwordCard);

        //Button to show visibility of the IP address
        showIP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.VISIBLE);
                    card.setVisibility(View.VISIBLE);
                } else {
                    password.setVisibility(View.INVISIBLE);
                    confirm.setVisibility(View.INVISIBLE);
                    card.setVisibility(View.INVISIBLE);
                }
            }
        });

        //button to check if user has entered the correct password
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String test =password.getText().toString();
                if (test.equals(passwordString)) {
                    ip.setText(brokerURL);
                }else if(test.equals(testtest)){
                    Intent intent = new Intent(ConnectBroker.this, List.class);
                    startActivity(intent);
                } else {
                    ip.setText("");
                }
            }
        });

        //Button to return user to dashboard
        backToDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConnectBroker.this, DashBoard.class);
                startActivity(intent);
            }
        });


        TextView tvMessage = (TextView) findViewById(R.id.subscribedMsg);
        tvMessage.setMovementMethod(new ScrollingMovementMethod());

        Random r = new Random();
        int i1 = r.nextInt(5000 - 1) + 1;
        clientid = "mqtt" + i1;

        String urlBroker    = brokerURL;

        //create a new mqtt client
        mqttClient = new PahoMqttClient();
        client = mqttClient.getMqttClient(  getApplicationContext(),
                                                urlBroker,
                                                clientid,
                                                "",
                                                ""
                                             );
        mqttCallback();

        //Create Timer to report MQTT connection Status every 1 second
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ScheduleTasks();
            }

        }, 0, 1000);


    }




    private void ScheduleTasks()
    {

        this.runOnUiThread(RunScheduledTasks);
    }


    protected void mqttCallback() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                TextView tvMessage = (TextView) findViewById(R.id.subscribedMsg);

                    String msg = "topic: " + topic + "\r\nMessage: " + message.toString() + "\r\n";
                    tvMessage.append( msg);

                    FSM.catchIt = message.toString();
                    opString = message.toString();
                    if(opString.split(";")[0].equals("OP")) hasOP = true;

                }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    private Runnable RunScheduledTasks = new Runnable() {
        public void run() {
            //Check Connection Status
            TextView tvMessage  = (TextView) findViewById(R.id.cnxStatus);
            String msg_new="";

            if(mqttClient.mqttAndroidClient.isConnected() ) {
                msg_new = "Connected\r\n";
                tvMessage.setTextColor(0xFF00FF00); //Green if connected
                tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
            else {
                msg_new = "Disconnected\r\n";
                tvMessage.setTextColor(0xFFFF0000); //Red if not connected
                tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
            tvMessage.setText(msg_new);
        }
    };

}
