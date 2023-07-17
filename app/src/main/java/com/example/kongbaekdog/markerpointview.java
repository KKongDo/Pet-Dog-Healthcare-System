package com.example.kongbaekdog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.example.kongbaekdog.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;


/* 각 포인트별 심박수 수치 값을 눌렀을 때 심박수 숫자를 보여주는 클래스 */
public class markerpointview extends MarkerView {

    private TextView MarkerPoint;

    public markerpointview(Context context, int layoutResource) {
        super(context, layoutResource);
        MarkerPoint = findViewById(R.id.MarkerPoint);
    }

    @Override
    public void refreshContent(Entry ent, Highlight highlight) {
        if (ent instanceof CandleEntry) {
            CandleEntry candleEnt = (CandleEntry) ent;
            MarkerPoint.setText("" + Utils.formatNumber(candleEnt.getHigh(), 0, true));
        }

        else {
            MarkerPoint.setText("" + Utils.formatNumber(ent.getY(), 0, true));
        }
        super.refreshContent(ent, highlight);
    }
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
