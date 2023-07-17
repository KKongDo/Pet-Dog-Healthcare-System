package com.example.kongbaekdog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    private Button NextB;
    private Spinner DogType;
    private TextView DBirth;
    private EditText DogName, Dweight;
    private String date;
    private RadioGroup DogSexRadiogroup;
    private String Dogsex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        DogName = findViewById(R.id.DogName);
        Dweight = findViewById(R.id.Dweight);

        NextB = findViewById(R.id.Nextb);
        DogType = findViewById(R.id.DogType);
        DBirth = findViewById(R.id.DBirth);
        DogSexRadiogroup=findViewById(R.id.Dogsex);

        //외부데이터베이스
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DogN  = database.getReference("이름");
        DatabaseReference DogW  = database.getReference("체중");
        DatabaseReference DogS  = database.getReference("성별");
        DatabaseReference DogB  = database.getReference("생년월일");
        DatabaseReference DogT  = database.getReference("견종");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        date = year+"/"+month+"/"+day;
                        DBirth.setText(date);
                    }
                },year, month, day);
                dialog.show();
            }
        });


        DogSexRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
                switch (checkID){
                    case R.id.male:
                        Dogsex="수컷(♂)";
                        break;
                    case R.id.female:
                        Dogsex="암컷(♀)";
                        break;
                    default:
                        Dogsex="None(중성)";
                        break;
                }
            }
        });

        DogType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        NextB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(ProfileActivity.this, BcsActivity.class);
                String DN = DogName.getText().toString();
                String DW = Dweight.getText().toString();

                //외부데이터베이스에 해당 값 삽입
                DogN.setValue(DN);
                DogW.setValue(DW);
                DogS.setValue(Dogsex);
                DogB.setValue(date);
                DogT.setValue(DogType.getSelectedItem().toString());

                n.putExtra("이름", DN);
                n.putExtra("체중", DW);
                n.putExtra("생년월일",date);
                n.putExtra("성별",Dogsex);
                n.putExtra("타입선택", DogType.getSelectedItem().toString());

                startActivity(n);
            }
        });

    }

}