package RestTests;

import  io.restassured.RestAssured;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.basic;

public class BaseTest {


    @BeforeClass
    public static void setUp(){

//        String port = System.getProperty("server.port");
//        if (port == null){
//            System.out.println("The port env var is " + port);
//            RestAssured.port = Integer.valueOf(8080);
//        }
//        else{
//            System.out.println("The port env var is " + port);
//            RestAssured.port = Integer.valueOf(port);
//        }

//        String baseHost = System.getProperty("server.host");
//        if(baseHost == null){
//            System.out.println("The host env var is " + baseHost);
//            baseHost = "";
//        }
//        System.out.println("The host env var is " + baseHost);
//        RestAssured.baseURI = baseHost;
        RestAssured.baseURI = "https://api.simplyrets.com/";
        RestAssured.authentication = basic("simplyrets", "simplyrets");
        RestAssured.useRelaxedHTTPSValidation();
    }
}
