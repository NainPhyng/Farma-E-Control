package com.example.farmae_control;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Verificar si hay usuario autenticado
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Usuario autenticado
            mostrarNavegacionPrincipal();
        } else {
            // No autenticado
            navView.setVisibility(View.GONE);
            navController.navigate(R.id.inicioSesionFragment);
        }

        // Listener para ocultar/mostrar bottom nav
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.inicioSesionFragment ||
                    destination.getId() == R.id.registroSesionFragment) {
                navView.setVisibility(View.GONE);
            } else {
                navView.setVisibility(View.VISIBLE);
            }
        });
    }

    // Método público para ir a la pantalla principal después del login
    public void irAPantallaPrincipal() {
        mostrarNavegacionPrincipal();
    }

    private void mostrarNavegacionPrincipal() {
        navView.setVisibility(View.VISIBLE);
        NavigationUI.setupWithNavController(navView, navController);
        navController.navigate(R.id.navegacionInicio);
    }
}