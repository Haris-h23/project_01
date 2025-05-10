// PrimaryController.java — Grouped by Functional Sections

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
                             activityEndWeekField, activityStartWeekField, activityEndYearField; 
    @FXML private ListView<String> projectListView, activityListView, assignedEmployeesList;
    @FXML private Label projectTitle, activityTitle, statusLabel;
    @FXML private TextField projectLeaderField, assignEmployeeField, hoursUsedField;
    @FXML private Button logoutButton;
    @FXML private HBox approvalSection;
    @FXML private VBox assignmentSection;
    @FXML private Button generateReportButton;

    private String loggedInUser;

    // ----------------------------- LOGIN -----------------------------

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
                        project.getName(),
                        percent);
                projectListView.getItems().add(display);
            }
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.setWidth(1000);
            stage.setHeight(800);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to return to login screen.");
        }
    }

    // ----------------------------- PROJECT -----------------------------

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
            String projectNumber = selected.split(":" )[0].trim();

            for (Project p : system.getProjects()) {
                if (p.getProjectNumber().equals(projectNumber)) {
                    selectedProject = p;
                    break;
                }
            }

            projectTitle.setText("Project: " + selectedProject.getName());
            generateReportButton.setVisible(selectedProject.isLeader(loggedInUser));
            showProjectDetails();
            updateActivityList();
        }
    }

    @FXML
    protected void generateProjectReport() {
        if (selectedProject == null) {
            showError("Select a project first!");
            return;
        }
        if (!selectedProject.isLeader(loggedInUser)) {
            showError("Only the project leader can generate a report.");
            return;
        }
        int totalUsed = 0;
        int totalBudget = 0;
        StringBuilder report = new StringBuilder();
        report.append("Project Report for: ").append(selectedProject.getName())
      .append(" (").append(selectedProject.getProjectNumber()).append(")\n");
        for (Activity a : selectedProject.getActivities()) {
            int used = a.getTotalRegisteredHours();
            int budget = a.getBudgetedHours();
            String endDate = String.format("Week %d, %d", a.getEndWeek(), a.getEndYear());
            report.append(String.format("\n• %-20s Used: %-3d / Budget: %-3d hours\n                                   Ends: %s\n",
        a.getName(), used, budget, endDate));

            List<Employee> assigned = a.getAssignedEmployees();
            if (!assigned.isEmpty()) {
                report.append("- Assigned to: ");
                for (int i = 0; i < assigned.size(); i++) {
                    report.append(assigned.get(i).getInitials());
                    if (i < assigned.size() - 1) report.append(", ");
                }
                report.append("\n");
            }
            totalUsed += used;
            totalBudget += budget;
        }
        int percent = totalBudget == 0 ? 0 : (int) ((totalUsed * 100.0f) / totalBudget);
        report.append("\n--------------------------------------------------\n");
        report.append(String.format("Total used hours:     %d\n", totalUsed));
        report.append(String.format("Total budgeted hours: %d\n", totalBudget));
        report.append(String.format("Overall used:         %d%%\n", percent));
        Label titleLabel = new Label("📊 Project Report");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TextArea reportArea = new TextArea(report.toString());
        reportArea.setEditable(false);
        reportArea.setStyle("-fx-font-family: 'monospace';");
        reportArea.setPrefSize(500, 350);
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> ((Stage) closeBtn.getScene().getWindow()).close());
        VBox layout = new VBox(10, titleLabel, reportArea, closeBtn);
        layout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");
        layout.setPrefWidth(520);
        Stage popup = new Stage();
        popup.setTitle("Project Report");
        popup.setScene(new Scene(layout));
        popup.show();
    }

    @FXML
    protected void goBackToProjects() {
        projectPage.setVisible(true);
        projectDetailsPage.setVisible(false);
    }

    private void showProjectDetails() {
        projectPage.setVisible(false);
        projectDetailsPage.setVisible(true);
    }

    // ----------------------------- ACTIVITY -----------------------------

    @FXML
    protected void createActivity() {
        if (selectedProject == null) {
            showError("Select a project first!");
            return;
        }
        if (!selectedProject.isLeader(loggedInUser)) {
            showError("Only the project leader can create activities.");
            return;
        }
        String defaultName = "New Activity";
        boolean nameExists = selectedProject.getActivities().stream()
            .anyMatch(a -> a.getName().equalsIgnoreCase(defaultName));
        if (nameExists) {
            showError("An activity with that name already exists.");
            return;
        }
        selectedActivity = new Activity(defaultName, 0, 0, 0, 2025);
        selectedProject.addActivity(selectedActivity);
        updateActivityList();
        activityTitle.setText("Create New Activity");
        activityNameField.clear();
        activityHoursField.clear();
        activityStartWeekField.clear();
        activityEndWeekField.clear();
        activityEndYearField.clear();
        showActivityDetails();
    }

    @FXML
    protected void editActivity() {
        if (selectedActivity == null) {
            showError("Select an activity first!");
            return;
        }
        String name = activityNameField.getText().trim();
        String hoursText = activityHoursField.getText().trim();
        String startText = activityStartWeekField.getText().trim();
        String endText = activityEndWeekField.getText().trim();
        String endYearText = activityEndYearField.getText().trim();
        if (!name.equalsIgnoreCase(selectedActivity.getName())) {
            boolean nameExists = selectedProject.getActivities().stream()
                .anyMatch(a -> a.getName().equalsIgnoreCase(name));
            if (nameExists) {
                showError("An activity with that name already exists.");
                return;
            }
        }
        if (name.isEmpty() || hoursText.isEmpty() || startText.isEmpty() || endText.isEmpty()) {
            showError("All fields must be filled.");
            return;
        }
        int hours = parseInt(hoursText);
        int startWeek = parseInt(startText);
        int endWeek = parseInt(endText);
        int endYear = parseInt(endYearText);
        if (hours < 0) {
            showError("Invalid number of hours.");
            return;
        }
        if (startWeek < 1 || startWeek > 52 || endWeek < 1 || endWeek > 52) {
            showError("Week numbers must be between 1 and 52.");
            return;
        }
        if (endWeek < startWeek && endYear == 2025) {
            showError("End week cannot be earlier than start week.");
            return;
        }
        if (endYearText.isEmpty()) {
            showError("End year must be filled.");
            return;
        }
        if (endYear < 2025) {
            showError("End Year must be 2025 or later.");
            return;
        }
        if (endYear > 2125) {
            showError("End Year must be reasonable");
            return;
        }
        selectedActivity.setName(name);
        selectedActivity.setBudgetedHours(hours);
        selectedActivity.setStartWeek(startWeek);
        selectedActivity.setEndWeek(endWeek);
        selectedActivity.setEndYear(endYear);
        updateActivityList();
        goBackToProject();
    }

    @FXML
    protected void enterActivity() {
        int selectedIndex = activityListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            selectedActivity = null;
            for (Activity activity : selectedProject.getActivities()) {
                if (activity.getName().equals(activityListView.getItems().get(selectedIndex))) {
                    selectedActivity = activity;
                    break;
                }
            }
            if (selectedActivity != null) {
                activityTitle.setText("Editing: " + selectedActivity.getName());
                fillActivityFields(selectedActivity);
                updateStatusLabel(selectedActivity);
                updateAssignedEmployeesList();
                assignmentSection.setVisible(selectedProject.isLeader(loggedInUser));
                approvalSection.setVisible(selectedProject.isLeader(loggedInUser));
                showActivityDetails();
            }
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

    private void fillActivityFields(Activity activity) {
        activityNameField.setText(activity.getName());
        activityHoursField.setText(String.valueOf(activity.getBudgetedHours()));
        activityStartWeekField.setText(String.valueOf(activity.getStartWeek()));
        activityEndWeekField.setText(String.valueOf(activity.getEndWeek()));
        activityEndYearField.setText(String.valueOf(activity.getEndYear()));
    }

    private void updateAssignedEmployeesList() {
        assignedEmployeesList.getItems().clear();
        for (Employee e : selectedActivity.getAssignedEmployees()) {
            int hours = selectedActivity.getRegisteredHours(e);
            assignedEmployeesList.getItems().add(e.getInitials() + " (" + hours + "h)");
        }
    }

    @FXML
    protected void goBackToProject() {
        projectDetailsPage.setVisible(true);
        activityPage.setVisible(false);
    }

    private void showActivityDetails() {
        projectDetailsPage.setVisible(false);
        activityPage.setVisible(true);
    }

    // ----------------------------- EMPLOYEE -----------------------------

    @FXML
    protected void assignEmployee() {
        if (selectedActivity == null) {
            showError("Select an activity first!");
            return;
        }
        if (!selectedProject.isLeader(loggedInUser)) {
            showError("Only the project leader can assign employees.");
            return;
        }
        String initials = assignEmployeeField.getText().trim().toLowerCase();
        if (!initials.matches("[a-z]{2,4}")) {
            showError("Enter initials (2-4).");
            return;
        }
        Employee employee = system.getOrCreateEmployee(initials);
        selectedActivity.assignEmployee(employee);
        assignEmployeeField.clear();
        showInfo("Assigned " + initials + " to " + selectedActivity.getName());
        updateAssignedEmployeesList();
    }

    @FXML
    protected void registerHours() {
        if (selectedActivity == null) return;
        if (selectedProject.isLeader(loggedInUser)) {
            showError("Leaders cannot register hours.");
            return;
        }
        try {
            int hours = Integer.parseInt(hoursUsedField.getText().trim());
            Employee emp = system.getOrCreateEmployee(loggedInUser);
            selectedActivity.registerHours(emp, hours);
            showInfo("Hours registered.");
            updateAssignedEmployeesList();
        } catch (NumberFormatException e) {
            showError("Enter a valid number of hours.");
        }
    }

    @FXML
    protected void markDone() {
        if (selectedActivity == null) return;
        if (selectedProject.isLeader(loggedInUser)) {
            showError("Only assigned employees can mark done.");
            return;
        }
        selectedActivity.setStatus(Activity.ActivityStatus.PENDING);
        updateStatusLabel(selectedActivity);
        updateActivityList();
    }

    // ----------------------------- APPROVAL -----------------------------

    @FXML
    protected void approveActivity() {
        if (!selectedProject.isLeader(loggedInUser)) {
            showError("Only the leader can approve.");
            return;
        }
        selectedActivity.setStatus(Activity.ActivityStatus.APPROVED);
        updateStatusLabel(selectedActivity);
        updateActivityList();
        updateProjectList();
    }

    @FXML
    protected void rejectActivity() {
        if (!selectedProject.isLeader(loggedInUser)) {
            showError("Only the leader can reject.");
            return;
        }
        selectedActivity.setStatus(Activity.ActivityStatus.REJECTED);
        updateStatusLabel(selectedActivity);
        updateActivityList();
        updateProjectList();
    }

    private void updateStatusLabel(Activity activity) {
        Activity.ActivityStatus status = activity.getStatus();
        statusLabel.setText("Status: " + status);
        switch (status) {
            case PENDING -> statusLabel.setStyle("-fx-text-fill: orange;");
            case APPROVED -> statusLabel.setStyle("-fx-text-fill: green;");
            case REJECTED -> statusLabel.setStyle("-fx-text-fill: red;");
            default -> statusLabel.setStyle("-fx-text-fill: black;");
        }
    }

    // ----------------------------- UTILITY -----------------------------

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void confirmDeleteProject(Project project) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Project Deletion");
        alert.setHeaderText("Are you sure you want to delete project: " + project.getName() + "?");
        alert.setContentText("This will delete all associated activities.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                system.getProjects().remove(project);
                updateProjectList();
                showInfo("Project deleted.");
            }
        });
    }

}