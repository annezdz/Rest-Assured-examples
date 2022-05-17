package br.udemy.anne.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.containsString;

public class EnvioDadosTeste {

    @Test
    public void deveEnviarValorViaQuery() {
                given()
                        .log().all()
                .when()
                        .get("https://restapi.wcaquino.me/v2/users?format=json")
                .then()
                        .log().all()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                ;
    }

    @Test
    public void deveEnviarViaParam() {
        given()
                .log().all()
                .queryParam("format","xml")
                .when()
                .get("https://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
                .contentType(containsString("utf-8"))
        ;
    }

    @Test
    public void deveEnviarViaHeader() {
                 given()
                         .log().all()
                         .accept(ContentType.JSON)
                .when()
                         .get("https://restapi.wcaquino.me/v2/users")
                .then()
                         .log().all()
                         .statusCode(200)
                         .contentType(ContentType.JSON)
        ;
    }
}
