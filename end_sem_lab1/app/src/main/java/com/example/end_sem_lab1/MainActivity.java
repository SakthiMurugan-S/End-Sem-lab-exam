package com.example.end_sem_lab1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText amountEditText;
    private Spinner fromCurrencySpinner;
    private Spinner toCurrencySpinner;
    private Button convertButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        amountEditText = findViewById(R.id.amountEditText);
        fromCurrencySpinner = findViewById(R.id.fromCurrencySpinner);
        toCurrencySpinner = findViewById(R.id.toCurrencySpinner);
        convertButton = findViewById(R.id.convertButton);
        resultTextView = findViewById(R.id.resultTextView);

        setupCurrencySpinners();
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertCurrency();
            }
        });
    }

    private void setupCurrencySpinners() {
        List<String> currencies = List.of("USD", "EUR", "GBP"); // Add more currencies as needed
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromCurrencySpinner.setAdapter(adapter);
        toCurrencySpinner.setAdapter(adapter);
    }

    private void convertCurrency() {
        String amountStr = amountEditText.getText().toString();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount = Double.parseDouble(amountStr);
        String fromCurrency = fromCurrencySpinner.getSelectedItem().toString();
        String toCurrency = toCurrencySpinner.getSelectedItem().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://open.er-api.com/v6/") // Ensure this is correct
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        String apiKey = "b01900fb7b16dab7fd4dd421778138c3";  // Your API key
        apiService.getExchangeRates(fromCurrency, apiKey).enqueue(new Callback<ExchangeResponse>() {
            @Override
            public void onResponse(Call<ExchangeResponse> call, Response<ExchangeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Double> rates = response.body().getRates();
                    Double conversionRate = rates.get(toCurrency);
                    if (conversionRate != null) {
                        double result = amount * conversionRate;
                        resultTextView.setText(String.format("%.2f", result));
                    } else {
                        resultTextView.setText("Conversion rate not found");
                    }
                } else {
                    resultTextView.setText("Error fetching rates");
                }
            }

            @Override
            public void onFailure(Call<ExchangeResponse> call, Throwable t) {
                resultTextView.setText("Failure: " + t.getMessage());
            }
        });
    }

}