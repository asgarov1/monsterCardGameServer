package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.dao.exception.EntityNotFoundException;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;
import at.fhtw.bif3.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static at.fhtw.bif3.http.Header.AUTHORIZATION;
import static at.fhtw.bif3.http.response.ContentType.APPLICATION_JSON;
import static at.fhtw.bif3.util.StringUtil.extractPassword;
import static at.fhtw.bif3.util.StringUtil.extractUsername;

public class UsersController implements Controller {
    public static final String USERS_ENDPOINT = "/users";
    private final UserService userService = new UserService();

    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        } else if (request.getMethod().equals(HttpMethod.PUT.name())) {
            return handlePut(request);
        } else if (request.getMethod().equals(HttpMethod.GET.name())) {
            return handleGet(request);
        }

        return notFound();
    }

    private HttpResponse handleGet(Request request) {
        String token = StringUtil.extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }

        String[] segments = request.getUrl().getSegments();
        if (segments.length != 2) {
            return notFound();
        }

        try {
            return HttpResponse.builder()
                    .status(HttpStatus.OK)
                    .contentType(APPLICATION_JSON)
                    .content(new Gson().toJson(userService.findByUsername(segments[1]))).build();
        } catch (EntityNotFoundException e){
            return badRequest("No user with username " + segments[1] + " found!");
        }
    }

    private HttpResponse handlePost(Request request) {
        if (request.getUrl().getPath().equals(USERS_ENDPOINT)) {
            return handleUsersPost(request);
        }
        return notFound();
    }

    private HttpResponse handlePut(Request request) {
        String token = StringUtil.extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }

        String[] segments = request.getUrl().getSegments();
        var username = SessionContext.getUsernameForToken(token);
        if (segments.length != 2){
            return notFound();

        }
        if (!segments[1].equals(username)) {
            return forbidden();
        }
        User user = new GsonBuilder().create().fromJson(request.getContentString(), User.class);
        applyUpdatedAttributes(user, username);
        return noContent();
    }

    private void applyUpdatedAttributes(User user, String username) {
        var existingUser = userService.findByUsername(username);

        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getBio() != null) {
            existingUser.setBio(user.getBio());
        }
        if (user.getImage() != null) {
            existingUser.setImage(user.getImage());
        }

        userService.update(existingUser);
    }

    private HttpResponse handleUsersPost(Request request) {
        var username = extractUsername(request.getContentString());
        var password = extractPassword(request.getContentString());
        try {
            userService.create(new User(username, username, password));
        } catch (DAOException e) {
            return badRequest();
        }
        return created();
    }

}