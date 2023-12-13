package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class RestfulBookerTest {
    private static final String baseURI = "https://restful-booker.herokuapp.com";
    private static String authToken;
    private static Integer bookingId;

    @BeforeClass
    public void authenticateAndGetToken() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("username", "admin");
        jsonAsMap.put("password", "password123");
        Response response = given()
                .baseUri(baseURI)
                .contentType(JSON)
                .body(jsonAsMap)
                .when()
                .post("/auth")
                .then().log().all()
                .extract().response();

        authToken = response.jsonPath().getString("token");
        System.out.println("Authentication successful. Token: " + authToken);
    }

    @Test(priority = 1)
    public void createBooking() {
        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("firstname", "Sally");
        bookingDetails.put("lastname", "Brown");
        bookingDetails.put("totalprice", 111);
        bookingDetails.put("depositpaid", true);

        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2013-02-23");
        bookingDates.put("checkout", "2014-10-23");
        bookingDetails.put("bookingdates", bookingDates);

        bookingDetails.put("additionalneeds", "Breakfast");

        Response response = RestAssured.given()
                .header("Authorization", "token " + authToken)
                .contentType(ContentType.JSON)
                .baseUri(baseURI)
                .body(bookingDetails)
                .when()
                .post("/booking")
                .then().log().all()
                .extract().response();

        bookingId = response.jsonPath().get("bookingid");
        System.out.println(bookingId);
    }

    @Test(priority = 2)
    public void updateBooking() {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> updateDetails = new HashMap<>();
        updateDetails.put("firstname", "Sally");
        updateDetails.put("lastname", "Brown");
        updateDetails.put("totalprice", 200);
        updateDetails.put("depositpaid", true);

        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2013-02-23");
        bookingDates.put("checkout", "2014-10-23");
        updateDetails.put("bookingdates", bookingDates);

        updateDetails.put("additionalneeds", "Breakfast");


        RestAssured.given()
                .header("Authorization", "token " + authToken)
                .cookie("token", authToken)
                .contentType(ContentType.JSON)
                .baseUri(baseURI)
                .body(updateDetails.toString())
                .when()
                .put("/booking/" + bookingId)
                .then().log().all()
                .extract().response()
                .getStatusCode();
    }

    @Test(priority = 3)
    public void getUpdatedBookingDetails(){
        Response response = RestAssured.given()
                .header("Authorization", "token " + authToken)
                .contentType(ContentType.JSON)
                .baseUri(baseURI)
                .when()
                .get("/booking/" + bookingId)
                .then().log().all()
                .extract().response();
    }

    @Test(priority = 4)
    public void getAllBookings() {
        Response response = RestAssured.given()
                .header("Authorization", "token " + authToken)
                .contentType(ContentType.JSON)
                .baseUri(baseURI)
                .when()
                .get("/booking")
                .then().log().all()
                .extract().response();
    }

    @Test(priority = 5)
    public void deleteBooking() {
        RestAssured.given()
                .header("Authorization", "token " + authToken)
                .contentType(ContentType.JSON)
                .baseUri(baseURI)
                .when()
                .delete("/booking/" + bookingId)
                .then().log().all()
                .extract().response();
    }
}
