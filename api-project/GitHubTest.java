package examples;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class GitHubTest {
    RequestSpecification requestSpecification;
    String sshKey = "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";
    int sshId;

    @BeforeClass
    public void setUp() {

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "token " + sshKey)
                .build();
    }

    @Test
    public void addSshKey() {
        String requestBody = "{\"title\": \"TestAPIKey\", \"key\": \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAg\"}";
        Response response = given().spec(requestSpecification)
                .body(requestBody)
                .when().post("/user/keys");
        sshId = response.then().extract().path("id");
        Assert.assertEquals(response.getStatusCode(), 201, "Status code not as expected!");
    }

    @Test(priority = 1)
    public void getSshKey() {
        Response response = given().spec(requestSpecification)
                .pathParam("keyId", sshId)
                .when().get("/user/keys/{keyId}");
        Reporter.log(response.getBody().asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200, "Status code not as expected!");
    }

    @Test(priority = 2)
    public void deleteSshKey() {
        Response response = given().spec(requestSpecification)
                .pathParam("keyId", sshId)
                .when().delete("/user/keys/{keyId}");
        Reporter.log(response.getBody().asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 204, "Status code not as expected!");
    }

}
