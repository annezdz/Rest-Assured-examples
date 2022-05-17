package br.udemy.anne.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public class VerbsTest {

    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://restapi.wcaquino.me";
        RestAssured.port = 443;
//        RestAssured.basePath = "/v2";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        reqSpec = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
//        resBuilder.expectStatusCode(201);
        resBuilder.log(LogDetail.ALL);
        resSpec = resBuilder.build();

        RestAssured.requestSpecification = reqSpec;
        RestAssured.responseSpecification = resSpec;
    }

    @Test
    public void deveSalvarUsuario() {
                given()
                        .contentType("application/json")
                        .body("{ \"name\": \"Anne\", \"age\" : 36 }")
                .when()
                    .post("/users")
                .then()
                        .log().all()
                        .body("id", is(notNullValue()))
                        .body("name", is("Anne"))
                        .body("age", is(36))
                ;
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome() {
                given()
                    .contentType("application/json")
                    .body("{\"age\" : 36 }")
                .when()
                    .post("/users")
                .then()
                        .log().all()
                        .statusCode(400)
                        .body("id", is(nullValue()))
                        .body("error",is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void deveSalvarUsuarioViaXML() {
        given()
                .contentType(ContentType.XML)
                .body("<user><name>Anne</name><age>36</age></user>")
                .when()
                .post("/usersXML")
                .then()
                .log().all()
                .body("user.@id", is(notNullValue()))
                .body("user.name", is("Anne"))
                .body("user.age.toInteger()", is(36))
        ;
    }

    @Test
    public void deveAlterarUsuario() {
        given()
                .contentType("application/json")
                .body("{ \"name\": \"Dudu\", \"age\" : 4 }")
                .when()
                .put("/users/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Dudu"))
                .body("age", is(4))
                .body("salary",is(1234.5678F))
        ;
    }

    @Test
    public void devoCustomizarURL() {
        given()
                .contentType("application/json")
                .body("{ \"name\": \"Dudu\", \"age\" : 4 }")
                .when()
                .put("/{entidade}/{userId}","users","1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Dudu"))
                .body("age", is(4))
                .body("salary",is(1234.5678F))
        ;
    }

    @Test
    public void devoCustomizarURLPt2() {
        given()
                    .contentType("application/json")
                    .body("{ \"name\": \"Dudu\", \"age\" : 4 }")
                    .pathParam("entidade","users")
                    .pathParam("userId","1")
                .when()
                     .put("/{entidade}/{userId}")
                .then()
                     .log().all()
                     .statusCode(200)
                     .body("id", is(1))
                     .body("name", is("Dudu"))
                     .body("age", is(4))
                     .body("salary",is(1234.5678F))
        ;
    }

    @Test
    public void deveRemoverUsuario() {
                given()
                        .log().all()
                .when()
                        .delete("/users/1")
                .then()
                        .log().all()
                        .statusCode(204)

                        ;
    }

    @Test
    public void naoDeveRemoverUsuario() {
        given()
                    .log().all()
                .when()
                    .delete("/users/1000")
                .then()
                    .log().all()
                    .statusCode(400)
                .body("error", is("Registro inexistente"))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoMAP() {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Usuario via MAP");
        params.put("age", 25);

        given()
                .contentType("application/json")
                .body(params)
                .when()
                .post("/users")
                .then()
                .log().all()
                .body("id", is(notNullValue()))
                .body("name", is("Usuario via MAP"))
                .body("age", is(25))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto() {

       User user = new User("Usuario via objeto", 35, 123.99);

        given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/users")
                .then()
                .log().all()
                .body("id", is(notNullValue()))
                .body("name", is("Usuario via objeto"))
                .body("age", is(35))
                .body("salary",is(123.99F))
        ;
    }

    @Test
    public void deveDeserializarObjetoAoSalvarUsuarioUsandoObjeto() {

        User user = new User("Usuario deserializado", 35, 123.99);

        User usuarioInserido = given()
                .contentType("application/json")
                .body(user)
                .when()
                .post("/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class);
        System.out.println(usuarioInserido);
        Assert.assertThat(usuarioInserido.getId(), is(notNullValue()));
        Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(35));
        Assert.assertThat(usuarioInserido.getSalary(), is(123.99));
        ;
    }

//    @Test
//    public void deveSalvarUsuarioViaXMLUsandoObjeto() {
//
//        User user = new User("Eduardo",4,29.00);
//        given()
//                .contentType(ContentType.XML)
//                .body(user)
//                .when()
//                .post("/usersXML")
//                .then()
//                .log().all()
//                .body("user.@id", is(notNullValue()))
//                .body("user.name", is("Eduardo"))
//                .body("user.age.toInteger()", is(4))
//                .body("user.salary.toDouble()",is(29.00))
//        ;
//    }

//    @Test
//    public void deveDeserializarXMLUsandoObjeto() {
//
//        User user = new User("Eduardo",4,29.00);
//        User usuarioInserido = given()
//                .contentType(ContentType.XML)
//                .body(user)
//                .when()
//                .post("/usersXML")
//                .then()
//                .log().all()
//                .extract().body().as(User.class)
//        ;
//        Assert.assertThat(usuarioInserido.getId(), is(notNullValue()));
//        Assert.assertThat(usuarioInserido.getName(), is("Eduardo"));
//        Assert.assertThat(usuarioInserido.getAge(), is(4));
//    }
}



