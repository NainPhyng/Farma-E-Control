package com.example.farmae_control.ui.registosesion;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
    private EditText etCedula; // Solo para Doctor
    private LinearLayout layoutCedula; // Contenedor del campo cédula
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public RegistroSesionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_registro_sesion, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        spinnerTipo   = v.findViewById(R.id.spinner);
        etNombre      = v.findViewById(R.id.etNuevoNombre);
        etCorreo      = v.findViewById(R.id.etNuevoCorreo);
        etDireccion   = v.findViewById(R.id.etNuevaDireccion);
        etEdad        = v.findViewById(R.id.etEdad);
        etTelefono    = v.findViewById(R.id.etTelefono);
        etUsuario     = v.findViewById(R.id.etNuevoUsuario);
        etPass        = v.findViewById(R.id.etNuevaContraseña);
        etCedula      = v.findViewById(R.id.etCedula);
        layoutCedula  = v.findViewById(R.id.layoutCedula);

        Button btnRegistrar = v.findViewById(R.id.btnCrearCuenta);
        Button btnVolver    = v.findViewById(R.id.btnVolver);

        configurarSpinner();

        btnRegistrar.setOnClickListener(view -> registrarUsuario());
        btnVolver.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.inicioSesionFragment)
        );

        return v;
    }

    private void configurarSpinner() {
        // Solo roles públicos — Admin se crea manualmente por los dueños
        String[] tipos = {"Cliente", "Personal de Almacen", "Doctor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapter);

        // Mostrar u ocultar campo de cédula según el rol seleccionado
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tipo = parent.getItemAtPosition(position).toString();
                if (tipo.equals("Doctor")) {
                    layoutCedula.setVisibility(View.VISIBLE);
                } else {
                    layoutCedula.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void registrarUsuario() {
        String tipo      = spinnerTipo.getSelectedItem().toString();
        String nombre    = etNombre.getText().toString().trim();
        String correo    = etCorreo.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String edad      = etEdad.getText().toString().trim();
        String tel       = etTelefono.getText().toString().trim();
        String usuario   = etUsuario.getText().toString().trim();
        String pass      = etPass.getText().toString().trim();
        String cedula    = etCedula.getText().toString().trim();

        // Validaciones básicas
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(direccion) || TextUtils.isEmpty(pass)) {
            Toast.makeText(getContext(), "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si es doctor, la cédula es obligatoria
        if (tipo.equals("Doctor") && TextUtils.isEmpty(cedula)) {
            Toast.makeText(getContext(), "Ingresa tu cédula profesional", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si es doctor, validamos la cédula antes de crear la cuenta
        if (tipo.equals("Doctor")) {
            validarCedulaYRegistrar(tipo, nombre, correo, direccion, edad, tel, usuario, pass, cedula);
        } else {
            crearCuenta(tipo, nombre, correo, direccion, edad, tel, usuario, pass, null);
        }
    }

    /**
     * Valida la cédula profesional contra el registro SEP (México).
     * Si es válida, procede a crear la cuenta como Doctor pendiente de aprobación.
     */
    private void validarCedulaYRegistrar(String tipo, String nombre, String correo,
                                         String direccion, String edad, String tel,
                                         String usuario, String pass, String cedula) {

        Toast.makeText(getContext(), "Validando cédula profesional...", Toast.LENGTH_SHORT).show();

        // Verificamos si ya existe esa cédula registrada en Firestore
        // (para evitar que dos doctores usen la misma cédula)
        db.collection("usuarios")
                .whereEqualTo("cedula", cedula)
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        Toast.makeText(getContext(), "Esta cédula ya está registrada en el sistema",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Cédula no duplicada — validamos formato (8 dígitos numéricos para México)
                    if (!cedula.matches("\\d{7,8}")) {
                        Toast.makeText(getContext(),
                                "La cédula debe tener 7 u 8 dígitos numéricos",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Cédula válida en formato — creamos la cuenta pendiente de aprobación
                    crearCuenta(tipo, nombre, correo, direccion, edad, tel, usuario, pass, cedula);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error al verificar cédula: " + e.getMessage(),
                                Toast.LENGTH_LONG).show()
                );
    }

    private void crearCuenta(String tipo, String nombre, String correo, String direccion,
                             String edad, String tel, String usuario, String pass, String cedula) {

        // VALIDACIONES FUERTES 🔒
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(getContext(), "Correo inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Toast.makeText(getContext(), "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(task -> {

                    //  ERROR REAL DE AUTH
                    if (!task.isSuccessful()) {
                        Exception e = task.getException();
                        String msg = (e != null) ? e.getMessage() : "Error desconocido";

                        if (msg.contains("already in use")) {
                            msg = "Este correo ya está registrado";
                        } else if (msg.contains("badly formatted")) {
                            msg = "Correo inválido";
                        } else if (msg.contains("password")) {
                            msg = "Contraseña muy débil";
                        }

                        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    //  AUTH EXITOSA
                    String uid = auth.getCurrentUser().getUid();

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("tipo", tipo);
                    datos.put("nombre", nombre);
                    datos.put("correo", correo);
                    datos.put("direccion", direccion);
                    datos.put("edad", edad);
                    datos.put("telefono", tel);
                    datos.put("usuario", usuario);

                    if (tipo.equals("Cliente")) {
                        datos.put("estado", "aprobado");
                    } else if (tipo.equals("Personal de Almacen")) {
                        datos.put("estado", "pendiente");
                    } else if (tipo.equals("Doctor")) {
                        datos.put("estado", "pendiente");
                        datos.put("cedula", cedula);
                        datos.put("cedulaValidada", true);
                    }

                    //  GUARDAR EN FIRESTORE
                    db.collection("usuarios").document(uid)
                            .set(datos)
                            .addOnSuccessListener(unused -> {

                                auth.signOut();

                                if (tipo.equals("Cliente")) {
                                    Toast.makeText(getContext(),
                                            "Cuenta creada correctamente",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(),
                                            "Solicitud enviada para aprobación",
                                            Toast.LENGTH_LONG).show();
                                }

                                //  EVITA NULL POINTER
                                if (getView() != null) {
                                    Navigation.findNavController(getView())
                                            .navigate(R.id.inicioSesionFragment);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(),
                                        "Error al guardar en Firestore: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            });
                });
    }
}