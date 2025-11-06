package steps;

import static io.restassured.RestAssured.*;
import io.restassured.response.Response;
import org.testng.Assert;
import io.cucumber.java.en.*;

public class LoginSteps {

	public static String token;
	Response response;
	String baseUri = "https://petstore.swagger.io/v2";

	@Given("Login to petstore with username {string} and password {string}")
	public void loginToPetStore(String username, String password) {
		response = given().baseUri(baseUri).header("Content-Type", "application/json").when()
				.get("/user/login?username=" + username + "&password=" + password);
		System.out.println("Login Response:\n" + response);
	}

	@Then("Valid token should be received")
	public void validateToken() {
		Assert.assertEquals(response.statusCode(), 200, "Login API failed!");
		token = response.getHeader("X-Expires-After");
		Assert.assertNotNull(token, "Token should not be null");
		System.out.println("Token Received: " + token);
	}

	@When("Request petstore user details for {string}")
	public void getUserDetails(String username) {
		response = given().baseUri(baseUri).header("Authorization", "Bearer " + token).when().get("/user/" + username);
		System.out.println("User Response:\n" + response);
	}

	@Then("User should see the username in response as {string}")
	public void verifyUserResponse(String expectedUsername) {
		Assert.assertEquals(response.statusCode(), 200, "Get user details API failed!");
		Assert.assertEquals(response.jsonPath().getString("username"), expectedUsername, "Username mismatch!");
	}
}
