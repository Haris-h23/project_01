package dtu.example.ui.softwarehouse;

import java.util.*;

public class Employee /*lavet af haris*/{
    private String initials;
    private List<Activity> activities = new ArrayList<>();

    public Employee(String initials) {
        this.initials = initials.toLowerCase();
    }

    public String getInitials() {
        return initials;
    }

    public void assignToActivity(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public List<Activity> getActivities() {
        return activities;
    }
}