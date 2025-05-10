package dtu.example.ui.softwarehouse;

import java.util.*;

public class Activity {
    private String name;
    private int budgetedHours;
    private int startWeek;
    private int endWeek;
    private int endYear;

    private List<Employee> assignedEmployees = new ArrayList<>();
    private Map<Employee, Integer> registeredHours = new HashMap<>();
    private ActivityStatus status = ActivityStatus.NONE;

    public enum ActivityStatus {
        NONE, PENDING, APPROVED, REJECTED
    }

    public Activity(String name, int budgetedHours, int startWeek, int endWeek, int endYear) {
        this.name = name;
        this.budgetedHours = budgetedHours;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.endYear = Calendar.getInstance().get(Calendar.YEAR); 
    }

    // ------------------ Getters ------------------

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

    public int getEndYear() {
        return endYear;
    }

    public List<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public int getRegisteredHours(Employee employee) {
        return registeredHours.getOrDefault(employee, 0);
    }

    public int getTotalRegisteredHours() {
        return registeredHours.values().stream().mapToInt(Integer::intValue).sum();
    }

    // ------------------ Setters ------------------

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

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    // ------------------ Core Logic ------------------

    public void assignEmployee(Employee employee) {
        if (!assignedEmployees.contains(employee)) {
            assignedEmployees.add(employee);
            employee.assignToActivity(this); // link both ways
        }
    }

    public void registerHours(Employee employee, int hours) {
        registeredHours.put(employee, hours);
    }
}
