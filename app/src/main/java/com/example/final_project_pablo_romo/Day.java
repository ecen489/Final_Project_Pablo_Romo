package com.example.final_project_pablo_romo;

// Class for the Day information

public class Day {

    String name;

    Meal Breakfast;
    Meal Lunch;
    Meal Dinner;
    Meal Snacks;

    public Day () {}
    public Day (String na, Meal b, Meal n, Meal d, Meal s) {
        name = na;
        Breakfast = b;
        Lunch = n;
        Dinner = d;
        Snacks = s;
    }

}
