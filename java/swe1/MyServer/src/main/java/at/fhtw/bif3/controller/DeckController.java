package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;
import com.google.gson.Gson;

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
        String token = extractToken(request.getHeaders().get("Authorization"));
        if (SessionContext.tokenNotPresent(token)) {
            httpResponse.setStatusCode(HttpStatus.FORBIDDEN.getCode());
        } else {
            User user = userService.findByUsername(extractUsernameFromToken(token));
            httpResponse.setStatusCode(HttpStatus.OK.getCode());
            httpResponse.setContent(new Gson().toJson(user.getDeck()));
        }
        return httpResponse;
    }

    //TODO question: what does show exactly mean? return JSON?
    //echo 10) show unconfigured deck
    //curl -X GET http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken"
    //echo.


//    TODO question: difference bettween 'deck', 'deck?format=plain', and 'configured deck with winning attributes'? ALWAYS JSON
//    echo 12) show configured deck
//curl -X GET http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken"
//echo.
//curl -X GET http://localhost:10001/deck --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken"
//echo.
//echo.
//
//echo 13) show configured deck different representation
//echo kienboec
//curl -X GET http://localhost:10001/deck?format=plain --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken"
//echo.
//echo.
//echo altenhof
//curl -X GET http://localhost:10001/deck?format=plain --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken"
//echo.
//echo.
//
//echo 14) show configured deck with winning attributes
//echo kienboec
//curl -X GET http://localhost:10001/deck?format=plain --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken"
//echo.
//echo.
//echo altenhof
//curl -X GET http://localhost:10001/deck?format=plain --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken"
//echo.
//echo.
}
