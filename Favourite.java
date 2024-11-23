package com.example.project498;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Favourite implements Serializable {

    private String email;
    private List<Shoe> shoes;

    // Default Constructor
    public Favourite() {
        this.shoes = new ArrayList<>();
    }

    // Parameterized Constructor
    public Favourite(String email, List<Shoe> shoes) {
        this.email = email;
        this.shoes = shoes;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for shoes
    public List<Shoe> getShoes() {
        return shoes;
    }

    // Setter for shoes
    public void setShoes(List<Shoe> shoes) {
        this.shoes = shoes;
    }

    // Add a single shoe to the list
    public void addShoe(Shoe shoe) {
        this.shoes.add(shoe);
    }

    // Remove a shoe from the list
    public void removeShoe(Shoe shoe) {
        this.shoes.remove(shoe);
    }
}
