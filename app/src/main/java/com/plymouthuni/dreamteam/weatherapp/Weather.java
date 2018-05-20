package com.plymouthuni.dreamteam.weatherapp;

public class Weather {

    private int tempurature;
    private String weather;
    private int picture;
    private String other_info;

    public Weather(int tempurature, String weather, int picture, String other_info) {
        this.tempurature = tempurature;
        this.weather = weather;
        this.picture = picture;
        this.other_info = other_info;
    }

    public Weather(int tempurature, String weather, String other_info) {
        this.tempurature = tempurature;
        this.weather = weather;
        this.other_info = other_info;
    }

    public int getTempurature() {
        return tempurature;
    }

    public void setTempurature(int tempurature) {
        this.tempurature = tempurature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info;
    }
}
