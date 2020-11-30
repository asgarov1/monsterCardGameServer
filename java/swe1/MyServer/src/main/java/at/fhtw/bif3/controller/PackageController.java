package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.dto.CardDTO;
import at.fhtw.bif3.domain.Bundle;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.domain.card.CardClass;
import at.fhtw.bif3.domain.card.ElementType;
import at.fhtw.bif3.domain.card.SpellCard;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.service.BundleService;
import at.fhtw.bif3.service.CardService;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        return created();
    }

    public List<Card> extractCards(String contentString) {
        return Arrays.stream(new GsonBuilder().create().fromJson(contentString, CardDTO[].class))
                .map(this::instantiateCard)
                .collect(Collectors.toList());
    }

    private Card instantiateCard(CardDTO cardDTO) {
        CardClass cardClass = CardClass.assignByName(cardDTO.getName());

        Card card = cardClass.instantiateByType();
        card.setId(cardDTO.getId());
        card.setName(cardDTO.getName());
        card.setElementType(ElementType.assignByName(cardDTO.getName()));
        card.setDamage(cardDTO.getDamage());

        if(cardDTO.getWeakness() != null){
            ((SpellCard) card).setWeakness(cardDTO.getWeakness());
        }

        return card;
    }
}
