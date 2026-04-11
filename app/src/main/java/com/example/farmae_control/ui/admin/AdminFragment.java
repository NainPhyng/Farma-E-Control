package com.example.farmae_control.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.farmae_control.R;

public class AdminFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_admin, container, false);

        Button btnAgregar = v.findViewById(R.id.btnAgregarProducto);

        btnAgregar.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.adminAgregarProductoFragment)
        );

        return v;
    }
}
