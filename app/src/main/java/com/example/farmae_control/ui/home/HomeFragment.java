package com.example.farmae_control.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.farmae_control.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView tvNombre, tvDireccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvNombre = v.findViewById(R.id.tvNombreUsuario);
        tvDireccion = v.findViewById(R.id.tvDireccionUsuario);

        cargarDatosUsuario();
        cargarCategorias(v);
        cargarServicios(v);
        cargarCarrusel(v);

        return v;
    }

    private void cargarDatosUsuario() {
        String uid = auth.getCurrentUser().getUid();

        db.collection("usuarios").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    tvNombre.setText(doc.getString("nombre"));
                    tvDireccion.setText("📍 " + doc.getString("direccion"));
                });
    }

    private void cargarCarrusel(View v) {
        ViewPager2 viewPager = v.findViewById(R.id.viewPager);
        TabLayout tab = v.findViewById(R.id.tabIndicator);

        List<Integer> imagenes = Arrays.asList(
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3
        );

        CarruselAdapter adapter = new CarruselAdapter(imagenes);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tab, viewPager, (tab1, position) -> {}).attach();
    }

    public static class CarruselAdapter extends RecyclerView.Adapter<CarruselAdapter.Holder> {

        private List<Integer> imagenes;

        public CarruselAdapter(List<Integer> imagenes) {
            this.imagenes = imagenes;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ImageView iv = new ImageView(parent.getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            return new Holder(iv);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.iv.setImageResource(imagenes.get(position));
        }

        @Override
        public int getItemCount() {
            return imagenes.size();
        }

        public static class Holder extends RecyclerView.ViewHolder {
            ImageView iv;

            public Holder(@NonNull View itemView) {
                super(itemView);
                iv = (ImageView) itemView;
            }
        }
    }

    private void cargarCategorias(View v) {
        LinearLayout cont = v.findViewById(R.id.categoriesContainer);
        cont.removeAllViews();

        String[] categorias = {"Farmacia", "Belleza", "Vitaminas", "Salud Sexual", "Higiene"};

        Map<String, Integer> categoriasImg = new HashMap<>();
        categoriasImg.put("Farmacia", R.drawable.bg_farmacia);
        categoriasImg.put("Belleza", R.drawable.bg_belleza);
        categoriasImg.put("Vitaminas", R.drawable.bg_vitaminas);
        categoriasImg.put("Salud Sexual", R.drawable.bg_salud_sexual);
        categoriasImg.put("Higiene", R.drawable.bg_higiene);

        for (String c : categorias) {
            TextView tv = new TextView(getContext());
            tv.setText(c);
            tv.setBackgroundResource(categoriasImg.get(c));
            tv.setTextSize(16);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(40, 20, 40, 20);
            tv.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 0, 16, 0);
            tv.setLayoutParams(params);

            tv.setOnClickListener(view -> {
                Bundle b = new Bundle();
                b.putString("categoria", c);
                Navigation.findNavController(view)
                        .navigate(R.id.productosFragment, b);
            });

            cont.addView(tv);
        }
    }

    private void cargarServicios(View v) {
        GridLayout cont = v.findViewById(R.id.servicesContainer);
        cont.removeAllViews();

        String[] servicios = {"Orientación Médica", "Recetas", "Recordatorios", "Membresía"};

        for (String s : servicios) {
            TextView tv = new TextView(getContext());
            tv.setText(s);
            tv.setPadding(24, 24, 24, 24);
            tv.setBackgroundResource(R.drawable.bg_servicio);
            tv.setTextSize(16);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(16, 16, 16, 16);

            tv.setLayoutParams(params);

            tv.setOnClickListener(view -> {
                Bundle b = new Bundle();
                b.putString("categoria", s);

                Navigation.findNavController(view)
                        .navigate(R.id.categoriaFragment, b);
            });

            cont.addView(tv);

        }
    }
}
