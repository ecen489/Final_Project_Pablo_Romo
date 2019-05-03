package com.example.final_project_pablo_romo;

// Class that holds the Food Week data to be pulled from firebase

public class FoodWeek {

    String name;
    String user;
    Day Monday;
    Day Tuesday;
    Day Wednesday;
    Day Thursday;
    Day Friday;
    Day Saturday;
    Day Sunday;

    public FoodWeek() {}
    public FoodWeek(String n, String u, Day m, Day t, Day w, Day r, Day f, Day s, Day sd) {
        name = n;
        user = u;
        Monday = m;
        Tuesday = t;
        Wednesday = w;
        Thursday = r;
        Friday = f;
        Saturday = s;
        Sunday = sd;
    }
}
