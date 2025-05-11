package dtu.example.ui.softwarehouse;

public class SystemHolder /*lavet af abdullah*/{
    private static final ProjectManagementSystem system = new ProjectManagementSystem();

    public static ProjectManagementSystem getSystem() {
        return system;
    }
}