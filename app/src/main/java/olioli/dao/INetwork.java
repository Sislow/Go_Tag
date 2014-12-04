package olioli.dao;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import olioli.dto.Users;

/**
 * Created by Ohardwick on 10/27/14.
 */
public interface INetwork {

    //public void fetchJSON();
    public int Login(String username, String password, double lat, double lng);
    public String getStats(int inputID, double latitude, double longitude);
    public List<Users> getUsers();
}
