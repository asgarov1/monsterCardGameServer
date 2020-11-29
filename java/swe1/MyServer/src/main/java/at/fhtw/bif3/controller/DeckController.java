package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.TradingDeal;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import static at.fhtw.bif3.domain.User.DECK_SIZE;
import static at.fhtw.bif3.http.Header.AUTHORIZATION;
import static at.fhtw.bif3.util.StringUtil.extractToken;
import static at.fhtw.bif3.util.StringUtil.extractUsernameFromToken;

public class DeckController implements Controller {

    public static final String DECK_ENDPOINT = "/deck";
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
        List<String> cardIds = new Gson().fromJson(request.getContentString(), new TypeToken<List<String>>() {}.getType());
        user.configureDeck(cardIds);
        userService.update(user);
        return noContent();
    }

    private HttpResponse handleGet(Request request) {
        if (!request.getUrl().getPath().equals(DECK_ENDPOINT)) {
            return notFound();
        }

        HttpResponse httpResponse = new HttpResponse();
        String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }
        User user = userService.findByUsername(extractUsernameFromToken(token));
        if(user.getDeck().isEmpty() && user.getCards().size() >= DECK_SIZE){
            user.generateBattleDeck();
        }
        httpResponse.setStatusCode(HttpStatus.OK.getCode());
        httpResponse.setContent(new Gson().toJson(user.getDeck()));
        return httpResponse;
    }
}
