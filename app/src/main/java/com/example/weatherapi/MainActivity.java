package com.example.weatherapi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    EditText editText;
    TextView cityTextView, degreTextView, desCriptiontextView;
    ImageView imageView ;

    FloatingActionButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView  = findViewById(R.id.wheather_image);
        editText = findViewById(R.id.search_edit);
        cityTextView = findViewById(R.id.cityName);
        degreTextView = findViewById(R.id.temp_textView);
        desCriptiontextView = findViewById(R.id.ription_textView);

        button = findViewById(R.id.floating_search);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager methodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(), 0);
        
        api_key(editText.getText().toString().trim());
    }

    private void api_key(final String city ) {

        OkHttpClient client = new OkHttpClient();
        //TODO::  use your won appID 
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=fdfa8b80824797e4dbc476e375646e9b&units=metric")
                .get()
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try{

            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    String data = response.body().string();
                    try {
                        JSONObject json = new JSONObject(data);
                        JSONArray array = json.getJSONArray("weather");
                        JSONObject object = array.getJSONObject(0);

                        String description = object.getString("description");
                        String icon = object.getString("icon");

                        JSONObject main = json.getJSONObject("main");
                        double temp = main.getDouble("temp");

                        setText(cityTextView, city);
                        String tempC = Math.round(temp)+ " °C";
                        setText(degreTextView, tempC);
                        setText(desCriptiontextView, description);

                        // setlogo()
                        Log.i(TAG, "onResponse: "+ icon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setText(final TextView text, final  String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
    private void setLogo(final ImageView text, final  String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
