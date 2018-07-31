package com.example.a16022970.p11_knowyournationalday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoAccessCode extends AppCompatActivity {
    Button btnTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_access_code);
        btnTry = (Button) findViewById(R.id.btnTry);


        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NoAccessCode.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}
