package java_classes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;


public class List extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);


        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.linearBackground);
        mLinearLayout.setBackgroundResource(R.drawable.mo);

    }}
