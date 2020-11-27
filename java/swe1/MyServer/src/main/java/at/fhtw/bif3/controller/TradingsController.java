package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.context.SessionContext;
import at.fhtw.bif3.domain.TradingDeal;
import at.fhtw.bif3.http.request.HttpMethod;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.service.TradingDealService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static at.fhtw.bif3.http.response.ContentType.APPLICATION_JSON;
import static at.fhtw.bif3.util.StringUtil.extractToken;
import static at.fhtw.bif3.util.StringUtil.extractUsernameFromToken;

public class TradingsController implements Controller {

    private final TradingDealService tradingService = new TradingDealService();

    @Override
    public HttpResponse handleRequest(Request request) {
        String token = extractToken(request.getHeaders().get("Authorization"));
        if (!SessionContext.isTokenPresent(token)) {
            return forbidden();
        }

        if (request.getMethod().equals(HttpMethod.GET.name())) {
            return handleGet(request);
        } else if (request.getMethod().equals(HttpMethod.POST.name())) {
            return handlePost(request);
        } else if (request.getMethod().equals(HttpMethod.DELETE.name())) {
            return handleDelete(request);
        }
        return notFound();
    }

    private HttpResponse handleGet(Request request) {
        return HttpResponse.builder()
                .status(HttpStatus.OK)
                .contentType(APPLICATION_JSON)
                .content(new Gson().toJson(tradingService.findAll())).build();
    }

    private HttpResponse handlePost(Request request) {
        String[] segments = request.getUrl().getSegments();
        if (segments.length == 1) {
            return handlePostCreate(request);
        } else if (segments.length == 2) {
            return handlePostTrade(request);
        } else {
            return notFound();
        }
    }

    private HttpResponse handlePostCreate(Request request) {
        TradingDeal tradingDeal = new GsonBuilder().create().fromJson(request.getContentString(), TradingDeal.class);
        tradingService.create(tradingDeal);
        return noContent();
    }

    private HttpResponse handlePostTrade(Request request) {
        String token = extractToken(request.getHeaders().get("Authorization"));
        String username = extractUsernameFromToken(token);
        TradingDeal deal = tradingService.findById(new Gson().fromJson(request.getContentString(), String.class));
        if (deal.getCreator().getUsername().equals(username)) {
            return badRequest();
        } else {
            return processTrade() ? noContent() : badRequest();
        }
    }

    private boolean processTrade() {
        return true;
//        TODO: implement
    }

    private HttpResponse handleDelete(Request request) {
        String[] segments = request.getUrl().getSegments();
        if (segments.length > 2) {
            String dealId = segments[1];
            tradingService.delete(dealId);
            return noContent();
        }
        return notFound();
    }
}
