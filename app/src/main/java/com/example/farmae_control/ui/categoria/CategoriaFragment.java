package com.example.farmae_control.ui.categoria;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farmae_control.R;

public class CategoriaFragment extends Fragment {

    private TextView titulo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_categoria, container, false);

        titulo = v.findViewById(R.id.tituloCategoria);

        String categoria = getArguments().getString("categoria");
        titulo.setText(categoria);

        // TODO: Aquí cargas productos reales desde Firebase por categoría

        return v;
    }
}
