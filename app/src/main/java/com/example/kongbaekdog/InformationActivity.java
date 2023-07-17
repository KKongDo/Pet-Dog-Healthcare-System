package com.example.kongbaekdog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InformationActivity extends AppCompatActivity {

    private Button okayB;
    private String name="", weight="",birth="",sex="",dogtype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_information);
        Intent BCSIntent= getIntent();

        name=BCSIntent.getStringExtra("DogName");
        weight=BCSIntent.getStringExtra("DogWeight");
        birth=BCSIntent.getStringExtra("DogBirth");
        sex=BCSIntent.getStringExtra("DogSex");
        dogtype=BCSIntent.getStringExtra("DogType");

        okayB = findViewById(R.id.okayB);
        okayB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent oB = new Intent(InformationActivity.this,BcsActivity.class);
                oB.putExtra("DogName",name);
                oB.putExtra("DogWeight",weight);
                oB.putExtra("DogBirth",birth);
                oB.putExtra("DogSex",sex);
                oB.putExtra("DogType",dogtype);
                setResult(1559,oB);
                finish();
            }
        });
    }
}