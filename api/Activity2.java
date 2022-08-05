package activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Activity2 {
    private final static String ROOT_URI = "https://petstore.swagger.io/v2/user";

    @Test(priority=1)
    public void addNewUserFromFile() throws IOException {
        FileInputStream inputJSON = new FileInputStream("src/test/java/Activities/userinfo.json");
        String reqBody = inputJSON.toString();

        Response response =
                given().contentType(ContentType.JSON)
                        .body(reqBody)
                        .when().post(ROOT_URI);
        inputJSON.close();
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("1000"));
    }

    @Test(priority=2)
    public void getUserInfo() {
        File outputJSON = new File("src/test/java/activities/userGETResponse.json");
        Response response =
                given().contentType(ContentType.JSON)
                        .pathParam("username", "userTest")
                        .when().get(ROOT_URI + "/{username}");
        String resBody = response.getBody().asPrettyString();

        try {
            outputJSON.createNewFile();
            FileWriter writer = new FileWriter(outputJSON.getPath());
            writer.write(resBody);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        response.then().body("id", equalTo(1000));
        response.then().body("username", equalTo("userTest"));
        response.then().body("firstName", equalTo("testFirstName"));
        response.then().body("lastName", equalTo("testLastName"));
        response.then().body("email", equalTo("test@gmail.com"));
        response.then().body("password", equalTo("pass"));
        response.then().body("phone", equalTo("981234567"));
    }

    @Test(priority=3)
    public void deleteUser() {
        Response response =
                given().contentType(ContentType.JSON)
                        .pathParam("username", "userTest")
                        .when().delete(ROOT_URI + "/{username}");

        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("userTest"));
    }
}