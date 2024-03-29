package at.fhtw.bif3.controller.dispatcher;

import at.fhtw.bif3.controller.*;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.Socket;

import static at.fhtw.bif3.http.response.HttpStatus.*;

@Slf4j
public class FrontDispatcher implements Runnable {

    private final Socket connectionSocket;

    public FrontDispatcher(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    @SneakyThrows
    public void run() {
        var request = HttpRequest.valueOf(connectionSocket.getInputStream());
        log.info("\nRECEIVED REQUEST: " + request.getReceivedRequest() + "\n");
        HttpResponse response = getResponse(request);
        log.info("\nRESPONSE: " + response);
        response.send(connectionSocket.getOutputStream());
        connectionSocket.close();
    }

    private HttpResponse getResponse(Request request) {
        var response = new HttpResponse();
        try {
            response = handle(request);
        } catch (IllegalArgumentException e) {
            response.setStatusCode(NOT_FOUND.getCode());
        }
        return response;
    }

    private HttpResponse handle(Request request) {
        String path;
        try {
            path = request.getUrl().getSegments()[0];
        } catch (NullPointerException e){
            //Postman's first NPE creating request handler
            return new HttpResponse();
        }

        Controller controller = switch (path) {
            case "users" -> new UsersController();
            case "sessions" -> new SessionsController();
            case "packages" -> new BundleController();
            case "transactions" -> new TransactionController();
            case "cards" -> new CardController();
            case "deck" -> new DeckController();
            case "stats" -> new StatsController();
            case "score" -> new ScoreController();
            case "battles" -> new BattlesController();
            case "tradings" -> new TradingsController();
            default -> throw new IllegalArgumentException("Unexpected url path: " + path);
        };
        try {
            return controller.handleRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setStatusCode(INTERNAL_SERVER_ERROR.getCode());
            return httpResponse;
        }
    }
}