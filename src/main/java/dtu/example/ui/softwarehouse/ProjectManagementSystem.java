package dtu.example.ui.softwarehouse;

import java.util.*;

public class ProjectManagementSystem /*lavet af abdullah*/{
    private List<Project> projects = new ArrayList<>();
    private Map<String, Employee> employees = new HashMap<>();

    public Project createProject(String name) {
        Project project = new Project(name);
        projects.add(project);
        return project;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Employee getOrCreateEmployee(String initials) {
        return employees.computeIfAbsent(initials.toLowerCase(), Employee::new);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }
}