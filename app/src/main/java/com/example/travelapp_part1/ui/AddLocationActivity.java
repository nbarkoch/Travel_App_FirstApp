package com.example.travelapp_part1.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.travelapp_part1.entities.UserLocation;
import com.example.travelapp_part1.R;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        MyRecyclerViewAdapter.ItemClickListener,
        MyRecyclerViewAdapter.ItemLongClickListener {

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    Button searchButton;
    Button addSrcLocationButton;
    Button addDstLocationButton;
    Button doneButton;
    EditText searchEditText;
    TextView locationTv;
    LatLng latLng;
    LatLng defaultLocation = new LatLng(31.7650471,35.1911197);
    GoogleMap map;

    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> userLocationsView;
    ArrayList<UserLocation> userLocations;
    ArrayList<Marker> markers;
    int position;
    float[] markerColors = {
            BitmapDescriptorFactory.HUE_AZURE,
            BitmapDescriptorFactory.HUE_RED,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_MAGENTA,
            BitmapDescriptorFactory.HUE_ORANGE,
            BitmapDescriptorFactory.HUE_BLUE,
            BitmapDescriptorFactory.HUE_ROSE,
            BitmapDescriptorFactory.HUE_VIOLET,
            BitmapDescriptorFactory.HUE_CYAN,
            BitmapDescriptorFactory.HUE_YELLOW
    };
    final int SIZE_OF_COLOR_ARRAY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Add Locations");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        initGoogleMap(savedInstanceState);

        locationTv = (TextView) findViewById(R.id.latlongLocation);
        addSrcLocationButton = (Button) findViewById(R.id.addSourceLocationButton);
        addDstLocationButton = (Button) findViewById(R.id.addTargetLocationButton);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchButton = (Button) findViewById(R.id.searchButton);

        doneButton = (Button) findViewById(R.id.IdDoneButton);
        // data to populate the RecyclerView with
        userLocationsView = new ArrayList<String>();
        userLocations = new ArrayList<UserLocation>();
        markers = new ArrayList<Marker>();
        // set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new MyRecyclerViewAdapter(this, userLocationsView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // listeners
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.user_list_map);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    public void searchButton_onClick(View view) {
        String stringAddress = searchEditText.getText().toString();
        Geocoder geocoder = new Geocoder(getBaseContext());
        List<Address> addresses = null;
        try {
            // Getting a maximum of 3 Address that matches the input
            // text
            addresses = geocoder.getFromLocationName(stringAddress, 3);
            if (addresses != null && !addresses.equals(""))
                search(addresses);
        } catch (Exception e) {

        }
    }

    /**
     * Function that should get a single address and
     * set the global value latLong, the current marker and the camera to that location
     * @param addresses
     */
    protected void search(List<Address> addresses) {
        Address address = (Address) addresses.get(0);
        latLng = new LatLng(address.getLatitude(), address.getLongitude());
        ChangeMarkerPosition();
        ChangeLocationView(latLng.latitude, latLng.longitude);
    }


    /**
     * method which save the current state of map
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    /**
     * Life cycle methods - also activate lifecycle methods of map
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    /**
     * when the map first created it activate this method,
     * this method set a listener for click and drag markers in map,
     * also sets a default location for camera and the global latlng
     * @param googleMap instance of the map, which we save it in a global variable.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        addDstLocationButton.setEnabled(false);
        doneButton.setEnabled(false);
        latLng = defaultLocation;
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        // Set a listener for marker click.
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                position = markers.indexOf(m);
                setNewLatLng(position);
                ChangeMarkerPosition();
                markers.get(position).showInfoWindow();
                return true;
            }
        });
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                position = markers.indexOf(marker);
                //ChangeMarkerPosition();
                markers.get(position).showInfoWindow();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                ChangeLocationView(marker.getPosition().latitude,marker.getPosition().longitude);
            }
        });
    }

    /**
     * function that get a latitude and longtitude and set them in the focused
     * item of the userLocation list of view
     * @param latitude
     * @param longitude
     */
    void ChangeLocationView(double latitude, double longitude){
        UserLocation location = new UserLocation(latitude, longitude);
        userLocations.set(position, location);
        if (position == 0)
            userLocationsView.set(position, "Source Location: " + getPlace(location));
        else
            userLocationsView.set(position, "Target Location: " + getPlace(location));
        adapter.notifyDataSetChanged();
        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
    }

    /**
     * function that accesses the marker by index (position) and changes its position according
     * to the value of the global variable LatLng.
     */
    void ChangeMarkerPosition(){
        MarkerOptions markerOptions = new MarkerOptions();
        Marker marker = markers.get(position);
        Marker newMarker = map.addMarker(markerOptions.position(latLng).title(userLocationsView.get(position)).draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(markerColors[position%SIZE_OF_COLOR_ARRAY])));
        marker.remove();
        markers.set(position,newMarker);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * function that set to the global latLng, a latLng value of item in index "position"
     * @param position the index of the item which we want to put it's latLng value to the global latLng
     */
    void setNewLatLng(int position){
        UserLocation userLocation =  userLocations.get(position);
        this.position = position;
        latLng = new LatLng(userLocation.getLat(),userLocation.getLon());
    }


    /**
     * method that activate by click on one of the item in cycle view
     * @param view which view call that method
     * @param position the current item that have been clicked
     */
    @Override
    public void onItemClick(View view, int position) {
        setNewLatLng(position);
        ChangeMarkerPosition();
        markers.get(position).showInfoWindow();
    }

    /**
     * method that activate by long click on one of the item in cycle view
     * it will try to delete the selected item
     * @param view which view call that method
     * @param position the current item that have been clicked
     */
    @Override
    public void onItemLongClick(View view, int position) {
        if (position != 0){
            new AlertDialog.Builder(AddLocationActivity.this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Delete target location")
                    .setMessage("Do you want to remove this location?")
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            userLocationsView.remove(position);
                            userLocations.remove(position);
                            DoneButtonCheck();
                            Marker marker = markers.get(position);
                            markers.remove(position);
                            marker.remove();
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("No", null).
                    show();
        }
    }

    public void addNewSrcLocation(View view) {
        addNewLocation(view,"Source Location: ");
        addSrcLocationButton.setEnabled(false);
        addDstLocationButton.setEnabled(true);
    }

    public void addNewDstLocation(View view) {
        addNewLocation(view,"Target Location: ");
    }


    public void addNewLocation(View view, String locationType) {
        // first we take the current size, which is the new item's index
        int position = userLocationsView.size();
        latLng = defaultLocation;
        UserLocation location = new UserLocation(latLng.latitude, latLng.longitude);
        userLocationsView.add(locationType + getPlace(location));
        userLocations.add(location);
        markers.add(
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(markerColors[position%SIZE_OF_COLOR_ARRAY])))
        );
        onItemClick(view, position); // let's go to the default location
        adapter.notifyDataSetChanged(); // and notify the adapter for our new item in it
        DoneButtonCheck();
    }

    /**
     * function that saves the values of the list of our user locations for the caller activity
     * @param view is the button "done"
     */
    public void addLocations_onClick(View view) {
        Intent newIntent = new Intent();
        UserLocation sourceLocation = userLocations.remove(0);
        newIntent.putExtra("SRC_LOC", sourceLocation);
        newIntent.putParcelableArrayListExtra("LIST_DST",userLocations);
        newIntent.putStringArrayListExtra("LIST_VIEW", userLocationsView);
        setResult(Activity.RESULT_OK, newIntent);
        finish();
    }


    /**
     * function that set the "done" button to be enabled only if there are at least two instances of locations;
     * one source and the rest targets.
     */
    public void DoneButtonCheck() {
        doneButton.setEnabled(userLocations.size() >= 2);
    }


    /**
     * function that receive a lat-lon location and translates it to understandable address
     * @param location UserLocation instant
     * @return string address
     */
    public String getPlace(UserLocation location) {
        Geocoder geocoder = new Geocoder(this.getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLat(), location.getLon(), 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0);
            }
            return "unknown place: \n ("+location.getLat()+", "+location.getLon()+")";
        }
        catch(
                IOException e)
        {
            e.printStackTrace();
        }
        return "IOException ...";
    }

}