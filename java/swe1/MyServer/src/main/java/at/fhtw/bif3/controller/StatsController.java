package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.controller.dto.StatsDTO;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;
import at.fhtw.bif3.util.StringUtil;
import com.google.gson.Gson;

import static at.fhtw.bif3.http.request.HttpMethod.GET;
import static at.fhtw.bif3.http.response.ContentType.APPLICATION_JSON;
import static at.fhtw.bif3.util.StringUtil.extractUsernameFromToken;

public class StatsController implements Controller {

    public static final String STATS_ENDPOINT = "/stats";

    private final UserService userService = new UserService();

    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(GET.name()) && request.getUrl().getPath().equals(STATS_ENDPOINT)) {
            return handleGet(request);
        }
        return notFound();
    }

    private HttpResponse handleGet(Request request) {
        String token = StringUtil.extractToken(request.getHeaders().get("Authorization"));
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }

        String username = extractUsernameFromToken(token);
        User user = userService.findByUsername(username);
        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .content(new Gson().toJson(new StatsDTO(user.getGamesPlayed(), user.getElo())))
                .build();
    }
}
