package com.ubiobio.uv_ubb_iot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView valueUV ;
    JSONObject responseJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnUv = findViewById(R.id.btnUv);
        Button btnHumedad = findViewById(R.id.btnHum);
        Button btnTemperatura = findViewById(R.id.btnTemp);

        btnUv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cambiar a la Activity de UV
                Intent intentUv = new Intent(getApplicationContext(), MainUV.class);
                startActivity(intentUv);

            }
        });


        btnHumedad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cambiar a la Activity de UV
                Intent intentUv = new Intent(getApplicationContext(), MainHumedad.class);
                startActivity(intentUv);

            }
        });

        btnTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cambiar a la Activity de UV
                Intent intentUv = new Intent(getApplicationContext(), MainTemperatura.class);
                startActivity(intentUv);

            }
        });




    }

}
