package com.theapp.naturalisation;

import android.app.Application;

import com.stripe.android.PaymentConfiguration;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51HNFh3L2Y5wKJYjRFdLNbcsJpICuHHUdH8CGpEbFLp86dhV1euYVBKssc4hsLXUVWFh8GEOOqVNxaYKp1XJKRQ5s00FNkbGyzh"
        );
    }
}