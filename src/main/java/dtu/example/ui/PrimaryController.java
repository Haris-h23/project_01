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
    
}