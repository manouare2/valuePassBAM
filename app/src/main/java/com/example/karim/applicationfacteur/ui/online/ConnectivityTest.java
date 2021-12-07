package com.example.karim.applicationfacteur.ui.online;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karim.applicationfacteur.R;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static android.view.View.GONE;

public class ConnectivityTest extends AppCompatActivity {

    private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
    private ConnectionClassManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;
    private ProgressBar mRunningBar;
    private int mTries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectivity_test);

        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mTries = 0;
        mRunningBar = (ProgressBar) findViewById(R.id.pb2);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.youtube.com/")
                .build();

        mDeviceBandwidthSampler.startSampling();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                mDeviceBandwidthSampler.stopSampling();
                // Retry for up to 10 times until we find a ConnectionClass.
                if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                    mTries++;
                    //checkNetworkQuality();

                }
                if (!mDeviceBandwidthSampler.isSampling()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRunningBar.setVisibility(GONE);
                            Toast.makeText(getApplicationContext(),"Timed out",Toast.LENGTH_SHORT).show();
                        }
                    });

                    //
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d("Headers", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                Log.d("Body", response.body().string());
                Log.d("Bandwidth", mConnectionClassManager.getCurrentBandwidthQuality().toString());

                mDeviceBandwidthSampler.stopSampling();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRunningBar.setVisibility(GONE);
                        Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    public class ConnectionChangedListener implements ConnectionClassManager.ConnectionClassStateChangeListener {

        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }
}
