package com.example.final_project_pablo_romo;

// Class for the Ingredients

public class Ingredient {

    String name;
    String serving;

    int cals;
    int protein;
    int carbs;
    int fat;

    String barcode;

    public Ingredient () {}

    public Ingredient(String n, String s, String b, int c, int p, int ca, int f) {
        name = n;
        serving = s;
        barcode = b;
        cals = c;
        protein = p;
        carbs = ca;
        fat = f;
    }

}
