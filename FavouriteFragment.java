package com.example.project498;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment implements FavouriteAdapter.OnShoeClickListener {

    private RecyclerView recyclerView;
    private FavouriteAdapter shoeAdapter; // Custom adapter for displaying shoes
    private List<Shoe> favouriteShoes = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view_favourites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shoeAdapter = new FavouriteAdapter(favouriteShoes, this); // Pass the listener (this fragment)
        recyclerView.setAdapter(shoeAdapter);

        // Load favourite shoes from Firestore
        loadFavourites();

        return rootView;
    }

    private void loadFavourites() {
        String userEmail = SigninFragment.userEmail; // Retrieve the static email of the logged-in user

        db.collection("favourites")
                .document(userEmail)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Parse the data
                        Favourite favourite = documentSnapshot.toObject(Favourite.class);
                        if (favourite != null && favourite.getShoes() != null) {
                            favouriteShoes.clear();
                            favouriteShoes.addAll(favourite.getShoes());
                            shoeAdapter.notifyDataSetChanged(); // Update RecyclerView
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle error (e.g., show a toast)
                });
    }

    @Override
    public void onShoeClick(Shoe shoe) {
        // Handle the shoe click event (e.g., show details of the shoe)
        Toast.makeText(getContext(), "Clicked: " + shoe.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveClick(Shoe shoe) {
        // Handle the remove click event
        String userEmail = SigninFragment.userEmail; // Retrieve the static email of the logged-in user

        // Remove the shoe from the favourite list
        favouriteShoes.remove(shoe);

        // Update the RecyclerView
        shoeAdapter.notifyDataSetChanged();

        // Update Firestore to remove the shoe from the user's favourites
        db.collection("favourites")
                .document(userEmail)
                .update("shoes", favouriteShoes)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to remove from favourites", Toast.LENGTH_SHORT).show();
                });
    }
}
