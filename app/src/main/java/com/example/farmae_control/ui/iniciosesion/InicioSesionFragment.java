package com.example.farmae_control.ui.iniciosesion;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.farmae_control.R;
import com.example.farmae_control.ui.admin.AdminActivity;
import com.example.farmae_control.ui.almacen.AlmacenActivity;
import com.example.farmae_control.ui.doctor.DoctorActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class InicioSesionFragment extends Fragment {

    private EditText etUser, etPass;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public InicioSesionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);

        auth = FirebaseAuth.getInstance();
        db   = FirebaseFirestore.getInstance();

        etUser = view.findViewById(R.id.etUser);
        etPass = view.findViewById(R.id.etPassword);

        Button btnLogin     = view.findViewById(R.id.btnLogin);
        Button btnIrRegistro = view.findViewById(R.id.btnIrRegistro);

        btnLogin.setOnClickListener(v -> login());
        btnIrRegistro.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.registroSesionFragment)
        );

        return view;
    }

    private void login() {
        String correo = etUser.getText().toString().trim();
        String pass   = etPass.getText().toString().trim();

        if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(pass)) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(correo, pass)
                .addOnSuccessListener(authResult -> {
                    String uid = auth.getCurrentUser().getUid();

                    db.collection("usuarios")
                            .document(uid)
                            .get()
                            .addOnSuccessListener(doc -> {

                                if (!doc.exists()) {
                                    Toast.makeText(getContext(),
                                            "Usuario no registrado correctamente",
                                            Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    return;
                                }

                                String tipo   = doc.getString("tipo");
                                String estado = doc.getString("estado");

                                if (tipo == null) {
                                    Toast.makeText(getContext(), "Rol no definido", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    return;
                                }

                                // ✅ Verificar estado de aprobación para roles que lo requieren
                                if ((tipo.equals("Personal de Almacen") || tipo.equals("Doctor"))
                                        && !"aprobado".equals(estado)) {

                                    auth.signOut();

                                    if ("rechazado".equals(estado)) {
                                        Toast.makeText(getContext(),
                                                "Tu solicitud fue rechazada. Contacta al administrador.",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getContext(),
                                                "Tu cuenta está pendiente de aprobación por un administrador.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    return;
                                }

                                // Redirigir según el rol
                                switch (tipo) {
                                    case "Administrador":
                                        startActivity(new Intent(requireContext(), AdminActivity.class));
                                        requireActivity().finish();
                                        break;

                                    case "Personal de Almacen":
                                        startActivity(new Intent(requireContext(), AlmacenActivity.class));
                                        requireActivity().finish();
                                        break;

                                    case "Doctor":
                                        startActivity(new Intent(requireContext(), DoctorActivity.class));
                                        requireActivity().finish();
                                        break;

                                    default: // Cliente
                                        Navigation.findNavController(requireView())
                                                .navigate(R.id.navegacionInicio);
                                        break;
                                }
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(),
                                            "Error al leer rol: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show()
                            );
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
    }
}