package com.example.ubercaranimationjava;

import androidx.fragment.app.FragmentActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.ubercaranimationjava.util.AnimationUtils;
import com.example.ubercaranimationjava.util.MapUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng defaultLocation;
    private Marker originMarker = null;
    private Marker destinationMarker = null;
    private Polyline grayPolyline =null;
    private Polyline blackPolyline= null;
    private Marker movingCabMarker= null;
    private LatLng previousLatLng = null;
    private LatLng currentLatLng= null;
    private Handler handler  = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void moveCamera(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    private  void animateCamera(LatLng latLng) {
        CameraPosition cameraPosition = (new CameraPosition.Builder()).target(latLng).zoom(15.5F).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private  Marker addCarMarkerAndGet(LatLng latLng) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(MapUtils.getCarBitmap((Context)this));
        GoogleMap var10000 = this.mMap;
        return mMap.addMarker( new MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor));
    }

    private  Marker addOriginDestinationMarkerAndGet(LatLng latLng) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(MapUtils.getOriginDestinationMarkerBitmap());
        return mMap.addMarker(
                (new MarkerOptions()).position(latLng).flat(true).icon(bitmapDescriptor)
        );
    }

    private void showDefaultLocationOnMap(LatLng latLng) {
        this.moveCamera(latLng);
        this.animateCamera(latLng);
    }

    private void showPath(ArrayList<LatLng> latLngList) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(LatLng latLng : latLngList){
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,2));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.GRAY);
        polylineOptions.width(5f);
        polylineOptions.addAll(latLngList);
        this.grayPolyline = mMap.addPolyline(polylineOptions);

        PolylineOptions blackPolylineOptions = new PolylineOptions();
        blackPolylineOptions.color(Color.BLACK);
        blackPolylineOptions.width(5f);
        this.blackPolyline = mMap.addPolyline(blackPolylineOptions);

        this.originMarker = this.addOriginDestinationMarkerAndGet((LatLng) latLngList.get(0));
        if(originMarker!= null)
            originMarker.setAnchor(0.5f,0.5f);
        destinationMarker = addOriginDestinationMarkerAndGet(latLngList.get(latLngList.size() - 1));
        if(destinationMarker != null)
            destinationMarker.setAnchor(0.5f,0.5f);

        ValueAnimator polylineAnimator = AnimationUtils.polylineAnimator();
        polylineAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)(new ValueAnimator.AnimatorUpdateListener() {
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                int percentValue = (int) valueAnimator.getAnimatedValue();
                int index = 0;
                if(grayPolyline != null) {
                    index = (int) (grayPolyline.getPoints().size() * (int) (percentValue / 100.0f));
                }
                if(blackPolyline != null)
                    blackPolyline.setPoints(grayPolyline.getPoints().subList(0,index));
            }
        }));
        polylineAnimator.start();
    }
    private final void updateCarLocation(LatLng latLng) {
        if (movingCabMarker == null) {
            movingCabMarker = this.addCarMarkerAndGet(latLng);
        }
        if (previousLatLng == null) {
            currentLatLng = latLng;
            previousLatLng = currentLatLng;
            if (movingCabMarker != null) {
                movingCabMarker.setPosition(this.currentLatLng);
                movingCabMarker.setAnchor(0.5f,0.5f);
            }
            animateCamera(currentLatLng);
        } else {
            previousLatLng = currentLatLng;
            this.currentLatLng = latLng;
            ValueAnimator valueAnimator = AnimationUtils.carAnimator();
            valueAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)(new ValueAnimator.AnimatorUpdateListener() {
                public final void onAnimationUpdate(ValueAnimator va) {
                    if (currentLatLng != null && previousLatLng != null) {
                        float multiplier = va.getAnimatedFraction();

                        LatLng nextLocation = new LatLng(
                                multiplier * currentLatLng.latitude + (1-multiplier)*previousLatLng.latitude,
                                multiplier * currentLatLng.longitude + (1-multiplier)*previousLatLng.longitude);
                        Marker var10000 = MapsActivity.this.movingCabMarker;
                        if (movingCabMarker != null)
                            movingCabMarker.setPosition(nextLocation);
                        float rotation = MapUtils.getRotation(previousLatLng,nextLocation);
                        if (!Float.isNaN(rotation)) {
                            if (movingCabMarker != null)
                                movingCabMarker.setRotation(rotation);
                        }
                        if (movingCabMarker != null)
                            movingCabMarker.setAnchor(0.5f,0.5f);
                        animateCamera(nextLocation);

                    }
                }
            }));
            valueAnimator.start();
        }
    }
    private  void showMovingCab(final ArrayList<LatLng> cabLatLngList) {
        this.handler = new Handler();
        this.runnable = (Runnable)(new Runnable() {
            int index = 0;
            public final void run() {
                if (index < 10) {
                    updateCarLocation(cabLatLngList.get(index));
                    handler.postDelayed(runnable,3000);
                    ++index;
                } else {
                    handler.removeCallbacks(runnable);
                    Toast.makeText(MapsActivity.this, "Trip Ends", Toast.LENGTH_LONG).show();
                }

            }
        });
        handler.postDelayed(runnable,5000);
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
        defaultLocation = new LatLng(28.435350000000003, 77.11368);
        showDefaultLocationOnMap(defaultLocation);
        // Add a marker in Sydney and move the camera
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Write whatever to want to do after delay specified (1 sec)
                Log.d("Handler", "Running Handler");
                showPath(MapUtils.getListOfLocations());
                showMovingCab(MapUtils.getListOfLocations());
            }
        }, 3000);
    }
}