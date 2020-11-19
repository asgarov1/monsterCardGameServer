package at.fhtw.bif3.controller;

import at.fhtw.bif3.domain.Bundle;
import at.fhtw.bif3.domain.Card;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.BundleService;
import at.fhtw.bif3.service.CardService;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class PackageController implements Controller {

    private final CardService cardService = new CardService();
    private final BundleService bundleService = new BundleService();
    public static final String PACKAGES_ENDPOINT = "/packages";

    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        }
        return notFound();
    }

    private HttpResponse handlePost(Request request) {
        if (request.getUrl().getPath().equals(PACKAGES_ENDPOINT)) {
            return handlePackagePost(request);
        }
        return notFound();
    }

    private HttpResponse handlePackagePost(Request request) {
        List<Card> cards = extractCards(request.getContentString());
        cards.forEach(cardService::create);
        bundleService.create(Bundle.builder().cards(cards).id(Integer.toString(this.hashCode())).build());
        return noContent();
    }

    public List<Card> extractCards(String contentString) {
        return Arrays.asList(new GsonBuilder().create().fromJson(contentString, Card[].class));
    }
}
