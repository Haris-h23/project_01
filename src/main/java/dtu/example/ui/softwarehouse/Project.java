package dtu.example.ui.softwarehouse;

import java.util.*;

public class Project /*lavet af lano*/{
    private static int projectCounter = 25000;
    private String projectNumber;
    private String name; 
    private String projectLeaderInitials;
    private List<Activity> activities = new ArrayList<>();
    private boolean isDone = false;

    public Project(String name) {
        this.projectNumber = String.valueOf(++projectCounter);
        this.name = name;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public String getName() {
        return name;
    }

    public void setProjectLeader(String initials) {
        this.projectLeaderInitials = initials.toLowerCase();
    }

    public String getProjectLeader() {
        return projectLeaderInitials;
    }

    public boolean isLeader(String initials) {
        return projectLeaderInitials != null && projectLeaderInitials.equalsIgnoreCase(initials);
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        this.isDone = true;
    }
}
