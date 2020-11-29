package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.controller.dto.ScoreDTO;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.ScoreboardService;
import at.fhtw.bif3.util.StringUtil;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static at.fhtw.bif3.http.Header.AUTHORIZATION;
import static at.fhtw.bif3.http.response.ContentType.APPLICATION_JSON;

public class ScoreController implements Controller {
    public static final String SCORE_ENDPOINT = "/score";

    private final ScoreboardService scoreboardService = new ScoreboardService();

    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.GET.name()) && request.getUrl().getPath().equals(SCORE_ENDPOINT)) {
            return handleGet(request);
        }
        return notFound();
    }

    private HttpResponse handleGet(Request request) {
        String token = StringUtil.extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }

        AtomicInteger index = new AtomicInteger(1);
        List<ScoreDTO> scores = scoreboardService.getUsersSortedByElo()
                .stream()
                .map(user -> new ScoreDTO(index.getAndIncrement(), user.getUsername(), user.getElo()))
                .collect(Collectors.toList());

        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .content(new Gson().toJson(scores)).build();
    }
}
