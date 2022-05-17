package br.udemy.anne.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

public class AuthTest {

    @Test
    public void deveAcessarSWAPI() {
                given()
                        .log().all()
                .when()
                        .get("https://swapi.dev/api/people/1")
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("name", is("Luke Skywalker"));
    }

    @Test
    public void deveObterClima() {
                given()
                        .log().all()
                        .queryParam("q","Blumenau")
                        .queryParam("appid","5da4e4e4755659fc010bf4da70853148")
                        .queryParam("units","metric")
                .when()
                        .get("https://api.openweathermap.org/data/2.5/forecast")

                .then()
                        .log().all()
                        .statusCode(200)
                        .body("temp_min",is(20.45))
                ;
    }

    @Test
    public void naoDeveAcessarSemSenha() {
                 given()
                     .log().all()
                .when()
                    .get("https://restapi.wcaquino.me/basicauth")
                .then()
                         .log().all()
                         .statusCode(401)
                ;
    }

    @Test
    public void deveFazerAutenticacao() {
        given()
                .log().all()
                .when()
                .get("https://admin:senha@restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status",is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacao2() {
                given()
                        .log().all()
                        .auth().basic("admin","senha")
                .when()
                        .get("https://restapi.wcaquino.me/basicauth")
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("status",is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoComChallenge() {
        given()
                .log().all()
                .auth().preemptive().basic("admin","senha")
                .when()
                .get("https://restapi.wcaquino.me/basicauth2")
                .then()
                .log().all()
                .statusCode(200)
                .body("status",is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoComToken() {

        Map<String,String> login = new HashMap<>();
        login.put("email","annezdz@hotmail.com");
        login.put("senha","123456");

        //Login na API
        // Receber o token
        String token = given()
                    .log().all()
                .body(login)
                .contentType(ContentType.JSON)
                .when()
                    .post("https://barrigarest.wcaquino.me/signin")
                .then()
                    .log().all()
                    .statusCode(200)
                .extract().path("token");

        //Obter as contas

        given()
                     .log().all()
                     .header("Authorization","JWT " + token)
                     .body(login)
                     .contentType(ContentType.JSON)
                .when()
                     .post("https://barrigarest.wcaquino.me/contas")
                .then()
                     .log().all()
                .statusCode(200)
                .body("nome", hasItem("conta teste"));
    }
}
