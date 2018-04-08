package goparento.com.sample.Utilities;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


/**
 * Created by mohit on 15/6/16.m
 */
public class MultiDexApplication extends Application {
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}