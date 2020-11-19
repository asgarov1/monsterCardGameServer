package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.dao.exception.DAOException;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.ContentType;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;
import at.fhtw.bif3.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static at.fhtw.bif3.http.response.ContentType.APPLICATION_JSON;
import static at.fhtw.bif3.util.StringUtil.*;

public class UserController implements Controller {
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
        String token = StringUtil.extractToken(request.getHeaders().get("Authorization"));
        if (!SessionContext.isTokenPresent(token)) {
            return forbidden();
        }

        String username = extractUsernameFromToken(token);
        String[] segments = request.getUrl().getSegments();
        if (segments.length > 2 && segments[1].equals(username)) {
            return HttpResponse.builder()
                    .status(HttpStatus.OK)
                    .contentType(APPLICATION_JSON)
                    .content(new Gson().toJson(userService.findByUsername(username))).build();
        }
        return badRequest();
    }

    private HttpResponse handlePost(Request request) {
        if (request.getUrl().getPath().equals(USERS_ENDPOINT)) {
            return handleUsersPost(request);
        }
        return notFound();
    }

    private HttpResponse handlePut(Request request) {
        String token = StringUtil.extractToken(request.getHeaders().get("Authorization"));
        if (!SessionContext.isTokenPresent(token)) {
            return forbidden();
        }

        String[] segments = request.getUrl().getSegments();
        if (segments.length > 2 && segments[1].equals(extractUsernameFromToken(token))) {
            User user = new GsonBuilder().create().fromJson(request.getContentString(), User.class);
            userService.update(user);
            return noContent();
        }
        return notFound();
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

//TODO question: what do GETs do? just return user data in Json

//echo 15) edit user data
//curl -X GET http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken"
//echo.
//curl -X GET http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: Basic altenhof-mtcgToken"
//echo.
//
//curl -X PUT http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: Basic kienboec-mtcgToken" -d "{\"Name\": \"Kienboeck\", \"Bio\": \"me playin...\", \"Image\": \":-)\"}"
