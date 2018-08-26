package com.github.prokopiy.mcchestshop.data;

import java.math.BigDecimal;

public class Price {
    protected Integer amount;
    protected BigDecimal buyPrice;
    protected BigDecimal sellPrice;
    protected String filter;

    public Price(Integer amount, BigDecimal buyPrice, BigDecimal sellPrice, String filter) {
        this.amount = amount;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.filter = filter;
    }

    public Integer getAmount(){
        return amount;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public String getFilter() {
        return filter;
    }

    public String toString() {
        String s = filter + "*" + amount.toString() + " - B " + buyPrice.toString() + " : " + sellPrice.toString() + " S";
        return s;
    }

}
