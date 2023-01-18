package com.example.myapplication;


import java.math.BigDecimal;

final class CryptoCurrency {
    private String name;
    private String symbol;
    private BigDecimal price;


    private CryptoCurrency(CryptoCurrencyBuilder builder) {
        this.name = builder.name;
        this.symbol = builder.symbol;
        this.price = builder.price.setScale(5,BigDecimal.ROUND_HALF_UP);

    }

    public static class CryptoCurrencyBuilder {
        private final String name;
        private final String symbol;
        private final BigDecimal price;

        public CryptoCurrencyBuilder(String name, String symbol, BigDecimal price) {
            this.name = name;
            this.symbol = symbol;
            this.price = price;
        }


        public CryptoCurrency build() {
            return new CryptoCurrency(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}