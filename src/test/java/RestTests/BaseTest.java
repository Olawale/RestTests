package RestTests;

import  io.restassured.RestAssured;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.basic;

public class BaseTest {


    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI = "https://api.simplyrets.com/";
        RestAssured.authentication = basic("simplyrets", "simplyrets");
        RestAssured.useRelaxedHTTPSValidation();
    }
}
