package com.example.kongbaekdog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //프레그멘트 선언
    MainHome mainHome;
    HeartBeat HeartBeat;
    PositionMap PositionMap;
    String name="", weight="", birth="",sex="",Dogsex="",Dogtype="";
    int BCS;
    ETC ETC;
    private long lastTimeBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent MainActIntent = getIntent();

        name=MainActIntent.getStringExtra("DogName");
        weight=MainActIntent.getStringExtra("DogWeight");
        birth=MainActIntent.getStringExtra("DogBirth");
        sex=MainActIntent.getStringExtra("DogSex");
        BCS=MainActIntent.getIntExtra("BCS",0);
        Dogtype=MainActIntent.getStringExtra("DogType");

        Dogsex=sex.substring(3,4);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainHome=new MainHome();
        HeartBeat=new HeartBeat();
        PositionMap=new PositionMap();
        ETC=new ETC();

        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();

        Bundle bundle=new Bundle();
        bundle.putString("DogName",name);
        bundle.putString("DogType",Dogtype);
        bundle.putString("DogBirth",birth);
        bundle.putString("DogSex",sex);
        bundle.putString("DogWeight",weight);
        bundle.putInt("BCS",BCS);

        mainHome=new MainHome();
        mainHome.setArguments(bundle);

        transaction.replace(R.id.container,mainHome).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,mainHome).commit();
                        return true;
                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,HeartBeat).commit();
                        return true;
                    case R.id.tab3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,PositionMap).commit();
                        return true;
                    case R.id.tab4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,ETC).commit();
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    //뒤로가기 버튼을 눌렀을 때 프로필설정화면과 BCS설정화면으로 돌아가지않게 하는 코드 AND 뒤로가기 버튼 두번 눌렀을 시 앱 완전 종료 버튼
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "연속으로 누를 시 종료됩니다.", Toast.LENGTH_SHORT).show();
        if(System.currentTimeMillis()-lastTimeBackPressed < 1000){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
        lastTimeBackPressed=System.currentTimeMillis();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.doginf :
                showMessage();
                break;
        }
        return true;

    }

    private void showMessage(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("나의 반려견 정보");
        builder.setMessage("이름 : "+name+"\n품종 : "+Dogtype+"\n생년월일 : "+birth+"\n성별(수컷:♂/암컷:♀) : "+Dogsex+"\nBCS : "+BCS+"단계"); // 반려견 이름, BCS, 생년월일, 성별

        /*반려견 정보 이미지 사진 출처*/
        /*<a href="https://www.flaticon.com/free-icons/dog" title="dog icons">Dog icons created by Freepik - Flaticon</a>*/

        builder.setNegativeButton("로그아웃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"로그아웃 하였습니다.",Toast.LENGTH_SHORT).show();
                Intent Logout = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(Logout);
            }
        });

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"반려견 정보를 확인하셨습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

}
