package com.example.weatherapp.logic;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.weatherapp.data.WeatherModel;
import com.example.weatherapp.views.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPresenter {

    private static final int REQUEST_CODE = 200;
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private final String IPP_ID = "f187342de9efacc09244fdbde294d54b";
    private MainActivity mainActivity;
    private String currentCity;
    private WeatherService service;

    private String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    private LocationManager mManager;
    private LocationListener lListener;

    public MainPresenter(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    private void setCurrentCity(String currentCity) {
        if(currentCity.equals("City") || currentCity.isEmpty()){
            this.currentCity = "Sofia";
        } else {
            this.currentCity = currentCity;
        }
    }

    private String getCurrentCity() {
        return currentCity;
    }

    public void setupRetrofitWithCity(String city) {
        setCurrentCity(city);
        service = getWeatherService();
        Call<WeatherModel> callWithCity = service.getCurrentWeatherForCity(getCurrentCity(),IPP_ID,"metric");

        getBodyResponse(callWithCity);
    }

    private void getBodyResponse(Call<WeatherModel> call) {
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if(response != null && response.isSuccessful()){
                    Log.d("Clima","onResponse() method called with success");
                    Log.d("Clima",response.toString());
                    Log.d("Clima",response.body().toString());
                    WeatherModel model = response.body();
                    mainActivity.updateUI(model);
                } else {
                    Log.d("Clima","onResponse() method called with error");
                    Log.d("Clima",response.message());
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d("Clima","onFailure() method called");
                Log.d("Clima",t.toString());
            }
        });
    }

    private WeatherService getWeatherService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(WeatherService.class);
    }

    public void setupRetrofitWithLongLat(String longitude,String latitude){
        service = getWeatherService();
        Call<WeatherModel> callWithLocation = service.getCurrentWeatherWithCoordinates(longitude,latitude, IPP_ID,"metric");
        getBodyResponse(callWithLocation);
    }

    public void requestPermission(int requestCode, @NonNull int[] grantResults){
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Clima", "orRequestPermission(): Permission granted!");
                getWeatherForCurrentLocation();
            } else {
                Log.d("Clima", "orRequestPermission(): Permission denied!");
            }
        }
    }

    public void getWeatherForCurrentLocation() {
        mManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        lListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Clima","onLocationChanged() callback received");
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());
                setupRetrofitWithLongLat(longitude,latitude);
                Log.d("Clima","latitude: " + latitude  + " longitude: " + longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Clima","onStatusChanged() callback received");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Clima","onProviderEnabled () callback received");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Clima","onProviderDisabled() callback received");
            }
        };

        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(mainActivity,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;
        }

        mManager.requestLocationUpdates(LOCATION_PROVIDER, 5000, 1000, lListener);
    }
}
