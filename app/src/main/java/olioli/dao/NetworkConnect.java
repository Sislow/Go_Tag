package olioli.dao;

import android.content.Context;
import android.util.Log;

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

    Connection connection;

    private List<Users> userList= new ArrayList<Users>();

    /**
     * connect method utilizes jbdc and db4free
     * If the connection fails due to driver issue or network they are printed under error log
     *
     */
    public boolean connect()
    {
        boolean complete = false;
        connection = null;
            try{
                Class.forName("com.mysql.jdbc.Driver");
            } catch(Exception e) {
                Log.e("Error-D","UNABLE TO FIND DRIVER " + e);
            }

            try{

                connection = DriverManager.getConnection("jdbc:mysql://www.db4free.net:3306/gameoftag", "gotag", "taggame");
                complete = true;

            } catch (SQLException e) {
                Log.e("Error-N","Connectfailed " + e);
            }

        return complete;
    }

    /**
     * Gives application a context. Will be used later for update method
     *
     */
    public Context getApplicationContext() {

        return null;
    }

    /**
     * Collects a list of users and returns them to set marker users in current game
     * setUsers();
     *
     */
    public List<Users> collectUsers(){
        String sql = "SELECT * FROM users";
        Users user;

        //Connect to mySQL
        try{
            if(connect() == true){

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                // Get row count for for loop
                int rowNumber = getRows(resultSet);

                while(resultSet.next())
                {
                    for(int i = 0; i <=rowNumber; i++)
                    {

                        if(resultSet.getString(2)!= null)
                        {

                            user = new Users();

                            user.setName(resultSet.getString(2));
                            user.setLat(resultSet.getDouble(3));
                            user.setLng(resultSet.getDouble(4));
                            userList.add(user);

                        }

                    }


                }
            }else
            {
                System.out.println("Connection failed");
            }
        }
        catch(Exception e)
        {
            Log.e("Error-CU", "" + e);
        }
        return userList;

    }

    public Boolean update(){

        String sql = "SELECT * FROM users WHERE name=otis" ;
        Boolean updated = false;
        Double lat = null;
        Double lng = null;
        GPSTracker mGPS = new GPSTracker(this.getApplicationContext());
        if(mGPS.canGetLocation != false) {
            lat = mGPS.getLatitude();
            Log.d("lat", lat.toString());

            lng = mGPS.getLongitude();

        }
        try{
            if(connect() == true){

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);


                resultSet.updateDouble(3, lat);
                resultSet.updateDouble(4, lng);
                updated = true;
            }else
            {
                System.out.println("Connection failed");
            }
        }
        catch(Exception e)
        {
            Log.e("Error-U", "" + e);
        }

        return updated;
    }

    // Goes to last row and gets the row count then returns it to collectUsers()
    //Maybe in the catch block give a specific exception, I understand you are returning 0
    //but If the code in the try fails and it is just returning 0 not explaining what the problem is unless this is for the user specifically
    
    public int getRows(ResultSet res){
        int totalRows = 0;
        try {
            res.last();
            totalRows = res.getRow();
            res.beforeFirst();
        }
        catch(Exception ex)  {
            return 0;
        }
        return totalRows;
    }

}
