package RestTests;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.core.IsEqual.equalTo;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import io.restassured.module.jsv.JsonSchemaValidator.*;

public class RestApiTest extends BaseTest{

    @Test
    public void getAllProperties(){
        given()
                .when().get("/properties")
                .then()
                .contentType(ContentType.JSON)
                .time(lessThan(3l), TimeUnit.SECONDS)
                .body(is(not(empty())))
                .statusCode(200);
    }

    @Test
    public void getAProperty(){

        ArrayList<Integer> mlsId = given()
                .when().get("/properties")
                .then()
                .extract().path("mlsId");

        int aProperty = mlsId.get(1);

       // System.out.println("Total number of properties is " +  mlsId.size());
        System.out.println("The property Id is " + aProperty);

        given()
                .pathParam("mlsId", aProperty)
                .when().get("/properties/{mlsId}")
                .then()
                .contentType(ContentType.JSON)
                .body("mlsId", equalTo(aProperty))
                .body("property.style", isOneOf("Traditional", "Ranch", "Ranch, Traditional", "Contemporary/Modern"))
                .statusCode(200);
    }

    @Test
    public void getAnOpenHouse(){

        ArrayList<Integer> mlsId = given()
                .when().get("/openhouses")
                .then()
                .extract().path("listing.mlsId");

        int aProperty = mlsId.get(1);

        System.out.println("Total number of properties is " +  mlsId.size());
        System.out.println("The property Id is " + aProperty);

        given()
                .pathParam("mlsId", aProperty)
                .when().get("/properties/{mlsId}")
                .then()
                .contentType(ContentType.JSON)
                .body("mlsId", equalTo(aProperty))
                .body("property.style", isOneOf("Traditional", "Ranch", "Ranch, Traditional", "Contemporary/Modern", "Other Style"))
                .statusCode(200);}

    @Test
    public void getInvalidProperty(){
        given()
                .pathParam("mlsId", 10051990)
                .when().get("/properties/{mlsId}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(404);
    }

    @Test
    public void complexParsingAndValidation(){

        String response = given().when().get("/properties").asString();
        List<String> propIds = from(response).getList("property.findAll {it.bathsHalf == 6}.bathsFull");

        List<String> Ids = given().when().get("/properties").path("property.findAll {it.bathsHalf == 2}.bathsFull");

        System.out.print(propIds.size());
        System.out.print(Ids.size());
    }

    @Test
    public void testHeaders(){

        Response response = given()
                .when().get("/properties")
                .then()
                .header("Content-Type", equalTo("application/json; charset=utf-8"))
                .extract().response();
        System.out.println(response.headers().toString());
//        System.out.println(response.headers().size());
        System.out.println(response.header("Content-Type"));

        Headers hd = response.getHeaders();
        List<String> hd_values = hd.getValues("Content-Type"); // Multivalue headers
        System.out.println(hd_values.toString());

//        given().auth().basic("simplyrets", "simplyrets")
//                .when().get("/properties")
//                .then()
//                .header("Content-Type", equalTo("application/json; charset=utf-8"));

    }

    @Test
    public void testCookies(){
        Response response = given()
                .when().get("/properties")
                .then()
                .extract().response();

        Map<String, String> allCookies = response.getCookies();
        System.out.println("All cookies are : " + allCookies.toString());

        Cookie detailedCookie = response.getDetailedCookie("");
//        detailedCookie.getComment();
    }

    @Test
    public void testStatus(){
        Response response = given()
                .when().get("/properties")
                .then()
                .extract().response();

        String statusLine = response.getStatusLine();
        int statusCode = response.getStatusCode();
        System.out.println("The status line is : " + statusLine);
        System.out.println("The status code is : " + statusCode);
    }

    @Test
    public void testResponseTime(){
        Response response = given()
                .when().get("/properties")
                .then()
                .extract().response();

        Long respTimeInSeconds = response.timeIn(TimeUnit.SECONDS);
        Long respTime = response.time();
        System.out.println("The api response time is : " + respTime);
        System.out.println("The api response time in SECONDS is : " + respTimeInSeconds);
    }


}
