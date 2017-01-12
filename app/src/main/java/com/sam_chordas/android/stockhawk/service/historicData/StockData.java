package com.sam_chordas.android.stockhawk.service.historicData;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Angelo on 06/08/2016.
 * Inspired by https://github.com/skyrohithigh/StockHawk code.
 * There are some variable i didn't use in the project but they could be useful in the future.
 */
public class StockData implements Parcelable
{
    public String companyName;
    public String exchangeName;
    public String firstTrade;
    public String lastTrade;
    public String currency;
    public double previousClosePrice;
    public ArrayList<StockSymbol> stockSymbols;

    public StockData(String companyName, String exchangeName, String firstTrade, String lastTrade, String currency, double previousClosePrice, ArrayList<StockSymbol> stockSymbols) {
        this.companyName = companyName;
        this.exchangeName = exchangeName;
        this.firstTrade = firstTrade;
        this.lastTrade = lastTrade;
        this.currency = currency;
        this.previousClosePrice = previousClosePrice;
        this.stockSymbols = stockSymbols;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.companyName);
        dest.writeString(this.exchangeName);
        dest.writeString(this.firstTrade);
        dest.writeString(this.lastTrade);
        dest.writeString(this.currency);
        dest.writeDouble(this.previousClosePrice);
        dest.writeTypedList(this.stockSymbols);
    }

    protected StockData(Parcel in) {
        this.companyName = in.readString();
        this.exchangeName = in.readString();
        this.firstTrade = in.readString();
        this.lastTrade = in.readString();
        this.currency = in.readString();
        this.previousClosePrice = in.readDouble();
        this.stockSymbols = in.createTypedArrayList(StockSymbol.CREATOR);
    }

    public static final Parcelable.Creator<StockData> CREATOR = new Parcelable.Creator<StockData>() {
        @Override
        public StockData createFromParcel(Parcel source) {
            return new StockData(source);
        }

        @Override
        public StockData[] newArray(int size) {
            return new StockData[size];
        }
    };
}