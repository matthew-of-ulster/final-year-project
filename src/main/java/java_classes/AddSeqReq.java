package java_classes;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

public class AddSeqReq extends AppCompatActivity {

    EditText etText;
    Button btAdd;
    Button btClear;
    Button btConfirm;
    ListView listView;

    DatabaseHelperSR databaseHelper1;
    static ArrayList arrayList1;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_seq_req);

        //Assign Variable
        etText = findViewById(R.id.et_text);
        btAdd = findViewById(R.id.bt_add);
        btClear = findViewById(R.id.bt_clear);
        btConfirm = findViewById(R.id.bt_confirm);
        listView = findViewById(R.id.list_view);

        databaseHelper1 = new DatabaseHelperSR(AddSeqReq.this);

        arrayList1 = databaseHelper1.getAllText();

        arrayAdapter = new ArrayAdapter(AddSeqReq.this, android.R.layout.simple_list_item_1,arrayList1);

        listView.setAdapter(arrayAdapter);

        //Button to add a security requirement to the SR database
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get text from EditText
                String text = etText.getText().toString();
                if(!text.isEmpty()){
                    String response = text;
                    etText.setText("");
                    Toast.makeText(getApplicationContext(), "Security Requirement Added: "+response, Toast.LENGTH_SHORT).show();

                        if (databaseHelper1.addText(text)) {
                            arrayList1.clear();
                            arrayList1.addAll(databaseHelper1.getAllText());
                            arrayAdapter.notifyDataSetChanged();
                            listView.invalidateViews();
                            listView.refreshDrawableState();
                        }

                }
            }
        });

        //Button to remove all dynamic security requirements from the SR database
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper1.clearTable();
                showToast("User Defined Security Requirements Removed");
            }
        });


        //Button to return user to dashboard
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSeqReq.this, DashBoard.class);
                startActivity(intent);
            }
        });


    }
    private void showToast(String text){
        Toast.makeText(AddSeqReq.this, text, Toast.LENGTH_SHORT).show();
    }
}