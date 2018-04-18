package com.example.eraky.reviewit.user;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Eraky on 2/28/2018.
 */

public class Service extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new Factory(this.getApplicationContext());

    }
}
