package olioli.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import olioli.dto.Users;
import olioli.tag.GPSTracker;

/**
 * Created by Ohardwick on 10/27/14.
 *
 * Known issue: need to async task. The connection wont complete without it
 *
 * Log.e Errors
 *      -D Driver issue
 *      -N Network issue
 *      -CU Collect User method
 *      -U Update method
 */

public class NetworkConnect implements INetwork {

    private List<Users> userList= new ArrayList<Users>();
    String line;
    String result;
    String status = "0";
    int ID = 0;

    public int Login(String username, String password, double lat, double lng){

        String url = "http://homepages.uc.edu/~scheidrt/login.php";
        String result = "";
        InputStream isr = null;

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }catch(Exception e){
            Log.d("debug", "Error in http connection " + e.toString());
        }

        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "UTF-8"), 8);
            String line = "";
            int counter = 0;
            while((line = reader.readLine()) != null){
                if(counter==2){
                    //pulls string in json array format + </body>
                    result = line;
                }
                counter++;
            }
            counter = result.length() - 7;
            result = result.substring(0, counter);
            Log.d("debug", "Result: " + result);
        }catch(Exception e){
            Log.d("debug", "Error converting result " + e.toString());
        }

        //parse json data
        try{
            //if(status.equalsIgnoreCase("1")){
            //String s = "";
            JSONArray jArray = new JSONArray(result);
            for(int i=0; i<jArray.length(); i++){
                JSONObject json = jArray.getJSONObject(i);
                if(json.getString("name").equalsIgnoreCase(username) && json.getString("password").equalsIgnoreCase(password)){
                    //if username and password match, return id
                    ID = json.getInt("id");
                }
            }
        }catch(Exception e){
            Log.d("debug", "Error parsing data " + e.toString());
        }
        Log.d("debuger", "LOGIN ID: " + ID);
        if(ID != 0){
            getStats(ID, lat, lng);
        }
        return ID;
    }

    public String getStats(int inputID, double latitude, double longitude) {
        int ID = inputID;
        double lat = latitude;
        double lng = longitude;

        String url = "http://homepages.uc.edu/~scheidrt/user.php?id=" + ID + "&lat=" + lat + "&lng=" + lng;
        result = "";
        line = "";

        InputStream isr = null;

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }catch(Exception e){
            Log.d("debug", "Error in http connection " + e.toString());
            //resultView.setText("Couldn't connect to DB.");
        }

        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "UTF-8"), 8);
            line = "";
            int counter = 0;
            result = "";
            while((line = reader.readLine()) != null){
                if(counter==4){
                    //pulls string in json array format + </body>
                    result = line;
                }
                counter++;
            }
            status = result.substring(0, 1);
            //find length of result string and subtract "</body>" (7)
            counter = result.length() - 7;
            //result = string of json array data
            result = result.substring(1, counter);
            Log.d("debug", "SQL Status: " + status + "; Result: " + result);
        }catch(Exception e){
            Log.d("debug", "Error converting result " + e.toString());
        }

        //parse json data
        getUsers();
        return result;
    }

    public List<Users> getUsers() {
        String url = "http://homepages.uc.edu/~scheidrt/getUsers.php";
        InputStream isr = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
        }catch(Exception e){
            Log.d("debug", "Error in http connection " + e.toString());
            //resultView.setText("Couldn't connect to DB.");
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "UTF-8"), 8);
            line = "";
            int counter = 0;
            result = "";
            while((line = reader.readLine()) != null){
                if(counter==4){
                    //pulls string in json array format + </body>
                    result = line;

                }
                Log.d("debug", "line " + counter+ " " + line);
                counter++;

            }

            status = result.substring(0, 1);
            //find length of result string and subtract "</body>" (7)
            counter = result.length() - 7;
            //result = string of json array data
            result = result.substring(0, counter);
            Log.d("debug", "SQL Status: " + status + "; Result: " + result);
        }catch(Exception e){
            Log.d("debug", "Error converting result " + e.toString());
        }

        Users user;

        try{

            line = "";
            JSONArray jArray = new JSONArray(result);
            for(int i=0; i<jArray.length(); i++){
                JSONObject json = jArray.getJSONObject(i);

                user = new Users();
                user.setName(json.getString("username"));
                user.setId(json.getInt("id"));
                user.setLat(json.getDouble("lat"));
                user.setLng(json.getDouble("long"));

                userList.add(user);

            }
            Log.d("debug", line);
            //resultView.setText(s);

        }catch(Exception e){
            Log.d("debug", "Error parsing data " + e.toString());
        }

        return userList;
    }


}
