package at.fhtw.bif3.controller;

import at.fhtw.bif3.dao.BundleCardDAO;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.Response;
import at.fhtw.bif3.service.BundleService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static at.fhtw.bif3.http.response.HttpStatus.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PackageControllerTest {

    String createPackagesRequest = "POST /packages HTTP/1.1\n" +
            "Content-Type: application/json\n" +
            "User-Agent: PostmanRuntime/7.26.8\n" +
            "Accept: */*\n" +
            "Postman-Token: 8e204b3d-0b76-40df-9923-f439e2c75a0d\n" +
            "Host: localhost:10001\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 441\n" +
            "[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0},{\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0},{\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Weakness\": 45.0, \"Damage\": 20.0},{\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45.0},{\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\", \"Weakness\": 45.0, \"Damage\": 25.0}]";

    Request httpRequest;

    @BeforeEach
    void init() {
        try {
            var inputStream = new ByteArrayInputStream(createPackagesRequest.getBytes(StandardCharsets.UTF_8));
            httpRequest = HttpRequest.valueOf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void cleanUp() {
        String bundle_id = new BundleCardDAO().findByField("card_id", "845f0dc7-37d0-426e-994e-43fc3ac83c08").getBundleId();
        new BundleService().deleteWithCards(bundle_id);
    }

    @Test
    public void createPackagesShouldWork() {
        BundleService bundleService = new BundleService();
        int bundlesBefore = bundleService.countEntities();

        Response httpResponse = new BundleController().handleRequest(httpRequest);
        assertEquals(CREATED.getCode(), httpResponse.getStatusCode());
        assertEquals(bundlesBefore + 1, bundleService.countEntities());
    }

    @Test
    public void extractCardsShouldWork() {
        String contentString = "[{\"Id\":\"67f9048f-99b8-4ae4-b866-d8008d00c53d\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, " +
                "{\"Id\":\"aa9999a0-734c-49c6-8f4a-651864b14e62\", \"Name\":\"RegularSpell\", \"Weakness\": 45.0, \"Damage\": 50.0}, " +
                "{\"Id\":\"d6e9c720-9b5a-40c7-a6b2-bc34752e3463\", \"Name\":\"Knight\", \"Damage\": 20.0}, " +
                "{\"Id\":\"02a9c76e-b17d-427f-9240-2dd49b0d3bfd\", \"Name\":\"RegularSpell\", \"Weakness\": 45.0, \"Damage\": 45.0}, " +
                "{\"Id\":\"2508bf5c-20d7-43b4-8c77-bc677decadef\", \"Name\":\"FireElf\", \"Damage\": 25.0}]";

        var cards = new BundleController().extractCards(contentString);
        assertEquals(5, cards.size());
        cards.forEach(card -> assertNotNull(card.getId()));
        cards.forEach(card -> assertNotNull(card.getName()));
    }
}