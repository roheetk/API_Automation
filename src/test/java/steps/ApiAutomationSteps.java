package steps;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class ApiAutomationSteps {

	private static String token;
	private Response response;

	@Given("User logs in to the application with valid credentials")
	public void user_logs_in_to_the_application_with_valid_credentials() {
		String loginUrl = "https://evergreen.atheer.qa/api/authenticate";

		String requestBody = "{ \"username\": \"automationtest\", \"password\": \"Test@111\" }";

		Response loginResponse = given().header("Content-Type", "application/json").body(requestBody).when()
				.post(loginUrl).then().log().all().extract().response();

		System.out.println("Login Response Status: " + loginResponse.getStatusCode());
		System.out.println("Login Response Body: " + loginResponse.getBody().asString());

		token = loginResponse.jsonPath().getString("id_token");
		System.out.println("Extracted Token: " + token);

		assertNotNull(token, "Token should not be null");
	}

	@When("User sends a GET request to protected API using the token")
	public void user_sends_a_get_request_to_protected_api_using_the_token() {
		String protectedUrl = "https://evergreen.atheer.qa/api/users/me?permissions=true&include-audiences=false&widget-auth=false&update-visit=false";

		System.out.println("Token being used: " + token);

		response = given().header("Authorization", "Bearer ath-" + token).when().get(protectedUrl).then().log().all()
				.extract().response();
	}

	@Then("The response status code should be 200")
	public void the_response_status_code_should_be_200() {
		int statusCode = response.getStatusCode();
		assertEquals(statusCode, 200, "Expected 200 OK but found: " + statusCode);
	}

	@And("The response should contain expected JSON fields")
	public void the_response_should_contain_expected_json_fields() {
		String username = response.jsonPath().getString("username");
		String email = response.jsonPath().getString("email");

		assertNotNull(username, "Username field should be present");
		assertNotNull(email, "Email field should be present");
		System.out.println("Verified JSON fields successfully.");
	}
}
