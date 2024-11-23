package com.example.project498;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.project498.databinding.FragmentEditBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class EditFragment extends Fragment {

    private FragmentEditBinding binding;
    private FirebaseFirestore db;

    private EditText editFullName, editEmail, editPhoneNumber;
    private Button buttonSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance(); // Initialize Firestore
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditBinding.inflate(inflater, container, false);

        editFullName = binding.editFullName;
        editEmail = binding.editEmail;
        editPhoneNumber = binding.editPhoneNumber;
        buttonSave = binding.buttonSave;

        // Get email from the arguments passed from SettingsFragment
        String email = getArguments().getString("email");
        if (email != null && !email.isEmpty()) {
            fetchUserData(email);
        } else {
            Toast.makeText(requireContext(), "Email not found!", Toast.LENGTH_SHORT).show();
        }

        // Set OnClickListener for Save button
        buttonSave.setOnClickListener(v -> {
            updateUserData();
        });

        return binding.getRoot();
    }

    private void fetchUserData(String email) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String fullName = documentSnapshot.getString("fullName");
                            String phoneNumber = documentSnapshot.getString("phoneNumber");

                            // Set the data into EditText fields for editing
                            editFullName.setText(fullName);
                            editEmail.setText(email); // Display email but not editable
                            editPhoneNumber.setText(phoneNumber);
                        }
                    } else {
                        Toast.makeText(requireContext(), "User data not found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error fetching data: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void updateUserData() {
        String updatedFullName = editFullName.getText().toString().trim();
        String updatedEmail = editEmail.getText().toString().trim();
        String updatedPhoneNumber = editPhoneNumber.getText().toString().trim();

        // Retrieve the current user's data (Assuming you have the user's email to query)
        db.collection("users")
                .whereEqualTo("email", updatedEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Get the current values from Firestore
                            String currentFullName = documentSnapshot.getString("fullName");
                            String currentEmail = documentSnapshot.getString("email");
                            String currentPhoneNumber = documentSnapshot.getString("phoneNumber");

                            // Check if the new data is different from the existing data
                            boolean shouldUpdate = false;

                            // Compare each field and set shouldUpdate to true if the field is different
                            if (!updatedFullName.equals(currentFullName)) {
                                shouldUpdate = true;
                            }
                            if (!updatedEmail.equals(currentEmail)) {
                                shouldUpdate = true;
                            }
                            if (!updatedPhoneNumber.equals(currentPhoneNumber)) {
                                shouldUpdate = true;
                            }

                            // Only update if there is any change
                            if (shouldUpdate) {
                                documentSnapshot.getReference().update("fullName", updatedFullName,
                                                "email", updatedEmail,
                                                "phoneNumber", updatedPhoneNumber)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(requireContext(), "User data updated!", Toast.LENGTH_SHORT).show();
                                            // Navigate back to the SettingsFragment after update

                                        })
                                        .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error updating data: " + e.getMessage(), Toast.LENGTH_LONG).show());
                            } else {
                                // No changes detected, so no need to update
                                Toast.makeText(requireContext(), "No changes.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Error finding user: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }


    private void navigateToSettingsFragment() {
        // Navigate back to SettingsFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.action_EditFragment_to_SettingsFragment, new SettingsFragment()) // Ensure you are replacing in the correct container
                .addToBackStack(null)  // Add to the back stack so the user can navigate back
                .commit();
    }
}
