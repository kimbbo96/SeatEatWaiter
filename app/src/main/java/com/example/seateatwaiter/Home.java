package com.example.seateatwaiter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends AppCompatActivity {

    String url = "https://seateat-be.herokuapp.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Activity activity = this;

        Toolbar toolbar = findViewById(R.id.tool_bar_simple);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        Button settings = findViewById(R.id.settings);
        System.out.println("settings"+settings.getText());

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        Button scanQR = findViewById(R.id.buttonScan);
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if(result.getContents()== null){
                Toast.makeText(this,"you cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else{
                final String QRScanned = result.getContents();
                Toast.makeText(this, QRScanned,Toast.LENGTH_LONG).show();

                /*
                OkHttpClient cl = new OkHttpClient(); // inizio la procedura di get
                Request request = new Request.Builder().url(url+"/api/testnotifications/"+QRScanned).build();
                System.out.println(url+"/api/testnotifications/"+QRScanned);
                cl.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.err.println("invio token fallito");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("invio token ok");
                    }
                });*/
                String credenziali = "a" + ":" + "a";
                final String BasicBase64format = "Basic " + Base64.getEncoder().encodeToString(credenziali.getBytes());



                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(BasicBase64format);
                        OkHttpClient client = new OkHttpClient();
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("customerToken", QRScanned);

                        } catch (JSONException e) {
                            Log.d("OKHTTP3", "JSON exception");
                            e.printStackTrace();
                        }
                        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                        Request newReq = new Request.Builder()
                                .url(url + "/api/scancustomerqr")
                                .post(body).addHeader("Authorization", BasicBase64format)
                                .build();

                        Response response = null;
                        try {
                            response = client.newCall(newReq).execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("www" + response.message());
                        System.out.println("www" + response.isSuccessful());

                        if (!response.isSuccessful()) {
                            System.err.println("invio messaggio non riuscito");
                        }
                    }
                }).start();

            }
        }




        else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
