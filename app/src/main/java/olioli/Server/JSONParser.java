package olioli.Server;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import olioli.tag.GPSTracker;

/**
 * Created by Ohardwick on 9/25/14.
 */
public class JSONParser {

    private String latitude = "lat";
    private String longitude = "long";
    private String user = "name";
    private String URL = "";

    GPSTracker mGps = new GPSTracker(null);

    private class HttpGetTask extends AsyncTask<Void, Void, String> {
        Double lat = mGps.getLatitude();
        Double lng = mGps.getLongitude();

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected String doInBackground(Void... voids) {

            HttpGet request = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            try {

                return mClient.execute(request, responseHandler);

            } catch (ClientProtocolException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (null != mClient)
                mClient.close();
            Log.v("Response", result);
            // Toast.makeText(getBaseContext(), result,
            // Toast.LENGTH_SHORT).show();

            try {


                JSONArray json = new JSONArray(result);

                for (int i = 0; i < json.length(); i++) {
                    JSONObject e = json.getJSONObject(i);
                    String point = e.getString("point");
                    Log.v("POINT", point);

                    String[] point2 = point.split(",");
                    double lat1 = Double.parseDouble(point2[0]);
                    double lng1 = Double.parseDouble(point2[1]);

                    Log.v("LLDN", ""+lat1+"&"+lng1);

//                    gMap.addMarker(new MarkerOptions().title(e
//                            .getString("name")).position(new LatLng(lat1, lng1)));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        }
    }

