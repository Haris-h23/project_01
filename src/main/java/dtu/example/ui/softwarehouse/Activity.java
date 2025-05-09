package dtu.example.ui.softwarehouse;

import java.util.*;

public class Activity {
    private String name;
    private int budgetedHours;
    private int startWeek;
    private int endWeek;

    public Activity(String name, int budgetedHours, int startWeek, int endWeek) {
        this.name = name;
        this.budgetedHours = budgetedHours;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
    }

    public String getName() {
        return name;
    }

    public int getBudgetedHours() {
        return budgetedHours;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBudgetedHours(int budgetedHours) {
        this.budgetedHours = budgetedHours;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }
}
