package br.udemy.anne.rest;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.is;

public class WebTest {

    @Test(expected = SAXParseException.class )
    public void deveAcessarAplicacaoWeb() {
        //login
                 String cookie = given()
                         .log().all()
                         .formParam("email","wagner@aquino")
                         .formParam("senha","123456")
                         .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .when()
                         .post("http://seubarriga.wcaquino.me/logar")
                .then()
                         .log().all()
                         .statusCode(200)
                         .extract().header("set-cookie");
                 ;
                 cookie = cookie.split("=")[1].split(";")[0]; // salvando apena o cookie
        //obter conta
                           String body =  given()
                                .log().all()
                                     .cookie("connect.sid", cookie)
                            .when()
                                .get("http://seubarriga.wcaquino.me/contas")
                            .then()
                                     .log().all()
                                     .statusCode(200)
                                     .contentType(ContentType.HTML)
                                     //.body("html.body.table.tbody.tr[0].td[0]", is("Conta para movimentacoes"))
                                   .body(hasXPath("//tbody/tr[17]/td[1]", is("conta teste alterada")))
                                     .extract().body().asString();
                           ;

                           XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, body);
                           System.out.println(xmlPath.getString(body));

    }
}
