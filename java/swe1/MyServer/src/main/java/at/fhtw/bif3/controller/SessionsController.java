package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;

import static at.fhtw.bif3.util.StringUtil.extractPassword;
import static at.fhtw.bif3.util.StringUtil.extractUsername;

public class SessionsController implements Controller {

    public static final String SESSIONS_ENDPOINT = "/sessions";

    private final UserService userService = new UserService();

    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        }
        return notFound();
    }

    private HttpResponse handlePost(Request request) {
        if (request.getUrl().getPath().equals(SESSIONS_ENDPOINT)) {
            return handleSessionsPost(request);
        }
        return notFound();
    }

    private HttpResponse handleSessionsPost(Request request) {
        var username = extractUsername(request.getContentString());

        if (SessionContext.isUserLoggedIn(username)) {
            return badRequest();
        }

        if (!credentialsAreCorrect(username, extractPassword(request.getContentString()))) {
            return unauthorized();
        }

        SessionContext.loginUser(username);
        return noContent();
    }

    private boolean credentialsAreCorrect(String username, String password) {
        return userService.findByUsername(username).getPassword().equals(password);
    }

}
