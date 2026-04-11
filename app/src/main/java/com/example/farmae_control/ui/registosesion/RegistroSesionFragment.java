package com.example.farmae_control.ui.registosesion;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.farmae_control.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroSesionFragment extends Fragment {

    private Spinner spinnerTipo;
    private EditText etNombre, etCorreo, etDireccion, etEdad, etTelefono, etUsuario, etPass;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public RegistroSesionFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_registro_sesion, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        spinnerTipo = v.findViewById(R.id.spinner);
        etNombre = v.findViewById(R.id.etNuevoNombre);
        etCorreo = v.findViewById(R.id.etNuevoCorreo);
        etDireccion = v.findViewById(R.id.etNuevaDireccion);
        etEdad = v.findViewById(R.id.etEdad);
        etTelefono = v.findViewById(R.id.etTelefono);
        etUsuario = v.findViewById(R.id.etNuevoUsuario);
        etPass = v.findViewById(R.id.etNuevaContraseña);

        Button btnRegistrar = v.findViewById(R.id.btnCrearCuenta);
        Button btnVolver = v.findViewById(R.id.btnVolver);

        configurarSpinner();

        btnRegistrar.setOnClickListener(view -> registrarUsuario());
        btnVolver.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.inicioSesionFragment)
        );

        return v;
    }

    private void configurarSpinner() {
        String[] tipos = {"Cliente", "Administrador", "Personal de Almacen","Doctor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);
    }

    private void registrarUsuario() {
        String tipo = spinnerTipo.getSelectedItem().toString();
        String nombre = etNombre.getText().toString();
        String correo = etCorreo.getText().toString();
        String direccion = etDireccion.getText().toString();
        String edad = etEdad.getText().toString();
        String tel = etTelefono.getText().toString();
        String usuario = etUsuario.getText().toString();
        String pass = etPass.getText().toString();

        // Validamos las cosas
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(direccion) || TextUtils.isEmpty(pass)) {

            Toast.makeText(getContext(), "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(correo, pass)
                .addOnSuccessListener(result -> {

                    String uid = auth.getCurrentUser().getUid();

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("tipo", tipo);
                    datos.put("nombre", nombre);
                    datos.put("correo", correo);
                    datos.put("direccion", direccion);
                    datos.put("edad", edad);
                    datos.put("telefono", tel);
                    datos.put("usuario", usuario);

                    db.collection("usuarios").document(uid)
                            .set(datos)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(getContext(), "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getView())
                                        .navigate(R.id.inicioSesionFragment);
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_LONG).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error en autenticación: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
