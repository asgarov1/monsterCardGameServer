package at.fhtw.bif3.controller;

import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;

import static at.fhtw.bif3.util.StringUtil.extractPassword;
import static at.fhtw.bif3.util.StringUtil.extractUsername;

public class UserController implements Controller {
    public static final String USERS_ENDPOINT = "users";
    private final UserService playerService = new UserService();

    @Override
    public HttpStatus handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        }

        return HttpStatus.NOT_FOUND;
    }

    private HttpStatus handlePost(Request request) {
        if (request.getUrl().getPath().equals(USERS_ENDPOINT)) {
            return handleUsersPost(request);
        }
        return HttpStatus.NOT_FOUND;
    }

    private HttpStatus handleUsersPost(Request request) {
        var username = extractUsername(request.getContentString());
        var password = extractPassword(request.getContentString());
        try {
            playerService.create(new User(username, username, password));
        } catch (DAOException e) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.CREATED;
    }
}
