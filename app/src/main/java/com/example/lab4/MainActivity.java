package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button start_button;
    private TextView download_progress;
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_button = findViewById(R.id.start_button);
        download_progress = findViewById(R.id.download_text);
    }

    public void mockFileDownloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                start_button.setText("DOWNLOADING...");
            }
        });

        for(int downloadProgress = 0; downloadProgress <= 100; downloadProgress=downloadProgress+10) {
            if(stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        start_button.setText("Start");
                    }
                });
                return;
            }
            Log.d(TAG, "Download Progress: " + downloadProgress + "%");

            int finalDownloadProgress = downloadProgress;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    download_progress.setText("Download Progress: " + finalDownloadProgress + "%");
                }
            });

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                start_button.setText("Start");
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                download_progress.setText("");
            }
        });
    }

    public void startDownload(View view){
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view) {
        stopThread = true;
    }

    private class ExampleRunnable implements Runnable {
        @Override
        public void run() {
            mockFileDownloader();
        }
    }
}
