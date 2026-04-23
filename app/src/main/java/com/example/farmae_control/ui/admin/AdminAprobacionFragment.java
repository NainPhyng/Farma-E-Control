package com.example.farmae_control.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmae_control.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminAprobacionFragment extends Fragment {

    private RecyclerView recyclerView;
    private AprobacionAdapter adapter;
    private ArrayList<UsuarioPendiente> listaPendientes;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_admin_aprobacion, container, false);

        db = FirebaseFirestore.getInstance();
        listaPendientes = new ArrayList<>();

        recyclerView = v.findViewById(R.id.recyclerPendientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AprobacionAdapter(listaPendientes, getContext());
        recyclerView.setAdapter(adapter);

        cargarPendientes();

        return v;
    }

    private void cargarPendientes() {
        // Cargamos todos los usuarios con estado "pendiente"
        db.collection("usuarios")
                .whereEqualTo("estado", "pendiente")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Error al cargar solicitudes", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    listaPendientes.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        UsuarioPendiente u = new UsuarioPendiente();
                        u.setUid(doc.getId());
                        u.setNombre(doc.getString("nombre"));
                        u.setCorreo(doc.getString("correo"));
                        u.setTipo(doc.getString("tipo"));
                        u.setCedula(doc.getString("cedula")); // Solo doctores
                        u.setCedulaValidada(Boolean.TRUE.equals(doc.getBoolean("cedulaValidada")));
                        listaPendientes.add(u);
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}