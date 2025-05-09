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

    @FXML
    protected void createProject() {
        String projectName = projectNameField.getText();
        String leaderInitials = projectLeaderField.getText().trim().toLowerCase();

        if (!projectName.isEmpty() && leaderInitials.matches("[a-z]{2,4}")) {
            Project newProject = system.createProject(projectName);
            newProject.setProjectLeader(leaderInitials);
            system.getOrCreateEmployee(leaderInitials);

            updateProjectList();
            projectNameField.clear();
            projectLeaderField.clear();
        } else {
            showError("Enter project name and valid leader initial (2-4).");
        }
    }

    private void updateProjectList() {
        setLoggedInUser(loggedInUser);
    }

    @FXML
    protected void enterProject() {
        int selectedIndex = projectListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String selected = projectListView.getItems().get(selectedIndex);
            String projectNumber = selected.split(":")[0].trim();
    
            for (Project p : system.getProjects()) {
                if (p.getProjectNumber().equals(projectNumber)) {
                    selectedProject = p;
                    break;
                }
            }
    
            projectTitle.setText("Project: " + selectedProject.getProjectName());
            generateReportButton.setVisible(selectedProject.isLeader(loggedInUser)); // <-- Make sure this is here
            showProjectDetails();
            updateActivityList();
        }
    }

    private void updateActivityList() {
        activityListView.getItems().clear();
        if (selectedProject == null) return;

        boolean isLeader = selectedProject.isLeader(loggedInUser);
        List<Activity> activities = selectedProject.getActivities();

        activityListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    Activity activity = activities.stream()
                        .filter(a -> a.getName().equals(item))
                        .findFirst().orElse(null);
                    if (activity != null) {
                        switch (activity.getStatus()) {
                            case APPROVED -> setStyle("-fx-background-color: lightgreen;");
                            case PENDING -> setStyle("-fx-background-color: yellow;");
                            case REJECTED -> setStyle("-fx-background-color: red;");
                            default -> setStyle("");
                        }
                    }
                }
            }
        });

        for (Activity activity : selectedProject.getActivities()) {
            if (isLeader) {
                activityListView.getItems().add(activity.getName());
            } else {
                for (Employee e : activity.getAssignedEmployees()) {
                    if (e.getInitials().equalsIgnoreCase(loggedInUser)) {
                        activityListView.getItems().add(activity.getName());
                        break;
                    }
                }
            }
        }
    }



}