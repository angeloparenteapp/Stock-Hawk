package com.sam_chordas.android.stockhawk.StockWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Angelo on 27/07/2016.
 */
public class StockWidgetService extends RemoteViewsService
{
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new StockWidgetFactory(getApplicationContext(), intent);
    }
}
