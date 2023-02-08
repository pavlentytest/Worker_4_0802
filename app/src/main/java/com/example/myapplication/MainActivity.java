package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private OneTimeWorkRequest work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                work = new OneTimeWorkRequest.Builder(MyWorker.class).build();
                WorkManager.getInstance(getApplicationContext()).enqueue(work);
                WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(work.getId()).observe(
                        MainActivity.this, workInfo -> {
                            if(workInfo != null) {
                                Log.d("RRR","WorkInfo received state: "+workInfo.getState());
                                String message =  workInfo.getOutputData().getString("keyA");
                                int i = workInfo.getOutputData().getInt("keyB",0);
                                Log.d("RRR","message: "+ message + ", i: "+ i);
                            }
                        }
                );
            }
        });
    }

    public void getData() throws IOException {
        String str = "https://predictor.yandex.net/api/v1/predict.json/complete?key=pdct.1.1.20220412T145449Z.ed53b660ddacdc8e.353ee4c0c5adc174b6be636450d97faa6e34a072&q=hello+wo&lang=en&limit=5";
        HttpsURLConnection connection;
        URL url = new URL(str);
        connection = (HttpsURLConnection) url.openConnection();
        connection.setConnectTimeout(10000);
        connection.connect();

        int code = connection.getResponseCode();
        Scanner scanner = new Scanner(connection.getInputStream());
        
    }
}