package com.ubiobio.uv_ubb_iot;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainTemperatura extends AppCompatActivity {

    ArrayList<Integer> datos = new ArrayList<>();
    final int  UV_SENSOR = 100;
    final int  TEMPERATURA_SENSOR = 101;
    final int  HUMEDAD_SENSOR = 102;
    int valor;
    int valorPromedio;
    int valorMinimo;
    int valorMaximo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_temperatura);

        Button btnUpdate = findViewById(R.id.btnActualizarTemperatura);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar( TEMPERATURA_SENSOR);


            }
        });
        btnUpdate.callOnClick();
        //Log.d("lala","probando");




    }

    private void actualizarChart() {
        valorPromedio=calcularPromedio(datos);

        TextView valorTV = findViewById(R.id.valorTempId);
        TextView promTV = findViewById(R.id.promTempId);
        valorTV.setText("Actual: "+valor+"°C");
        promTV.setText("Promedio: "+valorPromedio+"°C");



        DecoView decoView = (DecoView) findViewById(R.id.dynamicArcViewTemperatura);
        decoView.deleteAll();
        decoView.configureAngles(220,0);

        //Background

        int backIndex = decoView.addSeries(new SeriesItem.Builder(Color.parseColor("#99BDBDBD"))
                .setRange(valorMinimo, valorMaximo, valorMinimo)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .setSpinClockwise(false)


                .setCapRounded(false)
                .setLineWidth(180f)
                .build()
        );


        //IntentoDeGradiente
        int seriesIndexValor = decoView.addSeries(
                new SeriesItem.Builder(getResources().getColor(R.color.DeepSkyBlue),
                        getResources().getColor(R.color.Coral))
                        .setRange(valorMinimo, valorMaximo, valorMinimo)
                        .setInitialVisibility(false)
                        .setLineWidth(100f)
                        .setCapRounded(false)
                        .setInitialVisibility(false)
                        .setInset(new PointF(-30f, -30f))


                        .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                        .build());



        int seriesIndexProm = decoView.addSeries(
                //de verde claro a verdeoscuro
                new SeriesItem.Builder(Color.parseColor("#00ff00"), Color.parseColor("#004000"))
                        .setRange(valorMinimo, valorMaximo, valorMinimo)
                        .setInitialVisibility(false)
                        .setLineWidth(50f)
                        .setCapRounded(false)
                        .setInset(new PointF(50f, 50f))
                        .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)

                        .build())

                ;
        //cambia al promedio
        decoView.addEvent(new DecoEvent.Builder(valorPromedio)
                .setIndex(seriesIndexProm)
                .setDuration(1000)
                .build());

        //se cambia el back
        decoView.addEvent(new DecoEvent.Builder(valorMaximo)
                .setIndex(backIndex)
                .setDuration(1000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(valor)
                .setIndex(seriesIndexValor)
                .setDuration(1000)
                .build());

    }

    private void actualizar(int SENSOR){
        ObtenerArrayDeDatos(SENSOR);

    }

    private int calcularPromedio (ArrayList<Integer> datos){
        int result=0;
        for (Integer el:datos) {
            result+=el;
        }
        if(datos.size()>0){
            return result/datos.size();
        }else{
            return -1;
        }

    }

    private void ObtenerArrayDeDatos(int TIPO_SENSOR) {

        String ACCESS_TOKEN = "ohABQEDJZN";
        String SENSOR_TOKEN ="";
        switch (TIPO_SENSOR){
            case UV_SENSOR: {
                SENSOR_TOKEN = "8IvrZCP3qa";
                valorMinimo = 280;
                valorMaximo = 390;
                break;
            }
            case TEMPERATURA_SENSOR: {
                SENSOR_TOKEN = "E1yGxKAcrg";
                valorMinimo = -40;
                valorMaximo = 80;
                break;

            }case HUMEDAD_SENSOR: {
                SENSOR_TOKEN = "VIbSnGKyLW";
                valorMinimo = 0;
                valorMaximo = 100;
                break;
            }
        }

        /*
        * DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
String date = df.format(Calendar.getInstance().getTime());
        * */
        DateFormat df = new SimpleDateFormat("ddMMyyyy");
        String dia=df.format(Calendar.getInstance().getTime());

        String WS_URL="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/"+ACCESS_TOKEN+"/"+SENSOR_TOKEN+"/"+dia;


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                WS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JsonResponse = new JSONObject(response);
                            /*SnackBar.make(MainActivity.this.getCurrentFocus(), responseJson.getString("info"), Snackbar.LENGTH_LONG)
                                    .show();
*/
                            JSONArray data = JsonResponse.getJSONArray("data");
                            Log.d("LOGWS",response);
                            datos.clear();
                            for (int i=0; i<data.length(); i++) {
                                datos.add(i,data.getJSONObject(i).getInt("valor"));
                                if(i == data.length()-1){
                                    valor=data.getJSONObject(i).getInt("valor");

                                    actualizarChart();
                                }

                            }

                            Log.d("ARRAYyyyy",datos.toString());
                            //JSONObject object=  data.getJSONObject(data.length()-1);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG WS",error.toString());
            }
        });/*{
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("login",nick);
                params.put("pass",pass);
                return params;
            }
        };*/
        requestQueue.add(request);


    }
}
