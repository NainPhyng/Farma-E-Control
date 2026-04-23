package com.example.farmae_control.ui.almacen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.farmae_control.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AlmacenFragment extends Fragment {

    private TextView tvBienvenida;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_almacen, container, false);

        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        tvBienvenida = v.findViewById(R.id.tvBienvenidaAlmacen);

        cargarNombre();

        // Botón cerrar sesión
        v.findViewById(R.id.btnCerrarSesionAlmacen).setOnClickListener(btn -> {
            auth.signOut();
            requireActivity().finish();
        });

        return v;
    }

    private void cargarNombre() {
        String uid = auth.getCurrentUser().getUid();
        db.collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    String nombre = doc.getString("nombre");
                    tvBienvenida.setText("Bienvenido, " + (nombre != null ? nombre : "Personal de Almacén"));
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
                );
    }
}