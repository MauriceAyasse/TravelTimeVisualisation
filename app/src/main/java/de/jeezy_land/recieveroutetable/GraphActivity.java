package de.jeezy_land.recieveroutetable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    String graphDate;
    private LogDataHandler logDataHandler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_graph);
        Intent intent = getIntent();
        graphDate = intent.getStringExtra(MainActivity.GRAPH_DATE);
        logDataHandler = (LogDataHandler) intent.getParcelableExtra(MainActivity.LOG_DATA);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> strRouten = new ArrayList<>();
        strRouten.add("Nach Hause");
        strRouten.add("Zur Arbeit");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strRouten);
        if (spinner != null) {
            spinner.setAdapter(arrayAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setLineChart(position+1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        setLineChart(1);

    }

    private void setLineChart(int route) {
        if (route == 1 || route == 2) {
            LineChart lineChart = (LineChart) findViewById(R.id.chart);
            if (lineChart != null) {
                lineChart.setData(getLineData(graphDate,route));
                lineChart.invalidate();
            }
        }
    }

    private LineData getLineData(String datum,int route) {
        LineData lineData;
        LineDataSet lineDataSet;
        List<Entry> entries = new ArrayList<>();
        List<String> entrieDescriptions = new ArrayList<>();

        List<LogData> logDatas = logDataHandler.getLoggedDay(datum,7,route);
        int i = 0;
        int cutBeginning = "xxxx-xx-xx ".length();
        int cutEnding = cutBeginning + "00:00".length();
        for (LogData logData : logDatas) {
            entrieDescriptions.add(logData.getEintragsdatumDatum().substring(cutBeginning,cutEnding));
            entries.add(new Entry(logData.getReiseZeit()/60,i++));
        }
        lineDataSet = new LineDataSet(entries,"Reisezeit");
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawCubic(true);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setColor(R.color.colroGraph);
        lineDataSet.setDrawCircles(false);
        lineData = new LineData(entrieDescriptions,lineDataSet);
        return lineData;
    }
}
