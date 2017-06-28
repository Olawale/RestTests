package RestTests;

import io.restassured.http.ContentType;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.core.IsEqual.equalTo;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import io.restassured.module.jsv.JsonSchemaValidator.*;

public class RestApiTest extends BaseTest{

    @Test
    public void getAllProperties(){
        given().auth().basic("simplyrets", "simplyrets")
                .when().get("/properties")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200);
    }

    @Test
    public void getAProperty(){

        ArrayList<Integer> mlsId = given().auth().basic("simplyrets", "simplyrets")
                .when().get("/properties")
                .then()
                .extract().path("mlsId");

        int aProperty = mlsId.get(18);

        System.out.println("Total number of properties is " +  mlsId.size());
        System.out.println("The property Id is " + aProperty);

        given().auth().basic("simplyrets", "simplyrets")
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

        ArrayList<Integer> mlsId = given().auth().basic("simplyrets", "simplyrets")
                .when().get("/openhouses")
                .then()
                .extract().path("listing.mlsId");

        int aProperty = mlsId.get(1);

        System.out.println("Total number of properties is " +  mlsId.size());
        System.out.println("The property Id is " + aProperty);

        given().auth().basic("simplyrets", "simplyrets")
                .pathParam("mlsId", aProperty)
                .when().get("/properties/{mlsId}")
                .then()
                .contentType(ContentType.JSON)
                .body("mlsId", equalTo(aProperty))
                .body("property.style", isOneOf("Traditional", "Ranch", "Ranch, Traditional", "Contemporary/Modern", "Other Style"))
                .statusCode(200);}

    @Test
    public void getInvalidProperty(){
        given().auth().basic("simplyrets", "simplyrets")
                .pathParam("mlsId", 10051990)
                .when().get("/properties/{mlsId}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(404);
    }

    @Test
    public void complexParsingAndValidation(){

        String response = given().auth().basic("simplyrets", "simplyrets").when().get("/properties").asString();
        List<String> propIds = from(response).getList("property.findAll {it.bathsHalf == 6}.bathsFull");

        List<String> Ids = given().auth().basic("simplyrets", "simplyrets").when().get("/properties").path("property.findAll {it.bathsHalf == 2}.bathsFull");

        System.out.print(propIds.size());
        System.out.print(Ids.size());
    }


}
