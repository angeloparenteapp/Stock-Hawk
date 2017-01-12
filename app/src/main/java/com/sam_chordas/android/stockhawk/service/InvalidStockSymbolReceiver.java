package com.sam_chordas.android.stockhawk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.sam_chordas.android.stockhawk.R;

/**
 * Created by Angelo on 21/07/2016.
 * to manage the invalid stock.
 */
public class InvalidStockSymbolReceiver extends BroadcastReceiver
{
    public InvalidStockSymbolReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, R.string.invalid_stock, Toast.LENGTH_LONG).show();
    }
}