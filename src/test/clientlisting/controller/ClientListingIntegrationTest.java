package clientlisting.controller;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import PIIApplication;

import jline.internal.Log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PIIApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class ClientListingIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testEndpointNoParam() throws Exception {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> response = testRestTemplate
                .getForEntity(urlBuilder(""), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testEndpointSingleClientWithHeader() throws Exception {
        String customerID = "01S2585257E";
        HttpHeaders headers = new HttpHeaders();
        JSONObject obj = new JSONObject();
        obj.put("relId", customerID);
        headers.add("CSL_USER", obj.toString());
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange(urlBuilder(""), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThatJson(response.getBody()).node("data[0].type").isStringEqualTo("clients");
    }

    @Test
    public void testEndpointSingleClientWithQueryParam() throws Exception {
        String customerID = "01S2585257E";
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> response = testRestTemplate
                .getForEntity(urlBuilder("filter[clients][custId]=" + customerID), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThatJson(response.getBody()).node("data").isPresent();
    }

    private String urlBuilder(String uri) {
        return "http://localhost:" + this.port + "/clients?" + uri;
    }

}