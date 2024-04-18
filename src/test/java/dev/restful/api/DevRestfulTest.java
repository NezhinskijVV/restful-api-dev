package dev.restful.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.restful.api.model.Data;
import dev.restful.api.model.Product;
import dev.restful.api.model.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.restful.api.specs.Specification.requestSpecification;
import static dev.restful.api.specs.Specification.responseSpecification;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DevRestfulTest {

    @Test
    public void shouldHaveCorrectMethodGetTest() {
        given()
                .spec(requestSpecification())
                .when()
                .get()
                .then()
                .spec(responseSpecification());
    }

    @Test
    public void shouldHaveCorrectMethodPostTest() {
        given()
                .spec(requestSpecification())
                .when()
                .contentType(JSON)
                .body(
                        "{\n" +
                                "   \"name\": \"Apple MacBook Pro 16\",\n" +
                                "   \"data\": {\n" +
                                "      \"year\": 2019,\n" +
                                "      \"price\": 1849.99,\n" +
                                "      \"CPU model\": \"Intel Core i9\",\n" +
                                "      \"Hard disk size\": \"1 TB\"\n" +
                                "   }\n" +
                                "}")
                .post()
                .then()
                .spec(responseSpecification());
    }

    @Test
    public void shouldHaveCorrectMethodPost2Test() throws JsonProcessingException {
        Data data = new Data(2019, 18.05, "Intel Core i9", "1TB");
        Product product = new Product("Apple MacBook Pro 16", data);

        String productJson = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(product);

        ExtractableResponse<Response> response = given()
                .spec(requestSpecification())
                .when()
                .contentType(JSON)
                .body(productJson)
                .post()
                .then()
                .log().all()
                .statusCode(200)
                .extract();

//        List<Product> list = response.body().jsonPath().getList("..", Product.class);
//
//        assertAll(
//                () -> assertEquals("Apple MacBook Pro 16", response.jsonPath().get("name"), "Некорректное имя"),
//                () -> assertEquals(Integer.valueOf(2019), response.jsonPath().get("data.year"), "Некорретный год")
//        );

        ProductResponse product2 = response.body().jsonPath().getObject("", ProductResponse.class);
        System.out.println("product2 = " + product2);
    }
}