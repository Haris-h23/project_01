package hellocucumber;

import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import dtu.example.ui.softwarehouse.Activity;
import dtu.example.ui.softwarehouse.Activity.ActivityStatus;
import dtu.example.ui.softwarehouse.Employee;
import dtu.example.ui.softwarehouse.Project;
import dtu.example.ui.softwarehouse.ProjectManagementSystem;

public class StepDefinitions {
	
    private Activity activity;
    private Employee employee;
    private Project project;
    private ProjectManagementSystem system;


/*------------------------------------------ Activity.java tests -------------------------------------------- */

    @Given("I create an activity named {string} with {int} hours, start week {int}, and end week {int}")
    public void i_create_an_activity(String name, Integer hours, Integer startWeek, Integer endWeek) {
        // Write code here that turns the phrase above into concrete actions
        activity = new Activity(name, hours, startWeek, endWeek);
    }

    @Then("the activity name should be {string}")
    public void the_activity_name_should_be(String expectedName) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(expectedName, activity.getName());
    }

    @Then("the budgeted hours should be {int}")
    public void the_budgeted_hours_should_be(Integer expectedHours) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(expectedHours.intValue(), activity.getBudgetedHours());
    }

    @Then("the start week should be {int}")
    public void the_start_week_should_be(Integer expectedStartWeek) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(expectedStartWeek.intValue(), activity.getStartWeek());
    }

    @Then("the end week should be {int}")
    public void the_end_week_should_be(Integer expectedEndWeek) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(expectedEndWeek.intValue(), activity.getEndWeek());
    }

    @Then("the activity status should be {string}")
    public void the_activity_status_should_be(String expectedStatus) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(ActivityStatus.valueOf(expectedStatus), activity.getStatus());
    }

    @When("I change the name to {string}")
    public void i_change_the_name_to(String newName) {
        // Write code here that turns the phrase above into concrete actions
        activity.setName(newName);
    }

    @When("I set the budgeted hours to {int}")
    public void i_set_the_budgeted_hours_to(Integer newHours) {
        // Write code here that turns the phrase above into concrete actions
        activity.setBudgetedHours(newHours);
    }

    @When("I set the start week to {int}")
    public void i_set_the_start_week_to(Integer newStartWeek) {
        // Write code here that turns the phrase above into concrete actions
        activity.setStartWeek(newStartWeek);
    }

    @When("I set the end week to {int}")
    public void i_set_the_end_week_to(Integer newEndWeek) {
        // Write code here that turns the phrase above into concrete actions
        activity.setEndWeek(newEndWeek);
    }

    @When("I set the activity status to {string}")
    public void i_set_the_activity_status_to(String newStatus) {
        // Write code here that turns the phrase above into concrete actions
        activity.setStatus(ActivityStatus.valueOf(newStatus));
    }

    @Given("I have a user named {string}")
    public void iHaveAUserNamed(String initials) {
        // Write code here that turns the phrase above into concrete actions
        employee = new Employee(initials);    }
    
    @When("I assign the user to the activity")
    public void iAssignTheUserToTheActivity() {
        // Write code here that turns the phrase above into concrete actions
        activity.assignEmployee(employee);
    }
    
    @Then("the activity should have the user assigned to it")
    public void theActivityShouldHaveTheUserAssignedToIt() {
        // Write code here that turns the phrase above into concrete actions
        assertTrue(activity.getAssignedEmployees().contains(employee),
        "The employee was not assigned to the activity.");    
    }

    @When("the user registers {int} hours")
    public void theUserRegistersHours(Integer hours) {
        // Write code here that turns the phrase above into concrete actions
        activity.registerHours(employee, hours);
    }
    
    @Then("the user should have {int} registered hours")
    public void theUserShouldHaveRegisteredHours(Integer expected) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(expected.intValue(), activity.getRegisteredHours(employee));
    }
    
    @Then("the total registered hours should be {int}")
    public void theTotalRegisteredHoursShouldBe(Integer expectedTotal) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(expectedTotal.intValue(), activity.getTotalRegisteredHours());
    }
    
/*------------------------------------------ project.java tests -------------------------------------------- */

@Given("I create a project named {string}")
public void iCreateAProjectNamed(String name) {
    // Write code here that turns the phrase above into concrete actions
    project = new Project(name);
}

@Then("the project name should be {string}")
public void theProjectNameShouldBe(String expectedName) {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(expectedName, project.getProjectName());
}

@Then("the project number should be a number greater than {int}")
public void theProjectNumberShouldBeANumberGreaterThan(Integer min) {
    // Write code here that turns the phrase above into concrete actions
    int number = Integer.parseInt(project.getProjectNumber());
    assertTrue(number > min);}

@Then("the project should not be marked as done")
public void theProjectShouldNotBeMarkedAsDone() {
    // Write code here that turns the phrase above into concrete actions
    assertFalse(project.isDone());
}

@When("I assign {string} as the project leader")
public void iAssignAsTheProjectLeader(String initials) {
    // Write code here that turns the phrase above into concrete actions
    project.setProjectLeader(initials);
}

@Then("the project leader should be {string}")
public void theProjectLeaderShouldBe(String expected) {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(expected.toLowerCase(), project.getProjectLeader());
}

@Then("{string} should be recognized as the project leader")
public void shouldBeRecognizedAsTheProjectLeader(String input) {
    // Write code here that turns the phrase above into concrete actions
    assertTrue(project.isLeader(input));
}

@When("I mark the project as done")
public void iMarkTheProjectAsDone() {
    // Write code here that turns the phrase above into concrete actions
    project.markAsDone();
}

@Then("the project should be marked as done")
public void theProjectShouldBeMarkedAsDone() {
    // Write code here that turns the phrase above into concrete actions
    assertTrue(project.isDone());
}

@When("I add the activity to the project")
public void iAddTheActivityToTheProject() {
    // Write code here that turns the phrase above into concrete actions
    project.addActivity(activity);
}

@Then("the project should contain {int} activity")
public void theProjectShouldContainActivity(Integer count) {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(count, project.getActivities().size());
}

/*------------------------------------------ projectManagementSystem.java tests -------------------------------------------- */


}
