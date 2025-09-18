//package com.example.currencyconverter;
//
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.utils.Utils;
//import java.util.ArrayList;
//
//public class ResultActivity extends AppCompatActivity {
//    private LineChart lineChart;
//    private RadioGroup timeFilterGroup;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_result);
//
//        TextView resultText = findViewById(R.id.result_text);
//        Button backButton = findViewById(R.id.back_button);
//        lineChart = findViewById(R.id.line_chart);
//        timeFilterGroup = findViewById(R.id.time_filter_group);
//
//        String convertedAmount = getIntent().getStringExtra("converted_amount");
//        resultText.setText(convertedAmount);
//
//        setupChart(getMockData("1M"));
//
//        timeFilterGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            String selectedTimeFrame;
//            if (checkedId == R.id.rb_1d) {
//                selectedTimeFrame = "1D";
//            } else if (checkedId == R.id.rb_5d) {
//                selectedTimeFrame = "5D";
//            } else if (checkedId == R.id.rb_1m) {
//                selectedTimeFrame = "1M";
//            } else if (checkedId == R.id.rb_1y) {
//                selectedTimeFrame = "1Y";
//            } else {
//                selectedTimeFrame = "5Y";
//            }
//            setupChart(getMockData(selectedTimeFrame));
//        });
//
//        backButton.setOnClickListener(v -> finish());
//    }
//
//    private void setupChart(ArrayList<Entry> dataValues) {
//        LineDataSet lineDataSet = new LineDataSet(dataValues, "Currency Trend");
//        lineDataSet.setColor(Color.parseColor("#FF6D72"));
//        lineDataSet.setLineWidth(2f);
//        lineDataSet.setDrawCircles(false);
//        lineDataSet.setDrawValues(false);
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        lineDataSet.setDrawFilled(true);
//
//        if (Utils.getSDKInt() >= 18) {
//            Drawable gradient = getResources().getDrawable(R.drawable.gradient_fill);
//            lineDataSet.setFillDrawable(gradient);
//        } else {
//            lineDataSet.setFillColor(Color.parseColor("#FF6D72"));
//        }
//
//        LineData lineData = new LineData(lineDataSet);
//        lineChart.setData(lineData);
//        lineChart.invalidate();
//
//        lineChart.setDrawGridBackground(false);
//        lineChart.getDescription().setEnabled(false);
//        lineChart.setTouchEnabled(true);
//        lineChart.setPinchZoom(true);
//        lineChart.setScaleEnabled(true);
//        lineChart.setBackgroundColor(Color.BLACK);
//
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextColor(Color.LTGRAY);
//        xAxis.setDrawGridLines(false);
//        xAxis.setLabelCount(6, true);
//
//
//        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setTextColor(Color.LTGRAY);
//        leftAxis.setDrawGridLines(true);
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
//        lineChart.getAxisRight().setEnabled(false);
//
//        Legend legend = lineChart.getLegend();
//        legend.setTextColor(Color.LTGRAY);
//        legend.setEnabled(true);
//    }
//
//    private ArrayList<Entry> getMockData(String timeFrame) {
//        ArrayList<Entry> data = new ArrayList<>();
//
//        switch (timeFrame) {
//            case "1D":
//                data.add(new Entry(0, 86.2f));
//                data.add(new Entry(6, 86.5f));
//                data.add(new Entry(12, 86.7f));
//                data.add(new Entry(18, 86.4f));
//                data.add(new Entry(24, 86.5f));
//                break;
//
//            case "5D":
//                for (int i = 0; i <= 5; i++) {
//                    data.add(new Entry(i, 86.0f + (float) Math.random()));
//                }
//                break;
//
//            case "1M":
//                for (int i = 0; i <= 30; i += 5) {
//                    data.add(new Entry(i, 86.5f + (float) Math.random()));
//                }
//                break;
//
//            case "1Y":
//                for (int i = 0; i <= 12; i++) {
//                    data.add(new Entry(i, 85.5f + (float) Math.random() * 2));
//                }
//                break;
//
//            case "5Y":
//                for (int i = 0; i <= 5; i++) {
//                    data.add(new Entry(i, 84.0f + (float) Math.random() * 3));
//                }
//                break;
//        }
//        return data;
//    }
//}

