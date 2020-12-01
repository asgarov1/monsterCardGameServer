package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.dao.UserCardDAO;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.domain.card.Card;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.stream.Collectors;

import static at.fhtw.bif3.http.Header.AUTHORIZATION;
import static at.fhtw.bif3.http.response.ContentType.APPLICATION_JSON;
import static at.fhtw.bif3.http.response.HttpStatus.OK;
import static at.fhtw.bif3.service.UserService.PLAYER_DECK_CARD_TABLE;
import static at.fhtw.bif3.util.StringUtil.extractToken;
import static at.fhtw.bif3.util.StringUtil.extractUsernameFromToken;
import static java.util.stream.Collectors.toList;

public class DeckController implements Controller {

    public static final String DECK_ENDPOINT = "/deck";
    public static final String FORMAT_PLAIN = "plain";
    private final UserService userService = new UserService();

    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.GET.name())) {
            return handleGet(request);
        } else if (request.getMethod().equals(HttpMethod.PUT.name())) {
            return handlePut(request);
        }
        return notFound();
    }

    private HttpResponse handlePut(Request request) {
        if (!request.getUrl().getPath().equals(DECK_ENDPOINT)) {
            return notFound();
        }

        String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }

        User user = userService.findByUsername(extractUsernameFromToken(token));
        List<String> cardIds = new Gson().fromJson(request.getContentString(), new TypeToken<List<String>>() {
        }.getType());

        List<String> userCardIds = user.getCards().stream().map(Card::getId).collect(toList());
        if(!userCardIds.containsAll(cardIds)) {
            return badRequest("User doesn't have these card ids");
        }

        user.configureDeck(cardIds);
        userService.update(user);
        return noContent();
    }

    private HttpResponse handleGet(Request request) {
        if (!request.getUrl().getPath().equals(DECK_ENDPOINT)) {
            return notFound();
        }

        String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }

        String format = request.getUrl().getParameter().get("format");
        if(FORMAT_PLAIN.equals(format)) {
            return handleGetPlain(request);
        } else {
            return handleGetJson(request);
        }
    }

    private HttpResponse handleGetPlain(Request request) {
        String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        HttpResponse httpResponse = new HttpResponse();
        String username = SessionContext.getUsernameForToken(token);
        User user = userService.findByUsername(username);
        httpResponse.setStatusCode(OK.getCode());
        httpResponse.setContent(user.getDeck().toString());
        return httpResponse;
    }

    private HttpResponse handleGetJson(Request request) {
        String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        HttpResponse httpResponse = new HttpResponse();
        String username = SessionContext.getUsernameForToken(token);
        User user = userService.findByUsername(username);
        httpResponse.setStatusCode(OK.getCode());
        httpResponse.setContentType(APPLICATION_JSON.getName());
        httpResponse.setContent(new Gson().toJson(user.getDeck()));
        return httpResponse;
    }

}
