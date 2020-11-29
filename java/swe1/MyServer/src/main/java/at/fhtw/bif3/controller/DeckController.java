package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;

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
        }
        return notFound();
    }

    private HttpResponse handleGet(Request request) {
        if (request.getUrl().getPath().equals(DECK_ENDPOINT)) {
            return handleGetDeck(request);
        }
        return notFound();
    }

    private HttpResponse handleGetDeck(Request request) {
        HttpResponse httpResponse = new HttpResponse();
        String token = extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }
        User user = userService.findByUsername(extractUsernameFromToken(token));
        httpResponse.setStatusCode(HttpStatus.OK.getCode());
        httpResponse.setContent(new Gson().toJson(user.getDeck()));
        return httpResponse;
    }
}
