package com.example.adam_.tunt;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private boolean SRC;
    private boolean DST;

    public LatLng SRCP;
    public LatLng DSTP;

    private Marker SRCM;
    private Marker DSTM;

    private Polyline LINE;

    Button B ;

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
     * we just add a marker near Tunis, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        SRC = true;
        DST = false;

        DSTP = null;
        SRCP = null;

        LINE = null;

        // Add a marker in Tunis and move the camera
        LatLng Tunis = new LatLng(36.809182, 10.185);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Tunis));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(Tunis)
                .zoom(13)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                clear();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(SRC || DST ){

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.pos));
                    Marker X= mMap.addMarker(markerOptions);

                    if(SRC){
                        SRCM = X ;
                        SRCP = new LatLng(latLng.latitude,latLng.longitude);
                        SRC=false;
                        DST=true;
                    } else if (DST){
                        DSTM = X;
                        DSTP = new LatLng(latLng.latitude,latLng.longitude);
                        DST=false;
                    }


                    String serverKey = "AIzaSyBRW2XPYj-NBSrhlOY_uc64O5WtD3LxdtU";

                    Log.v("TESTTEST", SRCP+"");
                    Log.v("TESTTEST", SRC+"");
                    Log.v("TESTTEST", SRC+"");

                    Log.v("TESTTEST", DSTP+"");
                    Log.v("TESTTEST", DST+"");
                    Log.v("TESTTEST", DST+"");


                    if( DSTP != null && SRCP != null)
                    GoogleDirection.withServerKey(serverKey)
                            .from(SRCP)
                            .to(DSTP)
                            .execute(new DirectionCallback() {
                                @Override
                                public void onDirectionSuccess(Direction direction, String rawBody) {
                                    if ( direction.isOK() ) {
                                        Route route = direction.getRouteList().get(0);
                                        Leg leg = route.getLegList().get(0);
                                        ArrayList<LatLng> pointList = leg.getDirectionPoint();
                                        PolylineOptions polylineOptions = DirectionConverter.createPolyline(getBaseContext(), pointList, 5, Color.RED);
                                        Polyline X = mMap.addPolyline(polylineOptions);
                                        LINE = X;
                                        Toast.makeText(getBaseContext(),"Appuyez longtemps pour refaire une recherce",Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.v("TESTTEST", " SHIT NOT OK");
                                    }
                                }

                                @Override
                                public void onDirectionFailure(Throwable t) {
                                    Log.v("TESTTEST", " FAILED");
                                }
                            });



                }



            }
        });

    }

    public void clear(){


        SRC = true;
        DST = false;

        DSTP = null;
        SRCP = null;

        LINE.remove();

        SRCM.remove();
        DSTM.remove();

    }

}
