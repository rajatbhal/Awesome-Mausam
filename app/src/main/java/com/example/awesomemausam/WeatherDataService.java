package com.example.awesomemausam;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {


    public static final String QUERY_TO_WEATHER_API_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_TO_WEATHER_API_BY_ID = "https://www.metaweather.com/api/location/";

    Context context;
    String cityId = "";

    //will help to deal with main activity
    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{

        void onError(String issue);

        void onResponse(String cityId);
    }



    public void getCityId(String city_name, VolleyResponseListener volleyResponseListener){

        String url = QUERY_TO_WEATHER_API_FOR_CITY_ID + city_name;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                //not sure of response will use try/catch
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityId = cityInfo.getString("woeid");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(context, ""+cityId, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityId);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something went wrong");

            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);

        //return cityId;

    }



    public interface Forecast_By_Id_Response{

        void onError(String issue);

        void onResponse(List<WeatherReportModel> weatherReportModelList);
    }


    public void getCityWeatherForecastById(String cityId, final Forecast_By_Id_Response forecast_by_id_response){
        final List<WeatherReportModel> weatherReportModelList = new ArrayList<>();

        String url = QUERY_TO_WEATHER_API_BY_ID +  cityId;

        ///get the json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                try {
                    //List<JSONObject> consolidated_weather = (List<JSONObject>) response.getJSONArray("consolidated_weather");
                    JSONArray consolidated_weather = response.getJSONArray("consolidated_weather");

                    ///create instance of WeatherReportModel
                    ///created empty weather report model that we can fill


                    for(int i=0; i<consolidated_weather.length(); i++) {
                        WeatherReportModel weatherReportModel = new WeatherReportModel();


                        JSONObject one_day = (JSONObject) consolidated_weather.get(i);
                        weatherReportModel.setId(one_day.getInt("id"));
                        weatherReportModel.setWeather_state_name(one_day.getString("weather_state_name"));
                        weatherReportModel.setWeather_state_abbr(one_day.getString("weather_state_abbr"));
                        weatherReportModel.setWind_direction_compass(one_day.getString("wind_direction_compass"));
                        weatherReportModel.setCreated(one_day.getString("created"));
                        weatherReportModel.setApplicable_date(one_day.getString("applicable_date"));
                        weatherReportModel.setMin_temp(one_day.getLong("min_temp"));
                        weatherReportModel.setMax_temp(one_day.getLong("max_temp"));
                        weatherReportModel.setThe_temp(one_day.getLong("the_temp"));
                        weatherReportModel.setWind_speed(one_day.getLong("wind_speed"));
                        weatherReportModel.setWind_direction(one_day.getLong("wind_direction"));
                        weatherReportModel.setAir_pressure(one_day.getInt("air_pressure"));
                        weatherReportModel.setHumidity(one_day.getInt("humidity"));
                        weatherReportModel.setVisibility(one_day.getLong("visibility"));
                        weatherReportModel.setPredictability(one_day.getInt("predictability"));

                        weatherReportModelList.add(weatherReportModel);

                    }

                    forecast_by_id_response.onResponse(weatherReportModelList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
                ///get the proprty of consolidated_weather
                ///get each item from the array


        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public interface  getCityWeatherForecastByNameCallback{
        void onError(String issue);

        void onResponse(List<WeatherReportModel> weatherReportModelList);

    }

    public void getCityWeatherForecastByName(String city_name, getCityWeatherForecastByNameCallback getCityWeatherForecastByNameCallback){
        ///call api twice get the city id for the name

        getCityId(city_name, new VolleyResponseListener() {
                    @Override
                    public void onError(String issue) {

                    }

                    @Override
                    public void onResponse(String cityId) {
                        ///create a new instance of volleylistener
                        getCityWeatherForecastById(cityId, new Forecast_By_Id_Response() {
                            @Override
                            public void onError(String issue) {

                            }

                            @Override
                            public void onResponse(List<WeatherReportModel> weatherReportModelList) {
                                //we have the weather report
                                getCityWeatherForecastByNameCallback.onResponse(weatherReportModelList);

                            }
                        });

                    }
                });

                ///fetch the city forecast if we have the city id


    }

}
