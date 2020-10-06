package com.example.weatherapp.logic;

        import com.example.weatherapp.data.WeatherModel;

        import retrofit2.Call;
        import retrofit2.http.GET;
        import retrofit2.http.Query;

public interface WeatherService {

    @GET("weather")
    Call<WeatherModel> getCurrentWeatherForCity(@Query("q") String city,
                                                @Query("appid") String key,
                                                @Query("units") String units);

    @GET("weather")
    Call<WeatherModel> getCurrentWeatherWithCoordinates(@Query("lon") String lon,
                                                        @Query("lat") String lat,
                                                        @Query("appid") String key,
                                                        @Query("units") String units);
}
