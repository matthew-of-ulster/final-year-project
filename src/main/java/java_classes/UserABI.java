package java_classes;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class UserABI extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Initialize Variable
    LinearLayout visBox;
    EditText etText1;
    Button btClear;
    Button btConfirm;
    Button btAddABI;
    static ListView listView;
    String[] measure = {"No Measurement", "Pulse Rate", "Respiration Rate", "Dosage Amount", "Injection Rate", "Drug Reservoir"};
    String[] action = {"Raise Alert", "Don't Raise Alert", "Accept Command", "Reject Command", "Accept Communications", "Don't Communicate"};
    String[] status = {"No Status", "Defibrillated","Not Defibrillated", "Authorised", "Unauthorised", "Healthy", "Unhealthy"};
    String[] comp = {"No Comparator","Less Than","Greater Than","Equals","Unequal"};
    Spinner spMeasure;
    Spinner spAction;
    Spinner spComp;
    Spinner spStatus;
    CheckBox SR1;
    CheckBox SR2;
    CheckBox SR3;
    CheckBox SR4;
    CheckBox SR5;

    static DatabaseHelper databaseHelper;
    static ArrayList arrayList;
    static ArrayAdapter arrayAdapter;

    boolean compAndTextVis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_abi);

        //Assign Variable
        etText1 = findViewById(R.id.et_text1);
        btClear = findViewById(R.id.bt_clear1);
        btConfirm = findViewById(R.id.bt_confirm1);
        btAddABI = findViewById(R.id.bt_showSpinners);
        listView = findViewById(R.id.list_view1);
        spMeasure = findViewById(R.id.measureSpinner);
        spAction = findViewById(R.id.actionSpinner);
        spStatus = findViewById(R.id.statusSpinner);
        spComp = findViewById(R.id.compSpinner);
        visBox = findViewById(R.id.compAndText);
        spMeasure.setOnItemSelectedListener(this);
        SR1 = findViewById(R.id.SR1cb);
        SR2 = findViewById(R.id.SR2cb);
        SR3 = findViewById(R.id.SR3cb);
        SR4 = findViewById(R.id.SR4cb);
        SR5 = findViewById(R.id.SR5cb);

        compAndTextVis = false;
        //Set up spinner

        final ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,measure);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMeasure.setAdapter(aa);


        spAction.setOnItemSelectedListener(this);
        ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,action);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAction.setAdapter(aa1);

        spStatus.setOnItemSelectedListener(this);
        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,status);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(aa2);

        spComp.setOnItemSelectedListener(this);
        ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,comp);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComp.setAdapter(aa3);


        DatabaseHelperSR databaseHelper1 = new DatabaseHelperSR(UserABI.this);

        ArrayList test;

        test = databaseHelper1.getAllText();

        //show dynamic security requirements
        if(test.size()>0) {
            String seqReqCus1 = (String) test.get(0);
            SR4.setVisibility(View.VISIBLE);
            SR4.setText(seqReqCus1);
        }
        if(test.size()>1) {
            String seqReqCus2 = (String) test.get(1);
            SR5.setVisibility(View.VISIBLE);
            SR5.setText(seqReqCus2);
        }
        if(test.size()==0) {
            SR4.setVisibility(View.INVISIBLE);
            SR5.setVisibility(View.INVISIBLE);
        }






        //Initialise DatabaseHelper
        databaseHelper = new DatabaseHelper(UserABI.this);

        //Add Database Values to ArrayList
        arrayList = databaseHelper.getAllText();

        //Initialise ArrayAdapter
        arrayAdapter = new ArrayAdapter(UserABI.this, android.R.layout.simple_list_item_1,arrayList);

        //Set ArraAdapter to ListView
        listView.setAdapter(arrayAdapter);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.clearTable();

                Toast.makeText(getApplicationContext(), "User ABIs Cleared", Toast.LENGTH_SHORT).show();

            }
        });


        //button to return user to dashboard
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserABI.this, DashBoard.class);
                startActivity(intent);
            }
        });

        //button to add user ABI
        btAddABI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ABI tempABI = new ABI();
                //set ABI variables using recognition methods
                tempABI = checkStatus(tempABI);
                tempABI = checkMeasure(tempABI);
                tempABI = checkComp(tempABI);
                tempABI = checkAction(tempABI);

                boolean isInt = true;
                String testInt = etText1.getText().toString();
                char[] testChar = testInt.toCharArray();
                for(char i: testChar){
                    if(!Character.isDigit(i)){
                        isInt = false;
                    }
                }
                if (!etText1.getText().toString().equals("") && isInt) {
                    tempABI.amount = Integer.parseInt(etText1.getText().toString());
                }

                //Set the security requirement for ABI based ogg security requirement check boxes
                tempABI.SR1 = SR1.isChecked();
                tempABI.SR2 = SR2.isChecked();
                tempABI.SR3 = SR3.isChecked();
                tempABI.SR4 = SR4.isChecked();
                tempABI.SR5 = SR5.isChecked();

                //Make ABI to String method
                String printString = ABIVerificationMethods.add(tempABI);

                Toast.makeText(UserABI.this, printString,
                        Toast.LENGTH_SHORT).show();
            }
        });


    }
    //Set ABI action variable
    public ABI checkAction(ABI tempABI) {
        String actionString = String.valueOf(spAction.getSelectedItem());
        switch(actionString){
            case "Raise Alert":
                tempABI.action= Action.alert;
                break;
            case "Don't Raise Alert":
                tempABI.action= Action.nalert;
                break;
            case "Accept Command":
                tempABI.action= Action.accept;
                break;
            case "Reject Command":
                tempABI.action= Action.reject;
                break;
            case "Accept Communications":
                tempABI.action= Action.communicate;
                break;
            case "Don't Communicate":
                tempABI.action= Action.ncommunicate;
                break;
        }
         return tempABI;
    }
    public ABI checkMeasure(ABI tempABI) {
        String measureString = String.valueOf(spMeasure.getSelectedItem());

        if(measureString.equals("No Measurement")) {
            compAndTextVis = false;
        }else{
                compAndTextVis = true;

            visBox.setVisibility(View.VISIBLE);
            }

        switch(measureString){
            case "No Measurement":
                tempABI.var1= Measurement.NA;

                break;
            case "Pulse Rate":
                tempABI.var1= Measurement.PR;
                break;
            case "Respiration Rate":
                tempABI.var1= Measurement.RR;
                break;
            case "Dosage Amount":
                tempABI.var1= Measurement.DA;
                break;
            case "Injection Rate":
                tempABI.var1= Measurement.IR;
                break;
            case "Drug Reservoir":
                tempABI.var1= Measurement.DR;
                break;
        }
        return tempABI;
    }
    public ABI checkStatus(ABI tempABI) {
        String statusString = String.valueOf(spStatus.getSelectedItem());
        switch(statusString){
            case "No Status":
                tempABI.status= Status.na;
                break;
            case "Defibrillated":
                tempABI.status= Status.def;
                break;
            case "Not Defibrillated":
                tempABI.status= Status.ndef;
                break;
            case "Authorised":
                tempABI.status= Status.auth;
                break;
            case "Unauthorised":
                tempABI.status= Status.nauth;
                break;
            case "Healthy":
                tempABI.status= Status.healthy;
                break;
            case "Unhealthy":
                tempABI.status= Status.nhealthy;
                break;

        }
        return tempABI;
    }
    public ABI checkComp(ABI tempABI) {
        String compString = String.valueOf(spComp.getSelectedItem());
        switch(compString){
            case "No Comparator":
                tempABI.comp= Comparator.na;
                break;
            case "Less Than":
                tempABI.comp= Comparator.less_than;
                break;
            case "Greater Than":
                tempABI.comp= Comparator.greater_than;
                break;
            case "Equals":
                tempABI.comp= Comparator.equals;
                break;
            case "Unequal":
                tempABI.comp= Comparator.nequal;
                break;
        }
        return tempABI;
    }

    @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}