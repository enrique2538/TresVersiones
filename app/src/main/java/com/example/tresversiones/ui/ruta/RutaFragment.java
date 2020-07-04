package com.example.tresversiones.ui.ruta;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.tresversiones.MapsActivity;
import com.example.tresversiones.R;

public class RutaFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    Button buscar;
    EditText latinicio, longinicio, latdestino, longdestino;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragmento_ruta, container, false);

        latinicio = (EditText) root.findViewById(R.id.latdestino);
        longinicio = (EditText) root.findViewById(R.id.longdestino);
        latdestino = (EditText) root.findViewById(R.id.latdestino);
        longdestino = (EditText) root.findViewById(R.id.longdestino);
        buscar = (Button) root.findViewById(R.id.botonbuscar2);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitudinicio = latinicio.getText().toString();
                String longitudinicio = longinicio.getText().toString();
                String latituddestino = latdestino.getText().toString();
                String longituddestino = longdestino.getText().toString();
                String opcion = "2";

                try {
                    Double.parseDouble(latitudinicio);
                    Double.parseDouble(longitudinicio);
                    Double.parseDouble(latituddestino);
                    Double.parseDouble(longituddestino);

                    Intent i = new Intent(getActivity(), MapsActivity.class);
                    i.putExtra("latinicio", latitudinicio);
                    i.putExtra("longinicio", longitudinicio);
                    i.putExtra("latdestino", latituddestino);
                    i.putExtra("longdestino", longituddestino);
                    i.putExtra("opcion", opcion);
                    startActivity(i);

                }catch(Exception ex)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Los datos ingresados no son válidos", Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }


}