package com.example.tresversiones.ui.marcador;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tresversiones.MapsActivity;
import com.example.tresversiones.R;

public class MarcadorFragment extends Fragment {

    private HomeViewModel homeViewModel;

    EditText latinicio, longinicio;
    Button buscar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragmento_marcador, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        latinicio = (EditText) root.findViewById(R.id.latmarcador);
        longinicio = (EditText) root.findViewById(R.id.longmarcador);

        buscar = (Button) root.findViewById(R.id.botonbuscar3);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latinicioo = latinicio.getText().toString();
                String longinicioo = longinicio.getText().toString();
                String opcion = "1";

                try {
                    Double.parseDouble(latinicioo);
                    Double.parseDouble(longinicioo);

                    Intent i = new Intent(getActivity(), MapsActivity.class);
                    i.putExtra("latinicio", latinicioo);
                    i.putExtra("longinicio", longinicioo);
                    i.putExtra("opcion", opcion);
                    startActivity(i);

                }catch(Exception ex)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "Coordenadas no válidas", Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }
}