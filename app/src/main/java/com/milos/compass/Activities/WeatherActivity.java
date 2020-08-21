package com.milos.compass.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.milos.compass.Constants;
import com.milos.compass.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;



public class WeatherActivity extends AppCompatActivity {


    public TextView cityNameTxt, detailsTxt, currentTemperatureTxt, lastUpdatedTxt, weatherIcon, checkRestaurants;
    public String cityName;
    public ImageView restaurantCheck;
    Typeface weatherFont;

    public AlertDialog.Builder builder;
    public AlertDialog dialog;

    public Button checkWeatherAgain;
    public Button checkRestaurantsFromWeather;
    public Button closeWeather;
    public Button closeRestaurants;

    public EditText editWeather;
    public  EditText editRestaurants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        getSupportActionBar().setElevation(0);

        Intent intent = getIntent();
        cityName = intent.getStringExtra("cityName");
        new RetrieveFeedTask(cityName).execute();

        cityNameTxt = findViewById(R.id.cityName);
        detailsTxt = findViewById(R.id.detailsField);
        currentTemperatureTxt = findViewById(R.id.currentTemperature);
        lastUpdatedTxt = findViewById(R.id.lastUpdated);
        restaurantCheck = findViewById(R.id.foodImageView);
        checkRestaurants = findViewById(R.id.checkRestaurants);
        weatherIcon = findViewById(R.id.weatherIcon);
        weatherFont = Typeface.createFromAsset(WeatherActivity.this.getAssets(), "fonts/weathericons-regular-webfont.ttf");

        weatherIcon.setTypeface(weatherFont);

    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = WeatherActivity.this.getString(R.string.weather_sunny);
            } else {
                icon = WeatherActivity.this.getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = WeatherActivity.this.getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = WeatherActivity.this.getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = WeatherActivity.this.getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = WeatherActivity.this.getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = WeatherActivity.this.getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = WeatherActivity.this.getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, JSONObject> {

        private URL url;
        public String lat, lng;

        public RetrieveFeedTask(String city) {
            try {
                this.url = new URL(String.format(Constants.WEATHER_URL, city));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder json = new StringBuilder(1024);
                String temp = "";

                while ((temp = bufferedReader.readLine()) != null) {
                    json.append(temp).append("\n");
                }
                bufferedReader.close();

                final JSONObject data = new JSONObject(json.toString());
                final JSONObject detailsObject = data.getJSONArray("weather").getJSONObject(0);

                JSONObject coords = data.getJSONObject("coord");
                lat = coords.getString("lat");
                lng = coords.getString("lon");

                Log.d("lat", lat);
                Log.d("lng", lng);

                Log.d("JSON: ", json.toString());



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject main = data.getJSONObject("main");

                            currentTemperatureTxt.setText(main.getDouble("temp") + " ℃");
                            detailsTxt.setText(detailsObject.getString("description").toUpperCase() + "\n\n" +
                                    "Humidity: " + main.getString("humidity") + "%" + "\n" +
                                    "Pressure: " + main.getString("pressure") + " hPa");
                            cityNameTxt.setText(data.getString("name").toUpperCase(Locale.US) +
                                    ", " +
                                    data.getJSONObject("sys").getString("country"));

                            DateFormat df = DateFormat.getDateTimeInstance();
                            String updatedOn = df.format(new Date(data.getLong("dt") * 1000));
                            lastUpdatedTxt.setText("Last update: " + updatedOn);

                            setWeatherIcon(detailsObject.getInt("id"),
                                    data.getJSONObject("sys").getLong("sunrise") * 1000,
                                    data.getJSONObject("sys").getLong("sunset") * 1000);

                            checkRestaurants.setText(data.getString("name") +
                                    ", " +
                                    data.getJSONObject("sys").getString("country"));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            } catch (Exception e) {
                Log.d("Error", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            restaurantCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(WeatherActivity.this, RestaurantListActivity.class);
                    intent.putExtra("restCityName", cityName);
                    startActivity(intent);

                }


            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_search:

                builder = new AlertDialog.Builder(WeatherActivity.this);
                View view = getLayoutInflater().inflate(R.layout.popup_weather, null);

                checkWeatherAgain = view.findViewById(R.id.popup_weather_button);
                closeWeather = view.findViewById(R.id.closeButtonWeather);
                editWeather = view.findViewById(R.id.popup_weather_editText);

                checkWeatherAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (editWeather.getText().toString().trim().length() != 0) {


                            Intent intent = new Intent(WeatherActivity.this, WeatherActivity.class);
                            intent.putExtra("cityName", editWeather.getText().toString());
                            startActivity(intent);
                            dialog.dismiss();

                            finish();


                        } else {

                            Toast.makeText(WeatherActivity.this,
                                    "Enter city name to check the weather.", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                closeWeather.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                builder.setView(view);
                dialog = builder.create(); //creating dialog object
                dialog.show(); // important step

                break;



            case R.id.chekRestfromWeather:

                builder = new AlertDialog.Builder(WeatherActivity.this);
                View restView = getLayoutInflater().inflate(R.layout.popup_food, null);

                checkRestaurantsFromWeather = restView.findViewById(R.id.popup_food_button);
                closeRestaurants = restView.findViewById(R.id.closeButtonFood);
                editRestaurants = restView.findViewById(R.id.popup_food_editText);

                checkRestaurantsFromWeather.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (editRestaurants.getText().toString().trim().length() != 0) {


                            Intent intent = new Intent(WeatherActivity.this, RestaurantListActivity.class);
                            intent.putExtra("restCityName", editRestaurants.getText().toString());
                            startActivity(intent);
                            dialog.dismiss();



                        } else {

                            Toast.makeText(WeatherActivity.this,
                                    "Enter city name to check the restaurants.", Toast.LENGTH_LONG).show();
                        }

                    }
                });

                closeRestaurants.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                builder.setView(restView);
                dialog = builder.create(); //creating dialog object
                dialog.show(); // important step

                break;

            case R.id.home_button:

                Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                startActivity(intent);

                finish();

        }



        return super.onOptionsItemSelected(item);
    }
}

