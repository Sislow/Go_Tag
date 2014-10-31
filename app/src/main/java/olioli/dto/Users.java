package olioli.dto;

import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Ohardwick on 10/27/14.
 *
 * Collects Users to pass between classes
 * id/it will be used later when the game logic is put in place
 *
 */
public class Users {

    private int id;
    private String name;
    private Double lat;
    private Double lng;
    private boolean it;


    public Users(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }




}
