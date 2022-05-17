package br.udemy.anne.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {

    public static void main(String[] args) {

        //Fazendo o Request
        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");

        //Imprimindo o corpo da mensagem
        System.out.println(response.getBody().asString());

        //Status Code no Rest Assured

        System.out.println(response.getStatusCode());

        //Primeiro teste

        ValidatableResponse validacao = response.then();
        validacao.statusCode(201);


    }
}
