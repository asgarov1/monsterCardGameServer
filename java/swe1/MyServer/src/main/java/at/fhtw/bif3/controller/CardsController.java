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

public class CardsController implements Controller {

    public static final String CARDS_ENDPOINT = "/cards";
    private final UserService userService = new UserService();

    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        }
        return notFound();
    }

    private HttpResponse handlePost(Request request) {
        if (request.getUrl().getPath().equals(CARDS_ENDPOINT)) {
            return handleCardsPost(request);
        }
        return notFound();
    }

    private HttpResponse handleCardsPost(Request request) {
        HttpResponse httpResponse = new HttpResponse();
        String token = StringUtil.extractToken(request.getHeaders().get(AUTHORIZATION.name()));
        if (SessionContext.tokenNotPresent(token)) {
            httpResponse.setStatusCode(HttpStatus.FORBIDDEN.getCode());
        } else {
            httpResponse.setStatusCode(HttpStatus.OK.getCode());
            User user = userService.findByUsername(extractUsernameFromToken(token));
            httpResponse.setContent(new Gson().toJson(user.getCards()));
        }
        return httpResponse;
    }

//    TODO question: what is supposed to be different here? regarding the response
//        should I not return anything unless "Content-Type" is set?  - ANSWER: no difference, typo
//      echo 8) show all acquired cards kienboec
//      curl -X GET http://localhost:10001/cards --header "Content-Type: application/json" "Authorization: Basic kienboec-mtcgToken"
//      echo should fail (no token)
//      curl -X GET http://localhost:10001/cards
//      echo.
//      echo.
//
//      echo 9) show all acquired cards altenhof
//      curl -X GET http://localhost:10001/cards --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken"
//      echo.
//      echo.
}
