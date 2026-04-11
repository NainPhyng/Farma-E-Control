package com.example.farmae_control.ui.cuenta;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmae_control.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CuentaFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private TextView tvUserName, tvUserType, tvNombre, tvCorreo, tvTelefono, tvEdad, tvDireccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_cuenta, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Header
        tvUserName = v.findViewById(R.id.tvUserName);
        tvUserType = v.findViewById(R.id.tvUserType);

        // Datos personales
        tvNombre = v.findViewById(R.id.tvNombre);
        tvCorreo = v.findViewById(R.id.tvCorreo);
        tvTelefono = v.findViewById(R.id.tvTelefono);
        tvEdad = v.findViewById(R.id.tvEdad);
        tvDireccion = v.findViewById(R.id.tvDireccion);

        cargarDatosUsuario();

        // Botón Editar Perfil
        v.findViewById(R.id.btnEditarPerfil).setOnClickListener(btn -> {
            Navigation.findNavController(btn).navigate(R.id.editarCuentaFragment);
        });

        // Botón Cerrar Sesión
        v.findViewById(R.id.btnCerrarSesion).setOnClickListener(btn -> {
            auth.signOut();
            Toast.makeText(getContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        });

        return v;
    }

    private void cargarDatosUsuario() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "No hay sesión activa", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();

        db.collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        Toast.makeText(getContext(), "Datos no encontrados", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Header
                    tvUserName.setText(doc.getString("nombre") != null ? doc.getString("nombre") : "Usuario");
                    tvUserType.setText("Cliente");

                    // Datos del card
                    tvNombre.setText(doc.getString("nombre") != null ? doc.getString("nombre") : "-");
                    tvCorreo.setText(doc.getString("correo") != null ? doc.getString("correo") : "-");
                    tvTelefono.setText(doc.getString("telefono") != null ? doc.getString("telefono") : "-");
                    tvEdad.setText(doc.getString("edad") != null ? doc.getString("edad") : "-");
                    tvDireccion.setText(doc.getString("direccion") != null ? doc.getString("direccion") : "-");

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                });
    }
    @Override
    public void onResume() {
        super.onResume();
        cargarDatosUsuario(); // ← se recargan al volver del fragmento de edición
    }

}
