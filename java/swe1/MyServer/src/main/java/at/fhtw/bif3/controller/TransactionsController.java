package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.UserService;
import at.fhtw.bif3.service.exception.TooPoorException;
import at.fhtw.bif3.service.exception.TransactionException;
import at.fhtw.bif3.util.StringUtil;

import static at.fhtw.bif3.util.StringUtil.extractUsername;

public class TransactionsController implements Controller {

    private final UserService userService = new UserService();

    public static final String TRANSACTIONS_PACKAGES = "/transactions/packages";

    @Override
    public HttpResponse handleRequest(Request request) {
        if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        }
        return notFound();
    }

    private HttpResponse handlePost(Request request) {
        if (request.getUrl().getPath().equals(TRANSACTIONS_PACKAGES)) {
            return handleTransactionsPackagesPost(request);
        }
        return notFound();
    }

    private HttpResponse handleTransactionsPackagesPost(Request request) {
        String token = StringUtil.extractToken(request.getHeaders().get("Authorization"));
        if (!SessionContext.isTokenPresent(token)) {
            return forbidden();
        }

        try {
            userService.processPackagePurchaseFor(extractUsername(request.getContentString()));
            return noContent();
        } catch (TooPoorException | TransactionException e) {
            return internalServerError();
        }
    }
}
