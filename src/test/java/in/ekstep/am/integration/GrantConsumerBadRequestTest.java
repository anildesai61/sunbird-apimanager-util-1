package in.ekstep.am.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import in.ekstep.am.builders.GrantConsumerRequestBuilder;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class GrantConsumerBadRequestTest {

  @LocalServerPort
  private int serverPort;

  @Rule
  public WireMockRule amAdminApi = new WireMockRule(3010);

  @Before
  public void setUp() throws Exception {
    amAdminApi.resetAll();
  }

  @Test
  public void shouldReturnBadRequestResponseWhenRequestBodyIsEmpty() throws Exception {

    given()
        .port(serverPort)
        .body("")
        .contentType(ContentType.JSON)
        .post("/v1/consumer/consumer_name/grant")
        .then()
        .statusCode(400);
  }

  @Test
  public void shouldReturnBadRequestResponseWhenRequestIsEmpty() throws Exception {

    given()
        .port(serverPort)
        .body(new GrantConsumerRequestBuilder().withParams(null).withNullRequest().build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/consumer_name/grant")
        .then()
        .body("params.status", is("failed"))
        .body("params.err", is("BAD_REQUEST"))
        .body("params.errmsg", is("REQUEST IS MANDATORY"))
        .body("result.username", is(not(nullValue())))
        .body("result.groups", is(not(nullValue()))); //Currently mocked
  }

  @Test
  public void shouldReturnBadRequestResponseWhenGroupIsEmpty() throws Exception {

    given()
        .port(serverPort)
        .body(new GrantConsumerRequestBuilder().withParams(null).withGroups(new String[]{}).build())
        .contentType(ContentType.JSON)
        .post("/v1/consumer/consumer_name/grant")
        .then()
        .body("params.status", is("failed"))
        .body("params.err", is("BAD_REQUEST"))
        .body("params.errmsg", is("GROUPS ARE MANDATORY"))
        .body("result.username", is(not(nullValue())))
        .body("result.groups", is(not(nullValue()))); //Currently mocked
  }

}
