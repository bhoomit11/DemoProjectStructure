package com.timeout72hours.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.timeout72hours.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by hardip on 16/12/17.
 */

public class DirectionFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    private Context mContext;
    private View view;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
//    protected LocationManager locationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_directions, null);
        init();
        return view;
    }

    private void init() {
        mContext = getActivity();

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMaps) {
        this.googleMap = googleMaps;
        double latitude = 15.591708;
        double longitude = 73.739880;
//        15.591708, 73.739880
//        15.591760, 73.739923
//        15.591750, 73.739837
//        15.591750, 73.739934
//        15.591750, 73.739816
//        15.591729, 73.739945
//        15.591750, 73.740009
//        15.591719, 73.739644
//        15.591678,73.739955
//        15.591618, 73.739765
//        15.591877, 73.73949
//        15.591949, 73.739684
//        15.591639, 73.739824
//        15.591644, 73.739797
//        15.591618, 73.739765

        final LatLng currentPosition = new LatLng(latitude, longitude);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPosition).bearing(-90).zoom(16.3f).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.map_direction_2);
        googleMap.addGroundOverlay(new GroundOverlayOptions()
                .image(image)
                .position(currentPosition, 895));
        //  .position(currentPosition,1026, 706));
        //  .transparency(0.5f));
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);

        //  googleMap.setMinZoomPreference(15.0f);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Write you code here if permission already given.

            googleMap.setMyLocationEnabled(true);
            View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
            //  getLocation();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapFragment != null) {
            // image=null;
            if (googleMap!=null){
                googleMap.clear();
            }

            //  locationManager.removeUpdates(DirectionFragment.this);
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .remove(mapFragment).commit();
//            System.gc();
        }

    }

//    boolean isGPSEnabled = false;
//
//    // Flag for network status
//    boolean isNetworkEnabled = false;
//
//    // Flag for GPS status
//    boolean canGetLocation = false;
//
//    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
//
//    // The minimum time between updates in milliseconds
//    private static final long MIN_TIME_BW_UPDATES = 10000;
//
//    Location location=null; // Location
//    double latitude; // Latitude
//    double longitude;

//    public void getLocation() {
//
//        try {
//            locationManager = (LocationManager) mContext
//                    .getSystemService(LOCATION_SERVICE);
//
//            // Getting GPS status
//            isGPSEnabled = locationManager
//                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            // Getting network status
//            isNetworkEnabled = locationManager
//                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                // No network provider is enabled
//            } else {
//                this.canGetLocation = true;
//                if (isNetworkEnabled) {
//                    locationManager.requestLocationUpdates(
//                            LocationManager.NETWORK_PROVIDER,
//                            MIN_TIME_BW_UPDATES,
//                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    Log.d("Network", "Network");
//                    if (locationManager != null) {
//                        location = locationManager
//                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                        }
//                    }
//                }
//                // If GPS enabled, get latitude/longitude using GPS Services
//                if (isGPSEnabled) {
//                    if (location == null) {
//                        locationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.d("GPS Enabled", "GPS Enabled");
//                        if (locationManager != null) {
//                            location = locationManager
//                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if (location != null) {
//                                latitude = location.getLatitude();
//                                longitude = location.getLongitude();
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        setmarker();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//          this.location = location;
//        setmarker();
//    }
//
//    private void setmarker(){
//
//        if (marker!=null){
//            marker.remove();
//        }
//
//        if (location!=null && canGetLocation){
//            googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLatitude())));
//        }
//
//    }
//
//    private Marker marker;
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
}
