package com.example.farmae_control.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmae_control.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AprobacionAdapter extends RecyclerView.Adapter<AprobacionAdapter.Holder> {

    private ArrayList<UsuarioPendiente> lista;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AprobacionAdapter(ArrayList<UsuarioPendiente> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_usuario_pendiente, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        UsuarioPendiente u = lista.get(position);

        h.tvNombre.setText(u.getNombre());
        h.tvCorreo.setText(u.getCorreo());
        h.tvTipo.setText("Rol: " + u.getTipo());

        // Si es doctor, mostrar info de cédula
        if ("Doctor".equals(u.getTipo()) && u.getCedula() != null) {
            h.tvCedula.setVisibility(View.VISIBLE);
            h.tvCedula.setText("Cédula: " + u.getCedula() +
                    (u.isCedulaValidada() ? "  Formato válido" : "  No validada"));
        } else {
            h.tvCedula.setVisibility(View.GONE);
        }

        // Boton aprobar
        h.btnAprobar.setOnClickListener(v -> {
            db.collection("usuarios")
                    .document(u.getUid())
                    .update("estado", "aprobado")
                    .addOnSuccessListener(unused ->
                            Toast.makeText(context,
                                    u.getNombre() + " aprobado correctamente",
                                    Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Error al aprobar", Toast.LENGTH_SHORT).show()
                    );
        });

        // Boton rechazar
        h.btnRechazar.setOnClickListener(v -> {
            db.collection("usuarios")
                    .document(u.getUid())
                    .update("estado", "rechazado")
                    .addOnSuccessListener(unused ->
                            Toast.makeText(context,
                                    u.getNombre() + " rechazado",
                                    Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Error al rechazar", Toast.LENGTH_SHORT).show()
                    );
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCorreo, tvTipo, tvCedula;
        Button btnAprobar, btnRechazar;

        public Holder(@NonNull View v) {
            super(v);
            tvNombre   = v.findViewById(R.id.tvNombrePendiente);
            tvCorreo   = v.findViewById(R.id.tvCorreoPendiente);
            tvTipo     = v.findViewById(R.id.tvTipoPendiente);
            tvCedula   = v.findViewById(R.id.tvCedulaPendiente);
            btnAprobar  = v.findViewById(R.id.btnAprobar);
            btnRechazar = v.findViewById(R.id.btnRechazar);
        }
    }
}