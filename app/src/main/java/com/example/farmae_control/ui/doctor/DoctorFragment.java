package com.example.farmae_control.ui.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.farmae_control.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorFragment extends Fragment {

    private TextView tvBienvenida, tvCedula;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_doctor, container, false);

        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        tvBienvenida = v.findViewById(R.id.tvBienvenidaDoctor);
        tvCedula     = v.findViewById(R.id.tvCedulaDoctor);

        cargarDatos();

        v.findViewById(R.id.btnCerrarSesionDoctor).setOnClickListener(btn -> {
            auth.signOut();
            requireActivity().finish();
        });

        return v;
    }

    private void cargarDatos() {
        String uid = auth.getCurrentUser().getUid();
        db.collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    String nombre = doc.getString("nombre");
                    String cedula = doc.getString("cedula");
                    tvBienvenida.setText("Dr. " + (nombre != null ? nombre : "Doctor"));
                    tvCedula.setText("Cédula: " + (cedula != null ? cedula : "-"));
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
                );
    }
}