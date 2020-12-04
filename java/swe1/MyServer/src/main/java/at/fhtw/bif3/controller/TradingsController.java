package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.controller.dto.TradingDTO;
import at.fhtw.bif3.dao.exception.EntityNotFoundException;
import at.fhtw.bif3.domain.TradingDeal;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.CardService;
import at.fhtw.bif3.service.TradingDealService;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import static at.fhtw.bif3.http.Header.AUTHORIZATION;
import static at.fhtw.bif3.http.response.ContentType.APPLICATION_JSON;
import static at.fhtw.bif3.util.StringUtil.extractToken;
import static at.fhtw.bif3.util.StringUtil.extractUsernameFromToken;
import static java.util.stream.Collectors.toList;

public class TradingsController implements Controller {

    public static final String TRADINGS_ENDPOINT = "/tradings";
    private final TradingDealService tradingService = new TradingDealService();
    private final UserService userService = new UserService();

    @Override
    public HttpResponse handleRequest(Request request) {
        try {
            String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
            if (SessionContext.tokenNotPresent(token)) {
                return forbidden();
            }
        } catch (NullPointerException e) {
            return forbidden();
        }

        if (request.getMethod().equals(HttpMethod.GET.name())) {
            return handleGet();
        } else if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        } else if (request.getMethod().equals(HttpMethod.DELETE.name())) {
            return handleDelete(request);
        }
        return notFound();
    }

    private HttpResponse handleGet() {
        List<TradingDTO> tradingDTOs = tradingService
                .findAll()
                .stream()
                .map(TradingDeal::createDTO)
                .collect(toList());

        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .content(new Gson().toJson(tradingDTOs)).build();
    }

    private HttpResponse handlePost(Request request) {
        String[] segments = request.getUrl().getSegments();
        if (segments.length == 1) {
            return handlePostCreate(request);
        } else if (segments.length == 2) {
            return handlePostTrade(request);
        } else {
            return notFound();
        }
    }

    private HttpResponse handlePostCreate(Request request) {
        String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        TradingDTO tradingDTO = new GsonBuilder().create().fromJson(request.getContentString(), TradingDTO.class);
        User user = new UserService().findByUsername(SessionContext.getUsernameForToken(token));
        try {
            Card cardToTrade = user.getCards()
                    .stream()
                    .filter(card -> card.getId().equals(tradingDTO.getCardToTradeId()))
                    .findFirst()
                    .orElseThrow(EntityNotFoundException::new);

            user.lockCard(cardToTrade);
            TradingDeal tradingDeal = new TradingDeal(tradingDTO.getId(), cardToTrade, cardToTrade.getType(), tradingDTO.getMinimumDamage(), user);
            tradingService.create(tradingDeal);
            userService.update(user);
            return noContent();
        } catch (EntityNotFoundException e) {
            return badRequest("User " + user.getUsername() + " doesn't possess this card");
        }
    }

    private HttpResponse handlePostTrade(Request request) {
        String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        String username = SessionContext.getUsernameForToken(token);
        TradingDeal deal = tradingService.findById(request.getUrl().getSegments()[1]);
        if (deal.getCreator().getUsername().equals(username)) {
            return badRequest("Can't trade with yourself!");
        } else {
            String dealAcceptersCardId = new Gson().fromJson(request.getContentString(), String.class);
            return processTrade(deal, username, dealAcceptersCardId);
        }
    }

    private HttpResponse processTrade(TradingDeal deal, String username, String dealAcceptersCardId) {
        User dealCreator = userService.findByUsername(deal.getCreator().getUsername());

        User dealAccepter = userService.findByUsername(username);
        Card dealAcceptersCard = new CardService().findById(dealAcceptersCardId);

        if (dealAcceptersCard.getDamage() < deal.getMinimumDamage() || dealAcceptersCard.getType() != deal.getType()) {
            return badRequest("Offered card doesn't fit the trade requirements: minimum damage =" + deal.getMinimumDamage() +
                    ", type " + deal.getType());
        }

        dealCreator.unlockCard(deal.getCardToTrade());
        userService.transferCard(dealAccepter, dealCreator, dealAcceptersCard);
        userService.transferCard(dealCreator, dealAccepter, deal.getCardToTrade());
        tradingService.delete(deal.getId());
        return noContent();
    }

    private HttpResponse handleDelete(Request request) {
        String[] segments = request.getUrl().getSegments();
        if (segments.length != 2) {
            return notFound();
        }
        String dealId = segments[1];
        TradingDeal tradingDeal = tradingService.findById(dealId);
        String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        String username = extractUsernameFromToken(token);

        if (!tradingDeal.getCreator().getUsername().equals(username)) {
            return forbidden();
        }
        User user = userService.findByUsername(username);
        user.unlockCard(tradingDeal.getCardToTrade());
        userService.update(user);
        tradingService.delete(dealId);
        return noContent();
    }
}
