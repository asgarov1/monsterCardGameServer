package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.User;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.service.BattleManager;
import at.fhtw.bif3.service.UserService;
import at.fhtw.bif3.util.StringUtil;
import lombok.SneakyThrows;

import static at.fhtw.bif3.http.Header.AUTHORIZATION;
import static at.fhtw.bif3.util.StringUtil.extractUsernameFromToken;

public class BattlesController implements Controller {

    public static final String BATTLES_ENDPOINT = "/battles";
    private final UserService userService = new UserService();

    @SneakyThrows
    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        }
        return notFound();
    }

    private HttpResponse handlePost(Request request) {
        if (!request.getUrl().getPath().equals(BATTLES_ENDPOINT)) {
            return notFound();
        }

        String token = StringUtil.extractToken(request.getHeaders().get(AUTHORIZATION.getName()));
        if (SessionContext.tokenNotPresent(token)) {
            return forbidden();
        }

        User user = userService.findByUsername(extractUsernameFromToken(token));
        BattleManager.getInstance().putUserToBattle(user);
        return noContent();
    }
}
