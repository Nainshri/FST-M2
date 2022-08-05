package activities;

import org.testng.Reporter;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Activity1 {
    final static String Base_URL = "https://petstore.swagger.io/v2/pet";

    @Test(priority = 1)
    public void addNewPet() {
        String postReq = "{"
                + "\"id\": 77232,"
                + "\"name\": \"Riley\","
                + " \"status\": \"alive\""
                + "}";

        Response response =
                given().contentType(ContentType.JSON)
                        .body(postReq)
                        .when().post(Base_URL);
        Reporter.log(response.getBody().asPrettyString());
        response.then().body("id", equalTo(77232));
        response.then().body("name", equalTo("Riley"));
        response.then().body("status", equalTo("alive"));
    }


    @Test(priority = 2)
    public void getPetID() {
        Response response =
                given().contentType(ContentType.JSON)
                        .when().get(Base_URL + "/77232");
        Reporter.log(response.getBody().asPrettyString());
        response.then().body("id", equalTo(77232));
        response.then().body("name", equalTo("Riley"));
        response.then().body("status", equalTo("alive"));
    }

    @Test(priority = 3)
    public void deletePet() {
        Response response =
                given().contentType(ContentType.JSON)
                        .when().delete(Base_URL + "/77232");
        Reporter.log(response.getBody().asPrettyString());
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("77232"));
    }
}