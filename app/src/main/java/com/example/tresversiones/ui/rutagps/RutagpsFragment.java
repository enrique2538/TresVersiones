package com.example.tresversiones.ui.rutagps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.tresversiones.MainActivity;
import com.example.tresversiones.MapsActivity;
import com.example.tresversiones.R;

import java.util.List;
import java.util.Locale;

public class RutagpsFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    Button buscar;
    EditText latdestino, longdestino;
    double latmiorigen, longmiorigen;
    Boolean estado = true;
    TextView m1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragmento_rutagps, container, false);

        latdestino = (EditText) root.findViewById(R.id.latdestino);
        longdestino = (EditText) root.findViewById(R.id.longdestino);
        latdestino.setEnabled(false);
        longdestino.setEnabled(false);
        this.m1 = (TextView) root.findViewById(R.id.m1);
        m1.setText("Arica xd");
        buscar = (Button) root.findViewById(R.id.botonbuscar3);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    getLocation(location);
                }
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
                public void onProviderEnabled(String provider) {
                }
                public void onProviderDisabled(String provider) {
                }
            };

            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!estado) {
                    String latdestinoo = latdestino.getText().toString();
                    String longdestinoo = longdestino.getText().toString();
                    String latmiorigenn = String.valueOf(latmiorigen);
                    String longmiorigenn = String.valueOf(longmiorigen);
                    String opcion = "3";
                    try {
                        Double.parseDouble(latdestinoo);
                        Double.parseDouble(longdestinoo);
                        Double.parseDouble(latmiorigenn);
                        Double.parseDouble(longmiorigenn);
                        Intent i = new Intent(getActivity(), MapsActivity.class);
                        i.putExtra("latdestino", latdestinoo);
                        i.putExtra("longdestino", longdestinoo);
                        i.putExtra("latmiorigen", latmiorigenn);
                        i.putExtra("longmiorigen", longmiorigenn);
                        i.putExtra("opcion", opcion);
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Por favor verifique coordenadas", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return root;
    }

    public void getLocation(Location origen) {
        latmiorigen = origen.getLatitude();
        longmiorigen = origen.getLongitude();
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(
                    origen.getLatitude(), origen.getLongitude(), 1);
            if (!list.isEmpty()) {
                Address DirCalle = list.get(0);
                m1.setText("Direccion encontrada: \n" + DirCalle.getAddressLine(0));
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Error en la búsqueda de ubicación", Toast.LENGTH_LONG).show();
        }
    }
}