package com.example.farmae_control.ui.sucursales;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farmae_control.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SucursalesFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLoadedCallback {

    private GoogleMap mMap;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_sucursales, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sucursal1 = new LatLng(19.686557287676205, -99.16358211203583);
        LatLng sucursal2 = new LatLng(19.685719306876152, -99.1563312958929);

        mMap.addMarker(new MarkerOptions().position(sucursal1).title("Sucursal 1"));
        mMap.addMarker(new MarkerOptions().position(sucursal2).title("Sucursal 2"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sucursal1, 14));


        mMap.setOnMapClickListener(this);
        mMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        TextView textView = root.findViewById(R.id.textView);
        if (textView != null)
            textView.setText("Click: " + latLng.latitude + ", " + latLng.longitude);
    }

    @Override
    public void onMapLoaded() {
        // Opcional
    }
}
