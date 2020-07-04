package com.example.tresversiones;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latinicio, longinicio, latdestino, longdestino, latmiorigen, longmiorigen;
    int opcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        recibircoordenadas();
    }

    private void recibircoordenadas() {
        Bundle coordenadas = this.getIntent().getExtras();
        latinicio = Double.parseDouble(coordenadas.getString("latinicio"));
        longinicio = Double.parseDouble(coordenadas.getString("longinicio"));
        opcion = Integer.parseInt(coordenadas.getString("opcion"));

        if(opcion == 2)
        {
            latdestino = Double.parseDouble(coordenadas.getString("latdestino"));
            longdestino = Double.parseDouble(coordenadas.getString("longdestino"));
        }
        else if(opcion == 3)
        {
            latmiorigen = Double.parseDouble(coordenadas.getString("latmiorigen"));
            longmiorigen = Double.parseDouble(coordenadas.getString("latmiorigen"));
        }
        //latdestino = Double.parseDouble(coordenadas.getString("latdestino"));
        //longdestino = Double.parseDouble(coordenadas.getString("longdestino"));
        //latmiorigen = Double.parseDouble(coordenadas.getString("latmiorigen"));
        //longmiorigen = Double.parseDouble(coordenadas.getString("longmiorigen"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String coordenadas;

        //latitud -18.478518 longitud -70.32106 arica
        LatLng marcador1 = new LatLng(latinicio, longinicio);
        coordenadas = (opcion==1)? "Tu Marcador" : (opcion==2) ? "Origen" : "Destino";
        mMap.addMarker(new MarkerOptions().position(marcador1).title(coordenadas));


        if(opcion < 3)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marcador1));

        if(opcion == 2)
        {
            LatLng marcador2 = new LatLng(latdestino, longdestino);
            mMap.addMarker(new MarkerOptions().position(marcador2).title("Origen 2"));

            String url = getRequestUrl(marcador1, marcador2);
            TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
            taskRequestDirections.execute(url);
        }

        else if(opcion == 3)
        {
            LatLng miorigen = new LatLng(latmiorigen, longmiorigen);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(miorigen));
            mMap.addMarker(new MarkerOptions().position(miorigen).title("Origen"));

            String url = getRequestUrl(miorigen, marcador1);
            TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
            taskRequestDirections.execute(url);
        }

    }


    private String getRequestUrl(LatLng origen, LatLng destino) {
        String resultado = "";

        String string_origen = "origin="+origen.latitude+","+origen.longitude;
        String string_destino = "destination="+destino.latitude+","+destino.longitude;

        String sensor = "sensor=false";
        String modo = "mode=driving";

        String param = string_origen+"&"+string_destino+"&"+sensor+"&"+modo;
        String salida = "json";
        String llaveapi = "key=AIzaSyAuzNxrS6pWKhm1pNPW2dDYezJDZ1tfWMY";

        resultado = "https://maps.googleapis.com/maps/api/directions/"+salida+"?"+param+"&"+llaveapi;

        return resultado;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String linea = "";

            while ((linea = bufferedReader.readLine())!=null){
                stringBuffer.append(linea);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();

            httpURLConnection.disconnect();
        }

        return responseString;
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";

            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);

        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);

            } catch (JSONException e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);

            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for(List<HashMap<String, String>> path : lists){
                points = new ArrayList();
                polylineOptions = new PolylineOptions();
                for (HashMap<String, String> point : path){
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));
                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!= null){
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direccion no encontradaa", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
