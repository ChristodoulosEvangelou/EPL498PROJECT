package com.example.project498;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class AvailableShopsFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_shops, container, false);

        // Shop buttons
        Button showPlaceButton1 = view.findViewById(R.id.show_place_button1);
        Button showPlaceButton2 = view.findViewById(R.id.show_place_button2);
        Button showPlaceButton3 = view.findViewById(R.id.show_place_button3);
        Button viewWeatherButton = view.findViewById(R.id.view_weather_button); // Προσθήκη για το κουμπί View Weather

        showPlaceButton1.setOnClickListener(v -> checkAndShowPlace("Nicosia Mall", 35.1345, 33.2797));
        showPlaceButton2.setOnClickListener(v -> checkAndShowPlace("Prive Sports", 35.135767, 33.293250));
        showPlaceButton3.setOnClickListener(v -> checkAndShowPlace("Sports Spirits", 35.125556, 33.292745));

        // Προσθήκη κώδικα για το κουμπί View Weather
        viewWeatherButton.setOnClickListener(v -> {
            // Να γίνει πλοήγηση στο ApiCallFragment
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_AvailableShopsFragment_to_ApiCallFragment);
        });

        return view;
    }

    private String shopName;
    private double latitude;
    private double longitude;

    private void checkAndShowPlace(String shopName, double latitude, double longitude) {
        this.shopName = shopName;
        this.latitude = latitude;
        this.longitude = longitude;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Ζητάμε την άδεια αν δεν υπάρχει
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            // Αν η άδεια υπάρχει, πλοηγούμε αμέσως στο MapFragment
            showPlace(shopName, latitude, longitude);
        }
    }

    // Μετά την παραχώρηση της άδειας, ελέγχεται αν τα δεδομένα καταστήματος υπάρχουν και γίνεται η πλοήγηση
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Αν παραχωρηθεί η άδεια, εκτελείται η πλοήγηση αμέσως
                    showPlace(shopName, latitude, longitude);
                } else {
                    Toast.makeText(requireContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
                }
            });

    private void showPlace(String shopName, double latitude, double longitude) {
        // Ετοιμάζουμε τα δεδομένα και πλοηγούμε στο MapFragment
        Bundle args = new Bundle();
        args.putString("shopName", shopName);
        args.putDouble("latitude", latitude);
        args.putDouble("longitude", longitude);

        // Πλοήγηση στο MapFragment με τα δεδομένα του καταστήματος
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_AvailableShopsFragment_to_MapFragment, args);
    }
}
