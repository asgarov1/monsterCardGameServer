package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;
import at.fhtw.bif3.util.StringUtil;
import com.google.gson.Gson;

import static at.fhtw.bif3.http.Header.AUTHORIZATION;
import static at.fhtw.bif3.util.StringUtil.extractUsernameFromToken;

public class CardController implements Controller {

    public static final String CARDS_ENDPOINT = "/cards";
    private final UserService userService = new UserService();

    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.GET.name())) {
            return handleGet(request);
        }
        return notFound();
    }

    private HttpResponse handleGet(Request request) {
        if (request.getUrl().getPath().equals(CARDS_ENDPOINT)) {
            return handleCardsPost(request);
        }
        return notFound();
    }

    private HttpResponse handleCardsPost(Request request) {
        HttpResponse httpResponse = new HttpResponse();
        String authorizationHeaderValue = request.getHeaders().get(AUTHORIZATION.getName());
        if(authorizationHeaderValue == null){
            return badRequest();
        }

        String token = StringUtil.extractToken(authorizationHeaderValue);
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }

        httpResponse.setStatusCode(HttpStatus.OK.getCode());
        User user = userService.findByUsername(extractUsernameFromToken(token));
        httpResponse.setContent(new Gson().toJson(user.getCards()));
        return httpResponse;
    }
}
