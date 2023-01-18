package com.example.myapplication;


import static com.android.volley.toolbox.Volley.newRequestQueue;
import static java8.util.stream.Collectors.toList;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import java8.util.Comparators;
import java8.util.Maps;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class MainActivity extends AppCompatActivity {


    private List<CryptoCurrency> currencies;
    private static List<CryptoCurrency> favoriteCurrenciesList;
    private CurrencyAdapter currencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView currencyRecyclerView = findViewById(R.id.currencyRecycleView);


        this.currencies = new ArrayList<>();
        fetchData();
        this.currencyAdapter = new CurrencyAdapter(sort(currencies), this);
        this.favoriteCurrenciesList = new ArrayList<>();

        currencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        currencyRecyclerView.setAdapter(this.currencyAdapter);


        EditText searchEdt = findViewById(R.id.currencySearch);

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable sequence) {
                filterCurrenciesList(sequence.toString());
            }
        });
        Button showAllCryptoListButton = findViewById(R.id.showAllCryptoListButton);
        showAllCryptoListButton.setOnClickListener(
                onclick -> this.currencyAdapter
                        .setCurrencies(sort(this.currencies)));

        Button favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(
                onclick -> this.currencyAdapter
                        .setCurrencies(sort(this.favoriteCurrenciesList)));

    }

    public static List<CryptoCurrency> sort(List<CryptoCurrency> currencies) {
        return StreamSupport.stream(currencies)
                .sorted(Comparators.reversed(Comparators.comparing(CryptoCurrency::getPrice)))
                .collect(Collectors.toList());
    }

    public static void addFavorite(CryptoCurrency cryptoCurrency) {
        favoriteCurrenciesList.add(cryptoCurrency);
    }

    private void filterCurrenciesList(String sequence) {
        this.currencyAdapter.setCurrencies(
                new ArrayList<>(
                        StreamSupport.stream(this.currencyAdapter.getCurrencies())
                                .filter(x -> x.getName()
                                        .toLowerCase(Locale.ROOT)
                                        .startsWith(sequence))
                                .collect(toList())));

    }

    private void fetchData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest",
                null,
                response -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("data");
                        for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            this.currencies.add(
                                    new CryptoCurrency.
                                            CryptoCurrencyBuilder(
                                            dataObj.getString("name"),
                                            dataObj.getString("symbol"),
                                            new BigDecimal((dataObj.getJSONObject("quote")
                                                    .getJSONObject("USD"))
                                                    .getString("price"))).build()
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->
                        Toast.makeText(
                                MainActivity.this,
                                "Check your internet connection, unable to retrieve data!",
                                Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() {
                return Maps.of("X-CMC_PRO_API_KEY", "4e17aa40-259d-4214-89a9-b8cd8dad7198");
            }
        };
        RequestQueue queue = newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}