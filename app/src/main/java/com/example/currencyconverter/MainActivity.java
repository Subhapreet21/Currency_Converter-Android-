//package com.example.currencyconverter;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import org.json.JSONObject;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    private Spinner fromCurrency, toCurrency;
//    private EditText amountInput;
//    private TextView resultText;
//    private Button convertButton;
//    private ImageView swapButton;
//    private JSONObject exchangeRates;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        fromCurrency = findViewById(R.id.from_currency);
//        toCurrency = findViewById(R.id.to_currency);
//        amountInput = findViewById(R.id.amount_input);
//        convertButton = findViewById(R.id.convert_button);
//        swapButton = findViewById(R.id.swap_button);
//
//        new FetchExchangeRates().execute("https://open.er-api.com/v6/latest/USD");
//
//        convertButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                convertCurrency();
//            }
//        });
//
//        swapButton.setOnClickListener(v -> {
//            int fromPosition = fromCurrency.getSelectedItemPosition();
//            int toPosition = toCurrency.getSelectedItemPosition();
//
//            fromCurrency.setSelection(toPosition);
//            toCurrency.setSelection(fromPosition);
//        });
//    }
//
//    private class FetchExchangeRates extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//            try {
//                URL url = new URL(urls[0]);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder response = new StringBuilder();
//                String line;
//
//                while ((line = reader.readLine()) != null) {
//                    response.append(line);
//                }
//
//                reader.close();
//                return response.toString();
//
//            } catch (Exception e) {
//                Log.e("API Error", "Failed to fetch exchange rates", e);
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                resultText.setText("Error: Unable to fetch exchange rates");
//                return;
//            }
//
//            try {
//                Log.d("API Response", result);
//                JSONObject jsonResponse = new JSONObject(result);
//
//                if (!jsonResponse.has("rates")) {
//                    resultText.setText("Error: Invalid API response");
//                    return;
//                }
//
//                exchangeRates = jsonResponse.getJSONObject("rates");
//
//                List<String> currencyList = new ArrayList<>();
//                Iterator<String> keys = exchangeRates.keys();
//
//                while (keys.hasNext()) {
//                    currencyList.add(keys.next());
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, currencyList);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                fromCurrency.setAdapter(adapter);
//                toCurrency.setAdapter(adapter);
//            } catch (Exception e) {
//                Log.e("JSON Error", "Failed to parse API response", e);
//                resultText.setText("Error: Failed to process exchange rates");
//            }
//        }
//    }
//    private void convertCurrency() {
//        try {
//            if (exchangeRates == null) {
//                resultText.setText("Error: Exchange rates not loaded");
//                return;
//            }
//
//            String from = fromCurrency.getSelectedItem().toString();
//            String to = toCurrency.getSelectedItem().toString();
//            String amountStr = amountInput.getText().toString();
//
//            if (amountStr.isEmpty()) {
//                Toast.makeText(MainActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            double amount = Double.parseDouble(amountStr);
//
//            if (!exchangeRates.has(from) || !exchangeRates.has(to)) {
//                resultText.setText("Error: Invalid currency selection");
//                return;
//            }
//
//            double fromRate = exchangeRates.getDouble(from);
//            double toRate = exchangeRates.getDouble(to);
//
//            double convertedAmount = (amount / fromRate) * toRate;
//            String result = String.format("%.2f %s", convertedAmount, to);
//
//            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
//            intent.putExtra("converted_amount", result);
//            startActivity(intent);
//
//        } catch (NumberFormatException e) {
//            Toast.makeText(MainActivity.this, "Invalid amount entered", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Log.e("Conversion Error", "Failed to convert currency", e);
//            Toast.makeText(MainActivity.this, "Conversion failed", Toast.LENGTH_SHORT).show();
//        }
//    }
//}

package com.example.currencyconverter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner fromCurrency, toCurrency;
    private EditText amountInput;
    private TextView resultText;
    private Button convertButton;
    private ImageView swapButton;
    private JSONObject exchangeRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromCurrency = findViewById(R.id.from_currency);
        toCurrency = findViewById(R.id.to_currency);
        amountInput = findViewById(R.id.amount_input);
        convertButton = findViewById(R.id.convert_button);
        swapButton = findViewById(R.id.swap_button);
//        resultText = findViewById(R.id.result_text);

        new FetchExchangeRates().execute("https://open.er-api.com/v6/latest/USD");

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertCurrency();
            }
        });

        swapButton.setOnClickListener(v -> {
            int fromPosition = fromCurrency.getSelectedItemPosition();
            int toPosition = toCurrency.getSelectedItemPosition();

            fromCurrency.setSelection(toPosition);
            toCurrency.setSelection(fromPosition);
        });
    }

    private class FetchExchangeRates extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                return response.toString();

            } catch (Exception e) {
                Log.e("API Error", "Failed to fetch exchange rates", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                resultText.setText("Error: Unable to fetch exchange rates");
                return;
            }

            try {
                Log.d("API Response", result);
                JSONObject jsonResponse = new JSONObject(result);

                if (!jsonResponse.has("rates")) {
                    resultText.setText("Error: Invalid API response");
                    return;
                }

                exchangeRates = jsonResponse.getJSONObject("rates");

                List<String> currencyList = new ArrayList<>();
                Iterator<String> keys = exchangeRates.keys();

                while (keys.hasNext()) {
                    currencyList.add(keys.next());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, currencyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fromCurrency.setAdapter(adapter);
                toCurrency.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("JSON Error", "Failed to parse API response", e);
                resultText.setText("Error: Failed to process exchange rates");
            }
        }
    }

    private void convertCurrency() {
        try {
            if (exchangeRates == null) {
                resultText.setText("Error: Exchange rates not loaded");
                return;
            }

            String from = fromCurrency.getSelectedItem().toString();
            String to = toCurrency.getSelectedItem().toString();
            String amountStr = amountInput.getText().toString();

            if (amountStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);

            if (!exchangeRates.has(from) || !exchangeRates.has(to)) {
                resultText.setText("Error: Invalid currency selection");
                return;
            }

            double fromRate = exchangeRates.getDouble(from);
            double toRate = exchangeRates.getDouble(to);

            double convertedAmount = (amount / fromRate) * toRate;
            String result = String.format("%.2f %s", convertedAmount, to);

            // ✅ Pass from & to currencies along with converted amount
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("converted_amount", result);
            intent.putExtra("from_currency", from);
            intent.putExtra("to_currency", to);
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Invalid amount entered", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Conversion Error", "Failed to convert currency", e);
            Toast.makeText(MainActivity.this, "Conversion failed", Toast.LENGTH_SHORT).show();
        }
    }
}
