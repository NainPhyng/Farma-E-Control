package com.example.farmae_control.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmae_control.R;
import com.example.farmae_control.ui.productos.Productos;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductosAdminAdapter adapter;
    private ArrayList<Productos> listaProductos;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_admin, container, false);

        db = FirebaseFirestore.getInstance();
        listaProductos = new ArrayList<>();

        // Botón agregar producto
        Button btnAgregar = v.findViewById(R.id.btnAgregarProducto);
        btnAgregar.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_admin_to_agregarProducto)
        );

        // Botón de solicitudes pendientes
        Button btnSolicitudes = v.findViewById(R.id.btnSolicitudesPendientes);
        btnSolicitudes.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_admin_to_aprobacion)
        );

        // RecyclerView de productos
        recyclerView = v.findViewById(R.id.recyclerProductosAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductosAdminAdapter(listaProductos, getContext());
        recyclerView.setAdapter(adapter);

        cargarProductos();

        return v;
    }

    private void cargarProductos() {
        db.collection("productos")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Error al cargar productos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    listaProductos.clear();

                    for (QueryDocumentSnapshot doc : snapshots) {
                        Productos p = new Productos();
                        p.setId(doc.getId());
                        p.setNombre(doc.getString("nombre"));
                        p.setPrecio(doc.getDouble("precio") != null ? doc.getDouble("precio") : 0);
                        p.setStock(doc.getLong("stock") != null ? doc.getLong("stock").intValue() : 0);
                        p.setCategoria(doc.getString("categoria"));
                        listaProductos.add(p);
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarProductos();
    }
}