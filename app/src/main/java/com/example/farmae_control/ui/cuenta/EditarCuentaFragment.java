package com.example.farmae_control.ui.cuenta;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmae_control.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarCuentaFragment extends Fragment {

    private EditText etNombre, etTelefono, etEdad, etDireccion;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_editar_cuenta, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etNombre = v.findViewById(R.id.etNombre);
        etTelefono = v.findViewById(R.id.etTelefono);
        etEdad = v.findViewById(R.id.etEdad);
        etDireccion = v.findViewById(R.id.etDireccion);

        cargarDatosActuales();

        v.findViewById(R.id.btnGuardarCambios).setOnClickListener(btn -> {
            guardarCambios(btn);
        });

        return v;
    }

    private void cargarDatosActuales() {
        String uid = auth.getCurrentUser().getUid();

        db.collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    etNombre.setText(doc.getString("nombre"));
                    etTelefono.setText(doc.getString("telefono"));
                    etEdad.setText(doc.getString("edad"));
                    etDireccion.setText(doc.getString("direccion"));
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show());
    }

    private void guardarCambios(View btn) {
        String nombre = etNombre.getText().toString();
        String telefono = etTelefono.getText().toString();
        String edad = etEdad.getText().toString();
        String direccion = etDireccion.getText().toString();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(telefono)
                || TextUtils.isEmpty(edad) || TextUtils.isEmpty(direccion)) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = auth.getCurrentUser().getUid();

        Map<String, Object> datos = new HashMap<>();
        datos.put("nombre", nombre);
        datos.put("telefono", telefono);
        datos.put("edad", edad);
        datos.put("direccion", direccion);

        db.collection("usuarios").document(uid)
                .update(datos)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Datos actualizados", Toast.LENGTH_SHORT).show();

                    Navigation.findNavController(btn).navigate(R.id.navegacionInicio);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show());
    }
}
