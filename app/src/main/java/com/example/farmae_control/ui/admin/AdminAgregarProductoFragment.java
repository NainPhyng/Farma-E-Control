package com.example.farmae_control.ui.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.farmae_control.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminAgregarProductoFragment extends Fragment {

    private EditText etNombre, etPrecio, etStock, etCategoria;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_admin_agregar_producto, container, false);

        db = FirebaseFirestore.getInstance();

        etNombre = v.findViewById(R.id.etNombre);
        etPrecio = v.findViewById(R.id.etPrecio);
        etStock = v.findViewById(R.id.etStock);
        etCategoria = v.findViewById(R.id.etCategoria);

        Button btnGuardar = v.findViewById(R.id.btnGuardarProducto);

        btnGuardar.setOnClickListener(view -> guardarProducto());

        return v;
    }

    private void guardarProducto() {
        String nombre = etNombre.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(categoria)) {
            Toast.makeText(getContext(), "Completa los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(etPrecio.getText().toString());
        int stock = Integer.parseInt(etStock.getText().toString());

        Map<String, Object> producto = new HashMap<>();
        producto.put("nombre", nombre);
        producto.put("precio", precio);
        producto.put("stock", stock);
        producto.put("categoria", categoria);

        db.collection("productos")
                .add(producto)
                .addOnSuccessListener(doc ->
                        Toast.makeText(getContext(), "Producto agregado", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                );
    }
}
