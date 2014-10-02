package olioli.dto;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ohardwick on 9/16/14.
 * Holds paths for variables throughout the application
 */
public class Paths {

    public static ArrayList<String> users;

    //public static HashMap<profile, ll> points;
    public static String profile;
    public static boolean IT = false;
    public static Location userLoc;
    public static Date timer;

    public static Double lat;
    public static Double lon;
    public static ArrayList<Double> latList;
    public static ArrayList<Double> lonList;
    //public static LatLng ll = new LatLng(lat, lon);

    public static HashMap<String, LatLng> points = new HashMap<String, LatLng>();
}
