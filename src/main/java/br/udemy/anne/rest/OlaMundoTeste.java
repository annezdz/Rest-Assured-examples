package br.udemy.anne.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class OlaMundoTeste {

    @Test
    public void testeOlaMundo() {
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue(response.getStatusCode() == 200);
        Assert.assertEquals(response.getStatusCode(), 200);
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    //Outra forma de testar
    @Test
    public void devoConhecerOutrasFormasRestAssured() {
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get("http://restapi.wcaquino.me/ola")
                .then()
                .statusCode(200);
    }

    //Modo Fluent
    @Test
    public void devoConhecerOFormaFluent() {
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
                given()
                        //PréCondiçoes
                .when() //Ação
                    .get("http://restapi.wcaquino.me/ola")
                .then() //Assertiva
                        // .assertThat()
                .statusCode(200);
    }

    @Test
    public void devoConhecerMatcherHamcrest() {
        assertThat("Maria", is("Maria"));
        assertThat(128, is(128));
        assertThat(128, isA(Integer.class)); //validando tipo de dado
        assertThat(128D, isA(Double.class)); //validando tipo de dado
        assertThat(128D, greaterThan(120D)); //validando maior numero
        assertThat(128D, lessThan(130D)); //validando menor numero

        List<Integer> impares = Arrays.asList(1,3,5,7,9);
        assertThat(impares, hasSize(5)); // validando tamanho da lista
        assertThat(impares, contains(1,3,5,7,9)); // Tem que seguir a ordem, senao dá erro
        assertThat(impares, containsInAnyOrder(1,3,5,7,9)); // Não precisa seguir ordem, porém necessita
        // que contenha todos os elementos da lista
        assertThat(impares, hasItem(1));
        assertThat(impares, hasItems(1, 5));

        assertThat("Maria", is(not("Joao"))); // is é opcional
        assertThat("Maria", not("Joao"));
        assertThat("Maria", anyOf(is("Maria"), is("Joaquina"))); // equivale ao OU
        assertThat("Joaquina", allOf(startsWith("Joa"), endsWith("ina"), containsString("qui"))); // equivale ao E
    }

    @Test
    public void devoValidarBody() {
        given()
                .when()
                .get("http://restapi.wcaquino.me/ola")
                .then()
                .statusCode(200)
                .body(is("Ola Mundo!"))
                .body(containsString("Mundo"))
                .body(is(notNullValue()));
    }
}
