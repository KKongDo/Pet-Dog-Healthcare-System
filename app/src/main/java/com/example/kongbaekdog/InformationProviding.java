package com.example.kongbaekdog;

import static com.example.kongbaekdog.HeartBeat.array;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InformationProviding extends LinearLayout  {

    TextView curtWt, targetWt, currentCal, mvdis, befdis, CurDay;
    TextView Dogname, Dogtype, Dogbirth, Dogsex, Dogbcs;

    //현재 날짜 설정을 위한 함수 선언 및 초기화
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy");
    Date date = new Date();
    Date date2 = new Date();
    String year = simpleDateFormat2.format(date2);
    String time = simpleDateFormat.format(date);

    public InformationProviding(Context context){
        super(context);
        init(context);
    }

    public InformationProviding(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.informationlayout,this,true);

        //현재 날짜, 이름, 품종, 생년월일, 성별, BCS단계 변수
        CurDay=findViewById(R.id.textView);
        CurDay.setText("오늘날짜 : " + time);
        Dogname=findViewById(R.id.textView8);

        Dogtype=findViewById(R.id.textView2);
        Dogbirth=findViewById(R.id.textView12);
        Dogsex=findViewById(R.id.textView13);
        Dogbcs=findViewById(R.id.textView9);

        // 현재 체중, 목표체중, 일일 칼로리 변수선언
        curtWt=findViewById(R.id.textView5);
        targetWt=findViewById(R.id.textView6);
        currentCal=findViewById(R.id.textView7);

        //이동거리 전날 대비 거리차 변수선언
        mvdis=findViewById(R.id.textView10);
        befdis=findViewById(R.id.textView11);

    }

    //반려견의 이름, 나이(생년), 성별, BCS단계를 보여주는 코드
    public void setDogInform(String name, String type, String birth, String sex, int BCS){
        String dogyear=birth.substring(0,4);
        int yeartmp=Integer.parseInt(year)-Integer.parseInt(dogyear);
        Dogbirth.setText(Integer.toString(yeartmp)+"살("+dogyear.substring(2,4)+"년생)");
        Dogname.setText(name);
        Dogsex.setText(sex);
        Dogtype.setText(type);

        switch (BCS){
            case 1:
                Dogbcs.setText("마름/"+Integer.toString(BCS)+"단계");
                break;
            case 3:
                Dogbcs.setText("저체중/"+Integer.toString(BCS)+"단계");
                break;
            case 5:
                Dogbcs.setText("정상/"+Integer.toString(BCS)+"단계");
                break;
            case 7:
                Dogbcs.setText("과체중/"+Integer.toString(BCS)+"단계");
                break;
            case 9:
                Dogbcs.setText("비만/"+Integer.toString(BCS)+"단계");
                break;
            default:
                Dogbcs.setText(""+Integer.toString(BCS)+"단계");
                break;
        }
    }

    // 현재 체중, 목표 체중, 일일 칼로리 정보제공을 위한 메소드
    double storeCurrentVal;
    public void setCurtWt(double curtWtVal){
        storeCurrentVal=Math.round(curtWtVal);
        curtWt.setText(Double.toString(storeCurrentVal)+"Kg");
    }

    double tmp;
    public void settargetWt(int currentBCS){

        int BCS=currentBCS;

        switch (BCS){
            case 1:
                tmp=Math.round(storeCurrentVal*1.7);
                targetWt.setText(Double.toString(tmp)+"Kg");
                break;
            case 3:
                tmp=Math.round(storeCurrentVal*1.25);
                targetWt.setText(Double.toString(tmp)+"Kg");
                break;
            case 5:
                tmp=Math.round(storeCurrentVal);
                targetWt.setText(Double.toString(tmp)+"Kg");
                break;
            case 7:
                tmp=Math.round(storeCurrentVal/1.2);
                targetWt.setText(Double.toString(tmp)+"Kg");
                break;
            case 9:
                tmp=Math.round(storeCurrentVal/1.4);
                targetWt.setText(Double.toString(tmp)+"Kg");
                break;
        }
        double targetcal= Math.round((30*tmp+70)*1.8);
        currentCal.setText(Double.toString((targetcal))+"kcal");
    }

}
