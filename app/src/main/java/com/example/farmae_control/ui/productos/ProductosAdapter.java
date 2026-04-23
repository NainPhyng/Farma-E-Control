package com.example.farmae_control.ui.productos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmae_control.R;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.Holder> {

    private List<Productos> lista;

    public ProductosAdapter(List<Productos> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_producto, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int i) {
        Productos p = lista.get(i);
        h.nombre.setText(p.getNombre());
        h.precio.setText("$" + p.getPrecio());
        h.stock.setText("Stock: " + p.getStock());
        h.categoria.setText("Categoría: " + p.getCategoria());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView nombre, precio, stock, categoria;

        public Holder(@NonNull View v) {
            super(v);
            nombre = v.findViewById(R.id.txtNombreProducto);
            precio = v.findViewById(R.id.txtPrecioProducto);
            stock = v.findViewById(R.id.txtStockProducto);
            categoria = v.findViewById(R.id.txtCategoriaProducto);
        }
    }
}
