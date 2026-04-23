package com.example.farmae_control.ui.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmae_control.R;
import com.example.farmae_control.ui.productos.Productos;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProductosAdminAdapter
        extends RecyclerView.Adapter<ProductosAdminAdapter.Holder> {

    ArrayList<Productos> lista;
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProductosAdminAdapter(ArrayList<Productos> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.fragment_item_producto_admin, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        Productos p = lista.get(position);

        h.txtNombre.setText(p.getNombre());
        h.txtPrecio.setText("$" + p.getPrecio());
        h.txtCategoria.setText("Categoría: " + p.getCategoria());

        h.btnEliminar.setOnClickListener(v ->
                db.collection("productos")
                        .document(p.getId())
                        .delete()
        );

        h.btnEditar.setOnClickListener(v -> mostrarDialogEditar(p));
    }

    private void mostrarDialogEditar(Productos p) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.dialog_editar_producto, null);

        EditText etNombre = v.findViewById(R.id.etNombre);
        EditText etPrecio = v.findViewById(R.id.etPrecio);
        EditText etStock = v.findViewById(R.id.etStock);
        EditText etCategoria = v.findViewById(R.id.etCategoria);

        etNombre.setText(p.getNombre());
        etPrecio.setText(String.valueOf(p.getPrecio()));
        etStock.setText(String.valueOf(p.getStock()));
        etCategoria.setText(p.getCategoria());

        new AlertDialog.Builder(context)
                .setTitle("Editar producto")
                .setView(v)
                .setPositiveButton("Guardar", (d, which) -> {

                    db.collection("productos")
                            .document(p.getId())
                            .update(
                                    "nombre", etNombre.getText().toString(),
                                    "precio", Double.parseDouble(etPrecio.getText().toString()),
                                    "stock", Integer.parseInt(etStock.getText().toString()),
                                    "categoria", etCategoria.getText().toString()
                            );
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtPrecio, txtCategoria;
        Button btnEditar, btnEliminar;

        public Holder(@NonNull View v) {
            super(v);
            txtNombre = v.findViewById(R.id.txtNombre);
            txtPrecio = v.findViewById(R.id.txtPrecio);
            txtCategoria = v.findViewById(R.id.txtCategoria);
            btnEditar = v.findViewById(R.id.btnEditar);
            btnEliminar = v.findViewById(R.id.btnEliminar);
        }
    }
}
