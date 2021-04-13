package com.example.awesomemausam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_getcityId, btn_getWeatherById, btn_getWeatherByCity;
    EditText entered_data_input;
    ListView weather_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

//        if(timeOfDay >= 0 && timeOfDay < 18) {
//            findViewById(R.id.main_page).setBackground(R.drawable.background);
//        } else if(timeOfDay >= 18 && timeOfDay < 24){
//            findViewById(R.id.main_page).setBackground(R.drawable.night_background);
//        }

        ///assigning values to each controls in the layout
        btn_getcityId = findViewById(R.id.button_getCity_id);
        btn_getWeatherById = findViewById(R.id.button_getWeatherByCityId);
        btn_getWeatherByCity = findViewById(R.id.button_getWeatherby_name);

        entered_data_input = findViewById(R.id.enter_data_input);
        weather_details = findViewById(R.id.weather_reports);


        btn_getcityId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherDataService weatheardata = new WeatherDataService(MainActivity.this);
                weatheardata.getCityId(entered_data_input.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String issue) {
                        Toast.makeText(MainActivity.this, ""+issue, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityId) {
                        Toast.makeText(MainActivity.this,""+cityId, Toast.LENGTH_SHORT).show();

                    }
                });

                ///this didn't returned
                //Toast.makeText(MainActivity.this,"Returned : "+cityId, Toast.LENGTH_SHORT).show();
            }


// Instantiate the RequestQueue.
                //from where it is being called
                //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);



        });

// Request a string response from the provided URL.
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this,"Some issue", Toast.LENGTH_SHORT).show();
//                    }
//                });



        btn_getWeatherById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Button is working fine", Toast.LENGTH_SHORT).show();


                WeatherDataService weatheardata = new WeatherDataService(MainActivity.this);
                weatheardata.getCityWeatherForecastById(entered_data_input.getText().toString(), new WeatherDataService.Forecast_By_Id_Response() {
                    @Override
                    public void onError(String issue) {
                        Toast.makeText(MainActivity.this, ""+issue, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        //Toast.makeText(MainActivity.this, weatherReportModel.toString(), Toast.LENGTH_SHORT).show();

                        //here the response will be a list we need to add that to list view
                        //we need an array adapter to show the list

                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModel);
                        weather_details.setAdapter(arrayAdapter);

                    }


                });
            }
        });

        btn_getWeatherByCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Button is working fine", Toast.LENGTH_SHORT).show();

                WeatherDataService weatheardata = new WeatherDataService(MainActivity.this);
                weatheardata.getCityWeatherForecastByName(entered_data_input.getText().toString(), new WeatherDataService.getCityWeatherForecastByNameCallback() {
                    @Override
                    public void onError(String issue) {
                        Toast.makeText(MainActivity.this, ""+issue, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        //Toast.makeText(MainActivity.this, weatherReportModel.toString(), Toast.LENGTH_SHORT).show();

                        //here the response will be a list we need to add that to list view
                        //we need an array adapter to show the list

                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModel);
                        weather_details.setAdapter(arrayAdapter);

                    }


                });
            }
        });
    }
}