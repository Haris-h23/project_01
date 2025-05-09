package dtu.example.ui;

import java.util.HashSet;
import java.util.Set;

import dtu.example.ui.softwarehouse.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class SecondaryController {

    @FXML private TextField initialsField;
    @FXML private Label errorLabel;

    @FXML
    protected void handleLogin() {
        String initials = initialsField.getText().trim().toLowerCase();

        if (initials.matches("[a-z]{2,4}")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
                Parent root = loader.load();

                PrimaryController controller = loader.getController();
                controller.setLoggedInUser(initials);  // Pass user to primary controller

                Stage stage = (Stage) initialsField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Welcome " + initials);
                stage.setWidth(1000);
                stage.setHeight(800);
                stage.show();

            } catch (Exception e) {
                errorLabel.setText("Failed to load main view.");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Please enter your initials (2-4)");
        }
    }

@FXML
protected void handleShowAll() {
    try {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-background-color: white;");

        Label title = new Label("All Projects Overview");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefSize(450, 400);
        outputArea.setWrapText(true);
        outputArea.setStyle("-fx-font-size: 13px;");

        Button backBtn = new Button("Close");
        backBtn.setOnAction(e -> ((Stage) backBtn.getScene().getWindow()).close());

        layout.getChildren().addAll(title, outputArea, backBtn);

        StringBuilder sb = new StringBuilder();
        ProjectManagementSystem system = SystemHolder.getSystem();

        for (Project p : system.getProjects()) {
            int total = p.getActivities().size();
            long approved = p.getActivities().stream()
                    .filter(a -> a.getStatus() == Activity.ActivityStatus.APPROVED)
                    .count();
            int percent = total == 0 ? 0 : (int)((approved * 100.0f) / total);

            // Collect unique employees involved in this project
            Set<String> employeeInitials = new HashSet<>();
            for (Activity a : p.getActivities()) {
                for (Employee emp : a.getAssignedEmployees()) {
                    employeeInitials.add(emp.getInitials());
                }
            }

            sb.append(String.format("Project: %s (%s)\n", p.getProjectName(), p.getProjectNumber()));
            sb.append(String.format(" - Project Leader: %s\n", p.getProjectLeader()));
            sb.append(String.format(" - Activities: %d\n", total));
            sb.append(String.format(" - Done: %d%%\n", percent));
            sb.append(" - Employees involved: ");

            if (employeeInitials.isEmpty()) {
                sb.append("None\n");
            } else {
                sb.append(String.join(", ", employeeInitials)).append("\n");
            }

            sb.append("\n");
        }

        outputArea.setText(sb.toString());

        Stage popup = new Stage();
        popup.setTitle("All Projects Overview");
        popup.setScene(new Scene(layout, 500, 500));
        popup.show();

    } catch (Exception e) {
        e.printStackTrace();
        errorLabel.setText("Unable to show project overview.");
    }
}

}