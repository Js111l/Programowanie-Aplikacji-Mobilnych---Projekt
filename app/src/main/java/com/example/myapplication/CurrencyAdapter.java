package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewholder> {
    private List<CryptoCurrency> currencies;
    private final Context context;

    public CurrencyAdapter(List<CryptoCurrency> currencies, Context context) {
        this.currencies = currencies;
        this.context = context;
    }

    public void setCurrencies(List<CryptoCurrency> currencies) {
        this.currencies = currencies;
        notifyDataSetChanged();
    }

    public List<CryptoCurrency> getCurrencies() {
        return new ArrayList<>(this.currencies);
    }

    @NonNull
    @Override
    public CurrencyAdapter.CurrencyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrencyViewholder(LayoutInflater.from(this.context)
                .inflate(R.layout.crypto_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.CurrencyViewholder holder, int position) {
        CryptoCurrency currency = this.currencies.get(position);
        holder.nameOfCurrency.setText(currency.getName());
        holder.price.setText(new StringBuilder().append("$ ")
                .append(currency.getPrice()).toString());
        holder.symbol.setText(currency.getSymbol());
        ImageButton button = holder.favButton;

        button.setOnClickListener(v -> MainActivity.addFavorite(new CryptoCurrency.CryptoCurrencyBuilder(
                currency.getName(),
                currency.getSymbol(),
                currency.getPrice())
                .build()));

    }

    @Override
    public int getItemCount() {
        return this.currencies.size();
    }


    public static class CurrencyViewholder extends RecyclerView.ViewHolder {
        private final TextView symbol;
        private final TextView price;
        private final TextView nameOfCurrency;
        private final ImageButton favButton;

        public CurrencyViewholder(@NonNull View itemView) {
            super(itemView);
            this.symbol = itemView.findViewById(R.id.symbol);
            this.price = itemView.findViewById(R.id.price);
            this.nameOfCurrency = itemView.findViewById(R.id.nameOfCurrency);
            this.favButton = itemView.findViewById(R.id.imageButton);
        }
    }
}

