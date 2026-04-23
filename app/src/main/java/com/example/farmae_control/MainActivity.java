package com.example.farmae_control;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.farmae_control.ui.admin.AdminActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // verificamos el rol
            verificarRolYNavegar();
        } else {
            // mostrar login
            navView.setVisibility(View.GONE);
            navController.navigate(R.id.inicioSesionFragment);
        }

        // Ocultar bottom nav en login y registro
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.inicioSesionFragment ||
                    destination.getId() == R.id.registroSesionFragment) {
                navView.setVisibility(View.GONE);
            } else {
                navView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void verificarRolYNavegar() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        // Documento no encontrado y mandamos al login XD
                        navView.setVisibility(View.GONE);
                        navController.navigate(R.id.inicioSesionFragment);
                        return;
                    }

                    String tipo = doc.getString("tipo");

                    if (tipo == null) {
                        navView.setVisibility(View.GONE);
                        navController.navigate(R.id.inicioSesionFragment);
                        return;
                    }

                    switch (tipo) {
                        case "Administrador":
                            // Va directo a AdminActivity
                            startActivity(new Intent(this, AdminActivity.class));
                            finish();
                            break;

                        default:
                            // Cliente, Personal de Almacen, Doctor a pantalla principal o inicio nose como quieran llamarle
                            mostrarNavegacionPrincipal();
                            break;
                    }
                })
                .addOnFailureListener(e -> {
                    navView.setVisibility(View.GONE);
                    navController.navigate(R.id.inicioSesionFragment);
                });
    }

    public void irAPantallaPrincipal() {
        mostrarNavegacionPrincipal();
    }

    private void mostrarNavegacionPrincipal() {
        navView.setVisibility(View.VISIBLE);
        NavigationUI.setupWithNavController(navView, navController);
        navController.navigate(R.id.navegacionInicio);
    }
}