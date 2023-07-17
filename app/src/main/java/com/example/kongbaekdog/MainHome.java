package com.example.kongbaekdog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MainHome extends Fragment {

    static String weight;
    static int BCS;
    static double dogweight;
    static String name,birth,type,sex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootview=(ViewGroup) inflater.inflate(R.layout.fragment_main_home,container,false);
        InformationProviding information = rootview.findViewById(R.id.information);

        weight=getArguments().getString("DogWeight");
        type=getArguments().getString("DogType");
        BCS = getArguments().getInt("BCS");
        name = getArguments().getString("DogName");
        birth=getArguments().getString("DogBirth");
        sex=getArguments().getString("DogSex");

        information.setDogInform(name,type,birth,sex,BCS);

        try{
            dogweight=Double.parseDouble(weight);
        }catch (NullPointerException e){
            dogweight=Double.parseDouble("65");
        }

        //첫 화면에서 반려인이 입력하는 값들
        information.setCurtWt(dogweight);
        information.settargetWt(BCS); // 반려인의 BCS 입력 ---> 목표체중 계산 및 일일 목표 칼로리 제공.

        //심박수 측정 프래그먼트에서 받아온 값을 정보제공자바클래스로 이동시키기 위한 메소드 사용
        return rootview;
    }
}
