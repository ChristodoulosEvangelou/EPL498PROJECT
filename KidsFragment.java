package com.example.project498;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class KidsFragment extends Fragment implements ShoeAdapter.OnShoeClickListener {

    private RecyclerView recyclerView;
    private ShoeAdapter shoeAdapter;
    private List<Shoe> shoeList;
    private List<Shoe> shoeNikeList;
    private List<Shoe> shoeAdidasList;
    private List<Shoe> shoeNewBalanceList;

    private Button buttonAll, buttonNike, buttonAdidas, buttonNewBalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mens, container, false);
        TextView textView = rootView.findViewById(R.id.textView);
        textView.setText("Kids Collection");

        // Initialize RecyclerView and lists
        recyclerView = rootView.findViewById(R.id.recyclerView_shoes);
        shoeList = new ArrayList<>();
        shoeNikeList = new ArrayList<>();
        shoeAdidasList = new ArrayList<>();
        shoeNewBalanceList = new ArrayList<>();

        populateAllShoeList();
        populateBrandSpecificLists();

        // Set up the Adapter for RecyclerView
        shoeAdapter = new ShoeAdapter(shoeList, this);  // Pass `this` as the listener
        recyclerView.setAdapter(shoeAdapter);

        // Set RecyclerView LayoutManager
        setRecyclerViewLayout();

        // Initialize buttons
        buttonAll = rootView.findViewById(R.id.button_all);
        buttonNike = rootView.findViewById(R.id.button_nike);
        buttonAdidas = rootView.findViewById(R.id.button_adidas);
        buttonNewBalance = rootView.findViewById(R.id.button_nbalance);

        // Set onClickListeners
        buttonAll.setOnClickListener(view -> {
            showAllShoes();
            changeButtonColor(buttonAll);
        });
        buttonNike.setOnClickListener(view -> {
            showNikeShoes();
            changeButtonColor(buttonNike);
        });
        buttonAdidas.setOnClickListener(view -> {
            showAdidasShoes();
            changeButtonColor(buttonAdidas);
        });
        buttonNewBalance.setOnClickListener(view -> {
            showNewBalanceShoes();
            changeButtonColor(buttonNewBalance);
        });

        return rootView;
    }

    private void setRecyclerViewLayout() {
        // Set GridLayoutManager with 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void populateAllShoeList() {

        shoeList.add(new Shoe("Nike Air Force 1 Black", R.drawable.airforcekids, 120, Shoe.ShoeType.KIDS));
        shoeList.add(new Shoe("Nike Dunk Panda k.", R.drawable.kidspanda, 120, Shoe.ShoeType.KIDS));
        shoeList.add(new Shoe("Nike Air Jordan 1 k.", R.drawable.jordankids, 120, Shoe.ShoeType.KIDS));
        shoeList.add(new Shoe("Adidas Yeezy k.", R.drawable.yeezyskids, 180, Shoe.ShoeType.KIDS));
        shoeList.add(new Shoe("Adidas Court Mid", R.drawable.grandcourtmid, 110, Shoe.ShoeType.KIDS));
        shoeList.add(new Shoe("Adidas Superstar k.", R.drawable.kidsuperstar, 80, Shoe.ShoeType.KIDS));
        shoeList.add(new Shoe("New Balance 990 k.", R.drawable.nbkids990, 90, Shoe.ShoeType.KIDS));
        shoeList.add(new Shoe("New Balance 9060 k.", R.drawable.nbkids9060, 95 , Shoe.ShoeType.KIDS));
        shoeList.add(new Shoe("New Balance 550 k.", R.drawable.nb550kids, 100 , Shoe.ShoeType.KIDS));
    }

    private void populateBrandSpecificLists() {


        // Nike shoes
        shoeNikeList.add(new Shoe("Nike Air Force 1 Black", R.drawable.airforcekids, 120, Shoe.ShoeType.KIDS));
        shoeNikeList.add(new Shoe("Nike Dunk Panda k.", R.drawable.kidspanda, 120, Shoe.ShoeType.KIDS));
        shoeNikeList.add(new Shoe("Nike Air Jordan 1 k.", R.drawable.jordankids, 120, Shoe.ShoeType.KIDS));

        shoeAdidasList.add(new Shoe("Adidas Yeezy k.", R.drawable.yeezyskids, 180, Shoe.ShoeType.KIDS));
        shoeAdidasList.add(new Shoe("Adidas Court Mid", R.drawable.grandcourtmid, 110, Shoe.ShoeType.KIDS));
        shoeAdidasList.add(new Shoe("Adidas Superstar k.", R.drawable.kidsuperstar, 80, Shoe.ShoeType.KIDS));

        shoeNewBalanceList.add(new Shoe("New Balance 990 k.", R.drawable.nbkids990, 90, Shoe.ShoeType.KIDS));
        shoeNewBalanceList.add(new Shoe("New Balance 9060 k.", R.drawable.nbkids9060, 95 , Shoe.ShoeType.KIDS));
        shoeNewBalanceList.add(new Shoe("New Balance 550 k.", R.drawable.nb550kids, 100 , Shoe.ShoeType.KIDS));
    }

    private void showAllShoes() {
        shoeList.clear();
        populateAllShoeList();
        shoeAdapter.notifyDataSetChanged();
    }

    private void showNikeShoes() {
        shoeList.clear();
        shoeList.addAll(shoeNikeList);
        shoeAdapter.notifyDataSetChanged();
    }

    private void showAdidasShoes() {
        shoeList.clear();
        shoeList.addAll(shoeAdidasList);
        shoeAdapter.notifyDataSetChanged();
    }

    private void showNewBalanceShoes() {
        shoeList.clear();
        shoeList.addAll(shoeNewBalanceList);
        shoeAdapter.notifyDataSetChanged();
    }

    private void changeButtonColor(Button selectedButton) {
        buttonAll.setBackgroundColor(Color.parseColor("#000000"));
        buttonNike.setBackgroundColor(Color.parseColor("#000000"));
        buttonAdidas.setBackgroundColor(Color.parseColor("#000000"));
        buttonNewBalance.setBackgroundColor(Color.parseColor("#000000"));

        selectedButton.setBackgroundColor(Color.parseColor("#FF0000"));
    }

    @Override
    public void onShoeClick(Shoe shoe) {
        // Δημιουργία του ShoeDetailFragment και περασμα των δεδομένων
        ShoeDetailFragment shoeDetailFragment = new ShoeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("selectedShoe", shoe);  // Προσθέστε το Shoe object
        shoeDetailFragment.setArguments(args);

        // Χρησιμοποιούμε το NavController για να πλοηγηθούμε στο νέο fragment
        NavController navController = NavHostFragment.findNavController(this); // Αυτό πρέπει να το κάνετε μέσα στο Fragment
        navController.navigate(R.id.action_KidsFragment_to_ShoeDetailFragment, args);
    }
}
