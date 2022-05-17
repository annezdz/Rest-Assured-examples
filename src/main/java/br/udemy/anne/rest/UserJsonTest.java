package br.udemy.anne.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {

    @Test
    public void deveVerificarPrimeiroNivel() {
                given()
                .when()
                        .get("http://restapi.wcaquino.me/users/1")
                .then()
                        .statusCode(200)
                        .body("id", is(1))
                        .body("name", containsString("Silva"))
                        .body("age", greaterThan(18));
    }

    @Test
    public void deveVerificarPrimeiroNivelOutrasFormas() {
        Response response = RestAssured.request(Method.GET,"http://restapi.wcaquino.me/users/1");
        // USANDO O PATH

        Assert.assertEquals(new Integer(1), response.path("id"));
        Assert.assertEquals(new Integer(1), response.path("%s","id")); // enviando o id via parametro

        //USANDO JSONPATH

        JsonPath jpath = new JsonPath(response.asString());
        Assert.assertEquals(1, jpath.getInt("id"));

        // USANDO O FROM

        int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1,id);
    }

    @Test
    public void deveVerificarJsonSegundoNivel() {
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/2")
                .then()
                .statusCode(200)
                .body("name", containsString("Joaquina"))
                .body("endereco.rua",is("Rua dos bobos"))
                .body("age", greaterThan(18));
    }

    @Test
    public void deveVerificarLista() {
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/3")
                .then()
                .statusCode(200)
                .body("name", containsString("Ana"))
                .body("filhos", hasSize(2))
                .body("filhos[0].name", is("Zezinho"))
                .body("filhos[1].name", is("Luizinho"))
                .body("filhos.name",hasItem("Luizinho"))
                .body("filhos.name", hasItems("Luizinho","Zezinho"))
        ;
    }

    @Test
    public void deveRetornarUsuarioInexistente() {
        given()
                .when()
                .get("http://restapi.wcaquino.me/users/4")
                .then()
                .statusCode(404)
                .body("error", is("Usuário inexistente"));
    }

    @Test
    public void deveriaVerificarListaRaiz() {
        given()
                .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(3)) // validando tamanho da lista
                .body("name", hasItems("João da Silva","Maria Joaquina","Ana Júlia"))
                .body("age[1]",is(25)) // validando a idade do segundo elemento
                .body("filhos.name",hasItem(Arrays.asList("Zezinho", "Luizinho")))
                .body("salary", contains(1234.5678F, 2500, null));
    }

    @Test
    public void devoFazerVerificacoesAvancadas() {
        given()
         .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(3)) // validando tamanho da lista
                .body("age.findAll{it <= 25}.size()", is(2))
                .body("age.findAll{it <= 25 && it > 20}.size()", is(1))
                .body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
                .body("findAll{it.age <= 25}.name[0]", is("Maria Joaquina"))
                .body("find{it.age <= 25}.name", is("Maria Joaquina"))
                .body("findAll{it.age <= 25}.name[-1]", is("Ana Júlia"))
                .body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
                .body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))
                .body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
                .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}",hasItem("MARIA JOAQUINA"))
                .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()"
                        ,allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
                .body("age.collect{it * 2}", hasItems(60, 50, 40))
                .body("id.max()", is(3))
                .body("salary.min()", is(1234.5677F))
                .body("salary.findAll{it != null}.sum()",is(closeTo(3734.5678F, 0.001)))
                .body("salary.findAll{it != null}.sum()",allOf(greaterThan(3000D), lessThan(5000D)));
        ;
    }

    @Test
    public void devoUnirJsonPathEJava() {

        ArrayList<String> names =
        given()
                .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .extract().path("name.findAll{it.startsWith('Maria')}")
    ;
        Assert.assertEquals(1, names.size());
        Assert.assertEquals(names.get(0).toUpperCase(),"maria joaquina".toUpperCase());

    }
}
