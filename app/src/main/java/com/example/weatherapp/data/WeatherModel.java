package com.example.weatherapp.data;

import java.util.List;

public class WeatherModel {

    private Coordinates coord;
    private List<Weather> weather;
    private Main main;
    private String name;

    class Coordinates {
        private float lon;
        private float lat;

        @Override
        public String toString() {
            return "Coordinates{" +
                    "lon=" + lon +
                    ", lat=" + lat +
                    '}';
        }

        public float getLon() {
            return lon;
        }

        public float getLat() {
            return lat;
        }
    }

    class Weather {
        private String main;
        private String description;
        private String icon;

        @Override
        public String toString() {
            return "Weather{" +
                    "main='" + main + '\'' +
                    ", description='" + description + '\'' +
                    ", icon='" + icon + '\'' +
                    '}';
        }

        public String getIcon() {
            return icon;
        }
    }

    class Main {
        private float temp;

        @Override
        public String toString() {
            return "Main{" +
                    "temp=" + temp +
                    '}';
        }

        public float getTemp() {
            return temp;
        }
    }

    @Override
    public String toString() {
        return "WeatherModel{" +
                "name = " + name +
                ", coord=" + coord.toString() +
                ", weather=" + weather.toString() +
                ", main=" + main.toString() +
                '}';
    }

    public float getLatitude() {
        return this.coord.getLat();
    }

    public float getLongitude() {
        return this.coord.getLat();
    }

    public String getWeatherIcon() {
        return "w"+this.weather.get(0).getIcon();
    }

    public String getName() {
        return this.name;
    }

    public String getTemp(){
        String temp = Float.toString((int)Math.rint(this.main.getTemp()));
        return temp + "°";
    }
}
