package dtu.example.ui;

import dtu.example.ui.softwarehouse.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.*;

public class PrimaryController {
    private ProjectManagementSystem system = SystemHolder.getSystem();
    private Project selectedProject;
    private Activity selectedActivity;

    @FXML private VBox projectPage, projectDetailsPage, activityPage;
    @FXML private TextField projectNameField, activityNameField, activityHoursField,
                             activityEndWeekField, activityStartWeekField; 
    @FXML private ListView<String> projectListView, activityListView, assignedEmployeesList;
    @FXML private Label projectTitle, activityTitle, statusLabel;
    @FXML private TextField projectLeaderField, assignEmployeeField, hoursUsedField;
    @FXML private Button logoutButton;
    @FXML private HBox approvalSection;
    @FXML private VBox assignmentSection;
    @FXML private Button generateReportButton;
    
    private String loggedInUser;

    public void setLoggedInUser(String initials) {
        this.loggedInUser = initials.toLowerCase();
        projectListView.getItems().clear();

        for (Project project : system.getProjects()) {  
            boolean isLeader = project.isLeader(loggedInUser);
            boolean isAssigned = false;

            for (Activity activity : project.getActivities()) {
                for (Employee e : activity.getAssignedEmployees()) {
                    if (e.getInitials().equalsIgnoreCase(loggedInUser)) {
                        isAssigned = true;
                        break;
                    }
                }
                if (isAssigned) break;
            }

            if (isLeader || isAssigned) {
                int total = project.getActivities().size();
                long done = project.getActivities().stream()
                    .filter(a -> a.getStatus() == Activity.ActivityStatus.APPROVED)
                    .count();
                int percent = total == 0 ? 0 : (int)((done * 100.0f) / total);

                String display = String.format("%s: %s - Done: %d%%",
                        project.getProjectNumber(),
                        project.getProjectName(),
                        percent);
                projectListView.getItems().add(display);
            }
        }
    }

}