package com.example.currencyconverter;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private LineChart lineChart;
    private RadioGroup timeFilterGroup;
    private String fromCurrency;
    private String toCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultText = findViewById(R.id.result_text);
        Button backButton = findViewById(R.id.back_button);
        lineChart = findViewById(R.id.line_chart);
        timeFilterGroup = findViewById(R.id.time_filter_group);

        // Get values passed from MainActivity
        String convertedAmount = getIntent().getStringExtra("converted_amount");
        fromCurrency = getIntent().getStringExtra("from_currency");
        toCurrency = getIntent().getStringExtra("to_currency");

        resultText.setText(convertedAmount);

        // Fetch default data (7 days)
        new FetchHistoricalRates().execute(fromCurrency, toCurrency, "7");

        // Radio button listener
        // Radio button listener
        timeFilterGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int days = 7; // default

            if (checkedId == R.id.rb_1d) {
                days = 2; // minimum 2 days to show line
            } else if (checkedId == R.id.rb_5d) {
                days = 5;
            } else if (checkedId == R.id.rb_1m) {
                days = 30;
            } else if (checkedId == R.id.rb_1y) {
                days = 365;
            } else if (checkedId == R.id.rb_5y) {
                days = 1825;
            }

            new FetchHistoricalRates().execute(fromCurrency, toCurrency, String.valueOf(days));
        });


        // Back button
        backButton.setOnClickListener(v -> finish());
    }

    // AsyncTask to fetch historical rates
    private class FetchHistoricalRates extends AsyncTask<String, Void, List<Entry>> {
        private List<String> xAxisLabels = new ArrayList<>();

        @Override
        protected List<Entry> doInBackground(String... params) {
            List<Entry> entries = new ArrayList<>();
            try {
                String from = params[0];
                String to = params[1];
                int days = Integer.parseInt(params[2]);

                // Prepare start and end dates
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String endDate = sdf.format(cal.getTime());
                cal.add(Calendar.DAY_OF_MONTH, -days);
                String startDate = sdf.format(cal.getTime());

                // API call
                String apiUrl = "https://api.frankfurter.app/" + startDate + ".." + endDate
                        + "?from=" + from + "&to=" + to;

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject rates = jsonResponse.getJSONObject("rates");

                List<String> dateKeys = new ArrayList<>();
                Iterator<String> keys = rates.keys();
                while (keys.hasNext()) {
                    dateKeys.add(keys.next());
                }
                Collections.sort(dateKeys);

                int index = 0;
                for (String date : dateKeys) {
                    double value = rates.getJSONObject(date).getDouble(to);
                    entries.add(new Entry(index++, (float) value));
                    xAxisLabels.add(date);
                }

            } catch (Exception e) {
                Log.e("API Error", "Failed to fetch historical data", e);
            }
            return entries;
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            if (entries.isEmpty()) {
                Toast.makeText(ResultActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                return;
            }

            LineDataSet dataSet = new LineDataSet(entries, fromCurrency + " → " + toCurrency);
            dataSet.setColor(Color.parseColor("#FF6D72"));
            dataSet.setLineWidth(2f);
            dataSet.setDrawCircles(false);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(Color.parseColor("#FF6D72"));

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);

            // Chart styling
            Description description = new Description();
            description.setText("Exchange Rate Trend");
            description.setTextColor(Color.WHITE);
            lineChart.setDescription(description);
            lineChart.setDrawGridBackground(false);
            lineChart.setTouchEnabled(true);
            lineChart.setPinchZoom(true);
            lineChart.setScaleEnabled(true);
            lineChart.setBackgroundColor(Color.BLACK);

            // X-Axis (date labels)
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(Color.WHITE);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f);

            // Show fewer labels, avoid clutter
            xAxis.setLabelCount(5, true);
            xAxis.setLabelRotationAngle(-45);

            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                    int index = (int) value;
                    return (index >= 0 && index < xAxisLabels.size()) ? xAxisLabels.get(index) : "";
                }
            });

            // ✅ Add custom marker view for exact values
            CustomMarkerView markerView = new CustomMarkerView(ResultActivity.this, R.layout.marker_view);
            markerView.setChartView(lineChart);
            lineChart.setMarker(markerView);

            lineChart.invalidate();

            // Legend styling
            Legend legend = lineChart.getLegend();
            legend.setTextColor(Color.WHITE);
            legend.setTextSize(14f);
            legend.setForm(Legend.LegendForm.LINE);
            legend.setFormSize(14f);
            legend.setXEntrySpace(10f);
            legend.setYEntrySpace(5f);
            legend.setWordWrapEnabled(true);

        }
    }
}
