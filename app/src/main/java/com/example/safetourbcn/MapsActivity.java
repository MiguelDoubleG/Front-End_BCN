package com.example.safetourbcn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.ArrayList;

public class MapsActivity
        extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {

    private GoogleMap map;
    private final BackEndRequests ber = BackEndRequests.getInstance();
    private AppBarConfiguration mAppBarConfiguration;
    private Session session;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private LocationManager locationManager;
    private AlertDialog dialog;


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ber.updatePlacesList();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            try {
                if (ber.getUserInfo(personEmail) == null) {
                    ber.addUser(personEmail, "Google", personName);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Session currentSession = Session.getInstance();
            currentSession.initGoogle(personName, "Google", personEmail);
        }


        /////NAV///
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.welcome);
        navUsername.setText("Welcome " + session.getInstance().getName());


        View locationButton = findViewById(R.id.fab);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });


        MenuItem filterButton = findViewById(R.id.action_settings);
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
        map = googleMap;
        LatLng bcn = new LatLng(41.385064, 2.173404);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(bcn, 12));

        //show all
        showEstablishments(null, null, null, null, null);

        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (!permissionDenied) enableMyLocation();
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> tl = fusedLocationProviderClient.getLastLocation();
        tl.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                }
            }
        });


        map.setInfoWindowAdapter(new MyInfoWindowAdapter());
    }

    void showEstablishments(String category, Integer distance, Integer price, Float rating, Boolean discount) {
        PlacesList pl = PlacesList.getInstance();
        map.clear();

        for (int i = 0; i < pl.getLength(); ++i) {
            Establishment place = pl.getEstablishment(i);
            boolean bCategory = category == null || (place.getCategory() != null && category.equals(place.getCategory()));
            boolean bDistance = distance == null || (place.getLat() != null && place.getLng() != null && distance >= calcDistance(place));
            boolean bPrice = price == null || (place.getPrice() != null && price.equals(place.getPrice()));
            boolean bRating = rating == null || (place.getRating() != null && rating <= place.getRating());
            boolean bDiscount = discount == null || (place.getDiscount() != null && discount == place.getDiscount());

            if (bCategory && bDiscount && bDistance && bPrice && bRating)
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(place.getLat(), place.getLng()))
                        .title(place.getName()));
        }
    }

    private float calcDistance(Establishment place) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }

        float[] results = new float[3];
        Location.distanceBetween(place.getLat(), place.getLng(), currentLocation.getLatitude(), currentLocation.getLongitude(), results);

        return results[0];
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }


    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    /////////////PERMISSION MANAGEMENT///////////
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.

                    finish();
                    startActivity(getIntent());
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    permissionDenied = true;
                    //TODO: Pedir al user que introduzca una dirección
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }


    //////////NAV//////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showFilterMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void logout(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void goToProfile(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, PerfilUsuarioActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }


    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> tl = fusedLocationProviderClient.getLastLocation();
        tl.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom
                            (new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude()), 15), 1500, null);
                }
            }
        });
    }


    void showFilterMenu () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View viewFilterMenu = inflater.inflate(R.layout.filter_menu, null);
        builder.setView(viewFilterMenu);
        dialog = builder.create();

        Spinner spinner = (Spinner) viewFilterMenu.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_menu_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        viewFilterMenu.findViewById(R.id.reset_filter_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvDistance = (TextView) viewFilterMenu.findViewById(R.id.textNumberDistance);
                tvDistance.setText("20");
            }
        });

        viewFilterMenu.findViewById(R.id.cancel_filter_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        viewFilterMenu.findViewById(R.id.ok_filter_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CATEGORY
                String category = null;
                Spinner siCategory = (Spinner) viewFilterMenu.findViewById(R.id.category_spinner);
                String sCategory = siCategory.getSelectedItem().toString();
                if(!sCategory.equals("All") && !sCategory.equals("Todas")) {
                    category = sCategory;
                }

                // DISTANCE
                Integer distance = null;
                TextView tvDistance = (TextView) viewFilterMenu.findViewById(R.id.textNumberDistance);
                String sDistace = tvDistance.getText().toString();
                if(!sDistace.equals("")) {
                    distance = Integer.parseInt(sDistace) * 1000;
                }

                //PRICE
                Integer price = null;
                RadioGroup rgPrice = (RadioGroup) viewFilterMenu.findViewById(R.id.price_radio_group);
                int idPrice = rgPrice.getCheckedRadioButtonId();
                if(idPrice != -1) {
                    RadioButton checkedPrice = (RadioButton) viewFilterMenu.findViewById(idPrice);
                    String namePrice = checkedPrice.getText().toString();
                    price = (int) namePrice.chars().filter(ch -> ch == '$').count();
                }

                // RATING
                RatingBar rbRating = (RatingBar) viewFilterMenu.findViewById(R.id.rating_bar);
                Float rating = rbRating.getRating();

                //DISCOUNT
                Boolean discount = null;
                CheckBox cbDiscount = (CheckBox) viewFilterMenu.findViewById(R.id.discount_checkbox);
                if(cbDiscount.isChecked()) discount = true;

                showEstablishments(category, distance, price, rating, discount);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    /////////////
    //INFOWINDOW
    //////////////

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            PlacesList pl = PlacesList.getInstance();
            int b = 0;
            String direccion = null;
            String horario = null;
            for (int i = 0; i < pl.getLength() && b == 0; ++i) {
                Establishment place = pl.getEstablishment(i);
                if(place.getName() == marker.getTitle()){
                    b = 1;
                    direccion = place.getAddress();
                    horario = place.getSchedule();
                }

            }

            TextView tvTitle = (TextView)myContentsView.findViewById(R.id.iw_name);
            tvTitle.setText(marker.getTitle());
            TextView adress = ((TextView)myContentsView.findViewById(R.id.iw_adress));
            adress.setText(direccion);
            TextView schedule = ((TextView)myContentsView.findViewById(R.id.iw_horari));
            schedule.setText(horario);



            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
