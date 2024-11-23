package com.example.project498;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the buttons
        Button registerButton = view.findViewById(R.id.button4);
        Button signInButton = view.findViewById(R.id.button3);

        // Set click listener for Register button to navigate to RegisterFragment
        registerButton.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_HomeFragment_to_RegisterFragment)
        );

        // Set click listener for Sign In button to navigate to SigninFragment
        signInButton.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_HomeFragment_to_SigninFragment)
        );

        return view;
    }
}
