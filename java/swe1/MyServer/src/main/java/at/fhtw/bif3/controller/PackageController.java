package at.fhtw.bif3.controller;

import at.fhtw.bif3.domain.Card;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.CardService;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class PackageController implements Controller {

    private final CardService cardService = new CardService();
    public static final String PACKAGES_ENDPOINT = "/packages";

    @Override
    public HttpStatus handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        }

        return HttpStatus.NOT_FOUND;
    }

    private HttpStatus handlePost(Request request) {
        if (request.getUrl().getPath().equals(PACKAGES_ENDPOINT)) {
            return handlePackagePost(request);
        }
        return HttpStatus.NOT_FOUND;
    }

    private HttpStatus handlePackagePost(Request request) {
        List<Card> cards = extractCards(request.getContentString());
        cards.forEach(cardService::create);
        return HttpStatus.NO_CONTENT;
    }

    private List<Card> extractCards(String contentString) {
        return Arrays.asList(new Gson().fromJson(contentString, Card[].class));
    }
}
