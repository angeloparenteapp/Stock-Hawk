package com.sam_chordas.android.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.service.historicData.HistoricData;
import com.sam_chordas.android.stockhawk.service.historicData.StockData;
import com.sam_chordas.android.stockhawk.service.historicData.StockSymbol;

import java.util.ArrayList;

/**
 * Created by Angelo on 06/08/2016.
 * Inspired by https://github.com/skyrohithigh/StockHawk code.
 */
public class StockDetails extends AppCompatActivity implements HistoricData.HistoricalDataCallback
{
    HistoricData historicData;
    ArrayList<StockSymbol> stockSymbols;

    LineChart lineChart;

    String symbol;
    String bidPrice;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        //Binding views
        lineChart = (LineChart) findViewById(R.id.lineChart);
        lineChart.setNoDataText(getString(R.string.loading_stock_data));

        //Getting Values from intents
        symbol = getIntent().getStringExtra(QuoteColumns.SYMBOL);
        bidPrice = getIntent().getStringExtra(QuoteColumns.BIDPRICE);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        actionBar.setTitle(String.format(getString(R.string.symbol_details), symbol));

        historicData = new HistoricData(this, this);

        if (Utils.isNetworkAvailable(this)) {
            historicData.getHistoricData(symbol);
        } else {
            historicData.setHistoricalDataStatus(HistoricData.STATUS_ERROR_NO_NETWORK);
            onFailure();
        }

    }

    @Override
    public void onSuccess(StockData stockMeta)
    {
        this.stockSymbols = stockMeta.stockSymbols;

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> xvalues = new ArrayList<>();

        for (int i = 0; i < this.stockSymbols.size(); i++) {

            StockSymbol stockSymbol = this.stockSymbols.get(i);
            double yValue = stockSymbol.close;

            xvalues.add(Utils.convertDate(stockSymbol.date));
            entries.add(new Entry((float) yValue, i));
        }


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        YAxis left = lineChart.getAxisLeft();
        left.setEnabled(true);
        left.setLabelCount(5, true);

        xAxis.setTextColor(Color.WHITE);
        left.setTextColor(Color.WHITE);

        lineChart.getAxisRight().setEnabled(false);

        lineChart.getLegend().setTextSize(12f);

        LineDataSet dataSet = new LineDataSet(entries, symbol);
        LineData lineData = new LineData(xvalues, dataSet);

        dataSet.setColor(Color.WHITE);
        dataSet.setValueTextColor(Color.WHITE);
        lineData.setValueTextColor(Color.WHITE);
        lineChart.setDescriptionColor(Color.WHITE);

        lineData.setDrawValues(false);
        dataSet.setDrawCircles(false);

        lineChart.setDescription(getString(R.string.last_12month_comparison));
        lineChart.setData(lineData);
        lineChart.animateX(3000);
    }

    @Override
    public void onFailure()
    {
        String errorMessage = "";

        @HistoricData.HistoricalDataStatuses
        int status = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(HistoricData.HISTORICAL_DATA_STATUS, -1);

        switch (status) {
            case HistoricData.STATUS_ERROR_JSON:
                errorMessage += getString(R.string.data_error_json);
                break;
            case HistoricData.STATUS_ERROR_NO_NETWORK:
                errorMessage += getString(R.string.data_no_internet);
                break;
            case HistoricData.STATUS_ERROR_PARSE:
                errorMessage += getString(R.string.data_error_parse);
                break;
            case HistoricData.STATUS_ERROR_UNKNOWN:
                errorMessage += getString(R.string.data_unknown_error);
                break;
            case HistoricData.STATUS_ERROR_SERVER:
                errorMessage += getString(R.string.data_server_down);
                break;
            case HistoricData.STATUS_OK:
                errorMessage += getString(R.string.data_no_error);
                break;
            default:
                break;
        }

        lineChart.setNoDataText(errorMessage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}