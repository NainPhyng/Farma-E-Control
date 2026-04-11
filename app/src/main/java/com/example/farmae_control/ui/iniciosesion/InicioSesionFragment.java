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

import com.example.farmae_control.ui.admin.AdminActivity;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.farmae_control.R;
import com.google.firebase.auth.FirebaseAuth;

public class InicioSesionFragment extends Fragment {

    private EditText etUser, etPass;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public InicioSesionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();


        View view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);

        auth = FirebaseAuth.getInstance();
        etUser = view.findViewById(R.id.etUser);
        etPass = view.findViewById(R.id.etPassword);

        Button btnLogin = view.findViewById(R.id.btnLogin);
        Button btnIrRegistro = view.findViewById(R.id.btnIrRegistro);

        btnLogin.setOnClickListener(v -> login());
        btnIrRegistro.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.registroSesionFragment)
        );

        return view;
    }

    private void login() {
        String correo = etUser.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

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
                                    Toast.makeText(getContext(), "Usuario no registrado correctamente", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String tipo = doc.getString("tipo");

                                if (tipo == null) {
                                    Toast.makeText(getContext(), "Rol no definido", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (tipo.equals("Administrador")) {

                                    Intent intent = new Intent(requireContext(), AdminActivity.class);
                                    startActivity(intent);
                                    requireActivity().finish();

                                } else {

                                    Navigation.findNavController(requireView())
                                            .navigate(R.id.navegacionInicio);

                                }




                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Error al leer rol", Toast.LENGTH_SHORT).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }


}
