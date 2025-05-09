package dtu.example.ui.softwarehouse;

public class SystemHolder {
    private static final ProjectManagementSystem system = new ProjectManagementSystem();

    public static ProjectManagementSystem getSystem() {
        return system;
    }
}