package com.example.kongbaekdog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BcsActivity extends AppCompatActivity {

    private Button BackB;
    private Button SaveB;
    private ImageView BcsH;
    public int BcsValue;
    private int year;
    private RadioButton B1,B2,B3,B4,B5;
    String name,birth;
    public String dogweight;
    public String dogtype;
    String sex;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1559){
            if (resultCode==RESULT_OK){
                name=data.getStringExtra("DogName");
                dogweight=data.getStringExtra("DogWeight");
                birth=data.getStringExtra("DogBirth");
                sex=data.getStringExtra("DogSex");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcs);

        //데이터베이스 추가
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DogB  = database.getReference("BCS");

        BackB = findViewById(R.id.backB);
        SaveB = findViewById(R.id.saveB);
        BcsH = findViewById(R.id.bcsH);

        B1 = findViewById(R.id.bcs1);
        B2 = findViewById(R.id.bcs2);
        B3 = findViewById(R.id.bcs3);
        B4 = findViewById(R.id.bcs4);
        B5 = findViewById(R.id.bcs5);

        Intent secondIntent = getIntent();

        name=secondIntent.getStringExtra("이름");
        dogweight=secondIntent.getStringExtra("체중");
        birth=secondIntent.getStringExtra("생년월일");
        sex=secondIntent.getStringExtra("성별");
        year=secondIntent.getIntExtra("생년",0);
        dogtype=secondIntent.getStringExtra("타입선택");
        //견종도 같이 넘기기.

        BackB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(BcsActivity.this, ProfileActivity.class);
                startActivity(b);
            }
        });


        SaveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sb = new Intent(BcsActivity.this, MainActivity.class);
                sb.putExtra("DogName",name);
                sb.putExtra("DogWeight",dogweight);
                sb.putExtra("DogBirth",birth);
                sb.putExtra("DogSex",sex);
                sb.putExtra("BCS",BcsValue);
                DogB.setValue(BcsValue); //데이터베이스 추가
                sb.putExtra("DogType",dogtype);
                //견종 부분도 같이 넘기기

                startActivity(sb);
            }
        });

        BcsH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent h = new Intent(BcsActivity.this,InformationActivity.class);
                h.putExtra("DogName",name);
                h.putExtra("DogWeight",dogweight);
                h.putExtra("DogBirth",birth);
                h.putExtra("DogSex",sex);
                h.putExtra("DogType",dogtype);
                startActivity(h);
            }
        });

    }


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.bcs1:
                if (checked)
                    B1.setChecked(true);
                    BcsValue=1;
                    B2.setChecked(false);
                    B3.setChecked(false);
                    B4.setChecked(false);
                    B5.setChecked(false);
                    break;

            case R.id.bcs2:
                if (checked)
                    B1.setChecked(false);
                    B2.setChecked(true);
                    BcsValue=3;
                    B3.setChecked(false);
                    B4.setChecked(false);
                    B5.setChecked(false);
                    break;

            case R.id.bcs3:
                if (checked)
                    B1.setChecked(false);
                    B2.setChecked(false);
                    B3.setChecked(true);
                    BcsValue=5;
                    B4.setChecked(false);
                    B5.setChecked(false);
                    break;

            case R.id.bcs4:
                if (checked)
                    B1.setChecked(false);
                    B2.setChecked(false);
                    B3.setChecked(false);
                    B4.setChecked(true);
                    BcsValue=7;
                    B5.setChecked(false);
                    break;

            case R.id.bcs5:
                if (checked)
                    B1.setChecked(false);
                    B2.setChecked(false);
                    B3.setChecked(false);
                    B4.setChecked(false);
                    B5.setChecked(true);
                    BcsValue=9;
                    break;
        }
    }
}