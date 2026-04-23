package com.example.farmae_control.ui.productos;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farmae_control.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductosFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recycler;
    private ProductosAdapter adapter;
    private List<Productos> lista;
    private TextView titulo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_productos, container, false);

        db = FirebaseFirestore.getInstance();
        recycler = v.findViewById(R.id.recyclerProductos);
        titulo = v.findViewById(R.id.txtTituloProductos);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        lista = new ArrayList<>();
        adapter = new ProductosAdapter(lista);
        recycler.setAdapter(adapter);

        String categoria = getArguments() != null
                ? getArguments().getString("categoria")
                : null;

        if (categoria == null) {
            titulo.setText("Todos los productos");
            cargarTodos();
            return v;
        }
        titulo.setText(categoria);

        cargarProductos(categoria);

        return v;
    }

    private void cargarProductos(String categoria) {
        db.collection("productos")
                .whereEqualTo("categoria", categoria)
                .get()
                .addOnSuccessListener(query -> {
                    lista.clear();
                    for (var doc : query) {
                        Productos p = new Productos(
                                doc.getId(),
                                doc.getString("nombre"),
                                doc.getDouble("precio"),
                                doc.getLong("stock").intValue(),
                                doc.getString("categoria")
                        );
                        lista.add(p);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void cargarTodos() {
        db.collection("productos")
                .get()
                .addOnSuccessListener(query -> {
                    lista.clear();
                    for (var doc : query) {
                        Productos p = new Productos(
                                doc.getId(),
                                doc.getString("nombre"),
                                doc.getDouble("precio"),
                                doc.getLong("stock").intValue(),
                                doc.getString("categoria")
                        );
                        lista.add(p);
                    }
                    adapter.notifyDataSetChanged();
                });
    }


}
