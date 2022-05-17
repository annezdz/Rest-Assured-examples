package br.udemy.anne.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class XMLTest {

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
        resBuilder.expectStatusCode(200);
        resSpec = resBuilder.build();

        RestAssured.requestSpecification = reqSpec;
        RestAssured.responseSpecification = resSpec;
    }


    @Test
    public void devoTrabalharComXML() {

        given()
                .when()
               // .get("https://restapi.wcaquino.me/usersXML/3")
                .get("/usersXML/3")
                .then()
                .rootPath("user")
                .body("name", is("Ana Julia"))
                .body("@id",is("3"))

                .rootPath("user.filhos")
                .body("name.size()",is(2))

                .detachRootPath("filhos")
                .body("filhos.name[0]", is("Zezinho"))
                .body("filhos.name[1]", is("Luizinho"))

                .appendRootPath("filhos")
                .body("name", hasItem("Zezinho"))
                .body("name", hasItems("Zezinho", "Luizinho"))
        ;
    }

    @Test
    public void devoFazerPesquisasAvancadasComXML() {
        given()
                .when()
                .get("/usersXML")
                .then()
        //descobrir quantos usuarios tem na lista
                .body("users.user.size()",is(3))
        //Quantos usuarios tem 25 anos ou -
                .rootPath("users.user")
                .body("findAll{it.age.toInteger() <= 25}.size()",is(2))
                .body("@id", hasItems("1","2","3"))
        //Encontrar usuarios com 25 anos
                .body("find{it.age.toInteger() == 25}.name",is("Maria Joaquina"))
        //Nomes que contém n
                .body("findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina","Ana Julia"))
                //Como String
                .body("salary.find{it != null}",is("1234.5678"))
        //Como Double
                .body("salary.find{it != null}.toDouble()",is(1234.5678D))
                //Multiplicando as idades por 2
                .body("age.collect{it.toInteger() * 2}", hasItems(40,50,60))
                //Usando o find e o collect para passar para UpperCase
                .body("name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}.toArray()"
                        ,allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)));
    }

    @Test
    public void devoFazerPesquisasAvancadasComXMLEJava() {
        //String nome = given()
        ArrayList<NodeImpl> nomes = given()
                .when()
                .get("/usersXML")
                .then()
               // .extract().path("users.user.name.findAll{it.toString().startsWith('Maria')}");
                .extract().path("users.user.name.findAll{it.toString().contains('n')}");
        Assert.assertEquals(2, nomes.size());
        Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
        Assert.assertTrue("Ana Julia".equalsIgnoreCase(nomes.get(1).toString()));

        //Assert.assertEquals("Maria Joaquina".toUpperCase(), nome.toUpperCase());
    }


    @Test
    public void devoFazerPesquisasAvancadasComXpath() {
                 given()
                .when()
                .get("/usersXML")
                .then()
                         .body(hasXPath("count(/users/user)",is("3")))
                         .body(hasXPath("/users/user[@id = '1']"))
                         .body(hasXPath("//user[@id = '1']"))
                         .body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
                         .body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos",
                                 allOf(containsString("Zezinho"), containsString("Luizinho"))))
                         //Retornar o primeiro name encontrado
                         .body(hasXPath("/users/user/name", is("João da Silva")))
                 //Pode ser assim também
                         .body(hasXPath("//name", is("João da Silva")))
                 //Para encontrar o segundo nome
                         .body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
                 //Encontrando o nome do último registro da lista
                         .body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
                //Contar todos nomes que contém n
                         .body(hasXPath("count(/users/user/name[contains(.,'n')])", is("2")))
                 //Encontrar usuários cuja idade seja menor que 24
                         .body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
                 //encontrar usuários com menos de 20 e menos de 30 anos
                         .body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
                 //outra forma de resolver a busca acima
                         .body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))
                 ;
    }
    }
