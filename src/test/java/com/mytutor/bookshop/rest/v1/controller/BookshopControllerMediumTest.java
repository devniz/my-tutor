package com.mytutor.bookshop.rest.v1.controller;

import com.mytutor.bookshop.BookshopApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class BookshopControllerMediumTest {

    @BeforeAll
    static void setUp() {
        RestAssured.port = 8080;
        RestAssured.basePath = "/";
        BookshopApplication.main(new String[]{});
    }

    @Test
    @DisplayName("/POST to order endpoint")
    void itShouldOrderWithSuccess() {
        given().log().all()
                .when()
                .param("bookType", "A")
                .param("quantity", 4)
                .basePath("/api/v1/shop")
                .post("/order")
                .then().log().all()
                .body(equalTo("Thank you for your purchase!"))
                .assertThat().statusCode(200);
    }

    @Test
    @DisplayName("/POST to order endpoint with failure 1")
    void itShouldReturnHttpNotAcceptableIfQuantityIsBiggerThanStock() {
        given().log().all()
                .when()
                .param("bookType", "D")
                .param("quantity", 100)
                .basePath("/api/v1/shop")
                .post("/order")
                .then()
                .assertThat().statusCode(406);
    }

    @Test
    @DisplayName("/POST to order endpoint with failure 2")
    void itShouldReturnOutOfStockError() {
        given().log().all()
                .when()
                .param("bookType", "E")
                .param("quantity", 10)
                .basePath("/api/v1/shop")
                .post("/order")
                .then()
                .body(equalTo("Thank you for your purchase!"))
                .assertThat().statusCode(200);
        given().log().all()
                .when()
                .param("bookType", "E")
                .param("quantity", 1)
                .basePath("/api/v1/shop")
                .post("/order")
                .then()
                .body(equalTo("Sorry, we are out of stock."))
                .assertThat().statusCode(200);
    }

    @Test
    @DisplayName("/GET to report endpoint")
    void itShouldGetTheShopReportWithSuccess() {
        given().log().all()
                .when()
                .basePath("/api/v1/shop")
                .get("/report")
                .then().log().all()
                .assertThat().statusCode(200);
    }

}