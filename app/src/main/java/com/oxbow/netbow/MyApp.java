package com.oxbow.netbow;

import android.app.Application;

/**
 * Created by kubap on 18.04.2018.
 */

public class MyApp extends Application{

        private static MyApp mInstance;

        @Override
        public void onCreate() {
            super.onCreate();

            mInstance = this;
        }

        public static synchronized MyApp getInstance() {
            return mInstance;
        }

        public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
            ConnectivityReceiver.connectivityReceiverListener = listener;
        }
}

