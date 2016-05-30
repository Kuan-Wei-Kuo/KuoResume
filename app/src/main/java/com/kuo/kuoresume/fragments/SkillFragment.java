package com.kuo.kuoresume.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuo.kuoresume.R;
import com.kuo.mychartlib.model.ColumnData;
import com.kuo.mychartlib.model.LineData;
import com.kuo.mychartlib.until.ChartRendererUntil;
import com.kuo.mychartlib.view.ColumnChartView;
import com.kuo.mychartlib.view.LineChartView;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/30.
 */
public class SkillFragment extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_skill, container, false);
            initView();
        }

        return rootView;
    }

    private void initView() {
        createLanguageChart();
        createSoftwareChart();
    }

    private void createLanguageChart() {

        int[] colors = {ChartRendererUntil.CHART_GREEN, ChartRendererUntil.CHART_PINK,
                ChartRendererUntil.CHART_RED, ChartRendererUntil.CHART_YELLOW,};

        int[] values = {5, 3, 2, 2};

        String[] axisX = {"Java", "C", "C#", "JavaScript"};

        ArrayList<ColumnData> columnData = new ArrayList<>();

        for(int i = 0 ; i < values.length ; i++)
            columnData.add(new ColumnData(axisX[i],
                    values[i],
                    colors[i]));

        ColumnChartView columnChartView = (ColumnChartView) rootView.findViewById(R.id.columnChartView);
        columnChartView.setColumnData(columnData);
    }

    private void createSoftwareChart() {

        int[] colors = {ChartRendererUntil.CHART_GREEN, ChartRendererUntil.CHART_PINK,
                ChartRendererUntil.CHART_RED, ChartRendererUntil.CHART_YELLOW,};

        int[] values = {5, 3, 2, 2};

        String[] axisX = {"AndroidStudio", "Eclipse", "Git", "Unity"};

        ArrayList<LineData> lineData = new ArrayList<>();

        for(int i = 0 ; i < values.length ; i++)
            lineData.add(new LineData(axisX[i],
                    values[i],
                    colors[i]));

        LineChartView lineChartView = (LineChartView) rootView.findViewById(R.id.lineChartView);
        lineChartView.setLineData(lineData);
    }
}
