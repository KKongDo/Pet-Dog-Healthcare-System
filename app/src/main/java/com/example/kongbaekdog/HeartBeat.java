package com.example.kongbaekdog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


//심박수 화면 프래그먼트
public class HeartBeat extends Fragment {

    int change;
    private LineChart lineChart;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList<Entry> heartbeat_chart;
    TextView AvgHTtextView;
    TextView befday;
    static String tmp;
    int HBnum,beforeHBnum;
    static int BeforeHB1,BeforeHB2,BeforeHB3,BeforeHB4;

    static public String array[];
    private BluetoothSPP button;

    //심장 박동수 측정을 위한 타이머 클래스
    class HMTimer extends CountDownTimer {
        // 첫번째 인자는 타이머를 동작하는 총 시간 (밀리세컨드 단위), 두번째 인자는 카운트 다운되는 시간
        public HMTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            Toast.makeText(getContext(), "심박수 측정이 완료되었습니다. '그래프' 버튼을 누르세요.", Toast.LENGTH_SHORT).show();
            button.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
                public void onDataReceived(byte[] data, String message) {
                    array = message.split(",");
                }
            });
            dialog.dismiss(); // 10초동안 심박수를 측정하고 다이어로그가 없어지게 하는 메소드.
        }
    }

    ProgressDialog dialog;

    HMTimer hmTimer;

    // 현재 일수부터 5일전까지에 심박수 값을 나타내기 위한 날짜 설정
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
    Calendar rightnowtime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_heart_beat, container, false);
        AvgHTtextView=rootView.findViewById(R.id.textView4);
        befday=rootView.findViewById(R.id.textView15);
        lineChart = rootView.findViewById(R.id.heartbeat_chart); //layout의 id
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);

        //데이터베이스 추가
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference DogH  = database.getReference("심박수");
        DatabaseReference HB1  = database.getReference("HB1");
        DatabaseReference HB2  = database.getReference("HB2");
        DatabaseReference HB3  = database.getReference("HB3");
        DatabaseReference HB4  = database.getReference("HB4");
        DatabaseReference HB5  = database.getReference("HB5");

        HB1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BeforeHB1=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        HB2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BeforeHB2=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        HB3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BeforeHB3=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        HB4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BeforeHB4=snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(change==2){
            ChartUpdate();
        }

        button = new BluetoothSPP(getContext()); //초기화

        if (!button.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getContext(), "블루투스를 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        button.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getContext(), "연결완료 : " + name + "\n" + "'측정시작' 버튼을 누르세요.", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getContext(), "일별 평균 심박수 그래프 시각화 완료", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getContext(), "연결실패 : ", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = rootView.findViewById(R.id.button9); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (button.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    button.disconnect();
                } else {
                    Intent intent = new Intent(getContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        lineChart.setNoDataText("♡반려견의 심박수를 측정해주세요♡");
        hmTimer = new HMTimer(10000, 1000);

        //심박수 측정 버튼을 눌렀을시 심박수 측정 시작 & Toast메세지 출력
        Button measurebutton=rootView.findViewById(R.id.button10);
        measurebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "10초동안 심박수를 측정합니다. 움직이지 마세요.", Toast.LENGTH_SHORT).show();
                dialog = new ProgressDialog(rootView.getContext());
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("심박수 측정중입니다.");
                dialog.show(); // 다이어 그래프 보여줌
                hmTimer.start();
            }
        });

        //그래프 그리기 버튼 눌렀을 시 그래프 그리기 시작
        Button startbutton = rootView.findViewById(R.id.button);
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmp=array[0];
                HBnum=Integer.parseInt(tmp);
                HB5.setValue(HBnum);
                change=2;
                ChartUpdate();
                onDestroy();
            }
        });

        beforeHBnum=66;
        return rootView;
    }

    public void onDestroy() {
        super.onDestroy();
        button.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!button.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!button.isServiceAvailable()) {
                button.setupService();
                button.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    public void setup() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                button.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                button.setupService();
                button.startService(BluetoothState.DEVICE_OTHER);
                setup();

            } else {
                Toast.makeText(getContext(), "블루투스를 이용할 수 없었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void ChartUpdate(){

        heartbeat_chart= new ArrayList<>(); // 그래프에서 표현하고자 하는 데이터

        //데이터베이스에서 값을 가져와 그래프로 보여주는 코드
        rightnowtime= Calendar.getInstance();
        rightnowtime.add(Calendar.DATE,-4);
        heartbeat_chart.add(new Entry(0, BeforeHB1));
        xVals.add(simpleDateFormat.format(rightnowtime.getTime()));

        rightnowtime= Calendar.getInstance();
        rightnowtime.add(Calendar.DATE,-3);
        heartbeat_chart.add(new Entry(1, BeforeHB2));
        xVals.add(simpleDateFormat.format(rightnowtime.getTime()));

        rightnowtime= Calendar.getInstance();
        rightnowtime.add(Calendar.DATE,-2);
        heartbeat_chart.add(new Entry(2, BeforeHB3));
        xVals.add(simpleDateFormat.format(rightnowtime.getTime()));

        rightnowtime= Calendar.getInstance();
        rightnowtime.add(Calendar.DATE,-1);
        heartbeat_chart.add(new Entry(3, BeforeHB4));
        xVals.add(simpleDateFormat.format(rightnowtime.getTime()));

        rightnowtime= Calendar.getInstance();
        rightnowtime.add(Calendar.DATE,0);
        heartbeat_chart.add(new Entry(4, HBnum)); // 금일 심박수 데이터 값
        xVals.add(simpleDateFormat.format(rightnowtime.getTime()));

        int i=4;

        if(i==4){
            AvgHTtextView.setText(" "+Integer.toString(HBnum)+" BPM");

            int differhtmp=(int)(Math.round((HBnum-BeforeHB4)*100)/100.0); // 전날 대비 심박수 정보제공 메소드

            // 전날 대비 심박수 값에 따라 양수면 빨강, 음수면 파랑색을 표시하며 수치값도 나타내는 코드
            if(differhtmp>0){
                befday.setText(" "+Integer.toString(differhtmp)+" BPM▲");
                befday.setTextColor(0xFFFF7979);
            }

            else if(differhtmp<0){
                befday.setText(" "+Integer.toString(differhtmp).substring(1)+" BPM▼");
                befday.setTextColor(0xFF68C4F1);
            }
            else{
                befday.setText(" "+Integer.toString(differhtmp)+" BPM");
                befday.setTextColor(0xFF000000);
            }
        }


        LineDataSet heartData = new LineDataSet(heartbeat_chart, "심박수"); // 선의 색깔, 포인트 지점의 색깔 등을 설정하는 클래스
        heartData.setLineWidth(3); //선(라인)의 두께
        heartData.setCircleRadius(4); // 점 크기
        heartData.setDrawCircleHole(false);
        heartData.setDrawCircles(true);
        heartData.setCircleColor(Color.rgb(255, 230, 155)); //점 색깔
        heartData.setColor(Color.rgb(155, 155, 155));
        heartData.setDrawHorizontalHighlightIndicator(false);
        heartData.setDrawHighlightIndicators(false);
        heartData.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(heartData);

        LineData lineData = new LineData(dataSets);
        lineData.setValueTextSize(10);

        lineChart.setData(lineData);
        lineChart.setVisibleXRangeMaximum(4);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.moveViewToX(0); // 처음 시작했을때 X축이 어디서부터 보여지는지 알려주는 메소드
        lineChart.setScrollContainer(true);


        XAxis xAxis = lineChart.getXAxis(); //X축 설정하기
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x축에 데이터를 표시하는 위치
        xAxis.setLabelCount(4);
        xAxis.setTextColor(Color.rgb(118, 118, 118));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.enableGridDashedLine(10, 25, 0); //수직 격자선

        YAxis yLAxis = lineChart.getAxisLeft(); //y축 왼쪽 설정하기
        yLAxis.setTextColor(Color.rgb(163, 163, 16));
        yLAxis.setAxisMaximum(((float) 95.0));
        yLAxis.setAxisMinimum(((float) 40.0));
        yLAxis.setDrawAxisLine(true);
        yLAxis.setDrawGridLines(false);

        YAxis yRAxis = lineChart.getAxisRight(); //y축 오른쪽 설정하기, 오른쪽은 invisible 처리할거임.
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        // 마커포인트 해당 지점을 눌렀을 때 수치 값을 보여주는 클래스
        markerpointview marker = new markerpointview(getActivity().getApplicationContext(), R.layout.markerpointview);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
}
