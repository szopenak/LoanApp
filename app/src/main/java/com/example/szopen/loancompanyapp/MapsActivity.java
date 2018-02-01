package com.example.szopen.loancompanyapp;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ViewGroup container;
    private Button goBack;
    private TextView selectLocationTV;
    private List<LatLng> polygon = new ArrayList<>();
    private Marker userMarker;
    private String distance;
    private boolean isFree = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        container = findViewById(R.id.transitions_container);
        goBack = findViewById(R.id.go_back_button);
        selectLocationTV = findViewById(R.id.select_location_on_map_tv);

        // Add a marker in Sydney and move the camera
        final LatLng company = new LatLng(51.775439, 19.463628);
        mMap.addMarker(new MarkerOptions().position(company).title("Our company"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(company, 13));
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            }
        });

        polygon.add(new LatLng(51.778606, 19.451957));
        polygon.add(new LatLng(51.780915, 19.477840));
        polygon.add(new LatLng(51.779773, 19.481563));
        polygon.add(new LatLng(51.772172, 19.472855));
        polygon.add(new LatLng(51.770361, 19.453526));
        mMap.addPolygon(new PolygonOptions().add(
           new LatLng(51.778606, 19.451957),
           new LatLng(51.780915, 19.477840),
           new LatLng(51.779773, 19.481563),
           new LatLng(51.772172, 19.472855),
           new LatLng(51.770361, 19.453526)
        ).strokeColor(Color.RED));

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                TransitionManager.beginDelayedTransition(container);
                goBack.setVisibility(View.VISIBLE);

                if(userMarker != null) userMarker.remove();
                userMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("This is Your address")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                float[] results = new float[1];
                Location.distanceBetween(
                        company.latitude,
                        company.longitude,
                        latLng.latitude,
                        latLng.longitude,
                        results);
                distance = String.valueOf(Math.round(results[0]));

                if (PolyUtil.containsLocation(latLng, polygon, true)) {
                    selectLocationTV.setText(getString(R.string.select_location_free_of_charge));
                    isFree = true;
                } else {
                    selectLocationTV.setText(getString(R.string.select_location_distance) + " " + distance + " m");
                    isFree = false;
                }
            }
        });
    }

    public void goBack(View v) {
        Intent intent = new Intent(this, Calculator.class);
        intent.putExtra("DISTANCE_KEY", distance);
        intent.putExtra("IS_FREE", isFree ? "true" : "false");
        startActivity(intent);
    }
}
