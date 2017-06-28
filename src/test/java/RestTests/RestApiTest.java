package RestTests;

import io.restassured.http.ContentType;
import org.junit.Test;
import java.util.ArrayList;
import static io.restassured.RestAssured.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

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
                .statusCode(200);
    }

    @Test
    public void getInvalidProperty(){
        given().auth().basic("simplyrets", "simplyrets")
                .pathParam("mlsId", 10051990)
                .when().get("/properties/{mlsId}")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(404);
    }


}
