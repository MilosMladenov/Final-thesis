package com.milos.compass.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.milos.compass.R;

public class MainActivity extends AppCompatActivity {

    public AlertDialog.Builder builder;
    public AlertDialog dialog;

    public ImageView weatherImageView;
    public ImageView foodImageView;

    public Button checkWeather;
    public Button checkRestaurants;
    public Button closeWeather;
    public Button closeRestaurants;

    public EditText editWeather;
    public  EditText editRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);



        weatherImageView = findViewById(R.id.weather_image_view);
        foodImageView = findViewById(R.id.food_image_view);



        weatherImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createPopupDialogWeather();



            }
        });

        foodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createPopupDialogRestaurants();

            }
        });



    }

    private void createPopupDialogWeather() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_weather, null);

        checkWeather = view.findViewById(R.id.popup_weather_button);
        closeWeather = view.findViewById(R.id.closeButtonWeather);
        editWeather = view.findViewById(R.id.popup_weather_editText);

        checkWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editWeather.getText().toString().trim().length() != 0) {


                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("cityName", editWeather.getText().toString());
                    startActivity(intent);
                    dialog.dismiss();



                } else {

                    Toast.makeText(MainActivity.this,
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


    }

    private void createPopupDialogRestaurants() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_food, null);

        checkRestaurants = view.findViewById(R.id.popup_food_button);
        closeRestaurants = view.findViewById(R.id.closeButtonFood);
        editRestaurants = view.findViewById(R.id.popup_food_editText);

        checkRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (editRestaurants.getText().toString().trim().length() != 0) {


                    Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
                    intent.putExtra("restCityName", editRestaurants.getText().toString());
                    startActivity(intent);
                    dialog.dismiss();


                } else {

                    Toast.makeText(MainActivity.this,
                            "Enter city name to check nearby restaurants.", Toast.LENGTH_LONG).show();
                }

            }
        });

        closeRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


        builder.setView(view);
        dialog = builder.create(); //creating dialog object
        dialog.show(); // important step


    }
}