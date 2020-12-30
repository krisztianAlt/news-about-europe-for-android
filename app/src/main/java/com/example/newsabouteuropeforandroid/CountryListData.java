package com.example.newsabouteuropeforandroid;

public class CountryListData {
    private String countryName;
    private int imgId;

    public CountryListData(String countryName, int imgId) {
        this.countryName = countryName;
        this.imgId = imgId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
